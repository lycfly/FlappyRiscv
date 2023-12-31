package boom_v1.predictor

import boom_v1.Parameters
import boom_v1.RISCVConstants.{ComputeBranchTarget, ComputeJALTarget, IsCall, IsReturn}
import boom_v1.Utils.{PriorityEncoder, Str}
import boom_v1.exec.BranchUnitResp
import boom_v1.fetch.FrontendResp
import boom_v1.predictor.boom.{BpdResp, BrPredictor, BrobBackendIo, NullBrPredictor, RandomBrPredictor}
import boom_v1.predictor.gshare.{GShareBrPredictor, SimpleGShareBrPredictor, SimpleGShareParameters}
import boom_v1.predictor.tage.TageBrPredictor
import boom_v1.predictor.gskew.GSkewBrPredictor
import boom_v1.Utils.PriorityEncoder
import boom_v1.decode.BranchDecode
import spinal.core._
import spinal.lib._
import spinal.lib.PriorityMux
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// RISCV Processor Branch Prediction Pipeline
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2014 Apr 23
//
// Access branch predictor and redirect the pipeline as necessary. Also in
// charge of JALs (direction and target are known).
//
// These stages are effectively in parallel with instruction fetch and decode.
// BHT look-up (bp1) is in parallel with I$ access, and Branch Decode (bp2)
// occurs before fetch buffer insertion.
//
// Currently, I ignore JALRs (either the BTB took care of it or it'll get
// mispredicted and kill everything behind it anyways).

class RedirectRequest(fetch_width: Int)(implicit p: Parameters) extends Bundle
{
  val target  = UInt(width = p.vaddrBits+1 bits)
  val br_pc   = UInt(width = p.vaddrBits+1 bits) // PC of the instruction changing control flow (to update the BTB with jumps)
  val idx     = UInt(width = log2Up(fetch_width) bits) // idx of br in fetch bundle (to mask out the appropriate fetch
  // instructions)
  val is_jump = Bool() // (only valid if redirect request is valid)
  val is_cfi  = Bool() // Is redirect due to a control-flow instruction?
  val is_taken= Bool() // (true if redirect is to "take" a branch,
  //  false if it's to request PC+4 for a mispred
}

// this information is shared across the entire fetch packet, and stored in the
// branch snapshots. Since it's not unique to an instruction, it could be
// compressed further. It can be de-allocated once the branch is resolved in
// Execute.
class BranchPredictionResp(implicit p: Parameters) extends Bundle
{
  val btb_resp_valid = Bool()
  val btb_resp       = new BTBResp

  val bpd_resp       = new BpdResp

  // used to tell front-end how to mask off instructions
  val mask           = Bits(width = p.fetchWidth bits)
  val br_seen        = Bool() // was a branch seen in this fetch packet?
}

// give this to each instruction/uop and pass this down the pipeline to the branch-unit
class BranchPrediction(implicit p: Parameters) extends Bundle
{
  val bpd_predict_val  = Bool() // did the bpd predict this instruction? (ie, tag hit in the BPD)
  val bpd_predict_taken= Bool() // did the bpd predict taken for this instruction?
  val btb_hit          = Bool() // this instruction was the br/jmp predicted by the BTB
  val btb_predicted    = Bool() // Does the BTB get credit for the prediction? (FU checks)

  val is_br_or_jalr    = Bool() // is this instruction a branch or jalr?
  // (need to allocate brob entry).

  def wasBTB = btb_predicted
}

class BranchPredictionStage(fetch_width: Int)(implicit p: Parameters) extends Module
{
  val io = new Bundle
  {
    val req        = master Stream(new RedirectRequest(fetch_width))
    val imem_resp  = slave Stream(new FrontendResp)
    val btb_resp   = slave Flow(new BTBResp)
    val npc        = in UInt(width = p.vaddrBitsExtended bits)
    val ras_update = master Flow(new RASUpdate)

    val pred_resp  = out(new BranchPredictionResp())
    val predictions= Vec(out(new BranchPrediction()), fetch_width)
    val br_unit    = in(new BranchUnitResp())

    val brob       = new BrobBackendIo(fetch_width)
    val flush      = in Bool()
    val status_prv = in UInt( width = p.PRV_SZ bits)
  }

