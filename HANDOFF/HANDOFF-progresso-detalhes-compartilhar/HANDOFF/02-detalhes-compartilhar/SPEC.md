# SPEC 02 - Detalhes da Corrida + botão de compartilhar + cards novos

**Leia antes:** `../LEIA-ME.md` (ordem de trabalho), `../THEME.md` (tokens, fontes, ícones).
Protótipo: `screen.html` (2 vistas: Detalhes e Preview de compartilhar). Imagens de referência: `screens/*.png`. Medidas em dp: `spec/*.md`.
A navegação **Progresso -> Detalhes** (a seta) NÃO está aqui: está em `../01-progresso-seta/`. Esta pasta só mexe dentro da tela de Detalhes.

## 0. O que foi pedido (resumo, vale para livre, 2,5K, 5K e 10K)
1. **Botão de compartilhar em Detalhes da Corrida**: no topo da tela, **à direita, na mesma linha do título** "Detalhes da Corrida" (`det.top.share`). Abre o preview do cartão.
2. Nessa tela, **um card com todos os dados** da corrida (`det.data`) e **um card de compartilhamento** (`det.share`: miniatura do cartão + botão "Compartilhar").
3. Funciona igual nas 4 modalidades: o app já guarda cada modalidade em um banco próprio e a tela só recebe o log da corrida, então **não há código por modalidade**; só o texto do chip muda.

## 1. Objetivo detalhado e fluxo
Pedido do usuário, valendo para **todas as modalidades** (Corrida Livre `livre`, `plano_2_5k`, `plano_5k`, `plano_10k`):

1. **Botão de compartilhar na tela "Detalhes da Corrida"**, no topo, à direita, na mesma linha do título (`det.top.share`). Abre o preview do cartão que já existe (`ShareCardPreviewScreen`); lá o botão vermelho "Compartilhar" abre o seletor do Android.
2. Nessa tela, dois cards novos: **"Todos os dados"** (`det.data`) e **"Compartilhar corrida"** (`det.share`, com miniatura do cartão + botão).

Também corrigidos (bugs achados no código, aparecem no seu print):
- Hero mostra "Tempo" grande e "01:45" pequeno: o `MetricBlock` recebe `(value, label)` e a tela chama `MetricBlock("Tempo", timeFormatted)` com os argumentos **trocados**. Usar argumentos nomeados.

**Fluxo completo**
```
(Progresso --(seta)--> Detalhes: ver pasta 01)
Detalhes --(ícone compartilhar no topo | miniatura | botão do card)--> Preview "Compartilhar no Elite"
Preview --(botão vermelho Compartilhar)--> chooser do Android (já existe: requestSharing/startSharing)
Detalhes --(voltar)--> quem abriu (Progresso ou Hoje)      Preview --(voltar)--> Detalhes
```


## 4. Mapa de componentes
Vista 1 = Detalhes (`det.*`), vista 2 = Preview de compartilhamento (`shr.*`). A aba Progresso e a seta que abre estes detalhes estão na pasta `01-progresso-seta/`. `testTag` = o próprio `data-id` quando indicado "tag".

| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `app.detail` | `WorkoutSummaryScreen(isDetailOnly = true)` (tag `screen_workout_summary`) | ui/screens/WorkoutSummaryScreen.kt | não |
| `det.top.back` | `IconButton` + `ArrowBack` | WorkoutSummaryScreen.kt | não |
| `det.top.title` | `Text` `titleMedium` Bold, centralizado | idem | não |
| `det.top.share` | `IconButton` 48 dp + `Icons.Default.Share` 24 dp `Olive` (tag `btn_share_detail_top`) | idem | **sim** (substitui o `Spacer(36.dp)`) |
| `det.hero.*` | `Column` + 2× `MetricBlock` | idem | não (corrigir argumentos) |
| `det.route` | `EliteCard` tag `card_route_canvas` | idem | não |
| `det.data` | `EliteCard` tag `card_all_data` (`RunAllDataCard`) | idem (novo `private @Composable`) | **sim** |
| `det.data.mode` | chip pílula 22 dp | idem | **sim** |
| `det.data.grid` | 3 `Row`s com 2× `MetricBlock(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f))` | idem | **sim** |
| `det.share` | `EliteCard` tag `card_share_run` (`ShareRunCard`) | idem (novo `private @Composable`) | **sim** |
| `det.share.thumb` | `ShareCardThumb` 88x110 dp, clicável (tag `btn_share_thumb`) | idem | **sim** |
| `det.share.cta` | botão pílula contorno (tag `btn_share_card_run`) | idem | **sim** |
| `det.pace` | `EliteCard` tag `card_pace_chart` | idem | não |
| (não desenhados) | `KmSplitsCard` (`card_km_splits`), `TechnicalDetailsCard` | idem | não, ficam onde estão (splits depois do gráfico de ritmo; detalhes técnicos depois das anotações) |
| `det.records` | `EliteCard` tag `card_personal_records` | idem | não |
| `det.notes` | `EliteCard` tag `card_anotacoes_particulares` | idem | não |
| `det.footer` / `det.save` | `Column` fixo + `BrandGradientButton("Salvar alterações")` tag `btn_save_workout_changes` | idem | não |
| `app.share` | `ShareCardPreviewScreen` (tag `share_card_preview_screen`) | ui/screens/ShareCardPreviewScreen.kt | não |
| `shr.back` | `IconButton` + `ArrowBack`, canto superior esquerdo (tag `share_preview_back`) | idem | **sim** (hoje só há `BackHandler`) |
| `shr.card.*` | `ShareCardPreview` (tag `share_card_preview`) | idem | não |
| `shr.cta` | `Button` vermelho (tag `btn_share_card_preview`) | idem | não |


## 5. Regras de layout
**Rolagem: SIM na vista Detalhes** (já é assim hoje: `Column(weight(1f).verticalScroll)` + rodapé fixo). O protótipo tem a região `det.scroll` com 710 dp visíveis (851 - 32 status - 109 rodapé) e rolagem interna. Progresso: a lista é `LazyColumn` (já existe); o protótipo mostra a lista já rolada até "Histórico de Corridas", os cards acima (semana, evolução, marcos) não mudam.

**Detalhes (ordem dos itens na rolagem, `verticalArrangement = spacedBy(12.dp)`, `padding(top = 12.dp)`, `padding(horizontal = 20.dp)` na tela):**
1. `det.top`: `Row(SpaceBetween, CenterVertically)`, altura 48. Esquerda: voltar 48x48. Centro: título. Direita: compartilhar 48x48. Os dois botões têm o mesmo tamanho, então o título fica centrado de verdade (o `Spacer(36.dp)` antigo some).
2. `det.hero`: padding vertical 12. Distância 30/32 `Olive`; abaixo `Row(spacedBy(12.dp))` com Tempo e Ritmo Médio (valor 28 sp, rótulo 12.5 sp).
3. `det.route`: card, padding 12, cabeçalho 16 dp, espaço 12, canvas 220 dp (**sem mudança**: ver Perguntas em aberto sobre a espessura).
4. `det.data` (novo): `EliteCard`, padding 16. Cabeçalho: "Todos os dados" (`titleSmall` Bold, `Text`) à esquerda + chip da modalidade à direita; linha de data `labelSmall` `TextMuted` 2 dp abaixo; espaço 14; grade 2 colunas, 3 linhas, `column gap 16`, `row gap 14` (cada célula: `Row` com 2 `weight(1f)`).
5. `det.share` (novo): `EliteCard`, padding 16, `Row(CenterVertically, spacedBy(16.dp))`: miniatura 88x110 (largura fixa) + `Column(weight(1f))` com título, texto, botão (40 dp de altura, 12 dp acima).
6. `det.pace`, depois `KmSplitsCard`, `det.records`, `det.notes`, `TechnicalDetailsCard` (se houver diagnóstico), `Spacer(16.dp)`.
7. Rodapé fixo (fora da rolagem): divisor 1 dp `LineStrong`, `padding(top = 12.dp, bottom = 40.dp)` + insets de sistema, botão 56 dp de altura, largura total (353 dp).

