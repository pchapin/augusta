package org.kelseymountain.agc

enum BranchLabel:
  case True, False, Unconditional

/**
 * Represents a node in a control flow graph. A node is a basic block together with information
 * about the following blocks. Note that of the eight combinations of branch labels, only three
 * are possible for a node: (True, False), (Unconditional), and ().
 *
 * @param block The basic block that this node represents.
 * @param edges A map of branch labels to the names of the target blocks.
 */
// TODO: Add better checking for invalid combinations of branch labels.
case class CFGNode(block: BasicBlock, edges: Map[BranchLabel, BlockName])
