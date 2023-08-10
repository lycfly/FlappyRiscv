package flappyOoO.Decode

import flappyOoO.BaseIsa.RV32I
import flappyOoO.Frontend.if2id_interface
import flappyOoO.{AccessWidth, Config, InstructionFormat, InstructionType, Opcodes, RegisterSource, RegisterType, Utils}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
import spinal.core._
import spinal.core.internals.Literal

import scala.collection.mutable

object UOPs extends SpinalEnum {
  val IDLE, ADD, SUB, SLT, SLTU, AND, OR, XOR = newElement()
  val SLL, SRL, SRA = newElement()
  val LOAD, LOADU, STORE = newElement()
  val JAL, JALR, BEQ, BNE, BLT, BGE, BLTU, BGEU = newElement()
  //SHL, SHR, MULL, MULH, DIV, REM, NPC, AUIPC, RA
  //  val SH1ADD, SH2ADD, SH3ADD = newElement()
  //  val ANDN, CLZ, CPOP, CTZ, MAX, MAXU, MIN, MINU, ORC_B, ORN, REV8, ROL, ROR, SEXT_B, SEXT_H, XNOR, ZEXT_H = newElement()
  //  val CLMUL, CLMULH, CLMULR = newElement()
  //  val BCLR, BEXT, BINV, BSET = newElement()
}

