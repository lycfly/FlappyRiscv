package boom_v1.exec

import boom_v1.{MaskedDC, Parameters}
import boom_v1.predictor.BranchPredictionResp
import spinal.sim._
import spinal.core._
import spinal.lib._
object FUConstants
{
  // bit mask, since a given execution pipeline may support multiple functional units
  val FUC_SZ = 8
  val FU_X   = MaskedDC(FUC_SZ)
  val FU_ALU = U(  1, FUC_SZ bits)
  val FU_BRU = U(  2, FUC_SZ bits)
  val FU_MEM = U(  4, FUC_SZ bits)
  val FU_MUL = U(  8, FUC_SZ bits)
  val FU_DIV = U( 16, FUC_SZ bits)
  val FU_FPU = U( 32, FUC_SZ bits)
  val FU_CSR = U( 64, FUC_SZ bits)
  val FU_FDV = U(128, FUC_SZ bits)
}
// tell the FUDecoders what units it needs to support
class SupportedFuncUnits(
                          val alu: Boolean  = false,
                          val bru: Boolean  = false,
                          val mem: Boolean  = false,
                          val muld: Boolean = false,
                          val fpu: Boolean  = false,
                          val csr: Boolean  = false,
                          val fdiv: Boolean = false)
{
}


class FunctionalUnitIo(num_stages: Int
                       , num_bypass_stages: Int
                       , data_width: Int
                      )(implicit p: Parameters) extends Bundle
{
  val req     = slave(Stream(new FuncUnitReq(data_width)))
  val resp    = master Stream(new FuncUnitResp(data_width))

  val brinfo  = new BrResolutionInfo().asInput

  val bypass  = new BypassData(num_bypass_stages, data_width).asOutput

  val br_unit = new BranchUnitResp().asOutput

  // only used by the fpu unit
  val fcsr_rm = in UInt(tile.FPConstants.RM_SZ bits)

  // only used by branch unit
  // TODO name this, so ROB can also instantiate it
  val get_rob_pc = new RobPCRequest().flip
  val get_pred = new GetPredictionInfo
  val status = new rocket.MStatus().asInput
}

class GetPredictionInfo(implicit p: Parameters) extends Bundle
{
  val br_tag = out UInt(p.BR_TAG_SZ bits)
  val info = in(new BranchPredictionResp())
}