Augusta
=======

Augusta is an open source Ada 2012 compiler written in Scala that targets LLVM. This document is
a quick description of how to get set up for Augusta development. For more information see the
documentation in the 'doc' folder, particularly the file Build.xml. The GitHub wiki (or the wiki
folder in this repository) may contain additional information for Augusta contributors.

Augusta is not even remotely usable at this time. However we intend to keep the system in a
buildable state so it should always be possible to create the Augusta jar file as well as
execute whatever tests have been written to date.

Augusta development is done primarily on Linux and Windows. However, we expect it would be
possible to do Augusta development on any system that supports the prerequisites. Mostly that
means any system that supports Java and LLVM.


Prerequisites
-------------

The prerequisites necessary for setting up an Augusta development system are listed below. The
version numbers given are for the specific versions we're using. In most cases other closely
related versions would probably also work, but have not been tested.

+ Several third party libraries are used by Augusta. The jar files for those libraries are not
  included as part of this repository, but can be downloaded from elsewhere. See the README file
  in the lib folder for more information on obtaining and setting up the required supporting
  libraries. You should set up all libraries mentioned; the IntelliJ module files assume they
  are available.

+ [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (1.8.0_77)

  There are some Java source files in Augusta so a JRE is not sufficient.

+ [Scala](http://www.scala-lang.org/) (2.11.8)

  It is important that you use a Scala version that matches the major release number mentioned
  here. For example, using a Scala version such as 2.10.x or 2.12.x will not work. However, any
  minor release within a major release (the 'x' in the version numbers above) should be fine.

+ DocBook tool chain

  The documentation for Augusta is written in an XML vocabulary called DocBook. The source files
  for the documentation can be read directly in a text editor. If you want to convert the
  documentation into some other format (HTML, PDF, etc) you will need a DocBook tool chain. We
  use the commercial XML editor <oXygen/>. Open source alternatives exist.

  It is possible to view and edit the XML source of the documentation from inside IntelliJ.
  Depending on your needs this may be sufficient. To do this you will first need to download and
  unpack the DocBook XML DTD distribution to any suitable location on your system. Next continue
  with the installation of IntelliJ as described below.

+ [IntelliJ IDEA](http://www.jetbrains.com/idea/) (2016.1.2)

  We use the Community Edition. You will also need to download and install the Scala plugin from
  the plugin control panel. You may have to configure IntelliJ to find your Java JDK.

  The Augusta project assumes a suitable Scala SDK has been configured. This can be done from
  inside IntelliJ by pointing the IDE to the folder where Scala has been installed. For the
  scaladoc build to work you need to define the `SCALA_HOME` environment variable at the
  operating system level. It should point at the folder containing your Scala installation.

  Finally for the Augusta.jar artifact to be correctly built you will need to configure the
  IntelliJ path variable `SCALA_HOME` to point at the folder containing your Scala installation.
  You also need to define the IntelliJ path variable `DOCBOOK_XML` to point at the folder
  containing the DocBook XML 4.5. DTD.

  We also recommend that you install the ANTLR v4 grammar support plugin and the Markdown
  editing plugin from the IntelliJ plugins repository.

+ [LLVM](http://llvm.org/) (3.8)

  Augusta generates code for the Low Level Virtual Machine (LLVM). Only the back-end tools from
  the LLVM project are needed. None of the front-end compilers (gcc, clang, etc) are necessary.
  Detailed instructions for installing the LLVM tools are outside the scope of this document.
  See the LLVM web site or the file Build.xml in the documentation folder for more information.

  At this time LLVM is not strictly necessary as no back-end code generation is currently being
  done.

+ [GNAT](http://libre.adacore.com/) (GPL-2015)

  The Augusta run time system is largely written in Ada. Since Augusta is currently not mature
  enough to compile it, GNAT is (temporarily) used for run time system development.


Building
--------

### Augusta

The Ada grammar that Augusta uses is currently in a formative state. However it will generate a
parser. Since the grammar is very much a work in progress the build of the parser is currently
done manually. In the top level folder of the project run `bin/build-augusta-parser.{bat,sh}`.
You will need to do this each time the grammar is updated.

The Augusta project includes two additional modules named Tiger and Dragon. There is also a
Scripts module that contains the source code for the Groovy scripts we use.

The Tiger module is an implementation of the Tiger programming language as described in book
"Modern Compiler Implementation in ML" by Andrew W. Appel. This module is not directly related
to Augusta and is used as a testing ground for implementation ideas that might be useful in
Augusta. Solutions to some of the exercises in the book (all in Scala) are also included.

The Dragon module is a library of useful material of interest to compiler writers. It factors
out code that can be shared between Augusta and Tiger. It also contains other tidbits of general
interest. The code in Dragon could potentially be used for other compiler projects as well.

The Augusta runtime system is in the RTS folder. It is written in Ada and is currently compiled
and tested using GNAT with GPS as the integrated development environment. Load stdlibrary.gpr in
the RTS/SL/src folder into GPS to build the current version of the standard library. At the time
of this writing the runtime library in RTS/RL is empty.

To build Augusta you can use IntelliJ's integrated Git support to clone the project to your
system. Then populate the lib folder (see the README in that folder for more information).
Finally you should be able to build the project from the usual IntelliJ menu items.

### Documentation

The external documentation can be built using your DocBook tool chain. You will find the DocBook
sources of the documentation in the 'doc' folder. That folder also contains other documentation
in the form of tutorials and presentations. Relevant reference material that is not developed as
part of this project, such as the Ada reference manual, can be found on the associated Assembla
site. A placeholder folder under 'doc' named 'references' can be used to store local copies of
these additional reference material. See the README file in the references folder for more
information.

Two tutorials, also in DocBook format, are provided. One targets Ada programmers wishing to
learn Scala, and the other targets Scala programmers wishing to learn Ada.

The internal documentation can be built using scaladoc with the Ant build script provided. You
can launch targets in this script from inside IntelliJ or you can execute ant at a console
prompt to build the various documentation targets. There is a separate scaladoc build for each
module. The resulting documentation is put into the 'doc' folder.


Testing
-------

Each IntelliJ module has an associated collection of test cases. They are stored in the 'test'
folder of each module. All tests use the ScalaTest testing framework. You can define IntelliJ
run configurations as appropriate to execute them.

Testing of the standard library is currently done with GNAT and the AUnit test framework. After
loading stdlibrary.gpr into GPS as described above you can build and run the test program from
inside GPS.


Contact Information
-------------------

If you have any questions or concerns about this project, or if you are interested in reporting
bugs, contributing code, or generally giving words of support you can follow the project or
contact me at one or more of the resources below:

Peter C. Chapin  
PChapin@vtc.vsc.edu  
