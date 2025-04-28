package org.kelseymountain.agc

/**
 * Represents a control flow graph (CFG) as a collection of `BasicBlock`, each identified by its
 * name. The graph also contains edges that represent the flow of control between these blocks.
 *
 * @param entry The name of the entry block in the control flow graph. This is the starting
 * point of the program's or for a specific function/method's flow.
 *
 *  @param blocks A map that associates block names with their corresponding `BasicBlock`
 *  instances.
 *
 * @param edges A map that associates block names with their outgoing labeled edges.
 *
 *  @param exit The name of the exit block in the control flow graph.
 */
case class ControlFlowGraph(
  entry: BlockName,
  blocks: Map[BlockName, BasicBlock],
  edges: Map[BlockName, Map[BranchLabel, BlockName]],
  exit: BlockName)
