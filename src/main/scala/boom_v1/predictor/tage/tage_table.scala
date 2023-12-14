package boom_v1.predictor.tage
import boom_v1.Parameters
import boom_v1.predictor.boom.{BpdUpdate, GHistUpdate}
import boom_v1.predictor.gshare.TwobcCounterTable
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// TAGE Table (used by the TAGE branch predictor)
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2016 Feb 26


class TageTableIo(
                   fetch_width: Int,
                   num_entries: Int,
                   history_length: Int,
                   tag_sz: Int,
                   counter_sz: Int,
                   this_index_sz: Int
                 )(implicit p: Parameters) extends Bundle
{
  private val index_sz = log2Up(num_entries)

  // instruction fetch - request prediction
  val if_req_pc = in UInt(width = p.xLen bits)

  // bp2 - send prediction to bpd pipeline
  val bp2_resp = master Stream(new TageTableResp(fetch_width, history_length, log2Up(num_entries), tag_sz))

  // bp2 - update histories speculatively
  val bp2_update_history = slave(Flow(new GHistUpdate))
  // TODO: this is painfully special-cased -- move this into an update_csr bundle?
  val bp2_update_csr_evict_bit = in Bool()

  // commit - update predictor tables (allocate entry)
  val allocate = slave(Flow(new TageAllocateEntryInfo(fetch_width, index_sz, tag_sz, history_length)))
  def AllocateNewEntry(idx: UInt, tag: UInt, executed: UInt, taken: UInt, debug_pc: UInt, debug_hist_ptr: UInt) =
  {
    this.allocate.valid := Bool(true)
    this.allocate.payload.index := idx
    this.allocate.payload.tag :=tag
    this.allocate.payload.executed :=executed
    this.allocate.payload.taken :=taken
    this.allocate.payload.debug_pc := debug_pc
    this.allocate.payload.debug_hist_ptr :=debug_hist_ptr
  }

  // commit - update predictor tables (update counters)
  val update_counters = slave(Flow(new TageUpdateCountersInfo(fetch_width, index_sz)))
  def UpdateCounters(idx: UInt, executed: UInt, taken: UInt, mispredicted: Bool) =
  {
    this.update_counters.valid := Bool(true)
    this.update_counters.payload.index := idx
    this.update_counters.payload.executed := executed
    this.update_counters.payload.taken := taken
    this.update_counters.payload.mispredicted := mispredicted
  }

  // commit - update predictor tables (update u-bits)
  val update_usefulness = slave(Flow(new TageUpdateUsefulInfo(index_sz)))
  def UpdateUsefulness(idx: UInt, inc: Bool) =
  {
    this.update_usefulness.valid := Bool(true)
    this.update_usefulness.payload.index := idx
    this.update_usefulness.payload.inc := inc
  }

  val usefulness_req_idx = in UInt(index_sz bits)
  val usefulness_resp = out UInt(2 bits) // TODO u-bit_sz
  def GetUsefulness(idx: UInt, idx_sz: Int) =
  {
    //      this.usefulness_req_idx := idx(this_index_sz-1,0) // TODO CODEREVIEW
    this.usefulness_req_idx := idx(idx_sz-1 downto 0) // TODO CODEREVIEW
    this.usefulness_resp
  }

  val degrade_usefulness_valid = in Bool()
  def DegradeUsefulness(dummy: Int=0) =
  {
    this.degrade_usefulness_valid := Bool(true)
  }

  // BP2 - speculatively update the spec copy of the CSRs (branch history registers)
  //   val spec_csr_update = Valid(new CircularShiftRegisterUpdate).flip
  // Commit - update the commit copy of the CSRs (branch history registers)
  val commit_csr_update = slave Flow(new CircularShiftRegisterUpdate)
  val debug_ghistory_commit_copy= in UInt(history_length bits) // TODO REMOVE for debug

  // branch resolution comes from the branch-unit, during the Execute stage.
  val br_resolution = slave Flow(new BpdUpdate)
  // reset CSRs to commit copies during pipeline flush
  val flush = in Bool()

  def InitializeIo(dummy: Int=0) =
  {
    this.allocate.valid := Bool(false)
    this.update_counters.valid := Bool(false)
    this.update_usefulness.valid := Bool(false)
    this.allocate.payload.index := U(0)
    this.allocate.payload.tag := U(0)
    this.allocate.payload.executed := U(0)
    this.allocate.payload.taken := U(0)
    this.allocate.payload.debug_pc := U(0)
    this.allocate.payload.debug_hist_ptr := U(0)
    this.update_counters.payload.index := U(0)
    this.update_counters.payload.executed := U(0)
    this.update_counters.payload.taken := U(0)
    this.update_counters.payload.mispredicted := Bool(false)
    this.update_usefulness.payload.index := U(0)
    this.update_usefulness.payload.inc := Bool(false)
    this.usefulness_req_idx := U(0)
    this.degrade_usefulness_valid := Bool(false)
  }


}

