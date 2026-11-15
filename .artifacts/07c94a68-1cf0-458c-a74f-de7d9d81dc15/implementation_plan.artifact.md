# Reestruturação da TodayScreen

O objetivo é refatorar a `TodayScreen.kt` para melhorar a manutenibilidade, dividindo-a em componentes menores e aplicando o design solicitado, mantendo a funcionalidade existente.

## User Review Required

Nenhuma mudança de comportamento. Apenas refatoração da UI.

## Proposed Changes

### [UI Components]

#### [MODIFY] [TodayScreen.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/ui/screens/TodayScreen.kt)

- Refatoração total para dividir `TodayScreen` em sub-componentes privados:
  - `TodayHeader` (Cabeçalho + WeekStrip)
  - `TodayHeroCard` (Treino de hoje)
  - `TodayRestCard` (Dia de descanso)
  - `TodayWeeklyGoalSection` (Metas semanais)
  - `TodayRecentRunsSection` (Corridas recentes)
  - `TodayPausaBanner` / `TodayRecoveryCard` / `TodaySuggestionSection`

## Verification Plan

### Automated Tests
- Compilar o app (`gradle_build("app:assembleDebug")`).
- Verificar se todos os `testTag` exigidos continuam presentes.

### Manual Verification
- Navegar na tela `Hoje` em diferentes estados (dia de treino, descanso, corrida concluída, corrida pausada/recuperável).
- Verificar se a hierarquia visual segue as especificações (Hero no topo, lista plana, divisores).
