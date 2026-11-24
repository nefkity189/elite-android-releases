---
name: html-to-native-replica
description: Replicate an HTML/CSS layout prototype (made by Claude in chat, or any .html mockup, screenshot or SPEC.md) as a native mobile screen in Jetpack Compose, Flutter, React Native or SwiftUI with pixel-level fidelity, including animations. Use this whenever the user hands over an .html prototype, a "layout corrigido", a SPEC.md, a HANDOFF folder, a reference screenshot, or says "faça igual ao protótipo", "replique esse layout", "ficou diferente do HTML", "não ficou igual", "copie essa tela", "refaça a tela como no mockup" — even if they never mention this skill. Gives you eyes (render the HTML to PNG), hands (measure every element in dp) and a verify loop (screenshot the app, diff it against the reference).
---

# HTML → native replica

You cannot judge an HTML file by reading its CSS: CSS is a recipe, the *result* is what the user approved. Past attempts failed for three reasons — the agent never saw the rendered prototype, it translated CSS to native by guessing (so traps like 1px gradients, `weight(1f, fill=false)`, missing fonts crept in), and it never looked at its own result. This skill fixes all three: **see → measure → build → look → diff → fix**.

Units: CSS px = Android dp = Flutter logical px = SwiftUI pt. Copy numbers as-is; never rescale.

## 0. Find the inputs (30 seconds)
Look for, in this order of authority: `HANDOFF/` folder (`SPEC.md`, `screens/*.png`, `*.html`), reference PNGs from the user, the `.html`. If there is a `SPEC.md` or `CLAUDE_CODE_PROMPT.md`, read it fully first — it records design intent the HTML can't ("this is the only animated element"). If the HTML and the SPEC disagree, trust the rendered PNG, then ask.

Scripts live in `scripts/` next to this file; run them with `python3`. They need `playwright` + `Pillow` + `numpy` (Chromium already installed). If missing, tell the user the one `pip install playwright pillow numpy && playwright install chromium` line and continue with what you can.

## 1. SEE — render the prototype and look at it
```bash
python3 scripts/render.py proto.html --out work/ref/main.png            # default 393x851dp @2x, cropped to the phone frame
python3 scripts/render.py proto.html --out work/ref/menu-open.png --step click:#fab   # every interactive state
python3 scripts/render.py proto.html --out work/ref/orb-anim.png --step click:#coach --filmstrip 6 --interval 120   # animation frames
python3 scripts/render.py proto.html --out work/ref/reduced.png --reduced-motion
```
Then **open each PNG with the Read tool** — you do read images. Write down what you see in one list (regions top→bottom, what overlaps what, what is a map/background vs a card). Do this before touching code. Capture one PNG per distinct state (menu open, pressed, empty/filled data) — the states are part of the design.

## 2. MEASURE — get exact numbers instead of eyeballing
```bash
python3 scripts/inspect_layout.py proto.html --out work/spec/main [--step click:#fab]
```
Read `work/spec/main.md`: tokens, fonts, media queries, every animation with keyframes/duration/easing, and an element tree with `@(x,y) wxh` in dp plus colors, radius, padding, gap, shadows, gradients. This is the ground truth for sizes and positions. Pay attention to:
- `Fonts used … loaded=False` → the renderer lacked the font (offline); numbers are still right, but **in the app you must bundle the real font files** (see references/compose.md / flutter.md). A silent fallback to the system font was the biggest visual miss last time.
- `Media queries` → responsive rules (e.g. hide the axis under 760 dp height) you must port as conditionals on available height.
- Elements with `flex-grow:1` are the *flexible* ones; everything else has a fixed size. Port that distinction literally (see the traps list).
- `::before/::after` entries are real drawn layers (e.g. the rotating gradient ring of a button).

## 3. TRANSLATE — use the mapping, not intuition
Open the reference for your stack **before writing code**:
- Jetpack Compose → `references/compose.md`
- Flutter → `references/flutter.md`
- Animations (any stack) → `references/animations.md` (CSS keyframes → native, with exact recipes for spin, breathe, pop, flip, ripple wave, radial menu fan-out)
- Which stack? Detect from the repo (`build.gradle`/`*.kt` → Compose, `pubspec.yaml` → Flutter, `package.json` with react-native → RN, `*.swift` → SwiftUI). If several exist, ask.

Rules that prevent the known failures:
1. **Fonts and theme first.** Add the .ttf files, declare the font families, put colors/sizes in one theme file, *then* build screens. Verify a font is really used by checking the screenshot glyph shapes (condensed numbers look narrow).
2. **Keep the app's logic.** Change only layout/visuals unless told otherwise. Don't rename state, repositories, view-models.
3. **One screen at a time, in dependency order**: theme+fonts → shared components → each screen → bottom nav. Commit/build after each.
4. **No invented elements.** If the prototype lacks it, don't add it; if the app has a feature the prototype lacks, keep it but ask where it goes.
5. Preserve behavior of states: press scale, toggles, dismiss-on-outside-tap, safe-area insets, reduced-motion.

## 4. LOOK — screenshot the real app and compare (never skip)
```bash
# build + run on emulator (Compose: ./gradlew installDebug ; Flutter: flutter run -d <id>)
bash scripts/android_shot.sh work/app/main.png          # adb screencap
python3 scripts/compare.py work/ref/main.png work/app/main.png --out work/diff/main.png
```
Read `work/diff/main.png` (left reference, middle app, right red diff) and the printed worst regions (in dp). Fix the biggest region, rebuild, repeat. Stop when diff is under ~1% outside of dynamic content (map tiles, real time/dates), or after 5 rounds — then report the remaining differences honestly instead of claiming success.

Match the emulator to the reference size: Pixel-class 393×851 dp (`adb shell wm size 1080x2340; adb shell wm density 420`) — otherwise compare.py will warn about aspect mismatch. The app screenshot includes status/nav bars; crop them or compare below the status bar (`--grid` regions in dp tell you where). Animations: capture a few frames (`adb exec-out screencap` in a loop every 120 ms) and compare against the filmstrip, or at least confirm duration/easing/axis from the spec numbers.

## 5. Report
End with: screens done, final diff % per screen, what still differs and why (e.g. "map tiles are live", "font X missing from res/font — build failed"), and anything you couldn't verify (no emulator → say "not visually verified"). Never write "identical" without a diff run.

## Checklist before saying done
- [ ] Rendered every state PNG and looked at it
- [ ] Fonts bundled and confirmed in screenshot
- [ ] Flexible vs fixed sizes ported as in spec
- [ ] Animations match duration/easing/loop count; only the intended ones run at rest
- [ ] Reduced-motion path implemented
- [ ] Safe-area/insets respected (status bar, gesture bar)
- [ ] App screenshot diffed; remaining gaps listed
