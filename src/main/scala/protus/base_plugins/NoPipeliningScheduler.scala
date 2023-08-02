package protus.base_plugins

import protus.backbone._

import spinal.core._
import spinal.lib._

class NoPipeliningScheduler extends Scheduler {
  override def build(): Unit = {
    super.build()

    pipeline plug new Area {
      val stages = pipeline.stages
      stages.head.arbitration.isValid := !stages.tail.map(_.arbitration.isValid).orR
    }
  }
}
