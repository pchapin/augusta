//-----------------------------------------------------------------------
// FILE    : SemanticSuite.scala
// SUBJECT : Tests of the Rabbit1 semantic analyzer.
// AUTHOR  : (C) Copyright 2014 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package org.pchapin.augusta

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers
import java.io.{FileInputStream, File}
import org.antlr.v4.runtime.{CommonTokenStream, ANTLRInputStream}
import org.antlr.v4.runtime.tree.ParseTreeWalker

class SemanticSuite extends FunSuite with Assertions with Matchers {
  import SemanticSuite._
  
  /**
   * Execute test cases.
   * 
   * @param testCaseNames An array of file names representing the test cases.
   */
  private def doTests(testCaseNames: Array[String]) {
    
    for (testCaseName <- testCaseNames) {
      val baseName  = testCaseName.substring(0, testCaseName.lastIndexOf('.'))
      val checkName = baseName + ".check"
      val errorName = baseName + ".err"
      val reporter  = new BasicFileReporter(errorName)

      val testFile  = new FileInputStream(new File(testCaseName))
      try {
        val lex    = new AdaLexer(new ANTLRInputStream(testFile))
        val tokens = new CommonTokenStream(lex)
        val parser = new AdaParser(tokens)
        val tree   = parser.compilation_unit()

        // Walk the tree created during the parse and analyze it for semantic errors.
        val symbolTable    = new StackedSymbolTable
        val myAnalyzer     = new SemanticAnalyzer(symbolTable, reporter)
        val analyzerWalker = new ParseTreeWalker
        analyzerWalker.walk(myAnalyzer, tree)
        reporter.close()

        assert(Check.compare(checkName, errorName), testCaseName)
        new File(errorName).delete()
      }
      finally {
        testFile.close()
      }
    }
  }


  test("Semantics: Basic") {
    doTests(SemanticSuite.basicCases)
  }

  test("Semantics: Type Declarations") {
    doTests(basicCases)
    doTests(typeDeclarations)
  }

}

object SemanticSuite {
  val prefix = "testData" + File.separator + "semantics"

  val basicCases =
    Array("semantic_errors.ada", "subprogram0001.ada") map { prefix + File.separator + _ }

  val typeDeclarations =
    Array(
      "type_declare1.ada", "type_declare2.ada", "type_declare3.ada",
      "type_declare4.ada") map { prefix + File.separator + _ }
}
