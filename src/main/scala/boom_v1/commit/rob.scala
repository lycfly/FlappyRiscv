package boom_v1.commit

import boom_v1.{Causes, MicroOp, Parameters}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

class Exception(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()
  val cause = Bits(width=log2Up(Causes.all.max) bits)
  val badvaddr = UInt(width=p.coreMaxAddrBits bits)
}