  //-------------------------------------------------------------
  // Branch Prediction (BP1 Stage)

  val bp2_br_seen = (Bool())  // did we see a branch to make a prediction?
  // (and not overridden by an earlier jal or jr)
  val bp2_jr_seen = (Bool())  // did we see a JALR (not overriden by earlier jal but a taken br could)?
  val bp2_br_taken = (Bool()) // was there a taken branch in the bp2 stage
  // we use this to update the bpd's history register speculatively

  var br_predictor: BrPredictor = null
  if (p.ENABLE_BRANCH_PREDICTOR && p.tageParams.isDefined && p.tageParams.get.enabled)
  {
    br_predictor = (new TageBrPredictor(fetch_width = fetch_width,
      num_tables = p.tageParams.get.num_tables,
      table_sizes = p.tageParams.get.table_sizes,
      history_lengths = p.tageParams.get.history_lengths,
      tag_sizes = p.tageParams.get.tag_sizes,
      ubit_sz = p.tageParams.get.ubit_sz
    ))
  }
  else if (p.ENABLE_BRANCH_PREDICTOR && p.gskewParams.isDefined && p.gskewParams.get.enabled)
  {
    br_predictor = (new GSkewBrPredictor(fetch_width = fetch_width,
      history_length = p.gskewParams.get.history_length,
      dualported = p.gskewParams.get.dualported,
      enable_meta = p.gskewParams.get.enable_meta))
  }
  else if (p.ENABLE_BRANCH_PREDICTOR && p.gshareParams.isDefined && p.gshareParams.get.enabled)
  {
    br_predictor = (new GShareBrPredictor(fetch_width = fetch_width,
      history_length = p.gshareParams.get.history_length,
      dualported = p.gshareParams.get.dualported))
  }
  else if (p.ENABLE_BRANCH_PREDICTOR && p.simple_gshare)
  {
    br_predictor = (new SimpleGShareBrPredictor(fetch_width = fetch_width,
      history_length = SimpleGShareParameters().history_length))
  }
  else if (p.ENABLE_BRANCH_PREDICTOR && p.random_bpd)
  {
    br_predictor = (new RandomBrPredictor(fetch_width = fetch_width))
  }
  else
  {
    br_predictor = (new NullBrPredictor(fetch_width = fetch_width,
      history_length = p.GLOBAL_HISTORY_LENGTH))
  }

  br_predictor.io.req_pc := io.npc
  br_predictor.io.br_resolution <> io.br_unit.bpd_update
  br_predictor.io.hist_update_spec.valid := (bp2_br_seen || bp2_jr_seen) && io.req.ready
  br_predictor.io.hist_update_spec.payload.taken := bp2_br_taken || bp2_jr_seen
  br_predictor.io.resp.ready := io.req.ready

  io.brob <> br_predictor.io.brob
  br_predictor.io.flush := io.flush
  br_predictor.io.status_prv := io.status_prv

  //   val bpd_valid = br_predictor.io.resp.valid && (io.status_prv === UInt(rocket.PRV.U) || !Bool(ENABLE_BPD_UMODE_ONLY))
  val bpd_valid = br_predictor.io.resp.valid
  val bpd_bits = br_predictor.io.resp.payload


  //-------------------------------------------------------------
  // Branch Decode (BP2 Stage)
  //
  // 1) Which branch to take?
  // 2) Is there a jal earlier to take?
  // 3) Does the BTB override our prediction?
  //    - 3b) if no, verify BTB is correct?
  // 4) Update RAS

  // round off to nearest fetch boundary
  val aligned_pc = ~(~io.imem_resp.payload.pc | (U(fetch_width*p.coreInstBytes-1)))

