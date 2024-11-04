package org.kelseymountain.augusta

import java.io.{BufferedWriter, FileWriter, PrintWriter}

class BasicFileReporter(fileName: String) extends Reporter {

  var errorCount   = 0
  var warningCount = 0

  val output = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))

  def getErrorCount: Int   = errorCount
  def getWarningCount: Int = warningCount

  def reportError(line: Int, column: Int, message: String): Unit = {
    errorCount += 1
    output.printf("error: (%d, %d) %s%n", line, column, message)
  }


  def reportWarning(line: Int, column: Int, message: String): Unit = {
    warningCount += 1
    output.printf("warning: (%d, %d) %s%n", line, column, message)
  }


  def close(): Unit = {
    output.close()
  }

}
