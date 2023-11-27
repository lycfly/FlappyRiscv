package flappyOoO.backend.fu

import flappyOoO.BaseIsa.RV32I
import flappyOoO.{AccessWidth, Config, MemBus}
import flappyOoO.Decode.UOPs
import flappyOoO.regfile_read.iq2fu_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.language.postfixOps


class LSU(conf: Config) extends Component {
  val io = new Bundle {
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
    val address_result_in = in UInt (conf.xlen bits)
    val dBus = master(MemBus(conf.dbusConfig, conf.dbusIdWidth))

  }
  noIoPrefix()
  val src1 = io.fu_if.payload.rs1
  val src2 = io.fu_if.payload.rs2

  def checkAccessWidth(accessWidth: SpinalEnumCraft[AccessWidth.type], address: UInt) = {
    val misaligned = Bool()
    val baseMask = Bits(conf.xlen / 8 bits)

    switch(accessWidth) {
      is(AccessWidth.B) {
        misaligned := False
        baseMask := B"0001"
      }
      is(AccessWidth.H) {
        misaligned := (address & 1) =/= 0
        baseMask := B"0011"
      }
      is(AccessWidth.W) {
        misaligned := (address & 3) =/= 0
        baseMask := B"1111"
      }
    }
    (misaligned, baseMask)
  }

  val (misaligned, baseMask) = checkAccessWidth(io.fu_if.payload.lsu_width, io.address_result_in)

  val mask = baseMask |<< io.address_result_in(1 downto 0)

}

object LSU_inst {
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
      .generate(new LSU())
  }.printPruned()
}