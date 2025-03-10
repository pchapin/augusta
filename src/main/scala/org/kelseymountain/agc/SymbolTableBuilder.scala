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
    visitChildren(ctx)  // This is the default behavior shown here for illustration.
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
    symbolTable.addIdentifierByName(identifierName, typeName) match
      case Left(message) => reporter.reportSourceError(ctx.IDENTIFIER(0), message)
      case Right(_)  => // Do nothing.
