package protus.base_plugins

import protus.backbone.{BranchTargetPredictorService, Pipeline, PipelineData, Plugin, Stage}
import spinal.core._

class NoPredictionPredictor(fetchStage: Stage, executeStage: Stage)
    extends Plugin[Pipeline]
    with BranchTargetPredictorService {
  private object Data {
    object PREDICTED_PC extends PipelineData(UInt(config.xlen bits))
  }

//  override def getPredictPcValid = null
//  override def getPredictPcPayload = null
  override def build(): Unit = {
    fetchStage plug new Area {
      fetchStage.output(Data.PREDICTED_PC) := fetchStage.value(pipeline.data.NEXT_PC)
    }

    executeStage plug new Area {
      executeStage.output(Data.PREDICTED_PC)
    }
  }

  override def predictedPc(stage: Stage): UInt = {
    stage.output(Data.PREDICTED_PC)
  }

  override def setPredictedPc(stage: Stage, pc: UInt): Unit = {
    stage.input(Data.PREDICTED_PC) := pc
  }

  override def predictionForAddress(address: UInt): UInt = {
    address + 4
  }
}
