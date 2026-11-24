# Plan: Redesign "Corrida livre" Screen (Runna-style Dark Theme)

**Source PRD**: User requirements (inline)
**Complexity**: Large

## Summary
Complete redesign of the ActiveRunScreen for "Corrida livre" mode with a professional dark theme inspired by Runna app. The new design features a full-screen MapLibre map background with glassmorphism overlays, a quarter-circle radial menu in the top-right, a compact 112px timer ring with progress arc, a 2-column stats row, and a bottom action bar with Pause/Stop buttons. All existing logic (timer, GPS, distance, pace) is preserved.

## Patterns to Mirror
| Category | Source | Pattern |
|---|---|---|
| Naming | `ActiveRunScreen.kt:141` | Component functions use PascalCase, composables prefixed with descriptive names |
| Theme | `Color.kt:45-70` | Semantic color layer: primitives → semantic → component |
| Typography | `Type.kt:78-102` | Number styles with tabular figures (`tnum` feature) in `EliteNumberStyles` |
| Spacing | `Spacing.kt:5-18` | `EliteSpacing` object with named spacing tokens |
| Map | `MapComponents.kt:147-402` | `MapViewComposable` via `AndroidView` with `DisposableEffect` for lifecycle |
| State | `ActiveRunScreen.kt:230-298` | `animateFloatAsState` for pulse/scale animations, `LaunchedEffect` for side effects |
| Components | `FreeRunLayout.kt:42-149` | Dedicated composable for free-run layout with window size class adaptation |
| Testing | `test_full_flow2.yaml` | Maestro flows use `testTag` for selectors, `extendedWaitUntil` for async UI |

## Files to Change
| File | Action | Why |
|---|---|---|
| `app/src/main/java/com/example/ui/theme/Color.kt` | UPDATE | Add new semantic colors for dark theme palette |
| `app/src/main/java/com/example/ui/theme/Type.kt` | UPDATE | Add Barlow Semi Condensed for numbers, update number styles |
| `app/src/main/java/com/example/ui/theme/Spacing.kt` | UPDATE | Add new spacing tokens for radial menu, timer ring |
| `app/src/main/java/com/example/ui/components/RunnaTheme.kt` | CREATE | Centralized theme file with colors, dimensions, typography for new design |
| `app/src/main/java/com/example/ui/components/TimerRing.kt` | CREATE | 112px circular timer with progress arc |
| `app/src/main/java/com/example/ui/components/StatsRow.kt` | CREATE | 2-column stats with vertical divider |
| `app/src/main/java/com/example/ui/components/ActionBar.kt` | CREATE | Bottom bar with Pause (teal) and Stop (orange) buttons |
| `app/src/main/java/com/example/ui/components/RadialMenu.kt` | CREATE | Quarter-circle radial menu with 3 toggle buttons |
| `app/src/main/java/com/example/ui/components/GpsStatusChip.kt` | CREATE | Top-left GPS status chip with red dot |
| `app/src/main/java/com/example/ui/components/BatteryWarningBanner.kt` | CREATE | Dismissible battery warning card |
| `app/src/main/java/com/example/ui/components/MapOverlay.kt` | CREATE | "Seguindo você" pill and recenter button |
| `app/src/main/java/com/example/ui/components/FreeRunLayout.kt` | UPDATE | Refactor to use new components |
| `app/src/main/java/com/example/ui/screens/ActiveRunScreen.kt` | UPDATE | Rebuild layout with new structure, full-screen map background |
| `app/src/main/java/com/example/ui/state/MapState.kt` | UPDATE | Add radial menu open state if needed |
| `app/src/main/java/com/example/ui/theme/Motion.kt` | UPDATE | Add spring specs for radial menu animation |

## Tasks

### Task 1: Update Theme - Colors & Typography
- **Action**: Add new semantic colors (RunnaBackground, RunnaGlass, RunnaGlassElevated, RunnaTextPrimary, RunnaTextSecondary, RunnaTeal, RunnaOrange, RunnaRedDot) and update typography for Barlow Semi Condensed with tabular figures
- **Mirror**: `Color.kt:45-70`, `Type.kt:78-102`
- **Validate**: `./gradlew :app:compileDebugKotlin` — no errors

### Task 2: Create RunnaTheme.kt Centralized Theme
- **Action**: Create single source of truth for all Runna-specific dimensions (timer ring 112px, radial menu 132px/80px/44px, button heights 56px, chip height 32px, etc.)
- **Mirror**: `Spacing.kt`, `Shapes.kt`
- **Validate**: Build compiles, all dimensions accessible

### Task 3: Create TimerRing Component
- **Action**: 112px circular timer with glass background, 4px teal progress arc with rounded caps, 34px Barlow Semi Condensed time text
- **Mirror**: `FreeRunLayout.kt:87-119` (existing timer), `EliteNumberStyles.Metric`
- **Validate**: Preview in Compose Preview, timer animates smoothly

### Task 4: Create StatsRow Component
- **Action**: Two equal columns separated by 1px vertical divider. Left: distance (34px number + 16px unit in secondary) + "Distância" label. Right: pace (34px or "--:--") + "Ritmo (min/km)" label. Labels 12px secondary.
- **Mirror**: `FreeRunLayout.kt:122-140`, `MetricBlock`
- **Validate**: Layout works at 360-430px widths

