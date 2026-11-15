# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Aisle** (`com.example`, applicationId `com.aistudio.primeiro5k.kptwlz`) — on-device Android running-training app ("Primeiro 5K"). No cloud, no auth, no third-party APIs in this phase. Everything (plan generation, persistence, run tracking) lives on the device.

## Build & Test Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Unit tests (JVM, Robolectric)
./gradlew :app:testDebugUnitTest

# Instrumented tests (device/emulator)
./gradlew :app:connectedAndroidTest

# KSP / Room schema generation
./gradlew :app:kspDebugKotlin

# Lint (if configured)
./gradlew :app:lintDebug
```

**Gradle / SDK / Language:**
- Gradle 9.5.1 (wrapper present)
- AGP 9.5.1
- Kotlin 2.4.21 (official code style via `kotlin.code.style=official` in `gradle.properties`)
- minSdk 24, targetSdk 37, compileSdk 37
- Java 11 compatibility (desugaring enabled)

**gradle.properties highlights:**
```
org.gradle.caching=true
org.gradle.configuration-cache=true
org.gradle.parallel=true
kotlin.compiler.execution.strategy=in-process
```

## Architecture

### Layers
```
com.example.ui          → Jetpack Compose + Material 3 screens, components, theme
com.example.generator   → Pure Kotlin plan math (PlanGenerator, PlanRegenerator, templates)
com.example.model       → WorkoutStep, StageProgression
com.example.data.local  → Room DBs, DAOs, entities, repositories, migrations
com.example.tracker     → GPS session, distance schedule, pace/split/week calculators
com.example.service     → RunTrackingService (foreground GPS tracking)
com.example.worker      → WorkoutReminderWorker (WorkManager reminders)
com.example.workspace   → WorkspaceManager, WorkspaceState, GlobalSession, AppScreen
com.example.backup      → BackupManager (JSON export/import)
com.example.receiver    → BootReceiver (restore reminders on boot)
```

### MVVM & State
- `MainActivity` hosts `AppNavigation` (Compose).
- `MainViewModel` (AndroidViewModel) delegates to `WorkspaceManager`.
- `WorkspaceManager` owns reactive `WorkspaceState` (`StateFlow`), consumed via `collectAsStateWithLifecycle()`.
- Navigation is state-driven: `screenStack: List<AppScreen>` in the ViewModel; `AppScreen` is a sealed interface.

### Multi-Database by Modality
4 separate Room databases (shared entities/migrations via `BaseAppDatabase` + `BaseMigrations`):

| Modality | DB File | Subclass | Constant |
|---|---|---|---|
| livre | `primeiro_5k_livre.db` | `FreeRunDatabase` | `MODALIDADE_LIVRE` |
| plano_2_5k | `primeiro_5k_2_5k.db` | `Plan2_5kDatabase` | `MODALIDADE_2_5K` |
| plano_5k | `primeiro_5k.db` | `AppDatabase` | `MODALIDADE_5K` |
| plano_10k | `primeiro_5k_10k.db` | `Plan10kDatabase` | `MODALIDADE_10K` |

- All at version 10 with 7 shared migrations (v3→v10) in `BaseMigrations.all()`.
- Factory: `DatabaseProvider.getDatabase(modality, ctx)` returns the correct singleton.
- `WorkspaceManager.switchToModality()` / `selectModality()` handle DB swap, cancel old observers, re-observe.

### Plan Engine (Deterministic, Not AI)
- `PlanGenerator.generate(PlanInput)` — pure Kotlin function (ADR-01).
- 9 provisional stages in `ProgressionTable.kt` (ADR-02) — editable without touching scheduling logic.
- Hard rule: **no two run workouts on consecutive days** (ADR-05). Validation in `hasConsecutiveDays()`.
- Progression: short plans compress stages; long plans repeat stages (monotonic). `calculateStageForWeek()`.
- High-fitness users (3×/week + cardio) start at stage 2; others at stage 1.
- Regeneration (`PlanRegenerator`): max 2 stage advances/week; min-weeks formula `ceil((9 - stage) / 2) + 1` (ADR-06).
- Templates for 2.5K/10K: `PlanGenerator.generateFromTemplate()` with `Template25K` / `Template10K`.

### Adaptive Engine & Pause (M3a)
- `AdaptiveEngine` — pain/effort/skip-driven plan adjustments (pure Kotlin).
- `PauseApplier` — pause/return with step-back (0–3 stages) based on time off and return pace; 3 activity levels (`sem_treino`, `caminhada`, `reduzido`); auto-extends plan end date (ADR-07).
- `PlanRepository.finishRunAndAdjust()` runs log + post-check-in + adaptive engine in one transaction.

### Run Tracking
- `RunTrackingService` (foreground) — GPS session, distance schedule, pace/splits/milestones.
- `DistanceSchedule` derives current step from accumulated distance (resilient to service restarts).
- Notifications: pause/resume toggle action; no stop button (avoid accidental taps).
- `WorkoutReminderWorker` — daily reminders via WorkManager; dedicated channel `lembretes_treino` (IMPORTANCE_DEFAULT).

## Key Conventions

### Kotlin Style
- `val` by default; `var` only when mutation is required.
- Sealed interfaces for state hierarchies (`UiState`, `AppScreen`); exhaustive `when` without `else`.
- No `!!` — use `?.`, `?:`, `requireNotNull()`, `checkNotNull()`.
- Immutable data classes; updates via `.copy()`.
- Extensions in type-named files (`StringExt.kt`, `FlowExt.kt`).

### State & Persistence
- One `StateFlow` per screen/workspace; UI collects with `collectAsStateWithLifecycle()`.
- All persistence local (Room); **no network calls** in the app.
- `.env` / `.env.example` drive the Secrets Gradle plugin; `GEMINI_API_KEY` is commented out and **not packaged**.
- Release signing uses env vars (`KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD`) — **never commit `my-upload-key.jks`**.
- ProGuard/R8: release build has `isMinifyEnabled = false`, `isShrinkResources = false`.

### Testing
- Unit: `app/src/test` — JUnit 4, Robolectric, Turbine, `kotlinx-coroutines-test` (`runTest`).
- Instrumented: `app/src/androidTest` — Compose UI tests, Room migrations.
- Test naming: backtick-quoted descriptive names (e.g., `` `nunca dois treinos de corrida em dias seguidos` ``).
- Minimum coverage: ViewModel + UseCase for every feature.

### Permissions (AndroidManifest.xml)
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`
- `POST_NOTIFICATIONS` (Android 13+)
- Foreground service types: `location`, `health` (for RunTrackingService)

