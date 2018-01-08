// ------------------------------------------------------------------------
// FILE   : TimeSuite.scala
// SUMMARY: Tests for class Time.
// AUTHOR : (C) Copyright 2018 by Peter C. Chapin <peter@pchapin.org>
// ------------------------------------------------------------------------
package org.pchapin.dragon

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers

class TimeSuite extends FunSuite with Assertions with Matchers {

  test("Time constructor") {

    class InvalidTimeArguments(
      val badHours  : Int,
      val badMinutes: Int,
      val badSeconds: Int,
      val message   : String)

    val invalidTests = List(
      new InvalidTimeArguments(24,  0,  0, "Invalid hour"  ),
      new InvalidTimeArguments(-1,  0,  0, "Invalid hour"  ),
      new InvalidTimeArguments( 0, 60,  0, "Invalid minute"),
      new InvalidTimeArguments( 0, -1,  0, "Invalid minute"),
      new InvalidTimeArguments( 0,  0, 60, "Invalid second"),
      new InvalidTimeArguments( 0,  0, -1, "Invalid second")
    )

    // Construct the two extreme time values.
    val t1 = new Time( 0,  0,  0)
    val t2 = new Time(23, 59, 59)
    assert(t1.hours ==  0 && t1.minutes ==  0 && t1.seconds ==  0)
    assert(t2.hours == 23 && t2.minutes == 59 && t2.seconds == 59)

    // Try various invalid times.
    for (testArguments <- invalidTests) {
      val thrownException = intercept[Time.InvalidTimeException] {
        val invalidTime =
          new Time(testArguments.badHours, testArguments.badMinutes, testArguments.badSeconds)
      }
      thrownException.getMessage should equal (testArguments.message)
    }

  }

}
