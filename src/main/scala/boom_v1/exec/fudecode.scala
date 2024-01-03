package boom_v1.exec
import boom_v1.BranchType._
import boom_v1.exec.ALU.SZ_ALU_FN
import boom_v1.{BranchType, CSR, IMMType, MicroOp, Parameters, RS1Type, RS2Type}
import boom_v1.IMMType._
import boom_v1.RS1Type._
import boom_v1.RS2Type._
import boom_v1.ScalarOpConstants._
import boom_v1.UOPs._
import boom_v1.decode.DecodeLogic
import boom_v1.exec.ALU._
import boom_v1.exec.ALUFN._
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// Functional Unit Decode
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

// Generate the functional unit control signals from the micro-op opcodes.


class RRdCtrlSigs(implicit p: Parameters) extends Bundle
{
  val br_type          = Bits(BranchType().getBitsWidth bits)
  val use_alupipe      = Bool()
  val use_muldivpipe   = Bool()
  val use_mempipe      = Bool()
  val op_fcn      = Bits(ALUFN().getBitsWidth bits)
  val fcn_dw      = Bool()
  val op1_sel     = UInt(RS1Type().getBitsWidth bits)
  val op2_sel     = UInt(RS2Type().getBitsWidth bits)
  val imm_sel     = UInt(IMMType().getBitsWidth bits)
  val rf_wen      = Bool()
  val csr_cmd     = Bits(width = CSR.SZ bits)

  def decode(uopc: UInt, table: Iterable[(MaskedLiteral, List[MaskedLiteral])]) =
  {
    val decoder = DecodeLogic(uopc, AluRRdDecode.default, table)
    val sigs = Seq(br_type, use_alupipe, use_muldivpipe, use_mempipe, op_fcn,
      fcn_dw, op1_sel, op2_sel, imm_sel, rf_wen, csr_cmd)
    sigs zip decoder map {case(s,d) => s := d.asInstanceOf[s.type]}
    this
  }
}

abstract trait RRdDecodeConstants
{
  val default: List[MaskedLiteral] =
    List[MaskedLiteral](BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_X  , OP1_X   , OP2_X   , IS_X, REN_0, CSR.N)
  val table: Array[(MaskedLiteral, List[MaskedLiteral])]
}

object AluRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                        br type
      //                        |           use alu pipe                     op1 sel         op2 sel
      //                        |           |  use muldiv pipe               |               |              immsel            csr_cmd
      //                        |           |  |  use mem pipe               |               |              |          rf wen |
      //                        |           |  |  |  alu fcn         wd/word?|               |              |          |      |
      //                        |           |  |  |  |               |       |               |              |          |      |
      (uopLUI)  .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD .toM(), DW_XPR, OP1_ZERO.toM(), OP2_IMM.toM(), IS_U.toM(), REN_1, CSR.N),

      (uopADDI) .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopANDI) .toM() -> List(BR_N.toM() , Y, N, N, FN_AND .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopORI)  .toM() -> List(BR_N.toM() , Y, N, N, FN_OR  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopXORI) .toM() -> List(BR_N.toM() , Y, N, N, FN_XOR .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSLTI) .toM() -> List(BR_N.toM() , Y, N, N, FN_SLT .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSLTIU).toM() -> List(BR_N.toM() , Y, N, N, FN_SLTU.toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSLLI) .toM() -> List(BR_N.toM() , Y, N, N, FN_SL  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSRAI) .toM() -> List(BR_N.toM() , Y, N, N, FN_SRA .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSRLI) .toM() -> List(BR_N.toM() , Y, N, N, FN_SR  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),

      (uopADDIW).toM() -> List(BR_N.toM() , Y, N, N, FN_ADD .toM(), DW_32 , OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSLLIW).toM() -> List(BR_N.toM() , Y, N, N, FN_SL  .toM(), DW_32 , OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSRAIW).toM() -> List(BR_N.toM() , Y, N, N, FN_SRA .toM(), DW_32 , OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),
      (uopSRLIW).toM() -> List(BR_N.toM() , Y, N, N, FN_SR  .toM(), DW_32 , OP1_RS1 .toM(), OP2_IMM.toM(), IS_I.toM(), REN_1, CSR.N),

      (uopADD)  .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSLL)  .toM() -> List(BR_N.toM() , Y, N, N, FN_SL  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSUB)  .toM() -> List(BR_N.toM() , Y, N, N, FN_SUB .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSLT)  .toM() -> List(BR_N.toM() , Y, N, N, FN_SLT .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSLTU) .toM() -> List(BR_N.toM() , Y, N, N, FN_SLTU.toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopAND)  .toM() -> List(BR_N.toM() , Y, N, N, FN_AND .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopOR)   .toM() -> List(BR_N.toM() , Y, N, N, FN_OR  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopXOR)  .toM() -> List(BR_N.toM() , Y, N, N, FN_XOR .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSRA)  .toM() -> List(BR_N.toM() , Y, N, N, FN_SRA .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSRL)  .toM() -> List(BR_N.toM() , Y, N, N, FN_SR  .toM(), DW_XPR, OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),

      (uopADDW) .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD .toM(), DW_32 , OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSUBW) .toM() -> List(BR_N.toM() , Y, N, N, FN_SUB .toM(), DW_32 , OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSLLW) .toM() -> List(BR_N.toM() , Y, N, N, FN_SL  .toM(), DW_32 , OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSRAW) .toM() -> List(BR_N.toM() , Y, N, N, FN_SRA .toM(), DW_32 , OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N),
      (uopSRLW) .toM() -> List(BR_N.toM() , Y, N, N, FN_SR  .toM(), DW_32 , OP1_RS1 .toM(), OP2_RS2.toM(), IS_X      , REN_1, CSR.N))
}

object BruRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                        br type
      //                        |           use alu pipe                    op1 sel   op2 sel
      //                        |           |  use muldiv pipe              |         |         immsel             csr_cmd
      //                        |           |  |  use mem pipe              |         |         |           rf wen |
      //                        |           |  |  |  alu fcn        wd/word?|         |         |           |      |
      //                        |           |  |  |  |              |       |         |         |           |      |
      (uopBEQ)  .toM() -> List(BR_EQ .toM(),Y, N, N, FN_SUB .toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),
      (uopBNE)  .toM() -> List(BR_NE .toM(),Y, N, N, FN_SUB .toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),
      (uopBGE)  .toM() -> List(BR_GE .toM(),Y, N, N, FN_SLT .toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),
      (uopBGEU) .toM() -> List(BR_GEU.toM(),Y, N, N, FN_SLTU.toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),
      (uopBLT)  .toM() -> List(BR_LT .toM(),Y, N, N, FN_SLT .toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),
      (uopBLTU) .toM() -> List(BR_LTU.toM(),Y, N, N, FN_SLTU.toM(), DW_XPR, OP1_X   , OP2_X   , IS_B.toM(), REN_0, CSR.N),

      (uopJAL)  .toM() -> List(BR_J  .toM(),Y, N, N, FN_ADD.toM() , DW_XPR, OP1_PC  , OP2_FOUR, IS_J.toM(), REN_1, CSR.N),
      (uopJALR) .toM() -> List(BR_JR .toM(),Y, N, N, FN_ADD.toM() , DW_XPR, OP1_PC  , OP2_FOUR, IS_I.toM(), REN_1, CSR.N),
      (uopAUIPC).toM() -> List(BR_N  .toM(),Y, N, N, FN_ADD.toM() , DW_XPR, OP1_PC  , OP2_IMM , IS_U.toM(), REN_1, CSR.N))
}

object MulDivRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                          br type
      //                          |          use alu pipe                      op1 sel         op2 sel
      //                          |          |  use muldiv pipe                |               |              immsel       csr_cmd
      //                          |          |  |  use mem pipe                |               |              |     rf wen |
      //                          |          |  |  |  alu fcn          wd/word?|               |              |     |      |
      //                          |          |  |  |  |                |       |               |              |     |      |
      (uopMUL)   .toM() -> List(BR_N.toM() , N, Y, N, FN_MUL   .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X,  REN_1, CSR.N),
      (uopMULH)  .toM() -> List(BR_N.toM() , N, Y, N, FN_MULH  .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X,  REN_1, CSR.N),
      (uopMULHU) .toM() -> List(BR_N.toM() , N, Y, N, FN_MULHU .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X,  REN_1, CSR.N),
      (uopMULHSU).toM() -> List(BR_N.toM() , N, Y, N, FN_MULHSU.toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X,  REN_1, CSR.N),
      (uopMULW)  .toM() -> List(BR_N.toM() , N, Y, N, FN_MUL   .toM(), DW_32 , OP1_RS1.toM(), OP2_RS2.toM() , IS_X,  REN_1, CSR.N),

      (uopDIV)   .toM() -> List(BR_N.toM() , N, Y, N, FN_DIV   .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopDIVU)  .toM() -> List(BR_N.toM() , N, Y, N, FN_DIVU  .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopREM)   .toM() -> List(BR_N.toM() , N, Y, N, FN_REM   .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopREMU)  .toM() -> List(BR_N.toM() , N, Y, N, FN_REMU  .toM(), DW_XPR, OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopDIVW)  .toM() -> List(BR_N.toM() , N, Y, N, FN_DIV   .toM(), DW_32 , OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopDIVUW) .toM() -> List(BR_N.toM() , N, Y, N, FN_DIVU  .toM(), DW_32 , OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopREMW)  .toM() -> List(BR_N.toM() , N, Y, N, FN_REM   .toM(), DW_32 , OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N),
      (uopREMUW) .toM() -> List(BR_N.toM() , N, Y, N, FN_REMU  .toM(), DW_32 , OP1_RS1.toM(), OP2_RS2.toM() , IS_X, REN_1,  CSR.N))
}

object MemRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                          br type
      //                          |          use alu pipe                      op1 sel         op2 sel
      //                          |          |  use muldiv pipe                |               |              immsel       csr_cmd
      //                          |          |  |  use mem pipe                |               |              |     rf wen |
      //                          |          |  |  |  alu fcn          wd/word?|               |              |     |      |
      //                          |          |  |  |  |                |       |               |              |     |      |
      (uopLD)    .toM() -> List(BR_N.toM() , N, N, Y, FN_ADD.toM() , DW_XPR, OP1_RS1.toM() , OP2_IMM .toM(), IS_I, REN_0, CSR.N),
      (uopSTA)   .toM() -> List(BR_N.toM() , N, N, Y, FN_ADD.toM() , DW_XPR, OP1_RS1.toM() , OP2_IMM .toM(), IS_S, REN_0, CSR.N),
      (uopSTD)   .toM() -> List(BR_N.toM() , N, N, Y, FN_X         , DW_X  , OP1_RS1.toM() , OP2_RS2 .toM(), IS_X, REN_0, CSR.N),

      (uopAMO_AG).toM() -> List(BR_N.toM() , N, N, Y, FN_ADD.toM() , DW_XPR, OP1_RS1.toM() , OP2_ZERO.toM(), IS_X, REN_0, CSR.N))
}

object CsrRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                          br type
      //                          |          use alu pipe                      op1 sel         op2 sel
      //                          |          |  use muldiv pipe                |               |              immsel       csr_cmd
      //                          |          |  |  use mem pipe                |               |              |     rf wen |
      //                          |          |  |  |  alu fcn          wd/word?|               |              |     |      |
      //                          |          |  |  |  |                |       |               |              |     |      |
      (uopCSRRW) .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_RS1 .toM(), OP2_ZERO.toM(), IS_I, REN_1, CSR.W),
      (uopCSRRS) .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_RS1 .toM(), OP2_ZERO.toM(), IS_I, REN_1, CSR.S),
      (uopCSRRC) .toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_RS1 .toM(), OP2_ZERO.toM(), IS_I, REN_1, CSR.C),

      (uopCSRRWI).toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_ZERO.toM(), OP2_IMMC.toM(), IS_I, REN_1, CSR.W),
      (uopCSRRSI).toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_ZERO.toM(), OP2_IMMC.toM(), IS_I, REN_1, CSR.S),
      (uopCSRRCI).toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_ZERO.toM(), OP2_IMMC.toM(), IS_I, REN_1, CSR.C),

      (uopSYSTEM).toM() -> List(BR_N.toM() , Y, N, N, FN_ADD.toM() , DW_XPR, OP1_ZERO.toM(), OP2_IMMC.toM(), IS_I, REN_0, CSR.I))
}

object FpuRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                          br type
      //                          |          use alu pipe                op1 sel   op2 sel
      //                          |          |  use muldiv pipe          |         |        immsel       csr_cmd
      //                          |          |  |  use mem pipe          |         |        |     rf wen |
      //                          |          |  |  |  alu fcn    wd/word?|         |        |     |      |
      //                          |          |  |  |  |          |       |         |        |     |      |
      (uopFCLASS_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCLASS_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFMV_S_X)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMV_D_X)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMV_X_S)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMV_X_D)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSGNJ_S)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSGNJ_D)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFCVT_S_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_D_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFCVT_S_W) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_S_WU).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_S_L) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_S_LU).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_D_W) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_D_WU).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_D_L) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_D_LU).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFCVT_W_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_WU_S).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_L_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_LU_S).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_W_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_WU_D).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_L_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFCVT_LU_D).toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFEQ_S)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFLT_S)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFLE_S)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFEQ_D)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFLT_D)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFLE_D)    .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFMIN_S)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMAX_S)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMIN_D)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMAX_D)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFADD_S)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSUB_S)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMUL_S)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFADD_D)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSUB_D)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMUL_D)   .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),

      (uopFMADD_S)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMSUB_S)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFNMADD_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFNMSUB_S) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMADD_D)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFMSUB_D)  .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFNMADD_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFNMSUB_D) .toM() ->List(BR_N.toM(),Y, N, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N))
}


