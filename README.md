# Augusta

Augusta is an open source Ada 2012/2022 compiler written in Scala that targets LLVM. This
document is a quick description of how to get set up for Augusta development. For more
information, see the documentation in the 'doc' folder, particularly the file Build.xml.

Augusta is not even remotely usable at this time. However, we intend to keep the system in a
buildable state, so it should always be possible to create the Augusta jar file as well as
execute whatever tests have been written to date.

Augusta's development is done on Linux, macOS, and Windows. We expect it would be
possible to do Augusta development on any system that supports the prerequisites. Mostly, that
means any system that supports Java and LLVM.


## Disclaimer

First, let me assure you that we are completely aware that the goal of writing a full-featured
Ada compiler is unrealistic for us at this time. We are not so naive as to think it is feasible
given our limited resources. Our true goals are:

+ Having fun working on a compiler for a semi-realistic, even if minimal, language based on Ada.

+ Creating educational opportunities for other hobbyists and students.

This project is done entirely for fun and to learn. To the extent that it satisfies those goals,
we will judge it as a success. If, at some future time, this project ever attracts enough
attention and resources to significantly push it forward... well, we can revisit our goals then.


## Prerequisites

The prerequisites necessary for setting up an Augusta development system are listed below. The
version numbers given are for the specific versions we are using. In most cases, other closely
related versions would probably also work, but have not been tested.

+ Augusta uses several third party libraries. The jar files for those libraries are not
  included as part of this repository, but are downloaded by SBT during the build process.

+ [Java Development Kit](https://www.oracle.com/java/technologies/downloads/) (21.0.3)

  There are some Java source files in Augusta, so a JRE is not sufficient.
  
+ [SBT](https://www.scala-sbt.org/) (1.9.7)

  SBT is the build tool we use. It knows how to download the various libraries and certain other
  components that are required (such as Scala).

+ DocBook tool chain

  The documentation for Augusta is written in an XML vocabulary called DocBook. The source files
  for the documentation can be read and edited directly in a text editor if necessary. If you
  want to convert the documentation into some other format (HTML, PDF, etc.) you will need a
  DocBook tool chain. We use the commercial XML editor <oXygen/>. Open source alternatives
  exist.

  It is possible to view and edit the XML source of the documentation from inside IntelliJ (see
  below). Depending on your needs, this may be sufficient. To do this, you will first need to
  download and unpack the DocBook 5.1 RelaxNG schema to any suitable location on your system.
  Next, continue with the installation of IntelliJ as described below.

+ [IntelliJ IDEA](http://www.jetbrains.com/idea/) (2024.2.4)

  Officially, we use the Community Edition, but some of us use the Ultimate edition. You will
  need to download and install the Scala plugin from the plugin control panel (or during the
  installation of IntelliJ when you are given the opportunity to install popular plugins). You
  may have to configure IntelliJ to find your Java JDK.

  You also need to define the IntelliJ path variable `DOCBOOK_XML` to point at the folder
  containing the DocBook 5.1 RelaxNG schema.

  We also recommend that you install the ANTLR v4 grammar support plugin and the Markdown
  editing plugin from the IntelliJ plugins repository.

+ [LLVM](http://llvm.org/) (7.0.1)

  Augusta generates (will eventually generate) code for the Low Level Virtual Machine (LLVM).
  Only the back-end tools from the LLVM project are needed. None of the front-end compilers
  (gcc, clang, etc) are necessary. Detailed instructions for installing the LLVM tools are
  outside the scope of this document. See the LLVM website or the file Build.xml in the
  documentation folder for more information.

+ [GNAT](http://www.adacore.com/community) (GNAT-Community-2021)

  The Augusta run time system is largely written in Ada. Since Augusta is currently not mature
  enough to compile it, GNAT is (temporarily) used for run time system development.


## Building

### Augusta

The Augusta project includes two additional modules named Tiger and Dragon.

The Tiger module is an implementation of the Tiger programming language as described in the book
"Modern Compiler Implementation in ML" by Andrew W. Appel. This module is not directly related
to Augusta and is used as a testing ground for implementation ideas that might be useful in
Augusta. Solutions to some exercises in the book (all in Scala) are also included.

The Dragon module is a library of useful material of interest to compiler writers. It factors
out code that can be shared between Augusta, Tiger, and potentially other compiler projects that
use Scala as an implementation language. It also contains other tidbits of general interest.

The Augusta runtime system is in the RTS folder. It is written in Ada and is currently compiled
and tested using GNAT with GPS as the integrated development environment. Load stdlibrary.gpr in
the RTS/SL/src folder into GPS to build the current version of the standard library. At the time
of this writing, the runtime library in RTS/RL is empty.

The Dragon and Tiger projects can be compiled via SBT. The SBT build control file has all the
necessary dependencies declared so that building one project should trigger the download of the
required libraries and the building of required subprojects. The usual SBT tasks can be used to
build API (internal) documentation, run tests, and so forth. See the [SBT
documentation](https://www.scala-sbt.org/documentation.html) for more details.

### Documentation

The external documentation can be built using your DocBook tool chain. You will find the DocBook
sources of the documentation in the 'doc' folder. That folder also contains other documentation
in the form of tutorials and presentations. Relevant reference material that is not developed as
part of this project, such as the Ada reference manual, can be found elsewhere. A placeholder
folder under 'doc' named 'references' can be used to store local copies of these additional
reference materials. See the README file in the references folder for more information.

Two tutorials, also in DocBook format, are provided. One targets Ada programmers wishing to
learn Scala, and the other targets Scala programmers wishing to learn Ada.

## Testing

Each SBT project has associated tests. They are stored in the 'test' folder of each project, as
usual for SBT. All tests use the ScalaTest testing framework.

Testing of the standard library is currently done with GNAT and the AUnit test framework. After
loading stdlibrary.gpr into GPS as described above, you can build and run the test program from
inside GPS.

## Contact Information

If you have any questions or concerns about this project, or if you are interested in reporting
bugs, contributing code, or generally giving words of support, you can follow the project or
contact me at one or more of the resources below:

Peter Chapin  
spicacality@kelseymountain.org  
