package boom_v1

import EasonLib.Utils.Enum.EasonEnum
import boom_v1.exec.ALUFN
import boom_v1.predictor.BranchPrediction
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

object UOPs extends EasonEnum {
  val uopNOP = newElement()
  val uopLD = newElement()
  val uopSTA = newElement() // store address generation
  val uopSTD = newElement() // store data generation
  val uopLUI = newElement()

  val uopADDI, uopANDI, uopORI, uopXORI, uopSLTI, uopSLTIU, uopSLLI, uopSRAI, uopSRLI, uopSLL = newElement()
  val uopADD, uopSUB, uopSLT, uopSLTU, uopAND, uopOR, uopXOR, uopSRA, uopSRL = newElement()

  val uopBEQ, uopBNE, uopBGE, uopBGEU, uopBLT, uopBLTU, uopCSRRW, uopCSRRS, uopCSRRC, uopCSRRWI, uopCSRRSI, uopCSRRCI = newElement()

  val uopJ, uopJAL, uopJALR, uopAUIPC = newElement()

  //   val uopSRET = UInt(40, UOPC_SZ)
  val uopCFLSH, uopFENCE = newElement()

  val uopADDIW, uopADDW, uopSUBW, uopSLLIW, uopSLLW, uopSRAIW, uopSRAW, uopSRLIW, uopSRLW, uopMUL = newElement()
  val uopMULH, uopMULHU, uopMULHSU, uopMULW, uopDIV, uopDIVU, uopREM, uopREMU, uopDIVW, uopDIVUW, uopREMW, uopREMUW = newElement()

  val uopFENCEI, uopAMO_AG = newElement() // AMO-address gen (use normal STD for datagen)

  val uopFMV_S_X, uopFMV_D_X, uopFMV_X_S, uopFMV_X_D = newElement()

  val uopFSGNJ_S, uopFSGNJ_D, uopFCVT_S_D, uopFCVT_D_S = newElement()

  val uopFCVT_S_W, uopFCVT_S_WU, uopFCVT_S_L, uopFCVT_S_LU, uopFCVT_D_W, uopFCVT_D_WU, uopFCVT_D_L, uopFCVT_D_LU = newElement()
  val uopFCVT_W_S, uopFCVT_WU_S, uopFCVT_L_S, uopFCVT_LU_S, uopFCVT_W_D, uopFCVT_WU_D, uopFCVT_L_D, uopFCVT_LU_D = newElement()

  val uopFEQ_S, uopFLT_S, uopFLE_S, uopFEQ_D, uopFLT_D, uopFLE_D = newElement()

  val uopFCLASS_S, uopFCLASS_D = newElement()

  val uopFMIN_S, uopFMAX_S, uopFMIN_D, uopFMAX_D = newElement()

  val uopFADD_S, uopFSUB_S, uopFMUL_S, uopFADD_D, uopFSUB_D, uopFMUL_D = newElement()

  val uopFMADD_S, uopFMSUB_S, uopFNMADD_S, uopFNMSUB_S, uopFMADD_D, uopFMSUB_D, uopFNMADD_D, uopFNMSUB_D = newElement()

  val uopFDIV_S, uopFDIV_D, uopFSQRT_S, uopFSQRT_D = newElement()

  val uopSYSTEM = newElement() // pass uop down the CSR pipeline and let it handle it



}

object FUType extends EasonEnum {
  // bit mask, since a given execution pipeline may support multiple functional units
  val FU_ALU, FU_BRU, FU_MEM, FU_MUL, FU_DIV, FU_FPU, FU_CSR, FU_FDV = newElement()
  defaultEncoding = binaryOneHot
}

