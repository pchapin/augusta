package org.pchapin.augusta

import org.pchapin.augusta.SymbolTable.SymbolTableException
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.ParserRuleContext

class SemanticAnalyzer(
  symbolTable: StackedSymbolTable,
  reporter   : Reporter) extends AdaBaseListener {

  import AdaTypes._

  // Processing Declarations
  // -----------------------

  override def exitObject_declaration(ctx: AdaParser.Object_declarationContext) {
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


  override def exitType_declaration(ctx: AdaParser.Type_declarationContext) {
    val integerTypeContext = ctx.type_definition.integer_type_definition
    val arrayTypeContext   = ctx.type_definition.array_type_definition

    try {
      if (integerTypeContext != null) {
        // Must be a declaration of an integer type.
        symbolTable.addTypeName(
          ctx.IDENTIFIER.getText,
          IntegerTypeRep(integerTypeContext.lowerBound, integerTypeContext.upperBound))
      }
      else {
        // Must be a declaration of an array type.
        symbolTable.addTypeName(
          ctx.IDENTIFIER.getText,
          ArrayTypeRep(arrayTypeContext.indexTypeName, arrayTypeContext.elementTypeName))
      }
    }
    catch {
      case ex: SymbolTableException =>
        val node = ctx.IDENTIFIER
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
  }


  private def getRangeConstraint(
    lowerNode: TerminalNode, upperNode: TerminalNode): (Int, Int) = {

    // Convert the lower bound and check it for validity.
    val lowerBound = try {
      Literals.convertIntegerLiteral(lowerNode.getText)
    }
    catch {
      case ex: InvalidLiteralFormatException =>
        val node = lowerNode
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
        -2147483648   // Error recovery. Should be Integer'First or Parent_Type'First.
    }

    // Convert the upper bound and check it for validity.
    val upperBound = try {
      Literals.convertIntegerLiteral(upperNode.getText)
    }
    catch {
      case ex: InvalidLiteralFormatException =>
        val node = upperNode
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
        +2147483647   // Error recovery. Should be Integer'Last or Parent_Type'Last.
    }

    // Check the sanity of the range.
    if (lowerBound <= upperBound) {
      (lowerBound, upperBound)
    }
    else {
      val node = lowerNode
      reporter.reportError(
        node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, "Invalid range")
      (lowerBound, lowerBound)    // Error recovery.
    }
  }


  override def exitInteger_type_definition(ctx: AdaParser.Integer_type_definitionContext) {
    val (lower, upper) = getRangeConstraint(ctx.NUMERIC_LITERAL(0), ctx.NUMERIC_LITERAL(1))
    ctx.lowerBound = lower
    ctx.upperBound = upper
  }


  override def exitArray_type_definition(ctx: AdaParser.Array_type_definitionContext) {
    val indexTypeName = ctx.IDENTIFIER(0).getText
    val elementTypeName = ctx.IDENTIFIER(1).getText

    // Verify the correctness of the index type.
    try {
      val indexTypeRep = symbolTable.getTypeRepresentation(indexTypeName)
      ctx.indexTypeName = indexTypeRep match {
        case IntegerRep           => indexTypeName
        case IntegerTypeRep(_, _) => indexTypeName
        case SubtypeRep(_, _, _)  => indexTypeName
        case _ =>
          val node = ctx.IDENTIFIER(0)
          reporter.reportError(
            node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, "Invalid index type")
          "Integer"   // Error recovery.
      }
    }
    catch {
      case ex: SymbolTableException =>
        val node = ctx.IDENTIFIER(0)
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
        ctx.indexTypeName = "Integer"   // Error recovery.
    }

    // Verify the correctness of the element type.
    try {
      val elementTypeRep = symbolTable.getTypeRepresentation(elementTypeName)
      ctx.elementTypeName = elementTypeRep match {
        case IntegerRep           => elementTypeName
        case BooleanRep           => elementTypeName
        case IntegerTypeRep(_, _) => elementTypeName
        case SubtypeRep(_, _, _)  => elementTypeName
        case _ =>
          // TODO: Arrays of arrays currently forbidden. Supporting them requires changes to array access syntax.
          val node = ctx.IDENTIFIER(1)
          reporter.reportError(
            node.getSymbol.getLine,
            node.getSymbol.getCharPositionInLine + 1,
            "Invalid element type")
          "Integer"   // Error recovery.
      }
    }
    catch {
      case ex: SymbolTableException =>
        val node = ctx.IDENTIFIER(1)
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
        ctx.elementTypeName = "Integer"   // Error recovery.
    }
  }


  override def exitSubtype_declaration(ctx: AdaParser.Subtype_declarationContext) {
    val (lower, upper) = getRangeConstraint(ctx.NUMERIC_LITERAL(0), ctx.NUMERIC_LITERAL(1))
    val parentTypeName = ctx.IDENTIFIER(1).getText

    def checkBoundsAgainst(parentLower: Int, parentUpper: Int): Unit = {
      if (lower < parentLower) {
        val node = ctx.NUMERIC_LITERAL(0)
        reporter.reportError(
          node.getSymbol.getLine,
          node.getSymbol.getCharPositionInLine + 1,
          "Lower bound outside range of parent type or subtype")
      }
      if (upper > parentUpper) {
        val node = ctx.NUMERIC_LITERAL(1)
        reporter.reportError(
          node.getSymbol.getLine,
          node.getSymbol.getCharPositionInLine + 1,
          "Upper bound outside range of parent type or subtype")
      }
    }

    try {
      // Check validity of parent type and bounds; compute corrected versions if necessary for
      // error recovery.
      val (correctedParentType, correctedLower, correctedUpper) =
        symbolTable.getTypeRepresentation(parentTypeName) match {
          case IntegerRep =>
            // All bounds are valid for Integer parents.
            ("Integer", lower, upper)

          case IntegerTypeRep(parentLower, parentUpper) =>
            // The parent is an integer type representation. Verify bounds.
            checkBoundsAgainst(parentLower, parentUpper)
            (parentTypeName, lower, upper)

          case SubtypeRep(_, parentLower, parentUpper) =>
            // The parent is a subtype representation. Verify bounds.
            checkBoundsAgainst(parentLower, parentUpper)
            (parentTypeName, lower, upper)

          case _ =>
            // An invalid parent type was used. Report error and correct to Integer parent with
            // full bounds.
            val node = ctx.IDENTIFIER(1)
            reporter.reportError(
              node.getSymbol.getLine,
              node.getSymbol.getCharPositionInLine + 1,
              "Invalid parent type")
            ("Integer", -2147483648, +2147483647)   // Error recovery.
        }

      symbolTable.addTypeName(
        ctx.IDENTIFIER(0).getText,
        SubtypeRep(correctedParentType, correctedLower, correctedUpper))
    }
    catch {
      case ex: SymbolTableException =>
        val node = ctx.IDENTIFIER(0)
        reporter.reportError(
          node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, ex.getMessage)
    }
  }


  override def enterSubprogram_declaration(ctx: AdaParser.Subprogram_declarationContext) {
    symbolTable.enterScope()
  }


  override def exitSubprogram_declaration(ctx: AdaParser.Subprogram_declarationContext) {
    symbolTable.exitScope()
  }


  override def exitParameter_specification(ctx: AdaParser.Parameter_specificationContext) {
    val parameterNameNode = ctx.IDENTIFIER(0)
    val parameterTypeNode = ctx.IDENTIFIER(1)

    try {
      symbolTable.addObjectName(parameterNameNode.getText, parameterTypeNode.getText)
    }
    catch {
      case ex: SymbolTable.UnknownTypeNameException =>
        // Type used in the declaration has not been declared.
        reporter.reportError(
          parameterTypeNode.getSymbol.getLine,
          parameterTypeNode.getSymbol.getCharPositionInLine + 1,
          ex.getMessage)

      case ex: SymbolTable.SymbolTableException =>
        // Duplicate declaration or name already declared as a type.
        reporter.reportError(
          parameterNameNode.getSymbol.getLine,
          parameterNameNode.getSymbol.getCharPositionInLine + 1,
          ex.getMessage)
    }
  }

  // Processing Statements
  // ---------------------


  // Processing Expressions
  // ----------------------

  private def typeError(message: String, node: TerminalNode) {
    reporter.reportError(
      node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + 1, message)
  }


  private def getTopNode(ctx: ParserRuleContext): TerminalNode = {
    import AdaParser._

    ctx match {
      case ctx: Array_access_expressionContext =>
        ctx.IDENTIFIER

      case ctx: Primary_expressionContext =>
        if      (ctx.IDENTIFIER      != null) ctx.IDENTIFIER
        else if (ctx.NUMERIC_LITERAL != null) ctx.NUMERIC_LITERAL
        else if (ctx.array_access_expression != null) getTopNode(ctx.array_access_expression)
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

      case ctx: Relational_expressionContext =>
        if      (ctx.EQUAL         != null) ctx.EQUAL
        else if (ctx.NOT_EQUAL     != null) ctx.NOT_EQUAL
        else if (ctx.LESS          != null) ctx.LESS
        else if (ctx.LESS_EQUAL    != null) ctx.LESS_EQUAL
        else if (ctx.GREATER       != null) ctx.GREATER
        else if (ctx.GREATER_EQUAL != null) ctx.GREATER_EQUAL
        else getTopNode(ctx.additive_expression)

      case ctx: ExpressionContext =>
        getTopNode(ctx.relational_expression)

      case _ =>
        throw new Reporter.InternalErrorException(
          "Unexpected parse tree context while searching for a top level expression")
    }
  }


  override def exitArray_access_expression(ctx: AdaParser.Array_access_expressionContext) {
    val actualIdentifierType = symbolTable.getObjectType(ctx.IDENTIFIER.getText)
    val actualIdentifierTypeRep = symbolTable.getTypeRepresentation(actualIdentifierType)
    val actualIndexType = ctx.expression.expressionType

    // The identifier in an array access expression must have an array type.
    actualIdentifierTypeRep match {
      case ArrayTypeRep(indexType, elementType) =>
        // TODO: Handle subtype conversions.
        if (indexType != actualIndexType)
          typeError("Invalid index type", getTopNode(ctx.expression))
        ctx.expressionType = elementType

      case _ =>
        typeError("Attempt to index non-array", getTopNode(ctx))
        ctx.expressionType = actualIdentifierType   // Error recovery.
    }
  }


  override def exitPrimary_expression(ctx: AdaParser.Primary_expressionContext) {
    val identifier = ctx.IDENTIFIER

    try {
      if (identifier != null) {
        ctx.expressionType = symbolTable.getObjectType(identifier.getText)
      }
      else if (ctx.NUMERIC_LITERAL != null) {
        // TODO: Numeric literals should really have a special "Universal Integer" type.
        ctx.expressionType = "Integer"
      }
      else if (ctx.array_access_expression != null) {
        ctx.expressionType = ctx.array_access_expression.expressionType
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


  override def exitMultiplicative_expression(ctx: AdaParser.Multiplicative_expressionContext) {
    if (ctx.MULTIPLY == null && ctx.DIVIDE == null) {
      ctx.expressionType = ctx.primary_expression.expressionType
    }
    else {
      val actualLeftType = ctx.multiplicative_expression.expressionType
      val actualRightType = ctx.primary_expression.expressionType
      // TODO: Handle subtype conversions.
      if (actualLeftType == actualRightType) {
        // TODO: Check to be sure only numbers are multiplied or divided (no Booleans or arrays).
        ctx.expressionType = actualLeftType
      }
      else {
        typeError("Type mismatch", getTopNode(ctx))
        ctx.expressionType = actualLeftType   // Error recovery.
      }
    }
  }


  override def exitUnary_expression(ctx: AdaParser.Unary_expressionContext) {
    if (ctx.PLUS == null && ctx.MINUS == null) {
      ctx.expressionType = ctx.multiplicative_expression.expressionType
    }
    else {
      val actualType = ctx.multiplicative_expression.expressionType
        // TODO: Check to be sure only numbers are negated (no Booleans or arrays).
        ctx.expressionType = actualType
    }
  }


  override def exitAdditive_expression(ctx: AdaParser.Additive_expressionContext) {
    if (ctx.PLUS == null && ctx.MINUS == null) {
      ctx.expressionType = ctx.unary_expression.expressionType
    }
    else {
      val actualLeftType = ctx.additive_expression.expressionType
      val actualRightType = ctx.unary_expression.expressionType
      // TODO: Handle subtype conversions.
      if (actualLeftType == actualRightType) {
        // TODO: Check to be sure only numbers are multiplied or divided (no Booleans or arrays).
        ctx.expressionType = actualLeftType
      }
      else {
        typeError("Type mismatch", getTopNode(ctx))
        ctx.expressionType = actualLeftType   // Error recovery.
      }
    }
  }

  override def exitRelational_expression(ctx: AdaParser.Relational_expressionContext) {
    if (ctx.EQUAL         == null &&
        ctx.NOT_EQUAL     == null &&
        ctx.LESS          == null &&
        ctx.LESS_EQUAL    == null &&
        ctx.GREATER       == null &&
        ctx.GREATER_EQUAL == null) {

      ctx.expressionType = ctx.additive_expression.expressionType
    }
    else {
      val actualLeftType = ctx.relational_expression.expressionType
      val actualRightType = ctx.additive_expression.expressionType
      // TODO: Handle subtype conversions.
      if (actualLeftType == actualRightType) {
        // TODO: Check to be sure only numbers are multiplied or divided (no Booleans or arrays).
        ctx.expressionType = "Boolean"
      }
      else {
        typeError("Type mismatch", getTopNode(ctx))
        ctx.expressionType = "Boolean"   // Error recovery.
      }
    }
  }


  override def exitExpression(ctx: AdaParser.ExpressionContext) {
    ctx.expressionType = ctx.relational_expression.expressionType
  }


  override def exitLeft_expression(ctx: AdaParser.Left_expressionContext) {
    val identifier = ctx.IDENTIFIER

    try {
      if (identifier != null) {
        ctx.expressionType = symbolTable.getObjectType(identifier.getText)
      }
      else {
        ctx.expressionType = ctx.array_access_expression.expressionType
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
