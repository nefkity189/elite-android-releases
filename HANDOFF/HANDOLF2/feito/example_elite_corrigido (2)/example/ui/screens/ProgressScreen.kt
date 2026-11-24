package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity
import com.example.tracker.PaceCalculator
import com.example.tracker.ProgressStatsCalculator
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleTopBar
import com.example.ui.components.ProgressRing
import com.example.ui.theme.AisleChartColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.ui.theme.Background
import com.example.ui.theme.Card as CardColor
import com.example.ui.theme.Text
import com.example.ui.theme.TextMuted
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.Olive
import com.example.ui.theme.Red
import com.example.ui.theme.RedSoft
import com.example.ui.theme.Brass
import com.example.ui.theme.LineStrong
import com.example.ui.theme.CardElevated
import com.example.ui.theme.Text as TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    workouts: List<WorkoutEntity> = emptyList(),
    logs: List<WorkoutLogEntity> = emptyList(),
    currentDate: LocalDate = LocalDate.now(),
    onSaveAnotacoes: (logId: Long, anotacoes: String) -> Unit = { _, _ -> },
    onRequestRoutePoints: suspend (workoutId: Long) -> List<RoutePointEntity> = { emptyList() },
    onViewTodayClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedLogForDetail by remember { mutableStateOf<Pair<WorkoutLogEntity, WorkoutEntity?>?>(null) }

    // Diálogo detalhe da corrida (altura total)
    selectedLogForDetail?.let { (log, workout) ->
        RunDetailDialog(
            log = log,
            workout = workout,
            onDismiss = { selectedLogForDetail = null },
            onSaveAnotacoes = { text ->
                onSaveAnotacoes(log.id, text)
                selectedLogForDetail = log.copy(anotacoes = text) to workout
            },
            onRequestRoutePoints = onRequestRoutePoints
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = AisleSpacing.xl)
            .testTag("screen_progress"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar (aba principal: sem seta de voltar)
        AisleTopBar(
            title = "Progresso",
            titleStyle = MaterialTheme.typography.headlineMedium,
            onBack = null,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        val weekStats = remember(workouts, logs, currentDate) {
            ProgressStatsCalculator.calculateCurrentWeekStats(workouts, logs, currentDate)
        }
        val streak = remember(workouts, currentDate) {
            ProgressStatsCalculator.calculateWeeklyStreak(workouts, currentDate)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
        ) {
            // 1. Cartão Hero da semana com ProgressRing de 140 dp
            item {
                CurrentWeekSummaryCard(
                    stats = weekStats,
                    streakWeeks = streak
                )
            }

            // 2. Gráfico de Evolução (Distância & Ritmo por corrida)
            if (logs.isNotEmpty()) {
                item {
                    EvolutionChartCard(logs = logs)
                }
            }

            // 3. Marcos Opcionais Calculados da UI
            item {
                MilestonesCard(logs = logs)
            }

            // 4. Cabeçalho da Lista de Corridas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AisleSpacing.xs),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Histórico de Corridas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Background
                    )
                    if (logs.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = CardElevated
                        ) {
                            Text(
                                text = "${logs.size} ${if (logs.size == 1) "corrida" else "corridas"}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.xs)
                            )
                        }
                    }
                }
            }

            // 5. ESTADO VAZIO ou LISTA DE CORRIDAS
            if (logs.isEmpty()) {
                item {
                    EmptyRunsCard(onViewTodayClick = onViewTodayClick)
                }
            } else {
                items(logs, key = { it.id }) { log ->
                    val workout = remember(log.workoutId, workouts) {
                        workouts.find { it.id == log.workoutId }
                    }
                    CompletedRunCard(
                        log = log,
                        workout = workout,
                        onRequestRoutePoints = onRequestRoutePoints,
                        onClick = {
                            selectedLogForDetail = log to workout
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(AisleSpacing.xxl))
            }
        }
    }
}

/**
 * 1. Cartão Hero da semana: ProgressRing de 140 dp + 3 métricas
 */
