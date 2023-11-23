package flappyOoO.issue

import EasonLib.Common_ip.{CompareBalancedTree, CompareOpFunction, OlderCompFunc}
import EasonLib.Utils.PortToRegWithInit
import flappyOoO.BaseIsa.RV32I
import flappyOoO.{Config, FU_TYPE, RegisterSource, RegisterType}
import flappyOoO.Decode.{UOPs, micro_op_if}
import flappyOoO.dispatch.dispatch_iq_if
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.language.postfixOps
case class rs_iq_entry(conf: Config) extends Bundle {
  val source = RegisterSource()
  val index = UInt(log2Up(conf.PhysicalRegsNum) bits)
  val matched = Bool() // find rs1 = broadcast rd
  val mcycle_cnt = UInt(log2Up(conf.MaxInstrCycles) bits)
  val ready = Bool()
}
case class rd_iq_entry(conf: Config) extends Bundle {
  val index = UInt(log2Up(conf.PhysicalRegsNum) bits)
  val used = Bool()
}
case class iq_entry(conf: Config) extends Bundle{
  val freedBit = Bool()
  val issuedBit = Bool()
  val robAge = UInt(log2Up(conf.RobEntryNum) bits)
  val fu = FU_TYPE()
  val cycles = UInt(log2Up(conf.MaxInstrCycles) bits)
  val imm = Bits(conf.xlen bits)
  val op = UOPs()
  val pc = UInt(32 bits)
  val rs1 = rs_iq_entry(conf)
  val rs2 = rs_iq_entry(conf)
  val rd = rd_iq_entry(conf)

}
class issueQueue(implicit conf: Config) extends Component {
  val io = new Bundle {
    val dispIq = Vec(slave(Stream(dispatch_iq_if(conf))), conf.DecoderWidth)
    val iq_issued = Vec(master(Stream(iq_entry(conf))), conf.IssueWidth)
  }
  noIoPrefix()
  val iq = Vec(PortToRegWithInit(iq_entry(conf)), conf.IqEntryNum)
  // dispatch to IQ
  val IqWriteIndex = Vec(UInt(log2Up(conf.IqEntryNum) bits), conf.DecoderWidth)
  val flist = new IqFreelist(UInt(log2Up(conf.IqEntryNum) bits), pushNum = conf.IssueWidth, popNum = conf.DecoderWidth, depth = conf.IqEntryNum)
  for(i <- 0 until conf.DecoderWidth) {
    io.dispIq(i).ready := flist.io.pop(i).valid
    flist.io.pop(i).ready := io.dispIq(i).fire
    IqWriteIndex(i) := flist.io.pop(i).payload

    flist.io.push(i).payload := 0 //TODO
    flist.io.push(i).valid := False //TODO

    // issue queue entry in
    when(io.dispIq(i).fire){
      iq(IqWriteIndex(i)).robAge := io.dispIq(i).robAge
      iq(IqWriteIndex(i)).freedBit := False
      iq(IqWriteIndex(i)).issuedBit := False
      iq(IqWriteIndex(i)).cycles := io.dispIq(i).uopRn.cycles
      iq(IqWriteIndex(i)).fu := io.dispIq(i).uopRn.fu
      iq(IqWriteIndex(i)).imm := io.dispIq(i).uopRn.imm
      iq(IqWriteIndex(i)).op := io.dispIq(i).uopRn.op
      iq(IqWriteIndex(i)).pc := io.dispIq(i).uopRn.pc
      //rs1 info
      iq(IqWriteIndex(i)).rs1.source := io.dispIq(i).uopRn.rs1.source
      iq(IqWriteIndex(i)).rs1.index := io.dispIq(i).uopRn.rs1.index
      iq(IqWriteIndex(i)).rs1.matched := False
      iq(IqWriteIndex(i)).rs1.mcycle_cnt := io.dispIq(i).uopRn.cycles
      iq(IqWriteIndex(i)).rs1.ready := ~io.dispIq(i).uopRn.rs1.is_used || io.dispIq(i).uopRn.rs1.source =/= RegisterSource.REGISTER
      //rs2 info
      iq(IqWriteIndex(i)).rs2.source := io.dispIq(i).uopRn.rs2.source
      iq(IqWriteIndex(i)).rs2.index := io.dispIq(i).uopRn.rs2.index
      iq(IqWriteIndex(i)).rs2.matched := False
      iq(IqWriteIndex(i)).rs2.mcycle_cnt := io.dispIq(i).uopRn.cycles
      iq(IqWriteIndex(i)).rs2.ready := ~io.dispIq(i).uopRn.rs2.is_used || io.dispIq(i).uopRn.rs2.source =/= RegisterSource.REGISTER
      //rd info
      iq(IqWriteIndex(i)).rd.used := io.dispIq(i).uopRn.rd.is_used
      iq(IqWriteIndex(i)).rd.index := io.dispIq(i).uopRn.rd.index

    }

  }

