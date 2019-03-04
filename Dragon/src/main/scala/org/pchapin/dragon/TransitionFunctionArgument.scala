package org.pchapin.dragon

/**
  * Instances of this class are arguments to the NFA transition function.
  *
  * @param state Every NFA uses integers to number the states.
  * @param inputCharacter The special value \u0000 represents the epsilon symbol.
  */
case class TransitionFunctionArgument(state: Int, inputCharacter: Char)
