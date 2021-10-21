package mf.dabi.aco.algorithm.examples

import mf.dabi.aco.algorithm.AntColonyOptimization.algorithm
import mf.dabi.aco.algorithm.function.CostFunction
import mf.dabi.aco.algorithm.space._

import scala.util.Random

object RandomExample {

  def main(args: Array[String]): Unit = {
    val result = algorithm(sp, colonySize, iterJourney)

    /** best = true -> show only best path in plot; else plot all paths */
    result.show(path = s"src/main/resources/plot/random/$randomName")
  }

  /** Since graph-plot library is still developing, the plotting method is experimental
   * at the moment only 2-dim plane is be able to be plotted, the rest just log the solution */
  val dim: Int = 2
  val nodes: Int = 5
  val colonySize: Int = 10
  val iterJourney: Int = 100

  /** Environment system params */
  val prms: SystemParams = SystemParams.build(bta = 6, r = 0.6, q = 0.2)

  /** Space random builder */
  val bounded: Bound[Double] = SpaceBuilder.cube(dim, -100, 100)
  implicit val spb: SpaceBuilder = SpaceBuilder.dimension(dim, bounded)

  /** Build a random set of nodes */
  val ref: RefCities = spb.refs(nodes)
  /** Cost function, in this case is the distance function between node-i and node-j */
  val costFunction: FuncCoste = CostFunction.invDistance(ref)

  /** Search space generated randomly */
  val sp: SearchSpace = SearchSpace.build(costFunction, ref, prms)


  def randomHash(length: Int): String = Random.alphanumeric.take(length).mkString("")

  val randomName: String = randomHash(5)

}
