package boom_v1.predictor.gshare

import boom_v1.Parameters
import boom_v1.predictor.boom.BrPredictor
import spinal.core._

case class GShareParameters(
                             enabled: Boolean = true,
                             history_length: Int = 12,
                             // The prediction table requires 1 read and 1 write port.
                             // Should we use two ports or should we bank the p-table?
                             dualported: Boolean = false
                           )



class GShareResp(index_sz: Int) extends Bundle
{
  val index = UInt(width = index_sz bits) // needed to update predictor at Commit
}

object GShareBrPredictor
{
  def GetRespInfoSize(p: Parameters, hlen: Int): Int =
  {
    val dummy = new GShareResp(hlen)
    dummy.getBitsWidth
  }
}


class GShareBrPredictor(
                         fetch_width: Int,
                         history_length: Int = 12,
                         dualported: Boolean = false
                       )(implicit p: Parameters)
  extends BrPredictor(fetch_width, history_length)(p)
{
  val num_entries = 1 << history_length

  println ("\tBuilding (" + (num_entries * fetch_width * 2/8/1024) +
    " kB) GShare Predictor, with " + history_length + " bits of history for (" +
    fetch_width + "-wide fetch) and " + num_entries + " entries.")

  require (p.coreInstBytes == 4)

  //------------------------------------------------------------

  private def Hash (addr: UInt, hist: UInt) =
    (addr >> U(log2Up(fetch_width*p.coreInstBytes))) ^ hist

  //------------------------------------------------------------
  // Predictor state.

  val counters = new TwobcCounterTable(fetch_width, num_entries, dualported)

  //------------------------------------------------------------
  // Get prediction.

  val stall = !io.resp.ready

  val r_idx = Hash(io.req_pc, this.ghistory.asUInt)
  counters.io.s0_r_idx := r_idx
  counters.io.stall := stall

  val resp_info = (new GShareResp(log2Up(num_entries)))
  resp_info.index      := RegNext(RegNext(r_idx))
  io.resp.payload.takens  := counters.io.s2_r_out
  io.resp.payload.info    := resp_info.asBits

  // Always overrule the BTB, which will almost certainly have less history.
  io.resp.valid := Bool(true) && !this.disable_bpd

  //------------------------------------------------------------
  // Update counter table.

  val commit_info = (new GShareResp(log2Up(num_entries)))
  commit_info.assignFromBits(this.commit.payload.info.info.asBits)

  counters.io.update.valid                 := this.commit.valid && !this.disable_bpd
  counters.io.update.payload.index            := commit_info.index
  counters.io.update.payload.executed         := this.commit.payload.ctrl.executed
  counters.io.update.payload.takens           := this.commit.payload.ctrl.taken
  counters.io.update.payload.was_mispredicted := this.commit.payload.ctrl.mispredicted.reduce(_|_)
  counters.io.update.payload.do_initialize    := Bool(false)

  //------------------------------------------------------------
}
