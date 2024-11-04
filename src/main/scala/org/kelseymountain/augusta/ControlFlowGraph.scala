package org.kelseymountain.augusta

import scalax.collection.Graph
import scalax.collection.edge.LDiEdge

/**
 * A control flow graph always has an entry block and an exit block. "Null" blocks that contain
 * no statements are used as placeholders to simplify the construction of the CFG; they may or
 * may not be optimized away. Graph edges are labeled with 'U' for unconditional branches or 'T'
 * (true), 'F' (false) for conditional ones.
 *
 * Pre: entryBlock and exitBlock are both nodes in the graph. In the graph there are at most two
 * edges away from a node. If a node has only one exit edge it must be 'U'; if it has two exit
 * edges one must be 'T' and the other 'F'. If the entry block is the Null block, then the graph
 * contains only a single node consisting of the Null block.
 *
 * @param entryBlock The block at the entry point of the CFG. It could be a Null block.
 * @param graph The CFG itself.
 * @param exitBlock The block at the exit point of the CFG. It could be a Null block.
 */
case class ControlFlowGraph(
  entryBlock: BasicBlock, graph: Graph[BasicBlock, LDiEdge], exitBlock: BasicBlock)
