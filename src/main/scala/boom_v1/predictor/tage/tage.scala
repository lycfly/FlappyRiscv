package boom_v1.predictor.tage

import boom_v1.Parameters
import boom_v1.Utils.PriorityEncoder
import boom_v1.predictor.boom.BrPredictor
import spinal.core._
import spinal.lib._
import spinal.lib.PriorityMux
case class TageParameters(
                           enabled: Boolean = true,
                           num_tables: Int = 4,
                           table_sizes: Seq[Int] = Seq(4096,4096,2048,2048),
                           history_lengths: Seq[Int] = Seq(5,17,44,130),
                           tag_sizes: Seq[Int] = Seq(10,10,10,12),
                           ubit_sz: Int = 1)


class TageResp(
                fetch_width: Int,
                num_tables: Int,
                max_history_length: Int,
                max_index_sz: Int,
                max_tag_sz: Int)
  extends Bundle
{
  val provider_hit = Bool() // did tage make a prediction?
  val provider_id = UInt(width = 5 bits) // which table is providing the prediction?
  val provider_predicted_takens = UInt(width = fetch_width bits)
  val alt_hit = Bool()  // an alternate table made a prediction too
  val alt_id = UInt(width = 5 bits)  // which table is the alternative?
  val alt_predicted_takens = UInt(width = fetch_width bits)

  val indexes  = Vec(UInt(width = max_index_sz bits), num_tables) // needed to update predictor at Commit
  val tags     = Vec(UInt(width = max_tag_sz bits), num_tables)   // needed to update predictor at Commit
  val evict_bits = Vec(Bool(), num_tables)                   // needed to update predictor on branch misprediction

  val idx_csr  = Vec(UInt(width = max_index_sz bits), num_tables) // needed to perform rollback
  val tag_csr1 = Vec(UInt(width = max_tag_sz bits), num_tables)   // needed to perform rollback
  val tag_csr2 = Vec(UInt(width = max_tag_sz-1 bits), num_tables) // needed to perform rollback


  val debug_history_ptr = UInt(width = max_history_length bits) // stored in snapshots (dealloc after Execute)
  val debug_br_pc = UInt(width=64 bits)

}

// provide information to the BpdResp bundle how many bits a TageResp needs
object TageBrPredictor
{
  def GetRespInfoSize(p: Parameters, fetchWidth: Int): Int =
  {
    val params = p.tage.get
    val dummy = new TageResp(
      fetch_width = fetchWidth,
      num_tables = params.num_tables,
      max_history_length = params.history_lengths.max,
      max_index_sz = log2Up(params.table_sizes.max),
      max_tag_sz = params.tag_sizes.max
    )
    dummy.getBitsWidth
  }
}

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------

