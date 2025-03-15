
LLVM Experiments
================

This folder contains some experiments with LLVM to see how the system can be made to work with
the AGC front end. Here are a few notes:

+ On Windows, the LLVM tools themselves do *not* require the MSVC runtime libraries. They are
  probably statically linked, which explains the large size of their executable files.
  
+ On Windows, the LLVM tools generate COFF formatted object files. This is true for both x64
  Windows and ARM64 windows.
