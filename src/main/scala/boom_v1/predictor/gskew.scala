package boom_v1.predictor

import boom_v1.Parameters
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
case class GSkewParameters(
                            enabled: Boolean = true,
                            history_length: Int = 21,
                            bimo_num_entries: Int = 16*1024,
                            gsh0_num_entries: Int = 64*1024,
                            gsh1_num_entries: Int = 64*1024,
                            meta_num_entries: Int = 64*1024,
                            dualported: Boolean = false,
                            // Enable meta predictor to choose between bimodal and gskew majority vote.
                            enable_meta: Boolean = true
                          )

class GSkewResp(fetch_width: Int, bi_idx_sz: Int, g0_idx_sz: Int, g1_idx_sz: Int, me_idx_sz: Int) extends Bundle
{
  // needed to update predictor at Commit
  val bimo_index = UInt(width = bi_idx_sz bits)
  val gsh0_index = UInt(width = g0_idx_sz bits)
  val gsh1_index = UInt(width = g1_idx_sz bits)
  val meta_index = UInt(width = me_idx_sz bits)

  val bimo = UInt(width = fetch_width bits)
  val gsh0 = UInt(width = fetch_width bits)
  val gsh1 = UInt(width = fetch_width bits)
  val meta = UInt(width = fetch_width bits)

}

object GSkewBrPredictor
{
  def GetRespInfoSize(p: Parameters, fetchWidth: Int): Int =
  {
    val params = p.gskew.get
    val dummy = new GSkewResp(
      fetchWidth,
      log2Up(params.bimo_num_entries),
      log2Up(params.gsh0_num_entries),
      log2Up(params.gsh1_num_entries),
      log2Up(params.meta_num_entries)
    )
    dummy.getBitsWidth
  }
}