## Notable Files for Quick Orientation

| File | Purpose |
|---|---|
| `app/src/main/java/com/example/workspace/WorkspaceManager.kt` | Central orchestrator: modality switching, plan generation, DB swap, clear-all-data |
| `app/src/main/java/com/example/generator/PlanGenerator.kt` | Deterministic plan math (generate, generateFromTemplate, validations) |
| `app/src/main/java/com/example/generator/PlanRegenerator.kt` | Plan extension/regeneration with safety bounds |
| `app/src/main/java/com/example/generator/ProgressionTable.kt` | 9-stage table (editable by fitness pros) |
| `app/src/main/java/com/example/generator/AdaptiveEngine.kt` | Pain/effort/skip-driven adjustments |
| `app/src/main/java/com/example/generator/PauseApplier.kt` | Pause/return step-back logic |
| `app/src/main/java/com/example/data/local/MULTI_DATABASE_GUIDE.md` | 4-DB modality architecture in detail |
| `app/src/main/java/com/example/data/local/DatabaseProvider.kt` | Factory for the 4 Room DBs |
| `app/src/main/java/com/example/ui/MainViewModel.kt` | Thin delegation layer to WorkspaceManager |
| `app/src/main/java/com/example/service/RunTrackingService.kt` | Foreground GPS tracking + notifications |
| `app/src/main/java/com/example/tracker/DistanceSchedule.kt` | Distance-based step evaluation |
| `ARCHITECTURE.md` | C4 diagram + layer descriptions |
| `DECISIONS.md` | 7 ADRs (plan engine, DB, nav, no-consecutive, M2c, M3a) |
| `TESTING.md` | Test strategy, scenarios, how to run |
| `CORRECOES_P1-P7.md` | UI restoration notes (TodayScreen, ActiveRunScreen) |
| `CHANGELOG-UI.md` | Delivery log (D1, D4, D4b, D5, Entrega 2/3) |

