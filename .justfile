set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

root := justfile_directory()
server_dir := root / "server"
client_dir := root / "client"

server *args:
	direnv exec {{server_dir}} just {{args}}

client *args:
	direnv exec {{client_dir}} just {{args}}

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
