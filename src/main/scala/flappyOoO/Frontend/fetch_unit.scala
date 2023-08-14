package flappyOoO.Frontend

import EasonLib.Common_ip.{lzc, popcount}
import flappyOoO.BaseIsa.RV32I
import flappyOoO.{Config, FetchService, MemBus, Stage, UnitModule}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import EasonLib.CocoSim._

object Instr_type extends SpinalEnum {
  val RVL, RVH, RVC, INVALID = newElement()
}
case class if2id_interface(val conf: Config) extends Bundle with IMasterSlave {
  val instr_data = Bits(conf.xlen bits)
  val instr_pc = UInt(32 bits)
  val valid = Bool()
  val isRvc = if(conf.Rvc) Bool() else null

  override def asMaster(): Unit = {
    out(instr_data)
    out(instr_pc)
    out(valid)
    if(conf.Rvc){
      out(isRvc)
    }
  }
}
class fetch_unit(resetVec: BigInt = 0x0)(implicit conf: Config) extends Component {
  val io = new Bundle {
    val core_en = in Bool()
    val ibus = master(new MemBus(conf.ibusConfig, 0 bits)).setName("ibus")
    val if2id_itf = Vec(master(if2id_interface(conf)), conf.DecoderWidth)
    val fetchStage_flush = in Bool()
    val fetchStage_isDone = in Bool()
    val fetchStage_isReady = out Bool()
    val pcJumpTarget = in(Flow(UInt(32 bits)))
    val pcPredicted = in(Flow(UInt(32 bits)))
  }


  noIoPrefix()
  val MinSliceByte = if (conf.Rvc) 2 else 4
  val FetchByteNum = conf.FetchWidth / 8
  val MaxSliceNum = FetchByteNum / MinSliceByte

  val pc = Reg(UInt(conf.xlen bits)).init(resetVec)
  val default_next_pc = pc + FetchByteNum
  val fetch_pending = Bool()

  // Fifos
  val addressFifo = new StreamFifoWithHead(UInt(conf.xlen bits), conf.FetchConfig.FetchAddrBuffDepth, 1, true)
  val validNumFifo = new PopToleranceFifo(UInt(log2Up(MaxSliceNum + 1) bits), conf.FetchConfig.FetchAddrBuffDepth)
  val xlenWithCflag = if (conf.Rvc) conf.xlen + 1 else conf.xlen // with isRvc flag
  val instrFifo = new StreamFifo1delayMultiPort(Bits(xlenWithCflag bits), pushNum = MaxSliceNum, popNum = conf.DecoderWidth,
    waterline = conf.FetchConfig.FetchInstrBuffWaterline, depth = conf.FetchConfig.FetchInstrBuffWaterline + MaxSliceNum*2)
  val addressOffset = if (conf.Rvc) addressFifo.io.head(log2Up(FetchByteNum) - 1 downto 1) else addressFifo.io.head(log2Up(FetchByteNum) - 1 downto 2)

  addressFifo.io.push.valid := io.ibus.cmd.fire & addressFifo.io.push.ready
  addressFifo.io.push.payload := io.ibus.cmd.payload.address
  // pc gen
  when(io.pcJumpTarget.valid) {
    pc := io.pcJumpTarget.payload
  }.elsewhen(io.pcPredicted.valid) {
    pc := io.pcPredicted.payload
  }.elsewhen(fetch_pending) {
    pc := pc
  } otherwise {
    pc := default_next_pc
  }
  // fetch pending should be pull up when fifos is full
  fetch_pending := ~io.core_en | instrFifo.io.ov_waterline | ~validNumFifo.io.push.ready | ~addressFifo.io.push.ready
  for (i <- 0 until conf.DecoderWidth) instrFifo.io.pop(i).ready := io.fetchStage_isDone
  io.fetchStage_isReady := instrFifo.io.pop(0).valid
  // fetch bus driven
  io.ibus.cmd.valid := ~fetch_pending
  io.ibus.cmd.payload.id := 0
  when(io.pcJumpTarget.valid & io.ibus.cmd.ready) {
    io.ibus.cmd.payload.address  := io.pcJumpTarget.payload
  }.elsewhen(io.pcPredicted.valid & io.ibus.cmd.ready ) {
    io.ibus.cmd.payload.address := io.pcPredicted.payload
  }.otherwise{
    io.ibus.cmd.payload.address := pc
  }
  io.ibus.rsp.ready := True // rsp is always receivable

