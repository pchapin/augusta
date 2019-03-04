package org.pchapin.dragon

/**
  * This class represents nondeterministic finite automata. The states of the NFA are numbered
  * with integers starting at stateLower and going to stateUpper. The range of states is
  * contiguous. The first state in the range is the start state and the last state in the range
  * is the final state.
  *
  * NFAs represented by this class contain only a single final state. While this is a restriction
  * over the formal definition of an NFA, it is all that is necessary in this application. The
  * restriction does not restrict expressiveness; for an NFA with multiple final states, a new
  * NFA could be built that has a single final state reachable from the original final states
  * via epsilon transitions.
  */
class NFA(
  private val stateLower: Int,
  private val stateUpper: Int,
  private val transitionFunction: Map[TransitionFunctionArgument, Set[Int]]) {

  import NFA._

  /**
    * Returns an NFA that is the concatenation of this NFA followed by the other NFA. Neither
    * NFA input to this operation is modified.
    */
  def concatenate(other: NFA): NFA = {
    val secondEntry = stateUpper + 1
    val newLower = stateLower
    val newUpper = stateUpper + (other.stateUpper - other.stateLower + 1)
    var newTransition = transitionFunction

    // Copy the other transition function into newTransition, modifying the state numbers.
    for ((oldArgument, oldResult) <- other.transitionFunction) {
      // Convert state numbers in the other NFA's transition function arguments.
      val newArgument =
        TransitionFunctionArgument(
          oldArgument.state - other.stateLower + secondEntry,
          oldArgument.inputCharacter)

      // Convert state numbers in the other NFA's transition function result sets.
      val newResult = oldResult map { state => state - other.stateLower + secondEntry }
      newTransition = newTransition + (newArgument -> newResult)
    }

    // Add an epsilon transition between the original final state and the other start state.
    // TODO: This will overwrite any existing epsilon transition from stateUpper!
    val extraArgument = TransitionFunctionArgument(stateUpper, '\u0000')
    var extraResult = Set[Int](secondEntry)
    newTransition = newTransition + (extraArgument -> extraResult)

    // Create the new NFA.
    new NFA(newLower, newUpper, newTransition)
  }


  /**
    * Returns an NFA that is the union of this NFA followed and the other NFA. Neither NFA
    * input to this operation is modified.
    */
  def union(other: NFA): NFA = {
    // TODO: This method body is just a place holder!
    new NFA(stateLower, stateUpper, transitionFunction)
  }


  /**
    * Returns an NFA that is the Kleene closure of this NFA. This NFA is not modified.
    */
  def kleeneClosure: NFA = {
    // TODO: This method body is just a place holder!
    new NFA(stateLower, stateUpper, transitionFunction)
  }


  /**
    * Returns true if this NFA is really a DFA (no use of epsilon transitions and only a single
    * state as the target of each transition.
    */
  def isDFA: Boolean = {
    for ((key: TransitionFunctionArgument, value: Set[Int]) <- transitionFunction) {
      // Make sure each set has exactly one element.
      if (value.size != 1) return false

      // Make sure no epsilon transitions occur.
      if (key.inputCharacter == '\u0000') return false
    }
    true
  }


  /**
    * Returns a DFA obtained from Subset Construction on this NFA. Note that the return value
    * will be such that isDFA() is true.
    */
  def toDFA: NFA = {
    // TODO: This method body is just a place holder!
    new NFA(stateLower, stateUpper, transitionFunction)
  }


  /**
    * This method implements a state minimization algorithm (Hopcroft's Algorithm) to minimize
    * the number of states in a DFA. Note that the "NFA" must be a DFA (that is, isDFA must
    * return 'true') before this method is called.
    *
    * @return An DFA that recognizes the same language but that contains a minimal number of states.
    */
  def minimize: NFA = {
    // TODO: This method body is just a place holder!
    new NFA(stateLower, stateUpper, transitionFunction)
  }


  /**
    * Returns true if this NFA accepts the given text; false otherwise.
    */
  def `match`(text: String): Boolean = {
    if (!isDFA) {
      throw new SimulationException("Simulating an NFA without conversion to a DFA")
    }

    var currentState = stateLower;  // The start state.
    for (i <- 0 to text.length()) {
      val argument = TransitionFunctionArgument(currentState, text.charAt(i))

      // If there is no explicit transition for this (state, character) input, then make
      // a transition to an implicit error state that is non-accepting and that absorbs
      // all following characters. The text does not match.
      if (!transitionFunction.contains(argument)) return false

      // The result of the transition function must have exactly one state.
      val nextStateSet = transitionFunction.get(argument)
      val it = nextStateSet.iterator
      //currentState = it.next()
    }

    // Are we in the accepting state at the end?
    currentState == stateUpper
  }

}


object NFA {
  class SimulationException(message: String) extends Exception(message)
}
