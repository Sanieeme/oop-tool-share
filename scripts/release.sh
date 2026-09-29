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
