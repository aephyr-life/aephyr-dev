{
  description = "Scala 3 project with sbt via nix";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
      in {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            pkgs.jdk17
            pkgs.sbt
            pkgs.just
            pkgs.postgresql_16
          ];
          shellHook = ''
            export PG_PORT=54329
            export PG_DIR="$PWD/.pg"
            export PGDATA=$"PG_DIR/data"
            export PGHOST=localhost
            export PGDATABASE=aephyr

            mkdir -p "$PG_DIR"
            # minimal noise:
            :
          '';
        };
      });
}

