# Animations: CSS → native (read the numbers from spec `Animations` section)

Always take duration, delay, iterations, easing, keyframe offsets and values from `inspect_layout.py` output. CSS easings: `ease-in-out` = cubic-bezier(.42,0,.58,1); `ease-out` = (0,0,.58,1); `linear`; custom `cubic-bezier(a,b,c,d)` maps 1:1 (Compose `CubicBezierEasing(a,b,c,d)`, Flutter `Cubic(a,b,c,d)`).

| CSS recipe | Compose | Flutter |
|---|---|---|
| `spin` 5s linear infinite (rotating gradient ring) | `rememberInfiniteTransition().animateFloat(0f,360f, infiniteRepeatable(tween(5000, easing=LinearEasing)))` → `rotate(angle)` inside `drawWithContent`; clip to the rounded square | `AnimationController(duration: 5s)..repeat()`; `Transform.rotate` / sweep `GradientRotation`; clip with `ClipRRect` |
| `breathe` 3.2s infinite (glow 0→.35→0) | infinite transition, `tween(1600, FastOutSlowInEasing)` with `RepeatMode.Reverse` (0→.5→1 in CSS = out and back); drive shadow alpha/blur | `controller.repeat(reverse: true)` duration 1.6s, `Curves.easeInOut`; drive `BoxShadow` |
| `pop` 0.5s `cubic-bezier(.3,1.6,.5,1)` scale .88→1.12→1 | `Animatable(1f)`: `animateTo(.88f,snap)`, then `animateTo(1.12f, tween(300))`, `animateTo(1f, tween(200))` or `spring(dampingRatio=.45f, stiffness=Spring.StiffnessMedium)` | `TweenSequence([.88→1.12 weight 60, 1.12→1 weight 40])`, 500ms |
| `flip` 0.6s rotateY 360° | `graphicsLayer { rotationY = v; cameraDistance = 12 * density }` | `Transform(transform: Matrix4.identity()..setEntry(3,2,.001)..rotateY(v))` |
| `wave` 0.6s ease-out, scale 1→1.7, opacity .7→0 (ripple outline) | `Box` with border; `graphicsLayer{ scaleX=s; scaleY=s; alpha=a }`, `tween(600, easing=CubicBezierEasing(0f,0f,.58f,1f))` | `ScaleTransition` + `FadeTransition`, `Curves.easeOut` |
| `:active { transform: scale(.9) }` + `transition .18s` | `val pressed by interactionSource.collectIsPressedAsState()`; `animateFloatAsState(if(pressed).9f else 1f, tween(180))` | `GestureDetector` down/up/cancel → `AnimatedScale(scale: .9, duration: 180ms)` |
| pulse ring on map marker (scale .6→2.2, opacity .5→0, 2s infinite) | infinite transition 2000 ms, draw circle in `Canvas` | repeating controller 2s, `CustomPaint` |
| radial menu: arc `scale(.2)→1` origin top-right, items translate from FAB center to polar positions, .25–.28s `cubic-bezier(.2,.8,.2,1)` | one `animateFloatAsState(open?1f:0f, tween(280, CubicBezierEasing(.2f,.8f,.2f,1f)))` = progress; item offset = `lerp(0, target, progress)`, alpha = progress; arc scale with `TransformOrigin(1f, 0f)`; FAB `rotationZ = 90*progress` | one `AnimationController` + `CurvedAnimation(Cubic(.2,.8,.2,1))`; `Transform.translate`, `Transform.scale(alignment: Alignment.topRight)` |

## Rules
1. **Only animate what the spec animates.** If the spec says "the only element animated at rest is X", no other `repeat()`.
2. **Retriggerable**: a tap during the animation restarts it (Compose: `Animatable.snapTo` then animate; Flutter: `controller.forward(from: 0)`).
3. **Reduced motion** → jump to end state, no infinite loops, keep the static gradient look.
4. **Verify**: use the filmstrip PNG from `render.py --filmstrip` as the reference and capture the app mid-animation (adb screencap loop) — compare the poses (scale at ~60% of pop is ~1.12, flip is edge-on at 50%).
5. Frame cost: infinite animations should live in leaf composables/widgets (`RepaintBoundary` in Flutter, `drawWithContent` reading the animated value in the draw phase in Compose) so the whole screen doesn't recompose 60×/s.
