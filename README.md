# Augusta

Augusta is simple, low-level, systems programming language based on Ada. This repository
describes the Augusta language and provides a compiler for that language called AGC (pronounced
"Agency"). The compiler translates Augusta source files into either LLVM assembly language,
where final code generation can be done by LLVM, or into C99 that can be compiled with any
standard-conforming C compiler. AGC is written in Scala.

This project is very much in the early stages. The Augusta language is not well-defined and the
AGC compiler is not usable at this time. However, we intend to keep the documentation and
compiler in a buildable state, so it should always be possible to follow the progress of the
project using concrete artifacts. We welcome contributions and feedback from any interested
persons.

Augusta/AGC's development is done on Windows, macOS, and Linux. We intend to fully support all
three platforms, both for the development of AGC and for developing and running Augusta
programs.


## Vision

The original goal of this project was to create a full-featured Ada compiler. However, that
goal has always been unrealistic. In December 2024, we radically reimagined this project.

The new vision for Augusta is to be a language that is simple, easy to learn and implement, and
yet powerful enough for serious programming. Augusta takes inspiration from C in terms of its
scope, but borrows syntax and semantic behavior from Ada. It contains basic procedural
constructs, arrays, records, and pointers. It uses a simplified version of Ada's type system and
packaging mechanism, and it eliminates many of Ada's more advanced features.

Inspired by the development of RISC hardware, the concept is for Augusta to be a self-contained
core language from which various extensions can be created through a variety of *extension
points*. The precise nature of these extension points has yet to be defined, but may include:

+ Aspects, pragmas, or annotations.
+ Hygienic macros.
+ Compiler plugins.
+ API access to compiler data structures (compiler-as-library)

The idea is to create a language that invites experimentation and the development of advanced
tooling, rather than creating a language with numerous built-in features (i.e., Ada).

