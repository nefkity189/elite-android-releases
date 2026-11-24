package com.elite.sch.app.ui.components

import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteTrack
import com.elite.sch.app.ui.theme.EliteCardBottom
import com.elite.sch.app.ui.theme.EliteCardTop
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.ActionGradient
import com.elite.sch.app.ui.theme.EliteActionOrange
import com.elite.sch.app.ui.theme.EliteBackground
import com.elite.sch.app.ui.theme.EliteBorder
import com.elite.sch.app.ui.theme.EliteTextPrimary
import com.elite.sch.app.ui.theme.EliteTextSecondary
import com.elite.sch.app.ui.theme.EliteTurquoise
import com.elite.sch.app.ui.theme.BrandGradient
import com.elite.sch.app.ui.theme.BrandButtonGradient
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.Text as EliteTextColor
import com.elite.sch.app.ui.theme.OnRed
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.R

/**
 * Cartão padrão Elite: fundo Card, borda 1 dp LineStrong, cantos 18 dp (SPEC seção 5).
 * Card selecionado: borda Olive, fundo degradê OliveSoft→Card, barra 3dp à esquerda.
 */
@Composable
fun EliteCard(
    modifier: Modifier = Modifier,
    border: BorderStroke? = BorderStroke(1.dp, LineStrong),
    onClick: (() -> Unit)? = null,
    selected: Boolean = false,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
) {
    val cardBackground: androidx.compose.ui.graphics.Brush = if (selected) {
        Brush.verticalGradient(listOf(OliveSoft, CardColor))
    } else {
        androidx.compose.ui.graphics.SolidColor(CardColor)
    }
    val cardBorder = if (selected) BorderStroke(1.dp, Olive) else border

    Surface(
        shape = shape,
        color = Color.Transparent,
        border = cardBorder,
        onClick = { onClick?.invoke() },
        modifier = modifier
            .clip(shape)
            .background(cardBackground)
    ) {
        if (selected) {
            // Barra 3dp à esquerda, inset 14dp topo/baixo
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(3.dp)
                    .padding(top = 14.dp, bottom = 14.dp)
                    .background(Olive),
                contentAlignment = Alignment.CenterStart
            ) {
            }
        }
        content()
    }
}

/**
 * Botão pílula principal Elite: altura 56, raio 28, fundo Olive sólido (SPEC seção 5).
 * Gradientes teal→roxo não existem mais.
 */
