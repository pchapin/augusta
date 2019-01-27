package org.pchapin.augusta

/**
 * This object contains methods that perform various kinds of analysis, typically on the
 * program's CFG.
 */
object Analysis {

  /**
   * Compute the upwardly exposed sets and kill sets for each basic block in the given CFG. This
   * method mutates the basic blocks by decorating them with appropriate sets. It assumes the UE
   * and kill sets are empty initially.
   *
   * @param CFG The control flow graph to process.
   */
  private def computeUEAndKillSets(CFG: ControlFlowGraph): Unit = {
    val ControlFlowGraph(entryBlock, graph, _) = CFG
    // for all someNode in the nodes of the CFG.
    // for all statement in the assignment statements in the basic block in node someNode.
    //   Let x = e be the assignment statement. (x is a variable and e is an expression).
    //   for every variable v in e...
    //      if v is not in the kill set, then add v to the UE set.
    //   add x to the kill set.
    for (someNode  <- graph.innerNodeTraverser(graph get entryBlock);
         statement <- someNode.assignments) {

        // ...
    }
  }

  /**
   * Conducts a liveness analysis on the given CFG.
   *
   * @param CFG A representation of the control flow of the program being analyzed.
   */
  def liveness(CFG: ControlFlowGraph): Unit = {
    computeUEAndKillSets(CFG)

    val ControlFlowGraph(entryBlock, graph, _) = CFG

    // Keep looping until a fixed point is reached.
    var changed = true
    while (changed) {
      changed = false
      for (someNode  <- graph.innerNodeTraverser(graph get entryBlock);
           successor <- someNode.diSuccessors) {

        val oldLive = someNode.live
        // TODO: The last term should be (successor.live intersect (NOT successor.kill))
        someNode.live = someNode.live union (successor.upwardlyExposed union successor.live)
        if (someNode.live != oldLive) changed = true
      }
    }
  }

}
