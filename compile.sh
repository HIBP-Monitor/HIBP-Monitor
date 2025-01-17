#!/usr/bin/env bash
# compile.sh
# This script compiles the maven project
# ./compile.sh

# Stops the script if something went wrong
set -e

echo "Compiling project..."
# Executes the maven-command to compile
mvn clean package

echo "Compilation finished. Fat JAR created in target/"
