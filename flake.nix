{
  description = "Aephyr mono: CI shells for client & server (no local nix expected)";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [
            (import ./overlays/sbt-memory.nix)
          ];
        };

        commonTools = [
          pkgs.git
          pkgs.just
          pkgs.ripgrep
          pkgs.gnused
          pkgs.coreutils
          pkgs.jq
        ];
      in {
        devShells = {
          # Minimal root shell (kept for CI or occasional scripted use)
          default = pkgs.mkShell {
            packages = commonTools;
            shellHook = ''echo "[aephyr] root shell (system=${system})"'';
          };

          # Server CI shell (kept in case you run server tasks in Actions)
          server = pkgs.mkShell {
            packages = commonTools ++ [
              pkgs.jdk21
              pkgs.sbt-extras
              pkgs.postgresql_16
            ];
            shellHook = ''
              echo "[aephyr] server shell (CI)"
              export PGDIR="$PWD/.dev/pg"
              export PGDATA="$PGDIR/data"
              export PGHOST=localhost
              export PGPORT=54329
              export PGDATABASE=aephyr
              mkdir -p "$PGDIR"

              # SBT/JVM
              export JAVA_HOME="${pkgs.jdk21}"
              if [ -z "$XDG_CACHE_HOME" ]; then XDG_CACHE_HOME="$HOME/.cache"; fi
              export XDG_CACHE_HOME
              export COURSIER_CACHE="$XDG_CACHE_HOME/coursier"

              # thin client alias (nixpkgs 24.05 has no pkgs.sbtn)
              if command -v sbt >/dev/null; then
                alias sbtn='sbt --client'
              fi
            '';
          };

          # NEW: Shared (KMM) CI shell — used by GitHub Actions via bin/with-env
          shared = pkgs.mkShell {
            packages = commonTools ++ [
              pkgs.jdk17
              # Gradle wrapper is preferred; no need to add pkgs.gradle
            ];

            # Keep caches in-repo so Actions can cache them easily
            GRADLE_USER_HOME = "$PWD/.gradle";
            KONAN_DATA_DIR   = "$PWD/.konan";

            # Reasonable Gradle defaults for CI
            GRADLE_OPTS = "-Dorg.gradle.jvmargs=-Xmx3g -Dfile.encoding=UTF-8";

            shellHook = ''echo "[aephyr] shared KMM shell (CI) — JDK 17"'';
          };
        };
      });
}
