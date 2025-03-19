#!/bin/zsh

# This script compiles LLVM assembly language files to object files on macOS.
# It was adapted from a PowerShell script written with the help of ChatGPT.

# Check if input file is provided
if [[ $# -ne 1 ]]; then
    echo "Usage: ./compile-llvm.zsh <input.ll>"
    exit 1
fi

INPUT_FILE="$1"

# Check if input file exists
if [[ ! -f "$INPUT_FILE" ]]; then
    echo "Error: File '$INPUT_FILE' not found."
    exit 1
fi

# Derive output file names
# Note: On macOS, the LLVM tools generate Mach-O formatted object files (*.o).
BITCODE_FILE="${INPUT_FILE:r}.bc"
OPTIMIZED_BITCODE_FILE="${INPUT_FILE:r}.opt.bc"
OBJECT_FILE="${INPUT_FILE:r}.o"

# Run llvm-as to convert .ll to .bc
LLVM_AS_CMD="llvm-as \"$INPUT_FILE\" -o \"$BITCODE_FILE\""
echo "Running: $LLVM_AS_CMD"
eval $LLVM_AS_CMD

# Verify bitcode file was created
if [[ ! -f "$BITCODE_FILE" ]]; then
    echo "Error: Failed to generate bitcode file."
    exit 1
fi

# Run opt to optimize the bitcode
OPT_CMD="opt -O2 \"$BITCODE_FILE\" -o \"$OPTIMIZED_BITCODE_FILE\""
echo "Running: $OPT_CMD"
eval $OPT_CMD

# Verify optimized bitcode file was created
if [[ ! -f "$OPTIMIZED_BITCODE_FILE" ]]; then
    echo "Error: Failed to optimize bitcode."
    exit 1
fi

# Run llc to generate object file
LLC_CMD="llc -filetype=obj \"$OPTIMIZED_BITCODE_FILE\" -o \"$OBJECT_FILE\""
echo "Running: $LLC_CMD"
eval $LLC_CMD

# Verify object file was created
if [[ -f "$OBJECT_FILE" ]]; then
    echo "Success: Object file created -> $OBJECT_FILE"
else
    echo "Error: Failed to generate object file."
    exit 1
fi
