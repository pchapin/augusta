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

package org.slem

import org.bitbucket.inkytonik.kiama.util.Emitter
import org.bitbucket.inkytonik.kiama.attribution.Attribution

class IRTreeEncoder(emitter: Emitter) extends Attribution {

  import org.slem.IRTree._
  import java.io.FileWriter

  var debugmode = true
  var fileOutputEnabled = true
  var debugtreemode = false
  var fileout = ""

  var currentParamNum = 0
  var currentSSA = 0
  var currentGlobalNum = 0
  var currentFuncNum = 0
  var currentLabelNum = 0

  val labelname: L_Label => String = {
    attr {
      case n: L_Label =>
        if (n.label.size != 0) {
          n.label.label
        }
        else {
          getNewLabelName()
        }
    }
  }


  val funcname: L_Function => String = {
    attr {
      case n: L_FunctionDefinition =>
        if (n.funcName.size != 0) {
          "@" + n.funcName
        }
        else {
          getNewFuncName()
        }

      case n: L_FunctionDeclaration =>
        if (n.funcName.size != 0) {
          "@" + n.funcName
        }
        else {
          getNewFuncName()
        }

      case _ =>
        getNewFuncName()
    }
  }

  val paramName: L_Argument => String = {
    attr {
      case n: L_Argument =>
        if (n.argName.size == 0) {
          getNewParamName()
        }
        else {
          "%" + n.argName
        }
    }
  }

  val ssa: L_Value => String = {
    attr {
      case _ => getNewSSA()
    }
  }

  val gvarname: L_GlobalVariable => String = {
    attr {
      case n: L_GlobalVariable =>
        if (n.name.size == 0) {
          getNewGlobalVarName()
        }
        else {
          "@" + n.name
        }
    }
  }

  def getNewLabelName(): String = {
    currentLabelNum = currentLabelNum + 1
    "block" + (currentLabelNum - 1)
  }

  def getNewFuncName(): String = {
    currentFuncNum = currentFuncNum + 1
    "@F" + (currentFuncNum - 1)
  }

  def getNewGlobalVarName(): String = {
    currentGlobalNum = currentGlobalNum + 1
    "@G" + (currentGlobalNum - 1)
  }

  def getNewParamName(): String = {
    currentParamNum = currentParamNum + 1
    "%param" + (currentParamNum - 1)
  }

  def getNewSSA(): String = {
    currentSSA = currentSSA + 1
    "%" + (currentSSA - 1)
  }

  def reset(): Unit = {
    fileout = ""
    currentParamNum = 0
    currentSSA = 0
  }

  def encodeTree(p: L_Program): Unit = {
    reset()
    for (m <- p.modules) {
      for (g <- m.globals) {
        encodeGlobal(g)
      }
    }
    if (fileOutputEnabled) {
      writeFile()
    }
  }

  def writeFile(): Unit = {
    val fw = new FileWriter("LLVMIR/newprog.ll")
    fw.write(fileout)
    fw.close()
  }

  def encodeGlobal(g: L_Global): Unit = {
    g match {
      case v: L_GlobalVariable => encodeGlobalVariable(v)
      case f: L_FunctionReference => encodeGlobal(f.funcPtr)
      case f: L_FunctionDeclaration => encodeFunctionDeclaration(f)
      case f: L_FunctionDefinition => encodeFunctionDefinition(f)
      case _ =>
    }
    //0
  }

  def encodeArgument(a: L_Argument): Unit = {
    encodeType(a.ty)
    emit(" ")
    if (a.value == null) {
      emit(paramName(a))
    }
    else {
      encodeValue(a.value)
    }

    for (attrib <- a.attrs) {
      emit(" ")
      emit(attrib)
    }
  }

