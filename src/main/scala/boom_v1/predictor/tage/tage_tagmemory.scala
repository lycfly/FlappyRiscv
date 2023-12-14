package boom_v1.predictor.tage
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// Abstracted Memory 1r/1w Helper for TAGE Tag Memory
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2016 Nov
//
// Goal:
//    - Provide a wrapper to a 1 read/1 write memory that is oblivious to the implementation.
//
// Notes:
//    - Handles read requests being stalled.
//    - Assumes that writes can be delayed (or even dropped).
//    - Does not support masking.



class TageTagMemory(
                     num_entries: Int,
                     memwidth: Int,
                     dualported: Boolean = false
                   ) extends Module
{
  private val index_sz = log2Up(num_entries)
  val io = new Bundle
  {
    // the reader is not ready; stall the read pipeline.
    val stall = in Bool()

    // send read addr on cycle 0, get data out on cycle 2.
    val s0_r_idx = in UInt(width = index_sz bits)
    val s2_r_out = out UInt(width = memwidth bits)

    val w_en = in Bool()
    val w_idx = in UInt(width = index_sz bits)
    val w_data = in UInt(width = memwidth bits)
    def write(idx: UInt, data: UInt) =
    {
      this.w_en := Bool(true)
      this.w_idx := idx
      this.w_data := data
    }

    def InitializeIo(dummy: Int=0) =
    {
      this.w_en := Bool(false)
      this.w_idx := U(0)
      this.w_data := U(0)
    }
  }

  //------------------------------------------------------------

  val smem = Mem(UInt(width = memwidth bits),num_entries)

  //------------------------------------------------------------

  val idx = UInt()
  val last_idx = RegNext(idx)

  idx := Mux(io.stall, last_idx, io.s0_r_idx)

  val r_s1_out = smem.readSync(idx, !io.stall)
  val r_s2_out = RegNextWhen(r_s1_out, !io.stall)
  io.s2_r_out := r_s2_out


  when (io.w_en)
  {
    smem(io.w_idx) := io.w_data
  }
}

