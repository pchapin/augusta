//-----------------------------------------------------------------------
// FILE    : Controller.scala
// SUBJECT : Object containing methods that allow configuration of contract behaviors.
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package org.pchapin.contracts

/**
 * Object that allows control and configuration of contract behaviors. Various aspects of the
 * contracts can be changed at runtime by calling the methods of this object.
 */
object Controller {
  private[contracts] var preconditionsActive: Boolean = true

  /**
   * Control the evaluation of preconditions. The default behavior is for preconditions to be
   * evaluated.
   *
   * @param state If true then preconditions are evaluated; otherwise they are not evaluated. In
   * the later case the associated block is always executed.
   */
  def preconditionsEvaluated(state: Boolean) {
    preconditionsActive = state
  }
}
