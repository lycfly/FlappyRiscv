package flappyOoO

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

class Config(val baseIsa: BaseIsa = RV32I, val debug: Boolean = true) {
  def xlen = baseIsa.xlen
  def numRegs = baseIsa.xlen
  def IMemWordWidth: Int = 64
  def FetchWidth: Int = 64
  def DecoderWidth: Int = 2
  def IssueWidth: Int = 2
  def Rvc: Boolean = true
  def Rvf: Boolean = false
  def Rvd: Boolean = false
  object FetchConfig{
    def FetchAddrBuffDepth = 6
//    def FetchInstrBuffDepth = 12
    def FetchInstrBuffWaterline = 6
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
