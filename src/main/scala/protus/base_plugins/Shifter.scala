package protus.base_plugins

import protus.backbone.{DecoderService, InstructionType, IssueService, Opcodes, Pipeline, PipelineData, Plugin, Stage}
import spinal.core._

class Shifter(exeStages: Set[Stage]) extends Plugin[Pipeline] {
  object ShiftOp extends SpinalEnum {
    val SHIFTNONE, SLL, SRL, SRA = newElement()
  }

  object Data {
    object SHIFT_OP extends PipelineData(ShiftOp())
  }

  override def setup(): Unit = {
    val issuer = pipeline.service[IssueService]

    pipeline.service[DecoderService].configure { config =>
      config.addDefault(
        Map(
          Data.SHIFT_OP -> ShiftOp.SHIFTNONE
        )
      )

      val ops = Seq(
        (Opcodes.SLLI, ShiftOp.SLL, InstructionType.I),
        (Opcodes.SRLI, ShiftOp.SRL, InstructionType.I),
        (Opcodes.SRAI, ShiftOp.SRA, InstructionType.I),
        (Opcodes.SLL, ShiftOp.SLL, InstructionType.R),
        (Opcodes.SRL, ShiftOp.SRL, InstructionType.R),
        (Opcodes.SRA, ShiftOp.SRA, InstructionType.R)
      )

      for ((opcode, op, itype) <- ops) {
        config.addDecoding(
          opcode,
          itype,
          Map(
            Data.SHIFT_OP -> op
          )
        )
        issuer.setDestinations(opcode, exeStages)
      }
    }
  }

  override def build(): Unit = {
    for (exeStage <- exeStages) {
      exeStage plug new Area {
        import exeStage._

        val src = UInt(config.xlen bits)
        src := value(pipeline.data.RS1_DATA)
        val shamt = UInt(5 bits)
        val useImm = value(pipeline.data.IMM_USED)

        when(useImm) {
          shamt := value(pipeline.data.IMM)(4 downto 0)
        } otherwise {
          shamt := value(pipeline.data.RS2_DATA)(4 downto 0)
        }

        val op = value(Data.SHIFT_OP)

        val result = op.mux(
          ShiftOp.SHIFTNONE -> U(0),
          ShiftOp.SLL -> (src |<< shamt),
          ShiftOp.SRL -> (src |>> shamt),
          ShiftOp.SRA -> (src.asSInt >> shamt).asUInt
        )

        when(arbitration.isValid && op =/= ShiftOp.SHIFTNONE) {
          arbitration.rs1Needed := True
          arbitration.rs2Needed := !useImm
          output(pipeline.data.RD_DATA) := result
          output(pipeline.data.RD_DATA_VALID) := True
        }
      }
    }
  }
}
