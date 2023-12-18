package boom_v1.predictor.gshare

//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// RISCV GShare Branch Predictor (simplified)
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2016 Mar 4

// This version of gshare demonstrates how to create a prototype branch
// predictor that interfaces properly with the rest of BOOM. It is
// combinational. It would also not have great synthesis QoR.
//
// A more realistic version would:
//    - be implemented using single-ported synchronous memory.
//    - separate each bit of the two-bit counters into separate memory banks.
//    - banked to allow simultaneous updates and predictions.
//
// NOTE: global history is already handled automatically in the BrPredictor
// super-class.

import boom_v1.Parameters
import boom_v1.predictor.boom.BrPredictor
import spinal.core._
import spinal.lib._

//case object SimpleGShareKey extends Field[SimpleGShareParameters]

case class SimpleGShareParameters(
                                   enabled: Boolean = false,
                                   history_length: Int = 10)

class SimpleGShareResp(index_sz: Int) extends Bundle
{
  val index = UInt(width = index_sz bits) // needed to update predictor at Commit
}

object SimpleGShareBrPredictor
{
  def GetRespInfoSize(p: Parameters): Int =
  {
    val dummy = new SimpleGShareResp(SimpleGShareParameters().history_length)
    dummy.getBitsWidth
  }
}

class SimpleGShareBrPredictor(
                               fetch_width: Int,
                               num_entries: Int = 4096,
                               history_length: Int = 12
                             )(implicit p: Parameters) extends BrPredictor(fetch_width, history_length)(p)
{
  println ("\tBuilding Simple GShare Predictor, with "
    + history_length + " bits of history for ("
    + fetch_width + "-wide fetch) and "
    + num_entries + " entries.")

  //------------------------------------------------------------
  // constants
  val CNTR_SZ = 2
  val CNTR_MAX = (1 << CNTR_SZ) - 1

  //------------------------------------------------------------
  // helper functions

  private def Hash (addr: UInt, hist: UInt): UInt =
  {
    (addr >> UInt(log2Up(fetch_width*p.coreInstBytes) bits)) ^ hist
  }

  private def GetPrediction(cntr: UInt): Bool =
  {
    // return highest-order bit
    (cntr >> U(CNTR_SZ-1))(0)
  }

  private def UpdateCounters(
                              valid: Bool,
                              counter_row: Vec[UInt],
                              enables: Vec[Bool],
                              takens: Vec[Bool]): Vec[UInt] =
  {
    val updated_row = (Vec(UInt(width=CNTR_SZ bits), fetch_width))
    for (i <- 0 until fetch_width)
    {
      updated_row(i) := counter_row(i)
      when (valid)
      {
        when (enables(i) && takens(i) && counter_row(i) =/= U(CNTR_MAX))
        {
          updated_row(i) := counter_row(i) + U(1)
        }
          .elsewhen (enables(i) && !takens(i) && counter_row(i) =/= U(0))
          {
            updated_row(i) := counter_row(i) - U(1)
          }
      }
    }
    updated_row
  }

  //------------------------------------------------------------
  // state

  // CNTR_SZ-bit counters, one counter per instruction in the fetch packet
  val counters = Mem(Vec( UInt(width=CNTR_SZ bits),fetch_width),num_entries)

  //------------------------------------------------------------
  // get prediction (delay response 2 cycles to match fetch pipeline)

  val resp_info = (new SimpleGShareResp(log2Up(num_entries)))

  val p_idx = Hash(io.req_pc, this.ghistory.asUInt)
  io.resp.valid        := Bool(true)
  resp_info.index      := RegNext(RegNext(p_idx))
  io.resp.payload.takens  := RegNext(RegNext(Vec(counters(p_idx).map(GetPrediction(_))).asBits.asUInt))

  io.resp.payload.info    := resp_info.asBits.asUInt

  //------------------------------------------------------------
  // update predictor

  val commit_info = new SimpleGShareResp(log2Up(num_entries))
  commit_info.assignFromBits(commit.payload.info.info.asBits)
  val u_idx = commit_info.index
  counters(u_idx) := UpdateCounters(commit.valid, counters(u_idx), commit.payload.ctrl.executed, commit.payload.ctrl.taken)


  //------------------------------------------------------------

}