  def encodeConstant(c: L_Constant): Unit = {
    c match {
      //Simple Constants
      case n: L_Boolean =>
        if (n.value) {
          emit("true")
        }
        else {
          emit("false")
        }

      case n: L_Int =>
        emit("" + n.value)

      case n: L_Float =>
        emit(n.value)

      case n: L_Double =>
        emit(n.value)

      /* No such thing as a pointer constant except for null ptr
      case n : L_Pointer =>
        //encodeConstant(n.value)
        //emit("*")
        encodeValue(n.value)
        emit("*")
      */
      case n: L_NullPointer =>
        emit("null")

      // Complex Constants.
      case n: L_Structure =>
        emit("{ ")
        val imax = n.elements.size
        val i = 1
        for (e <- n.elements) {
          encodeType(resultType(e))
          emit(" ")
          encodeValue(e)
          if (i < imax) {
            emit(", ")
          }
        }
        emit(" }")

      case n: L_Array =>
        emit("[ ")
        val imax = n.elements.size
        val i = 1
        for (e <- n.elements) {
          encodeType(resultType(e))
          emit(" ")
          encodeValue(e)
          if (i < imax) {
            emit(", ")
          }
        }
        emit(" ]")

      case n: L_String =>
        emit("c" + '"' + n.s + '"')

      case n: L_ZeroInitializer =>
        emit("zeroinitialiser")

      case n: L_Vector =>
        emit("< ")
        val imax = n.elements.size
        val i = 1
        for (e <- n.elements) {
          encodeType(resultType(e))
          emit(" ")
          encodeValue(e)
          if (i < imax) {
            emit(", ")
          }
        }
        emit(" >")

      case _ => emit("Unknown Constant")
    }
  }

  def encodeLabel(l: L_Label): Unit = {
    emit(labelname(l))
  }

  def encodeValue(v: L_Value): Unit = {
    v match {
      case n: L_Instruction         => emit(ssa(n))
      case n: L_Argument            => emit(paramName(n))
      case n: L_GlobalVariable      => emit(gvarname(n))
      case n: L_Constant            => encodeConstant(n)
      case n: L_FunctionReference   => encodeValue(n.funcPtr)
      case n: L_FunctionDefinition  => emit(funcname(n))
      case n: L_FunctionDeclaration => emit(funcname(n))
      case _ => emit("UnknownValue : " + v)
    }
    //0
  }


