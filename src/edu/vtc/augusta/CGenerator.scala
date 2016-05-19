package edu.vtc.augusta

import org.antlr.v4.runtime.tree._

// TODO: Accept a file as a parameter so the generated output can be saved to disk.
class CGenerator(
  symbolTable: SymbolTable,
  reporter   : Reporter) extends AdaBaseVisitor[Void] {

  private val out = System.out
  private var indentationLevel = 0  // The number of indentations where each output line starts.
  private var expressionLevel = 0   // The number of open 'expression' rules that are active.

  private def doIndentation() {
    for (i <- 0 until indentationLevel) {
      out.print("    ")
    }
  }

  override def visitCompilation_unit(ctx: AdaParser.Compilation_unitContext): Void = {
    out.println("#include <stdio.h>")
    out.println("")

    visitChildren(ctx)
    null
  }

  override def visitProcedure_definition(ctx: AdaParser.Procedure_definitionContext): Void = {
    out.println("int main( void )")
    out.println("{")
    indentationLevel += 1

    visitChildren(ctx)

    out.println("")
    for (variableName <- symbolTable.getObjectNames) {
      doIndentation()
      out.println("printf(\"" + variableName + " => %d\\n\", " + variableName + ");")
    }
    out.println("}")
    indentationLevel -= 1
    null
  }

  override def visitObject_declaration(ctx: AdaParser.Object_declarationContext): Void = {
    val id = ctx.IDENTIFIER(0)
    doIndentation()
    out.print("int " + id.getText)

    if (ctx.expression() == null) {
      out.print(" = 0")
    }
    else {
      out.print(" = ")
      visitExpression(ctx.expression())
    }

    out.println(";")
    null
  }

  override def visitBlock(ctx: AdaParser.BlockContext): Void = {
    out.println("")

    visitChildren(ctx)
    null
  }

  override def visitAssignment_statement(ctx: AdaParser.Assignment_statementContext): Void = {
    // TODO: Handle assignment to an array element.
    val id = ctx.left_expression().IDENTIFIER()
    doIndentation()
    out.print(id.getText + " = ")

    visitChildren(ctx)

    out.println(";")
    null
  }

  override def visitExpression(ctx: AdaParser.ExpressionContext): Void = {
    expressionLevel += 1
    visitChildren(ctx)
    expressionLevel -= 1
    null
  }

  override def visitAdditive_expression(ctx: AdaParser.Additive_expressionContext): Void = {
    out.print("(")
    visitChildren(ctx)
    out.print(")")
    null
  }

  override def visitTerminal(node: TerminalNode): Void = {
    try {
      if (expressionLevel > 0) {
        node.getSymbol.getType match {
          case AdaLexer.DIVIDE     |
               AdaLexer.IDENTIFIER |
               AdaLexer.MINUS      |
               AdaLexer.MULTIPLY   |
               AdaLexer.PLUS =>
            out.print(node.getText)

          case AdaLexer.NUMERIC_LITERAL =>
            out.print(Literals.convertIntegerLiteral(node.getText))

          case _ =>
            // Do nothing.
        }
      }
    }
    catch {
      // This exception should normally never arise if illegal literals are ruled out during
      // semantic analysis. However, literal analysis is currently not being done there.
      //
      // TODO: Check literal format during semantic analysis.
      case ex: InvalidLiteralFormatException =>
        reporter.reportError(node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
    null
  }

}
