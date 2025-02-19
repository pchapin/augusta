package org.kelseymountain.agc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * This class is the base class for all unit spec tests in this project. It provides the basic
 * infrastructure for running tests and defines the testing style used for all specification
 * tests.
 */
abstract class UnitSpec extends AnyFlatSpec with Matchers
