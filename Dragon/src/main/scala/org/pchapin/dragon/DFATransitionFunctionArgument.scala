package org.pchapin.dragon

/**
  * Instances of this class are arguments to the DFA transition function.
  *
  * @param state The source state. Every DFA uses integers to number the states.
  * @param inputCharacter The input character. Avoid using '\u0000' since that might cause
  * conflict in situations where NFAs and DFAs are used together. (The input character of
  * '\u0000' has a special meaning to the NFA class)
  */
case class DFATransitionFunctionArgument(state: Int, inputCharacter: Char)