  val is_br     = (Vec(Bool(),fetch_width))
  val is_jal    = (Vec(Bool(),fetch_width))
  val is_jr     = (Vec(Bool(),fetch_width))
  val br_targs  = (Vec(UInt(width=p.vaddrBits+1 bits),fetch_width))
  val jal_targs = (Vec(UInt(width=p.vaddrBits+1 bits),fetch_width))

  for (i <- 0 until fetch_width)
  {
    val inst = io.imem_resp.payload.data(i*p.coreInstBits+p.coreInstBits-1 downto i*p.coreInstBits)
    val bpd_decoder = (new BranchDecode)
    bpd_decoder.io.inst := inst

    is_br(i)  := bpd_decoder.io.is_br   && io.imem_resp.payload.mask(i)
    is_jal(i) := bpd_decoder.io.is_jal  && io.imem_resp.payload.mask(i)
    is_jr(i)  := bpd_decoder.io.is_jalr && io.imem_resp.payload.mask(i)

    val pc = aligned_pc + U(i << 2)
    br_targs(i)  := ComputeBranchTarget(pc, inst, p.xLen, p.coreInstBytes)
    jal_targs(i) := ComputeJALTarget(pc, inst, p.xLen, p.coreInstBytes)
  }


  //-------------------------------------------------------------
  // Output (make an actual prediction/redirect request)

  // There are many predictions vying for priority.
  // The following are equal priority - whosever has
  // the instruction earliest in program-order wins:
  //    - JALs
  //    - BTB (JALs/JRs)
  //    - BPD (branches)
  //
  // At a lower priority is the BTB predicting branches:
  //    - BTB (branches)
  //
  // If the BPD defers (bpd_valid == false), then the BTB's
  // branch prediction stand.

  val bpd_predictions  = is_br.asBits & bpd_bits.takens.asBits
  val bpd_br_taken     = bpd_predictions.orR && bpd_valid
  val bpd_br_idx       = PriorityEncoder(bpd_predictions)
  val bpd_jal_val      = is_jal.reduce(_|_)
  val bpd_jr_val       = is_jr.reduce(_|_)
  val bpd_jal_idx      = PriorityEncoder(is_jal.asBits)
  val bpd_br_beats_jal = bpd_br_taken && (!bpd_jal_val || (bpd_br_idx < bpd_jal_idx))
  val bpd_req_idx      = Mux(bpd_br_beats_jal, bpd_br_idx, bpd_jal_idx)
  val bpd_req_target   = Mux(bpd_br_beats_jal, br_targs(bpd_br_idx), jal_targs(bpd_jal_idx))



  // For the particular instruction the BTB predicted, does the BPD agree with the direction?
  // (this is only valid if the BTB is predicting a branch.)
  val bpd_agrees_with_btb = !(IsIdxAMatch(io.btb_resp.payload.bridx.asUInt, bpd_predictions.asUInt) ^
    io.btb_resp.payload.taken)

  // bpd will make a redirection request (either for a br or jal)
  // for "taking" a branch or JAL.
  val bpd_br_fire = Bool(false)
  val bpd_jal_fire = Bool(false)
  // If the BTB predicted taken, and the BPD disagrees and believes no branch
  // is taken, we must instead redirect the FrontEnd to fetch the "next packet"
  // in program order (PC+4-ish, if you will).
  val bpd_nextline_fire = Bool(false)
  val nextline_pc = aligned_pc + U(fetch_width*p.coreInstBytes)
  // BTB provides a suggested "valid instruction" mask, based on its prediction.
  // Should we override its mask?
  val override_btb = Bool(false)

  // Is the predicted instruction a control-flow instruction?
  // Used to invalidate BTB entries due to it predicting on
  // a non-branch/non-jump instruction.
  val is_cfi = Bool(true)

