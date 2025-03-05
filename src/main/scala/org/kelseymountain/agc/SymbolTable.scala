package org.kelseymountain.agc

/**
 * A trait describing readable symbol tables. These tables are used to look up the types of
 * identifiers and the representations of types. They are never mutated. Errors are reported by
 * returning an error message.
 */
trait ReadableSymbolTable:

  /**
   * Looks up the type associated with a given identifier name. Returns a human-readable
   * message if the identifier name is not known.
   *
   * @param name The identifier name object to look up.
   * @return The name of the type associated with the named identifier or an error message.
   */
  def getIdentifierType(name: IdentifierName): Either[String, TypeName]

  /**
   * Looks up the representation of a type given its name. Returns a human-readable message
   * if the type name is not known.
   *
   * @param name The name of the type to look up.
   * @return The representation of the named type or an error message.
   */
  def getTypeRepresentation(name: TypeName): Either[String, TypeRep]

end ReadableSymbolTable

/**
 * A trait describing writable symbol tables. These tables are used to add new symbols to the
 * table. Errors are reported by throwing exceptions.
 */
trait WritableSymbolTable:

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

end WritableSymbolTable

/**
 * A trait describing AGC symbol tables. Symbols are divided into type names and other names
 * (referred to here as "identifier names"). Note that symbol tables are mutable. Most of the
 * methods return Unit, and errors are reported by throwing exceptions.
 */
trait SymbolTable extends ReadableSymbolTable with WritableSymbolTable


object SymbolTable:
  /**
   * The super class of all SymbolTable exceptions. These exceptions are only thrown when
   * mutating a WriteableSymbolTable.
   *
   * @param message A human-readable message describing the error.
   */
  abstract class SymbolTableException(message: String) extends Exception(message)

  class UnknownTypeNameException        (message: String) extends SymbolTableException(message)
  class DuplicateIdentifierNameException(message: String) extends SymbolTableException(message)
  class DuplicateTypeNameException      (message: String) extends SymbolTableException(message)
  class ConflictingNameException        (message: String) extends SymbolTableException(message)
end SymbolTable
