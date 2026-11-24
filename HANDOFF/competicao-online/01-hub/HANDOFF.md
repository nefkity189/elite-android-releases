# HANDOFF - Hub de Competição Online  (v1)

> Parte do pacote **Competição Online** (10 telas). Leia `../LEIAME.md` primeiro: ordem de implementação e componentes compartilhados.

## 1. Objetivo
Tela de entrada do modo Competição Online. Mostra o nome Elite com o lema, o título, atalhos para **Ranking** (com a liga do usuário) e **Conquistas**, o seletor de distância 2,5K | 5K | 10K e a lista de missões por horário, de 02:00 a 21:00, cada uma com status, participantes, "Mínimo 2 competidores", botão **Entrar na missão** e botão de **compartilhar sempre visível**, em qualquer status. Tela nova (não existe hoje no app). O protótipo mostra 7 horários de amostra; no app a lista tem os 20 horários e abre já rolada até a próxima missão.

## 2. Dispositivo
Poco X7, 393 x 851 dp, tema escuro, Jetpack Compose. Fontes do app: Barlow Condensed (números e títulos), Inter (texto), Saira Stencil One (logotipo). O HTML reserva 28dp no topo (barra de status; no app usar `statusBarsPadding()`) e 16dp nas laterais (`AisleSpacing.screenHorizontal`).
Outros aparelhos Android: o layout precisa funcionar de 360 a 412dp de largura e com `fontScale` até 1.3. Regra: larguras fixas só em avatar, ícone e botão de ícone (48dp); todo o resto é `weight`/`fillMaxWidth`; textos de badge, chip e botão usam `maxLines = 1` + `TextOverflow.Ellipsis`; números grandes usam `maxLines = 1` e `softWrap = false`. A barra inferior (dock) usa `navigationBarsPadding()`; o HTML representa isso com 18dp de respiro inferior.

## 3. Tokens
| Variável CSS | Valor | Nome Kotlin |
|---|---|---|
| --bg | #0A0B09 | Background (existe) |
| --surface | #10120D | Surface (existe) |
| --card | #161911 | Card (existe) |
| --card-el | #1D2116 | CardElevated (existe) |
| --field | #0D0F0A | Field (existe) |
| --line | rgba(236,232,218,.10) = 0x1AECE8DA | Line (existe) |
| --line-strong | rgba(236,232,218,.18) = 0x2EECE8DA | LineStrong (existe) |
| --text | #ECE8DA | Text (existe) |
| --muted | #8F9384 | TextMuted (existe) |
| --dim | #5E6255 | TextDim (existe) |
| --olive | #A3B565 | Olive (existe) |
| --olive-dark | #5D6B34 | OliveDark (existe) |
| --olive-soft | rgba(163,181,101,.14) = 0x24A3B565 | OliveSoft (existe) |
| --on-olive | #0E1306 | OnOlive (existe) |
| --red | #D12B2B | Red (existe) |
| --red-soft | rgba(209,43,43,.15) = 0x26D12B2B | RedSoft (existe) |
| --on-red | #FFF4F0 | OnRed (existe) |
| --brass | #C9A24B | Brass (existe) |
| --amber | #E0A93A | Amber (existe) |
| --ok | #7FB069 | Ok (existe) |
| --glass-bar | rgba(16,18,13,.90) = 0xE610120D | GlassBar (existe) |
| --brass-soft | rgba(201,162,75,.16) = 0x29C9A24B | BrassSoft (NOVO) |
| --amber-soft | rgba(224,169,58,.14) = 0x24E0A93A | AmberSoft (NOVO) |
| --ok-soft | rgba(127,176,105,.15) = 0x267FB069 | OkSoft (NOVO) |
| --amber-line | rgba(224,169,58,.45) = 0x73E0A93A | AmberLine (NOVO) |
| --red-line | rgba(209,43,43,.50) = 0x80D12B2B | RedLine (NOVO) |
| --ok-line | rgba(127,176,105,.45) = 0x737FB069 | OkLine (NOVO) |
| --brass-line | rgba(201,162,75,.50) = 0x80C9A24B | BrassLine (NOVO) |
| --scrim | rgba(0,0,0,.62) = 0x9E000000 | EliteScrim (NOVO) |
| --lg-bronze | #B07A4A | EliteLeague.Bronze (NOVO) |
| --lg-prata | #B4BAB8 | EliteLeague.Prata (NOVO) |
| --lg-ouro | #C9A24B | EliteLeague.Ouro (NOVO; mesmo valor de Brass) |
| --lg-platina | #86BDB3 | EliteLeague.Platina (NOVO) |
| --lg-diamante | #7FB4DE | EliteLeague.Diamante (NOVO) |
| --lg-elite | #E7E2D0 | EliteLeague.Elite (NOVO; osso, com a lâmina do Elite dentro) |
| --av1..--av5 | #3B4426 #4A3F1E #2E3B3A #46301F #3A2E3F | EliteAvatar.A1..A5 (NOVO; fundo do avatar de iniciais, escolhido por hash do id) |
| --num / --ui / --logo | Barlow Condensed / Inter / Saira Stencil One | EliteDisplayFont / EliteBodyFont / EliteLogoFont (existem) |

