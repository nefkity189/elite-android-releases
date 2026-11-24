# HANDOFF - Menu Adicionar corrida + acesso Premium à Competição Online  (v1)

> Parte do pacote **Competição Online** (10 telas). Leia `../LEIAME.md` primeiro: ordem de implementação e componentes compartilhados.

## 1. Objetivo
Inclui no bottom sheet **Adicionar corrida** (`AisleQuickLauncherBottomSheet`) um quinto item, **embaixo** dos 4 tiles atuais (Corrida livre, Treino 2.5k, Treino 5k, Treino 10k): o card **Competição online**, largura total, 112dp, com visual de recurso **Premium** (gradiente dourado escuro, borda Brass 1,5dp, brilho que atravessa o card, etiqueta vermelha 'NOVO', selo 'PREMIUM' com coroa, troféu em círculo dourado, chips 'Toda hora, 02h–21h' e 'Ranking e ligas'). Dois estados: **bloqueado** (usuário sem premium: cadeado; toque abre a tela de assinatura) e **ativo** (usuário premium: seta; toque abre o Hub de Competição Online). Os 4 tiles e o cabeçalho ficam idênticos ao que já existe; o fundo da Hoje no protótipo é só contexto.

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
Tokens extras desta tela (NOVOS): `--prem-a` #3A2E12 `PremiumGradientStart`; `--prem-b` #1A1608 `PremiumGradientEnd`; `--prem-glow` rgba(201,162,75,.35) = 0x59C9A24B `PremiumGlow`; `--bg-clear` rgba(26,22,8,0) = 0x001A1608 (transparente do mesmo matiz, para gradientes); `--sheen` rgba(255,244,214,.22) = 0x38FFF4D6 `PremiumSheen`.
Os 4 tiles existentes usam as cores atuais do app (Corrida livre = Olive, Treino 2.5k e 10k = Brass, Treino 5k = Red) e texto branco; não mudam.

## 4. Mapa de componentes
| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `screen` | (contexto) TodayScreen + sheet aberto | ui/screens/TodayScreen.kt | não |
| `menu` | AisleQuickLauncherBottomSheet | ui/components/AisleQuickLauncherBottomSheet.kt | não (alterar) |
| `menu.free`, `menu.t25`, `menu.t5`, `menu.t10` | MenuCard (existe, sem mudança) | ui/components/AisleQuickLauncherBottomSheet.kt | não |
| `menu.prem` | PremiumCompetitionCard(isPremium, onClick) | ui/components/competition/PremiumCompetitionCard.kt | sim |
| `menu.prem.sheen` | brilho animado (`drawWithContent`) | PremiumCompetitionCard.kt | sim |
| `menu.prem.new` | tag 'NOVO' (removível por remote config) | PremiumCompetitionCard.kt | sim |

## 5. Regras de layout
- Sheet: `ModalBottomSheet` existente (containerColor Card, cantos 28dp no topo); coluna com padding horizontal 20dp, `spacedBy(14dp)`, bottom 28dp + `navigationBarsPadding()`. O HTML usa 52dp embaixo (28 + ~24 de navegação).
- O novo card entra como **3º filho** da `Column`, depois da linha 2 (Treino 5k | Treino 10k), com `fillMaxWidth()` e `height(112.dp)`. O sheet cresce 126dp (112 + 14 de espaçamento); `ModalBottomSheet` já acomoda a altura.
- Card: Box com cantos 16dp, borda `BorderStroke(1.5.dp, Brass)`, fundo `Brush.linearGradient(PremiumGradientStart -> PremiumGradientEnd)`. Conteúdo com padding 14: linha 1 (troféu 52dp em círculo Brass + coluna com selo e título `weight(1f)` + botão de 34dp), linha 2 (chips h22, gap 6).
- Título 'Competição online' em Barlow Condensed 22sp/24 caixa-alta (`maxLines = 1`); com `fontScale` 1.3 usar `autoSize` ou cair para 18sp. Chips: se faltar largura, o 2º chip some (não quebra).
- Tag 'NOVO' h18 sobre a borda superior, 16dp da esquerda, -9dp de deslocamento (`offset(y = (-9).dp)`), fora do clip do brilho.
- Brilho (sheen) fica **dentro** do clip do card (`clip(RoundedCornerShape(16.dp))`) e atrás do conteúdo.
- Telas baixas (< 700dp): o sheet pode rolar (`verticalScroll`) com o cabeçalho fixo; o card premium nunca é escondido.
- Rolagem: não no Poco X7.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| premium_bloqueado | `eval: __setState('premium_bloqueado')` | Cadeado no canto direito; toque -> tela de assinatura. |
| premium_ativo | `eval: __setState('premium_ativo')` | Seta no lugar do cadeado; toque -> Hub de Competição Online. |
| card_pressionado | press `menu.prem` | Escala 0.98. |
| brilho_meio | `eval` + frames 600/1200/1800/2400 | Brilho em 4 posições da varredura. |
| abertura_menu | `eval` + frames 0/120/320/600 | Sheet subindo (animação padrão do ModalBottomSheet). |

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `menu.prem.sheen` | repouso, enquanto o sheet estiver aberto | translateX (faixa 70dp, inclinada -20deg) | -120dp -> 420dp | 3600ms (varre em 45% = 1620ms, depois pausa) | 600ms | ease-in-out | infinito (keyframes `prem-sheen`) | brilho oculto |
| `menu.prem` | toque | escala | 1 -> 0.98 | 120ms | 0 | ease-out | não | sem escala |
| `menu.free`, `menu.t25`, `menu.t5`, `menu.t10` | toque | escala | 1 -> 0.97 | 120ms | 0 | ease-out | não | sem escala |
| `menu` | abrir o sheet | translateY | 100% -> 0 | 320ms | 0 | cubic-bezier(.2,.8,.2,1) | não (keyframes `sheet-up`; no app vale a animação nativa do ModalBottomSheet) | aparece direto |
| `menu.prem.new`, `menu.prem.glow` | — | estáticos | - | - | - | - | não | - |

