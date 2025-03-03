
Run Time System
===============

This folder contains the AGC runtime system. It is divided into two parts: the runtime support
library in the RL folder, and the Augusta standard library in the SL folder.

The runtime support library contains components that are needed to faithfully implement Augusta
semantics. This will ultimately include exception handling support, dynamic memory support,
constraint checking, and other things. The Augusta standard library contains a subset of the
components described in Annex A ("Predefined Language Environment") of the Ada standard, but
with Augusta-specific adjustments and additions. See the Augusta Language Reference Manual for
more information.
