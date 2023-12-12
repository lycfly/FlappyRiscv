package boom_v1.predictor
import boom_v1.Parameters
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

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
