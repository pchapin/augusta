package org.pchapin.augusta

import org.slem.IRTree._

// TODO: Finish Me!
class LLVMGenerator(symbolTable: SymbolTable, reporter: Reporter) extends AdaBaseVisitor[L_Node] {

  override def visitCompilation_unit(ctx: AdaParser.Compilation_unitContext): L_Node = {
    visitChildren(ctx)
  }


  override def visitPrimary_expression(ctx: AdaParser.Primary_expressionContext): L_Node = {
    if (ctx.expression != null) {
      visit(ctx.expression)
    }
    else if (ctx.IDENTIFIER != null) {
      val name = ctx.IDENTIFIER.getText
      // TODO: Actually the allocation needs to be looked up in the symbol table.
      L_Load(L_PointerType(L_IntType(64)), L_Alloca(L_IntType(64)))
    }
    else if (ctx.NUMERIC_LITERAL != null) {
      try {
        L_Int(64, Literals.convertIntegerLiteral(ctx.NUMERIC_LITERAL.getText))
      }
      catch {
        // This exception should normally never arise if illegal literals are ruled out during
        // semantic analysis. However, literal analysis is currently not being done there.
        // TODO: Check literal format during semantic analysis.
        case ex: InvalidLiteralFormatException =>
          reporter.reportError(
            ctx.NUMERIC_LITERAL.getSymbol.getLine,
            ctx.NUMERIC_LITERAL.getSymbol.getCharPositionInLine + 1,
            ex.getMessage)
          L_Int(64, 0: Long)
      }
    }
    else {
      ctx.BOOLEAN_LITERAL.getText match {
        case "true"  => L_Boolean(true)
        case "false" => L_Boolean(false)
          // TODO: If the input parses, no other boolean literals exists. Check this?
      }
    }
  }


  override def visitMultiplicative_expression(ctx: AdaParser.Multiplicative_expressionContext): L_Node =  {
    if (ctx.MULTIPLY != null) {
      val leftSubexpression  = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.primary_expression).asInstanceOf[L_Value]

      L_Add(leftSubexpression, rightSubexpression)
    }
    else if (ctx.DIVIDE != null) {
      val leftSubexpression  = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.primary_expression).asInstanceOf[L_Value]

      L_Sub(leftSubexpression, rightSubexpression)
    }
    else {
      visit(ctx.primary_expression).asInstanceOf[L_Value]
    }
  }


  override def visitUnary_expression(ctx: AdaParser.Unary_expressionContext): L_Node = {
    val subexpression = visit(ctx.multiplicative_expression).asInstanceOf[L_Value]

    if (ctx.PLUS != null) {
      subexpression
    }
    else if (ctx.MINUS != null) {
      L_Sub(0: Long, subexpression)
    }
    else {
      subexpression
    }
  }


  override def visitAdditive_expression(ctx: AdaParser.Additive_expressionContext): L_Node = {
    if (ctx.PLUS != null) {
      val leftSubexpression  = visit(ctx.additive_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.unary_expression).asInstanceOf[L_Value]

      L_Add(leftSubexpression, rightSubexpression)
    }
    else if (ctx.MINUS != null) {
      val leftSubexpression  = visit(ctx.additive_expression).asInstanceOf[L_Value]
      val rightSubexpression = visit(ctx.unary_expression).asInstanceOf[L_Value]

      L_Sub(leftSubexpression, rightSubexpression)
    }
    else {
      visit(ctx.unary_expression).asInstanceOf[L_Value]
    }
  }

}
