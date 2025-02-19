package org.kelseymountain.agc

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * This class is the base class for all unit suite tests in this project. It provides the basic
 * infrastructure for running tests and defines the testing style used for all suite tests.
 */
abstract class UnitSuite extends AnyFunSuite with Matchers
