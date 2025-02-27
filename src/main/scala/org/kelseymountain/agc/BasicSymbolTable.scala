package org.kelseymountain.agc

/**
 * A BasicSymbolTable implements the SymbolTable trait by way of two ordinary in-memory maps.
 */
class BasicSymbolTable extends SymbolTable:
  import collection.mutable

  private val identifierMap = mutable.Map[IdentifierName, TypeName]()
  private val typeMap = mutable.Map[TypeName, TypeRep]()

  def addIdentifierByName(name: IdentifierName, typeName: TypeName): Unit =
    if identifierMap.contains(name) then
      throw SymbolTable.DuplicateIdentifierNameException("Duplicate identifier name: " + name)
    else if !typeMap.contains(typeName) then
      throw SymbolTable.UnknownTypeNameException("Unknown type name: " + typeName)
    else if typeMap.contains(name) then
      throw SymbolTable.ConflictingNameException("Already a type: " + name)
    else
      identifierMap.put(name, typeName)

  def addTypeByName(name: TypeName, representation: TypeRep): Unit =
    if typeMap.contains(name) then
      throw SymbolTable.DuplicateTypeNameException("Duplicate type name: " + name)
    else if identifierMap.contains(name) then
      throw SymbolTable.ConflictingNameException("Already an object: " + name)
    else
      typeMap.put(name, representation)

  def getIdentifierNames: Iterable[IdentifierName] =
    identifierMap.values

  def getIdentifierType(name: IdentifierName): TypeName =
    identifierMap.get(name) match
      case Some(typeName) => typeName
      case None => throw SymbolTable.UnknownIdentifierNameException("Unknown object: " + name)

  def getTypeRepresentation(name: TypeName): TypeRep =
    typeMap.get(name) match
      case Some(representation) => representation
      case None => throw new SymbolTable.UnknownTypeNameException("Unknown type: " + name)

end BasicSymbolTable
