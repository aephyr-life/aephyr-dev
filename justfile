set shell := ["bash", "-euo", "pipefail", "-c"]

default:
  @just --list

# builds the program
build:
  sbt compile

# cleans the build
clean:
  sbt clean

# runs the tests
test:
  sbt test

# starts the api server
api-run:
  sbt api-server/run

# opens a repl
repl:
  sbt console

# migrates the db
migrate:
  FLYWAY_URL=${FLYWAY_URL:-jdbc:postgresql://localhost:54329/aephyr} \
  FLYWAY_USER=${FLYWAY_USER:-aephyr_migrator} \
  FLYWAY_PASSWORD=${FLYWAY_PASSWORD:-} \
  sbt db-migrations/run

# formats the code
fmt:
  sbt scalafmtAll

# updates flake.lock
update-lock:
  nix flake update

# devShell neu laden
reload:
  direnv reload

# generates a 32-Byte Base64URL-Key without padding
gen-key:
  openssl rand -base64 32 | tr '+/' '-_' | tr -d '='

# checks dependencies
dependencies:
  sbt ";undeclaredCompileDependencies;unusedCompileDependencies"

# initializes the db
db-init:
    mkdir -p .pg
    initdb -D .pg/data -U postgres
    # relax local dev auth (trust for local connections)
    awk '{print} END {print "host all all 127.0.0.1/32 trust"; print "host all all ::1/128 trust"}' .pg/data/pg_hba.conf > .pg/data/pg_hba.conf.new
    mv .pg/data/pg_hba.conf.new .pg/data/pg_hba.conf
    pg_ctl -D .pg/data -l .pg/postgres.log -o "-p 54329 -c listen_addresses=localhost" start
    sleep 1
    createdb -h localhost -p 54329 -U postgres aephyr || true
    pg_ctl -D .pg/data stop -m fast

# start the db
db-start:
  pg_ctl -D .pg/data -l .pg/postgres.log -o "-p 54329 -c listen_addresses=localhost" start
  sleep 1
  echo "Postgres on postgres://localhost:54329/aephyr (log: .pg/postgres.log)"

# stop the db
db-stop:
    pg_ctl -D .pg/data stop -m fast

# view db logs
db-logs:
    tail -f .pg/postgres.log

# bootstrap roles for hardening (run once after start)
db-bootstrap:
  @echo "psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c \"CREATE ROLE $FLYWAY_USER LOGIN PASSWORD '*****';\""
  @psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "CREATE ROLE $FLYWAY_USER LOGIN PASSWORD '$$FLYWAY_PASSWORD';" || true
  @echo "psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c \"CREATE ROLE $DB_USER LOGIN PASSWORD '*****';\""
  @psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "CREATE ROLE $DB_USER LOGIN PASSWORD '$$DB_PASSWORD';" || true
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "GRANT CREATE, USAGE ON SCHEMA public TO aephyr_migrator;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "CREATE SCHEMA events;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "CREATE SCHEMA read;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "CREATE SCHEMA tech;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "ALTER SCHEMA events OWNER TO aephyr_migrator;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "ALTER SCHEMA read OWNER TO aephyr_migrator;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "ALTER SCHEMA tech OWNER TO aephyr_migrator;"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "ALTER ROLE aephyr_app SET search_path = '';"
  psql -h localhost -p 54329 -U postgres -d aephyr -v ON_ERROR_STOP=1 -c "REVOKE USAGE ON SCHEMA public FROM aephyr_app;"
# psql console
psql:
  psql -h localhost -p 54329 -U postgres -d aephyr

# db seed
db-seed:
  psql -h localhost -p 54329 -U aephyr_migrator -d aephyr -v ON_ERROR_STOP=1 -f modules/db-migrations/src/main/resources/db/seed/dev_users.sql
