# SPEC 01 - Aba Progresso: a seta de cada corrida abre os Detalhes da Corrida

**Leia antes:** `../LEIA-ME.md` (ordem de trabalho), `../THEME.md` (tokens, fontes, ícones).
Protótipo: `screen.html` (2 vistas: **Progresso** e, como destino, **Detalhes**). Referência visual: `screens/progresso--ONDE-ESTA.png` (aba e seta contornadas em vermelho), `screens/progresso.png`. Medidas em dp: `spec/*.md`.
O conteúdo da tela de Detalhes (botão compartilhar, card "Todos os dados", card de compartilhar) está em `../02-detalhes-compartilhar/`. **Esta pasta só trata da aba Progresso e de como ela abre os Detalhes.**

## 0. Onde isso fica no app (não confundir)
A barra inferior (`RunnaBottomNav`) tem 5 posições: **Hoje (0) | Plano (1) | botão E (coach) | Progresso (3) | Perfil (4)**. Esta pasta é a **4ª posição, "Progresso"** (`AppScreen.Progress`, tag `screen_progress`), a que o usuário contornou de vermelho no print. É outra tela, em outro menu: **não é o "Hoje"**.

No print do usuário o card "Segunda-feira, 23 de nov. / Corrida Livre" com mini-rota, **seta**, e as 4 métricas (Tempo, Distância, Ritmo, Esforço) é o `CompletedRunCard` do Histórico de Corridas da aba Progresso (`ProgressScreen.kt`).

## 1. O pedido
> Na guia Progresso tem a setinha onde eu deveria abrir os detalhes daquela corrida. Abriria um card com todos os dados e um card também de compartilhamento. Vale para os modos livre, 2,5K, 5K e 10K.

Ou seja: **tocar na seta (ou no card) de uma corrida do Progresso abre a tela "Detalhes da Corrida"** daquela corrida, a mesma tela que o "Hoje" já abre, e que na pasta 02 recebe o botão compartilhar, o card "Todos os dados" e o card "Compartilhar corrida".

## 2. Por que está faltando (diagnóstico no código real)
| Fato | Onde | Consequência |
|---|---|---|
| A seta é só `Icon(ArrowForward, 20.dp, onSurfaceVariant)`, **sem clique** | `ProgressScreen.kt` ~L839, dentro de `CompletedRunCard` | tocar na seta não faz nada por si; só o clique do card inteiro funciona |
| O clique do card chama `selectedLogForDetail = log to workout` e abre **`RunDetailDialog`** ("Métricas da corrida", tag `dialog_run_detail`), um diálogo antigo e diferente | `ProgressScreen.kt` L114-127, L223-229, L932 | a aba Progresso **nunca abre** `WorkoutSummaryScreen`, então nunca mostra o botão compartilhar, "Todos os dados" nem o card de compartilhar |
| O "Hoje" já abre a tela certa: `onViewWorkoutDetail` -> `selectedDetailLog` -> `Dialog { WorkoutSummaryScreen(isDetailOnly = true) }` | `TodayScreen.kt` L643, `MainActivity.kt` L357, L417-452, L631 | é só **reaproveitar esse mesmo caminho** no Progresso |
| `ProgressScreen(...)` não recebe nenhum callback de detalhe | `ProgressScreen.kt` L105-113 e chamada em `MainActivity.kt` L750 | falta o parâmetro `onViewRunDetail` |
| Título "Histórico de Corridas" com `color = Background` (invisível: texto da cor do fundo) | `ProgressScreen.kt` L191-195 | bug a corrigir junto |

Conferido: nenhum teste do projeto referencia `dialog_run_detail` nem `run_card_`; só aparecem em `ProgressScreen.kt` (L773 e L959).