Tipografia usada (px = sp): `t1` 30/32 Barlow 700 caixa-alta ls .9; `t1s` 24/26 Barlow 700 caixa-alta ls .96; `t2` 20/23 Barlow 600 caixa-alta ls .8; `t3` 16/20 Inter 600; `body` 14/19.6 Inter 400; `cap` 12/16 Inter 400 cor TextMuted; `eyebrow` 11/14 Inter 600 caixa-alta ls 1.3; `num` Barlow 700 com `fontFeatureSettings = "tnum"`. Formas: card r18, botão pill r24 (h48) / r28 (h56), chip r15, badge r12, dock r26 no topo.

## 4. Mapa de componentes
| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `screen` | CompetitionHubScreen | ui/screens/competition/CompetitionHubScreen.kt | sim |
| `hub.topbar` | CompetitionTopBar (voltar + AisleLogo + chip de hora) | ui/components/competition/CompetitionTopBar.kt | sim (usa AisleLogo existente) |
| `hub.shortcuts.ranking`, `hub.shortcuts.achievements` | RankingShortcutCard, AchievementsShortcutCard | ui/components/competition/ShortcutCards.kt | sim |
| `hub.distance` | DistanceSegmented | ui/components/competition/DistanceSegmented.kt | sim (reaproveitar visual de abas do app se houver) |
| `hub.list` | LazyColumn de MissionCard | CompetitionHubScreen.kt | sim |
| `hub.m1300` (cartão cheio), `hub.m1100`/`hub.m1200` (compacto), `hub.m1600` (slot vazio) | MissionCard(variant = Full/Compact), MissionEmptySlot | ui/components/competition/MissionCard.kt | sim |
| `hub.m1300.status` | MissionStatusBadge(status) | ui/components/competition/MissionStatusBadge.kt | sim |
| `hub.m1300.join` | ActionButton vermelho (existe)  | ui/components/AisleComponents.kt | não |
| `hub.m1300.share` | ShareIconButton (48dp, sempre visível) | ui/components/competition/ShareIconButton.kt | sim |
| `hub.m1300.row2.stack` | AvatarStack + ParticipantCounter | ui/components/competition/Avatars.kt | sim |
| `hub.empty` | MissionsEmptyState | CompetitionHubScreen.kt | sim |

