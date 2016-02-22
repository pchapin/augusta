package edu.vtc.augusta

/**
 * Object that wraps methods for converting literals into their corresponding values.
 */
object Literals {

  private object StateType {
    final val START = 1
    final val COLLECT_NUMBER = 2
    final val COLLECT_BASED_NUMBER = 3
    final val COLLECT_ONE_DIGIT = 4
    final val COLLECT_ONE_BASED_DIGIT = 5
    final val BASED_NUMBER_DONE = 6
    final val COLLECT_EXPONENT = 7
  }

  /**
   * The following method computes the overall integer from it's value, base, and exponent. This
   * version of the method assumes a base of 10. It has no protection against overflow
   * (arbitrary precision integers should eventually be used).
   *
   * @param value The value given before the exponent.
   * @param exponent The exponent. If no exponent is provided, use an exponent of zero.
   * @return The overall integer value.
   */
  private def computeResult(base: Int, value: Int, exponent: Int): Int = {
    var multiplier = 1

    for (i <- 0 until exponent) {
      multiplier *= base
    }
    value * multiplier
  }

  /**
   * Coverts the text of an integer numeric literal into its corresponding value. This method
   * uses a finite state machine to process the characters of the numeric literal. The state
   * diagram is as follows.
   *
   * <pre>
   * START                   -- {digit}     --> COLLECT_NUMBER (set 'value' to 10*'value' + digit)
   * COLLECT_NUMBER          -- {digit}     --> COLLECT_NUMBER (set 'value' to 10*'value' + digit)
   * COLLECT_NUMBER          -- {'e' | 'E'} --> COLLECT_EXPONENT
   * COLLECT_NUMBER          -- {'_'}       --> COLLECT_ONE_DIGIT
   * COLLECT_NUMBER          -- {'#'}       --> COLLECT_BASED_NUMBER (set 'base' to 'value' and 'value' to 0)
   * COLLECT_BASED_NUMBER    -- {digit}     --> COLLECT_BASED_NUMBER (set 'value' to 'base'*'value' + digit)
   * COLLECT_BASED_NUMBER    -- {'_'}       --> COLLECT_ONE_BASED_DIGIT
   * COLLECT_BASED_NUMBER    -- {'#'}       --> BASED_NUMBER_DONE
   * COLLECT_ONE_DIGIT       -- {digit}     --> COLLECT_NUMBER (set 'value' to 10*'value' + digit)
   * COLLECT_ONE_BASED_DIGIT -- {digit}     --> COLLECT_BASED_NUMBER (set 'value' to 'base'*'value' + digit)
   * BASED_NUMBER_DONE       -- {'e' | 'E'} --> COLLECT_EXPONENT
   * COLLECT_EXPONENT        -- {digit}     --> COLLECT_EXPONENT (set 'exponent' to 10*'exponent' + digit)
   * </pre>
   *
   * @param text The raw text of the numeric literal.
   * @return The integer value of the literal.
   * @throws InvalidLiteralFormatException if the text could not be interpreted as a valid
   * integer literal.
   */
  def convertIntegerLiteral(text: String): Int = {
    // Decode the given string to see if it's a valid integer literal.
    var state    = StateType.START
    var base     = 10
    var value    = 0
    var exponent = 0

    for (ch <- text) {
      state match {
        case StateType.START =>
          if (ch >= '0' && ch <= '9') {
            value = 10 * value + (ch - '0')
            state = StateType.COLLECT_NUMBER
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.COLLECT_NUMBER =>
          if (ch >= '0' && ch <= '9') {
            value = 10 * value + (ch - '0')
          }
          else if (ch == 'e' || ch == 'E') {
            state = StateType.COLLECT_EXPONENT
          }
          else if (ch == '_') {
            state = StateType.COLLECT_ONE_DIGIT
          }
          else if (ch == '#') {
            // TODO: Verify that the new base is in the allowed range.
            base = value
            value = 0
            state = StateType.COLLECT_BASED_NUMBER
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.COLLECT_BASED_NUMBER =>
          // TODO: This check won't work properly with bases > 10.
          if (ch >= '0' && ch < (base + '0')) {
            value = base * value + (ch - '0')
          }
          else if (ch == '_') {
            state = StateType.COLLECT_ONE_BASED_DIGIT
          }
          else if (ch == '#') {
            state = StateType.BASED_NUMBER_DONE
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.COLLECT_ONE_DIGIT =>
          if (ch >= '0' && ch <= '9') {
            value = 10 * value + (ch - '0')
            state = StateType.COLLECT_NUMBER
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.COLLECT_ONE_BASED_DIGIT =>
          // TODO: This check won't work properly with bases > 10.
          if (ch >= '0' && ch < (base + '0')) {
            value = base * value + (ch - '0')
            state = StateType.COLLECT_BASED_NUMBER
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.BASED_NUMBER_DONE =>
          if (ch == 'e' || ch == 'E') {
            state = StateType.COLLECT_EXPONENT
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }

        case StateType.COLLECT_EXPONENT =>
          if (ch >= '0' && ch <= '9') {
            exponent = 10 * exponent + (ch - '0')
          }
          else {
            throw new InvalidLiteralFormatException(
                        "Error: '" + text + "' is not a valid integer literal!")
          }
      }
    }

    // Check the state at the end. Only certain states are allowed. This test is only
    // interesting if no error was noted above (otherwise you see spurious messages).
    //
    if (state != StateType.COLLECT_NUMBER    &&
        state != StateType.BASED_NUMBER_DONE &&
        state != StateType.COLLECT_EXPONENT) {

      throw new InvalidLiteralFormatException(
                  "Error: '" + text + "' is not a valid integer literal!")
    }
    // Return the numeric result.
    computeResult(base, value, exponent)
  }

}