Regra geral: no máximo uma animação em repouso por tela. `AisleMotion` já tem Tap=100/Standard=200/Enter=300; os valores acima (120/180/240/320ms) são os do HTML, use-os.

## 8. Assets
Fontes (res/font, nomes exatos, já existem no app): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`, `saira_stencil_one_regular.ttf`.
Lâmina do Elite: `drawable/ic_elite_blade` (mesmo path de `AisleSymbol`/`drawEliteMark`; polígono oliva + faixa vermelha, viewBox 64).
Insígnia de liga: `drawable/ic_league_<liga>_<n>` (escudo viewBox 24 + `n` chevrons; n = 1 para divisão III, 2 para II, 3 para I; Elite = escudo com a lâmina). Gerar por uma função `LeagueBadge(league, division, size)` em Compose (Path), tingindo o escudo com a cor da liga e os chevrons com `Background`. Path do escudo: `M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z`. Não usar PNG.
Ícones: SVG inline com `data-icon="material:..."`; no app usar os Material Icons indicados (ArrowBack, ChevronRight, Close, Share, Schedule, Groups, Person, EmojiEvents, WorkspacePremium, Flag, Notifications, Bolt, Check, Info, Lock, Star, Place, Link, Add, Download, CalendarMonth, BarChart, DirectionsRun, LocalFireDepartment, CardGiftcard, Refresh, PhotoCamera, MoreHoriz, Warning, Stars). O traçado do SVG é aproximação visual; stroke 2, cantos arredondados, 24dp de caixa.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/theme/Color.kt | Sem tokens premium | Adicionar `PremiumGradientStart/End`, `PremiumGlow`, `PremiumSheen`, `BrassSoft` | Tokens da tabela; BrassSoft é o mesmo das outras telas |
| ui/components/competition/PremiumCompetitionCard.kt (novo) | — | Card 112dp com gradiente, borda Brass, brilho animado, selo, chips, tag NOVO, estado `isPremium` | Brilho: `rememberInfiniteTransition` + `drawWithContent` (faixa inclinada); respeitar `reduceMotion` |
| ui/components/AisleQuickLauncherBottomSheet.kt | Grade 2x2 de MenuCard | Adicionar o `PremiumCompetitionCard` como 3º item da Column, abaixo da linha 2; novos parâmetros `isPremium: Boolean`, `onOpenCompetition: () -> Unit`, `onOpenPaywall: () -> Unit` | `testTag("btn_menu_competicao")`; ao tocar: `onDismiss()` e depois a ação conforme `isPremium` |
| ui/screens/TodayScreen.kt (ou quem chama o sheet) | Chama o sheet sem premium | Passar `isPremium` do estado de assinatura e as duas ações | Depende do módulo de assinatura (ver Perguntas em aberto) |

## 10. Armadilhas
- O card é `Box` com `clip` antes do `drawWithContent` do brilho; a tag 'NOVO' fica **fora** do clip (usar `Box` externo sem clip e `offset`).
- Brilho: faixa de 70dp inclinada -20deg = `rotate`/`skew` via `graphicsLayer` (sem skew nativo: usar `drawWithContent` com `withTransform { }` ou um `Path` de paralelogramo). Animar só `translationX` com `animateFloat`.
- Gradiente do brilho e do glow termina em `--bg-clear` (mesmo matiz com alpha 0), não em `Color.Transparent`, para não 'sujar' o meio do gradiente.
- Glow dourado é `Brush.radialGradient` de 200dp no canto superior direito, cortado pelo clip; não usar `Modifier.shadow` (borra diferente e vaza).
- Os tiles existentes usam texto branco puro; não migrar para `OnRed` neste pacote (a diferença é invisível, mas mantenha o código atual).
- Não renderizar o brilho com o sheet fechado/animando para baixo (gasta bateria): rodar a transição só com `sheetState.isVisible`.
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
- Como o app sabe se o usuário é premium (Play Billing, backend)? Preciso do nome do estado/repositório.
- A tela de assinatura (paywall) existe? Não desenhei; posso desenhar se quiser.
- A tag 'NOVO' deve sumir depois de quantos dias ou após o primeiro toque?
- Usuário bloqueado vê o Hub em modo prévia (só leitura) ou vai direto ao paywall?

## Changelog
- v1: primeira entrega.
