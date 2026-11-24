package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

// ============================================================
// ELITE COLORS — Paleta Elite (SPEC seção 3)
// Tema escuro profissional inspirado em operações táticas
// Fonte da verdade: HANDOFF/elite/theme/EliteColors.kt e SPEC.md
// ============================================================

// --- Camada 1: Primitivos Elite (valores brutos) ---
private val EliteBackgroundPrimitive    = Color(0xFF0A0B09) // fundo das telas, base do scrim
private val EliteSurfacePrimitive       = Color(0xFF10120D) // barra inferior, sheets, base do vidro
private val EliteCardPrimitive          = Color(0xFF161911) // cards
private val EliteCardElevatedPrimitive  = Color(0xFF1D2116) // arco do menu, trilhas, item inativo
private val EliteFieldPrimitive         = Color(0xFF0D0F0A) // campos de texto
private val EliteLinePrimitive          = Color(0x1AECE8DA) // 10% osso: bordas 1 px, divisores
private val EliteLineStrongPrimitive    = Color(0x2EECE8DA) // 18% osso: bordas de campo, sheet, botão fantasma

private val EliteTextPrimitive          = Color(0xFFECE8DA) // osso
private val EliteTextMutedPrimitive     = Color(0xFF8F9384)
private val EliteTextDimPrimitive       = Color(0xFF5E6255) // desabilitado

private val EliteOlivePrimitive         = Color(0xFFA3B565) // seleção, Pausar, abas ativas, progresso
private val EliteOliveDarkPrimitive     = Color(0xFF5D6B34) // switch ligado, bordas de chip ativo
private val EliteOliveSoftPrimitive     = Color(0x24A3B565) // 14%: fundo de chip/ícone ativo
private val EliteOnOlivePrimitive       = Color(0xFF0E1306)
private val EliteRedPrimitive           = Color(0xFFD12B2B) // Começar treino, Parar, destrutivo
private val EliteRedSoftPrimitive       = Color(0x26D12B2B)
private val EliteOnRedPrimitive         = Color(0xFFFFF4F0)
private val EliteBrassPrimitive         = Color(0xFFC9A24B) // conquistas, patente, destaques secundários
private val EliteAmberPrimitive         = Color(0xFFE0A93A) // avisos de saúde, pausa
private val EliteOkPrimitive            = Color(0xFF7FB069) // GPS ativo, ponto verde

// Glass (backdrop blur não é reproduzível em Compose: usar só o fundo)
private val EliteGlassPrimitive         = Color(0xD110120D) // 82%
private val EliteGlassBarPrimitive      = Color(0xE610120D) // 90%: barra de ações da corrida
private val EliteStepRestPrimitive      = Color(0xFF4A4E40) // blocos de "caminhar" no gráfico de intensidade

// --- Camada 2: SEMÂNTICOS (usar nas telas e componentes) ---

// Superfícies principais
val Background      = EliteBackgroundPrimitive
val Surface         = EliteSurfacePrimitive
val Card            = EliteCardPrimitive
val CardElevated    = EliteCardElevatedPrimitive
val Field           = EliteFieldPrimitive
val Line            = EliteLinePrimitive
val LineStrong      = EliteLineStrongPrimitive

// Texto
val Text            = EliteTextPrimitive
val TextMuted       = EliteTextMutedPrimitive
val TextDim         = EliteTextDimPrimitive

// Ação e destaque
val Olive           = EliteOlivePrimitive
val OliveDark       = EliteOliveDarkPrimitive
val OliveSoft       = EliteOliveSoftPrimitive
val OnOlive         = EliteOnOlivePrimitive
val Red             = EliteRedPrimitive
val RedSoft         = EliteRedSoftPrimitive
val OnRed           = EliteOnRedPrimitive
val Brass           = EliteBrassPrimitive
val Amber           = EliteAmberPrimitive
val Ok              = EliteOkPrimitive

// Glass
val Glass           = EliteGlassPrimitive
val GlassBar        = EliteGlassBarPrimitive
val StepRest        = EliteStepRestPrimitive

// ============================================================
// CAMADA 2b: ALIASES DE COMPATIBILIDADE (Aisle / Runna existentes)
// MANTENHA OS NOMES; SÓ TROQUE OS VALORES PARA NÃO QUEBRAR O RESTO.
// ============================================================

// Aisle aliases
val AisleBackground = Background
val AisleCard       = Card
val AisleBorder     = LineStrong
val AisleTextPrimary   = Text
val AisleTextSecondary = TextMuted
val AisleTextTertiary  = TextDim
val AisleTurquoise     = Olive       // era teal → agora oliva
val AislePurple        = Brass       // era roxo → agora latão
val AisleTextAccent2   = Brass       // era roxo claro → latão
val AisleTealDark      = OliveDark   // era teal dark → oliva dark
val AisleFieldBorder   = LineStrong
val AisleActionOrange  = Red         // era laranja → agora vermelho
val AisleAlert         = Red

