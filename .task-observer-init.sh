#!/usr/bin/env bash
d="/c/Users/rodri/.claude/skill-observations"
[ -e "$d/log.md" ] && [ ! -d "$d/observation-log" ] && { echo "LEGACY log.md — run migration before creating anything"; exit 1; }
[ -e "$d/log.md" ] && [ -d "$d/observation-log" ] && { echo "SPLIT STORE — migrate the remainder before creating anything"; exit 1; }
c=
for x in observation-log observation-log/archive last-review-date.txt cross-cutting-principles.md; do
  [ -e "$d/$x" ] || c="$c${c:+,}$x"
done
mkdir -p "$d/observation-log/archive" || exit 1
[ -e "$d/last-review-date.txt" ] || printf 'never\n' > "$d/last-review-date.txt"
[ -e "$d/cross-cutting-principles.md" ] || printf '# Cross-Cutting Principles\n\nPrinciples that apply to all skills. Read as a mandatory checklist during\nany skill creation or regeneration.\n\n---\n\n## Active Principles\n' > "$d/cross-cutting-principles.md"
for x in observation-log observation-log/archive last-review-date.txt cross-cutting-principles.md; do
  [ -e "$d/$x" ] || { echo "WORKSPACE SETUP INCOMPLETE: $x"; exit 1; }
done
printf '%s [%s] workspace: created=%s\n' "$(date '+%F %H:%M')" "${PWD##*/}" "${c:-none}" >> "$d/checkpoints.log"
printf 'WORKSPACE OK created=%s\n' "${c:-none}"