**Elástico:** só a região de rolagem (`weight(1f)`). Nada mais é elástico. Larguras: cards = largura da tela - 40 (353 dp).
**Telas baixas (< 760 dp):** nada some; a rolagem absorve. Em telas estreitas, o valor do `MetricBlock` tem `maxLines = 1` + `Ellipsis` (já é assim): "5:36 /km" em 28 sp precisa de ~95 dp, cabe em meia coluna (~160 dp) até 320 dp de largura de tela.

**Card "Todos os dados" - dados (todos já existem em `WorkoutLogEntity`/`PaceSeriesCalculator`):**
| Célula | Valor | Fonte | Vazio |
|---|---|---|---|
| Distância | `"%.2f km"` pt-BR, cor `Olive` | `distanciaMetros` | "0,00 km" |
| Tempo total | `mm:ss` ou `hh:mm:ss` (>= 60 min) | `duracaoSegundos` | - |
| Ritmo médio | `PaceCalculator.formatPace(...) + " /km"` | `ritmoMedioSegundos` | `--:--` (null) |
| Ritmo mais rápido | menor `paceSeconds` da série de 30 s, `+ " /km"` | `paceSeries` | `--:--` se a série tem < 2 pontos |
| Esforço (rótulo `"Esforço (Moderado)"`) | `"{n}/10"`, texto de `describeExertion(n)` | `esforcoPercebido` | - |
| Pontos de GPS | `gpsAceitas` com separador de milhar pt-BR | `gpsAceitas` | "0" |
| Chip de modalidade | "Corrida livre" / "Primeiros 2,5K" / "Primeiros 5K" / "Primeiros 10K" | modalidade ativa (`state.selectedModality`: `livre`, `plano_2_5k`, `plano_5k`, `plano_10k`; títulos em `GoalScreen.kt`) | oculta o chip se desconhecida |
| Linha de data | `"EEEE, d 'de' MMMM · HH:mm"` pt-BR, inicial maiúscula | `finalizadoEm` | - |

O card **só aparece quando `isDetailOnly = true`**. No fluxo pós-treino ("Treino Concluído!") continua o `card_summary_metrics` atual (Distância/Tempo/Ritmo), porque ali o esforço ainda não foi gravado. Quando `isDetailOnly = true`, **`card_summary_metrics` é substituído** por `card_all_data` (evita repetir os mesmos 3 números do hero).

**Onde os itens novos aparecem:** o card "Compartilhar corrida" e o ícone de compartilhar do topo aparecem **só quando `isDetailOnly = true`**. No pós-treino ("Treino Concluído!") já existe o botão "Compartilhar no Elite" no rodapé, então não se duplica; ali o topo continua com um `Spacer` de equilíbrio, agora de 48 dp (antes 36 dp) para o título ficar centrado igual nas duas telas.

**Sem GPS (rota com < 2 pontos):** o card de compartilhar continua visível; a miniatura mostra o texto "Sem rota" (mesma regra do `RoutePreview`) e o compartilhamento cai no texto puro (comportamento atual de `startSharing()`).


## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| Detalhes, topo | `detalhes_topo` | tela padrão |
| Detalhes rolado (cards novos) | `detalhes_rolado_cards` | `det.data` + `det.share` visíveis |
| Detalhes rolado até o fim | `detalhes_rolado_fim` | records/anotações, rodapé fixo continua |
| Ícone de compartilhar pressionado | `botao_compartilhar_pressionado` | escala .92 |
| Botão do card pressionado | `cta_compartilhar_pressionado` | escala .97 |
| Preview aberto (pelo topo) | `preview_aberto_pelo_topo` | vista 3 entra |
| Preview aberto (pelo card) | `preview_aberto_pelo_cartao` | idem |
| Preview parado | `preview_compartilhar` | vista 3 |
| Compartilhando (carregando) | **não está no HTML**; ver abaixo | botões desabilitados |
| Sem GPS / vazio | **não está no HTML**; ver abaixo | ver Regras de layout |