class TageTableResp(fetch_width: Int, history_length: Int, index_length: Int, tag_sz: Int) extends Bundle
{
  val takens  = UInt(width = fetch_width bits)  // the actual prediction
  val index   = UInt(width = index_length bits) // the index of the prediction
  val tag     = UInt(width = tag_sz bits)       // the tag we computed for the prediction

  // Instead of passing huge histories around, just pass around a CSR of the
  // folded history (circular shift register).
  // This are snapshotted and reset on a misprediction.
  // Two CSRs are used for the tags to manage the scenario of repeating history
  // with the frequency equal to the history_length (it would fold down to
  // 0x0).
  val idx_csr  = UInt(width = index_length bits)
  val tag_csr1 = UInt(width = tag_sz bits)
  val tag_csr2 = UInt(width = tag_sz-1 bits)

}

class TageIndex(index_sz: Int) extends Bundle
{
  val index = UInt(width = index_sz bits)
}

class TageUpdateUsefulInfo(index_sz: Int) extends Bundle
{
  val index = UInt(width = index_sz bits)
  val inc = Bool()
}

class TageAllocateEntryInfo(fetch_width: Int, index_sz: Int, tag_sz: Int, hist_sz: Int) extends Bundle //TageIndex(index_sz)
{
  val index = UInt(width = index_sz bits)
  val tag = UInt(width = tag_sz bits)
  val executed = UInt(width = fetch_width bits)
  val taken = UInt(width = fetch_width bits)
  val debug_pc = UInt(width = 32 bits)
  val debug_hist_ptr = UInt(width = hist_sz bits)
}

class TageUpdateCountersInfo(fetch_width: Int, index_sz: Int) extends Bundle //extends TageIndex(index_sz)
{
  val index = UInt(width = index_sz bits)
  val executed = UInt(width = fetch_width bits)
  val taken = UInt(width = fetch_width bits)
  val mispredicted = Bool()
}

// The CSRs contain the "folded" history. For them to work, we need to pass them
// the latest new bit to add in and the oldest bit to evict out.
class CircularShiftRegisterUpdate extends Bundle
{
  val new_bit = Bool()
  val evict_bit = Bool()
}