  def encodeInstruction(b: L_Instruction): Unit = {
    b match {
      case n: L_BinOpInstruction =>
        emit(ssa(n))
        emit(" = ")
        emitw(n.instructionString)
        encodeType(resultType(n.LHS))
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_ExtractElement =>
        emit(ssa(n))
        emit(" = extractelement ")
        encodeType(resultType(n.vec))
        emit(" ")
        encodeValue(n.vec)
        emit(", i32 ")
        emit("" + n.idx)

      case n: L_InsertElement =>
        emit(ssa(n))
        emit(" = insertelement ")
        encodeType(resultType(n.vec))
        emit(" ")
        encodeValue(n.vec)
        emit(", ")
        encodeType(resultType(n.elt))
        emit(" ")
        encodeValue(n.elt)
        emit(", i32 ")
        encodeValue(n.idx)

      case n: L_ShuffleVector =>
        emit(ssa(n))
        emit(" = shufflevector ")
        encodeType(resultType(n.v1))
        emit(" ")
        encodeValue(n.v1)
        emit(", ")
        encodeType(resultType(n.v2))
        emit(" ")
        encodeValue(n.v2)
        emit(", ")
        encodeType(resultType(n.mask))
        emit(" ")
        encodeValue(n.mask)

      case n: L_ExtractValue =>
        emit(ssa(n))
        emit(" = extractvalue ")
        encodeType(resultType(n.value))
        emit(" ")
        encodeValue(n.value)
        for (idx <- n.indexes) {
          emit(", ")
          encodeValue(idx)
        }

      case n: L_InsertValue =>
        emit(ssa(n))
        emit(" = insertvalue ")
        encodeType(resultType(n.value))
        emit(" ")
        encodeValue(n.value)
        emit(", ")
        encodeType(resultType(n.elt))
        emit(" ")
        encodeValue(n.elt)
        emit(", ")
        encodeValue(n.idx)

      case n: L_Alloca =>
        emit(ssa(n))
        emit(" = alloca ")
        encodeType(n.typ)
        if (n.hasNumElements) {
          emit(", ")
          encodeType(n.numElementsType)
          emit(" ")
          encodeValue(n.numElements)
        }
        if (n.hasAlign) {
          emit(", align " + n.alignment)
        }

      case n: L_Load =>
        emit(ssa(n))
        emit(" = ")
        if (n.isVolatile) {
          emit("volatile ")
        }
        emit("load ")
        encodeType(n.typ)
        emit("* ")
        encodeValue(n.pointer)
        if (n.hasAlign) {
          emit(", align " + n.alignment)
        }
        if (n.nonTemporal) {
          emit(", !nontemporal !" + n.nonTempIndex)
        }

      case n: L_Store =>
        if (n.isVolatile) {
          emit("volatile ")
        }
        emitw("store")
        encodeType(resultType(n.value))
        emit(" ")
        encodeValue(n.value)
        emit(", ")
        encodeType(resultType(n.pointer))
        emit(" ") //pointer bug.
        encodeValue(n.pointer)
        if (n.hasAlign) {
          emit(", align " + n.alignment)
        }
        if (n.nonTemporal) {
          emit(", !nontemporal !" + n.nonTempIndex)
        }

      case n: L_GetElementPtr =>
        emit(ssa(n))
        emit(" = getelementptr ")
        if (n.inBounds) {
          emit("inbounds ")
        }
        encodeType(n.pty)
        emit(" ")
        encodeValue(n.pval)
        for (ti <- n.typeIndexes) {
          emit(", ")
          encodeType(resultType(ti))
          emit(" ")
          encodeValue(ti)
          /*
          encodeType(ti.ty)
          emit(" ")// + ti.idx)
          encodeValue(ti.idx)
          */
        }

      case n: L_ConversionOperation =>
        emit(ssa(n))
        emit(" = " + n.instructionString + " ")
        encodeType(resultType(n.value))
        emit(" ")
        encodeValue(n.value)
        emit(" to ")
        encodeType(n.targetType)

      case n: L_ICMP =>
        emit(ssa(n))
        emit(" = icmp " + n.compCode + " ")
        encodeType(resultType(n.LHS))
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_FCMP =>
        emit(ssa(n))
        emit(" = icmp " + n.compCode + " ")
        encodeType(resultType(n.LHS))
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_Phi =>
        emit(ssa(n))
        emit(" = phi ")
        encodeType(resultType(n.valueLabels.head))
        for (vlab <- n.valueLabels) {
          emit(" [ ")
          encodeValue(vlab.value)
          emit(" ")
          encodeLabel(vlab.label)
          emit(" ]")
        }

      case n: L_Select =>
        emit(ssa(n))
        emit(" = select ")
        encodeType(resultType(n.cond))
        emit(" ")
        encodeValue(n.cond)
        emit(", ")
        encodeType(resultType(n.val1))
        emit(" ")
        encodeValue(n.val1)
        emit(", ")
        encodeType(resultType(n.val2))
        emit(" ")
        encodeValue(n.val2)

      case n: L_Call =>
        emit(ssa(n))
        emit(" = ")
        if (n.tail) {
          emitw("tail")
        }
        emitw("call")
        if (n.callConvention.nonEmpty) {
          emitw(n.callConvention)
        }
        for (ra <- n.returnAttributes) {
          emitw(ra)
        }
        encodeType(n.typ)
        emit(" ")
        if (n.fnty.nonEmpty) {
          emit(n.fnty + "* ") // TODO : implement function type pointers properly.
        }
        encodeValue(n.fnptrval)
        val imax = n.fnargs.size
        var i = 1
        emit("( ")
        for (arg <- n.fnargs) {
          encodeArgument(arg)
          if (i < imax) {
            emit(", ")
          }
          i = i + 1
        }
        emit(" ) ")
        for (fnattr <- n.fnattrs) {
          emit(" " + fnattr)
        }

      case n: L_Va_Arg =>
        emit(ssa(n))
        emit(" = va_arg ")
        encodeValue(n.argPtr)
        emit("* ")
        encodeValue(n.argList)
        emit(", ")
        encodeType(n.argType)

      case _ => emit("Unknown Instruction : " + b)
    }
  }

