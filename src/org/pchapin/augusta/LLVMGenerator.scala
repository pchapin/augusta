package org.pchapin.augusta

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter, PrintWriter}

import org.antlr.v4.runtime.tree._

// TODO: Currently this is just a skeleton!
class LLVMGenerator(symbolTable: SymbolTable, reporter: Reporter) extends AdaBaseVisitor[Void] {
  private val output = new PrintWriter(
    new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.ll"), "US-ASCII"))
  )
  private var expressionLevel = 0

  override def visitCompilation_unit(ctx: AdaParser.Compilation_unitContext): Void = {
    output.println("Hello, World!")
    output.println("")

    visitChildren(ctx)
    output.close()
    null
  }


  override def visitTerminal(node: TerminalNode): Void = {
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
