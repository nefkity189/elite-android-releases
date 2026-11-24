# PLANO DE CLONAGEM: RUNMASTER 4.39 → MEU APP

> Salvar na raiz do projeto. Ler este arquivo ANTES de qualquer ação, em toda sessão nova.

## 1. Objetivo e regras do jogo

- **Propósito:** exercício educativo meu para medir a capacidade do Claude de imitar um app e chegar a **mais de 90% de semelhança**.
- **Referência (fonte da verdade):** `D:\REPLICADOR\Runmaster 4.39.apk`, instalado no emulador.
- **SEM SERVIDOR:** o clone não faz nenhuma chamada de rede ao backend original. Tudo roda local: dados fictícios (seed), Room/DataStore, GPS simulado quando necessário. Telas de login, sincronização, ranking e pagamentos viram versões locais que imitam o comportamento visível.
- **Código 100% novo:** Kotlin + Jetpack Compose escrito do zero. Não copiar código decompilado, assets protegidos, logos, fontes licenciadas nem textos literais. Replicar comportamento, layout e fluxo com implementação e assets próprios.
- **Autonomia:** ao concluir uma etapa, avançar para a próxima SEM pedir autorização. Só parar se houver bloqueio real depois de 3 tentativas diferentes.
- **Nunca avançar com bug conhecido.** Testou, falhou, corrigiu, retestou, só então segue.

## 2. Meta de semelhança (como medir os "90%")

Score final = **60% paridade de features + 25% paridade visual + 15% paridade de comportamento**

| Componente | Como medir | Ferramenta |
|---|---|---|
| Features (60%) | Itens da MATRIZ.md concluídos e testados, ponderados: must=3, should=2, could=1 | replica-diff |
| Visual (25%) | Screenshot de cada tela do clone vs. original, comparando layout/estrutura (ignorando cor exata) | replica-diff + Maestro screenshots |
| Comportamento (15%) | Fluxos Maestro idênticos rodando nos dois apps com o mesmo resultado observável | Maestro MCP |

**Meta:** ≥ 90% no score final. Registrar o score a cada fim de etapa em `PROGRESSO.md`.

## 3. Skills: o que cada uma faz e quando usar

> Os nomes abaixo são as skills disponíveis. Se alguma não existir no Claude Code, rodar `/skills` e usar a equivalente mais próxima.

### Núcleo (usar sempre)

| Skill | O que faz | Quando usar |
|---|---|---|
| **reverse-engineer-anything (REA)** | Analisa o APK (estrutura, recursos, telas, permissões, serviços) e explica como cada feature é montada | Etapa 1 e sempre que houver dúvida sobre como algo funciona no original |
| **replica-recon** | Gera o mapa do app: inventário de telas, fluxos, componentes, modelo de dados inferido e matriz de features | Etapa 1 |
| **replica-architect** | Define stack, schema do banco local, camadas e a ordem de construção (fatia vertical primeiro) | Etapa 2 |
| **replica-design** | Extrai e recria o design system: cores, tipografia, espaçamento, raio, sombras e componentes com estados | Etapa 3 |
| **replica-build** | Constrói tela por tela, com todos os estados, marcando a matriz conforme avança | Etapas 4 em diante |
| **replica-test** | Cria plano de testes a partir dos fluxos, com casos felizes e de borda, e relatórios de bug padronizados | A cada etapa |
| **replica-diff** | Calcula o score de paridade e compara screenshots (layout) entre original e clone | Fim de cada etapa e etapa final |
| **Maestro MCP (/maestro-smart)** | Executa fluxos no emulador, tira screenshots e valida cada tela | Toda etapa, obrigatório |

### Apoio (usar quando fizer sentido)

| Skill | O que faz | Quando usar |
|---|---|---|
| **replica-backend** | Normalmente liga auth/pagamentos/servidor. **Aqui usar só no modo local**: Room, DataStore, repositórios com dados fictícios, sem rede | Etapas de dados e login simulado |
| **ui-ux-pro-max** | Referência de UI/UX: acessibilidade, responsividade, tipografia, interação | Refinar telas e estados |
| **frontend-design** | Escolhas visuais intencionais e consistentes | Polimento visual |
| **design-system** | Arquitetura de tokens (primitivo → semântico → componente) | Organizar o tema Compose |
| **gradle** | Ajuda com configuração e problemas de build | Quando o build quebrar |
| **ponytail (+ review/audit)** | Modo "mudança mínima": evita over-engineering, revisa a qualidade do código e lista dívidas técnicas | Revisão ao fim de cada etapa |
| **skill-creator** | Cria uma skill própria com o que foi aprendido | Opcional, ao final: transformar o processo em skill reutilizável |

