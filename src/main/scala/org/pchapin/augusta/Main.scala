package org.pchapin.augusta

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._
import org.bitbucket.inkytonik.kiama.util.OutputEmitter
import org.slem.IRTreeEncoder

/**
  * The main object of the Augusta. This object contains the main method and other methods that
  * drive the overall activity of the compiler.
  */
object Main {
  private val reporter = new BasicConsoleReporter


  private object Mode {
    final val CHECK = 0
    final val LLVM  = 1
  }


  /**
    * The entry point of the program.
    * 
    * @param args The command line arguments.
    */
  def main(args: Array[String]): Unit = {
    if (!(args.length == 1 || args.length == 2)) {
      println("Usage: java -jar Augusta.jar [-k | -l] source-file")
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

    val input  = CharStreams.fromFileName(sourceFile)
    val lexer  = new AdaLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new AdaParser(tokens)
    val tree   = parser.compilation_unit()
    // TODO: Abort compilation if there are parse errors.

    val symbolTable    = new BasicSymbolTable
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
      val optimizedCFG = CFGBuilder.optimize(rawCFG)
      val CFG = CFGBuilder.label(optimizedCFG)
      println(CFG)

      // Do a liveness analysis on the CFG.
      //Analysis.liveness(CFG)
      // TODO: Check to see if there are any variables that might be used uninitialized.

      // Do what must be done.
      mode match {
        case Mode.CHECK =>
          // Do nothing more (semantic analysis is all that is necessary).

        case Mode.LLVM =>
          val myCodeGenerator = new CodeGenerator(CFG, symbolTable, reporter)
          val LLVMAbstractSyntax = myCodeGenerator.makeAST
          val output = new OutputEmitter
          val encoder = new IRTreeEncoder(output)
          encoder.encodeFunctionDefinition(LLVMAbstractSyntax)
      }
    }
  }

}
