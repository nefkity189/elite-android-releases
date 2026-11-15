# Plan: Implement Rigid Specification for App de Corrida

**Source**: `D:\Especificacao_Rigida_Fluxograma_App_Corrida.txt`
**Complexity**: Large
**Selected Milestone**: Full implementation per spec S00-S14

## Summary
This plan implements the complete rigid specification for a running app with four independent plan types (Livre, 2.5K, 5K, 10K). Each plan type has its own quiz → generate → accept → training flow, with total isolation between distances. The app uses modality-separated Room databases, and plan activation requires explicit user acceptance persisted with `accepted_at` and `accepted_terms_version`.

## Patterns to Mirror

| Category | Source | Pattern |
|---|---|---|
| Naming | `WorkspaceState.kt:20-85` | `data class WorkspaceState` with val immutability, copy-on-write via `_uiStateUpdate { it.copy(...) }` |
| Error handling | `PlanRepository.kt:512-554` | `finishRunAndAdjust` uses `database.withTransaction { ... }` with catch for optional adjustments |
| Tests | `spec §10` | 16 mandatory tests covering first-access, name persistence, free-run isolation, quiz isolation per distance, acceptance gating, delete-plan independence, persistence across restart, DB failure handling, double-tap prevention |
| Data access | `DatabaseProvider.kt:12-20` | `getDatabase(modality, context)` routes to separate DB per modality (FreeRunDatabase, Plan2_5kDatabase, AppDatabase, Plan10kDatabase) |
| State management | `WorkspaceManager.kt:289-291` | `_uiStateUpdate { _currentWorkspace.value = block(...) }` thread-safe state update pattern |

## Files to Change

| File | Action | Why |
|---|---|---|
| `app/src/main/java/com/example/workspace/WorkspaceManager.kt` | UPDATE | Add `resetWizardState()` call in `hydrateWorkspace`; add `clearRunSummaryData()` (exists at line 845); ensure modality switch resets wizard fields per spec §6 |
| `app/src/main/java/com/example/data/local/entities/PlanEntity.kt` | UPDATE | Add `accepted_at: Long` and `accepted_terms_version: String` fields; add `STATUS_DRAFT = "rascunho"` state |
| `app/src/main/java/com/example/data/local/dao/PlanDao.kt` | UPDATE | Add `getActivePlan(modalidade: String)` keyed by modality; add `updatePlanAcceptance(planId, acceptedAt, termsVersion)` |
| `app/src/main/java/com/example/data/local/PlanRepository.kt` | UPDATE | Add per-modalidadeness to `finishRunAndAdjust`; add `savePlanAcceptance(planId, acceptedAt, termsVersion)`; ensure delete-plan frees only that distance's quiz |
| `app/src/main/java/com/example/ui/screens/WelcomeScreen.kt` | UPDATE | Add mandatory name gate: reject empty/whitespace, block advance until valid name saved |
| `app/src/main/java/com/example/ui/screens/GoalSelectionScreen.kt` | UPDATE | Route 2.5K/10K to their own quizzes; remove shared questionnaire state |
| `app/src/main/java/com/example/ui/MainViewModel.kt` | UPDATE | Route `saveWorkoutSummary` through modality-aware DB; ensure `clearRunSummaryData()` resolves; add acceptance persistence calls |
| `app/src/main/java/com/example/workspace/WorkspaceState.kt` | UPDATE | Add `acceptedAt: Long?` and `acceptedTermsVersion: String?`; add plan-type-specific fields isolation |

## Tasks

### Task 1: Add acceptance fields to PlanEntity and Dao
- **Action**: Add `accepted_at: Long` and `accepted_terms_version: String` to `PlanEntity`; add `STATUS_DRAFT = "rascunho"`; add `getActivePlan(modalidade)` to `PlanDao`; add acceptance update methods to `PlanRepository`
- **Mirror**: `PlanEntity.kt:8-19` pattern for new fields; `PlanDao.kt` query pattern
- **Validate**: `./gradlew assembleDebug` compiles; unit test verifies acceptance persistence

### Task 2: Implement mandatory name gate in WelcomeScreen
- **Action**: Modify `WelcomeScreen` to reject empty/whitespace name; only advance to Home after valid name is saved to DB (per spec §2)
- **Mirror**: `WelcomeScreen.kt:162-170` OutlinedTextField pattern; name validation in `MainViewModel.setUserName`
- **Validate**: Name field blocks advance when empty; reopening app shows saved name

### Task 3: Implement DB-driven ACTIVE+accepted decision per plan type
- **Action**: Modify `WorkspaceManager.selectModality()` and `MainViewModel.selectModality()` to query per-modality DB for ACTIVE+accepted plan; route to training screen if found, otherwise open quiz (per spec §5, §6)
- **Mirror**: `WorkspaceManager.kt:647-674` `selectModality` modality-to-screen mapping; `DatabaseProvider.getDatabase` per-modality routing
- **Validate**: Each plan type independently checks its own DB; 2.5K active doesn't waive 5K/10K quiz

### Task 4: Add resetWizardState() in hydrateWorkspace
- **Action**: Call `resetWizardState()` at the start of `hydrateWorkspace(modality)` to clear `diasSelecionados`, `dataInicio`, `totalSemanas`, `minSemanas`, `tempoSemCorrer`, `frequenciaExercicio`, `tipoExercicio`, quiz answers per spec §6
- **Mirror**: `WorkspaceManager.kt:852-866` `resetWizardState()` existing method
- **Validate**: Switching between modalities resets wizard fields; answers don't bleed between distances

