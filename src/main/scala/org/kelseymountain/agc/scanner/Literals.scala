package org.kelseymountain.agc.scanner

object Literals:

  /**
   * This method decodes an integer literal. It is used by the semantic analyzer to convert the
   * text of an integer literal into its value.
   *
   * @param text The text of the integer literal.
   * @return The value of the integer literal.
   */
  def decodeIntegerLiteral(text: String): BigInt =
    BigInt(0)

  /**
   * This method decodes a real literal. It is used by the semantic analyzer to convert the text
   * of a real literal into its value. Real literals differ from integer literals in that they
   * always contain a '.' (integer literals never do). Otherwise, they have a very similar form.
   *
   * @param text The text of the real literal as contained in the literal token.
   * @return The value of the real literal.
   * @throws InvalidLiteralException if the literal is in an invalid format. The exception
   * contains a human-readable error message.
   */
  def decodeRealLiteral(text: String): BigDecimal =
    /*
     * This method is written in an imperative style. Accordingly, it reports errors by throwing
     * exceptions. It might be better to refactor this code to make it more functional and
     * return an Either[String, BigDecimal] for error handling. At the time of this writing, it
     * is unclear which approach is better. It will depend on the overall architecture used for
     * the application.
     *
     * TODO: Handle based real literals.
     */

    enum State:
      case GetFirstWholeDigit
      case GetWholeDigits
      case GetFirstFractionalDigit
      case GetFractionalDigits
      case GetExponent
      case GetFirstExponentDigit
      case GetExponentDigits
      case WholeUnderscore
      case FractionalUnderscore
      case ExponentUnderscore
    end State

    var state = State.GetFirstWholeDigit
    var wholePart = BigDecimal(0.0)
    var fractionalPart = BigDecimal(0.0)
    var digitDivisor = BigDecimal(10.0)
    var exponent = 0

    for (ch <- text) do
      state match
        case State.GetFirstWholeDigit =>
          if ch.isDigit then
            wholePart = ch.asDigit
            state = State.GetWholeDigits
          else throw InvalidLiteralException("Invalid start of real literal")

        case State.GetWholeDigits =>
          if ch.isDigit then
            wholePart = wholePart * 10 + ch.asDigit
          else if ch == '_' then
            state = State.WholeUnderscore
          else if ch == '.' then
            state = State.GetFirstFractionalDigit
          else throw InvalidLiteralException("Invalid character in real literal")

        case State.WholeUnderscore =>
          if ch == '_' then
            throw InvalidLiteralException("Invalid consecutive underscores in real literal")
          else if ch.isDigit then
            wholePart = wholePart * 10 + ch.asDigit
            state = State.GetWholeDigits
          else throw InvalidLiteralException("Invalid character in real literal")

        case State.GetFirstFractionalDigit =>
          if ch.isDigit then
            fractionalPart = ch.asDigit / digitDivisor
            digitDivisor = digitDivisor * 10.0
            state = State.GetFractionalDigits
          else throw InvalidLiteralException("Invalid start of fractional part in real literal")

        case State.GetFractionalDigits =>
          if ch.isDigit then
            fractionalPart = fractionalPart + (ch.asDigit / digitDivisor)
            digitDivisor = digitDivisor * 10.0
          else if ch == '_' then
            state = State.FractionalUnderscore
          else if ch == 'e' || ch == 'E' then
            state = State.GetExponent
          else throw InvalidLiteralException("Invalid character in real literal")

        case State.FractionalUnderscore =>
          if ch == '_' then
            throw InvalidLiteralException("Invalid consecutive underscores in real literal")
          else if ch.isDigit then
            fractionalPart = fractionalPart + (ch.asDigit / digitDivisor)
            digitDivisor = digitDivisor * 10.0
            state = State.GetFractionalDigits
          else throw InvalidLiteralException("Invalid character in real literal")

        case State.GetExponent =>
          if ch == '+' || ch == '-' then
            exponent = 0 // TODO: Handle negative exponents!
            state = State.GetFirstExponentDigit
          else if ch.isDigit then
            exponent = ch.asDigit
            state = State.GetExponentDigits
          else throw InvalidLiteralException("Invalid start of exponent in real literal")

        case State.GetFirstExponentDigit =>
          if ch.isDigit then
            exponent = exponent * 10 + ch.asDigit
            state = State.GetExponentDigits
          else throw InvalidLiteralException("Invalid start of exponent in real literal")

        case State.GetExponentDigits =>
          if ch.isDigit then
            exponent = exponent * 10 + ch.asDigit
          else if ch == '_' then
            state = State.ExponentUnderscore
          else throw InvalidLiteralException("Invalid character in real literal")

        case State.ExponentUnderscore =>
          if ch == '_' then
            throw InvalidLiteralException("Invalid consecutive underscores in real literal")
          else if ch.isDigit then
            exponent = exponent * 10 + ch.asDigit
            state = State.GetExponentDigits
          else throw InvalidLiteralException("Invalid character in real literal")

    (wholePart + fractionalPart) * BigDecimal(10.0).pow(exponent)

  /**
   * This exception is thrown when an invalid literal is encountered.
   *
   * @param message A human-readable error message to be displayed. The message should start
   *                with an uppercase letter and have no trailing punctuation. The message
   *                can be a sentence fragment without articles.
   */
  class InvalidLiteralException(message: String) extends Exception:
    override def getMessage: String = message
