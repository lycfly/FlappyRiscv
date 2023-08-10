package flappyOoO

import spinal.core._
import spinal.lib._

class IdInfo[T <: Data](val dataType: HardType[T]) {
  def name: String = getClass.getSimpleName.takeWhile(_ != '$')

  override def toString: String = name
}

trait FetchService {
//  var pcPredicted: Flow[UInt] = null
//  var pcJumpTarget: Flow[UInt] = null
}


object AluOp extends SpinalEnum {
  val ADD, SUB, SLT, SLTU, XOR, OR, AND, SRC2 = newElement()
}
object AluSrc1Select extends SpinalEnum {
  val RS1, PC = newElement()
}
object AluSrc2Select extends SpinalEnum {
  val RS2, IMM = newElement()
}

object LsuOperationType extends SpinalEnum {
  val NONE, LOAD, STORE = newElement()
}
object AccessWidth extends SpinalEnum {
  val B, H, W = newElement()
}

case class idInfo_Alu() extends Bundle {
  val op = AluOp()
  val r1_source = AluSrc1Select()
  val r2_source = AluSrc2Select()
  val commit_en = Bool()
}
