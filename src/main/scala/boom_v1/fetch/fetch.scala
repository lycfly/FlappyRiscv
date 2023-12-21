package boom_v1.fetch
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// Fetch Unit
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

import boom_v1.BOOMDebugConstants.{DEBUG_PRINTF, O3PIPEVIEW_PRINTF, O3_CYCLE_TIME}
import boom_v1.Utils.Str
import boom_v1.exec.BranchUnitResp
import boom_v1.{DebugStageEvents, Parameters}
import boom_v1.predictor.{BHTUpdate, BTBResp, BTBUpdate, BranchPrediction, BranchPredictionResp, CFIType, RASUpdate}
import spinal.core._
import spinal.lib._
class FrontendReq(implicit p: Parameters) extends Bundle {
  val pc = UInt(width = p.vaddrBitsExtended bits)
  val speculative = Bool()
}

class FrontendResp(implicit p: Parameters) extends Bundle {
  val btb = Flow(new BTBResp)
  val pc = UInt(width = p.vaddrBitsExtended bits)  // ID stage PC
  val data = UInt(width = p.fetchWidth * p.coreInstBits bits)
  val mask = Bits(width = p.fetchWidth bits)
  val xcpt_if = Bool()
  val replay = Bool()
}

class FrontendIO(implicit p: Parameters) extends Bundle {
  val req = master Flow(new FrontendReq)
  val resp = slave Stream(new FrontendResp)
  val btb_update = master Flow(new BTBUpdate)
  val bht_update = master Flow(new BHTUpdate)
  val ras_update = master Flow(new RASUpdate)
  val flush_icache = out Bool()
  val flush_tlb = out Bool()
  val npc = out UInt(width = p.vaddrBitsExtended bits)

  // counter event
  val ic_miss = in Bool()
  val tlb_miss = in Bool()
}

class FetchBundle(implicit p: Parameters) extends Bundle
{
  val pc          = UInt(width = p.vaddrBits+1 bits)
  val insts       = Vec(Bits(width = 32 bits),p.FETCH_WIDTH)
  val mask        = Bits(width = p.FETCH_WIDTH bits) // mark which words are valid instructions
  val xcpt_if     = Bool()
  val replay_if   = Bool()

  val pred_resp   = new BranchPredictionResp
  val predictions = Vec(new BranchPrediction, p.FETCH_WIDTH)

  val debug_events = Vec(new DebugStageEvents, p.FETCH_WIDTH)

}


class FetchUnit(fetch_width: Int)(implicit p: Parameters) extends Module
{
  val io = new Bundle
  {
    val imem              = new FrontendIO
    val br_unit           = in(new BranchUnitResp())

    val tsc_reg           = in UInt(p.xLen bits)

    val bp2_take_pc       = in Bool()
    val bp2_is_taken      = in Bool()
    val bp2_br_seen       = in Bool()
    val bp2_is_jump       = in Bool()
    val bp2_is_cfi        = in Bool()
    val bp2_pred_resp     = in(new BranchPredictionResp())
    val bp2_predictions   = in Vec(new BranchPrediction(), fetch_width)
    val bp2_pc_of_br_inst = in UInt(p.vaddrBits+1 bits)
    val bp2_pred_target   = in UInt(p.vaddrBits+1 bits)

    val clear_fetchbuffer = in Bool()

    val flush_take_pc     = in Bool()
    val flush_pc          = in UInt(p.vaddrBits+1 bits)

    val stalled           = out Bool()
    val resp              = master(Stream(new FetchBundle))
  }

  val fseq_reg = RegInit(U(0, p.xLen bits))
  val if_pc_next = (UInt(width = p.vaddrBits+1 bits))

  val br_unit = io.br_unit


  val fetch_bundle = (new FetchBundle())

  val FetchBuffer = new StreamFifoLowLatency(dataType = new FetchBundle,
    depth = p.FETCH_BUFFER_SZ)
  FetchBuffer.io.flush := io.clear_fetchbuffer

  if (O3PIPEVIEW_PRINTF)
  {
    when (FetchBuffer.io.push.fire)
    {
      fseq_reg := fseq_reg + CountOne(FetchBuffer.io.push.payload.mask)

      for (i <- 0 until fetch_width)
      {
        when (fetch_bundle.mask(i))
        {
          // TODO for now, manually set the fetch_tsc to point to when the fetch
          // started. This doesn't properly account for i-cache and i-tlb misses. :(
          printf("%d; O3PipeView:fetch:%d:0x%x:0:%d:DASM(%x)\n",
            fetch_bundle.debug_events(i).fetch_seq,
            io.tsc_reg - U(2*O3_CYCLE_TIME),
            (fetch_bundle.pc.asSInt & S(-(fetch_width*p.coreInstBytes))).asUInt + U(i << 2),
            fetch_bundle.debug_events(i).fetch_seq,
            fetch_bundle.insts(i))
        }
      }
    }
  }

