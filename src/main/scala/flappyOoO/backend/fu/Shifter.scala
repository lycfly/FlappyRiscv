package flappyOoO.backend.fu

import EasonLib.Arithmetic.barrel_shift
import flappyOoO.BaseIsa.RV32I
import flappyOoO.Config
import flappyOoO.Decode.{UOPs, decoder}
import flappyOoO.regfile_read.iq2fu_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

class Shifter(conf: Config) extends Component {
  val io = new Bundle {
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
  }
  noIoPrefix()
  val shifter = new barrel_shift(conf.xlen)

  io.fu_if.ready := io.result.ready
  val op = io.fu_if.payload.op
  val src1 = io.fu_if.payload.rs1
  val src2 = io.fu_if.payload.rs2.asUInt

  io.result.valid := io.fu_if.valid

  shifter.io.shift_amount := src2.resized
  shifter.io.rotate_en := False  // No Rotate CMD
  shifter.io.direction := True  // 1: left shift, 0: right shift
  shifter.io.signed := False
  shifter.io.data_in := src1
  io.result.payload := shifter.io.data_out.asUInt

  def build() = {
    switch(op) {
      is(UOPs.SLL) {
        shifter.io.direction := True
        shifter.io.signed := False
      }
      is(UOPs.SRL) {
        shifter.io.direction := False
        shifter.io.signed := False
      }
      is(UOPs.SRA) {
        shifter.io.direction := False
        shifter.io.signed := True
      }

    }
  }


}

object shifter_inst {
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
      .generate({
        val config = Config(RV32I)
        val shifter = new Shifter(config)
        val dec = decoder()(config)

        shifter.rework({
          shifter.build()
        })
        shifter
      })
  }.printPruned()
}