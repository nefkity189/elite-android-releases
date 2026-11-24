#!/usr/bin/env bash
# Screenshot of the running Android emulator/phone -> PNG. Usage: android_shot.sh out.png
set -euo pipefail
OUT="${1:-app.png}"
adb get-state >/dev/null 2>&1 || { echo "no adb device/emulator running. Start one: emulator -avd <name>  (or plug in the phone with USB debugging)"; exit 1; }
adb exec-out screencap -p > "$OUT"
SIZE=$(adb shell wm size | tail -1); DENS=$(adb shell wm density | tail -1)
echo "saved $OUT  ($SIZE, $DENS)  -> dp = px / (density/160)"
