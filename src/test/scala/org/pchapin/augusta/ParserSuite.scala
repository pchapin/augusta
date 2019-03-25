package org.pchapin.augusta

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers
import java.io.{File, FileInputStream}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

class ParserSuite extends FunSuite with Assertions with Matchers {
  import ParserSuite._
  
  /**
   * Execute test cases.
   * 
   * @param testCaseNames An array of file names representing the test cases.
   * @param doParse A function that takes a RabbitParser and executes the parse at a particular
   * start symbol.
   */
  private def doTests(testCaseNames: Array[String],
                      doParse      : AdaParser => Any) {
    
    for (testCaseName <- testCaseNames) {
      val testCase = new File(testCaseName)
      val testFile = new FileInputStream(testCase.getPath)
      try {
        val lex    = new AdaLexer(CharStreams.fromStream(testFile))
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

  private def doWholeFileTests(testCaseNames: Array[String]) {
    doTests(testCaseNames, _.compilation_unit())
  }

  private def doBlockTests(testCaseNames: Array[String]) {
    doTests(testCaseNames, _.block())
  }

  test("Syntax: Expressions") {
    doBlockTests(expressionCases)
  }

  test ("Syntax: Control Structures") {
    doBlockTests(controlCases)
  }

  test("Syntax: Whole File") {
    doWholeFileTests(wholeFileCases)
  }

}


object ParserSuite {
  val prefix: String = "testData" +  File.separator + "syntax" + File.separator + "positive"

  val expressionCases: Array[String] =
    Array("expr0000.adb", "expr0001.adb", "expr0002.adb", "expr0003.adb") map { prefix + File.separator + _}

  val controlCases: Array[String] =
    Array("control0000.adb", "control0001.adb", "control0002.adb") map { prefix + File.separator + _}

  val wholeFileCases: Array[String] =
    Array("hello1.adb", "hello2.adb" /*, "hello3.adb", "subprogram0001.adb", "subprogram0002.adb" */) map { prefix + File.separator + _}
}

