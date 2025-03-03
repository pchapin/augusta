
Augusta Standard Library
========================

This folder contains the Augusta standard library.

We anticipate these packages as being a useful "proving ground" for AGC as the compiler matures.
Clearly, implementing language features required for processing critical library packages should
be given priority.

The packages here are fully specified in the Ada Reference Manual and are further documented in
many books and tutorials about Ada. Thus we do not bother to include documentation in the
package specifications (for now). In theory, Augusta modifies the Ada standard by specifying
only a subset of the Ada standard library and by making other changes and additions. The
low-level packages are, however, essentially identical to those in the Ada standard.

The specifications are stored separately from the bodies so the specifications could, in
principle, be easily packaged, distributed, and studied on their own. The bodies are, at the
time of this writing, mostly place holders that raise a special Augusta.Not_Implemented
exception.

The library test program is in the 'check' folder beneath 'src.' The test program exercises all
library components that are currently implemented.
