package org.kelseymountain.dragon

object LLVMAbstractSyntax {

  sealed abstract class AST

  /**
   * An Instruction is an AST node that returns a result as a named temporary. Many instructions
   * take instructions as parameters, i.e., the temporary containing the result of previous
   * computations.
   */
  abstract class Instruction extends AST

  /**
   * An InstructionSequence is an AST node that contains a sequence of instructions. The
   * instructions are executed in order without any outgoing or incoming branches. The body of
   * a basic block could be represented as an instruction sequence.
   *
   * @param sequence An ordered sequence of Instruction nodes. Each instruction node could
   *                 be, but does not need to be, a significant abstract syntax tree of its own.
   */
  case class InstructionSequence(sequence : Seq[Instruction]) extends AST

  /**
   * A Declaration is an AST node representing LLVM declarations. It will likely need parameters
   * at some point (declared name? type? etc.?)
   */
  abstract class Declaration extends AST

  // What follows are case classes for the various instruction types.

  // Unary Operations. See: https://llvm.org/docs/LangRef.html#unary-operations
  case class FNegInstruction(operand: Instruction) extends Instruction

  // Binary Operations. See: https://llvm.org/docs/LangRef.html#binary-operations
  case class AddInstruction(left: Instruction, right: Instruction) extends Instruction
  case class FAddInstruction(left: Instruction, right: Instruction) extends Instruction
  case class SubInstruction(left: Instruction, right: Instruction) extends Instruction
  case class FSubInstruction(left: Instruction, right: Instruction) extends Instruction
  case class MulInstruction(left: Instruction, right: Instruction) extends Instruction
  case class FMulInstruction(left: Instruction, right: Instruction) extends Instruction
  case class UDivInstruction(left: Instruction, right: Instruction) extends Instruction
  case class SDivInstruction(left: Instruction, right: Instruction) extends Instruction
  case class FDivInstruction(left: Instruction, right: Instruction) extends Instruction
  case class URemInstruction(left: Instruction, right: Instruction) extends Instruction
  case class SRemInstruction(left: Instruction, right: Instruction) extends Instruction
  case class FRemInstruction(left: Instruction, right: Instruction) extends Instruction

  // Bitwise Binary Operations. See: https://llvm.org/docs/LangRef.html#bitwise-binary-operations
  case class ShlInstruction(left: Instruction, right: Instruction) extends Instruction
  case class LShrInstruction(left: Instruction, right: Instruction) extends Instruction
  case class AShrInstruction(left: Instruction, right: Instruction) extends Instruction
  case class AndInstruction(left: Instruction, right: Instruction) extends Instruction
  case class OrInstruction(left: Instruction, right: Instruction) extends Instruction
  case class XorInstruction(left: Instruction, right: Instruction) extends Instruction

  // Memory Access and Addressing Operations. See: https://llvm.org/docs/LangRef.html#memory-access-and-addressing-operations
  case class AllocaInstruction(varName: String) extends Instruction
  case class LoadInstruction(varName: String) extends Instruction
  case class ConstantInstruction(constantValue: Int) extends Instruction

  // Store isn't really an instruction because it doesn't return a temporary. However, the
  // "temporary" it returns should never be used, so it doesn't matter. Damn imperative languages!
  case class StoreInstruction(value: Instruction, varName: String) extends Instruction

  // Used to mark unimplemented things. Ultimately, this should be removed.
  case class NullInstruction(message: String) extends Instruction

  private var tempNumber = 0

  // Each time generateTemp is called, it returns a "fresh" temporary name.
  private def generateTemp(): String = {
    tempNumber += 1
    "%tmp" + tempNumber
  }

  def makeConcrete(abstractSyntax: Instruction): String = {
    abstractSyntax match {
      case AllocaInstruction(name) =>
        val temp = "%" + name + "_ptr"
        println(temp + " = alloca i32, i32 1")
        temp

      case LoadInstruction(varName) =>
        val temp = generateTemp()
        println(temp + " = load i32, i32* %" + varName + "_ptr")
        temp

      case ConstantInstruction(value) =>
        val temp = generateTemp()
        println(temp + " = " + value)
        temp

      case StoreInstruction(value, varName) =>
        val rightHandSide = makeConcrete(value)
        println("store i32 " + rightHandSide + ", i32* " + "%" + varName + "_ptr")
        "***NOT USED***"

      case AddInstruction(left, right) =>
        val temp = generateTemp()
        val leftTemp = makeConcrete(left)
        val rightTemp = makeConcrete(right)
        println(temp + " = add i32 " + leftTemp + ", " + rightTemp)
        temp

      case SubInstruction(left, right) =>
        val temp = generateTemp()
        val leftTemp = makeConcrete(left)
        val rightTemp = makeConcrete(right)
        println(temp + " = sub i32 " + leftTemp + ", " + rightTemp)
        temp

      case MulInstruction(left, right) =>
        val temp = generateTemp()
        val leftTemp = makeConcrete(left)
        val rightTemp = makeConcrete(right)
        println(temp + " = mul i32 " + leftTemp + ", " + rightTemp)
        temp
    }
  }

}
