package boom_v1.predictor
import boom_v1.Parameters
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
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