package org.kelseymountain.agc

/**
 * A BasicSymbolTable implements the SymbolTable trait by way of two ordinary in-memory maps.
 */
class BasicSymbolTable extends SymbolTable:
  import collection.mutable

  private val identifierMap = mutable.Map[IdentifierName, TypeName]()
  private val typeMap = mutable.Map[TypeName, TypeRep]()

  override def addIdentifierByName(name: IdentifierName, typeName: TypeName): Unit =
    if identifierMap.contains(name) then
      throw SymbolTable.DuplicateIdentifierNameException(s"Duplicate identifier name: $name")
    else if !typeMap.contains(typeName) then
      throw SymbolTable.UnknownTypeNameException(s"Unknown type name: $typeName")
    else if typeMap.contains(name) then
      throw SymbolTable.ConflictingNameException(s"Already a type: $name")
    else
      identifierMap.put(name, typeName)

  override def addTypeByName(name: TypeName, representation: TypeRep): Unit =
    if typeMap.contains(name) then
      throw SymbolTable.DuplicateTypeNameException(s"Duplicate type name: $name")
    else if identifierMap.contains(name) then
      throw SymbolTable.ConflictingNameException(s"Already an object: $name")
    else
      typeMap.put(name, representation)

  override def getIdentifierType(name: IdentifierName): Either[String, TypeName] =
    identifierMap.get(name) match
      case Some(typeName) => Right(typeName)
      case None => Left(s"Unknown object: $name")

  override def getTypeRepresentation(name: TypeName): Either[String, TypeRep] =
    typeMap.get(name) match
      case Some(representation) => Right(representation)
      case None => Left(s"Unknown type: $name")

end BasicSymbolTable
