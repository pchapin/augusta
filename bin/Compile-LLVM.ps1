
# This script compiles LLVM assembly language files to object files on Windows.
# It was written with the help of ChatGPT and should be considered a first pass attempt.

param (
    [string]$InputFile  # LLVM assembly file (.ll)
)

# Check if input file is provided
if (-not $InputFile) {
    Write-Host "Usage: .\Compile-LLVM.ps1 <input.ll>"
    exit 1
}

# Check if input file exists
if (-not (Test-Path $InputFile)) {
    Write-Host "Error: File '$InputFile' not found."
    exit 1
}

# Derive output file names
# Note that on Windows the LLVM tools generate COFF formatted object files (*.obj).
$BitcodeFile = [System.IO.Path]::ChangeExtension($InputFile, ".bc")
$OptimizedBitcodeFile = [System.IO.Path]::ChangeExtension($InputFile, ".opt.bc")
$ObjectFile = [System.IO.Path]::ChangeExtension($InputFile, ".obj")

# Run llvm-as to convert .ll to .bc
$llvmAsCmd = "llvm-as `"$InputFile`" -o `"$BitcodeFile`""
Write-Host "Running: $llvmAsCmd"
Invoke-Expression $llvmAsCmd

# Verify bitcode file was created
if (-not (Test-Path $BitcodeFile)) {
    Write-Host "Error: Failed to generate bitcode file."
    exit 1
}

# Run opt to optimize the bitcode
$optCmd = "opt -O2 `"$BitcodeFile`" -o `"$OptimizedBitcodeFile`""
Write-Host "Running: $optCmd"
Invoke-Expression $optCmd

# Verify optimized bitcode file was created
if (-not (Test-Path $OptimizedBitcodeFile)) {
    Write-Host "Error: Failed to optimize bitcode."
    exit 1
}

# Run llc to generate object file
$llcCmd = "llc -filetype=obj `"$OptimizedBitcodeFile`" -o `"$ObjectFile`""
Write-Host "Running: $llcCmd"
Invoke-Expression $llcCmd

# Verify object file was created
if (Test-Path $ObjectFile) {
    Write-Host "Success: Object file created -> $ObjectFile"
} else {
    Write-Host "Error: Failed to generate object file."
    exit 1
}
