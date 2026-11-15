# DECISIONS — Architecture Decision Records (ADRs)

## ADR-01: Motor de Plano Puro vs. Geração por IA em Tempo Real
- **Status:** Aceito.
- **Contexto:** Planos de exercício físico impactam a saúde articular e cardiovascular do usuário. Gerar rotinas por IA generativa a cada execução introduz alucinações, variabilidade indesejada e riscos de segurança.
- **Decisão:** O motor `PlanGenerator` é uma função Kotlin pura, 100% determinística, auditável e coberta por testes unitários locais.

## ADR-02: Separação da Tabela de Progressão (`ProgressionTable.kt`)
- **Status:** Aceito.
- **Contexto:** As 9 etapas são provisórias e precisarão ser revisadas por profissionais de educação física.
- **Decisão:** Toda a parametrização de intervalos de corrida, caminhada, repetições e tempos foi colocada em um único arquivo dedicado (`ProgressionTable.kt`), permitindo edição sem alterar a lógica de agendamento ou o banco de dados.

## ADR-03: Persistência 100% Local com Room Database
- **Status:** Aceito.
- **Contexto:** O corredor treina em praças, estradas ou esteiras frequentemente sem sinal de celular. O app não deve exigir login para funcionar.
- **Decisão:** Utilização do SQLite via Room com KSP, armazenando perfil, plano e treinos localmente no aparelho.

## ADR-04: Navegação Baseada em Estado Unificado no ViewModel
- **Status:** Aceito.
- **Contexto:** O fluxo de onboarding e questionário requer transições sem perda de estado parcial, além de suporte obrigatório ao botão físico/gesto de voltar do Android (`BackHandler`).
- **Decisão:** Pilha de telas (`screenStack`) controlada de maneira segura pelo `MainViewModel` e tratada por `BackHandler` em cada tela secundária.

## ADR-05: Regra Absoluta de Não Consecutividade de Treinos
- **Status:** Aceito.
- **Contexto:** Treinar corrida em dias consecutivos para iniciantes é a causa número um de canelite, tendinite e abandono do programa.
- **Decisão:** O gerador impõe uma regra estrita: `workout[i+1].date >= workout[i].date + 2 days`.

## ADR-06: Correções e Segurança do Marco M2c
- **Status:** Aceito.
- **Contexto:** Avanços abruptos de etapa após regeneração poderiam expor o atleta a saltos excessivos (ex: etapa 3 para 9 em 2 semanas). Lembretes derivavam e usavam canal do serviço de rastreamento.
- **Decisão:** Limite de no máximo 2 etapas de avanço por semana; exigência de prazo mínimo com base na fórmula `teto((9 - etapa) / 2) + 1`; preenchimento prioritário de semanas incompletas; canal de notificação dedicado `lembretes_treino` (IMPORTANCE_DEFAULT); agendamento OneTimeWork sem drift; e retenção das preferências de lembretes ao criar novo plano.

## ADR-07: Pausa e Retorno Gradual (Marco M3a)
- **Status:** Aceito.
- **Contexto:** Interrupções por lesão, dor ou férias exigem retorno adaptado para evitar lesões por reentrada brusca.
- **Decisão:** Criação da tabela `plan_adjustment` (Migration 5->6) e status `pausado`. Implementação da função pura `PauseApplier` com cálculo de recuo (0 a 3 etapas) baseado no tempo parado e ritmo de retorno (Gradual, Normal, Rápido), três níveis de atividade ("reduzido", "caminhada", "sem_treino") e extensão automática do prazo final para respeitar a progressão biomecanicamente segura. Supressão de lembretes em dias pausados.
