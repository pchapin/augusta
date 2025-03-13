package org.kelseymountain.contracts

class ContractFailureException(message: String) extends Exception(message)

/**
 * Potentially executes a precondition check. The check is only evaluated if the Controller
 * objects says to do so.
 *
 * @param condition Boolean expression to test.
 * @param message Message to include in the exception if the condition is false.
 * @param action Block to execute if the condition succeeds.
 *
 * @throws ContractFailureException if the condition is false and precondition evaluation is
 * active.
 */
def requires[T](condition: => Boolean, message: String = "Failed precondition")(action: => T): T =
  if Controller.preconditionsActive && !condition then
    throw new ContractFailureException(message)
  else
    action

extension [T](value: T)
  def ensures(condition: T => Boolean, message: String = "Failed postcondition"): T =
    if Controller.postconditionsActive && !condition(value) then
      throw new ContractFailureException(message)
    else
      value

