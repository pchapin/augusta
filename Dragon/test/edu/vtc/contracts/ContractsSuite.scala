//-----------------------------------------------------------------------
// FILE    : ContractsSuite.scala
// SUBJECT : Tests for package edu.vtc.contracts
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------

package edu.vtc.contracts

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers

class ContractsSuite extends FunSuite with Assertions with Matchers {

  test("Basic Precondition Test") {

    // Basic test that pass precondition evaluation.
    val result = requiring (1 == 1) { "Hello" }
    assert(result == "Hello")

    // Basic test that fails precondition evaluation.
    val thrownException = intercept[ContractFailureException] {
      requiring (1 == 0) { "Hello" }
    }
    thrownException.getMessage should equal ("Failed precondition")
  }


  test("Side Effecting Precondition Test") {
    var counter: Int = 0

    def booleanWithSideEffects() = {
      counter = counter + 1
      true
    }

    val result1 = requiring (booleanWithSideEffects()) { "Hello_1" }
    assert(result1 == "Hello_1")
    assert(counter == 1)

    Controller.preconditionsEvaluated(state = false)
    val result2 = requiring (booleanWithSideEffects()) { "Hello_2" }
    assert(result2 == "Hello_2")
    assert(counter == 1)

    Controller.preconditionsEvaluated(state = true)
  }

}