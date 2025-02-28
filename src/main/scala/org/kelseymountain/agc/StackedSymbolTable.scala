package org.kelseymountain.agc

import scala.collection.mutable
import SymbolTable.{SymbolTableException, UnknownIdentifierNameException, UnknownTypeNameException}

/**
 * A StackedSymbolTable manages a collection of basic symbol tables used for representing
 * symbols in nested scopes. At any time the basic table at the top of the stack refers to
 * symbols in the "current" or "innermost" scope. The symbol tables deeper in the stack are for
 * symbols in "enclosing" or "outer" scopes. Because a stack is used (and not a tree) only
 * symbols above the current point are known. When a scope is left, exitScope should be called
 * to pop and remove symbols from the scope just exited.
 *
 * Most operations on a StackedSymbolTable operate on the basic table at the top of the stack.
 * However, a few special methods are provided that allow access to other scopes under special
 * circumstances.
 */
class StackedSymbolTable extends SymbolTable:
  // TODO: Add methods for adding objects to the "enclosing" symbol table.

  private val stack = mutable.Stack[BasicSymbolTable]()
  stack.push(new BasicSymbolTable)   // For the global scope.

  /**
   * Whenever a new scope is entered, a new basic symbol table needs to be pushed onto the stack
   * to create a place for managing the symbols in the new scope.
   */
  def enterScope(): Unit =
    stack.push(new BasicSymbolTable)

  /**
   * Whenever a scope is left, the symbol table on top of the stack representing that scope
   * needs to be removed. The names in the scope we are leaving are no longer accessible.
   */
  def exitScope(): Unit =
    stack.pop()

  def addIdentifierByName(name: IdentifierName, typeName: TypeName): Unit =
    // TODO: BasicSymbolTable doesn't know how to check if typeName is in an outer scope.
    stack.top.addIdentifierByName(name, typeName)

  def addTypeByName(name: TypeName, representation: TypeRep): Unit =
    stack.top.addTypeByName(name, representation)

  def getIdentifierNames: Iterable[IdentifierName] =
    val workspaceSet = mutable.Set[IdentifierName]()
    // If a name appears in two scopes, it only appears once in the workspaceSet
    for (basicTable <- stack) {
      workspaceSet ++= basicTable.getIdentifierNames
    }
    workspaceSet

  def getIdentifierType(name: IdentifierName): TypeName =
    // Lazily produce a stack with either error messages or type names.
    // Search that stack for the first type name.
    val result = stack.view
      .map(basicTable =>
        // FIXME: This style of exception handling suggests that exceptions are not appropriate.
        try
          Right(basicTable.getIdentifierType(name))
        catch
          case ex: SymbolTableException => Left(ex.getMessage)
      )
      .find(_.isRight)
      .getOrElse(Left("Unknown object: " + name))

    result match
      case Left(message) => throw new UnknownIdentifierNameException(message)
      case Right(typeName) => typeName

  def getTypeRepresentation(name: TypeName): TypeRep =
    // Lazily produce a stack with either error messages or type representations.
    // Search that stack for the first representation.
    val result = stack.view
      .map(basicTable =>
        // FIXME: This style of exception handling suggests that exceptions are not appropriate.
        try
          Right(basicTable.getTypeRepresentation(name))
        catch
          case ex: SymbolTableException => Left(ex.getMessage)
      )
      .find(_.isRight)
      .getOrElse(Left("Unknown type: " + name))

    result match
      case Left(message) => throw new UnknownTypeNameException(message)
      case Right(typeRepresentation) => typeRepresentation

end StackedSymbolTable
