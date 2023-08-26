package flappyOoO.Rename

import EasonLib.CocoSim.CocoTestFlow
import flappyOoO.BaseIsa.RV32I
import flappyOoO.{Config, RegisterSource, RegisterType, result_info, retire_if}
import flappyOoO.Decode.{UOPs, micro_op_if}
import spinal.core._
import spinal.lib._

import scala.language.postfixOps

case class dispatch_rob_if(conf: Config) extends Bundle{
  val flist_popptr_store = Flow(UInt(log2Up(conf.PhysicalRegsNum) bits))
  val rdInfo = result_info(conf)
}

class rename(implicit conf: Config) extends Component {
  val io = new Bundle {
    val valid = in Bool()
    val ready = out Bool()
    val flush = in Bool()
//    val recovery = in Bool()
    val uop = Vec(in(micro_op_if(conf)), conf.DecoderWidth)
    val dispRob = Vec(out(dispatch_rob_if(conf)),conf.DecoderWidth)
    val retired = in(retire_if(conf))
    val uopRenamed = Vec(out(micro_op_if(conf, isPhysical = true)), conf.DecoderWidth)

  }
  def connect_uop(bundle_in: Bundle, bundle_out: Bundle) = {
    for((in,out) <- bundle_in.flatten.zip(bundle_out.flatten)){
      if(out.getBitsWidth == in.getBitsWidth){
        out := in
      }
      else{
        out := in.resized
      }
    }
  }
  noIoPrefix()
  val srat = new rat()
  val arat = new arat()
  val flist = new freelist(UInt(log2Up(conf.PhysicalRegsNum) bits),conf.DecoderWidth,conf.DecoderWidth,depth=conf.PhysicalRegsNum-1)

  //WAW
  val WawFound = Vec(Bool(), conf.DecoderWidth-1)
  val srat_rd_write_value = Vec(UInt(log2Up(conf.PhysicalRegsNum) bits), conf.DecoderWidth)
  val srat_rd_write_enable = Vec(Bool(), conf.DecoderWidth)
  val oldPhyIndex = Vec(UInt(log2Up(conf.PhysicalRegsNum) bits), conf.DecoderWidth)

  srat.io.flush := io.flush
  srat.io.recovery := io.retired.branch_error_restore
  srat.io.arat_in := arat.io.rat_out

  arat.io.flush := False
  for(i <- 0 until conf.DecoderWidth){
    srat.io.rat_if.rs1_read(i).valid := io.valid & io.uop(i).rs1.is_used & (io.uop(i).rs1.source === RegisterSource.REGISTER) & (io.uop(i).rs1.kind === RegisterType.GPR)
    srat.io.rat_if.rs1_read(i).payload := io.uop(i).rs1.index
    srat.io.rat_if.rs2_read(i).valid := io.valid & io.uop(i).rs2.is_used & (io.uop(i).rs2.source === RegisterSource.REGISTER) & (io.uop(i).rs2.kind === RegisterType.GPR)
    srat.io.rat_if.rs2_read(i).payload := io.uop(i).rs2.index
    srat.io.rat_if.rd_read(i).valid := io.valid & io.uop(i).rd.is_used & (io.uop(i).rd.kind === RegisterType.GPR)
    srat.io.rat_if.rd_read(i).payload := io.uop(i).rd.index
  }

  val is_branch = Vec(Bool(), conf.DecoderWidth)
  for(i <- 0 until conf.DecoderWidth){
    var is_jump_list = Vec(for(x <- Array(UOPs.JAL, UOPs.JALR, UOPs.BEQ, UOPs.BNE, UOPs.BLT, UOPs.BGE, UOPs.BLTU, UOPs.BGEU)) yield (io.uop(i).op === x))
    is_branch(i) := is_jump_list.reduce(_ && _)
    io.dispRob(i).flist_popptr_store.payload := 0
    io.dispRob(i).flist_popptr_store.valid := False
    when(is_branch(i)) {
      io.dispRob(i).flist_popptr_store.push(flist.io.pop_ptr_store(i))
    }
    // flist to srat
    flist.io.pop(i).ready := srat.io.rat_if.rd_read(i).valid & io.ready
    srat.io.rat_if.rd_write(i).valid := srat_rd_write_enable(i)
    srat.io.rat_if.rd_write(i).payload := io.uop(i).rd.index
    srat.io.rat_if.rd_write_value(i) := srat_rd_write_value(i)

    // ROB retired to flist
    flist.io.push(i).valid := io.retired.rdInfo(i).valid & io.retired.ready(i)
    flist.io.push(i).payload := io.retired.rdInfo(i).oldPhyIndex

    // ROB retired to arat
    arat.io.rd_write(i).valid := io.retired.rdInfo(i).valid & io.retired.ready(i)
    arat.io.rd_write(i).payload := io.retired.rdInfo(i).archIndex
    arat.io.rd_write_value(i) := io.retired.rdInfo(i).phyIndex

    io.dispRob(i).rdInfo.valid := srat.io.rat_if.rd_read(i).valid
    io.dispRob(i).rdInfo.archIndex := io.uop(i).rd.index
    io.dispRob(i).rdInfo.phyIndex := flist.io.pop(i).payload
    io.dispRob(i).rdInfo.oldPhyIndex := oldPhyIndex(i)
  }
  // ROB restore to flist
  flist.io.pop_ptr_restore.payload := io.retired.flist_restore
  flist.io.pop_ptr_restore.valid := io.retired.branch_error_restore

