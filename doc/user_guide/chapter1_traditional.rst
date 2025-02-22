
===================
1: Traditional User
===================

In this chapter we describe the use of AGC as a traditional, command-line driven tool. We
describe installation, command-line syntax and options, error message format, and the way AGC
interacts with other tools, in particular the LLVM back-end tools.

Installation
------------

AGC requires that several prerequisites systems be installed before AGC is installed. These
prerequisite systems are listed below with some comments.

.. TODO: Is a full JDK really necessary, or is a JRE enough? 
.. TODO: Should we describe how to edit the path?

- Java Development Kit

  Augusta requires that you install a full JDK and not just a Java Runtime Environment (JRE).
  Currently, only version 21 is officially supported, but other versions may also work. After
  installation, be sure both the ``java`` and ``javac`` commands are accessible at the
  command-line. You may have to edit your path manually to do this.

  The Java API documentation ("JavaDoc") is not required in order to use AGC although you might
  find it useful if you ever build tools around AGC or extend AGC.

- Scala

  The Scala distribution contains both the runtime environment used by Scala programs and the
  Scala compiler. Currently, only version 3.3 is officially supported, but other versions may
  also work.

  The Scala API documentation ("ScalaDoc") is not required in order to use AGC, although you
  might find it useful if you ever build tools around AGC or extend AGC.

.. TODO: Should we describe how to edit the path?
.. TODO: What about the LLVM documentation? Do you need that or not?

- LLVM

  AGC requires the full set of LLVM back-end tools but none of the front-ends or test programs
  distributed by LLVM are required. After installation be sure the LLVM tools are accessible at
  the command-line. You may have to edit your path manually to do this.

This manual does not provide specific instructions for installing the prerequisites above.
However, AGC is intended to support modern versions of Windows, Linux, and macOS. The three
prerequisites are available on all of those systems although detailed installation instructions
for them vary. Note that if you are using a modern Linux distribution you may be able to install
appropriate versions of the prerequisites using your distribution's package manager.

At the time of this writing Augusta/AGC is not supported by any integrated development
environments (IDEs). To write programs you will need a text editor. Many text editors exist. We
suggest Emacs, Vim, or Visual Studio Code as good, general purpose choices. AGC integration into
an IDE is an area for future work.

Once the prerequisites are installed, AGC can be installed by simply unpacking its distribution
archive at some suitable location and adding AGC's ``bin`` directory to the path. In addition,
you should set the ``AUGUSTA_HOME`` environment variable to contain the path to the root of the
distribution after unpacking. There is no installation script.

AGC archives contain the compiler, runtime libraries, documentation, test programs, sample
programs, and source code of the AGC system. They have names of the form ``agc-yyyy-mm-dd.ext``
where "yyyy-mm-dd" is the date of release and "ext" is the file extension. The extension
specifies the archive format; use an appropriate tool to extract the files. Currently, AGC is
not versioned in the conventional sense.

The AGC archive expands to a folder ``AGC`` that contains the entire system. To upgrade AGC,
simply remove the ``AGC`` folder from your previous installation and expand the new archive in
its place. In most cases no other steps should be necessary.

It is also possible to install AGC from its GitHub repository. This gives you access to the
latest version of the system. However, be aware that there are sometimes (hopefully temporary)
problems introduced into the development system; we recommend that you only use dated releases
unless you are interested in contributing to the Augusta/AGC project. Installing AGC from GitHub
also requires that you build the system and its runtime libraries from source. The steps for
doing this are described in the repository's README file.

Command Line Syntax
-------------------

AGC is invoked using the ``agc`` command. Command-line options all begin with a dash. Every
other word on the command line is taken to be a file that AGC is to process in some way. The
action AGC takes on each file depends on the options in force at the time the file name is seen,
and on the type of the file.

AGC creates a folder ``Library`` (by default) as a direct descendant of the current folder when
it is invoked. In the ``Library`` folder AGC maintains a program library database described in
more detail elsewhere. Normally, AGC manages the contents of this database automatically. You
can usually just ignore it. If you remove the ``Library`` folder, AGC will just recreate it the
next time it is run.

