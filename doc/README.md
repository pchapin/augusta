
README
======

This folder contains the main Augusta documentation set. There are four independent documents,
each in its own subfolder.

- **Design Documentation** describes the design of Augusta, including architecture and
  implementation notes.

- **Reference Manual** provides a detailed reference for Augusta and the specific subset of Ada
  that it compiles.

- **Tutorial** is a step-by-step guide to using Augusta, including getting-started instructions
  and simple examples.

- **User Guide** is a more complete description of how to use Augusta, with detailed
  installation instructions, and examples of Augusta's more advanced features.

## Building the Documentation

The Augusta documentation is written using reStructuredText (reST) and compiled with Sphinx into
presentation formats. To set up Sphinx, first install a suitable version of Python (3.6 or
later) and then create a virtual environment in the `doc` folder as follows:

```bash
$ python -m venv venv
```

This runs the module `venv` and creates a virtual environment in the `venv` folder. To activate
the virtual environment, run the following command:

```bash
$ source venv/bin/activate
```

On Windows, replace the `source` command by `.\venv\Scripts\activate`.

When the virtual environment is activated for the first time, do:
    
```bash 
$ pip install -r requirements.txt
```

This installs the required packages based on the list in `requirements.txt`. You can then build
the documentation by changing into the desired subfolder and running the following command:

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

