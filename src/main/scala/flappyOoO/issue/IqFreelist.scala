package flappyOoO.issue

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.util.Random
import scala.language.postfixOps


class IqFreelist(val dataType: UInt,val pushNum: Int, val popNum: Int, val depth: Int,
               useVec : Boolean = true, strict_full: Boolean = true, strict_empty: Boolean = true) extends Component {
  require(depth >= 1)
  val io = new Bundle {
    val push = Vec(slave(Stream(dataType)), pushNum)
    val pop = Vec(master(Stream (dataType)), popNum)
    val flush = in Bool() default (False)
  }
  val occupancy = out UInt (log2Up(depth + 1) bit)
  val vec = useVec generate Vec(Reg(dataType), depth)
  val ram = !useVec generate Mem(dataType, depth)
  val pushPtrGlobal = Reg(UInt(log2Up(depth+1) bits)) init(depth-1)
  val popPtrGlobal = Reg(UInt(log2Up(depth+1) bits)) init(0)
  val ptrMatch = pushPtrGlobal === popPtrGlobal
  val risingOccupancy = RegInit(False)
  val empty = ptrMatch & !risingOccupancy
  val full = ptrMatch & risingOccupancy
  empty.allowPruning()
  full.allowPruning()
  val pushing_vec = Vec(for(i <- 0 until pushNum) yield io.push(i).fire)
  val popping_vec = Vec(for(i <- 0 until popNum) yield io.pop(i).fire)

  val inc_num = pushing_vec.sCount(True)
  val dec_num = popping_vec.sCount(True)

  for(i <- 0 until depth){
    vec(i).init(U(i))
    when(io.flush){
      vec(i) := U(i)
    }
  }

  if(strict_full){
    for(i <- 0 until pushNum){
      io.push(i).ready := (depth - occupancy) > i
    }
  }
  else {
    /* This may not be strict enough **/
    io.push.foreach(_.ready := !full)
  }

  val pushPtrs = Vec(UInt(),pushNum)
  val popPtrs = Vec(UInt(), popNum)
  val pushPtrs_inc = Vec(UInt(), pushNum)
  val popPtrs_inc = Vec(UInt(), popNum)
  for(i <- 0 until pushNum){
    pushPtrs_inc(i) := pushPtrGlobal +^ U(i)
    when(pushPtrs_inc(i) >= depth) {
      pushPtrs(i) := pushPtrs_inc(i) - depth
    }.otherwise {
      pushPtrs(i) := pushPtrs_inc(i)
    }
  }
  for (i <- 0 until popNum) {
    popPtrs_inc(i) := popPtrGlobal +^ U(i)
    when(popPtrs_inc(i) >= depth) {
      popPtrs(i) := popPtrs_inc(i) - depth
    }.otherwise {
      popPtrs(i) := popPtrs_inc(i)
    }
  }
  val readedVec = Vec(for(i <- 0 until popNum) yield {
    if(useVec) vec(popPtrs(i)(log2Up(depth)-1 downto 0))
    else ram(popPtrs(i)(log2Up(depth)-1 downto 0))
  })

  for(i <- 0 until popNum){
    if (strict_empty) {
      io.pop(i).valid := occupancy > i
    }
    else {
      /* This may not be strict enough **/
      io.pop(i).valid := !empty
    }
    io.pop(i).payload := readedVec(i)
  }

  when(inc_num > dec_num) {
    risingOccupancy := True
  }.otherwise{
    risingOccupancy := False
  }

  for(i <- 0 until pushNum){
    when(pushing_vec(i)) {
      if (useVec)
        vec.write(pushPtrs(i)(log2Up(depth)-1 downto 0), io.push(i).payload)
      else
        ram.write(pushPtrs(i)(log2Up(depth)-1 downto 0), io.push(i).payload)
    }
  }

  val pushPtrGlobalInc = pushPtrGlobal +^ inc_num.resized
  val popPtrGlobalInc = popPtrGlobal +^ dec_num.resized

  when(inc_num =/= 0){
    when(pushPtrGlobalInc >= depth){
      pushPtrGlobal := (pushPtrGlobalInc - depth).resized
    }.otherwise{
      pushPtrGlobal := pushPtrGlobalInc.resized
    }
  }

  when(dec_num =/= 0) {
    when(popPtrGlobalInc >= depth){
      popPtrGlobal := (popPtrGlobalInc - depth).resized
    }.otherwise{
      popPtrGlobal := popPtrGlobalInc.resized
    }
  }

  val ptrDif = pushPtrGlobal - popPtrGlobal
  if (isPow2(depth))
    occupancy := ((risingOccupancy && ptrMatch) ## ptrDif(log2Up(depth)-1 downto 0)).asUInt
  else {
    when(ptrMatch) {
      occupancy := Mux(risingOccupancy, U(depth), U(0))
    } otherwise {
      occupancy := Mux(pushPtrGlobal > popPtrGlobal, ptrDif, U(depth) + ptrDif)
    }
  }

  when(io.flush){
    pushPtrGlobal := 0
    popPtrGlobal := 0
    risingOccupancy := False
  }
}

object IqFreelist_inst {
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
      .generate(new IqFreelist(UInt(log2Up(8) bits),2,2,8))
  }.printPruned()
}