## 5. Regras de layout
- Coluna raiz: topbar 48dp, título (eyebrow 14 + t1 32), atalhos 60dp (grade 2 colunas, gap 10), seletor 48dp, cabeçalho da lista, lista.
- Elástico: `hub.list` ocupa o espaço restante (`weight(1f)`), mínimo 200dp, sem máximo; é a única área que rola (`LazyColumn`, `contentPadding bottom = 12dp`, `spacedBy(10dp)`). Ao abrir, `animateScrollToItem` até a próxima missão (13:00 no protótipo).
- Cartão cheio: padding 14, linha 1 (hora `m-time` 36sp + chip distância + badge à direita), linha 2 (avatares 28dp sobrepostos -8dp + `1/2` 20sp + 'Mínimo 2 competidores'), linha 3 (botão `weight(1f)` h48 + botão de ícone 48x48 com gap 10). Altura resultante 164dp.
- Cartão compacto (Encerrada, Não iniciada, Em andamento): padding 12/14, hora 28sp + chip, badge + legenda, botão de compartilhar 48dp à direita. Altura 74dp. Compartilhar continua visível; 'Entrar na missão' some nesses status.
- Telas com altura < 760dp: a lista simplesmente mostra menos cards; esconder os atalhos `hub.shortcuts` só abaixo de 680dp.
- O badge 'Aguardando' é a forma curta do status 'Aberta / Aguardando competidores'; o texto completo vai no `contentDescription`.
- Rolagem: sim, só na lista.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| padrao / proxima_missao | `eval: __setState('padrao')` | Lista rolada até 13:00; seletor em 5K. |
| passadas | `eval: __setState('passadas')` | Lista no topo: mostra missões encerradas/não iniciada/em andamento (cards compactos). |
| vazio | `eval: __setState('vazio')` | Depois das 21:00: `hub.empty` no lugar da lista. |
| distancia_10k | click `hub.distance.d10` | Segmento 10K ativo; todos os chips de distância viram 10K. |
| entrar_pressionado | press `hub.m1300.join` | Botão com escala 0.96. |
| (status) | — | Mapeamento status -> badge: Aberta = `b-open` oliva 'Aguardando'; Falta 1 = `b-one` âmbar; Confirmada = `b-conf` verde sólido; Em andamento = `b-live` vermelho com ponto pulsante; Encerrada = `b-done` cinza; Não iniciada = `b-none` contorno + legenda 'por falta de competidores'. |

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `hub.m1200.status.dot` | repouso (status Em andamento) | alpha | 1 -> 0.3 -> 1 | 1400ms | 0 | ease-in-out | infinito (keyframes `live-pulse`) | parado em alpha 1 |
| `hub.distance.d25`/`d5`/`d10` | toque | cor de fundo e do texto | inativo -> ativo | 180ms | 0 | ease-out | não | troca instantânea |
| `hub.shortcuts.ranking`, `hub.shortcuts.achievements` | toque | escala | 1 -> 0.97 | 120ms | 0 | ease-out | não | sem escala |
| `hub.m1300.join` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |
| `hub.m1300.share` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |

Regra geral: no máximo uma animação em repouso por tela. `AisleMotion` já tem Tap=100/Standard=200/Enter=300; os valores acima (120/180/240/320ms) são os do HTML, use-os.