  // does the index match on a true bit in the mask?
  def IsIdxAMatch(idx: UInt, mask: UInt) : Bool =
  {
    mask(idx)
  }
  val btb_predicted_br          = IsIdxAMatch(io.btb_resp.payload.bridx.asUInt, is_br.asBits.asUInt)
  val btb_predicted_br_taken    = btb_predicted_br && io.btb_resp.payload.taken
  val btb_predicted_br_nottaken = btb_predicted_br && !io.btb_resp.payload.taken
  val btb_predicted_jump        = IsIdxAMatch(io.btb_resp.payload.bridx.asUInt, is_jal.asBits.asUInt | is_jr.asBits.asUInt)
  val btb_predicted_jal         = IsIdxAMatch(io.btb_resp.payload.bridx.asUInt, is_jal.asBits.asUInt)

  val btb_predicted_wrong_jal_target = btb_predicted_jal && io.btb_resp.payload.target =/= jal_targs(bpd_jal_idx)

  when (io.btb_resp.valid)
  {
    // BTB made a prediction -
    // make a redirect request if:
    //    - if the BPD (br) or JAL comes earlier than the BTB's redirection
    //    - if both the BTB and the BPD predicted a branch, the BPD wins
    //       * involves refetching the latter half of the packet if we "undo"
    //          the BTB's taken branch.

    when (btb_predicted_jump) // both JAL and JRs
    {
      val btb_nt = !io.btb_resp.payload.taken
      bpd_br_fire  := bpd_br_beats_jal && bpd_br_taken && (bpd_br_idx < io.btb_resp.payload.bridx.asUInt)
      bpd_jal_fire := !bpd_br_beats_jal && bpd_jal_val &&
        ((bpd_jal_idx < io.btb_resp.payload.bridx.asUInt) || btb_nt || btb_predicted_wrong_jal_target)

      when (io.imem_resp.valid)
      {
        when (!io.btb_resp.payload.taken)
        {
          val btb_predicted_jr = IsIdxAMatch(io.btb_resp.payload.bridx.asUInt, is_jr.asBits.asUInt)
          // if JR but predicted not TAKEN, do nothing and let BrUnit fix mispredicton.
          assert (bpd_br_fire ||
            (bpd_jal_fire && (bpd_jal_idx <= io.btb_resp.payload.bridx.asUInt)) ||
            btb_predicted_jr,
            "[bpd_pipeline] BTB predicted a jump but didn't take it, and we are failing to correct it.")
        }
      }
    }
      .elsewhen (btb_predicted_br_taken)
      {
        // overrule the BTB if
        //    1. either a jump or branch occurs earlier
        //          (bpd_jal_fire, bpd_br_fire)
        //    2. OR the BPD predicts the BTB's branch as not taken...
        //       a. and no other branch taken (bpd_nextline_fire)
        //       b. a later branch as taken (bpd_br_fire)
        //       c. a later jump is present (bpd_jal_fire)

        // does the bpd predict the branch is taken too? (assuming bpd_valid)
        val bpd_agrees_with_btb = bpd_predictions(io.btb_resp.payload.bridx.asUInt)

        bpd_jal_fire := !bpd_br_beats_jal && bpd_jal_val &&
          ((bpd_jal_idx < io.btb_resp.payload.bridx.asUInt) ||
            (bpd_valid && !bpd_agrees_with_btb))
        bpd_br_fire  := bpd_br_beats_jal && bpd_br_taken &&
          (bpd_br_idx < io.btb_resp.payload.bridx.asUInt ||  // earlier than BTB's branch
            (bpd_valid && !bpd_agrees_with_btb))     // taken later
        //                         (bpd_valid && !bpd_agrees_with_btb && io.btb_resp.bits.bridx =/= bpd_br_idx)) // taken later
        // than BTB's branch

        bpd_nextline_fire := bpd_valid && !bpd_predictions.orR && !bpd_jal_val
        override_btb := bpd_valid && !bpd_agrees_with_btb

        when (bpd_nextline_fire)
        {
          assert (override_btb, "[bpd_pipeline] redirecting the PC without overriding the BTB.")
        }
        when (override_btb)
        {
          assert (bpd_nextline_fire || bpd_jal_fire || bpd_br_fire,
            "[bpd_pipeline] overriding the BTB without redirecting the PC.")
        }
      }
      .elsewhen (btb_predicted_br_nottaken)
      {
        // completely overrule the BTB if it predicted not-taken, but the BPD is predicted taken
        bpd_br_fire  := bpd_br_beats_jal && bpd_br_taken
        bpd_jal_fire := !bpd_br_beats_jal && bpd_jal_val
      }
      .otherwise
      {
        when (io.imem_resp.valid)
        {
          // Thanks to uncacheable regions (also, the BTB is never invalidated),
          // The BTB may actually predict things that aren't branches!
          // But we must undo these "mispredictions"!
          //printf ("[bpd_pipeline] BTB resp is valid, but didn't detect what it predicted.")
          override_btb := Bool(true)
          is_cfi := Bool(false)

          // but is there a branch or jump we need to handle? Or just fetch the nextline?
          bpd_br_fire  := bpd_br_beats_jal && bpd_br_taken
          bpd_jal_fire := !bpd_br_beats_jal && bpd_jal_val
          bpd_nextline_fire := !bpd_br_fire && !bpd_jal_fire
        }
      }
  }
    .otherwise
    {
      // BTB made no prediction - let the BPD do what it wants
      bpd_br_fire  := bpd_br_beats_jal
      bpd_jal_fire := bpd_jal_val && !bpd_br_fire
    }

