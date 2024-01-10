package boom_v1.Utils

import spinal.core.internals.UIntRangedAccessFixed
import spinal.core.{Bundle, Data, HardType, IntToBuilder}
import spinal.lib.Flow

object INPUT extends DIR
object OUTPUT extends DIR

class DIR()

object Bool {
  def apply(in: DIR) = {
    if(in == INPUT) {
      spinal.core.Bool().asInput()
    } else{
      spinal.core.Bool().asOutput()
    }
  }
}
object Bits {
  def apply(in: DIR, width: Int) = {
    if(in == INPUT) {
      spinal.core.Bits().setWidth(width).asInput()
    } else{
      spinal.core.Bits().setWidth(width).asOutput()
    }
  }
  def apply(width: Int) = {
    spinal.core.Bits().setWidth(width)
  }
}

object UInt {
  def apply(in: DIR, width: Int) = {
    if(in == INPUT) {
      spinal.core.UInt().setWidth(width).asInput()
    } else{
      spinal.core.UInt().setWidth(width).asOutput()
    }
  }

  def apply(width: Int) = {
    spinal.core.UInt().setWidth(width)
  }
}

object SInt {
  def apply(in: DIR, width: Int) = {
    if(in == INPUT) {
      spinal.core.SInt().setWidth(width).asInput()
    } else{
      spinal.core.SInt().setWidth(width).asOutput()
    }
  }

  def apply(width: Int) = {
    spinal.core.SInt().setWidth(width)
  }
}

object Valid {
  def apply[T<:Data](payloadData:HardType[T]) = Flow(payloadData)
}

class ValidIO[T <: Data](payloadType: HardType[T]) extends Flow[T](payloadType)
//class Decoupled[T <: Data](payloadType: HardType[T]) extends Stream[T](payloadType)
object chiselExtract{
  implicit def chiselUInt2Spinal(a: spinal.core.UInt) = new chiselUInt(a)

  class chiselUInt(a: spinal.core.UInt){
    def slice(end: Int, start: Int): spinal.core.UInt = {
      a.apply(offset = start, bitCount= spinal.core.BitCount(end-start+1))
    }
  }

  object test{
    val b = spinal.core.UInt(4 bits)
//    val a = new chiselUInt(b)
    val c = b.slice(4,3)
  }
//  implicit class UIntExtract(x: UInt) {
//    def apply(start: Int, end: Int): UInt = {
//      x(start downto end)
//    }
//  }
}
