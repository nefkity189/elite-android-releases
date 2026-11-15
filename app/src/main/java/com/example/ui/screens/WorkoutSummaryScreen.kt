package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import kotlinx.coroutines.withContext
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.WorkoutLogEntity
import com.example.tracker.KmSplitsCalculator
import com.example.tracker.PaceCalculator
import com.example.tracker.PaceDataPoint
import com.example.tracker.PaceSeriesCalculator
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import com.example.ui.components.ActionButton
import com.example.ui.components.AisleCard
import com.example.ui.components.BrandGradientButton
import com.example.ui.components.MetricBlock
import com.example.ui.components.ShareCardData
import com.example.ui.components.ShareCardGenerator
import com.example.ui.components.WorkoutFormatters
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextAccent2
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.StatusCompleted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

@Composable
fun WorkoutSummaryScreen(
    workoutTitle: String,
    elapsedSeconds: Long,
    totalDistanceMeters: Double,
    modifier: Modifier = Modifier,
    routePoints: List<RoutePointEntity> = emptyList(),
    allCompletedLogs: List<WorkoutLogEntity> = emptyList(),
    initialRpe: Int? = null,
    initialAnotacoes: String? = null,
    isDetailOnly: Boolean = false,
    diagnostics: com.example.tracker.GpsDiagnostics? = null,
    ajustesAutomaticos: Boolean = true,
    onPainChange: (dor: Int, regiaoDor: String?) -> Unit = { _, _ -> },
    onSaveLog: (esforco: Int, anotacoes: String?) -> Unit,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var rpeValue by remember { mutableIntStateOf(initialRpe ?: 6) }
    var dorValue by remember { mutableIntStateOf(0) }
    var regiaoDorValue by remember { mutableStateOf<String?>(null) }
    var anotacoesInput by remember { mutableStateOf(initialAnotacoes ?: "") }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeFormatted = if (minutes >= 60) {
        val hours = minutes / 60
        val remMins = minutes % 60
        "%02d:%02d:%02d".format(hours, remMins, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
    val distanceKm = "%.2f km".format(totalDistanceMeters / 1000.0)

    val paceSeconds = PaceCalculator.calculateAveragePaceSeconds(elapsedSeconds, totalDistanceMeters)
    val paceFormatted = PaceCalculator.formatPace(paceSeconds)

    // Série de ritmo calculada pela função pura
    val paceSeries = remember(routePoints) {
        PaceSeriesCalculator.calculate(routePoints, windowSeconds = 30L)
    }

    // Seus recordes pessoais calculados entre todas as corridas concluídas + corrida atual
    val allDistances = allCompletedLogs.map { it.distanciaMetros } + totalDistanceMeters
    val allDurations = allCompletedLogs.map { it.duracaoSegundos } + elapsedSeconds
    val maxDistanceMeters = allDistances.maxOrNull() ?: totalDistanceMeters
    val maxDurationSeconds = allDurations.maxOrNull() ?: elapsedSeconds

    val maxDistanceFormatted = "%.2f km".format(maxDistanceMeters / 1000.0)
    val maxDurationFormatted = run {
        val m = maxDurationSeconds / 60
        val s = maxDurationSeconds % 60
        if (m >= 60) "%02d:%02d:%02d".format(m / 60, m % 60, s) else "%02d:%02d".format(m, s)
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .testTag("screen_workout_summary")
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(top = AisleSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
        ) {
            // Barra superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDetailOnly) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = AisleTextPrimary
                        )
                    }
                } else {
                    Surface(
                        shape = CircleShape,
                        color = StatusCompleted.copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = StatusCompleted, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Text(
                    text = if (isDetailOnly) "Detalhes da Corrida" else "Treino Concluído!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AisleTextPrimary
                )

                // 10. BOTÃO COMPARTILHAR (Abre compartilhamento nativo do Android)
                Surface(
                    shape = CircleShape,
                    color = AisleCard,
                    border = BorderStroke(1.dp, AisleBorder),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                val uri = withContext(Dispatchers.Default) {
                                    val shareData = ShareCardData(
                                        title = workoutTitle,
                                        distanceMeters = totalDistanceMeters.toInt(),
                                        elapsedSeconds = elapsedSeconds.toInt(),
                                        routePoints = routePoints
                                    )
                                    ShareCardGenerator.generateShareImage(context, shareData)
                                }

                                val distStr = WorkoutFormatters.formatDistance(totalDistanceMeters.toInt())
                                val timeStr = WorkoutFormatters.formatTime(elapsedSeconds.toInt())
                                val paceStr = WorkoutFormatters.formatPace(totalDistanceMeters.toInt(), elapsedSeconds.toInt())
                                val shareText = "Completei $workoutTitle no Aisle: $distStr em $timeStr • Pace $paceStr"

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    if (uri != null) {
                                        putExtra(Intent.EXTRA_STREAM, uri as android.os.Parcelable)
                                        type = "image/png"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                    } else {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        android.widget.Toast.makeText(context, "Não foi possível gerar a imagem. Compartilhando apenas o texto.", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Compartilhar no Aisle")
                                context.startActivity(shareIntent)
                            }
                        },
                        modifier = Modifier.testTag("btn_share_workout")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar",
                            tint = AisleTurquoise,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Hero
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = AisleSpacing.md), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = distanceKm, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = AisleTurquoise)
                Row(horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
                    MetricBlock("Tempo", timeFormatted)
                    MetricBlock("Ritmo Médio", paceFormatted)
                }
            }


            // CARTÃO COM A ROTA DESENHADA (Início e fim marcados)
            AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_route_canvas")) {
                if (routePoints.size >= 2) {
                    Column(modifier = Modifier.padding(AisleSpacing.md)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Trajeto percorrido",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = AisleTextSecondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(AisleSpacing.sm), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(AisleTurquoise, CircleShape))
                                Text("Início", style = MaterialTheme.typography.labelSmall, color = AisleTextSecondary)
                                Spacer(modifier = Modifier.width(AisleSpacing.xs))
                                Box(modifier = Modifier.size(8.dp).background(AisleActionOrange, CircleShape))
                                Text("Fim", style = MaterialTheme.typography.labelSmall, color = AisleTextSecondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(AisleSpacing.md))
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            val orderedPoints = routePoints.sortedBy { it.timestampMillis }
                            val coords = orderedPoints.map { RoutePointCoord(it.latitude, it.longitude) }
                            val normalized = RouteNormalizer.normalize(coords)
                            val pixelPoints = RouteNormalizer.toCanvasOffsets(
                                normalizedPoints = normalized,
                                widthPx = size.width,
                                heightPx = size.height,
                                paddingPx = 20f
                            )

                            if (pixelPoints.size >= 2) {
                                val path = Path().apply {
                                    moveTo(pixelPoints.first().first, pixelPoints.first().second)
                                    for (i in 1 until pixelPoints.size) {
                                        lineTo(pixelPoints[i].first, pixelPoints[i].second)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    brush = BrandGradient,
                                    style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )

                                // Marcador de Início (Turquesa com centro branco)
                                val start = pixelPoints.first()
                                drawCircle(color = AisleTurquoise, radius = 9f, center = Offset(start.first, start.second))
                                drawCircle(color = Color.White, radius = 4f, center = Offset(start.first, start.second))

                                // Marcador de Fim (Laranja com centro branco)
                                val finish = pixelPoints.last()
                                drawCircle(color = AisleActionOrange, radius = 9f, center = Offset(finish.first, finish.second))
                                drawCircle(color = Color.White, radius = 4f, center = Offset(finish.first, finish.second))
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(AisleSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOff,
                            contentDescription = null,
                            tint = AisleTextSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.md))
                        Text(
                            text = "Corrida registrada sem dados de rota GPS. Tempo e passos foram gravados normalmente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AisleTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 7. LINHA DE MÉTRICAS: TEMPO, RITMO MÉDIO E ESFORÇO PERCEBIDO
            AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_summary_metrics")) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(AisleSpacing.md),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBlock(
                        label = "Tempo",
                        value = timeFormatted,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBlock(
                        label = "Ritmo médio",
                        value = "$paceFormatted /km",
                        valueColor = AisleTurquoise,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBlock(
                        label = "Recorde Dist.",
                        value = maxDistanceFormatted,
                        valueColor = AisleTextAccent2,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            // Pós-treino: esforço em 5 níveis, dor e prévia do próximo passo
            if (!isDetailOnly) {
                PostWorkoutFeedback(
                    esforcoGravado = rpeValue,
                    onEsforcoChange = { rpeValue = it },
                    dor = dorValue,
                    onDorChange = {
                        dorValue = it
                        onPainChange(dorValue, regiaoDorValue)
                    },
                    regiaoDor = regiaoDorValue,
                    onRegiaoChange = {
                        regiaoDorValue = it
                        onPainChange(dorValue, regiaoDorValue)
                    },
                    ajustesAutomaticos = ajustesAutomaticos
                )
            }

            // 8. GRÁFICO "RITMO AO LONGO DO TREINO" DESENHADO EM CANVAS A PARTIR DE ROUTE_POINT
            AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_pace_chart")) {
                Column(modifier = Modifier.padding(AisleSpacing.md)) {
                    Text(
                        text = "Ritmo ao longo do treino",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AisleTextPrimary
                    )
                    Text(
                        text = "Janela móvel de 30s filtrada",
                        style = MaterialTheme.typography.labelSmall,
                        color = AisleTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (paceSeries.size >= 2) {
                        PaceCanvasChart(
                            paceSeries = paceSeries,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sem dados de GPS suficientes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AisleTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            KmSplitsCard(routePoints = routePoints)

            // 9. CARTÃO "SEUS RECORDES": MAIOR DISTÂNCIA E MAIOR DURAÇÃO ENTRE AS CORRIDAS CONCLUÍDAS
            AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_personal_records")) {
                Column(modifier = Modifier.padding(AisleSpacing.md)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = AisleActionOrange,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Seus recordes",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AisleTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MetricBlock(
                            label = "Maior distância",
                            value = maxDistanceFormatted,
                            valueColor = AisleTurquoise
                        )
                        MetricBlock(
                            label = "Maior duração",
                            value = maxDurationFormatted,
                            valueColor = AisleTextAccent2
                        )
                    }
                }
            }

            // 11. ANOTAÇÕES PARTICULARES
            AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_anotacoes_particulares")) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                ) {
                    Text(
                        text = "Anotações particulares",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AisleTextPrimary
                    )
                    Text(
                        text = "Sensações, clima ou observações do seu treino (salvo no celular).",
                        style = MaterialTheme.typography.bodySmall,
                        color = AisleTextSecondary
                    )

                    OutlinedTextField(
                        value = anotacoesInput,
                        onValueChange = { anotacoesInput = it },
                        placeholder = { Text("Ex: Bom ritmo no trecho intermediário...", color = AisleTextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AisleTurquoise,
                            unfocusedBorderColor = com.example.ui.theme.AisleFieldBorder,
                            focusedTextColor = AisleTextPrimary,
                            unfocusedTextColor = AisleTextPrimary
                        ),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_anotacoes_particulares")
                    )
                }
            }

            if (diagnostics != null && diagnostics.received > 0) {
                TechnicalDetailsCard(diagnostics)
            }

            Spacer(modifier = Modifier.height(AisleSpacing.lg))
        }

        // RODAPÉ FIXO: botão fora da rolagem, acima dos botões do sistema
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AisleBackground)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AisleBorder)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.systemBars.union(WindowInsets.tappableElement)
                            .only(WindowInsetsSides.Bottom)
                    )
                    .padding(top = 12.dp, bottom = 40.dp)
            ) {
                if (!isDetailOnly) {
                    ActionButton(
                        text = "Salvar e concluir treino",
                        onClick = { onSaveLog(rpeValue, anotacoesInput.trim().ifBlank { null }) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_save_workout_summary")
                    )
                } else {
                    BrandGradientButton(
                        text = "Salvar alterações",
                        onClick = {
                            onSaveLog(rpeValue, anotacoesInput.trim().ifBlank { null })
                            onClose()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_save_workout_changes")
                    )
                }
            }
        }
      }
    }
}

@Composable
private fun KmSplitsCard(
    routePoints: List<RoutePointEntity>,
    modifier: Modifier = Modifier
) {
    val splits = remember(routePoints) {
        KmSplitsCalculator.calculateSplits(routePoints)
    }

    if (splits.isNotEmpty()) {
        AisleCard(modifier = modifier.testTag("card_km_splits")) {
            Column(modifier = Modifier.padding(AisleSpacing.lg)) {
                Text(
                    text = "Parciais por Quilômetro (Splits)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AisleTextPrimary
                )
                Spacer(modifier = Modifier.height(AisleSpacing.md))

                val maxPace = splits.maxOfOrNull { it.paceSeconds } ?: 1.0

                splits.forEach { split ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AisleSpacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (split.isPartial) "Km ${split.kmIndex} (parcial)" else "Km ${split.kmIndex}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AisleTextPrimary,
                            modifier = Modifier.width(110.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AisleBorder)
                        ) {
                            val progress = (split.paceSeconds / maxPace).toFloat().coerceIn(0.1f, 1.0f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AisleTurquoise)
                            )
                        }
                        Spacer(modifier = Modifier.width(AisleSpacing.md))
                        Text(
                            text = "${split.paceFormatted} /km",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AisleTurquoise
                        )
                    }
                }
            }
        }
    }
}

