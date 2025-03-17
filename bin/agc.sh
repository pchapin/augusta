#!/bin/bash
# Bash script to run the AGC compiler (agc.jar)
# Ensures Java 21 is available before executing

# Get the Java version
javaVersionOutput=$(java -version 2>&1)
if [ $? -ne 0 ]; then
    echo "Error: Java is not installed or not in the PATH." >&2
    exit 1
fi

# Extract only the first line from the output
javaVersionLine=$(echo "$javaVersionOutput" | head -n 1)

# Extract version number
if [[ $javaVersionLine =~ "([0-9]+)\.([0-9]+)\.([0-9]+)" ]]; then
    majorVersion=${BASH_REMATCH[1]}
else
    echo "Error: Unable to determine Java version." >&2
    exit 1
fi

# Check if Java 21 is installed
if [ "$majorVersion" -ne 21 ]; then
    echo "Error: Java 21 is required but Java $majorVersion is installed." >&2
    exit 1
fi

# Determine the absolute path to the JAR file
scriptDir=$(dirname "$(realpath "$0")")
jarPath="$scriptDir/../target/scala-3.3.4/agc-assembly-0.1.0-SNAPSHOT.jar"

# Check if the JAR file exists
if [ ! -f "$jarPath" ]; then
    echo "Error: AGC JAR file not found at '$jarPath'." >&2
    echo "Please run 'sbt assembly' to build the JAR file." >&2
    exit 1
fi

# Run the AGC compiler with all passed arguments
java -jar "$jarPath" "$@"
