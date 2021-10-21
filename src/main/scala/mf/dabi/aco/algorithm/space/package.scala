package mf.dabi.aco.algorithm

import breeze.linalg.{DenseMatrix, DenseVector}
import mf.dabi.aco.algorithm.ant.Ant

import scala.collection.parallel.immutable.ParMap
import scala.collection.parallel.mutable.ParArray

package object space {
  type City = DenseVector[Double]
  type Cities = ParArray[DenseVector[Double]]
  type RefCities = ParMap[Int, DenseVector[Double]]
  type RIndex = ParArray[Int]
  type Bound[@specialized(Int, Boolean) T] = List[Limit[T]]
  type Limit[@specialized(Int, Boolean) T] = Tuple2[T, T]
  type FuncCoste = Int => Int => Double
  type Pheromonas = DenseMatrix[Double]
  type Visibility = DenseMatrix[Double]
  //  type Probabilities = DenseMatrix[Double]
  type Colony = ParArray[Ant]
}
