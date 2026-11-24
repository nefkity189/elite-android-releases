Use a skill `html-to-native-replica` (em `HANDOFF/02-detalhes-compartilhar/.claude/skills/`; leia o SKILL.md dela antes de tudo).

Projeto: Android, Jetpack Compose, pacote `com.elite.sch.app`, tema escuro, tela de referência 393x851 dp (Poco X7), textos em pt-BR.
Pasta do trabalho: `HANDOFF/02-detalhes-compartilhar/`. Leia nesta ordem: `../LEIA-ME.md`, `../THEME.md`, `SPEC.md`, depois abra os PNGs de `screens/` e `spec/*.md`.

Tarefa: na tela **Detalhes da Corrida** (`WorkoutSummaryScreen` com `isDetailOnly = true`), (1) botão de compartilhar no topo, à direita do título; (2) card "Todos os dados"; (3) card "Compartilhar corrida" com miniatura + botão. Vale para livre, 2,5K, 5K e 10K. Corrija também o bug do `MetricBlock` (argumentos trocados) descrito no SPEC.

Passos:
1. Preflight da skill: me diga se faltar algo (Chromium, emulador, fontes) antes de escrever código.
2. Renderize `screen.html` com `--fonts-dir app/src/main/res/font` e **olhe os PNGs** (Detalhes topo, Detalhes rolado, Preview).
3. Siga a tabela "Mudanças" do SPEC.md, na ordem, compilando a cada bloco. Motion: tabela do SPEC, linha por linha.
4. Rode no emulador (393 dp), capture, compare com a skill (`compare.py`/`diff_images.py`) e repita até a seção "Aceitação". Faça a **verificação funcional** nas 4 modalidades, com corrida com rota e sem rota.
5. Relatório final: prints antes/depois, números do diff, checklist de animações, desvios conhecidos e o que **não** foi verificado.

Não altere o design nem crie elementos que não estejam no HTML. **Não crie um segundo fluxo de compartilhamento**: reaproveite `showSharePreview` / `ShareCardPreviewScreen`. A ligação a partir da aba Progresso é outra tarefa (pasta 01): não mexa em `ProgressScreen` agora. Se algo estiver ambíguo ou faltar arquivo, pergunte antes de codar. Responda em português.
