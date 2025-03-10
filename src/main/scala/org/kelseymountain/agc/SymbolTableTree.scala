package org.kelseymountain.agc

/**
 * Manages a collection of basic symbol tables used for representing symbols in nested scopes.
 * The tree of symbol tables corresponds to the scopes defined in the complete program. Once the
 * tree is populated, all symbols in every scope are known, allowing the type checker and code
 * generator to resolve forward references. This is intended to support a multi-pass
 * architecture.
 *
 * Because a symbol table tree is intended to be created once and used throughout the
 * compilation, there are two kinds of navigation methods. The first set of methods is used to
 * create new child scopes as they are encountered during symbol table population
 * ([[enterBuilderScope]] and [[exitBuilderScope]]). The second set of methods is used to
 * traverse the symbol table tree during type checking and code generation ([[enterReaderScope]]
 * and [[exitReaderScope]].
 *
 * After construction a symbol table tree is in a state where it can be built. Once it has
 * been fully populated, the caller can reset the reader traversal with [[resetReaderTraversal]]
 * to begin traversing the tree to look up symbols.
 *
 * @param rootScope The basic symbol table representing the root of the tree. This table can be
 * used for built-in symbols. It is assumed to be pre-defined.
 */
class SymbolTableTree(rootScope: BasicSymbolTable) extends SymbolTable:
  import collection.mutable

  private class TreeNode(val table: BasicSymbolTable, val parentNode: Option[TreeNode]):
    val childNodes: mutable.ArrayBuffer[TreeNode] = mutable.ArrayBuffer()

    def addChild(table: BasicSymbolTable): TreeNode =
      val child = new TreeNode(table, Some(this))
      childNodes += child
      child
  end TreeNode

  // The rootScope is for built-in symbols. It has one child representing the global scope.
  private val root = new TreeNode(rootScope, None)
  private var current: TreeNode = root.addChild(new BasicSymbolTable)

  /**
   * Create a new symbol table context when building the symbol table. Whenever a new scope is
   * entered, a new basic symbol table needs to be appended onto the end of the current scope's
   * children.
   */
  def enterBuilderScope(): Unit =
    current = current.addChild(new BasicSymbolTable)

  /**
   * Return to a higher-level symbol table context when leaving a scope. Whenever a scope is
   * left, we need to move up to the enclosing scope (up the tree toward the root). It's an
   * internal error if we try to move out of the root scope.
   *
   * @throws IllegalStateException if the root scope is exited.
   */
  def exitBuilderScope(): Unit =
    current = current.parentNode.getOrElse(
      throw new IllegalStateException("Cannot exit the root scope of a symbol table tree."))

  /** @inheritdoc */
  override def addIdentifierByName(name: IdentifierName, typeName: TypeName): Either[String, Unit] =
    current.table.addIdentifierByName(name, typeName)

  /** @inheritdoc */
  override def addTypeByName(name: TypeName, representation: TypeRep): Either[String, Unit] =
    current.table.addTypeByName(name, representation)

  private val scopeIndexStack = mutable.Stack[Int]()

  /**
   * Reset the reader traversal to the root scope. This method should be called before starting
   * a new traversal of an already populated symbol table tree.
   */
  def resetReaderTraversal(): Unit =
    current = root.childNodes.head
    scopeIndexStack.clear()
    scopeIndexStack.push(0)                 // Initialize child index for the root scope.

  /**
   * Move to the next child scope in the symbol table tree. This method should be called while
   * traversing the tree to look up symbols.
   */
  def enterReaderScope(): Unit =
    // TODO: Check for "impossible" states and throw IllegalStateException.
    val nextIndex = scopeIndexStack.pop()   // Current child index for the current scope.
    scopeIndexStack.push(nextIndex + 1)     // Update child index for the current scope.
    scopeIndexStack.push(0)                 // Initialize child index for the new scope.
    current = current.childNodes(nextIndex) // Move to the new scope.

  /**
   * Move to the parent scope in the symbol table tree. This method should be called while
   * traversing the tree to look up symbols.
   *
   * @throws IllegalStateException if the root scope is exited.
   */
  def exitReaderScope(): Unit =
    // TODO: Check for other "impossible" states and throw IllegalStateException.
    scopeIndexStack.pop()                   // Pop the child index for the current scope.
    current = current.parentNode.getOrElse( // Move to the parent scope.
      throw new IllegalStateException("Cannot exit the root scope of a symbol table tree."))

  /** @inheritdoc */
  override def getIdentifierType(name: IdentifierName): Either[String, TypeName] =
    var node: Option[TreeNode] = Some(current)            // Start from the current scope
    while node.isDefined do
      node.get.table.getIdentifierType(name) match
        case Right(typeName) => return Right(typeName)    // Found in the current scope
        case Left(_) => node = node.flatMap(_.parentNode) // Move up the tree
    Left(s"Unknown object: $name")                        // Not found in any scope

  /** @inheritdoc */
  def getTypeRepresentation(name: TypeName): Either[String, TypeRep] =
    var node: Option[TreeNode] = Some(current)            // Start from the current scope
    while node.isDefined do
      node.get.table.getTypeRepresentation(name) match
        case Right(typeRep) => return Right(typeRep)      // Found in the current scope
        case Left(_) => node = node.flatMap(_.parentNode) // Move up the tree
    Left(s"Unknown type: $name")                          // Not found in any scope

end SymbolTableTree
