# ROADMAP — Primeiro 5K

## Visão Geral
Roteiro de entregas do aplicativo **Primeiro 5K**, organizado por marcos (milestones) incrementais. Cada marco adiciona valor verificável ao usuário final.

---

## M0 — Fundação e Onboarding (ATUAL)
**Status**: ✅ Concluído  
**Entregas**:
- Fluxo de boas-vindas e questionário de triagem (3 perguntas)
- Configuração de frequência semanal (2–4 dias), dias preferidos e data de início
- Seleção de duração do plano (6–16 semanas)
- Prévia do plano com aviso médico obrigatório
- Motor `PlanGenerator` puro, determinístico, com 9 etapas em `ProgressionTable.kt`
- Regra absoluta: **nunca treinos de corrida em dias consecutivos**
- Tela "Hoje" com passos do treino ou "Dia de descanso"
- Ações: Marcar concluído, Pular, Desfazer
- Cronograma completo com progresso percentual
- Reinício/reconfiguração do plano
- Persistência 100% local (Room + SQLite)
- UI Jetpack Compose + Material 3, temas claro/escuro, pt-BR

---

## M1 — Registro de Treino e Histórico (PRÓXIMO)
**Status**: 🔄 Planejado  
**Objetivo**: Permitir registrar execução real do treino (GPS, tempo, pace) e visualizar histórico.
**Entregas previstas**:
- Tela de execução de treino (`ActiveRunScreen`) com GPS, pace, distância, tempo
- Serviço foreground `RunTrackingService` para rastreamento contínuo
- Persistência de `RoutePointEntity` (pontos GPS) e `WorkoutLogEntity` (resumo da sessão)
- Histórico de treinos concluídos com métricas (distância, pace médio, duração)
- Exportação/importação de backup (JSON) — `BackupManager`

---

## M2 — Adaptação e Regeneração de Plano
**Status**: 🔄 Planejado  
**Objetivo**: Ajustar o plano conforme execução real, pausas, dores ou mudanças de disponibilidade.
**Entregas previstas**:
- `PlanRegenerator`: extensão/regeneração com limite de 2 etapas/semana
- `AdaptiveEngine`: ajustes baseados em dor/esforço/skip (puro Kotlin)
- `PauseApplier`: pausa/retorno com step-back (0–3 etapas) por tempo parado e ritmo de retorno
- 3 níveis de atividade durante pausa: `sem_treino`, `caminhada`, `reduzido`
- Extensão automática da data final do plano
- Check-in pós-treino (dor, esforço, sentimento)

---

## M3 — Modalidades Adicionais
**Status**: 📋 Backlog  
**Objetivo**: Planos para 2.5K, 10K e corrida livre.
**Entregas previstas**:
- Modalidade `livre` (corrida sem plano fixo)
- Modalidade `plano_2_5k` (template adaptado)
- Modalidade `plano_10k` (template adaptado)
- Multi-database: 4 Room DBs isolados por modalidade
- Troca de modalidade preservando preferências de lembrete

---

## M4 — Lembretes e Notificações
**Status**: 📋 Backlog  
**Objetivo**: Engajamento via lembretes diários configuráveis.
**Entregas previstas**:
- `WorkoutReminderWorker` (WorkManager) com canal dedicado `lembretes_treino`
- Agendamento `OneTimeWorkRequest` sem drift
- Preferências de horário e dias de lembrete
- Restauro automático no boot (`BootReceiver`)
- Supressão em dias pausados

---

## M5 — Qualidade, Testes e Preparação para Release
**Status**: 📋 Backlog  
**Objetivo**: Cobertura de testes ≥ 80%, CI/CD, assinatura de release.
**Entregas previstas**:
- Testes unitários (JUnit, Robolectric, Turbine) — gerador, repositório, engine adaptativa
- Testes instrumentados (Compose UI, Room migrations)
- CI (GitHub Actions): build, lint, test
- Assinatura de release via secrets (keystore não commitado)
- ProGuard/R8 configuração

---

## Critérios de Avanço de Fase (conforme Plano de Estruturação)
| Fase | Critério |
|---|---|
| Requisitos | Funcionalidades, restrições e critérios de aceite definidos |
| Arquitetura | Decisões críticas documentadas e riscos analisados (ADRs) |
| Estrutura técnica | Projeto executa e verificações básicas funcionam |
| Implementação | Mudança revisada e critérios da tarefa atendidos |
| Testes | Testes relevantes aprovados e falhas críticas resolvidas |
| Implantação | Monitoramento, backups e reversão preparados |

---

## Métricas de Acompanhamento
- Cobertura de testes por módulo (meta ≥ 80%)
- Número de issues abertos/fechados por milestone
- Tempo médio de ciclo (lead time) por tarefa
- Retrabalho introduzido por mudança