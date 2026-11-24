package com.elite.sch.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Font resources - these need to be added to res/font/
// Barlow Condensed: barlow_condensed_400.ttf, barlow_condensed_600.ttf
// Inter: inter_400.ttf, inter_500.ttf, inter_600.ttf

private val BarlowCondensedRegular = Font(R.font.barlow_condensed_400, FontWeight.Normal)
private val BarlowCondensedMedium = Font(R.font.barlow_condensed_500, FontWeight.Medium)
private val BarlowCondensedSemiBold = Font(R.font.barlow_condensed_600, FontWeight.SemiBold)

private val InterRegular = Font(R.font.inter_400, FontWeight.Normal)
private val InterMedium = Font(R.font.inter_500, FontWeight.Medium)
private val InterSemiBold = Font(R.font.inter_600, FontWeight.SemiBold)

val BarlowCondensedFontFamily = FontFamily(
    BarlowCondensedRegular,
    BarlowCondensedMedium,
    BarlowCondensedSemiBold
)

val InterFontFamily = FontFamily(
    InterRegular,
    InterMedium,
    InterSemiBold
)

private val DarkColorScheme = darkColorScheme(
    primary = EliteTeal,
    onPrimary = EliteTealOn,
    secondary = EliteBrass,
    onSecondary = Color.Black,
    tertiary = EliteAmber,
    onTertiary = Color.Black,
    error = EliteRed,
    onError = EliteRedOn,
    background = EliteBackground,
    onBackground = EliteText,
    surface = EliteGlass,
    onSurface = EliteText,
    surfaceVariant = EliteCardElevated,
    onSurfaceVariant = EliteText,
    outline = EliteLine,
    outlineVariant = EliteLine,
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = EliteText,
    inverseOnSurface = EliteBackground,
    inversePrimary = EliteTeal,
)

private val LightColorScheme = lightColorScheme(
    primary = EliteTeal,
    onPrimary = EliteTealOn,
    secondary = EliteBrass,
    onSecondary = Color.Black,
    tertiary = EliteAmber,
    onTertiary = Color.Black,
    error = EliteRed,
    onError = EliteRedOn,
    background = Color.White,
    onBackground = Color.Black,
    surface = EliteGlass.copy(alpha = 0.9f),
    onSurface = Color.Black,
    surfaceVariant = EliteCardElevated.copy(alpha = 0.9f),
    onSurfaceVariant = Color.Black,
    outline = EliteLine.copy(alpha = 0.2f),
    outlineVariant = EliteLine.copy(alpha = 0.2f),
    shadow = Color.Black,
    scrim = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    inversePrimary = EliteTeal,
)

@Composable
fun EliteTheme(
    darkTheme: Boolean = true, // App is dark-only
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = EliteTypography
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

// Custom theme properties for easy access
val RunnaTheme = object {
    // Colors
    val Background = EliteBackground
    val Glass = EliteGlass
    val GlassElevated = EliteGlassElevated
    val Line = EliteLine
    val Text = EliteText
    val Muted = EliteMuted
    val Teal = EliteTeal
    val TealOn = EliteTealOn
    val Brass = EliteBrass
    val Amber = EliteAmber
    val Red = EliteRed
    val RedOn = EliteRedOn
    val ItemOffBg = EliteItemOffBg
    val CardElevated = EliteCardElevated
    
    // Dimensions
    val TopHeight = 64.dp
    val ChipSize = 32.dp
    val TopFabSize = 48.dp
    val LocateSize = 46.dp
    val ItemSize = 44.dp
    val ArcSize = 132.dp
    val RingSize = 112.dp
    val RingStroke = 4.dp
    val StatsHeight = 64.dp
    val ActionBarHeight = 90.dp
    val ActionBarButtonSize = 56.dp
    val ActionBarButtonRadius = 28.dp
    val ActionBarRadius = 26.dp
    val TopPadding = 16.dp
    val FollowPillRadius = 15.dp
    val FollowTextSize = 13.sp
    val IconSize = 24.dp
    val ItemIconSize = 20.dp
    
    // Shapes
    val FollowPillShape = androidx.compose.foundation.shape.RoundedCornerShape(FollowPillRadius)
    val CardShape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    
    // Action bar paddings and gaps
    val ActionBarPadding = 16.dp
    val ActionBarGap = 12.dp
    
    // Gradient stops for the map overlay
    val GradientStops = listOf(
        0.0f to 0.96f,
        0.30f to 0.94f,
        0.43f to 0.90f,
        0.53f to 0.0f,
        0.76f to 0.0f,
        1.0f to 0.85f,
    )
    
    // Animation durations
    val FanArcDuration = 250
    val FanItemDuration = 280
    val FanItemDelay = 50
    val RadialArcDuration = 250
    val RadialItemDuration = 280
    val RadialItemDelay = 50
    val RadialAnchorDuration = 300
    val PressScaleDuration = 150 // spring
    val StopHoldDuration = 1500
}

// Aliases for backward compatibility with existing code
val Background = EliteBackground
val Glass = EliteGlass
val GlassElevated = EliteGlassElevated
val Line = EliteLine
val LineStrong = EliteLine.copy(alpha = 0.2f)
val Text = EliteText
val Muted = EliteMuted
val Teal = EliteTeal
val Olive = EliteTeal // Alias for Teal
val TealOn = EliteTealOn
val Brass = EliteBrass
val Amber = EliteAmber
val Red = EliteRed
val RedOn = EliteRedOn
val CardElevated = EliteCardElevated
val ItemOffBg = EliteItemOffBg

// Glass variants
val RunnaGlass = EliteGlass
val RunnaGlassBorder = BorderStroke(1.dp, EliteLine)
val GlassBar = EliteGlass

// Spacing
val EliteSpacing = object {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}

// Top scrim gradient stops
val EliteTopScrimStops = listOf(
    0.0f to 0.96f,
    0.30f to 0.94f,
    0.43f to 0.90f,
    0.53f to 0.0f,
    0.76f to 0.0f,
    1.0f to 0.85f,
)

// Track color for progress indicators
val EliteTrack = EliteLine

// Missing theme properties from existing code
val EliteBottomBarSurface = EliteGlass
val EliteBorder = EliteLine
val ElitePurple = Color(0xFF673AB7) // Placeholder, adjust if actual value is found
val EliteTextPrimary = EliteText
val EliteTextSecondary = EliteMuted
val EliteTurquoise = EliteTeal // Placeholder, adjust if actual value is found
val BrandGradient = listOf(EliteTeal, EliteBrass) // Placeholder, adjust if actual value is found
val EliteActionOrange = Color(0xFFFFA726) // Placeholder, adjust if actual value is found
val EliteCard = EliteCardElevated

@Composable
fun rememberReducedMotion(): Boolean {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    return configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
        // Actually check for reduced motion accessibility setting
        // This is a placeholder - in real app use:
        // configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
}

val EliteTypography = androidx.compose.material3.Typography(
    displayLarge = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 57.sp,
        lineHeight = 64.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = BarlowCondensedFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(400),
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(400),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
)