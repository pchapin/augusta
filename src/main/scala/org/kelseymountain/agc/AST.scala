package org.kelseymountain.agc

import org.antlr.v4.runtime.tree.TerminalNode

object AST:
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

  object SourceRegion:
    /**
     * Creates a SourceRegion from a TerminalNode. This is used in AST construction from ANTLR
     * parse trees.
     *
     * @param ctx The TerminalNode from which to extract the region.
     * @return A SourceRegion representing the position of the TerminalNode.
     */
    def apply(ctx: TerminalNode): SourceRegion =
      val token = ctx.getSymbol
      val start = SourcePosition(
        token.getLine,
        token.getCharPositionInLine,
        token.getStartIndex)
      val end = SourcePosition(
        token.getLine,
        token.getCharPositionInLine + token.getText.length,
        token.getStopIndex + 1)
      SourceRegion(start, end)

    /**
     * Combines two SourceRegions into one that spans both.
     * @param a The first SourceRegion starting earlier in the source.
     * @param b The second SourceRegion starting later in the source.
     * @return A new SourceRegion that spans from the start of `a` to the end of `b`.
     */
    def span(a: SourceRegion, b: SourceRegion): SourceRegion =
      SourceRegion(a.start, b.end)

  end SourceRegion

  // A temporary initial region for testing purposes.
  val initialRegion = SourceRegion(
    SourcePosition(1, 0, 0),
    SourcePosition(1, 0, 0)
  )

  /**
   * A trait for AST nodes that have a source code position.
   */
  trait Positioned {
    val region: SourceRegion
  }

  sealed trait ASTNode extends Positioned
  sealed trait Declaration extends ASTNode
  sealed trait Statement extends ASTNode
  sealed trait Expression extends ASTNode

  // Expressions
  case class Identifier
    (name: IdentifierName, region: SourceRegion) extends Expression

  case class IntegerLiteral
    (value: BigInt, region: SourceRegion) extends Expression

  case class RealLiteral
    (value: BigDecimal, region: SourceRegion) extends Expression

  case class BooleanLiteral
    (value: Boolean, region: SourceRegion) extends Expression

  case class UnaryOperation
    (operator: String, expression: Expression, region: SourceRegion) extends Expression

  case class BinaryOperation
    (left: Expression, operator: String, right: Expression, region: SourceRegion) extends Expression

  // Statements
  case class Block
    (statements: Seq[Statement], region: SourceRegion) extends Statement

  case class AssignmentStatement
    (variable: Identifier, value: Expression, region: SourceRegion) extends Statement

  case class DeclareStatement
    (declarations: Seq[Declaration], statements: Block, region: SourceRegion) extends Statement

  case class IfStatement
    (condition: Expression, thenBranch: Block, elseBranch: Option[Block], region: SourceRegion) extends Statement

  case class WhileStatement
    (condition: Expression, body: Block, region: SourceRegion) extends Statement

  // Declarations
  case class ObjectDeclaration
    (objectName: IdentifierName, objectType: TypeName, initializer: Option[Expression], region: SourceRegion) extends Declaration

  case class ParameterDeclaration
    (parameterName: IdentifierName, parameterType: TypeName, region: SourceRegion) extends Declaration

  case class ProcedureDeclaration
    (procedureName: IdentifierName, parameters: Seq[ParameterDeclaration], body: Block, region: SourceRegion) extends Declaration

  case class FunctionDeclaration
    (functionName: IdentifierName, parameters: Seq[ParameterDeclaration], returnType: TypeName, body: Block, region: SourceRegion) extends Declaration

end AST
