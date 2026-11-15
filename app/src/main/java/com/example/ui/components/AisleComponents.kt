package com.example.ui.components

import com.example.ui.theme.AisleNumberStyles
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTrack
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AisleCardTop
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActionGradient
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandButtonGradient

/**
 * Fundo com leve gradiente vertical de #17203A para #121829.
 * Visivelmente mais claro que o fundo #0B1020 do app.
 */
val AisleCardGradient = Brush.verticalGradient(
    listOf(AisleCardTop, AisleCardBottom)
)

/**
 * Cartão padrão Aisle: fundo com leve gradiente vertical de #17203A para #121829,
 * borda de 1 dp #232B42 e cantos de 20 dp.
 */
@Composable
fun AisleCard(
    modifier: Modifier = Modifier,
    border: BorderStroke? = BorderStroke(1.dp, AisleBorder),
    onClick: (() -> Unit)? = null,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit
) {
    Surface(
        shape = shape,
        color = Color.Transparent,
        border = border,
        modifier = modifier
            .clip(shape)
            .background(AisleCardGradient)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
    ) {
        content()
    }
}

/**
 * Botão pílula com gradiente da marca (Turquesa para Roxo).
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
        shape = CircleShape,
        color = Color.Transparent,
        modifier = modifier
            .clip(CircleShape)
            .background(if (enabled) BrandButtonGradient else Brush.horizontalGradient(listOf(AisleBorder, AisleBorder)))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (enabled) AisleTextPrimary else AisleTextSecondary
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

/**
 * Botão pílula de ação com gradiente laranja (#FF8A3D para #FF5A1F).
 */
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Surface(
        shape = CircleShape,
        color = Color.Transparent,
        modifier = modifier
            .clip(CircleShape)
            .background(if (enabled) ActionGradient else Brush.horizontalGradient(listOf(AisleBorder, AisleBorder)))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp)
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (enabled) AisleBackground else AisleTextSecondary
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
    valueColor: Color = AisleTextPrimary,
    labelColor: Color = AisleTextSecondary,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = value,
            style = AisleNumberStyles.MetricMedium,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(AisleSpacing.xs))
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
    color: Color = AisleTurquoise,
    gradient: Brush? = null,
    valueText: String,
    labelText: String = "",
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = AisleTrack
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
            modifier = Modifier.padding(horizontal = AisleSpacing.sm)
        ) {
            Text(
                text = valueText,
                style = AisleNumberStyles.MetricMedium,
                color = AisleTextPrimary,
                maxLines = 1
            )
            if (labelText.isNotBlank()) {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.labelMedium,
                    color = AisleTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Símbolo "A" minimalista estilizado em vetor Canvas.
 */
@Composable
fun AisleSymbol(
    modifier: Modifier = Modifier,
    brush: Brush = BrandGradient,
    strokeWidth: Float = 6f
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            // Perna esquerda
            moveTo(w * 0.18f, h * 0.85f)
            lineTo(w * 0.5f, h * 0.15f)
            // Perna direita
            lineTo(w * 0.82f, h * 0.85f)
        }

        drawPath(
            path = path,
            brush = brush,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Traço horizontal do A
        drawLine(
            brush = brush,
            start = Offset(w * 0.32f, h * 0.60f),
            end = Offset(w * 0.68f, h * 0.60f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Logotipo Aisle completo (símbolo + texto).
 */
@Composable
fun AisleLogo(
    modifier: Modifier = Modifier,
    symbolSize: Dp = 44.dp,
    symbolBrush: Brush = androidx.compose.ui.graphics.SolidColor(Color.White),
    textColor: Color = Color.White
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AisleSymbol(
            modifier = Modifier.size(symbolSize),
            brush = symbolBrush,
            strokeWidth = 8f
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Aisle",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Black,
            color = textColor,
            letterSpacing = (-1).sp
        )
    }
}

/**
 * Arcos de onda decorativos para os lados do botão de ação da corrida.
 */
@Composable
fun WaveArcs(
    modifier: Modifier = Modifier,
    isLeft: Boolean = true,
    color: Color = AisleActionOrange.copy(alpha = 0.8f)
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
fun aisleTextFieldColors() = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AisleTurquoise,
    unfocusedBorderColor = com.example.ui.theme.AisleFieldBorder,
    focusedTextColor = AisleTextPrimary,
    unfocusedTextColor = AisleTextPrimary,
    focusedLabelColor = AisleTurquoise,
    unfocusedLabelColor = AisleTextSecondary,
    cursorColor = AisleTurquoise
)
