package org.kelseymountain.agc

class SymbolTableBuilder(private val reporter: Reporter,
                         private val symbolTable: SymbolTableTree) extends AugustaBaseVisitor[Unit]:
  import org.kelseymountain.agc.AugustaParser.*

  // ==================
  // Visitation methods
  // ==================

  override def visitCompilation_unit(ctx: Compilation_unitContext): Unit =
    // Create and enter the global scope.
    symbolTable.enterBuilderScope()
    visitChildren(ctx)
    // Exit the global scope.
    symbolTable.exitBuilderScope()

  override def visitProcedure_definition(ctx: Procedure_definitionContext): Unit =
    val identifierName = ctx.IDENTIFIER(0).getText
    // TODO: Verify that the identifier associated with `end` (if any) is the same.
    // TODO: Generate a unique internal name for this procedure's type.
    symbolTable.addIdentifierByName(identifierName, "Procedure") match
      case Left(message) => reporter.reportSourceError(ctx.IDENTIFIER(0), message)
      case Right(_)  => // Do nothing.
    // TODO: Add the procedure's type to the symbol table.
    symbolTable.enterBuilderScope()
    // TODO: Add the parameters to the symbol table.
    visit(ctx.declarations)
    visit(ctx.block)
    symbolTable.exitBuilderScope()

  override def visitObject_declaration(ctx: Object_declarationContext): Unit =
    val identifierName = ctx.IDENTIFIER(0).getText
    val typeName = ctx.IDENTIFIER(1).getText
    // Add the object to the symbol table.
    symbolTable.addIdentifierByName(identifierName, typeName) match
      case Left(message) => reporter.reportSourceError(ctx.IDENTIFIER(0), message)
      case Right(_)  => // Do nothing.

  override def visitSubtype_declaration(ctx: Subtype_declarationContext): Unit =
    val subTypeName = ctx.IDENTIFIER(0).getText
    val parentTypeName = ctx.IDENTIFIER(1).getText
    val lowerBound = scanner.Literals.decodeIntegerLiteral(ctx.INTEGER_LITERAL(0).getText)
    val upperBound = scanner.Literals.decodeIntegerLiteral(ctx.INTEGER_LITERAL(1).getText)
    val rangeRepresentation: TypeRep.RangeRep = TypeRep.RangeRep(lowerBound, upperBound)
    val typeRepresentation = TypeRep.SubtypeRep(
      parentType = parentTypeName,
      range = rangeRepresentation
    )
    // Add the subtype to the symbol table.
    symbolTable.addTypeByName(subTypeName, typeRepresentation) match
      case Left(message) => reporter.reportSourceError(ctx.IDENTIFIER(0), message)
      case Right(_)  => // Do nothing.

  override def visitType_declaration(ctx: Type_declarationContext): Unit =
    val typeName = ctx.IDENTIFIER(0).getText
    val parentTypeName = ctx.IDENTIFIER(1).getText
    // Add the type to the symbol table.
    symbolTable.addTypeByName(typeName, TypeRep.ParentRep(parentTypeName)) match
      case Left(message) => reporter.reportSourceError(ctx.IDENTIFIER(0), message)
      case Right(_)  => // Do nothing.

  override def visitDeclare_statement(ctx: Declare_statementContext): Unit =
    symbolTable.enterBuilderScope()
    visit(ctx.declarations)
    ctx.statement forEach { visit(_) }
    symbolTable.exitBuilderScope()

end SymbolTableBuilder
