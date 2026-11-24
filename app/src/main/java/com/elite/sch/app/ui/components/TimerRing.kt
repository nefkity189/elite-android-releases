package com.elite.sch.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elite.sch.app.ui.theme.RunnaNumberStyles

@Composable
fun TimerRing(
    timeFormatted: String,
    progress: Float = 0.0f,
    progressColor: androidx.compose.ui.graphics.Color = RunnaTheme.Teal,
    textColor: androidx.compose.ui.graphics.Color = RunnaTheme.Text,
    modifier: Modifier = Modifier,
    testTag: String = "timer_ring"
) {
    Box(
        modifier = modifier.size(RunnaTheme.TimerSize).testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(RunnaTheme.TimerSize)) {
            val c = size.minDimension / 2f
            val strokePx = RunnaTheme.TimerStroke.toPx()
            val r = size.minDimension / 2f - strokePx

            // Fundo de vidro (glass) com borda fina Line
            drawCircle(color = RunnaTheme.Glass, radius = size.minDimension / 2f, center = Offset(c, c))
            drawCircle(color = RunnaTheme.Line, radius = r, center = Offset(c, c), style = Stroke(width = strokePx))

            // Anel de progresso 4px com pontas arredondadas
            val sweep = 360f * progress
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(c - r, c - r),
                size = Size(r * 2, r * 2),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Text(
            text = timeFormatted,
            style = RunnaNumberStyles.Timer,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
