package org.kelseymountain.agc

object CFGInstances:
  /**
   * A given instance of the `Graph` type class that provides a representation of a control flow
   * graph (`ControlFlowGraph`) where nodes are identified by their block names and edges are
   * labeled with `BranchLabel`.
   *
   *
   * This given instance allows the `ControlFlowGraph` to be used generically in contexts that
   * require a `Graph` instance by delegating the graph-related operations to the methods
   * provided here.
   *
   * @param cfg Implicitly provided control flow graph instance, which is required to extract
   * the set of nodes and their successors.
   */
  given cfgGraph(using cfg: ControlFlowGraph): Graph[BlockName, BranchLabel] with
    def nodes: Set[BlockName] = cfg.blocks.keySet

    def successors(node: BlockName): Map[BranchLabel, BlockName] =
      cfg.edges.getOrElse(node, Map.empty)