## 3. Fluxo exato
```
[Aba Progresso]  toque na SETA (40x40 dp)  ou  toque em qualquer parte do card
        |
        v   onViewRunDetail(log, workout)   <- novo callback
[MainActivity]   selectedDetailLog = log to workout   (estado que já existe)
        |
        v   Dialog em tela cheia (já existe, L417)
[Detalhes da Corrida]  WorkoutSummaryScreen(isDetailOnly = true)   -> conteúdo: pasta 02
        |
        v   voltar (seta do topo OU botão voltar do sistema) -> selectedDetailLog = null
[Aba Progresso]  volta exatamente onde estava (a LazyColumn não é recriada)
```
- A seta e o card disparam **a mesma ação**, para a **mesma corrida** (a linha tocada).
- Como o diálogo fica sobre a tela (hospedado no `MainActivity`), a aba Progresso continua composta embaixo: **a posição de rolagem da lista é preservada** ao voltar. Não guardar posição manualmente.
- A barra inferior fica coberta enquanto o diálogo está aberto (já é assim no Hoje).
- Funciona igual em **Corrida Livre, 2,5K, 5K e 10K**: a lista mostra os logs da modalidade ativa (cada uma tem seu banco: `FreeRunDatabase`, `Plan2_5kDatabase`, `Plan5kDatabase`, `Plan10kDatabase`). **Não existe código por modalidade.**

## 4. Mapa de componentes (data-id = tag/nome no protótipo)
| data-id | Composable | Arquivo | Muda? |
|---|---|---|---|
| `screen` / `app.progress` | `ProgressScreen` (tag `screen_progress`) | ui/screens/ProgressScreen.kt | sim: novo parâmetro `onViewRunDetail`; remove diálogo antigo |
| `prog.top.title` | título "Progresso" | idem | não |
| `prog.history.title` | `Text("Histórico de Corridas")` | idem ~L191 | **sim: cor** `Background` -> `TextColor` |
| `prog.history.count` | chip "3 corridas" | idem | não |
| `prog.run1` ... `prog.run3` | `CompletedRunCard` (tag `run_card_{log.id}`) | idem ~L741 | clique passa a chamar `onViewRunDetail` |
| `prog.run1.mini` | mini-rota (Canvas 44x28) | idem | não |
| `prog.run1.arrow` | **`IconButton` 40 dp** (tag `run_card_arrow_{log.id}`) | idem ~L839 | **sim (novo)** |
| `prog.run1.arrow.bg` | `Box` 32 dp `CircleShape`, fundo `OliveSoft` | idem | **sim (novo)** |
| `prog.run1.arrow.icon` | `Icon(AutoMirrored.Filled.ArrowForward)` 18 dp, `Olive` | idem | **sim** (era 20 dp cinza) |
| `prog.run1.metrics` | 4 `MetricColumn` | idem | não |
| `prog.nav` / `nav.bar` | `RunnaBottomNav` (selected = 3) | ui/components/RunnaBottomNav.kt | **NÃO mexer** (flutuante 64 dp, raio 24, margens 12/12/10; item selecionado em `Olive`) |
| `nav.coach` | `CoachButton` (E inclinada, animada) | idem | **NÃO mexer** |
| `app.detail` | `WorkoutSummaryScreen(isDetailOnly = true)` | ui/screens/WorkoutSummaryScreen.kt | ver pasta 02 |

O protótipo mostra a lista com 3 corridas. A parte de cima da aba (cards de semana/evolução/marcos, se houver) não foi redesenhada e **não muda**; o protótipo mostra a lista já rolada até "Histórico de Corridas".

## 5. Medidas da seta (do `spec/progresso.md`)
- `IconButton`: **40 x 40 dp** (alvo de toque; o lint avisa < 44 dp: é aceito aqui porque o card inteiro também é clicável e o mesmo toque já funciona em qualquer ponto).
- Círculo interno: **32 x 32 dp**, `OliveSoft` (`0x24A3B565`), centrado no botão (4 dp de folga).
- Ícone: **18 x 18 dp**, `Olive` (`#A3B565`).
- Posição no card: canto direito do cabeçalho, depois da mini-rota (44 x 28 dp) com 8 dp de espaço. No protótipo: botão em x=316, círculo em x=320 (cartão de 353 dp de largura).
- Altura do cabeçalho do card: o botão de 40 dp pode aumentar a linha se o bloco data+título for menor que 40 dp (hoje ~41 dp: não deve mudar; **confirmar na comparação**).
- `contentDescription = "Ver detalhes da corrida"`.
- Alternativa mínima (se preferir **não** mudar o visual): manter o ícone cinza de 20 dp como está e só envolvê-lo em `IconButton(onClick, Modifier.size(40.dp))`. O pedido é só que a seta abra os detalhes; o círculo oliva é sugestão de design (`Pergunta 1`).

