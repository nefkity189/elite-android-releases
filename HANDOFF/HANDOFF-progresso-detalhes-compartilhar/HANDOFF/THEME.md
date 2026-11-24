# THEME - valores compartilhados pelas duas pastas

Valem para `01-progresso-seta/` e `02-detalhes-compartilhar/`. **Nenhum token novo**: tudo já existe no projeto.

## 2. Dispositivo
Poco X7, 393 x 851 dp, tema escuro, Jetpack Compose, pt-BR. Insets do sistema (status bar no topo, barra de navegação embaixo) **não são desenhados**: no protótipo, `*.statusbar` (32 dp) é só marcador de `statusBarsPadding()`. Fontes do app: Barlow Condensed (números/títulos), Inter (texto), Saira Stencil One (só o logotipo ELITE). Fallback do protótipo se as fontes não carregarem: Arial Narrow; **meça sempre com as fontes do app** (`--fonts-dir app/src/main/res/font`).


## 3. Tokens
Todos já existem em `ui/theme/Color.kt`, `Spacing.kt`, `Shapes.kt`. **Nenhum token novo** (só 2 valores crus que já existem no código, listados no fim).

| Variável CSS | Valor | Nome Kotlin |
|---|---|---|
| --bg | #0A0B09 | `Background` |
| --surface | #10120D | `Surface` |
| --card | #161911 | `Card` (`EliteCard`) |
| --card-elev | #1D2116 | `CardElevated` |
| --line | rgba(236,232,218,.10) | `Line` |
| --line-strong | rgba(236,232,218,.18) | `LineStrong` (borda de card, divisor do rodapé) |
| --text | #ECE8DA | `Text` (import `as TextColor`) |
| --muted | #8F9384 | `TextMuted` |
| --olive | #A3B565 | `Olive` |
| --olive-dark | #5D6B34 | `OliveDark` |
| --olive-soft | rgba(163,181,101,.14) (0x24) | `OliveSoft` |
| --on-olive | #0E1306 | `OnOlive` |
| --red | #D12B2B | `Red` |
| --on-red | #FFF4F0 | `OnRed` |
| --brass | #C9A24B | `Brass` |
| --share-glow | #1E2314 | cru em `ShareCardPreviewScreen` (`Color(0xFF1E2314)`), já existe |
| --route-halo | #1C2015 | cru em `RoutePreview` (`Color(0xFF1C2015)`), já existe |

Espaçamentos (`EliteSpacing`): xs 4, sm 8, md 12, lg 16, xl 20. Formas: card 18 dp (padrão do `EliteCard`), card da lista 16 dp, pílula 28 dp (botão salvar), 20 dp (botão do card de compartilhar).

Tipografia usada (Type.kt): `displayMedium` (Barlow Bold 30/32, ls 0.9), `EliteNumberStyles.MetricMedium` (Barlow SemiBold 28/28, tabular), `titleMedium` (Inter SemiBold 16/22), `titleSmall` e `labelMedium` (Inter Medium 12.5/16), `labelSmall` (Inter Medium 11/16), `bodySmall` (Inter 12/16), `headlineMedium` (Barlow SemiBold 20/23, ls 0.8), `EliteTextStyles.Label` (Inter SemiBold 11, ls 1.32).


## 8. Assets
- Fontes (já em `app/src/main/res/font`, nomes exatos): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.otf`, `inter_medium.otf`, `inter_semibold.otf`, `inter_bold.otf`, `saira_stencil_one_regular.ttf`. **Nada novo.** Atenção: não há Barlow "Black"; `FontWeight.Black` cai em Bold (já é assim hoje).
- Ícones Material (já importados nas telas): `Icons.AutoMirrored.Filled.ArrowBack`, `Icons.AutoMirrored.Filled.ArrowForward`, `Icons.Default.Share`, `Icons.Default.EmojiEvents`, `Icons.Default.LocationOff`.
- Drawable: `R.drawable.ic_elite_mark` (o desenho do protótipo em `shr.card.mark` é só um marcador; usar o drawable real, 26x30 dp).
- Imagens: nenhuma. A miniatura é desenhada, não é arquivo.

