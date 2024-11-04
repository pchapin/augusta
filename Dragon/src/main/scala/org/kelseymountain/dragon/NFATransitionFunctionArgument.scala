package org.kelseymountain.dragon

/**
  * Instances of this class are arguments to the NFA transition function.
  *
  * @param state The source state. Every NFA uses integers to number the states.
  * @param inputCharacter The input character. The special value \u0000 represents the epsilon
  * symbol.
  */
case class NFATransitionFunctionArgument(state: Int, inputCharacter: Char)
