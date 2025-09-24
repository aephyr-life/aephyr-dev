# ==============================================================================
# Root Justfile for Monorepo
#
# Delegates to project justfiles and provides developer utilities.
# ==============================================================================

set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

[private]
default:
  @just --list --unsorted

# ------------------------------------------------------------------------------
# Path Variables
# ------------------------------------------------------------------------------

root        := justfile_directory()
shared_dir  := root / "client/shared"
ios_dir     := root / "client/ios"
server_dir  := root / "server"
WITH_ENV    := root / "bin/with-env"


# ------------------------------------------------------------------------------
# Developer Quality of Life (QoL) Utilities
# ------------------------------------------------------------------------------

# Create a new branch interactively
[group('dev')]
branch:
  @echo "Select branch type:"
  @PS3="> "; select prefix in feature fix chore test refactor docs; do \
    if [[ -n "$prefix" ]]; then \
      read -r -p "Enter branch name (kebab-case): " name; \
      git switch -c "$prefix/$name"; \
      git push -f -u origin "$prefix/$name"; \
      break; \
    else \
      echo "Invalid choice"; \
    fi \
  done

# Create a (draft) pull-request
[group('dev')]
pr draft="release":
  #!/usr/bin/env bash
  set -euo pipefail
  case "{{draft}}" in
  draft)
    gh pr create --fill --base main --draft
    ;;
  *)
    gh pr create --fill --base main
    ;;
  esac

# ------------------------------------------------------------------------------
# Aider Integration
# ------------------------------------------------------------------------------

# Run aider in the context of a project, with auto-test and file watching.
[group('dev')]
aider project *args:
  #!/usr/bin/env bash
  set -euo pipefail

  case "{{project}}" in
    root)
      exec aider --watch-files --notifications {{args}}
      ;;
    server|ios|shared) 
      TEST_CMD="bash -lc 'cd {{root}} && just {{project}} test'"
      exec aider --test-cmd "$TEST_CMD" --auto-test --watch-files --notifications {{args}}
      ;;
    *) 
      echo "Usage: just aider (server|ios|shared) [AIDER_ARGS…]" >&2; exit 2
      ;;
  esac

# ------------------------------------------------------------------------------
# Delegators to Child Justfiles
# ------------------------------------------------------------------------------

# Delegate to the shared project
[group('delegated')]
shared *args:
  {{WITH_ENV}} shared -- just --justfile "{{shared_dir}}/justfile" {{args}}

# Delegate to the ios project
[group('delegated')]
ios *args:
  {{WITH_ENV}} ios -- just --justfile "{{ios_dir}}/justfile" {{args}}

# Delegate to the server project
[group('delegated')]
server *args:
  {{WITH_ENV}} server -- just --justfile "{{server_dir}}/justfile" {{args}}
