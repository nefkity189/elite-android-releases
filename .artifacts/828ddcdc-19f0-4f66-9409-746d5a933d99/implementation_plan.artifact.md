# Plano de Implementação: Fase 1 (Semanas 1-2)

## Objetivo
Implementar as funcionalidades principais ("Must-haves"): Corrida Livre, planos de 2,5K e 10K, conclusão por distância, e realizar a higiene das dependências do projeto.

## User Review Required
1. **Build Changes**: Conforme notado, o `build.gradle.kts` sofreu alterações na assinatura e no `minify`. **Não alterei estas configurações** conforme solicitado, mas preciso que você confirme se prefere manter o `debugConfig` em release ou se devemos voltar para a chave original antes de avançar.
2. **Assunções de Distância**: Confirme se está de acordo com:
   - Distância conta apenas o bloco principal (aquecimento/desaquecimento fora).
   - Sem GPS, o app retorna ao guia por tempo estimado.

## Open Questions
- **D3 (Forma de gravar corrida livre)**: Seguirei a Opção A (plano-contêiner sob demanda) se possível, mantendo o esquema de tabelas intacto.

## Proposed Changes

### [Higiene de Dependências (D1)]
- **[MODIFY]** [build.gradle.kts](file:///D:/AISLE/AISLE_Proj/app/build.gradle.kts)
  - Remover dependências não utilizadas (Firebase, Retrofit, Moshi, OkHttp).
  - Manter dependências necessárias (Location, Coroutines).

### [Modelo de Dados (D2)]
- **[MODIFY]** [AppDatabase.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/data/local/AppDatabase.kt) e entidades
  - Migração para versão 9 (adição de campos de modalidade, distância e modo de conclusão).
- **[NEW]** [StepEstimator.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/generator/StepEstimator.kt)

### [Corrida Livre e Voz (D3)]
- **[NEW]** [KmMilestoneTracker.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/tracker/KmMilestoneTracker.kt)

### [Conclusão por Distância (D4)]
- **[NEW]** [DistanceSchedule.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/tracker/DistanceSchedule.kt)

### [Planos e Motor de Modelos (D5)]
- **[NEW]** [PlanTemplate.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/generator/PlanTemplate.kt)
- **[NEW]** [Template25K.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/generator/Template25K.kt)

## Verification Plan

### Automated Tests
- Rodar `assembleDebug` para validar a build após higiene.
- Rodar `testDebugUnitTest` após cada tarefa de lógica (D2-D5).

### Manual Verification
- Testar fluxo de Corrida Livre (pausa/retoma, aviso de km).
- Validar comportamento de um treino de teste com conclusão por distância.
