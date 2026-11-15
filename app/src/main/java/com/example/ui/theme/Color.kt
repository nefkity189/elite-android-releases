package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ============================================================
// CAMADA 1: PRIMITIVOS (valores brutos; não usar direto nas telas)
// ============================================================
private val Navy900 = Color(0xFF0B1020)
private val Navy850 = Color(0xFF101729)
private val Navy800 = Color(0xFF141B2E)
private val Navy750 = Color(0xFF17203A)
private val Navy700 = Color(0xFF121829)
private val Navy650 = Color(0xFF1C253D)
private val Navy600 = Color(0xFF232B42)
private val Navy500 = Color(0xFF1F2945)
private val Slate400 = Color(0xFF6A759E)
private val Slate300 = Color(0xFF9AA3B8)
private val White = Color(0xFFFFFFFF)
private val Teal500 = Color(0xFF19C3B1)
private val Teal700 = Color(0xFF0B857A)
private val Teal900 = Color(0xFF003833)
private val Teal800 = Color(0xFF005049)
private val Teal100 = Color(0xFF70F7E5)
private val Cyan400 = Color(0xFF00E5FF)
private val Violet500 = Color(0xFF7C4DFF)
private val Violet300 = Color(0xFFA68BFF)
private val Violet200 = Color(0xFFB388FF)
private val Violet800 = Color(0xFF3B1E87)
private val Violet950 = Color(0xFF381577)
private val Violet100 = Color(0xFFEADBFF)
private val Orange500 = Color(0xFFFF7A2F)
private val Orange400 = Color(0xFFFF8A3D)
private val Orange600 = Color(0xFFFF5A1F)
private val Orange900 = Color(0xFF551E00)
private val Red400 = Color(0xFFFF5C5C)
private val Red900 = Color(0xFF3B1820)
private val Green400 = Color(0xFF4ADE80)
private val Amber400 = Color(0xFFFFC857)
private val Scrim = Color(0xFF000000)

// ============================================================
// CAMADA 2: SEMÂNTICOS (usar nas telas e componentes)
// ============================================================
val AisleBackground = Navy900
val AisleCard = Navy800
val AisleBorder = Navy600
val AisleTextPrimary = White
val AisleTextSecondary = Color(0xFFA8B2D4) // contraste melhorado sobre Navy900
val AisleTextTertiary = Color(0xFF7E89AC) // para rótulos secundários internos
val AisleTurquoise = Teal500
val AislePurple = Violet500
val AisleTextAccent2 = Violet300 // roxo claro, só para TEXTO
val AisleTealDark = Teal700 // início do gradiente em botões com texto
val AisleFieldBorder = Color(0xFF4E5878) // borda mais escura e definida
val AisleActionOrange = Orange500
val AisleAlert = Red400

val AisleSuccess = Green400
val AisleWarning = Amber400
val AisleError = Red400

// Superfícies específicas
val AisleBottomBarSurface = Navy850
val AisleCardTop = Navy750
val AisleCardBottom = Navy700
val AisleTrack = Navy650
val AisleOnAction = Navy900 // texto sobre o botão laranja
val AisleShadowOverlay = Color(0x33000000)
val AisleDangerContainer = Red900

// Gradientes
val BrandGradient = Brush.horizontalGradient(listOf(Teal500, Violet500))
val BrandButtonGradient = Brush.horizontalGradient(listOf(Teal700, Violet500))
val ActionGradient = Brush.horizontalGradient(listOf(Orange400, Orange600))
val BrandGradientVertical = Brush.verticalGradient(listOf(Teal500, Violet500))
val CardGradient = Brush.verticalGradient(listOf(Navy750, Navy700))
val ActionGradientVertical = Brush.verticalGradient(listOf(Orange400, Orange600))

// Status
val StatusCompleted = AisleTurquoise
val StatusSkipped = AisleTextSecondary
val StatusPlanned = AisleTextAccent2

// ============================================================
// CAMADA 3: COMPONENTES
// ============================================================
/** Cores das etapas do treino, em um só lugar. */
object StepColors {
    val Warmup = Violet300
    val Run = Teal500
    val Walk = Slate400
    val Cooldown = Violet200
    val RunContainer = Teal800
    val WalkContainer = Navy650
}

/** Cores dos marcadores de rota e dos anéis de meta. */
object AisleChartColors {
    val RouteStart = Teal500
    val RouteEnd = Orange500
    val RingTurquoiseEnd = Cyan400
    val RingPurpleEnd = Violet200
    val StreakContainer = Orange500.copy(alpha = 0.18f)
    val StreakContent = Orange500
}

val PauseBannerGradientEnd = Violet950

// Compatibilidade com o MaterialTheme
val PrimaryDark = Teal500
val OnPrimaryDark = Teal900
val PrimaryContainerDark = Teal800
val OnPrimaryContainerDark = Teal100
val SecondaryDark = Violet500
val OnSecondaryDark = White
val SecondaryContainerDark = Violet800
val OnSecondaryContainerDark = Violet100
val TertiaryDark = Orange500
val OnTertiaryDark = Orange900
val TertiaryContainerDark = Violet950
val OnTertiaryContainerDark = Violet200
val SurfaceVariantDark = Navy650
val SurfaceContainerDark = Navy800
val SurfaceContainerHighDark = Navy750
val OutlineVariantDark = Navy600
val ScrimDark = Scrim
val InverseSurfaceDark = White
val InverseOnSurfaceDark = Navy900

// Aliases para uso direto nas telas (compatibilidade semântica)
val AislePrimary = PrimaryDark
val AisleOnPrimary = OnPrimaryDark
val AislePrimaryContainer = PrimaryContainerDark
val AisleOnPrimaryContainer = OnPrimaryContainerDark
val AisleSecondary = SecondaryDark
val AisleOnSecondary = OnSecondaryDark
val AisleSecondaryContainer = SecondaryContainerDark
val AisleOnSecondaryContainer = OnSecondaryContainerDark
val AisleTertiary = TertiaryDark
val AisleOnTertiary = OnTertiaryDark
val AisleTertiaryContainer = TertiaryContainerDark
val AisleOnTertiaryContainer = OnTertiaryContainerDark
val AisleSurfaceVariant = SurfaceVariantDark
val AisleOnSurfaceVariant = OutlineVariantDark
val AisleSurface = SurfaceContainerDark
val AisleOnSurface = White
