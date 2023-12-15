package boom_v1.predictor.tage
import boom_v1.Utils.PerformCircularShiftRegister
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// Circular Shift Register (CSR), used by the TAGE branch predictor.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2016 Sept 23

// Instead of attempting to dynamically fold a very long history register (1000s
// of bits) into ~10 bits, we will use a ~10b CSR instead. Faster and cheaper.

// Example: An 12 bit value (0b_0111_1001_1111) folded onto a 5 bit CSR becomes
// (0b_0_0010), which can be found of as:


//                /-- history[12] (evict bit)
//                |
//  c[4], c[3], c[2], c[1], c[0]
//   |                        ^
//   |                        |
//   \_______________________/ \---history[0] (newly taken bit)
//
//
// (c[4] ^ h[0] generates the new c[0]).
// (c[1] ^ h[12] generates the new c[2]).


class CircularShiftRegisterIO(compressed_length: Int, history_length: Int) extends Bundle
{
  // current value of the register
  val value = out UInt(compressed_length bits)
  // the next value that will be written into the register
  val next  = out UInt(compressed_length bits)

  val do_shift = in Bool()
  val taken = in Bool()
  val evict = in Bool()
  def shift(taken: Bool, evict: Bool) =
  {
    this.do_shift := Bool(true)
    this.taken := taken
    this.evict := evict
  }

  val do_rollback= in Bool()
  val rollback_value = in UInt(compressed_length bits)
  val do_rbk_shift = in Bool()
  val rs_new_bit = in Bool()
  val rs_evict_bit = in Bool()
  // we either perform a rollback or a rollback and shift
  // to handle flushes and branch mispredictions respectively.
  def rollback(v: UInt, and_shift: Bool, shift_bit: Bool=Bool(false), evict_bit: Bool=Bool(false)) =
  {
    do_rollback := Bool(true)
    rollback_value := v
    do_rbk_shift := and_shift
    rs_new_bit := shift_bit
    rs_evict_bit := evict_bit
  }

  def InitializeIo(dummy: Int=0) =
  {
    this.do_shift := Bool(false)
    this.taken := Bool(false)
    this.evict := Bool(false)
    this.do_rollback := Bool(false)
    this.rollback_value := U(0)
    this.rs_new_bit := Bool(false)
    this.rs_evict_bit := Bool(false)
  }
}

class CircularShiftRegister(
                             compressed_length: Int,
                             history_length: Int
                           ) extends Module
{
  val io = new CircularShiftRegisterIO(compressed_length, history_length)

  // TODO XXX remove init once fully debugged
  // TODO XXX handle case when clen > hlen
  val csr = RegInit(U(0, width=compressed_length bits))
  val next = csr

  when (io.do_rollback && io.do_rbk_shift)
  {
    next :=
      PerformCircularShiftRegister(
        io.rollback_value,
        io.rs_new_bit,
        io.rs_evict_bit,
        history_length,
        compressed_length)
  }
    .elsewhen (io.do_rollback)
    {
      next := io.rollback_value
    }
    .elsewhen (io.do_shift)
    {
      next :=
        PerformCircularShiftRegister(
          csr,
          io.taken,
          io.evict,
          history_length,
          compressed_length)
    }

  csr := next

  io.value := csr
  io.next := next
}
