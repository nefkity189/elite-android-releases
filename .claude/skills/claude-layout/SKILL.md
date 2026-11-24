---
name: claude-layout
description: Standard procedure for Claude (chat) to design a new mobile app screen or layout as an HTML prototype AND deliver it so a coding agent (Claude Code) can replicate it exactly in the user's native app (Compose, Flutter, React Native, SwiftUI). Use whenever the user asks for a new layout, screen, redesign, "tela estilo Runna", menu, animation, or says "crie o prompt", "para o Claude Code replicar", "faça igual ao protótipo" — any time a prototype will later be rebuilt by another agent. Produces the HTML, a self-checked render, a measured spec, a SPEC.md and a ready-to-paste prompt in a HANDOFF folder.
---

# Layout handoff designer (for Claude in chat)

The user designs with you and builds with Claude Code in a terminal. Claude Code has the `replica-claude` skill (render HTML → PNG, measure in dp, diff app vs reference), but it can only be as exact as what you hand it. Past failure: a fast, unchecked prototype with CSS-only features, a text layer hidden under an SVG, no font instructions and a prompt with `[SUA STACK]` left blank — the replica came out different. Speed is not the goal; **a prototype that is correct, checked and replicable on the first pass is.**

## Workflow
1. **Pin the facts (ask only what you can't infer).** Stack (Compose/Flutter/…: check the user's memory, uploaded zip or earlier messages; if truly unknown ask once), device (default Poco X7 = 393×851 dp), what must be preserved (logic, names, data), existing screenshot/code if any. Write portuguese-BR UI text; no emojis in the UI.
2. **Design with intent** (load `frontend-design` first for aesthetic decisions; keep the user's accumulated taste: dark theme, Runna-style, flat — not "AI-looking": no decorative glows/gradients/pills unless asked, no uppercase micro-labels, no "·" in titles). Decide one hero element per screen; only it may animate at rest.
3. **Build the prototype under the replicability rules** in `references/prototype-rules.md` (short, read it — it is what makes the handoff work).
4. **Verify like a user would — never skip:**
   ```bash
   python3 scripts/lint.py proto.html                      # must print errors=0
   python3 scripts/render.py proto.html --out /tmp/v/main.png   # then Read the PNG and look: overlaps, clipping, dead space, fonts
   python3 scripts/render.py proto.html --out /tmp/v/menu.png --step click:#fab   # every interactive state
   python3 scripts/render.py proto.html --out /tmp/v/anim.png --step click:#x --filmstrip 6   # animations really move?
   ```
   Fix what you see, re-render, repeat. If the sandbox has no network, Google Fonts won't load and text will use a fallback font: judge layout by structure, say so, and never "fix" the design to suit the fallback.
5. **Build the HANDOFF folder:**
   ```bash
   python3 scripts/handoff_pack.py proto.html --out HANDOFF/<screen> --name <screen> --stack compose \
       --state "menu-open=click:#fab" --state "tap=click:#coach"
   ```
   It writes screens/*.png, spec/*.md (measured dp), lint.txt, a stack-specific `README_FOR_CLAUDE_CODE.md` and a copy of the replication skill under `.claude/skills/`.
6. **Write `HANDOFF/<screen>/SPEC.md`** from `assets/SPEC_template.md`: intent, tokens, layers, components, an animation table, states, known approximations, Do-NOT list. Numbers must match `spec/*.md` — copy, don't recompute. Several screens → one folder per screen plus a shared `THEME.md` (tokens used by all).
7. **Deliver**: publish the prototype as an Artifact for the user to look at (one URL, update in place); put the HANDOFF folder in a zip and send with SendUserFile; give the user a 3-line "how to use": unzip into the project root (or next to it), open Claude Code there, paste the contents of `README_FOR_CLAUDE_CODE.md`. If fonts are needed tell them exactly which files to download (family + weights) and where they go.
8. **Close honestly**: say which states you rendered and looked at, lint result, and what is approximated in native (backdrop blur, glow). Do not claim "pixel-perfect" for something no one compared on a device.

## Prompt hygiene (the thing the user will paste)
- Stack filled in; never leave placeholders. Refer to files by name (`SPEC.md`, `screens/x.png`), not by pasting hex tables that already live there.
- One screen per prompt, in order: theme+fonts → shared components → screen → navigation.
- Include the verify loop (build → screenshot → compare → fix) — it is the single most important instruction.
- State what must NOT change (logic, data, routes).

## When the user later sends a screenshot instead of HTML
Treat the screenshot as the reference: write the SPEC from what you see (measure by eye in dp and say it's approximate), skip the HTML unless asked, still produce the compare loop prompt.
