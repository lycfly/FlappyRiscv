package flappyOoO.regfile_read

import flappyOoO.Config
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps

case class rfReadIo(config: Config) extends Bundle with IMasterSlave {
  val rs1Index = UInt(log2Up(config.PhysicalRegsNum) bits)
  val rs2Index = UInt(log2Up(config.PhysicalRegsNum) bits)
  val rs1Data = Bits(config.xlen bits)
  val rs2Data = Bits(config.xlen bits)

  override def asMaster(): Unit = {
    out(rs1Index, rs2Index)
    in(rs1Data, rs2Data)
  }
}

case class rfWriteIo(config: Config) extends Bundle with IMasterSlave {
  val rd = UInt(log2Up(config.PhysicalRegsNum) bits)
  val data = Bits(config.xlen bits)
  val valid = Bool()

  override def asMaster(): Unit = {
    out(rd, data, valid)
  }
}
class regfile(conf: Config) extends Component {

  private val registerNames = Seq(
    "zero",
    "ra",
    "sp",
    "gp",
    "tp",
    "t0",
    "t1",
    "t2",
    "s0_fp",
    "s1",
    "a0",
    "a1",
    "a2",
    "a3",
    "a4",
    "a5",
    "a6",
    "a7",
    "s2",
    "s3",
    "s4",
    "s5",
    "s6",
    "s7",
    "s8",
    "s9",
    "s10",
    "s11",
    "t3",
    "t4",
    "t5",
    "t6"
  )
  val io = new Bundle {
    val read_io = Vec(slave(rfReadIo(conf)), conf.IssueWidth)
    val write_io = Vec(slave(rfWriteIo(conf)), conf.IssueWidth)
  }
  noIoPrefix()
  val regs = Mem(UInt(conf.xlen bits), conf.PhysicalRegsNum)


    // Add a wire for each register with a readable name. This is to easily
    // view register values in a wave dump.
    for (i <- 0 until conf.PhysicalRegsNum) {
      val regWire = UInt(conf.xlen bits)
//      var regName = registerNames(i)
//      regWire.setName(s"x${i}_${regName}")
      regWire.setName(s"p${i}")

      regWire := regs.readAsync(U(i).resized, writeFirst)
    }

    // We explicitly check if x0 is read and return 0 in that case. Another
    // option would be to initialize regs(0) to zero at boot. However, this
    // slows-down riscv-formal runs significantly for some reason...
    def readReg(addr: UInt) = {
      addr.mux(
        0 -> U(0, conf.xlen bits),
        default -> regs.readAsync(addr, writeFirst)
      )
    }

   for(i <- 0 until conf.IssueWidth) {
     io.read_io(i).rs1Data := readReg(io.read_io(i).rs1Index).asBits
     io.read_io(i).rs2Data := readReg(io.read_io(i).rs2Index).asBits

     when(io.write_io(i).valid && io.write_io(i).rd =/= 0) {
       regs(io.write_io(i).rd) := io.write_io(i).data.asUInt
     }
   }
  }



object regfile_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new regfile(Config()))
  }.printPruned()
}