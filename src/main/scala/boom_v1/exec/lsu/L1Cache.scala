// See LICENSE.SiFive for license details.

package boom_v1.exec.lsu

import boom_v1.Parameters
import spinal.core._
import spinal.lib._
import boom_v1.Utils._
import boom_v1.Utils.chiselExtract._
import boom_v1.Utils.chiselDotDef._
trait L1CacheParams {
  def nSets:         Int
  def nWays:         Int
  def rowBits:       Int
  def nTLBEntries:   Int
  def splitMetadata: Boolean
  def ecc:           Option[Code]
}

trait HasL1CacheParameters {
  implicit val p: Parameters
  val cacheParams: L1CacheParams

  def cacheBlockBytes = p.CacheBlockBytes
  def lgCacheBlockBytes = log2Up(cacheBlockBytes)
  def nSets = cacheParams.nSets
  def blockOffBits = lgCacheBlockBytes
  def idxBits = log2Up(cacheParams.nSets)
  def untagBits = blockOffBits + idxBits
  def tagBits = p.PAddrBits - untagBits
  def nWays = cacheParams.nWays
  def wayBits = log2Up(nWays)
  def isDM = nWays == 1
  def rowBits = cacheParams.rowBits
  def rowBytes = rowBits/8
  def rowOffBits = log2Up(rowBytes)
  def code = cacheParams.ecc.getOrElse(new IdentityCode)
  def nTLBEntries = cacheParams.nTLBEntries
  def hasSplitMetadata = cacheParams.splitMetadata

  def cacheDataBits = p(SharedMemoryTLEdge).bundle.dataBits
  def cacheDataBeats = (cacheBlockBytes * 8) / cacheDataBits
  def refillCycles = cacheDataBeats
}

abstract class L1CacheModule(implicit val p: Parameters) extends Module
  with HasL1CacheParameters

abstract class L1CacheBundle(implicit val p: Parameters) extends Bundle()(p)
  with HasL1CacheParameters
