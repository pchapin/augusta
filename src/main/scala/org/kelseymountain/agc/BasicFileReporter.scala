package org.kelseymountain.agc

import java.io.{BufferedWriter, FileWriter, PrintWriter}
import org.antlr.v4.runtime.Token

class BasicFileReporter(fileName: String) extends Reporter:

  var errorCount   = 0
  var warningCount = 0

  val output = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))

  def getErrorCount: Int   = errorCount
  def getWarningCount: Int = warningCount

  def reportSourceError(errorToken: Token, message: String): Unit =
    errorCount += 1
    output.printf("error: (%d, %d) %s%n",
      errorToken.getLine, errorToken.getCharPositionInLine + 1, message)

  def reportSourceWarning(warningToken: Token, message: String): Unit =
    warningCount += 1
    output.printf("warning: (%d, %d) %s%n",
      warningToken.getLine, warningToken.getCharPositionInLine + 1, message)

  def close(): Unit =
    output.close()
