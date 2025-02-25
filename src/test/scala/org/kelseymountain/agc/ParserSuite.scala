package org.kelseymountain.agc

import java.io.{File, FileInputStream}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

class ParserSuite extends UnitSuite:
  import ParserSuite._

  /**
   * Execute test cases.
   *
   * @param testCaseNames An array of file names representing the test cases.
   * @param doParse A function that takes a RabbitParser and executes the parse at a particular
   * start symbol.
   */
  private def doTests(testCaseNames: Array[String],
                      doParse      : AugustaParser => Any): Unit =

    for (testCaseName <- testCaseNames) do
      val testCase = new File(testCaseName)
      val testFile = new FileInputStream(testCase.getPath)
      try
        val lex    = new AugustaLexer(CharStreams.fromStream(testFile))
        val tokens = new CommonTokenStream(lex)
        val parser = new AugustaParser(tokens)

        // Configure error handling.
        val errorListener = new SyntaxErrorListener
        parser.removeErrorListeners()           // Remove default console error listener.
        parser.addErrorListener(errorListener)  // Add custom error listener.

        doParse(parser)

        // Did it work? For positive tests, there should be no errors.
        val errors = errorListener.getErrors
        assert(!errorListener.hasErrors, s"but expected no errors. File: $testCaseName\n${errors.mkString("\n")}")
      finally
        testFile.close()

  private def doBlockTests(testCaseNames: Array[String]): Unit =
    doTests(testCaseNames, _.block())

  private def doWholeFileTests(testCaseNames: Array[String]): Unit =
    doTests(testCaseNames, _.compilation_unit())

  test("Syntax: Expressions") {
    doBlockTests(expressionCases)
  }

  test("Syntax: Control Structures") {
    doBlockTests(controlCases)
  }

  test("Syntax: Whole File") {
    doWholeFileTests(wholeFileCases)
  }


object ParserSuite:
  val prefix: String = "testData" +  File.separator + "syntax" + File.separator + "positive"

  val expressionCases: Array[String] =
    Array("expr0000.agb", "expr0001.agb", "expr0002.agb", "expr0003.agb"
      ) map { prefix + File.separator + _}

  val controlCases: Array[String] =
    Array("control0000.agb", "control0001.agb", "control0002.agb"
      ) map { prefix + File.separator + _}

  val wholeFileCases: Array[String] =
    Array("hello1.agb", "hello2.agb" /*, "hello3.agb", "subprogram0001.agb", "subprogram0002.agb" */
      ) map { prefix + File.separator + _}
