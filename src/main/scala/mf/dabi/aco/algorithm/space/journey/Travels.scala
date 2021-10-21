package mf.dabi.aco.algorithm.space.journey

case class Travels(pathss: Path*) {
  override def toString: String =
    pathss.map(p => p.toString).mkString("\n")
}