package boom_v1.exec.FPU

import boom_v1.{MicroOp, Parameters, UOPs}
import spinal.core._
import spinal.lib._
import boom_v1.Utils._
import boom_v1.UOPs._
import boom_v1.decode.DecodeLogic
import boom_v1.exec.ExeUnitResp
import boom_v1.exec.FPU.FPConstants._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------

  // Note: fdiv, fsqrt unsupported.
  // Note: (this FPU currently only supports fixed latency ops)

  // TODO get rid of this decoder and move into the Decode stage? Or the RRd stage?
  // most of these signals are already created, just need to be translated
  // to the Rocket FPU-speak
  class UOPCodeFPUDecoder extends Module
  {
    val io = new Bundle {
      val uopc = Bits(INPUT, UOPs().getBitsWidth)
      val sigs = new FPUCtrlSigs().asOutput
    }

    // TODO change N,Y,X to BitPat("b1"), BitPat("b0"), and BitPat("b?")
    val N = MaskedLiteral("0")
    val Y = MaskedLiteral("1")
    val X = MaskedLiteral("-")

    val default: List[MaskedLiteral] = List(FCMD_X,    X,X,X,X,X, X,X,X,X,X,X,X, X,X,X,X)

    val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    // Note: not all of these signals are used or necessary, but we're
    // constrained by the need to fit the rocket.FPU units' ctrl signals.
    //                                                       swap12         div
    //                                                       | swap32       | sqrt
    //                               cmd                     | | single     | | round
    //                               |            ldst       | | | fromint  | | | wflags
    //                               |            | wen      | | | | toint  | | | |
    //                               |            | | ren1   | | | | | fastpipe | |
    //                               |            | | | ren2 | | | | | | fma| | | |
    //                               |            | | | | ren3 | | | | | |  | | | |
    //                               |            | | | | |  | | | | | | |  | | | |
      Array(
        (uopFCLASS_S) .toM() -> List(FCMD_MV_XF,  X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,N),
        (uopFCLASS_D) .toM() -> List(FCMD_MV_XF,  X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,N),
        (uopFMV_S_X)  .toM() -> List(FCMD_MV_FX,  X,X,N,N,N, X,X,Y,Y,N,N,N, N,N,Y,N),
        (uopFMV_D_X)  .toM() -> List(FCMD_MV_FX,  X,X,N,N,N, X,X,N,Y,N,N,N, N,N,Y,N),
        (uopFMV_X_S)  .toM() -> List(FCMD_MV_XF,  X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,N),
        (uopFMV_X_D)  .toM() -> List(FCMD_MV_XF,  X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,N),
        (uopFCVT_S_D) .toM() -> List(FCMD_CVT_FF, X,X,Y,N,N, N,X,Y,N,N,Y,N, N,N,Y,Y),
        (uopFCVT_D_S) .toM() -> List(FCMD_CVT_FF, X,X,Y,N,N, N,X,N,N,N,Y,N, N,N,Y,Y),

        (uopFCVT_S_W) .toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,Y,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_S_WU).toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,Y,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_S_L) .toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,Y,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_S_LU).toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,Y,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_D_W) .toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,N,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_D_WU).toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,N,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_D_L) .toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,N,Y,N,N,N, N,N,Y,Y),
        (uopFCVT_D_LU).toM() -> List(FCMD_CVT_FI, X,X,N,N,N, X,X,N,Y,N,N,N, N,N,Y,Y),

        (uopFCVT_W_S) .toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_WU_S).toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_L_S) .toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_LU_S).toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,Y,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_W_D) .toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_WU_D).toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_L_D) .toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,Y),
        (uopFCVT_LU_D).toM() -> List(FCMD_CVT_IF, X,X,Y,N,N, N,X,N,N,Y,N,N, N,N,Y,Y),

        (uopFEQ_S)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,Y,N,Y,N,N, N,N,N,Y),
        (uopFLT_S)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,Y,N,Y,N,N, N,N,N,Y),
        (uopFLE_S)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,Y,N,Y,N,N, N,N,N,Y),
        (uopFEQ_D)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,N,N,Y,N,N, N,N,N,Y),
        (uopFLT_D)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,N,N,Y,N,N, N,N,N,Y),
        (uopFLE_D)    .toM() -> List(FCMD_CMP,    X,X,Y,Y,N, N,N,N,N,Y,N,N, N,N,N,Y),

        (uopFSGNJ_S)  .toM() -> List(FCMD_SGNJ,   X,X,Y,Y,N, N,N,Y,N,N,Y,N, N,N,N,N),
        (uopFSGNJ_D)  .toM() -> List(FCMD_SGNJ,   X,X,Y,Y,N, N,N,N,N,N,Y,N, N,N,N,N),

        (uopFMIN_S)   .toM() -> List(FCMD_MINMAX, X,X,Y,Y,N, N,N,Y,N,N,Y,N, N,N,N,Y),
        (uopFMAX_S)   .toM() -> List(FCMD_MINMAX, X,X,Y,Y,N, N,N,Y,N,N,Y,N, N,N,N,Y),
        (uopFMIN_D)   .toM() -> List(FCMD_MINMAX, X,X,Y,Y,N, N,N,N,N,N,Y,N, N,N,N,Y),
        (uopFMAX_D)   .toM() -> List(FCMD_MINMAX, X,X,Y,Y,N, N,N,N,N,N,Y,N, N,N,N,Y),

        (uopFADD_S)   .toM() -> List(FCMD_ADD,    X,X,Y,Y,N, N,Y,Y,N,N,N,Y, N,N,Y,Y),
        (uopFSUB_S)   .toM() -> List(FCMD_SUB,    X,X,Y,Y,N, N,Y,Y,N,N,N,Y, N,N,Y,Y),
        (uopFMUL_S)   .toM() -> List(FCMD_MUL,    X,X,Y,Y,N, N,N,Y,N,N,N,Y, N,N,Y,Y),
        (uopFADD_D)   .toM() -> List(FCMD_ADD,    X,X,Y,Y,N, N,Y,N,N,N,N,Y, N,N,Y,Y),
        (uopFSUB_D)   .toM() -> List(FCMD_SUB,    X,X,Y,Y,N, N,Y,N,N,N,N,Y, N,N,Y,Y),
        (uopFMUL_D)   .toM() -> List(FCMD_MUL,    X,X,Y,Y,N, N,N,N,N,N,N,Y, N,N,Y,Y),

        (uopFMADD_S)  .toM() -> List(FCMD_MADD,   X,X,Y,Y,Y, N,N,Y,N,N,N,Y, N,N,Y,Y),
        (uopFMSUB_S)  .toM() -> List(FCMD_MSUB,   X,X,Y,Y,Y, N,N,Y,N,N,N,Y, N,N,Y,Y),
        (uopFNMADD_S) .toM() -> List(FCMD_NMADD,  X,X,Y,Y,Y, N,N,Y,N,N,N,Y, N,N,Y,Y),
        (uopFNMSUB_S) .toM() -> List(FCMD_NMSUB,  X,X,Y,Y,Y, N,N,Y,N,N,N,Y, N,N,Y,Y),
        (uopFMADD_D)  .toM() -> List(FCMD_MADD,   X,X,Y,Y,Y, N,N,N,N,N,N,Y, N,N,Y,Y),
        (uopFMSUB_D)  .toM() -> List(FCMD_MSUB,   X,X,Y,Y,Y, N,N,N,N,N,N,Y, N,N,Y,Y),
        (uopFNMADD_D) .toM() -> List(FCMD_NMADD,  X,X,Y,Y,Y, N,N,N,N,N,N,Y, N,N,Y,Y),
        (uopFNMSUB_D) .toM() -> List(FCMD_NMSUB,  X,X,Y,Y,Y, N,N,N,N,N,N,Y, N,N,Y,Y)
      )

    val decoder = DecodeLogic(io.uopc.asUInt, default, table)

    val s = io.sigs
    val sigs = Seq(s.cmd, s.ldst, s.wen, s.ren1, s.ren2, s.ren3, s.swap12,
      s.swap23, s.single, s.fromint, s.toint, s.fastpipe, s.fma,
      s.div, s.sqrt, s.round, s.wflags)
    sigs zip decoder map {case(s,d) => s := d}
  }


  class FpuReq()(implicit p: Parameters) extends Bundle
  {
    val uop      = new MicroOp()
    val rs1_data = Bits(width = 65)
    val rs2_data = Bits(width = 65)
    val rs3_data = Bits(width = 65)
    val fcsr_rm  = Bits(width = FPConstants.RM_SZ)
  }

  class FPU(implicit p: Parameters) extends Module
  {
    val io = new Bundle
    {
      val req = slave Flow(new FpuReq)
      val resp = master (new ValidIO(new ExeUnitResp(65)))
    }

    // all FP units are padded out to the same latency for easy scheduling of the write port
    val fpu_latency = p.dfmaLatency
    val io_req = io.req.payload


    val fp_decoder = (new UOPCodeFPUDecoder)
    fp_decoder.io.uopc:= io_req.uop.uopc
    val fp_ctrl = fp_decoder.io.sigs
    val fp_rm = Mux(ImmGenRm(io_req.uop.imm_packed) === Bits(7), io_req.fcsr_rm, ImmGenRm(io_req.uop.imm_packed))

    val req = (new FPInput)
    req := fp_ctrl
    req.rm := fp_rm
    req.in1 := io_req.rs1_data
    req.in2 := io_req.rs2_data
    req.in3 := io_req.rs3_data
    when (fp_ctrl.swap23) { req.in3 := io_req.rs2_data }

    req.typ := ImmGenTyp(io_req.uop.imm_packed)


    val dfma = (new FPUFMAPipe(latency = fpu_latency, expWidth = 11, sigWidth = 53))
    dfma.io.in.valid := io.req.valid && fp_ctrl.fma && !fp_ctrl.single
    dfma.io.in.payload := req


    val sfma = (new FPUFMAPipe(latency = fpu_latency, expWidth = 8, sigWidth = 24))
    sfma.io.in.valid := io.req.valid && fp_ctrl.fma && fp_ctrl.single
    sfma.io.in.payload := req


    val ifpu = (new IntToFP(fpu_latency)) // 3 for rocket
    ifpu.io.in.valid := io.req.valid && fp_ctrl.fromint
    ifpu.io.in.payload := req
    assert (!(io.req.valid && fp_ctrl.fromint && req.in1(64)),
      "IntToFP integer input has 65th high-order bit set!")


    val fpiu = (new FPToInt)
    fpiu.io.in.valid := io.req.valid && (fp_ctrl.toint || fp_ctrl.cmd === FCMD_MINMAX)
    fpiu.io.in.payload := req
    val fpiu_out = Pipe(RegNext(fpiu.io.in.valid && !fp_ctrl.fastpipe),
      fpiu.io.out.payload, fpu_latency-1)


    val fpiu_result  = (new FPResult)
    fpiu_result.data := fpiu_out.bits.toint
    fpiu_result.exc  := fpiu_out.bits.exc


    val fpmu = (new FPToFP(fpu_latency)) // latency 2 for rocket
    fpmu.io.in.valid := io.req.valid && fp_ctrl.fastpipe
    fpmu.io.in.bits := req
    fpmu.io.lt := fpiu.io.out.bits.lt

    // Response (all FP units have been padded out to the same latency)
    io.resp.valid := ifpu.io.out.valid ||
      fpiu_out.valid ||
      fpmu.io.out.valid ||
      sfma.io.out.valid ||
      dfma.io.out.valid
    val fpu_out   = Mux(dfma.io.out.valid, dfma.io.out.bits,
      Mux(sfma.io.out.valid, sfma.io.out.bits,
        Mux(ifpu.io.out.valid, ifpu.io.out.bits,
          Mux(fpiu_out.valid,    fpiu_result,
            fpmu.io.out.bits))))

    io.resp.bits.data              := fpu_out.data
    io.resp.bits.fflags.valid      := io.resp.valid
    io.resp.bits.fflags.bits.flags := fpu_out.exc

    // TODO why is this assertion failing?
    //   assert (PopCount(Vec(ifpu.io.out, fpiu_out, fpmu.io.out, sfma.io.out, dfma.io.out).map(_.valid)) <= UInt(1),
    //      "Multiple FPU units are firing requests.")
  }


