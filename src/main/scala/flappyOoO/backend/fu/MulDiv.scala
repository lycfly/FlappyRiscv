package flappyOoO.backend.fu

import EasonLib.Arithmetic.multiplier.{MUlConfig, MulWraper, SignMultiplier, booth4}
import EasonLib.Utils.MyProgress
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

class MulDiv(conf: Config) extends Component {
  val io = new Bundle {
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
  }
  noIoPrefix()
  val op = io.fu_if.payload.op
  val src1 = io.fu_if.payload.rs1
  val src2 = io.fu_if.payload.rs2

  val mc = MUlConfig(conf.xlen+1, conf.xlen+1, conf.MultiplierType, use_busy_flag = true)
  val mul_wraper = MulWraper(mc)
  mul_wraper.io.mulif.dinA := (src1.msb ## src1).asBits
  mul_wraper.io.mulif.dinB := (src2.msb ## src2).asBits
  mul_wraper.io.mulif.din_vld := False

  io.fu_if.ready := io.result.ready
  io.result.payload := 0
  io.result.valid := False

  def build() = {
    switch(op) {
      is(UOPs.get_element("MUL")) {
        io.result.payload :=  mul_wraper.io.mulif.dout.asUInt(conf.xlen-1 downto 0)
        io.result.valid := mul_wraper.io.mulif.dout_vld
        mul_wraper.io.mulif.din_vld := io.fu_if.valid
        io.fu_if.ready := io.result.ready & ~mul_wraper.io.mulif.busy
      }
      is(UOPs.get_element("MULH")) {
        io.result.payload := mul_wraper.io.mulif.dout.asUInt(conf.xlen*2 - 1 downto conf.xlen)
        io.result.valid := mul_wraper.io.mulif.dout_vld
        io.fu_if.ready := io.result.ready & ~mul_wraper.io.mulif.busy
      }
      is(UOPs.get_element("MULHSU")) {
        mul_wraper.io.mulif.dinA := (src1.msb ## src1).asBits
        mul_wraper.io.mulif.dinB := (False ## src2).asBits
        io.result.payload := mul_wraper.io.mulif.dout.asUInt(conf.xlen * 2 - 1 downto conf.xlen)
        io.result.valid := mul_wraper.io.mulif.dout_vld
        mul_wraper.io.mulif.din_vld := io.fu_if.valid
        io.fu_if.ready := io.result.ready & ~mul_wraper.io.mulif.busy
      }
      is(UOPs.get_element("MULHU")) {
        mul_wraper.io.mulif.dinA := (False ## src1).asBits
        mul_wraper.io.mulif.dinB := (False ## src2).asBits
        io.result.payload := mul_wraper.io.mulif.dout.asUInt(conf.xlen * 2 - 1 downto conf.xlen)
        io.result.valid := mul_wraper.io.mulif.dout_vld
        mul_wraper.io.mulif.din_vld := io.fu_if.valid
        io.fu_if.ready := io.result.ready & ~mul_wraper.io.mulif.busy
      }
    }
  }


}

object MulDiv_inst {
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
        val mul = new MulDiv(config)
        val dec = decoder()(config)
        MyProgress("my dec")

        mul.rework({
          mul.build()
        })
        mul
      })
  }.printPruned()
}