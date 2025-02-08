package org.kelseymountain.agc

import scanner.Literals.*

class NumericLiteralSpec extends UnitSpec:

  "A decimal integer literal" should "handle underscores" in {
    val testCases = Array(
      ("42", BigInt("42")),               // The most basic example.
      ("1_234", BigInt("1234")),          // A simple example with an underscore.
      ("12_345_678", BigInt("12345678")), // A more complex example with multiple underscores.
      ("1_2_3_4", BigInt("1234"))         // A somewhat odd, but legal use of underscores.
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle powers of two" in {
    val testCases = Array(
      ("0", BigInt("0")),            // 2**0 - 1
      ("1", BigInt("1")),            // 2**0
      ("127", BigInt("127")),        // 2**7 - 1
      ("128", BigInt("128")),        // 2**7
      ("255", BigInt("255")),        // 2**8  - 1
      ("256", BigInt("256")),        // 2**8
      ("32_767", BigInt("32767")),   // 2**15 - 1
      ("32_768", BigInt("32768")),   // 2**15
      ("65_535", BigInt("65535")),   // 2**16 - 1
      ("65_536", BigInt("65536")),   // 2**16
      ("2_147_483_647", BigInt("2147483647")),  // 2**31 - 1
      ("2_147_483_648", BigInt("2147483648")),  // 2**31
      ("4_294_967_295", BigInt("4294967295")),  // 2**32 - 1
      ("4_294_967_296", BigInt("4294967296")),  // 2**32
      ("9_223_372_036_854_775_807", BigInt("92230372036854775807")),  // 2**63 - 1
      ("9_223_372_036_854_775_808", BigInt("9223372036854775808")),   // 2**63
      ("18_446_744_073_709_551_615", BigInt("18446744073709551615")), // 2**64 - 1
      ("18_446_744_073_709_551_616", BigInt("18446744073709551615"))  // 2**64
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle exponents" in {
    val testCases = Array(
      ("1E0", BigInt("1")),
      ("1E1", BigInt("10")),
      ("1E2", BigInt("100")),
      ("5E+5", BigInt("500000")),
      ("50E5", BigInt("5000000")),
      ("5_0E5", BigInt("5000000")),
      ("1E1_0", BigInt("10000000000")),
      ("1E1_000", BigInt("10").pow(1000))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  "A based integer literal" should "handle base 2" in {
    val testCases = Array(
      ("2#0#", BigInt("0")),
      ("2#1#", BigInt("1")),
      ("2#10#", BigInt("2")),
      ("2#1100#", BigInt("12")),
      ("2#1100_0011#", BigInt("195")),
      ("2#1100_0011_0101_1010#", BigInt("50010")),
      ("2#1100_0011_0101_1010_1100_0011_0101_1010#", BigInt("3277505370"))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle base 8" in {
    val testCases = Array(
      ("8#0#", BigInt("0")),
      ("8#1#", BigInt("1")),
      ("8#7#", BigInt("7")),
      ("8#10#", BigInt("8")),
      ("8#52#", BigInt("42")),
      ("8#77#", BigInt("63")),
      ("8#77_77#", BigInt("4095")),
      ("8#77_77_77#", BigInt("262143")),
      ("8#77_77_77_77#", BigInt("16777215")),
      ("8#77_77_77_77_77#", BigInt("1073741823")),
      ("8#77_77_77_77_77_77#", BigInt("68719476735")),
      ("8#77_77_77_77_77_77_77#", BigInt("4398046511103")),
      ("8#77_77_77_77_77_77_77_77#", BigInt("281474976710655"))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle base 16" in {
    val testCases = Array(
      ("16#0#", BigInt("0")),
      ("16#1#", BigInt("1")),
      ("16#F#", BigInt("15")),
      ("16#10#", BigInt("16")),
      ("16#2A#", BigInt("42")),
      ("16#FF#", BigInt("255")),
      ("16#FFFF#", BigInt("65535")),
      ("16#FF_FFFF#", BigInt("16777215")),
      ("16#FFFF_FFFF#", BigInt("4294967295")),
      ("16#FF_FFFF_FFFF#", BigInt("1099511627775")),
      ("16#FFFF_FFFF_FFFF#", BigInt("281474976710655")),
      ("16#FF_FFFF_FFFF_FFFF#", BigInt("72057594037927935")),
      ("16#FFFF_FFFF_FFFF_FFFF#", BigInt("18446744073709551615")),
      ("16#7FFF_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF#", BigInt("2").pow(127) - 1)
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle other bases" in {
    val testCases = Array(
      // TODO: Add other test cases.
      ("13#2C#", BigInt("38"))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle underscores" in {
    val testCases = Array(
      // TODO: Add other test cases (are there any?).
      ("1_6#2A#", BigInt("42"))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

  it should "handle exponents" in {
    val testCases = Array(
      // TODO: Add other test cases.
      ("16#2A#E+2", BigInt("10752"))
    )

    for (text, value) <- testCases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)
  }

//  // Strictly speaking, we only need to test for invalid literals that would pass the RE used
//  // by the lexical analyzer. The lexical analyzer will not tokenize anything that doesn't match
//  // one of its regular expressions. It may be reasonable to let the lexical analyzer match a
//  // variety of illegal things that nevertheless appear approximately like integer literals
//  // since 'decodeIntegerLiteral' is likely able to produce better error messages than the
//  // lexical analyzer could.
//  it should "be detected as invalid" in {
//    val testCases = Array(
//      ("xyz", "Invalid start of literal") // Should never occur. The lexer won't tokenize this.
//      // TODO: Add more! Out of range values, bad suffix combinations, bad use of underscore...
//    )
//
//    for (text, errorMessage) <- testCases do
//      val caught = intercept[InvalidLiteralException] {
//        val _ = decodeIntegerLiteral(text)
//      }
//      assert(caught.getMessage == errorMessage)
//  }


//  "A decimal real literal" should "be decoded" in {
//    val testCases = Array(
//      // A couple of basic test cases
//      ("1.234", BigDecimal("1.234")),
//      ("0.01234", BigDecimal("0.01234"))
//      // TODO: Add more test cases (especially when the TODO items in Literals.scala are fixed)!
//    )
//    for (text, value) <- testCases do
//      val literalValue = decodeRealLiteral(text)
//      assert(literalValue == value)
//  }

//  // See the comment on the testing of invalid integer literals above. The same applies here.
//  it should "be detected as invalid" in {
//    val testCases = Array(
//      ("1.a", "Invalid start of fractional part in literal")
//      // TODO: Add more! (Lots more!)
//    )
//    for (text, errorMessage) <- testCases do
//      val caught = intercept[InvalidLiteralException] {
//        val _ = decodeRealLiteral(text)
//      }
//      assert(caught.getMessage == errorMessage)
//  }

end NumericLiteralSpec
