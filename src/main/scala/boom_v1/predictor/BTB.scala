package boom_v1.predictor

// See LICENSE.Berkeley for license details.
// See LICENSE.SiFive for license details.

import boom_v1.Parameters
import boom_v1.Utils.{PopCountAtLeast, PseudoLRU}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
case class BHTParams(
                      nEntries: Int = 512,
                      counterLength: Int = 1,
                      historyLength: Int = 8,
                      historyBits: Int = 3)

case class BTBParams(
                      nEntries: Int = 28,
                      nMatchBits: Int = 14,
                      nPages: Int = 6,
                      nRAS: Int = 6,
                      bhtParams: Option[BHTParams] = Some(BHTParams()),
                      updatesOutOfOrder: Boolean = false)
//
trait HasBtbParameters {
  implicit val p: Parameters
  val btbParams = p.btb.getOrElse(BTBParams(nEntries = 0))
//  val matchBits = btbParams.nMatchBits max log2Ceil(p(CacheBlockBytes) * tileParams.icache.get.nSets)
  val matchBits = btbParams.nMatchBits
  val entries = btbParams.nEntries
  val updatesOutOfOrder = btbParams.updatesOutOfOrder
  val nPages = (btbParams.nPages + 1) / 2 * 2 // control logic assumes 2 divides pages
}
//
abstract class BtbModule(implicit val p: Parameters) extends Module with HasBtbParameters
abstract class BtbBundle(implicit val p: Parameters) extends Bundle with HasBtbParameters

class RAS(nras: Int) {
  def push(addr: UInt): Unit = {
    when (count < nras) { count := count + 1 }
    val nextPos = Mux(Bool(isPow2(nras)) || pos < (nras-1), pos+1, U(0)) // lyc: wrap if entry num of ras is not pow2
    stack(nextPos) := addr
    pos := nextPos
  }
  def peek: UInt = stack(pos)
  def pop(): Unit = when (!isEmpty) {
    count := count - 1
    pos := Mux(Bool(isPow2(nras)) || pos > 0, pos-1, U(nras-1))
  }
  def clear(): Unit = count := 0
  def isEmpty: Bool = count === 0

  private val count = RegInit(U(0, log2Up(nras+1) bits))
  private val pos = RegInit(U(0, log2Up(nras) bits))
  private val stack = Reg(Vec(UInt(),nras))
}

class BHTResp(implicit p: Parameters) extends BtbBundle()(p) {
  val history = UInt(btbParams.bhtParams.map(_.historyLength).getOrElse(1) bits)
  val value = UInt(btbParams.bhtParams.map(_.counterLength).getOrElse(1) bits)
  def taken = value(0)
  def strongly_taken = value === 1
}


// BHT contains table of 2-bit counters and a global history register.
// The BHT only predicts and updates when there is a BTB hit.
// The global history:
//    - updated speculatively in fetch (if there's a BTB hit).
//    - on a mispredict, the history register is reset (again, only if BTB hit).
// The counter table:
//    - each counter corresponds with the address of the fetch packet ("fetch pc").
//    - updated when a branch resolves (and BTB was a hit for that branch).
//      The updating branch must provide its "fetch pc".
class BHT(params: BHTParams)(implicit val p: Parameters) {
  def index(addr: UInt, history: UInt) = {
    def hashHistory(hist: UInt) = if (params.historyLength == params.historyBits) hist else {
      val k = scala.math.sqrt(3)/2
      val i = BigDecimal(k * scala.math.pow(2, params.historyLength)).toBigInt
      (U(i) * hist)(params.historyLength-1 downto params.historyLength-params.historyBits) // lyc: why use sqrt(3)/2
    }
    def hashAddr(addr: UInt) = {
      val hi = addr >> log2Up(p.fetchBytes)
      hi(log2Up(params.nEntries)-1 downto 0) ^ (hi >> log2Up(params.nEntries))(1 downto 0)
    }
    hashAddr(addr) ^ (hashHistory(history) << (log2Up(params.nEntries) - params.historyBits))
  }
  def get(addr: UInt): BHTResp = {
    val res = (new BHTResp)
    res.value := Mux(resetting, U(0), table(index(addr, history)))
    res.history := history
    res
  }
  def updateTable(addr: UInt, d: BHTResp, taken: Bool): Unit = {
    wen := True
    when (!resetting) {
      waddr := index(addr, d.history)
      wdata := (params.counterLength match {
        case 1 => taken
        case 2 => Cat(taken ^ d.value(0), d.value === 1 || d.value(1) && taken)
      })
    }
  }
  def resetHistory(d: BHTResp): Unit = {
    history := d.history
  }
  def updateHistory(addr: UInt, d: BHTResp, taken: Bool): Unit = {
    history := Cat(taken, d.history >> 1)
  }
  def advanceHistory(taken: Bool): Unit = {
    history := Cat(taken, history >> 1)
  }