  val if_stalled = !(FetchBuffer.io.push.ready) // if FetchBuffer backs up, we have to stall the front-end

  val take_pc = br_unit.take_pc ||
    io.flush_take_pc ||
    (io.bp2_take_pc && !if_stalled) // TODO this seems way too low-level to get backpressure signal correct

  io.imem.req.valid   := take_pc // tell front-end we had an unexpected change in the stream
  io.imem.req.payload.pc := if_pc_next
  io.imem.req.payload.speculative := !(io.flush_take_pc)
  io.imem.resp.ready  := !(if_stalled) // TODO perf BUG || take_pc?

  if_pc_next := Mux(io.flush_take_pc, io.flush_pc,
    Mux(br_unit.take_pc,  br_unit.target(p.vaddrBits downto 0),
      io.bp2_pred_target)) // bp2_take_pc

  // Fetch Buffer
  FetchBuffer.io.push.valid := io.imem.resp.valid && !io.clear_fetchbuffer
  FetchBuffer.io.push.payload  := fetch_bundle

  fetch_bundle.pc := io.imem.resp.payload.pc
  fetch_bundle.xcpt_if := io.imem.resp.payload.xcpt_if
  fetch_bundle.replay_if := io.imem.resp.payload.replay

  for (i <- 0 until fetch_width)
  {
    fetch_bundle.insts(i) := io.imem.resp.payload.data(i*p.coreInstBits+p.coreInstBits-1 downto i*p.coreInstBits)

    if (i == 0)
    {
      fetch_bundle.debug_events(i).fetch_seq := fseq_reg
    }
    else
    {
      fetch_bundle.debug_events(i).fetch_seq := fseq_reg +
        CountOne(fetch_bundle.mask.asBits(i-1 downto 0))
    }
  }

  if (p.enableBTB)
  {
    io.imem.btb_update.valid := (br_unit.btb_update_valid ||
      (io.bp2_take_pc && io.bp2_is_taken && !if_stalled && !br_unit.take_pc)) &&
      !io.flush_take_pc
  }
  else
  {
    io.imem.btb_update.valid := Bool(false)
  }

  // update the BTB
  // If a branch is mispredicted and taken, update the BTB.
  // (if branch unit mispredicts, instructions in decode are no longer valid)
  io.imem.btb_update.payload.pc         := Mux(br_unit.btb_update_valid, br_unit.btb_update.pc, io.imem.resp.payload.pc)
  io.imem.btb_update.payload.br_pc      := Mux(br_unit.btb_update_valid, br_unit.btb_update.br_pc, io.bp2_pc_of_br_inst)
  io.imem.btb_update.payload.target     := Mux(br_unit.btb_update_valid, br_unit.btb_update.target,
    (io.bp2_pred_target.asSInt &
      S(-p.coreInstBytes)).asUInt)
  io.imem.btb_update.payload.prediction := Mux(br_unit.btb_update_valid, br_unit.btb_update.prediction,
    io.imem.resp.payload.btb)
  io.imem.btb_update.payload.taken      := Mux(br_unit.btb_update_valid, br_unit.btb_update.taken,
    io.bp2_take_pc && io.bp2_is_taken && !if_stalled)
  io.imem.btb_update.payload.cfiType     := (br_unit.btb_update_valid ? br_unit.btb_update.cfiType |
                                                (io.bp2_is_jump ? (CFIType.call | CFIType.branch)))
//  io.imem.btb_update.payload.isReturn   := Mux(br_unit.btb_update_valid, br_unit.btb_update.isReturn, Bool(false))
  io.imem.btb_update.payload.isValid    := Mux(br_unit.btb_update_valid, Bool(true), io.bp2_is_cfi)

  // Update the BHT in the BP2 stage.
  // Also update the BHT in the Exe stage IF and only if the branch is a misprediction.
  // TODO move this into the bpd_pipeline
  val bp2_bht_update = (Flow(new BHTUpdate()).asOutput)
  bp2_bht_update.valid           := io.imem.resp.valid && io.bp2_br_seen && !if_stalled && !br_unit.take_pc
  bp2_bht_update.payload.prediction := io.imem.resp.payload.btb
  bp2_bht_update.payload.pc         := io.imem.resp.payload.pc
  bp2_bht_update.payload.taken      := Mux(io.bp2_take_pc,
    io.bp2_is_taken,
    io.imem.resp.payload.btb.valid && io.imem.resp.payload.btb.payload.taken)
  bp2_bht_update.payload.mispredict := io.bp2_take_pc

