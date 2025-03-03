
# Scala Tutorial

This folder contains a tutorial on the Scala programming language. It is intended to support
members of the Ada community who are interested in contributing to Augusta, but who don't know
Scala. The Augusta Wiki on GitHub also contains links to various Scala resources that might be
of interest to Augusta/AGC developers.

This tutorial is a general-purpose tutorial on Scala. It currently does not contain any
Augusta/AGC-centric material. At some future time, it might incorporate information about Scala
that is focused on the needs of the Augusta/AGC project specifically (e.g., certain libraries
used by AGC).

The `presentations` folder contains slides one of us (Peter Chapin) prepared in support of the
Programming Languages class at Vermont State University. That class was taught using Scala as a
demonstration language. The slides are evolving to make them more up-to-date, more general, and
more complete.

This tutorial is written using DocBook 5.1. The file `ScalaTutorial.xpr` in this folder is an
oXygen project file that can be used to open the tutorial source files in the [oXygen XML
editor](https://www.oxygenxml.com/). Although oXygen is a commercial product, it is the official
tool for editing this tutorial.

This tutorial can also be effectively edited in Visual Studio Code using the XML extension by
Red Hat. For maximum assistance, you should download and install the DocBook schema. See the
README file in the `lib` folder off the root of the Augusta repository for more information.
Visual Studio Code is already configured by the repository `settings.json` file to use the
schema once it is in place.

Unfortunately, oXygen and Visual Studio Code tend to produce documents with different formatting
defaults. This can create a lot of spurious changes in the Git repository unless carefully
managed. We recommend that you use one tool or the other for editing the tutorial, but not
both. At the time of this writing, the oXygen editor is the preferred tool.
