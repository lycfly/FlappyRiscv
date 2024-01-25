package boom_v1.Utils
import boom_v1.IMMType._
import boom_v1.MicroOp
import boom_v1.RISCVConstants._
import boom_v1.exec.BrResolutionInfo
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.math.log
object Counter {

  /** Instantiate a [[Counter! counter]] with the specified number of counts.
   */
  def apply(n: Int): Counter = new Counter(start = 0, end = if(n>0) n-1 else 0)

  /** Instantiate a [[Counter! counter]] with the specified number of counts and a gate.
   *
   * @param cond condition that controls whether the counter increments this cycle
   * @param n number of counts before the counter resets
   * @return tuple of the counter value and whether the counter will wrap (the value is at
   * maximum and the condition is true).
   */
  def apply(cond: Bool, n: Int): (UInt, Bool) = {
    val c = new Counter(start = 0, end = if(n>0) n-1 else 0)
    val wrap = False
    when(cond) {
      c.increment()
      wrap := c.willOverflow
    }
    (c.value, wrap)
  }

  /** Creates a counter that steps through a specified range of values.
   *
   * @param r the range of counter values
   * @param enable controls whether the counter increments this cycle
   * @param reset resets the counter to its initial value during this cycle
   * @return tuple of the counter value and whether the counter will wrap (the value is at
   * maximum and the condition is true).
   */
  def apply(r: Range, enable: Bool = True, reset: Bool = False): (UInt, Bool) = {
    val c = new Counter(start = r.start, end = r.end)
    val wrap = False

    when(reset) {
      c.clear()
    }.elsewhen(enable) {
      c.increment()
      wrap := c.willOverflow
    }

    (c.value, wrap)
  }
}
object log2Floor {
  def apply(in: BigInt): Int = log2Up(in) - (if (isPow2(in)) 0 else 1)
  def apply(in: Int):    Int = apply(BigInt(in))
}
class Field[T]

object MuxT {
  def apply[T <: Data, U <: Data](cond: Bool, con: (T, U), alt: (T, U)): (T, U) =
    (Mux(cond, con._1, alt._1), Mux(cond, con._2, alt._2))

  def apply[T <: Data, U <: Data, W <: Data](cond: Bool, con: (T, U, W), alt: (T, U, W)): (T, U, W) =
    (Mux(cond, con._1, alt._1), Mux(cond, con._2, alt._2), Mux(cond, con._3, alt._3))

  def apply[T <: Data, U <: Data, W <: Data, X <: Data](cond: Bool, con: (T, U, W, X), alt: (T, U, W, X)): (T, U, W, X) =
    (Mux(cond, con._1, alt._1), Mux(cond, con._2, alt._2), Mux(cond, con._3, alt._3), Mux(cond, con._4, alt._4))
}
object countLeadingZeros
{
  def apply(in: UInt): UInt = PriorityEncoder(in.asBools.reverse)
}

object Fill{
  def apply[T <: Data](x: Int, data: T): Bits = {
    (data #* x)
  }
}
object MuxLookup {

  /** Creates a cascade of n Muxs to search for a key value.
    *
    * @example {{{
    * MuxLookup(idx, default, Seq(0.U -> a, 1.U -> b))
    * }}}
    *
    * @param key a key to search for
    * @param default a default value if nothing is found
    * @param mapping a sequence to search of keys and values
    * @return the value found or the default if not
    */

  def apply[S <: UInt, T <: Data](key: S, default: T, mapping: Seq[(S, T)]): T =
    do_apply(key, default, mapping)

  def do_applyEnum[S <: SpinalEnum, T <: Data](
                                              key:     SpinalEnumCraft[S],
                                              default: T,
                                              mapping: Seq[(SpinalEnumElement[S], T)]
                                            ): T =
    do_apply[UInt, T](key.asBits.asUInt, default, mapping.map { case (s, t) => (s.asBits.asUInt, t) })

  def do_apply[S <: UInt, T <: Data](key: S, default: T, mapping: Seq[(S, T)]): T = {
    /* If the mapping is defined for all possible values of the key, then don't use the default value */
//    val (defaultx, mappingx) = key.widthOption match {
//      case Some(width) =>
//        val keySetSize = BigInt(1) << width
//        val keyMask = keySetSize - 1
//        val distinctLitKeys = mapping.flatMap(_._1.litOption).map(_ & keyMask).distinct
//        if (distinctLitKeys.size == keySetSize) {
//          (mapping.head._2, mapping.tail)
//        } else {
//          (default, mapping)
//        }
//      case None => (default, mapping)
//    }
//
//    mappingx.foldLeft(defaultx) { case (d, (k, v)) => Mux(k === key, v, d) }
    var res = default
    for ((t, v) <- mapping.reverse) {
      res = Mux(t === key, v, res)
    }
    res
  }
}
/** Given an association of values to enable signals, returns the first value with an associated
  * high enable signal.
  *
  * @example {{{
  * MuxCase(default, Array(c1 -> a, c2 -> b))
  * }}}
  */
object MuxCase {

  /** @param default the default value if none are enabled
    * @param mapping a set of data values with associated enables
    * @return the first value in mapping that is enabled
    */
  def apply[T <: Data](default: T, mapping: Seq[(Bool, T)]): T = {
    var res = default
    for ((t, v) <- mapping.reverse) {
      res = Mux(t, v, res)
    }
    res
  }
}

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
    U(i, s.length*8 bits)
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


object Sext
{
  def apply(x: UInt, length: Int): UInt =
  {
    return Cat(x(x.getWidth-1) #* (length-x.getWidth), x).asUInt
  }
}

// translates from BOOM's special "packed immediate" to a 32b signed immediate
// Asking for U-type gives it shifted up 12 bits.
object ImmGen
{
  def apply(ip: UInt, isel: Bits): SInt =
  {
    val sign = ip(LONGEST_IMM_SZ-1).asUInt
    val i30_20 = Mux(isel === IS_U.asBits, ip(18 downto 8), sign#*10)
    val i19_12 = Mux(isel === IS_U.asBits || isel === IS_J.asBits, ip(7 downto 0), sign#*8)
    val i11    = Mux(isel === IS_U.asBits, U(0, 1 bits),
      Mux(isel === IS_J.asBits || isel === IS_B.asBits, ip(8).asUInt, sign))
    val i10_5  = Mux(isel === IS_U.asBits, U(0,6 bits), ip(18 downto 14))
    val i4_1   = Mux(isel === IS_U.asBits, U(0,4 bits), ip(13 downto 9))
    val i0     = Mux(isel === IS_S.asBits || isel === IS_I.asBits, ip(8).asUInt, U(0, 1 bits))

    return Cat(sign, i30_20, i19_12, i11, i10_5, i4_1, i0).asSInt
  }
}

// store the rounding-mode and func type for FP in the packed immediate as well
object ImmGenRm { def apply(ip: UInt): UInt = { return ip(2 downto 0) }}
object ImmGenTyp { def apply(ip: UInt): UInt = { return ip(9 downto 8) }} // only works if !(IS_B or IS_S)
object AgePriorityEncoder
{
  def apply(in: Seq[Bool], head: UInt): UInt =
  {
    val n = in.size
    require (isPow2(n))
    val temp_vec = (0 until n).map(i => in(i) && UInt(i) >= head) ++ in
    val idx = PriorityEncoder(temp_vec)
    idx(log2Up(n)-1 downto 0) //discard msb
  }
}