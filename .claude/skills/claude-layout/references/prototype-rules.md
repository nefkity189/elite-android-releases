# Rules for prototypes that can be replicated exactly

Why: the HTML is only a picture-maker. Native toolkits have no CSS cascade, no `calc`, no `backdrop-filter` parity. Anything that depends on those hides bugs when ported. Write the prototype in the subset below.

## Structure
1. One root element `<main class="phone" data-screen="NAME">` that is the screen. On a real phone viewport (≤480px wide) it fills 100% width/height with no radius; on desktop it is centered with max-width 393. (The scripts crop to it, so coordinates become dp.)
2. All design values in `:root` CSS variables with the same names you will put in SPEC.md tokens (`--teal`, `--card`, `--radius-card`...). Native theme files mirror them.
3. Layout with flexbox/grid + fixed px sizes. Mark the single flexible region with `flex:1; min-height:0` and say so in SPEC. Avoid `%`/`vh`/`calc()` heights, `aspect-ratio`, `clamp()`, `auto` margins for vertical centering unless noted.
4. Every element that has text must be above any decorative layer: give text wrappers `position:relative; z-index:1` when a sibling `svg`/`::before` is absolutely positioned behind. (`lint.py` catches this — it found exactly this bug in a timer ring.)
5. Real names: class names equal the native component names (`.timer-ring`, `.action-bar`, `.radial-menu`). Add `data-component="RadialMenu"` to component roots and `data-anim="spin|breathe|pop|…"` to animated elements.
6. Icon-only buttons: `aria-label` in Portuguese; ≥44px hit area (use padding, not tiny boxes).
7. Safe areas through `env(safe-area-inset-*)` only on the screen's top/bottom containers — they map 1:1 to native insets.

## Visual features and their native cost (prefer left column)
| Prefer | Avoid / flag in SPEC "approximations" |
|---|---|
| solid fills, 1px borders, border-radius | `backdrop-filter` (Compose can't match), `mix-blend-mode`, CSS filters |
| linear/radial gradients with explicit stops | multi-layer gradient stacks, `conic-gradient` (ok only for a spinning ring — give the recipe) |
| SVG icons 24×24 stroke 2 round | icon fonts, emoji |
| `box-shadow` flat elevation | colored glow (works, but declare it: "glow = blur 16, alpha .35") |
| text sizes in px with explicit line-height | `em/rem` sizing, `text-transform`, `font-variant` other than tabular-nums |
| transforms: translate/scale/rotate | skew, 3D beyond a single rotateY flip (declare perspective) |

## Animations
- Use `@keyframes` + `animation` or `transition` with explicit duration/easing; no JS-driven per-frame animation. JS only toggles classes/state.
- Document each in SPEC section 6 (element, trigger, from→to, duration, easing, loop, reduced-motion). The measured values will also appear in `spec/*.md` — they must agree.
- Always include `@media (prefers-reduced-motion: reduce)`.
- At rest at most ONE looping animation (the hero). Map pulses count.

## Interactive states
Anything toggled by JS (menu open, selected tab, pressed) must be reachable by a CSS selector the render script can click (`--step click:#id`) so that a PNG + spec can be produced per state. Menus that fan out: give item positions as explicit translate values from the anchor center and keep every item inside the screen (lint flags otherwise).

## Responsiveness
State the target (393×851) and the range (360–430 wide). Any `@media (max-height:…)` rule must be listed in SPEC so native uses height conditionals.

## Content
Realistic data from the user's capture; write dates in pt-BR; keep numbers tabular. Mark placeholder/illustrative data in SPEC ("illustrative: workout segments").
