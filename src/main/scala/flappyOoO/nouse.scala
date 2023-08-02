
import protus.backbone.BaseIsa.RV32I
import protus.backbone.{DecoderService, InstructionFormat, InstructionType, Pipeline, PipelineData, Plugin, Stage, StandardPipelineData, StaticPipeline, TrapCause, TrapService, Utils}
import spinal.core._
import spinal.core.internals.Literal

import scala.collection.mutable

case class ImmediateDecoder(ir: Bits) {
  private def signExtend(data: Bits): UInt = {
    Utils.signExtend(data, 32).asUInt
  }

  def i = signExtend(ir(31 downto 20))
  def s = signExtend(ir(31 downto 25) ## ir(11 downto 7))
  def b = signExtend(ir(31) ## ir(7) ## ir(30 downto 25) ## ir(11 downto 8) ## False)
  def u = (ir(31 downto 12) << 12).asUInt
  def j = signExtend(
    ir(31) ## ir(19 downto 12) ## ir(20) ## ir(30 downto 25) ## ir(24 downto 21) ## False
  )
}

class Decoder(decodeStage: Stage) extends Plugin[Pipeline] with DecoderService {
  private case class FixedRegisters(rs1: Option[Int], rs2: Option[Int], rd: Option[Int])

  private val instructionTypes = mutable.Map[MaskedLiteral, InstructionType]()
  private val decodings = mutable.Map[MaskedLiteral, Action]()
  private val fixedRegisters = mutable.Map[MaskedLiteral, FixedRegisters]()
  private val defaults = mutable.Map[PipelineData[_ <: Data], Data]()

  private val defaultIrMapper: IrMapper = (_, ir) => ir
  private var irMapper = defaultIrMapper

  override protected val decoderConfig = new DecoderConfig {

    override def setIrMapper(mapper: IrMapper): Unit = {
      assert(irMapper eq defaultIrMapper)
      irMapper = mapper
    }

    override def addDecoding(
                              opcode: MaskedLiteral,
                              itype: InstructionType,
                              action: Action
                            ): Unit = {
      assert(!instructionTypes.contains(opcode), s"Multiple instruction types set for $opcode")

      instructionTypes(opcode) = itype
      addDecoding(opcode, action)
    }

    override def addDecoding(opcode: MaskedLiteral, action: Action): Unit = {
      val currentAction = decodings.getOrElse(opcode, Map())

      for ((key, data) <- currentAction) {
        assert(
          !action.contains(key),
          s"Conflicting decodings for opcode $opcode: $key overspecified"
        )
      }

      decodings(opcode) = currentAction ++ action
    }

    override def setFixedRegisters(
                                    opcode: MaskedLiteral,
                                    rs1: Option[Int],
                                    rs2: Option[Int],
                                    rd: Option[Int]
                                  ): Unit = {
      assert(!fixedRegisters.contains(opcode), s"Fixed registers already set for $opcode")

      def check(reg: Int) = assert(reg >= 0 && reg < 32)
      rs1.foreach(check)
      rs2.foreach(check)
      rd.foreach(check)

      fixedRegisters(opcode) = FixedRegisters(rs1, rs2, rd)
    }

    override def addDefault(action: Action): Unit = {
      defaults ++= action
    }

    override def addDefault(data: PipelineData[_ <: Data], value: Data): Unit = {
      defaults(data) = value
    }
  }

  override def setup(): Unit = {
    configure { config =>
      config.addDefault(
        Map(
          pipeline.data.RD_DATA_VALID -> False,
          pipeline.data.IMM -> U(0)
        )
      )
    }
  }

  override protected def stage(): Stage = decodeStage

  override def build(): Unit = {
//    val decodeStage = new Stage("decode") {
//    }


    decodeStage plug new Area {

      import decodeStage._

      applyAction(decodeStage, defaults.toMap)

      val ir = irMapper(decodeStage, value(pipeline.data.IR))

      output(pipeline.data.RS1) := ir(19 downto 15)
      output(pipeline.data.RS2) := ir(24 downto 20)
      output(pipeline.data.RD) := ir(11 downto 7)

      val immDecoder = ImmediateDecoder(ir.asBits)

      switch(ir) {
        for ((key, action) <- decodings) {
          is(key) {
            applyAction(decodeStage, action)

            assert(
              instructionTypes.contains(key),
              s"Opcode $key has decodings but no instruction type set"
            )

            val instructionType = instructionTypes(key)

            val imm = instructionType.format match {
              case InstructionFormat.I => immDecoder.i
              case InstructionFormat.S => immDecoder.s
              case InstructionFormat.B => immDecoder.b
              case InstructionFormat.U => immDecoder.u
              case InstructionFormat.J => immDecoder.j
              case InstructionFormat.R => U(0)
            }

            output(pipeline.data.IMM) := imm
            output(pipeline.data.IMM_USED) :=
              Bool(instructionType.format != InstructionFormat.R)

            output(pipeline.data.RS1_TYPE) := instructionType.rs1Type
            output(pipeline.data.RS2_TYPE) := instructionType.rs2Type
            output(pipeline.data.RD_TYPE) := instructionType.rdType

            def updateReg(reg: PipelineData[UInt], value: Int) = {
              output(reg) := value
            }

            fixedRegisters.get(key) match {
              case Some(FixedRegisters(rs1, rs2, rd)) =>
                rs1.foreach(updateReg(pipeline.data.RS1, _))
                rs2.foreach(updateReg(pipeline.data.RS2, _))
                rd.foreach(updateReg(pipeline.data.RD, _))
              case _ =>
            }
          }
        }
        default {
          pipeline.serviceOption[TrapService] foreach { trapHandler =>
            trapHandler.trap(decodeStage, TrapCause.IllegalInstruction(ir))
          }
        }
      }
    }
//    decodeStage
  }

  private def applyAction(stage: Stage, action: Action) = {
    for ((data, value) <- action) {
      val out = stage.output(data)
      out := value
    }
  }

  override def getSupportedOpcodes: Iterable[MaskedLiteral] = {
    decodings.keys
  }
}
object nouse_inst {
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
        import protus.backbone.Config
        val conf = new Config(RV32I)
        val pipeline = new Component with StaticPipeline {
          setDefinitionName("Pipeline")
          val decode = new Stage("ID")


          override val stages = Seq(decode)
          override val passThroughStage: Stage = decode
          override val config: Config = conf
          override val data: StandardPipelineData = new StandardPipelineData(conf)
          override val pipelineComponent: Component = this
        }
//        val decoder = new Decoder(pipeline.decode)
        pipeline.addPlugins(
          Seq(
            new Decoder(pipeline.decode)
          )
        )
        pipeline.build()
        import protus.backbone.Plugin
        pipeline.rework {
          val my = new Component {
            override type RefOwnerType = this.type
            val ins = out(UInt(3 bits))
            ins := 3
          }
          my.setName(getClass.getSimpleName.replace("$", ""))
          pipeline.decode.input(pipeline.data.RS1_DATA) := my.ins.resized
        }
        pipeline
      })
  }.printPruned()
}

