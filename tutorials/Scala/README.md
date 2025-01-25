
# Scala Tutorial

This folder contains a tutorial for the Scala programming language. It is written using DocBook
5.1. The file `ScalaTutorial.xpr` in this folder is an oXygen project file that can be used to
open the tutorial source files in the [oXygen XML editor](https://www.oxygenxml.com/). Although
oXygen is a commercial product, it is the official tool for editing this tutorial.

This tutorial can also be effectively edited in Visual Studio Code using the XML extension by
Red Hat. For maximum assistance, you should download and install the DocBook schema. See the
README file in the `lib` folder off the root of the Augusta repository for more information.
Visual Studio Code is already configured by the repository `settings.json` file to use the
schema once it is in place.

Unfortunately, oXygen and Visual Studio Code tend to produce documents with different formatting
defaults. This can create a lot of spurious changes in the Git repository unless carefully
managed. We recommend that you use one tool or the other for editing the tutorial, but not
both. At the time of this writing, the oXygen editor is the preferred tool.