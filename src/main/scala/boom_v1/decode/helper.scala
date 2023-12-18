package boom_v1.decode
import boom_v1.MaskedDC
import spinal.core._
import spinal.lib._
object DecodeLogic {
  def term(lit: MaskedLiteral) =
    new Term(lit.value, BigInt(2).pow(lit.getWidth) - (lit.careAbout + 1))

  def logic(addr: UInt, addrWidth: Int, cache: scala.collection.mutable.Map[Term, Bool], terms: Seq[Term]) = {
    terms.map { t =>
      cache.getOrElseUpdate(t, (if (t.mask == 0) addr else addr & U(BigInt(2).pow(addrWidth) - (t.mask + 1), addrWidth bits)) === U(t.value, addrWidth bits))
    }.foldLeft(Bool(false))(_ || _)
  }

  def apply(addr: UInt, default: MaskedLiteral, mapping: Iterable[(MaskedLiteral, MaskedLiteral)]): UInt = {
    val cache = caches.getOrElseUpdate(addr, collection.mutable.Map[Term, Bool]())
    val dterm = term(default)
    val (keys, values) = mapping.unzip
    val addrWidth = keys.map(_.getWidth).max
    val terms = keys.toList.map(k => term(k))
    val termvalues = terms zip values.toList.map(term(_))

    for (t <- keys.zip(terms).tails; if !t.isEmpty)
      for (u <- t.tail)
        assert(!t.head._2.intersects(u._2), "DecodeLogic: keys " + t.head + " and " + u + " overlap")

    Cat((0 until default.getWidth.max(values.map(_.getWidth).max)).map({ case (i: Int) =>
      val mint = termvalues.filter { case (k, t) => ((t.mask >> i) & 1) == 0 && ((t.value >> i) & 1) == 1 }.map(_._1)
      val maxt = termvalues.filter { case (k, t) => ((t.mask >> i) & 1) == 0 && ((t.value >> i) & 1) == 0 }.map(_._1)
      val dc = termvalues.filter { case (k, t) => ((t.mask >> i) & 1) == 1 }.map(_._1)

      if (((dterm.mask >> i) & 1) != 0) {
        logic(addr, addrWidth, cache, SimplifyDC(mint, maxt, addrWidth))
      } else {
        val defbit = (dterm.value.toInt >> i) & 1
        val t = if (defbit == 0) mint else maxt
        val bit = logic(addr, addrWidth, cache, Simplify(t, dc, addrWidth))
        if (defbit == 0) bit else ~bit
      }
    }).reverse)
  }

  def apply(addr: UInt, default: Seq[MaskedLiteral], mappingIn: Iterable[(MaskedLiteral, Seq[MaskedLiteral])]): Seq[UInt] = {
    val mapping = collection.mutable.ArrayBuffer.fill(default.size)(collection.mutable.ArrayBuffer[(MaskedLiteral, MaskedLiteral)]())
    for ((key, values) <- mappingIn)
      for ((value, i) <- values zipWithIndex)
        mapping(i) += key -> value
    for ((thisDefault, thisMapping) <- default zip mapping)
      yield apply(addr, thisDefault, thisMapping)
  }

  def apply(addr: UInt, default: Seq[MaskedLiteral], mappingIn: List[(UInt, Seq[MaskedLiteral])]): Seq[UInt] =
    apply(addr, default, mappingIn.map(m => (MaskedLiteral(m._1), m._2)).asInstanceOf[Iterable[(MaskedLiteral, Seq[MaskedLiteral])]])

  def apply(addr: UInt, trues: Iterable[UInt], falses: Iterable[UInt]): Bool =
    apply(addr, MaskedDC(1), trues.map(MaskedLiteral(_) -> MaskedLiteral("b1")) ++ falses.map(MaskedLiteral(_) -> MaskedLiteral("b0"))).toBool

  private val caches = collection.mutable.Map[UInt, collection.mutable.Map[Term, Bool]]()
}

class Term(val value: BigInt, val mask: BigInt = 0)
{
  var prime = true

  def covers(x: Term) = ((value ^ x.value) &~ mask | x.mask &~ mask) == 0
  def intersects(x: Term) = ((value ^ x.value) &~ mask &~ x.mask) == 0
  override def equals(that: Any) = that match {
    case x: Term => x.value == value && x.mask == mask
    case _ => false
  }
  override def hashCode = value.toInt
  def < (that: Term) = value < that.value || value == that.value && mask < that.mask
  def similar(x: Term) = {
    val diff = value - x.value
    mask == x.mask && value > x.value && (diff & diff-1) == 0
  }
  def merge(x: Term) = {
    prime = false
    x.prime = false
    val bit = value - x.value
    new Term(value &~ bit, mask | bit)
  }

  override def toString = value.toString(16) + "-" + mask.toString(16) + (if (prime) "p" else "")
}