### Fora de escopo (não usar neste exercício)

`replica-entrepreneur`, `replica-brand`, `replica-launch`, `replica-deploy`: servem para posicionar, nomear, lançar e publicar um clone comercial. Aqui o objetivo é só educativo.

### Plano ECC

Usar o modo de planejamento do Claude (plan/ECC) no início de cada etapa para escrever o plano da etapa (arquivos, telas, critério de aceite) ANTES de codar.

## 4. Sistema de memória (para não se perder)

Criar estes arquivos na raiz do projeto e **atualizá-los ao fim de cada etapa**:

| Arquivo | Conteúdo | Quando atualizar |
|---|---|---|
| `CLAUDE.md` (do projeto) | Resumo fixo: objetivo, regras (sem servidor, código novo, autonomia), caminhos, comandos de build/teste, link para este plano | Uma vez, e ajustar se as regras mudarem |
| `PROGRESSO.md` | Diário: etapa atual, o que foi feito, resultado dos testes, bugs achados/corrigidos, score, **próximo passo exato** | Fim de cada etapa (e a cada checkpoint) |
| `MATRIZ.md` | Todas as features/telas com prioridade e status: pendente / feito / testado | Sempre que um item muda |
| `RECON.md` | Mapa do original: telas S01..Sn, fluxos F01..Fn, componentes, modelo de dados | Etapa 1; ajustar se descobrir algo novo |
| `DECISOES.md` | Decisões de arquitetura e o porquê (ex.: "histórico em Room, GPS simulado por rota fixa") | Quando tomar uma decisão relevante |
| `LICOES.md` | Erros que ocorreram e como evitar (ex.: "seletor Maestro X falha em Compose, usar testTag") | Sempre que resolver um problema que pode se repetir |
| `maestro/` | Um fluxo `.yaml` por tela/fluxo, nomeado com o ID (ex.: `F03_iniciar_corrida.yaml`) | Etapa a etapa |
| `screenshots/original/` e `screenshots/clone/` | Evidência visual por tela, mesmo nome nas duas pastas | Etapa a etapa |

### Protocolo de checkpoint

1. **Início de toda sessão/contexto novo:** ler `CLAUDE.md`, `PROGRESSO.md` (última entrada), `MATRIZ.md` e `LICOES.md`. Continuar do "próximo passo exato", sem recomeçar nada.
2. **Fim de cada etapa:** atualizar `PROGRESSO.md`, `MATRIZ.md`, `LICOES.md` e fazer `git commit` (`etapa N: descrição, score X%`).
3. **A cada ~10 ações grandes dentro de uma etapa longa:** checkpoint rápido em `PROGRESSO.md` ("estou em X, falta Y").
4. **Se o contexto estiver ficando cheio:** fazer checkpoint completo ANTES de continuar, e usar `/compact` ou nova sessão, retomando pelo item 1.
5. Também salvar na memória do Claude Code (`#` ou `/memory`) os fatos-chave: caminho do APK, comando de build, como rodar a suíte Maestro, e a etapa atual.

## 5. Etapas

Cada etapa segue o **ciclo padrão** (seção 6). Cada uma tem critério de aceite próprio; só avança quando cumprido.

### Etapa 0: Preparação
- Ler o repertório atual inteiro e o `CLAUDE.md` global (regras de Maestro).
- Criar `CLAUDE.md` do projeto e os arquivos de memória da seção 4 (vazios com cabeçalho).
- Subir emulador, confirmar Maestro MCP, instalar o APK original (e o clone em outro emulador, se possível).
- **Aceite:** Maestro abre o app original e tira um screenshot.

### Etapa 1: Reconhecimento (REA + replica-recon)
- Analisar o APK com REA: telas, navegação, permissões, serviços em background, sensores.
- Navegar o original pelo Maestro e capturar screenshot de TODA tela e estado (vazio, carregando, erro, sucesso).
- Produzir `RECON.md` e `MATRIZ.md` completos.
- **Aceite:** toda tela alcançável do original está inventariada com screenshot.

