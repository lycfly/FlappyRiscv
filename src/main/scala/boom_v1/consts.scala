package boom_v1

import EasonLib.Utils.Enum.EasonEnum
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
object ScalarOpConstants extends ScalarOpConstants

object BitMap {
  def apply(str: String) = {
    val strRmb = str.replace("b", "")
    val strcleaned = strRmb.replace("?", "-")
    val strCleaned = strcleaned.replace("_", "")

    for (c <- strCleaned) assert(c == '1' || c == '0' || c == '-', s"""M"$str" is not correctly formated.""")

    val careAbout = strCleaned.map(c => if (c == '-') '0' else '1')
    val value = strCleaned.map(c => if (c == '-') '0' else c)
    new MaskedLiteral(BigInt(value, 2), BigInt(careAbout, 2), strCleaned.length())
  }
}

//class BitMap(override val value: BigInt, override val careAbout: BigInt, override val width: Int) extends MaskedLiteral(value, careAbout, width)

object MaskedDC {
  def apply(width: Int) = MaskedLiteral(("-" * width))
}

trait ScalarOpConstants {
  val X = MaskedLiteral("-")
  val Y = MaskedLiteral("1")
  val N = MaskedLiteral("0")

  //************************************
  // Extra Constants
  val uopX = MaskedDC(UOPs().getBitsWidth)
  val FU_X = MaskedDC(FUType().getBitsWidth)
  val IS_X = MaskedDC(IMMType().getBitsWidth)
  val MSK_X = MaskedDC(MSKType().getBitsWidth)
  val CSR_X = MaskedDC(CSRType().getBitsWidth)

  //************************************
  // Control Signals

//  val s_invalid :: s_valid_1 :: s_valid_2 :: Nil = Enum(UInt(), 3)
  val s_invalid = U(0, 2 bits)
  val s_valid_1 = U(1, 2 bits)
  val s_valid_2 = U(2, 2 bits)

  // PC Select Signal
  val PC_PLUS4 = U(0, 2 bits) // PC + 4
  val PC_BRJMP = U(1, 2 bits) // brjmp_target
  val PC_JALR = U(2, 2 bits) // jump_reg_target

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
  val RT_FIX = U(0, 2 bits)
  val RT_FLT = U(1, 2 bits)
  val RT_PAS = U(3, 2 bits) // pass-through (pop1 := lrs1, etc)
//  val RT_X = U(2, 2 bits) // not-a-register (but shouldn't get a busy-bit, etc.)
val RT_X = MaskedLiteral("10") // not-a-register (but shouldn't get a busy-bit, etc.)

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
  def IsCall(inst: UInt): Bool = (inst === Instructions.JAL ||
    inst === Instructions.JALR) && GetRd(inst) === RA
  def IsReturn(inst: UInt): Bool = GetUop(inst) === jalr_opc && GetRd(inst) === X0 && GetRs1(inst) === RA

  def ComputeBranchTarget(pc: UInt, inst: UInt, xlen: Int, coreInstBytes: Int): UInt =
  {
    val b_imm32 = Cat((inst(31)#*20), inst(7), inst(30 downto 25), inst(11 downto 8), U(0,1 bits))
    ((pc.asSInt + (b_imm32.asSInt.resize(xlen))) & S(-coreInstBytes, xlen bits)).asUInt
  }
  def ComputeJALTarget(pc: UInt, inst: UInt, xlen: Int, coreInstBytes: Int): UInt =
  {
    val j_imm32 = Cat((inst(31)#*12), inst(19 downto 12), inst(20), inst(30 downto 25), inst(24 downto 21), U(0,1 bits))
    ((pc.asSInt + (j_imm32.asSInt.resize(xlen))) & S(-coreInstBytes, xlen bits)).asUInt
  }
}


object MemoryOpConstants extends MemoryOpConstants
trait MemoryOpConstants {
  val NUM_XA_OPS = 9
  val M_SZ      = 5
  def M_X       = BitMap("b?????");

  object MEMOP extends SpinalEnum {
    val M_XRD = newElement(); // int load
    val M_XWR = newElement(); // int store
    val M_PFR = newElement(); // prefetch with intent to read
    val M_PFW = newElement(); // prefetch with intent to write
    val M_XA_SWAP = newElement();
    val M_FLUSH_ALL = newElement() // flush all lines
    val M_XLR = newElement();
    val M_XSC = newElement();
    val M_XA_ADD = newElement();
    val M_XA_XOR = newElement();
    val M_XA_OR = newElement();
    val M_XA_AND = newElement();
    val M_XA_MIN = newElement();
    val M_XA_MAX = newElement();
    val M_XA_MINU = newElement();
    val M_XA_MAXU = newElement();
    val M_FLUSH = newElement() // write back dirty data and cede R/W permissions
    val M_PRODUCE = newElement() // write back dirty data and cede W permissions
    val M_CLEAN = newElement() // write back dirty data and retain R/W permissions
  }
  def isAMO(cmd: UInt) = cmd(3) || cmd === MEMOP.M_XA_SWAP.asBits.asUInt
  def isPrefetch(cmd: UInt) = cmd === MEMOP.M_PFR.asBits.asUInt || cmd === MEMOP.M_PFW.asBits.asUInt
  def isRead(cmd: UInt) = cmd === MEMOP.M_XRD.asBits.asUInt || cmd === MEMOP.M_XLR.asBits.asUInt || cmd === MEMOP.M_XSC.asBits.asUInt || isAMO(cmd)
  def isWrite(cmd: UInt) = cmd === MEMOP.M_XWR.asBits.asUInt || cmd === MEMOP.M_XSC.asBits.asUInt || isAMO(cmd)
  def isWriteIntent(cmd: UInt) = isWrite(cmd) || cmd === MEMOP.M_PFW.asBits.asUInt || cmd === MEMOP.M_XLR.asBits.asUInt
}


