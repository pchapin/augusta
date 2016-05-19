package edu.vtc.augusta

import scala.collection.mutable
import edu.vtc.augusta.SymbolTable.{UnknownTypeNameException, UnknownObjectNameException, SymbolTableException}

/**
 * A StackedSymbolTable manages a collection of basic symbol tables used for representing
 * symbols in nested scopes. At any time the basic table at the top of the stack refers to
 * symbols in the "current" or "innermost" scope. The symbol tables deeper in the stack are for
 * symbols in "enclosing" or "outer" scopes. Because a stack is used (and not a tree) only
 * symbols above the current point are known. When a scope is left exitScope should be called to
 * pop and remove symbols from the scope just exited.
 *
 * Most operations on a StackedSymbolTable operate on the basic table at the top of the stack.
 * However a few special methods are provided that allow access to other scopes under special
 * circumstances.
 */
class StackedSymbolTable extends SymbolTable {
  // TODO: Add methods for adding objects to the "enclosing" symbol table.

  val stack = mutable.Stack[BasicSymbolTable]()
  stack.push(new BasicSymbolTable)   // For the global scope.

  /**
   * Whenever a new scope is entered a new basic symbol table needs to be pushed onto the stack
   * to create a place for managing the symbols in the new scope.
   */
  def enterScope(): Unit = {
    stack.push(new BasicSymbolTable)
  }


  /**
   * Whenever a scope is left, the symbol table on top of the stack representing that scope
   * needs to be removed. The names in the scope we are leaving are no longer accessible.
   */
  def exitScope(): Unit = {
    stack.pop()
  }


  def addObjectName(name: String, typeName: String): Unit = {
    // TODO: BasicSymbolTable doesn't know how to check if typeName is in an outer scope.
    stack.top.addObjectName(name, typeName)
  }


  def addTypeName(name: String, representation: AdaTypes.TypeRep): Unit = {
    stack.top.addTypeName(name, representation)
  }


  def getObjectNames: Iterable[String] = {
    val workspaceSet = mutable.Set[String]()
    // If a name appears in two scopes, it only appears once in the workspaceSet at the end of
    // the method.
    for (basicTable <- stack) {
      workspaceSet ++= basicTable.getObjectNames
    }
    workspaceSet
  }


  def getObjectType(name: String): String = {

    // Produce a stack with either error messages or type names after doing a look-up in each
    // scope.
    val typeStack = for (basicTable <- stack) yield {
      try {
        Right(basicTable.getObjectType(name))
      }
      catch {
        case ex: SymbolTableException =>
          Left(ex.getMessage)
      }
    }

    // Fold the stack from top to bottom, favoring the first type name but otherwise taking the
    // first error message.
    val result = typeStack.foldLeft(Left("Unknown object: " + name): Either[String, String]) {
        case (Left(m1),     Left(m2)    ) => Left(m1)
        case (Left(m1),     Right(name2)) => Right(name2)
        case (Right(name1), Left(m2)    ) => Right(name1)
        case (Right(name1), Right(name2)) => Right(name1)
    }

    result match {
      case Left(message) => throw new UnknownObjectNameException(message)
      case Right(typeName) => typeName
    }
  }


  def getTypeRepresentation(name: String): AdaTypes.TypeRep = {
    // Produce a stack with either error messages or type representations after doing a look-up
    // in each scope.
    val representationStack = for (basicTable <- stack) yield {
      try {
        Right(basicTable.getTypeRepresentation(name))
      }
      catch {
        case ex: SymbolTableException =>
          Left(ex.getMessage)
      }
    }

    // Fold the stack from top to bottom, favoring the first representation but otherwise taking
    // the first error message.
    val result = representationStack.foldLeft(Left("Unknown type: " + name): Either[String, AdaTypes.TypeRep]) {
      case (Left(m1),    Left(m2)   ) => Left(m1)
      case (Left(m1),    Right(rep2)) => Right(rep2)
      case (Right(rep1), Left(m2)   ) => Right(rep1)
      case (Right(rep1), Right(rep2)) => Right(rep1)
    }

    result match {
      case Left(message) => throw new UnknownTypeNameException(message)
      case Right(typeRepresentation) => typeRepresentation
    }
  }
}
