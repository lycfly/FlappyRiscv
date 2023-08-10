package flappyOoO.backend

import flappyOoO.BaseIsa.RV32I
import flappyOoO.Decode.{decoder}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
import flappyOoO.{AluOp, AluSrc1Select, AluSrc2Select, Config, IdInfo, InstructionType, Opcodes, idInfo_Alu}
import spinal.core._


class IntAlu(implicit conf: Config) extends Component{
  object Data {
    object ALU_OP extends IdInfo(AluOp())
    object ALU_SRC1 extends IdInfo(AluSrc1Select())
    object ALU_SRC2 extends IdInfo(AluSrc2Select())
    object ALU_COMMIT_RESULT extends IdInfo(Bool())
  }

//  override def addOperation(
//                             opcode: MaskedLiteral,
//                             op: SpinalEnumElement[AluOp.type],
//                             src1: SpinalEnumElement[Src1Select.type],
//                             src2: SpinalEnumElement[Src2Select.type]
//                           ): Unit = {
//    pipeline.service[DecoderService].configure { config =>
//      config.addDecoding(
//        opcode,
//        Map(
//          Data.ALU_OP -> op,
//          Data.ALU_SRC1 -> src1,
//          Data.ALU_SRC2 -> src2
//        )
//      )
//    }
//  }

  def regist_decode(decoder: decoder): Unit = {
//    decoder.addDefault(
//        Map(
//          Data.ALU_OP -> Src1Select.RS1,
//          Data.ALU_COMMIT_RESULT -> False
//        )
//      )
//
//      val regRegOpcodes = Map(
//        Opcodes.ADD -> AluOp.ADD,
//        Opcodes.SUB -> AluOp.SUB,
//        Opcodes.SLT -> AluOp.SLT,
//        Opcodes.SLTU -> AluOp.SLTU,
//        Opcodes.XOR -> AluOp.XOR,
//        Opcodes.OR -> AluOp.OR,
//        Opcodes.AND -> AluOp.AND
//      )
//
//      for ((opcode, op) <- regRegOpcodes) {
//        decoder.addDecoding(
//          opcode,
//          InstructionType.R,
//          Map(
//            Data.ALU_OP -> op,
//            Data.ALU_SRC2 -> Src2Select.RS2,
//            Data.ALU_COMMIT_RESULT -> True
//          )
//        )
//      }
//
//      val regImmOpcodes = Map(
//        Opcodes.ADDI -> AluOp.ADD,
//        Opcodes.SLTI -> AluOp.SLT,
//        Opcodes.SLTIU -> AluOp.SLTU,
//        Opcodes.XORI -> AluOp.XOR,
//        Opcodes.ORI -> AluOp.OR,
//        Opcodes.ANDI -> AluOp.AND
//      )
//
//      for ((opcode, op) <- regImmOpcodes) {
//        decoder.addDecoding(
//          opcode,
//          InstructionType.I,
//          Map(
//            Data.ALU_OP -> op,
//            Data.ALU_SRC2 -> Src2Select.IMM,
//            Data.ALU_COMMIT_RESULT -> True
//          )
//        )
//      }
//
//     decoder.addDecoding(
//        Opcodes.LUI,
//        InstructionType.U,
//        Map(
//          Data.ALU_OP -> AluOp.SRC2,
//          Data.ALU_SRC2 -> Src2Select.IMM,
//          Data.ALU_COMMIT_RESULT -> True
//        )
//      )
//     decoder.addDecoding(
//        Opcodes.AUIPC,
//        InstructionType.U,
//        Map(
//          Data.ALU_OP -> AluOp.ADD,
//          Data.ALU_SRC1 -> Src1Select.PC,
//          Data.ALU_SRC2 -> Src2Select.IMM,
//          Data.ALU_COMMIT_RESULT -> True
//        )
//      )
   }

     val idInfo_alu_op = in(AluOp())
     val alu_src1 = in(UInt(conf.xlen bits))
     val alu_src2 = in(UInt(conf.xlen bits))
     val alu_result = UInt(conf.xlen bits)

        val op = idInfo_alu_op
        val src1 = alu_src1
        val src2 = alu_src2

        switch(op) {
          is(AluOp.ADD) {
            alu_result := src1 + src2
          }
          is(AluOp.SUB) {
            alu_result := src1 - src2
          }
          is(AluOp.SLT) {
            alu_result := (src1.asSInt < src2.asSInt).asUInt.resized
          }
          is(AluOp.SLTU) {
            alu_result := (src1 < src2).asUInt.resized
          }
          is(AluOp.XOR) {
            alu_result := src1 ^ src2
          }
          is(AluOp.OR) {
            alu_result := src1 | src2
          }
          is(AluOp.AND) {
            alu_result := src1 & src2
          }
          is(AluOp.SRC2) {
            alu_result := src2
          }
        }

//        when(value(Data.ALU_COMMIT_RESULT)) {
//          output(pipeline.data.RD_DATA) := result
//          output(pipeline.data.RD_DATA_VALID) := True
//        } otherwise {
//          output(Data.ALU_RESULT) := result
//        }

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
      targetDirectory = "rtl")
      .addStandardMemBlackboxing(blackboxAll)
      .generate({
        implicit val config = new Config(RV32I)
        new IntAlu()
      })
  }.printPruned()
}