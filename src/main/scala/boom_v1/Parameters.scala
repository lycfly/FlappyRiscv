package boom_v1

import boom_v1.predictor.BTBParams
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class Parameters(
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
                       //                           tage: Option[TageParameters] = None,
                       //                           gshare: Option[GShareParameters] = None,
                       //                           gskew: Option[GSkewParameters] = None
                     ) {
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
}



