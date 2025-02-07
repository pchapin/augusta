
README
======

This folder contains the main Augusta/AGC documentation set. There are four independent
documents, each in its own subfolder.

- **Design Documentation** describes the design of AGC, including architecture and
  implementation notes.

- **Language Reference** provides a detailed reference for the Augusta language. This document
  is written in an implementation-independent manner and applies to all Augusta compilers, not
  just AGC.

- **Tutorial** is a step-by-step guide to using AGC to compile Augusta programs, including
  getting-started instructions and simple examples.

- **User Guide** is a more complete description of how to use AGC, with detailed installation
  instructions, and examples of the compiler's more advanced features.

## Building the Documentation

First, be sure you have configured the python virtual environment as described in the main
README. Next, activate that virtual environment. You can then build the documentation by
changing into the desired subfolder and running the following command:

```bash
$ make html
```

## Additional Documentation

In this folder, the file `Ada2012.g4` is the ANTLR grammar for Ada 2012. It is nothing more than
a copy of the grammar in the Ada 2012 standard and thus not suitable as the basis for a parser.
The file is included for reference only.

The subfolder `jEdit` contains documentation related to the jEdit text editor. In particular, it
contains an enhanced version of jEdit's Ada mode.

Finally, the subfolder `references` is a placeholder for additional reference material that each
developer may wish to keep on hand. It exists as a convenience for developers and is not part of
the Augusta documentation set.

See the README files in each subfolder for more information about the contents of that
subfolder.

