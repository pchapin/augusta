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

  // Used for testing declarations.
  private def doDeclarationTests(testCaseNames: Array[String]): Unit =
    doTests(testCaseNames, _.declarations())

  // Used for testing both expressions (via assignment statements) and control structures.
  private def doBlockTests(testCaseNames: Array[String]): Unit =
    doTests(testCaseNames, _.block())

  // Used for testing complete programs.
  private def doWholeFileTests(testCaseNames: Array[String]): Unit =
    doTests(testCaseNames, _.compilation_unit())

  test("Syntax: Declarations") {
    doDeclarationTests(declarationCases)
  }

  test("Syntax: Expressions") {
    doBlockTests(expressionCases)
  }

  test("Syntax: Statements") {
    doBlockTests(statementCases)
  }

  test("Syntax: Whole File") {
    doWholeFileTests(wholeFileCases)
  }


object ParserSuite:
  val prefix: String = "testData" +  File.separator + "syntax" + File.separator + "positive"

  val declarationCases: Array[String] =
    Array("decl0000.agb"
      ) map { prefix + File.separator + _}

  val expressionCases: Array[String] =
    Array("expr0000.agb", "expr0001.agb", "expr0002.agb", "expr0003.agb"
      ) map { prefix + File.separator + _}

  val statementCases: Array[String] =
    Array("stmt0000.agb", "stmt0001.agb", "stmt0002.agb"
      ) map { prefix + File.separator + _}

  val wholeFileCases: Array[String] =
    Array("hello0.agb", "hello1.agb", "hello2.agb" /*, "hello3.agb", "subprogram0001.agb", "subprogram0002.agb" */
      ) map { prefix + File.separator + _}
