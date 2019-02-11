//-----------------------------------------------------------------------
// FILE    : package.scala
// SUBJECT : The package object for edu.vtc.contracts
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package org.pchapin

/**
 * Object to wrap various contract methods and other related materials.
 */
package object contracts {

  class ContractFailureException(message: String) extends Exception(message)

  /**
   * Potentially executes a precondition check. The check is only evaluated if the Controller
   * objects says to do so.
   *
   * @param condition Boolean expression to test.
   * @param action Block to execute if the condition succeeds.
   *
   * @throws ContractFailureException if the condition is false and precondition evaluation is
   * active.
   */
  def requiring[T](condition: => Boolean)(action: => T) = {
    Controller.preconditionsActive match {
      case false => action
      case true  =>
        if (!condition) {
          throw new ContractFailureException("Failed precondition")
        }
        else {
          action
        }
    }
  }

}
