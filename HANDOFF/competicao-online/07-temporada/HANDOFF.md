# HANDOFF - Temporada, ligas e recompensas  (v1)

> Parte do pacote **Competição Online** (10 telas). Leia `../LEIAME.md` primeiro: ordem de implementação e componentes compartilhados.

## 1. Objetivo
Tela aberta pela faixa de temporada do Ranking: **fim de temporada** (contador e aviso de reset parcial), **liga/divisão atual** com progresso ('faltam X pontos para subir de divisão'), **escada de ligas** Bronze, Prata, Ouro, Platina, Diamante, Elite com subdivisões I/II/III e faixas de pontos, **recompensas por divisão**, **melhores posições**, **conquistas do ranking** (Top 10 global, Top 10 local, Elite da temporada) e ações **Compartilhar posição** / **Compartilhar liga**. Tela nova.
Faixas adotadas (proposta): Bronze 0–599, Prata 600–1.199, Ouro 1.200–1.799, Platina 1.800–2.399, Diamante 2.400–2.999, Elite 3.000+; cada liga tem III/II/I de 200 pontos; Elite não tem subdivisão.

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
| `screen` | SeasonScreen | ui/screens/competition/SeasonScreen.kt | sim |
| `ssn.time` | SeasonTimeCard | ui/components/competition/SeasonTimeCard.kt | sim |
| `ssn.me` | LeagueProgressCard | ui/components/competition/LeagueProgressCard.kt | sim |
| `ssn.ladder`, `ssn.dv` | LeagueLadder + DivisionList | ui/components/competition/LeagueLadder.kt | sim |
| `ssn.rewards` | SeasonRewardsCard | ui/components/competition/SeasonRewardsCard.kt | sim |
| `ssn.best` | SeasonBestCard | ui/components/competition/SeasonBestCard.kt | sim |
| `ssn.achv` | RankingAchievementsRow | ui/components/competition/RankingAchievementsRow.kt | sim |
| `ssn.dock` | BottomActionBar com 2 botões | ui/components/ActionBar.kt | parcial |

## 5. Regras de layout
- Coluna: topbar 48, `ssn.scroll` (`weight(1f)`, `LazyColumn`, gap 10), dock fixo.
- Escada: 6 botões iguais (`weight(1f)`, gap 8), insígnia 34dp + nome 10.5sp; selecionado = BrassSoft + borda Brass; liga atual tem halo OliveSoft de 2dp.
- Lista de divisões troca conforme a liga selecionada: 3 linhas de 52dp (I em cima, III embaixo) ou 1 linha para Elite.
- Recompensas: 6 linhas de 48dp separadas por divisor; textos de recompensa são exemplos.
- Rolagem: sim; dock fixo.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| padrao | `eval: __setState('padrao')` | Liga Ouro selecionada. |
| liga_platina | click `ssn.ladder.platina` | Divisões da Platina. |
| liga_elite | click `ssn.ladder.elite` | Linha única Elite 3.000+. |
| compartilhar_posicao_pressionado | press `ssn.dock.pos` | Escala 0.96. |

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `ssn.ladder.bronze` ... `ssn.ladder.elite` | toque | cor de fundo e da borda | normal -> selecionado | 180ms | 0 | ease-out | não | troca instantânea |
| `ssn.dock.pos`, `ssn.dock.league` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |

Regra geral: no máximo uma animação em repouso por tela. `AisleMotion` já tem Tap=100/Standard=200/Enter=300; os valores acima (120/180/240/320ms) são os do HTML, use-os.

## 8. Assets
Fontes (res/font, nomes exatos, já existem no app): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`, `saira_stencil_one_regular.ttf`.
Lâmina do Elite: `drawable/ic_elite_blade` (mesmo path de `AisleSymbol`/`drawEliteMark`; polígono oliva + faixa vermelha, viewBox 64).
Insígnia de liga: `drawable/ic_league_<liga>_<n>` (escudo viewBox 24 + `n` chevrons; n = 1 para divisão III, 2 para II, 3 para I; Elite = escudo com a lâmina). Gerar por uma função `LeagueBadge(league, division, size)` em Compose (Path), tingindo o escudo com a cor da liga e os chevrons com `Background`. Path do escudo: `M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z`. Não usar PNG.
Ícones: SVG inline com `data-icon="material:..."`; no app usar os Material Icons indicados (ArrowBack, ChevronRight, Close, Share, Schedule, Groups, Person, EmojiEvents, WorkspacePremium, Flag, Notifications, Bolt, Check, Info, Lock, Star, Place, Link, Add, Download, CalendarMonth, BarChart, DirectionsRun, LocalFireDepartment, CardGiftcard, Refresh, PhotoCamera, MoreHoriz, Warning, Stars). O traçado do SVG é aproximação visual; stroke 2, cantos arredondados, 24dp de caixa.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/theme/EliteLeague.kt | (criado no Hub) | Enum `League(name, color, range)` + `Division` | Tabela de faixas vem do servidor; HTML usa a proposta |
| ui/components/competition/LeagueLadder.kt (novo) | — | Escada + lista de divisões | — |
| ui/components/competition/SeasonTimeCard.kt, SeasonRewardsCard.kt, SeasonBestCard.kt, RankingAchievementsRow.kt (novos) | — | Cards | — |
| ui/screens/competition/SeasonScreen.kt (novo) | — | Tela completa | Aberta por `rank.season` no Ranking |

## 10. Armadilhas
- Cor da insígnia de liga vem do enum, nunca do nome do arquivo.
- Barra de progresso do ponto atual até o teto da divisão: `progress = (elo - piso) / (teto - piso)` = 71% para 1.542 em 1.400–1.599.
- Contador de temporada atualiza por minuto; formato `Xd YYh`.
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
- Faixas de pontos por liga são as do Objetivo? Podem vir do servidor.
- Recompensas reais (texto ilustrativo no HTML) e % do reset parcial: o que dizer ao usuário?
- Duração da temporada (usei 30 dias, 60% decorrido).

## Changelog
- v1: primeira entrega.
