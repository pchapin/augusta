.. AGC User Guide master file, created by sphinx-quickstart

AGC User Guide
==============

.. Add your content using ``reStructuredText`` syntax. See the `reStructuredText
.. <https://www.sphinx-doc.org/en/master/usage/restructuredtext/index.html>`_ documentation for
.. details.

This document contains the user documentation for AGC. It is divided into chapters that target
different kinds of users. Chapter 1 is for traditional users who wish to use AGC
non-interactively, for example at the command line or by way of a build script, to compile
programs. Chapter 2 is for tool builders who wish to use AGC, or parts of it, as a library in
programs of their own construction. Chapter 3 is for users who wish to extend AGC in various
ways.

The chapters are ordered in the sense that they assume a reader of chapter n is familiar with
the contents of chapter n-1. In contrast, a reader of chapter n need not be concerned about the
contents of chapter n+1.

.. toctree::
   :maxdepth: 2
   :caption: Contents:

   vision
   chapter1_traditional
   chapter2_tool_building
   chapter3_extensions
