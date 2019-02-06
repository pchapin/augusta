package org.pchapin.augusta

import org.pchapin.augusta.SymbolTable.SymbolTableException
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.ParserRuleContext

class SemanticAnalyzer(
  symbolTable: BasicSymbolTable,
  reporter   : Reporter) extends AdaBaseListener {

  import AdaTypes._

  // Processing High Level Grammar
  // -----------------------------

  override def enterProcedure_definition(ctx: AdaParser.Procedure_definitionContext): Unit = {
    val procedureName = ctx.IDENTIFIER(0)

    if (procedureName.getText != "Main") {
      reporter.reportError(
        procedureName.getSymbol.getLine, procedureName.getSymbol.getCharPositionInLine + 1,
        "The name of the main procedure must be \"Main\"")
    }
  }

  override def exitProcedure_definition(ctx: AdaParser.Procedure_definitionContext): Unit = {
    if (ctx.IDENTIFIER.size == 2) {
      val beginName = ctx.IDENTIFIER(0)
      val endName = ctx.IDENTIFIER(1)

      if (beginName.getText != endName.getText) {
        reporter.reportError(
          endName.getSymbol.getLine, endName.getSymbol.getCharPositionInLine + 1,
          "The closing name must match the procedure name")
      }
    }
  }

  // Processing Declarations
  // -----------------------

  override def exitObject_declaration(ctx: AdaParser.Object_declarationContext): Unit = {
    val id = ctx.IDENTIFIER(0)
    val typeName = ctx.IDENTIFIER(1)

    try {
      symbolTable.addObjectName(id.getText, typeName.getText)
    }
    catch {
      case ex: SymbolTable.UnknownTypeNameException =>
        // Type used in the declaration has not been declared.
        reporter.reportError(
          typeName.getSymbol.getLine, typeName.getSymbol.getCharPositionInLine + 1, ex.getMessage)

      case ex: SymbolTable.SymbolTableException =>
        // Duplicate declaration or name already declared as a type.
        reporter.reportError(
          id.getSymbol.getLine, id.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
  }


  // Processing Statements
  // ---------------------


  // Processing Expressions
  // ----------------------

  private def typeError(message: String, node: TerminalNode): Unit = {
    reporter.reportError(
      node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, message)
  }


  private def getTopNode(ctx: ParserRuleContext): TerminalNode = {
    import AdaParser._

    ctx match {
      case ctx: Primary_expressionContext =>
        if      (ctx.IDENTIFIER      != null) ctx.IDENTIFIER
        else if (ctx.NUMERIC_LITERAL != null) ctx.NUMERIC_LITERAL
        else getTopNode(ctx.expression)

      case ctx: Multiplicative_expressionContext =>
        if      (ctx.MULTIPLY != null) ctx.MULTIPLY
        else if (ctx.DIVIDE   != null) ctx.DIVIDE
        else getTopNode(ctx.primary_expression)

      case ctx: Unary_expressionContext =>
        if      (ctx.PLUS  != null) ctx.PLUS
        else if (ctx.MINUS != null) ctx.MINUS
        else getTopNode(ctx.multiplicative_expression)

      case ctx: Additive_expressionContext =>
        if      (ctx.PLUS  != null) ctx.PLUS
        else if (ctx.MINUS != null) ctx.MINUS
        else getTopNode(ctx.unary_expression)

      case ctx: ExpressionContext =>
        getTopNode(ctx.additive_expression)

      case _ =>
        throw new Reporter.InternalErrorException(
          "Unexpected parse tree context while searching for a top level expression")
    }
  }


  override def exitPrimary_expression(ctx: AdaParser.Primary_expressionContext): Unit = {
    val identifier = ctx.IDENTIFIER

    try {
      if (identifier != null) {
        ctx.expressionType = symbolTable.getObjectType(identifier.getText)
      }
      else if (ctx.NUMERIC_LITERAL != null) {
        // TODO: Numeric literals should really have a special "Universal Integer" type.
        ctx.expressionType = "Integer"
      }
      else {
        ctx.expressionType = ctx.expression.expressionType
      }
    }
    catch {
      case ex: SymbolTable.SymbolTableException =>
        // Undefined identifier in an expression.
        reporter.reportError(
          identifier.getSymbol.getLine,
          identifier.getSymbol.getCharPositionInLine + 1,
          ex.getMessage)
        ctx.expressionType = "Integer"   // Error recovery.
    }
  }


  override def exitMultiplicative_expression(ctx: AdaParser.Multiplicative_expressionContext): Unit = {
    if (ctx.MULTIPLY == null && ctx.DIVIDE == null) {
      ctx.expressionType = ctx.primary_expression.expressionType
    }
    else {
      val actualLeftType = ctx.multiplicative_expression.expressionType
      val actualRightType = ctx.primary_expression.expressionType
      if (actualLeftType == actualRightType) {
        ctx.expressionType = actualLeftType
      }
      else {
        typeError("Type mismatch", getTopNode(ctx))
        ctx.expressionType = actualLeftType   // Error recovery.
      }
    }
  }


  override def exitUnary_expression(ctx: AdaParser.Unary_expressionContext): Unit = {
    if (ctx.PLUS == null && ctx.MINUS == null) {
      ctx.expressionType = ctx.multiplicative_expression.expressionType
    }
    else {
      val actualType = ctx.multiplicative_expression.expressionType
        ctx.expressionType = actualType
    }
  }


  override def exitAdditive_expression(ctx: AdaParser.Additive_expressionContext): Unit = {
    if (ctx.PLUS == null && ctx.MINUS == null) {
      ctx.expressionType = ctx.unary_expression.expressionType
    }
    else {
      val actualLeftType = ctx.additive_expression.expressionType
      val actualRightType = ctx.unary_expression.expressionType
      if (actualLeftType == actualRightType) {
        ctx.expressionType = actualLeftType
      }
      else {
        typeError("Type mismatch", getTopNode(ctx))
        ctx.expressionType = actualLeftType   // Error recovery.
      }
    }
  }


  override def exitExpression(ctx: AdaParser.ExpressionContext): Unit = {
    ctx.expressionType = ctx.additive_expression.expressionType
  }


  override def exitLeft_expression(ctx: AdaParser.Left_expressionContext): Unit = {
    val identifier = ctx.IDENTIFIER

    try {
      if (identifier != null) {
        ctx.expressionType = symbolTable.getObjectType(identifier.getText)
      }
    }
    catch {
      case ex: SymbolTable.SymbolTableException =>
        // Undefined identifier in an expression.
        reporter.reportError(
          identifier.getSymbol.getLine,
          identifier.getSymbol.getCharPositionInLine + 1,
          ex.getMessage)
    }
  }
}
