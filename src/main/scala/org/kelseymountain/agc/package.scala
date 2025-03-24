package org.kelseymountain.agc

import org.kelseymountain.agc.TypeRep.*

/**
 * Represents names of Augusta identifiers. This is a placeholder in the sense that it is
 * subject to elaboration into a specialized class if the need ever arises. Note that
 * "identifier" in this context refers to any name that is not the name of a type.
 */
type IdentifierName = String

/**
 * Represents Augusta type names. This is a placeholder in the sense that it is subject to
 * elaboration into a specialized class if the need ever arises.
 */
type TypeName = String

/**
 * Represents names of basic blocks. The name serves as a label on the basic block when
 * generating LLVM IR, and it is used to identify the basic block in the CFG. This is a
 * placeholder in the sense that it is subject to elaboration into a specialized class if the
 * need ever arises.
 */
type BlockName = String

/**
 * Computes a symbol table containing built-in type declarations. The returned symbol table
 * can be used to initialize a new symbol table stack or symbol table tree.
 */
def preDefinedSymbols: BasicSymbolTable =
  val preDefinedScope = new BasicSymbolTable
  // TODO: Configure the range of Augusta integers with a platform configuration file(?)
  preDefinedScope.addTypeByName("Integer", RangeRep(Int.MinValue, Int.MaxValue))
  preDefinedScope.addTypeByName("Natural", SubtypeRep("Integer", RangeRep(0, Int.MaxValue)))
  preDefinedScope.addTypeByName("Positive", SubtypeRep("Integer", RangeRep(1, Int.MaxValue)))
  preDefinedScope.addTypeByName("Boolean", EnumRep(List("False", "True")))
  preDefinedScope

/**
 * Internal compiler errors are handled by throwing instances of this exception. They are not
 * reported in the usual way. Instead, the compiler is intended to abort.
 *
 * @param message A human-readable, but not necessarily user-friendly, message describing the
 * error.
 */
class InternalErrorException(message: String) extends Exception(message)