  // RAW check
  for(i <- 0 until conf.DecoderWidth){  //default
//    io.uopRenamed(i).op := io.uop(i).op
//    io.uopRenamed(i).imm := io.uop(i).imm
//    io.uopRenamed(i).pc := io.uop(i).pc
//    io.uopRenamed(i).rs1.is_used := srat.io.rat_if.phy_rs1(i).valid
//    io.uopRenamed(i).rs1.kind := io.uop(i).rs1.kind
//    io.uopRenamed(i).rs1.source := io.uop(i).rs1.source
//    io.uopRenamed(i).rs1.width := io.uop(i).rs1.width
//    io.uopRenamed(i).rs2.is_used := srat.io.rat_if.phy_rs2(i).valid
//    io.uopRenamed(i).rs2.kind := io.uop(i).rs2.kind
//    io.uopRenamed(i).rs2.source := io.uop(i).rs2.source
//    io.uopRenamed(i).rs2.width := io.uop(i).rs2.width
//    io.uopRenamed(i).rd.is_used := srat.io.rat_if.phy_rd(i).valid
//    io.uopRenamed(i).rd.kind := io.uop(i).rd.kind
//    io.uopRenamed(i).rd.width := io.uop(i).rd.width
//    io.uopRenamed(i).rs1.index := srat.io.rat_if.phy_rs1(i).payload
//    io.uopRenamed(i).rs2.index := srat.io.rat_if.phy_rs2(i).payload
//    io.uopRenamed(i).rd.index := flist.io.pop(i).payload
    connect_uop(bundle_in = io.uop(i), bundle_out = io.uopRenamed(i))
    io.uopRenamed(i).flatten.foreach(_.allowOverride)
    io.uopRenamed(i).rs1.is_used := srat.io.rat_if.phy_rs1(i).valid
    io.uopRenamed(i).rs2.is_used := srat.io.rat_if.phy_rs2(i).valid
    io.uopRenamed(i).rd.is_used := srat.io.rat_if.phy_rd(i).valid
    io.uopRenamed(i).rs1.index := srat.io.rat_if.phy_rs1(i).payload
    io.uopRenamed(i).rs2.index := srat.io.rat_if.phy_rs2(i).payload

    srat_rd_write_enable(i) := flist.io.pop(i).fire
    srat_rd_write_value(i) := flist.io.pop(i).payload
    oldPhyIndex(i) := srat.io.rat_if.phy_rd(i).payload
  }
  for(i <- 1 until conf.DecoderWidth){
    var rs1RawIndex_list = for(j <- 0 until i) yield (io.uopRenamed(i).rd.index , (io.uop(i).rs1.index === io.uop(j).rd.index))
    var rs2RawIndex_list = for(j <- 0 until i) yield (io.uopRenamed(i).rd.index , (io.uop(i).rs2.index === io.uop(j).rd.index))
    for(maps <- rs1RawIndex_list){
      when(maps._2) {
        io.uopRenamed(i).rs1.index := maps._1
      }
    }
    for (maps <- rs2RawIndex_list) {
      when(maps._2) {
        io.uopRenamed(i).rs2.index := maps._1
      }
    }

    // WAW
    var currWawIndex = conf.DecoderWidth - i - 1
    var rdWawIndex_list = for(j <- currWawIndex+1 until conf.DecoderWidth) yield (io.uop(currWawIndex).rd.index === io.uop(j).rd.index)
    WawFound(currWawIndex) := rdWawIndex_list.reduce(_ || _)
    when(WawFound(currWawIndex)){
      srat_rd_write_enable(currWawIndex) := False
    }

  }

  for(i <- (1 until conf.DecoderWidth).reverse){
    var oldIndex_list = for(j <- 0 until i) yield (flist.io.pop(j).payload , io.uop(i).rd.index === io.uop(j).rd.index)
    for(maps <- oldIndex_list) {
      when(maps._2){
        oldPhyIndex(i) := maps._1
      }
    }

  }

  io.ready := flist.io.pop(conf.DecoderWidth-1).valid
}

object rename_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = true,
      anonymSignalPrefix = "tmp",
      _withEnumString = false,
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate({
        implicit val config = new Config(RV32I)
        val rename = new rename()
        rename      })
  }.printPruned()
}

object rename_coco {
  def main(args: Array[String]): Unit = {
    val workspacePath = "/home/lyc/projects/riscv/FlappyRiscv"
    new CocoTestFlow(design = {
      implicit val config = new Config(RV32I)
      val rena = new rename()
      rena
    }, workspacePath = workspacePath).doit()
  }
}