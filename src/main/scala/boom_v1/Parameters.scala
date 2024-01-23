package boom_v1

import boom_v1.exec.FPU.FPUParams
import boom_v1.predictor.gshare.{GShareBrPredictor, GShareParameters, SimpleGShareBrPredictor, SimpleGShareParameters}
import boom_v1.predictor.gskew.{GSkewBrPredictor, GSkewParameters}
import boom_v1.predictor.tage.{TageBrPredictor, TageParameters}
import boom_v1.predictor.BTBParams
import boom_v1.predictor.boom.RandomBrPredictor
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
import scala.sys.runtime

case class Parameters(
                       DEBUG_PRINTF: Boolean = false,

                       instBits: Int = 32,
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

                       btb: Option[BTBParams] = Some(BTBParams()),

                       usingFPU: Boolean = false,
                       usingFDivSqrt: Boolean = false,
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
                       gskew: Option[GSkewParameters] = None,
                       simple_gshare: Boolean = false,
                       random_bpd: Boolean = false,
                     ) {
  val xLen = 32
  val fLen = xLen // TODO relax this
  val CacheBlockBytes = 64
  val PRV_SZ = 2
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
  // Extra Knobs and Features
  val ENABLE_REGFILE_BYPASSING = true // bypass regfile write ports to read ports
  val MAX_WAKEUP_DELAY = 3 // unused
  val ENABLE_COMMIT_MAP_TABLE = enableCommitMapTable

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

/** Tile parameters */
  val usingVM = true
  val usingUser = false || usingVM
  val usingDebug = false
  val usingRoCC = false
  val usingBTB = btb.isDefined && btb.get.nEntries > 0
  val usingPTW = usingVM
//  val usingDataScratchpad = dcache.isDefined && tileParams.dataScratchpadBytes > 0
//  def dcacheArbPorts = 1 + usingVM.toInt + usingDataScratchpad.toInt + tileParams.rocc.size

  /** Core parameters */
//  val usingMulDiv = coreParams.mulDiv.nonEmpty
//  val usingFPU = coreParams.fpu.nonEmpty
//  val usingAtomics = coreParams.useAtomics
//  val usingCompressed = coreParams.useCompressed

  val coreInstBits = instBits
  val coreInstBytes = coreInstBits / 8
  val coreDataBits = xLen
  val coreDataBytes = coreDataBits / 8

  val coreDCacheReqTagBits = 6
//  val dcacheReqTagBits = coreDCacheReqTagBits + log2Up(dcacheArbPorts)
  def instBytes: Int = instBits / 8

  def fetchBytes: Int = fetchWidth * instBytes

  def pgIdxBits = 12

  def PAddrBits = 32

  def PgLevels = if (xLen == 64) 3 /* Sv39 */
                      else 2 /* Sv32 */
  def ASIdBits = 7
  def pgLevelBits = 10 - log2Up(xLen / 32)

  def vaddrBits = pgIdxBits + PgLevels * pgLevelBits

  val paddrBits = 32 //p(PAddrBits)

  def ppnBits = paddrBits - pgIdxBits

  def vpnBits = vaddrBits - pgIdxBits

//  val pgLevels = p(PgLevels)
//  val asIdBits = p(ASIdBits)
  val vpnBitsExtended = vpnBits + (vaddrBits < xLen).toInt
  val vaddrBitsExtended = vpnBitsExtended + pgIdxBits
  val coreMaxAddrBits = paddrBits max vaddrBitsExtended
  val maxPAddrBits = xLen match {
    case 32 => 34;
    case 64 => 50
  }
  require(paddrBits <= maxPAddrBits)
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
  else if (simple_gshare) {
    GLOBAL_HISTORY_LENGTH = SimpleGShareParameters().history_length
    BPD_INFO_SIZE = SimpleGShareBrPredictor.GetRespInfoSize(this)
  }
  else if (random_bpd) {
    GLOBAL_HISTORY_LENGTH = 1
    BPD_INFO_SIZE = RandomBrPredictor.GetRespInfoSize(this)
  }
  else {
    require(!ENABLE_BRANCH_PREDICTOR) // set branch predictor in configs.scala
    BPD_INFO_SIZE = 1
    GLOBAL_HISTORY_LENGTH = 1
  }
  VLHR_LENGTH = GLOBAL_HISTORY_LENGTH + 2 * NUM_ROB_ENTRIES

  //************************************
  // Functional Units
  val usingFDivSqrt = FPUParams().divSqrt

  val mulDivParams = rocketParams.mulDiv.getOrElse(MulDivParams())
  //************************************
  // Load/Store Unit
//  val dcacheParams: DCacheParams = tileParams.dcache.get
  val dcacheParams = boom_v1.exec.lsu.DCacheParams()
  val nTLBEntries = dcacheParams.nTLBEntries

  //************************************
  // Pipelining

  val IMUL_STAGES = dfmaLatency
  val dfmaLatency = FPUParams().dfmaLatency
  val sfmaLatency = FPUParams().sfmaLatency
  // All FPU ops padded out to same delay for writeport scheduling.
  require(sfmaLatency == dfmaLatency)

  val coreDCacheReqTagBits = 6
  val dcacheReqTagBits = coreDCacheReqTagBits + log2Up(dcacheArbPorts)
  //************************************
  // Non-BOOM parameters

  val corePAddrBits = paddrBits
  val corePgIdxBits = pgIdxBits
}



