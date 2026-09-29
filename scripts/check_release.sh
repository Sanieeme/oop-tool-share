#!/usr/bin/env bash
# Self-check for the argument handling in release.sh (Q1.3, requirements 2 and 3).
# It does NOT run a real release. Usage: ./scripts/check_release.sh
set -u
cd "$(dirname "$0")/.."
fail=0

expect_status() {
  local expected="$1"; shift
  local label="$1"; shift
  local err status
  err=$("$@" 2>&1 >/dev/null); status=$?
  if [ "$status" -eq "$expected" ] && [ -n "$err" ]; then
    echo "PASS: $label (exit $status, message on stderr)"
  else
    echo "FAIL: $label (expected exit $expected with a stderr message, got exit $status)"
    fail=1
  fi
}

expect_status 2 "no argument"         ./scripts/release.sh
expect_status 2 "too many arguments"  ./scripts/release.sh 1.0.0 extra
expect_status 1 "two-part version"    ./scripts/release.sh 1.2
expect_status 1 "leading v"           ./scripts/release.sh v1.2.3
expect_status 1 "letters in version"  ./scripts/release.sh 1.2.x
expect_status 1 "four-part version"   ./scripts/release.sh 1.2.3.4

exit $fail