  // Below vals is used to sequance instrs
  val valid_instr_slice = Vec(for (i <- 0 until MaxSliceNum) yield i >= addressOffset) //16 bit slice valid bit
  val instr_slices = Vec(Flow(Bits(xlenWithCflag bits)), MaxSliceNum)
  val tail_one_matrix = Vec(Bits(MaxSliceNum bits), MaxSliceNum)
  val tail_one_index_vec = Vec(UInt(log2Up(MaxSliceNum) bits), MaxSliceNum)
  val tail_one_valid_vec = Vec(Bool(), MaxSliceNum)
  val slice_valid_vec = Vec(for (j <- 0 until MaxSliceNum) yield instr_slices(j).valid.asBits)
  tail_one_matrix(0) := slice_valid_vec.reverse.reduce(_ ## _)



  if (conf.Rvc) {
    val unalign_state = RegFlow(Bits(16 bits))
    unalign_state.payload.init(0)

    val maybeRvc_vec = Vec(for (i <- 0 until MaxSliceNum) yield (io.ibus.rsp.payload.rdata(16 * i + 1 downto 16 * i) =/= 3))
    val realRvc_vec = Vec(Bool(), MaxSliceNum)
    val type_vec = Vec(Instr_type(), MaxSliceNum)
    for (i <- 0 until MaxSliceNum) {
      instr_slices(i).valid := False
      instr_slices(i).payload := 0
      type_vec(i) := Instr_type.INVALID
      if (i == 0) {
        when(unalign_state.valid) {
          when(valid_instr_slice(i)) {
            type_vec(i) := Instr_type.RVH
            instr_slices(i).push(False ## io.ibus.rsp.payload.rdata((i + 1) * 16 - 1 downto i * 16) ## unalign_state.payload)
          }
        }.otherwise {
          when(maybeRvc_vec(i)) {
            type_vec(i) := Instr_type.RVC
            when(valid_instr_slice(i)) {
              instr_slices(i).push(True ## B"16'd0" ## io.ibus.rsp.payload.rdata((i + 1) * 16 - 1 downto i * 16))
            }
          }.otherwise {
            type_vec(i) := Instr_type.RVL
          }
        }
      }
      else {
        switch(type_vec(i - 1)) {
          is(Instr_type.RVL) {
            type_vec(i) := Instr_type.RVH
            when(valid_instr_slice(i)) {
              instr_slices(i).push(False ## io.ibus.rsp.payload.rdata((i + 1) * 16 - 1 downto (i - 1) * 16))
            }
          }
          is(Instr_type.RVH, Instr_type.RVC) {
            when(maybeRvc_vec(i)) {
              type_vec(i) := Instr_type.RVC
              when(valid_instr_slice(i)) {
                instr_slices(i).push(True ## B"16'd0" ## io.ibus.rsp.payload.rdata((i + 1) * 16 - 1 downto i * 16))
              }
            }.otherwise {
              type_vec(i) := Instr_type.RVL
            }
          }
        }
      }
      realRvc_vec(i) := type_vec(i) === Instr_type.RVC
    }
    // unalign buffer update
    when(type_vec(MaxSliceNum-1) === Instr_type.RVL){
      unalign_state.push(io.ibus.rsp.payload.rdata((MaxSliceNum) * 16 - 1 downto (MaxSliceNum-1) * 16).asBits)
    }
  }
  else
    {
      for (i <- 0 until MaxSliceNum) {
        instr_slices(i).valid := False
        instr_slices(i).payload := 0
        when(valid_instr_slice(i)) {
          instr_slices(i).push(io.ibus.rsp.payload.rdata(32 * (i + 1) - 1 downto 32 * i).asBits)
        }
      }
    }

    for (i <- 0 until MaxSliceNum) {
      // get tail_one_index
      val trailone_index = lzc(Name = s"trailone_index_${i}",WIDTH = MaxSliceNum, mode = True, lead = False, trail = True, data_in = tail_one_matrix(i))
      tail_one_index_vec(i) := trailone_index(log2Up(MaxSliceNum) - 1 downto 0)
      tail_one_valid_vec(i) := trailone_index =/= MaxSliceNum
      if (i != MaxSliceNum - 1) {
        tail_one_matrix(i + 1) := tail_one_matrix(i)
        tail_one_matrix(i + 1)(tail_one_index_vec(i)) := False
      }
    }

    for (i <- 0 until MaxSliceNum) {
      instrFifo.io.push(i).payload := 0
      instrFifo.io.push(i).valid := False
      when(io.ibus.rsp.fire) {
        instrFifo.io.push(i).payload := instr_slices(tail_one_index_vec(i)).payload
        instrFifo.io.push(i).valid := tail_one_valid_vec(i) & instr_slices(tail_one_index_vec(i)).valid
      }
    }

    val instr_push_valid_vec = {
      val valid_pushs = Bits(MaxSliceNum bits)
      for (i <- 0 until MaxSliceNum) {
        valid_pushs(i) := instrFifo.io.push(i).valid
      }
      valid_pushs
    }
    // validNumFifo push and pop logic
    val instr_push_valid_num = popcount(WIDTH = MaxSliceNum, din_vld = instrFifo.io.push(0).fire, data_in = instr_push_valid_vec)
    validNumFifo.io.push.valid := False
    validNumFifo.io.push.payload := 0
    when(instrFifo.io.push(0).fire) {
      when(instr_push_valid_num =/= 0) {
        validNumFifo.io.push.valid := True
        validNumFifo.io.push.payload := instr_push_valid_num.resized
      }
    }
    val instr_pop_valid = Bits(conf.DecoderWidth bits)
    for(i <- 0 until conf.DecoderWidth){instr_pop_valid(i) := instrFifo.io.pop(i).fire}
    val instr_pop_num = popcount(WIDTH = conf.DecoderWidth, din_vld = instrFifo.io.pop(0).fire, data_in = instr_pop_valid)
    validNumFifo.io.pop.pop_req_num := 0
    validNumFifo.io.pop.valid := False
    when(instrFifo.io.pop(0).fire){
      validNumFifo.io.pop.pop_req_num := instr_pop_num.resized
      validNumFifo.io.pop.valid := True
    }
  // addressFifo pop logic
  addressFifo.io.pop.ready := False
  when(instrFifo.io.pop(0).fire) {
    addressFifo.io.pop.ready := validNumFifo.io.pop.pop_real & validNumFifo.io.pop.fire
  }

  // how to recover instr's pc ?
  val pc_pop_offset = Reg(UInt()) init(0)
  val pc_step_all = Vec(UInt(), conf.DecoderWidth + 1)
  pc_step_all(0) := 0       // first pop instr's pc is raw value of addressFifo.head
  val real_instr = Vec(Bits(32 bits),conf.DecoderWidth)
  val pc_step = Vec(UInt(3 bits), conf.DecoderWidth)
  if(conf.Rvc){
    val isRvc = Vec(Bool(), conf.DecoderWidth)
    for (i <- 0 until conf.DecoderWidth) {
      io.if2id_itf(i).isRvc := isRvc(i)
      isRvc(i) := instrFifo.io.pop(i).payload.msb
      val decomp_instr = RvcDecompressor(rvf=conf.Rvf,rvd=conf.Rvd, xlen=conf.xlen, i=instrFifo.io.pop(i).payload(conf.xlen - 1 downto 0))
      real_instr(i) := (isRvc(i) & ~decomp_instr.illegal) ? decomp_instr.inst | instrFifo.io.pop(i).payload(conf.xlen - 1 downto 0)
      pc_step(i) := isRvc(i) ? U"3'd2" | U"3'd4"
      pc_step_all(i+1) := pc_step_all(i) + pc_step(i)
    }
  }
  else{
    for (i <- 0 until conf.DecoderWidth) {
      real_instr(i) := instrFifo.io.pop(i).payload(conf.xlen - 1 downto 0)
      val pc_step = U"3'd4"
      pc_step_all(i+1) := pc_step_all(i) + pc_step
    }
  }

  for (i <- 0 until conf.DecoderWidth) {
    io.if2id_itf(i).instr_data := 0
    io.if2id_itf(i).instr_pc := 0
    when(instrFifo.io.pop(i).fire) {
      io.if2id_itf(i).instr_data := real_instr(i)
      io.if2id_itf(i).instr_pc := addressFifo.io.head + pc_pop_offset + pc_step_all(i)
    }
    io.if2id_itf(i).valid := instrFifo.io.pop(i).fire
  }

  when(addressFifo.io.pop.fire){
    pc_pop_offset := 0
  }.elsewhen(instrFifo.io.pop(0).fire){    //update pc_pop_offset buffer for next instr in the same address
    pc_pop_offset := pc_step_all(conf.DecoderWidth)
  }

  // flush logic
  instrFifo.io.flush := False
  addressFifo.io.flush := False
  validNumFifo.io.flush := False
  when(io.fetchStage_flush){
    instrFifo.io.flush := True
    addressFifo.io.flush := True
    validNumFifo.io.flush := True
  }


  }

object ifetch_inst {
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
      .generate(new fetch_unit(0x0)(new Config(RV32I)))
  }.printPruned()
}

object fetch_unit_test {
  import scala.math._
  def main(args: Array[String]): Unit = {
    val spinalConfig = SpinalConfig(
        defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
        mode=Verilog,
        targetDirectory="rtl_gen",
        oneFilePerComponent = true,
        nameWhenByFile = false,
        inlineConditionalExpression = true,
        enumPrefixEnable = true,
        anonymSignalPrefix = "tmp"
    )
    var start_time = 0.0
    var end_time = 0.0
    val compiled = SimConfig.withConfig(spinalConfig).withWave.allOptimisation.compile(
    rtl = {
      implicit val config = new Config(RV32I)
      val feu = new fetch_unit(0x0)
      println(feu.getClass.getSimpleName.replace("$", ""))
      val inputs = feu.getAllIo.filter(_.isInput)
      var inputs_string = for(io <- inputs) yield io.getName()
      val sb = new StringBuilder()
      var input_list = inputs_string.addString(sb," ")
      println(s"tse ${input_list}")
      feu
    })
    compiled.doSim { dut =>
      dut.io.core_en #= false
      dut.clockDomain.forkStimulus(2)
//      sleep(100)
      start_time = (simTime()/1000.0).toFloat

      dut.clockDomain.waitSampling()
      dut.io.core_en #= true
      dut.io.pcPredicted.valid #= false
      dut.io.pcPredicted.payload #= 0
      dut.io.pcJumpTarget.valid #= false
      dut.io.pcJumpTarget.payload #= 0
      dut.io.fetchStage_flush #= false
      dut.io.fetchStage_isDone #= true
      dut.io.ibus.cmd.ready #= true
      var IRAM = (for(i <- 1 until 100) yield i).toArray

      /** Add Driver here. */
      val driver_thread = fork{
        for(i <- 0 to 10) {
          dut.clockDomain.waitSampling()
          if(dut.io.ibus.cmd.valid.toBoolean){
            dut.io.ibus.rsp.valid #= true
            val addr = (dut.io.ibus.cmd.payload.address.toBigInt / 4).toInt
            dut.io.ibus.rsp.payload.rdata #= 2
          }
          else{
            dut.io.ibus.rsp.valid #= false
            dut.io.ibus.rsp.payload.rdata #= 0
          }
        }

      }
        //dut.io.  #= true
        //dut.clockDomain.waitSampling()
        //dut.io. #= false
        //dut.clockDomain.waitSamplingWhere(dut.io..toBoolean)

      /** Add Monitor here. */
      val monitor_thread = fork{
        //dut.clockDomain.waitSamplingWhere(dut.io..toBoolean)
        //assert(()==dut.io..toInt,"data Mismathc")
        //println(s"PASSED! Input A = ${}, B = ${}. DUT Result = ${}; Expect Result = ${}")
      }
      //monitor_thread.join()
      sleep(100)
      end_time = (simTime()/1000.0).toFloat
      println(s"SIM Takes ${end_time-start_time} s.")
      simSuccess()
    }
  }
}

object ifetch_coco {
  def main(args: Array[String]): Unit = {
    val workspacePath = "/home/lyc/projects/riscv/FlappyRiscv"
    new CocoTestFlow(design = {
      implicit val config = new Config(RV32I)
      val feu = new fetch_unit(0x0)
      feu
    }, workspacePath = workspacePath).doit()
  }
}


