package org.kelseymountain.agc

import org.antlr.v4.runtime.ParserRuleContext

class ASTBuilder(private val reporter: Reporter) extends AugustaBaseVisitor[AST.ASTNode]:
  import org.kelseymountain.agc.AugustaParser.*
  import AST.*

  private def createPositionMessage(other: ASTNode, expected: String, ctx: ParserRuleContext): String =
    val start = ctx.getStart
    val position = s"at line ${start.getLine}, column ${start.getCharPositionInLine + 1}"
    s"ASTBuilder: expected $expected but found ${other.getClass.getSimpleName} $position"

  private def expectExpression(node: ASTNode, ctx: ParserRuleContext): Expression =
    node match
      case e: Expression => e
      case other =>
        throw InternalErrorException(createPositionMessage(other, "Expression", ctx))

  private def expectStatement(node: ASTNode, ctx: ParserRuleContext): Statement =
    node match
      case e: Statement => e
      case other =>
        throw InternalErrorException(createPositionMessage(other, "Statement", ctx))

  private def expectDeclaration(node: ASTNode, ctx: ParserRuleContext): Declaration =
    node match
      case e: Declaration => e
      case other =>
        throw InternalErrorException(createPositionMessage(other, "Declaration", ctx))

  override def visitObject_declaration(ctx: Object_declarationContext): ASTNode =
    val objectIdentifier = ctx.IDENTIFIER(0).getText
    val objectRegion = AST.SourceRegion(ctx.IDENTIFIER(0))
    val objectType = ctx.IDENTIFIER(1).getText
    val typeRegion = AST.SourceRegion(ctx.IDENTIFIER(1))
    ObjectDeclaration(objectIdentifier, objectType, None, AST.SourceRegion.span(objectRegion, typeRegion))

  override def visitPrimary_expression(ctx: Primary_expressionContext): ASTNode =
    if ctx.IDENTIFIER != null then
      Identifier(ctx.IDENTIFIER.getText, AST.SourceRegion(ctx.IDENTIFIER))
    else if ctx.INTEGER_LITERAL != null then
      IntegerLiteral(BigInt(ctx.INTEGER_LITERAL.getText), AST.SourceRegion(ctx.INTEGER_LITERAL))
    else if ctx.REAL_LITERAL != null then
      RealLiteral(BigDecimal(ctx.REAL_LITERAL.getText), AST.SourceRegion(ctx.REAL_LITERAL))
    else if ctx.BOOLEAN_LITERAL != null then
      BooleanLiteral(ctx.BOOLEAN_LITERAL.getText.toBoolean, AST.SourceRegion(ctx.BOOLEAN_LITERAL))
    else
      throw InternalErrorException(s"Unexpected primary expression context: $ctx")

  override def visitUnary_expression(ctx: Unary_expressionContext): ASTNode =
    val expressionNode = visit(ctx.primary_expression)
    val expression = expectExpression(expressionNode, ctx)
    val opToken =
      Option(ctx.NOT)
        .orElse(Option(ctx.PLUS))
        .orElse(Option(ctx.MINUS))
    opToken match
      case Some(tokenNode) =>
        val opText = tokenNode.getText
        val region = AST.SourceRegion(tokenNode)
        UnaryOperation(opText, expression, AST.SourceRegion.span(region, expression.region))
      case None =>
        expression

  override def visitMultiplicative_expression(ctx: Multiplicative_expressionContext): ASTNode =
    val rightNode = visit(ctx.unary_expression)
    val right = expectExpression(rightNode, ctx)
    val opToken =
      Option(ctx.MULTIPLY)
        .orElse(Option(ctx.DIVIDE))
        .orElse(Option(ctx.REM))
    opToken match
      case Some(tokenNode) =>
        val leftNode = visit(ctx.multiplicative_expression)
        val left = expectExpression(leftNode, ctx)
        val opText = tokenNode.getText
        BinaryOperation(left, opText, right, AST.SourceRegion.span(left.region, right.region))
      case None =>
        right

  override def visitAdditive_expression(ctx: Additive_expressionContext): ASTNode =
    val rightNode = visit(ctx.multiplicative_expression)
    val right = expectExpression(rightNode, ctx)
    val opToken =
      Option(ctx.PLUS)
        .orElse(Option(ctx.MINUS))
    opToken match
      case Some(tokenNode) =>
        val leftNode = visit(ctx.additive_expression)
        val left = expectExpression(leftNode, ctx)
        val opText = tokenNode.getText
        BinaryOperation(left, opText, right, AST.SourceRegion.span(left.region, right.region))
      case None =>
        right

  override def visitRelational_expression(ctx: Relational_expressionContext): ASTNode =
    val rightNode = visit(ctx.additive_expression)
    val right = expectExpression(rightNode, ctx)
    val opToken =
      Option(ctx.EQUAL)
        .orElse(Option(ctx.NOT_EQUAL))
        .orElse(Option(ctx.LESS))
        .orElse(Option(ctx.LESS_EQUAL))
        .orElse(Option(ctx.GREATER))
        .orElse(Option(ctx.GREATER_EQUAL))
    opToken match
      case Some(tokenNode) =>
        val leftNode = visit(ctx.relational_expression)
        val left = expectExpression(leftNode, ctx)
        val opText = tokenNode.getText
        BinaryOperation(left, opText, right, AST.SourceRegion.span(left.region, right.region))
      case None =>
        right

  override def visitAnd_expression(ctx: And_expressionContext): ASTNode =
    val rightNode = visit(ctx.relational_expression)
    val right = expectExpression(rightNode, ctx)
    val opToken = Option(ctx.AND)
    opToken match
      case Some(tokenNode) =>
        val leftNode = visit(ctx.and_expression)
        val left = expectExpression(leftNode, ctx)
        val opText = tokenNode.getText
        BinaryOperation(left, opText, right, AST.SourceRegion.span(left.region, right.region))
      case None =>
        right

  override def visitOr_expression(ctx: Or_expressionContext): ASTNode =
    val rightNode = visit(ctx.and_expression)
    val right = expectExpression(rightNode, ctx)
    val opToken = Option(ctx.OR)
    opToken match
      case Some(tokenNode) =>
        val leftNode = visit(ctx.or_expression)
        val left = expectExpression(leftNode, ctx)
        val opText = tokenNode.getText
        BinaryOperation(left, opText, right, AST.SourceRegion.span(left.region, right.region))
      case None =>
        right

  override def visitExpression(ctx: ExpressionContext): ASTNode =
    visit(ctx.or_expression)
