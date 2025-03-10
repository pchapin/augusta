package org.kelseymountain.agc

/**
 * Provides symbol table services for a single (flat) scope. Basic symbol tables implement the
 * SymbolTable trait by way of two ordinary in-memory maps.
 */
class BasicSymbolTable extends SymbolTable:
  import collection.mutable

  private val identifierMap = mutable.Map[IdentifierName, TypeName]()
  private val typeMap = mutable.Map[TypeName, TypeRep]()

  /** @inheritdoc */
  override def addIdentifierByName(name: IdentifierName, typeName: TypeName): Either[String, Unit] =
    if identifierMap.contains(name) then
      Left(s"Duplicate identifier name: $name")
    else if typeMap.contains(name) then
      Left(s"Already a type: $name")
    else
      identifierMap.put(name, typeName)
      Right(())

  /** @inheritdoc */
  override def addTypeByName(name: TypeName, representation: TypeRep): Either[String, Unit] =
    if typeMap.contains(name) then
      Left(s"Duplicate type name: $name")
    else if identifierMap.contains(name) then
      Left(s"Already an object: $name")
    else
      typeMap.put(name, representation)
      Right(())

  /** @inheritdoc */
  override def getIdentifierType(name: IdentifierName): Either[String, TypeName] =
    identifierMap.get(name) match
      case Some(typeName) => Right(typeName)
      case None => Left(s"Unknown object: $name")

  /** @inheritdoc */
  override def getTypeRepresentation(name: TypeName): Either[String, TypeRep] =
    typeMap.get(name) match
      case Some(representation) => Right(representation)
      case None => Left(s"Unknown type: $name")

end BasicSymbolTable