@Composable
private fun CurrentWeekSummaryCard(
    stats: com.example.tracker.CurrentWeekStats,
    streakWeeks: Int
) {
    AisleCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_current_week_summary")
    ) {
        Column(
            modifier = Modifier.padding(AisleSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resumo da Semana",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Sequência de semanas (streak)
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (streakWeeks > 0) AisleChartColors.StreakContainer else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.xs)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = if (streakWeeks > 0) AisleChartColors.StreakContent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.xs))
                        Text(
                            text = if (streakWeeks == 1) "1 semana" else "$streakWeeks semanas",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (streakWeeks > 0) AisleChartColors.StreakContent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.xl))

            // ProgressRing de 140 dp
            val progress = if (stats.totalWorkoutsPlanned > 0) {
                stats.completedWorkoutsCount.toFloat() / stats.totalWorkoutsPlanned.toFloat()
            } else {
                0f
            }

            ProgressRing(
                progress = progress,
                gradient = Brush.linearGradient(listOf(Olive, AisleChartColors.RingTurquoiseEnd)),
                valueText = "${stats.completedWorkoutsCount}/${stats.totalWorkoutsPlanned}",
                labelText = "treinos",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(AisleSpacing.xxl))

            // 3 colunas de métricas: Treinos, Tempo total, Distância total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryMetricItem(
                    label = "Treinos",
                    value = stats.completedWorkoutsCount.toString(),
                    unit = if (stats.completedWorkoutsCount == 1) "concluído" else "concluídos"
                )

                SummaryMetricItem(
                    label = "Tempo total",
                    value = formatDuration(stats.totalDurationSeconds),
                    unit = if (stats.totalDurationSeconds < 3600) "min:seg" else "horas"
                )

                SummaryMetricItem(
                    label = "Distância",
                    value = formatDistance(stats.totalDistanceMeters),
                    unit = "km"
                )
            }
        }
    }
}

@Composable
private fun SummaryMetricItem(
    label: String,
    value: String,
    unit: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = TextColor
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 2. Gráfico de evolução em Canvas com eixos, rótulos, grade e marcadores de forma distinta
 */
@Composable
private fun EvolutionChartCard(
    logs: List<WorkoutLogEntity>
) {
    val recentLogs = remember(logs) {
        logs.take(10).reversed() // As 10 corridas mais recentes, em ordem cronológica
    }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = AisleChartColors.StreakContent
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)

    val summaryText = remember(recentLogs) {
        if (recentLogs.size < 2) "Evolução do seu ritmo e distância por corrida."
        else "Evolução das últimas ${recentLogs.size} corridas registadas."
    }

    AisleCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = summaryText }
            .testTag("card_evolution_chart")
    ) {
        Column(modifier = Modifier.padding(AisleSpacing.lg)) {
            Text(
                text = "Evolução do Desempenho",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(AisleSpacing.md))

            // Legenda de formas e cores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AisleSpacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(primaryColor)
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.xs))
                    Text(
                        text = "Km (Círculo)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(secondaryColor)
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.xs))
                    Text(
                        text = "Ritmo (Quadrado)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.lg))

            // Exibição do ponto selecionado
            selectedIndex?.let { idx ->
                val log = recentLogs.getOrNull(idx)
                if (log != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CardElevated.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = AisleSpacing.md)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.md),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Corrida #${idx + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${formatDistance(log.distanciaMetros)} km • ${PaceCalculator.formatPace(log.ritmoMedioSegundos)} min/km",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Canvas do gráfico
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(recentLogs) {
                            detectTapGestures { tapOffset ->
                                if (recentLogs.isEmpty()) return@detectTapGestures
                                val paddingLeft = 40.dp.toPx()
                                val paddingRight = 20.dp.toPx()
                                val availableWidth = size.width - paddingLeft - paddingRight
                                val stepX = if (recentLogs.size > 1) availableWidth / (recentLogs.size - 1) else availableWidth

                                var closestIdx = 0
                                var minDiff = Float.MAX_VALUE
                                recentLogs.forEachIndexed { i, _ ->
                                    val x = paddingLeft + i * stepX
                                    val diff = kotlin.math.abs(tapOffset.x - x)
                                    if (diff < minDiff) {
                                        minDiff = diff
                                        closestIdx = i
                                    }
                                }
                                selectedIndex = if (selectedIndex == closestIdx) null else closestIdx
                            }
                        }
                        .testTag("canvas_evolution_chart")
                ) {
                    if (recentLogs.isEmpty()) return@Canvas

                    val paddingLeft = 40.dp.toPx()
                    val paddingRight = 20.dp.toPx()
                    val paddingTop = 24.dp.toPx()
                    val paddingBottom = 30.dp.toPx()

                    val chartWidth = size.width - paddingLeft - paddingRight
                    val chartHeight = size.height - paddingTop - paddingBottom

                    val maxKm = (recentLogs.maxOfOrNull { it.distanciaMetros / 1000.0 } ?: 5.0).coerceAtLeast(3.0)
                    val stepX = if (recentLogs.size > 1) chartWidth / (recentLogs.size - 1) else chartWidth / 2f

                    // Grade horizontal de fundo (3 linhas)
                    for (i in 0..2) {
                        val y = paddingTop + chartHeight * (i / 2f)
                        drawLine(
                            color = gridColor,
                            start = Offset(paddingLeft, y),
                            end = Offset(size.width - paddingRight, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                        )
                    }

                    // Linha de distância
                    val distancePath = Path()
                    recentLogs.forEachIndexed { i, log ->
                        val km = log.distanciaMetros / 1000.0
                        val x = paddingLeft + (if (recentLogs.size > 1) i * stepX else chartWidth / 2f)
                        val y = paddingTop + chartHeight * (1f - (km / maxKm).toFloat())

                        if (i == 0) distancePath.moveTo(x, y)
                        else distancePath.lineTo(x, y)
                    }

                    drawPath(
                        path = distancePath,
                        color = primaryColor,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // Marcadores de distância: CÍRCULOS
                    recentLogs.forEachIndexed { i, log ->
                        val km = log.distanciaMetros / 1000.0
                        val x = paddingLeft + (if (recentLogs.size > 1) i * stepX else chartWidth / 2f)
                        val y = paddingTop + chartHeight * (1f - (km / maxKm).toFloat())
                        val center = Offset(x, y)

                        val isSelected = selectedIndex == i
                        drawCircle(color = Color.White, radius = if (isSelected) 8.dp.toPx() else 6.dp.toPx(), center = center)
                        drawCircle(color = primaryColor, radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(), center = center)
                    }
                }
            }
        }
    }
}

