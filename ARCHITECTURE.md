# ARCHITECTURE — Primeiro 5K

## 1. Visão Geral (C4 Context & Container)

O aplicativo **Primeiro 5K** opera no modelo On-Device First:
- Toda a lógica de geração de plano e persistência reside localmente no smartphone Android.
- Não existem dependências de microsserviços, APIs remotas de terceiros ou autenticação em nuvem nesta fase.

```
+-----------------------------------------------------------+
|                      Dispositivo Android                  |
|                                                           |
|   +---------------------------------------------------+   |
|   |         Interface Jetpack Compose (M3)            |   |
|   |   (Welcome, Quiz, Days, Duration, Preview, Today) |   |
|   +-------------------------+-------------------------+   |
|                             |                             |
|                             v                             |
|   +---------------------------------------------------+   |
|   |                  MainViewModel                    |   |
|   +-------------------+-------------------------------+   |
|                       |                                   |
|         +-------------+-------------+                     |
|         v                           v                     |
|   +-------------------+       +-----------------------+   |
|   |   PlanGenerator   |       |    PlanRepository     |   |
|   | (ProgressionTable)|       +-----------+-----------+   |
|   +-------------------+                   |               |
|                                           v               |
|                               +-----------------------+   |
|                               |  AppDatabase (Room)   |   |
|                               | (Profile, Plan, Work) |   |
|                               +-----------------------+   |
+-----------------------------------------------------------+
```

## 2. Camadas do Sistema

### 2.1 Apresentação (`com.elite.sch.app.ui`)
- **Jetpack Compose + Material 3**: UI moderna, reativa, responsiva e com suporte a temas claro/escuro.
- **`MainViewModel`**: Expõe `uiState: StateFlow<MainUiState>` consumido com `collectAsStateWithLifecycle()`.
- **Navegação & Telas**:
  - `WelcomeScreen`: Introdução em 2 frases e botão Começar.
  - `QuizQuestionScreen`: Questionário 1, 2 e 3 em telas dedicadas com barra de progresso.
  - `DaysSelectionScreen`: Configuração de dias, dias da semana e data inicial.
  - `DurationScreen`: Slider de duração com equivalência de ritmo.
  - `PlanPreviewScreen`: Visualização do plano e consentimento de saúde obrigatório.
  - `TodayScreen`: Treino do dia com passos (ou descanso) e ações de conclusão/pulo.
  - `FullPlanScreen`: Navegação completa por semanas e progresso acumulado.

### 2.2 Domínio e Motor de Treino (`com.elite.sch.app.generator`, `com.elite.sch.app.model`)
- **`PlanGenerator`**: Função Kotlin pura e determinística. Calcula a distribuição de semanas, comprime ou repete etapas e agenda as datas respeitando a proibição de treinos em dias consecutivos.
- **`ProgressionTable`**: Arquivo isolado e desacoplado que centraliza as 9 etapas provisórias de treino para fácil revisão e ajustes por profissionais de saúde.
- **`WorkoutStep` & `StageProgression`**: Modelos puros representando cada fração de aquecimento, corrida, caminhada e desaquecimento.

### 2.3 Persistência Local (`com.elite.sch.app.data.local`)
- **Room Database** (`primeiro_5k.db`):
  - `RunnerProfileEntity`: Perfil e respostas de triagem do corredor.
  - `PlanEntity`: Metadados do plano ativo (início, término, total de semanas, termo aceito).
  - `WorkoutEntity`: Registro individual de cada treino agendado (etapa, data, passos em JSON, status e timestamp de conclusão).
  - `PlanRepository`: Abstração de acesso aos DAOs (`RunnerProfileDao`, `PlanDao`, `WorkoutDao`) retornando `Flow` reativo.
