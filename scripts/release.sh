#!/usr/bin/env bash
#
# Usage: ./scripts/release.sh <version>        e.g. ./scripts/release.sh 1.4.0
#
# TODO (Q1.3): finish this script. Requirements:
#
#   1. Stop on the first failing command, on unset variables, and on a
#      failure anywhere inside a pipeline.
#   2. Exactly one argument is required. If it is missing (or there are too
#      many), print a usage message to STANDARD ERROR and exit with status 2.
#   3. The argument must be a three-part version (MAJOR.MINOR.PATCH, digits
#      only, e.g. 1.4.0). If it is not, print an error to STANDARD ERROR and
#      exit with status 1. (Use a regular expression, not string slicing.)
#   4. Refuse to release from a dirty working tree: if `git status --porcelain`
#      prints anything, print an error to standard error and exit with status 1.
#   5. Run the test suite (`make test`) and then package the application
#      (`make package`). A test failure must abort the release BEFORE packaging.
#   6. Create the directory dist/ if it does not exist, and copy
#      target/toolshare.jar to dist/toolshare-<version>.jar
#   7. Write a SHA-256 checksum of the copied jar to
#      dist/toolshare-<version>.jar.sha256
#   8. Print the path of the released jar as the LAST line of output.
#

# Self-check for the argument handling in release.sh (Q1.3, requirements 2 and 3).
# It does NOT run a real release. Usage: ./scripts/check_release.sh
# Check that exactly one argument was provided
if [ "$#" -ne 1 ]; then
    echo "Usage: ./scripts/release.sh <version>" >&2
    exit 2
fi

version="$1"

# Check that version is MAJOR.MINOR.PATCH
if [[ ! "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: version must be MAJOR.MINOR.PATCH" >&2
    exit 1
fi

# Check that the working tree is clean
if [ -n "$(git status --porcelain)" ]; then
    echo "Error: working tree is not clean" >&2
    exit 1
fi

# Run tests
make test

# Package the application
make package

# Create dist directory
mkdir -p dist

# Copy the JAR
cp target/toolshare.jar "dist/toolshare-$version.jar"

# Create SHA-256 checksum
sha256sum "dist/toolshare-$version.jar" > "dist/toolshare-$version.jar.sha256"

# Print the released JAR path as the last line
echo "dist/toolshare-$version.jar"


