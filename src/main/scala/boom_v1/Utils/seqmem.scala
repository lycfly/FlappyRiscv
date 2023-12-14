package boom_v1.Utils

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

// Provide the "logical" sizes and we will map
// the logical SeqMem into a physical, realizable SeqMem
// that is in a square aspect ratio (rounded to a pow2 for the depth).
// Only supports a single read/write port.
class SeqMem1rwTransformable (
                               l_depth: Int,
                               l_width: Int
                             ) extends Module
{
  val p_depth = 1 << log2Up(scala.math.sqrt(l_depth*l_width).toInt)
  val p_width = l_depth*l_width/p_depth

  require (l_depth*l_width == p_depth*p_width)
  require (p_depth > 0)
  require (p_width > 0)
  require (p_width % l_width == 0)

  val l_idx_sz = log2Up(l_depth)
  val p_idx_sz = log2Up(p_depth)
  val l_off_sz = log2Up(l_width)
  val p_off_sz = log2Up(p_width/l_width)
  require (p_off_sz > 0)

  println("\tSeqMem transformed from ("+ l_depth +" x "+l_width+") to ("+ p_depth +" x "+p_width+")")

  val io = new Bundle
  {
    val wen   = in Bool()
    val waddr = in UInt( width = l_idx_sz bits)
    val wmask = in UInt( width = l_width bits)
    val wdata = in UInt( width = l_width bits)

    val ren   = in Bool()                   // valid cycle s0
    val raddr = in UInt(width = l_idx_sz bits) // input cycle s0
    val rout  = out UInt(width = l_width bits) // returned cycle s1
  }

  val smem = Mem( Vec(Bool(), p_width),p_depth)


  private def getIdx(addr:UInt) =
    addr >> p_off_sz

  // must compute offset from address but then factor in the l_width.
  private def getOffset(addr:UInt) =
    addr(p_off_sz-1 downto 0) << l_off_sz


  assert (!(io.wen && io.ren), "[SMUtil] writer and reader fighting over the single port.")
  when (io.wen && !io.ren)
  {
    val waddr = getIdx(io.waddr)
    val wdata = (io.wdata << getOffset(io.waddr))(p_width-1 downto 0)
    val wmask = (io.wmask << getOffset(io.waddr))(p_width-1 downto 0)
    smem.write(waddr, Vec(wdata.asBools),wmask.orR, wmask.asBits)
  }

  // read
  val ridx = getIdx(io.raddr)
  val roff = getOffset(io.raddr)
  val r_offset = RegNextWhen(roff, io.ren)
  // returned cycle s1
  val s1_rrow = smem.readSync(ridx, io.ren).asBits.asUInt
  io.rout := (s1_rrow >> r_offset)(l_width-1 downto 0)
}