/**
 * Gráfico de ritmo em Canvas conectando os pontos de PaceDataPoint.
 */
@Composable
private fun PaceCanvasChart(
    paceSeries: List<PaceDataPoint>,
    modifier: Modifier = Modifier
) {
    // Eixo vertical limitado ao intervalo entre os percentis 5 e 95
    val (minPace, maxPace) = PaceSeriesCalculator.axisRange(paceSeries)
    val segments = PaceSeriesCalculator.splitIntoSegments(paceSeries)
    val minTime = paceSeries.first().timeSeconds
    val maxTime = paceSeries.last().timeSeconds

    val minPaceFormatted = PaceCalculator.formatPace(minPace)
    val maxPaceFormatted = PaceCalculator.formatPace(maxPace)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mais rápido: $minPaceFormatted /km",
                style = MaterialTheme.typography.labelSmall,
                color = AisleTurquoise
            )
            Text(
                text = "Mais lento: $maxPaceFormatted /km",
                style = MaterialTheme.typography.labelSmall,
                color = AisleTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(AisleSpacing.xs))

        Canvas(modifier = Modifier.fillMaxWidth().weight(1f)) {
            val w = size.width
            val h = size.height
            val padX = 16f
            val padY = 16f

            val timeDelta = max(1L, maxTime - minTime).toDouble()
            val paceDelta = max(1.0, maxPace - minPace)

            // Cada trecho contínuo é uma linha; intervalos longos não são ligados
            segments.forEach { segment ->
                val xyPoints = segment.map { pt ->
                    val normX = ((pt.timeSeconds - minTime) / timeDelta).toFloat().coerceIn(0f, 1f)
                    val normY = ((pt.paceSecondsPerKm - minPace) / paceDelta).toFloat().coerceIn(0f, 1f)
                    Offset(padX + normX * (w - 2 * padX), padY + normY * (h - 2 * padY))
                }
                if (xyPoints.size == 1) {
                    drawCircle(brush = BrandGradient, radius = 4f, center = xyPoints[0])
                } else {
                    val path = Path()
                    xyPoints.forEachIndexed { index, o ->
                        if (index == 0) path.moveTo(o.x, o.y) else path.lineTo(o.x, o.y)
                    }
                    drawPath(
                        path = path,
                        brush = BrandGradient,
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }
    }
}


/** Seção recolhida com os contadores do filtro de GPS (diagnóstico). */
@Composable
private fun TechnicalDetailsCard(d: com.example.tracker.GpsDiagnostics) {
    var expanded by remember { mutableStateOf(false) }
    AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_detalhes_tecnicos")) {
        Column(modifier = Modifier.padding(AisleSpacing.lg), verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Detalhes técnicos", fontWeight = FontWeight.Bold, color = AisleTextPrimary)
                Text(if (expanded) "Ocultar" else "Mostrar", color = AisleTextSecondary)
            }
            if (expanded) {
                val lines = listOf(
                    "Localizações recebidas" to d.received.toString(),
                    "Aceitas" to d.accepted.toString(),
                    "Rejeitadas por precisão" to d.rejectedAccuracy.toString(),
                    "Rejeitadas por velocidade" to d.rejectedSpeed.toString(),
                    "Rejeitadas por deslocamento" to d.rejectedDisplacement.toString(),
                    "Rejeitadas: anteriores ao início" to d.rejectedBeforeStart.toString(),
                    "Rejeitadas: início (estabilização)" to d.rejectedWarmup.toString(),
                    "Maior intervalo sem ponto" to "%.1f s".format(d.maxGapMillis / 1000.0)
                )
                lines.forEach { (k, v) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(k, color = AisleTextSecondary, style = MaterialTheme.typography.bodySmall)
                        Text(v, color = AisleTextPrimary, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
