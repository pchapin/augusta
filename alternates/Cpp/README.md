
# AGC++ (An Augusta Compiler in C++)

This project is intended to be an Augusta compiler written in C++. The name, AGC++, pronounced
"Agency++," is a play on the name of the main Augusta compiler, AGC.

## Building

Building AGC++ is currently only supported on Unix-like systems such as Linux and macOS. It uses
the `bison` parser generator and the `flex` lexical analyzer generator. Support for native
Windows development is not yet available, however building on Windows via WSL should work.

To build the compiler, change into the `src` folder and run `make`. This compiles the grammar
file and all the C++ source files into an executable called `agc++`.

The grammar file can be compiled separately, if desired, by running the following command:

    $ ./build-parser.sh

We do not specify any recommended development tools at this time. Any development system that
can manage C++ builds should be sufficient.

## Documentation

AGC++'s main documentation set is in DocBook format in the `doc` folder. We consider the use of
DocBook experimental.

To build the documentation you will need to have DocBook processing tools installed on your
system that understand DocBook 5.1. We use the commercial tool
[oXygen](https://www.oxygenxml.com/) for this purpose.

The internal documentation is extracted from the C++ source code using
[Doxygen](https://www.doxygen.nl/). The Doxygen configuration file is in the `src` folder. To
build the internal documentation you will need to have Doxygen installed on your system.

Peter Chapin  
spicacality@kelseymountain.org  
