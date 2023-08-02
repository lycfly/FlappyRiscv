package flappyOoO.Frontend

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.util.Random
import scala.language.postfixOps

class StreamFifo1delayMultiPort[T <: Data](val dataType: HardType[T],val pushNum: Int, val popNum: Int, val waterline: Int, val depth: Int,
                                           useVec : Boolean = true, strict_full: Boolean = true, strict_empty: Boolean = true) extends Component {
  require(depth >= 1)
  val io = new Bundle {
    val push = Vec(slave(Stream(dataType)), pushNum)
    val pop = Vec(master(Stream (dataType)), popNum)
    val flush = in Bool() default (False)
    val occupancy = out UInt (log2Up(depth + 1) bit)
    val ov_waterline = out Bool()
  }
  val vec = useVec generate Vec(Reg(dataType), depth)
  val ram = !useVec generate Mem(dataType, depth)
  val pushPtrGlobal = Reg(UInt(log2Up(depth+1) bits)) init(0)
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

  if(strict_full){
    for(i <- 0 until pushNum){
      io.push(i).ready := (depth - io.occupancy) > i
    }
  }
  else {
    /* This may not be strict enough **/
    io.push.foreach(_.ready := !full)
  }


  io.ov_waterline := io.occupancy >= waterline

  val pushPtrs = Vec(UInt(),pushNum)
  val popPtrs = Vec(UInt(), popNum)
  for(i <- 0 until pushNum){
    when(pushPtrGlobal + i >= depth){
      pushPtrs(i) := 0
    }.otherwise{
      pushPtrs(i) := pushPtrGlobal + i
    }
  }
  for (i <- 0 until popNum) {
    when(popPtrGlobal + i >= depth) {
      popPtrs(i) := 0
    }.otherwise {
      popPtrs(i) := popPtrGlobal + i
    }
  }
  val readedVec = Vec(for(i <- 0 until popNum) yield {
    if(useVec) vec(popPtrs(i) + i)
    else ram(popPtrs(i) + i)
  })

  for(i <- 0 until popNum){
    if (strict_empty) {
        io.pop(i).valid := io.occupancy > i
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
        vec.write(pushPtrs(i), io.push(i).payload)
      else
        ram.write(pushPtrs(i), io.push(i).payload)
    }
  }
  
  val pushPtrGlobalInc = pushPtrGlobal + inc_num.resized
  val popPtrGlobalInc = popPtrGlobal + dec_num.resized

  when(inc_num =/= 0){
    when(pushPtrGlobalInc >= depth){
      pushPtrGlobal := 0
    }.otherwise{
      pushPtrGlobal := pushPtrGlobalInc
    }
  }
  when(dec_num =/= 0) {
    when(popPtrGlobalInc >= depth){
      popPtrGlobal := 0
    }.otherwise{
      popPtrGlobal := popPtrGlobalInc
    }
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

  when(io.flush){
    pushPtrGlobal := 0
    popPtrGlobal := 0
    risingOccupancy := False
  }
}

object StreamFifoLowLatencyMultiPort_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = true,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new StreamFifo1delayMultiPort(Bits(32 bits),4,2,8,12))
  }.printPruned()
}

object StreamFifo1delayMultiPort_test {
  import scala.math._
  def main(args: Array[String]): Unit = {
    val spinalConfig = SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
        mode = Verilog,
        oneFilePerComponent = true,
        nameWhenByFile = false,
        inlineConditionalExpression = true,
        enumPrefixEnable = true,
        anonymSignalPrefix = "tmp",
        targetDirectory = "rtl_gen"
        )
    var start_time = 0.0
    var end_time = 0.0
    val compiled = SimConfig.withConfig(spinalConfig).withWave.allOptimisation.compile(
    rtl = new StreamFifo1delayMultiPort(Bits(32 bits),4,2,8,12))
    compiled.doSim { dut =>
      dut.clockDomain.forkStimulus(2)
      dut.io.push.foreach(_.valid #= false) 
      dut.io.pop.foreach(_.ready #= false) 
      sleep(100)
      start_time = (simTime()/1000.0).toFloat
      dut.clockDomain.waitSampling()
      // Add Driver here.
      val driver_thread = fork{
        for(i <- 0 until 2){
          dut.io.push.foreach(_.valid #= true)
          dut.io.push.foreach(_.payload #= 2) 
          dut.clockDomain.waitSampling()
          dut.io.push.foreach(_.valid #= false)  
        }

        //dut.clockDomain.waitSamplingWhere(dut.io..toBoolean)
      }
      // Add Monitor here.
      val monitor_thread = fork{
        dut.clockDomain.waitSamplingWhere(dut.io.pop(0).valid.toBoolean)
        dut.io.pop.foreach(_.ready #= true)
        dut.clockDomain.waitSampling()
        dut.clockDomain.waitSampling()
        dut.clockDomain.waitSampling()
        dut.io.pop.foreach(_.ready #= false)
        dut.clockDomain.waitSampling()
        dut.io.pop.foreach(_.ready #= true)
        dut.clockDomain.waitSampling()
        dut.io.pop.foreach(_.ready #= false)
        //assert(()==dut.io..toInt,"data Mismathc")
        //println(s"PASSED! Input A = ${}, B = ${}. DUT Result = ${}; Expect Result = ${}")
      }
      // monitor_thread.join()
      sleep(100)
      end_time = (simTime()/1000.0).toFloat
      println(s"SIM Takes ${end_time-start_time} s.")
      simSuccess()
    }
  }
}