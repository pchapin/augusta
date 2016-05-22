//-----------------------------------------------------------------------
// FILE    : StraightSuite.scala
// SUBJECT : Tests for the "straight line" language in MCIML Chapter 1
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <PChapin@vtc.edu.edu>
//
//-----------------------------------------------------------------------
package org.pchapin.chapter01

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers

class StraightSuite extends FunSuite with Assertions with Matchers {
  import Straight._

  // Nested print statements.
  val program0 =
    PrintStatement(List(
      IdentifierExpression("a"),
      SequenceExpression(
        PrintStatement(List(
          IdentifierExpression("b"),
          IdentifierExpression("c"),
          IdentifierExpression("d")
        )),
        IdentifierExpression("e")
      )
    ))

  // An empty print statement.
  val program1 = PrintStatement(List())

  // A simple program without print statements.
  val program2 = AssignmentStatement(
    "a", OperationExpression(IdentifierExpression("b"), Plus, IdentifierExpression("c")))

  // From MCIML, Chapter 1.
  val program3 =
    CompoundStatement(
      AssignmentStatement("a", OperationExpression(
        NumericLiteralExpression(5), Plus, NumericLiteralExpression(3))),
      CompoundStatement(
        AssignmentStatement("b", SequenceExpression(
          PrintStatement(List(
            IdentifierExpression("a"),
            OperationExpression(IdentifierExpression("a"), Minus, NumericLiteralExpression(1))
          )),
          OperationExpression(NumericLiteralExpression(10), Multiply, IdentifierExpression("a"))
        )),
        PrintStatement(List(IdentifierExpression("b")))
      )
    )


  test("Print Argument Count") {
    val messagePrefix = "maximumPrintArgumentCount failed for "
    assert(maximumPrintArgumentCount(program0) == 3, messagePrefix + "program0")
    assert(maximumPrintArgumentCount(program1) == 0, messagePrefix + "program1")
    assert(maximumPrintArgumentCount(program2) == 0, messagePrefix + "program2")
    assert(maximumPrintArgumentCount(program3) == 2, messagePrefix + "program3")
  }


  // TODO: Added more interesting test cases for the interpreter.
  test("Interpretation") {

    // Program0 is not a closed term.
    val thrownException = intercept[UndefinedIdentifierException] {
      interpret(program0)
    }
    thrownException.getMessage should equal ("Undefined identifier: a")

    // Program3 produces real results as follows:
    //
    // 8 7
    // 80
    //
    // TODO: To facilitate testing modify the interpreter so that it returns the output.
    interpret(program3)
  }

}
