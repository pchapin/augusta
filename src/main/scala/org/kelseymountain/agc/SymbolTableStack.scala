package org.kelseymountain.agc

/**
 * A SymbolTableStack manages a stack of basic symbol tables used for representing symbols in
 * nested scopes.
 *
 * ''This class is currently not used and is being retained and maintained for possible future
 * use.''
 *
 * At any time the basic table at the top of the stack refers to symbols in the current or
 * innermost scope. The symbol tables deeper in the stack are for symbols in enclosing or outer
 * scopes. Because a stack is used, only symbols above the current point are known. This makes
 * SymbolTableStacks suitable for single pass algorithms where the symbols are processed as they
 * are discovered. One consequence of this is that forward references are not supported. A
 * symbol is unknown if its declaration has not yet been seen, even if in the same scope.
 *
 * When a scope is first seen, [[enterScope]] should be called to create context for the nested
 * scope. When a scope is left, [[exitScope]] should be called to pop and remove symbols from
 * the scope just exited.
 *
 * @param baseScope The basic symbol table representing the base of the symbol table stack. This
 * table can be used for built-in symbols that are predefined.
 */
class SymbolTableStack(baseScope: BasicSymbolTable) extends SymbolTable:
  import collection.mutable

  private val stack = mutable.Stack[BasicSymbolTable]()
  stack.push(baseScope)

  /**
   * Push a new, empty symbol table on the symbol table stack. Whenever a new scope is entered,
   * this method should be called to create a context for the new scope.
   */
  def enterScope(): Unit =
    stack.push(new BasicSymbolTable)

  /**
   * Pop and discard the symbol table at the top of the symbol table stack. Whenever a scope is
   * left, this method should be called to remove the context for the exiting scope.
   */
  def exitScope(): Unit =
    // TODO: Maybe throw IllegalStateException if the stack is too short?
    assert(stack.size > 1)   // The global scope should never be removed.
    stack.pop()

  /** @inheritdoc */
  override def addIdentifierByName(name: IdentifierName, typeName: TypeName): Either[String, Unit] =
    stack.top.addIdentifierByName(name, typeName)

  /** @inheritdoc */
  override def addTypeByName(name: TypeName, representation: TypeRep): Either[String, Unit] =
    stack.top.addTypeByName(name, representation)

  /** @inheritdoc */
  override def getIdentifierType(name: IdentifierName): Either[String, TypeName] =
    // Lazily produce a stack with either error messages or type names.
    // Search that stack for the first type name.
    stack.view
      .map(_.getIdentifierType(name))
      .find(_.isRight)
      .getOrElse(Left(s"Unknown object: $name"))

  /** @inheritdoc */
  def getTypeRepresentation(name: TypeName): Either[String, TypeRep] =
    // Lazily produce a stack with either error messages or type representations.
    // Search that stack for the first representation.
    stack.view
      .map(_.getTypeRepresentation(name))
      .find(_.isRight)
      .getOrElse(Left(s"Unknown type: $name"))

end SymbolTableStack
