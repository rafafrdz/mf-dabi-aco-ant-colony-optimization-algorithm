package mf.dabi.aco.algorithm.examples

import mf.dabi.aco.algorithm.AntColonyOptimization.algorithm
import mf.dabi.aco.algorithm.function.CostFunction
import mf.dabi.aco.algorithm.space.{FuncCoste, RefCities, SearchSpace, SystemParams}

/**
 * Trait to make easy building new examples.
 * You have just extend it and implement.
 */
trait Example {

  def main(args: Array[String]): Unit = {
    val result = algorithm(sp, colonySize, iterJourney)

    /** best = true -> show only best path in plot; else plot all paths */
    result.show(true, plotPath)
    result.show(false, plotPath)
  }

  /** Example: "src/main/resources/plot/scratchExample" */
  val plotPath: String

  /** Since graph-plot library is still developing, the plotting method is experimental
   * at the moment only 2-dim plane is be able to be plotted, the rest just log the solution
   * Example:
   *
   * val colonySize: Int = 15
   * val iterJourney: Int = 30
   * */
  val colonySize: Int
  val iterJourney: Int

  /** Environment system params
   * Example:
   * val beta: Int = 6
   * val rho: Double = 0.6
   * val q0: Double = 0.2
   * */
  val beta: Int
  val rho: Double
  val q0: Double
  private val prms: SystemParams = SystemParams.build(bta = beta, r = rho, q = q0)

  /**
   * Example:
   *
   * val ref: RefCities = ParMap(
   * 4 -> DenseVector(14.413884661701701, -39.23001747899442)
   * , 3 -> DenseVector(-24.606006359049985, -38.2050360735867)
   * , 2 -> DenseVector(-8.619639124561914, 40.04472716943292)
   * , 1 -> DenseVector(94.5884088179508, -36.37376034398181)
   * , 0 -> DenseVector(-63.76390967730487, 71.81213487032437)
   * )
   *
   * */
  val ref: RefCities

  /** Cost function, in this case is the distance function between node-i and node-j */
  val costFunction: FuncCoste = CostFunction.invDistance(ref)

  /** Search space generated randomly */
  val sp: SearchSpace = SearchSpace.build(costFunction, ref, prms)

}
