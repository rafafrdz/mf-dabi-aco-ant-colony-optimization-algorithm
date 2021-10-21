package mf.dabi.aco.algorithm.function

import mf.dabi.aco.algorithm.space.{City, RefCities}

import scala.collection.parallel.mutable.ParArray

object CostFunction {

  def distance(ref: RefCities): Int => Int => Double = (i: Int) => (j: Int) => distance(ref(i), ref(j))

  def invDistance(ref: RefCities): Int => Int => Double = (i: Int) => (j: Int) => if (i == j) 0.0 else 1 / distance(ref(i), ref(j))

  private def distance(ci: City, cj: City): Double = dEuclidean(ci, cj)

  /** Distance Euclidean */
  private def dEuclidean(ci: City, cj: City): Double = {
    val (ciXs, cjXs) = (ParArray(ci.toArray: _*), ParArray(cj.toArray: _*))
    val dSqre: Double = ciXs.zip(cjXs).map { case (i, j) => math.pow(i - j, 2) }.sum
    math.sqrt(dSqre)
  }

}
