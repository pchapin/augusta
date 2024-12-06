package org.kelseymountain.agc

import java.io.{FileReader, BufferedReader}

/**
 * This object contains a number of useful helper methods for checking the results of testing.
 */
object Check {

  /**
   * This method returns 'true' if the two files contain the same text. It is useful for
   * checking test output.
   *
   * @param checkName The name of the file containing the expected text.
   * @param errorName The name of the file containing the actual text produced by the program
   * under test.
   * @return True if the two files have the same content.
   */
  def compare(checkName: String, errorName: String): Boolean = {
    val checkFile = new BufferedReader(new FileReader(checkName))
    val errorFile = new BufferedReader(new FileReader(errorName))

    // Read the entire check file into a string.
    var checkString = ""
    var checkLine = ""
    while ({ checkLine = checkFile.readLine(); checkLine != null }) {
      checkString = checkString + checkLine
    }
    checkFile.close()

    // Read the entire error file into a string.
    var errorString = ""
    var errorLine = ""
    while ({ errorLine = errorFile.readLine(); errorLine != null }) {
      errorString = errorString + errorLine
    }
    errorFile.close()

    // Are they the same?
    checkString == errorString
  }

}