  def encodeTerminator(t: L_TerminatorInstruction): Unit = {
    t match {
      case n: L_Ret =>
        emitw("ret")
        resultType(n.rvalue) match {
          case n2: L_VoidType => emit("void")
          case _ =>
            encodeType(resultType(n.rvalue))
            emit(" ")
            encodeValue(n.rvalue)
        }

      case n: L_Br =>
        emit("br label %")
        encodeLabel(n.dest)

      case n: L_BrCond =>
        emitw("br")
        encodeType(resultType(n.cond))
        emit(" ")
        encodeValue(n.cond)
        emit(", label %")
        encodeLabel(n.ifTrue)
        emit(", label %")
        encodeLabel(n.ifFalse)

      case n: L_Switch =>
        emitw("switch")
        encodeType(resultType(n.value))
        emit(" ")
        encodeValue(n.value)
        emit(" label %")
        encodeLabel(n.default)
        emit("[ ")
        for (valLab <- n.cases) {
          encodeType(resultType(valLab.value))
          emit(" ")
          encodeValue(valLab.value)
          emit(", label %")
          encodeLabel(valLab.label)
        }
        emit(" ]")

      case n: L_IndirectBr =>
        emitw("indirectbr")
        encodeType(resultType(n.address))
        emit("* ")
        encodeValue(n.address)
        emit(", [")
        for (lab <- n.possibleDestinations) {
          emit("label %")
          encodeLabel(lab)
        }
        emit(" ]")

      case n: L_Invoke =>
        emit(ssa(n))
        emit(" = invoke ")
        if (n.callConv.nonEmpty) {
          emit(n.callConv)
        }
        for (ra <- n.retAttrs) {
          emit(" " + ra)
        }
        encodeType(n.funcTypePtr) //TODO - make this work correctly with pointers
        emit(" ")
        encodeValue(n.funcPtrVal)
        emit(" (")
        for (arg <- n.args) {
          encodeArgument(arg)
        }
        emit(")")
        for (at <- n.attrs) {
          emit(" " + at)
        }
        emit(" to label %")
        encodeLabel(n.normal)
        emit(" unwind label %")
        encodeLabel(n.unwind)

      case n: L_Unwind =>
        emit("unwind")

      case n: L_Unreachable =>
        emit("unreachable")

      case _ => emit("Unknown Terminator Instruction : " + t)
    }
  }

  def encodeBlock(b: L_Block): Unit = {
    encodeLabel(b.label)
    emitln(":")
    for (instr <- b.instructions) {
      emit("  ")
      encodeInstruction(instr)
      emitln()
    }
    emit("  ")
    encodeTerminator(b.terminator)
    emitln()
    emitln()
  }

  def encodeFunctionDefinition(f: L_FunctionDefinition): Unit = {
    currentParamNum = 0
    currentSSA = 0
    emitw("define")
    emitw(f.linkage)
    emitw(f.visibilityStyle)
    emitw(f.callConvention)
    for (retattr <- f.returnAttributes) {
      emitw(retattr)
    }
    encodeType(f.returnType)
    emit(" ")
    emit(funcname(f))
    emit("(")

    val imax = f.arguments.size
    var i = 1
    for (a <- f.arguments) {
      encodeArgument(a)
      if (i < imax) {
        emit(", ")
      }
      i = i + 1
    }

    emitw(")")
    for (funcAtt <- f.funcAttributes) {
      emitw(funcAtt)
    }
    if (f.section.nonEmpty) {
      emitw("section " + '"' + f.section + '"')
    }
    if (f.alignment != 0) {
      emitw("align " + f.alignment)
    }
    if (f.garbageCollector.nonEmpty) {
      emitw("gc " + '"' + f.garbageCollector + '"')
    }
    emitln("{")

    for (b <- f.blocks) {
      encodeBlock(b)
    }

    emitln("}")
    emitln("")
  }