object BranchType extends EasonEnum {
  // Branch Type
  val BR_N, BR_NE, BR_EQ, BR_GE, BR_GEU, BR_LT, BR_LTU, BR_J, BR_JR = newElement() // Jump Register
}
object MSKType extends EasonEnum {
  // Mask Type
  val MSK_B, MSK_H, MSK_W, MSK_D, MSK_BU, MSK_HU, MSK_WU = newElement()
}
object MEMType extends EasonEnum {
  // Mem Type
  val MT_B, MT_H, MT_W, MT_D, MT_BU, MT_HU, MT_WU = newElement()
}

object RS1Type extends EasonEnum {
  // RS1 Operand Select Signal
  val OP1_RS1, OP1_ZERO , OP1_PC = newElement()
}

object RS2Type extends EasonEnum {
  // RS2 Operand Select Signal
  val OP2_RS2 = newElement() // Register Source #2
  val OP2_IMM = newElement() // immediate
  val OP2_ZERO = newElement() // constant 0
  val OP2_FOUR = newElement() // constant 4 (for PC+4)
  val OP2_IMMC = newElement() // for CSR imm found in RS1}
}

object IMMType extends EasonEnum {
  // Immediate Extend Select
  val IS_I = newElement() // I-Type  (LD,ALU)
  val IS_S = newElement() // S-Type  (ST)
  val IS_B = newElement() // SB-Type (BR)
  val IS_U = newElement() // U-Type  (LUI/AUIPC)
  val IS_J = newElement() // UJ-Type (J/JAL)
}

object CSRType extends EasonEnum {
  val N = newElement()
  val W = newElement()
  val S = newElement()
  val C = newElement()
  val I = newElement()
  val R = newElement()
}

class MicroOp(implicit p: Parameters) extends Bundle {
  val valid = Bool() // is this uop valid? or has it been masked out,
  // used by fetch buffer and Decode stage
  val iw_state = UInt(2 bits) // what is the next state of this uop in the issue window? useful
  // for the compacting queue? TODO or is this not really belong
  // here?

  val uopc = UOPs() // micro-op code
  val inst = UInt(width = 32 bits)
  val pc = UInt(width = p.coreMaxAddrBits bits)
  val fu_code = FUType() // which functional unit do we use?
  val ctrl = new CtrlSignals

  val wakeup_delay = UInt(width = log2Up(p.MAX_WAKEUP_DELAY) bits) // unused
  val allocate_brtag = Bool() // does this allocate a branch tag? (is branch or JR but not JAL)
  val is_br_or_jmp = Bool() // is this micro-op a (branch or jump) vs a regular PC+4 inst?
  val is_jump = Bool() // is this a jump? (jal or jalr)
  val is_jal = Bool() // is this a JAL (doesn't include JR)? used for branch unit
  val is_ret = Bool() // is jalr with rd=x0, rs1=x1? (i.e., a return)
  val is_call = Bool() //
  val br_mask = UInt(width = p.maxBrCount bits) // which branches are we being speculated under?
  val br_tag = UInt(width = p.BR_TAG_SZ bits)

  val br_prediction = new BranchPrediction

  // stat tracking of committed instructions
  val stat_brjmp_mispredicted = Bool() // number of mispredicted branches/jmps
  val stat_btb_made_pred = Bool() // the BTB made a prediction (even if BPD overrided it)
  val stat_btb_mispredicted = Bool() //
  val stat_bpd_made_pred = Bool() // the BPD made the prediction
  val stat_bpd_mispredicted = Bool() // denominator: all committed branches

  val fetch_pc_lob = UInt(width = log2Up(p.fetchWidth * p.coreInstBytes) bits) // track which PC was used to fetch this
  // instruction

  val imm_packed = UInt(width = RISCVConstants.LONGEST_IMM_SZ bits) // densely pack the imm in decode...
  // then translate and sign-extend in execute
  val csr_addr = UInt(width = RISCVConstants.CSR_ADDR_SZ bits) // only used for critical path reasons in Exe
  val rob_idx = UInt(width = p.ROB_ADDR_SZ bits)
  val ldq_idx = UInt(width = p.MEM_ADDR_SZ bits)
  val stq_idx = UInt(width = p.MEM_ADDR_SZ bits)
  val brob_idx = UInt(width = p.BROB_ADDR_SZ bits)
  val pdst = UInt(width = p.PREG_SZ bits)
  val pop1 = UInt(width = p.PREG_SZ bits)
  val pop2 = UInt(width = p.PREG_SZ bits)
  val pop3 = UInt(width = p.PREG_SZ bits)

