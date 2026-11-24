package com.elite.sch.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.elite.sch.app.ui.theme.EliteActionOrange
import com.elite.sch.app.ui.theme.EliteCard
import com.elite.sch.app.ui.theme.EliteTurquoise

/**
 * Botão circular central com anel de progresso radial, inspirado no paradigma de
 * apps de treino: toque alterna entre Play (Retomar) e Pause, e o anel desenha
 * a fração [progress] (0..1) do arco em torno do botão.
 *
 * O anel é desenhado via Canvas para evitar dependência de bibliotecas externas
 * e para permitir cor e largura personalizadas.
 *
 * @param isPlaying Se true, exibe ícone Pause e anel turquesa; caso contrário, Play e laranja.
 * @param progress Fração 0..1 do anel preenchida.
 * @param onClick Ação ao tocar no botão.
 */
@Composable
fun PlayPauseRing(
    isPlaying: Boolean,
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val size = 64.dp
    val ringColor = if (isPlaying) EliteTurquoise else EliteActionOrange
    val progressFraction = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Anel de progresso radial
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Trilha do anel (fundo)
            drawCircle(
                color = ringColor.copy(alpha = 0.18f),
                radius = size.toPx() / 2
            )
            // Arco de progresso
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = progressFraction * 360f,
                useCenter = false,
                size = Size(size.toPx(), size.toPx()),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Botão central
        Surface(
            shape = CircleShape,
            color = EliteCard,
            border = androidx.compose.foundation.BorderStroke(2.dp, ringColor),
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .semantics {
                    contentDescription = if (isPlaying) "Pausar treino" else "Retomar treino"
                }
                .testTag("btn_play_pause_ring")
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = ringColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}