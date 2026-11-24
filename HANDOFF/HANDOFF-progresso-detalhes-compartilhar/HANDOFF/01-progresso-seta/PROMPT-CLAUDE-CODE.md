Use a skill `html-to-native-replica` (em `HANDOFF/01-progresso-seta/.claude/skills/`; leia o SKILL.md dela antes de tudo).

Projeto: Android, Jetpack Compose, pacote `com.elite.sch.app`, tema escuro, tela de referência 393x851 dp (Poco X7), textos em pt-BR.
Pasta do trabalho: `HANDOFF/01-progresso-seta/`. Leia nesta ordem: `../LEIA-ME.md`, `../THEME.md`, `SPEC.md` (seção 0 explica onde essa tela fica), depois abra `screens/progresso--ONDE-ESTA.png` e os demais PNGs e `spec/*.md`.

Pré-requisito: a tarefa da pasta 02 (Detalhes + compartilhar) já feita e aprovada.

Tarefa: na **aba Progresso** (4ª posição da barra inferior, `AppScreen.Progress` / `ProgressScreen.kt`), a **seta de cada corrida do Histórico** e o card inteiro devem abrir a tela **Detalhes da Corrida** (`WorkoutSummaryScreen(isDetailOnly = true)`) daquela corrida, nas 4 modalidades (livre, 2,5K, 5K, 10K). Hoje a seta é só um ícone sem clique e o card abre o diálogo antigo `RunDetailDialog`.

Passos:
1. Preflight da skill: me diga se faltar algo antes de escrever código.
2. Renderize `screen.html` com `--fonts-dir app/src/main/res/font` e **olhe os PNGs**.
3. Siga a seção 6 do SPEC.md (6.1 a 6.6) na ordem, compilando a cada bloco: novo parâmetro `onViewRunDetail`, seta como `IconButton`, remover `RunDetailDialog`, corrigir a cor do título "Histórico de Corridas", ligar em `MainActivity` com `selectedDetailLog = log to workout`.
4. Compile, rode no emulador (393 dp), compare a aba Progresso com a skill e repita até a seção 10 (Aceitação).
5. Faça a **verificação funcional** da seção 10 nas 4 modalidades, incluindo: voltar mantém a posição da lista; cada seta abre a **própria** corrida.
6. Relatório final: prints, números do diff, desvios e o que não foi verificado.

Não altere `RunnaBottomNav` nem `CoachButton`. Não crie outro `Dialog` dentro do `ProgressScreen`: reaproveite o `selectedDetailLog` do `MainActivity`. Não altere o design além do que está no HTML. Se algo estiver ambíguo, pergunte antes de codar. Responda em português.