  private val table = Mem(UInt(params.counterLength bits), params.nEntries)
  val history = RegInit(U(0,(params.historyLength bits)))

  private val reset_waddr = RegInit(U(0,(log2Up(params.nEntries)+1) bits))
  private val resetting = !reset_waddr.msb
  private val wen = Bool().default(resetting)
  private val waddr = UInt().default(reset_waddr)
  private val wdata = UInt().default(U(0, params.counterLength bits))
  when (resetting) { reset_waddr := reset_waddr + 1 }
  when (wen) { table(waddr) := wdata }
}

object CFIType {
  def SZ = 2
  def apply() = UInt(SZ bits)
  def branch = 0
  def jump = 1
  def call = 2
  def ret = 3
}

// BTB update occurs during branch resolution (and only on a mispredict).
//  - "pc" is what future fetch PCs will tag match against.
//  - "br_pc" is the PC of the branch instruction.
class BTBUpdate(implicit p: Parameters) extends BtbBundle()(p) {
  val prediction = new BTBResp
  val pc = UInt(p.vaddrBits bits)
  val target = UInt(p.vaddrBits bits)
  val taken = Bool()
  val isValid = Bool()
  val br_pc = UInt(p.vaddrBits bits)
  val cfiType = CFIType()
}

// BHT update occurs during branch resolution on all conditional branches.
//  - "pc" is what future fetch PCs will tag match against.
class BHTUpdate(implicit p: Parameters) extends BtbBundle()(p) {
  val prediction = new BTBResp
  val pc = UInt(p.vaddrBits bits)
  val branch = Bool()
  val taken = Bool()
  val mispredict = Bool()
}

class RASUpdate(implicit p: Parameters) extends BtbBundle()(p) {
  val cfiType = CFIType()
  val returnAddr = UInt(p.vaddrBits bits)
}

//  - "bridx" is the low-order PC bits of the predicted branch (after
//     shifting off the lowest log(inst_bytes) bits off).
//  - "mask" provides a mask of valid instructions (instructions are
//     masked off by the predicted taken branch from the BTB).
class BTBResp(implicit p: Parameters) extends BtbBundle()(p) {
  val cfiType = CFIType()
  val taken = Bool()
  val mask = Bits(p.fetchWidth bits)
  val bridx = Bits(log2Up(p.fetchWidth) bits)
  val target = UInt(p.vaddrBits bits)
  val entry = UInt(log2Up(entries + 1) bits)
  val bht = new BHTResp
}

class BTBReq(implicit p: Parameters) extends BtbBundle()(p) {
  val addr = UInt(p.vaddrBits bits)
}

// fully-associative branch target buffer
// Higher-performance processors may cause BTB updates to occur out-of-order,
// which requires an extra CAM port for updates (to ensure no duplicates get
// placed in BTB).
class BTB(implicit p: Parameters) extends BtbModule {
  val io = new Bundle {
    val req = slave(Flow(new BTBReq))
    val resp = master(Flow(new BTBResp))
    val btb_update = slave(Flow(new BTBUpdate))
    val bht_update = slave(Flow(new BHTUpdate))
    val bht_advance = slave(Flow(new BTBResp))
    val ras_update = slave(Flow(new RASUpdate))
    val ras_head = master(Flow(UInt(p.vaddrBits bits)))
    val flush = in(Bool())
  }

