package org.kelseymountain.agc

class SemanticAnalyzer(private val reporter: Reporter,
                       private val symbolTable: SymbolTableTree) extends AugustaBaseVisitor[Option[TypeName]]:
  import org.kelseymountain.agc.AugustaParser.*

  // Prepare the symbol table for reading.
  symbolTable.resetReaderTraversal()

  // ==================
  // Visitation methods
  // ==================

  override def visitCompilation_unit(ctx: Compilation_unitContext): Option[TypeName] =
    // Enter the global scope.
    symbolTable.enterReaderScope()
    visitChildren(ctx)  // This is the default behavior shown here for illustration.
    // Exit the global scope.
    symbolTable.exitReaderScope()
    None

  override def visitAnd_expression(ctx: And_expressionContext): Option[TypeName] =
    val rightType = visit(ctx.relational_expression)

    if ctx.and_expression == null then
      rightType
    else
      val leftType = visit(ctx.and_expression)
      if leftType.getOrElse(throw InternalErrorException("Subexpression with no type")) != "Boolean" then
        reporter.reportSourceError(ctx.AND, "Left operand of `and` must be Boolean")
      if rightType.getOrElse(throw InternalErrorException("Subexpression with no type")) != "Boolean" then
        reporter.reportSourceError(ctx.AND, "Right operand of `and` must be Boolean")
      Some("Boolean")
