package org.kelseymountain.agc

import AST.*

object CFG:

  def build(ast: ASTNode): ControlFlowGraph =
    ast match
      case statement @ AssignmentStatement(_, _, _) =>
        val blockName = "FIXME" // Placeholder for block name
        val block = BasicBlock(blockName, Seq(statement))
        ControlFlowGraph(
          entry = blockName,
          blocks = Map(blockName -> block),
          edges = Map.empty,  // No edges in a single block
          exit = blockName    // Exit is the same as entry in this case
        )

      case statement @ WhileStatement(_, _, _) =>
        val conditionCFG = build(statement.condition)
        val bodyCFG = build(statement.body)
        val afterBlockName = "FIXME" // Placeholder for block name
        val afterBlock = BasicBlock(afterBlockName)
        val newEdges = Map(
            conditionCFG.exit -> Map(BranchLabel.True -> bodyCFG.entry, BranchLabel.False -> afterBlockName),
            bodyCFG.exit -> Map(BranchLabel.Unconditional -> conditionCFG.entry)
          ) ++
          conditionCFG.edges ++
          bodyCFG.edges
        ControlFlowGraph(
          entry = conditionCFG.entry,
          blocks = conditionCFG.blocks ++ bodyCFG.blocks + (afterBlockName -> afterBlock),
          edges = newEdges,
          exit = afterBlockName
        )

      case _ => ???
    end match

  end build