### Task 5: Persist accepted_at and acceptance in transaction
- **Action**: Modify `PlanRepository.saveProfileAndGeneratePlan` and `finishRunAndAdjust` to persist `accepted_at`/`accepted_terms_version` in a transaction when user explicitly accepts; ensure failure never shows plan as active (per spec §5, §10 #14)
- **Mirror**: `PlanRepository.kt:150-221` `saveProfileAndGeneratePlan` transaction pattern; `finishRunWithLog` error handling
- **Validate**: Acceptance only persists after explicit save; DB failure shows error, never assumes absence

### Task 6: Implement delete-plan confirmation freeing only that distance's quiz
- **Action**: Modify `MainViewModel.deleteCurrentPlan()` and `WorkspaceManager.deleteActivePlan` to delete only the selected plan's data and free only that distance's quiz; cancel keeps plan active; confirm deletes only that plan and liberates quiz (per spec §10 #9-11)
- **Mirror**: `PlanRepository.kt:332-342` `deleteActivePlan` transaction; `WorkspaceManager.resetWizardState()` pattern
- **Validate**: Deleting 5K only affects 5K; 2.5K/10K remain independent; history preserved

### Task 7: Persist lastActiveModality and onboarding across restarts
- **Action**: Ensure `GlobalSession.lastActiveModality` and `completedOnboardingModalities` are saved to and loaded from the profile DB per modality; on restart, user returns to last modality/state (per spec §10 #13)
- **Mirror**: `MainViewModel.loadGlobalSession()` at line 74-96; `WorkspaceManager.completeOnboardingForCurrentModality()` at line 680-685
- **Validate**: Closing and reopening app maintains name, accepted plans, and onboarding state

### Task 8: Fix MainViewModel.clearRunSummaryData() compilation error
- **Action**: The method already exists in `WorkspaceManager.kt:845-847`; ensure the call site in `MainViewModel.saveWorkoutSummary()` resolves (currently unresolved due to missing method at time of edit)
- **Validate**: `./gradlew assembleDebug` compiles without errors

## Validation
```bash
# Build verification
./gradlew assembleDebug

# Run mandatory tests (spec §10)
# 1. Install/primeiro acesso sem nome: usuário não sai da tela boas-vindas
# 2. Nome válido salvo: Home abre; reabrir app não solicita nome novamente
# 3. Treino Livre: abre tela de treino sem quiz
# 4. Primeiro acesso ao 2.5K: abre quiz 2.5K; após concluir, gerar plano e pedir aceite
# 5. 2.5K sem aceite: não pode ficar ACTIVE nem abrir como plano criado
# 6. 2.5K aceito e salvo: abre tela de treino 2.5K
# 7. Próximo acesso ao 2.5K: vai direto à tela de treino, sem quiz
# 8. Ter 2.5K ativo não dispensa o quiz do 5K ou 10K
# 9. Apagar 5K e confirmar: somente 5K perde o estado ativo e fica liberado para novo quiz
# 10. Apagar 5K e cancelar: plano 5K permanece ativo
# 11. Apagar 5K não altera 2.5K, 10K nem seus históricos
# 12. Escolher outro plano retorna à Home sem apagar o plano atual
# 13. Fechar e reabrir app mantém nome, planos aceitos e histórico
# 14. Falha de banco durante salvamento: não exibir plano como ativo
# 15. Toque duplo em aceitar: não criar dois planos ativos duplicados
# 16. Falha ao consultar banco: mostrar erro; não presumir ausência de plano
```

## Risks
| Risk | Likelihood | Mitigation |
|---|---|---|
| HIGH: DB migration failures adding `accepted_at`/`accepted_terms_version` fields | Medium | Use Room's `fallbackToDestructiveMigrationFrom(true, 1, 2)`; test on clean install only |
| HIGH: Quiz answers bleeding between 2.5K/5K/10K if `resetWizardState()` not called | Medium | Always call `resetWizardState()` at start of `hydrateWorkspace`; add unit test verifying field reset on modality switch |
| MEDIUM: `clearRunSummaryData()` call site unresolved if method signature changes | Low | Method already exists at `WorkspaceManager.kt:845-847`; verify compile |
| MEDIUM: Modality switching with existing active plans may have edge cases | Medium | Add logic: if plan is ACTIVE+accepted for new modality, keep it; otherwise reset; test all switch combinations |
| LOW: Performance with 4 separate DBs and frequent modality switches | Low | DBs are lightweight Room in-memory; migration tested per spec |

## Acceptance
- [ ] All 16 mandatory tests from spec §10 pass
- [ ] `./gradlew assembleDebug` compiles without errors
- [ ] No hardcoded secrets in source
- [ ] No console.log or debug statements in final code
- [ ] All functions < 50 lines (per kotlin coding-style)
- [ ] No deep nesting (>4 levels) in when/sealed interfaces
- [ ] Immutaability: `val` preferred, copy-on-write via `_uiStateUpdate { it.copy(...) }`
- [ ] Each plan type independently checks its own modality DB
- [ ] 2.5K active plan does not waive 5K/10K quiz
- [ ] Delete-plan confirmation: cancel keeps plan, confirm frees only that distance
- [ ] Acceptance persisted with `accepted_at` and `accepted_terms_version`
- [ ] Name mandatory gate: empty/whitespace blocked, advance only after valid save
- [ ] Persistence across app restart: name, last modality, accepted plans maintained

---
*Plan generated grounded in codebase analysis. WAITING FOR EXPLICIT USER CONFIRMATION before any code is written. Confirm with "yes" or "proceed" to begin implementation.*