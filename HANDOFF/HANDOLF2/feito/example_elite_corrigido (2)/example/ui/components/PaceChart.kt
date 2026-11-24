package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleTurquoise

/**
 * Mantém os últimos 30 valores de pace para desenhar a linha do gráfico.
 */
class PaceHistory {
    private val _history = mutableListOf<Double>()
    val history: List<Double> = _history

    fun addPace(pace: Double?) {
        if (pace == null) return
        _history.add(pace)
        if (_history.size > 30) {
            _history.removeAt(0)
        }
    }

    fun clear() {
        _history.clear()
    }
}

/**
 * Componente de gráfico de ritmo em tempo real.
 *
 * Exibe a linha do ritmo instantâneo do corredor em relação ao ritmo alvo do treino.
 * O gráfico é atualizado a cada segundo com os últimos valores de pace.
 */
@Composable
fun PaceChart(
    currentPaceSeconds: Double?,
    targetPaceSeconds: Double?,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp
) {
    val paceHistory = remember { PaceHistory() }

    LaunchedEffect(currentPaceSeconds) {
        paceHistory.addPace(currentPaceSeconds)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(chartHeight)
    ) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()

        // Determina o range de ritmo para o eixo Y (min/km em segundos)
        // Ex: 240s (4:00) a 900s (15:00)
        val minPace = 240.0   // 4:00 min/km (corrida rápida)
        val maxPace = 900.0   // 15:00 min/km (caminhada lenta)

        // Linha de ritmo alvo (tracejada)
        if (targetPaceSeconds != null) {
            val targetY = (height - ((targetPaceSeconds.coerceIn(minPace, maxPace) - minPace) / (maxPace - minPace)) * (height - padding * 2) - padding).toFloat()
            drawLine(
                color = AisleTurquoise.copy(alpha = 0.6f),
                start = Offset(padding, targetY),
                end = Offset(width - padding, targetY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        // Linha do ritmo instantâneo
        if (paceHistory.history.size > 1) {
            val points = paceHistory.history.mapIndexed { index, pace ->
                val x = padding + (index.toFloat() / (paceHistory.history.size - 1)) * (width - padding * 2)
                val y = (height - ((pace.coerceIn(minPace, maxPace) - minPace) / (maxPace - minPace)) * (height - padding * 2) - padding).toFloat()
                Offset(x, y)
            }

            // Gradiente de fundo sob a linha
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
                lineTo(points.last().x, height - padding)
                lineTo(points.first().x, height - padding)
                close()
            }

            // Desenha a área preenchida com gradiente
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AisleTurquoise.copy(alpha = 0.3f),
                        AisleTurquoise.copy(alpha = 0.0f)
                    ),
                    endY = height - padding
                )
            )

            // Desenha a linha principal
            drawPath(
                path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                },
                color = AisleTurquoise,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Marca o último ponto (ritmo atual)
            points.lastOrNull()?.let { last ->
                drawCircle(
                    color = AisleTurquoise,
                    center = last,
                    radius = 5.dp.toPx()
                )
                drawCircle(
                    color = AisleBackground,
                    center = last,
                    radius = 2.5.dp.toPx()
                )
            }
        }
    }
}