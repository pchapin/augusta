//-----------------------------------------------------------------------
// FILE    : FileSystemLibrary.scala
// SUBJECT : A compilation unit library implemented as files in the file system.
// AUTHOR  : (C) Copyright 2011 by Peter C. Chapin <PChapin@vtc.vsc.edu>
//
//-----------------------------------------------------------------------
package org.pchapin.augusta

import java.io.File

class FileSystemLibrary extends ProgramLibrary {
  import FileSystemLibrary._
  
  /**
   * TODO: The root of the file system library should be a configurable parameter.
   */
  def lookup(name: String): Option[CompilationUnit] = {
    val fileName = name map { ch => if (ch == '.') File.separatorChar else ch }
    val unitFile = new File(fileName)
    if (unitFile.isFile) Some(new FileSystemUnit(name, unitFile)) else None
  }

}

object FileSystemLibrary {

  private class FileSystemUnit(
    val name: String, private val file: File) extends CompilationUnit {

    // TODO: The constructor should read the .asi/.abi file and extract the stored information.
    
    def dependencies: List[String] = {
      List()
    }

  }

}