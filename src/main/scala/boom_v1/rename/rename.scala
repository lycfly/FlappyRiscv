package boom_v1.rename
import boom_v1.ScalarOpConstants._
import boom_v1.Utils.{GetNewBrMask, MuxCase, PriorityEncoder}
import boom_v1.exec.{BrResolutionInfo, GetPredictionInfo}
import boom_v1.predictor.BranchPredictionResp
import boom_v1.{MicroOp, Parameters}
import spinal.core._
import spinal.lib._

//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// RISCV Processor Datapath: Rename Logic
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2012 Feb 14

  class RenameMapTableElementIo(pl_width: Int)(implicit p: Parameters) extends Bundle
  {
    val element            = out UInt(p.PREG_SZ bits)

    val wens               = in Vec(Bool(), pl_width)
    val ren_pdsts          = in Vec(UInt(width=p.PREG_SZ bits), pl_width) // rename physical destination

    val ren_br_vals        = in Vec(Bool(), pl_width)
    val ren_br_tags        = in Vec(UInt(width=p.BR_TAG_SZ bits), pl_width)

    val br_mispredict      = in Bool()
    val br_mispredict_tag  = in UInt(p.BR_TAG_SZ bits)

    // rollback (on exceptions)
    // TODO REMOVE THIS ROLLBACK PORT, since wens is mutually exclusive with rollback_wens
    val rollback_wen        = in Bool()
    val rollback_stale_pdst = in UInt(p.PREG_SZ bits)

    // TODO scr option
    val flush_pipeline      = in Bool()
    val commit_wen          = in Bool()
    val commit_pdst         = in UInt(p.PREG_SZ bits)
    val committed_element   = out UInt(p.PREG_SZ bits)

  }

  class RenameMapTableElement(pipeline_width: Int)(implicit p: Parameters) extends Module
  {
    val io = new RenameMapTableElementIo(pipeline_width)

    // Note: I don't use a "valid" signal, since it's annoying to deal with and
    // only necessary until the map tables are filled. So instead I reset the
    // map table to all point to P0. I'm not sure which is less expensive.  The
    // corner-case to deal with is the "stale" register needs to be correct and
    // valid, so that freeing it won't free an actual register that was handed
    // out in the meantime. A software solution is also possible, but I'm
    // unwilling to trust that.

    val element = RegInit(U(0, p.PREG_SZ bits))

    // handle branch speculation
    val element_br_copies = Mem(UInt(width = p.PREG_SZ bits), p.MAX_BR_COUNT)


    // this is possibly the hardest piece of code I have ever had to reason about in my LIFE.
    // Or maybe that's the 5am talking.
    // on every branch, make a copy of the rename map table state
    // if jal/jalr, we want to capture our own setting of this register
    // We need to know the AGE of the branch!
    // 1st, is "wen" (incoming)
    // 2nd, is older instructions in same bundle
    // 3rd, current element

    for (w <- 0 until pipeline_width)
    {
      var elm_cases = Array((Bool(false),  U(0,p.PREG_SZ bits)))

      for (xx <- w to 0 by -1)
      {
        elm_cases ++= Array((io.wens(xx),  io.ren_pdsts(xx)))
      }

      when (io.ren_br_vals(w))
      {
        element_br_copies(io.ren_br_tags(w)) := MuxCase(element, elm_cases)
      }
    }


    // reset table on mispredict
    when(io.br_mispredict)
    {
      element := element_br_copies(io.br_mispredict_tag)
    }
      // rollback to the previous mapping
      .elsewhen (io.rollback_wen)
      {
        element := io.rollback_stale_pdst
      }
      // free list is giving us a new pdst
      .elsewhen (io.wens.reduce(_|_))
      {
        // give write priority to the last instruction in the bundle
        element := PriorityMux(io.wens.reverse, io.ren_pdsts.reverse)
      }

    if (p.ENABLE_COMMIT_MAP_TABLE)
    {
      val committed_element = RegInit(U(0,p.PREG_SZ bits))
      when (io.commit_wen)
      {
        committed_element := io.commit_pdst
      }
      when (io.flush_pipeline)
      {
        element := committed_element
      }
      io.committed_element := committed_element
    }

    // outputs
    io.element  := element
  }

  //-------------------------------------------------------------
  //-------------------------------------------------------------

  class FreeListIo(num_phys_registers: Int, pl_width: Int)(implicit p: Parameters) extends Bundle
  {
    val req_preg_vals = in Vec(Bool(), pl_width)
    val req_pregs     = out Vec(UInt(width=log2Up(num_phys_registers) bits), pl_width)

    // committed and newly freed register
    val enq_vals      = in Vec(Bool(), pl_width)
    val enq_pregs     = in Vec(UInt(width=log2Up(num_phys_registers) bits), pl_width)

    // do we have space to service incoming requests? (per inst granularity)
    val can_allocate  = out Vec(Bool(), pl_width)

    // handle branches (save copy of freelist on branch, merge on mispredict)
    val ren_br_vals   = in Vec(Bool(), pl_width)
    val ren_br_tags   = in Vec(UInt(width=p.BR_TAG_SZ bits), pl_width)

    // handle mispredicts
    val br_mispredict_val = in Bool()
    val br_mispredict_tag = in UInt(p.BR_TAG_SZ bits)

    // rollback (on exceptions)
    val rollback_wens  = in Vec(Bool(), pl_width)
    val rollback_pdsts = in Vec(UInt(width=log2Up(num_phys_registers) bits),pl_width)

    // or...
    // TODO there are TWO free-list IOs now, based on constants. What is the best way to handle these two designs?
    // perhaps freelist.scala, and instantiate which-ever one I want?
    // TODO naming is inconsistent
    // TODO combine with rollback, whatever?
    val flush_pipeline = in Bool()
    val com_wens       = in Vec(Bool(), pl_width)
    val com_uops       = in Vec(new MicroOp(), pl_width)

    val debug = out(new DebugFreeListIO(num_phys_registers))
  }

  class DebugFreeListIO(num_phys_registers: Int) extends Bundle
  {
    val freelist = Bits(width=num_phys_registers bits)
    val isprlist = Bits(width=num_phys_registers bits)
  }

  // provide a fixed set of renamed destination registers
  // i.e., it doesn't matter if a previous UOP needs a pdst or not
  // this prevents a dependency chain from existing between UOPs when trying to
  // compute a pdst to give away (as well as computing if an available free
  // register exists
  class RenameFreeList(num_phys_registers: Int // number of physical registers
                       , pl_width: Int          // pipeline width ("dispatch group size")
                      )(implicit p: Parameters) extends Module
  {
    val io = new FreeListIo(num_phys_registers, pl_width)

    // ** FREE LIST TABLE ** //
    val free_list = RegInit((~B(1,num_phys_registers bits))) // reg0 is tie to 0 and is not in freelist

    // track all allocations that have occurred since branch passed by
    // can quickly reset pipeline on branch mispredict
    val allocation_lists = Reg(Vec(Bits(width = num_phys_registers bits), p.MAX_BR_COUNT))

    // TODO why is this a Vec? can I do this all on one bit-vector?
    val enq_mask = (Vec(Bits(width = num_phys_registers bits), pl_width))

    // ------------------------------------------
    // find new,free physical registers

    val requested_pregs_oh_array = Array.fill(pl_width,num_phys_registers){Bool(false)}
    val requested_pregs_oh       = (Vec(Bits(width=num_phys_registers bits), pl_width))
    val requested_pregs          = (Vec(UInt(width=log2Up(num_phys_registers) bits), pl_width))
    var allocated                = (Vec(Bool(), pl_width)) // did each inst get allocated a register?

    // init
    for (w <- 0 until pl_width)
    {
      allocated(w) := Bool(false)
    }


    for (i <- 1 until num_phys_registers) // note: p0 stays zero
    {
      val next_allocated = Vec(Bool(), pl_width)
      var can_allocate = free_list(i)

      for (w <- 0 until pl_width)
      {
        requested_pregs_oh_array(w)(i) = can_allocate && !allocated(w)

        next_allocated(w) := can_allocate | allocated(w)
        can_allocate = can_allocate && allocated(w)
      }

      allocated = next_allocated
    }

    for (w <- 0 until pl_width)
    {
      requested_pregs_oh(w) := Vec(requested_pregs_oh_array(w)).asBits
      requested_pregs(w) := PriorityEncoder(requested_pregs_oh(w))
    }


    // ------------------------------------------
    // Calculate next Free List
    val req_free_list = (Bits(width = num_phys_registers bits))
    val enq_free_list = (Bits(width = num_phys_registers bits))
    req_free_list := free_list
    enq_free_list := free_list

    // ** Set requested PREG to "Not Free" ** //

    // bit vector of newly allocated physical registers
    var just_allocated_mask = B(0, num_phys_registers bits)

    // track which allocation_lists just got cleared out by a branch,
    // to enforce a write priority to allocation_lists()
    val br_cleared = (Vec(Bool(), p.MAX_BR_COUNT))
    for (i <- 0 until p.MAX_BR_COUNT) { br_cleared(i) := Bool(false) }

    for (w <- pl_width-1 to 0 by -1)
    {
      // When branching, start a fresh copy of the allocation_list
      // but don't forget to bypass in the allocations within our bundle
      when (io.ren_br_vals(w))
      {
        allocation_lists(io.ren_br_tags(w)) := just_allocated_mask
        br_cleared(io.ren_br_tags(w)) := Bool(true)
      }

      // check that we both request a register and was able to allocate a register
      just_allocated_mask = Mux(io.req_preg_vals(w) && allocated(w), requested_pregs_oh(w) | just_allocated_mask,
        just_allocated_mask)
    }

    for (i <- 0 until p.MAX_BR_COUNT)
    {
      when (!br_cleared(i))
      {
        allocation_lists(i) := allocation_lists(i) | just_allocated_mask
      }
    }


    // ** Set enqueued PREG to "Free" ** //
    for (w <- 0 until pl_width)
    {
      enq_mask(w) := B(0,num_phys_registers bits)
      when (io.enq_vals(w))
      {
        enq_mask(w) := U(1) << io.enq_pregs(w)
      }
        .elsewhen (io.rollback_wens(w))
        {
          enq_mask(w) := U(1) << io.rollback_pdsts(w)
        }
    }


    // Update the Free List
    when (!io.br_mispredict_val)
    {
      free_list := (free_list & ~(just_allocated_mask)) | (enq_mask.reduce(_|_))
    }

    // Handle Misprediction
    //merge misspeculated allocation_list with free_list
    val allocation_list = (Bits(width = num_phys_registers bits))
    allocation_list := allocation_lists(io.br_mispredict_tag)

    when (io.br_mispredict_val)
    {
      // include newly freed register as well!
      free_list := allocation_list | free_list | (enq_mask.reduce(_|_))

      // set other branch allocation_lists to zero where allocation_list(j) == 1...
      for (i <- 0 until p.MAX_BR_COUNT)
      {
        allocation_lists(i) := allocation_lists(i) & ~allocation_list
      }
    }


    // OPTIONALLY: handle single-cycle resets
    // Committed Free List tracks what the free list is at the commit point,
    // allowing for a single-cycle reset of the rename state on a pipeline flush.
    if (p.ENABLE_COMMIT_MAP_TABLE)
    {
      val committed_free_list = RegInit((~B(1,num_phys_registers bits)))

      val com_mask = (Vec(Bits(width=num_phys_registers bits),pl_width))
      val stale_mask = (Vec(Bits(width=num_phys_registers bits),pl_width))
      for (w <- 0 until pl_width)
      {
        com_mask(w) := B(0,width=num_phys_registers bits)
        stale_mask(w) := B(0,width=num_phys_registers bits)
        when (io.com_wens(w))
        {
          com_mask(w) := U(1) << io.com_uops(w).pdst
          stale_mask(w) := U(1) << io.com_uops(w).stale_pdst
        }
      }

      committed_free_list := (committed_free_list & ~(com_mask.reduce(_|_))) | stale_mask.reduce(_|_)

      when (io.flush_pipeline)
      {
        free_list := committed_free_list
      }
      io.debug.isprlist := committed_free_list
    }



    // ** SET OUTPUTS ** //
    io.req_pregs := requested_pregs

    io.can_allocate := allocated

    io.debug.freelist := free_list
  }


  //-------------------------------------------------------------
  //-------------------------------------------------------------

  // internally bypasses newly busy registers (.write) to the read ports (.read)
  // num_operands is the maximum number of operands per instruction (.e.g., 2 normally, but 3 if FMAs are supported)
  class BusyTableIo(pipeline_width:Int, num_read_ports:Int, num_wb_ports:Int)(implicit p: Parameters) extends Bundle
  {
    // reading out the busy bits
    val p_rs           = in Vec(UInt(width=p.PREG_SZ bits), num_read_ports)
    val p_rs_busy      = out Vec(Bool(),num_read_ports)

    def prs(i:Int, w:Int):UInt      = p_rs     (w+i*pipeline_width)
    def prs_busy(i:Int, w:Int):Bool = p_rs_busy(w+i*pipeline_width)

    // marking new registers as busy
    val allocated_pdst = Vec(slave Flow(UInt(width=p.PREG_SZ bits)),pipeline_width)

    // marking registers being written back as unbusy
    val unbusy_pdst    = Vec(slave Flow(UInt(width = p.PREG_SZ bits)),num_wb_ports)

    val debug = new Bundle { val bsy_table= out Bits(width=p.PHYS_REG_COUNT bits)}
  }

  // Register P0 is always NOT_BUSY, and cannot be set to BUSY
  // Note: I do NOT bypass from newly busied registers to the read ports.
  // That bypass check should be done elsewhere (this is to get it off the
  // critical path).
  class BusyTable(pipeline_width:Int, num_read_ports:Int, num_wb_ports:Int)(implicit p: Parameters) extends Module
  {
    val io = new BusyTableIo(pipeline_width, num_read_ports, num_wb_ports)

    def BUSY     = Bool(true)
    def NOT_BUSY = Bool(false)

    //TODO BUG chisel3
    //val table_bsy = Reg(init=Bits(0,PHYS_REG_COUNT))
    val table_bsy = RegInit(Vec.fill(p.PHYS_REG_COUNT){Bool(false)})

    for (wb_idx <- 0 until num_wb_ports)
    {
      when (io.unbusy_pdst(wb_idx).valid)
      {
        table_bsy(io.unbusy_pdst(wb_idx).payload) := NOT_BUSY
      }
    }

    for (w <- 0 until pipeline_width)
    {
      when (io.allocated_pdst(w).valid && io.allocated_pdst(w).payload =/= U(0))
      {
        table_bsy(io.allocated_pdst(w).payload) := BUSY
      }
    }

    // handle bypassing a clearing of the busy-bit
    for (ridx <- 0 until num_read_ports)
    {
      val just_cleared = io.unbusy_pdst.map(p => p.valid && (p.payload === io.p_rs(ridx))).reduce(_|_)
      // note: no bypassing of the newly busied (that is done outside this module)
      io.p_rs_busy(ridx) := (table_bsy(io.p_rs(ridx)) && !just_cleared)
    }

    io.debug.bsy_table := table_bsy.asBits
  }

  //-------------------------------------------------------------
  //-------------------------------------------------------------
  //-------------------------------------------------------------
  //-------------------------------------------------------------

  class RenameStageIO(pl_width: Int, num_wb_ports: Int)(implicit p: Parameters) extends Bundle
  {
    val ren_mask  = Vec(out Bool(), pl_width) // mask of valid instructions
    val inst_can_proceed = Vec(out Bool(), pl_width)

    val kill      = in Bool()

    val dec_mask  = Vec(in Bool(), pl_width)

    val dec_uops  = in(Vec(new MicroOp(), pl_width))
    val ren_uops  = out(Vec(new MicroOp(), pl_width))

    val ren_pred_info = in(new BranchPredictionResp())

    // branch resolution (execute)
    val brinfo    = in(new BrResolutionInfo())
    val get_pred  = new GetPredictionInfo().flip

    val dis_inst_can_proceed = Vec(in Bool(), p.DISPATCH_WIDTH)

    // issue stage (fast wakeup)
    val wb_valids = Vec(in Bool(), num_wb_ports)
    val wb_pdsts  = Vec(in UInt(width=p.PREG_SZ bits), num_wb_ports)

    // commit stage
    val com_valids = Vec(in Bool(), pl_width)
    val com_uops   = Vec(in(new MicroOp()), pl_width)
    val com_rbk_valids = Vec(in Bool(),pl_width)

    val flush_pipeline = in Bool() // TODO only used for SCR (single-cycle reset)

    val debug = out(new DebugRenameStageIO())
  }

  class DebugRenameStageIO(implicit p: Parameters) extends Bundle
  {
    val freelist = Bits(width=p.PHYS_REG_COUNT bits)
    val isprlist = Bits(width=p.PHYS_REG_COUNT bits)
    val bsy_table = UInt(width=p.PHYS_REG_COUNT bits)
  }

  class RenameStage(pl_width: Int, num_wb_ports: Int)(implicit p: Parameters) extends Module
  {
    val io = new RenameStageIO(pl_width, num_wb_ports)

    val ren_br_vals = Vec(Bool(),pl_width)
    val freelist_can_allocate = Vec(Bool(),pl_width)

    val max_operands = if(p.usingFPU) 3 else 2

    //-------------------------------------------------------------
    // Set outputs up... we'll write in the pop*/pdst info below
    for (w <- 0 until pl_width)
    {
      io.ren_mask(w)         := io.dec_mask(w) && io.inst_can_proceed(w) && !io.kill
      io.ren_uops(w)         := io.dec_uops(w)
      io.ren_uops(w).br_mask := GetNewBrMask(io.brinfo, io.dec_uops(w))
      ren_br_vals(w)         := Mux(io.dec_mask(w), io.dec_uops(w).allocate_brtag, Bool(false))
    }


    //-------------------------------------------------------------
    // Rename Table

    val map_table_io = Vec.fill(p.LOGICAL_REG_COUNT) { (new RenameMapTableElement(p.DECODE_WIDTH)).io }

    for (i <- 0 until p.LOGICAL_REG_COUNT)
    {
      map_table_io(i).rollback_wen := Bool(false)
      map_table_io(i).rollback_stale_pdst := io.com_uops(0).stale_pdst

      map_table_io(i).commit_wen := Bool(false)
      map_table_io(i).commit_pdst := io.com_uops(0).pdst

      for (w <- 0 until pl_width)
      {
        map_table_io(i).wens(w)        := io.ren_uops(w).ldst === U(i) &&
          io.ren_mask(w) &&
          io.ren_uops(w).ldst_val &&
          (io.ren_uops(w).dst_rtype === RT_FIX.value ||
            io.ren_uops(w).dst_rtype === RT_FLT.value) &&
          !io.kill &&
          io.inst_can_proceed(w) &&
          freelist_can_allocate(w)
        map_table_io(i).ren_pdsts(w)   := io.ren_uops(w).pdst

        map_table_io(i).ren_br_vals(w) := ren_br_vals(w)
        map_table_io(i).ren_br_tags(w) := io.ren_uops(w).br_tag
      }

      map_table_io(i).br_mispredict     := io.brinfo.mispredict
      map_table_io(i).br_mispredict_tag := io.brinfo.tag

      map_table_io(i).flush_pipeline    := io.flush_pipeline
    }

    // backwards, because rollback must give highest priority to 0 (the oldest instruction)
    for (w <- pl_width-1 to 0 by -1)
    {
      val ldst = io.com_uops(w).ldst

      when (io.com_rbk_valids(w))
      {
        map_table_io(ldst).rollback_wen        := Bool(true)
        map_table_io(ldst).rollback_stale_pdst := io.com_uops(w).stale_pdst
      }
    }

    if (p.ENABLE_COMMIT_MAP_TABLE)
    {
      for (w <- 0 until pl_width)
      {
        val ldst = io.com_uops(w).ldst
        when (io.com_valids(w) && (io.com_uops(w).dst_rtype === RT_FIX.value || io.com_uops(w).dst_rtype === RT_FLT.value))
        {
          map_table_io(ldst).commit_wen := Bool(true)
          map_table_io(ldst).commit_pdst := io.com_uops(w).pdst
        }
      }
    }

    // read out the map-table entries ASAP, then deal with bypassing busy-bits later
    private val map_table_output = (Vec(UInt(width=p.PREG_SZ bits),pl_width*3))
    def map_table_prs1(w:Int) = map_table_output(w+0*pl_width)
    def map_table_prs2(w:Int) = map_table_output(w+1*pl_width)
    def map_table_prs3(w:Int) = map_table_output(w+2*pl_width)


    for (w <- 0 until pl_width)
    {
      map_table_prs1(w) := map_table_io(io.ren_uops(w).lrs1).element
      map_table_prs2(w) := map_table_io(io.ren_uops(w).lrs2).element
      if (max_operands > 2)
        map_table_prs3(w) := map_table_io(io.ren_uops(w).lrs3).element
      else
        map_table_prs3(w) := U(0)
    }

    // Bypass the physical register mappings
    val prs1_was_bypassed = (Vec(Bool(),pl_width))
    val prs2_was_bypassed = (Vec(Bool(),pl_width))
    val prs3_was_bypassed = (Vec(Bool(),pl_width))

    for (w <- 0 until pl_width)
    {
      var rs1_cases =  Array((Bool(false),  U(0,p.PREG_SZ bits)))
      var rs2_cases =  Array((Bool(false),  U(0,p.PREG_SZ bits)))
      var rs3_cases =  Array((Bool(false),  U(0,p.PREG_SZ bits)))
      var stale_cases= Array((Bool(false),  U(0,p.PREG_SZ bits)))

      prs1_was_bypassed(w) := Bool(false)
      prs2_was_bypassed(w) := Bool(false)
      prs3_was_bypassed(w) := Bool(false)

      // Handle bypassing new physical destinations to operands (and stale destination)
      // scalastyle:off
      for (xx <- w-1 to 0 by -1)
      {
        rs1_cases  ++= Array(((io.ren_uops(w).lrs1_rtype === RT_FIX.value || io.ren_uops(w).lrs1_rtype === RT_FLT.value)
          && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs1 === io.ren_uops(xx).ldst), (io.ren_uops(xx).pdst)))
        rs2_cases  ++= Array(((io.ren_uops(w).lrs2_rtype === RT_FIX.value || io.ren_uops(w).lrs2_rtype === RT_FLT.value)
          && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs2 === io.ren_uops(xx).ldst), (io.ren_uops(xx).pdst)))
        rs3_cases  ++= Array((io.ren_uops(w).frs3_en            && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs3 === io.ren_uops(xx).ldst), (io.ren_uops(xx).pdst)))
        stale_cases++= Array(( io.ren_uops(w).ldst_val          && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).ldst === io.ren_uops(xx).ldst), (io.ren_uops(xx).pdst)))

        when ((io.ren_uops(w).lrs1_rtype === RT_FIX.value || io.ren_uops(w).lrs1_rtype === RT_FLT.value) && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs1 === io.ren_uops(xx).ldst))
        { prs1_was_bypassed(w) := Bool(true) }
        when ((io.ren_uops(w).lrs2_rtype === RT_FIX.value || io.ren_uops(w).lrs2_rtype === RT_FLT.value) && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs2 === io.ren_uops(xx).ldst))
        { prs2_was_bypassed(w) := Bool(true) }
        when (io.ren_uops(w).frs3_en                                                         && io.ren_mask(xx) && io.ren_uops(xx).ldst_val && (io.ren_uops(w).lrs3 === io.ren_uops(xx).ldst))
        { prs3_was_bypassed(w) := Bool(true) }
      }

      // add default case where we can just read the map table for our information
      rs1_cases ++= Array(((io.ren_uops(w).lrs1_rtype === RT_FIX.value || io.ren_uops(w).lrs1_rtype === RT_FLT.value) &&
        (io.ren_uops(w).lrs1 =/= U(0)), map_table_prs1(w)))
      rs2_cases ++= Array(((io.ren_uops(w).lrs2_rtype === RT_FIX.value || io.ren_uops(w).lrs2_rtype === RT_FLT.value) &&
        (io.ren_uops(w).lrs2 =/= U(0)), map_table_prs2(w)))
      rs3_cases ++= Array((io.ren_uops(w).frs3_en  &&
        (io.ren_uops(w).lrs3 =/= U(0)), map_table_prs3(w)))

      io.ren_uops(w).pop1                       := MuxCase(io.ren_uops(w).lrs1, rs1_cases)
      io.ren_uops(w).pop2                       := MuxCase(io.ren_uops(w).lrs2, rs2_cases)
      if (max_operands > 2){io.ren_uops(w).pop3 := MuxCase(io.ren_uops(w).lrs3, rs3_cases)}
      io.ren_uops(w).stale_pdst                 := MuxCase(map_table_io(io.ren_uops(w).ldst).element, stale_cases)

    }


    //-------------------------------------------------------------
    // Busy Table

    val freelist_req_pregs = (Vec(UInt(width=p.PREG_SZ bits), pl_width))

    // 3 WB ports for now
    // 1st is back-to-back bypassable ALU ops
    // 2nd is memory/muldiv
    // TODO 3rd is ALU ops that aren't bypassable... can maybe remove this? or set TTL countdown on 1st port?
    // TODO optimize - too many write ports, but how to deal with that? (slow + fast...)
    val bsy_table = (new BusyTable(pipeline_width=pl_width, num_read_ports = pl_width*max_operands, num_wb_ports=num_wb_ports))

    for (w <- 0 until pl_width)
    {
      // Reading the Busy Bits
      // for critical path reasons, we speculatively read out the busy-bits assuming no dependencies between uops
      // then verify if the uop actually uses a register and if it depends on a newly unfreed register
      bsy_table.io.prs(0,w) := map_table_prs1(w)
      bsy_table.io.prs(1,w) := map_table_prs2(w)

      io.ren_uops(w).prs1_busy := (io.ren_uops(w).lrs1_rtype === RT_FIX.value || io.ren_uops(w).lrs1_rtype === RT_FLT.value) && (bsy_table.io.prs_busy(0,w) || prs1_was_bypassed(w))
      io.ren_uops(w).prs2_busy := (io.ren_uops(w).lrs2_rtype === RT_FIX.value || io.ren_uops(w).lrs2_rtype === RT_FLT.value) && (bsy_table.io.prs_busy(1,w) || prs2_was_bypassed(w))

      if (max_operands > 2)
      {
        bsy_table.io.prs(2,w) := map_table_prs3(w)
        io.ren_uops(w).prs3_busy := (io.ren_uops(w).frs3_en) && (bsy_table.io.prs_busy(2,w) || prs3_was_bypassed(w))
      }
      else
      {
        io.ren_uops(w).prs3_busy := Bool(false)
      }


      // Updating the Table (new busy register)
      bsy_table.io.allocated_pdst(w).valid := freelist_can_allocate(w) &&
        io.ren_mask(w) &&
        io.ren_uops(w).ldst_val &&
        (io.ren_uops(w).dst_rtype === RT_FIX.value || io.ren_uops(w).dst_rtype === RT_FLT.value)
      bsy_table.io.allocated_pdst(w).payload  := freelist_req_pregs(w)
    }

    // Clear Busy-bit
    for (i <- 0 until num_wb_ports)
    {
      bsy_table.io.unbusy_pdst(i).valid := io.wb_valids(i)
      bsy_table.io.unbusy_pdst(i).payload := io.wb_pdsts(i)
    }

    // scalastyle:on

    //-------------------------------------------------------------
    // Free List

    val freelist = (new RenameFreeList(p.PHYS_REG_COUNT, pl_width))

    for (w <- 0 until pl_width)
    {
      freelist.io.req_preg_vals(w) := (io.inst_can_proceed(w) &&
        !io.kill &&
        io.ren_mask(w) &&
        io.ren_uops(w).ldst_val &&
        (io.ren_uops(w).dst_rtype === RT_FIX.value || io.ren_uops(w).dst_rtype === RT_FLT.value))
    }
    freelist_req_pregs := freelist.io.req_pregs

    for (w <- 0 until pl_width)
    {
      freelist.io.enq_vals(w)    := io.com_valids(w) &&
        (io.com_uops(w).dst_rtype === RT_FIX.value || io.com_uops(w).dst_rtype === RT_FLT.value) &&
        (io.com_uops(w).stale_pdst =/= U(0))
      freelist.io.enq_pregs(w)   := io.com_uops(w).stale_pdst

      freelist.io.ren_br_vals(w) := ren_br_vals(w)
      freelist.io.ren_br_tags(w) := io.ren_uops(w).br_tag

      freelist_can_allocate(w)   := freelist.io.can_allocate(w)

      freelist.io.rollback_wens(w)  := io.com_rbk_valids(w) &&
        (io.com_uops(w).pdst =/= U(0)) &&
        (io.com_uops(w).dst_rtype === RT_FIX.value || io.com_uops(w).dst_rtype === RT_FLT.value)
      freelist.io.rollback_pdsts(w) := io.com_uops(w).pdst

      freelist.io.com_wens(w)    := io.com_valids(w) &&
        (io.com_uops(w).pdst =/= U(0)) &&
        (io.com_uops(w).dst_rtype === RT_FIX.value || io.com_uops(w).dst_rtype === RT_FLT.value)
      freelist.io.com_uops(w)    := io.com_uops(w)
    }

    freelist.io.br_mispredict_val := io.brinfo.mispredict
    freelist.io.br_mispredict_tag := io.brinfo.tag

    freelist.io.flush_pipeline := io.flush_pipeline


    // x0 is a special-case and should not be renamed
    for (w <- 0 until pl_width)
    {
      io.ren_uops(w).pdst := Mux(io.ren_uops(w).ldst === U(0), U(0), freelist_req_pregs(w))
    }


    //-------------------------------------------------------------
    // Branch Predictor Snapshots

    // Each branch prediction must snapshot the predictor (history state, etc.).
    // On a mispredict, the snapshot must be used to reset the predictor.
    // TODO use Mem(), but it chokes on the undefines in VCS
    val prediction_copies = Reg(Vec(new BranchPredictionResp, p.MAX_BR_COUNT))

    for (w <- 0 until pl_width)
    {
      when(ren_br_vals(w))
      {
        prediction_copies(io.ren_uops(w).br_tag) := io.ren_pred_info
      }
    }

    io.get_pred.info := prediction_copies(io.get_pred.br_tag)

    val temp = (new BranchPredictionResp)
    println("\t\tPrediction Snapshots: " + temp.asBits.getWidth + "-bits, " + p.MAX_BR_COUNT + " entries")

    //-------------------------------------------------------------
    // Outputs
    for (w <- 0 until pl_width)
    {
      // TODO REFACTOR, make == rt_x?
      io.inst_can_proceed(w) := (freelist.io.can_allocate(w) ||
        (io.ren_uops(w).dst_rtype =/= RT_FIX.value && io.ren_uops(w).dst_rtype =/= RT_FLT.value)) &&
        io.dis_inst_can_proceed(w)
    }


    //-------------------------------------------------------------
    // Debug signals

    io.debug.freelist  := freelist.io.debug.freelist
    io.debug.isprlist  := freelist.io.debug.isprlist
    io.debug.bsy_table := bsy_table.io.debug.bsy_table



}