  io.imem.bht_update := Mux(br_unit.brinfo.valid &&
    br_unit.brinfo.mispredict &&
    RegNext(br_unit.bht_update.valid),
    RegNext(br_unit.bht_update),
    bp2_bht_update)


  // bp2 stage
  fetch_bundle.mask := io.imem.resp.payload.mask & io.bp2_pred_resp.mask
  fetch_bundle.pred_resp := io.bp2_pred_resp
  fetch_bundle.predictions := io.bp2_predictions

  // output
  io.stalled := if_stalled
  io.resp <> FetchBuffer.io.pop


  //-------------------------------------------------------------
  if (DEBUG_PRINTF)
  {
    // Fetch Stage 1
    printf("BrPred1:    (IF1_PC= 0x%x Predict:n/a) ------ PC: [%c%c%c-%c for br_id:(n/a), %c %c next: 0x%x ifst:%d]\n"
      , io.imem.npc
      , Mux(br_unit.brinfo.valid, Str("V"), Str("-"))
      , Mux(br_unit.brinfo.taken, Str("T"), Str("-"))
      , Mux(br_unit.debug_btb_pred, Str("B"), Str("_"))
      , Mux(br_unit.brinfo.mispredict, Str("M"), Str(" "))
      // chisel3 lacks %s support
      //, Mux(br_unit.brinfo.mispredict, Str(b_mgt + "MISPREDICT" + end), Str(grn + "          " + end))
      //, bpd_stage.io.req.bits.idx
      , Mux(take_pc, Str("T"), Str(" "))
      //, Mux(take_pc, Str("TAKE_PC"), Str(" "))
      , Mux(io.flush_take_pc, Str("F"),
        Mux(br_unit.take_pc, Str("B"),
          Mux(io.bp2_take_pc && !if_stalled, Str("P"),
            Mux(io.bp2_take_pc, Str("J"),
              Str(" ")))))
      //         , Mux(io.flush_take_pc, Str("FLSH"),
      //           Mux(br_unit.take_pc, Str("BRU "),
      //           Mux(io.bp2_take_pc && !if_stalled, Str("BP2"),
      //           Mux(io.bp2_take_pc, Str("J-s"),
      //                              Str(" ")))))
      , if_pc_next
      , if_stalled)

    // Fetch Stage 2
    //      printf("I$ Response: (%s) IF2_PC= 0x%x (mask:0x%x) \u001b[1;35m TODO need Str in Chisel3\u001b[0m  ----BrPred2:(%s,%s,%d) [btbtarg: 0x%x] jkilmsk:0x%x ->(0x%x)\n"
    //      printf("I$ Response: (%s) IF2_PC= 0x%x (mask:0x%x) [1;35m TODO need Str in Chisel3[0m  ----BrPred2:(%s,%s,%d) [btbtarg: 0x%x] jkilmsk:0x%x ->(0x%x)\n"

    printf("I$ Response: (%c) IF2_PC= 0x%x (mask:0x%x) "
      , Mux(io.imem.resp.valid, Str("v"), Str("-"))
      , io.imem.resp.payload.pc
      , io.imem.resp.payload.mask
    )

    if (fetch_width == 1)
    {
      printf("DASM(%x) "
        , io.imem.resp.payload.data(p.coreInstBits-1 downto 0)
      )
    }
    else if (fetch_width >= 2)
    {
      printf("DASM(%x)DASM(%x) "
        , io.imem.resp.payload.data(p.coreInstBits-1 downto 0)
        , io.imem.resp.payload.data(2*p.coreInstBits-1 downto p.coreInstBits)
      )
    }

    printf("----BrPred2:(%c,%c,%d) [btbtarg: 0x%x] jkilmsk:0x%x ->(0x%x)\n"
      , Mux(io.imem.resp.payload.btb.valid, Str("H"), Str("-"))
      , Mux(io.imem.resp.payload.btb.payload.taken, Str("T"), Str("-"))
      , io.imem.resp.payload.btb.payload.bridx
      , io.imem.resp.payload.btb.payload.target(19 downto 0)
      , io.bp2_pred_resp.mask
      , fetch_bundle.mask
    )
  }
}
