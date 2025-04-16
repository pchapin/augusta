package org.kelseymountain.agc

/**
 * Represents a position in the source code.
 * @param line The line number (1-based).
 * @param column The column number (0-based).
 * @param offset The offset from the start of the source file (0-based).
 */
case class SourcePosition(line: Int, column: Int, offset: Int)

/**
 * Represents a region in the source code.
 * @param start The starting position of the region.
 * @param end The position just past the end of the region.
 */
case class SourceRegion(start: SourcePosition, end: SourcePosition)

// A temporary dummy region for testing purposes.
val dummyRegion = SourceRegion(
  SourcePosition(1, 0, 0),
  SourcePosition(1, 0, 0)
)

trait Positioned {
  val region: SourceRegion
}

sealed trait ASTNode
sealed trait Declaration extends ASTNode
sealed trait Statement extends ASTNode
sealed trait Expression extends ASTNode

// Expressions
case class Identifier(name: IdentifierName) extends Expression
case class IntegerLiteral(value: BigInt) extends Expression
case class RealLiteral(value: BigDecimal) extends Expression
case class UnaryOperation(operator: String, expression: Expression) extends Expression
case class BinaryOperation(left: Expression, operator: String, right: Expression) extends Expression

// Statements
case class Block(statements: Seq[Statement]) extends Statement
case class AssignmentStatement(variable: Identifier, value: Expression) extends Statement
case class IfStatement(condition: Expression, thenBranch: Block, elseBranch: Option[Block]) extends Statement
case class DeclareStatement(declarations: Seq[Declaration], statements: Block) extends Statement
case class WhileStatement(condition: Expression, body: Block) extends Statement

// Declarations
case class ParameterDeclaration(parameterName: IdentifierName, parameterType: TypeName) extends Declaration
case class ProcedureDeclaration(procedureName: IdentifierName, parameters: Seq[ParameterDeclaration], body: Block) extends Declaration
case class FunctionDeclaration(functionName: IdentifierName, parameters: Seq[ParameterDeclaration], returnType: TypeName, body: Block) extends Declaration
case class ObjectDeclaration(objectName: IdentifierName, objectType: TypeName) extends Declaration

// begin
//    X := X + 1;
//    if X > 10 then
//      X := 0;
//    end if;
// end

val myAST = Block(
  Seq(
    AssignmentStatement(
      Identifier("X"),
      BinaryOperation(
        Identifier("X"),
        "+",
        IntegerLiteral(1)
      )
    ),
    IfStatement(
      BinaryOperation(
        Identifier("X"),
        ">",
        IntegerLiteral(10)
      ),
      Block(
        Seq(
          AssignmentStatement(
            Identifier("X"),
            IntegerLiteral(0)
          )
        )
      ),
      None
    )
  )
)
