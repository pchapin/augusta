package org.slem

import org.scalatest.{Assertions, FunSuite}
import org.scalatest.Matchers

import org.bitbucket.inkytonik.kiama.util.OutputEmitter
import org.slem.IRTree._

class BasicSuite extends FunSuite with Assertions with Matchers {

  test(testName = "Example from Web") {

    // This is the example on the SLEM web page here:
    // https://code.google.com/archive/p/slem/
    //
    //define i64 @F0(i64 %param0, i64 %param1)
    //{
    //  block0:
    //    %0 = add i64 %param0, %param1
    //    %1 = mul i64 %0, 5
    //    ret i64 %1
    //}

    val param0 = L_Argument(L_IntType(64))
    val param1 = L_Argument(L_IntType(64))

    val addParameters = L_Add(param0, param1)
    val mulByFive     = L_Mul(addParameters, 5: Long)
    val returnResult  = L_Ret(mulByFive)
    val block0        = L_Block(List(addParameters, mulByFive), returnResult)

    val functionF0 =
      L_FunctionDefinition(L_IntType(64), List(block0), arguments = List(param0, param1))

    // OutputEmitters print to the standard output device.
    val output = new OutputEmitter
    val encoder = new IRTreeEncoder(output)
    encoder.encodeFunctionDefinition(functionF0)
  }

}
