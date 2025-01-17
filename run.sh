#!/usr/bin/env bash
# run.sh
# This script executes the jar.

# Finish script if command fails
set -e

JAR_FILE="target/monitoring-demon-1.0-shaded.jar"

# Check if exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: $JAR_FILE not found. Execute ./compile.sh first."
    exit 1
fi

echo "Execute jar..."
# Executes fat jar
java -jar "$JAR_FILE"