@Composable
fun BrandGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(if (enabled) Olive else Olive.copy(alpha = 0.4f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (enabled) OnOlive else OnOlive.copy(alpha = 0.6f)
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

/**
 * Botão pílula de ação Elite (vermelho): altura 56, raio 28, fundo Red sólido.
 * Usado para "Começar treino", "Parar", ações destrutivas.
 */
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    horizontalPadding: Dp = 24.dp,
    verticalPadding: Dp = 14.dp,
    textStyle: TextStyle? = null,
    maxLines: Int = 1
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(if (enabled) Red else Red.copy(alpha = 0.4f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle ?: MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (enabled) OnRed else OnRed.copy(alpha = 0.6f),
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

/**
 * Bloco de métrica: valor grande em cima (ExtraBold com fontFeatureSettings = "tnum")
 * e rótulo pequeno embaixo.
 */
@Composable
fun MetricBlock(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = EliteTextPrimary,
    labelColor: Color = EliteTextSecondary,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = value,
            style = EliteNumberStyles.MetricMedium,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(EliteSpacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = labelColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = if (horizontalAlignment == Alignment.CenterHorizontally) TextAlign.Center else TextAlign.Start
        )
    }
}

/**
 * Anel de progresso desenhado em Canvas com valor no centro.
 * Traço de 12 dp com pontas arredondadas, trilha #1C253D e traço em gradiente.
 */
@Composable
fun ProgressRing(
    progress: Float, // 0f a 1f
    color: Color = EliteTurquoise,
    gradient: Brush? = null,
    valueText: String,
    labelText: String = "",
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = EliteTrack
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val activeBrush = gradient ?: Brush.linearGradient(listOf(color, color))

    Box(
        modifier = modifier.size(128.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.minDimension - strokePx
            val topLeft = Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)

            // Trilha de fundo (#1C253D)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Arco de progresso em gradiente com pontas arredondadas
            if (clampedProgress > 0f) {
                drawArc(
                    brush = activeBrush,
                    startAngle = -90f,
                    sweepAngle = clampedProgress * 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = EliteSpacing.sm)
        ) {
            Text(
                text = valueText,
                style = EliteNumberStyles.MetricMedium,
                color = EliteTextPrimary,
                maxLines = 1
            )
            if (labelText.isNotBlank()) {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.labelMedium,
                    color = EliteTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Símbolo Elite ("E" inclinada) — usa drawable ic_elite_mark (osso) ou ic_elite_blade (oliva).
 * A barra vermelha do meio NÃO deve ser tingida (tint).
 */
@Composable
fun EliteSymbol(
    modifier: Modifier = Modifier,
    useBlade: Boolean = false, // false = mark (osso), true = blade (oliva)
    size: Dp = 44.dp
) {
    val painter = if (useBlade) {
        androidx.compose.ui.res.painterResource(id = R.drawable.ic_elite_blade)
    } else {
        androidx.compose.ui.res.painterResource(id = R.drawable.ic_elite_mark)
    }
    androidx.compose.foundation.Image(
        painter = painter,
        contentDescription = null,
        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
        modifier = modifier.size(size)
    )
}

/**
 * Logotipo Elite completo (símbolo + texto "ELITE" em Saira Stencil One).
 * SPEC seção 2: símbolo 46x46 + gap 12 + "ELITE" 26sp Saira Stencil One, caixa alta, ls 0.24em, cor osso.
 * Abaixo: 8sp Inter 600, ls 0.13em, cor muted: "MISSÃO DADA É MISSÃO CUMPRIDA"
 */
@Composable
fun EliteLogo(
    modifier: Modifier = Modifier,
    symbolSize: Dp = 46.dp,
    showTagline: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        EliteSymbol(modifier = Modifier.size(symbolSize), useBlade = false)
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ELITE",
                fontFamily = EliteLogoFont,
                fontWeight = FontWeight.Normal,
                fontSize = 26.sp,
                letterSpacing = 6.24.sp, // 0.24em
                color = EliteTextColor
            )
            if (showTagline) {
                Text(
                    text = "MISSÃO DADA É MISSÃO CUMPRIDA",
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 8.sp,
                    letterSpacing = 1.04.sp, // 0.13em
                    color = TextMuted
                )
            }
        }
    }
}

/**
 * Arcos de onda decorativos para os lados do botão de ação da corrida.
 */
@Composable
fun WaveArcs(
    modifier: Modifier = Modifier,
    isLeft: Boolean = true,
    color: Color = EliteActionOrange.copy(alpha = 0.8f)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 2.5f

        val startAngle = if (isLeft) 110f else -70f
        val sweepAngle = 140f

        val size1 = Size(w * 0.6f, h * 0.7f)
        val topLeft1 = if (isLeft) Offset(w * 0.2f, h * 0.15f) else Offset(w * 0.2f, h * 0.15f)
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft1,
            size = size1,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )

        val size2 = Size(w * 0.9f, h * 0.95f)
        val topLeft2 = if (isLeft) Offset(0f, h * 0.025f) else Offset(w * 0.1f, h * 0.025f)
        drawArc(
            color = color.copy(alpha = color.alpha * 0.5f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft2,
            size = size2,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
    }
}


/** Cores de campos de texto com borda de contraste >= 3:1 (#6A759E; foco turquesa). */
@Composable
fun eliteTextFieldColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = EliteTurquoise,
    unfocusedBorderColor = com.elite.sch.app.ui.theme.EliteFieldBorder,
    focusedTextColor = EliteTextPrimary,
    unfocusedTextColor = EliteTextPrimary,
    focusedLabelColor = EliteTurquoise,
    unfocusedLabelColor = EliteTextSecondary,
    cursorColor = EliteTurquoise
)
