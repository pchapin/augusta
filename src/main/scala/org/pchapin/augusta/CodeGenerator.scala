package org.pchapin.augusta

import scala.collection.mutable
import org.slem.IRTree._

class CodeGenerator(CFG: ControlFlowGraph, symbolTable: SymbolTable, reporter: Reporter) {

  type LocationMap = mutable.Map[String, L_Alloca]

  /**
    * Creates a block of L_Alloca instructions based on the variables mentioned in the given
    * symbol table.
    *
    * @return The first block of the LLVM AST along with a map that associates variable names
    * with specific locations (L_Alloca instructions).
    */
  val (initialBlock, locationMap) = {
    // TODO: Implement this initialization!
    (L_Block(List(), L_Ret(0: Long), L_Label("prologue")), mutable.Map())
  }

  /**
    * This objects computes LLVM ASTs for various expression forms. It is used when processing
    * the statements in the various basic blocks of a subprogram definition.
    */
  object LLVMGenerator {

    def visit(ctx: AdaParser.Assignment_statementContext): L_Store = {
      val rightHandSide = visit(ctx.expression)
      // TODO: Actually, the allocation needs to be looked up in the location map.
      L_Store(rightHandSide, L_Alloca(L_IntType(64)))
    }


    def visit(ctx: AdaParser.Primary_expressionContext): L_Value = {
      if (ctx.expression != null) {
        visit(ctx.expression)
      }
      else if (ctx.IDENTIFIER != null) {
        val name = ctx.IDENTIFIER.getText
        // TODO: Actually, the allocation needs to be looked up in the symbol table.
        L_Load(L_PointerType(L_IntType(64)), L_Alloca(L_IntType(64)))
      }
      else if (ctx.NUMERIC_LITERAL != null) {
        try {
          L_Int(64, Literals.convertIntegerLiteral(ctx.NUMERIC_LITERAL.getText))
        }
        catch {
          // This exception should normally never arise if illegal literals are ruled out during
          // semantic analysis. However, literal analysis is currently not being done there.
          case ex: InvalidLiteralFormatException =>
            reporter.reportError(
              ctx.NUMERIC_LITERAL.getSymbol.getLine,
              ctx.NUMERIC_LITERAL.getSymbol.getCharPositionInLine + 1,
              ex.getMessage)
            L_Int(64, 0: Long)
        }
      }
      else {
        ctx.BOOLEAN_LITERAL.getText match {
          case "true"  => L_Boolean(true)
          case "false" => L_Boolean(false)
        }
      }
    }


    def visit(ctx: AdaParser.Multiplicative_expressionContext): L_Value =  {
      if (ctx.MULTIPLY != null) {
        val leftSubexpression  = visit(ctx.multiplicative_expression)
        val rightSubexpression = visit(ctx.primary_expression)

        L_Mul(leftSubexpression, rightSubexpression)
      }
      else if (ctx.DIVIDE != null) {
        val leftSubexpression  = visit(ctx.multiplicative_expression)
        val rightSubexpression = visit(ctx.primary_expression)

        L_SDiv(leftSubexpression, rightSubexpression)
      }
      else if (ctx.REM != null) {
        val leftSubexpression  = visit(ctx.multiplicative_expression)
        val rightSubexpression = visit(ctx.primary_expression)

        L_SRem(leftSubexpression, rightSubexpression)
      }
      else {
        visit(ctx.primary_expression)
      }
    }


    def visit(ctx: AdaParser.Unary_expressionContext): L_Value = {
      val subexpression = visit(ctx.multiplicative_expression)

      if (ctx.PLUS != null) {
        subexpression
      }
      else if (ctx.MINUS != null) {
        L_Sub(0: Long, subexpression)
      }
      else {
        subexpression
      }
    }


    def visit(ctx: AdaParser.Additive_expressionContext): L_Value = {
      if (ctx.PLUS != null) {
        val leftSubexpression  = visit(ctx.additive_expression)
        val rightSubexpression = visit(ctx.unary_expression)

        L_Add(leftSubexpression, rightSubexpression)
      }
      else if (ctx.MINUS != null) {
        val leftSubexpression  = visit(ctx.additive_expression)
        val rightSubexpression = visit(ctx.unary_expression)

        L_Sub(leftSubexpression, rightSubexpression)
      }
      else {
        visit(ctx.unary_expression)
      }
    }


    def visit(ctx: AdaParser.Relational_expressionContext): L_Value = {
      // TODO: Finish me!
      visit(ctx.additive_expression)
    }


    def visit(ctx: AdaParser.ExpressionContext): L_Value = {
      // TODO: Finish me!
      visit(ctx.relational_expression)
    }

  }


  def makeAST: L_FunctionDefinition = {

    def makeBasicBlockAST(block: BasicBlock): L_Block = {
      val assignments = for (statement <- block.assignments) yield {
        LLVMGenerator.visit(statement)
      }
      // TODO: Fix the terminator instruction and label.
      L_Block(assignments, L_Ret(0: Long), "labelMe")
    }

    val generatedBlocks = for (node <- CFG.graph.nodes) yield makeBasicBlockAST(node)
    L_FunctionDefinition(L_VoidType(), initialBlock :: generatedBlocks.toList, "main")
  }

}