/**
 * 3. Marcos / Conquistas calculadas dinamicamente na UI a partir dos logs
 */
@Composable
private fun MilestonesCard(
    logs: List<WorkoutLogEntity>
) {
    val totalKm = logs.sumOf { it.distanciaMetros } / 1000.0
    val maxSingleRunKm = (logs.maxOfOrNull { it.distanciaMetros } ?: 0.0) / 1000.0

    val milestones = listOf(
        MilestoneItem("Primeira corrida", "Início da jornada", logs.isNotEmpty()),
        MilestoneItem("Marca de 1 km", "Primeiro km percorrido", maxSingleRunKm >= 1.0 || totalKm >= 1.0),
        MilestoneItem("Marca de 3 km", "Superou 3 km numa corrida", maxSingleRunKm >= 3.0),
        MilestoneItem("Marca de 5 km", "Meta dos 5K atingida!", maxSingleRunKm >= 5.0)
    )

    AisleCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_milestones")
    ) {
        Column(modifier = Modifier.padding(AisleSpacing.lg)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Conquistas do Plano",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(milestones) { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (item.achieved) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (item.achieved) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (item.achieved) Icons.Default.Check else Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = if (item.achieved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.achieved) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class MilestoneItem(
    val title: String,
    val subtitle: String,
    val achieved: Boolean
)

/**
 * 4. Estado Vazio motivador com CTA para a aba Hoje
 */
@Composable
private fun EmptyRunsCard(
    onViewTodayClick: () -> Unit = {}
) {
    AisleCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("empty_runs_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AisleSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = CardElevated.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Suas corridas aparecem aqui depois do primeiro treino",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Complete seu primeiro treino para acompanhar distância, ritmo e evolução no gráfico.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            AisleButton(
                text = "Ir para o treino de hoje",
                onClick = onViewTodayClick,
                variant = AisleButtonVariant.Primary,
                modifier = Modifier.testTag("btn_ir_treino_hoje")
            )
        }
    }
}

/**
 * 5. Lista de corridas compacta
 */
