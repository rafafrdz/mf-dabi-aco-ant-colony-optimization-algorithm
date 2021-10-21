package mf.dabi.aco.algorithm

import mf.dabi.aco.algorithm.ant.Ant
import mf.dabi.aco.algorithm.space._

import scala.annotation.tailrec
import scala.collection.parallel.mutable.ParArray


object AntColonyOptimization {

  def algorithm(sp: SearchSpace, colonySize: Int, journeys: Int): SearchSpace = {
    @tailrec
    def aux(ss: SearchSpace, jrn: Int): SearchSpace = {
      if (jrn == 0) ss
      else {
        val ants: Colony = ParArray.fill(colonySize)(Ant.init(ss))
        val antsTravelers: Colony = ants.map(ant => ant.travel)
        val ssUp: SearchSpace = ss.update(antsTravelers)
        aux(ssUp, jrn - 1)
      }
    }

    aux(sp, journeys)
  }

}
