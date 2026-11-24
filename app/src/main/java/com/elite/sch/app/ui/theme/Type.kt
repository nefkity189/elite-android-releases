package com.elite.sch.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.elite.sch.app.R

private const val TNUM = "tnum"

// ============================================================
// FAMÍLIAS DE FONTE ELITE (Barlow Condensed, Inter, Saira Stencil One)
// Fonte da verdade: SPEC seção 4. Arquivos em res/font/ (minúsculas com underscore).
// ============================================================

/** Barlow Condensed — números grandes, títulos de tela, títulos de cartão. */
val EliteDisplayFont: FontFamily = FontFamily(
    Font(R.font.barlow_condensed_medium, FontWeight.Medium),
    Font(R.font.barlow_condensed_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_condensed_bold, FontWeight.Bold)
)

/** Inter — corpo, rótulos, botões, chips, abas. */
val EliteBodyFont: FontFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

/** Saira Stencil One — apenas logotipo "ELITE". */
val EliteLogoFont: FontFamily = FontFamily(
    Font(R.font.saira_stencil_one_regular, FontWeight.Normal)
)

val RunnaNumberFont: FontFamily = EliteDisplayFont
val RunnaTextFont: FontFamily = EliteBodyFont

/** Tipografia Material3 baseada na tabela Elite (SPEC seção 4). */
val Typography = Typography(
    // Título de tela (t1): Barlow Condensed 30/32, 700, caixa alta, ls 0.03em
    displayLarge = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 30.sp, lineHeight = 32.sp, letterSpacing = 0.9.sp, fontFeatureSettings = TNUM
    ),
    displayMedium = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 30.sp, lineHeight = 32.sp, letterSpacing = 0.9.sp, fontFeatureSettings = TNUM
    ),
    // Título de topo: Barlow Condensed 24/24, 700, caixa alta, ls 0.04em
    headlineLarge = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold,
        fontSize = 24.sp, lineHeight = 24.sp, letterSpacing = 0.96.sp, fontFeatureSettings = TNUM
    ),
    // Título de cartão/seção (t2): Barlow Condensed 20/23, 600, caixa alta, ls 0.04em
    headlineMedium = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 23.sp, letterSpacing = 0.8.sp, fontFeatureSettings = TNUM
    ),
    headlineSmall = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 23.sp, letterSpacing = 0.8.sp, fontFeatureSettings = TNUM
    ),
    titleLarge = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 23.sp, letterSpacing = 0.8.sp, fontFeatureSettings = TNUM
    ),
    titleMedium = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 12.5.sp, lineHeight = 16.sp, letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 19.6.sp, letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 19.6.sp, letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 12.5.sp, lineHeight = 16.sp, letterSpacing = 0.15.sp
    ),
    labelSmall = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.132.sp
    )
)

/** Estilos numéricos Elite (Barlow Condensed, algarismos tabulares). */
object EliteNumberStyles {
    // Cronômetro (número grande): 32sp, 600, tabular
    val Timer = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp, lineHeight = 32.sp, fontFeatureSettings = TNUM
    )
    // Números de estatística (corrida): 40sp, 600, tabular
    val MetricLarge = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp, lineHeight = 40.sp, fontFeatureSettings = TNUM
    )
    // Números de estatística (cards): 30sp, 600, tabular
    val Metric = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp, lineHeight = 30.sp, fontFeatureSettings = TNUM
    )
    // Números de estatística (resumo): 28sp, 600, tabular
    val MetricMedium = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp, lineHeight = 28.sp, fontFeatureSettings = TNUM
    )
    val Small = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp, lineHeight = 24.sp, fontFeatureSettings = TNUM
    )
    val Tiny = TextStyle(
        fontFamily = EliteDisplayFont, fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp, lineHeight = 20.sp, fontFeatureSettings = TNUM
    )
    val Hero = Timer
    val HeroMedium = MetricLarge
}

object RunnaNumberStyles {
    val Timer = EliteNumberStyles.Timer
    val MetricLarge = EliteNumberStyles.MetricLarge
    val MetricUnit = EliteNumberStyles.Small
}

/** Estilos de texto Elite (Inter). */
object EliteTextStyles {
    // Rótulo (lb): Inter 11, 600, caixa alta, ls 0.12em, cor muted
    val Label = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 1.32.sp
    )
    // Chip/aba ativa: Inter 12.5, 500
    val ChipActive = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Medium,
        fontSize = 12.5.sp, lineHeight = 16.sp, letterSpacing = 0.15.sp
    )
    // Chip/aba inativa: Inter 11, 400
    val ChipInactive = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.132.sp
    )
    // Botão: Inter 16 (44dp: 14), 700
    val Button = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Bold,
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.sp
    )
    // Botão pequeno (44dp): Inter 14, 700
    val ButtonSmall = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Bold,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.sp
    )
    // Corpo: Inter 14/19.6, 400
    val Body = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 19.6.sp, letterSpacing = 0.sp
    )
    // Corpo pequeno: Inter 12, 400
    val BodySmall = TextStyle(
        fontFamily = EliteBodyFont, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.sp
    )
}

/** Aliases de compatibilidade. */
object RunnaTextStyles {
    val Label = EliteTextStyles.Label
    val LabelSmall = EliteTextStyles.BodySmall
    val Button = EliteTextStyles.Button
    val BodySmall = EliteTextStyles.BodySmall
}

