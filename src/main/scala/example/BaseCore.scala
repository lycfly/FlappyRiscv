package example

import soc._
import backbone._
import base_plugins._
import spinal.core._
import spinal.core.sim._

object createStaticPipeline {
  def apply(
      disablePipelining: Boolean = false,
      extraPlugins: Seq[Plugin[Pipeline]] = Seq(),
      build: Boolean = true
  )(implicit conf: Config): StaticPipeline = {
    // import base_plugins._

    val pipeline = new Component with StaticPipeline {
      setDefinitionName("Pipeline")

      val fetch = new Stage("IF")
      val decode = new Stage("ID")
      val execute = new Stage("EX")
      val memory = new Stage("MEM")
      val writeback = new Stage("WB")

      override val stages = Seq(fetch, decode, execute, memory, writeback)
      override val passThroughStage: Stage = execute
      override val config: Config = conf
      override val data: StandardPipelineData = new StandardPipelineData(conf)
      override val pipelineComponent: Component = this
    }

    if (disablePipelining) {
      pipeline.addPlugin(new NoPipeliningScheduler)
    } else {
      pipeline.addPlugins(
        Seq(
          new Scheduler,
          new DataHazardResolver(firstRsReadStage = pipeline.execute)
        )
      )
    }

    pipeline.addPlugins(
      Seq(
        new StaticMemoryBackbone,
        new Fetcher(pipeline.fetch),
        new Decoder(pipeline.decode),
        new RegisterFileAccessor(pipeline.decode, pipeline.writeback),
        new IntAlu(Set(pipeline.execute)),
        new Shifter(Set(pipeline.execute)),
        new Lsu(Set(pipeline.memory), Seq(pipeline.memory), pipeline.memory),
        new BranchUnit(Set(pipeline.execute)),
        new PcManager(0x200L),
//        new NoPredictionPredictor(pipeline.fetch, pipeline.execute),
        new BranchTargetPredictor(pipeline.fetch, pipeline.execute, 128, conf.xlen),
        new CsrFile(pipeline.writeback, pipeline.writeback), // TODO: ugly
        new Timers,
        new MachineMode(pipeline.execute),
        new TrapHandler(pipeline.writeback),
        new TrapStageInvalidator,
//        new Interrupts(pipeline.writeback),
        new MulDiv(Set(pipeline.execute))
      ) ++ extraPlugins
    )

    if (build) {
      pipeline.build()
    }

    pipeline
  }
}

object SoC {
  def static(ramType: RamType, extraDbusReadDelay: Int = 0): SoC = {
    new SoC(ramType, config => createStaticPipeline()(config), extraDbusReadDelay)
  }
}

object Core {
  def main(args: Array[String]): Unit = {
    //    SpinalVerilog(SoC.static(RamType.OnChipRam(10 MiB, args.headOption)))
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
      .addStandardMemBlackboxing(blackboxAll)
      .generate(SoC.static(RamType.OnChipRam(10 MiB, args.headOption)))
  }
//  }.printPruned()
}

// object CoreSim {
//   def main(args: Array[String]) {
//     SimConfig.withWave.compile(SoC.static(RamType.OnChipRam(10 MiB, Some(args(0))))).doSim { dut =>
//       dut.clockDomain.forkStimulus(10)

//       val byteDevSim = new riscv.sim.StdioByteDev(dut.io.byteDev)

//       var done = false

//       while (!done) {
//         dut.clockDomain.waitSampling()

//         if (dut.io.charOut.valid.toBoolean) {
//           val char = dut.io.charOut.payload.toInt.toChar

//           if (char == 4) {
//             println("Simulation halted by software")
//             done = true
//           } else {
//             print(char)
//           }
//         }

//         byteDevSim.eval()
//       }
//     }
//   }
// }

class CoreFormal extends Component {
  setDefinitionName("Core")

  implicit val config = new Config(BaseIsa.RV32I)
  val pipeline = createStaticPipeline(extraPlugins = Seq(new RiscvFormal))
}

object CoreFormal {
  def main(args: Array[String]) {
    SpinalVerilog(new CoreFormal)
  }
}

// object CoreTestSim {
//   def main(args: Array[String]) {
//     var mainResult = 0

//     SimConfig.withWave.compile(SoC.static(RamType.OnChipRam(10 MiB, Some(args(0))))).doSim { dut =>
//       dut.clockDomain.forkStimulus(10)

//       var done = false

//       while (!done) {
//         dut.clockDomain.waitSampling()

//         if (dut.io.testDev.valid.toBoolean) {
//           val result = dut.io.testDev.payload.toBigInt

//           if (result == 0) {
//             println("All tests passed")
//           } else {
//             println(s"Test $result failed")
//             mainResult = 1
//           }

//           done = true
//         }
//       }
//     }

//     sys.exit(mainResult)
//   }
// }

// object CoreExtMem {
//   def main(args: Array[String]) {
//     SpinalVerilog(SoC.static(RamType.ExternalAxi4(10 MiB), 32))
//   }
// }
