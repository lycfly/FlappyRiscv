package flappyOoO.backend.fu

import flappyOoO.BaseIsa.RV32I
import flappyOoO.Decode.{UOPs, decoder}
import flappyOoO._
import flappyOoO.regfile_read.iq2fu_if
import spinal.core._
import spinal.lib.{Stream, master, slave}


class IntAlu(conf: Config, withAGU: Boolean = false) extends Component{
  val io = new Bundle{
    val fu_if = slave(Stream(iq2fu_if(conf)))
    val result = master(Stream(UInt(conf.xlen bits)))
    val address_result_out = withAGU generate out(UInt(conf.xlen bits))
  }

  noIoPrefix()
  io.fu_if.ready := io.result.ready
  val op = io.fu_if.payload.op
  val src1 = io.fu_if.payload.rs1.asUInt
  val src2 = io.fu_if.payload.rs2.asUInt

  io.result.valid := io.fu_if.valid
  io.result.payload := 0

  val is_minus_flag = Bool()
  val src2_neg = ~src2 + 1
  val add_pinA = src1
  val add_pinB = is_minus_flag ? src2_neg | src2
  val add_result = UInt(conf.xlen + 1 bits)
  add_result := add_pinA +^ add_pinB
  if(withAGU){
    io.address_result_out := 0
  }

  def build() = {
    is_minus_flag := False
    switch(op) {
      if(withAGU){
        is(UOPs.JAL, UOPs.JALR, UOPs.LOAD, UOPs.LOADU, UOPs.STORE) {
          is_minus_flag := False
          io.address_result_out := add_result(conf.xlen - 1 downto 0)
        }
      }
      is(UOPs.ADD) {
        is_minus_flag := False
        io.result.payload := add_result(conf.xlen - 1 downto 0)
      }
      is(UOPs.SUB) {
        is_minus_flag := True
        io.result.payload := add_result(conf.xlen - 1 downto 0)
      }
      is(UOPs.SLT, UOPs.SLTU) {
        is_minus_flag := True
        io.result.payload := add_result.msb.asUInt.resized
      }
      is(UOPs.XOR) {
        io.result.payload := src1 ^ src2
      }
      is(UOPs.OR) {
        io.result.payload := src1 | src2
      }
      is(UOPs.AND) {
        io.result.payload := src1 & src2
      }

    }
  }

}

object IntAlu_inst {
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
        val alu = new IntAlu(config, true)
        val dec = decoder()(config)
        println(UOPs.elements.filter(_.getName() == "CSRRW")(0))
        alu.rework({
          alu.build()
        })
        alu
      })
  }.printPruned()
}