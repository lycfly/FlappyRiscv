package flappyOoO.regfile_read

import flappyOoO.{Config, FU_TYPE, RegisterSource}
import flappyOoO.Decode.UOPs
import flappyOoO.issue.{iq_entry, rd_iq_entry, rs_iq_entry}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
case class iq2fu_if(conf: Config) extends Bundle{
  val fu = FU_TYPE()
  val op = UOPs()
  val pc = UInt(conf.xlen bits)
  val rs1 = Bits(conf.xlen bits)
  val rs2 = Bits(conf.xlen bits)
  val rd_index = UInt(log2Up(conf.PhysicalRegsNum) bits)
}
class rf_access(conf: Config) extends Component {
  val io = new Bundle {
    val iq_issued = Vec(slave(Stream(iq_entry(conf))), conf.IssueWidth)
    val fu_if = Vec(master(Stream(iq2fu_if(conf))), conf.IssueWidth)
    val write_io = Vec(slave(rfWriteIo(conf)), conf.IssueWidth)

  }
  noIoPrefix()
  val regfile = new regfile(conf)
  regfile.io.write_io <> io.write_io


  for(i <- 0 until conf.IssueWidth){
    regfile.io.read_io(i).rs1Index := io.iq_issued(i).payload.rs1.index
    regfile.io.read_io(i).rs2Index := io.iq_issued(i).payload.rs2.index

    io.fu_if(i).valid := io.iq_issued(i).valid
    io.iq_issued(i).ready := io.fu_if(i).ready

    io.fu_if(i).payload.fu := io.iq_issued(i).payload.fu
    io.fu_if(i).payload.op := io.iq_issued(i).payload.op
    io.fu_if(i).payload.rd_index := io.iq_issued(i).payload.rd.index
    io.fu_if(i).payload.pc := io.iq_issued(i).payload.pc

    switch(io.iq_issued(i).payload.rs1.source){
      is(RegisterSource.REGISTER){
        io.fu_if(i).payload.rs1 := regfile.io.read_io(i).rs1Data
      }
      is(RegisterSource.X0){
        io.fu_if(i).payload.rs1 := 0
      }
      is(RegisterSource.PC){
        io.fu_if(i).payload.rs1 := io.iq_issued(i).payload.pc.asBits
      }
      is(RegisterSource.IMM){
        io.fu_if(i).payload.rs1 := io.iq_issued(i).payload.imm
      }
    }
    switch(io.iq_issued(i).payload.rs2.source){
      is(RegisterSource.REGISTER){
        io.fu_if(i).payload.rs2 := regfile.io.read_io(i).rs2Data
      }
      is(RegisterSource.X0){
          io.fu_if(i).payload.rs2 := 0
      }
      is(RegisterSource.PC){
        io.fu_if(i).payload.rs2 := io.iq_issued(i).payload.pc.asBits
      }
      is(RegisterSource.IMM){
        io.fu_if(i).payload.rs2 := io.iq_issued(i).payload.imm
      }
    }

  }

}

object rf_access_inst {
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
//      .addStandardMemBlackboxing(blackboxAll)
      .generate(new rf_access(conf = Config()))
  }.printPruned()
}