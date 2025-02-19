package org.kelseymountain.agc

enum TypeRep:
  case RangeRep(lower: BigInt, upper: BigInt)
  case ArrayRep(indexSubtype: String, elementType: TypeRep)
  case RecordRep(components: Map[String, TypeRep])
  case AccessRep(referentType: TypeRep)
  case EnumRep(values: List[String])
  case SubtypeRep(parentType: String, range: RangeRep)