package org.kelseymountain.agc

/**
 * Describes readable symbol tables. These tables are used to look up the types of identifiers
 * and the representations of those types. Readable symbol tables are never mutated. Errors are
 * reported by returning a human-readable error message wrapped in a `Left`.
 */
trait ReadableSymbolTable:

  /**
   * Looks up the type name associated with a given identifier name.
   *
   * @param name The identifier name object to look up.
   * @return The name of the type associated with the named identifier wrapped in a `Right`or a
   * human-readable error message wrapped in a `Left`.
   */
  def getIdentifierType(name: IdentifierName): Either[String, TypeName]

  /**
   * Looks up the representation of a type given its name.
   *
   * @param name The name of the type to look up.
   * @return The representation of the named type wrapped in a `Right` or a human-readable error
   * message wrapped in a `Left`.
   */
  def getTypeRepresentation(name: TypeName): Either[String, TypeRep]

end ReadableSymbolTable

/**
 * Describes writable symbol tables. These tables are used to add new symbols to the table.
 * Errors are reported by returning a human-readable error message wrapped in a `Left`.
 */
trait WritableSymbolTable:

  /**
   * Adds an identifier name along with the name of its type to this symbol table. Duplicate
   * identifier names within the same scope or identifier names that conflict with an existing
   * type name within the same scope are not allowed.
   *
   * @param name The name of the identifier to add.
   * @param typeName The name of its type.
   * @return A human-readable message describing the error if one occurred, wrapped in a `Left`;
   * otherwise Unit wrapped in a `Right`.
   */
  def addIdentifierByName(name: IdentifierName, typeName: TypeName): Either[String, Unit]

  /**
   * Adds the name of a type along with information about that type's representation to this
   * symbol table. Duplicate type names within the same scope or type names that conflict with
   * an existing object name within the same scope are not allowed.
   *
   * @param name The name of the type to add.
   * @param representation The representation of the type.
   * @return A human-readable message describing the error if one occurred, wrapped in a `Left`;
   * otherwise Unit wrapped in a `Right`.
   */
  def addTypeByName(name: TypeName, representation: TypeRep): Either[String, Unit]

end WritableSymbolTable

/**
 * Describes AGC symbol tables. Symbols are divided into type names and other names (referred to
 * here as ''identifier names''). Symbol tables are mutable. The methods all return `Either`s
 * containing human-readable error messages in the case of an error.
 */
trait SymbolTable extends ReadableSymbolTable with WritableSymbolTable
