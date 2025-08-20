{
  description = "Aephyr mono: pinned tools for client & server";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
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
              pkgs.sbt
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
              export SBT_OPTS="''${SBT_OPTS:--Xms1G -Xmx2G -XX:+UseG1GC}"
              
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
