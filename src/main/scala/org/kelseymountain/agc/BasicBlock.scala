package org.kelseymountain.agc

/**
 * Represents basic blocks. A basic block is a sequence of instructions that has a single entry
 * point and a single exit point at the end of the block. The block is identified by a name,
 * which is used as a label in the generated LLVM IR.
 *
 * At the moment, the contents of the block are the assignment statements from the AST.
 *
 * @param name The name of the label at the start of the basic block.
 * @param instructions A sequence of assignment statements in the block.
 * @param condition An optional condition for the block, which can be used to represent the
 * branching logic of the block.
 */
case class BasicBlock(
  name: BlockName,   // TODO: Is this really needed?
  instructions: Seq[AST.AssignmentStatement] = Seq.empty,
  condition: Option[AST.Expression] = None):

  /**
   * Returns a string representation of the basic block, which includes its name and the
   * instructions it contains.
   */
  override def toString: String =
    val instructionString = instructions.map(_.toString).mkString("\n  ")
    s"BasicBlock($name):\n  $instructionString"
