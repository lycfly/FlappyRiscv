package protus.soc

import spinal.core._
import spinal.lib._
import spinal.lib.bus.simple._
import spinal.lib.bus.misc._

import scala.language.postfixOps


class DmemBusDecoder(AddrWidth: Int = 32, DataWidth: Int = 32, DbusOutNum: Int = 3) extends Component {
    val io = new Bundle {
        val dmem_bus_in = slave(PipelinedMemoryBus(AddrWidth,DataWidth))
        val dmem_bus_out = Vec(master(PipelinedMemoryBus(AddrWidth,DataWidth)),DbusOutNum)
    }
    noIoPrefix()
    val decode_map = Map(
        io.dmem_bus_out(0) -> DefaultMapping,
        io.dmem_bus_out(1) -> MaskMapping(0x00480000L, 0xFFFF0000L), // DTCM, 0x00480000 ~ 0x0048FFFF, 64KB
        io.dmem_bus_out(2) -> MaskMapping(0x00490000L, 0xFFFFFFE0L) // TIMER, 0x004900E0 ~ 0x004900FF, 32B
    )
    val decoder = PipelinedMemoryBusDecoder(PipelinedMemoryBusConfig(AddrWidth,DataWidth), mappings = decode_map.values.toSeq, pendingMax = 3)
    decoder.io.input <> io.dmem_bus_in

    io.dmem_bus_out <> decoder.io.outputs
}


object DmemBusDecoder_hdlgen {
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
            enumPrefixEnable = false,
            anonymSignalPrefix = "tmp",
            targetDirectory = "rtl_gen")
        .addStandardMemBlackboxing(blackboxAll)
        .generate(new DmemBusDecoder())
    }.printPruned()
}