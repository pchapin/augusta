package org.pchapin.augusta

/**
 * A representation of basic blocks. Here the assignment statements are "full" statements with
 * arbitrary expressions on the right hand side (and not, for example, SSA form assignments).
 * Similarly the expression used at the end of the basic block is an arbitrary expression.
 *
 * @param assignments A list of Augusta assignment statements forming the basic block.
 * @param condition The condition at the end of the basic block or None if the block ends with
 * an unconditional jump.
 * @param upwardlyExposed The set of variables used in this block before being defined.
 * @param killed The set of variables defined in this block.
 * @param live The set of variables that are live at the end of this block.
 */
case class BasicBlock(
  assignments: List[AdaParser.Assignment_statementContext],
  condition  : Option[AdaParser.ExpressionContext],
  var upwardlyExposed: Set[String] = Set(),
  var killed         : Set[String] = Set(),
  var live           : Set[String] = Set())
