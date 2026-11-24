# SPEC — <screen name>   (written by Claude chat; numbers in dp = CSS px)

## 1. Purpose and intent
One paragraph: what the user does here and what must feel right (e.g. "numbers are the hero; the map is atmosphere").

## 2. Screen frame
Target device 393x851dp. Safe areas: top = status bar, bottom = gesture bar. Scroll: none / <where>.
Flexible element (the only one that absorbs height): <name>. Everything else fixed.

## 3. Tokens
Colors (hex + where used), type scale (family / size / weight / line-height), radii, spacing scale. Same names as CSS variables in proto.html.

## 4. Layers (back to front)
1. <layer> — what it is, size, behavior
2. ...

## 5. Components (top to bottom)
For each: name suggested for the native component, dp size, padding, gap, radius, colors, text style, alignment, states (default/pressed/on/off), a11y label.

## 6. Animations
Table: element | trigger (rest / tap / press / open) | property | from → to | duration | easing | loop | reduced-motion behavior.
State clearly "ONLY animated element at rest: X".

## 7. States to implement (each has a PNG in screens/)
default, <menu-open>, <pressed>, <empty/data>.

## 8. Known approximations (be honest)
Things CSS does that native can't match exactly (backdrop blur, glow shadows) and the agreed substitute.

## 9. Do NOT
List of traps for this screen (e.g. "no gradient on the card", "no uppercase labels", "no emojis").
