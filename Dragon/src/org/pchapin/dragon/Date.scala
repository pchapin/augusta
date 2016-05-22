// ------------------------------------------------------------------------
// FILE   : Data.scala
// SUMMARY: Interface to date implementations.
// AUTHOR : Peter C. Chapin <PChapin@vtc.vsc.edu>
// ------------------------------------------------------------------------
package org.pchapin.dragon

trait Date {

  // Access (read only to allow immutable implementations). These methods may be non-trivial in
  // some implementations.
  def day  : Int
  def month: Int
  def year : Int

  // Basic operations return new objects to allow immutable implementations.
  def next: Date
  def previous: Date

  // Operations defined in terms of the abstract methods above.
  def isLeap = Date.isLeapYear(year)
}

object Date {
  class InvalidDateException(message: String) extends Exception(message)

  // If you change these values you should review and update the documentation as needed. This
  // trait only supports dates with years in the range yearMin <= year < yearMax.
  private val yearMin = 1900
  private val yearMax = 2100

  /**
   * Checks a given candidate date for validity. This method can be used by subtypes of Date (or
   * clients) to verify a date value before constructing a Date object using that value. Since
   * Dates are potentially immutable, depending on the implementation, it may not possible to
   * correct a Date object after it has been constructed.
   *
   * @return True if the given year, month, and day values represent a valid date. Only dates in
   * the range 1900 to 2099 (inclusive) are supported; dates outside that range are considered
   * invalid.
   */
  def valid(year: Int, month: Int, day: Int) = {

    // Return true if the day is out of bounds for the given (year, month).
    def outOfBoundsDay(year: Int, month: Int, day: Int): Boolean = {
      val monthLengths = Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
      val daysInMonth =
        if (month != 2) monthLengths(month - 1) else (if (isLeapYear(year)) 29 else 28)
      (day < 1 || day > daysInMonth)
    }

    if (year < yearMin || year >= yearMax) false
    else if (month < 1 || month > 12) false
    else if (outOfBoundsDay(year, month, day)) false
    else true
  }


  /**
   * Checks if the given year is a leap year. This method assumes the year is in the range 1900
   * to 2099 (inclusive). The behavior is undefined if that is not the case.
   *
   * @return True if the given year is a leap year and false otherwise.
   */
  def isLeapYear(year: Int) = {
    if (year % 4 != 0) false
    else if (year % 400 == 0) true
    else if (year % 100 == 0) false
    else true
  }

}
