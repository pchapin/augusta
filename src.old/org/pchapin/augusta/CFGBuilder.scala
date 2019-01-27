package org.pchapin.augusta

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.LDiEdge

class CFGBuilder(
  symbolTable: SymbolTable,
  reporter   : Reporter) extends AdaBaseVisitor[ControlFlowGraph] {

  import scala.collection.JavaConverters._    // ctx is a java.util.List, not a scala.List.

  private def combineStatementSequence(
    statements: Iterable[AdaParser.StatementContext]): ControlFlowGraph = {

    val graphList = statements map { visit(_) }
    graphList reduce { (left: ControlFlowGraph, right: ControlFlowGraph) =>
      (left, right) match {
        case (ControlFlowGraph(leftEntry, leftGraph, leftExit), ControlFlowGraph(rightEntry, rightGraph, rightExit)) =>
          ControlFlowGraph(leftEntry, (leftGraph union rightGraph) + LDiEdge(leftExit, rightEntry)('U'), rightExit)
      }
    }
  }

  override def visitProcedure_definition(
    ctx: AdaParser.Procedure_definitionContext): ControlFlowGraph = {
    // TODO: Initialized declarations should be the first basic block of the procedure's CFG.
    visit(ctx.block)
  }


  override def visitBlock(ctx: AdaParser.BlockContext): ControlFlowGraph = {
    combineStatementSequence(ctx.statement.asScala)
  }


  override def visitAssignment_statement(
    ctx: AdaParser.Assignment_statementContext): ControlFlowGraph = {

    val primitiveBlock = new BasicBlock(List(ctx), None)
    ControlFlowGraph(primitiveBlock, Graph[BasicBlock, LDiEdge](primitiveBlock), primitiveBlock)
  }


  // TODO: Implement the CFG construction of conditional statements.
  // override def visitConditional_statement(
  //   ctx: RabbitParser.Conditional_statementContext): ControlFlowGraph = {
  // }


  override def visitIteration_statement(
    ctx: AdaParser.Iteration_statementContext): ControlFlowGraph = {

    val expressionBlock = new BasicBlock(List(), Some(ctx.expression))
    val nullBlock = new BasicBlock(List(), None)
    val ControlFlowGraph(bodyEntry, bodyGraph, bodyExit) =
      combineStatementSequence(ctx.statement.asScala)
    val allNodesGraph = Graph[BasicBlock, LDiEdge](expressionBlock, nullBlock) union bodyGraph
    val overallGraph = allNodesGraph +
      LDiEdge(expressionBlock, bodyEntry)('T') +
      LDiEdge(expressionBlock, nullBlock)('F') +
      LDiEdge(bodyExit, expressionBlock)('U')
    ControlFlowGraph(expressionBlock, overallGraph, nullBlock)
  }


  override def visitNull_statement(
    ctx: AdaParser.Null_statementContext): ControlFlowGraph = {

    val nullBlock = new BasicBlock(List(), None)
    ControlFlowGraph(nullBlock, Graph[BasicBlock, LDiEdge](nullBlock), nullBlock)
  }

}


object CGFBuilder {

  /**
   * Method that optimizes the CFG by 1) removing all possible null blocks, and 2) combining
   * blocks when possible to eliminate or minimize the number of blocks containing just one
   * assignment statement.
   *
   * @param CFG The control flow graph to optimize.
   * @return The optimized control flow graph.
   */
  def optimize(CFG: ControlFlowGraph): ControlFlowGraph = {
    // TODO: Implement CFG optimization.
    CFG
  }

}
