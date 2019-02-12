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

class IRTreeEncoder(emitter: Emitter) {

  import org.bitbucket.inkytonik.kiama.attribution.Attribution._
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

  def reset() = {
    fileout = ""
    currentParamNum = 0
    currentSSA = 0
  }

  def encodeTree(p: L_Program) = {
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

  def writeFile() = {
    val fw = new FileWriter("LLVMIR/newprog.ll")
    fw.write(fileout)
    fw.close()
  }

  def encodeGlobal(g: L_Global): Int = {
    g match {
      case v: L_GlobalVariable => encodeGlobalVariable(v)
      case f: L_FunctionReference => encodeGlobal(f.funcPtr)
      case f: L_FunctionDeclaration => encodeFunctionDeclaration(f)
      case f: L_FunctionDefinition => encodeFunctionDefinition(f)
      case _ =>
    }
    0
  }

  def encodeArgument(a: L_Argument) = {
    encodeType(a.ty)
    emit(" ")
    if (a.value == null) {
      emit(a -> paramName)
    }
    else {
      encodeValue(a.value)
    }

    for (attrib <- a.attrs) {
      emit(" ")
      emit(attrib)
    }
  }

  def encodeConstant(c: L_Constant) {
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
          encodeType(e -> resultType)
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
          encodeType(e -> resultType)
          emit(" ")
          encodeValue(e)
          if (i < imax) {
            emit(", ")
          }
        }
        emit(" ]")

      case n: L_String =>
        emit("c" + '"' + n.s + '"')

      case n: L_ZeroInitialiser =>
        emit("zeroinitialiser")

      case n: L_Vector =>
        emit("< ")
        val imax = n.elements.size
        val i = 1
        for (e <- n.elements) {
          encodeType(e -> resultType)
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

  def encodeLabel(l: L_Label) = {
    emit(l -> labelname)
  }

  def encodeValue(v: L_Value): Int = {
    v match {
      case n: L_Instruction         => emit(n -> ssa)
      case n: L_Argument            => emit(n -> paramName)
      case n: L_GlobalVariable      => emit(n -> gvarname)
      case n: L_Constant            => encodeConstant(n)
      case n: L_FunctionReference   => encodeValue(n.funcPtr)
      case n: L_FunctionDefinition  => emit(n -> funcname)
      case n: L_FunctionDeclaration => emit(n -> funcname)
      case _ => emit("UnknownValue : " + v)
    }
    0
  }


  def encodeInstruction(b: L_Instruction) = {
    b match {
      case n: L_BinOpInstruction =>
        emit(n -> ssa)
        emit(" = ")
        emitw(n.instructionString)
        encodeType(n.LHS -> resultType)
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_ExtractElement =>
        emit(n -> ssa)
        emit(" = extractelement ")
        encodeType(n.vec -> resultType)
        emit(" ")
        encodeValue(n.vec)
        emit(", i32 ")
        emit("" + n.idx)

      case n: L_InsertElement =>
        emit(n -> ssa)
        emit(" = insertelement ")
        encodeType(n.vec -> resultType)
        emit(" ")
        encodeValue(n.vec)
        emit(", ")
        encodeType(n.elt -> resultType)
        emit(" ")
        encodeValue(n.elt)
        emit(", i32 ")
        encodeValue(n.idx)

      case n: L_ShuffleVector =>
        emit(n -> ssa)
        emit(" = shufflevector ")
        encodeType(n.v1 -> resultType)
        emit(" ")
        encodeValue(n.v1)
        emit(", ")
        encodeType(n.v2 -> resultType)
        emit(" ")
        encodeValue(n.v2)
        emit(", ")
        encodeType(n.mask -> resultType)
        emit(" ")
        encodeValue(n.mask)

      case n: L_ExtractValue =>
        emit(n -> ssa)
        emit(" = extractvalue ")
        encodeType(n.value -> resultType)
        emit(" ")
        encodeValue(n.value)
        for (idx <- n.indexes) {
          emit(", ")
          encodeValue(idx)
        }

      case n: L_InsertValue =>
        emit(n -> ssa)
        emit(" = insertvalue ")
        encodeType(n.value -> resultType)
        emit(" ")
        encodeValue(n.value)
        emit(", ")
        encodeType(n.elt -> resultType)
        emit(" ")
        encodeValue(n.elt)
        emit(", ")
        encodeValue(n.idx)

      case n: L_Alloca =>
        emit(n -> ssa)
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
        emit(n -> ssa)
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
        encodeType(n.value -> resultType)
        emit(" ")
        encodeValue(n.value)
        emit(", ")
        encodeType(n.pointer -> resultType)
        emit(" ") //pointer bug.
        encodeValue(n.pointer)
        if (n.hasAlign) {
          emit(", align " + n.alignment)
        }
        if (n.nonTemporal) {
          emit(", !nontemporal !" + n.nonTempIndex)
        }

      case n: L_GetElementPtr =>
        emit(n -> ssa)
        emit(" = getelementptr ")
        if (n.inBounds) {
          emit("inbounds ")
        }
        encodeType(n.pty)
        emit(" ")
        encodeValue(n.pval)
        for (ti <- n.typeIndexes) {
          emit(", ")
          encodeType(ti -> resultType)
          emit(" ")
          encodeValue(ti)
          /*
          encodeType(ti.ty)
          emit(" ")// + ti.idx)
          encodeValue(ti.idx)
          */
        }

      case n: L_ConversionOperation =>
        emit(n -> ssa)
        emit(" = " + n.instructionString + " ")
        encodeType(n.value -> resultType)
        emit(" ")
        encodeValue(n.value)
        emit(" to ")
        encodeType(n.targetType)

      case n: L_ICMP =>
        emit(n -> ssa)
        emit(" = icmp " + n.compCode + " ")
        encodeType(n.LHS -> resultType)
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_FCMP =>
        emit(n -> ssa)
        emit(" = icmp " + n.compCode + " ")
        encodeType(n.LHS -> resultType)
        emit(" ")
        encodeValue(n.LHS)
        emit(", ")
        encodeValue(n.RHS)

      case n: L_Phi =>
        emit(n -> ssa)
        emit(" = phi ")
        encodeType(n.valueLabels.head -> resultType)
        for (vlab <- n.valueLabels) {
          emit(" [ ")
          encodeValue(vlab.value)
          emit(" ")
          encodeLabel(vlab.label)
          emit(" ]")
        }

      case n: L_Select =>
        emit(n -> ssa)
        emit(" = select ")
        encodeType(n.cond -> resultType)
        emit(" ")
        encodeValue(n.cond)
        emit(", ")
        encodeType(n.val1 -> resultType)
        emit(" ")
        encodeValue(n.val1)
        emit(", ")
        encodeType(n.val2 -> resultType)
        emit(" ")
        encodeValue(n.val2)

      case n: L_Call =>
        emit(n -> ssa)
        emit(" = ")
        if (n.tail) {
          emitw("tail")
        }
        emitw("call")
        if (n.callConvention.size > 0) {
          emitw(n.callConvention)
        }
        for (ra <- n.returnAttributes) {
          emitw(ra)
        }
        encodeType(n.typ)
        emit(" ")
        if (n.fnty.size > 0) {
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
        emit(n -> ssa)
        emit(" = va_arg ")
        encodeValue(n.argPtr)
        emit("* ")
        encodeValue(n.argList)
        emit(", ")
        encodeType(n.argType)

      case _ => emit("Unknown Instruction : " + b)
    }
  }

  def encodeTerminator(t: L_TerminatorInstruction) = {
    t match {
      case n: L_Ret =>
        emitw("ret")
        n.rvalue -> resultType match {
          case n2: L_VoidType => emit("void")
          case _ =>
            encodeType(n.rvalue -> resultType)
            emit(" ")
            encodeValue(n.rvalue)
        }

      case n: L_Br =>
        emit("br label %")
        encodeLabel(n.dest)

      case n: L_BrCond =>
        emitw("br")
        encodeType(n.cond -> resultType)
        emit(" ")
        encodeValue(n.cond)
        emit(", label %")
        encodeLabel(n.ifTrue)
        emit(", label %")
        encodeLabel(n.ifFalse)

      case n: L_Switch =>
        emitw("switch")
        encodeType(n.value -> resultType)
        emit(" ")
        encodeValue(n.value)
        emit(" label %")
        encodeLabel(n.default)
        emit("[ ")
        for (valLab <- n.cases) {
          encodeType(valLab.value -> resultType)
          emit(" ")
          encodeValue(valLab.value)
          emit(", label %")
          encodeLabel(valLab.label)
        }
        emit(" ]")

      case n: L_IndirectBr =>
        emitw("indirectbr")
        encodeType(n.address -> resultType)
        emit("* ")
        encodeValue(n.address)
        emit(", [")
        for (lab <- n.possibleDestinations) {
          emit("label %")
          encodeLabel(lab)
        }
        emit(" ]")

      case n: L_Invoke =>
        emit(n -> ssa)
        emit(" = invoke ")
        if (n.callConv.size > 0) {
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

  def encodeBlock(b: L_Block) = {
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

  def encodeFunctionDefinition(f: L_FunctionDefinition) = {
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
    emit(f -> funcname)
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
    if (f.section.size > 0) {
      emitw("section " + '"' + f.section + '"')
    }
    if (f.alignment != 0) {
      emitw("align " + f.alignment)
    }
    if (f.garbageCollector.size > 0) {
      emitw("gc " + '"' + f.garbageCollector + '"')
    }
    emitln("{")

    for (b <- f.blocks) {
      encodeBlock(b)
    }

    emitln("}")
    emitln("")
  }

  def encodeFunctionDeclaration(f: L_FunctionDeclaration) = {
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
    emit(f -> funcname)
    emit("(")

    val imax = f.arguments.size
    var i = 1
    for (a <- f.arguments) {
      encodeType(a -> resultType)
      if (i < imax) {
        emit(", ")
      }
      i = i + 1
    }

    emitw(")")
    if (f.alignment != 0) {
      emitw("align " + f.alignment)
    }
    if (f.garbageCollector.size > 0) {
      emitw("gc " + '"' + f.garbageCollector + '"')
    }
    emitln("")
  }

  def encodeType(t: L_Type): Int = {
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
      case n: L_VarArgsType   => emit("...")

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
    0
  }

  def encodeGlobalVariable(g: L_GlobalVariable) = {
    emit(g -> gvarname)
    emit(" =")
    if (g.linkage.size > 0)
      emit(" " + g.linkage)
    if (g.addressSpace != 0) {
      emit(" addrspace(" + g.addressSpace + ")")
    }
    if (g.isConstant) {
      emit(" constant")
    }
    emit(" ")
    encodeType(g.value -> resultType)
    emit(" ")
    encodeValue(g.value)
    if (g.section.size > 0) {
      emit(", section " + g.section)
    }
    if (g.alignment != 0) {
      emit(", align " + g.alignment)
    }
    emitln()
    emitln()
  }

  def emitln() = {
    emit("\n")
  }

  def emitln(s: String) = {
    emit(s + "\n")
  }

  def emit(s: String) = {
    emitter.emit(s)
    if (fileOutputEnabled) {
      appendFile(s)
    }
  }

  def emitw(s: String) = {
    if (s.length > 0) {
      emit(s + " ")
    }
  }

  def appendFile(s: String) {
    fileout = fileout + s
  }

}
