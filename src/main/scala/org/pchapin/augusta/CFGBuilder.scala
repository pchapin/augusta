package org.pchapin.augusta

import scala.collection.JavaConverters._    // ctx is a java.util.List, not a scala.List.
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.LDiEdge

class CFGBuilder(
  symbolTable: SymbolTable,
  reporter   : Reporter) extends AdaBaseVisitor[ControlFlowGraph] {

  /**
    * Combines statements in a straight line statement sequence to create a CFG where the CFG
    * for each of the statements are connected in order. Note that the statements in the
    * sequence need not be simple statements. Entire CFGs are combined. However, if the sequence
    * contains only assignment statements, no attempt is made to put all assignment statements
    * into the same basic block. That can potentially be done later as an optimization.
    *
    * @param statements An iterable collection containing the statement sequence to combine.
    * @return An overall, non-optimized CFG built from the CFGs of the individual statements.
    */
  private def combineStatementSequence(
    statements: Iterable[AdaParser.StatementContext]): ControlFlowGraph = {
    // TODO: Think about combining sequential assignment statements into one basic block.

    val graphList = statements map { visit(_) }
    graphList reduce { (left: ControlFlowGraph, right: ControlFlowGraph) =>
      (left, right) match {
        case (ControlFlowGraph(leftEntry,  leftGraph,  leftExit),
              ControlFlowGraph(rightEntry, rightGraph, rightExit)) =>

          // Make a CFG by combining the left and right CFGs in a straight line.
          ControlFlowGraph(
            leftEntry,
            (leftGraph union rightGraph) + LDiEdge(leftExit, rightEntry)('U'),
            rightExit)
      }
    }
  }


  override def visitProcedure_definition(
    ctx: AdaParser.Procedure_definitionContext): ControlFlowGraph = {
    // TODO: Initialized declarations should form the first basic block of the procedure's CFG.
    visit(ctx.block)
  }


  override def visitBlock(ctx: AdaParser.BlockContext): ControlFlowGraph = {
    combineStatementSequence(ctx.statement.asScala)
  }


  override def visitAssignment_statement(
    ctx: AdaParser.Assignment_statementContext): ControlFlowGraph = {

    val primitiveBlock = BasicBlock(List(ctx), None)
    ControlFlowGraph(primitiveBlock, Graph[BasicBlock, LDiEdge](primitiveBlock), primitiveBlock)
  }


  // This method doesn't handle ELSIF clauses. See my comment in Ada.g4 about this.
  // TODO: Handle ELSIF clauses properly.
  override def visitConditional_statement(
     ctx: AdaParser.Conditional_statementContext): ControlFlowGraph = {

    // Create additional blocks and sub-graphs as needed.
    val expressionBlock = BasicBlock(List(), Some(ctx.expression))
    val emptyBlock = BasicBlock(List(), None)
    val ControlFlowGraph(thenBodyEntry, thenBodyGraph, thenBodyExit) =
      combineStatementSequence(ctx.thenStatements.asScala)

    val overallGraph = if (ctx.ELSE == null) {
      // Collect all the additional blocks and sub-graphs into a common structure.
      val allNodesGraph =
        Graph[BasicBlock, LDiEdge](expressionBlock, emptyBlock) union thenBodyGraph

      // Connect the additional blocks as appropriate.
      allNodesGraph +
        LDiEdge(expressionBlock, thenBodyEntry)('T') +
        LDiEdge(expressionBlock, emptyBlock)('F') +
        LDiEdge(thenBodyExit, emptyBlock)('U')
    }
    else {
      val ControlFlowGraph(elseBodyEntry, elseBodyGraph, elseBodyExit) =
        combineStatementSequence(ctx.elseStatements.asScala)

      // Collect all the additional blocks and sub-graphs into a common structure.
      val allNodesGraph =
        Graph[BasicBlock, LDiEdge](expressionBlock, emptyBlock) union thenBodyGraph union elseBodyGraph

      // Connect the additional blocks as appropriate.
      allNodesGraph +
        LDiEdge(expressionBlock, thenBodyEntry)('T') +
        LDiEdge(expressionBlock, elseBodyEntry)('F') +
        LDiEdge(thenBodyExit, emptyBlock)('U') +
        LDiEdge(elseBodyExit, emptyBlock)('U')
    }

    // Return the overall CFG with properly specified entry and exit blocks.
    ControlFlowGraph(expressionBlock, overallGraph, emptyBlock)
  }


  override def visitIteration_statement(
    ctx: AdaParser.Iteration_statementContext): ControlFlowGraph = {

    // Create additional blocks and sub-graphs as needed.
    val expressionBlock = BasicBlock(List(), Some(ctx.expression))
    val emptyBlock = BasicBlock(List(), None)
    val ControlFlowGraph(bodyEntry, bodyGraph, bodyExit) =
      combineStatementSequence(ctx.statement.asScala)

    // Collect all the additional blocks and sub-graphs into a common structure.
    val allNodesGraph = Graph[BasicBlock, LDiEdge](expressionBlock, emptyBlock) union bodyGraph

    // Connect the additional blocks as appropriate.
    val overallGraph = allNodesGraph +
      LDiEdge(expressionBlock, bodyEntry)('T') +
      LDiEdge(expressionBlock, emptyBlock)('F') +
      LDiEdge(bodyExit, expressionBlock)('U')

    // Return the overall CFG with properly specified entry and exit blocks.
    ControlFlowGraph(expressionBlock, overallGraph, emptyBlock)
  }


  override def visitNull_statement(
    ctx: AdaParser.Null_statementContext): ControlFlowGraph = {

    val emptyBlock = BasicBlock(List(), None)
    ControlFlowGraph(emptyBlock, Graph[BasicBlock, LDiEdge](emptyBlock), emptyBlock)
  }

}


object CFGBuilder {

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


  /**
    * Method that traverses the CFG and creates labels for each basic block in the CFG. These
    * labels are used during code generation as the target labels for each basic block, for
    * example when generating assembly language.
    * 
    * It is important that this method start the label numbering at one and not zero. This is
    * because the label "block0" is treated in a special way in the code generator. It is
    * reserved for the prologue block of a subprogram where local variables are allocated.
    * 
    * @param CFG The control flow graph to label.
    * @return The labeled CFG.
    */
  def label(CFG: ControlFlowGraph): ControlFlowGraph = {
    var blockNumber = 1
    val ControlFlowGraph(entryBlock, graph, _) = CFG
    val initialInnerNode = graph get entryBlock
    for (node <- initialInnerNode.outerNodeTraverser) {
      node.label = "block" + blockNumber
      blockNumber += 1
    }
    CFG
  }

}