class TageBrPredictor(
                       fetch_width: Int,
                       num_tables: Int,
                       table_sizes: Seq[Int],
                       history_lengths: Seq[Int],
                       tag_sizes: Seq[Int],
                       ubit_sz: Int
                     )(implicit p: Parameters)
  extends BrPredictor(
    fetch_width    = fetch_width,
    history_length = history_lengths.max)(p)
{
  val counter_sz = 2
  val size_in_bits = (for (i <- 0 until num_tables) yield
  {
    val entry_sz_in_bits = tag_sizes(i) + ubit_sz + (counter_sz*fetch_width)
    table_sizes(i) * entry_sz_in_bits
  }).reduce(_+_)

  println ("\tBuilding " + (size_in_bits/8/1024.0) + " kB TAGE Predictor ("
    + (size_in_bits/1024) + " Kbits) (max history length: " + history_lengths.max + " bits)")
  require (num_tables == table_sizes.size)
  require (num_tables == history_lengths.size)
  require (num_tables == tag_sizes.size)
  // require (log2Up(num_tables) <= TageResp.provider_id.getWidth()) TODO implement this check
  require (p.coreInstBytes == 4)

  //------------------------------------------------------------
  //------------------------------------------------------------

  private val MAX_TABLE_ID = num_tables-1

  //------------------------------------------------------------
  //------------------------------------------------------------

  def GetProviderTableId(hits:IndexedSeq[Bool]): UInt =
  {
    // return the id of the highest table with a hit
    PriorityMux(hits.reverse, (num_tables-1 to 0 by -1).map(U(_)))
  }

  def GetAlternateTableId(hits:IndexedSeq[Bool]): (Bool, UInt) =
  {
    // return the id of the 2nd highest table with a hit
    // also returns whether a 2nd hit was found (PopCount(hits) > 1)
    val alt_id = UInt(log2Up(num_tables) bits)
    alt_id := 0
    var found_first = Bool(false)
    var found_second = Bool(false)
    for (i <- num_tables-1 to 0 by -1)
    {
      when (found_first && !found_second)
      {
        alt_id := U(i)
      }
      found_second = (hits(i) && found_first) | found_second
      found_first = hits(i) | found_first
    }
    assert ((CountOne(hits.asInstanceOf[Vec[Bool]].asBits) > U(1)) ^ !found_second,
      "[Tage] GetAltId has a disagreement on finding a second hit.")
    (found_second, alt_id)
  }

  //------------------------------------------------------------
  //------------------------------------------------------------

  val stall = !io.resp.ready

  //------------------------------------------------------------
  //------------------------------------------------------------

  val tables = for (i <- 0 until num_tables) yield
  {
    val table = (new TageTable(
      fetch_width        = fetch_width,
      id                 = i,
      num_tables         = num_tables,
      num_entries        = table_sizes(i),
      history_length     = history_lengths(i),
      tag_sz             = tag_sizes(i),
      max_num_entries    = table_sizes.max,
      max_history_length = history_lengths.max,
      max_tag_sz         = tag_sizes.max,
      counter_sz         = counter_sz,
      ubit_sz            = ubit_sz))

    // check that the user ordered his TAGE tables properly
    if (i > 0) require(history_lengths(i) > history_lengths(i-1))

    table
  }

  val tables_io = Vec(tables.map(_.io))

  tables_io.zipWithIndex.foreach{ case (table, i) =>
    table.InitializeIo()

    // Send prediction request. ---
    table.if_req_pc := io.req_pc

    // update CSRs. ---
    table.br_resolution <> io.br_resolution
    table.flush := io.flush

    // Update ghistory speculatively once a prediction is made.
    table.bp2_update_history <> io.hist_update_spec
    table.bp2_update_csr_evict_bit := r_vlh.getSpecBit(history_lengths(i)-1)

    // Update commit copies.
    table.commit_csr_update.valid := commit.valid
    table.commit_csr_update.payload.new_bit := commit.payload.ctrl.taken.reduce(_|_)
    table.commit_csr_update.payload.evict_bit := r_vlh.getCommitBit(history_lengths(i)-1)

    assert(r_ghistory_commit_copy(history_lengths(i)-1) === r_vlh.getCommitBit(history_lengths(i)-1),
      "[tage] commit bits of short and vlh do not match.")

    table.debug_ghistory_commit_copy := r_ghistory_commit_copy
  }


  // get prediction (priority to last table)
  val valids = tables_io.map{ _.bp2_resp.valid }
  val predictions = tables_io.map{ _.bp2_resp.payload }
  tables_io.map{ _.bp2_resp.ready := io.resp.ready }
  val best_prediction_valid = valids.reduce(_|_)
  val best_prediction_bits = PriorityMux(valids.reverse, predictions.reverse)

  val resp_info = (new TageResp(
    fetch_width = fetch_width,
    num_tables = num_tables,
    max_history_length = history_lengths.max,
    max_index_sz = log2Up(table_sizes.max),
    max_tag_sz = tag_sizes.max))

  io.resp.valid       := best_prediction_valid
  io.resp.payload.takens := best_prediction_bits.takens
  resp_info.indexes   := Vec(predictions.map(_.index))
  resp_info.tags      := Vec(predictions.map(_.tag))
  resp_info.idx_csr   := Vec(predictions.map(_.idx_csr))
  resp_info.tag_csr1  := Vec(predictions.map(_.tag_csr1))
  resp_info.tag_csr2  := Vec(predictions.map(_.tag_csr2))
  resp_info.evict_bits:= Vec(tables_io.map(_.bp2_update_csr_evict_bit))

  resp_info.provider_hit := io.resp.valid
  resp_info.provider_id := GetProviderTableId(valids)
  resp_info.provider_predicted_takens := best_prediction_bits.takens

  val (p_alt_hit, p_alt_id) = GetAlternateTableId(valids)
  resp_info.alt_hit := p_alt_hit
  resp_info.alt_id  := p_alt_id
  resp_info.alt_predicted_takens := Vec(predictions.map(_.takens))(p_alt_id)
  resp_info.debug_br_pc := RegNextWhen(RegNextWhen(io.req_pc, !stall), !stall)

  io.resp.payload.info := resp_info.asBits

  require (log2Up(num_tables) <= resp_info.provider_id.getWidth)

  //------------------------------------------------------------
  //------------------------------------------------------------
  // update predictor during commit

  // Commit&Update takes 3 cycles.
  //    - First cycle: begin read of state (u-bits).
  //    - Second cycle: compute (some) updates.
  //    - Second cycle: perform updates.
  // Specifically, the u-bits are "read-do-stuff-write", so spreading
  // across three-cycles is a requirement:
  //    (address setup, read, compute/write).

  //-------------------------------------------------------------
  // Cycle 0 and 1 - read info

  val info = new TageResp(
    fetch_width = fetch_width,
    num_tables = num_tables,
    max_history_length = history_lengths.max,
    max_index_sz = log2Up(table_sizes.max),
    max_tag_sz = tag_sizes.max
  )
  info.assignFromBits(commit.payload.info.info.asBits)

  val executed = commit.payload.ctrl.executed.asBits

  when (commit.valid && commit.payload.ctrl.executed.reduce(_|_))
  {
    assert (info.provider_id < U(num_tables) || !info.provider_hit, "[Tage] provider_id is out-of-bounds.")
  }

  // TODO verify this behavior/logic is correct (re: toBits/Vec conversion)
  val s2_alt_agrees = RegNext(RegNext(
    info.alt_hit && (info.provider_predicted_takens & executed.asUInt) === (info.alt_predicted_takens & executed.asUInt)))

  val s2_ubits_notuseful = Range(0, num_tables).map{ i =>
    (tables_io(i).GetUsefulness(info.indexes(i), log2Up(table_sizes(i))) === 0)
  }

  //-------------------------------------------------------------
  // Track ubit degrade flush timer.

  val degrade_counter = Counter((1<<19), commit.valid && commit.payload.ctrl.executed.reduce(_|_))
  val do_degrade = degrade_counter === U(1<<19)
  when (do_degrade)
  {
    degrade_counter.clear()
    for (i <- 0 until num_tables)
    {
      tables_io(i).DegradeUsefulness()
    }
  }

  //-------------------------------------------------------------
  // Cycle 1 - perform state changes

  val s2_commit      = RegNext(RegNext(commit))
  val s2_info        = RegNext(RegNext(info))
  val s2_provider_id = RegNext(RegNext(info.provider_id))
  val s2_takens      = RegNext(RegNext(commit.payload.ctrl.taken.asBits))
  val s2_correct     = RegNext(RegNext(!commit.payload.ctrl.mispredicted.reduce(_|_)))
  val s2_executed    = RegNext(RegNext(commit.payload.ctrl.executed.asBits))


  // provide some randomization to the allocation process
  val rand = Reg(UInt(2 bits)) init(0)
  rand := rand + U(1)

  val ubit_update_wens = Vec.fill(num_tables) {Bool(false)}
  val ubit_update_incs = Vec.fill(num_tables) {Bool(false)}

  when (s2_commit.valid && s2_commit.payload.ctrl.executed.reduce(_|_))
  {
    // no matter what happens, update table that made a prediction
    when (s2_info.provider_hit)
    {
      tables_io(s2_provider_id).UpdateCounters(s2_info.indexes(s2_provider_id), s2_executed.asUInt, s2_takens.asUInt, !s2_correct)
      when (!s2_alt_agrees)
      {
        ubit_update_wens(s2_provider_id) := Bool(true)
        ubit_update_incs(s2_provider_id) := s2_correct
      }
    }


    when (!s2_correct && (s2_provider_id < U(MAX_TABLE_ID) || !s2_info.provider_hit))
    {
      // try to allocate a new entry

      // if provider not T_max, try to allocate an entry on T_k (i < k < max).
      // - only allocate one entry.
      // - a) if find an entry u_k that == 0, then allocate T_k
      // - b) ELSE decrement u_counters from Tj where (i<j<=max), or just (i<j<max).
      //    b.i) randomize r, where i<=(i+r)<k<=max, to prevent ping-ponging
      //       where new allocations simply over-write once another before the u-bit
      //       can be strengthened.


      val temp = Mux(rand === U(3), U(2),
        Mux(rand === U(2), U(1),
          U(0)))
      val ridx = Mux((Cat(U(0), s2_provider_id).asUInt + temp) >= U(MAX_TABLE_ID),
        U(0),
        temp)


      // find lowest alloc_idx where u_bits === 0
      val can_allocates = Range(0, num_tables).map{ i =>
        s2_ubits_notuseful(i) &&
          ((U(i) > (Cat(U(0), s2_provider_id).asUInt + ridx)) || !s2_info.provider_hit)
      }

      val alloc_id = PriorityEncoder(can_allocates)
      when (can_allocates.reduce(_|_))
      {
        tables_io(alloc_id).AllocateNewEntry(
          s2_info.indexes(alloc_id),
          s2_info.tags(alloc_id),
          s2_executed.asUInt,
          s2_takens.asUInt,
          s2_info.debug_br_pc,
          s2_info.debug_history_ptr)
      }
        .otherwise
        {
          //decrementUBits for tables[provider_id+1: T_max]
          for (i <- 0 until num_tables)
          {
            when ((U(i) > s2_provider_id) || !s2_info.provider_hit)
            {
              ubit_update_wens(i) := Bool(true)
              ubit_update_incs(i) := Bool(false)
            }
          }
        }
    }
  }

  for (i <- 0 until num_tables)
  {
    when (ubit_update_wens(i))
    {
      tables_io(i).UpdateUsefulness(s2_info.indexes(i), inc=ubit_update_incs(i))
      assert (s2_commit.valid && s2_commit.payload.ctrl.executed.reduce(_|_),
        "[tage] updating ubits when not committing.")
    }
  }
}

