package org.kelseymountain.agc

/**
 * A trait describing AGC symbol tables. Symbols are divided into type names and other names
 * (referred to here as "identifier names"). Note that symbol tables are mutable. Most of the
 * methods return Unit, and errors are reported by throwing exceptions.
 */
trait SymbolTable:

  /**
   * Adds an identifier name along with its type to this symbol table. Duplicate identifier
   * names or identifier names that conflict with existing type names are not allowed. A
   * SymbolTableException is thrown if these rules are violated.
   *
   * @param name The name of the identifier to add.
   * @param typeName The name of its type.
   */
  def addIdentifierByName(name: IdentifierName, typeName: TypeName): Unit

  /**
   * Adds the name of a type along with information about its representation to this symbol
   * table. Duplicate type names or type names that conflict with existing object names are not
   * allowed. A SymbolTableException is thrown if these rules are violated.
   *
   * @param name The name of the type to add.
   * @param representation The representation of the type.
   */
  def addTypeByName(name: TypeName, representation: TypeRep): Unit

  /**
   * @return An iterable collection containing the names of all the identifiers in this symbol
   * table.
   */
  def getIdentifierNames: Iterable[IdentifierName]

  /**
   * Looks up the type associated with a given identifier name. Throws an exception if the
   * object name is not known.
   *
   * @param name The identifier name object to look up.
   * @return The name of the type associated with the named object.
   */
  def getIdentifierType(name: IdentifierName): TypeName

  /**
   * Looks up the representation of a type given its name. Throws an exception if the type name
   * is not known.
   *
   * @param name The name of the type to look up.
   * @return The representation of the named type.
   */
  def getTypeRepresentation(name: TypeName): TypeRep

end SymbolTable

object SymbolTable:
  class SymbolTableException(message: String) extends Exception(message)

  class UnknownIdentifierNameException  (message: String) extends SymbolTableException(message)
  class UnknownTypeNameException        (message: String) extends SymbolTableException(message)
  class DuplicateIdentifierNameException(message: String) extends SymbolTableException(message)
  class DuplicateTypeNameException      (message: String) extends SymbolTableException(message)
  class ConflictingNameException        (message: String) extends SymbolTableException(message)
end SymbolTable
