//-----------------------------------------------------------------------
// FILE    : Main.scala
// SUBJECT : Main program of the Augusta Ada compiler.
// AUTHOR  : (C) Copyright 2014 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package edu.vtc.augusta

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 * Wrapper class for the main method.
 *
 * @author Peter C. Chapin
 */
object Main {

  /**
   * This program provides a simple front end to the Augusta compiler.
   *
   * @param args The command line arguments
   */
  def main(args : Array[String])
  {
    // Analyze the command line.
    if (args.length != 2) {
      println("Usage: java -jar Augusta.jar (-k | -c) source-file")
      System.exit(1)
    }

    val mode  = args(1) match {
      case "-k" => Mode.CHECK
      case "-c" => Mode.COMPILE
    }

    // Create a stream that reads from the specified file.
    val input = new ANTLRFileStream(args(1))

    // Parse the input file as Ada.
    val lexer  = new AdaLexer(input)
    val tokens = new CommonTokenStream(lexer)
    val parser = new AdaParser(tokens)
    val tree   = parser.compilation_unit

    // Walk the tree created during the parse and analyze it for semantic errors.
    val symbolTable    = new BasicSymbolTable
    val myAnalyzer     = new SemanticAnalyzer(symbolTable)
    val analyzerWalker = new ParseTreeWalker
    analyzerWalker.walk(myAnalyzer, tree)

    if (myAnalyzer.getErrorCount > 0) {
      printf("%d errors found; execution aborted!", myAnalyzer.getErrorCount)
    }
    else {
      // Do what must be done.
      mode match {
        case Mode.CHECK =>
          // Do nothing more (semantic analysis is all that is necessary).
          val parseTree = tree.toStringTree(parser)
          println("*** AST ==> " + parseTree)
          println()

        case Mode.COMPILE =>
          val myLLVMGenerator = new LLVMGenerator(symbolTable)
          myLLVMGenerator.visit(tree)
      }
    }
  }

  private object Mode {
    final val CHECK   = 0
    final val COMPILE = 1
  }
}
