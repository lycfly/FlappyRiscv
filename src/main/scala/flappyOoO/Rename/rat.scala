package flappyOoO.Rename

import EasonLib.Arithmetic.booth4
import EasonLib.DesignCompiler.{DesignCompilerFlow, DesignCompiler_config}
import flappyOoO.BaseIsa.RV32I
import flappyOoO.Config
import flappyOoO.Decode.{rd_bundle, rs_bundle}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class rat_bundle(conf: Config) extends Bundle with IMasterSlave{
  val rs1_read = Vec((Flow(UInt(log2Up(conf.ArchRegsNum) bits))), conf.DecoderWidth)
  val rs2_read = Vec((Flow(UInt(log2Up(conf.ArchRegsNum) bits))), conf.DecoderWidth)
  val rd_read = Vec((Flow(UInt(log2Up(conf.ArchRegsNum) bits))), conf.DecoderWidth)
  val rd_write = Vec((Flow(UInt(log2Up(conf.ArchRegsNum) bits))), conf.DecoderWidth)
  val rd_write_value = Vec((UInt(log2Up(conf.PhysicalRegsNum) bits)), conf.DecoderWidth)
  val phy_rs1 = Vec((Flow(UInt(log2Up(conf.PhysicalRegsNum) bits))), conf.DecoderWidth)
  val phy_rs2 = Vec((Flow(UInt(log2Up(conf.PhysicalRegsNum) bits))), conf.DecoderWidth)
  val phy_rd = Vec((Flow(UInt(log2Up(conf.PhysicalRegsNum) bits))), conf.DecoderWidth)
  override def asMaster(): Unit = {
    for(i <- 0 until conf.DecoderWidth){
      master(rs1_read(i))
      master(rs2_read(i))
      master(rd_read(i))
      master(rd_write(i))
      out(rd_write_value)
      slave(phy_rs1(i))
      slave(phy_rs2(i))
      slave(phy_rd(i))
    }
  }
}

class rat(implicit val conf: Config) extends Component {
  val io = new Bundle {
    val flush = in Bool()
    val recovery = in Bool()
    val rat_if = slave(rat_bundle(conf))
    val arat_in = Vec(in UInt(log2Up(conf.PhysicalRegsNum) bits), conf.ArchRegsNum)


  }
  noIoPrefix()
  val rat_mem = Vec(UInt(log2Up(conf.PhysicalRegsNum) bits), conf.ArchRegsNum)
  rat_mem(0) := 0
  rat_mem(0).allowPruning()
  for(i <- 1 until conf.ArchRegsNum){
    rat_mem(i).setAsReg().init(0)
  }
  for(i <- 0 until conf.DecoderWidth){
    io.rat_if.phy_rs1(i).valid := False
    io.rat_if.phy_rs1(i).payload := 0
    when(io.rat_if.rs1_read(i).valid){
      io.rat_if.phy_rs1(i).push(rat_mem(io.rat_if.rs1_read(i).payload))
    }
    io.rat_if.phy_rs2(i).valid := False
    io.rat_if.phy_rs2(i).payload := 0
    when(io.rat_if.rs2_read(i).valid) {
      io.rat_if.phy_rs2(i).push(rat_mem(io.rat_if.rs2_read(i).payload))
    }
    io.rat_if.phy_rd(i).valid := False
    io.rat_if.phy_rd(i).payload := 0
    when(io.rat_if.rd_read(i).valid) {
      io.rat_if.phy_rd(i).push(rat_mem(io.rat_if.rd_read(i).payload))
    }
  }
  for(i <- 0 until conf.DecoderWidth) {
    for(j <- 1 until conf.ArchRegsNum){
      when(io.rat_if.rd_write(i).valid) {
        when(io.rat_if.rd_write(i).payload === j){
          rat_mem(j) := io.rat_if.rd_write_value(i)
        }
      }
    }
  }
  for(i <- 1 until conf.ArchRegsNum){
    when(io.flush) {
      rat_mem(i) := 0
    }
  }
  for(i <- 1 until conf.ArchRegsNum){
    when(io.recovery) {
      rat_mem(i) := io.arat_in(i)
    }
  }

  io.arat_in(0).allowPruning()

}

object rat_inst {
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
        val rat = new rat()
        rat
      }
      )
  }.printPruned()
}

object rat_inst_syn {
  def main(args: Array[String]): Unit = {

    val dc_config = DesignCompiler_config(process = 28, freq = 100)
    val dc = new DesignCompilerFlow(
      design = {
        implicit val config = new Config(RV32I)
        val rat = new rat()
        rat
      },
      topModuleName = "rat",
      workspacePath = "/home/lyc/projects/riscv/FlappyRiscv/syn/rat",
      DCConfig = dc_config,
      designPath = ""
    ).doit()
  }
}