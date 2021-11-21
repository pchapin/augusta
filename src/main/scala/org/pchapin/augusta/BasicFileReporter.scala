package org.pchapin.augusta

import java.io.{BufferedWriter, FileWriter, PrintWriter}

class BasicFileReporter(fileName: String) extends Reporter {

  var errorCount   = 0
  var warningCount = 0

  val output = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))

  def getErrorCount   = errorCount
  def getWarningCount = warningCount

  def reportError(line: Int, column: Int, message: String): Unit = {
    errorCount += 1
    output.printf("error: (%d, %d) %s%n", line, column, message):
        @annotation.nowarn("msg=discarded non-Unit value")
  }


  def reportWarning(line: Int, column: Int, message: String): Unit = {
    warningCount += 1
    output.printf("warning: (%d, %d) %s%n", line, column, message):
        @annotation.nowarn("msg=discarded non-Unit value")
  }


  def close(): Unit = {
    output.close()
  }

}
