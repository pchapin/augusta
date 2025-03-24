package org.kelseymountain.agc

/**
 * Represents a control flow graph (CFG) as a collection of `CFGNodes`, each identified by the
 * name of the basic block contained in those nodes.
 *
 * @param entry  The name of the entry block in the control flow graph. This is the starting
 * point of the program's control flow or a specific function/method's flow.
 *
 *  @param blocks A map that associates block names with their corresponding `CFGNode` instances.
 *  Each `CFGNode` contains a basic block and details about its successors through labeled edges.
 */
// TODO: Probably `entry` should be an Option[BlockName] to allow for empty graphs.
case class ControlFlowGraph(entry: BlockName, blocks: Map[BlockName, CFGNode])
