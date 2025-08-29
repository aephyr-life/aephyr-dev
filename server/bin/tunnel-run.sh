#!/usr/bin/env bash
set -euo pipefail

CFG="${CFG:-infra/cloudflare/config.yml}"
echo "Using config: $CFG"
cloudflared tunnel --config "$CFG" run
