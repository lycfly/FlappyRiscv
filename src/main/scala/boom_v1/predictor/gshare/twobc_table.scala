package boom_v1.predictor.gshare

import boom_v1.Utils.SeqMem1rwTransformable
import spinal.core._
import spinal.lib._

class UpdateEntry(fetch_width: Int, index_sz: Int) extends Bundle
{
  val index            = UInt(width = index_sz bits)
  val executed         = Vec(Bool(), fetch_width)
  val takens           = Vec(Bool(), fetch_width)
  // Was there a misprediction? If yes, we need to read the h-tables.
  val was_mispredicted = Bool()
  // Are we initializing this entry? If yes, we need to write directly to both P and H-tables.
  // If takens(i), then we initialize entry to Weak-Taken. Otherwise, Weak-NotTaken.
  val do_initialize    = Bool()
}


class BrTableUpdate(fetch_width: Int, index_sz: Int) extends Bundle
{
  val index      = UInt(width = index_sz bits)
  val executed   = UInt(width = fetch_width bits) // which words in the fetch packet does the update correspond to?
  val new_value  = UInt(width = fetch_width bits)

}


// Read p-table every cycle for a prediction.
// Write p-table only if a misprediction occurs.
// The p-table requires 1read/1write port.
abstract class PTable(
                       fetch_width: Int,
                       num_entries: Int
                     ) extends Module
{
  val index_sz = log2Up(num_entries)
  val io = new Bundle
  {
    val s0_r_idx = in UInt(width = index_sz bits)
    val s2_r_out = out UInt(width = fetch_width bits)
    val stall    = in Bool()
    val update   = slave Stream(new BrTableUpdate(fetch_width, index_sz))
  }

  val ridx = UInt()
  val last_idx = RegNext(ridx)
  ridx := Mux(io.stall, last_idx, io.s0_r_idx)
}

// This version uses 1 read and 1 write port.
class PTableDualPorted(
                        fetch_width: Int,
                        num_entries: Int
                      ) extends PTable(fetch_width, num_entries)
{
  val p_table = Mem(Vec(Bool(), fetch_width), num_entries)

  io.update.ready := Bool(true)

  when (io.update.valid)
  {
    val waddr = io.update.payload.index
    val wdata = io.update.payload.new_value.asBools
    val wmask = io.update.payload.executed.asBits
    p_table.write(waddr, wdata, enable = wmask.orR, mask = wmask)
  }

  io.s2_r_out := RegNextWhen(p_table.readSync(this.ridx).asBits.asUInt, !io.stall)
}

// Read p-table every cycle for a prediction.
// Write p-table only if a misprediction occurs.
// The p-table requires 1read/1write port.
// This version banks the table to get by with a single 1rw port.
class PTableBanked(
                    fetch_width: Int,
                    num_entries: Int
                  ) extends PTable(fetch_width, num_entries)
{
  //   val p_table_0 = SeqMem(num_entries/2, Vec(fetch_width, Bool()))
  //   val p_table_1 = SeqMem(num_entries/2, Vec(fetch_width, Bool()))
  val p_table_0 = (new SeqMem1rwTransformable(num_entries/2, fetch_width))
  val p_table_1 = (new SeqMem1rwTransformable(num_entries/2, fetch_width))

  private def getBank (idx: UInt): UInt = idx(0).asUInt
  private def getRowIdx (idx: UInt): UInt = idx >> 1

  val widx = io.update.payload.index
  val rbank = getBank(ridx)
  val wbank = getBank(widx)
  io.update.ready := rbank =/= wbank

  val ren_0   = rbank === U(0)
  val ren_1   = rbank === U(1)
  //   val rout_0  = RegEnable(p_table_0.read(getRowIdx(ridx), ren_0).toBits, !io.stall)
  //   val rout_1  = RegEnable(p_table_1.read(getRowIdx(ridx), ren_1).toBits, !io.stall)
  val wdata   = Vec(io.update.payload.new_value.asBools)
  val wmask = io.update.payload.executed

  //   when (!ren_0 && wbank === UInt(0) && io.update.valid)
  //   {
  //      p_table_0.write(getRowIdx(widx), wdata, wmask)
  //   }
  //   when (!ren_1 && wbank === UInt(1) && io.update.valid)
  //   {
  //      p_table_1.write(getRowIdx(widx), wdata, wmask)
  //   }

  // ** use resizable SeqMems ** //
  p_table_0.io.wen   := !ren_0 && wbank === U(0) && io.update.valid
  p_table_0.io.waddr := getRowIdx(widx)
  p_table_0.io.wmask := wmask.asBits
  p_table_0.io.wdata := wdata.asBits.asUInt
  p_table_1.io.wen   := !ren_1 && wbank === U(1) && io.update.valid
  p_table_1.io.waddr := getRowIdx(widx)
  p_table_1.io.wmask := wmask
  p_table_1.io.wdata := wdata.asBits.asUInt

  p_table_0.io.ren   := ren_0
  p_table_0.io.raddr := getRowIdx(ridx)
  p_table_1.io.ren   := ren_1
  p_table_1.io.raddr := getRowIdx(ridx)
  val rout_0 = RegNextWhen(p_table_0.io.rout, !io.stall)
  val rout_1 = RegNextWhen(p_table_1.io.rout, !io.stall)

  val s2_ren = RegNextWhen(RegNextWhen(ren_0, !io.stall), !io.stall)
  io.s2_r_out := Mux(s2_ren, rout_0, rout_1)
}


