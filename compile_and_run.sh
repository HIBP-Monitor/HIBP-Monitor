#!/usr/bin/env bash
# compile_and_run.sh
# This script first performs the compilation and then starts the program.

# Exit the script if a command fails
set -e

# Compile
./compile.sh

# Execute
./run.sh