Estados só em código (sem quadro no protótipo; implementar conforme a regra):
- **Preparando cartão** (`isSharing = true`): no preview, o botão vermelho mostra "Preparando cartão..." e o spinner 22 dp `Olive` abaixo (já existe). No card `det.share`, botão e miniatura ficam `enabled = false` e o botão em alpha .5 (`Olive.copy(alpha = .5f)`), igual ao `btn_share_workout` atual.
- **Carregando rota** (`routePoints` ainda vazio, `LaunchedEffect` em andamento): hoje o `MainActivity` já passa `emptyList()` até carregar. O card de compartilhar mostra a miniatura "Sem rota" até chegar; **não bloquear** o botão (o preview recalcula com os pontos quando chegam, porque `sharePreviewData` usa `remember(routePoints, ...)`).
- **Erro ao compartilhar:** toast já existente ("Não foi possível compartilhar: ...").


## 7. Motion
Tudo reage a toque ou a navegação; **nenhuma animação em repouso**. Reduzir animação: sem fade/slide e sem escala (a mudança de tela é instantânea).

| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `app.detail` (`detail-enter`) | abrir a partir do Progresso ou do Hoje | opacidade | 0 -> 1 | 200 ms | 0 | cubic-bezier(.2,0,0,1) | não | sem animação |
| `app.share` (`share-enter`) | abrir o preview | opacidade + deslocamento Y | 0 -> 1 e +16 dp -> 0 | 220 ms | 0 | cubic-bezier(.2,.8,.2,1) | não | sem animação |
| `det.top.share`, `shr.back`, `det.top.back` | toque (pressionado) | escala | 1 -> .92 | 100 ms | 0 | ease-out | não | sem escala |
| `det.share.cta` | pressionado | escala | 1 -> .97 | 100 ms | 0 | ease-out | não | sem escala |
| `shr.cta` | pressionado | escala | 1 -> .97 | 100 ms | 0 | ease-out | não | sem escala |

Compose: `detail-enter` = o `Dialog` já faz fade padrão; só garanta que não há `slide`. `share-enter` = trocar o `if (showSharePreview) { ShareCardPreviewScreen(...); return }` por `AnimatedVisibility(visible = showSharePreview, enter = fadeIn(tween(220, easing = CubicBezierEasing(.2f,.8f,.2f,1f))) + slideInVertically(tween(220, easing = CubicBezierEasing(.2f,.8f,.2f,1f))) { 16.dp.roundToPx() }, exit = fadeOut(tween(120)))` num `Box` que cobre a tela. Escala ao toque: `Modifier.graphicsLayer { scaleX = s; scaleY = s }` com `animateFloatAsState(if (pressed) .92f else 1f, tween(100))` e `interactionSource.collectIsPressedAsState()`.
Reduzir animação: ler `Settings.Global.ANIMATOR_DURATION_SCALE == 0f` e trocar os `tween` por `snap()`.


## 9. Mudanças
Ordem de trabalho (compilar após cada bloco).

| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| `ui/components/EliteComponents.kt` `MetricBlock` | assinatura `(value, label, modifier, valueColor, labelColor, horizontalAlignment)` | **sem mudança** | só confirmar; o bug está nas chamadas |
| `WorkoutSummaryScreen.kt` hero (L~314) | `MetricBlock("Tempo", timeFormatted)` e `MetricBlock("Ritmo Médio", paceFormatted)` (valor e rótulo trocados: sai "Tempo" grande e "01:45" pequeno) | `MetricBlock(value = timeFormatted, label = "Tempo")` e `MetricBlock(value = paceFormatted, label = "Ritmo Médio")` | **sempre argumentos nomeados**. Corrige o print do usuário |
| `WorkoutSummaryScreen.kt` barra superior (L~292) | `Row(SpaceBetween)`: voltar, título, `Spacer(36.dp)` | voltar `IconButton`, título, e à direita `IconButton` de compartilhar quando `isDetailOnly`, senão `Spacer(48.dp)`; ícone `Icons.Default.Share` 24 dp `Olive`; tag `btn_share_detail_top`; `onClick = { showSharePreview = true }` | mesma ação do botão "Compartilhar no Elite" do pós-treino |
| `WorkoutSummaryScreen.kt` novo `RunAllDataCard(...)` | não existe | card `card_all_data` conforme tabela da seção 5 | parâmetros: `log` dados (distância, duração, ritmo, esforço, gpsAceitas, finalizadoEm), `paceSeries`, `modalityLabel: String?`. `WorkoutSummaryScreen` hoje **não recebe** `finalizadoEm`, `gpsAceitas` nem modalidade: adicionar parâmetros opcionais `finalizadoEm: Long? = null`, `modalidadeLabel: String? = null` (`gpsAceitas` já vem em `diagnostics.accepted`) |
| `WorkoutSummaryScreen.kt` `card_summary_metrics` (L~395) | sempre exibido | exibido só se `!isDetailOnly` | no detalhe é substituído por `card_all_data` |
| `WorkoutSummaryScreen.kt` novo `ShareRunCard(...)` + `ShareCardThumb(...)` | não existe | card `card_share_run` conforme seção 5; exibir só se `isDetailOnly`, logo após `card_all_data` | miniatura = `ShareCardPreview` real escalada (ver Armadilhas) ou versão simplificada igual ao protótipo |
| `WorkoutSummaryScreen.kt` | `showSharePreview` existe e já abre `ShareCardPreviewScreen` com `sharePreviewData` | reaproveitar tal qual; o ícone do topo, a miniatura e o botão do card só fazem `showSharePreview = true` | **não** criar segundo fluxo de compartilhamento |
| `WorkoutSummaryScreen.kt` `if (showSharePreview) { ...; return }` | troca de tela sem animação | `AnimatedVisibility` conforme Motion | dentro de um `Dialog`, manter o `BackHandler` que já existe no preview |
| `ui/screens/ShareCardPreviewScreen.kt` | sem botão de voltar (só `BackHandler`) | `IconButton` voltar no canto superior esquerdo (`Box` sobreposto, `statusBarsPadding`, `padding(start = 8.dp)`), tag `share_preview_back`, chama `onBack` | não altera o layout do cartão nem do botão vermelho |
| `MainActivity.kt` `selectedDetailLog` (L~417) | passa título, duração, distância, rota, RPE, anotações, diagnóstico | acrescentar `finalizadoEm = log.finalizadoEm` e `modalidadeLabel = <título da modalidade ativa>` | a modalidade vem de `state.selectedModality`; **os logs mostrados são sempre os da modalidade ativa** (cada modalidade tem seu banco: `FreeRunDatabase`, `Plan2_5kDatabase`, `Plan5kDatabase`, `Plan10kDatabase`), então a tela funciona igual nas 4 |
| Testes | `ActiveRunScreenTest`, `ShareCardGeneratorTest` etc. | rodar; adicionar teste de UI simples para `btn_share_detail_top` e `run_card_arrow_*` | opcional, recomendado |


## 10. Armadilhas
- **Argumentos trocados do `MetricBlock`** (causa do "Tempo" gigante no print). Usar argumentos nomeados sempre; o rótulo vai em `label`, o número em `value`.
- **Botão no topo precisa de 48x48 dp** (alvo de toque) e o mesmo tamanho do voltar; se usar 40 dp o título sai do centro. `Row(SpaceBetween)` com 3 filhos: título fica no meio só se os dois botões tiverem a mesma largura.
- **Miniatura do cartão (`det.share.thumb`)**: `ShareCardPreview` limita sua escala a 0.72..1.05, então **não encolhe** para 88 dp sozinha. Duas opções: (a) renderizar `ShareCardPreview` em `Box(Modifier.requiredSize(359.dp, 449.dp).graphicsLayer { scaleX = 88f/359f; scaleY = 110f/449f; transformOrigin = TransformOrigin(0f, 0f) })` dentro de `Box(Modifier.size(88.dp, 110.dp).clip(RoundedCornerShape(12.dp)))`, com `wrapContentSize(unbounded = true)`; ou (b) desenhar a versão simplificada do protótipo (marca, rota, distância, tempo/ritmo). Prefira (a); se o diff passar de 3.0, use (b). Em ambas: `ShareCardPreview` é `private`, torná-la `internal`.
- **Rota na miniatura**: usar `RouteNormalizer.normalize` + `ShareCardGenerator.filterRoutePoints` (como o preview), nunca os pontos crus.
- **Clique duplo**: ícone do topo, miniatura e botão do card disparam a mesma ação; `showSharePreview` é estado simples (`mutableStateOf(false)`), não lista. Ao tocar no ícone com `isSharing = true`, ignorar (`enabled = !isSharing`).
- **`verticalScroll` + `weight(1f)`**: a coluna rolável mantém `weight(1f)`; **não** trocar por `fill = false`. Rodapé fora da rolagem (já é assim).
- **Grade 2 colunas**: cada célula `Modifier.weight(1f)` dentro de `Row`; não usar `LazyVerticalGrid` dentro de `verticalScroll` (crash de altura infinita).
- **Pontos de GPS**: `diagnostics?.accepted` pode ser `0` em corridas antigas (colunas adicionadas na versão 7 do banco); mostrar "0", não esconder.
- **Fontes como arquivos** (`R.font.*`), não `FontFamily.SansSerif`. O `ShareCardGenerator` (bitmap do cartão) já carrega as mesmas fontes via `ShareCardFonts.load` - não mexer.
- **Cores ARGB**: `Color(0xFF1E2314)` e `Color(0xFF1C2015)` são ARGB; `OliveSoft` já é `0x24A3B565`.
- **Raio e espessura do canvas da rota**: o Canvas desenha em **px** (`radius = 9f`, `Stroke(width = 5f)`), por isso a linha é fina no aparelho (~1,8 dp). O protótipo reproduz isso (1,8 dp / raio 3,3 dp). **Não** converter para dp neste pacote.


