package boom_v1.exec

import boom_v1.{Causes, FUType, MicroOp, Parameters}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

class ExeUnitResp(data_width: Int)(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()
  val data = Bits(data_width bits)
  val fflags = Flow(new FFlagsResp) // write fflags to ROB
//  override def cloneType: this.type = new ExeUnitResp(data_width).asInstanceOf[this.type]
}

class FFlagsResp(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()
//  val flags = Bits(width=tile.FPConstants.FLAGS_SZ)  //TODO: What is Flags sz?
  val flags = Bits(5 bits)

}
class ExecutionUnitIO(num_rf_read_ports: Int
                      , num_rf_write_ports: Int
                      , num_bypass_ports: Int
                      , data_width: Int
                     )(implicit p: Parameters) extends Bundle
{
  // describe which functional units we support (used by the issue window)
  val fu_types = FUType()

  val req     = slave(Stream(new FuncUnitReq(data_width)))
  val resp    = Vec(master Stream(new ExeUnitResp(data_width)), num_rf_write_ports)
  val bypass  = out(new BypassData(num_bypass_ports, data_width))
  val brinfo  = new BrResolutionInfo().asInput

  // only used by the branch unit
  val br_unit = new BranchUnitResp().asOutput
  val get_rob_pc = new RobPCRequest().flip
  val get_pred = new GetPredictionInfo
  val status = new rocket.MStatus().asInput

  // only used by the fpu unit
  val fcsr_rm = Bits(INPUT, tile.FPConstants.RM_SZ)

  // only used by the mem unit
  val lsu_io = new LoadStoreUnitIO(DECODE_WIDTH).flip
  val dmem   = new DCMemPortIO() // TODO move this out of ExecutionUnit
  val com_exception = Bool(INPUT)
}
class FuncUnitReq(data_width: Int)(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()

  val num_operands = 3

  val rs1_data = UInt(width = data_width bits)
  val rs2_data = UInt(width = data_width bits)
  val rs3_data = UInt(width = data_width bits) // only used for FMA units
  //   val rs_data = Vec.fill(num_operands) {UInt(width=data_width)}
  //   def rs1_data = rs_data(0)
  //   def rs2_data = rs_data(1)
  //   def rs3_data = rs_data(2)

  val kill = Bool() // kill everything

}

class FuncUnitResp(data_width: Int)(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()
  val data = UInt(width = data_width bits)
  val fflags =  Flow(new FFlagsResp)
  val addr = UInt(width = p.vaddrBits+1 bits) // only for maddr -> LSU
  val mxcpt = Flow(UInt(width=Causes.all.max+2 bits)) //only for maddr->LSU TODO: why + 2
}

class BypassData(num_bypass_ports: Int, data_width: Int)(implicit p: Parameters) extends Bundle
{
  val valid = Vec(Bool(),num_bypass_ports)
  val uop   = Vec(new MicroOp(), num_bypass_ports)
  val data  = Vec(UInt(width = data_width bits),num_bypass_ports)

  def getNumPorts: Int = num_bypass_ports
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
  val btb_update      = new rocket.BTBUpdate
  val bht_update      = Valid(new rocket.BHTUpdate)
  val bpd_update      = Valid(new BpdUpdate)

  val xcpt            = Valid(new Exception)

  val debug_btb_pred  = Bool() // just for debug, did the BTB and BHT predict taken?
}

abstract class FunctionalUnit(is_pipelined: Boolean
                              , num_stages: Int
                              , num_bypass_stages: Int
                              , data_width: Int
                              , has_branch_unit: Boolean = false)
                             (implicit p: Parameters) extends BoomModule()(p)
  with HasBoomCoreParameters
{
  val io = new FunctionalUnitIo(num_stages, num_bypass_stages, data_width)
}

object execute_inst {
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
      .generate(new execute())
  }.printPruned()
}