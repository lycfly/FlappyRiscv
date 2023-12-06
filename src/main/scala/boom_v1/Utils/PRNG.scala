package boom_v1.Utils

import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import scala.util.Random

/** Pseudo Random Number Generators (PRNG) interface
  * @param n the width of the LFSR
  * @groupdesc Signals The actual hardware fields of the Bundle
  */
class PRNGIO(val n: Int) extends Bundle with IMasterSlave {

  /** A [[chisel3.util.Valid Valid]] interface that can be used to set the seed (internal PRNG state)
    * @group Signals
    */
  val seed: Flow[Vec[Bool]] = Flow(Vec( Bool(), n))

  /** When asserted, the PRNG will increment by one
    * @group Signals
    */
  val increment: Bool = Bool()

  /** The current state of the PRNG
    * @group Signals
    */
  val prng_out: Vec[Bool] = Vec(Bool(), n)
  override def asMaster() = {
    master(seed)
    in(prng_out)
    out(increment)

  }
}

/** An abstract class representing a Pseudo Random Number Generator (PRNG)
  * @param width the width of the PRNG
  * @param seed the initial state of the PRNG
  * @param step the number of state updates per cycle
  * @param updateSeed if true, when loading the seed the state will be updated as if the seed were the current state, if
  * false, the state will be set to the seed
  */
abstract class PRNG(val width: Int, val seed: Option[BigInt], step: Int = 1, updateSeed: Boolean = false)
  extends Module {
  require(width > 0, s"Width must be greater than zero! (Found '$width')")
  require(step > 0, s"Step size must be greater than one! (Found '$step')")

  val io: PRNGIO = slave(new PRNGIO(width))

  /** Allow implementations to override the reset value, e.g., if some bits should be don't-cares. */
  protected def resetValue: Vec[Bool] = seed match {
//    case Some(s) => Vec.fill[Bool](width)(for(i <- 0 until width) yield U(s, width bits)(i))
    case Some(s) => U(s, width bits).asBools

    case None    => Vec({val b = Bool(); b.assignDontCare(); b}, width)
  }

  /** Internal state of the PRNG. If the user sets a seed, this is initialized to the seed. If the user does not set a
    * seed this is left uninitialized. In the latter case, a PRNG subclass *must do something to handle lockup*, e.g.,
    * the PRNG state should be manually reset to a safe value. [[LFSR]] handles this by, based on the chosen reduction
    * operator, either sets or resets the least significant bit of the state.
    */
  private[random] val state: Vec[Bool] = RegInit(resetValue)

  /** State update function
    * @param s input state
    * @return the next state
    */
  def delta(s: Seq[Bool]): Seq[Bool]

  /** The method that will be used to update the state of this PRNG
    * @param s input state
    * @return the next state after `step` applications of [[PRNG.delta]]
    */
  final def nextState(s: Seq[Bool]): Seq[Bool] = (0 until step).foldLeft(s) { case (s, _) => delta(s) }

  when(io.increment) {
    state := nextState(state).asInstanceOf[Vec[Bool]]
  }

  when(io.seed.fire) {
    state := (if (updateSeed) { nextState(io.seed.payload).asInstanceOf[Vec[Bool]] }
    else { io.seed.payload })
  }

  io.prng_out := state

}

/** Helper utilities related to the construction of Pseudo Random Number Generators (PRNGs) */
object PRNG {

  /** Wrap a [[PRNG]] to only return a pseudo-random [[UInt]]
    * @param gen a pseudo random number generator
    * @param increment when asserted the [[PRNG]] will increment
    * @return the output (internal state) of the [[PRNG]]
    */
  def apply(gen: => PRNG, increment: Bool = True): UInt = {
    val prng = gen
    prng.io.seed.valid := False
    prng.io.seed.payload.assignDontCare()
    prng.io.increment := increment
    prng.io.prng_out.asBits.asUInt
  }

}
