package org.kelseymountain.contracts

import org.scalatest.*
import funsuite.*
import matchers.*

class ContractsSuite extends AnyFunSuite with Assertions with should.Matchers {

  test("Basic Precondition Test") {

    // Basic test that passes precondition evaluation.
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