val AisleSuccess      = Ok
val AisleWarning      = Amber
val AisleError        = Red

// Runna aliases
val RunnaBackground       = Background
val RunnaGlass            = Glass
val RunnaGlassElevated    = GlassBar
val RunnaGlassBorder      = LineStrong
val RunnaTextPrimary      = Text
val RunnaTextSecondary    = TextMuted
val RunnaTeal             = Olive      // era teal → oliva
val RunnaTealOn           = OnOlive
val RunnaOrange           = Red        // era laranja → vermelho
val RunnaOrangeOn         = OnRed
val RunnaRedDot           = Red

// Settings / Profile screen additions (from SPEC perfil)
val TealSoft     = OliveSoft
val RedLine      = Red.copy(alpha = 0.35f)
val TrackOffBorder = LineStrong

// Superfícies específicas
val AisleBottomBarSurface = Surface
val AisleCardTop          = Card
val AisleCardBottom       = CardElevated
val AisleTrack            = CardElevated
val AisleOnAction         = OnRed
val AisleShadowOverlay    = Color(0x33000000)
val AisleDangerContainer  = RedSoft

// Gradientes
// IMPORTANTE: Gradientes teal→roxo (BrandGradient) DEIXAM DE EXISTIR.
// Use oliva sólido. Mantemos os nomes para compatibilidade, mas apontam para Olive.
val BrandGradient              = SolidColor(Olive)
val BrandButtonGradient        = SolidColor(Olive)
val ActionGradient             = SolidColor(Red)
val BrandGradientVertical      = SolidColor(Olive)
val CardGradient               = Brush.verticalGradient(listOf(Card, CardElevated))
val ActionGradientVertical     = SolidColor(Red)

// Status
val StatusCompleted = Olive
val StatusSkipped   = TextMuted
val StatusPlanned   = Brass

// ============================================================
// CAMADA 3: COMPONENTES
// ============================================================

/** Cores das etapas do treino, em um só lugar. */
object StepColors {
    val Warmup      = Brass
    val Run         = Olive
    val Walk        = TextMuted
    val Cooldown    = Brass.copy(alpha = 0.5f)
    val RunContainer = OliveSoft
    val WalkContainer = CardElevated
}

/** Cores dos marcadores de rota e dos anéis de meta. */
object AisleChartColors {
    val RouteStart        = Olive
    val RouteEnd          = Red
    val RingTurquoiseEnd  = Ok
    val RingPurpleEnd     = Brass
    val StreakContainer   = Red.copy(alpha = 0.18f)
    val StreakContent     = Red
}

val PauseBannerGradientEnd = CardElevated

// Compatibilidade com o MaterialTheme
val PrimaryDark       = Olive
val OnPrimaryDark     = OnOlive
val PrimaryContainerDark = OliveSoft
val OnPrimaryContainerDark = OnOlive
val SecondaryDark     = Brass
val OnSecondaryDark   = Text
val SecondaryContainerDark = Brass.copy(alpha = 0.14f)
val OnSecondaryContainerDark = Text
val TertiaryDark      = Red
val OnTertiaryDark    = OnRed
val TertiaryContainerDark = RedSoft
val OnTertiaryContainerDark = OnRed
val SurfaceVariantDark = CardElevated
val SurfaceContainerDark = Card
val SurfaceContainerHighDark = CardElevated
val OutlineVariantDark = LineStrong
val ScrimDark         = Color.Black
val InverseSurfaceDark = Text
val InverseOnSurfaceDark = Background

// Aliases para uso direto nas telas (compatibilidade semântica)
val AislePrimary              = PrimaryDark
val AisleOnPrimary            = OnPrimaryDark
val AislePrimaryContainer     = PrimaryContainerDark
val AisleOnPrimaryContainer   = OnPrimaryContainerDark
val AisleSecondary            = SecondaryDark
val AisleOnSecondary          = OnSecondaryDark
val AisleSecondaryContainer   = SecondaryContainerDark
val AisleOnSecondaryContainer = OnSecondaryContainerDark
val AisleTertiary             = TertiaryDark
val AisleOnTertiary           = OnTertiaryDark
val AisleTertiaryContainer    = TertiaryContainerDark
val AisleOnTertiaryContainer  = OnTertiaryContainerDark
val AisleSurfaceVariant       = SurfaceVariantDark
val AisleOnSurfaceVariant     = OutlineVariantDark
val AisleSurface              = SurfaceContainerDark
val AisleOnSurface            = Text


/** Paradas do degradê de cima sobre o mapa (fração da altura da tela -> alpha), cor base Background. */
val EliteTopScrimStops = arrayOf(
    0.00f to 0.96f, 0.30f to 0.94f, 0.43f to 0.90f, 0.53f to 0.00f, 0.76f to 0.00f, 1.00f to 0.85f,
)