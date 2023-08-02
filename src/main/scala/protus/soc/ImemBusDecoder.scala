package protus.soc

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.lib.bus.simple._
import spinal.lib.bus.misc._
import scala.language.postfixOps


  class ImemBusDecoder(AddrWidth: Int = 32, DataWidth: Int = 32, DbusOutNum: Int = 3) extends Component {
    val io = new Bundle {
      val imem_bus_in = slave(PipelinedMemoryBus(AddrWidth,DataWidth))
      val imem_bus_out = Vec(master(PipelinedMemoryBus(AddrWidth,DataWidth)),DbusOutNum)
    }
    noIoPrefix()
    val decode_map = Map(
      io.imem_bus_out(0) -> DefaultMapping,
      io.imem_bus_out(1) -> MaskMapping(0x00480000L, 0xFFFF0000L) // DTCM, 0x00480000 ~ 0x0048FFFF, 64KB
    )
    val decoder = PipelinedMemoryBusDecoder(PipelinedMemoryBusConfig(AddrWidth,DataWidth), mappings = decode_map.values.toSeq, pendingMax = 3)
    decoder.io.input <> io.imem_bus_in

    io.imem_bus_out <> decoder.io.outputs
  }


object ImemBusDecoder_inst {
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
      .generate(new ImemBusDecoder())
  }.printPruned()
}