package mf.dabi.aco.algorithm.examples

import breeze.linalg.DenseVector
import mf.dabi.aco.algorithm.AntColonyOptimization.algorithm
import mf.dabi.aco.algorithm.function.CostFunction
import mf.dabi.aco.algorithm.space.{FuncCoste, RefCities, SearchSpace, SystemParams}

import scala.collection.parallel.immutable.ParMap

object SmallExample {


  def main(args: Array[String]): Unit = {
    val result = algorithm(sp, colonySize, iterJourney)

    /** best = true -> show only best path in plot; else plot all paths */
    result.show(true, "src/main/resources/plot/smallExample")
  }

  /** Since graph-plot library is still developing, the plotting method is experimental
   * at the moment only 2-dim plane is be able to be plotted, the rest just log the solution */
  val colonySize: Int = 15
  val iterJourney: Int = 300

  /** Environment system params */
  val prms: SystemParams = SystemParams.build(bta = 6, r = 0.6, q = 0.2)

  val ref: RefCities = ParMap(
    4 -> DenseVector(14.413884661701701, -39.23001747899442)
    , 3 -> DenseVector(-24.606006359049985, -38.2050360735867)
    , 2 -> DenseVector(-8.619639124561914, 40.04472716943292)
    , 1 -> DenseVector(94.5884088179508, -36.37376034398181)
    , 0 -> DenseVector(-63.76390967730487, 71.81213487032437)
  )

  /** Cost function, in this case is the distance function between node-i and node-j */
  val costFunction: FuncCoste = CostFunction.invDistance(ref)

  /** Search space generated randomly */
  val sp: SearchSpace = SearchSpace.build(costFunction, ref, prms)

}
