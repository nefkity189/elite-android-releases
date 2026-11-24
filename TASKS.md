# TASKS — Primeiro 5K

## Formato
| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|

---

## M0 — Fundação e Onboarding (Concluído)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T01 | Tela Welcome + botão Começar | ui | Alta | — | Exibe 2 frases, navega para Quiz | ✅ |
| T02 | Quiz 3 perguntas (tempo, frequência, tipo) | ui | Alta | T01 | Navegação step-by-step, progresso, respostas salvas | ✅ |
| T03 | Seleção dias/semana + data início | ui | Alta | T02 | 2–4 dias, checkboxes dias semana, date picker | ✅ |
| T04 | Seleção duração 6–16 semanas | ui | Alta | T03 | Slider com label "Mais curto = mais puxado" | ✅ |
| T05 | PlanPreview + aviso médico + checkbox | ui | Alta | T04 | Lista semanas, checkbox obrigatório habilita botão | ✅ |
| T06 | PlanGenerator puro (Kotlin) | generator | Crítica | — | Função determinística, sem rede, 9 etapas | ✅ |
| T07 | ProgressionTable.kt (9 etapas) | generator | Crítica | — | Arquivo isolado, editável sem tocar lógica | ✅ |
| T08 | Regra não-consecutividade | generator | Crítica | T06 | Validação `hasConsecutiveDays()` passa | ✅ |
| T09 | Nível alto inicia etapa 2 | generator | Alta | T06 | 3x/sem + cardio → stage 2 | ✅ |
| T10 | Estrutura treino (aquecimento, intervalos, desaquecimento) | generator | Alta | T06 | 5min walk + blocos + 5min walk | ✅ |
| T11 | TodayScreen (treino do dia / descanso) | ui | Alta | T05 | Passos clicáveis, ações concluir/pular/desfazer | ✅ |
| T12 | FullPlanScreen (cronograma + progresso) | ui | Alta | T05 | Semanas expansíveis, % progresso | ✅ |
| T13 | Reinício/reconfiguração plano | ui | Média | T11 | Limpa estado, volta para Welcome | ✅ |
| T14 | Room Database (Profile, Plan, Workout) | data | Crítica | — | 3 entities, DAOs, Flow reativo | ✅ |
| T15 | PlanRepository (abstração DAOs) | data | Crítica | T14 | CRUD + observers via Flow | ✅ |
| T16 | MainViewModel + StateFlow + Navigation stack | ui | Crítica | — | screenStack, BackHandler, collectAsStateWithLifecycle | ✅ |
| T17 | Temas claro/escuro Material 3 | ui | Média | — | Cores, tipografia, dark mode automático | ✅ |
| T18 | Testes unitários PlanGenerator | test | Alta | T06 | Cobertura regras negócio ≥ 80% | ✅ |
| T19 | Testes unitários PlanRepository | test | Alta | T14 | CRUD + observers | ✅ |

---

## M1 — Registro de Treino e Histórico (Próximo)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T20 | ActiveRunScreen (GPS, pace, distância, tempo) | ui | Alta | T11 | Mapa, métricas tempo real, botão pausar/continuar | 🔄 |
| T21 | RunTrackingService (foreground GPS) | service | Crítica | — | Location updates, notification, sobrevive a kill | 🔄 |
| T22 | DistanceSchedule (step evaluation by distance) | tracker | Alta | T21 | Deriva step atual por distância acumulada | 🔄 |
| T23 | RoutePointEntity + DAO + Migration | data | Alta | T14 | Pontos GPS persistidos, schema v7+ | 🔄 |
| T24 | WorkoutLogEntity + DAO + Migration | data | Alta | T14 | Resumo sessão (dist, pace, tempo, status) | 🔄 |
| T25 | Histórico de treinos (lista + detalhes) | ui | Alta | T24 | Cards com métricas, navegação para detalhe | 🔄 |
| T26 | BackupManager (export/import JSON) | backup | Média | T24 | Exporta perfil+plano+logs, importa valida | 🔄 |
| T27 | Testes instrumentados ActiveRunScreen | test | Média | T20 | Compose UI test, mock GPS | 🔄 |
| T28 | Testes unitários RunTrackingService | test | Média | T21 | DistanceSchedule, pace calc | 🔄 |

---

