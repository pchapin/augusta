package org.kelseymountain.agc

import org.kelseymountain.agc.TypeRep.{EnumRep, RangeRep}

class SemanticAnalyzer(private val reporter: Reporter) extends AugustaBaseVisitor[Unit]:
  import org.kelseymountain.agc.AugustaParser._

  // Configure the built-in types in a special "internal symbol table."
  private val internalScope = new BasicSymbolTable
  // TODO: Configure the range of Augusta integers with a platform configuration file(?)
  internalScope.addTypeByName("Integer", RangeRep(Int.MinValue, Int.MaxValue))
  internalScope.addTypeByName("Boolean", EnumRep(List("False", "True")))

  // Create a stacked symbol table with the internal scope at the bottom.
  // Immediately enter a new, global scope for the compilation unit.
  private val symbolTable = new StackedSymbolTable(internalScope)
  symbolTable.enterScope()

  // ==================
  // Visitation methods
  // ==================

  override def visitCompilation_unit(ctx: Compilation_unitContext): Unit =
    // visitChildren(ctx)
    ctx.subprogram_definition.forEach(visit)

  override def visitProcedure_definition(ctx: Procedure_definitionContext): Unit =
    val identifierName = ctx.IDENTIFIER(0).getText
    // TODO: Generate a unique internal type name for this procedure.
    // TODO: Deal with the exception thrown by addIdentifierByName.
    symbolTable.addIdentifierByName(identifierName, "Procedure")
    // TODO: Add the procedure's type to the symbol table.
    symbolTable.enterScope()
    // TODO: Add the parameters to the symbol table.
    visit(ctx.declarations)
    visit(ctx.block)
    symbolTable.exitScope()

  override def visitObject_declaration(ctx: Object_declarationContext): Unit =
    val identifierName = ctx.IDENTIFIER(0).getText
    val typeName = ctx.IDENTIFIER(1).getText
    // TODO: Deal with the exception thrown by addIdentifierByName.
    symbolTable.addIdentifierByName(identifierName, typeName)
