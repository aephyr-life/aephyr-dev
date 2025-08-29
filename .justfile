# .justfile (hidden, at repo root)
set shell := ["bash", "-eu", "-o", "pipefail", "-c"]
root := justfile_directory()
server_dir := root / "server"
client_dir := root / "client"

server *args:
	direnv exec {{server_dir}} just {{args}}

client *args:
	direnv exec {{client_dir}} just {{args}}
