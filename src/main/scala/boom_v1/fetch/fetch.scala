package boom_v1.fetch
import boom_v1.Parameters
import boom_v1.predictor.BTBResp
import spinal.core._
import spinal.lib._
class FrontendReq(implicit p: Parameters) extends Bundle {
  val pc = UInt(width = p.vaddrBitsExtended bits)
  val speculative = Bool()
}

class FrontendResp(implicit p: Parameters) extends Bundle {
  val btb = Flow(new BTBResp)
  val pc = UInt(width = p.vaddrBitsExtended bits)  // ID stage PC
  val data = UInt(width = p.fetchWidth * p.coreInstBits bits)
  val mask = Bits(width = p.fetchWidth bits)
  val xcpt_if = Bool()
  val replay = Bool()
}

