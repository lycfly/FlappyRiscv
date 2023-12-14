package boom_v1

import boom_v1.predictor.gshare.{GShareBrPredictor, GShareParameters}
import boom_v1.predictor.tage.{TageBrPredictor, TageParameters}
import boom_v1.predictor.{BTBParams, GSkewBrPredictor, GSkewParameters}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class Parameters(
                       DEBUG_PRINTF: Boolean = false,

                       instBits: Int = 32,
                       coreMaxAddrBits: Int = 32,
                       fetchWidth: Int = 2,
                       decodeWidth: Int = 1,
                       issueWidth: Int = 1,
                       retireWidth: Int = 2,

                       numRobEntries: Int = 16,
                       numIssueSlotEntries: Int = 12,
                       numLsuEntries: Int = 8,
                       numPhysRegisters: Int = 110,
                       maxBrCount: Int = 4,
                       fetchBufferSz: Int = 4,
                       vaddrBits: Int = 32,

                       btb: Option[BTBParams] = Some(BTBParams()),

                       usingFPU: Boolean = false,
                       enableAgePriorityIssue: Boolean = true,
                       enablePrefetching: Boolean = false,
                       enableFetchBufferFlowThrough: Boolean = false,
                       enableBrResolutionRegister: Boolean = true,
                       enableCommitMapTable: Boolean = false,
                       enableBTBContainsBranches: Boolean = true,
                       enableBranchPredictor: Boolean = true,
                       enableBpdUModeOnly: Boolean = false,
                       enableBpdUSModeHistory: Boolean = false,
                       tage: Option[TageParameters] = None,
                       gshare: Option[GShareParameters] = None,
                       gskew: Option[GSkewParameters] = None
                     ) {
  val xLen = 32
  val coreInstBytes = instBits / 8
  val MAX_WAKEUP_DELAY = 3 // unused
  val FETCH_WIDTH = fetchWidth // number of insts we can fetch
  val DECODE_WIDTH = decodeWidth
  val DISPATCH_WIDTH = DECODE_WIDTH // number of insts put into the IssueWindow
  val ISSUE_WIDTH = issueWidth
  val COMMIT_WIDTH = retireWidth

  require(DECODE_WIDTH == COMMIT_WIDTH)
  require(DISPATCH_WIDTH == COMMIT_WIDTH)
  require(isPow2(FETCH_WIDTH))
  require(DECODE_WIDTH <= FETCH_WIDTH)

  // Data Structure Sizes
  val NUM_ROB_ENTRIES = numRobEntries // number of ROB entries (e.g., 32 entries for R10k)
  val NUM_LSU_ENTRIES = numLsuEntries // number of LD/ST entries
  val MAX_BR_COUNT = maxBrCount // number of branches we can speculate simultaneously
  val PHYS_REG_COUNT = numPhysRegisters // size of the unified, physical register file
  val FETCH_BUFFER_SZ = fetchBufferSz // number of instructions that stored between fetch&decode

  //************************************
  // Implicitly calculated constants
  val NUM_ROB_ROWS = NUM_ROB_ENTRIES / DECODE_WIDTH
  val ROB_ADDR_SZ = log2Up(NUM_ROB_ENTRIES)
  // the f-registers are mapped into the space above the x-registers
  val LOGICAL_REG_COUNT = if (usingFPU) 64 else 32
  val LREG_SZ = log2Up(LOGICAL_REG_COUNT)
  val PREG_SZ = log2Up(PHYS_REG_COUNT)
  val MEM_ADDR_SZ = log2Up(NUM_LSU_ENTRIES)
  val MAX_ST_COUNT = (1 << MEM_ADDR_SZ)
  val MAX_LD_COUNT = (1 << MEM_ADDR_SZ)
  val BR_TAG_SZ = log2Up(MAX_BR_COUNT)
  val NUM_BROB_ENTRIES = NUM_ROB_ROWS //TODO explore smaller BROBs
  val BROB_ADDR_SZ = log2Up(NUM_BROB_ENTRIES)

  require(PHYS_REG_COUNT >= (LOGICAL_REG_COUNT + DECODE_WIDTH))
  require(MAX_BR_COUNT >= 2)
  require(NUM_ROB_ROWS % 2 == 0)
  require(NUM_ROB_ENTRIES % DECODE_WIDTH == 0)
  require(isPow2(NUM_LSU_ENTRIES))
  require((NUM_LSU_ENTRIES - 1) > DECODE_WIDTH)


  /** Core parameters */
  def instBytes: Int = instBits / 8

  def fetchBytes: Int = fetchWidth * instBytes

  //************************************
  // Branch Prediction

  val enableBTB = true
  val btbParams: BTBParams = BTBParams()

  val ENABLE_BRANCH_PREDICTOR = enableBranchPredictor
  val ENABLE_BPD_UMODE_ONLY = enableBpdUModeOnly
  val ENABLE_BPD_USHISTORY = enableBpdUSModeHistory
  // What is the maximum length of global history tracked?
  var GLOBAL_HISTORY_LENGTH = 0
  // What is the physical length of the VeryLongHistoryRegister? This must be
  // able to handle the GHIST_LENGTH as well as being able hold all speculative
  // updates well beyond the GHIST_LENGTH (i.e., +ROB_SZ and other buffering).
  var VLHR_LENGTH = 0
  var BPD_INFO_SIZE = 0
  var ENABLE_VLHR = false

  val tageParams = tage
  val gshareParams = gshare
  val gskewParams = gskew

  if (!ENABLE_BRANCH_PREDICTOR) {
    BPD_INFO_SIZE = 1
    GLOBAL_HISTORY_LENGTH = 1
  }
  else if (tageParams.isDefined && tageParams.get.enabled) {
    GLOBAL_HISTORY_LENGTH = tageParams.get.history_lengths.max
    BPD_INFO_SIZE = TageBrPredictor.GetRespInfoSize(this, fetchWidth)
    ENABLE_VLHR = true
  }
  else if (gskewParams.isDefined && gskewParams.get.enabled) {
    GLOBAL_HISTORY_LENGTH = gskewParams.get.history_length
    BPD_INFO_SIZE = GSkewBrPredictor.GetRespInfoSize(this, fetchWidth)
  }
  else if (gshareParams.isDefined && gshareParams.get.enabled) {
    GLOBAL_HISTORY_LENGTH = gshareParams.get.history_length
    BPD_INFO_SIZE = GShareBrPredictor.GetRespInfoSize(this, GLOBAL_HISTORY_LENGTH)
  }
//  else if (.enabled) {
//    GLOBAL_HISTORY_LENGTH = p(SimpleGShareKey).history_length
//    BPD_INFO_SIZE = SimpleGShareBrPredictor.GetRespInfoSize(p)
//  }
//  else if (p(RandomBpdKey).enabled) {
//    GLOBAL_HISTORY_LENGTH = 1
//    BPD_INFO_SIZE = RandomBrPredictor.GetRespInfoSize(p)
//  }
  else {
    require(!ENABLE_BRANCH_PREDICTOR) // set branch predictor in configs.scala
    BPD_INFO_SIZE = 1
    GLOBAL_HISTORY_LENGTH = 1
  }
  VLHR_LENGTH = GLOBAL_HISTORY_LENGTH + 2 * NUM_ROB_ENTRIES

}



