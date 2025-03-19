#!/bin/zsh

# This script disassembles object files using llvm-objdump on macOS.
# It was adapted from a PowerShell script.

# Check if input file is provided
if [[ $# -ne 1 ]]; then
    echo "Usage: ./disassemble-llvm.zsh <input.o>"
    exit 1
fi

OBJECT_FILE="$1"

# Check if input file exists
if [[ ! -f "$OBJECT_FILE" ]]; then
    echo "Error: File '$OBJECT_FILE' not found."
    exit 1
fi

# Run llvm-objdump to disassemble the object file
LLVM_CMD="llvm-objdump -d \"$OBJECT_FILE\""
echo "Running: $LLVM_CMD"
eval $LLVM_CMD
