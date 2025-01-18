============
Introduction
============

The Augusta language is intended to be a simple, easy to learn and easy to implement language
based on Ada. It is defined in several *levels* of increasing complexity and capability. At this
time, only Augusta Level 1 (also known as "Augusta L1," or just "L1") is specified here.

A future version of this document will include the definitions of higher Augusta levels.
However, we expect that all Augusta levels will have independent and well-defined specifications
and will be supported by implementations indefinitely. In other words, L1 will continue to exist
and be usable even after L2, L3, etc. are defined and implemented.

The long-term goal is to create a sequence of language that can satisfy various needs. August L1
will be sufficient for simple applications, while higher levels will be suitable for more complex
applications.

Augusta is based on Ada and, as such, the specification of the Augusta language can be done in
terms of the `Ada Reference Manual <https://arg.adaic.org/ada-reference-manual>`_ (ARM). This
specification doesn't so much describe what Augusta is, but rather describes what Augusta is
not. This specification often refers to the ARM, but then adds information about the aspects of
Ada that are *not* part of Augusta. The specific version of Ada we use as a base is `Ada 2022
<https://ada-rapporteur-group.github.io/ARM/Ada_2022/RM-TOC.html>`_.

Augusta is not intended, at this time, to be a strict subset of Ada. This means that Augusta
programs are not necessarily valid Ada programs. We use Ada as a source of inspiration, not as a
constraint. Augusta shares much syntax and many semantic behaviors with Ada, but we may deviate
from strict Ada compatibility in order to simplify the language. Although Ada has remarkable
coherence despite its age, there are aspects of Ada that are needless awkward in retrospect. We
don't intend to saddle Augusta with Ada's mistakes.

However, we do feel the need to justify any incompatibilities with Ada that we introduce. This
language specification contains embedded rationale statements that explain those
incompatibilities along with other language design decisions. Augusta is a work in progress.
Since it is, in effect, a new language with a tiny user community, we can even consider making
breaking changes to the language in these early days. We welcome feedback and suggestions.

