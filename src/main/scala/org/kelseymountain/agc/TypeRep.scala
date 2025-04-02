package org.kelseymountain.agc

enum ParameterMode:
  case InMode
  case OutMode
  case InOutMode

enum TypeRep:
  case ParentRep(parentType: TypeName)
  case RangeRep(lower: BigInt, upper: BigInt)
  case SubtypeRep(parentType: TypeName, range: RangeRep)
  case EnumRep(values: List[IdentifierName])
  case ArrayRep(indexSubtype: TypeName, elementType: TypeName)
  case RecordRep(components: Map[IdentifierName, TypeName])
  case AccessRep(referentType: TypeName)
  case ProcedureRep(parameters: List[(IdentifierName, ParameterMode, TypeName)])
  case FunctionRep(parameters: List[(IdentifierName, ParameterMode, TypeName)], resultType: TypeName)
