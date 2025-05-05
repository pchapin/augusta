package org.kelseymountain.agc

import scala.collection.mutable

object Analysis:

  type DataFlowSets = mutable.Map[BasicBlock, mutable.Set[IdentifierName]]

  /**
   * Compute the upwardly exposed sets and kill sets for each basic block in the given CFG.
   *
   * @param cfg The control flow graph to process.
   * @return A tuple containing two maps: the first map contains the upwardly exposed sets,
   * and the second map contains the kill sets for each basic block.
   */
  private def computeUEAndKillSets(cfg: Graph[BasicBlock, BranchLabel]): (DataFlowSets, DataFlowSets) =
    val ueSets: DataFlowSets = mutable.Map()
    val killSets: DataFlowSets = mutable.Map()

    // for all someNode in the nodes of the CFG.
    // for all assignment expressions in the basic block someNode.
    //   Let x = e be the assignment expression.
    //   for every variable v in e...
    //      if v is not in the kill set, then add v to the UE set.
    //   add x to the kill set.
    for someNode  <- cfg.nodes do
      for statement <- someNode.instructions do
        ???
      end for
    end for

    // TODO: Process the expression used as a condition at the end of the block.
    (ueSets, killSets)
  end computeUEAndKillSets

  /**
   * Conducts a liveness analysis on the given CFG.
   *
   * @param cfg A representation of the control flow of the program being analyzed.
   */
  def liveness(cfg: Graph[BasicBlock, BranchLabel]): (DataFlowSets, DataFlowSets) =
    val (ueSets, killSets) = computeUEAndKillSets(cfg)

    val liveInSets: DataFlowSets =
      mutable.Map.from(cfg.nodes.map(bb => bb -> mutable.Set.empty[IdentifierName]))

    val liveOutSets: DataFlowSets =
      mutable.Map.from(cfg.nodes.map(bb => bb -> mutable.Set.empty[IdentifierName]))

    // Keep looping until a fixed point is reached.
    var changed = true
    while changed do
      changed = false

      for block <- cfg.nodes do
        val ue     = ueSets(block)
        val kill   = killSets(block)
        val oldIn  = liveInSets(block).toSet
        val oldOut = liveOutSets(block).toSet

        // Compute LIVE_out as the union of LIVE_in of all successors
        val newOut = cfg.successors(block).values
          .flatMap(successor => liveInSets(successor))
          .toSet

        // Compute LIVE_in = UEVar ∪ (LIVE_out \ KILL)
        val newIn = mutable.Set.from(ue)
        newIn ++= newOut
        newIn --= kill

        // Update maps and check for changes
        if newIn != oldIn then
          liveInSets(block) = newIn
          changed = true

        if newOut != oldOut then
          liveOutSets(block) = mutable.Set.from(newOut)
          changed = true
    end while

    // TODO: Return immutable copies?
    (liveInSets, liveOutSets)
  end liveness

end Analysis
