#!/usr/bin/env bash
# bin/on_fail.sh
# Capture build/test output, store last run log, and extract failures.

set -eo pipefail

mkdir -p build

# Save full output
tee build/last-run.log >/dev/null
status=${PIPESTATUS[0]}

if [ $status -ne 0 ]; then
  # Save a shorter tail for aider/test debugging
  tail -n 300 build/last-run.log > build/fail.log

  # Optional: desktop notification (macOS)
  if command -v terminal-notifier >/dev/null; then
    terminal-notifier -title "Tests failed" -message "See build/fail.log"
  fi
fi

exit $status
