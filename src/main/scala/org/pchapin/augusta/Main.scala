package org.pchapin.augusta

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._

/**
  * The main object of the Augusta. This object contains the main method and other methods that
  * drive the overall activity of the compiler.
  */
object Main {
  private val reporter = new BasicConsoleReporter

  /**
    * The entry point of the program.
    * 
    * @param args The command line arguments.
    */
  def main(args: Array[String]): Unit = {
    if (args.length != 1) {
      println("Usage: java -jar Augusta.jar source-file")
    }
    else {
      val input  = CharStreams.fromFileName(args(0))
      val lexer  = new AdaLexer(input)
      val tokens = new CommonTokenStream(lexer)
      val parser = new AdaParser(tokens)
      val tree   = parser.compilation_unit()
      // TODO: Abort compilation if there are parse errors.

      val symbolTable    = new BasicSymbolTable
      val myAnalyzer     = new SemanticAnalyzer(symbolTable, reporter)
      val analyzerWalker = new ParseTreeWalker
      analyzerWalker.walk(myAnalyzer, tree)
    }
  }

}
