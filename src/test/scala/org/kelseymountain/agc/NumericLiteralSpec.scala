package org.kelseymountain.agc

import scanner.Literals.*

class NumericLiteralSpec extends UnitSpec:

  "An integer literal" should "be decoded correctly" in {
    val testCases = Array(
      // Let's explore legal uses of the underscore.
      ("1_2_3_4", BigInt("1234")),

      // Let's explore decimal values without exponents.
      ("0", BigInt("0")),
      ("1", BigInt("1")),
      ("32_767", BigInt("32767")),
      ("32_768", BigInt("32768")),
      ("2_147_483_647", BigInt("2147483647")),
      ("2_147_483_648", BigInt("2147483648")),
      ("9_223_372_036_854_775_807", BigInt("9223372036854775807")),
      ("9_223_372_036_854_775_808", BigInt("9223372036854775808")),
      ("18_446_744_073_709_551_615", BigInt("18446744073709551615")),

      // Let's explore decimal values with exponents.
      ("0L", BigInt("0")),
      ("9_223_372_036_854_775_807L", BigInt("9223372036854775807")),
      ("0u", BigInt("0")),
      ("4_294_967_295U", BigInt("4294967295")),
      ("4_294_967_296U", BigInt("4294967296")),
      ("18_446_744_073_709_551_615U", BigInt("18446744073709551615")),
      ("0uL", BigInt("0")),
      ("0Lu", BigInt("0")),
      ("18_446_744_073_709_551_615UL", BigInt("18446744073709551615"))

      // TODO: Finish these!
      // Let's explore hexadecimal values without exponents...
      // Let's explore hexadecimal values with exponents...
      // Let's explore binary values without exponents...
      // Let's explore binary values with exponents...
      // Let's explore other bases without exponents...
      // Let's explore other bases with exponents...
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  // Strictly speaking, we only need to test for invalid literals that would pass the RE used
  // by the lexical analyzer. The lexical analyzer will not tokenize anything that doesn't match
  // one of its regular expressions. It may be reasonable to let the lexical analyzer match a
  // variety of illegal things that nevertheless appear approximately like integer literals
  // since 'decodeIntegerLiteral' is likely able to produce better error messages than the
  // lexical analyzer could.
  it should "be detected as invalid where appropriate" in {
    val testCases = Array(
      ("xyz", "Invalid start of literal") // Should never occur. The lexer won't tokenize this.
      // TODO: Add more! Out of range values, bad suffix combinations, bad use of underscore...
    )

    for (text, errorMessage) <- testCases do
      val caught = intercept[InvalidLiteralException] {
        val _ = decodeIntegerLiteral(text)
      }
      assert(caught.getMessage == errorMessage)
  }


  "A real literal" should "be decoded correctly" in {
    val testCases = Array(
      // A couple of basic test cases
      ("1.234", BigDecimal("1.234")),
      ("0.01234", BigDecimal("0.01234"))
      // TODO: Add more test cases (especially when the TODO items in Literals.scala are fixed)!
    )
    for (text, value) <- testCases do
      val literalValue = decodeRealLiteral(text)
      assert(literalValue == value)
  }

  // See the comment on the testing of invalid integer literals above. The same applies here.
  it should "be detected as invalid where appropriate" in {
    val testCases = Array(
      ("1.a", "Invalid start of fractional part in literal")
      // TODO: Add more! (Lots more!)
    )
    for (text, errorMessage) <- testCases do
      val caught = intercept[InvalidLiteralException] {
        val _ = decodeRealLiteral(text)
      }
      assert(caught.getMessage == errorMessage)
  }

end NumericLiteralSpec