  /** Select arbitration */
  val candiEnableForCluster = Vec(Vec(Bool(), conf.IqEntryNum), conf.IssueWidth)
  val candiIsInCluster = Vec(Vec(Bool(), conf.IqEntryNum), conf.IssueWidth)
  val entry_ready = Vec(Bool(), conf.IqEntryNum)
  for(i <- 0 until conf.IqEntryNum){
    entry_ready(i) := iq(i).rs1.ready & iq(i).rs2.ready & ~iq(i).freedBit & ~iq(i).issuedBit
    for (j <- 0 until conf.IssueWidth) {
      var fuIsIncluster = for(x <- conf.clusters(j)) yield (iq(i).fu === x)
      candiIsInCluster(j)(i) := fuIsIncluster.reduce(_ | _)
      candiEnableForCluster(j)(i) := candiIsInCluster(j)(i) & entry_ready(i)
    }
  }

  val compTreeAgeList = Vec(for(i <- 0 until conf.IqEntryNum) yield iq(i).robAge)
  val compTreeValidList = Vec(Vec(Bool(), conf.IqEntryNum), conf.IssueWidth)
  val compTreeOutIndex = Vec(UInt(log2Up(conf.IqEntryNum) bits), conf.IssueWidth)
  for(i <- 0 until conf.IssueWidth){
    if(i == 0){
      compTreeValidList(i) := candiEnableForCluster(i)
    }
    else{
      val lastIndexOneHot = Bits(conf.IqEntryNum bits)
      lastIndexOneHot.setAll()
      lastIndexOneHot(compTreeOutIndex(i-1)) := False
      compTreeValidList(i) := Vec(for(j <- 0 until conf.IqEntryNum) yield compTreeValidList(i-1)(j) & (lastIndexOneHot(j)))
    }
    var cmpTree= CompareBalancedTree("cmpTree"+i.toString, OlderCompFunc, true, compTreeAgeList, compTreeValidList(i))
    compTreeOutIndex(i) := cmpTree(1).asInstanceOf[UInt]

    val allInvalid = cmpTree(2).asInstanceOf[Bool]
    val selectedIq = iq(compTreeOutIndex(i))
    /** Wake up */

    when(~allInvalid){
      io.iq_issued(i).payload := selectedIq
      io.iq_issued(i).valid :=  True
      selectedIq.issuedBit := True
      for(k <- 0 until conf.IqEntryNum){
        when(iq(k).rs1.index === compTreeOutIndex(i) && iq(k).rs1.source === RegisterSource.REGISTER){
          iq(k).rs1.matched := True
          when(selectedIq.cycles === 1){
            iq(k).rs1.ready := True
            iq(k).rs1.mcycle_cnt := 0
          }.otherwise{
            iq(k).rs1.mcycle_cnt := selectedIq.cycles - 1
          }
        }
        when(iq(k).rs2.index === compTreeOutIndex(i) && iq(k).rs2.source === RegisterSource.REGISTER) {
          iq(k).rs2.matched := True
          when(selectedIq.cycles === 1) {
            iq(k).rs2.ready := True
            iq(k).rs1.mcycle_cnt := 0
          }.otherwise {
            iq(k).rs2.mcycle_cnt := selectedIq.cycles - 1
          }
        }
      }

    }
    // delay cnt decr
    for(k <- 0 until conf.IqEntryNum){
      when(iq(k).rs1.mcycle_cnt =/= 0) {
        iq(k).rs1.mcycle_cnt := iq(k).rs1.mcycle_cnt - 1
      }.otherwise{
        iq(k).rs1.ready := True
      }
      when(iq(k).rs2.mcycle_cnt =/= 0) {
        iq(k).rs2.mcycle_cnt := iq(k).rs2.mcycle_cnt - 1
      }.otherwise {
        iq(k).rs2.ready := True
      }
    }



  }





}


object issueQueue_hdlgen {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(
        resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW
      ),
      mode = Verilog,
      oneFilePerComponent = true,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = true,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate({
        implicit val config = new Config(RV32I)
        val issueQueue = new issueQueue()
        issueQueue
      })
  }.printPruned()
}