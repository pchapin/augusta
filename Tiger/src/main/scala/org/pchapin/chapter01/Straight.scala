//-----------------------------------------------------------------------
// FILE    : Straight.scala
// SUBJECT : Interpreter for the "straight line" language in MCIML Chapter 1.
// AUTHOR  : (C) Copyright 2012 by Peter C. Chapin <PChapin@vtc.edu.edu>
//
//-----------------------------------------------------------------------
package org.pchapin.chapter01

object Straight {

  sealed abstract class ASTNode

  sealed abstract class BinaryOperator extends ASTNode
  case object Plus     extends BinaryOperator
  case object Minus    extends BinaryOperator
  case object Multiply extends BinaryOperator
  case object Divide   extends BinaryOperator

  type Identifier = String
  type NumericLiteral = Int

  sealed abstract class Statement extends ASTNode
  case class CompoundStatement(s1: Statement,  s2: Statement) extends Statement
  case class AssignmentStatement(target: Identifier, source: Expression) extends Statement
  case class PrintStatement(arguments: List[Expression]) extends Statement

  sealed abstract class Expression extends ASTNode
  case class IdentifierExpression(id: Identifier) extends Expression
  case class NumericLiteralExpression(value: NumericLiteral) extends Expression
  case class OperationExpression(e1: Expression, operation: BinaryOperator, e2: Expression)
    extends Expression
  case class SequenceExpression(s: Statement, e: Expression) extends Expression

  /**
   * Computes the largest number of arguments in any print statement embedded in
   * topLevelStatement.
   *
   * @param topLevelStatement The statement to analyze. It does not need to be a print statement
   * but it can (and probably does) contain embedded print statements.
   *
   * @return The number of arguments in the print statement containing the most arguments. Zero
   * is returned if there are no print statements embedded in topLevelStatement (or if all
   * embedded print statements have empty argument lists).
   */
  def maximumPrintArgumentCount(topLevelStatement: Statement): NumericLiteral = {

    // This helper method is needed since it must process both statements and expressions.
    // It is necessary to descend into expressions because an expression can contain embedded
    // statements.
    //
    def ASTWalker(node: ASTNode): Int = {

      node match {
        // This is the interesting case.
        case PrintStatement(arguments) =>
          val maximumsList = arguments map { ASTWalker }
          val subordinateMaximum = maximumsList.foldLeft(0)( math.max )
          math.max(subordinateMaximum, arguments.size)

        // The base cases.
        case IdentifierExpression(id)        => 0
        case NumericLiteralExpression(value) => 0

        // The "pass through" recursive cases. In a large language there would be a huge number
        // of these.
        //
        case CompoundStatement(s1, s2)      => math.max(ASTWalker(s1), ASTWalker(s2))
        case AssignmentStatement(_, source) => ASTWalker(source)
        case OperationExpression(e1, _, e2) => math.max(ASTWalker(e1), ASTWalker(e2))
        case SequenceExpression(s, e)       => math.max(ASTWalker(s), ASTWalker(e))

        // These cases should never occur.
        case Divide | Minus | Multiply | Plus =>
          throw new Exception("Internal Error: ASTWalker called on binary operator!")
      }
    }

    ASTWalker(topLevelStatement)
  }

  // Not a very elegant data structure for maintaining symbol information, but it will work.
  type SymbolTable = List[(String,  Int)]

  /**
   * Exception thrown when an undefined identifier is encountered during interpretation.
   */
  class UndefinedIdentifierException(message: String) extends Exception(message)


  def interpret(program: Statement): Unit = {
    // The environment of the program itself contains no symbols. The program is a closed term.
    interpretStatement(program, List()): @annotation.nowarn("msg=discarded non-Unit value")
  }


  def interpretStatement(statement: Statement, table: SymbolTable): SymbolTable = {
    statement match {
      case CompoundStatement(s1, s2) =>
        val symbolsAfterStatement1 = interpretStatement(s1, table)
        interpretStatement(s2, symbolsAfterStatement1)

      case AssignmentStatement(target, source) =>
        val (expressionValue, expressionTable) = interpretExpression(source, table)
        (target, expressionValue) :: expressionTable

      // This case has side effects.
      case PrintStatement(arguments) =>
        val overallTable = arguments.foldLeft(table)( (currentTable, e) => {
          val (expressionResult, expressionTable) = interpretExpression(e, currentTable)
          print(s"$expressionResult ")
          expressionTable
        })
        print("\n")
        overallTable
    }
  }


  def interpretExpression(expression: Expression, table: SymbolTable): (Int,  SymbolTable) = {
    expression match {
      case IdentifierExpression(id) =>
        val possibleTableEntry = table.find( entry => id == entry._1 )
        val entry = possibleTableEntry.getOrElse(
          throw new UndefinedIdentifierException("Undefined identifier: " + id))
        (entry._2, table)

      case NumericLiteralExpression(value) =>
        (value, table)

      case OperationExpression(e1, operation, e2) =>
        val (leftResult, leftTable) = interpretExpression(e1, table)
        val (rightResult, rightTable) = interpretExpression(e2, leftTable)
        // This is where the rubber hits the road.
        // TODO: What should we do about overflow or division by zero?
        operation match {
          case Plus     => (leftResult + rightResult, rightTable)
          case Minus    => (leftResult - rightResult, rightTable)
          case Multiply => (leftResult * rightResult, rightTable)
          case Divide   => (leftResult / rightResult, rightTable)
        }

      case SequenceExpression(s, e) =>
        val symbolsAfterStatement = interpretStatement(s, table)
        interpretExpression(e, symbolsAfterStatement)
    }
  }
}
