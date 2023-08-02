package protus.soc

import EasonLib.Bus.MemBus.{PipelineMemBus2Ahblite, PipelineMemBusRamMultiPort}
import protus.backbone.{BaseIsa, Config, InterruptService, MemBus, MemoryService, Pipeline}
import protus.example.{createDynamicPipeline, createStaticPipeline}
import protus.soc.devices.{Apb3ByteDev, Apb3CharDev, Apb3MachineTimers, Apb3TestDev, ByteDevIo}
import protus.soc.devices._
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.ahblite.{AhbLite3, AhbLite3Config, AhbLite3OnChipRamMultiPort}
import spinal.lib.misc.HexTools
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.simple.PipelinedMemoryBusConfig

sealed abstract class RamType(val size: BigInt)

object RamType {
  case class OnChipRam(override val size: BigInt, initHexFile: Option[String]) extends RamType(size)

  case class ExternalAxi4(override val size: BigInt) extends RamType(size)
}

class SoC(
           ramType: RamType,
           createPipeline: Config => Pipeline,
           extraDbusReadDelay: Int = 0
         ) extends Component {
  setDefinitionName("Core")

  implicit val config = new Config(BaseIsa.RV32I)

  val io = new Bundle {
    // Peripherals
    val charOut = master(Flow(UInt(8 bits)))
    val testDev = master(Flow(UInt(config.xlen bits)))
    val byteDev = master(new ByteDevIo)

    val axi = ramType match {
      case RamType.ExternalAxi4(size) =>
        val axiConfig = Axi4SharedOnChipRam.getAxiConfig(
          dataWidth = config.xlen,
          byteCount = size,
          idWidth = 4
        )

        master(Axi4Shared(axiConfig))
      case _ => null
    }
  }

  val socClockDomain = ClockDomain(
    clock = clockDomain.clock,
    reset = clockDomain.reset,
    frequency = FixedFrequency(100 MHz)
  )

  val coreClockDomain = ClockDomain(
    clock = clockDomain.clock,
    reset = clockDomain.reset
  )

  val soc = new ClockingArea(socClockDomain) {
    val core = new ClockingArea(coreClockDomain) {
      val pipeline = createPipeline(config)

      val memService = pipeline.service[MemoryService]
      val ibus = memService.getExternalIBus
      val dbus = memService.getExternalDBus
    }

    core.setName("")

    val ramAxi = ramType match {
      case RamType.ExternalAxi4(_) => io.axi
      case RamType.OnChipRam(size, initHexFile) =>
        val ram = Axi4SharedOnChipRam(
          byteCount = size,
          dataWidth = config.xlen,
          idWidth = 4
        )

        initHexFile.foreach(HexTools.initRam(ram.ram, _, 0x80000000L))
        ram.io.axi
    }

    val apbBridge = Axi4SharedToApb3Bridge(
      addressWidth = config.dbusConfig.addressWidth,
      dataWidth = config.dbusConfig.dataWidth,
      idWidth = 4
    )

    val axiCrossbar = Axi4CrossbarFactory()

    // Without low latency, only one command every 2 cycles is accepted on the master bus which
    // wouldn't allow us to reach IPC=1. This could be fixed by using bursts on the ibus.
    axiCrossbar.lowLatency = true

    axiCrossbar.addSlaves(
      ramAxi -> (0x80000000L, ramType.size),
      apbBridge.io.axi -> (0x00000000L, 1 GiB)
    )

    val ibusAxi = core.ibus.toAxi4ReadOnly()
    val dbusAxi = core.dbus.toAxi4Shared()

    axiCrossbar.addConnections(
      ibusAxi -> List(ramAxi),
      dbusAxi -> List(ramAxi, apbBridge.io.axi)
    )

    // This pipelining is used to cut combinatorial loops caused by lowLatency=true. It is based on
    // advice from the Spinal developers: "m2sPipe is a full bandwidth master to slave cut,
    // s2mPipe is a full bandwidth slave to master cut".
    // TODO I should really read-up on this pipelining stuff...
    axiCrossbar.addPipelining(ibusAxi)((ibus, crossbar) => {
      ibus.readCmd.m2sPipe() >> crossbar.readCmd
      ibus.readRsp << crossbar.readRsp.s2mPipe()
    })

    if (extraDbusReadDelay > 0) {
      axiCrossbar.addPipelining(dbusAxi)((dbus, crossbar) => {
        import protus.backbone.Utils._

        dbus.sharedCmd >> crossbar.sharedCmd
        dbus.writeData >> crossbar.writeData
        dbus.readRsp << crossbar.readRsp.stage(extraDbusReadDelay)
        dbus.writeRsp << crossbar.writeRsp
      })
    }

    axiCrossbar.build()

    val machineTimers = new Apb3MachineTimers(core.pipeline)

    val charDev = new Apb3CharDev
    io.charOut << charDev.io.char

    val testDev = new Apb3TestDev
    io.testDev << testDev.io.test

    val byteDev = new Apb3ByteDev
    io.byteDev <> byteDev.io.bytes

    core.pipeline.serviceOption[InterruptService] foreach { interruptService =>
      interruptService.getExternalIrqIo <> byteDev.io.irq
    }

    val apbDecoder = Apb3Decoder(
      master = apbBridge.io.apb,
      slaves = List(
        machineTimers.io.apb -> (0x02000000L, 4 KiB),
        charDev.io.apb -> (0x10000000L, 4 KiB),
        byteDev.io.apb -> (0x20000000L, 4 KiB),
        testDev.io.apb -> (0x30000000L, 4 KiB)
      )
    )
  }
}

