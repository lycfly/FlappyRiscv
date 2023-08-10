package flappyOoO.Rename

import EasonLib.DesignCompiler.{DesignCompilerFlow, DesignCompiler_config}
import flappyOoO.BaseIsa.RV32I
import flappyOoO.Config
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps


class arat(implicit val conf: Config) extends Component {
  val io = new Bundle {
    val flush = in Bool()
    val rd_write = Vec(slave(Flow(UInt(log2Up(conf.ArchRegsNum) bits))), conf.DecoderWidth)
    val rd_write_value = Vec(in(UInt(log2Up(conf.PhysicalRegsNum) bits)), conf.DecoderWidth)
    val rat_out = Vec(out UInt(log2Up(conf.PhysicalRegsNum) bits), conf.ArchRegsNum)
  }
  noIoPrefix()
  val rat_mem = Vec(UInt(log2Up(conf.PhysicalRegsNum) bits), conf.ArchRegsNum)
  rat_mem(0) := 0
  for(i <- 1 until conf.ArchRegsNum){
    rat_mem(i).setAsReg().init(0)
  }
  for (i <- 0 until conf.DecoderWidth) {
    for (j <- 1 until conf.ArchRegsNum) {
      when(io.rd_write(i).valid) {
        when(io.rd_write(i).payload === j) {
          rat_mem(j) := io.rd_write_value(i)
        }
      }
    }
  }
  for(i <- 1 until conf.ArchRegsNum){
    when(io.flush) {
      rat_mem(i) := 0
    }
  }

  io.rat_out := rat_mem

}

object arat_inst {
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
        val rat = new arat()
        rat
      })
  }.printPruned()
}
object arat_inst_syn {
  def main(args: Array[String]): Unit = {

    val dc_config = DesignCompiler_config(process = 28, freq = 100)
    val dc = new DesignCompilerFlow(
      design = {
        implicit val config = new Config(RV32I)
        val arat = new arat()
        arat
      },
      topModuleName = "arat",
      workspacePath = "/home/lyc/projects/riscv/FlappyRiscv/syn/arat",
      DCConfig = dc_config,
      designPath = ""
    ).doit()
  }
}