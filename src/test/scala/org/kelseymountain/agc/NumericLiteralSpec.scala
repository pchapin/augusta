package org.kelseymountain.agc

import scanner.Literals.*

class NumericLiteralSpec extends UnitSpec:

  def runIntTests(cases: Array[(String, BigInt)]): Unit =
    for (text, value) <- cases do
      val literalValue = decodeIntegerLiteral(text)
      assert(literalValue == value)

  "A decimal integer literal" should "handle underscores" in {
    val testCases = Array(
      ("42", BigInt("42")),               // The most basic example.
      ("1_234", BigInt("1234")),          // A simple example with an underscore.
      ("12_345_678", BigInt("12345678")), // A more complex example with multiple underscores.
      ("1_2_3_4", BigInt("1234"))         // A somewhat odd, but legal use of underscores.
    )
    runIntTests(testCases)
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
      ("9_223_372_036_854_775_807", BigInt("9223372036854775807")),   // 2**63 - 1
      ("9_223_372_036_854_775_808", BigInt("9223372036854775808")),   // 2**63
      ("18_446_744_073_709_551_615", BigInt("18446744073709551615")), // 2**64 - 1
      ("18_446_744_073_709_551_616", BigInt("18446744073709551616"))  // 2**64
    )
    runIntTests(testCases)
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
    runIntTests(testCases)
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
    runIntTests(testCases)
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
    runIntTests(testCases)
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
    runIntTests(testCases)
  }

  it should "handle other bases" in {
    val testCases = Array(
      // TODO: Add other test cases.
      ("13#2C#", BigInt("38"))
    )
    runIntTests(testCases)
  }

  it should "handle underscores" in {
    val testCases = Array(
      // TODO: Add other test cases (are there any?).
      ("1_6#2A#", BigInt("42"))
    )
    runIntTests(testCases)
  }

  it should "handle exponents" in {
    val testCases = Array(
      // TODO: Add other test cases.
      ("16#2A#E+2", BigInt("10752"))
    )
    runIntTests(testCases)
  }

//  // Strictly speaking, we only need to test for invalid literals that would pass the RE used
//  // by the lexical analyzer. The lexical analyzer will not tokenize anything that doesn't match
//  // one of its regular expressions. It may be reasonable to let the lexical analyzer match a
//  // variety of illegal things that nevertheless appear approximately like integer literals
//  // since 'decodeIntegerLiteral' is likely able to produce better error messages than the
//  // lexical analyzer could.
//  it should "be detected as invalid appropriately" in {
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

  def runRealTests(cases: Array[(String, BigDecimal)]): Unit =
    for (text, value) <- cases do
      val literalValue = decodeRealLiteral(text)
      assert(literalValue == value)

  "A decimal real literal" should "handle underscores" in {
    val testCases = Array(
      ("1.234", BigDecimal("1.234")),
      ("1.234_567", BigDecimal("1.234567")),
      ("1_2.345", BigDecimal("12.345")),
      ("1_2.3_4_5", BigDecimal("12.345"))
    )
    runRealTests(testCases)
  }

  it should "handle exponents" in {
    // TODO: Add test cases for negative exponents.
    val testCases = Array(
      ("1.234E0", BigDecimal("1.234")),
      ("1.234e1", BigDecimal("12.34")),
      ("1.234E2", BigDecimal("123.4")),
      ("5.678e+5", BigDecimal("567800.0")),
      ("56.78E5", BigDecimal("5678000.0")),
      ("5_6.7_8e5", BigDecimal("5678000.0")),
      ("1.234E1_3", BigDecimal("12340000000000.0")),
      ("1.234e1_000", BigDecimal("1.234E1000"))
    )
    runRealTests(testCases)
  }

  // See the comment on the testing of invalid integer literals above. The same applies here.
  it should "be detected as invalid appropriately" in {
    val testCases = Array(
      ("a.2", "Invalid start of real literal"),
      ("1a.2", "Invalid character in real literal"),
      ("1__1.2", "Invalid consecutive underscores in real literal"),
      ("1_a.2", "Invalid character in real literal"),
      ("1.a", "Invalid start of fractional part in real literal"),
      ("1.2a", "Invalid character in real literal"),
      ("1.2__3", "Invalid consecutive underscores in real literal"),
      ("1.2_a", "Invalid character in real literal"),
      ("1.2ea", "Invalid start of exponent in real literal"),
      ("1.2e+a", "Invalid start of exponent in real literal"),
      ("1.2e1a", "Invalid character in real literal"),
      ("1.2e1__2", "Invalid consecutive underscores in real literal"),
      ("1.2e1_a", "Invalid character in real literal")
    )
    for (text, errorMessage) <- testCases do
      val caught = intercept[InvalidLiteralException] {
        val _ = decodeRealLiteral(text)
      }
      assert(caught.getMessage == errorMessage)
  }

end NumericLiteralSpec
