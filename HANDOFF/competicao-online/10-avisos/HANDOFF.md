# HANDOFF - Avisos e notificações  (v1)

> Parte do pacote **Competição Online** (10 telas). Leia `../LEIAME.md` primeiro: ordem de implementação e componentes compartilhados.

## 1. Objetivo
Catálogo dos avisos do modo: **banners dentro do app** (10 min, 5 min, entrada confirmada, 'Falta 1 competidor para a missão iniciar', 'Missão confirmada. Prepare-se.', 'Missão não iniciada por falta de competidores', conquista global e local, promoção, rebaixamento, fim de temporada), **notificações push** (aparência Android) e **bottom sheets** para promoção de liga, rebaixamento, fim de temporada e conquista. Não é uma tela de navegação: é a especificação dos componentes `WarningBanner`, push e `EventBottomSheet` para uso nas outras telas.

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
| `screen` | AvisosCatalog (apenas prévia; não existe no app) | — | não |
| `avi.b10` ... `avi.bend` | WarningBanner(variant, title, text, cta?) | ui/components/competition/WarningBanner.kt | reuso da Sala |
| `avi.p1` ... `avi.p6` | CompetitionNotifications (NotificationCompat: título + texto, ícone pequeno Bolt, cor Olive) | ui/notifications/CompetitionNotifications.kt | sim |
| `avi.sheet` | EventBottomSheet(kind) | ui/components/competition/EventBottomSheet.kt | sim (base: AisleQuickLauncherBottomSheet) |

## 5. Regras de layout
- Banner: padding 12/14, raio 16, ícone 36dp em círculo, texto `weight(1f)` (título 14sp + legenda 12sp), botão opcional h36. Variantes: Âmbar (10 min, falta 1, fim de temporada), Vermelho (5 min, rebaixamento), Verde (confirmada, promoção), Oliva (entrada), Brass (conquista), Neutro (não iniciada).
- Push: aparência padrão do Android (a prévia imita o heads-up); o app define só título, texto, ícone pequeno e `contentIntent` com deep link para a missão/tela.
- Sheet: raio 26 no topo, grab 40x4, ícone/insígnia 64dp, título t1s centrado, texto body, botão primário h56 (oliva) e secundário h48 (fantasma). Scrim = EliteScrim.
- Rolagem: sim na prévia dos banners; sheets não rolam.
- Banners nas telas (posição): Sala logo abaixo do topbar; Hub acima do seletor; Prova não mostra banners (só push).

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| banners | `eval: __setState('banners')` | Lista de 11 banners. |
| push | click `avi.tabs.p` | 6 notificações. |
| sheet_promocao | `eval: __setState('sheet_promocao')` | Sheet 'Você subiu para Ouro I'. |
| sheet_rebaixamento | `eval: __setState('sheet_rebaixamento')` | Sheet 'Você caiu para Ouro III'. |
| sheet_fim | `eval: __setState('sheet_fim')` | Sheet 'Temporada 1 encerrada'. |
| sheet_conquista | `eval: __setState('sheet_conquista')` | Sheet 'Conquista desbloqueada: Pódio'. |

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `avi.sheet.scrim` | abrir sheet | alpha | 0 -> 1 | 240ms | 0 | ease-out | não (keyframes `fade-in`) | aparece direto |
| `avi.sheet.panel` | abrir sheet | translateY | 100% -> 0 | 320ms | 0 | cubic-bezier(.2,.8,.2,1) | não (keyframes `sheet-up`) | aparece direto |
| `avi.sh-promo.primary` e demais botões | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |

Regra geral: no máximo uma animação em repouso por tela. `AisleMotion` já tem Tap=100/Standard=200/Enter=300; os valores acima (120/180/240/320ms) são os do HTML, use-os.

## 8. Assets
Fontes (res/font, nomes exatos, já existem no app): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`, `saira_stencil_one_regular.ttf`.
Lâmina do Elite: `drawable/ic_elite_blade` (mesmo path de `AisleSymbol`/`drawEliteMark`; polígono oliva + faixa vermelha, viewBox 64).
Insígnia de liga: `drawable/ic_league_<liga>_<n>` (escudo viewBox 24 + `n` chevrons; n = 1 para divisão III, 2 para II, 3 para I; Elite = escudo com a lâmina). Gerar por uma função `LeagueBadge(league, division, size)` em Compose (Path), tingindo o escudo com a cor da liga e os chevrons com `Background`. Path do escudo: `M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z`. Não usar PNG.
Ícones: SVG inline com `data-icon="material:..."`; no app usar os Material Icons indicados (ArrowBack, ChevronRight, Close, Share, Schedule, Groups, Person, EmojiEvents, WorkspacePremium, Flag, Notifications, Bolt, Check, Info, Lock, Star, Place, Link, Add, Download, CalendarMonth, BarChart, DirectionsRun, LocalFireDepartment, CardGiftcard, Refresh, PhotoCamera, MoreHoriz, Warning, Stars). O traçado do SVG é aproximação visual; stroke 2, cantos arredondados, 24dp de caixa.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/components/competition/EventBottomSheet.kt (novo) | — | Sheet com 4 tipos (promoção, rebaixamento, fim de temporada, conquista) | Disparado por evento do servidor |
| ui/notifications/CompetitionNotifications.kt (novo) | Canal de notificação do treino (se existir) | Canal 'Competição online' (importância alta) + 6 modelos | Pedir `POST_NOTIFICATIONS` no Android 13+ |
| ui/components/competition/WarningBanner.kt | (criado na Sala) | Todas as variantes | — |

## 10. Armadilhas
- Push com ação 'Convidar' abre o Share intent via PendingIntent; testar com o app fechado.
- Um banner por vez na tela (fila por prioridade: 5 min > 10 min > falta 1); não empilhar.
- Sheet de promoção e rebaixamento nunca aparecem durante a prova; adiar até o resultado.
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
- Usuário pode silenciar as notificações do modo? Onde (Perfil > Notificações)?
- Lembretes de 10 e 5 minutos também por push com o app fechado? (Assumi que sim.)
- Texto final de fim de temporada e do reset parcial.

## Changelog
- v1: primeira entrega.
