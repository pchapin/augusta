
# This script links a single object file assumed to contain a main function.
# It was written with the help of ChatGPT and should be considered a first pass attempt.

param (
    [string]$ObjectFile  # Object file (.obj)
)

# Check if input file is provided
if (-not $ObjectFile) {
    Write-Host "Usage: .\Link-LLVM.ps1 <input.obj>"
    exit 1
}

# Check if input file exists
if (-not (Test-Path $ObjectFile)) {
    Write-Host "Error: File '$ObjectFile' not found."
    exit 1
}

# Derive output file name by changing the extension to .exe
$OutputFile = [System.IO.Path]::ChangeExtension($ObjectFile, ".exe")

# Run lld-link to generate the executable
$lldCmd = "lld-link /subsystem:console /entry:main `"$ObjectFile`" /out:`"$OutputFile`" kernel32.lib"

Write-Host "Running: $lldCmd"
Invoke-Expression $lldCmd

# Check if executable was created successfully
if (Test-Path $OutputFile) {
    Write-Host "Success: Executable created -> $OutputFile"
} else {
    Write-Host "Error: Failed to generate executable."
    exit 1
}
