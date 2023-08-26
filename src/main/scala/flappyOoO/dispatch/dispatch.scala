package flappyOoO.dispatch

import flappyOoO.BaseIsa.RV32I
import flappyOoO.Config
import flappyOoO.Decode.{UOPs, micro_op_if}
import flappyOoO.Rename.dispatch_rob_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class dispatch_iq_if(conf: Config) extends Bundle{
//  val wr_index = UInt(log2Up(conf.IqEntryNum) bits)
  val uopRn = micro_op_if(conf, isPhysical = true)
  val robAge = UInt(log2Up(conf.RobEntryNum) bits)
}
class dispatch(implicit conf: Config) extends Component {
  val io = new Bundle {
    val valid = in Bool()
    val dispRobIn = Vec(in(dispatch_rob_if(conf)), conf.DecoderWidth)
    val robId = Vec(slave(Flow(UInt(log2Up(conf.RobEntryNum) bits))), conf.DecoderWidth)
    val uopRenamed = Vec(in(micro_op_if(conf, isPhysical = true)), conf.DecoderWidth)
//    val IqBusyVector = in Bits(conf.IqEntryNum bits)
    val dispIq = Vec(master(Stream(dispatch_iq_if(conf))), conf.DecoderWidth)
    val dispRobOut = Vec(master(Stream(dispatch_rob_if(conf))), conf.DecoderWidth)

  }
  noIoPrefix()
  for(i <- 0 until conf.DecoderWidth) {
    io.dispRobOut(i).valid := io.valid && (io.uopRenamed(i).op =/= UOPs.IDLE)
    io.dispRobOut(i).payload <> io.dispRobIn(i)

    io.dispIq(i).valid := io.valid && (io.uopRenamed(i).op =/= UOPs.IDLE)
    io.dispIq(i).payload.uopRn := io.uopRenamed(i)
    io.dispIq(i).robAge := io.robId(i).payload
  }




}

object dispatch_inst {
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
      .generate({
        implicit val config = new Config(RV32I)
        val dispatch = new dispatch()
        dispatch
      }
      )
  }.printPruned()
}