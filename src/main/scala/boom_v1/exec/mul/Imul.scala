package boom_v1.exec.mul

//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
import boom_v1.ScalarOpConstants._
import boom_v1.Utils.{Fill, MuxCase, Pipe}
import boom_v1.exec.ALU._
import spinal.core._
import spinal.lib._
import boom_v1.Utils.chiselExtract._
// TODO can repurpose the FN_* ctrl signals,
// as we don't overlap with the ALU

class IMul(imul_stages: Int) extends Module
{
  val io = new Bundle {
    val valid = in Bool()
    val fn  = in UInt(SZ_ALU_FN bits)
    val dw  = in UInt(SZ_DW bits)
    val in0 = in UInt( 64 bits)
    val in1 = in UInt( 64 bits)
    val mul_out = out UInt(64 bits)
  }

  def FN(dw: UInt, fn: UInt) = io.dw === dw && io.fn === fn
  val sxl64 = FN(DW_64.asUInt(), FN_MULH.toU()) | FN(DW_64.asUInt(), FN_MULHSU.toU())
  val sxr64 = FN(DW_64.asUInt(), FN_MULH.toU())
  val zxl32 = FN(DW_32.asUInt(), FN_MULHU.toU())
  val zxr32 = FN(DW_32.asUInt(), FN_MULHU.toU()) | FN(DW_32.asUInt(), FN_MULHSU.toU())
  val sxl32 = FN(DW_32.asUInt(), FN_MULH.toU()) | FN(DW_32.asUInt(), FN_MULHSU.toU())
  val sxr32 = FN(DW_32.asUInt(), FN_MULH.toU())

  val lhs = Cat(
    io.in0(63) & sxl64,
    Fill(32, ~zxl32) & io.in0(63 downto 32).asBits | Fill(32, sxl32&io.in0(31)),
    io.in0(31 downto 0).asBits) //TODO: 65 bits
  val rhs = Cat(
    io.in1(63) & sxr64,
    Fill(32, ~zxr32)&io.in1(63 downto 32).asBits | Fill(32, sxr32&io.in1(31)),
    io.in1(31 downto 0).asBits) //TODO: 65 bits

  val mul_result = lhs.asSInt * rhs.asSInt //TODO:130 bits

  val mul_output_mux = MuxCase(
    U(0, 64 bits), Array(
      FN(DW_64.asUInt(), FN_MUL.toU())    -> mul_result.extract(63,0),
      FN(DW_64.asUInt(), FN_MULH.toU())   -> mul_result.extract(127,64),
      FN(DW_64.asUInt(), FN_MULHU.toU())  -> mul_result.extract(127,64),
      FN(DW_64.asUInt(), FN_MULHSU.toU()) -> mul_result.extract(127,64),
      FN(DW_32.asUInt(), FN_MUL.toU())    -> Cat(Fill(32, mul_result(31)), mul_result.extract(31,0)),
      FN(DW_32.asUInt(), FN_MULH.toU())   -> Cat(Fill(32, mul_result(63)), mul_result.extract(63,32)),
      FN(DW_32.asUInt(), FN_MULHU.toU())  -> Cat(Fill(32, mul_result(63)), mul_result.extract(63,32)),
      FN(DW_32.asUInt(), FN_MULHSU.toU()) -> Cat(Fill(32, mul_result(63)), mul_result.extract(63,32))
    ))

  io.mul_out := Pipe(io.valid, mul_output_mux, imul_stages).payload
}

