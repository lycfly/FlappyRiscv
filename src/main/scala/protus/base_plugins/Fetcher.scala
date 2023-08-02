package protus.base_plugins

import protus.backbone.{FetchAddressTranslator, FetchService, IBusControl, MemoryService, Pipeline, Plugin, Stage}
import spinal.core._
import spinal.lib._

class Fetcher(fetchStage: Stage, ibusLatency: Int = 2) extends Plugin[Pipeline] with FetchService {
  private var addressTranslator = new FetchAddressTranslator {
    override def translate(stage: Stage, address: UInt): UInt = {
      address
    }
  }

  private var addressTranslatorChanged = false

  override def build(): Unit = {
    fetchStage plug new Area {
      import fetchStage._

      val ibus = pipeline.service[MemoryService].createInternalIBus(fetchStage)
      val ibusCtrl = new IBusControl(ibus, ibusLatency)

      arbitration.isReady := False

//      val Branch_service = pipeline.service[BranchTargetPredictorService]

      val pc =  input(pipeline.data.PC)
      val nextPc = pc + 4

      when(arbitration.isRunning) {
//        val pc_with_predict = (output(Branch_service.getPredictPcValid) & fetchStage.arbitration.isDone) ? output(Branch_service.getPredictPcPayload) | pc
        val fetchAddress = addressTranslator.translate(fetchStage, pc)
//        val fetchAddress = addressTranslator.translate(fetchStage, pc)
        val (valid, rdata) = ibusCtrl.read(fetchAddress)

        when(valid) {
          arbitration.isReady := True

          output(pipeline.data.NEXT_PC) := nextPc
          output(pipeline.data.IR) := rdata
        }
      }
    }
  }

  override def setAddressTranslator(translator: FetchAddressTranslator): Unit = {
    assert(!addressTranslatorChanged, "FetchAddressTranslator can only be set once")

    addressTranslator = translator
    addressTranslatorChanged = true
  }
}
