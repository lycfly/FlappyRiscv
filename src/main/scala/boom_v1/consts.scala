package boom_v1

import EasonLib.Utils.Enum.EasonEnum
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

trait ScalarOpConstants {
  val X = BitPat("b?")
  val Y = BitPat("b1")
  val N = BitPat("b0")

  //************************************
  // Extra Constants


  //************************************
  // Control Signals

  val s_invalid :: s_valid_1 :: s_valid_2 :: Nil = Enum(UInt(), 3)

  // PC Select Signal
  val PC_PLUS4 = UInt(0, 2) // PC + 4
  val PC_BRJMP = UInt(1, 2) // brjmp_target
  val PC_JALR = UInt(2, 2) // jump_reg_target





  // Register File Write Enable Signal
  val REN_0 = Bool(false)
  val REN_1 = Bool(true)

  // Is 32b Word or 64b Doubldword?
  val SZ_DW = 1
  val DW_X = Bool(true) // Bool(xLen==64)
  val DW_32 = Bool(false)
  val DW_64 = Bool(true)
  val DW_XPR = Bool(true) // Bool(xLen==64)

  // Memory Enable Signal
  val MEN_0 = Bool(false)
  val MEN_1 = Bool(true)
  val MEN_X = Bool(false)




  // Decode Stage Control Signals
  val RT_FIX = UInt(0, 2)
  val RT_FLT = UInt(1, 2)
  val RT_PAS = UInt(3, 2) // pass-through (pop1 := lrs1, etc)
  val RT_X = UInt(2, 2) // not-a-register (but shouldn't get a busy-bit, etc.)
  // TODO rename RT_NAR
}
object RISCVConstants
{
  // abstract out instruction decode magic numbers
  val RD_MSB  = 11
  val RD_LSB  = 7
  val RS1_MSB = 19
  val RS1_LSB = 15
  val RS2_MSB = 24
  val RS2_LSB = 20
  val RS3_MSB = 31
  val RS3_LSB = 27

  val CSR_ADDR_MSB = 31
  val CSR_ADDR_LSB = 20
  val CSR_ADDR_SZ = 12

  // location of the fifth bit in the shamt (for checking for illegal ops for SRAIW,etc.)
  val SHAMT_5_BIT = 25
  val LONGEST_IMM_SZ = 20
  val X0 = U(0)
  val RA = U(1) // return address register

  // memory consistency model
  // The C/C++ atomics MCM requires that two loads to the same address maintain program order.
  // The Cortex A9 does NOT enforce load/load ordering (which leads to buggy behavior).
  val MCM_ORDER_DEPENDENT_LOADS = false

  val jal_opc = U(0x6f)
  val jalr_opc = U(0x67)
  def GetUop(inst: UInt): UInt = inst(6 downto 0)
  def GetRd (inst: UInt): UInt = inst(RD_MSB downto RD_LSB)
  def GetRs1(inst: UInt): UInt = inst(RS1_MSB downto RS1_LSB)
  def IsCall(inst: UInt): Bool = (inst === Opcodes.JAL ||
    inst === Opcodes.JALR) && GetRd(inst) === RA
  def IsReturn(inst: UInt): Bool = GetUop(inst) === jalr_opc && GetRd(inst) === X0 && GetRs1(inst) === RA

  def ComputeBranchTarget(pc: UInt, inst: UInt, xlen: Int, coreInstBytes: Int): UInt =
  {
    val b_imm32 = Cat(Fill(20,inst(31)), inst(7), inst(30,25), inst(11,8), UInt(0,1))
    ((pc + Sext(b_imm32, xlen)).asSInt & SInt(-coreInstBytes)).asUInt
  }
  def ComputeJALTarget(pc: UInt, inst: UInt, xlen: Int, coreInstBytes: Int): UInt =
  {
    val j_imm32 = Cat(Fill(12,inst(31)), inst(19,12), inst(20), inst(30,25), inst(24,21), UInt(0,1))
    ((pc + Sext(j_imm32, xlen)).asSInt & SInt(-coreInstBytes)).asUInt
  }
}



object Opcodes {
  val ADD = M"0000000----------000-----0110011"
  val SUB = M"0100000----------000-----0110011"
  val SLT = M"0000000----------010-----0110011"
  val SLTU = M"0000000----------011-----0110011"
  val XOR = M"0000000----------100-----0110011"
  val OR = M"0000000----------110-----0110011"
  val AND = M"0000000----------111-----0110011"
  val ADDI = M"-----------------000-----0010011"
  val SLTI = M"-----------------010-----0010011"
  val SLTIU = M"-----------------011-----0010011"
  val XORI = M"-----------------100-----0010011"
  val ORI = M"-----------------110-----0010011"
  val ANDI = M"-----------------111-----0010011"
  val LUI = M"-------------------------0110111"
  val AUIPC = M"-------------------------0010111"

  val SLLI = M"0000000----------001-----0010011"
  val SRLI = M"0000000----------101-----0010011"
  val SRAI = M"0100000----------101-----0010011"
  val SLL = M"0000000----------001-----0110011"
  val SRL = M"0000000----------101-----0110011"
  val SRA = M"0100000----------101-----0110011"

  val JAL = M"-------------------------1101111"
  val JALR = M"-----------------000-----1100111"
  val BEQ = M"-----------------000-----1100011"
  val BNE = M"-----------------001-----1100011"
  val BLT = M"-----------------100-----1100011"
  val BGE = M"-----------------101-----1100011"
  val BLTU = M"-----------------110-----1100011"
  val BGEU = M"-----------------111-----1100011"

  val LB = M"-----------------000-----0000011"
  val LH = M"-----------------001-----0000011"
  val LW = M"-----------------010-----0000011"
  val LBU = M"-----------------100-----0000011"
  val LHU = M"-----------------101-----0000011"
  val SB = M"-----------------000-----0100011"
  val SH = M"-----------------001-----0100011"
  val SW = M"-----------------010-----0100011"

  val CSRRW = M"-----------------001-----1110011"
  val CSRRS = M"-----------------010-----1110011"
  val CSRRC = M"-----------------011-----1110011"
  val CSRRWI = M"-----------------101-----1110011"
  val CSRRSI = M"-----------------110-----1110011"
  val CSRRCI = M"-----------------111-----1110011"

  val ECALL = M"00000000000000000000000001110011"
  val EBREAK = M"00000000000100000000000001110011"
  val MRET = M"00110000001000000000000001110011"

  val MUL = M"0000001----------000-----0110011"
  val MULH = M"0000001----------001-----0110011"
  val MULHSU = M"0000001----------010-----0110011"
  val MULHU = M"0000001----------011-----0110011"
  val DIV = M"0000001----------100-----0110011"
  val DIVU = M"0000001----------101-----0110011"
  val REM = M"0000001----------110-----0110011"
  val REMU = M"0000001----------111-----0110011"
}