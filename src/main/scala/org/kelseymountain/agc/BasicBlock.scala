package org.kelseymountain.agc

/**
 * Represents basic blocks. A basic block is a sequence of instructions that has a single entry
 * point and a single exit point at the end of the block. The block is identified by a name,
 * which is used as a label in the generated LLVM IR.
 *
 * At the moment, the contents of the block are the assignment statements from the parse tree.
 * This will need to be changed later.
 *
 * @param name The name of the label at the start of the basic block.
 * @param instructions A sequence of assignment statements in the block.
 */
// TODO: Change the type of `instructions` to a more appropriate type.
case class BasicBlock(name: BlockName, instructions: Seq[AugustaParser.Assignment_statementContext])
