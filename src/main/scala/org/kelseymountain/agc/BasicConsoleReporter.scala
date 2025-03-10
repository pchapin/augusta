package org.kelseymountain.agc

import org.antlr.v4.runtime.tree.TerminalNode

class BasicConsoleReporter extends Reporter:

  var errorCount   = 0
  var warningCount = 0

  def getErrorCount: Int   = errorCount
  def getWarningCount: Int = warningCount

  /** @inheritdoc */
  override def reportSourceError(errorNode: TerminalNode, message: String): Unit =
    val errorToken = errorNode.getSymbol
    errorCount += 1
    printf("ERR: [Line: %3d, Column: %3d] %s\n",
      errorToken.getLine, errorToken.getCharPositionInLine + 1, message)

  /** @inheritdoc */
  override def reportSourceWarning(warningNode: TerminalNode, message: String): Unit =
    val warningToken = warningNode.getSymbol
    warningCount += 1
    printf("WRN: [Line: %3d, Column: %3d] %s\n",
      warningToken.getLine, warningToken.getCharPositionInLine + 1, message)