The simplest way to use AGC is to just apply it to a compilation unit containing your main
procedure:

::

  agc main.agb

Here AGC compiles the ``main`` procedure in the indicated file, along with any required
compilation units, and leaves an executable named ``main`` in the same folder. This behavior can
be changed and customized with appropriate command-line options.

AGC command-line options come in four forms:

- Normal Options. These options start with ``-`` and are intended for general users.

- Advanced Options. These options start with ``-Z`` and are intended for power users interested
  in AGC internals. The output produced in response to many of the advanced options is quite
  verbose; they should be used with caution. Ordinary users may find some advanced options
  useful for debugging difficult error messages or verifying faults in AGC itself.

- Extension Options. These options start with ``-E`` and allow the AGC user a way of
  communicating with extension modules. Each use of a ``-E`` option must include an extension
  identifier followed by a colon and then the text of the option itself. For example setting the
  "indent_level" option of a style checking extension might look like
  ``-Estyle:indent_level=3``.

- Experimental Options. These options start with ``-X`` and give the AGC user access to
  experimental features that are under development or evaluation. Experimental options should be
  considered volatile and may change semantics or be removed at any time. Some experimental
  options may eventually become normal options or advanced options. Some may remain
  "experimental" indefinitely.

All AGC options are spelled with lower case letters, possibly with embedded underscores, but no
leading or trailing underscores. These restriction are imposed on extension authors as well;
AGC checks the lexical syntax of extension options before passing them to extensions.

Options that require arguments have those arguments separated from the option by a ``=`` symbol.
Spaces are not allowed in an option name nor around the ``=`` symbol, but spaces in an option's
argument are allowed provided they are quoted at the level of the command shell. In general the
syntax of an option's argument is arbitrary and depends on the option. Most of AGC's options are
fully spelled out words with only a few commonly understood exceptions. Each option has only a
single name; there are no option aliases. Extension authors are encouraged to follow these
rules.

AGC also supports *option files* that allow command-line options to be placed in a file instead
of written out on the command-line. Options stored in option files can be easier to maintain and
to generate automatically.

Whenever AGC finds an option that starts with a ``@`` it takes the text after the ``@`` to be
the name of an option file. AGC behaves as if the contents of that file were placed on the
command line at the point where the ``@`` option is specified.

Option files can contain options with one option per line. Multiple options on the same line are
not permitted and may cause surprising errors. Blank lines are ignored. Despite the name, option
files can also contain the names of files to be processed. Option files can contain other
``@``-specified option files as well. AGC will detect and report any cycles that arise in option
files from mutual ``@``-specified inclusions.

Some of the rules on option formatting are relaxed in an option file. In particular, the ``#``
character introduces comments; any text after the ``#`` character up to the end of the line is
ignored. Also spaces around the ``=`` symbol separating an option from its argument is ignored.
Finally, embedded spaces in an option's argument need not be quoted (but can be). These rules
are intended to make formatting option files easier.

Any error in the syntax of the command line (or any option file) will cause an immediate
termination of AGC. No files will be processed, and no output files will be written or created.
AGC asks each extension to verify the sanity of the options sent to it before AGC begins
processing any file. Note that in some cases the full validity of an option might not be
verifiable until after file processing has begun; the intent of the rule described here is to
catch the common cases, and to avoid making a mess when possible.

In general the order of options is significant. Options may interact (although this is not
recommended). The options that are in force when a file is processed are only those options seen
on the command line before the file's name is reached. Many features of AGC can be activated and
deactivated using appropriate options. Thus, it is possible to turn a feature on before
processing a file and then turn it off again after the file has been processed so that files
subsequently named on the command line are processed differently.

At any given time AGC has a current *option state* containing the set of all active options and
their arguments (if any). The option state has a default value when AGC starts. Each option on
the command-line causes AGC to generate a new option state. When a file name is encountered, the
option state at that point on the command-line is made available to the AGC engine for
processing that file. Since each option on the command-line causes a new option state to be
generated, rather than mutating a single option state, parallel processing of multiple files
with different collections of options in force is made possible. See the later sections of this
document for more information on the option state structure.
