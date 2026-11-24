# HANDOFF - Compartilhar (cards para redes sociais e convite)  (v1)

> Parte do pacote **Competição Online** (10 telas). Leia `../LEIAME.md` primeiro: ordem de implementação e componentes compartilhados.

## 1. Objetivo
Tela de compartilhamento aberta pelos botões 'Compartilhar resultado', 'Compartilhar conquista', 'Compartilhar posição/liga' e 'Compartilhar convite'. Mostra o **card** que será gerado como imagem 1080x1350 (4:5) em 6 variações: **Participação** (colocação, tempo, pace, nome, ranking global e MT, liga, conquista, chamada para baixar), **Pódio** (1º, 2º, 3º), **Conquista**, **Ranking** ('Estou na Liga Ouro II'), **Convite** ('MISSÃO PRECISA DE VOCÊ', contador 1/2, 'Entrar agora', 'Clique no link e caia direto na missão') e **Convite sem app** ('Baixar Elite' / 'Abrir no Elite'). Todos com o nome Elite e o lema 'Foco, Missão dada, Missão cumprida.'. Destinos: Instagram, WhatsApp, Mais; salvar imagem. Estende `ShareCardGenerator.kt`.

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
| `screen` | ShareCompetitionScreen | ui/screens/competition/ShareCompetitionScreen.kt | sim (padrão de ShareCardPreviewScreen.kt) |
| `shr.chips` | ShareVariantChips | ui/components/competition/ShareVariantChips.kt | sim |
| `shr.c1` ... `shr.c5` | ShareCardPreview (Bitmap do gerador, exibido a 290x362dp) | ui/components/ShareCardGenerator.kt | estender: novas variantes em `ShareCardData` |
| `shr.targets` | ShareTargets | ui/components/competition/ShareTargets.kt | sim |
| `shr.dock` | BottomActionBar (salvar + compartilhar) | ui/components/ActionBar.kt | parcial |

## 5. Regras de layout
- Coluna: topbar 48, chips de variação (rolagem horizontal, h30, gap 8, sangra 16dp nas bordas), preview, destinos (3 botões iguais h72), espaçador `weight(1f)`, dock.
- **O card é um Bitmap gerado por Canvas em 1080x1350px.** O HTML o mostra em 290x362dp: escala 0,2685 (1dp do preview = 3,724px do bitmap). Converter todos os tamanhos do card por esse fator (ex.: número da colocação 104dp -> 387px; padding 16 -> 60px; logotipo 13 -> 48px). Não reimplementar o card em Compose para exibir: gerar o Bitmap e mostrar a imagem.
- Elementos do card: fundo em gradiente 160deg Card-elevated -> Background, 2 faixas inclinadas -18deg decorativas (OliveSoft e RedSoft) à direita; topo com lâmina + 'ELITE' (Saira, ls 2.6) e etiqueta da missão; miolo por variação; rodapé com faixa vermelha 60x3, lema 7.5sp caixa-alta e chamada.
- Variações: `participacao` (shr.c1), `podio` (shr.c2), `conquista` (shr.c3), `ranking` (shr.c4), `convite` e `convite_fallback` (shr.c5, alterna botão 'Entrar agora' vermelho x 'Baixar Elite' oliva e a linha de texto).
- Rolagem: não.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| participacao | `eval: __setState('participacao')` | Card de participação. |
| podio | click `shr.chips.podio` | Card de pódio. |
| conquista | click `shr.chips.conquista` | Card de conquista. |
| ranking | click `shr.chips.ranking` | Card de ranking/liga. |
| convite | click `shr.chips.convite` | Card de convite com 'Entrar agora'. |
| convite_fallback | click `shr.chips.fallback` | Card com 'Baixar Elite' / 'Abrir no Elite'. |
| compartilhar_pressionado | press `shr.dock.share` | Escala 0.96. |

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `shr.targets.stories`, `shr.targets.msg`, `shr.targets.more` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |
| `shr.dock.share` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |
| `shr.preview` | trocar variação | troca do card | instantânea | 0ms | 0 | - | não | - |

Regra geral: no máximo uma animação em repouso por tela. `AisleMotion` já tem Tap=100/Standard=200/Enter=300; os valores acima (120/180/240/320ms) são os do HTML, use-os.

## 8. Assets
Fontes (res/font, nomes exatos, já existem no app): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`, `saira_stencil_one_regular.ttf`.
Lâmina do Elite: `drawable/ic_elite_blade` (mesmo path de `AisleSymbol`/`drawEliteMark`; polígono oliva + faixa vermelha, viewBox 64).
Insígnia de liga: `drawable/ic_league_<liga>_<n>` (escudo viewBox 24 + `n` chevrons; n = 1 para divisão III, 2 para II, 3 para I; Elite = escudo com a lâmina). Gerar por uma função `LeagueBadge(league, division, size)` em Compose (Path), tingindo o escudo com a cor da liga e os chevrons com `Background`. Path do escudo: `M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z`. Não usar PNG.
Ícones: SVG inline com `data-icon="material:..."`; no app usar os Material Icons indicados (ArrowBack, ChevronRight, Close, Share, Schedule, Groups, Person, EmojiEvents, WorkspacePremium, Flag, Notifications, Bolt, Check, Info, Lock, Star, Place, Link, Add, Download, CalendarMonth, BarChart, DirectionsRun, LocalFireDepartment, CardGiftcard, Refresh, PhotoCamera, MoreHoriz, Warning, Stars). O traçado do SVG é aproximação visual; stroke 2, cantos arredondados, 24dp de caixa.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/components/ShareCardGenerator.kt | Gera o card de treino/mapa | Novas variantes `ShareCardData.Competition(kind, ...)` com os 6 layouts, 1080x1350, mesmas fontes (`ShareCardFonts`) e `drawEliteMark` | Cada variação tem os campos do HTML; textos fixos do lema |
| ui/components/competition/ShareVariantChips.kt (novo) | — | Chips de variação | — |
| ui/screens/competition/ShareCompetitionScreen.kt (novo) | — | Tela com preview do Bitmap + destinos + salvar | Padrão de `ShareCardPreviewScreen.kt` |
| AndroidManifest / FileProvider | (existente para o card atual) | Reusar para o novo URI | Sem mudança se já cobre `generateShareImage` |

## 10. Armadilhas
- Bitmap em 1080x1350: respeitar o fator 3,724 em fontes e espaçamentos; conferir cortes de texto com 'MISSÃO PRECISA DE VOCÊ' (3 linhas).
- Texto de lema a 7.5dp no preview = 28px no bitmap: legível; não aumentar.
- A imagem não leva foto de rosto; avatares são iniciais sobre `EliteAvatar`.
- Instagram/WhatsApp aparecem só como texto; sem logotipos de terceiros. O ícone de WhatsApp no HTML é um ícone genérico de link.
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
- Link do convite: domínio e esquema (App Link) para abrir o app ou ir ao download.
- O card de convite leva QR code? Não desenhado.
- Texto 'Baixar Elite': link da Play Store ainda não existe? Definir o destino de fallback.

## Changelog
- v1: primeira entrega.
