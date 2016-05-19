package edu.vtc.augusta

import java.io.{BufferedWriter, FileWriter, PrintWriter}

class BasicFileReporter(fileName: String) extends Reporter {

  var errorCount   = 0
  var warningCount = 0

  val output = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))

  def getErrorCount   = errorCount
  def getWarningCount = warningCount

  def reportError(line: Int, column: Int, message: String) {
    errorCount += 1
    output.printf("E: (%d, %d) %s%n", new Integer(line), new Integer(column), message)
  }


  def reportWarning(line: Int, column: Int, message: String) {
    warningCount += 1
    output.printf("W: (%d, %d) %s%n", new Integer(line), new Integer(column), message)
  }


  def close() {
    output.close()
  }

}
