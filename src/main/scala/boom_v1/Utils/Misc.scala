package boom_v1.Utils
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
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