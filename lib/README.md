
Libraries
=========

This folder contains third party libraries used by August and its related projects. These
libraries are pulled from the Maven repository by IntelliJ during the build process. The precise
versions in use are documented in IntelliJ's configuration.

Note that Augusta does *not* use Maven as a build system. Augusta uses IntelliJ for all building
operations. However, IntelliJ has been configured to access the Maven repositories so that
developers are not burdened with the need to download and install these libraries manually.

+ [ANTLR](http://www.antlr.org/)

  Augusta makes use of the ANTLR parser generator to convert grammars into parsers.

+ [Graph for Scala](http://www.scala-graph.org/)

  Graph for Scala is a library providing a DSL for graph manipulation in Scala. Here "graph"
  refers to the data structure with verticies and edges, not XY plots! It is used for creating
  and managing control flow graphs. This is just the core module.

+ [Groovy](http://www.groovy-lang.org/)

  Groovy is a scripting language that runs on the JVM. We use it for scripting and other
  administrative tasks. Because of its JVM connection is possible (should we need to do it) to
  call Scala or Java code from Groovy scripts.

+ [Kiama](http://code.google.com/p/kiama/)

  Kiama is a Scala library for language processing. It is currently used by the SLEM library in
  the Dragon project and may be useful for other things as well.

+ [ScalaTest](http://www.scalatest.org/)

  Scala unit testing is done with the ScalaTest framework.

+ [Scalaz](https://github.com/scalaz/scalaz)

  Scalaz is a library for functional programming. It provides some purely functional data
  structures and foundational type classes. This is just the core module.
