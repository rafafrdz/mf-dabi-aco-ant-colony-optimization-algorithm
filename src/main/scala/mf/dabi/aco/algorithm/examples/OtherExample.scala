package mf.dabi.aco.algorithm.examples

import breeze.linalg.DenseVector
import mf.dabi.aco.algorithm.AntColonyOptimization.algorithm
import mf.dabi.aco.algorithm.function.CostFunction
import mf.dabi.aco.algorithm.space.{FuncCoste, RefCities, SearchSpace, SystemParams}

import scala.collection.parallel.immutable.ParMap

object OtherExample {

  def main(args: Array[String]): Unit = {
    val result = algorithm(sp, colonySize, iterJourney)

    /** best = true -> show only best path in plot; else plot all paths */
    result.show(best = true, path = "src/main/resources/plot/otherExample")
  }

  /** Since graph-plot library is still developing, the plotting method is experimental
   * at the moment only 2-dim plane is be able to be plotted, the rest just log the solution */
  val colonySize: Int = 100
  val iterJourney: Int = 100

  /** Environment system params */
  val prms: SystemParams = SystemParams.build(bta = 6, r = 0.6, q = 0.2)

  val ref: RefCities = ParMap(
    15 -> DenseVector(56.049427817549116, -56.45394840281603)
    , 47 -> DenseVector(19.655989562222402, 94.90711834474322)
    , 4 -> DenseVector(98.66122029060276, -2.7629830683375474)
    , 19 -> DenseVector(-41.41589369782886, -95.20849313211879)
    , 30 -> DenseVector(29.222125560852874, 77.77153634522057)
    , 36 -> DenseVector(-12.625857423012405, -30.523866888335377)
    , 8 -> DenseVector(-93.43675178818435, 59.40710634330253)
    , 23 -> DenseVector(-62.79876190694091, -37.92487941133788)
    , 26 -> DenseVector(-90.92658573033239, -36.04498676390111)
    , 40 -> DenseVector(13.502624697237152, 81.75081923490927)
    , 11 -> DenseVector(-57.01802404427803, -46.0610108548712)
    , 43 -> DenseVector(65.48883394403984, -62.34433112830433)
    , 16 -> DenseVector(94.0296570620115, 3.5519277257685076)
    , 31 -> DenseVector(-68.86149146554048, -27.863358383056493)
    , 18 -> DenseVector(-20.193582155988807, 31.261380221542765)
    , 48 -> DenseVector(49.923685642125434, -43.4805610690002)
    , 3 -> DenseVector(-28.95354220550317, 55.01556289003008)
    , 35 -> DenseVector(-42.68843362664197, -68.66220762585347)
    , 7 -> DenseVector(48.32991466426776, 6.769878113602346)
    , 39 -> DenseVector(-10.532739217825608, -62.630461593035825)
    , 49 -> DenseVector(-39.89346733282338, 94.16971125604815)
    , 12 -> DenseVector(-56.90013128720168, -87.18310895210766)
    , 22 -> DenseVector(-25.946553199958714, 6.237875989125257)
    , 44 -> DenseVector(53.125616896668504, -41.97828818150451)
    , 27 -> DenseVector(31.251565733764323, 98.71383667391595)
    , 32 -> DenseVector(-59.962263853055745, 8.437229882033122)
    , 34 -> DenseVector(43.87609792646322, -80.01898600603528)
    , 45 -> DenseVector(90.70277209531417, 32.620719422305115)
    , 17 -> DenseVector(-90.63518620446315, -87.8381999849061)
    , 2 -> DenseVector(-17.126305204336518, 61.03333080992698)
    , 13 -> DenseVector(-56.130997085910984, 25.298473200251806)
    , 41 -> DenseVector(-90.33858540250348, 66.57875163325207)
    , 9 -> DenseVector(-60.812476156954176, -90.26377014517944)
    , 1 -> DenseVector(-0.009050372052115563, 27.288105931725326)
    , 6 -> DenseVector(-98.70881702364764, -64.7682025930545)
    , 28 -> DenseVector(85.70572864120066, 62.483861264630974)
    , 38 -> DenseVector(39.516172216552064, -35.32544035382767)
    , 21 -> DenseVector(-45.08721562338647, -25.42505513015243)
    , 33 -> DenseVector(26.086531279455798, -61.08962560447688)
    , 29 -> DenseVector(-14.585741898538345, -39.62405773434352)
    , 46 -> DenseVector(5.911891093763671, -35.11023055951968)
    , 20 -> DenseVector(16.323992198480838, -21.388993675357824)
    , 14 -> DenseVector(38.050815420906474, -77.60439155928736)
    , 25 -> DenseVector(75.52463730733587, -26.994033400709142)
    , 37 -> DenseVector(-16.38187351504301, -86.61050317829063)
    , 24 -> DenseVector(-58.09280581132732, 53.32574855287001)
    , 42 -> DenseVector(1.979614748445968, -10.45840822147892)
    , 10 -> DenseVector(-46.261059420411854, -50.09105759127195)
    , 5 -> DenseVector(78.55763042351921, 76.04836326666396)
    , 0 -> DenseVector(-26.614234004034955, 34.13701046613809)
  )

  /** Cost function, in this case is the distance function between node-i and node-j */
  val costFunction: FuncCoste = CostFunction.invDistance(ref)

  /** Search space generated randomly */
  val sp: SearchSpace = SearchSpace.build(costFunction, ref, prms)
}
