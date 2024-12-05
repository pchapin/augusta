# Augusta

Augusta is an open source compiler for an Ada-like subset written in Scala that targets LLVM.
This document is a quick description of how to get set up for Augusta development. For more
information, see the documentation in the 'doc' folder.

Augusta is not usable at this time. However, we intend to keep the system in a buildable state,
so it should always be possible to create the Augusta jar file as well as execute whatever tests
have been written to date.

Augusta's development is done on Linux, macOS, and Windows. We expect it would be possible to do
Augusta development on any system that supports the prerequisites. Mostly, that means any system
that supports Java, LLVM, and Python.


## Vision

The original goal of the Augusta project was to create a full-featured Ada compiler. However, we
fully realize that goal is, and always has been unrealistic given our limited resources. Thus,
we are now focusing on creating a compiler for a minimal Ada-like language that can stand on its
own merits. Maintaining complete compatibility with Ada is not a priority. The August language,
while inspired by Ada, is its own entity.

The vision for Augusta is to create a language that is simple, easy to learn and implement, and
yet powerful enough for serious programming. August takes inspiration from C in terms of its
scope. It contains basic procedural constructs, arrays, records, and pointers. It uses a simplified
version of Ada's type system and packaging mechanism, but it eliminates many of Ada's more advanced
features:

+ No exceptions.
+ No controlled types.
+ No tagged types.
+ No tasking or protected objects.
+ Simplified pointer semantics (less strict accessibility rules).

Augusta will ultimately include certain complicated Ada features such as generics. It will also
support something similar to Ada's aspect syntax to allow tools to interact with Augusta
programs in a well-defined way.

In fact, the idea of Augusta is to off-load as much language complexity to tools as possible. To
that end, the syntax and semantics of Augusta are designed to be simple and easy to parse. Also,
various language extension and tool interface points will be provided in the Augusta syntax,
such as aspects and pragmas, and in the Augusta compiler, such as plug-ins and APIs.

The modern trend in language design is for programming languages to become ever more complex and
feature rich the longer they exist. Each language standard adds more functionality, but also
more complexity. This makes it difficult for compiler vendors to keep up, for programmers to
learn and use the language, and discourages new compiler projects from starting.

Augusta is an attempt to reverse that trend. It is a language that is simple and easy to learn,
and that is not intended to grow indefinitely. Instead, functionality can be off loaded to tools
as necessary. In effect, Augusta is a kind of RISC-like programming language, relying on
extra-linguistic facilities to provide important functionality.

This is the vision of Augusta.


## Scala?

We are aware that using Scala as the implementation language for an Ada-like compiler is an
unusual choice, and that it may inhibit potential contributors from getting involved. We chose
it because we believe Scala is a good language for compiler implementation, bringing features to
the table for which many other languages are weaker (for example, algebraic data types). It also
allows us to leverage the language processing libraries and tools in the Java/Scala ecosystem,
which are considerable.

However, since the vision of Augusta is to be a simple language, we encourage others to
implement Augusta in other ways. The effort in doing so should not be extreme. In fact, that is
the point of this project.

We note that we use the name "Augusta" both for the language and for the compiler. That may
change in the future, but for now, it is a convenient shorthand. The precise meaning of
"Augusta" (language vs. compiler for that language) should be clear from context.


## Recent Restart

Recently (2024-11-21), this project was "reset" to a mostly initial state and restarted with a
significant change in vision and organization. The `master` branch reflects the state of the
project before the restart. That branch should be considered legacy-only. It is retained for
reference since some of the code in that branch may be integrated back into this new development
effort. In the future, the `master` branch may be tagged as `legacy` and then deleted. You
should follow the `main` branch if you want to follow the unfolding development of Augusta.

Here is a partial list of changes relative to the previous system. If you are not familiar with
the previous system, you can ignore this list.

+ The Dragon and Tiger subprojects have been removed. They didn't contain much of interest. Any
  code that was or would have been put into those subprojects that is still useful will
  eventually be brought back into the main Augusta project.
  
+ The main documentation format has been changed from DocBook to
  [reStructuredText](https://devguide.python.org/documentation/markup/). DocBook is a great
  system, but not an ideal match for the needs of this project. reST feels more appropriate and
  will make it easier for others to contribute.
  
+ The build now includes the [Cats](https://typelevel.org/cats/), [Cats
  Effect](https://github.com/typelevel/cats-effect), and
  [Kiama](https://github.com/inkytonik/kiama) libraries, setting the stage for a more functional
  style in the code base. For now, ANTLR is being retained as the parser generator of choice,
  but we may consider a more Scala-specific solution (perhaps
  [FastParse](https://com-lihaoyi.github.io/fastparse/)) going forward.


## Prerequisites

The prerequisites necessary for setting up an Augusta development system are listed below. The
version numbers given are for the specific versions we are using. In most cases, other closely
related versions would probably also work, but have not been tested.

+ [Java Development Kit](https://www.oracle.com/java/technologies/downloads/) (21.0.x)

  There are some Java source files in Augusta, so a JRE is not sufficient. The Java compiler is
  required.
  
+ [SBT](https://www.scala-sbt.org/) (1.10.5)

  SBT is the build tool we use. It knows how to download the various libraries and certain other
  components that are required (such as the Scala compiler).
  
+ [Sphinx](https://www.sphinx-doc.org/en/master/) (8.1.3)

  We use Sphinx to process the reStructuredText documentation. More information about setting up
  the documentation build environment can be found in the README in the `doc` folder. Sphinx
  (and Python) are not required for building Augusta itself.

+ [LLVM](http://llvm.org/) (19.1.4)

  Augusta generates code for the Low Level Virtual Machine (LLVM). Only the back-end tools from
  the LLVM project are needed. None of the front-end compilers (gcc, clang, etc.) are necessary.
  _TODO:_ Document how to set up LLVM more fully.

+ [GNAT](https://www.getada.dev/)

  The Augusta runtime system is largely written in Ada. Since Augusta is currently not able to
  compile it, AdaCore's GNAT is used for runtime system development. The GNAT compiler can be
  installed via the Alire tool.

+ A Development Environment

  Any development environment that can interact with SBT projects will work. We do not
  specifically recommend one, but we use [Visual Studio Code](https://code.visualstudio.com/)
  with the Metals extension or [IntelliJ IDEA](https://www.jetbrains.com/idea/) with the Scala
  plugin. A plain text editor of your choice can also work. SBT commands can be issued at a
  console/terminal prompt to build the system. _TODO:_ Document how to set up a development
  environment more fully.


## Building

### Augusta

After installing a suitable version of Java and a recent version of SBT, you can build Augusta
by issuing the following commands at a shell prompt:

    $ sbt compile  # Compiles the system.
    $ sbt test     # Executes the unit tests.
    $ sbt assembly # Builds the "uber-jar" containing Augusta and all dependencies.
    
These commands will work on all supported operating systems (Linux, macOS, and Windows).

The resulting JAR file is in `target/scala-3.3.4`. The file is large because it contains all of
Augusta's dependencies and is completely self-contained. You can deploy August by simply copying
that JAR file to another system and executing it. See the [SBT
documentation](https://www.scala-sbt.org/documentation.html) for more details about SBT.

### Documentation

The documentation can be built using Sphinx. This process is described in more detail in the
README in `doc` folder.


## Contact Information

If you have questions, concerns, want to report bugs, contribute code, or share words of
support, you can follow the project or contact us through one of the email addresses listed in
the CONTRIBUTORS file.

The Augusta Contributors  
