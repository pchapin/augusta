
Libraries
=========

This folder contains third party libraries used by Daja and its related projects and that are
*not* automatically downloaded and managed by SBT. Any jar file deposited here should be
picked up by SBT and included in the classpath of the Daja projects.

The following libraries need to be downloaded manually and put into this folder:

+ The [Graph-for-Scala core
  library](https://repo1.maven.org/maven2/org/scala-graph/graph-core_2.13/1.13.6/graph-core_2.13-1.13.6.jar).
  I don't know how to get SBT to automatically download the version of this library for Scala
  2.13 when the Scala version of the overall project is set to a 3.x version. See the comments
  in `build.sbt` and `projects/Dependencies.scala` for more information.
  
