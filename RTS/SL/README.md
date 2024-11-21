
Augusta Standard Library
========================

This folder contains the Augusta standard library. Much of this library can be developed in
parallel with the Augusta compiler. The code here can, in principle, be compiled and tested with
any existing Ada compiler even before Augusta is mature enough to handle this code itself. We
use GNAT as our library development compiler for now.

We anticipate these packages as being a useful "proving ground" for Augusta as the compiler
matures. Clearly, implementing language features required for processing critical library
packages should be given priority.

The packages here are fully specified in the Ada Reference Manual and are further documented in
many books and tutorials about Ada. Thus we do not bother to include documentation in the
package specifications (for now). The declarations in each package are ordered to follow the
sequence of declarations presented in the Ada standard. This is intended to facilitate manually
reviewing the packages for conformity to the standard.

The specifications are stored separately from the bodies so the specifications could, in
principle, be easily packaged, distributed, and studied on their own. The bodies are, at the
time of this writing, mostly place holders that raise a special Augusta.Not_Implemented
exception. As such all the bodies do compile as Ada (using GNAT) as they are currently defined.

The library test program is in the 'check' folder beneath 'src.' The test program exercises all
library components that are currently implemented.

The library is currently a child of package 'Augusta' instead of package 'Ada.' This avoids
conflicts between these components and the standard library of our development compiler. It also
emphasizes the formative nature of this library. As August approaches maturity the top level
package will eventually be renamed to 'Ada' as required by the standard.
