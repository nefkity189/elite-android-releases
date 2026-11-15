# Alterações de interface desta entrega (6 nov 2026)

Não compilado nem testado aqui (sem Android SDK). Rode `./gradlew test` e abra o app antes de seguir.

- Novo `ui/components/AisleDialogs.kt`: `AisleStepper` (− e + de 48 dp, repete ao segurar), `AisleSwitchRow` e `AisleDialog` (com `confirmTestTag`).
- Boas-vindas reescrita: `AisleLogo`, "Do zero aos 5 km", 3 benefícios, campo de nome, CTA fixo acima do teclado, entrada única com fade e subida (respeita animações desligadas). testTags `input_user_name` e `btn_comecar` mantidos.
- Dias, Duração e Prévia: `TopAppBar` e `Button` trocados por `AisleTopBar` e `AisleButton` (testTags mantidos).
- Duração, Corrida (encerrar) e Perfil (apagar dados, 2 etapas): `AlertDialog` trocado por `AisleDialog`.

Pendente: fontes Barlow (colocar .ttf em res/font), Cards das telas Dias/Duração/Prévia, e Material cru em Plano, Perfil, Hoje e Progresso (prompts P04 a P08).

---

## Entrega D1 (10 nov 2026): Conexão UI ↔ Backend — Seleção de Modalidade → Geração de Plano
**Base:** código estável com testes unitários passando (165 testes). Rode `./gradlew testDebugUnitTest` e `./gradlew assembleDebug` antes de testar no celular.

- **GoalScreen.kt (NOVO)**: Tela de escolha de objetivo com 4 modalidades (Corrida Livre, Primeiros 2,5K, Primeiros 5K, Primeiros 10K). Cada opção abre painel de detalhes animado com destaques específicos e botões Cancelar/Confirmar.
- **MainViewModel.kt**: Estado `selectedModality` + método `generateTemplatePlan()` que invoca `PlanGenerator.generateFromTemplate()` usando `Template25K` ou `Template10K` conforme a escolha. Preparado para Free Run (sem template).
- **MainActivity.kt**: Navegação `GoalScreen → DiasScreen → DuracaoScreen → PlanoScreen` conectando a modalidade selecionada à geração do plano. Passa `selectedModality` e `diasSelecionados` entre as telas.
- **Template10K.kt (NOVO)**: Estrutura completa do plano 10K (12 semanas, 3 treinos/semana) com sessões LEVE, INTERVALADO, LONGO e PROVA.
- **PlanGenerator.generateFromTemplate()**: Motor de templates funcionando e testado — distribui sessões nos dias escolhidos (sem dias consecutivos), coloca LONGO no último dia, trata semana de prova com PROVA no domingo.
- **Testes validando D1**: `Template25KTest` (3 testes), `Template10KTest` (a criar), `PlanGeneratorTemplateTest` (3 testes) — todos passando.

---

## Entrega D4b (6 nov 2026): Ação Pausar/Retomar na Notificação da Corrida
Base: entrega anterior. Esta entrega não foi compilada aqui: rode `./gradlew :app:testDebugUnitTest` e `./gradlew :app:assembleDebug` antes de testar no celular. Sem novas dependências.
- `RunTrackingService`: nova action `ACTION_TOGGLE_PAUSE_RESUME` que alterna entre `pauseRun()` e `resumeRun()` conforme o estado atual (`isPaused`).
- Notificação (setOngoing): ganha **um único botão** dinâmico — "Pausar" quando rodando, "Retomar" quando pausado — via `PendingIntent.getService` com `FLAG_IMMUTABLE`. **Sem botão de encerrar** (evita toque acidental).
- Texto da notificação mostra o passo atual (`schedule.currentStep?.instruction`) + tempo formatado; atualizado no ticker de 1 s existente.
- Strings: `notification_action_pause` / `notification_action_resume` em `strings.xml`.
- Teste unitário puro: `NotificationActionMapperTest.kt` (4 cenários: rodando→pausar, pausado→retomar, não rodando→nada, finalizado→nada).
- **Pendente validação no aparelho**: comportamento na tela bloqueada (Android 10+ exibe ações, mas alguns OEMs exigem desbloqueio para `PendingIntent.getService` com `FLAG_IMMUTABLE`).

