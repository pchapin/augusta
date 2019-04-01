package org.pchapin.augusta

import scala.collection.mutable
import org.slem.IRTree._

/**
  * A class that renames mutable maps from variable names (String) to LLVM alloca instructions
  * that represent where each variable is stored in memory.
  */
class LocationMap extends mutable.HashMap[String, L_Alloca]

object LocationMap {
  def apply() = new mutable.HashMap[String, L_Alloca]()
}