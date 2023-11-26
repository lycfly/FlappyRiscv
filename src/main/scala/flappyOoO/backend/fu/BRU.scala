package flappyOoO.backend.fu

import flappyOoO.BaseIsa.RV32I
import flappyOoO.Config
import flappyOoO.Decode.UOPs
import flappyOoO.regfile_read.iq2fu_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class branch_if(config: Config) extends Bundle {
  val taken = Bool()
  val target_address = UInt(config.xlen bits)
}

class BRU(conf: Config) extends Component {
  val io = new Bundle {
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
    val address_result_in = in UInt(conf.xlen bits)
    val branchIf = out(branch_if(conf))
  }
  noIoPrefix()
  val src1 = io.fu_if.payload.rs1
  val src2 = io.fu_if.payload.rs2

  io.fu_if.ready := io.result.ready
  io.result.payload := io.fu_if.payload.pc + (conf.xlen/8) // next pc
  io.result.valid := io.fu_if.valid

  io.branchIf.target_address := io.address_result_in

  val eq = src1 === src2
  val ne = !eq
  val lt = src1.asSInt < src2.asSInt
  val ltu = src1.asUInt < src2.asUInt
  val ge = !lt
  val geu = !ltu

  val condition = io.fu_if.payload.op

  io.branchIf.taken := condition.mux(
    UOPs.JAL -> True,
    UOPs.JALR -> True,
    UOPs.BEQ -> eq,
    UOPs.BNE -> ne,
    UOPs.BLT -> lt,
    UOPs.BGE -> ge,
    UOPs.BLTU -> ltu,
    UOPs.BGEU -> geu,
    default -> False
  )
}

object BRU_inst {
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
        val config = Config(RV32I)
        val bru =  new BRU(config)
        bru
      })
  }.printPruned()
}