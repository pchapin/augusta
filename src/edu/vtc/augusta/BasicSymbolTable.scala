package edu.vtc.augusta

/**
 * A BasicSymbolTable implements the SymbolTable trait by way of two ordinary in-memory maps.
 */
class BasicSymbolTable extends SymbolTable {
  import collection.mutable

  private val objectMap = mutable.Map[String, String]()
  private val typeMap = mutable.Map[String, AdaTypes.TypeRep]()

  // Pre-load the type map with information about the built-in types.
  typeMap.put("Integer", AdaTypes.IntegerRep)
  typeMap.put("Boolean", AdaTypes.BooleanRep)

  def addObjectName(name: String, typeName: String) {
    if (objectMap.get(name) != None) {
      throw new SymbolTable.DuplicateObjectNameException(name)
    }
    else if (typeMap.get(typeName) == None) {
      throw new SymbolTable.UnknownTypeNameException(typeName)
    }
    else if (typeMap.get(typeName) == Some(name)) {
      throw new SymbolTable.ConflictingNameException(name)
    }
    else {
      objectMap.put(name, typeName)
    }
  }


  def addTypeName(name: String, representation: AdaTypes.TypeRep) {
    if (typeMap.get(name) != None) {
      throw new SymbolTable.DuplicateTypeNameException(name)
    }
    else if (objectMap.get(name) == Some(name)) {
      throw new SymbolTable.ConflictingNameException(name)
    }
    else {
      typeMap.put(name, representation)
    }
  }


  def getObjectNames: Iterable[String] = {
    objectMap.values
  }


  def getObjectType(name: String): String = {
    objectMap.get(name) match {
      case Some(typeName) => typeName
      case None => throw new SymbolTable.UnknownObjectNameException(name)
    }
  }


  def getTypeRepresentation(name: String): AdaTypes.TypeRep = {
    typeMap.get(name) match {
      case Some(representation) => representation
      case None => throw new SymbolTable.UnknownTypeNameException(name)
    }
  }

}
