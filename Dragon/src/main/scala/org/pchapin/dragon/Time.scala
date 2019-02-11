// ------------------------------------------------------------------------
// FILE   : Time.scala
// SUMMARY: Time of day support.
// AUTHOR : Peter C. Chapin <PChapin@vtc.vsc.edu>
// ------------------------------------------------------------------------
package org.pchapin.dragon

import Time.InvalidTimeException

class Time(val hours: Int, val minutes: Int = 0, val seconds: Int = 0) {

  // TODO: What about leap seconds?

  if (hours < 0 || hours >= 24)
    throw new InvalidTimeException("Invalid hour")
  if (minutes < 0 || minutes >= 60)
    throw new InvalidTimeException("Invalid minute")
  if (seconds < 0 || seconds >= 60)
    throw new InvalidTimeException("Invalid second")
}

object Time {
  class InvalidTimeException(message: String) extends Exception(message)
}