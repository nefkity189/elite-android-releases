# CSS → Flutter (logical px = CSS px)

## Setup first
- Fonts in `assets/fonts/`, declared in `pubspec.yaml`:
  ```yaml
  fonts:
    - family: Inter
      fonts: [{asset: assets/fonts/Inter-Regular.ttf}, {asset: assets/fonts/Inter-Medium.ttf, weight: 500}, {asset: assets/fonts/Inter-SemiBold.ttf, weight: 600}]
    - family: BarlowSemiCondensed
      fonts: [{asset: assets/fonts/BarlowSemiCondensed-SemiBold.ttf, weight: 600}, {asset: assets/fonts/BarlowSemiCondensed-Bold.ttf, weight: 700}]
  ```
  Run `flutter pub get`. Tabular digits: `fontFeatures: [FontFeature.tabularFigures()]`. Do not rely on google_fonts at runtime for an offline running app unless the user already uses it.
- One `theme/tokens.dart` with `const Color`s (CSS `rgba(255,255,255,.08)` → `Color.fromRGBO(255,255,255,.08)` or `Colors.white.withOpacity(.08)`), spacing and text styles.
- Safe areas: `SafeArea` / `MediaQuery.of(context).padding`.

## Layout mapping
| CSS | Flutter |
|---|---|
| flex column / row | `Column` / `Row` (`mainAxisSize: MainAxisSize.max`) |
| `gap:10px` | `Column(spacing: 10)` (Flutter ≥3.27) or `SizedBox(height:10)` between children |
| `flex:1` | `Expanded(child: …)` (or `Flexible(fit: FlexFit.tight)`) — plain `Flexible` shrink-wraps |
| `min/max-height` | `ConstrainedBox(constraints: BoxConstraints(minHeight:, maxHeight:))` |
| padding / margin | `Padding(EdgeInsets...)` / outer `Padding` |
| `border:1px solid; radius:20px; bg` | `DecoratedBox/Container(decoration: BoxDecoration(color, borderRadius: BorderRadius.circular(20), border: Border.all(color: line, width: 1)))` |
| `border-radius:50%` | `shape: BoxShape.circle` |
| `position:absolute` | `Stack` + `Positioned(right:16, bottom:10, …)` |
| z-index | Stack child order |
| 7-col grid | `Row` of `Expanded` |
| overflow hidden | `ClipRRect` |
| viewport height rules (`max-height:760px`) | `LayoutBuilder` / `MediaQuery.sizeOf(context).height < 760` |
| scroll fallback | only if the spec says so; otherwise no `SingleChildScrollView` |

## Visual mapping
- Gradients: `LinearGradient(begin: Alignment.topCenter, end: Alignment.bottomCenter, colors: [...], stops: [0, .56, 1])` inside a `DecoratedBox` that fills the area (`Positioned.fill`). `SweepGradient` for conic (rotate with `transform: GradientRotation(angle)`), `RadialGradient(center: Alignment(-.4,-.6), radius: …)`.
- Glow shadow: `BoxShadow(color: teal.withOpacity(.35), blurRadius: 16, spreadRadius: 2)`. (CSS blur radius ≈ 2× Flutter `blurRadius`/… visually close at these sizes; compare in the diff.)
- Backdrop blur: `ClipRRect(child: BackdropFilter(filter: ImageFilter.blur(sigmaX: 7, sigmaY: 7), child: …))` — CSS `blur(14px)` ≈ sigma 7. Real support, use it.
- Text gradient: `ShaderMask(shaderCallback: (r) => LinearGradient(colors: [...]).createShader(r), child: Text(..., style: TextStyle(color: Colors.white)))`.
- Ring progress: `CustomPaint` + `canvas.drawArc(rect.deflate(stroke/2), -pi/2, 2*pi*progress, false, Paint()..style=PaintingStyle.stroke..strokeWidth=4..strokeCap=StrokeCap.round)`.
- Line-height: CSS `line-height:33.6px` with `fontSize:32` → `height: 33.6/32`, plus `leadingDistribution: TextLeadingDistribution.even` if the text sits too low.
- Icons: copy SVG path into `flutter_svg` or `CustomPainter` with `Path` if no matching Material icon.

## Interaction traps
- Use `InkWell/GestureDetector.onTap` (fires on release). Scale-on-press: `onTapDown/onTapUp/onTapCancel` toggling a `AnimatedScale`.
- Overlay menus: use `OverlayPortal` / `Stack` at screen root so children aren't clipped by their parent; position from the spec's center-of-FAB origin and verify nothing is off-screen.
- Reduced motion: `MediaQuery.of(context).disableAnimations` → skip repeating controllers (don't even create the `AnimationController.repeat`).
- Dispose controllers; repeating controllers cost battery — only the one intended resting animation may run.
