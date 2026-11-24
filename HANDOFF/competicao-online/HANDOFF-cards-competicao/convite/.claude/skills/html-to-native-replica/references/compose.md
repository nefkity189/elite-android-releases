# CSS → Jetpack Compose (dp = CSS px)

## Setup that must exist before any screen
- Fonts: download Barlow Semi Condensed (500/600/700) and Inter (400/500/600) → `res/font/` with **lowercase_underscore names** (`inter_regular.ttf`, `barlow_semi_condensed_semibold.ttf`...). Then
  ```kotlin
  val Inter = FontFamily(Font(R.font.inter_regular, FontWeight.Normal), Font(R.font.inter_medium, FontWeight.Medium), Font(R.font.inter_semibold, FontWeight.SemiBold))
  ```
  Use the app's real `R` import. If the build can't find fonts it fails loudly — good; never fall back to `FontFamily.SansSerif`.
- Tabular digits for timers: `TextStyle(fontFeatureSettings = "tnum")`.
- Colors: `Color(0xFF22D3B6)`. CSS `rgba(255,255,255,.08)` → `Color.White.copy(alpha = 0.08f)` (alpha is 0–1, not 0–255).
- `enableEdgeToEdge()` + `WindowInsets.statusBars` / `navigationBars` for `env(safe-area-inset-*)`.

## Layout mapping
| CSS | Compose |
|---|---|
| `display:flex; flex-direction:column` | `Column` |
| row | `Row` |
| `gap:10px` | `Arrangement.spacedBy(10.dp)` |
| `flex:1` / `flex-grow:1` | `Modifier.weight(1f)` — keep default `fill = true`. `fill=false` makes it shrink-wrap and the sibling below floats up (past bug: action bar left the bottom) |
| `min-height / max-height` | `Modifier.heightIn(min=, max=)` |
| `justify-content: space-between` | `Arrangement.SpaceBetween` |
| `padding: 14px 16px 0` | `Modifier.padding(start=16.dp, top=14.dp, end=16.dp, bottom=0.dp)` — apply padding **after** `background` if the padding should be inside the colored box |
| `border:1px solid X; border-radius:20px` | `Modifier.clip(RoundedCornerShape(20.dp)).background(card).border(1.dp, line, RoundedCornerShape(20.dp))` |
| `border-radius:50%` | `CircleShape` |
| `position:absolute; inset:0` | child of `Box` with `Modifier.fillMaxSize()` |
| `position:absolute; right:16px; bottom:10px` | `Modifier.align(Alignment.BottomEnd).padding(end=16.dp, bottom=10.dp)` inside `Box` |
| `z-index` | order of children in `Box`, or `Modifier.zIndex()` |
| `grid 7 columns` | `Row` with `Modifier.weight(1f)` children |
| `overflow:hidden` | `Modifier.clip(shape)` |

## Visual mapping
- `linear-gradient(180deg, a 0%, b 56%, c 100%)` → `Brush.verticalGradient(0f to a, 0.56f to b, 1f to c)` **with color stops, drawn in a layer that fills the real height** (`Modifier.fillMaxSize().background(brush)`). Past bug: a gradient inside a 1 dp-high box.
- `conic-gradient` → `Brush.sweepGradient(listOf(...))` and rotate via `drawWithContent { rotate(angle) { drawRect(brush) } }`.
- `radial-gradient(circle at 30% 20%, a, b 70%)` → `Brush.radialGradient(0f to a, 0.7f to b, center = Offset(w*.3f, h*.2f), radius = ...)`.
- `box-shadow: 0 0 16px 2px rgba(...)` → `Modifier.shadow(elevation, shape, ambientColor=, spotColor=)` only approximates glow. For a real colored glow draw it: `drawBehind { drawIntoCanvas { c -> c.nativeCanvas.drawRoundRect(..., Paint().apply { color=...; maskFilter = BlurMaskFilter(16f*density, BlurMaskFilter.Blur.NORMAL) }) } }` (needs `setLayerType` software on old APIs; fine from API 28+ with hardware).
- `backdrop-filter: blur(14px)` → `Modifier.blur()` blurs the node itself, not what's behind it. Use a semi-opaque solid (`glass` color at its alpha) — say in the report that the blur is approximated. Real backdrop blur needs Haze library or API 31 RenderEffect on a captured layer; don't half-do it.
- Text: `font-size:34px` → `fontSize = 34.sp` (dp==sp when user font scale is 1). Set `lineHeight` from spec (`line-height:33.6px` → `33.6.sp`) and `LineHeightStyle(trim = None)` if text clips. `letter-spacing:.5px` → `0.5.sp`.
- Text gradient → `TextStyle(brush = Brush.linearGradient(...))`.
- Ring progress (SVG circle `stroke-dasharray`) → `Canvas { drawArc(color, startAngle=-90f, sweepAngle=360f*progress, useCenter=false, style=Stroke(width=4.dp.toPx(), cap=StrokeCap.Round)) }` and inset the arc by `stroke/2`.
- SVG icons → Material Icons only if shape matches; otherwise import the `<path d=…>` from the spec's `svg:` entry as `ImageVector` via `PathParser` or an XML vector drawable (viewBox 24, stroke 2, round caps).

## Interaction traps
- Taps: use `Modifier.clickable { }` (fires on finger-up). Do **not** use `pointerInput` + `onPress` completion for clicks (past bug: fired on cancel).
- Min touch target 48 dp: wrap small icons in `Modifier.minimumInteractiveComponentSize()` or explicit `size`.
- Dismiss-on-outside-tap for the radial menu: full-screen transparent `Box(Modifier.fillMaxSize().clickable(indication=null, interactionSource=...) { close() })` placed *under* the menu, *over* the content.
- Popups/overlays that extend outside their parent (radial menu fan) are clipped if the parent has `clip`/small bounds. Put the menu in a root-level `Box` aligned TopEnd and use `Modifier.offset { IntOffset(...) }` with positions computed from the spec (center of FAB as origin, `x = -r*cos(θ)`, `y = r*sin(θ)`), and check on screen that every button is inside the window.
- Reduced motion: `Settings.Global.ANIMATOR_DURATION_SCALE == 0f` → treat as reduce-motion and skip infinite transitions.
