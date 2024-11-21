
Run Time System
===============

This folder contains the Augusta run time system. It is divided into two parts: the runtime
support library in the RL folder, and the standard library in the SL folder. The support library
contains components that are needed to faithfully implement Ada semantics. This will ultimately
include exception handling support, dynamic memory support, constraint checking, and other
things. The standard library contains the components described in Annex A ("Predefined Language
Environment") of the Ada standard.

It is hoped that August will eventually be able to compile its own runtime system. This can be
accomplished by implementing only the parts of the runtime system that are in the Ada subset
supported by Augusta. However, since it may take some time before Augusta is able to compile
even a very minimal subset of Ada, there are GNAT project files for the runtime system
components, allowing development on the runtime system to proceed using GNAT for the time being.
