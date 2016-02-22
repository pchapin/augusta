//-----------------------------------------------------------------------
// FILE    : CompilationUnit.scala
// SUBJECT : Abstract description of a compilation unit.
// AUTHOR  : (C) Copyright 2011 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package edu.vtc.augusta

/**
 * Trait that abstractly describes a compilation unit. The intent is for this trait to only
 * describe compilation units that have been compiled successfully. However, if this unit or one
 * of its dependencies fails to recompile this unit might temporary refer to an invalid unit.
 *
 * Each CompilationUnitLibrary has a factory method that returns compilation units from a
 * library specific subclass of this trait. The implementation of the methods defined here are
 * thus library specific.
 */
trait CompilationUnit {

  /** Returns the fully qualified name of the compilation unit or subunit. */
  def name: String

  /**
   * Returns the fully qualified names of the compilation units on which I depend. Note that
   * some or all of these units might be in compilation unit libraries other than the one
   * containing this unit.
   */
  def dependencies: List[String]

}
