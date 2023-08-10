package flappyOoO.Rename

import flappyOoO.BaseIsa.RV32I
import flappyOoO.{Config, RegisterSource, RegisterType}
import flappyOoO.Decode.micro_op_if
import spinal.core._

import scala.language.postfixOps

class rename(implicit conf: Config) extends Component {
  val io = new Bundle {
    val valid = in Bool()
    val flush = in Bool()
    val recovery = in Bool()
    val uop = Vec(in(micro_op_if(conf)), conf.DecoderWidth)


  }
  noIoPrefix()
  val srat = new rat()
  val arat = new arat()
  srat.io.flush := io.flush
  srat.io.recovery := io.recovery
  srat.io.arat_in := arat.io.rat_out

  arat.io.flush := io.recovery
  for(i <- 0 until conf.DecoderWidth){
    srat.io.rat_if.rs1_read(i).valid := io.valid & io.uop(i).rs1.is_used & (io.uop(i).rs1.source === RegisterSource.REGISTER) & (io.uop(i).rs1.kind === RegisterType.GPR)
    srat.io.rat_if.rs1_read(i).payload := io.uop(i).rs1.index
    srat.io.rat_if.rs2_read(i).valid := io.valid & io.uop(i).rs2.is_used & (io.uop(i).rs2.source === RegisterSource.REGISTER) & (io.uop(i).rs2.kind === RegisterType.GPR)
    srat.io.rat_if.rs2_read(i).payload := io.uop(i).rs2.index
    srat.io.rat_if.rd_read(i).valid := io.valid & io.uop(i).rd.is_used & (io.uop(i).rd.kind === RegisterType.GPR)
    srat.io.rat_if.rd_read(i).payload := io.uop(i).rd.index
  }





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
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate({
        implicit val config = new Config(RV32I)
        val rename = new rename()
        rename      })
  }.printPruned()
}