  val idxs = Reg(Vec(UInt((matchBits - log2Up(p.coreInstBytes)) bits), entries))
  val idxPages = Reg(Vec(UInt(log2Up(nPages) bits), entries))
  val tgts = Reg(Vec(UInt((matchBits - log2Up(p.coreInstBytes)) bits), entries))
  val tgtPages = Reg(Vec(UInt(log2Up(nPages) bits), entries))
  val pages = Reg(Vec(UInt((p.vaddrBits - matchBits) bits) , nPages))
  val pageValid = RegInit(U(0,nPages bits))
  val pagesMasked = (pageValid.asBools zip pages).map { case (v, p) => Mux(v, p, U(0)) }

  val isValid = RegInit(U(0, entries bits))
  val cfiType = Reg(Vec(CFIType(), entries))
  val brIdx = Reg(Vec(UInt(log2Up(p.fetchWidth) bits),entries))

  private def page(addr: UInt) = addr >> matchBits
  private def pageMatch(addr: UInt) = {
    val p = page(addr)
    pageValid & (pages.map(_ === p).asBits().asUInt)
  }
  private def idxMatch(addr: UInt) = {
    val idx = addr(matchBits-1 downto log2Up(p.coreInstBytes))
    idxs.map(_ === idx).asBits().asUInt & isValid
  }

  val r_btb_update = (io.btb_update).stage()
  val update_target = io.req.payload.addr

  val pageHit = pageMatch(io.req.payload.addr)
  val idxHit = idxMatch(io.req.payload.addr)

  val updatePageHit = pageMatch(r_btb_update.payload.pc)
  val (updateHit, updateHitAddr) =
    if (updatesOutOfOrder) {
      val updateHits = (pageHit << 1)(MuxOH(idxMatch(r_btb_update.payload.pc), idxPages))
      (updateHits.asBits.orR, OHToUInt(updateHits.asBits))
    } else (r_btb_update.payload.prediction.entry < entries, r_btb_update.payload.prediction.entry)

  val useUpdatePageHit = updatePageHit.orR
  val usePageHit = pageHit.orR
  val doIdxPageRepl = !useUpdatePageHit    // replacement should do when update page is not found in Index Seq
  val nextPageRepl = RegInit(U(0,log2Up(nPages) bits))
  val idxPageRepl = Cat(pageHit(nPages-2 downto 0), pageHit(nPages-1)).asUInt | Mux(usePageHit, U(0), UIntToOh(nextPageRepl).asUInt)
  val idxPageUpdateOH = Mux(useUpdatePageHit, updatePageHit, idxPageRepl)
  val idxPageUpdate = OHToUInt(idxPageUpdateOH)
  val idxPageReplEn = Mux(doIdxPageRepl, idxPageRepl, U(0))

  val samePage = page(r_btb_update.payload.pc) === page(update_target)
  val doTgtPageRepl = !samePage && !usePageHit
  val tgtPageRepl = Mux(samePage, idxPageUpdateOH, Cat(idxPageUpdateOH(nPages-2 downto 0), idxPageUpdateOH(nPages-1)).asUInt)
  val tgtPageUpdate = OHToUInt((pageHit.asBools | Mux(usePageHit, U(0), tgtPageRepl).asBools))
  val tgtPageReplEn = Mux(doTgtPageRepl, tgtPageRepl, U(0))

  when (r_btb_update.valid && (doIdxPageRepl || doTgtPageRepl)) {
    val both = doIdxPageRepl && doTgtPageRepl
    val next = nextPageRepl + Mux[UInt](both, U(2), U(1))
    nextPageRepl := Mux(next >= nPages, next(0), next)   //lyc: next(0) is a trick , e.g. 7+2 = 9=> cut to 1, then next(0) = 1
  }

  val repl = new PseudoLRU(entries)
  val waddr = Mux(updateHit, updateHitAddr, repl.way)  // lyc: if update hit, use hitaddr , otherwise need replace LRU entry
  val r_resp = (io.resp).stage()  //lyc: one cycle after resp
  when (r_resp.valid && r_resp.payload.taken || r_btb_update.valid) {
    repl.access(Mux(r_btb_update.valid, waddr, r_resp.payload.entry))
  }

