package mf.dabi.aco.algorithm.space

trait SystemParams {
  val alpha: Double
  val beta: Double
  val q0: Double
  val rho: Double
}

object SystemParams {
  val default: SystemParams = new SystemParams {
    val alpha: Double = 0.5
    val beta: Double = 0.5
    val q0: Double = 0.5
    val rho: Double = 0.5
  }

  def build(alp: Double = default.alpha,
            bta: Double = default.beta,
            q: Double = default.q0,
            r: Double = default.rho): SystemParams = new SystemParams {

    override val alpha: Double = alp
    override val beta: Double = bta
    override val q0: Double = q
    override val rho: Double = r
  }
}
