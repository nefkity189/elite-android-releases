package com.elite.sch.app.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tema centralizado da tela "Corrida livre" estilo Runna.
 * Valores idênticos ao HTML de referência (Corrida livre – layout corrigido.html).
 */
object RunnaTheme {

    // ===== PALETA (idêntica ao HTML) =====
    val Background = Color(0xFF0A0B09)      // Elite Background
    val Glass = Color(0xD110120D)      // Elite Surface a 82%
    val GlassElevated = Color(0xEB1D2116)      // Elite CardElevated a 92%
    val Line = Color(0x1AECE8DA)      // osso a 10%: borda 1px
    val Text = Color(0xFFECE8DA)      // osso
    val Muted = Color(0xFF8F9384)      // Elite TextMuted
    val Teal = Color(0xFFA3B565)      // Elite Olive (era teal)
    val TealOn = Color(0xFF0E1306)      // texto sobre Olive
    val Orange = Color(0xFFD12B2B)      // Elite Red (era laranja)
    val OrangeOn = Color(0xFFFFF4F0)      // texto sobre Red
    val RedDot = Color(0xFFD12B2B)      // ponto vermelho sem sinal
    val ItemOffBg = Color(0xFF1D2116)      // botao radial desligado: CardElevated

    val Card = Color(0xFF161911)      // fundo de cards
    val Warm = Color(0xFFC9A24B)      // aquecimento/desaquecimento: Brass
    val Walk = Color(0xFF4A4E40)      // caminhada: StepRest

    // ===== DIMENSÕES (idênticas ao HTML) =====
    val TopHeight = 64.dp
    val TopPadding = 16.dp
    val ChipHeight = 32.dp
    val ChipDotSize = 7.dp

    // Gradiente único contínuo idêntico ao HTML .shade (SPEC):
    // 0.00: 0.96, 0.30: 0.94, 0.43: 0.90, 0.53: 0.00, 0.76: 0.00, 1.00: 0.85
    data class GradientStop(val fraction: Float, val alpha: Float)
    val GradientStops = listOf(
        GradientStop(0.00f, 0.96f),
        GradientStop(0.30f, 0.94f),
        GradientStop(0.43f, 0.90f),
        GradientStop(0.53f, 0.00f),
        GradientStop(0.76f, 0.00f),
        GradientStop(1.00f, 0.85f)
    )
    val TopGradientHeightPercent = 0.53f
    val TopGradientHeightDp = 451.dp

    // Radial menu
    val FabSize = 56.dp
    val TopFabSize = 56.dp
    val ArcSize = 132.dp
    val ItemSize = 44.dp
    val ItemRadius = 80.dp
    val IconSize = 21.dp
    val ItemIconSize = 22.dp

    // Banner
    val BannerRadius = 14.dp
    val BannerTextSize = 12.5.sp

    // Hero / Timer
    val TimerSize = 112.dp
    val TimerStroke = 4.dp
    val TimerTextSize = 34.sp
    val ModeTextSize = 12.sp
    val StatsNumberSize = 34.sp
    val StatsUnitSize = 16.sp
    val StatsLabelSize = 12.sp
    val StatsDividerHeight = 34.dp
    val StatsRowHeight = 64.dp

    // Spacer overlays
    val FollowPillRadius = 15.dp
    val FollowTextSize = 12.5.sp
    val LocateSize = 46.dp

    // Action bar
    val ActionBarRadius = 26.dp
    val ActionBarPadding = 16.dp
    val ActionBarGap = 12.dp
    val ButtonHeight = 56.dp
    val ButtonRadius = 28.dp
    val ButtonTextSize = 16.sp
    val ButtonIconSize = 18.dp
    val ActionBarBottomPad = 18.dp

    // ===== FORMAS =====
    val ChipShape = RoundedCornerShape(16.dp)
    val BannerShape = RoundedCornerShape(BannerRadius)
    val TimerShape = CircleShape
    val FollowPillShape = RoundedCornerShape(FollowPillRadius)
    val LocateShape = CircleShape
    val ActionBarShape = RoundedCornerShape(topStart = ActionBarRadius, topEnd = ActionBarRadius)
    val ButtonShape = RoundedCornerShape(ButtonRadius)
    val ArcShape = RoundedCornerShape(bottomStart = 132.dp)  // 0 0 0 100%
}