case class ImmediateDecoder(ir: Bits) {
  private def signExtend(data: Bits): Bits = {
    Utils.signExtend(data, 32)
  }
  def i = signExtend(ir(31 downto 20))
  def s = signExtend(ir(31 downto 25) ## ir(11 downto 7))
  def b = signExtend(ir(31) ## ir(7) ## ir(30 downto 25) ## ir(11 downto 8) ## False)
  def u = (ir(31 downto 12) << 12).asUInt
  def j = signExtend(
    ir(31) ## ir(19 downto 12) ## ir(20) ## ir(30 downto 25) ## ir(24 downto 21) ## False
  )
  def csri = ir(31 downto 15).asUInt.resize(32)
}

case class rs_bundle(xlen: Int) extends Bundle {
  val is_used = Bool()
  val source = RegisterSource()
  val kind = RegisterType()
  val width = AccessWidth()
  val index = UInt(log2Up(xlen) bits)
}

case class rd_bundle(xlen: Int) extends Bundle {
  val is_used = Bool()
  val kind = RegisterType()
  val width = AccessWidth()
  val index = UInt(log2Up(xlen) bits)
}


case class micro_op_if(val conf: Config) extends Bundle {
  val rs1 = rs_bundle(conf.ArchRegsNum)
  val rs2 = rs_bundle(conf.ArchRegsNum)
  val rd = rd_bundle(conf.ArchRegsNum)
  val imm = Bits(conf.xlen bits)
  val op = UOPs()
  val pc = UInt(32 bits)
}

case class rg_handle(
                      val is_used: Boolean = true,
                      val source: SpinalEnumElement[RegisterSource.type] = RegisterSource.REGISTER,
                      val kind: SpinalEnumElement[RegisterType.type] = RegisterType.GPR,
                      val width: SpinalEnumElement[AccessWidth.type] = AccessWidth.W
                    ) {
  def is_used_default(): Boolean = {
    is_used
  }
  def source_is_default(): Boolean = {
    source == RegisterSource.REGISTER
  }
  def kind_is_default(): Boolean = {
    kind == RegisterType.GPR
  }
  def width_is_default(): Boolean = {
    width == AccessWidth.W
  }
}

case class uop_handle(
                       val rs1: rg_handle = rg_handle(), val rs2: rg_handle= rg_handle(), rd: rg_handle= rg_handle()
                     ) {
}

case class decoder()(implicit conf: Config) extends Component {
  type Action = mutable.Map[SpinalEnumElement[UOPs.type], uop_handle]
  noIoPrefix()
  val io = new Bundle {
    val if2id = slave(if2id_interface(conf))
    val uop = out(new micro_op_if(conf))
  }
  val instructionTypes = mutable.Map[MaskedLiteral, InstructionType]()
  val decodings = mutable.Map[MaskedLiteral, Action]()
  val ir = io.if2id.instr_data
  val immDecoder = ImmediateDecoder(ir.asBits)

  def addDecoding(
                   opcode: MaskedLiteral,
                   instr_type: InstructionType,
                   action: Action
                 ): Unit = {
    assert(!instructionTypes.contains(opcode), s"Multiple instruction types set for $opcode")
    instructionTypes(opcode) = instr_type
    decodings(opcode) = action
  }

  def addOPs_RvI(config: Config): Unit = {
    addDecoding(Opcodes.ADD, InstructionType.R, mutable.Map(UOPs.ADD -> uop_handle()))
    addDecoding(Opcodes.SUB, InstructionType.R, mutable.Map(UOPs.SUB -> uop_handle()))
    addDecoding(Opcodes.SLT, InstructionType.R, mutable.Map(UOPs.SLT -> uop_handle()))
    addDecoding(Opcodes.SLTU, InstructionType.R, mutable.Map(UOPs.SLTU -> uop_handle()))
    addDecoding(Opcodes.XOR, InstructionType.R, mutable.Map(UOPs.XOR -> uop_handle()))
    addDecoding(Opcodes.OR, InstructionType.R, mutable.Map(UOPs.OR -> uop_handle()))
    addDecoding(Opcodes.AND, InstructionType.R, mutable.Map(UOPs.AND -> uop_handle()))

    addDecoding(Opcodes.ADDI, InstructionType.I, mutable.Map(UOPs.ADD -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.SLTI, InstructionType.I, mutable.Map(UOPs.SLT -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.SLTIU, InstructionType.I, mutable.Map(UOPs.SLTU -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.XORI, InstructionType.I, mutable.Map(UOPs.XOR -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.ORI, InstructionType.I, mutable.Map(UOPs.OR -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.ANDI, InstructionType.I, mutable.Map(UOPs.AND -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))

    addDecoding(Opcodes.LUI, InstructionType.U, mutable.Map(UOPs.ADD -> uop_handle(rs1 = rg_handle(is_used = false), rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.AUIPC, InstructionType.U, mutable.Map(UOPs.ADD -> uop_handle(rs1 = rg_handle(is_used = false), rs2 = rg_handle(source = RegisterSource.IMM))))
    //shift
    addDecoding(Opcodes.SLL, InstructionType.R, mutable.Map(UOPs.SLL -> uop_handle()))
    addDecoding(Opcodes.SRL, InstructionType.R, mutable.Map(UOPs.SRL -> uop_handle()))
    addDecoding(Opcodes.SRA, InstructionType.R, mutable.Map(UOPs.SRA -> uop_handle()))
    addDecoding(Opcodes.SLLI, InstructionType.I, mutable.Map(UOPs.SLL -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.SRLI, InstructionType.I, mutable.Map(UOPs.SRL -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.SRAI, InstructionType.I, mutable.Map(UOPs.SRA -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    // load/store
    addDecoding(Opcodes.LW, InstructionType.I, mutable.Map(UOPs.LOAD -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.LH, InstructionType.I, mutable.Map(UOPs.LOAD -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM), rd = rg_handle(width = AccessWidth.H))))
    addDecoding(Opcodes.LB, InstructionType.I, mutable.Map(UOPs.LOAD -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM), rd = rg_handle(width = AccessWidth.B))))
    addDecoding(Opcodes.LHU, InstructionType.I, mutable.Map(UOPs.LOADU -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM), rd = rg_handle(width = AccessWidth.H))))
    addDecoding(Opcodes.LBU, InstructionType.I, mutable.Map(UOPs.LOADU -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM), rd = rg_handle(width = AccessWidth.B))))
    addDecoding(Opcodes.SW, InstructionType.S, mutable.Map(UOPs.STORE -> uop_handle()))
    addDecoding(Opcodes.SH, InstructionType.S, mutable.Map(UOPs.STORE -> uop_handle(rs2 = rg_handle(width = AccessWidth.H))))
    addDecoding(Opcodes.SB, InstructionType.S, mutable.Map(UOPs.STORE -> uop_handle(rs2 = rg_handle(width = AccessWidth.B))))
    // branch
    addDecoding(Opcodes.JAL, InstructionType.J, mutable.Map(UOPs.JAL -> uop_handle(rs1 = rg_handle(source = RegisterSource.PC), rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.JALR, InstructionType.I, mutable.Map(UOPs.JALR -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
    addDecoding(Opcodes.BEQ, InstructionType.B, mutable.Map(UOPs.BEQ -> uop_handle()))
    addDecoding(Opcodes.BNE, InstructionType.B, mutable.Map(UOPs.BNE -> uop_handle()))
    addDecoding(Opcodes.BLT, InstructionType.B, mutable.Map(UOPs.BLT -> uop_handle()))
    addDecoding(Opcodes.BGE, InstructionType.B, mutable.Map(UOPs.BGE -> uop_handle()))
    addDecoding(Opcodes.BLTU, InstructionType.B, mutable.Map(UOPs.BLTU -> uop_handle()))
    addDecoding(Opcodes.BGEU, InstructionType.B, mutable.Map(UOPs.BGEU -> uop_handle()))


  }

  def addOps_RvZicsr(config: Config): Unit = {
    if(config.RvZicsr){
      addDecoding(Opcodes.CSRRW, InstructionType.I, mutable.Map(UOPs.newElement("CSRRW") -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
      addDecoding(Opcodes.CSRRS, InstructionType.I, mutable.Map(UOPs.newElement("CSRRS") -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
      addDecoding(Opcodes.CSRRC, InstructionType.I, mutable.Map(UOPs.newElement("CSRRC") -> uop_handle(rs2 = rg_handle(source = RegisterSource.IMM))))
      addDecoding(Opcodes.CSRRWI, InstructionType.CSRI, mutable.Map(UOPs.newElement("CSRRWI") -> uop_handle(rs1 = rg_handle(is_used = false), rs2 = rg_handle(source = RegisterSource.IMM))))
      addDecoding(Opcodes.CSRRSI, InstructionType.CSRI, mutable.Map(UOPs.newElement("CSRRSI") -> uop_handle(rs1 = rg_handle(is_used = false), rs2 = rg_handle(source = RegisterSource.IMM))))
      addDecoding(Opcodes.CSRRCI, InstructionType.CSRI, mutable.Map(UOPs.newElement("CSRRCI") -> uop_handle(rs1 = rg_handle(is_used = false), rs2 = rg_handle(source = RegisterSource.IMM))))
    }
  }

  def addOPs_RvM(config: Config): Unit = {
    if(config.Rvm){
      addDecoding(Opcodes.MUL, InstructionType.R, mutable.Map(UOPs.newElement("MUL") -> uop_handle()))
      addDecoding(Opcodes.MULH, InstructionType.R, mutable.Map(UOPs.newElement("MULH") -> uop_handle()))
      addDecoding(Opcodes.MULHSU, InstructionType.R, mutable.Map(UOPs.newElement("MULHSU") -> uop_handle()))
      addDecoding(Opcodes.MULHU, InstructionType.R, mutable.Map(UOPs.newElement("MULHU") -> uop_handle()))

      addDecoding(Opcodes.DIV, InstructionType.R, mutable.Map(UOPs.newElement("DIV") -> uop_handle()))
      addDecoding(Opcodes.DIVU, InstructionType.R, mutable.Map(UOPs.newElement("DIVU") -> uop_handle()))
      addDecoding(Opcodes.REM, InstructionType.R, mutable.Map(UOPs.newElement("REM") -> uop_handle()))
      addDecoding(Opcodes.REMU, InstructionType.R, mutable.Map(UOPs.newElement("REMU") -> uop_handle()))
    }
  }

  //  def build(): Unit = {
  addOPs_RvI(conf)
  addOPs_RvM(conf)
  addOps_RvZicsr(conf)

  val default_handle = rg_handle()

  io.uop.rs1.index := ir(19 downto 15).asUInt
  io.uop.rs2.index := ir(24 downto 20).asUInt
  io.uop.rd.index := ir(11 downto 7).asUInt
  io.uop.pc := io.if2id.instr_pc
  io.uop.imm := 0
  io.uop.op := UOPs.IDLE
  io.uop.rs1.is_used := Bool(default_handle.is_used)
  io.uop.rs1.source :=  default_handle.source
  io.uop.rs1.kind := default_handle.kind
  io.uop.rs1.width := default_handle.width
  io.uop.rs2.is_used := Bool(default_handle.is_used)
  io.uop.rs2.source := default_handle.source
  io.uop.rs2.kind := default_handle.kind
  io.uop.rs2.width := default_handle.width
  io.uop.rd.is_used := Bool(default_handle.is_used)
  io.uop.rd.kind := default_handle.kind
  io.uop.rd.width := default_handle.width
  switch(ir) {
    for ((key, action) <- decodings) {
      is(key) {

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
          case InstructionFormat.R => U(0, conf.xlen bits)
          case InstructionFormat.CSRI => immDecoder.csri
        }

        io.uop.imm := imm.asBits

        if(!action.values.head.rs1.is_used_default()) io.uop.rs1.is_used := Bool(action.values.head.rs1.is_used)
        if(!action.values.head.rs1.source_is_default()) io.uop.rs1.source :=  action.values.head.rs1.source
        if(!action.values.head.rs1.kind_is_default()) io.uop.rs1.kind := action.values.head.rs1.kind
        if(!action.values.head.rs1.width_is_default()) io.uop.rs1.width := action.values.head.rs1.width

        if (!action.values.head.rs2.is_used_default()) io.uop.rs2.is_used := Bool(action.values.head.rs2.is_used)
        if (!action.values.head.rs2.source_is_default()) io.uop.rs2.source := action.values.head.rs2.source
        if (!action.values.head.rs2.kind_is_default()) io.uop.rs2.kind := action.values.head.rs2.kind
        if (!action.values.head.rs2.width_is_default()) io.uop.rs2.width := action.values.head.rs2.width

        if (!action.values.head.rd.is_used_default()) io.uop.rd.is_used := Bool(action.values.head.rd.is_used)
        if (!action.values.head.rd.kind_is_default()) io.uop.rd.kind := action.values.head.rd.kind
        if (!action.values.head.rd.width_is_default()) io.uop.rd.width := action.values.head.rd.width

        io.uop.op := action.keys.head

      }
    }
//    default {
//      io.uop.flatten.foreach(a => {
//        a.assignDontCare()
//      })
//      //          pipeline.serviceOption[TrapService] foreach { trapHandler =>
//      //            trapHandler.trap(decodeStage, TrapCause.IllegalInstruction(ir))
//      //          }
//    }
  }
  when(io.uop.rd.index === 0) {
    io.uop.rd.is_used := False
  }

}

object decoder_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = true,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate({
        implicit val config = new Config(RV32I)
        val dec = decoder()
        dec
      }
      )
  }.printPruned()
}