package flappyOoO

import EasonLib.Arithmetic.multiplier.{Booth4SignedMultiplier, CombSignedMultiplier, SignMultiplier}
import flappyOoO.BaseIsa.RV32I

sealed trait BaseIsa {
  val xlen: Int
  val numRegs: Int
}

object BaseIsa {
  case object RV32I extends BaseIsa {
    override val xlen = 32
    override val numRegs = 32
  }
  case object RV32E extends BaseIsa {
    override val xlen = 32
    override val numRegs = 16
  }
  case object RV64I extends BaseIsa {
    override val xlen = 64
    override val numRegs = 32
  }
}

case class Config(val baseIsa: BaseIsa = RV32I, val debug: Boolean = true) {
  def xlen = baseIsa.xlen
  def ArchRegsNum = baseIsa.xlen
  def PhysicalRegsNum: Int = 64
  def IMemWordWidth: Int = 64
  def FetchWidth: Int = 64
  def DecoderWidth: Int = 2
  def IssueWidth: Int = 2
  def IqEntryNum: Int = 8
  def RobEntryNum: Int = 8
  def ClusterNum: Int = IssueWidth
  def MaxInstrCycles = 32

  def MultiplierType = CombSignedMultiplier
  def DividerType = "radix4"

  def clusters = Array(
    Array(FU_TYPE.ALU, FU_TYPE.BRU, FU_TYPE.MUL, FU_TYPE.CSR, FU_TYPE.LSU, FU_TYPE.BIT), // cluster 0
    Array(FU_TYPE.ALU), // cluster 1
  )

  def RvZicsr: Boolean = true
  def Rvm: Boolean = true
  def Rvc: Boolean = true
  def Rvf: Boolean = false
  def Rvd: Boolean = false


  object FetchConfig{
    def FetchAddrBuffDepth = 6
//    def FetchInstrBuffDepth = 12
    def FetchInstrBuffWaterline = 6
  }
  object Loader {
    def cycles_fixed: Int = 2
    def cycles_unknown: Int = 0
  }
  object Multiplier {

    val cands = Map(CombSignedMultiplier -> Map("delay" -> 1),
                    Booth4SignedMultiplier -> Map("delay" -> (xlen/2 + 1)),

    )
  }
  object Divider{
    val cands = Map("comb" -> Map("delay" -> 1),
      "radix2" -> Map("delay" -> (xlen + 1)),
      "radix4" -> Map("delay" -> (xlen / 2 + 1)),
      "radix8" -> Map("delay" -> (xlen / 3 + 1))
    )
  }


  def ibusConfig = MemBusConfig(
    addressWidth = baseIsa.xlen,
    dataWidth = FetchWidth,
    readWrite = false
  )
  def readDbusConfig = MemBusConfig(
    addressWidth = baseIsa.xlen,
    dataWidth = baseIsa.xlen,
    readWrite = false
  )
  def dbusConfig = MemBusConfig(
    addressWidth = baseIsa.xlen,
    dataWidth = baseIsa.xlen
  )
}
