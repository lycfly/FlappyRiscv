package boom_v1.decode

//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------

import boom_v1.ExcCauseConstants._
import boom_v1.FUType._
import boom_v1.IMMType._
import boom_v1.Instructions._
import boom_v1.MSKType._
import boom_v1.MemoryOpConstants._
import boom_v1.ScalarOpConstants._
import boom_v1.{CSRType, Causes, FUType, IMMType, MEMType, MaskedDC, MicroOp, Parameters, UOPs}
import boom_v1.UOPs._
import boom_v1.MemoryOpConstants.MEMOP._
import boom_v1.RISCVConstants._
import boom_v1.Utils.GetNewBrMask
import spinal.core._
import spinal.lib._
import boom_v1.decode.DecodeLogic
import boom_v1.exec.BrResolutionInfo
import boom_v1.fetch.FetchBundle
import boom_v1.predictor.BranchPredictionResp
import boom_v1.regfile.MStatus
//import rocket.Instructions._
//import rocket.{CSR,Causes}
//import util.uintToMaskedLiteral
//import FUConstants._
//import uncore.constants.MemoryOpConstants._



  abstract trait DecodeConstants
  {
    // scalastyle:off
    val xpr64 = Y // TODO inform this from xLen

    def decode_default: List[MaskedLiteral] =
    //                                                           frs3_en                                wakeup_delay
    //     is val inst?                                          |  imm sel                             |                    bypassable (aka, known/fixed latency)
    //     |  is fp inst?                                        |  |     is_load                       |                    |  br/jmp
    //     |  |  is single-prec?               rs1 regtype       |  |     |  is_store                   |                    |  |  is jal
    //     |  |  |  micro-code                 |         rs2 type|  |     |  |  is_amo                  |                    |  |  |  allocate_brtag
    //     |  |  |  |         func unit        |         |       |  |     |  |  |  is_fence             |                    |  |  |  |
    //     |  |  |  |         |                |         |       |  |     |  |  |  |  is_fencei         |                    |  |  |  |
    //     |  |  |  |         |        dst     |         |       |  |     |  |  |  |  |  mem    mem     |                    |  |  |  |  is unique? (clear pipeline for it)
    //     |  |  |  |         |        regtype |         |       |  |     |  |  |  |  |  cmd    msk     |                    |  |  |  |  |  flush on commit
    //     |  |  |  |         |        |       |         |       |  |     |  |  |  |  |  |      |       |                    |  |  |  |  |  |  csr cmd
      List(N, N, X,uopX ,     FU_X , RT_X,MaskedDC(2),MaskedDC(2),X,IS_X,  X, X, X, X, N, M_X,   MSK_X,  MaskedDC(2),        X, X, X, X, N, X,CSR_X)

    val table: Array[(MaskedLiteral, List[MaskedLiteral])]
    // scalastyle:on
  }

  class CtrlSigs extends Bundle
  {
    val legal           = Bool()
    val fp_val          = Bool()
    val fp_single       = Bool()
    val uopc            = UInt(width = UOPs().getBitsWidth bits)
    val fu_code         = UInt(width = FUType().getBitsWidth bits)
    val dst_type        = UInt(width=2 bits)
    val rs1_type        = UInt(width=2 bits)
    val rs2_type        = UInt(width=2 bits)
    val frs3_en         = Bool()
    val imm_sel         = UInt(width = IS_X.getWidth bits)
    val is_load         = Bool()
    val is_store        = Bool()
    val is_amo          = Bool()
    val is_fence        = Bool()
    val is_fencei       = Bool()
    val mem_cmd         = UInt(width = MEMOP().getBitsWidth bits)
    val mem_typ         = UInt(width = MEMType().getBitsWidth bits)
    val wakeup_delay    = UInt(width = 2 bits)
    val bypassable      = Bool()
    val br_or_jmp       = Bool()
    val is_jal          = Bool()
    val allocate_brtag  = Bool()
    val inst_unique     = Bool()
    val flush_on_commit = Bool()
    val csr_cmd         = UInt(width = CSRType().getBitsWidth bits)
    val rocc            = Bool()

    def decode(inst: UInt, table: Iterable[(MaskedLiteral, List[MaskedLiteral])]) = {
      val decoder = DecodeLogic(inst, XDecode.decode_default, table)
      val sigs =
        Seq(legal, fp_val, fp_single, uopc, fu_code, dst_type, rs1_type
          , rs2_type, frs3_en, imm_sel, is_load, is_store, is_amo
          , is_fence, is_fencei, mem_cmd, mem_typ, wakeup_delay, bypassable
          , br_or_jmp, is_jal, allocate_brtag, inst_unique, flush_on_commit, csr_cmd)
      sigs zip decoder foreach {case(s,d) => s := d}
      rocc := Bool(value = false)
      this
    }
  }


  object XDecode extends DecodeConstants
  {
    implicit def int2Masked(i:Int) = new MaskedLiteral( BigInt(i), BigInt(2).pow(log2Up(i+1)) - 1 , log2Up(i+1))
    // scalastyle:off
    //                                                                                frs3_en                                                    wakeup_delay
    //                                                                                |  imm sel                                                 |     bypassable (aka, known/fixed latency)
    //                                                                                |  |           is_load                                     |     |  br/jmp
    //                is val inst?                                    rs1 regtype     |  |           |  is_store                                 |     |  |  is jal
    //                |  is fp inst?                                  |       rs2 type|  |           |  |  is_amo                                |     |  |  |  allocate_brtag
    //                |  |  is dst single-prec?                       |       |       |  |           |  |  |  is_fence                           |     |  |  |  |
    //                |  |  |  micro-opcode                           |       |       |  |           |  |  |  |  is_fencei                       |     |  |  |  |
    //                |  |  |  |                   func       dst     |       |       |  |           |  |  |  |  |  mem            mem           |     |  |  |  |  is unique? (clear pipeline for it)
    //                |  |  |  |                   unit       regtype |       |       |  |           |  |  |  |  |  cmd            msk           |     |  |  |  |  |  flush on commit
    //                |  |  |  |                   |          |       |       |       |  |           |  |  |  |  |  |              |             |     |  |  |  |  |  |   csr cmd
    val table: Array[(MaskedLiteral, List[MaskedLiteral])] = Array(
      LD      -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_D .toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LW      -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_W .toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LWU     -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_WU.toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LH      -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_H .toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LHU     -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_HU.toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LB      -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_B .toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),
      LBU     -> List(Y, N, X, uopLD.toM()   , FU_MEM .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM()  , MSK_BU.toM(), 3, N, N, N, N, N, N, CSRType.N.toM()),

      SD      -> List(Y, N, X, uopSTA.toM()  , FU_MEM .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM()  , MSK_D .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      SW      -> List(Y, N, X, uopSTA.toM()  , FU_MEM .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM()  , MSK_W .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      SH      -> List(Y, N, X, uopSTA.toM()  , FU_MEM .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM()  , MSK_H .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      SB      -> List(Y, N, X, uopSTA.toM()  , FU_MEM .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM()  , MSK_B .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),

      LUI     -> List(Y, N, X, uopLUI.toM()  , FU_ALU .toM(), RT_FIX, RT_X  , RT_X  , N, IS_U.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),

      ADDI    -> List(Y, N, X, uopADDI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      ANDI    -> List(Y, N, X, uopANDI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      ORI     -> List(Y, N, X, uopORI.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      XORI    -> List(Y, N, X, uopXORI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLTI    -> List(Y, N, X, uopSLTI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLTIU   -> List(Y, N, X, uopSLTIU.toM(), FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLLI    -> List(Y, N, X, uopSLLI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRAI    -> List(Y, N, X, uopSRAI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRLI    -> List(Y, N, X, uopSRLI.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),

      ADDIW   -> List(Y, N, X, uopADDIW.toM(), FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLLIW   -> List(Y, N, X, uopSLLIW.toM(), FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRAIW   -> List(Y, N, X, uopSRAIW.toM(), FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRLIW   -> List(Y, N, X, uopSRLIW.toM(), FU_ALU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),

      SLL     -> List(Y, N, X, uopSLL.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      ADD     -> List(Y, N, X, uopADD.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SUB     -> List(Y, N, X, uopSUB.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLT     -> List(Y, N, X, uopSLT.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLTU    -> List(Y, N, X, uopSLTU.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      AND     -> List(Y, N, X, uopAND.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      OR      -> List(Y, N, X, uopOR.toM()   , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      XOR     -> List(Y, N, X, uopXOR.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRA     -> List(Y, N, X, uopSRA.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRL     -> List(Y, N, X, uopSRL.toM()  , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),

      ADDW    -> List(Y, N, X, uopADDW.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SUBW    -> List(Y, N, X, uopSUBW.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SLLW    -> List(Y, N, X, uopSLLW.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRAW    -> List(Y, N, X, uopSRAW.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),
      SRLW    -> List(Y, N, X, uopSRLW.toM() , FU_ALU .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 1, Y, N, N, N, N, N, CSRType.N.toM()),

      MUL     -> List(Y, N, X, uopMUL  .toM(), FU_MUL .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      MULH    -> List(Y, N, X, uopMULH .toM(), FU_MUL .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      MULHU   -> List(Y, N, X, uopMULHU.toM(), FU_MUL .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      MULHSU  -> List(Y, N, X,uopMULHSU.toM(), FU_MUL .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      MULW    -> List(Y, N, X, uopMULW .toM(), FU_MUL .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      DIV     -> List(Y, N, X, uopDIV  .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      DIVU    -> List(Y, N, X, uopDIVU .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      REM     -> List(Y, N, X, uopREM  .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      REMU    -> List(Y, N, X, uopREMU .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      DIVW    -> List(Y, N, X, uopDIVW .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      DIVUW   -> List(Y, N, X, uopDIVUW.toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      REMW    -> List(Y, N, X, uopREMW .toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      REMUW   -> List(Y, N, X, uopREMUW.toM(), FU_DIV .toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      AUIPC   -> List(Y, N, X, uopAUIPC.toM(), FU_BRU .toM(), RT_FIX, RT_X  , RT_X  , N, IS_U.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, N, N, N, N, N, N, CSRType.N.toM()), // use BRU for the PC read
      JAL     -> List(Y, N, X, uopJAL  .toM(), FU_BRU .toM(), RT_FIX, RT_X  , RT_X  , N, IS_J.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, N, Y, Y, N, N, N, CSRType.N.toM()),
      JALR    -> List(Y, N, X, uopJALR .toM(), FU_BRU .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 1, N, Y, N, Y, N, N, CSRType.N.toM()),
      BEQ     -> List(Y, N, X, uopBEQ  .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),
      BNE     -> List(Y, N, X, uopBNE  .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),
      BGE     -> List(Y, N, X, uopBGE  .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),
      BGEU    -> List(Y, N, X, uopBGEU .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),
      BLT     -> List(Y, N, X, uopBLT  .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),
      BLTU    -> List(Y, N, X, uopBLTU .toM(), FU_BRU .toM(), RT_X  , RT_FIX, RT_FIX, N, IS_B.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, Y, N, Y, N, N, CSRType.N.toM()),

      // I-type, the immediate12 holds the CSR register.
      CSRRW   -> List(Y, N, X, uopCSRRW.toM(), FU_CSR .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.W.toM()),
      CSRRS   -> List(Y, N, X, uopCSRRS.toM(), FU_CSR .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.S.toM()),
      CSRRC   -> List(Y, N, X, uopCSRRC.toM(), FU_CSR .toM(), RT_FIX, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.C.toM()),

      CSRRWI  -> List(Y, N, X, uopCSRRWI.toM(),FU_CSR .toM(), RT_FIX, RT_PAS, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.W.toM()),
      CSRRSI  -> List(Y, N, X, uopCSRRSI.toM(),FU_CSR .toM(), RT_FIX, RT_PAS, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.S.toM()),
      CSRRCI  -> List(Y, N, X, uopCSRRCI.toM(),FU_CSR .toM(), RT_FIX, RT_PAS, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.C.toM()),

      SFENCE_VM->List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),
      SCALL   -> List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),
      SBREAK  -> List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),
      SRET    -> List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),
      MRET    -> List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),
      DRET    -> List(Y, N, X, uopSYSTEM.toM(),FU_CSR .toM(), RT_X  , RT_X  , RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, N, CSRType.I.toM()),

      WFI     -> List(Y, N, X, uopNOP   .toM(),FU_X         , RT_X  , RT_X  , RT_X  , N, IS_X      , N, N, N, N, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.N.toM()), // implemented as a NOP; TODO

      FENCE_I -> List(Y, N, X, uopNOP  .toM(), FU_X         , RT_X  , RT_X  , RT_X  , N, IS_X      , N, N, N, N, Y, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      FENCE   -> List(Y, N, X, uopFENCE.toM(), FU_MEM .toM(), RT_X  , RT_X  , RT_X  , N, IS_X      , N, Y, N, Y, N, M_X          , MSK_X       , 0, N, N, N, N, Y, Y, CSRType.N.toM()), // TODO PERF make fence higher performance
      // currently serializes pipeline
      // A-type
      AMOADD_W-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_ADD.toM(), MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()), // TODO make AMOs higherperformance
      AMOXOR_W-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_XOR.toM(), MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOSWAP_W->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_SWAP.toM(),MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOAND_W-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_AND.toM(), MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOOR_W -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_OR.toM(),  MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMIN_W-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MIN.toM(), MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMINU_W->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MINU.toM(),MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMAX_W-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MAX.toM(), MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMAXU_W->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MAXU.toM(),MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),

      AMOADD_D-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_ADD.toM(), MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOXOR_D-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_XOR.toM(), MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOSWAP_D->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_SWAP.toM(),MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOAND_D-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_AND.toM(), MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOOR_D -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_OR.toM(),  MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMIN_D-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MIN.toM(), MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMINU_D->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MINU.toM(),MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMAX_D-> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MAX.toM(), MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),
      AMOMAXU_D->List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XA_MAXU.toM(),MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()),

      LR_W    -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XLR.toM()   , MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()), // TODO optimize LR, SC
      LR_D    -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XLR.toM()   , MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()), // note LR generates 2 micro-ops,
      SC_W    -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XSC.toM()   , MSK_W.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM()), // one which isn't needed
      SC_D    -> List(Y, N, X, uopAMO_AG.toM(), FU_MEM.toM(), RT_FIX, RT_FIX, RT_FIX, N, IS_X      , N, Y, Y, N, N, M_XSC.toM()   , MSK_D.toM(), 0, N, N, N, N, Y, Y, CSRType.N.toM())
    )
    // scalastyle:on
  }

  object FDecode extends DecodeConstants
  {
    import XDecode.int2Masked
    // scalastyle:off
    val table: Array[(MaskedLiteral, List[MaskedLiteral])] = Array(
      //                                                                               frs3_en                                                  wakeup_delay
      //                                                                               |  imm sel                                               |     bypassable (aka, known/fixed latency)
      //                                                                               |  |           is_load                                   |     |  br/jmp
      //              is val inst?                                     rs1 regtype     |  |           |  is_store                               |     |  |  is jal
      //              |  is fp inst?                                   |       rs2 type|  |           |  |  is_amo                              |     |  |  |  allocate_brtag
      //              |  |  is dst single-prec?                        |       |       |  |           |  |  |  is_fence                         |     |  |  |  |
      //              |  |  |  micro-opcode                            |       |       |  |           |  |  |  |  is_fencei                     |     |  |  |  |
      //              |  |  |  |                    func       dst     |       |       |  |           |  |  |  |  |  mem          mem           |     |  |  |  |  is unique? (clear pipeline for it)
      //              |  |  |  |                    unit       regtype |       |       |  |           |  |  |  |  |  cmd          msk           |     |  |  |  |  |  flush on commit
      //              |  |  |  |                    |          |       |       |       |  |           |  |  |  |  |  |            |             |     |  |  |  |  |  |   csr cmd
      FLW     -> List(Y, Y, Y, uopLD.toM()     , FU_MEM.toM(), RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM(), MSK_W .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      FLD     -> List(Y, Y, N, uopLD.toM()     , FU_MEM.toM(), RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), Y, N, N, N, N, M_XRD.toM(), MSK_D .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSW     -> List(Y, Y, Y, uopSTA.toM()    , FU_MEM.toM(), RT_X  , RT_FIX, RT_FLT, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM(), MSK_W .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSD     -> List(Y, Y, N, uopSTA.toM()    , FU_MEM.toM(), RT_X  , RT_FIX, RT_FLT, N, IS_S.toM(), N, Y, N, N, N, M_XWR.toM(), MSK_D .toM(), 0, N, N, N, N, N, N, CSRType.N.toM()),

      FCLASS_S-> List(Y, Y, Y, uopFCLASS_S.toM(),FU_FPU.toM(), RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCLASS_D-> List(Y, Y, N, uopFCLASS_D.toM(),FU_FPU.toM(), RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FMV_S_X -> List(Y, Y, Y, uopFMV_S_X.toM(), FU_FPU.toM(), RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMV_D_X -> List(Y, Y, N, uopFMV_D_X.toM(), FU_FPU.toM(), RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMV_X_S -> List(Y, Y, Y, uopFMV_X_S.toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMV_X_D -> List(Y, Y, N, uopFMV_X_D.toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FSGNJ_S -> List(Y, Y, Y, uopFSGNJ_S.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSGNJ_D -> List(Y, Y, N, uopFSGNJ_D.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSGNJX_S-> List(Y, Y, Y, uopFSGNJ_S.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSGNJX_D-> List(Y, Y, N, uopFSGNJ_D.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSGNJN_S-> List(Y, Y, Y, uopFSGNJ_S.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSGNJN_D-> List(Y, Y, N, uopFSGNJ_D.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      // FP to FP
      FCVT_S_D-> List(Y, Y, Y, uopFCVT_S_D.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_D_S-> List(Y, Y, N, uopFCVT_D_S.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      // Int to FP
      FCVT_S_W-> List(Y, Y, Y, uopFCVT_S_W .toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_S_WU->List(Y, Y, Y, uopFCVT_S_WU.toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_S_L-> List(Y, Y, Y, uopFCVT_S_L .toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_S_LU->List(Y, Y, Y, uopFCVT_S_LU.toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FCVT_D_W-> List(Y, Y, N, uopFCVT_D_W .toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_D_WU->List(Y, Y, N, uopFCVT_D_WU.toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_D_L-> List(Y, Y, N, uopFCVT_D_L .toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_D_LU->List(Y, Y, N, uopFCVT_D_LU.toM(),FU_FPU.toM(),RT_FLT, RT_FIX, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      // FP to Int
      FCVT_W_S-> List(Y, Y, Y, uopFCVT_W_S .toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_WU_S->List(Y, Y, Y, uopFCVT_WU_S.toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_L_S-> List(Y, Y, Y, uopFCVT_L_S .toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_LU_S->List(Y, Y, Y, uopFCVT_LU_S.toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FCVT_W_D-> List(Y, Y, N, uopFCVT_W_D .toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_WU_D->List(Y, Y, N, uopFCVT_WU_D.toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_L_D-> List(Y, Y, N, uopFCVT_L_D .toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FCVT_LU_D->List(Y, Y, N, uopFCVT_LU_D.toM(),FU_FPU.toM(),RT_FIX, RT_FLT, RT_X  , N, IS_I.toM(), N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      // "fp_single" is used for wb_data formatting (and debugging)
      FEQ_S    ->List(Y, Y, Y, uopFEQ_S  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FLT_S    ->List(Y, Y, Y, uopFLT_S  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FLE_S    ->List(Y, Y, Y, uopFLE_S  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FEQ_D    ->List(Y, Y, N, uopFEQ_D  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FLT_D    ->List(Y, Y, N, uopFLT_D  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FLE_D    ->List(Y, Y, N, uopFLE_D  .toM(), FU_FPU.toM(), RT_FIX, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FMIN_S   ->List(Y, Y, Y, uopFMIN_S .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMAX_S   ->List(Y, Y, Y, uopFMAX_S .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMIN_D   ->List(Y, Y, N, uopFMIN_D .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMAX_D   ->List(Y, Y, N, uopFMAX_D .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FADD_S   ->List(Y, Y, Y, uopFADD_S .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSUB_S   ->List(Y, Y, Y, uopFSUB_S .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMUL_S   ->List(Y, Y, Y, uopFMUL_S .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FADD_D   ->List(Y, Y, N, uopFADD_D .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSUB_D   ->List(Y, Y, N, uopFSUB_D .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMUL_D   ->List(Y, Y, N, uopFMUL_D .toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),

      FMADD_S  ->List(Y, Y, Y, uopFMADD_S.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMSUB_S  ->List(Y, Y, Y, uopFMSUB_S.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FNMADD_S ->List(Y, Y, Y, uopFNMADD_S.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FNMSUB_S ->List(Y, Y, Y, uopFNMSUB_S.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMADD_D  ->List(Y, Y, N, uopFMADD_D.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FMSUB_D  ->List(Y, Y, N, uopFMSUB_D.toM(), FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FNMADD_D ->List(Y, Y, N, uopFNMADD_D.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FNMSUB_D ->List(Y, Y, N, uopFNMSUB_D.toM(),FU_FPU.toM(), RT_FLT, RT_FLT, RT_FLT, Y, IS_X      , N, N, N, N, N, M_X        , MSK_X       , 0, N, N, N, N, N, N, CSRType.N.toM())
    )

    // scalastyle:on
  }

  object FDivSqrtDecode extends DecodeConstants
  {
    // scalastyle:off
    val table: Array[(MaskedLiteral, List[MaskedLiteral])] = Array(
      //                                                                    frs3_en                                wakeup_delay
      //                                                                    |  imm sel                             |     bypassable (aka, known/fixed latency)
      //                                                                    |  |     is_load                       |     |  br/jmp
      //               is val inst?                         rs1 regtype     |  |     |  is_store                   |     |  |  is jal
      //               |  is fp inst?                       |       rs2 type|  |     |  |  is_amo                  |     |  |  |  allocate_brtag
      //               |  |  is dst single-prec?            |       |       |  |     |  |  |  is_fence             |     |  |  |  |
      //               |  |  |  micro-opcode                |       |       |  |     |  |  |  |  is_fencei         |     |  |  |  |
      //               |  |  |  |           func    dst     |       |       |  |     |  |  |  |  |  mem    mem     |     |  |  |  |  is unique? (clear pipeline for it)
      //               |  |  |  |           unit    regtype |       |       |  |     |  |  |  |  |  cmd    msk     |     |  |  |  |  |  flush on commit
      //               |  |  |  |           |       |       |       |       |  |     |  |  |  |  |  |      |       |     |  |  |  |  |  |  csr cmd
      FDIV_S    ->List(Y, Y, Y, uopFDIV_S .toM(), FU_FDV.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X, N, N, N, N, N, M_X  , MSK_X , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FDIV_D    ->List(Y, Y, N, uopFDIV_D .toM(), FU_FDV.toM(), RT_FLT, RT_FLT, RT_FLT, N, IS_X, N, N, N, N, N, M_X  , MSK_X , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSQRT_S   ->List(Y, Y, Y, uopFSQRT_S.toM(), FU_FDV.toM(), RT_FLT, RT_FLT, RT_X  , N, IS_X, N, N, N, N, N, M_X  , MSK_X , 0, N, N, N, N, N, N, CSRType.N.toM()),
      FSQRT_D   ->List(Y, Y, N, uopFSQRT_D.toM(), FU_FDV.toM(), RT_FLT, RT_FLT, RT_X  , N, IS_X, N, N, N, N, N, M_X  , MSK_X , 0, N, N, N, N, N, N, CSRType.N.toM())
    )
    // scalastyle:on
  }


  class DecodeUnitIo(implicit p: Parameters) extends Bundle
  {
    val enq = new Bundle { val uop = new MicroOp().asInput }
    val deq = new Bundle { val uop = new MicroOp().asOutput }
    // from CSRFile
    val status = new MStatus().asInput
    val interrupt = in Bool()
    val interrupt_cause = in UInt(p.xLen bits)

  }

  // Takes in a single instruction, generates a MicroOp (or multiply micro-ops over x cycles)
  class DecodeUnit(implicit p: Parameters) extends Bundle
  {
    val io = new DecodeUnitIo

    val uop = (new MicroOp())
    uop := io.enq.uop

    var decode_table = XDecode.table
    if (p.usingFPU) decode_table ++= FDecode.table
    if (p.usingFPU && p.usingFDivSqrt) decode_table ++= FDivSqrtDecode.table

    val cs = (new CtrlSigs()).decode(uop.inst, decode_table)

    // Exception Handling
    val id_illegal_insn = !cs.legal ||
      cs.fp_val && !io.status.fs.orR ||
      cs.rocc && !io.status.xs.orR

    def checkExceptions(x: Seq[(Bool, UInt)]) =
      (x.map(_._1).reduce(_||_), PriorityMux(x))

    val (xcpt_valid, xcpt_cause) = checkExceptions(List(
      (io.interrupt,     io.interrupt_cause),
      (uop.replay_if,    MINI_EXCEPTION_REPLAY),
      (uop.xcpt_if,      UInt(Causes.fault_fetch bits)),
      (id_illegal_insn,  UInt(Causes.illegal_instruction bits))))

    uop.exception := xcpt_valid
    uop.exc_cause := xcpt_cause

    //-------------------------------------------------------------

    uop.uopc       := cs.uopc
    uop.fu_code    := cs.fu_code

    // x-registers placed in 0-31, f-registers placed in 32-63.
    // This allows us to straight-up compare register specifiers and not need to
    // verify the rtypes (e.g., bypassing in rename).
    uop.ldst       := Cat(cs.dst_type === RT_FLT, uop.inst(RD_MSB downto RD_LSB))
    uop.lrs1       := Cat(cs.rs1_type === RT_FLT, uop.inst(RS1_MSB downto RS1_LSB))
    uop.lrs2       := Cat(cs.rs2_type === RT_FLT, uop.inst(RS2_MSB downto RS2_LSB))
    uop.lrs3       := Cat(Bool(true),      uop.inst(RS3_MSB downto RS3_LSB))
    // TODO do I need to remove (uop.lrs3) for integer-only? Or do synthesis tools properly remove it?

    uop.ldst_val   := (cs.dst_type =/= RT_X && uop.ldst =/= U(0))
    uop.dst_rtype  := cs.dst_type
    uop.lrs1_rtype := cs.rs1_type
    uop.lrs2_rtype := cs.rs2_type
    uop.frs3_en    := cs.frs3_en

    uop.fp_val     := cs.fp_val
    uop.fp_single  := cs.fp_single // TODO use this signal instead of the FPU decode's table signal?

    uop.mem_cmd    := cs.mem_cmd
    uop.mem_typ    := cs.mem_typ
    uop.is_load    := cs.is_load
    uop.is_store   := cs.is_store
    uop.is_amo     := cs.is_amo
    uop.is_fence   := cs.is_fence
    uop.is_fencei  := cs.is_fencei
    uop.is_unique  := cs.inst_unique
    uop.flush_on_commit := cs.flush_on_commit

    uop.wakeup_delay := cs.wakeup_delay
    uop.bypassable   := cs.bypassable

    //-------------------------------------------------------------
    // immediates

    // repackage the immediate, and then pass the fewest number of bits around
    val di24_20 = Mux(cs.imm_sel === IS_B.asBits.asUInt || cs.imm_sel === IS_S.asBits.asUInt, uop.inst(11 downto 7), uop.inst(24 downto 20))
    uop.imm_packed := Cat(uop.inst(31 downto 25), di24_20, uop.inst(19 downto 12)).asUInt

    //-------------------------------------------------------------

    uop.allocate_brtag := cs.allocate_brtag
    uop.is_br_or_jmp   := cs.br_or_jmp
    uop.is_jal         := cs.is_jal
    uop.is_jump        := cs.is_jal || (uop.uopc === uopJALR)
    uop.is_ret         := (uop.uopc === uopJALR) &&
      (uop.ldst === X0) &&
      (uop.lrs1 === RA)
    uop.is_call        := (uop.uopc === uopJALR || uop.uopc === uopJAL) &&
      (uop.ldst === RA)

    //-------------------------------------------------------------

    io.deq.uop := uop

    //-------------------------------------------------------------
  }


  class BranchDecode extends Module
  {
    val io = new Bundle
    {
      val inst    = in UInt(32 bits)
      val is_br   = out Bool()
      val is_jal  = out Bool()
      val is_jalr = out Bool()
    }

    val bpd_csignals =
      DecodeLogic(io.inst,
        List[MaskedLiteral](N, N, N, IS_X),
        ////         //   is br?
        ////         //   |  is jal?
        ////         //   |  |  is jalr?
        ////         //   |  |  |  br type
        ////         //   |  |  |  |
        Array[(MaskedLiteral, List[MaskedLiteral])](
          JAL     -> List(N, Y, N, IMMType.IS_J.toM()),
          JALR    -> List(N, N, Y, IMMType.IS_I.toM()),
          BEQ     -> List(Y, N, N, IMMType.IS_B.toM()),
          BNE     -> List(Y, N, N, IMMType.IS_B.toM()),
          BGE     -> List(Y, N, N, IMMType.IS_B.toM()),
          BGEU    -> List(Y, N, N, IMMType.IS_B.toM()),
          BLT     -> List(Y, N, N, IMMType.IS_B.toM()),
          BLTU    -> List(Y, N, N, IMMType.IS_B.toM())
        ))

    val (cs_is_br: Bool) :: (cs_is_jal: Bool) :: (cs_is_jalr:Bool) :: imm_sel_ :: Nil = bpd_csignals

    io.is_br   := cs_is_br
    io.is_jal  := cs_is_jal
    io.is_jalr := cs_is_jalr
  }


  class FetchSerializerResp(implicit p: Parameters) extends Bundle
  {
    val uops = Vec(new MicroOp(), p.DECODE_WIDTH)
    val pred_resp = new BranchPredictionResp()
  }
  class FetchSerializerIO(implicit p: Parameters) extends Bundle
  {
    val enq = slave Stream(new FetchBundle())
    val deq = master Stream(new FetchSerializerResp)
    val kill = in Bool()
  }


  // TODO horrific hodgepodge, needs refactoring
  // connect a N-word wide Fetch Buffer with a M-word decode
  // currently only works for 2 wide fetch to 1 wide decode, OR N:N fetch/decode
  // TODO instead of counter, clear mask bits as instructions are finished?
  class FetchSerializerNtoM(implicit p: Parameters) extends Bundle
  {
    val io = new FetchSerializerIO

    val counter = RegInit(U(0, log2Up(p.FETCH_WIDTH) bits))
    val inst_idx = (UInt())
    inst_idx := U(0)

    //-------------------------------------------------------------
    // Compute index for where to get the instruction
    when (counter === U(1))
    {
      inst_idx := U(1)
    }
      .otherwise
      {
        inst_idx := Mux(io.enq.payload.mask === U(2), U(1), U(0))
      }

    //-------------------------------------------------------------
    // Compute Enqueue Ready (get the next bundle)
    io.enq.ready := io.deq.ready &&
      ((io.enq.payload.mask =/= 3) || (counter === U(1)))


    //-------------------------------------------------------------
    // Compute Counter
    when (io.kill || io.enq.ready)
    {
      // reset counter on every new bundle
      counter := U(0)
    }
      .elsewhen (io.deq.valid && io.deq.ready)
      {
        counter := counter + U(1)
      }


    //-------------------------------------------------------------
    // override all the above logic for FW==1
    if (p.FETCH_WIDTH == 1)
    {
      inst_idx := U(0)
      io.enq.ready := io.deq.ready
    }

    io.deq.payload.uops(0).pc             := io.enq.payload.pc
    io.deq.payload.uops(0).fetch_pc_lob   := io.enq.payload.pc
    io.deq.payload.uops(0).inst           := io.enq.payload.insts(inst_idx)
    io.deq.payload.uops(0).br_prediction  := io.enq.payload.predictions(inst_idx)
    io.deq.payload.uops(0).valid          := io.enq.payload.mask(inst_idx)
    io.deq.payload.uops(0).xcpt_if        := io.enq.payload.xcpt_if
    io.deq.payload.uops(0).replay_if        := io.enq.payload.replay_if
    io.deq.payload.uops(0).debug_events   := io.enq.payload.debug_events(inst_idx)

    //-------------------------------------------------------------
    // override all the above logic for DW>1
    // assume FW is also DW, and pass everything through
    if ((p.DECODE_WIDTH == p.FETCH_WIDTH) && (p.FETCH_WIDTH > 1))
    {
      // 1:1, so pass everything straight through!
      for (i <- 0 until p.DECODE_WIDTH)
      {
        io.deq.payload.uops(i).valid          := io.enq.payload.mask(i)
        io.deq.payload.uops(i).pc             := (io.enq.payload.pc.asSInt & S(-(p.FETCH_WIDTH*p.coreInstBytes))).asUInt + U(i << 2)
        io.deq.payload.uops(i).fetch_pc_lob   := io.enq.payload.pc
        io.deq.payload.uops(i).inst           := io.enq.payload.insts(i)
        io.deq.payload.uops(i).xcpt_if        := io.enq.payload.xcpt_if
        io.deq.payload.uops(i).replay_if        := io.enq.payload.replay_if
        io.deq.payload.uops(i).br_prediction  := io.enq.payload.predictions(i)
        io.deq.payload.uops(i).debug_events   := io.enq.payload.debug_events(i)
      }
      io.enq.ready := io.deq.ready
    }

    // Pipe valid straight through, since conceptually,
    // we are just an extension of the Fetch Buffer
    io.deq.valid := io.enq.valid
    io.deq.payload.pred_resp := io.enq.payload.pred_resp

  }


  // track the current "branch mask", and give out the branch mask to each micro-op in Decode
  // (each micro-op in the machine has a branch mask which says which branches it
  // is being speculated under).

  class DebugBranchMaskGenerationLogicIO(implicit p: Parameters) extends Bundle
  {
    val branch_mask = UInt(width = p.MAX_BR_COUNT bits)
  }

  class BranchMaskGenerationLogic(val pl_width: Int)(implicit p: Parameters) extends Module
  {
    val io = new Bundle
    {
      // guess if the uop is a branch (we'll catch this later)
      val is_branch = in Vec(Bool(), pl_width)
      // lock in that it's actually a branch and will fire, so we update
      // the branch_masks.
      val will_fire = in Vec(Bool(), pl_width)

      // give out tag immediately (needed in rename)
      // mask can come later in the cycle
      val br_tag    = out Vec(UInt(width=p.BR_TAG_SZ bits), pl_width)
      val br_mask   = out Vec(UInt(width=p.MAX_BR_COUNT bits), pl_width)

      // tell decoders the branch mask has filled up, but on the granularity
      // of an individual micro-op (so some micro-ops can go through)
      val is_full   = out Vec(Bool(), pl_width)

      val brinfo         = in(new BrResolutionInfo())
      val flush_pipeline = in Bool()

      val debug = new DebugBranchMaskGenerationLogicIO().asOutput
    }

    val branch_mask = RegInit(U(0, p.MAX_BR_COUNT bits))

    //-------------------------------------------------------------
    // Give out the branch tag to each branch micro-op

    var allocate_mask = branch_mask
    val tag_masks = (Vec(UInt(width=p.MAX_BR_COUNT bits),pl_width))

    for (w <- 0 until pl_width)
    {
      // TODO this is a loss of performance as we're blocking branches based on potentially fake branches
      io.is_full(w) := (allocate_mask === ~(U(0,p.MAX_BR_COUNT bits))) && io.is_branch(w)

      // find br_tag and compute next br_mask
      val new_br_tag = (UInt(width = p.BR_TAG_SZ bits))
      new_br_tag := U(0)
      tag_masks(w) := U(0)

      for (i <- p.MAX_BR_COUNT-1 to 0 by -1)
      {
        when (~allocate_mask(i))
        {
          new_br_tag := U(i)
          tag_masks(w) := (U(1) << U(i))
        }
      }

      io.br_tag(w) := new_br_tag
      allocate_mask = Mux(io.is_branch(w), tag_masks(w) | allocate_mask, allocate_mask)
    }

    //-------------------------------------------------------------
    // Give out the branch mask to each micro-op
    // (kill off the bits that corresponded to branches that aren't going to fire)

    var curr_mask = branch_mask
    for (w <- 0 until pl_width)
    {
      io.br_mask(w) := GetNewBrMask(io.brinfo, curr_mask)
      curr_mask = Mux(io.will_fire(w), tag_masks(w) | curr_mask, curr_mask)
    }

    //-------------------------------------------------------------
    // Update the current branch_mask

    when (io.flush_pipeline)
    {
      branch_mask := U(0)
    }
      .elsewhen (io.brinfo.valid && io.brinfo.mispredict)
      {
        branch_mask := io.brinfo.exe_mask
      }
      .otherwise
      {
        branch_mask := GetNewBrMask(io.brinfo, curr_mask)
      }

    io.debug.branch_mask := branch_mask
  }