  when (r_btb_update.valid) {
    val mask = UIntToOh(waddr)
    idxs(waddr) := r_btb_update.payload.pc(matchBits-1 downto  log2Up(p.coreInstBytes))
    tgts(waddr) := update_target(matchBits-1 downto  log2Up(p.coreInstBytes))
    idxPages(waddr) := idxPageUpdate +^ 1 // the +1 corresponds to the <<1 on io.resp.valid
    tgtPages(waddr) := tgtPageUpdate
    cfiType(waddr) := r_btb_update.payload.cfiType
    isValid := Mux(r_btb_update.payload.isValid, isValid | mask.asUInt, isValid & ~mask.asUInt)
    if (p.fetchWidth > 1)
      brIdx(waddr) := r_btb_update.payload.br_pc >> log2Up(p.coreInstBytes)

    require(nPages % 2 == 0)
    val idxWritesEven = !idxPageUpdate(0)

    def writeBank(i: Int, mod: Int, en: UInt, data: UInt) =
      for (i <- i until nPages by mod)
        when (en(i)) { pages(i) := data }

    writeBank(0, 2, Mux(idxWritesEven, idxPageReplEn, tgtPageReplEn),
      Mux(idxWritesEven, page(r_btb_update.payload.pc), page(update_target)))
    writeBank(1, 2, Mux(idxWritesEven, tgtPageReplEn, idxPageReplEn),
      Mux(idxWritesEven, page(update_target), page(r_btb_update.payload.pc)))
    pageValid := pageValid | tgtPageReplEn | idxPageReplEn
  }

  io.resp.valid := (pageHit << 1)(MuxOH(idxHit, idxPages))
  io.resp.payload.taken := True
  io.resp.payload.target := Cat(pagesMasked(MuxOH(idxHit, tgtPages)), MuxOH(idxHit, tgts) << log2Up(p.coreInstBytes))
  io.resp.payload.entry := OHToUInt(idxHit)
  io.resp.payload.bridx := (if (p.fetchWidth > 1) MuxOH(idxHit, brIdx) else U(0))
  io.resp.payload.mask := Cat((U(1, 1 bits) << (~Mux(io.resp.payload.taken, ~io.resp.payload.bridx, B(0))).asUInt).resize(p.fetchWidth) - U(1, 1 bits), U(1,1 bits))
  io.resp.payload.cfiType := MuxOH(idxHit, cfiType)

  // if multiple entries for same PC land in BTB, zap them
  when (PopCountAtLeast(idxHit, 2)) {
    isValid := isValid & ~idxHit
  }
  when (io.flush) {
    isValid := 0
  }

  if (btbParams.bhtParams.nonEmpty) {
    val bht = new BHT(btbParams.bhtParams.get)
    val isBranch = (idxHit.asBits & cfiType.map(_ === CFIType.branch).asBits).orR
    val res = bht.get(io.req.payload.addr)
    when (io.bht_advance.valid) {
      bht.advanceHistory(io.bht_advance.payload.bht.taken)
    }
    when (io.bht_update.valid) {
      when (io.bht_update.payload.branch) {
        bht.updateTable(io.bht_update.payload.pc, io.bht_update.payload.prediction.bht, io.bht_update.payload.taken)
        when (io.bht_update.payload.mispredict) {
          bht.updateHistory(io.bht_update.payload.pc, io.bht_update.payload.prediction.bht, io.bht_update.payload.taken)
        }
      }.elsewhen (io.bht_update.payload.mispredict) {
        bht.resetHistory(io.bht_update.payload.prediction.bht)
      }
    }
    when (!res.taken && isBranch) { io.resp.payload.taken := False }
    io.resp.payload.bht := res
  }

  if (btbParams.nRAS > 0) {
    val ras = new RAS(btbParams.nRAS)
    val doPeek = (idxHit.asBits & cfiType.map(_ === CFIType.ret).asBits).orR
    io.ras_head.valid := !ras.isEmpty
    io.ras_head.payload := ras.peek
    when (!ras.isEmpty && doPeek) {
      io.resp.payload.target := ras.peek
    }
    when (io.ras_update.valid) {
      when (io.ras_update.payload.cfiType === CFIType.call) {
        ras.push(io.ras_update.payload.returnAddr)
      }.elsewhen (io.ras_update.payload.cfiType === CFIType.ret) {
        ras.pop()
      }
    }
  }
}
