package org.pchapin.augusta


/**
 * A trait describing Augusta symbol tables. Symbols are divided into object names and type
 * names. Note that symbol tables are mutable. The built-in types Integer and Boolean are known
 * to all symbol tables after they are constructed.
 */
trait SymbolTable {

  /**
   * Adds the name of an object along with its type to this symbol table. The type name must
   * already be known to the symbol table. Duplicate object names, or object names that conflict
   * with existing type names are not allowed. A SymbolTableException is thrown if these rules
   * are violated.
   *
   * @param name The name of the object to add.
   * @param typeName The name of its type.
   */
  def addObjectName(name: String, typeName: String): Unit

  /**
   * Adds the name of a type along with information about its representation to this symbol
   * table. Duplicate type names, or type names that conflict with existing object names are not
   * allowed. A SymbolTableException is throw if these rules are violated.
   *
   * @param name The name of the type to add.
   * @param representation The representation of the type.
   */
  def addTypeName(name: String, representation: AdaTypes.TypeRep): Unit

  /**
   * @return An iterable collection containing the names of all the objects in this symbol
   * table.
   */
  def getObjectNames: Iterable[String]

  /**
   * Looks up the type associated with a given object name. Throws an exception if the object
   * name is not known.
   * 
   * @param name The name object to look up.
   * @return The name of the type associated with the named object.
   */
  def getObjectType(name: String): String

  /**
   * Looks up the representation of a type given its name. Throws an exception if the type name
   * is not known.
   * 
   * @param name The name of the type to look up.
   * @return The representation of the named type.
   */
  def getTypeRepresentation(name: String): AdaTypes.TypeRep
}

object SymbolTable {
  class SymbolTableException(message: String) extends Exception(message)

  class UnknownObjectNameException  (message: String) extends SymbolTableException(message)
  class UnknownTypeNameException    (message: String) extends SymbolTableException(message)
  class DuplicateObjectNameException(message: String) extends SymbolTableException(message)
  class DuplicateTypeNameException  (message: String) extends SymbolTableException(message)
  class ConflictingNameException    (message: String) extends SymbolTableException(message)
}
