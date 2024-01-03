package boom_v1.issue
import boom_v1.Parameters
import boom_v1.ScalarOpConstants.{NullMicroOp, RT_X}
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
//


  //-------------------------------------------------------------
  //-------------------------------------------------------------

  class IssueUnitCollasping(num_issue_slots: Int, issue_width: Int, num_wakeup_ports: Int)(implicit p: Parameters)
    extends IssueUnit(num_issue_slots, issue_width, num_wakeup_ports)
  {
    //-------------------------------------------------------------
    // Figure out how much to shift entries by

    val MAX_SHIFT = p.DISPATCH_WIDTH
    val shamt_oh = Array.fill(num_issue_slots){UInt(width=issue_width bits)}
    // count total grants before this entry, and tus how many to shift upwards by
    val shamt = Array.fill(num_issue_slots){UInt(width=log2Up(issue_width+1) bits)}


    val vacants = issue_slots.map(s => !(s.valid)) ++ io.dis_valids.map(!_)
    val shamts_oh = Array.fill(num_issue_slots+p.DISPATCH_WIDTH) {(UInt(width=MAX_SHIFT bits))}
    // track how many to shift up this entry by by counting previous vacant spots
    def SaturatingCounterOH(count_oh:UInt, inc: Bool, max: Int): UInt =
    {
      val next = (UInt(width=max bits))
      next := count_oh
      when (count_oh === U(0) && inc)
      {
        next := U(1)
      }
        .elsewhen (!count_oh(max-1) && inc)
        {
          next := (count_oh << U(1))
        }
      next
    }
    shamts_oh(0) := U(0)
    for (i <- 1 until num_issue_slots + p.DISPATCH_WIDTH)
    {
      shamts_oh(i) := SaturatingCounterOH(shamts_oh(i-1), vacants(i-1), MAX_SHIFT)
    }

    //-------------------------------------------------------------

    // which entries' uops will still be next cycle? (not being issued and vacated)
    val will_be_valid = (0 until num_issue_slots).map(i => issue_slots(i).will_be_valid) ++
      (0 until p.DISPATCH_WIDTH).map(i => io.dis_valids(i) &&
        !io.dis_uops(i).exception &&
        !io.dis_uops(i).is_fence &&
        !io.dis_uops(i).is_fencei)

    val uops = issue_slots.map(s=>s.updated_uop) ++ dis_uops.map(s=>s)
    for (i <- 0 until num_issue_slots)
    {
      issue_slots(i).in_uop.valid := Bool(false)
      issue_slots(i).in_uop.payload  := uops(i+1)
      for (j <- 1 to MAX_SHIFT by 1)
      {
        when (shamts_oh(i+j) === U(1 << (j-1)))
        {
          issue_slots(i).in_uop.valid := will_be_valid(i+j)
          issue_slots(i).in_uop.payload  := uops(i+j)
        }
      }
      issue_slots(i).wakeup_dsts  := io.wakeup_pdsts
      issue_slots(i).brinfo       := io.brinfo
      issue_slots(i).kill         := io.flush_pipeline
      issue_slots(i).clear        := shamts_oh(i) =/= U(0)
    }

    //-------------------------------------------------------------
    // Dispatch/Entry Logic
    // did we find a spot to slide the new dispatched uops into?

    val will_be_available = (0 until num_issue_slots).map(i =>
      (!issue_slots(i).will_be_valid || issue_slots(i).clear) && !(issue_slots(i).in_uop.valid))
    val num_available = CountOne(will_be_available)
    for (w <- 0 until p.DISPATCH_WIDTH)
    {
      io.dis_readys(w) := RegNext(num_available > U(w))
    }

    //-------------------------------------------------------------
    // Issue Select Logic

    // set default
    for (w <- 0 until issue_width)
    {
      io.iss_valids(w) := Bool(false)
      io.iss_uops(w)   := NullMicroOp
      // unsure if this is overkill
      io.iss_uops(w).pop1 := U(0)
      io.iss_uops(w).pop2 := U(0)
      io.iss_uops(w).pop3 := U(0)
      io.iss_uops(w).lrs1_rtype := RT_X
      io.iss_uops(w).lrs2_rtype := RT_X
    }

    val requests = issue_slots.map(s => s.request)
    val port_issued = Array.fill(issue_width){Bool()}
    for (w <- 0 until issue_width)
    {
      port_issued(w) = Bool(false)
    }

    for (i <- 0 until num_issue_slots)
    {
      issue_slots(i).grant := Bool(false)
      var uop_issued = Bool(false)

      for (w <- 0 until issue_width)
      {
        val can_allocate = (issue_slots(i).uop.fu_code.asBits & io.fu_types(w).asBits) =/= U(0)

        when (requests(i) && !uop_issued && can_allocate && !port_issued(w))
        {
          issue_slots(i).grant := Bool(true)
          io.iss_valids(w) := Bool(true)
          io.iss_uops(w) := issue_slots(i).uop
        }
        val was_port_issued_yet = port_issued(w)
        port_issued(w) = (requests(i) && !uop_issued && can_allocate) | port_issued(w)
        uop_issued = (requests(i) && can_allocate && !was_port_issued_yet) | uop_issued
      }
    }

  }

