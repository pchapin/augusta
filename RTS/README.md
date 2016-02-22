
Run Time System
===============

This folder contains the Augusta run time system. It is divided into two parts: the run time
support library in the RL folder, and the standard library in the SL folder. The support library
contains components that are needed to faithfully implement Ada semantics. This includes
exception handling support, dynamic memory support, constraint checking, and so forth. The
standard library contains the components described in Annex A ("Predefined Language
Environment") of the Ada standard.

Since Augusta is not yet capable of compiling its run time system the code here has been
developed using the GNAT compiler with GPS as the development environment. The AUnit testing
framework is used to manage the unit tests.
