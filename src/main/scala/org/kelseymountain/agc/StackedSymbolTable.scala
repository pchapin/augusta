package org.kelseymountain.agc

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
 *
 * @param globalScope The basic symbol table representing the global scope. This table is
 * always at the bottom of the stack.
 */
class StackedSymbolTable(globalScope: BasicSymbolTable) extends SymbolTable:
  import collection.mutable

  private val stack = mutable.Stack[BasicSymbolTable]()
  stack.push(globalScope)

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
    assert(stack.size > 1)   // The global scope should never be removed.
    stack.pop()

  override def addIdentifierByName(name: IdentifierName, typeName: TypeName): Unit =
    // TODO: BasicSymbolTable doesn't know how to check if typeName is in an outer scope.
    stack.top.addIdentifierByName(name, typeName)

  override def addTypeByName(name: TypeName, representation: TypeRep): Unit =
    stack.top.addTypeByName(name, representation)

  override def getIdentifierType(name: IdentifierName): Either[String, TypeName] =
    // Lazily produce a stack with either error messages or type names.
    // Search that stack for the first type name.
    stack.view
      .map(basicTable => basicTable.getIdentifierType(name))
      .find(_.isRight)
      .getOrElse(Left(s"Unknown object: $name"))

  def getTypeRepresentation(name: TypeName): Either[String, TypeRep] =
    // Lazily produce a stack with either error messages or type representations.
    // Search that stack for the first representation.
    stack.view
      .map(basicTable => basicTable.getTypeRepresentation(name))
      .find(_.isRight)
      .getOrElse(Left(s"Unknown type: $name"))

end StackedSymbolTable
