set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

root        := justfile_directory()
shared_dir  := root / "client/shared"
ios_dir     := root / "client/ios"
server_dir  := root / "server"
WITH_ENV    := root / "bin/with-env"

# --- Delegators to child justfiles ---

shared *ARGS:
  {{WITH_ENV}} shared -- just --justfile "{{shared_dir}}/justfile" {{ARGS}}

ios *ARGS:
  {{WITH_ENV}} ios -- just --justfile "{{ios_dir}}/justfile" {{ARGS}}

server *ARGS:
  {{WITH_ENV}} server -- just --justfile "{{server_dir}}/justfile" {{ARGS}}

# --- Friendly aliases (match child recipe names) ---

# kmm-test:
#     bin/with-env shared -- just --justfile "{{shared_dir}}/justfile" test


# ---- Developer QoL ----

# creates a new branch
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

# creates a draft pull request
[group('dev')]
draft-pr:
  @gh pr create --fill --base main --draft