// In Chisel3, all Bundle elements in a Vec() must be homogenous (i.e., when
// using a Vec() of TageTableIOs, the sub-fields within the TageTableIOs must
// have the exact same widths (no heterogenous types/widths). Therefore, we must
// track the max_* size of the parameters, and then within the TageTable we must
// mask off extra bits as needed.
class TageTable(
                 fetch_width: Int,
                 num_entries: Int,
                 history_length: Int,
                 tag_sz: Int,
                 max_num_entries: Int,
                 max_history_length: Int,
                 max_tag_sz: Int,
                 counter_sz: Int,
                 ubit_sz: Int,
                 id: Int,
                 num_tables: Int
               )(implicit p: Parameters) extends Module
{
  val index_sz = log2Up(num_entries)

  val io = new TageTableIo(fetch_width, max_num_entries, max_history_length, max_tag_sz, counter_sz, this_index_sz = index_sz)

  private val CNTR_MAX = (1 << counter_sz) - 1
  private val CNTR_WEAK_TAKEN = 1 << (counter_sz-1)
  private val CNTR_WEAK_NOTTAKEN = CNTR_WEAK_TAKEN - 1
  private val UBIT_MAX = (1 << ubit_sz) - 1
  private val UBIT_INIT_VALUE = 1

  println("\t    TageTable - "
    + num_entries + " entries, "
    + history_length + " bits of history, "
    + tag_sz + "-bit tags, "
    + counter_sz + "-bit counters (max value=" + CNTR_MAX + ")")

  assert (counter_sz == 2)

  //------------------------------------------------------------
  // State
  val counter_table = (new TwobcCounterTable(fetch_width, num_entries, dualported=false))
  val tag_table     = (new TageTagMemory(num_entries, memwidth = tag_sz))
  val ubit_table    = if (ubit_sz == 1) (new TageUbitMemoryFlipFlop(num_entries, ubit_sz))
  else              (new TageUbitMemorySeqMem(num_entries, ubit_sz))
  val debug_pc_table= Mem(UInt(width = 32 bits),num_entries)
  val debug_hist_ptr_table=Mem(UInt(width = log2Up(p.VLHR_LENGTH) bits), num_entries)

  //history ghistory
  val idx_csr         = Module(new CircularShiftRegister(index_sz, history_length))
  val tag_csr1        = Module(new CircularShiftRegister(tag_sz  , history_length))
  val tag_csr2        = Module(new CircularShiftRegister(tag_sz-1, history_length))
  val commit_idx_csr  = Module(new CircularShiftRegister(index_sz, history_length))
  val commit_tag_csr1 = Module(new CircularShiftRegister(tag_sz  , history_length))
  val commit_tag_csr2 = Module(new CircularShiftRegister(tag_sz-1, history_length))

  tag_table.io.InitializeIo()
  ubit_table.io.InitializeIo()
  idx_csr.io.InitializeIo()
  tag_csr1.io.InitializeIo()
  tag_csr2.io.InitializeIo()
  commit_idx_csr.io.InitializeIo()
  commit_tag_csr1.io.InitializeIo()
  commit_tag_csr2.io.InitializeIo()


  //------------------------------------------------------------
  // functions

  private def Fold (input: UInt, compressed_length: Int) =
  {
    val clen = compressed_length
    val hlen = history_length
    if (hlen <= clen)
    {
      input
    }
    else
    {
      var res = UInt(0,clen)
      var remaining = input.toUInt
      for (i <- 0 to hlen-1 by clen)
      {
        val len = if (i + clen > hlen ) (hlen - i) else clen
        require(len > 0)
        res = res(clen-1,0) ^ remaining(len-1,0)
        remaining = remaining >> UInt(len)
      }
      res
    }
  }

  private def IdxHash (addr: UInt) =
  {
    val idx =
      ((addr >> UInt(log2Up(fetch_width*coreInstBytes))) ^
        idx_csr.io.next)

    idx(index_sz-1,0)
  }

  private def TagHash (addr: UInt) =
  {
    // the tag is computed by pc[n:0] ^ CSR1[n:0] ^ (CSR2[n-1:0]<<1).
    val tag_hash =
      (addr >> UInt(log2Up(fetch_width*coreInstBytes))) ^
        tag_csr1.io.next ^
        (tag_csr2.io.next << UInt(1))
    tag_hash(tag_sz-1,0)
  }

  //------------------------------------------------------------
  // Get Prediction

  val stall = !io.bp2_resp.ready

  val p_idx       = IdxHash(io.if_req_pc)
  val p_tag       = TagHash(io.if_req_pc)

  counter_table.io.s0_r_idx := p_idx
  tag_table.io.s0_r_idx := p_idx
  counter_table.io.stall := stall
  tag_table.io.stall := stall

  val s2_tag      = tag_table.io.s2_r_out
  val bp2_tag_hit = s2_tag === RegEnable(RegEnable(p_tag, !stall), !stall)

  io.bp2_resp.valid       := bp2_tag_hit
  io.bp2_resp.bits.takens := counter_table.io.s2_r_out
  io.bp2_resp.bits.index  := RegEnable(RegEnable(p_idx, !stall), !stall)(index_sz-1,0)
  io.bp2_resp.bits.tag    := RegEnable(RegEnable(p_tag, !stall), !stall)(tag_sz-1,0)

  io.bp2_resp.bits.idx_csr  := idx_csr.io.value
  io.bp2_resp.bits.tag_csr1 := tag_csr1.io.value
  io.bp2_resp.bits.tag_csr2 := tag_csr2.io.value

  //------------------------------------------------------------
  // Update (Branch Resolution)

  // only update history (CSRs)

  when (io.flush)
  {
    idx_csr.io.rollback (commit_idx_csr.io.value , and_shift=Bool(false))
    tag_csr1.io.rollback(commit_tag_csr1.io.value, and_shift=Bool(false))
    tag_csr2.io.rollback(commit_tag_csr2.io.value, and_shift=Bool(false))
  }
    .elsewhen (io.br_resolution.valid && io.br_resolution.bits.mispredict)
    {
      val resp_info = new TageResp(
        fetch_width = fetch_width,
        num_tables = num_tables,
        max_history_length = max_history_length,
        max_index_sz = log2Up(max_num_entries),
        max_tag_sz = max_tag_sz).fromBits(
        io.br_resolution.bits.info)

      val new_bit = io.br_resolution.bits.taken
      val evict_bit = resp_info.evict_bits(id)

      idx_csr.io.rollback (resp_info.idx_csr (id), and_shift=Bool(true), new_bit, evict_bit)
      tag_csr1.io.rollback(resp_info.tag_csr1(id), and_shift=Bool(true), new_bit, evict_bit)
      tag_csr2.io.rollback(resp_info.tag_csr2(id), and_shift=Bool(true), new_bit, evict_bit)
    }
    .elsewhen (io.bp2_update_history.valid)
    {
      val bp2_taken = io.bp2_update_history.bits.taken
      val bp2_evict = io.bp2_update_csr_evict_bit
      idx_csr.io.shift (bp2_taken, bp2_evict)
      tag_csr1.io.shift(bp2_taken, bp2_evict)
      tag_csr2.io.shift(bp2_taken, bp2_evict)
    }

  //------------------------------------------------------------
  // Update Commit-CSRs (Commit)

  val debug_folded_com_hist = Fold(io.debug_ghistory_commit_copy(history_length-1,0), index_sz)
  when (io.commit_csr_update.valid)
  {
    val com_taken = io.commit_csr_update.bits.new_bit
    val com_evict = io.commit_csr_update.bits.evict_bit
    commit_idx_csr.io.shift (com_taken, com_evict)
    commit_tag_csr1.io.shift(com_taken, com_evict)
    commit_tag_csr2.io.shift(com_taken, com_evict)
  }

  assert (commit_idx_csr.io.value === debug_folded_com_hist, "[TageTable] idx_csr not matching Fold() value.")


  //------------------------------------------------------------
  // Update (Commit)


  assert(!(io.allocate.valid && io.update_counters.valid),
    "[tage-table] trying to allocate and update the counters simultaneously.")

  when (io.allocate.valid)
  {
    val a_idx = io.allocate.bits.index(index_sz-1,0)

    ubit_table.io.allocate(a_idx)
    tag_table.io.write(a_idx, io.allocate.bits.tag(tag_sz-1,0))

    counter_table.io.update.valid                 := Bool(true)
    counter_table.io.update.bits.index            := a_idx
    counter_table.io.update.bits.executed         := io.allocate.bits.executed.toBools
    counter_table.io.update.bits.was_mispredicted := Bool(true)
    counter_table.io.update.bits.takens           := io.allocate.bits.taken.toBools
    counter_table.io.update.bits.do_initialize    := Bool(true)

    debug_pc_table(a_idx) := io.allocate.bits.debug_pc
    debug_hist_ptr_table(a_idx) := io.allocate.bits.debug_hist_ptr(history_length-1,0)

    assert (a_idx < UInt(num_entries), "[TageTable] out of bounds index on allocation")
  }
    .elsewhen (io.update_counters.valid)
    {
      counter_table.io.update.valid                 := Bool(true)
      counter_table.io.update.bits.index            := io.update_counters.bits.index
      counter_table.io.update.bits.executed         := io.update_counters.bits.executed.toBools
      counter_table.io.update.bits.was_mispredicted := io.update_counters.bits.mispredicted
      counter_table.io.update.bits.takens           := io.update_counters.bits.taken.toBools
      counter_table.io.update.bits.do_initialize    := Bool(false)
    }

  when (io.update_usefulness.valid)
  {
    ubit_table.io.update(io.update_usefulness.bits.index(index_sz-1,0), io.update_usefulness.bits.inc)
  }

  val ub_read_idx = io.usefulness_req_idx(index_sz-1,0)
  ubit_table.io.s0_read_idx := ub_read_idx
  io.usefulness_resp := ubit_table.io.s2_is_useful

  when (io.degrade_usefulness_valid)
  {
    ubit_table.io.degrade()
  }
}

