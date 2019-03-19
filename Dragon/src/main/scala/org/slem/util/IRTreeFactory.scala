/*
    Copyright 2010 Timothy Morton
    
    This file is part of SLEM.

    SLEM is free software: you can redistribute it and/or modify it under the terms of the GNU
    Lesser General Public License as published by the Free Software Foundation, either version 3
    of the License, or (at your option) any later version.

    SLEM is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
    even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License along with SLEM. If
    not, see <http://www.gnu.org/licenses/>.
*/

package org.slem.util

object IRTreeFactory {

  import org.slem.IRTree._

  /////////////////////
  // Macro functions //
  /////////////////////

  var printline_int_str: L_GlobalVariable = null
  var print_int_str: L_GlobalVariable = null
  var printf_int: L_FunctionDeclaration = null

  var imports: List[L_Global] = List()


  def reset() = {
    printline_int_str = null
    print_int_str = null
    printf_int = null
    imports = List()
  }

  def getImports(): List[L_Global] = {
    return imports
  }

  private def printline_int_str_definition(): L_GlobalVariable =
    L_GlobalVariable(L_String("%d\\0A\\00"), linkage = "private", isConstant = true, alignment = 1)

  private def print_int_str_definition(): L_GlobalVariable =
    L_GlobalVariable(L_String("%d\\00"), linkage = "private", isConstant = true, alignment = 1)


  private def printf_int_definition(): L_FunctionDeclaration = {
    L_FunctionDeclaration(
      L_IntType(32),
      arguments = List(L_PointerType(L_IntType(8)), L_VarArgsType()),
      funcName = "printf"
    )
  }

  private def import_printline_int_str() = {
    if (printline_int_str == null) {
      printline_int_str = printline_int_str_definition()
      imports = imports ::: List(printline_int_str)
    }
  }

  private def import_print_int_str() = {
    if (print_int_str == null) {
      print_int_str = print_int_str_definition()
      imports = imports ::: List(print_int_str)
    }
  }

  private def import_printf_int() = {
    if (printf_int == null) {
      printf_int = printf_int_definition()
      imports = imports ::: List(printf_int)
    }
  }

  // Prints an i32 to std out + a line break.
  def L_Macro_PrintLine(value: L_Int): List[L_Instruction] = {
    import_printline_int_str()
    import_printf_int()
    val printfGetPtr = L_GetElementPtr(L_PointerType(resultType(printline_int_str)), printline_int_str, List(0, 0))
    val printfCall = L_Call(L_IntType(32), printf_int, List(printfGetPtr, value))
    List(printfGetPtr, printfCall)
  }

  // Prints an i32 to std out (no line break).
  def L_Macro_Print(value: L_Int): List[L_Instruction] = {
    import_print_int_str()
    import_printf_int()
    val printfGetPtr = L_GetElementPtr(L_PointerType(resultType(print_int_str)), print_int_str, List(0, 0))
    val printfCall = L_Call(L_IntType(32), printf_int, List(printfGetPtr, value))
    List(printfGetPtr, printfCall)
  }

  //////////////////////////////
  // Simplified integer terms //
  //////////////////////////////
  def i32()           : L_IntType = L_IntType(32)
  def i32(value: Int) : L_Int     = L_Int(32, value)
  def i64()           : L_IntType = L_IntType(64)
  def i64(value: Int) : L_Int     = L_Int(64, value)
  def i64(value: Long): L_Int     = L_Int(64, value)
}
