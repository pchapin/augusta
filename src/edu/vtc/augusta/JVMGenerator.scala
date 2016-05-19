package edu.vtc.augusta

import collection.mutable
import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter, PrintWriter}
import org.antlr.v4.runtime.tree.TerminalNode

class JVMGenerator(
  symbolTable: SymbolTable,
  reporter   : Reporter) extends AdaBaseVisitor[Void] {

  private var output: PrintWriter = null
  private var expressionLevel = 0    // The number of open 'expression' rules that are active.

  private val localVariables = mutable.Map[String, Int]()
  private var localVariableCounter = 0
  for (symbol <- symbolTable.getObjectNames) {
    localVariableCounter += 1
    localVariables.put(symbol, localVariableCounter)
  }


  /**
   * Computes a load/store instruction given the local variable name. This method tries to use
   * the abbreviated instruction forms if possible, falling back to the instructions that take
   * an operand only when necessary.
   *
   * @param baseInstruction The base name of instruction. Should be either "iload" or "istore."
   * @param localVariableName The name of the local variable.
   * @return The appropriate instruction, possibly with an operand.
   */
  private def getLoadStoreInstruction(
      baseInstruction: String, localVariableName: String): String = {

    val localVariableNumber = localVariables(localVariableName)
    localVariableNumber match {
      case 0 | 1 | 2 | 3 => baseInstruction + "_" + localVariableNumber
      case _             => baseInstruction + " " + localVariableNumber
    }
  }


  override def visitProcedure_definition(ctx: AdaParser.Procedure_definitionContext): Void = {
    val className = ctx.IDENTIFIER.getText
    output = new PrintWriter(
      new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(className + ".j"), "US-ASCII")))

    output.println(".class public " + className)
    output.println(".super java/lang/Object")
    output.println()
    output.println(".method public <init>()V")
    output.println("    aload_0")
    output.println("    invokenonvirtual java/lang/Object/<init>()V")
    output.println("    return")
    output.println(".end method")
    output.println()
    output.println(".method public static main([Ljava/lang/String;)V")
    output.println("    .limit locals " + (localVariableCounter + 1))  // +1 for the argument?
    // TODO: Compute an appropriate size for the operand stack.
    output.println("    .limit stack 4")
    output.println("    ;")
    output.println("    ; Local Variable Table")
    output.println("    ; ====================")
    for ((variableName, variableIndex) <- localVariables) {
      output.println("    ; " + variableName + ": " + variableIndex)
    }
    output.println("    ;")

    visitChildren(ctx)

    output.println("    return")
    output.println(".end method")

    output.close()
    null
  }


  override def visitObject_declaration(ctx: AdaParser.Object_declarationContext): Void = {
    // Initialize to zero or the result of the expression (if one exists).
    if (ctx.expression == null) {
      output.println("    bipush 0")
    }
    else {
      visit(ctx.expression)
    }

    // Save the initial value in the appropriate local variable slot.
    output.println("    " + getLoadStoreInstruction("istore", ctx.IDENTIFIER(0).getText))
    null
  }


  override def visitAssignment_statement(ctx: AdaParser.Assignment_statementContext): Void = {
    visit(ctx.expression)
    // TODO: Deal with array access expressions on the left hand side of an assignment.
    output.println(
      "    " + getLoadStoreInstruction("istore", ctx.left_expression.IDENTIFIER.getText))
    null
  }


  override def visitTerminal(node: TerminalNode): Void = {
    try {
      if (expressionLevel > 0) {
        node.getSymbol.getType match {
          case AdaLexer.IDENTIFIER =>
            output.println("    " + getLoadStoreInstruction("iload", node.getText))

          case AdaLexer.NUMERIC_LITERAL =>
            val literalValue = Literals.convertIntegerLiteral(node.getText)
            val instruction =
              if (literalValue >= -128 && literalValue <= 127)
                "bipush"
              else if (literalValue >= -32768 && literalValue <= 32767)
                "sipush"
              else
                "ldc"   // TODO: The operand of ldc is really an index into the constant pool!
            output.println(
              "    " + instruction + " " + Literals.convertIntegerLiteral(node.getText))

          case _ =>
          // Do nothing.
        }
      }
    }
    catch {
      // This exception should normally never arise if illegal literals are ruled out during
      // semantic analysis. However, literal analysis is currently not being done there.
      // TODO: Check literal format during semantic analysis.
      case ex: InvalidLiteralFormatException =>
        reporter.reportError(node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
    null
  }


  override def visitMultiplicative_expression(
    ctx: AdaParser.Multiplicative_expressionContext): Void = {

    if (ctx.MULTIPLY != null) {
      visit(ctx.multiplicative_expression)
      visit(ctx.primary_expression)
      output.println("    imul")
    }
    else if (ctx.DIVIDE != null) {
      visit(ctx.multiplicative_expression)
      visit(ctx.primary_expression)
      output.println("    idiv")
    }
    else {
      visit(ctx.primary_expression)
    }
    null
  }


  override def visitUnary_expression(ctx: AdaParser.Unary_expressionContext): Void = {
    visit(ctx.multiplicative_expression)
    if (ctx.MINUS != null) {
      output.println("    ineg")
    }
    null
  }


  override def visitAdditive_expression(ctx: AdaParser.Additive_expressionContext): Void = {
    if (ctx.PLUS != null) {
      visit(ctx.additive_expression)
      visit(ctx.unary_expression)
      output.println("    iadd")
    }
    else if (ctx.MINUS != null) {
      visit(ctx.additive_expression)
      visit(ctx.unary_expression)
      output.println("    isub")
    }
    else {
      visit(ctx.unary_expression)
    }
    null
  }


  override def visitExpression(ctx: AdaParser.ExpressionContext): Void = {
    expressionLevel += 1
    visitChildren(ctx)
    expressionLevel -= 1
    null
  }

}