## M2 — Adaptação e Regeneração de Plano (Planejado)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T29 | PlanRegenerator (extend/regenerate) | generator | Alta | T06 | Limite 2 etapas/semana, min-weeks formula | 📋 |
| T30 | AdaptiveEngine (dor/esforço/skip) | generator | Alta | T29 | Ajustes puros, sem efeitos colaterais | 📋 |
| T31 | PauseApplier (pause/return step-back) | generator | Alta | T29 | 0–3 etapas recuo, 3 níveis atividade | 📋 |
| T32 | PlanAdjustmentEntity + Migration | data | Alta | T14 | Tabela plan_adjustment, status pausado | 📋 |
| T33 | Check-in pós-treino (UI + persistência) | ui/data | Alta | T32 | Dor (1-10), esforço (RPE), sentimento | 📋 |
| T34 | PlanRepository.finishRunAndAdjust() | data | Alta | T31,T33 | Transação: log + check-in + adaptive | 📋 |
| T35 | Testes unitários AdaptiveEngine | test | Alta | T30 | Cenários dor/skip/esforço | 📋 |
| T36 | Testes unitários PauseApplier | test | Alta | T31 | Cenários tempo parado/ritmo retorno | 📋 |

---

## M3 — Modalidades Adicionais (Backlog)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T37 | Modalidade Livre (FreeRun) | ui/generator | Média | — | Corrida sem plano, tracking apenas | 📋 |
| T38 | Modalidade 2.5K (Template25K) | generator | Média | T06 | generateFromTemplate, 9 etapas adaptadas | 📋 |
| T39 | Modalidade 10K (Template10K) | generator | Média | T06 | generateFromTemplate, 9 etapas adaptadas | 📋 |
| T40 | Multi-Database (4 DBs isolados) | data | Alta | T14 | FreeRun, 2.5K, 5K, 10K — factory DatabaseProvider | 📋 |
| T41 | WorkspaceManager (modality switch) | workspace | Alta | T40 | selectModality, switchToModality, cancel observers | 📋 |
| T42 | BootReceiver + WorkoutReminderWorker multi-modal | worker | Média | T40,T41 | Restore reminders per modality | 📋 |
| T43 | Testes migrações multi-DB | test | Média | T40 | Room migration test all 4 DBs | 📋 |

---

## M4 — Lembretes e Notificações (Backlog)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T44 | WorkoutReminderWorker (WorkManager) | worker | Média | — | OneTimeWorkRequest, canal lembretes_treino | 📋 |
| T45 | Preferências lembrete (horário, dias) | ui/data | Média | T44 | Settings screen, persistido no Profile | 📋 |
| T46 | BootReceiver (restore reminders) | receiver | Média | T44 | Reagenda works no boot | 📋 |
| T47 | Supressão lembretes dias pausados | worker | Baixa | T31,T44 | Worker checa status pausa antes de notificar | 📋 |
| T48 | Testes WorkoutReminderWorker | test | Baixa | T44 | WorkManager test rules | 📋 |

---

## M5 — Qualidade, Testes e Release (Backlog)

| ID | Título | Módulo | Prioridade | Dependências | Critérios de Aceite | Estado |
|---|---|---|---|---|---|---|
| T49 | CI GitHub Actions (build, lint, test) | ci | Alta | — | PR checks passam, badge no README | 📋 |
| T50 | Cobertura testes ≥ 80% (all modules) | test | Alta | T18,T19,T27,T28,T35,T36 | Jacoco/Kover report | 📋 |
| T51 | Assinatura release (secrets, no keystore commit) | build | Alta | — | keystore via env vars, CI deploy | 📋 |
| T52 | ProGuard/R8 release config | build | Média | — | isMinifyEnabled=true, shrinkResources=true | 📋 |
| T53 | Lint rules customizadas | build | Baixa | — | Detector rules para padrões do projeto | 📋 |

---

## Legenda de Estado
- ✅ Concluído
- 🔄 Em andamento / Próximo
- 📋 Backlog / Planejado
- ❌ Bloqueado / Cancelado

---

## Como Usar
1. **Filtrar por milestone**: `grep "M1" TASKS.md`
2. **Ver próximas tarefas**: `grep "🔄" TASKS.md`
3. **Atualizar estado**: Editar coluna "Estado" ao iniciar/concluir
4. **Adicionar tarefa**: Inserir linha na tabela do milestone correspondente