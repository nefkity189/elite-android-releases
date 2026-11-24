Use a skill **html-to-compose**.

Pasta do layout: `competicao-v2/` (contém `screen.html`, `HANDOFF.md`, `states.json`, `AUDITORIA-FLUXO.md`).
Projeto Android: `<CAMINHO_DO_PROJETO>`. Módulo: competição online (`ui/screens/competition`, `data/competition`, `service/RunTrackingService.kt`).

1. Rode o preflight da skill e me diga se faltar algo. Não escreva código antes de resolver.
2. Leia `AUDITORIA-FLUXO.md` e `HANDOFF.md` inteiros. A auditoria foi feita só lendo código, sem compilar: **confirme cada item C1 a C8 no código antes de corrigir** e me avise se algum não se confirmar.
3. Implemente na ordem da tabela **Mudanças** do HANDOFF: primeiro os itens **P0** (a prova roda: GPS iniciado pela competição, largada pela hora do servidor, desistir real, chegada detectada pelo serviço, reentrada na prova), depois **P1**. Pare ao final de cada grupo e me mostre o resultado antes de seguir.
4. Para as telas desenhadas: renderize o HTML com `--fonts-dir app/src/main/res/font`, **abra os PNGs**, meça com `inspect_dom.py`; compare com `diff_images.py` e `compare_bounds.py` (estados estáticos) e confira os **quadros** de `states.json` para as animações.
5. O emblema de liga (`LeagueEmblem`) deve seguir as camadas e animações da seção 8 e da tabela Motion; só um emblema animado visível por vez.
6. O `firestore.rules` e o `firestore.indexes.json` do seu repositório precisam acompanhar as mudanças (campos `status`, `abandonedAt`; índices por `elo25/elo5/elo10`). Mostre o diff das regras e **não publique nada** sem eu aprovar.
7. Não toque no que o HANDOFF marca como decisão pendente (pontos/elo, tabela de ligas, tempo máximo): pergunte antes.
8. Rode `./gradlew testDebugUnitTest` (nenhum teste antigo pode quebrar) e entregue o relatório com prints antes/depois, números do diff, checklist de animações e desvios conhecidos.
