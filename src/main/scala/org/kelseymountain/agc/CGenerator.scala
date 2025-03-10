package org.kelseymountain.agc

class CGenerator(private val reporter: Reporter,
                 private val symbolTable: SymbolTableTree) extends AugustaBaseVisitor[Unit]:
  import org.kelseymountain.agc.AugustaParser.*

  // The generated code.
  private val output = new StringBuilder()

  // Prepare the symbol table for reading.
  symbolTable.resetReaderTraversal()

  // ==================
  // Visitation methods
  // ==================

  override def visitCompilation_unit(ctx: Compilation_unitContext): Unit =
    // Enter the global scope.
    symbolTable.enterReaderScope()
    visitChildren(ctx)  // This is the default behavior shown here for illustration.
    // Exit the global scope.
    symbolTable.exitReaderScope()
    None

  override def visitAnd_expression(ctx: And_expressionContext): Unit =
    if ctx.AND == null then
      visit(ctx.relational_expression)
    else
      output.append("(")
      visit(ctx.and_expression)
      output.append(" && ")
      visit(ctx.relational_expression)
      output.append(")")