  val prs1_busy = Bool()
  val prs2_busy = Bool()
  val prs3_busy = Bool()
  val stale_pdst = UInt(width = p.PREG_SZ bits)
  val exception = Bool()
  val exc_cause = UInt(width = 32 bits) // TODO compress this down, xlen is insanity , lyc added: use 32 ???
  val bypassable = Bool() // can we bypass ALU results? (doesn't include loads, csr, etc...)
  val mem_cmd = UInt(width = 4 bits) // sync primitives/cache flushes
  val mem_typ = UInt(width = 3 bits) // memory mask type for loads/stores
  val is_fence = Bool()
  val is_fencei = Bool()
  val is_store = Bool() // anything that goes into the STQ, including fences and AMOs
  val is_amo = Bool()
  val is_load = Bool()
  val is_unique = Bool() // only allow this instruction in the pipeline, wait for STQ to
  // drain, clear fetch after it (tell ROB to un-ready until empty)
  val flush_on_commit = Bool() // some instructions need to flush the pipeline behind them

  // logical specifiers (only used in Decode->Rename), except rollback (ldst)
  val ldst = UInt(width = p.LREG_SZ bits)
  val lrs1 = UInt(width = p.LREG_SZ bits)
  val lrs2 = UInt(width = p.LREG_SZ bits)
  val lrs3 = UInt(width = p.LREG_SZ bits)
  val ldst_val = Bool() // is there a destination? invalid for stores, rd==x0, etc.
  val dst_rtype = UInt(width = 2 bits)
  val lrs1_rtype = UInt(width = 2 bits)
  val lrs2_rtype = UInt(width = 2 bits)
  val frs3_en = Bool()

  // floating point information
  val fp_val = Bool() // is a floating-point instruction (F- or D-extension)?
  // If it's non-ld/st it will write back exception bits to the fcsr.
  val fp_single = Bool() // single-precision floating point instruction (F-extension)

  // exception information
  val xcpt_if = Bool()
  val replay_if = Bool() // I$ wants us to replay our ifetch request

  // purely debug information
  val debug_wdata = UInt(width = 32 bits) // TODO compress this down, xlen is insanity , lyc added: use 32 ???
  val debug_events = new DebugStageEvents

  def fu_code_is(_fu: UInt) = fu_code === _fu
}

// NOTE: I can't promise these signals get killed/cleared on a mispredict,
// so I should listen to the corresponding valid bit
// For example, on a bypassing, we listen to rf_wen to see if bypass is valid,
// but we "could" be bypassing to a branch which kills us (a false positive combinational loop),
// so we have to keep the rf_wen enabled, and not dependent on a branch kill signal
// TODO REFACTOR this, as this should no longer be true, as bypass occurs in stage before branch resolution
class CtrlSignals extends Bundle() {
  val br_type = BranchType()
  val op1_sel = RS1Type()
  val op2_sel = RS2Type()
  val imm_sel = IMMType()
  val op_fcn = UInt(width = ALUFN().SZ_ALU_FN bits)
  val fcn_dw = Bool()
  val rf_wen = Bool()
  val csr_cmd = UInt(width = 3 bits) //rocket.CSR.SZ
  val is_load = Bool() // will invoke TLB address lookup
  val is_sta = Bool() // will invoke TLB address lookup
  val is_std = Bool()
}

class DebugStageEvents extends Bundle() {
  // Track the sequence number of each instruction fetched.
  val fetch_seq = UInt(width = 32 bits)
}
