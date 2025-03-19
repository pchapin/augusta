#!/bin/zsh

# This script links a single object file assumed to contain a main function.
# It was adapted from a PowerShell script.

# Check if input file is provided
if [[ $# -ne 1 ]]; then
    echo "Usage: ./link-llvm.zsh <input.o>"
    exit 1
fi

OBJECT_FILE="$1"

# Check if input file exists
if [[ ! -f "$OBJECT_FILE" ]]; then
    echo "Error: File '$OBJECT_FILE' not found."
    exit 1
fi

# Derive output file name by changing the extension to a macOS executable (no extension)
OUTPUT_FILE="${OBJECT_FILE:r}"

# Run ld to generate the executable (without the C standard library)
LD_CMD="ld -o \"$OUTPUT_FILE\" \"$OBJECT_FILE\" -e _main -macosx_version_min 11.0 -lSystem -no_pie"

echo "Running: $LD_CMD"
eval $LD_CMD

# Check if executable was created successfully
if [[ -f "$OUTPUT_FILE" ]]; then
    echo "Success: Executable created -> $OUTPUT_FILE"
else
    echo "Error: Failed to generate executable."
    exit 1
fi
