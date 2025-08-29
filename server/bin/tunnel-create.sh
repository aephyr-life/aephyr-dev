#!/usr/bin/env bash
set -euo pipefail

TUNNEL_NAME="${TUNNEL_NAME:-aephyr-dev}"
HOSTNAME="${HOSTNAME:-dev.aephyr.life}"

echo "Logging in to Cloudflare… (a browser window may open)"
cloudflared tunnel login

echo "Creating tunnel: $TUNNEL_NAME"
cloudflared tunnel create "$TUNNEL_NAME"

echo "Routing DNS $HOSTNAME to tunnel"
cloudflared tunnel route dns "$TUNNEL_NAME" "$HOSTNAME"

echo "Write config to infra/cloudflare/config.yml if needed (already in repo)."
echo "Now any dev can run: bin/tunnel-run.sh"
