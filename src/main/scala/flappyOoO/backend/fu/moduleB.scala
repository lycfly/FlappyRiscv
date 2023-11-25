package flappyOoO.backend.fu

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.util.Random
import scala.language.postfixOps
case class Configs()

class moduleA()(implicit conf: Configs) extends Component {
  val io = new Bundle {
    val ina = in Bool()
    val outb = out Bool()
  }
  noIoPrefix()
  io.outb := io.ina

}
class moduleB(implicit conf: Configs) extends Component {
  val io = new Bundle {
    val ina = in Bool()
    val outb = out Bool()
  }
  noIoPrefix()
  io.outb := ~io.ina
}

object moduleB_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      mode = Verilog,
      targetDirectory = "rtl"
    ).generate({
        implicit val conf = Configs()
        val modA = new moduleA()
        val modB = new moduleB()
        modB
       })
  }.printPruned()
}