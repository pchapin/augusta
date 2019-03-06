package org.pchapin.dragon

/**
  * This class represents deterministic finite automata. The states of the NFA are numbered with
  * integers starting at stateLower and going to stateUpper. The range of states is contiguous.
  * The first state in the range is the start state and the last state in the range is the final
  * state.
  *
  * DFAs represented by this class contain only a single final state. While this is a restriction
  * over the formal definition of an NFA, it is all that is necessary in this application.
  */
class DFA(
  private val stateLower: Int,
  private val stateUpper: Int,
  private val transitionFunction: Map[DFATransitionFunctionArgument, Int]) {

  /**
    * This method implements a state minimization algorithm (Hopcroft's Algorithm) to minimize
    * the number of states in a DFA.
    *
    * @return An DFA that recognizes the same language but that contains a minimal number of states.
    */
  def minimize: DFA = {
    // TODO: This method body is just a place holder!
    new DFA(stateLower, stateUpper, transitionFunction)
  }

  /**
    * Returns true if this DFA accepts the given text; false otherwise.
    */
  def `match`(text: String): Boolean = {
    var currentState = stateLower;  // The start state.
    for (i <- 0 to text.length()) {
      val argument = DFATransitionFunctionArgument(currentState, text.charAt(i))

      // If there is no explicit transition for this (state, character) input, then make a
      // transition to an implicit error state that is non-accepting and that absorbs all
      // following characters. The text does not match.
      // TODO: Rewrite to eliminate the return.
      if (!transitionFunction.contains(argument)) return false
      currentState = transitionFunction(argument)
    }

    // Are we in the accepting state at the end?
    currentState == stateUpper
  }

}
