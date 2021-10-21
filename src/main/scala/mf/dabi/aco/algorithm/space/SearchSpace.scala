package mf.dabi.aco.algorithm.space

import breeze.linalg.{DenseMatrix, _}
import mf.dabi.aco.algorithm.ant.Decidible
import mf.dabi.aco.algorithm.function.CostFunction
import mf.dabi.aco.algorithm.space.SearchSpace._
import mf.dabi.aco.algorithm.space.journey.{Path, Travels}

import scala.collection.parallel.ParIterable


trait SearchSpace {
  val ref: RefCities
  lazy val coste: FuncCoste = CostFunction.invDistance(ref)
  lazy val index: RIndex = ref.keys.toParArray
  lazy val size: Int = index.size
  lazy val dimCities: Int = cities.head.length
  lazy val cities: Cities = ref.values.toParArray
  lazy val pheromonas: Pheromonas = initPh(coste, index)
  lazy val visibility: Visibility = initVisibl(ref)
  lazy val bestc: Map[Int, Int] = Decidible.bestIndx(pheromonas, visibility, params.beta)

  lazy val params: SystemParams = SystemParams.default

  lazy val clean: Pheromonas = buildClean()
  lazy val paths: Travels = Travels(travel: _*)
  lazy val bestPath: Travels = Travels(travel.reduce((p1, p2) => if (p1.peso < p2.peso) p2 else p1))
  private lazy val travel: List[Path] = buildPath()

  def update(w: FuncCoste = coste,
             refs: RefCities = ref,
             ph: Pheromonas = pheromonas,
             mu: Pheromonas = visibility,
             prms: SystemParams = params): SearchSpace = SearchSpace.build(w, ref, ph, mu, prms)

  def update(ants: Colony): SearchSpace = updatePheromona(ants)

  def plot(path: String = null)(implicit pl: Plotable): Unit = generatePlot(paths, "all", path)

  def plotBest(path: String = null)(implicit pl: Plotable): Unit = generatePlot(bestPath, "best", path)

  private def generatePlot(travels: Travels, default: String, path: String = null)(implicit pl: Plotable): Unit = {
    Option(path) match {
      case Some(value) => pl.set(travels, this).renderPath(value, default)
      case None => pl.set(travels, this).render(default)
    }
  }

  def info(best: Boolean = false, path: String = null)(implicit pl: Plotable): Unit = SearchSpace.info(this, best, path)

  def show(best: Boolean = false, path: String = null)(implicit pl: Plotable): Unit = SearchSpace.info(this, best, path)

  private def updatePheromona(ants: Colony): SearchSpace = {
    val phsMap = ants.map(ant => ant.pht -> ant.dphk).toMap
    val (phts, dphks) = (phsMap.keys, phsMap.values.toArray)
    val bestpts: Double = phts.max
    val bestdph: Pheromonas = phsMap(bestpts)
    val updateAll: Pheromonas = updateAntPheromona(pheromonas, dphks: _*)
    val newph: Pheromonas = updateBestPheromona(updateAll, bestdph)
    update(ph = newph)
  }

  private def updateAntPheromona(ph: Pheromonas, dph: Pheromonas*): Pheromonas =
    (1 - params.rho) * ph + dph.reduce(_ + _)

  private def updateBestPheromona(ph: Pheromonas, bestdph: Pheromonas): Pheromonas =
    (1 - params.rho) * ph + params.rho * bestdph

  private def buildClean(): Pheromonas = {
    val r = 0.01
    pheromonas.map(v => if (v < r) 0.0 else v)
  }

  private def buildPath(): List[Path] = Path.build(pheromonas)

}


object SearchSpace {

  def info(sp: SearchSpace, best: Boolean = false, path: String = null)(implicit pl: Plotable): Unit = {
    infoD(sp)
    (sp.dimCities == 2, best) match {
      case (true, true) => sp.plotBest(path)
      case (true, false) => sp.plot(path)
      case _ => ()
    }
  }

  private def infoD(sp: SearchSpace): Unit = {
    val ph = "Pheromonas:\n" ++ sp.pheromonas.toString(10000, 10000)
    val nodes = sp.ref.map { case (i, value) => s"idx: $i --> node: ${value.toString().replace("DenseVector", "")}" }.mkString("\n")
    val allPaths: String = "Paths:\n" ++ sp.paths.toString
    val bestPath = "Best Paths:\n" ++ sp.bestPath.toString
    val inf = Array(ph, "\n", nodes, "", allPaths, "", bestPath).mkString("\n")
    println(inf)
  }

  private def inv(ks: ParIterable[Int]): Unit = {
    require(ks.min == 0, "Index-ref must start by 0")
    require(ks.size - 1 == ks.max, s"Index-ref must end by ${ks.size - 1}")
  }

  def build(w: FuncCoste, refs: RefCities): SearchSpace = new SearchSpace {
    inv(refs.keys)
    override val ref: RefCities = refs
    override lazy val coste: FuncCoste = w
  }

  def build(w: FuncCoste, refs: RefCities, prms: SystemParams): SearchSpace = new SearchSpace {
    inv(refs.keys)
    override val ref: RefCities = refs
    override lazy val coste: FuncCoste = w
    override lazy val params: SystemParams = prms
  }

  def build(w: FuncCoste, refs: RefCities, ph: Pheromonas, mu: Visibility, prms: SystemParams): SearchSpace = new SearchSpace {
    inv(refs.keys)
    override val ref: RefCities = refs
    override lazy val coste: FuncCoste = w
    override lazy val pheromonas: Pheromonas = ph
    override lazy val visibility: Pheromonas = mu
    override lazy val params: SystemParams = prms
  }


  private def initMatrix[T](index: RIndex, mx: DenseMatrix[T], f: Int => Int => T): DenseMatrix[T] = {
    index.foreach(i =>
      index.foreach { j =>
        val wt: T = f(i)(j)
        mx.update(i, j, wt)
        mx.update(j, i, wt)
      })
    mx
  }

  def initVisibl(ref: RefCities): Visibility = {
    val rindex: RIndex = ref.keys.toParArray
    val lnght: Int = rindex.size
    val mu: Visibility = DenseMatrix.zeros(lnght, lnght)
    val dis: Int => Int => Double = CostFunction.invDistance(ref)
    initMatrix(rindex, mu, dis)
  }

  def initPh(coste: FuncCoste, rindex: RIndex): Pheromonas = {
    val lnght: Int = rindex.size
    val ph: Pheromonas = DenseMatrix.zeros(lnght, lnght)
    initMatrix(rindex, ph, coste)
  }


}
