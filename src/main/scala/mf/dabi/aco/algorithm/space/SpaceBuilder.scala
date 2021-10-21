package mf.dabi.aco.algorithm.space

import breeze.linalg.DenseVector

import scala.collection.parallel.immutable.ParMap
import scala.collection.parallel.mutable.ParArray
import scala.util.Random

trait SpaceBuilder extends Plotable {

  import SpaceBuilder._

  val dim: Int
  lazy val bound: Bound[Double] = cube(dim, 0, 1)

  def cities(size: Int): Cities = SpaceBuilder.cities(size, bound)

  def refs(size: Int): ParMap[Int, City] = SpaceBuilder.refsCities(size, bound)

  def random(size: Int, w: FuncCoste): SearchSpace = SearchSpace.build(w, refs(size))

}

object SpaceBuilder {
  def dimension(n: Int): SpaceBuilder = new SpaceBuilder {
    val dim: Int = n
  }

  def dimension(n: Int, limit: Bound[Double]): SpaceBuilder =
    new SpaceBuilder {
      val dim: Int = n
      override lazy val bound: Bound[Double] = limit
    }

  def cube(dim: Int, l0: Double, lf: Double): Bound[Double] = List.fill(dim)((l0, lf))

  def rndmP(bound: Bound[Double]): City =
    DenseVector(bound.map { case (l0, lf) => Random.between(l0, lf) }: _*)

  def cities(size: Int, bound: Bound[Double]): Cities = ParArray.fill(size)(rndmP(bound))

  def refsCities(size: Int, bound: Bound[Double]): ParMap[Int, City] = {
    val cs: Cities = cities(size, bound)
    val xs = cs.toArray.zipWithIndex.map { case (city, i) => (i, city) }
    ParArray.createFromCopy(xs).toMap

  }

}
