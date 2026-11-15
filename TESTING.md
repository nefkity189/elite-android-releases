# TESTING — Primeiro 5K

## 1. Estratégia de Testes

Os testes do aplicativo estão organizados em duas frentes:
1. **Testes Unitários Puros de Domínio (`PlanGeneratorTest.kt`)**: Validam a matemática de progressão, distribuição de datas, regras de nível inicial, compressão e repetição de etapas sem dependências de framework Android.
2. **Testes Robolectric JVM (`ExampleRobolectricTest.kt`)**: Validam a integração com o contexto do Android e recursos de strings sem requerer emulador ou dispositivo físico.

## 2. Cenários Cobertos

### `PlanGeneratorTest`:
- `primeiro treino cai na data de inicio especificada`: Garante que a primeira sessão coincide exatamente com a data selecionada.
- `primeiro treino cai na data de inicio mesmo se data for um dia fora do padrao`: Garante aderência à regra mesmo com início em dias não convencionais.
- `nunca dois treinos de corrida em dias seguidos para qualquer configuracao`: Testa exaustivamente combinações de 2, 3 e 4 dias por semana e durações de 6 a 16 semanas para assegurar espaçamento mínimo de 1 dia de descanso.
- `usuario com condicionamento alto (3 vezes ou mais + cardio) comeca na etapa 2`: Valida a regra de avanço para praticantes de cardio ativo.
- `usuario iniciante comeca na etapa 1`: Valida início conservador para iniciantes.
- `total de treinos gerados eh exatamente diasPorSemana multiplicado por totalSemanas`: Garante consistência na contagem total de treinos.
- `plano mais curto de 6 semanas comprime etapas e atinge a etapa 9`: Valida aceleração gradual até a meta dos 5K.
- `plano mais longo de 16 semanas repete etapas sem regredir`: Valida evolução suave e estritamente monotônica.
- `cada treino contem aquecimento de 5 min e desaquecimento de 5 min`: Garante proteção pré e pós-corrida em todas as sessões.

### `PlanRegeneratorTest`:
- `completed and skipped workouts are preserved and not deleted`
- `nothing before today is altered even without status`
- `resumes at the stage of the last completed workout repeating that stage`
- `resumes at initial stage if no workout has been completed`
- `last workout date does not exceed newEndDate`
- `regenerated workouts fall strictly on chosen days with no consecutive days`
- `respects minimum weeks and rejects invalid short end date`
- `etapa 3 com 2 semanas restantes e rejeitada com mensagem de prazo curto`: Rejeição com cálculo de teto((9 - etapa) / 2) + 1.
- `etapa 3 com 4 semanas e aceita e nenhuma semana avanca mais de 2 etapas`: Valida avanço máximo de 2 etapas por semana até a etapa 9.
- `numeracao das semanas completa semana incompleta antes de abrir a proxima`: Completa a semana 3 antes de abrir a semana 4.
- `canal dos lembretes possui id e nome proprios e nao usa canal de tracking`: Garante canal `lembretes_treino` com `Lembretes de treino`.

### `ClearAllDataTest`:
- `clearAllData removes all rows from profile, plan, workout, workout_log, route_point and plan_adjustment`
- `novo plano preserva lembretes ativos e horario`: Garante que gerar novo plano não reseta lembretes existentes.

### `PauseApplierTest` (Marco M3a):
- `calculo do recuo para 3, 5 e 8 dias nos tres retornos`: Testa combinações de dias e ritmos Gradual, Normal e Rápido.
- `periodos de 3, 7 e 14 dias no PauseApplier preview`: Valida recuo e etapa de retomada para 3, 7 e 14 dias.
- `PauseApplier nos tres niveis - sem_treino, caminhada e reduzido`: Valida status pausado, caminhada de 20 min (um passo só) e recuo de etapa com piso 1.
- `PauseApplier retroativo transforma treinos passados sem status em pausado`: Preserva histórico e remove treinos perdidos no período.
- `PauseApplier preserva treinos concluidos e pulados`: Treinos finalizados ou pulados nunca são sobrescritos.
- `lembrete nao dispara em dia pausado`: `ReminderDecider` retorna nulo para dias com treino pausado.

## 3. Como Executar os Testes

Para executar toda a suíte de testes locais:
```bash
gradle :app:testDebugUnitTest
```
Resultado da última execução: **100% de sucesso (BUILD SUCCESSFUL)**.
