package org.kelseymountain.augusta

class BasicConsoleReporter extends Reporter {

  var errorCount   = 0
  var warningCount = 0

  def getErrorCount   = errorCount
  def getWarningCount = warningCount

  def reportError(line: Int, column: Int, message: String): Unit = {
    errorCount += 1
    printf("ERR: [Line: %3d, Column: %3d] %s\n", line, column, message)
  }


  def reportWarning(line: Int, column: Int, message: String): Unit = {
    warningCount += 1
    printf("WRN: [Line: %3d, Column: %3d] %s\n", line, column, message)
  }

}