class SocAHB(
      ramType: RamType,
      createPipeline: Config => Pipeline,
      extraDbusReadDelay: Int = 0
    ) extends Component {
  setDefinitionName("CoreAhb")
  implicit val config = new Config(BaseIsa.RV32I)
  val pipeline = createPipeline(config)

  val memService = pipeline.service[MemoryService]
  val ibus = memService.getExternalIBus
  val dbus = memService.getExternalDBus
//  val ramAhb = ramType match {
//    case RamType.OnChipRam(size, initHexFile) => {
//      val ram = AhbLite3OnChipRamMultiPort(portCount = 2, AhbLite3Config(32, config.xlen), size)
//      initHexFile.foreach(HexTools.initRam(ram.ram, _, 0x00000000L))
//      ram.io.ahbs
//    }
//    case _ => null
//  }
  val ibusMemBus = ibus.toPipeMemBus()
  val dbusMemBus = dbus.toPipeMemBus()
  val dmemdecoder = new DmemBusDecoder(32, config.xlen, DbusOutNum = 3)
  val imemdecoder = new ImemBusDecoder(32, config.xlen, DbusOutNum = 2)
  val TCM = new PipelineMemBusRamMultiPort(2, 32, config.xlen, 64 KiB)

  val ibus2ahb = new PipelineMemBus2Ahblite(AhbLite3Config(32,config.xlen), PipelinedMemoryBusConfig(32,config.xlen))
  val dbus2ahb = new PipelineMemBus2Ahblite(AhbLite3Config(32,config.xlen), PipelinedMemoryBusConfig(32,config.xlen))

  imemdecoder.io.imem_bus_in <> ibusMemBus
  dmemdecoder.io.dmem_bus_in <> dbusMemBus

  ibus2ahb.io.pipelinedMemoryBus <> imemdecoder.io.imem_bus_out(0)
  dbus2ahb.io.pipelinedMemoryBus <> dmemdecoder.io.dmem_bus_out(0)

  TCM.io.pipeMemBus(0) <> imemdecoder.io.imem_bus_out(1)
  TCM.io.pipeMemBus(1) <> dmemdecoder.io.dmem_bus_out(1)

//  ramAhb(0) <> ibus2ahb.io.ahb.toAhbLite3()
//  ramAhb(1) <> dbus2ahb.io.ahb.toAhbLite3()
val io = new Bundle{
  val ibusAhbMaster = master(AhbLite3(AhbLite3Config(32, config.xlen)))
  val dbusAhbMaster = master(AhbLite3(AhbLite3Config(32, config.xlen)))
}

  val iahb = ibus2ahb.io.ahb
  val dahb = dbus2ahb.io.ahb

  io.ibusAhbMaster << iahb
  io.dbusAhbMaster << dahb

  dmemdecoder.io.dmem_bus_out(2).cmd.ready := False
  dmemdecoder.io.dmem_bus_out(2).rsp.valid := False
  dmemdecoder.io.dmem_bus_out(2).rsp.payload.assignDontCare()
}


object CoreAhb {
  def main(args: Array[String]): Unit = {
    //    SpinalVerilog(SoC.static(RamType.OnChipRam(10 MiB, args.headOption)))
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
//      .addStandardMemBlackboxing(blackboxAll)
      .generate(new SocAHB(RamType.OnChipRam(1 MiB, args.headOption), config => createStaticPipeline()(config),0))
  }
  //  }.printPruned()
}

class ProteusCore(
              createPipeline: Config => Pipeline
            ) extends Component {
  setDefinitionName("ProteusCore")
  implicit val config = new Config(BaseIsa.RV32I)
  val pipeline = createPipeline(config)

  val memService = pipeline.service[MemoryService]
  val ibus = memService.getExternalIBus
  val dbus = memService.getExternalDBus

  val io = new Bundle{
    val ibus = master(new MemBus(config.ibusConfig, 2 bits)).setName("ibus")
    val dbus = master(new MemBus(config.dbusConfig, 2 bits)).setName("dbus")
  }
  io.ibus <> ibus
  io.dbus <> dbus
}

object ProteusCore_Inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = true,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      //      .addStandardMemBlackboxing(blackboxAll)
      .generate(new ProteusCore(config => createStaticPipeline()(config)))
  }
  //  }.printPruned()
}
object ProteusCoreDyn_Inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = true,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      //      .addStandardMemBlackboxing(blackboxAll)
      .generate(new ProteusCore(config => createDynamicPipeline()(config)))
  }
  //  }.printPruned()
}
object ProteusCore_syn {
  import EasonLib.DesignCompiler._
  def main(args: Array[String]): Unit = {

    val dc_config = DesignCompiler_config(process = 28, freq = 100)
    val dc = new DesignCompilerFlow(
      design = new ProteusCore(config => createStaticPipeline()(config)),
      topModuleName = "ProteusCore",
      workspacePath = "/home/lyc/projects/riscv/FlappyRiscv/syn/ProteusCore",
      DCConfig = dc_config,
      designPath = ""
    ).doit()
  }
}