package edu.vtc.augusta

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

    // Walk the tree created during the parse and analyze it for semantic errors.
    val symbolTable    = new StackedSymbolTable
    val myAnalyzer     = new SemanticAnalyzer(symbolTable, reporter)
    val analyzerWalker = new ParseTreeWalker
    analyzerWalker.walk(myAnalyzer, tree)

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

        case Mode.INTERPRET =>
          val interpreterWalker = new ParseTreeWalker
          val myInterpreter = new Interpreter(symbolTable, reporter)
          interpreterWalker.walk(myInterpreter, tree)
          myInterpreter.displayResults()

        case Mode.C =>
          val myCGenerator = new CGenerator(symbolTable, reporter)
          myCGenerator.visit(tree)

        case Mode.LLVM =>
          println("Generation of LLVM output is not implemented!")

        case Mode.JVM =>
          val myJVMGenerator = new JVMGenerator(symbolTable, reporter)
          myJVMGenerator.visit(tree)
      }
    }
  }


  def main(args: Array[String]) {

    // Analyze the command line.
    if (args.length != 3) {
      println("Usage: java -jar Augusta (-k | -i | -c | -l | -j) source-file")
      System.exit(1)
    }

    val level = args(0).toInt
    val mode  = args(1) match {
      case "-k" => Mode.CHECK
      case "-i" => Mode.INTERPRET
      case "-c" => Mode.C
      case "-l" => Mode.LLVM
      case "-j" => Mode.JVM
      case _ =>
        printf("The mode option %s is unknown, defaulting to CHECK%n", args(1))
        Mode.CHECK
    }

    // Create a stream that reads from the specified file.
    val input = new ANTLRFileStream(args(2))
    process(input, mode)

  }

  private object Mode {
    final val CHECK     = 0
    final val INTERPRET = 1
    final val C         = 2
    final val LLVM      = 3
    final val JVM       = 4
  }

}
