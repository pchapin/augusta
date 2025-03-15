param (
    [string]$ObjectFile  # Object file (.obj)
)

# Check if input file is provided
if (-not $ObjectFile) {
    Write-Host "Usage: .\Disassemble-LLVM.ps1 <input.obj>"
    exit 1
}

# Check if input file exists
if (-not (Test-Path $ObjectFile)) {
    Write-Host "Error: File '$ObjectFile' not found."
    exit 1
}

# Run llvm-objdump to disassemble the object file
$llvmCmd = "llvm-objdump -d `"$ObjectFile`""

Write-Host "Running: $llvmCmd"
Invoke-Expression $llvmCmd
