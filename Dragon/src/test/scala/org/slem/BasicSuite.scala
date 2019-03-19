package org.slem

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers

import org.bitbucket.inkytonik.kiama.util.OutputEmitter
import org.slem.IRTree._

class BasicSuite extends FunSuite with Assertions with Matchers {

  //define i64 @F0(i64 %param0, i64 %param1)
  //{
  //  block0:
  //    %0 = add i64 %param0, %param1
  //  %1 = mul i64 %0, 5
  //  ret i64 %1
  //}

  test(testName = "Example from Web") {
    val x = L_Argument(L_IntType(64))
    val y = L_Argument(L_IntType(64))

    val addxy = L_Add(x, y)
    val mulByFive = L_Mul(addxy, 5: Long)
    val returnValue = L_Ret(mulByFive)

    val myBlock = L_Block(List(addxy, mulByFive), returnValue)

    val myFunction = L_FunctionDefinition(L_IntType(64), List(myBlock), arguments = List(x, y))

    val output = new OutputEmitter
    val e = new IRTreeEncoder(output)
    e.encodeFunctionDefinition(myFunction)
  }

}
