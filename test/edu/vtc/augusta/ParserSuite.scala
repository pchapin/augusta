//-----------------------------------------------------------------------
// FILE    : ParserSuite.scala
// SUBJECT : Tests of the Augusta parser.
// AUTHOR  : (C) Copyright 2014 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package edu.vtc.augusta

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers
import java.io.{FileInputStream, File}
import org.antlr.v4.runtime.{CommonTokenStream, ANTLRInputStream}

class ParserSuite extends FunSuite with Assertions with Matchers {
  import ParserSuite._

  /**
   * Execute test cases.
   *
   * @param testCaseNames An array of file names representing the test cases.
   * @param doParse A function that takes a AllegraParser and executes the parse at a particular
   * start symbol.
   */
  private def doTests(testCaseNames: Array[String],
                      doParse      : AdaParser => Any) {

    for (testCaseName <- testCaseNames) {
      val testCase = new File(testCaseName)
      val testFile = new FileInputStream(testCase.getPath)
      try {
        val lex    = new AdaLexer(new ANTLRInputStream(testFile))
        val tokens = new CommonTokenStream(lex)
        val parser = new AdaParser(tokens)
        // TODO: Should indicate a failed test if parsing encounters any errors.
        doParse(parser)
      }
      finally {
        testFile.close()
      }
    }
  }


  // Helper methods for doing tests at various start symbols in the grammar
  // ----------------------------------------------------------------------

  private def doWholeFileTests(testCaseNames: Array[String]) {
    doTests(testCaseNames, _.compilation_unit())
  }

  private def doBlockTests(testCaseNames: Array[String]) {
    doTests(testCaseNames, _.block_statement())
  }

  private def doExpressionTests(testCaseNames: Array[String]) {
    doTests(testCaseNames, _.expression())
  }


  // ============
  // Test Methods
  // ============

  test("Syntax: Expressions") {
     doBlockTests(expressionCases)
  }

  test("Syntax: Miscellaneous") {
    doBlockTests(miscellaneousCases)
  }

  // test("Syntax: Whole File") {
  //   doWholeFileTests(wholeFileCases)
  // }

}


object ParserSuite {
  val prefix = "testData" + File.separator + "syntax" + File.separator + "positive"

  val expressionCases =
    Array("expressions.ada") map { prefix + File.separator + _}

  val miscellaneousCases =
    Array("character_literals.ada", "attributes.ada") map { prefix + File.separator + _}

  // val wholeFileCases =
  //   Array("hello1.ada", "hello2.ada") map { prefix + File.separator + _}
}
