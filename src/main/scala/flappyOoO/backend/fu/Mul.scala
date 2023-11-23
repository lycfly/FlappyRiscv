package flappyOoO.backend.fu

import EasonLib.Arithmetic.SignMultiplier
import flappyOoO.Config
import flappyOoO.regfile_read.iq2fu_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

class Mul(conf: Config) extends Component {
  val io = new Bundle {
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
  }
  noIoPrefix()
  io.fu_if.ready := io.result.ready
  val op = io.fu_if.payload.op
  val src1 = io.fu_if.payload.rs1.asUInt
  val src2 = io.fu_if.payload.rs2.asUInt
  io.result.valid := io.fu_if.valid

  val multiplier = conf.Multiplier.cands(conf.MultiplierType)("inst")


}

object Mul_inst {
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
      targetDirectory = "rtl")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new Mul())
  }.printPruned()
}