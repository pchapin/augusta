package org.pchapin.augusta

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._

/**
 * The main entry point of the Augusta compiler.
 */
object Main {
  val reporter = new BasicConsoleReporter

  def process(input: ANTLRFileStream, mode: Int) {
    // Parse the input file as Ada
    val lexer  = new AdaLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new AdaParser(tokens)
    val tree   = parser.compilation_unit
    // TODO: Abort compilation if there are parse errors.

    // Walk the tree created during the parse and analyze it for semantic errors.
    val symbolTable    = new StackedSymbolTable
    val myAnalyzer     = new SemanticAnalyzer(symbolTable, reporter)
    val analyzerWalker = new ParseTreeWalker
    analyzerWalker.walk(myAnalyzer, tree)

    // Abort compilation if there are semantic errors.
    if (reporter.getErrorCount > 0) {
      printf("%d errors found; execution aborted!%n", reporter.getErrorCount)
    }
    else {
      // Build a control flow graph.
      val graphBuilder = new CFGBuilder(symbolTable, reporter)
      val rawCFG = graphBuilder.visit(tree)
      val CFG = CGFBuilder.optimize(rawCFG)
      Analysis.liveness(CFG)

      // Do what must be done.
      mode match {
        case Mode.CHECK =>
        // Do nothing more (semantic analysis is all that is necessary).

        case Mode.LLVM =>
          val myLLVMGenerator = new LLVMGenerator(symbolTable, reporter)
          myLLVMGenerator.visit(tree)
      }
    }
  }


  def main(args: Array[String]) {

    // Analyze the command line.
    if (!(args.length == 1 || args.length == 2)) {
      println("Usage: java -jar Augusta [-k | -l] source-file")
      System.exit(1)
    }

    val (mode, sourceFile) = if (args.length == 1) {
      (Mode.LLVM, args(0))
    }
    else {
      val mode = args(0) match {
        case "-k" => Mode.CHECK
        case "-l" => Mode.LLVM
        case _ =>
          printf("The mode option '%s' is unknown, defaulting to CHECK%n", args(0))
          Mode.CHECK
      }
      (mode, args(1))
    }

    // Create a stream that reads from the specified file.
    val input = new ANTLRFileStream(sourceFile)
    process(input, mode)

  }

  private object Mode {
    final val CHECK = 0
    final val LLVM  = 1
  }

}
