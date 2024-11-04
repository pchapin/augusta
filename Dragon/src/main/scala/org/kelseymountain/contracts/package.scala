package org.kelseymountain

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
  def requiring[T](condition: => Boolean)(action: => T): T = {
    if (Controller.preconditionsActive) {
      if (!condition) {
        throw new ContractFailureException("Failed precondition")
      }
      else {
        action
      }
    }
    else {
      action
    }
  }

}
