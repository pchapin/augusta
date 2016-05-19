package edu.vtc.augusta

import org.antlr.v4.runtime.tree._

// TODO: Currently this is just a skeleton!
class LLVMGenerator(symbolTable: SymbolTable) extends AdaBaseVisitor[Void] {
  private val out = System.out
  private var expressionLevel = 0

  override def visitCompilation_unit(ctx: AdaParser.Compilation_unitContext): Void = {
    out.println("Hello, World!")
    out.println("")

    visitChildren(ctx)
    null
  }


  override def visitTerminal(node: TerminalNode): Void = {
    try {
      if (expressionLevel > 0) {
        node.getSymbol.getType match {
          case AdaLexer.IDENTIFIER  =>
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
      // TODO: Check literal format during semantic analysis.
      case ex: InvalidLiteralFormatException =>
        printf("[Line: %3d, Column: %3d] %s\n",
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
    null
  }
}
