#!/usr/bin/env bash
# bin/on_fail.sh

# NOTE: no `-e` here; we handle the non-zero ourselves.
set -Euo pipefail

mkdir -p build

if [[ $# -eq 0 ]]; then
  echo "Usage: $0 <command> [args...]" >&2
  exit 2
fi

# Run the command and always capture output, even on failure.
set +e
"$@" |& tee build/last-run.log
cmd_status=${PIPESTATUS[0]}
set -e

# Marker so we know this script ran
echo "on_fail.sh: command exit status = ${cmd_status}" >> build/last-run.log

if (( cmd_status != 0 )); then
  # Tail last 300 lines into fail.log
  if [[ -s build/last-run.log ]]; then
    tail -n 300 build/last-run.log > build/fail.log
  else
    echo "[on_fail.sh] WARNING: last-run.log empty" > build/fail.log
  fi

  if command -v terminal-notifier >/dev/null 2>&1; then
    terminal-notifier -title "Gradle failed" -message "See build/fail.log"
  fi
fi

exit "${cmd_status}"
