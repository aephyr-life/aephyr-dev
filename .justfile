# Requires: watchexec (brew install watchexec), xcbeautify (brew install xcbeautify)

set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

# ---- settings ----
root := justfile_directory()
server_dir := root / "server"
client_dir := root / "client"

KOTLIN_DIR := client_dir / "shared"
# SCALA_ID :=
XCODE_PROJECT := client_dir / "ios/Aephyr.xcodeproj"
XCODE_SCHEME := "Aephyr"
IOS_DEST := "platform=iOS Simulator,name=iPhone 17 Pro"
# ----------------------------

server *args:
	direnv exec {{server_dir}} just {{args}}

client *args:
	direnv exec {{client_dir}} just {{args}}

test-kotlin:
  ./bin/on_fail.sh client/shared/gradlew -p client/shared clean assemble check --stacktrace

# build-swift:
#   bash -lc 'set -o pipefail; xcodebuild \
#     -project "{{XCODE_PROJECT}}" \
#     -scheme "{{XCODE_SCHEME}}" \
#     -configuration Debug \
#     -sdk iphonesimulator \
#     -destination "{{IOS_DEST}}" \
#     build | xcbeautify |& ./bin/on_fail.sh'

# test-swift:
#   bash -lc 'set -o pipefail; xcodebuild \
#     -project "{{XCODE_PROJECT}}" \
#     -scheme "{{XCODE_SCHEME}}" \
#     -destination "{{IOS_DEST}}" \
#     test | xcbeautify |& ./bin/on_fail.sh'

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