## 6. Mudanças no código (na ordem; compilar após cada bloco)
**6.1 `ProgressScreen.kt`: assinatura**
```kotlin
fun ProgressScreen(
    workouts: List<WorkoutEntity> = emptyList(),
    logs: List<WorkoutLogEntity> = emptyList(),
    currentDate: LocalDate = LocalDate.now(),
    onSaveAnotacoes: (logId: Long, anotacoes: String) -> Unit = { _, _ -> }, // fica (sem uso), ver 6.4
    onRequestRoutePoints: suspend (workoutId: Long) -> List<RoutePointEntity> = { emptyList() },
    onViewTodayClick: () -> Unit = {},
    onViewRunDetail: (WorkoutLogEntity, WorkoutEntity?) -> Unit = { _, _ -> }, // NOVO
    modifier: Modifier = Modifier
)
```
**6.2 `ProgressScreen.kt`: clique do card** (hoje `selectedLogForDetail = log to workout`)
```kotlin
CompletedRunCard(
    log = log, workout = workout, onRequestRoutePoints = onRequestRoutePoints,
    onClick = { onViewRunDetail(log, workout) }   // card inteiro e seta usam este mesmo onClick
)
```
**6.3 `ProgressScreen.kt`: a seta** dentro de `CompletedRunCard` (substitui o `Icon` solto, ~L839)
```kotlin
IconButton(
    onClick = onClick,
    modifier = Modifier.size(40.dp).testTag("run_card_arrow_${log.id}")
) {
    Box(
        modifier = Modifier.size(32.dp).background(OliveSoft, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Ver detalhes da corrida",
            tint = Olive,
            modifier = Modifier.size(18.dp)
        )
    }
}
```
Manter o `Spacer(8.dp)` antes dela. Não colocar `clickable` no `Box`/`Icon` (o `IconButton` já trata o toque). O `Modifier.clickable` do card continua no card.
**6.4 `ProgressScreen.kt`: remover o diálogo antigo**
- apagar `var selectedLogForDetail ...` (L114) e o bloco `selectedLogForDetail?.let { ... RunDetailDialog(...) }` (L117-127);
- depois apagar o código que ficar sem uso: `RunDetailDialog` (L932), `RouteCanvasCard`, `DetailMetricBox` (conferir com busca antes);
- **manter** o parâmetro `onSaveAnotacoes` na assinatura (para não quebrar chamadas/testes), mesmo sem uso; as anotações agora são salvas pela tela de Detalhes (`onSaveLog` no `MainActivity`, L443), que já chama `viewModel.saveAnotacoes`.
**6.5 `ProgressScreen.kt`: bug do título** (~L191): `color = Background` -> `color = TextColor` (importar `com.elite.sch.app.ui.theme.Text as TextColor` se já não houver, como em `WorkoutSummaryScreen`).
**6.6 `MainActivity.kt`** (`is AppScreen.Progress`, ~L750): acrescentar
```kotlin
onViewRunDetail = { log, workout -> selectedDetailLog = log to workout }
```
`selectedDetailLog` (L357) e o `Dialog` (L417) **já existem** e já passam título, duração, distância, rota, RPE, anotações e diagnóstico de GPS. Nada mais a criar aqui. (As adições `finalizadoEm` e `modalidadeLabel` ao bloco do `Dialog` pertencem à pasta 02.)

## 7. Estados
| Estado | Como ver | O que muda |
|---|---|---|
| Progresso com corridas | `screens/progresso.png` | tela padrão; seta com círculo oliva |
| Seta pressionada | sem PNG (só escala do botão; ver seção 8) | escala do botão .9 |
| Progresso -> Detalhes | `screens/progresso--abre-detalhes.png` (e no protótipo: tocar na seta) | fade da tela de Detalhes (200 ms) |
| Lista vazia | **não está no HTML**; já existe (`EmptyRunsCard`) | sem corridas = sem seta; não mudar |
| Corrida sem rota | **não está no HTML**; já existe | a mini-rota some (como hoje); a seta e o clique continuam funcionando |
| Reduzir animação | `screens/progresso--reduced-motion.png` | sem escala e sem fade |

