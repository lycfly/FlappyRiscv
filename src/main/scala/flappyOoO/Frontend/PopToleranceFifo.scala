package flappyOoO.Frontend

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.util.Random
import scala.language.postfixOps
case class PopStream[T <: Data](val dataType :  HardType[T]) extends Bundle with IMasterSlave{
  val valid = Bool()
  val ready = Bool()
  val pop_req_num = dataType()
  val pop_out_data = dataType()
  val pop_real = Bool()
  override def asMaster(): Unit = {
    out(valid)
    out(pop_req_num)
    in(ready)
    in(pop_out_data)
    in(pop_real)
  }
  def fire = {
    valid & ready
  }
}
class PopToleranceFifo(val dataType: HardType[UInt], val depth: Int) extends Component {
  val io = new Bundle {
    val push = slave(Stream(dataType))
    val pop = slave(PopStream(dataType))
    val head = out(dataType())
    val flush = in Bool() default (False)
    val occupancy = out UInt (log2Up(depth + 1) bit)
  }
  noIoPrefix()
  val vec = Vec(Reg(dataType), depth)
  val pushPtrGlobal = Reg(UInt(log2Up(depth) bits)) init (0)
  val popPtrGlobal = Reg(UInt(log2Up(depth) bits)) init (0)
  val ptrMatch = pushPtrGlobal === popPtrGlobal
  val risingOccupancy = RegInit(False)
  val empty = ptrMatch & !risingOccupancy
  val full = ptrMatch & risingOccupancy
  val pushing = io.push.fire
  val popping = io.pop.pop_real
  io.push.ready := !full

  io.pop.ready := !empty && io.head =/= 0
  io.head := vec(popPtrGlobal)

  io.pop.pop_out_data := vec(popPtrGlobal)
  io.pop.pop_real := False
  when(io.pop.fire){
    vec(popPtrGlobal) := vec(popPtrGlobal) - io.pop.pop_req_num
    when(vec(popPtrGlobal) === io.pop.pop_req_num){
      when(popPtrGlobal >= depth - 1) {
        popPtrGlobal := 0
      }.otherwise {
        popPtrGlobal := popPtrGlobal + 1
      }
      io.pop.pop_out_data := 0
      io.pop.pop_real := True
    }
  }

  when(pushing) {
    vec.write(pushPtrGlobal, io.push.payload)
  }

  when(pushPtrGlobal >= depth - 1) {
    pushPtrGlobal := 0
  }.otherwise {
    pushPtrGlobal := pushPtrGlobal + 1
  }

  when(pushing =/= popping) {
    risingOccupancy := pushing
  }

  val ptrDif = pushPtrGlobal - popPtrGlobal
  if (isPow2(depth))
    io.occupancy := ((risingOccupancy && ptrMatch) ## ptrDif).asUInt
  else {
    when(ptrMatch) {
      io.occupancy := Mux(risingOccupancy, U(depth), U(0))
    } otherwise {
      io.occupancy := Mux(pushPtrGlobal > popPtrGlobal, ptrDif, U(depth) + ptrDif)
    }
  }
  when(io.flush) {
    pushPtrGlobal := 0
    popPtrGlobal := 0
    risingOccupancy := False
  }

}

object FifoWithPopTolerance_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new PopToleranceFifo(UInt(2 bits), 4))
  }.printPruned()
}


