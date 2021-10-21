package mf.dabi.aco.algorithm.ant

import breeze.linalg.DenseMatrix
import mf.dabi.aco.algorithm.ant.Decidible.choose
import mf.dabi.aco.algorithm.space._

import scala.util.Random

trait Decidible[T] {
  val now: Int
  val rest: RIndex

  def next(implicit sp: SearchSpace): Int = choose(rest)(now)
}

object Decidible {


  def choose(rest: => RIndex)(i: Int)(implicit sp: SearchSpace): Int = {
    val q: Double = Random.between(0.0, 1.0)
    if (q <= sp.params.q0) sp.bestc(i) else maxProb(rest)(i)
  }

  def bestIndx(ph: DenseMatrix[Double], mu: DenseMatrix[Double], beta: Double): Map[Int, Int] = {
    val (colMu, rowMu): (Int, Int) = (mu.cols, mu.rows)
    val (colPh, rowPh): (Int, Int) = (ph.cols, ph.rows)
    require(colMu == colPh && rowMu == rowPh)
    (0 until rowMu).toArray.map(i =>
      i -> (0 until colMu).toArray.map(j =>
        (j, ph(i, j) * math.pow(mu(i, j), beta)))
        .reduce[(Int, Double)] { case ((ic, acc), (iz, vz)) => if (acc < vz) (iz, vz) else (ic, acc) }._1
    ).toMap
  }

  def maxProb(rest: RIndex)(i: Int)(implicit sp: SearchSpace): Int = {
    sp.index.map(j => (j, prob(rest)(i, j))).reduce[(Int, Double)] { case ((ic, acc), (iz, vz)) => if (acc < vz) (iz, vz) else (ic, acc) }._1
  }


  def prob(rest: RIndex)(i: => Int, j: Int)(implicit sp: SearchSpace): Double =
    prob(sp.visibility, sp.pheromonas, sp.params.alpha, sp.params.beta, rest)(i, j)

  def prob(mu: => Visibility, ph: => Pheromonas, alpha: => Double, beta: => Double, rest: RIndex)(i: => Int, j: Int): Double = {
    if (!rest.toArray.contains(j)) 0 else pr(mu, ph, alpha, beta, rest)(i, j)
  }

  private def pr(mu: Visibility, ph: Pheromonas, alpha: Double, beta: Double, rest: RIndex)(i: Int, j: Int) = {
    val f: Int => Int => Double = (ci: Int) => (cj: Int) => math.pow(ph(ci, cj), alpha) * math.pow(mu(ci, cj), beta)
    val fi: Int => Double = f(i)
    val num: Double = fi(j)
    val den: Double = rest.map(fi).sum
    num / den
  }

}
