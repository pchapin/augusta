# PowerShell script to run the AGC compiler (agc.jar)
# Ensures Java 21 is available before executing

# Get the Java version
$javaVersionOutput = java -version 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Java is not installed or not in the PATH." -ForegroundColor Red
    exit 1
}

# Extract only the first line from the output
$javaVersionLine = $javaVersionOutput[0]

# Extract version number
if ($javaVersionLine -match '"([0-9]+)\.([0-9]+)\.([0-9]+)"') {
    $majorVersion = [int]$matches[1]
} else {
    Write-Host "Error: Unable to determine Java version." -ForegroundColor Red
    exit 1
}

# Check if Java 21 is installed
if ($majorVersion -ne 21) {
    Write-Host "Error: Java 21 is required but Java $majorVersion is installed." -ForegroundColor Red
    exit 1
}

# Determine the absolute path to the JAR file
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$jarPath = Join-Path $scriptDir "..\target\scala-3.3.4\agc-assembly-0.1.0-SNAPSHOT.jar"

# Check if the JAR file exists
if (-Not (Test-Path $jarPath)) {
    Write-Host "Error: AGC JAR file not found at '$jarPath'." -ForegroundColor Red
    Write-Host "Please run 'sbt assembly' to build the JAR file." -ForegroundColor Yellow
    exit 1
}

# Ensure arguments array is not null
if ($null -eq $Args) {
    $Args = @()
}

# Run the AGC compiler with all passed arguments
java -jar $jarPath @Args
