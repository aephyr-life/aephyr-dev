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
        # Shared tooling used across the repo:
        commonTools = [
          pkgs.git 
          pkgs.just 
          pkgs.direnv
        ];
      in {
        devShells = {
          # optional root shell (very small)
          default = pkgs.mkShell {
            packages = commonTools;
            shellHook = ''echo "[aephyr] root shell";'';
          };

          # Server shell: adjust to your stack
          server = pkgs.mkShell {
            packages = commonTools ++ [
              pkgs.jdk21 
              pkgs.sbt
              pkgs.postgresql_16
            ];
            shellHook = ''
              echo "[aephyr] server shell";
              export PG_PORT=54329
              export PG_DIR="$PWD/.pg"
              export PGDATA=$"PG_DIR/data"
              export PGHOST=localhost
              export PGDATABASE=aephyr
              mkdir -p "$PG_DIR"
              export SBT_OPTS="-Xms2G -Xmx2G -XX:+UseG1GC"
            # minimal noise:
            :
            '';
          };

          # Client shell: pick iOS or web
          client = pkgs.mkShell {
            packages = with pkgs; commonTools ++ [
              # For iOS: Xcode isn’t in Nix; keep fastlane/cocoapods if you use them:
              ruby_3_3 bundler
              # fastlane cocoapods # (if you vendor or use nix overlays)
            ];
            shellHook = ''echo "[aephyr] client shell";'';
          };
        };
      });
}

