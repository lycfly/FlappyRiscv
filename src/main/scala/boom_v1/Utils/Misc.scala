package boom_v1.Utils
import boom_v1.MicroOp
import boom_v1.exec.BrResolutionInfo
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.math.log
object PopCountAtLeast {
  private def two(x: UInt): (Bool, Bool) = x.getWidth match {
    case 1 => (x.asBool, False)
    case n =>
      val half = x.getWidth / 2
      val (leftOne, leftTwo) = two(x(half - 1 downto  0))
      val (rightOne, rightTwo) = two(x(x.getWidth - 1 downto half))
      (leftOne || rightOne, leftTwo || rightTwo || (leftOne && rightOne))
  }
  def apply(x: UInt, n: Int): Bool = n match {
    case 0 => True
    case 1 => x.orR
    case 2 => two(x)._2
    case 3 => CountOne(x) >= n
  }
}

// Shift a register over by one bit, wrapping the top bit around to the bottom
// (XOR'ed with a new-bit), and evicting a bit at index HLEN.
// This is used to simulate a longer HLEN-width shift register that is folded
// down to a compressed CLEN.
object PerformCircularShiftRegister
{
  def apply(csr: UInt, new_bit: Bool, evict_bit: Bool, hlen: Int, clen: Int): UInt =
  {
    val carry = csr(clen-1)
    val newval = Cat(csr, new_bit ^ carry).asUInt ^ (evict_bit.asUInt << U(hlen % clen))
    newval
  }
}

//do two masks have at least 1 bit match?
object maskMatch
{
  def apply(msk1: UInt, msk2: UInt): Bool = (msk1 & msk2) =/= U(0)
}
object IsKilledByBranch
{
  def apply(brinfo: BrResolutionInfo, uop: MicroOp): Bool =
  {
    return (brinfo.valid &&
      brinfo.mispredict &&
      maskMatch(brinfo.mask, uop.br_mask))
  }

  def apply(brinfo: BrResolutionInfo, uop_mask: UInt): Bool =
  {
    return (brinfo.valid &&
      brinfo.mispredict &&
      maskMatch(brinfo.mask, uop_mask))
  }
}
object GetNewBrMask
{
  def apply(brinfo: BrResolutionInfo, uop: MicroOp): UInt =
  {
    return Mux(brinfo.valid, (uop.br_mask & ~brinfo.mask),
      uop.br_mask)
  }
  def apply(brinfo: BrResolutionInfo, br_mask: UInt): UInt =
  {
    return Mux(brinfo.valid, (br_mask & ~brinfo.mask),
      br_mask)
  }
}
// Decrement the input "value", wrapping it if necessary.
object WrapSub
{
  // "n" is the number of increments, so we wrap to n-1.
  def apply(value: UInt, amt: Int, n: Int): UInt =
  {
    if (isPow2(n))
    {
      (value - U(amt))(log2Up(n)-1 downto 0)
    }
    else
    {
      val v = Cat(U(0,1 bits), value)
      val b = Cat(U(0,1 bits), U(amt))
      Mux(value >= U(amt),
        value - U(amt),
        U(n) - (U(amt) - value))
    }
  }
}

// Increment the input "value", wrapping it if necessary.
object WrapInc
{
  // "n" is the number of increments, so we wrap at n-1.
  def apply(value: UInt, n: Int): UInt =
  {
    if (isPow2(n))
    {
      (value + U(1))(log2Up(n)-1 downto 0)
    }
    else
    {
      val wrap = (value === U(n-1))
      Mux(wrap, U(0), value + U(1))
    }
  }
}
// Decrement the input "value", wrapping it if necessary.
object WrapDec
{
  // "n" is the number of increments, so we wrap at n-1.
  def apply(value: UInt, n: Int): UInt =
  {
    if (isPow2(n))
    {
      (value - U(1))(log2Up(n)-1 downto 0)
    }
    else
    {
      val wrap = (value === U(0))
      Mux(wrap, U(n-1), value - U(1))
    }
  }
}

object LFSR16 {
  /** Generates a 16-bit linear feedback shift register, returning the register contents.
    * May be useful for generating a pseudorandom sequence.
    *
    * @param increment optional control to gate when the LFSR updates.
    */
  def apply(increment: Bool = True): UInt = {
    val width = 16
    val lfsr = RegInit(UInt(width bits)) init(1)
    when (increment) { lfsr := Cat(lfsr(0)^lfsr(2)^lfsr(3)^lfsr(5), lfsr(width-1 downto 1)) }
    lfsr
  }
}

object Str
{
  def apply(s: String): UInt = {
    var i = BigInt(0)
    require(s.forall(validChar _))
    for (c <- s)
      i = (i << 8) | c
    U(i, s.length*8)
  }
  def apply(x: Char): UInt = {
    require(validChar(x))
    U(x.toInt, 8 bits)
  }
  def apply(x: UInt): UInt = apply(x, 10)
  def apply(x: UInt, radix: Int): UInt = {
    val rad = U(radix)
    val w = x.getWidth
    require(w > 0)

    var q = x
    var s = digit(q % rad)
    for (i <- 1 until scala.math.ceil(log(2)/log(radix)*w).toInt) {
      q = q / rad
      s = Cat(Mux(Bool(radix == 10) && q === U(0), Str(' '), digit(q % rad)), s).asUInt
    }
    s
  }
  def apply(x: SInt): UInt = apply(x, 10)
  def apply(x: SInt, radix: Int): UInt = {
    val neg = x < S(0)
    val abs = x.abs
    if (radix != 10) {
      Cat(Mux(neg, Str('-'), Str(' ')), Str(abs, radix)).asUInt
    } else {
      val rad = U(radix)
      val w = abs.getWidth
      require(w > 0)

      var q = abs
      var s = digit(q % rad)
      var needSign = neg
      for (i <- 1 until scala.math.ceil(log(2)/log(radix)*w).toInt) {
        q = q / rad
        val placeSpace = q === U(0)
        val space = Mux(needSign, Str('-'), Str(' '))
        needSign = needSign && !placeSpace
        s = Cat(Mux(placeSpace, space, digit(q % rad)), s).asUInt
      }
      Cat(Mux(needSign, Str('-'), Str(' ')), s).asUInt
    }
  }

  private def digit(d: UInt): UInt = Mux(d < U(10), Str('0')+d, Str(('a'-10).toChar)+d)(7 downto 0)
  private def validChar(x: Char) = x == (x & 0xFF)
}