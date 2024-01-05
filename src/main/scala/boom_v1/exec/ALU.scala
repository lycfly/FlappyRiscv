package boom_v1.exec

import EasonLib.Utils.Enum.EasonEnum
import boom_v1.ScalarOpConstants._
import boom_v1.{MaskedDC, Parameters}
import spinal.core._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

// See LICENSE.SiFive for license details.
// See LICENSE.Berkeley for license details.
object ALUFN extends EasonEnum{
  val FN_ADD = newElement()   // 0
  val FN_SL = newElement()    // 1
  val FN_SEQ = newElement()   // 2
  val FN_SNE = newElement()   // 3
  val FN_XOR = newElement()   // 4
  val FN_SR = newElement()    // 5
  val FN_OR = newElement()    // 6
  val FN_AND = newElement()   // 7
  val FN_SUB = newElement()   // 10
  val FN_SRA = newElement()   // 11
  val FN_SLT = newElement()   // 12
  val FN_SGE = newElement()   // 13
  val FN_SLTU = newElement()  // 14
  val FN_SGEU = newElement()  // 15
  defaultEncoding = SpinalEnumEncoding("staticEncoding")(
    FN_ADD -> 0,
    FN_SL ->  1,
    FN_SEQ -> 2,
    FN_SNE -> 3,
    FN_XOR -> 4,
    FN_SR ->  5,
    FN_OR ->  6,
    FN_AND -> 7,
    FN_SUB -> 10,
    FN_SRA -> 11,
    FN_SLT -> 12,
    FN_SGE -> 13,
    FN_SLTU -> 14,
    FN_SGEU -> 15,
  )
}

object ALU
{
//  val FN_ADD = newElement()
//  val FN_SL = newElement()
//  val FN_SEQ = newElement()
//  val FN_SNE = newElement()
//  val FN_XOR = newElement()
//  val FN_SR = newElement()
//  val FN_OR = newElement()
//  val FN_AND = newElement()
//  val FN_SUB = newElement()
//  val FN_SRA = newElement()
//  val FN_SLT = newElement()
//  val FN_SGE = newElement()
//  val FN_SLTU = newElement()
//  val FN_SGEU = newElement()
  import ALUFN._
  def SZ_ALU_FN = ALUFN().getBitsWidth
  def FN_X = MaskedDC(SZ_ALU_FN)

  def FN_DIV  = FN_XOR
  def FN_DIVU = FN_SR
  def FN_REM  = FN_OR
  def FN_REMU = FN_AND

  def FN_MUL    = FN_ADD
  def FN_MULH   = FN_SL
  def FN_MULHSU = FN_SLT
  def FN_MULHU  = FN_SLTU

  def isMulFN(fn: UInt, cmp: UInt) = fn(1 downto 0) === cmp(1 downto 0)
  def isSub(cmd: UInt) = cmd(3)
  def isCmp(cmd: UInt) = cmd === FN_SEQ.toU() || cmd === FN_SNE.toU() || cmd >= FN_SLT.toU()
  def cmpUnsigned(cmd: UInt) = cmd(1)
  def cmpInverted(cmd: UInt) = cmd(0)
  def cmpEq(cmd: UInt) = !cmd(3)
}
//import ALU._

class ALU(implicit p: Parameters) extends Module{
  import ALUFN._
  import ALU._
  val io = new Bundle {
    val dw = in Bits(SZ_DW bits)
    val fn = in Bits(SZ_ALU_FN bits)
    val in2 = in UInt(p.xLen bits)
    val in1 = in UInt(p.xLen bits)
    val alu_out = out UInt(p.xLen bits)
    val adder_out = out UInt(p.xLen bits)
    val cmp_out = out Bool()
  }

  // ADD, SUB
  val in2_inv = Mux(isSub(io.fn.asUInt), ~io.in2, io.in2)
  val in1_xor_in2 = io.in1 ^ in2_inv
  io.adder_out := io.in1 + in2_inv + isSub(io.fn.asUInt).asUInt

  // SLT, SLTU
  io.cmp_out := cmpInverted(io.fn.asUInt) ^
    Mux(cmpEq(io.fn.asUInt), in1_xor_in2 === U(0),
      Mux(io.in1(p.xLen-1) === io.in2(p.xLen-1), io.adder_out(p.xLen-1),
        Mux(cmpUnsigned(io.fn.asUInt), io.in2(p.xLen-1), io.in1(p.xLen-1))))

  // SLL, SRL, SRA
  val (shamt, shin_r) =
    if (p.xLen == 32) (io.in2(4 downto 0), io.in1)
    else {
      require(p.xLen == 64)
      val shin_hi_32 = Repeat(isSub(io.fn.asUInt) && io.in1(31), 32).asUInt
      val shin_hi = Mux(io.dw === DW_64, io.in1(63 downto 32), shin_hi_32)
      val shamt = Cat(io.in2(5) & (io.dw === DW_64), io.in2(4 downto 0)).asUInt
      (shamt, Cat(shin_hi, io.in1(31 downto 0)).asUInt)
    }
  val shin = Mux(io.fn === FN_SR.asBits  || io.fn === FN_SRA.asBits, shin_r, Reverse(shin_r))
  val shout_r = (Cat(isSub(io.fn.asUInt) & shin(p.xLen-1), shin).asSInt >> shamt)(p.xLen-1 downto 0).asUInt
  val shout_l = Reverse(shout_r)
  val shout = Mux(io.fn === FN_SR.asBits || io.fn === FN_SRA.asBits, shout_r, U(0)) |
    Mux(io.fn === FN_SL,                     shout_l, U(0))

  // AND, OR, XOR
  val logic = Mux(io.fn === FN_XOR.asBits || io.fn === FN_OR.asBits, in1_xor_in2, U(0)) |
    Mux(io.fn === FN_OR.asBits || io.fn === FN_AND.asBits, io.in1 & io.in2, U(0))
  val shift_logic = (isCmp(io.fn.asUInt) && io.cmp_out).asUInt | logic | shout
  val out_alu = Mux(io.fn === FN_ADD.asBits || io.fn === FN_SUB.asBits, io.adder_out, shift_logic)
  io.alu_out := out_alu
  if (p.xLen > 32) {
    require(p.xLen == 64)
    when (io.dw === DW_32) { io.alu_out := Cat(Repeat(out_alu(31),32), out_alu(31 downto 0)).asUInt }
  }
}

