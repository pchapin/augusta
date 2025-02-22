
======
Vision
======

In this chapter we outline the philosophy and long range goals of the Augusta/AGC project. This
articulates the vision of the project and guides the project's development. Note that the
Augusta language and the AGC compiler are separate entities and have separate vision statements.
Other implementations of the Augusta language may have separate goals from AGC's goals.

The vision for the Augusta language is as follows:

- Augusta is a relatively small and simple language *based* on Ada. It borrows syntax and
  semantics from Ada, but without many of the advanced and complex features of Ada. Augusta is
  intended to be easy to learn and easy to implement, yet powerful enough to write real
  applications.

- Augusta is an *approximate subset* of Ada. This means that most valid Augusta programs will
  also compile as Ada programs. However, there are some differences between the two languages
  and Augusta is not intended to be a strict subset of Ada. The rationale for any
  incompatibilities with Ada is documented in the language reference manual.

- Augusta supports several language levels, called "Level 1" (or L1), "Level 2" (or L2), etc.
  Each level builds on the previous level, adding more features and complexity. It is roughly
  true that the sequence L1, L2, ... converges to Ada. However, complete compatibility with Ada
  is not a goal of Augusta at any level. 

- Augusta supports system programming and native machine code generation. It is intended to be a
  language that can be used to write operating systems, device drivers, and other low-level
  software.

The primary implementation of the Augusta language is the AGC compiler (pronounced "agency"). It
is written in Scala, and can be found in the Augusta GitHub repository. Two alternate
implementations are also available in the Augusta GitHub repository: a C++ implementation and a
Rust implementation. These alternate implementations are extremely minimal at this time, but
serve as placeholders for future work.

The vision for the AGC compiler is as follows:

- AGC supports students learning about compiler construction. It is intended to be a "teaching
  compiler" that is easy to understand and modify while also being a useful production-quality
  compiler. We understand these goals are in tension. Resolving that tension is one of the
  challenges of the AGC project.

- AGC is also a "compiler writer's compiler," optionally exposing various compiler internals
  that might be of interest to the power user (or student) in ways that would not interest the
  average developer writing Augusta applications.

- AGC supports several *extension points* that allow the compiler (and the language it
  implements) to be extended in various ways. These extension points are intended to be used by
  the Augusta community to add new features and capabilities to the compiler and language
  independently of the main line of Augusta/AGC development. The details of these extension points
  have yet to be designed, but may include:

    - Compiler plugins.
    - Hygienic macros.
    - User-defined language attributes.
    - User-defined pragmas and aspects.

- AGC is constructed in a modular fashion so that it can be effectively called as a library from
  other applications. It supports developers of Augusta tools by exposing independent
  components, such as the parser and type checker, as well-defined modules with clean,
  documented interfaces, for example via a plugin API (yet to be designed) or similar.

- AGC supports being used from modern IDEs and power editors using whatever means and protocols
  are appropriate to do so.

- AGC supports integration into a modern build tool and package manager (yet to be determined)
  to support the development and deployment of Augusta applications.

- AGC is designed to take advantage of modern hardware by using parallelism and distributed
  computing when appropriate. It takes advantage of modern functional programming techniques for
  greater reliability and maintainability.

Finally, the Augusta/AGC project is intended to be a community-driven project. It is our hope
that it will integrate, to some degree, the Ada, Scala, and LLVM communities. It should, to the
greatest extent possible, follow the best practices of all these communities and attempt to
create a "comfortable common ground" for potential contributors from any of these communities.