In addition to off-loading advanced language features to compiler extensions and tools, we feel
that the infrastructure and ecosystem surrounding a language is as important as the language
itself. Thus, we hope to provide Augusta support for popular editors and IDEs, and some as
yet-to-be-determined build and package management system (for example,
[Bazel](https://bazel.build/)). We hope the early availability of these tools will encourage the
growth of an active community around Augusta and accelerate its development.

Obviously this is a grand goal that will take time to achieve. The first stage of this effort is
to define the base language (which we call "Augusta Level 1," or simply "L1") and implement the
AGC compiler for that language.


## Ada Subset?

When we say August is a "subset" of Ada we mean every valid Augusta program is also a valid Ada
program. However, it is an open question if Augusta will ultimately be a subset of Ada or not.
It is our intention to make Augusta as compatible with Ada as possible, but we also want to
honor the vision of Augusta being a simple language to learn and implement. At the moment we
regard the sequence of Augusta levels (L1, L2, etc.) as converging toward full Ada, but even
that is not certain at this time.

For example, Augusta Level 1 requires less checking of the source program than Ada does in the
interest of simplicity, even for Ada constructs L1 supports. This makes L1 not a strict subset
of Ada since some programs acceptable to an L1 compiler, would not be acceptable to an Ada
compiler. However, we expect that some Ada-mandated checks could be added back with the help of
Augusta's extension points. For example, a tool or compiler-plugin could be created that checks
for Ada compatibility. Also, Augusta Level 2, when it is defined, may introduce some of the
missing checks. This would technically break compatibility with L1, but it would move L2 closer
to Ada.

More details about Ada compatibility along with the rationale for breaking compatibility in
certain cases is provided in the Augusta Language Reference Manual.


## Building

### Prerequisites

The prerequisites necessary for setting up an Augusta/AGC development system are listed below.
The version numbers given are for the specific versions we are using. In most cases, other
closely related versions would probably also work, but have not been tested.

_TODO:_ Discuss how to install SBT via Coursier.

+ [Java](https://www.oracle.com/java/technologies/downloads/) (21.0.6)

  There are some Java source files in AGC, so a JRE is not sufficient. The Java compiler is
  required.
  
+ [SBT](https://www.scala-sbt.org/) (1.10.7)

  SBT is the build tool we use. It knows how to download the various libraries and certain other
  components that are required. SBT will download the Scala compiler we use; you do not need to
  install Scala separately.

+ [Python](https://www.python.org/) (3.13.1)

  Sphinx, a Python package, is used to build the documentation. The instructions for setting up
  the necessary virtual environment with all required components are detailed below.
  
+ [LLVM](http://llvm.org/) (19.1.4)

  Augusta generates code for the Low Level Virtual Machine (LLVM). Only the back-end tools from
  the LLVM project are needed. None of the front-end compilers (gcc, clang, etc.) are necessary.

  _TODO:_ Document how to set up LLVM.

### Python Virtual Environment

The Augusta/AGC documentation is written using reStructuredText (reST) and compiled with Sphinx
into presentation formats. If you intend to build the documentation, you will need to set up a
Python virtual environment as described here. The build of AGC itself does not require this, but
we use Python as our official scripting language so in the future Python might be used in other
supportive roles. Therefore, we encourage every developer to go through these steps regardless
of your current needs.

First, install a suitable version of Python. Use at least version 3.6 as a minimum for Sphinx,
but a more recent version is encouraged (we use 3.13.x). Then create a virtual environment in
the root folder of the repository as follows:

```bash
$ python -m venv venv
```

This runs the module `venv` and creates a virtual environment in the `venv` folder. To activate
the virtual environment, run the following command on Unix-like systems:

```bash
$ source venv/bin/activate
```

On Windows, use the command `.\venv\Scripts\Activate.ps1` (for PowerShell users) or
`.\venv\Scripts\activate` (for cmd.exe users).

Activation changes the environment of your shell so that the Python resources in the virtual
environment are directly available. When the virtual environment is activated for the first
time, do:
    
```bash 
$ pip install -r requirements.txt
```

This installs all required packages into the virtual environment. You only need to do this once.
When you are done working with Python you can deactivate the virtual environment by running:

```bash
$ deactivate
```

### AGC

After installing the Java and SBT prerequisites, you can build AGC by issuing the following
commands at a shell prompt in the root folder of the repository:

    $ sbt compile  # Compiles the system.
    $ sbt test     # Executes the unit tests.
    $ sbt assembly # Builds the "uber-JAR" containing AGC and all dependencies.
    
These commands will work on all supported operating systems (Windows, macOS, and Linux). Note
that "test" and "assembly" imply "compile," so you do not need to run "compile" separately.
However, doing so can be helpful in isolating errors. The first time you run these commands, SBT
will download many components, including the Scala compiler and the various libraries that AGC
depends on. This can take some time, depending on your network connection.

The resulting JAR file is in `target/scala-3.3.4`. The file is large because it contains all of
AGC's dependencies and is completely self-contained. You can deploy AGC by simply copying that
JAR file to another system and executing it.

Additional SBT commands of interest are:

    $ sbt clean    # Deletes all build artifacts.
    $ sbt doc      # Builds the API documentation in target/scala-3.3.4/api.
    $ sbt run      # Runs AGC interactively.
    $ sbt console  # Starts the Scala REPL with AGC's classpath configured.

See the [SBT documentation](https://www.scala-sbt.org/documentation.html) for more details about
SBT.

### Documentation

The documentation can be built using Sphinx. This process is described in the README in the
`doc` folder.

### Tutorials

Since AGC's source language (Augusta/Ada) is different from its implementation language (Scala),
we expect that potential contributors from either language community may want to consult a
tutorial about the other language. Some resources can be found on the [Augusta
Wiki](https://github.com/pchapin/augusta/wiki). However, this repository also contains a
tutorial for Scala in the `tutorial/Scala` folder. At some point this tutorial might be moved to
its own repository, but that won't happen until it is more complete.

The `tutorials` folder may contain other tutorials in the future that are relevant to
Augusta/AGC development. There is also a `tutorial` folder in `doc` that contains a
user-oriented tutorial on Augusta/AGC itself.

## Development Environments

We primarily use two development environments for working on Augusta/AGC: Visual Studio Code and
IntelliJ IDEA. Visual Studio Code is a general tool that can work across the entire project
seamlessly. We use IntelliJ to focus specifically on the Scala code base. Both tools support all
three of the platforms we target.

We recommend that you execute the full build from the console before configuring your
development environment. It is easier to troubleshoot the build without the complexity of a
large development environment interfering. Also, SBT will download all prerequisites, including
the Scala compiler, reducing the chance of spurious errors when you first configure your other
tools.

During normal development, continue to build the project via SBT either from the command line or
from within your development environment. This ensures the build is done properly. The SBT build
is the source of truth for the project's configuration and dependencies, so it should be used
consistently. One advantage of this is that any tool that knows how to work with SBT will work
for Augusta/AGC development.

### Visual Studio Code

We recommend first setting up the Python virtual environment for Sphinx, as described above,
before configuring Visual Studio Code. VS Code will automatically detect the virtual environment
and configure itself appropriately. If the virtual environment is not in place, you may
encounter some errors while using Visual Studio Code if you try working on the Python components
of the project.

Download and install [Visual Studio Code](https://code.visualstudio.com/). Then, install the
following extensions into Visual Studio Code:

+ Scala (Metals) by Scalameta
+ ANTLR4 grammar syntax support by Mike Lischke
+ Python by Microsoft (needed for editing Python support scripts)
+ reStructuredText by LeXtudio (needed for editing the documentation)
+ XML by Red Hat (needed for editing the Scala tutorial)

For basic development only the Scala and ANTLR4 extensions are needed. The others only pertain
to particular aspects of the project as indicated above and aren't necessary to install if you
don't anticipate working on those aspects.

Other extensions that may be useful include:

+ Code Spell Checker by Street Side Software
+ GitLens by GitKraken
+ Rewrap by stkb
+ Todo Tree by Gruntfuggly

The Rewrap extension is particularly useful for editing comments and documentation to ensure
that text wraps neatly. Use Alt+Q (or Option+Q on macOS) to rewrap a paragraph. The Todo Tree
extension is useful for tracking TODO and FIXME comments in the code.

Open Visual Studio Code on the top level folder of this repository. The Metals extension should
notice that there is an SBT build defined, and prompt you to import it. Some time is required to
execute this import and perform related indexing. Also, when you first open a Scala file, there
is some additional time required while Metals compiles the project.

### IntelliJ IDEA

Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/). The Community Edition
should be sufficient. Then, install the following plugins into IntelliJ:

+ Scala
+ ANTLR

Open IntelliJ IDEA on the top level folder of this repository. As with Visual Studio Code,
IntelliJ should notice the SBT build and prompt you to import it. This will also take some time
while IntelliJ configures and indexes the project.


## Contact Information

If you have questions, concerns, want to report bugs, contribute code, or share words of
support, you can follow the project or contact us through one of the email addresses listed in
the CONTRIBUTORS.md file.

The Augusta Contributors  
