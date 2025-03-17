package org.kelseymountain.agc

import cats.effect.{ExitCode, IO}
import cats.syntax.all.*
import com.monovore.decline.*
import com.monovore.decline.effect.*
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.*

private def processAugustaFile(inputFilePath: String, outputFilePath: String): IO[Unit] = {
  for {
    input <- IO.blocking(CharStreams.fromFileName(inputFilePath))
    _ <- IO {
      // Use ANTLR to parse the input file.
      val lexer = new AugustaLexer(input)
      val tokens = new CommonTokenStream(lexer)
      val parser = new AugustaParser(tokens)
      val tree = parser.compilation_unit()

      // Build the symbol table.
      val myConsoleReporter = new BasicConsoleReporter
      val mySymbolTable = new SymbolTableTree(preDefinedSymbols)
      val mySymbolTableBuilder = new SymbolTableBuilder(myConsoleReporter, mySymbolTable)
      mySymbolTableBuilder.visit(tree)

      // Perform semantic analysis.
      val mySemanticAnalyzer = new SemanticAnalyzer(myConsoleReporter, mySymbolTable)
      mySemanticAnalyzer.visit(tree)

      // Perform code generation.
      val myCGenerator = new CGenerator(myConsoleReporter, mySymbolTable)
      myCGenerator.visit(tree)
    }
  } yield ()
}

/**
  * The main object of the Augusta.
  */
object Main extends CommandIOApp(
  name    = "agc",
  header  = "AGC Compiler - A compiler for the Augusta language",
  version = "0.1.0") {

  // Define an option for setting the optimization level.
  val optimizationOpt: Opts[Int] = Opts
    .option[Int]("optimize", short = "O", help = "Set optimization level (0-3)")
    .withDefault(0)

  // Define an option for specifying the output file.
  val outputOpt: Opts[String] = Opts
    .option[String]("output", short = "o", help = "Output file name")
    .withDefault("a.out")

  // Define a positional argument for the input file.
  val inputFile: Opts[String] = Opts
    .argument[String]("input file")

  // Combine all options into a single command.
  override def main: Opts[IO[ExitCode]] = (optimizationOpt, outputOpt, inputFile).mapN {
    (optLevel, outputFile, fileName) =>
      processAugustaFile(fileName, outputFile)
        .as(ExitCode.Success)
        .handleErrorWith { err =>
          IO.println(s"Error: ${err.getMessage}").as(ExitCode.Error)
        }
    }
}
