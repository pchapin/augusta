package org.kelseymountain.agc

enum TypeRep:
  case RangeRep(lower: BigInt, upper: BigInt)
  case SubtypeRep(parentType: TypeName, range: RangeRep)
  case EnumRep(values: List[IdentifierName])
  case ArrayRep(indexSubtype: TypeName, elementType: TypeName)
  case RecordRep(components: Map[IdentifierName, TypeName])
  case AccessRep(referentType: TypeName)