object FDivRRdDecode extends RRdDecodeConstants
{
  val table: Array[(MaskedLiteral, List[MaskedLiteral])] =
    Array[(MaskedLiteral, List[MaskedLiteral])](
      //                          br type
      //                          |          use alu pipe               op1 sel   op2 sel
      //                          |          |  use muldiv pipe         |         |         immsel       csr_cmd
      //                          |          |  |  use mem pipe         |         |         |     rf wen |
      //                          |          |  |  |  alu fcn   wd/word?|         |         |     |      |
      //                          |          |  |  |  |         |       |         |         |     |      |
      (uopFDIV_S)  .toM() ->List(BR_N.toM(), N, Y, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFDIV_D)  .toM() ->List(BR_N.toM(), N, Y, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSQRT_S) .toM() ->List(BR_N.toM(), N, Y, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N),
      (uopFSQRT_D) .toM() ->List(BR_N.toM(), N, Y, N, FN_X   , DW_X  , OP1_X   , OP2_X   , IS_X, REN_1, CSR.N))
}

class RegisterReadDecode(supported_units: SupportedFuncUnits)(implicit p: Parameters) extends Module
{
  val io = new Bundle
  {
    val iss_valid = in Bool()
    val iss_uop   = new MicroOp().asInput

    val rrd_valid = out Bool()
    val rrd_uop   = new MicroOp().asOutput
  }

  // Issued Instruction
  val rrd_valid = io.iss_valid
  io.rrd_uop   := io.iss_uop

  var dec_table = AluRRdDecode.table
  if (supported_units.bru) dec_table ++= BruRRdDecode.table
  if (supported_units.mem) dec_table ++= MemRRdDecode.table
  if (supported_units.muld) dec_table ++= MulDivRRdDecode.table
  if (supported_units.csr) dec_table ++= CsrRRdDecode.table
  if (supported_units.fpu) dec_table ++= FpuRRdDecode.table
  if (supported_units.fdiv) dec_table ++= FDivRRdDecode.table
  val rrd_cs = (new RRdCtrlSigs()).decode(io.rrd_uop.uopc.asBits.asUInt, dec_table)

  // rrd_use_alupipe is unused
  io.rrd_uop.ctrl.br_type := rrd_cs.br_type
  io.rrd_uop.ctrl.rf_wen  := rrd_cs.rf_wen
  io.rrd_uop.ctrl.op1_sel := rrd_cs.op1_sel
  io.rrd_uop.ctrl.op2_sel := rrd_cs.op2_sel
  io.rrd_uop.ctrl.imm_sel := rrd_cs.imm_sel
  io.rrd_uop.ctrl.op_fcn  := rrd_cs.op_fcn
  io.rrd_uop.ctrl.fcn_dw  := rrd_cs.fcn_dw
  io.rrd_uop.ctrl.is_load := io.rrd_uop.uopc === uopLD
  io.rrd_uop.ctrl.is_sta  := io.rrd_uop.uopc === uopSTA || io.rrd_uop.uopc === uopAMO_AG
  io.rrd_uop.ctrl.is_std  := io.rrd_uop.uopc === uopSTD || (io.rrd_uop.ctrl.is_sta && io.rrd_uop.lrs2_rtype =/= RT_X.asUInt())

  when (io.rrd_uop.uopc === uopAMO_AG)
  {
    io.rrd_uop.imm_packed := U(0)
  }

  val raddr1 = io.rrd_uop.pop1 // although renamed, it'll stay 0 if lrs1 = 0
  val csr_ren = (rrd_cs.csr_cmd === CSR.S || rrd_cs.csr_cmd === CSR.C) && raddr1 === U(0)
  io.rrd_uop.ctrl.csr_cmd := Mux(csr_ren, CSR.R.asUInt(), rrd_cs.csr_cmd.asUInt)


  require (rrd_cs.op_fcn.getBitsWidth == FN_SRA.getBitsWidth)

  //-------------------------------------------------------------
  // set outputs

  io.rrd_valid := rrd_valid

}
