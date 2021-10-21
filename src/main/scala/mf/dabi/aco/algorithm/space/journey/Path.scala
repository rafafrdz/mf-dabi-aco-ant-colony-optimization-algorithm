package mf.dabi.aco.algorithm.space.journey

import breeze.linalg.DenseMatrix
import mf.dabi.aco.algorithm.ant.Trazable

import scala.annotation.tailrec

case class Path(traze: List[Int], peso: Double) {
  override def toString: String = traze.mkString(" -> ") ++ " " ++ s"(peso: $peso)"
}

object Path {
  private[space] def build(matrix: DenseMatrix[Double]): List[Path] = {
    val index = 0 until matrix.rows

    @tailrec
    def aux2(iM: Int, mtxM: DenseMatrix[Double], pathM: List[Path]): List[Path] = {
      if (mtxM.forall(d => d == 0)) pathM
      else {
        val (pth, mxT) = aux(iM, mtxM, Nil, 0.0)
        val last = pth.traze.last
        val salt = index.map(j => mtxM(last, j)).forall(d => d == 0)
        val k2 = index.flatMap(i1 => index.filter(j1 => mtxM(i1, j1) != 0)).head
        val k = if (salt) k2 else last
        aux2(k, mxT, pathM :+ pth)
      }
    }

    @tailrec
    def aux(i: Int, mtx: DenseMatrix[Double], path: List[Int], peso: Double): (Path, DenseMatrix[Double]) = {
      if (mtx.forall(d => d == 0) || path.contains(i)) (Path(path, peso), mtx)
      else {
        val row: Seq[(Int, Double)] = index.map(j => (j, mtx(i, j)))
        val (mxj, vmx): (Int, Double) = row.reduce[(Int, Double)] { case ((ic, acc), (iz, vz)) => if (acc < vz) (iz, vz) else (ic, acc) }
        val mxN = mtx.copy
        Trazable.replace(mxN)(i, mxj, 0)
        aux(mxj, mxN, path :+ i, peso + vmx)
      }
    }

    aux2(0, matrix, Nil)
  }
}
