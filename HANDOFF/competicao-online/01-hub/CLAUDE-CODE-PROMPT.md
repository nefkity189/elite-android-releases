Use a skill **html-to-compose**.

Pasta do layout: `competicao-online/01-hub/` (contém `screen.html`, `HANDOFF.md`, `states.json`).
Projeto Android: `<CAMINHO_DO_PROJETO>` (o mesmo do zip de exemplo: `ui/theme`, `ui/components`, `ui/screens`).
Tela: **Elite - Hub de Competição Online**. Composable(s) esperado(s): `CompetitionHubScreen, MissionCard, DistanceSegmented, ShareIconButton`.

Antes de tudo: leia `competicao-online/LEIAME.md` (ordem das telas, componentes compartilhados e tokens novos). Se esta tela depende de componentes definidos em outra pasta (a coluna **Novo?** do Mapa de componentes diz qual), implemente aquela tela primeiro ou crie os componentes compartilhados antes.

1. Rode o preflight da skill e me diga se faltar algo (Chromium, emulador, fontes). Não escreva código antes de resolver.
2. Renderize o HTML com `--fonts-dir app/src/main/res/font` e **abra os PNGs**; meça com `inspect_dom.py`. As fontes são Barlow Condensed, Inter e Saira Stencil One (já usadas pelo app).
3. Siga a tabela **Mudanças** do HANDOFF.md na ordem. Animações: tabela **Motion**, linha por linha.
4. Compile, rode no emulador (393dp), capture, compare com `diff_images.py` e `compare_bounds.py`; repita até a seção **Aceitação**.
5. Entregue o relatório com prints antes/depois, números do diff, checklist de animações e **desvios conhecidos**.

Não altere o design nem adicione elementos que não estejam no HTML. Se algo estiver ambíguo ou faltar arquivo (fonte, ícone), pergunte antes de codar.
