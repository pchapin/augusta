package org.kelseymountain.agc

import org.antlr.v4.runtime.Token

class BasicConsoleReporter extends Reporter:

  var errorCount   = 0
  var warningCount = 0

  def getErrorCount: Int   = errorCount
  def getWarningCount: Int = warningCount

  override def reportSourceError(errorToken: Token, message: String): Unit =
    errorCount += 1
    printf("ERR: [Line: %3d, Column: %3d] %s\n",
      errorToken.getLine, errorToken.getCharPositionInLine + 1, message)


  override def reportSourceWarning(warningToken: Token, message: String): Unit =
    warningCount += 1
    printf("WRN: [Line: %3d, Column: %3d] %s\n",
      warningToken.getLine, warningToken.getCharPositionInLine + 1, message)
