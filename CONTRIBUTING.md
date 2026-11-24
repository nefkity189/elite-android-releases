# CONTRIBUTING — Primeiro 5K

## Como Contribuir

Obrigado por considerar contribuir! Este projeto segue práticas de engenharia disciplinadas para manter qualidade, segurança e sustentabilidade.

---

## 1. Antes de Começar

### Leia a Documentação Essencial
- [PROJECT_BRIEF.md](PROJECT_BRIEF.md) — Problema, usuários, proposta, escopo
- [REQUIREMENTS.md](REQUIREMENTS.md) — Requisitos funcionais e não funcionais
- [ARCHITECTURE.md](ARCHITECTURE.md) — Camadas, módulos, fluxo de dados
- [DECISIONS.md](DECISIONS.md) — ADRs com decisões arquiteturais
- [TESTING.md](TESTING.md) — Estratégia e comandos de teste
- [SECURITY.md](SECURITY.md) — Riscos e controles de segurança
- [CLAUDE.md](CLAUDE.md) — Instruções para IA (Claude Code)

### Configure o Ambiente
```bash
# Pré-requisitos
- Android Studio Ladybug+ (ou IntelliJ com plugin Android)
- JDK 11+
- Android SDK 37 (compileSdk/targetSdk)

# Clone e abra no Android Studio
git clone <repo-url>
cd AISLE_Proj
# Android Studio → Open → selecione a pasta do projeto

# Build debug
./gradlew assembleDebug
```

---

## 2. Fluxo de Trabalho

### Branches
- `main` — branch estável, apenas releases
- `feature/<nome>` — novas funcionalidades
- `fix/<nome>` — correções de bugs
- `refactor/<nome>` — melhorias internas sem mudança de comportamento

### Commits (Conventional Commits)
```
<tipo>: <descrição curta>

<corpo opcional>

Tipos: feat, fix, refactor, docs, test, chore, perf, ci
```

Exemplos:
```
feat: adiciona ActiveRunScreen com tracking GPS
fix: corrige cálculo de pace no DistanceSchedule
docs: atualiza ARCHITECTURE com M2c decisions
test: adiciona testes unitários para AdaptiveEngine
```

### Pull Requests
1. Branch atualizada com `main` (`git rebase main`)
2. Todos os checks CI passando (build, lint, test)
3. Descrição clara: o que muda, por que, como testar
4. Referência a issue/TASKS.md ID quando aplicável
5. Self-review antes de pedir review

---

## 3. Padrões de Código (Kotlin/Android)

### Estilo
- **Kotlin Official Code Style** (`kotlin.code.style=official` em `gradle.properties`)
- `val` por padrão; `var` só quando mutação necessária
- Sealed interfaces para hierarquias de estado (`UiState`, `AppScreen`)
- `when` exaustivo sem `else`
- Sem `!!` — use `?.`, `?:`, `requireNotNull()`, `checkNotNull()`
- Data classes imutáveis; updates via `.copy()`
- Extensions em arquivos nomeados pelo tipo (`StringExt.kt`, `FlowExt.kt`)

### Arquitetura
- **Camadas**: `ui` → `generator`/`model` → `data.local` → `service`/`worker`/`receiver`
- **MVVM**: `MainViewModel` delega para `WorkspaceManager`
- **Estado**: `StateFlow` no ViewModel/WorkspaceManager; UI coleta com `collectAsStateWithLifecycle()`
- **Persistência**: Room (SQLite) — 4 DBs por modalidade, factory `DatabaseProvider`
- **Motor de plano**: Funções puras Kotlin (`PlanGenerator`, `PlanRegenerator`, `AdaptiveEngine`, `PauseApplier`)

### Testes
- **Mínimo 80% cobertura** (unit + instrumentado)
- **TDD**: Escreva teste primeiro (RED), implemente (GREEN), refatore
- Frameworks: JUnit 4, Robolectric, Turbine, `kotlinx-coroutines-test` (`runTest`)
- Nomes descritivos: `` `nunca dois treinos de corrida em dias seguidos` ``
- Estrutura AAA (Arrange-Act-Assert)

---

## 4. Segurança

- **NUNCA** commite segredos (API keys, passwords, tokens, keystores)
- Use `.env` (gitignored) + Secrets Gradle Plugin para chaves
- Variáveis de release: `KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD` via env
- Valide entrada do usuário nas boundaries
- Room usa queries parametrizadas (proteção SQL injection nativa)
- Sem chamadas de rede no app (on-device first)

---

## 5. Documentação

Atualize docs quando:
- Requisitos mudarem → `REQUIREMENTS.md`
- Interfaces/arquitetura mudarem → `ARCHITECTURE.md`
- Decisões estruturais → `DECISIONS.md` (novo ADR)
- Estratégia de teste → `TESTING.md`
- Riscos de segurança → `SECURITY.md`
- Mudanças visíveis ao usuário → `CHANGELOG-UI.md`

---

## 6. Checklist Pré-Commit

- [ ] `./gradlew assembleDebug` passa
- [ ] `./gradlew :app:testDebugUnitTest` passa
- [ ] `./gradlew :app:lintDebug` sem erros novos
- [ ] Testes relevantes para a mudança executados
- [ ] Cobertura ≥ 80% mantida
- [ ] Docs atualizadas se interfaces/decisões mudaram
- [ ] Sem `console.log` / `println` / debug code
- [ ] Sem segredos no diff (`git diff --staged`)

---

## 7. Relatando Issues

Use templates (quando disponíveis) ou inclua:
- Versão do Android / dispositivo
- Passos para reproduzir
- Comportamento esperado vs. observado
- Logs relevantes (logcat)
- Screenshot se UI

---

## 8. Código de Conduta

- Seja respeitoso e construtivo
- Foque no código, não na pessoa
- Assuma boa intenção
- Privacidade do usuário é prioridade (on-device, sem tracking)

---

## 9. Dúvidas?

- Consulte `CLAUDE.md` para contexto técnico completo
- Abra uma issue com label `question`
- Revise ADRs em `DECISIONS.md` para entender o "porquê"

---

**Lembre-se**: Pequenas mudanças, bem testadas, bem documentadas. Qualidade over velocidade.