## External Tooling

### jev-local Decision Server
Local classification/decision server at `http://127.0.0.1:8000` (model `Qwen/Qwen2.5-3B-Instruct`). Use from Python (`urllib`/`requests`) or `curl.exe` instead of cloud chat tokens.

- **Emulator**: use `http://10.0.2.1:8000`
- **Payload (POST JSON)**:
```json
{
  "state": "texto a avaliar",
  "model": "jev-latest",
  "questions": {
    "nome_decisao": {
      "type": "noul|score|choice",
      "instructions": "pergunta",
      "criteria": [...],  // for score
      "min": 0, "max": 10, // for score
      "choices": ["op1", "op2"], // for choice
      "criteria": {"op1": "desc", "op2": "desc"} // for choice
    }
  }
}
```
- Example: `teste_jev.py` in repo root.

### mobile-mcp
Plugin for driving a real/simulated Android device (list devices, launch app, click, swipe, screenshot, logs). Start with `mobile_list_available_devices`, pass device ID to all calls.

## Development Workflow (ECC Rules Applied)

This repo follows the ECC rule set (common + Kotlin/Android):

1. **Research & Reuse first** — GitHub search, library docs, package registries before new code.
2. **Plan first** — `ecc:planner` agent for complex features; produces PRD/architecture/task list.
3. **TDD** — `ecc:tdd-guide` agent; write test first (RED), implement (GREEN), refactor, verify 80%+ coverage.
4. **Code review** — `ecc:code-reviewer` (or `ecc:kotlin-reviewer`) immediately after writing code.
5. **Security review** — `ecc:security-reviewer` for auth, user input, DB queries, crypto, payments.
6. **Commit** — Conventional Commits format; detailed messages.
7. **Pre-review checks** — CI passing, no conflicts, branch up to date.

See `~/.claude/rules/ecc/common/development-workflow.md` and `~/.claude/rules/ecc/kotlin/` for full rules.

## Common Tasks

| Task | Command / Approach |
|---|---|
| Add a new modality | 1. Add constant in `DatabaseProvider` 2. Create `BaseAppDatabase` subclass 3. Add case in `getDatabase()` / `getDatabaseFileName()` 4. Update `BootReceiver` / `WorkoutReminderWorker` 5. Add case in `MainViewModel.selectModality()` — see `MULTI_DATABASE_GUIDE.md` |
| Modify stage definitions | Edit `ProgressionTable.kt` only — no DB migration needed |
| Add a migration | Add to `BaseMigrations.all()`; bump DB version in all 4 subclasses |
| Run only one test | `./gradlew :app:testDebugUnitTest --tests "com.example.PlanGeneratorTest.*"` |
| Debug Room schema | Check `app/schemas/com.example.data.local.AppDatabase/*.json` (and other DBs) |
| Verify KSP generated all DBs | `./gradlew :app:kspDebugKotlin` → check `app/build/generated/ksp/debug/kotlin/com/example/data/local/*_Impl.kt` for all 5 subclasses |

## What NOT to Do

- ❌ Don't add network calls or cloud sync — app is on-device first.
- ❌ Don't hardcode secrets — use `.env` (git-ignored) + Secrets plugin.
- ❌ Don't commit keystore files (`*.jks`, `debug.keystore`).
- ❌ Don't use `!!` or `try/catch` for control flow.
- ❌ Don't mutate state — use `.copy()` on data classes.
- ❌ Don't skip tests — 80% minimum coverage required.
- ❌ Don't add dependencies for a few lines — prefer stdlib or existing deps.
## Fluxo Padrão de Build e Testes UI (Android + Maestro MCP)
Sempre que eu pedir para rodar, testar ou validar o app no emulador, execute automaticamente esta sequência:
1. Compile a versão mais recente com .\gradlew.bat assembleDebug.
2. Verifique db devices. Se nenhum emulador estiver rodando como device, inicie o AVD disponível com -no-snapshot-load e aguarde db shell getprop sys.boot_completed retornar 1.
3. Instale o APK mais recente de pp\build\outputs\apk\debug\ usando db install -r -t.
4. Leia o pplicationId em pp/build.gradle.kts e use o Maestro MCP (launchApp) para abrir o app, garantindo que o Maestro Viewer (http://localhost:9999) esteja aberto no navegador.
