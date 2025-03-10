package org.kelseymountain.agc

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import org.antlr.v4.runtime.tree.TerminalNode

class BasicFileReporter(fileName: String) extends Reporter:

  var errorCount   = 0
  var warningCount = 0

  val output = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))

  def getErrorCount: Int   = errorCount
  def getWarningCount: Int = warningCount

  /** @inheritdoc */
  override def reportSourceError(errorNode: TerminalNode, message: String): Unit =
    val errorToken = errorNode.getSymbol
    errorCount += 1
    output.printf("error: (%d, %d) %s%n",
      errorToken.getLine, errorToken.getCharPositionInLine + 1, message)

  /** @inheritdoc */
  override def reportSourceWarning(warningNode: TerminalNode, message: String): Unit =
    val warningToken = warningNode.getSymbol
    warningCount += 1
    output.printf("warning: (%d, %d) %s%n",
      warningToken.getLine, warningToken.getCharPositionInLine + 1, message)

  def close(): Unit =
    output.close()
