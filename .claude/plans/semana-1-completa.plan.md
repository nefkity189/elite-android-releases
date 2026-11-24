# Plan: Elite — Implementação Completa da Semana 1 (D1 a D5)

**Source PRD**: Relatório de Revisão e Decisões (07/11/2026)
**Selected Milestone**: Semana 1 (D1 Higiene, D4 Conclusão por Distância, D5 Motor de Modelos e Plano 2,5K)
**Complexity**: Large

## Summary
Execução completa e perfeita de todos os pontos pendentes da Semana 1 solicitados na revisão de 07/11/2026: (1) Higiene de dependências e restauração das configurações originais de build (D1), (2) Implementação do motor de conclusão de treinos por distância e resiliência de GPS (D4), e (3) Introdução do motor de planos por modelo (`PlanTemplate`) e transcrição fiel do plano profissional de 2,5K em 6 semanas (`Template25K`) com marcação de suposições e testes rigorosos (D5).

## Patterns to Mirror
| Category | Source | Pattern |
|---|---|---|
| Naming | `generator/ProgressionTable.kt` | Objetos puros contendo definições de etapas e estruturas de passos |
| Errors | `tracker/RunLogic.kt` | Funções puras robustas sem dependências de Android Runtime |
| Tests | `app/src/test/` | Testes JUnit com asserções limpas e isolamento total |

## Files to Change / Create
| File | Action | Why |
|---|---|---|
| `app/build.gradle.kts` | UPDATE | Reverter assinatura para release e desativar minify prematuro conforme instrução |
| `app/proguard-rules.pro` | UPDATE | Remover regras obsoletas de Moshi se necessário |
| `app/src/main/java/com/example/tracker/DistanceSchedule.kt` | CREATE | Motor puro de avanço e avaliação de passos por distância (D4) |
| `app/src/main/java/com/example/service/RunTrackingService.kt` | UPDATE | Integrar `DistanceSchedule` e fallback de GPS > 30s para `StepEstimator` |
| `app/src/main/java/com/example/generator/PlanTemplate.kt` | CREATE | Estrutura de modelos de planos profissionais (D5) |
| `app/src/main/java/com/example/generator/Template25K.kt` | CREATE | Transcrição exata do plano 2,5K de 6 semanas com comentários `// ASSUMIDO (profissional)` |
| `app/src/main/java/com/example/generator/PlanGenerator.kt` | UPDATE | Ponto de entrada unificado para suportar modelos de planos sem afetar o 5K existente |
| `app/src/test/java/com/example/DistanceScheduleTest.kt` | CREATE | 12+ cenários de teste para o agendador por distância |
| `app/src/test/java/com/example/Template25KTest.kt` | CREATE | Testes de distribuição de dias, regras anti-consecutivos, semana de prova e regressão do 5K |
| `CHANGELOG-UI.md` | UPDATE | Registro detalhado das entregas |

## Tasks
### Task 1: Higiene de Build (D1)
- **Action**: Restaurar `signingConfig = signingConfigs.getByName("release")` e `isMinifyEnabled = false` / `isShrinkResources = false` em `app/build.gradle.kts`. Remover dependências não utilizadas conforme orientação.
- **Validate**: Compilação bem-sucedida.

### Task 2: Conclusão por Distância (D4)
- **Action**: Criar `DistanceSchedule.kt` para derivar o passo atual e metros restantes a partir da distância acumulada no bloco principal. Integrar em `RunTrackingService` com fallback para `StepEstimator` em caso de perda de GPS > 30s.
- **Validate**: `DistanceScheduleTest` com 12+ cenários passando.

### Task 3: Motor de Modelos e Plano 2,5K (D5)
- **Action**: Criar `PlanTemplate.kt` e `Template25K.kt` transcrevendo as 6 semanas do plano de 2,5K com marcação rigorosa de suposições. Integrar no `PlanGenerator` preservando o 5K original.
- **Validate**: `Template25KTest` validando distribuição de dias (nunca consecutivos), tratamento da semana de prova e testes de regressão do 5K.

## Validation
```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

## Risks
| Risk | Likelihood | Mitigation |
|---|---|---|
| Perda de sinal GPS em treinos por distância | Média | Fallback automático para `StepEstimator` após 30s sem ponto válido |
| Conflito de dias na semana de prova | Baixa | Aplicação da regra padrão que remove o treino pré-prova (ADR-05) |

## Acceptance
- [ ] Todos os testes unitários passando em `app/src/test`.
- [ ] Build de depuração compilando sem erros.
- [ ] Conformidade absoluta com as decisões da revisão de 07/11/2026.