@Composable
private fun CompletedRunCard(
    log: WorkoutLogEntity,
    workout: WorkoutEntity?,
    onRequestRoutePoints: suspend (workoutId: Long) -> List<RoutePointEntity>,
    onClick: () -> Unit
) {
    var routePoints by remember { mutableStateOf<List<RoutePointEntity>>(emptyList()) }

    LaunchedEffect(log.workoutId) {
        routePoints = onRequestRoutePoints(log.workoutId)
    }

    val dateText = remember(workout?.data, log.finalizadoEm) {
        if (!workout?.data.isNullOrBlank()) {
            try {
                val d = LocalDate.parse(workout?.data)
                d.format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMM", Locale.forLanguageTag("pt-BR"))).replaceFirstChar { it.uppercase() }
            } catch (_: Exception) {
                workout?.data ?: ""
            }
        } else {
            "Treino Concluído"
        }
    }

    val workoutTitle = workout?.titulo ?: "Treino de Corrida"

    AisleCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("run_card_${log.id}")
    ) {
        Column(modifier = Modifier.padding(AisleSpacing.lg)) {
            // Cabeçalho: data, título e mini-rota
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = workoutTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Mini traçado da rota em Canvas se houver pontos
                if (routePoints.size >= 2) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                            val coords = routePoints.map { RoutePointCoord(it.latitude, it.longitude) }
                            val normalized = RouteNormalizer.normalize(coords)
                            val canvasOffsets = RouteNormalizer.toCanvasOffsets(
                                normalizedPoints = normalized,
                                widthPx = size.width,
                                heightPx = size.height,
                                paddingPx = 4f
                            )
                            if (canvasOffsets.size >= 2) {
                                val path = Path()
                                path.moveTo(canvasOffsets.first().first, canvasOffsets.first().second)
                                for (i in 1 until canvasOffsets.size) {
                                    path.lineTo(canvasOffsets[i].first, canvasOffsets[i].second)
                                }
                                drawPath(
                                    path = path,
                                    color = primaryColor,
                                    style = Stroke(
                                        width = 2.dp.toPx(),
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ver detalhes da corrida",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Linha com 4 métricas compactas: tempo, distância, ritmo médio, esforço
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn(
                    label = "Tempo",
                    value = formatDuration(log.duracaoSegundos)
                )

                MetricColumn(
                    label = "Distância",
                    value = "${formatDistance(log.distanciaMetros)} km"
                )

                MetricColumn(
                    label = "Ritmo",
                    value = PaceCalculator.formatPace(log.ritmoMedioSegundos) + if (log.ritmoMedioSegundos != null) " /km" else ""
                )

                MetricColumn(
                    label = "Esforço",
                    value = "${log.esforcoPercebido}/10"
                )
            }

            // Anotações existentes
            if (!log.anotacoes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = log.anotacoes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricColumn(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 6. Diálogo modal em Altura Total para Detalhes da Corrida
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RunDetailDialog(
    log: WorkoutLogEntity,
    workout: WorkoutEntity?,
    onDismiss: () -> Unit,
    onSaveAnotacoes: (String) -> Unit,
    onRequestRoutePoints: suspend (workoutId: Long) -> List<RoutePointEntity>
) {
    var routePoints by remember { mutableStateOf<List<RoutePointEntity>>(emptyList()) }
    var isLoadingRoute by remember { mutableStateOf(true) }
    var anotacoesInput by remember(log.anotacoes) { mutableStateOf(log.anotacoes ?: "") }
    var anotacoesSalvasFeedback by remember { mutableStateOf(false) }

    LaunchedEffect(log.workoutId) {
        isLoadingRoute = true
        routePoints = onRequestRoutePoints(log.workoutId)
        isLoadingRoute = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp)
                .testTag("dialog_run_detail"),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AisleTopBar(
                title = workout?.titulo ?: "Detalhes da Corrida",
                onBack = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Rota em Canvas ou aviso de sem GPS
                if (isLoadingRoute) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    }
                } else if (routePoints.size >= 2) {
                    RouteCanvasCard(routePoints = routePoints)
                } else {
                    AisleCard(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(AisleSpacing.lg),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Corrida registrada sem dados de rota GPS. As métricas de tempo e passos foram calculadas normalmente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Cartão de métricas da corrida
                AisleCard(
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(AisleSpacing.lg)) {
                        Text(
                            text = "Métricas da corrida",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailMetricBox(
                                icon = Icons.Default.Timer,
                                label = "Tempo total",
                                value = formatDuration(log.duracaoSegundos)
                            )
                            DetailMetricBox(
                                icon = Icons.Default.DirectionsRun,
                                label = "Distância",
                                value = "${formatDistance(log.distanciaMetros)} km"
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailMetricBox(
                                icon = Icons.Default.Speed,
                                label = "Ritmo médio",
                                value = PaceCalculator.formatPace(log.ritmoMedioSegundos) + if (log.ritmoMedioSegundos != null) " min/km" else ""
                            )
                            DetailMetricBox(
                                icon = Icons.Default.FitnessCenter,
                                label = "Esforço percebido",
                                value = "${log.esforcoPercebido}/10 (${describeExertion(log.esforcoPercebido)})"
                            )
                        }
                    }
                }

                // Anotações particulares
                AisleCard(
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(AisleSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Anotações particulares",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Grave suas percepções deste treino (ex: sensação nas pernas, clima, motivação). Salvo apenas no seu celular.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = anotacoesInput,
                            onValueChange = {
                                anotacoesInput = it
                                anotacoesSalvasFeedback = false
                            },
                            placeholder = { Text("Ex: Me senti leve na primeira corrida e consegui manter o ritmo.") },
                            minLines = 3,
                            maxLines = 6,
                            shape = RoundedCornerShape(12.dp),
                            colors = com.example.ui.components.aisleTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_anotacoes_particulares")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (anotacoesSalvasFeedback) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Anotações salvas!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            AisleButton(
                                text = "Salvar anotações",
                                onClick = {
                                    onSaveAnotacoes(anotacoesInput.trim())
                                    anotacoesSalvasFeedback = true
                                },
                                modifier = Modifier.testTag("btn_salvar_anotacoes")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Traçado da rota via Canvas
 */
@Composable
private fun RouteCanvasCard(
    routePoints: List<RoutePointEntity>
) {
    AisleCard(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AisleSpacing.lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Traçado da rota",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AisleChartColors.RouteStart)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Início", style = MaterialTheme.typography.labelSmall)

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AisleChartColors.RouteEnd)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fim", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(AisleSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Norte",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "N",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }

                val primaryColor = MaterialTheme.colorScheme.primary

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("route_canvas")
                ) {
                    val coords = routePoints.map { RoutePointCoord(it.latitude, it.longitude) }
                    val normalized = RouteNormalizer.normalize(coords)
                    val canvasOffsets = RouteNormalizer.toCanvasOffsets(
                        normalizedPoints = normalized,
                        widthPx = size.width,
                        heightPx = size.height,
                        paddingPx = 36f
                    )

                    if (canvasOffsets.size >= 2) {
                        val path = Path()
                        val first = canvasOffsets.first()
                        path.moveTo(first.first, first.second)

                        for (i in 1 until canvasOffsets.size) {
                            val pt = canvasOffsets[i]
                            path.lineTo(pt.first, pt.second)
                        }

                        drawPath(
                            path = path,
                            color = primaryColor.copy(alpha = 0.25f),
                            style = Stroke(
                                width = 8.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        drawPath(
                            path = path,
                            color = primaryColor,
                            style = Stroke(
                                width = 4.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        val startOffset = Offset(first.first, first.second)
                        drawCircle(color = Color.White, radius = 7.dp.toPx(), center = startOffset)
                        drawCircle(color = AisleChartColors.RouteStart, radius = 5.5.dp.toPx(), center = startOffset)

                        val last = canvasOffsets.last()
                        val endOffset = Offset(last.first, last.second)
                        drawCircle(color = Color.White, radius = 7.dp.toPx(), center = endOffset)
                        drawCircle(color = AisleChartColors.RouteEnd, radius = 5.5.dp.toPx(), center = endOffset)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailMetricBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(150.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = CardElevated,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
}

private fun formatDistance(meters: Double): String {
    val km = meters / 1000.0
    return String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.2f", km)
}

private fun describeExertion(rpe: Int): String {
    return when (rpe) {
        in 1..2 -> "Muito leve"
        in 3..4 -> "Leve"
        in 5..6 -> "Moderado"
        in 7..8 -> "Forte"
        in 9..10 -> "Máximo"
        else -> "Moderado"
    }
}
