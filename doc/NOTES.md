
Various Notes
=============

This file contains a collection of random thoughts related to the Augusta project. The
information here might one day be moved into the main documentation set or, perhaps more likely,
just deleted eventually.

+ Randy Brukardt on comp.lang.ada (2014-03-19) points out that resolution, visibility, and
  interfaces are topics that tend to be especially difficult for Ada compilers. This leads us to
  wonder if it would be possible to experiment with the algorithms and data structures needed to
  solve those problems in isolation from the rest of the compiler somehow (meaning before the
  rest of the compiler is "ready"). If such an approach were possible it might promote a nice
  modular structure for the compiler or at least alert us to significant requirements before the
  code is too far implemented.

+ Brian Drummond on comp.lang.ada (2014-03-19) warns that there might be difficulties with
  nested scopes on LLVM. His understanding is that this has been an issue for the Dragonlace
  project. However, a technique using closures might be a solution.

+ J-P. Rosen on comp.lang.ada (2014-03-20) calls out specific areas of the Ada standard that
  cause difficulties for Ada compiler writers: "make sure you are able to understand the
  implications of 4.3.3 (a nightmare for code generation), or 13.14, or 3.10.2(3/2)."

+ In C it is necessary to collect information on typedef names during parsing to resolve
  syntactic ambiguities. The grammar in the Ada Reference Manual is also ambiguous and, for
  example, provides several interpretations for constructs such as A(B). It was pointed out on
  comp.lang.ada (2014-06-19) that this can be handled by creating a non-ambiguous grammar,
  treating things like A(B) as a kind of general application, and then resolving the precise
  meaning of the construct during semantic analysis.

  The advantages to doing this are that it separates parsing and semantic analysis "cleanly" and
  it allows units to be parsed without regard to any other units. The alternative is to do name
  resolution and visibility handling (basically symbol table management) while parsing...
  causing the compiler to consider withed units, etc., while it parses.

  On the other hand... doing the symbol table management in parallel with the parse would allow
  a more specific and accurate parse tree to be built up front. I wonder if that would have any
  advantage later on. Also, I wonder if the parser and symbol table manager could run physically
  in parallel, thus taking advantage of multicore systems, and still maintain good modularity by
  way of a clearly defined message passing interface.

  Finally, the need to process multiple files to parse a single file might be seen as an
  advantage. Again a multicore system could run multiple parsers in parallel as the compiler
  digests package specifications, etc., needed to parse a given unit. The resulting parse trees
  (of the package specifications) could potentially be cached somehow to speed up the processing
  of other units.

  On comp.lang.ada it was pointed out that "all" Ada compilers use the first approach and that
  doing so saves memory and reduces coupling. However, those comments may harken to a day when
  memories were small and multiprocessor systems were uncommon. I'm inclined to try the parallel
  parse/symbol management approach if for no other reason than it's something different. I
  wonder how well it would actually work. Augusta is, after all, a kind of research project.

  I do see a problem with forward references. Ada 2012 contracts, for example, can make use of
  names that have not yet been declared so "accurate" parsing of those expressions would seem to
  be very difficult. One approach would be to use a technique common in C++ compilers where
  parsing the expressions is deferred until complete symbol information is known. This is,
  however, an unfortunate complication. The good news is that there are very few places in Ada
  where forward references are allowed.
