package edu.vtc.augusta

/**
 * This object encapsulates the representation of Ada types. Only the representation of a type
 * is described here. The name of the type is handled elsewhere. When a type representation
 * refers to another type, for example the element type of an array, only the name is needed.
 * This is because Ada uses named type equivalence. Two types are only the same if they have the
 * same name.
 */
object AdaTypes {

  sealed abstract class TypeRep

  // Primitive types.
  case object IntegerRep extends TypeRep
  case object BooleanRep extends TypeRep

  // User defined types.
  case class IntegerTypeRep(lowerBound: Int, upperBound: Int) extends TypeRep
  case class ArrayTypeRep(indexType: String, elementType: String) extends TypeRep

  // User defined subtypes.
  case class SubtypeRep(parentType: String, lowerBound: Int, upperBound: Int) extends TypeRep
}