### Etapa 2: Plano e arquitetura (ECC plan + replica-architect)
- Definir arquitetura (ex.: MVVM, Room, DataStore, Navigation Compose), schema local, estratégia de dados fictícios e de GPS simulado.
- Quebrar o trabalho em etapas 4.x pequenas, cada uma com fluxo Maestro de validação. Registrar em `DECISOES.md`.
- **Aceite:** plano com etapas ordenadas e critério de aceite por etapa.

### Etapa 3: Design system (replica-design + design-system)
- Extrair tokens dos screenshots, implementar o tema Compose, criar os componentes base com todos os estados.
- **Aceite:** uma tela de "catálogo de componentes" lado a lado com o original, visualmente equivalente.

### Etapa 4.x: Construção por fatias (replica-build + replica-backend local)
Ordem sugerida (ajustar ao RECON):
1. **4.1 Casca do app:** navegação, barra inferior/menus, tela inicial vazia.
2. **4.2 Fluxo principal (fatia vertical):** iniciar → correr (dados simulados) → pausar → finalizar → resumo salvo no histórico.
3. **4.3 Histórico e detalhes** da corrida.
4. **4.4 Estatísticas/gráficos.**
5. **4.5 Perfil e configurações.**
6. **4.6 Onboarding, permissões e login simulado** (sem servidor).
7. **4.7 Demais telas** do inventário, em ordem de prioridade da matriz.
- **Aceite de cada uma:** fluxo Maestro passando no clone, igual ao do original.

### Etapa 5: Polimento e casos de borda (replica-test + ui-ux-pro-max)
- Testar sem permissão, rotação, voltar, app em background, dados vazios, textos longos.
- Corrigir diferenças de espaçamento, animações e transições.
- **Aceite:** zero bugs abertos em `PROGRESSO.md`.

### Etapa 6: Verificação de paridade (replica-diff)
- Rodar a suíte Maestro completa nos dois apps; comparar screenshots tela a tela.
- Calcular o score (seção 2). Se < 90%, listar o que falta em ordem de peso e voltar à Etapa 4/5.
- **Aceite:** score ≥ 90% e suíte de regressão toda verde.

### Etapa 7: Revisão e relatório final (ponytail-review + skill-creator opcional)
- Revisar o código (qualidade, dívida técnica), gerar build de release e instalar.
- Relatório final `RELATORIO_FINAL.md`: score por componente, telas/fluxos cobertos, diferenças residuais justificadas, lições aprendidas e **o que o Claude acertou/errou ao imitar** (é o objetivo educativo do exercício).

## 6. Ciclo padrão de cada etapa

1. **Planejar** (ECC plan): arquivos, telas e critério de aceite da etapa.
2. **Implementar** com a skill da etapa, escrevendo código novo.
3. **Compilar e instalar** no emulador (corrigir erros de build antes de seguir).
4. **Testar no Maestro:** escrever/atualizar o fluxo `.yaml` da etapa e rodar.
5. **Comparar** screenshot do clone vs. original (layout, textos, navegação, comportamento).
6. **Casos de borda** relevantes à etapa.
7. **Bug ou diferença?** Corrigir e voltar ao passo 3. Repetir até passar limpo.
8. **Registrar:** `PROGRESSO.md`, `MATRIZ.md`, `LICOES.md`, score parcial, `git commit`.
9. **Avançar** para a próxima etapa imediatamente, sem pedir autorização.

## 7. Critério de conclusão

Declarar "concluído" somente quando TODAS forem verdadeiras:
- Score de semelhança ≥ 90% (meta; continuar se for possível chegar a 100%).
- Suíte Maestro completa passando sem falhas.
- Zero bugs abertos.
- Todos os arquivos de memória atualizados e `RELATORIO_FINAL.md` entregue.

## 8. Prompt de retomada (colar se a sessão reiniciar)

```
Leia PLANO_CLONE_RUNMASTER.md, CLAUDE.md, PROGRESSO.md (última entrada), MATRIZ.md e LICOES.md.
Continue do "próximo passo exato", seguindo o ciclo padrão, sem pedir autorização entre etapas,
até o score de semelhança chegar a pelo menos 90% e a suíte Maestro completa estar verde.
```

 Temos novas skills instaladas. Para polimento de UI e animação neste app Android com Jetpack Compose, use emil-design-eng, apple-design, find-animation-opportunities e improve-animations. Aplique os princípios, mas implemente tudo em Compose, não em web. Comece rodando find-animation-opportunities no projeto.
