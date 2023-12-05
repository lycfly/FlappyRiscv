package boom_v1.exec

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.util.Random
import scala.language.postfixOps




// tell the FUDecoders what units it needs to support
class SupportedFuncUnits(
                          val alu: Boolean  = false,
                          val bru: Boolean  = false,
                          val mem: Boolean  = false,
                          val muld: Boolean = false,
                          val fpu: Boolean  = false,
                          val csr: Boolean  = false,
                          val fdiv: Boolean = false)
{
}


object function_units_inst {
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
      .generate(new function_units())
  }.printPruned()
}