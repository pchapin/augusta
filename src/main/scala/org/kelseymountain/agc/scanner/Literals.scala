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
   * of a real literal into its value.
   *
   * @param text The text of the real literal.
   * @return The value of the real literal.
   */
  def decodeRealLiteral(text: String): BigDecimal =
    BigDecimal(0.0)

  /**
   * This exception is thrown when an invalid literal is encountered. It is unclear if this
   * is an appropriate way to handle invalid literals. It may be better to return an Option
   * or an Either from the 'decode' methods.
   *
   * @param message A human-readable error message to be displayed.
   */
  class InvalidLiteralException(message: String) extends Exception:
    override def getMessage: String = message