## 11. Aceitação
- `diff_images.py`: média <= 3.0 e pior célula <= 10.0 em cada estado de `states.json` (inclusive `detalhes_rolado_cards`, `preview_compartilhar`).
- `compare_bounds.py`: todos os `data-id` dentro de ±2 dp.
- **Verificação funcional (obrigatória, o HTML não prova isso):** nas 4 modalidades (`livre`, `plano_2_5k`, `plano_5k`, `plano_10k`) com pelo menos uma corrida com rota e uma sem rota: (1) [coberto na pasta 01] ; (2) o ícone do topo abre o preview; (3) a miniatura e o botão do card abrem o preview; (4) o botão vermelho abre o seletor de compartilhar com a imagem; (5) voltar do preview volta aos Detalhes; voltar dos Detalhes fecha o diálogo (volta a quem abriu); (6) sem rota: o card aparece, a miniatura mostra "Sem rota" e o compartilhamento envia só o texto.
- Hero mostra o número grande (distância), depois `28:41` / `5:36` como **valor** e "Tempo"/"Ritmo Médio" como rótulo pequeno.
- Cada linha de Motion implementada e conferida.
- Desvios aceitos: fontes do protótipo caem em Arial Narrow se não carregarem (a medição usa as fontes do app); o logotipo em `shr.card.mark` é um marcador (vale o drawable real); cards `KmSplitsCard` e `TechnicalDetailsCard` não estão desenhados (existentes, sem mudança).


## 12. Perguntas em aberto
1. O botão de voltar no preview de compartilhamento (`shr.back`) é uma adição minha (hoje só há o gesto de voltar). Se não quiser, remover essa linha da tabela Mudanças e o elemento `shr.back`.
2. O chip de modalidade usa os títulos do `GoalScreen` ("Corrida Livre", "Primeiros 2,5K", ...). Confirmar se quer esses textos ou só "Livre / 2,5K / 5K / 10K".
3. O card "Todos os dados" **substitui** o `card_summary_metrics` apenas nos detalhes (o pós-treino mantém o atual). Se preferir manter os dois, é só não esconder o antigo.
4. Arquivos exatos de `AppScreen`/navegação e o nome do campo da modalidade em `state` não foram abertos por mim; as linhas "L~" são aproximadas (`MainActivity.kt` L357, L417, L751; `ProgressScreen.kt` L114, L155, L839, L932). Conferir pelo nome do composable.
5. A rota no canvas está fina (~1,8 dp) porque é desenhada em px. Se quiser mais grossa, é outro ajuste (não incluído).
6. Não abri o `ProgressScreen` inteiro (só a lista e o card da corrida); se houver outro ponto que abre o `RunDetailDialog`, ele precisa apontar para o mesmo `onViewRunDetail`.

