{
  description = "Aephyr mono: pinned tools for client & server";

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
          pkgs.direnv
          pkgs.jq
        ];
      in {
        devShells = {
          default = pkgs.mkShell {
            buildInputs = commonTools;
            shellHook = ''echo "[aephyr] root shell (system=${system})"'';
          };

          server = pkgs.mkShell {
            buildInputs = commonTools ++ [
              pkgs.jdk21
              pkgs.sbt-extras
              pkgs.postgresql_16
            ];
            shellHook = ''
              echo "[aephyr] server shell"
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
              
              # thin client alias (since nixpkgs 24.05 has no pkgs.sbtn)
              if command -v sbt >/dev/null; then
                alias sbtn='sbt --client'
              fi
              # minimal noise:
              :
            '';
          };

          client = pkgs.mkShell {
            buildInputs = commonTools ++ [
              pkgs.ruby_3_3
              pkgs.bundler
              pkgs.gradle
              pkgs.jdk17
            ];
            shellHook = ''
              echo "[aephyr] client shell"
              export JAVA_HOME="${pkgs.jdk17}"
              export GRADLE_USER_HOME="$PWD/.gradle"
            '';
          };
        };
      });
}