  def encodeFunctionDeclaration(f: L_FunctionDeclaration): Unit = {
    currentParamNum = 0
    currentSSA = 0
    emitw("declare")
    emitw(f.linkage)
    emitw(f.visibilityStyle)
    emitw(f.callConvention)
    for (retattr <- f.returnAttributes) {
      emitw(retattr)
    }
    encodeType(f.returnType)
    emit(" ")
    emit(funcname(f))
    emit("(")

    val imax = f.arguments.size
    var i = 1
    for (a <- f.arguments) {
      encodeType(resultType(a))
      if (i < imax) {
        emit(", ")
      }
      i = i + 1
    }

    emitw(")")
    if (f.alignment != 0) {
      emitw("align " + f.alignment)
    }
    if (f.garbageCollector.nonEmpty) {
      emitw("gc " + '"' + f.garbageCollector + '"')
    }
    emitln("")
  }

  def encodeType(t: L_Type): Unit = {
    t match {
      // Basic types.
      case n: L_IntType      => emit("i" + n.size)
      case n: L_FloatType    => emit("float")
      case n: L_DoubleType   => emit("double")
      case n: L_FP128Type    => emit("fp128")
      case n: L_X86FP80Type  => emit("x86fp80")
      case n: L_PPCFP128Type => emit("ppcfp128")
      case n: L_VoidType     => emit("void")
      case n: L_MetadataType => emit("metadata")
      case n: L_LabelType    => emit("label")
      case n: L_VarArgsType  => emit("...")

      // Derived types.
      case n: L_ArrayType =>
        emit("[" + n.numElements + " x ")
        encodeType(n.elementType)
        emit("]")

      case n: L_FunctionType =>
        encodeType(n.returnType)
        emit(" (")
        val imax = n.parameterList.size
        var i = 1
        for (param <- n.parameterList) {
          encodeType(param)
          if (i < imax) {
            emit(", ")
          }
          i = i + 1
        }
        emit(")")

      case n: L_StructureType =>
        emit("{ ")
        val imax = n.fields.size
        var i = 1
        for (field <- n.fields) {
          encodeType(field)
          if (i < imax) {
            emit(", ")
          }
          i = i + 1
        }
        emit(" }")

      case n: L_PackagedStructureType =>
        emit("< { ")
        val imax = n.fields.size
        var i = 1
        for (field <- n.fields) {
          encodeType(field)
          if (i < imax) {
            emit(", ")
          }
          i = i + 1
        }
        emit(" } >")

      case n: L_PointerType =>
        encodeType(n.pointer)
        emit("*")

      case n: L_VectorType =>
        emit("<" + n.numElements + " x ")
        encodeType(n.elementType)
        emit(">")

      case n: L_OpaqueType =>
        emit("opaque")

      case _ => "Unknown Type : " + t
    }
    //0
  }

  def encodeGlobalVariable(g: L_GlobalVariable): Unit = {
    emit(gvarname(g))
    emit(" =")
    if (g.linkage.nonEmpty)
      emit(" " + g.linkage)
    if (g.addressSpace != 0) {
      emit(" addrspace(" + g.addressSpace + ")")
    }
    if (g.isConstant) {
      emit(" constant")
    }
    emit(" ")
    encodeType(resultType(g.value))
    emit(" ")
    encodeValue(g.value)
    if (g.section.nonEmpty) {
      emit(", section " + g.section)
    }
    if (g.alignment != 0) {
      emit(", align " + g.alignment)
    }
    emitln()
    emitln()
  }

  def emitln(): Unit = {
    emit("\n")
  }

  def emitln(s: String): Unit = {
    emit(s + "\n")
  }

  def emit(s: String): Unit = {
    emitter.emit(s)
    if (fileOutputEnabled) {
      appendFile(s)
    }
  }

  def emitw(s: String): Unit = {
    if (s.length > 0) {
      emit(s + " ")
    }
  }

  def appendFile(s: String) {
    fileout = fileout + s
  }

}
