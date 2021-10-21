package mf.dabi.aco.algorithm.ant

import breeze.linalg.DenseMatrix
import mf.dabi.aco.algorithm.space._

import scala.annotation.tailrec
import scala.collection.parallel.mutable.ParArray

case class Ant(now: Int,
               rest: RIndex,
               visited: RIndex = ParArray.empty[Int],
               dph: Pheromonas)(implicit sp: SearchSpace)
  extends Avanzable[Ant] with Decidible[Ant] with Trazable[Ant] {
  ant =>

  def travel: Ant = go

  @tailrec
  final protected def go: Ant = {
    if (rest.isEmpty) ant
    else {
      val nx: Int = next
      val nrest: RIndex = rest.filterNot(_ == nx)
      val nvisited: RIndex = nx +: visited
      update(nx)
      //      println(s"nueva hormiga ($nx) Rest = ${nrest.mkString(", ")} Visitados = ${nvisited.mkString(", ")}")
      Ant(nx, nrest, nvisited, dph).go
    }
  }


}

object Ant {
  def init(sp: SearchSpace): Ant =
    Ant(now = 0, rest = sp.index, dph = DenseMatrix.zeros(sp.size, sp.size))(sp)
}


