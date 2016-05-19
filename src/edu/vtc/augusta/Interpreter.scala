package edu.vtc.augusta

class Interpreter(
  symbolTable: SymbolTable,
  reporter   : Reporter) extends AdaBaseListener {

  private val symbolValues = new java.util.TreeMap[String, Int]
  for (symbol <- symbolTable.getObjectNames) {
    symbolValues.put(symbol, 0)
  }

  override def exitObject_declaration(ctx: AdaParser.Object_declarationContext) {
    if (ctx.expression != null) {
      symbolValues.put(ctx.IDENTIFIER(0).getText, ctx.expression.value)
    }
  }

  override def exitAssignment_statement(ctx: AdaParser.Assignment_statementContext) {
    // TODO: Handle assignment to an array element.
    symbolValues.put(ctx.left_expression().IDENTIFIER.getText, ctx.expression.value)
  }

  override def exitPrimary_expression(ctx: AdaParser.Primary_expressionContext) {
    try {
      if (ctx.IDENTIFIER != null) {
        ctx.value = symbolValues.get(ctx.IDENTIFIER.getText)
      }
      else if (ctx.NUMERIC_LITERAL != null) {
        ctx.value = Literals.convertIntegerLiteral(ctx.NUMERIC_LITERAL.getText)
      }
      else {
        ctx.value = ctx.expression.value
      }
    }
    catch {
      // This exception should normally never arise if illegal literals are ruled out during
      // semantic analysis. However, literal analysis is currently not being done there.
      // TODO: Check literal format during semantic analysis.
      case ex: InvalidLiteralFormatException =>
        val node = ctx.NUMERIC_LITERAL
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
  }

  override def exitMultiplicative_expression(ctx: AdaParser.Multiplicative_expressionContext) {
    if (ctx.MULTIPLY != null) {
      ctx.value = ctx.multiplicative_expression.value * ctx.primary_expression.value
    }
    else if (ctx.DIVIDE != null) {
      ctx.value = ctx.multiplicative_expression.value / ctx.primary_expression.value
    }
    else {
      ctx.value = ctx.primary_expression.value
    }
  }

  override def exitUnary_expression(ctx: AdaParser.Unary_expressionContext) {
    if (ctx.MINUS() != null) {
      ctx.value = - ctx.multiplicative_expression.value
    }
    else {
      ctx.value = ctx.multiplicative_expression.value
    }
  }

  override def exitAdditive_expression(ctx: AdaParser.Additive_expressionContext) {
    if (ctx.PLUS != null) {
      ctx.value = ctx.additive_expression.value + ctx.unary_expression.value
    }
    else if (ctx.MINUS != null) {
      ctx.value = ctx.additive_expression.value - ctx.unary_expression.value
    }
    else {
      ctx.value = ctx.unary_expression.value
    }
  }

  override def exitRelational_expression(ctx: AdaParser.Relational_expressionContext) {
    // TODO: Handle interpretation of relational expressions
    ctx.value = ctx.additive_expression.value
  }

  override def exitExpression(ctx: AdaParser.ExpressionContext) {
    ctx.value = ctx.relational_expression.value
  }

  def displayResults() {
    import scala.collection.JavaConversions._

    // Display the symbol table.
    System.out.println("")
    System.out.println("SYMBOL VALUES")
    System.out.println("=============")

    for (key <- symbolValues.keySet) {
      printf("%s => %d\n", key, symbolValues.get(key))
    }
  }

}