// Write h-table for every branch resolution (we can buffer these up).
// Read h-table immediately to update the p-table (only if a mispredict occurred).
class HTable(
              fetch_width: Int,
              num_entries: Int
            ) extends Module
{
  private val index_sz = log2Up(num_entries)
  val io = new Bundle
  {
    // Update the h-table.
    val update   = slave Flow(new UpdateEntry(fetch_width, index_sz))
    // Enqueue an update to the p-table.
    val pwq_enq  = master Stream(new BrTableUpdate(fetch_width, index_sz))
  }

  //   val h_table = SeqMem(num_entries, Vec(fetch_width, Bool()))
  val h_table = (new SeqMem1rwTransformable(num_entries, fetch_width))
  val hwq = (new StreamFifoLowLatency(new UpdateEntry(fetch_width, index_sz), depth = 4))

  val update_stream = io.update.toStream // ported
  update_stream.ready := True  // ported
  hwq.io.push <> update_stream

  val h_ren = io.update.valid && io.update.payload.was_mispredicted && !io.update.payload.do_initialize
  hwq.io.pop.ready := !h_ren
  //when (!h_ren && hwq.io.deq.valid)
  //{
  //   val waddr = hwq.io.deq.bits.index
  //   val wmask = hwq.io.deq.bits.executed
  //   // if initializing, set to weak state.
  //   val wdata = Vec(hwq.io.deq.bits.takens.map(t =>
  //                  Mux(hwq.io.deq.bits.do_initialize, !t, t)))
  //   h_table.write(waddr, wdata, wmask)
  //}
  h_table.io.wen   := !h_ren && hwq.io.pop.valid
  h_table.io.waddr := hwq.io.pop.payload.index
  h_table.io.wmask := hwq.io.pop.payload.executed.asBits
  h_table.io.wdata := Vec(hwq.io.pop.payload.takens.map(t =>
    Mux(hwq.io.pop.payload.do_initialize, !t, t))).asBits.asUInt  // 2b state machine

  val h_raddr = io.update.payload.index
  h_table.io.ren   := h_ren
  h_table.io.raddr := h_raddr
  val h_rout = h_table.io.rout
  //   val h_rout = h_table.read(h_raddr, h_ren).toBits
  io.pwq_enq.valid          := RegNext(h_ren || io.update.payload.do_initialize)
  io.pwq_enq.payload.index     := RegNext(h_raddr)
  io.pwq_enq.payload.executed  := RegNext(io.update.payload.executed.asBits)
  io.pwq_enq.payload.new_value := Mux(RegNext(io.update.payload.do_initialize),
    RegNext(io.update.payload.takens.asBits.asUInt),
    h_rout)
}


class TwobcCounterTable(
                         fetch_width: Int,
                         num_entries: Int,
                         dualported: Boolean = false
                       ) extends Module
{
  private val index_sz = log2Up(num_entries)
  val io = new Bundle
  {
    // send read addr on cycle 0, get data out on cycle 2.
    val s0_r_idx = in UInt(width = index_sz bits)
    val s2_r_out = out UInt(width = fetch_width bits)
    val stall    = in Bool()

    val update   = slave Flow(new UpdateEntry(fetch_width, index_sz))
  }

  println ("\t\tBuilding (" +
    (num_entries * fetch_width * 2/8/1024) + " kB) 2-bit counter table for (" +
    fetch_width + "-wide fetch) and " +
    num_entries + " entries " +
    (if (dualported) "[1read/1write]." else "[1rw]."))

  //------------------------------------------------------------
  // prediction bits
  // hysteresis bits

  val p_table = if (dualported) (new PTableDualPorted(fetch_width, num_entries))
  else            (new PTableBanked(fetch_width, num_entries))
  val h_table = (new HTable(fetch_width, num_entries))


  //------------------------------------------------------------
  // write queue from h-table to p-table.

  val pwq_entries = if (dualported) 2 else 6
  val pwq = (new StreamFifoLowLatency(new BrTableUpdate(fetch_width, index_sz), depth=pwq_entries))

  //------------------------------------------------------------
  // p-table
  // Read table every cycle for a prediction.
  // Write table only if a misprediction occurs.

  p_table.io.s0_r_idx <> io.s0_r_idx
  io.s2_r_out <> p_table.io.s2_r_out
  p_table.io.update <> pwq.io.pop
  p_table.io.stall := io.stall


  //------------------------------------------------------------
  // h-table
  // Write table for every branch resolution (we can buffer these up).
  // Read table immediately to update the p-table (only if a mispredict occurred).

  h_table.io.update <> io.update
  pwq.io.push <> h_table.io.pwq_enq


  //------------------------------------------------------------
}