## Entrega 2 (6 nov 2026)
Base: o seu AISLE_1.zip, que compilou (APK e 7.json gerados). Esta entrega não foi compilada aqui.
- Perfil: Voz, Vibração e Lembrete diário agora usam `AisleSwitchRow` (testTags `switch_voice`, `switch_vibration`, `switch_reminders` mantidos). O diálogo de horário usa `AisleDialog` com dois `AisleStepper` (hora de 1 em 1, minuto de 5 em 5, giro 23→0 e 55→0; testTags `btn_hour_*`, `btn_min_*`, `btn_confirm_time` mantidos).
- `AisleDialog` aceita conteúdo próprio; `AisleStepper` aceita passo, giro e testTags.

## Entrega 3 (6 nov 2026): P11, motor de ajuste (Plano que respira)
Base: AISLE_2.zip. Esta entrega não foi compilada aqui: rode `./gradlew test` (gera o schema 8.json) antes de testar no celular. Sem novas dependências.
- Banco versão 8 com `MIGRATION_7_8` (sem apagar dados): tabelas `checkin` e `auto_adjustment`; colunas `ajustesAutomaticos` (padrão ligado) e `modoAjuste` (padrão cauteloso) em `runner_profile`.
- `generator/AdaptiveEngine.kt` (puro, sem Android/rede): dor forte (3 dias sem corrida, repete a etapa anterior, sem avanço por 7 dias), dor leve (repete a etapa, +30 s de caminhada), esforço 8 ou 10 (+30 s, máximo +60 s; dois seguidos repete a etapa), avanço (só devolve etapas tiradas antes, no máximo 1 por semana; cauteloso sugere, equilibrado aplica), 2 pulados seguidos (usa `PauseApplier.calculateRecuo`) e check-in antes do treino. Limiares em `AdaptiveConfig`.
- `PlanRepository`: `finishRunAndAdjust` (log + check-in pós + motor numa transação; falha do motor não perde o log), `applyAdaptiveResult`, `undoAdjustment`, `evaluateAfterSkip`; novo plano preserva as preferências de ajuste; apagar dados limpa as tabelas novas.
- `MainViewModel.saveWorkoutSummary` ganhou `dor` e `regiaoDor` (valores padrão), mais `acceptAdaptiveSuggestion`, `undoAutoAdjustment`, `setAjustesAutomaticos` e `setModoAjuste`. A UI do P12 ainda não existe: hoje o app ajusta o plano, mas só com dor 0 (a tela de resumo ainda não pergunta dor).
- Testes: `AdaptiveEngineTest` (21 cenários) e `ClearAllDataTest` cobrindo as tabelas novas. Falta o teste de migração 7→8 (precisa de androidTest com `MigrationTestHelper`).

---

## Entrega D4 (10 nov 2026): Motor de Distância e Conclusão por Distância
- `tracker/DistanceSchedule`: motor puro de avaliação de treino por distância.
  - Deriva o passo atual diretamente da distância acumulada no bloco principal (`totalDistanceMeters`), garantindo resiliência total a reinícios do serviço (`active_run`).
  - O aquecimento e desaquecimento ficam fora do cálculo da distância alvo do plano.
  - Emite avisos de voz específicos (`DistanceVoiceCue`: `StartStep`, `MainBlockCompleted`, `WorkoutCompleted`, `GpsLostFallback`, `GpsRestored`).
  - Detecção de perda de GPS (> 30s sem atualização válida) com fallback para tempo estimado via `StepEstimator`.
- Testes: `DistanceScheduleTest` (13 cenários cobrindo limites, passos parciais, retomada de serviço, GPS perdido/restaurado, transição de fases).

## Entrega D5 (10 nov 2026): Planos Profissionais (2,5K e 10K) e Motor de Templates
- `generator/PlanTemplate`: modelo de dados para planos baseados em template (suporta passos por tempo ou distância, aquecimento/desaquecimento, intensidades descritivas).
- `generator/Template25K`: transcrição completa do plano profissional de 2,5K (6 semanas, 18 sessões + prova, com 9 premissas documentadas em `assumidosReport`).
- `generator/PlanGenerator.generateFromTemplate()`: motor de geração de plano aplicando regras profissionais:
  - Distribuição de sessões nos dias escolhidos pelo usuário (respeitando ADR-05: sem dias consecutivos).
  - Sessão LONGO no último dia da semana.
  - Semana da prova: sessão PROVA no último dia (domingo); remoção automática de treino no dia anterior para evitar dias consecutivos (quando aplicável).
- Testes: `Template25KTest` validando a estrutura do template, geração do plano e relatório de premissas.
