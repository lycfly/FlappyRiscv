package boom_v1.exec

import boom_v1.Parameters
import boom_v1.commit.Exception
import boom_v1.predictor.{BHTUpdate, BTBUpdate}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps




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
class BrResolutionInfo(implicit p: Parameters) extends Bundle
{
  val valid      = Bool()
  val mispredict = Bool()
  val mask       = UInt(width = p.MAX_BR_COUNT bits) // the resolve mask
  val tag        = UInt(width = p.BR_TAG_SZ bits)    // the branch tag that was resolved
  val exe_mask   = UInt(width = p.MAX_BR_COUNT bits) // the br_mask of the actual branch uop
  // used to reset the dec_br_mask
  val rob_idx    = UInt(width = p.ROB_ADDR_SZ bits)
  val ldq_idx    = UInt(width = p.MEM_ADDR_SZ bits)  // track the "tail" of loads and stores, so we can
  val stq_idx    = UInt(width = p.MEM_ADDR_SZ bits)  // quickly reset the LSU on a mispredict
  val taken      = Bool()                     // which direction did the branch go?
  val is_jr      = Bool()

  // for stats
  val btb_made_pred  = Bool()
  val btb_mispredict = Bool()
  val bpd_made_pred  = Bool()
  val bpd_mispredict = Bool()
}

// for critical path reasons, some of the elements in this bundle may be delayed.
class BranchUnitResp(implicit p: Parameters) extends Bundle
{
  val take_pc         = Bool()
  val target          = UInt(width = p.vaddrBits+1 bits)

  val pc              = UInt(width = p.vaddrBits+1 bits) // TODO this isn't really a branch_unit thing

  val brinfo          = new BrResolutionInfo()
  val btb_update_valid= Bool() // TODO turn this into a directed bundle so we can fold this into btb_update?
  val btb_update      = new BTBUpdate
  val bht_update      = Flow(new BHTUpdate)
  val bpd_update      = Flow(new BpdUpdate)

  val xcpt            = Flow(new Exception)

  val debug_btb_pred  = Bool() // just for debug, did the BTB and BHT predict taken?
}


object function_units_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new function_units())
  }.printPruned()
}