## 8. Assets
Fontes (res/font, nomes exatos, já existem no app): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`, `saira_stencil_one_regular.ttf`.
Lâmina do Elite: `drawable/ic_elite_blade` (mesmo path de `AisleSymbol`/`drawEliteMark`; polígono oliva + faixa vermelha, viewBox 64).
Insígnia de liga: `drawable/ic_league_<liga>_<n>` (escudo viewBox 24 + `n` chevrons; n = 1 para divisão III, 2 para II, 3 para I; Elite = escudo com a lâmina). Gerar por uma função `LeagueBadge(league, division, size)` em Compose (Path), tingindo o escudo com a cor da liga e os chevrons com `Background`. Path do escudo: `M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z`. Não usar PNG.
Ícones: SVG inline com `data-icon="material:..."`; no app usar os Material Icons indicados (ArrowBack, ChevronRight, Close, Share, Schedule, Groups, Person, EmojiEvents, WorkspacePremium, Flag, Notifications, Bolt, Check, Info, Lock, Star, Place, Link, Add, Download, CalendarMonth, BarChart, DirectionsRun, LocalFireDepartment, CardGiftcard, Refresh, PhotoCamera, MoreHoriz, Warning, Stars). O traçado do SVG é aproximação visual; stroke 2, cantos arredondados, 24dp de caixa.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/theme/Color.kt | Sem BrassSoft/AmberSoft/OkSoft/linhas/scrim | Adicionar os tokens NOVOS da tabela | Fazer primeiro, vale para as 10 telas |
| ui/theme/EliteLeague.kt (novo) | — | Cores das 6 ligas + `LeagueBadge` | Compartilhado: Hub, Ranking, Temporada, Resultado, Cards |
| ui/components/competition/Avatars.kt (novo) | — | Avatar de iniciais (cores `EliteAvatar`), AvatarStack, slot vazio tracejado | Compartilhado |
| ui/components/competition/ShareIconButton.kt (novo) | — | Botão de ícone 48x48 oliva-soft, borda OliveDark | Compartilhado: Hub, Sala, Não iniciada |
| ui/components/competition/MissionStatusBadge.kt (novo) | — | Badge por status (enum `MissionStatus`) | Compartilhado: Hub, Sala |
| ui/components/competition/MissionCard.kt (novo) | — | Cartão cheio, compacto e slot vazio | Medir altura 164dp / 74dp |
| ui/state/CompetitionState.kt (novo) | — | `MissionStatus`, `Mission(time, distance, participants, status)`, `HubUiState` | Estados = tabela acima |
| ui/screens/competition/CompetitionHubScreen.kt (novo) | — | Tela completa | Última; ligar navegação (ver Perguntas em aberto) |

## 10. Armadilhas
- `LazyColumn` com cartões de alturas diferentes: não fixar altura; usar `animateScrollToItem` com `LaunchedEffect(missions)` só na primeira composição.
- O contador `1/2` e 'Mínimo 2 competidores' ficam na mesma linha dos avatares: `Row` com `weight` no texto e `maxLines = 1`.
- Badge 'Em andamento' pulsa só em cards visíveis; usar `rememberInfiniteTransition` dentro do item, não no pai (evita recomposição da lista inteira).
- Elástico = `Modifier.weight(1f)` (não `fill = false`); lista rolável dentro de `Column` com `weight(1f)` + `LazyColumn`/`verticalScroll`, nunca altura fixa.
- Gradientes: `Brush.verticalGradient(colorStops = ...)`; os de 135deg/160deg do HTML viram `Brush.linearGradient(colorStops, start, end)` calculado pelo tamanho; pare em transparente do mesmo matiz (OliveSoft → Card), não em `Color.Transparent` puro.
- Cor CSS `rgba(r,g,b,a)` é `Color(0xAARRGGBB)` no Kotlin (alpha primeiro); os valores já estão convertidos na tabela de tokens.
- Alvo de toque mínimo 48dp: botão de compartilhar (ícone) e voltar têm caixa de toque de 48dp mesmo quando o visual é 40dp (`Modifier.minimumInteractiveComponentSize()`).
- Estado vem do ViewModel (sealed class/enum + `StateFlow`), não de arrays soltos nem de `remember` local; `data-state` do HTML = o enum da tela.
- `font-variant-numeric: tabular-nums` = `fontFeatureSettings = "tnum"`; `letter-spacing` em px = `letterSpacing` em sp.
- `text-transform: uppercase` não existe no Compose: aplicar `.uppercase()` no texto (respeitando acentos, ex.: "MISSÃO").
- Fontes como arquivos `.ttf` em `res/font`; não depender de fonte do sistema (o render do HTML sem as fontes engana: meça sempre com `--fonts-dir`).
- Sombra/blur: nenhum `backdrop-filter` usado; o vidro do dock é só `GlassBar`.

## 11. Aceitação
- diff médio <= 3.0 e pior célula <= 10.0 em cada estado de `states.json`.
- `compare_bounds`: todos os `data-id` dentro de ±2dp (dock inferior: ±6dp por causa do inset de navegação real do aparelho).
- Cada linha da tabela Motion implementada e conferida na folha de movimento.
- Teste extra obrigatório: largura 360dp e 412dp, `fontScale` 1.3: nada cortado, nenhum botão abaixo de 48dp de toque, nenhuma sobreposição.
- Desvios aceitos: fontes do render sem os `.ttf` (use `--fonts-dir`); `backdrop-filter` não é usado; avatares de iniciais substituem foto até haver foto real.

## 12. Perguntas em aberto
- Fuso dos horários: horário local do usuário ou Brasília? O HTML mostra o relógio 'Agora 12:42'.
- Onde o modo entra no app (aba, card na Hoje, menu)? Marquei os arquivos de navegação como 'a confirmar'.
- 'Abrir' no slot sem missão (16:00): cria a sala? Se não existir essa ação, remover o botão.
- Depois de entrar, o card muda para 'Você está na sala'? Não desenhei esse estado porque o briefing não pede.
- Quantos competidores no máximo por missão? Afeta o contador `1/2`.

## Changelog
- v1: primeira entrega.
