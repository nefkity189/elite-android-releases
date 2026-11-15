# Plan: Separate Databases Per Plan Type

**Complexity**: High

## Summary
Create separate Room databases for each plan modality (livre, plano_2_5k, plano_5k, plano_10k) so users can switch between plans without losing data. When a plan is selected, its specific database is opened with all workouts and logs intact. If data already exists, the user goes directly to the Today screen without re-answering setup questions.

## Requirements Restatement
1. Four separate SQLite database files: `primeiro_5k_livre.db`, `primeiro_5k_2_5k.db`, `primeiro_5k_5k.db`, `primeiro_5k_10k.db`
2. Each database contains all entities (RunnerProfileEntity, PlanEntity, WorkoutEntity, WorkoutLogEntity, RoutePointEntity, ActiveRunEntity, PlanAdjustmentEntity, CheckinEntity, AutoAdjustmentEntity)
3. When user selects a plan modality, open its corresponding database
4. If database has existing data (active plan), navigate directly to Today screen
5. If database is empty, show onboarding wizard for that specific plan
6. Maintain all current functionality (adaptive engine, pauses, regeneration, free runs, reminders, GPS tracking)
7. Document all changes for future updates

## Patterns to Mirror

| Category | Source | Pattern |
|---|---|---|
| Database singleton | `AppDatabase.kt:55-182` | `INSTANCE` pattern with synchronized lazy initialization |
| Repository DI | `PlanRepository.kt:34-45` | Constructor injection of DAOs from database instance |
| ViewModel init | `MainViewModel.kt:122-138` | Database instance created in init, repository constructed from DAOs |
| Modalidade field | `PlanEntity.kt:17` | `modalidade` field distinguishes plan types |
| Migration system | `AppDatabase.kt:59-163` | Versioned migrations with `fallbackToDestructiveMigrationFrom` |

## Files to Change

| File | Action | Why |
|---|---|---|
| `app/src/main/java/com/example/data/local/AppDatabase.kt` | **REFACTOR** | Create abstract base class + 4 concrete subclasses (one per modality) |
| `app/src/main/java/com/example/data/local/DatabaseProvider.kt` | **CREATE** | Factory/provider to get correct database instance by modality |
| `app/src/main/java/com/example/data/local/PlanRepository.kt` | **UPDATE** | Accept modality parameter, use DatabaseProvider |
| `app/src/main/java/com/example/ui/MainViewModel.kt` | **MAJOR UPDATE** | Switch database on modality change, observe correct database, handle onboarding flow |
| `app/src/main/java/com/example/ui/screens/GoalScreen.kt` | **UPDATE** | Navigate directly to Today if plan data exists |
| `app/src/main/java/com/example/ui/screens/TodayScreen.kt` | **MINOR UPDATE** | Handle modality-specific data loading |
| `app/src/main/java/com/example/ui/screens/PlanPreviewScreen.kt` | **UPDATE** | Skip preview if plan already exists for modality |
| `app/build.gradle.kts` | **UPDATE** | Add Room annotation processor for new database classes |

## Tasks

### Task 1: Create Modality-Specific Database Classes
- **Action**: Refactor `AppDatabase.kt` into abstract base `BaseAppDatabase` + 4 concrete subclasses (`FreeRunDatabase`, `Plan2_5kDatabase`, `Plan5kDatabase`, `Plan10kDatabase`)
- **Mirror**: `AppDatabase.kt:29-183` pattern with entities, migrations, and singleton getter
- **Validate**: Each database builds independently, migrations apply correctly

### Task 2: Create DatabaseProvider Factory
- **Action**: New `DatabaseProvider.kt` with `getDatabase(modality: String, context: Context): BaseAppDatabase`
- **Mirror**: `AppDatabase.kt:165-182` singleton pattern but keyed by modality
- **Validate**: Returns correct instance per modality, caches instances

### Task 3: Update PlanRepository for Modality
- **Action**: Add `modality` parameter to constructor, get DAOs from modality-specific database
- **Mirror**: `PlanRepository.kt:34-45` constructor injection pattern
- **Validate**: Repository works with each database independently

### Task 4: Major MainViewModel Refactor
- **Action**: 
  - Add `currentModality` state, `databaseProvider` reference
  - On modality change: close old repo, create new repo with new database, re-observe flows
  - In `selectModality()`: check if plan exists in target DB → go to Today or start onboarding
  - Update `startPlan()`, `generateTemplatePlan()`, `deleteCurrentPlan()`, `resetEntirePlan()` to use modality-specific repo
- **Mirror**: `MainViewModel.kt:115-138` init pattern, `MainViewModel.kt:182-252` observeDatabase pattern
- **Validate**: Switching modalities loads correct data, onboarding only runs for empty DBs

### Task 5: Update GoalScreen Flow
- **Action**: In `selectModality()` handler, query target database for existing active plan before navigating
- **Mirror**: `MainViewModel.kt:475-532` selectModality logic
- **Validate**: Existing plan → Today screen; empty → onboarding

### Task 6: Update PlanPreviewScreen
- **Action**: Check if plan exists for selected modality; if yes, skip preview and go to Today
- **Mirror**: `MainViewModel.kt:394-413` generatePreview logic
- **Validate**: No redundant preview for existing plans

### Task 7: Update Tests
- **Action**: Add tests for DatabaseProvider, multi-database switching, modality-specific repositories
- **Mirror**: `Template25KTest.kt`, `Template10KTest.kt` patterns
- **Validate**: All existing tests pass + new tests cover switching

## Validation
```bash
# Build and test
./gradlew :app:build
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedAndroidTest

# Manual verification checklist
# 1. Select plano_2_5k → onboarding → Today → complete workouts → switch to plano_5k → onboarding → complete workouts → switch back to plano_2_5k → data intact → Today screen
# 2. Select livre → free run → switch to plano_10k → onboarding → switch back to livre → free run history intact
# 3. Delete plano_5k → confirm data gone → select plano_5k again → fresh onboarding
```

## Risks

| Risk | Likelihood | Mitigation |
|---|---|---|
| Migration conflicts across 4 databases | HIGH | Each database has independent migration history; test migrations on each |
| Memory leaks from multiple open databases | MEDIUM | Close previous database/repo when switching; use single cached instance per modality |
| ViewModel flow observation breaks on switch | HIGH | Properly dispose old flows, re-observe new repo flows in `observeDatabase()` |
| Schema drift between databases | MEDIUM | Single source of truth: all entities in common package; databases share entity classes |
| ReminderScheduler conflicts per modality | LOW | Reminders tied to active plan in current modality; cancel on switch |
| ActiveRun recovery across modalities | LOW | ActiveRunEntity stored per database; only current modality's run recoverable |

## Acceptance Criteria
- [ ] Four database files created and managed independently
- [ ] Switching modality loads correct data instantly
- [ ] Existing plan data persists across app restarts and modality switches
- [ ] Onboarding wizard only appears for empty databases
- [ ] All current features work per-modality (adaptive engine, pauses, GPS, reminders, free runs)
- [ ] Unit and instrumented tests pass
- [ ] Changes documented in this plan file

## Future Maintenance Notes
- **Adding new modality**: Create new database subclass, add to DatabaseProvider, add case in MainViewModel.selectModality()
- **Schema changes**: Update BaseAppDatabase entities + version; add migration to ALL 4 subclasses
- **Shared data across modalities**: Currently none (RunnerProfileEntity is per-modality). If needed, add separate shared database or sync logic.
- **Backup/restore**: Each database file backed up independently via `android:fullBackupContent`