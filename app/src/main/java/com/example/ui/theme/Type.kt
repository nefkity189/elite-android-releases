package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Famílias de fonte. Para usar Barlow Condensed / Barlow, coloque os .ttf em res/font
 * e troque apenas estas duas linhas (por FontFamily(Font(R.font....))).
 */
val AisleDisplayFont: FontFamily = FontFamily.SansSerif
val AisleBodyFont: FontFamily = FontFamily.SansSerif

private const val TNUM = "tnum"

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 56.sp, lineHeight = 60.sp, letterSpacing = (-1.5).sp, fontFeatureSettings = TNUM
    ),
    displayMedium = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 56.sp, lineHeight = 60.sp, letterSpacing = (-1).sp, fontFeatureSettings = TNUM
    ),
    headlineLarge = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Black,
        fontSize = 34.sp, lineHeight = 40.sp, letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 28.sp, lineHeight = 34.sp, letterSpacing = (-0.2).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 30.sp, letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp, lineHeight = 24.sp, letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.3.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.2.sp
    ),
    bodySmall = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = AisleBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.3.sp
    )
)

/** Estilos numéricos (algarismos tabulares, não pulam a cada segundo). */
object AisleNumberStyles {
    val Hero = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.ExtraBold,
        fontSize = 56.sp, lineHeight = 60.sp, fontFeatureSettings = TNUM
    )
    val HeroMedium = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 44.sp, lineHeight = 48.sp, fontFeatureSettings = TNUM
    )
    val Metric = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp, lineHeight = 36.sp, fontFeatureSettings = TNUM
    )
    val MetricMedium = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 26.sp, lineHeight = 30.sp, fontFeatureSettings = TNUM
    )
    val Small = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 20.sp, lineHeight = 24.sp, fontFeatureSettings = TNUM
    )
    val Tiny = TextStyle(
        fontFamily = AisleDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 20.sp, fontFeatureSettings = TNUM
    )
}