  assert (CountOne(Vec(bpd_br_fire, bpd_jal_fire, bpd_nextline_fire)) <= U(1),
    "[bpd_pipeline] mutually-exclusive signals firing")


  io.req.valid        := io.imem_resp.valid &&
    (bpd_br_fire || bpd_jal_fire || bpd_nextline_fire) &&
    !io.imem_resp.payload.xcpt_if
  io.req.payload.target  := Mux(bpd_nextline_fire, nextline_pc, bpd_req_target)
  io.req.payload.idx     := Mux(bpd_nextline_fire, U(fetch_width-1), bpd_req_idx)
  io.req.payload.br_pc   := aligned_pc + (io.req.payload.idx << U(2))
  io.req.payload.is_jump := !bpd_br_beats_jal
  io.req.payload.is_cfi  := is_cfi
  io.req.payload.is_taken:= bpd_br_fire || bpd_jal_fire

  io.pred_resp.btb_resp_valid   := io.btb_resp.valid
  io.pred_resp.btb_resp         := io.btb_resp.payload
  io.pred_resp.bpd_resp         := bpd_bits
  io.pred_resp.br_seen          := bp2_br_seen

  private def KillMask(m_enable: Bool, m_idx: UInt, m_width: Int): UInt =
  {
    val mask = (Bits(width = m_width bits))
    mask := (m_enable#*m_width) & ((U(1, 1 bits)#*m_width) << U(1) << m_idx)
    mask.asUInt
  }
  // mask out instructions after predicted branch
  val bpd_kill_mask = KillMask(io.req.valid,
    io.req.payload.idx,
    p.fetchWidth)
  // mask out instructions after first jr (doesn't matter if predicted correctly or not!)
  val jr_kill_mask = KillMask(is_jr.reduce(_|_),
    PriorityEncoder(is_jr.asBits),
    p.fetchWidth)
  // if we accept the BTB's prediction, mask out instructions after its prediction
  val btb_kill_mask = KillMask(io.btb_resp.valid && io.btb_resp.payload.taken &&
    !(bpd_br_fire || bpd_jal_fire || bpd_nextline_fire),
    PriorityEncoder(io.btb_resp.payload.bridx),
    p.fetchWidth)

  val btb_mask = Mux(override_btb || !io.btb_resp.valid,
    U(1,1 bits)#*p.fetchWidth,
    io.btb_resp.payload.mask)
  io.pred_resp.mask := ~bpd_kill_mask & ~jr_kill_mask & btb_mask.asUInt


  for (w <- 0 until p.FETCH_WIDTH)
  {
    io.predictions(w).is_br_or_jalr := is_br(w) || is_jr(w)
    io.predictions(w).bpd_predict_taken := bpd_predictions(w) && bpd_valid && !bpd_nextline_fire
    io.predictions(w).btb_predicted := io.btb_resp.valid &&
      !(bpd_nextline_fire || bpd_br_fire || bpd_jal_fire)
    io.predictions(w).btb_hit := Mux(io.btb_resp.payload.bridx === U(w),
      io.btb_resp.valid, Bool(false))
    io.predictions(w).bpd_predict_val   := bpd_valid
  }

  bp2_br_seen := io.imem_resp.valid &&
    !io.imem_resp.payload.xcpt_if &&
    is_br.reduce(_|_) &&
    (!bpd_jal_val || (PriorityEncoder(is_br.asBits) < PriorityEncoder(is_jal.asBits))) &&
    (!bpd_jr_val || (PriorityEncoder(is_br.asBits) < PriorityEncoder(is_jr.asBits)))
  bp2_jr_seen := io.imem_resp.valid &&
    !io.imem_resp.payload.xcpt_if &&
    is_jr.reduce(_|_) &&
    (!bpd_jal_val || (PriorityEncoder(is_jr.asBits) < PriorityEncoder(is_jal.asBits)))
  bp2_br_taken := bpd_br_fire || (io.btb_resp.valid && btb_predicted_br_taken && !bpd_nextline_fire && !override_btb)

  //-------------------------------------------------------------
  // Look for CALL and RETURN for RAS shenanigans.
  // TODO flush_take_pc should probably be given to the branch unit, instead of resetting it here?
  // NOTE: what about branch taken earlier?

  val jumps    = is_jal.asBits | is_jr.asBits
  val jmp_idx  = PriorityEncoder(jumps)
  val jmp_inst = (io.imem_resp.payload.data >> (jmp_idx*U(p.coreInstBits)))(p.coreInstBits-1 downto 0)
  val is_call  = IsCall(jmp_inst)
  val is_ret   = IsReturn(jmp_inst)
  io.ras_update.valid           := io.imem_resp.valid &&
    !io.imem_resp.payload.xcpt_if &&
    jumps.orR &&
    !bpd_br_beats_jal &&
    io.req.ready &&
    !io.flush
  io.ras_update.payload.cfiType     := is_call ? (CFIType.call | (is_ret ? (CFIType.ret | CFIType.branch)))
  io.ras_update.payload.returnAddr := aligned_pc + (jmp_idx << U(2)) + U(4)
//  io.ras_update.payload.prediction := io.btb_resp

  //-------------------------------------------------------------
  // printfs

  if (p.DEBUG_PRINTF)
  {
    printf("bp2_aligned_pc: 0x%x BHT:(%c 0x%x, %d) p:%x (%d) b:%x j:%x (%d) %c %c\n"
      , aligned_pc, Mux(io.req.valid, Str("T"), Str("-")), io.req.payload.target, io.req.payload.idx
      , bpd_predictions.asBits, bpd_br_idx, is_br.asBits, is_jal.asBits, bpd_jal_idx
      , Mux(bpd_br_beats_jal, Str("B"), Str("J")), Mux(bpd_nextline_fire, Str("N"), Str("-"))
    )
  }

  //-------------------------------------------------------------
  // asserts

  when (io.imem_resp.valid && io.btb_resp.valid && io.btb_resp.payload.taken && !io.imem_resp.payload.xcpt_if)
  {
    val idx = io.btb_resp.payload.bridx.asUInt
    val targ = Mux(is_br(idx), br_targs(idx), jal_targs(idx))
    when (!is_jr(idx) && !(bpd_nextline_fire))
    {
      //         assert (io.btb_resp.bits.target === targ(vaddrBits-1,0),
      //            "[bpd_pipeline] BTB is jumping to an invalid target.")
      when (io.btb_resp.payload.target =/= targ(p.vaddrBits-1 downto 0))
      {
        // TODO remove this... BTBs can now just predict total garbage
        //printf("[bpd_pipeline] BTB is jumping to an invalid target.\n")
      }
    }
  }

  if (!p.enableBTB)
  {
    assert (!(io.btb_resp.valid), "[bpd_pipeline] BTB predicted, but it's been disabled.")
  }

}
