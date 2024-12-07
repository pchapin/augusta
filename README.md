# Augusta

Augusta is simple, low-level, systems programming language based on Ada. This repository
describes the Augusta language and provides a compiler for that language called AGC (pronounced
"Agency"). The compiler translates Augusta source files into either LLVM assembly language,
where final code generation can be done by LLVM, or into C99 that can be compiled with any
standard-conforming C compiler. Currently, AGC is written in Scala.

This project is very much in the early stages. The Augusta language is not well-defined and the
AGC compiler is not usable at this time. However, we intend to keep the documentation and
compiler in a buildable state, so it should always be possible to follow the progress of the
project using concrete artifacts. We welcome contributions and feedback from any interested
persons.

Augusta/AGC's development is done on Windows, macOS, and Linux. We intend to fully support all
three platforms, both for development and for running AGC-generated code.


## Vision

The original goal of this project was to create a full-featured Ada compiler. However, that
goal has always been unrealistic. In December 2024, we radically reimagined this project.

The new vision for Augusta is to be a language that is simple, easy to learn and implement, and
yet powerful enough for serious programming. Augusta takes inspiration from C in terms of its
scope, but borrows syntax and much semantic behavior from Ada. It contains basic procedural
constructs, arrays, records, and pointers. It uses a simplified version of Ada's type system and
packaging mechanism, and it eliminates many of Ada's more advanced features.

Inspired by the development of RISC hardware, the concept is for Augusta to be fundamentally a
simple, but well-structured language that allows extensions through a variety of *extension
points*. The precise nature of these extension points has yet to be defined, but may include:

+ Language aspects, pragmas, or annotations.
+ Hygienic macros.
+ Compiler plugins.
+ API access to compiler data structures.

The goal is to create a language that invites experimentation and the development of advanced
tooling by using a simple and uncomplicated base.

In addition to off-loading advanced language features to compiler extensions and tools, we feel
that the infrastructure and ecosystem surrounding a language is as important as the language
itself. Thus, we hope to provide Augusta support for popular editors and IDEs, and some as
yet-to-be-determined build and package management system. We hope the early availability of
these tools will encourage the growth of an active community around Augusta and accelerate its
development.

Obviously this is a grand goal that will take time to achieve. The first stage of this effort is
to define the base language (which we call "Augusta, Level 1," or simply "L1") and implement the
AGC compiler for that language.


## Recent Restart

Recently (2024-11-21), this project was restarted with a significant change in vision as
described above. The repository was also significantly reorganized. The `master` branch reflects
the state of the project before the restart. That branch should be considered legacy-only. It is
retained for reference since some of the code in that branch may be integrated back into this
new development effort. In the future, the `master` branch may be tagged as `legacy` and then
deleted. You should follow the `main` branch if you want to follow the unfolding development of
Augusta/AGC.

Here is a partial list of changes relative to the previous system. If you are not familiar with
the previous system, you can ignore this list.

+ The Dragon and Tiger subprojects have been removed. They didn't contain much of interest. Any
  code that was or would have been put into those subprojects that is still useful will
  eventually be brought back into the main Augusta project.
  
+ The main documentation format has been changed from DocBook to
  [reStructuredText](https://devguide.python.org/documentation/markup/). DocBook is a great
  system, but not an ideal match for the needs of this project. reST feels more appropriate and
  will make it easier for others to contribute.
  
+ The build of AGC now includes the [Cats](https://typelevel.org/cats/), [Cats
  Effect](https://github.com/typelevel/cats-effect), and
  [Kiama](https://github.com/inkytonik/kiama) libraries, setting the stage for a more functional
  style in the code base. For now, ANTLR is being retained as the parser generator of choice,
  but we may consider a more Scala-specific solution (perhaps
  [FastParse](https://com-lihaoyi.github.io/fastparse/)) going forward.


## Building

### Prerequisites

The prerequisites necessary for setting up an Augusta/AGC development system are listed below.
The version numbers given are for the specific versions we are using. In most cases, other
closely related versions would probably also work, but have not been tested.

+ [Java](https://www.oracle.com/java/technologies/downloads/) (21.0.x)

  There are some Java source files in AGC, so a JRE is not sufficient. The Java compiler is
  required.
  
+ [SBT](https://www.scala-sbt.org/) (1.10.5)

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
Python virtual environment as described here. Technically, the build of AGC itself does not
require this, but we use Python as our official scripting language so in the future it might be
used in other supportive roles. Therefore, we encourage every developer to go through these
steps regardless of your current needs.

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

On Windows, replace the `source` command by `.\venv\Scripts\activate`.

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
JAR file to another system and executing it. See the [SBT
documentation](https://www.scala-sbt.org/documentation.html) for more details about SBT.

### Documentation

The documentation can be built using Sphinx. This process is described in the README in the
`doc` folder.

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

During normal development, continue to build the project via SBT even from within your
development environment. This ensures the build is done properly. The SBT build is the source of
truth for the project's configuration and dependencies, so it should be used consistently. One
advantage of this is that any tool that knows how to work with SBT will work for Augusta/AGC
development.

### Visual Studio Code

We recommend first setting up the Python virtual environment for Sphinx, as described above,
before configuring Visual Studio Code. VSCode will automatically detect the virtual environment
and configure itself appropriately. If the virtual environment is not in place, you may
encounter some errors while using Visual Studio Code.

Download and install [Visual Studio Code](https://code.visualstudio.com/). Then, install the
following extensions into Visual Studio Code:

+ Scala (Metals) by Scalameta
+ Python by Microsoft
+ reStructuredText by LeXtudio

Open Visual Studio Code on the top level folder of this repository. The Metals extension should
notice that there is an SBT build defined, and prompt you to import it. Some time is required to
execute this import and perform related indexing. Also, when you first open a Scala file, there
is some additional extra time required while Metals compiles the project.

### IntelliJ IDEA

Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/). The Community Edition
should be sufficient. Then, install the following plugins into IntelliJ:

+ Scala
+ ANTLR
+ Python (Optional)

Open IntelliJ IDEA on the top level folder of this repository. As with Visual Studio Code,
IntelliJ should notice the SBT build and prompt you to import it. This will also take some time
while IntelliJ configures and indexes the project.


## Contact Information

If you have questions, concerns, want to report bugs, contribute code, or share words of
support, you can follow the project or contact us through one of the email addresses listed in
the CONTRIBUTORS file.

The Augusta Contributors  
