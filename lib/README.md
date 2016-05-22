
Libraries
=========

This folder contains third party libraries used by August and its related projects. Note that
many of the libraries also have associated Javadoc jar files. Those files should be downloaded
and put in this folder as well.

+ [ANTLR](http://www.antlr.org/) (4.5.1)

  antlr-4.5.1.jar. Augusta makes use of the ANTLR parser generator to convert grammars into
  parsers.

+ [Graph for Scala](http://www.scala-graph.org/) (1.9.2)

  graph-core_2.11-1.9.2.jar. Graph for Scala is a library providing a DSL for graph manipulation
  in Scala. Here "graph" refers to the data structure with verticies and edges, not XY plots! It
  is used for creating and managing control flow graphs. This is just the core module.

+ [Groovy](http://www.groovy-lang.org/) (2.4.3)

  groovy-binary-2.4.3.zip. Groovy is a scripting language that runs on the JVM. We use it for
  scripting and other administrative tasks. Because of its JVM connection is possible (should we
  need to do it) to call Scala or Java code from Groovy scripts. Unzip the archive in the `lib`
  folder to create a `groovy-2.4.3` folder. You should also download the corresponding
  documentation archive and unzip it in the same place. This will create an `html` folder inside
  `groovy-2.4.3` holding extensive documentation.

+ [Kiama](http://code.google.com/p/kiama/) (1.8.0)

  kiama_2.11-1.8.0.jar. Kiama is a Scala library for language processing. It is currently used
  by the SLEM library in the Dragon project and may be useful for other things as well.

+ [ScalaTest](http://www.scalatest.org/) (2.2.4)

  scalatest_2.11-2.2.4.jar. Scala unit testing is done with the ScalaTest framework.

+ [Scalaz](https://github.com/scalaz/scalaz) (7.1.1)

  scalaz-core_2.11-7.1.1.jar. Scalaz is a library for functional programming. It provides some
  purely functional data structures and foundational type classes. This is just the core module.
