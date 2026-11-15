package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.RunState
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextAccent2
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTrack
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.AisleNumberStyles
import com.example.ui.components.rememberAisleWindowSizeClass
import com.example.ui.components.rememberTimerFontScale
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * Layout dedicado à Corrida Livre: timer grande centralizado com indicador visual
 * de ritmo e subtítulo motivacional.
 */
@Composable
fun FreeRunLayout(
    runState: RunState,
    totalTimeFormatted: String,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = rememberAisleWindowSizeClass()
    val timerFontScale = rememberTimerFontScale(windowSizeClass)
    val circleSize = when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 240.dp
        WindowWidthSizeClass.MEDIUM -> 280.dp
        WindowWidthSizeClass.EXPANDED -> 320.dp
        else -> 240.dp
    }

    val pace = runState.averagePaceSeconds
    val paceFormatted = runState.averagePaceFormatted
    val paceColor = when {
        pace == null -> AisleTextSecondary
        pace <= 300 -> AisleTurquoise      // ≤ 5:00 min/km — rápido
        pace <= 420 -> AisleTextPrimary    // ≤ 7:00 min/km — moderado
        else -> AisleTextAccent2           // > 7:00 min/km — devagar
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
    ) {
        // Cabeçalho discreto com badge "Corrida Livre"
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(AisleTurquoise.copy(alpha = 0.14f))
                .padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.xxs),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CORRIDA LIVRE",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = AisleTurquoise
            )
        }

        // Timer + anel de ritmo
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(if (pace != null) paceColor.copy(alpha = 0.18f) else AisleTrack),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = totalTimeFormatted,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFeatureSettings = "tnum",
                            fontSize = (56 * timerFontScale).sp
                        ),
                        color = AisleTextPrimary
                    )
                    if (pace != null) {
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        Text(
                            text = paceFormatted,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = paceColor
                        )
                    }
                }
            }
        }

        // Linha separadora sutil + métricas em par
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MetricBlock(
                label = "Distância",
                value = "%.2f km".format(runState.totalDistanceMeters / 1000.0),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier.width(1.dp).height(28.dp).background(AisleTrack)
            )
            MetricBlock(
                label = "Ritmo (min/km)",
                value = runState.averagePaceFormatted,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Mantenha o seu ritmo",
            style = MaterialTheme.typography.bodyLarge,
            color = AisleTextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
