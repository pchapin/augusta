package org.kelseymountain.agc

import scala.annotation.tailrec

class SemanticAnalyzer(private val reporter: Reporter,
                       private val symbolTable: SymbolTableTree) extends AugustaBaseVisitor[Option[TypeName]]:
  import org.kelseymountain.agc.AugustaParser.*

  // Prepare the symbol table for reading.
  symbolTable.resetReaderTraversal()

  @tailrec
  private def resolveSubtype(typeName: TypeName): Either[String, TypeName] =
    // TODO: This starts at the current scope during the recursive calls, which is wrong.
    symbolTable.getTypeRepresentation(typeName) match
      case Left(message) => Left(message)
      case Right(representation) =>
        // If the type is a subtype, resolve the parent type. Otherwise, return the type name.
        representation match
          case TypeRep.SubtypeRep(parentType, _) => resolveSubtype(parentType)
          case _ => Right(typeName)

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

  override def visitAdditive_expression(ctx: Additive_expressionContext): Option[TypeName] =
    val rightType = visit(ctx.multiplicative_expression)

    if ctx.additive_expression == null then
      rightType
    else
      // Which operator is being used?
      val operator =
        if ctx.PLUS != null then ctx.PLUS
        else if ctx.MINUS != null then ctx.MINUS
        else throw InternalErrorException("Parse tree malformed: Unknown additive operator")

      // What are the operand types?
      val leftType = visit(ctx.additive_expression)
      val leftTypeName =
        resolveSubtype(leftType.getOrElse(throw InternalErrorException("Subexpression with no type")))
      val rightTypeName =
        resolveSubtype(rightType.getOrElse(throw InternalErrorException("Subexpression with no type")))

      // Are the operand types valid?
      (leftTypeName, rightTypeName) match
        case (Left(leftMessage), Left(rightMessage)) =>
          reporter.reportSourceError(operator, s"$leftMessage and $rightMessage")
          Some("Integer")  // Error recovery.
          
        case (Left(leftMessage), Right(resolvedRightType)) =>
          reporter.reportSourceError(operator, leftMessage)
          Some(resolvedRightType)  // Error recovery.
          
        case (Right(resolvedLeftType), Left(rightMessage)) =>
          reporter.reportSourceError(operator, rightMessage)
          Some(resolvedLeftType)  // Error recovery.
          
        case (Right(resolvedLeftType), Right(resolvedRightType)) =>
          // Are the operand types compatible?
          if resolvedLeftType != resolvedRightType then
            reporter.reportSourceError(operator, "Operands must have the same type")
          // Are the operand types arithmetic?
          // This doesn't properly deal with user-defined arithmetic types.  
          if resolvedLeftType != "Integer" && resolvedLeftType != "Float" then
            reporter.reportSourceError(operator, "Operands must be Integer or Float")
          Some(resolvedLeftType)
          