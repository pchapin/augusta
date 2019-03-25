package org.pchapin.augusta

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter, PrintWriter}
import org.antlr.v4.runtime.tree._
import org.slem.IRTree._

// TODO: Currently this is just a skeleton!
class LLVMGenerator(symbolTable: SymbolTable, reporter: Reporter) extends AdaBaseVisitor[L_Node] {
  private val output = new PrintWriter(
    new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.ll"), "US-ASCII"))
  )
  private var expressionLevel = 0

  override def visitCompilation_unit(ctx: AdaParser.Compilation_unitContext): L_Node = {
    output.println("Hello, World!")
    output.println("")

    visitChildren(ctx)
    output.close()
    null
  }


  override def visitPrimary_expression(ctx: AdaParser.Primary_expressionContext): L_Node = {
    if (ctx.expression != null) {
      visit(ctx.expression)
    }
    else if (ctx.IDENTIFIER != null) {
      val name = ctx.IDENTIFIER.getText
      // TODO: Actually the allocation needs to be looked up in the symbol table.
      L_Load(L_PointerType(L_IntType(64)), L_Alloca(L_IntType(64)))
    }
    else if (ctx.NUMERIC_LITERAL != null) {
      visitTerminal(ctx.NUMERIC_LITERAL)
    }
    else {
      visitTerminal(ctx.BOOLEAN_LITERAL)
    }
  }


  override def visitMultiplicative_expression(ctx: AdaParser.Multiplicative_expressionContext): L_Node =  {
    if (ctx.MULTIPLY != null) {
      val leftSubexpression  = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.primary_expression).asInstanceOf[L_Value]

      L_Add(leftSubexpression, rightSubexpression)
    }
    else if (ctx.DIVIDE != null) {
      val leftSubexpression  = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.primary_expression).asInstanceOf[L_Value]

      L_Sub(leftSubexpression, rightSubexpression)
    }
    else {
      visit(ctx.primary_expression).asInstanceOf[L_Value]
    }
  }


  override def visitUnary_expression(ctx: AdaParser.Unary_expressionContext): L_Node = {
    val subexpression = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]

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


  override def visitAdditive_expression(ctx: AdaParser.Additive_expressionContext): L_Node = {
    if (ctx.PLUS != null) {
      val leftSubexpression  = visit(ctx.additive_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.unary_expression).asInstanceOf[L_Value]

      L_Add(leftSubexpression, rightSubexpression)
    }
    else if (ctx.MINUS != null) {
      val leftSubexpression  = visit(ctx.additive_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.unary_expression).asInstanceOf[L_Value]

      L_Sub(leftSubexpression, rightSubexpression)
    }
    else {
      visit(ctx.unary_expression).asInstanceOf[L_Value]
    }
  }


  override def visitTerminal(node: TerminalNode): L_Node = {
    try {
      if (expressionLevel > 0) {
        node.getSymbol.getType match {
          case AdaLexer.IDENTIFIER  =>
            output.print(node.getText)

          case AdaLexer.NUMERIC_LITERAL =>
            output.print(Literals.convertIntegerLiteral(node.getText))

          case _ =>
            // Do nothing.
        }
      }
    }
    catch {
      // This exception should normally never arise if illegal literals are ruled out during
      // semantic analysis. However, literal analysis is currently not being done there.
      // TODO: Check literal format during semantic analysis.
      case ex: InvalidLiteralFormatException =>
        printf("[Line: %3d, Column: %3d] %s\n",
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
    null
  }
}
