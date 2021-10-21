package mf.dabi.aco.algorithm.space

import breeze.linalg.DenseVector
import breeze.plot._
import mf.dabi.aco.algorithm.space.journey.Travels

import java.nio.file.{Files, Paths}
import scala.util.{Random, Try}

/** Experimental: queda pendiente incorporar una dependencia grafica para plotear
 * en Scala excasean, y las que existen no tienen mucha documentacion */
trait Plotable {

  self =>
  private lazy val f: Figure = Figure()
  private var p: Plot = f.subplot(0)

  def labels(xLabel: String, yLabel: String): Plotable = {
    p.xlabel = xLabel
    p.ylabel = yLabel
    self
  }

  def rndmColor(oth: Boolean = false): String = {
    def plt: Int = Random.between(0, 255)

    if (!oth) s"$plt,$plt,$plt" else s"0,$plt,$plt"
  }

  def set(trvl: Travels, sp: SearchSpace): Plotable = {
    val phts: Seq[List[Int]] = trvl.pathss.map(_.traze)
    val points: Array[Array[DenseVector[Double]]] = phts.map(pt => pt.map(i => sp.ref(i)).toArray).toArray
    if (trvl.pathss.length == 1) set(points.head)
    else points.foldLeft(set(points.head)) { case (plt, pnts) => plt.set(pnts, rndmColor(true)) }
  }

  def set(sp: SearchSpace): Plotable = {
    val trvl: Travels = sp.paths
    set(trvl, sp)
  }

  def set(points: Array[DenseVector[Double]], color: String = "red"): Plotable = {
    val xvalues = points.map(v => v(0))
    val yvalues = points.map(v => v(1))
    val axisX = DenseVector(xvalues)
    val axisY = DenseVector(yvalues)
    //    val c = new Color(10, 10, 10, 10)
    val newPlot =
      p +=
        plot(axisX, axisY, '.', "black", shapes = true) +=
        plot(axisX, axisY, colorcode = color)
    p = newPlot
    self
  }

  def render(name: String): Unit = f.saveas(s"src/main/resources/plot/$name.png")

  def renderPath(path: String, name: String): Unit = {
    def create: Unit = Files.createDirectories(Paths.get(path))

    def save: Unit = f.saveas(s"$path/$name.png")

    Try(save).getOrElse {
      create
      save
    }
  }

}

object Plotable {
  implicit val default: Plotable = new Plotable {}
}