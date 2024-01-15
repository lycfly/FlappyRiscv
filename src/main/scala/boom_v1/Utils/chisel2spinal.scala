package boom_v1.Utils

import spinal.core.internals.UIntRangedAccessFixed
import spinal.core.{Bundle, Cat, Data, HardType, IntToBuilder, RegNext, RegNextWhen}
import spinal.lib.Flow

object Wire {
  def apply[T <: Data](init: HardType[T]) = init()
}

object Input {
  def apply[T <: Data](port: HardType[T]) = spinal.core.in(port)
}

object Output {
  def apply[T <: Data](port: HardType[T]) = spinal.core.out(port)
}

object Vec {
  def apply[T <: Data](size: Int, gen: HardType[T]): spinal.core.Vec[T] = spinal.core.Vec.fill(size)(gen())
}

object INPUT extends DIR

object OUTPUT extends DIR

class DIR()

object Bool {
  def apply(in: DIR) = {
    if (in == INPUT) {
      spinal.core.Bool().asInput()
    } else {
      spinal.core.Bool().asOutput()
    }
  }
}

object Bits {
  def apply(in: DIR, width: Int) = {
    if (in == INPUT) {
      spinal.core.Bits().setWidth(width).asInput()
    } else {
      spinal.core.Bits().setWidth(width).asOutput()
    }
  }

  def apply(width: Int) = {
    spinal.core.Bits().setWidth(width)
  }
}

object UInt {
  def apply(in: DIR, width: Int) = {
    if (in == INPUT) {
      spinal.core.UInt().setWidth(width).asInput()
    } else {
      spinal.core.UInt().setWidth(width).asOutput()
    }
  }

  def apply(width: Int) = {
    spinal.core.UInt().setWidth(width)
  }
}

object SInt {
  def apply(in: DIR, width: Int) = {
    if (in == INPUT) {
      spinal.core.SInt().setWidth(width).asInput()
    } else {
      spinal.core.SInt().setWidth(width).asOutput()
    }
  }

  def apply(width: Int) = {
    spinal.core.SInt().setWidth(width)
  }
}

object Valid {
  def apply[T <: Data](payloadData: HardType[T]) = Flow(payloadData)
}

class ValidIO[T <: Data](payloadType: HardType[T]) extends Flow[T](payloadType)

//class Decoupled[T <: Data](payloadType: HardType[T]) extends Stream[T](payloadType)
object chiselDotDef {
  implicit def chiselBoolean2SpinalBool(a: Boolean) = new BooleanDotDefs(a)

  implicit def chiselWidth2SpinalBitCount(a: Int) = new IntDotDefs(BigInt(a))

  implicit def chiselWidthBigInt2SpinalBitCount(a: BigInt) = new IntDotDefs(a)

  class BooleanDotDefs(a: Boolean) {
    def B = spinal.core.Bool(a)
  }

  class IntDotDefs(a: BigInt) {
    def W: spinal.core.BitCount = {
      spinal.core.BitCount(a.toInt)
    }

    def S: spinal.core.SInt = {
      spinal.core.S(a)
    }

    def S(width: spinal.core.BitCount): spinal.core.SInt = {
      spinal.core.S(a).setWidth(width.value)
    }

    def U: spinal.core.UInt = {
      spinal.core.U(a)
    }

    def U(width: spinal.core.BitCount): spinal.core.UInt = {
      spinal.core.U(a).setWidth(width.value)
    }

    def B: spinal.core.Bits = {
      spinal.core.B(a)
    }

    def B(width: spinal.core.BitCount): spinal.core.Bits = {
      spinal.core.B(a).setWidth(width.value)
    }
  }

}

object chiselExtract {
  implicit def chiselUInt2Spinal(a: spinal.core.UInt) = new chiselUInt(a)

  implicit def chiselBits2Spinal(a: spinal.core.Bits) = new chiselBits(a)

  implicit def chiselSInt2Spinal(a: spinal.core.SInt) = new chiselSInt(a)

  class chiselUInt(a: spinal.core.UInt) {
    def slice(end: Int, start: Int): spinal.core.UInt = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def extract(end: Int, start: Int): spinal.core.UInt = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def sextTo(n: Int): spinal.core.UInt =
      if (a.getWidth == n) a
      else Cat(Fill(n - a.getWidth, a(a.getWidth - 1)), a).asUInt

    def zext: spinal.core.SInt = (spinal.core.False ## a).asSInt

    def sext: spinal.core.SInt = (a.msb ## a).asSInt

  }

  class chiselBits(a: spinal.core.Bits) {
    def slice(end: Int, start: Int): spinal.core.Bits = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def extract(end: Int, start: Int): spinal.core.Bits = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def sextTo(n: Int): spinal.core.Bits =
      if (a.getWidth == n) a
      else Cat(Fill(n - a.getWidth, a(a.getWidth - 1)), a)

    def zext: spinal.core.SInt = (spinal.core.False ## a).asSInt

    def sext: spinal.core.SInt = (a.msb ## a).asSInt
  }

  class chiselSInt(a: spinal.core.SInt) {
    def slice(end: Int, start: Int): spinal.core.SInt = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def extract(end: Int, start: Int): spinal.core.SInt = {
      a.apply(offset = start, bitCount = spinal.core.BitCount(end - start + 1))
    }

    def zext: spinal.core.SInt = (spinal.core.False ## a).asSInt

    def sext: spinal.core.SInt = (a.msb ## a).asSInt
  }

  object test {
    val b = spinal.core.UInt(4 bits)
    //    val a = new chiselUInt(b)
    val c = b.slice(4, 3)
  }
  //  implicit class UIntExtract(x: UInt) {
  //    def apply(start: Int, end: Int): UInt = {
  //      x(start downto end)
  //    }
  //  }
}

object Pipe {

  /** Generate a one-stage pipe from an explicit valid bit and some data
    *
    * @param enqValid the valid bit (must be a hardware type)
    * @param enqBits  the data (must be a hardware type)
    * @return $returnType
    */
  def apply[T <: Data](enqValid: spinal.core.Bool, enqBits: T): Flow[T] = {
    apply(enqValid, enqBits, 1)
  }

  /** Generate a pipe for a [[Valid]] interface
    *
    * @param enq     a [[Valid]] interface (must be a hardware type)
    * @param latency the number of pipeline stages
    * @return $returnType
    */
  def apply[T <: Data](enq: Flow[T], latency: Int = 1): Flow[T] = {
    apply(enq.valid, enq.payload, latency)
  }

  /** Generate a pipe from an explicit valid bit and some data
    *
    * @param enqValid the valid bit (must be a hardware type)
    * @param enqBits  the data (must be a hardware type)
    * @param latency  the number of pipeline stages
    * @return $returnType
    */
  def apply[T <: Data](enqValid: spinal.core.Bool, enqBits: T, latency: Int): Flow[T] = {
    require(latency >= 0, "Pipe latency must be greater than or equal to zero!")
    if (latency == 0) {
      val out = Flow(enqBits)
      out.valid := enqValid
      out.payload := enqBits
      out
    } else {
      val pipe = new spinal.core.Area {
        val v = RegNext(enqValid, spinal.core.False)
        val b = RegNextWhen(enqBits, enqValid)
      }
      apply(pipe.v, pipe.b, latency - 1)

    }
  }

}