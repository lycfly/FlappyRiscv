package boom_v1.issue
import boom_v1.BOOMDebugConstants.{DEBUG_PRINTF, O3PIPEVIEW_PRINTF}
import boom_v1.ScalarOpConstants._
import boom_v1.UOPs._
import boom_v1.Utils.Str
import boom_v1.exec.BrResolutionInfo
import boom_v1.exec.FUConstants.FUC_SZ
import boom_v1.{FUType, MicroOp, Parameters}
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// RISCV Processor Issue Logic
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------



  //-------------------------------------------------------------
  //-------------------------------------------------------------

  class IssueUnitIO(issue_width: Int, num_wakeup_ports: Int)(implicit p: Parameters) extends Bundle
  {
    val dis_valids     = in Vec(Bool(),p.DISPATCH_WIDTH)
    val dis_uops       = in Vec(new MicroOp(),p.DISPATCH_WIDTH)
    val dis_readys     = out Vec(Bool(),p.DISPATCH_WIDTH)

    val iss_valids     = out Vec(Bool(),issue_width)
    val iss_uops       = out Vec(new MicroOp(), issue_width)
    val wakeup_pdsts   = Vec(slave Flow(UInt(width=p.PREG_SZ bits)),num_wakeup_ports)

    // tell the issue unit what each execution pipeline has in terms of functional units
    val fu_types       = in Vec(FUType(),issue_width)

    val brinfo         = in(new BrResolutionInfo())
    val flush_pipeline = in Bool()

    val event_empty    = out Bool() // used by HPM events; is the issue unit empty?

    val tsc_reg        = in UInt(p.xLen bits)
  }

  abstract class IssueUnit(num_issue_slots: Int, issue_width: Int, num_wakeup_ports: Int)(implicit p: Parameters)
    extends Module
  {
    val io = new IssueUnitIO(issue_width, num_wakeup_ports)

    //-------------------------------------------------------------
    // Set up the dispatch uops
    // special case "storing" 2 uops within one issue slot.

    val dis_uops = Array.fill(p.DISPATCH_WIDTH) {(new MicroOp())}
    for (w <- 0 until p.DISPATCH_WIDTH)
    {
      dis_uops(w) := io.dis_uops(w)
      dis_uops(w).iw_state := s_valid_1
      when (dis_uops(w).uopc === uopSTA || dis_uops(w).uopc === uopAMO_AG)
      {
        dis_uops(w).iw_state := s_valid_2
      }
    }

    //-------------------------------------------------------------
    // Issue Table

    val issue_slots = Vec.fill(num_issue_slots) {(new IssueSlot(num_wakeup_ports)).io}

    io.event_empty := !(issue_slots.map(s => s.valid).reduce(_|_))

    //-------------------------------------------------------------

    assert (CountOne(issue_slots.map(s => s.grant)) <= U(issue_width), "Issue window giving out too many grants.")

    //-------------------------------------------------------------

    if (O3PIPEVIEW_PRINTF)
    {
      for (i <- 0 until p.ISSUE_WIDTH)
      {
        // only print stores once!
        when (io.iss_valids(i) && io.iss_uops(i).uopc =/= uopSTD)
        {
          printf("%d; O3PipeView:issue: %d\n",
            io.iss_uops(i).debug_events.fetch_seq,
            io.tsc_reg)
        }
      }
    }

    if (DEBUG_PRINTF)
    {
      for (i <- 0 until num_issue_slots)
      {
        printf("  integer_issue_slot[%d](%c)(Req:%c):wen=%c P:(%c,%c,%c) OP:(%d,%d,%d) PDST:%d %c [[DASM(%x)]" +
          " 0x%x: %d] ri:%d bm=%d imm=0x%x\n"
          , U(i, log2Up(num_issue_slots) bits)
          , Mux(issue_slots(i).valid, Str("V"), Str("-"))
          //            , Mux(issue_slots(i).request, Str(u_red + "R" + end), Str(grn + "-" + end))
          //            , Mux(issue_slots(i).in_uop.valid, Str(u_wht + "W" + end),  Str(grn + " " + end))
          , Mux(issue_slots(i).request, Str("R"), Str("-"))
          , Mux(issue_slots(i).in_uop.valid, Str("W"),  Str(" "))
          , Mux(issue_slots(i).debug.p1, Str("!"), Str(" "))
          , Mux(issue_slots(i).debug.p2, Str("!"), Str(" "))
          , Mux(issue_slots(i).debug.p3, Str("!"), Str(" "))
          , issue_slots(i).uop.pop1
          , issue_slots(i).uop.pop2
          , issue_slots(i).uop.pop3
          , issue_slots(i).uop.pdst
          , Mux(issue_slots(i).uop.dst_rtype === RT_FIX, Str("X"),
            Mux(issue_slots(i).uop.dst_rtype === RT_X, Str("-"),
              Mux(issue_slots(i).uop.dst_rtype === RT_FLT, Str("f"),
                Mux(issue_slots(i).uop.dst_rtype === RT_PAS, Str("C"), Str("?")))))
          , issue_slots(i).uop.inst
          , issue_slots(i).uop.pc(31 downto 0)
          , issue_slots(i).uop.uopc
          , issue_slots(i).uop.rob_idx
          , issue_slots(i).uop.br_mask
          , issue_slots(i).uop.imm_packed
        )
      }
    }
  }



