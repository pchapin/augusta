package org.kelseymountain.agc

import org.kelseymountain.agc.TypeRep.{EnumRep, RangeRep, SubtypeRep}

class SymbolTableBuilder(private val reporter: Reporter) extends AugustaBaseVisitor[Option[TypeName]]:
  import org.kelseymountain.agc.AugustaParser.*

  // Configure the built-in types in a special "internal symbol table."
  // TODO: Move populating the internal symbol table to a separate method.
  private val internalScope = new BasicSymbolTable
  // TODO: Configure the range of Augusta integers with a platform configuration file(?)
  internalScope.addTypeByName("Integer", RangeRep(Int.MinValue, Int.MaxValue))
  internalScope.addTypeByName("Natural", SubtypeRep("Integer", RangeRep(0, Int.MaxValue)))
  internalScope.addTypeByName("Positive", SubtypeRep("Integer", RangeRep(1, Int.MaxValue)))
  internalScope.addTypeByName("Boolean", EnumRep(List("False", "True")))

  // Create a stacked symbol table with the internal scope at the bottom.
  // Immediately enter a new, global scope for the compilation unit.
  private val symbolTable = new SymbolTableStack(internalScope)
  symbolTable.enterScope()

  // ==================
  // Visitation methods
  // ==================

  override def visitCompilation_unit(ctx: Compilation_unitContext): Option[TypeName] =
    // visitChildren(ctx)
    ctx.subprogram_definition.forEach(visit)
    None

  override def visitProcedure_definition(ctx: Procedure_definitionContext): Option[TypeName] =
    val identifierName = ctx.IDENTIFIER(0).getText
    // TODO: Verify that the identifier associated with `end` (if any) is the same.
    // TODO: Generate a unique internal name for this procedure's type.
    // TODO: Deal with the exception thrown by addIdentifierByName.
    symbolTable.addIdentifierByName(identifierName, "Procedure")
    // TODO: Add the procedure's type to the symbol table.
    symbolTable.enterScope()
    // TODO: Add the parameters to the symbol table.
    visit(ctx.declarations)
    visit(ctx.block)
    symbolTable.exitScope()
    None

  override def visitObject_declaration(ctx: Object_declarationContext): Option[TypeName] =
    val identifierName = ctx.IDENTIFIER(0).getText
    val typeName = ctx.IDENTIFIER(1).getText
    // TODO: Deal with the exception thrown by addIdentifierByName.
    symbolTable.addIdentifierByName(identifierName, typeName)
    None
