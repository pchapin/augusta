package org.kelseymountain.contracts

/**
 * Object that allows control and configuration of contract behaviors. Various aspects of the
 * contracts can be changed at runtime by calling the methods of this object.
 */
object Controller:
  private[contracts] var preconditionsActive: Boolean = true
  private[contracts] var postconditionsActive: Boolean = true

  /**
   * Control the evaluation of preconditions. The default behavior is for preconditions to be
   * evaluated.
   *
   * @param state If true, then preconditions are evaluated; otherwise they are not evaluated. In
   * the later case the associated block is always executed.
   */
  def preconditionsEvaluated(state: Boolean): Unit =
    preconditionsActive = state

  /**
   * Control the evaluation of postconditions. The default behavior is for postconditions to be
   * evaluated.
   *
   * @param state If true, then postconditions are evaluated; otherwise they are not evaluated.
   */
  def postconditionsEvaluated(state: Boolean): Unit =
    postconditionsActive = state

end Controller
