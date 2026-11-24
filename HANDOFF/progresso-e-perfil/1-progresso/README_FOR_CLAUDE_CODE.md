# Task for Claude Code: replicate the "1-progresso" screen in Jetpack Compose (Kotlin)

Use the skill `html-to-native-replica` (in `.claude/skills/`, read its SKILL.md first and follow its 5 steps).
Read `SPEC.md` in this folder first: it holds the design intent and the exact component/animation list.

Reference material in this folder (look at the PNGs with your image reading before coding):
- screens/1-progresso--reduced-motion.png
- screens/1-progresso.png
- proto.html (renders with `scripts/render.py`), spec/*.md (measured dp values, tokens, fonts, animations)
- lint.txt (known issues in the prototype)

Rules
1. Keep the app's existing logic, data and navigation. Change only layout and visuals.
2. Fonts and theme first (bundle the font files listed in spec/*.md "Fonts used"), then components, then the screen.
3. Port flexible vs fixed sizes exactly as in the spec; port media-query rules as height-based conditionals.
4. Animations exactly as in the spec "Animations" section; nothing else animates at rest; honour reduced motion.
5. After building: run the app on an emulator (393x851dp), take a screenshot with `scripts/android_shot.sh`, run `scripts/compare.py` against the reference PNG and fix the largest differing region. Repeat until under ~1% (excluding live map/time/data) or 5 rounds.
6. Do not invent elements. If something is missing (icon, font, measurement) ask me.
7. Final report: diff % per screen, remaining differences, anything not verified.