### Task 5: Create ActionBar Component
- **Action**: Glass container with 26px top radius, 16px padding, safe area inset. Two equal columns (12px gap): Pause button (teal bg, pause icon) and Stop button (orange bg, square icon), both 56px height, fully rounded.
- **Mirror**: `ActiveRunScreen.kt:842-862` (AnimatedActionButton, StopButton)
- **Validate**: Buttons respond to touch, proper colors

### Task 6: Create RadialMenu Component
- **Action**: 48px FAB anchor (gear icon, glass). On tap: rotates 90°, becomes teal. Opens 90° fan (left + down) with 132px quarter-circle glass-elevated backdrop (scale animation from top-right, 250ms ease-out). Three 44px icon buttons at 180°, 135°, 90° on 80px radius: Speaker, Vibration, Location Pin. States: on=teal+dark icon, off=#1B2738+secondary icon. Accessibility labels. Close on outside tap.
- **Mirror**: `ActiveRunScreen.kt:570-708` (existing map menu), `MapControlButton`
- **Validate**: Menu opens/closes, toggles work, proper z-index over battery warning

### Task 7: Create GpsStatusChip Component
- **Action**: Top-left chip, 32px height, fully rounded, glass background. Red dot (#E0524F) + "Sem sinal" text when no GPS. Uses existing GPS signal logic from ActiveRunScreen.
- **Mirror**: `ActiveRunScreen.kt:334-369` (GPS indicator)
- **Validate**: Shows correct state based on GPS

### Task 8: Create BatteryWarningBanner Component
- **Action**: Glass card, 14px radius, 12.5px text: "O celular pode pausar o GPS com a tela apagada. Toque para ajustar." Teal "Não mostrar de novo" link, × close button. Opens battery optimization settings on tap.
- **Mirror**: `ActiveRunScreen.kt:422-488` (existing battery warning)
- **Validate**: Dismisses correctly, opens settings

### Task 9: Create MapOverlay Component
- **Action**: Bottom-left pill "Seguindo você" (glass, 13px, radius 15). Bottom-right 46px circular recenter button (glass, teal crosshair icon). Positioned above action bar with safe spacing.
- **Mirror**: `ActiveRunScreen.kt:775-805` (camera mode legend), `MapControlButton`
- **Validate**: Overlays don't touch action bar, visible on map

### Task 10: Rebuild ActiveRunScreen Layout
- **Action**: Restructure as full-screen Box with MapViewComposable as background (weight 1f). Layer overlays in order: map → top bar (GPS chip + radial menu) → battery warning → center block (TimerRing + "Corrida livre" label + StatsRow) → map overlays → action bar. Gradients at top (92%→0 at 56%) and bottom (0→85%). Keep all existing logic (timer, GPS, distance, pace, steps bar for non-free-run).
- **Mirror**: `ActiveRunScreen.kt:309-953` (current layout)
- **Validate**: Maestro test flow passes (onboarding → free run → map features → finish)

### Task 11: Update FreeRunLayout
- **Action**: Refactor to compose TimerRing + StatsRow + new label, remove duplicate metric logic
- **Mirror**: `FreeRunLayout.kt:42-149`
- **Validate**: Free run mode displays correctly

### Task 12: Add Font Assets
- **Action**: Add Barlow Semi Condensed (weights 400, 500, 600, 700) and Inter (400, 500, 600) TTF files to `res/font/`, update `Type.kt` font families
- **Mirror**: `Type.kt:13-14`
- **Validate**: Fonts render in preview

### Task 13: Maestro Test Updates
- **Action**: Update test flows to use new testTags: `timer_ring`, `stats_row`, `btn_pause`, `btn_stop`, `radial_menu_anchor`, `radial_menu_sound`, `radial_menu_vibration`, `radial_menu_gps`, `gps_chip`, `battery_warning`, `map_pill_following`, `btn_recenter`
- **Mirror**: `.maestro/test_full_flow2.yaml`
- **Validate**: `maestro test .maestro/test_corrida_livre.yaml` passes

## Validation
```bash
# Build
./gradlew :app:assembleDebug

# Unit tests
./gradlew :app:testDebugUnitTest

# Lint
./gradlew :app:lintDebug

# Maestro E2E test (requires running emulator)
maestro test .maestro/test_corrida_livre.yaml
```

## Risks
| Risk | Likelihood | Mitigation |
|---|---|---|
| MapLibre performance with full-screen background + overlays | Medium | Test on low-end devices, optimize layer updates, use `graphicsLayer` for animations |
| Radial menu animation jank | Medium | Use `animateFloatAsState` with spring, test with reduced motion |
| Font loading (Barlow/Inter) increases APK size | Low | Subset fonts to latin-ext only, use downloadable fonts if needed |
| Safe area handling on notched devices | Medium | Use `WindowInsets` APIs, test on various device profiles |
| GPS chip + radial menu z-index conflicts | Low | Explicit `zIndex` modifiers, test overlay order |
| Tabular figures not working on all Android versions | Low | `fontFeatureSettings = "tnum"` is widely supported |

## Acceptance
- [ ] All 13 tasks complete
- [ ] Build passes with zero errors/warnings
- [ ] Unit tests pass (80%+ coverage on new components)
- [ ] Maestro E2E test passes: onboarding → free run → map visible → radial menu toggles → pause/resume → finish → summary
- [ ] Visual match to Runna-style reference (dark map, glass cards, teal/orange accents)
- [ ] Works on 360-430px widths without scrolling
- [ ] Respects reduced motion preference
- [ ] Accessibility labels on all interactive elements
- [ ] No emojis in UI