package com.elite.sch.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.RunnaTheme

@Composable
fun TimerRing(
    timeFormatted: String,
    progress: Float,
    progressColor: Color = RunnaTheme.Teal,
    textColor: Color = RunnaTheme.Text,
    modifier: Modifier = Modifier,
    testTag: String = "timer_ring"
) {
    val radius = RunnaTheme.RingSize / 2
    val strokeWidth = RunnaTheme.RingStroke
    val sweep = progress * 360f

    Box(
        modifier = modifier
            .size(RunnaTheme.RingSize)
            .testTag(testTag)
    ) {
        // Background circle (glass)
        Box(
            modifier = Modifier
                .size(RunnaTheme.RingSize)
                .background(RunnaTheme.Glass)
                .border(strokeWidth, RunnaTheme.Line)
                .clip(CircleShape)
        )

        // Progress arc (teal or amber) - starts at top (-90°), goes clockwise
        Canvas(
            modifier = Modifier.size(RunnaTheme.RingSize)
        ) {
            val center = size.center
            val arcRadius = (size.minDimension - strokeWidth.toPx()) / 2
            val startAngle = -90f
            val sweepAngle = sweep

            drawArc(
                color = progressColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(
                    center.x - arcRadius,
                    center.y - arcRadius
                ),
                size = androidx.compose.ui.geometry.Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = strokeWidth.toPx(), cap = Stroke.Cap.Round)
            )
        }

        // Centered text
        Box(
            modifier = Modifier
                .size(RunnaTheme.RingSize)
                .align(Alignment.Center)
        ) {
            Text(
                text = timeFormatted,
                fontSize = 32.sp,
                color = textColor,
                fontWeight = androidx.compose.ui.text.style.FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}