## 8. Motion
Nada anima em repouso nesta tela (o botão E da barra é existente e não faz parte desta mudança).
| data-id | Gatilho | Propriedade | De -> Para | Duração | Easing | Reduzir animação |
|---|---|---|---|---|---|---|
| `prog.run{n}.arrow` | pressionado | escala | 1 -> .9 | 100 ms | ease-out | sem escala |
| `app.detail` | abrir (seta ou card) | opacidade | 0 -> 1 | 200 ms | cubic-bezier(.2, 0, 0, 1) | sem animação |
Compose: escala com `interactionSource.collectIsPressedAsState()` + `animateFloatAsState(if (pressed) .9f else 1f, tween(100))` + `Modifier.graphicsLayer { scaleX = s; scaleY = s }`. O `Dialog` já faz fade padrão: garanta que não há slide. Reduzir animação: `Settings.Global.ANIMATOR_DURATION_SCALE == 0f` -> `snap()`.

## 9. Armadilhas
- **Não criar outro `selectedDetailLog`/outro `Dialog` dentro do `ProgressScreen`**: o `Dialog` já é do `MainActivity`; o Progresso só **avisa** (`onViewRunDetail`). Um segundo diálogo duplicaria rota/diagnóstico e quebraria o compartilhar.
- Passar o **`workout` do mesmo log** (`workouts.find { it.id == log.workoutId }`, já calculado no `items`), não o primeiro da lista.
- `IconButton` dentro de um card `clickable`: o toque na seta é consumido pelo botão; o `onClick` é o mesmo, então não há ação dupla. Não usar `combinedClickable`.
- Manter as `testTag` `run_card_{id}` (card) e `run_card_arrow_{id}` (seta) para teste de UI.
- `OliveSoft` e `Olive` vêm de `ui/theme/Color.kt`; **não** usar `MaterialTheme.colorScheme.primary` (muda de cor).
- Não alterar `RunnaBottomNav` nem `CoachButton`: no protótipo são só referência de posição (o E gira e "respira" no app real).
- Fontes e cores: ver `../THEME.md`. Meça com as fontes reais (`--fonts-dir app/src/main/res/font`).

## 10. Aceitação
- `diff_images.py` (ou `compare.py` da skill): `progresso` e `progresso--abre-detalhes` com média <= 3.0 e pior célula <= 10.0; `compare_bounds.py` com os `data-id` em ±2 dp.
- **Verificação funcional (obrigatória; o HTML não prova isso)**, nas 4 modalidades (`livre`, `plano_2_5k`, `plano_5k`, `plano_10k`), com pelo menos uma corrida com rota e uma sem:
  1. Aba **Progresso** -> tocar na **seta** da 1ª corrida: abre **Detalhes da Corrida** (não mais "Métricas da corrida").
  2. Tocar em **outro ponto do card**: abre a mesma tela, da mesma corrida.
  3. Tocar na seta da 2ª e da 3ª corrida: cada uma abre os dados da **própria** corrida (distância/tempo conferem com o card).
  4. Voltar (seta do topo e botão do sistema): fecha os Detalhes e a lista do Progresso está **na mesma posição**.
  5. Em Detalhes aparecem o ícone compartilhar no topo, "Todos os dados" e "Compartilhar corrida" (pasta 02).
  6. "Histórico de Corridas" está legível (cor osso).
- Desvios aceitos: fontes do protótipo caem em Arial Narrow se não carregarem; o E da barra é um desenho aproximado (vale o real); o protótipo só mostra 3 corridas.

## 11. Perguntas em aberto
1. Manter o **círculo oliva** atrás da seta (sugestão visual) ou só tornar o ícone atual clicável (alternativa mínima, seção 5)?
2. Se existir outro ponto do app que abre o `RunDetailDialog` além de `CompletedRunCard`, ele deve apontar para o mesmo `onViewRunDetail` (busquei só em `ProgressScreen.kt`: o único uso é o do clique do card).
3. As linhas "L~" foram lidas do `src.zip` enviado; se o arquivo mudou, localizar pelo nome do composable.
