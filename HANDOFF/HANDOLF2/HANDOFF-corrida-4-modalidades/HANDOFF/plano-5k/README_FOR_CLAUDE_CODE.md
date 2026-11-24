# Task for Claude Code: replicate the "plano-5k" screen in Jetpack Compose (Kotlin)

Use the skill `replica-claude` (in `../.claude/skills/`, read its SKILL.md first and follow its 5 steps; copy `.claude/` to the project root if it is not there yet).
Read `SPEC.md` in this folder first: it holds the design intent and the exact component/animation list.

Reference material in this folder (look at the PNGs with your image reading before coding):
- screens/plano-5k--camera-direcao.png
- screens/plano-5k--camera-rota.png
- screens/plano-5k--caminhe.png
- screens/plano-5k--fan-dark.png
- screens/plano-5k--fan-open.png
- screens/plano-5k--parar-segurando.png
- screens/plano-5k--pausado.png
- screens/plano-5k--radial-aberto.png
- screens/plano-5k--reduced-motion.png
- screens/plano-5k--ultima-etapa.png
- screens/plano-5k.png
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


## Contexto desta entrega (importante)
1. Leia `../THEME.md` (tokens, componentes compartilhados, animações, divergências) e depois `SPEC.md` desta pasta.
2. O código Kotlin do leque, do estilo do mapa, do pill por modo de câmera e da câmera inicial vinda do Perfil **já foi escrito** sobre o `elite.zip` (`../reference-code/`, mesmas pastas; `../reference-code/leque-mapa.patch` é o diff). Não reescreva do zero: aplique, compile e ajuste só o que divergir do SPEC. O Claude do chat não compilou o código; se o build quebrar, conserte e me mostre.
3. **Não altere** o menu do topo direito (`RadialMenu`), o Pausar nem o Parar (animações existentes, descritas em THEME §5.3–5.4).
4. Comparação com o protótipo: ignore a região do mapa (ilustração); compare topo, hero, bloco inferior e os leques abertos.
5. Itens "Divergências e pontos a confirmar" do THEME §7: me avise o que encontrar no aparelho em vez de decidir sozinho.
