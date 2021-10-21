package mf.dabi.aco.algorithm.ant

import mf.dabi.aco.algorithm.space._

trait Trazable[T] {

  import Trazable._

  val now: Int
  val dph: Pheromonas
  lazy val (pht, dphk): (Double, Pheromonas) = resolve(dph)

  def update(next: Int)(implicit sp: SearchSpace): Unit =
    delta(dph)(now, next, sp.coste(now)(next))


}

object Trazable {

  def delta(dph: Pheromonas)(i: Int, j: Int, value: Double): Unit = {
    dph.update(i, j, dph(i, j) + value)
    dph.update(j, i, dph(j, i) + value)
  }

  def replace(dph: Pheromonas)(i: Int, j: Int, value: Double): Unit = {
    dph.update(i, j, value)
    dph.update(j, i, value)
  }

  def resolve(dph: Pheromonas): (Double, Pheromonas) = {
    val (idx, phs) = (0 until dph.rows).flatMap(i =>
      (0 until dph.cols).flatMap(j =>
        if (dph(i, j) != 0 && i <= j) Option((i, j, dph(i, j))) else None))
      .map { case (i, j, v) => (i, j) -> v }.unzip

    val dphPath: Double = phs.sum
    val dphResolved: Pheromonas = dph.copy
    idx.foreach { case (i, j) => replace(dphResolved)(i, j, dphPath) }
    (dphPath, dphResolved)
  }
}
