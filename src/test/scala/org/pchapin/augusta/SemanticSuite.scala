package org.pchapin.augusta

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers
import java.io.{File, FileInputStream}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
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
        val lex    = new AdaLexer(CharStreams.fromStream(testFile))
        val tokens = new CommonTokenStream(lex)
        val parser = new AdaParser(tokens)
        val tree   = parser.compilation_unit()

        // Walk the tree created during the parse and analyze it for semantic errors.
        //val symbolTable    = new StackedSymbolTable
        val symbolTable    = new BasicSymbolTable
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

  test("Semantics: Basic Type Checking") {
    doTests(SemanticSuite.basicTypeCheckingCases)
  }

  //test("Semantics: Type Declarations") {
  //  doTests(basicCases)
  //  doTests(typeDeclarations)
  //}

}

object SemanticSuite {
  val prefix: String = "testData" + File.separator + "semantics"

  val basicCases: Array[String] =
    Array("semantic_errors.adb" /*, "subprogram0001.adb" */) map { prefix + File.separator + _ }

  val basicTypeCheckingCases: Array[String] =
    Array("type_checking1.adb") map { prefix + File.separator + _ }

  val typeDeclarations: Array[String] =
    Array(
      "type_declare1.adb", "type_declare2.adb", "type_declare3.adb",
      "type_declare4.adb") map { prefix + File.separator + _ }
}
