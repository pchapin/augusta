//-----------------------------------------------------------------------
// FILE    : ProgramLibrary.scala
// SUBJECT : Abstract description of a library of compiled compilation units.
// AUTHOR  : (C) Copyright 2011 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package org.kelseymountain.augusta

trait ProgramLibrary {

  /**
   * Look up a compilation unit in the library.
   *
   * @param name The fully qualified name of the unit to locate.
   *
   * @return Some(compilationUnit) if found; None otherwise. Would it be better to throw an
   * exception if the unit is not found?
   */
  def lookup(name: String): Option[CompilationUnit]

}
