package boom_v1.exec

import boom_v1.{FUType, MicroOp, Parameters}
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
  val flags = Bits(2 bits)

}
class ExecutionUnitIO(num_rf_read_ports: Int
                      , num_rf_write_ports: Int
                      , num_bypass_ports: Int
                      , data_width: Int
                     )(implicit p: Parameters) extends Bundle
{
  // describe which functional units we support (used by the issue window)
  val fu_types = FUType()

  val req     = (new DecoupledIO(new FuncUnitReq(data_width))).flip
  val resp    = Vec(num_rf_write_ports, new DecoupledIO(new ExeUnitResp(data_width)))
  val bypass  = new BypassData(num_bypass_ports, data_width).asOutput
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
  val addr = UInt(width = p.vaddrBits+1) // only for maddr -> LSU
  val mxcpt = new ValidIO(UInt(width=rocket.Causes.all.max)) //only for maddr->LSU

  override def cloneType = new FuncUnitResp(data_width)(p).asInstanceOf[this.type]
}

class BypassData(num_bypass_ports: Int, data_width: Int)(implicit p: Parameters) extends BoomBundle()(p)
{
  val valid = Vec(num_bypass_ports, Bool())
  val uop   = Vec(num_bypass_ports, new MicroOp())
  val data  = Vec(num_bypass_ports, UInt(width = data_width))

  def getNumPorts: Int = num_bypass_ports
  override def cloneType: this.type = new BypassData(num_bypass_ports, data_width).asInstanceOf[this.type]
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