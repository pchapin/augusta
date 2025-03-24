package org.kelseymountain.agc

/**
 * Represents a graph type class with nodes of type `N` and labeled edges with a label type `L`.
 */
trait Graph[N, L]:
  def nodes: Set[N]
  def successors(node: N): Map[L, N]

/**
 * A given instance of the `Graph` type class that provides a representation of a control flow
 * graph (`ControlFlowGraph`) where nodes are identified by their block names and edges are
 * labeled with `BranchLabel`.
 *
 *
 * This given instance allows the `ControlFlowGraph` to be used generically in contexts that
 * require a `Graph` instance by delegating the graph-related operations to the methods provided
 * here.
 *
 * @param cfg Implicitly provided control flow graph instance, which is required to extract the
 * set of nodes and their successors.
 */
// TODO: Should the Graph use CFGNode as the node type instead of BlockName?
given cfgGraph(using cfg: ControlFlowGraph): Graph[BlockName, BranchLabel] with
  def nodes: Set[BlockName] = cfg.blocks.keySet

  def successors(node: BlockName): Map[BranchLabel, BlockName] =
    cfg.blocks.get(node).map(_.edges).getOrElse(Map.empty)
