package org.kelseymountain.agc

import java.io.{File, FileInputStream}
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

class SemanticSuite extends UnitSuite:
  import SemanticSuite._

  /**
   * Execute test cases.
   *
   * @param testCaseNames An array of file names representing the test cases.
   */
  private def doTests(testCaseNames: Array[String]): Unit =

    for (testCaseName <- testCaseNames)
      val baseName  = testCaseName.substring(0, testCaseName.lastIndexOf('.'))
      val checkName = baseName + ".check"
      val errorName = baseName + ".err"
      val myFileReporter  = new BasicFileReporter(errorName)

      val testFile  = new FileInputStream(new File(testCaseName))
      try
        // Use ANTLR to parse the input file.
        val lex    = new AugustaLexer(CharStreams.fromStream(testFile))
        val tokens = new CommonTokenStream(lex)
        val parser = new AugustaParser(tokens)
        val tree   = parser.compilation_unit()

        // Build the symbol table.
        val mySymbolTable = new SymbolTableTree(preDefinedSymbols)
        val mySymbolTableBuilder = new SymbolTableBuilder(myFileReporter, mySymbolTable)
        mySymbolTableBuilder.visit(tree)

        // Do semantic analysis.
        val myAnalyzer = new SemanticAnalyzer(myFileReporter, mySymbolTable)
        myAnalyzer.visit(tree)
        myFileReporter.close()

        assert(Check.compare(checkName, errorName), testCaseName)
        new File(errorName).delete()
      finally
        testFile.close()
      end try
    end for
  end doTests

  //test("Semantics: Basic") {
  //  doTests(basicCases)
  //}

  test("Semantics: Basic Type Checking") {
    // The type-checking tests are not passing yet. We should revisit once the AST is ready.
    pendingUntilFixed {
      doTests(basicTypeCheckingCases)
    }
  }

  //test("Semantics: Type Declarations") {
  //  doTests(basicCases)
  //  doTests(typeDeclarations)
  //}

end SemanticSuite


object SemanticSuite:
  val prefix: String = "testData" + File.separator + "semantics"

  val basicCases: Array[String] =
    Array("semantic_errors.agb" /*, "subprogram0001.adb" */) map { prefix + File.separator + _ }

  val basicTypeCheckingCases: Array[String] =
    Array("type_checking1.agb") map { prefix + File.separator + _ }

  val typeDeclarations: Array[String] =
    Array(
      "type_declare1.agb", "type_declare2.agb", "type_declare3.agb",
      "type_declare4.agb") map { prefix + File.separator + _ }

end SemanticSuite
