Use a skill `html-to-native-replica` (em `.claude/skills/html-to-native-replica/` desta pasta; leia o SKILL.md dela antes de tudo).

Projeto: Android, Jetpack Compose, pacote `com.elite.sch.app`, tema escuro, tela de referência 393x851 dp (Poco X7), pt-BR.
Pasta do trabalho: `HANDOFF/01-modo-espectador/`. Leia: `SPEC.md` inteiro, depois abra `screens/*.png` e `spec/*.md`.

Tarefa: **modo espectador** nas competições online. Na lista "Missões de hoje", a linha de prova **EM ANDAMENTO** ganha um botão discreto (olho) no lugar do ícone de compartilhar; ele abre uma tela **somente leitura** com o placar ao vivo, e vira "ENCERRADA" com o vencedor quando a prova termina. Vale para 2,5K, 5K e 10K. Arquivos: `CompetitionFlowScreen.kt` e `CompetitionComponents.kt`.

Passos:
1. Preflight da skill: me diga se faltar algo (Chromium, emulador, fontes) antes de escrever código.
2. Renderize `screen.html` com `--fonts-dir app/src/main/res/font` e **olhe os PNGs** (hub, espectador, encerrada).
3. Siga a seção 6 do SPEC.md na ordem (6.1 componente, 6.2 fluxo/tela), compilando a cada bloco.
4. Rode no emulador (393 dp), compare com a skill e repita até a seção 10. Faça a **verificação funcional** com 3 contas da mesma UF nas 3 distâncias, incluindo a checagem no Firestore (`playerCount` igual e nenhum `players/{uid do espectador}`).
5. Relatório final: prints antes/depois, números do diff, desvios e o que **não** foi verificado.

Regras: o espectador **não** chama `joinMission`, `updateRoomPresence`, `startRace` nem GPS; não altera repositório, ViewModel nem `firestore.rules`; reaproveita `RankingPersonRow`, `RaceMetric`, `CompetitionTopBar`, `CompetitionPill`, `CompetitionCard`, `CompetitionAction`. Não altere o design além do HTML. Se algo estiver ambíguo, pergunte antes de codar (veja a seção 11). Responda em português.
