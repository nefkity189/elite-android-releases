# Guia de Manutenção: Bancos de Dados por Modalidade

## Visão Geral

O app Aisle utiliza **4 bancos de dados Room separados**, um por modalidade de plano:

| Modalidade | Arquivo DB | Constante | Subclasse |
|---|---|---|---|
| livre | `primeiro_5k_livre.db` | `MODALIDADE_LIVRE` | `FreeRunDatabase` |
| plano_2_5k | `primeiro_5k_2_5k.db` | `MODALIDADE_2_5K` | `Plan2_5kDatabase` |
| plano_5k | `primeiro_5k.db` | `MODALIDADE_5K` | `AppDatabase` |
| plano_10k | `primeiro_5k_10k.db` | `MODALIDADE_10K` | `Plan10kDatabase` |

Cada banco compartilha as **mesmas entidades e migrações** (definidas em `BaseAppDatabase` e `BaseMigrations`), mas possui histórico de dados independente. Um usuário pode ter dados separados em cada modalidade.

---

## Arquitetura

```
BaseAppDatabase (abstrato)
├── 9 DAOs compartilhadas
└── BaseMigrations.all() → 7 migrações (v3→v10)

Subclasses concretas (4):
├── AppDatabase         → primeiro_5k.db       (plano_5k)
├── Plan5kDatabase      → primeiro_5k_5k.db    (plano_5k)
├── Plan2_5kDatabase    → primeiro_5k_2_5k.db  (plano_2_5k)
├── Plan10kDatabase     → primeiro_5k_10k.db   (plano_10k)
└── FreeRunDatabase     → primeiro_5k_livre.db (livre)
```

**Factory**: `DatabaseProvider.getDatabase(modality, context)` retorna a instância correta, cacheada por modalidade.

---

## Fluxo de Troca de Modalidade

1. `MainViewModel.selectModality(modality)` é chamado
2. `switchModality(modality)` cancela todos os jobs de observação anteriores
3. `DatabaseProvider.getDatabase(modality, ctx)` abre o banco correto
4. Novo `PlanRepository` é criado com os DAOs do novo banco
5. `observeDatabase()`, `observeProfile()`, `observeWorkoutLogs()`, `observeActiveRun()` são reexecutados
6. `repository.activePlan.firstOrNull()` verifica se já existe plano ativo
   - **Sim**: vai direto para `AppScreen.Today` (sem re-onboarding)
   - **Não**: fluxo de onboarding por modalidade (livre → plano livre; 2.5k/10k → DiasSelection → template; 5k → DiasSelection → Duration → Preview)

---

## Componentes Modificados

### `DatabaseProvider.kt`
- `getDatabase(modality, context)` factory com `when` sobre as 4 constantes de modalidade
- `getDatabaseFileName(modality)` retorna o nome do arquivo DB para cada modalidade

### `MainViewModel.kt`
- `currentModality`, `currentDb`, `currentRepo` rastreiam o estado atual
- `observeJobs: MutableList<Job>` permite cancelar observações ao trocar de banco
- `switchModality()` cancela jobs, abre novo DB, recria repository, re-observa
- `selectModality()` usa `repository.activePlan.firstOrNull()` para detectar dados existentes

### `RunTrackingService.kt`
- `EXTRA_MODALITY` extra na Intent
- `currentModality` field salvo em `startRun()`
- `savePointToDb()` e `saveActiveRunToDb()` usam `DatabaseProvider.getDatabase(currentModality, ctx)`

### `MainActivity.kt`
- `startRunServiceAndNavigate()` recebe `modality` param e passa via `EXTRA_MODALITY`
- Todas as chamadas passam `state.selectedModality`

### `BootReceiver.kt`
- Itera sobre todas as 4 modalidades para verificar lembretes ativos

### `WorkoutReminderWorker.kt`
- Itera sobre todas as 4 modalidades para verificar planos ativos e enviar notificações

---

## Adicionando uma Nova Modalidade

1. Adicione a constante em `DatabaseProvider` (ex: `MODALIDADE_X = "plano_x"`)
2. Crie o subclasse de `BaseAppDatabase` com o nome do arquivo DB desejado
3. Adicione o caso no `when` de `getDatabase()` e `getDatabaseFileName()`
4. Adicione a modalidade à lista em `BootReceiver` e `WorkoutReminderWorker`
5. Adicione o caso em `selectModality()` no `MainViewModel`

---

## Migrações

Todas as subclasses usam `BaseMigrations.all()` (migrações v3→v10 compartilhadas).
Cada banco começa em versão 10. Bancos existentes de versões anteriores são migrados via `addMigrations()`.

---

## Build / KSP

### Regra: imports explícitos nas subclasses de Database

Cada subclasse de `BaseAppDatabase` deve **importar explicitamente todas as entidades**
que declara em `@Database(entities = [...])`. A `AppDatabase` original já faz isso; as
subclasses criadas para as novas modalidades precisam fazer o mesmo.

Sem os imports, o KSP (Room 2.8.5 + KSP 2.3.13) falha com:

```
e: [ksp] [MissingType]: Element 'com.example.data.local.FreeRunDatabase' references a type that is not present
```

e gera apenas `AppDatabase_Impl.kt`, deixando as outras subclasses sem implementação.

**Verificação:** `./gradlew :app:kspDebugKotlin` deve gerar um `.kt` de `_Impl` em
`app/build/generated/ksp/debug/kotlin/com/example/data/local/` para cada uma das 5 subclasses.

### Regra: `dbConnSQL` não é `db/execSQL`

Na migração `MIGRATION_3_4` a linha `dbConnSQL(...)` (com `.`) foi escrita como
`db/execSQL(...)` (com `/`), o que o Kotlin interpretou como `db.div(...)`.
Sempre use `dbConnSQL(...)`.

### Regra: `repository` é `var` no MainViewModel

`MainViewModel.repository` é reatribuído em `switchModality()`, portanto é `var`,
não `val`.