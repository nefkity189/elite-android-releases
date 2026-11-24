package com.elite.sch.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.elite.sch.app.R
import com.elite.sch.app.ui.components.WorkoutBlocksBar
import com.elite.sch.app.ui.theme.EliteChartColors
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.rememberAnimatedVisibilitySpecs
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.ActiveRunEntity
import com.elite.sch.app.data.local.entities.PlanAdjustmentEntity
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.tracker.DayMarker
import com.elite.sch.app.tracker.DayMarkerType
import com.elite.sch.app.tracker.PaceCalculator
import com.elite.sch.app.tracker.RouteNormalizer
import com.elite.sch.app.tracker.RoutePointCoord
import com.elite.sch.app.tracker.WeekMarkerCalculator
import com.elite.sch.app.tracker.WeeklyGoalsCalculator
import com.elite.sch.app.ui.components.ActionButton
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteLogo
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
import com.elite.sch.app.ui.components.MetricBlock
import com.elite.sch.app.ui.components.ProgressRing
import com.elite.sch.app.ui.components.StepItemView
import com.elite.sch.app.ui.components.WaveArcs
import com.elite.sch.app.ui.components.WorkoutFormatters
import com.elite.sch.app.ui.screens.PreWorkoutCheckinBottomSheet
import com.elite.sch.app.ui.theme.rememberReducedMotion
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import com.elite.sch.app.ui.components.RunnaTheme
import com.elite.sch.app.ui.theme.RunnaNumberFont
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextStyle
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RunnaTextFont
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    todayWorkout: WorkoutEntity?,
    nextWorkout: WorkoutEntity?,
    allWorkouts: List<WorkoutEntity> = emptyList(),
    workoutLogs: List<WorkoutLogEntity> = emptyList(),
    userName: String = "",
    activeRunToRecover: ActiveRunEntity? = null,
    activePauseAdjustment: PlanAdjustmentEntity? = null,
    currentDate: LocalDate = LocalDate.now(),
    onStartRunClick: (workout: WorkoutEntity, useGps: Boolean) -> Unit,
    onResumeActiveRun: () -> Unit = {},
    onDiscardActiveRun: () -> Unit = {},
    onMarkCompleted: (Long) -> Unit,
    onMarkSkipped: (Long) -> Unit,
    onResetStatus: (Long) -> Unit,
    onViewFullPlan: () -> Unit,
    onResetEntirePlan: () -> Unit,
    onOpenProfile: () -> Unit = {},
    onOpenReminders: () -> Unit = {},
    onRequestRoutePoints: suspend (Long) -> List<RoutePointEntity> = { emptyList() },
    onViewWorkoutDetail: ((WorkoutLogEntity, WorkoutEntity?) -> Unit)? = null,
    onRefreshToday: () -> Unit = {},
    autoAdjustments: List<com.elite.sch.app.data.local.entities.AutoAdjustmentEntity> = emptyList(),
    adaptiveSuggestion: com.elite.sch.app.generator.AdaptiveResult? = null,
    ajustesAutomaticos: Boolean = true,
    onUndoAdjustment: (Long) -> Unit = {},
    onOpenAdjustments: () -> Unit = {},
    onAcceptSuggestion: () -> Unit = {},
    onDismissSuggestion: () -> Unit = {},
    onStartFreeRun: (() -> Unit)? = null,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    // P12: Check-in pré-treino
    showPreWorkoutCheckin: Boolean = false,
    preWorkoutCheckinWorkout: WorkoutEntity? = null,
    onConfirmPreWorkoutCheckin: (energia: Int, sono: Int) -> Unit = { _, _ -> },
    onClosePreWorkoutCheckin: () -> Unit = {},
    onOpenPreWorkoutCheckin: (WorkoutEntity) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // 1. Recalcula quando o app volta ao primeiro plano (ON_RESUME)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onRefreshToday()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 2. Recalcula quando passar da meia-noite enquanto o app está aberto
    LaunchedEffect(currentDate) {
        while (true) {
            delay(15_000L)
            if (LocalDate.now() != currentDate) {
                onRefreshToday()
            }
        }
    }

    var showResetDialog by remember { mutableStateOf(false) }

    // Diálogos de permissão GPS
    var showGpsExplanationDialog by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }
    var showGpsDisabledWarning by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val isLocationGranted = fineGranted || coarseGranted

        if (isLocationGranted) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: true
            if (!isGpsEnabled) {
                showGpsDisabledWarning = true
            } else {
                todayWorkout?.let { onStartRunClick(it, true) }
            }
        } else {
            showPermissionDeniedDialog = true
        }
    }

    if (isLoading) {
        LoadingState(message = "Carregando o treino de hoje...", modifier = modifier)
        return
    }

    if (isEmpty) {
        EmptyState(
            icon = Icons.Default.DirectionsRun,
            title = "Nenhum plano ativo",
            description = "Monte agora seu plano de caminhada e corrida para conquistar seus primeiros 5K.",
            actionLabel = "Criar meu plano",
            onActionClick = onResetEntirePlan,
            modifier = modifier
        )
        return
    }

    // Diálogos de permissão GPS mantidos
    if (showGpsExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showGpsExplanationDialog = false },
            title = { Text("Permissão de Localização", color = TextColor) },
            text = {
                Text(
                    "Usamos sua localização só para medir distância e ritmo. Os dados ficam no seu celular.",
                    color = TextMuted
                )
            },
            containerColor = CardColor,
            confirmButton = {
                Button(
                    onClick = {
                        showGpsExplanationDialog = false
                        val permissions = mutableListOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        permissionLauncher.launch(permissions.toTypedArray())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Olive),
                    modifier = Modifier.testTag("btn_conceder_permissao_gps")
                ) {
                    Text("Continuar", color = OnOlive, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGpsExplanationDialog = false }) {
                    Text("Cancelar", color = TextMuted)
                }
            }
        )
    }

    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text("Localização não concedida", color = TextColor) },
            text = {
                Text(
                    "Sem GPS, não registraremos distância nem ritmo em tempo real. Você pode seguir usando apenas o cronômetro e a voz, ou autorizar nas configurações.",
                    color = TextMuted
                )
            },
            containerColor = CardColor,
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDeniedDialog = false
                        todayWorkout?.let { onStartRunClick(it, false) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Olive),
                    modifier = Modifier.testTag("btn_seguir_sem_gps")
                ) {
                    Text("Seguir sem GPS", color = OnOlive, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionDeniedDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.testTag("btn_abrir_configuracoes_permissao")
                ) {
                    Text("Abrir Configurações", color = Olive)
                }
            }
        )
    }

    if (showGpsDisabledWarning) {
        AlertDialog(
            onDismissRequest = { showGpsDisabledWarning = false },
            title = { Text("GPS do aparelho desligado", color = TextColor) },
            text = {
                Text(
                    "O GPS do seu smartphone está desativado. Ative a localização para que o app possa medir sua distância.",
                    color = TextMuted
                )
            },
            containerColor = CardColor,
            confirmButton = {
                Button(
                    onClick = {
                        showGpsDisabledWarning = false
                        todayWorkout?.let { onStartRunClick(it, true) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Olive)
                ) {
                    Text("Continuar mesmo assim", color = OnOlive, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showGpsDisabledWarning = false
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                ) {
                    Text("Ativar GPS", color = Olive)
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reiniciar plano?", color = TextColor) },
            text = { Text("Isso apagará o progresso atual e permitirá configurar um novo plano do início.", color = TextMuted) },
            containerColor = CardColor,
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetEntirePlan()
                    },
                    modifier = Modifier.testTag("confirm_reset_plan_button")
                ) {
                    Text("Reiniciar", color = Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar", color = TextMuted)
                }
            }
        )
    }

    val currentHour = remember { LocalTime.now().hour }
    val greetingPrefix = when (currentHour) {
        in 5..11 -> "Bom dia"
        in 12..17 -> "Boa tarde"
        else -> "Boa noite"
    }
    val greetingText = if (userName.isNotBlank()) "$greetingPrefix, $userName!" else "$greetingPrefix!"

    val monday = remember(currentDate) {
        currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }
    val weekDates = remember(monday) {
        (0L..6L).map { monday.plusDays(it) }
    }
    val weekMarkers = remember(weekDates, allWorkouts, currentDate) {
        WeekMarkerCalculator.calculateWeekMarkers(weekDates, allWorkouts, currentDate)
    }

    var selectedDate by remember { mutableStateOf(currentDate) }
    val displayedWorkout = remember(selectedDate, allWorkouts, todayWorkout) {
        allWorkouts.find { it.data == selectedDate.toString() } ?: if (selectedDate == currentDate) todayWorkout else null
    }

    // Treinos da semana atual para Metas
    val currentWeekWorkouts = remember(allWorkouts, weekDates) {
        val weekDateStrings = weekDates.map { it.toString() }.toSet()
        allWorkouts.filter { it.data in weekDateStrings }
    }
    val weeklyGoals = remember(currentWeekWorkouts) {
        WeeklyGoalsCalculator.calculate(currentWeekWorkouts)
    }

    // Última corrida concluída para o cartão "Corridas recentes"
    val lastCompletedLog = remember(workoutLogs) {
        workoutLogs.maxByOrNull { it.id }
    }
    val lastCompletedWorkout = remember(lastCompletedLog, allWorkouts) {
        if (lastCompletedLog != null) allWorkouts.find { it.id == lastCompletedLog.workoutId } else null
    }

    val headerDateText = remember(currentDate) {
        currentDate.format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", java.util.Locale.forLanguageTag("pt-BR")))
            .replaceFirstChar { it.uppercase() }
    }
    val recentAdjView = remember(autoAdjustments) {
        com.elite.sch.app.ui.AdjustmentPresentation.recenteAtivo(autoAdjustments)?.let {
            com.elite.sch.app.ui.AdjustmentPresentation.toView(it, LocalDate.now())
        }
    }
    val headerGreeting = if (userName.isNotBlank()) "Olá, ${userName.trim()}" else "Olá!"
    val weekChipText = remember(allWorkouts, currentDate) {
        val todayIso = currentDate.toString()
        val ref = allWorkouts.firstOrNull { it.data == todayIso }
            ?: allWorkouts.filter { it.data >= todayIso }.minByOrNull { it.data }
            ?: allWorkouts.maxByOrNull { it.data }
        val total = allWorkouts.maxOfOrNull { it.semanaIndex }
        if (ref != null && total != null) "Semana ${ref.semanaIndex} de $total" else null
    }
    val weekLogsKm = remember(workoutLogs, currentWeekWorkouts) {
        val ids = currentWeekWorkouts.map { it.id }.toSet()
        workoutLogs.filter { it.workoutId in ids }.sumOf { it.distanciaMetros } / 1000.0
    }
    val weekLogsMinutes = remember(workoutLogs, currentWeekWorkouts) {
        val ids = currentWeekWorkouts.map { it.id }.toSet()
        workoutLogs.filter { it.workoutId in ids }.sumOf { it.duracaoSegundos } / 60
    }

    val userInitial = remember(userName) {
        if (userName.isNotBlank()) userName.trim().first().uppercase() else "A"
    }

    val hasNotices = activePauseAdjustment != null || activeRunToRecover != null ||
        adaptiveSuggestion != null || recentAdjView != null
    val totalWeeks = remember(allWorkouts) { allWorkouts.maxOfOrNull { it.semanaIndex } ?: 0 }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(RunnaTheme.Background)
    ) {
        val shortScreen = maxHeight < 760.dp
        val scrollContent = hasNotices || shortScreen

        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("screen_today")
                .statusBarsPadding()
                .then(if (scrollContent) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
        // --- Cabeçalho ---
        Column {
            Text(text = headerDateText, style = runnaUi(13f), color = RunnaTheme.Muted)
            Text(
                text = headerGreeting,
                style = runnaNum(if (shortScreen) 28 else 32, FontWeight.Bold),
                color = RunnaTheme.Text,
                modifier = Modifier.testTag("txt_saudacao")
            )
        }

        // --- Faixa da semana ---
        Box(modifier = Modifier.fillMaxWidth().testTag("week_strip_header")) {
            RunnaWeekStrip(
                weekDates = weekDates,
                markers = weekMarkers,
                currentDate = currentDate,
                selectedDate = selectedDate,
                onSelectDate = { selectedDate = it }
            )
        }

        // --- Avisos (aparecem só quando existem) ---
        if (activePauseAdjustment != null) {
            val fimFormatted = try {
                LocalDate.parse(activePauseAdjustment.fim).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            } catch (_: Exception) { activePauseAdjustment.fim }
            RunnaNotice(title = "Pausa até $fimFormatted", body = null, accent = RunnaTheme.Teal,
                actions = emptyList(), tag = "banner_pausa_ativa")
        }
        if (activeRunToRecover != null) {
            RunnaNotice(
                title = stringResource(R.string.recover_run_title),
                body = stringResource(R.string.recover_run_message),
                accent = RunnaTheme.Orange,
                actions = listOf(
                    NoticeAction("Retomar corrida", "btn_retomar_corrida", true, onResumeActiveRun),
                    NoticeAction("Descartar", "btn_descartar_corrida", false, onDiscardActiveRun)
                ),
                tag = "card_recuperar_corrida"
            )
        }
        if (adaptiveSuggestion != null) {
            RunnaNotice(
                title = "Sugestão do Elite",
                body = adaptiveSuggestion.mensagem
                    ?: adaptiveSuggestion.regra?.let { com.elite.sch.app.generator.AdaptiveEngine.mensagemPadrao(it) }
                    ?: "Dá para ajustar o seu plano.",
                accent = RunnaTheme.Teal,
                actions = listOf(
                    NoticeAction("Aplicar", "btn_aplicar_sugestao", true, onAcceptSuggestion),
                    NoticeAction("Agora não", "btn_dispensar_sugestao", false, onDismissSuggestion)
                ),
                tag = "card_sugestao_ajuste"
            )
        }
        if (recentAdjView != null) {
            var showWhy by remember(recentAdjView.id) { mutableStateOf(false) }
            val detail = buildString {
                append(recentAdjView.titulo)
                if (recentAdjView.antes != null && recentAdjView.depois != null && recentAdjView.antes != recentAdjView.depois) {
                    append("\n${recentAdjView.antes}  →  ${recentAdjView.depois}")
                }
            }
            RunnaNotice(
                title = "Seu plano se ajustou",
                body = if (showWhy) recentAdjView.motivo + "\n" + detail else recentAdjView.motivo,
                accent = RunnaTheme.Warm,
                actions = listOf(
                    NoticeAction(if (showWhy) "Ocultar" else "Por quê?", "btn_por_que_ajuste", true) { showWhy = !showWhy },
                    NoticeAction("Desfazer", "btn_desfazer_ajuste", false) { onUndoAdjustment(recentAdjView.id) }
                ),
                tag = "card_ajuste_plano"
            )
        }

        // --- Card do treino (flexível) ---
        val heroModifier = if (scrollContent) {
            Modifier.fillMaxWidth().heightIn(min = if (hasNotices) 380.dp else 260.dp)
        }
        else Modifier.fillMaxWidth().weight(1f)

        if (displayedWorkout != null) {
            RunnaWorkoutHero(
                workout = displayedWorkout,
                isToday = (selectedDate == currentDate),
                totalWeeks = totalWeeks,
                showAxis = !shortScreen,
                onStart = { onOpenPreWorkoutCheckin(displayedWorkout) },
                modifier = heroModifier
            )
        } else {
            RunnaRestCard(
                nextWorkout = nextWorkout,
                onViewFullPlan = onViewFullPlan,
                onStartFreeRun = { onStartFreeRun?.invoke() },
                modifier = heroModifier
            )
        }

        // --- Meta da semana ---
        Row(
            modifier = Modifier.fillMaxWidth().testTag("card_weekly_goals_minimal"),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RunnaTile(modifier = Modifier.weight(1f)) {
                RunnaProgressRing(
                    progress = weeklyGoals.workoutsProgress,
                    text = "${weeklyGoals.completedWorkouts}/${weeklyGoals.plannedWorkouts}"
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Treinos", style = runnaNum(22, FontWeight.Bold), color = RunnaTheme.Text)
                    Text("esta semana", style = runnaUi(12f), color = RunnaTheme.Muted)
                }
            }
            RunnaTile(modifier = Modifier.weight(1f)) {
                Column {
                    Text(
                        String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.1f km", weekLogsKm),
                        style = runnaNum(22, FontWeight.Bold), color = RunnaTheme.Text
                    )
                    Text("$weekLogsMinutes min em movimento", style = runnaUi(12f), color = RunnaTheme.Muted)
                }
            }
        }

        // --- Corridas recentes ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(RunnaTheme.Card)
                .border(BorderStroke(1.dp, RunnaTheme.Line), RoundedCornerShape(16.dp))
                .then(
                    if (lastCompletedLog != null && onViewWorkoutDetail != null)
                        Modifier.clickable { onViewWorkoutDetail(lastCompletedLog, lastCompletedWorkout) }
                    else Modifier
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag("card_recent_runs")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Corridas recentes", style = runnaNum(20, FontWeight.Bold), color = RunnaTheme.Text)
                if (lastCompletedLog != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ver detalhes",
                        tint = RunnaTheme.Teal,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            if (lastCompletedLog != null) {
                RecentRunMiniPreview(
                    log = lastCompletedLog,
                    workout = lastCompletedWorkout,
                    onRequestRoutePoints = onRequestRoutePoints
                )
            } else {
                Text(
                    "Sua primeira corrida aparece aqui",
                    style = runnaUi(14f), color = RunnaTheme.Muted,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // --- Rodapé ---
        if (!shortScreen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (ajustesAutomaticos) "Ajustes automáticos ligados · " else "Ajustes automáticos desligados · ",
                    style = runnaUi(12f), color = RunnaTheme.Muted
                )
                Text(
                    text = "Ver histórico",
                    style = runnaUi(12f, FontWeight.SemiBold), color = RunnaTheme.Teal,
                    modifier = Modifier.clickable(onClick = onOpenAdjustments).testTag("link_ver_historico")
                )
            }
        }
    }

    // P12: Check-in pré-treino (Bottom Sheet)
    PreWorkoutCheckinBottomSheet(
        isOpen = showPreWorkoutCheckin,
        onDismiss = onClosePreWorkoutCheckin,
        onConfirm = { energia, sono ->
            onConfirmPreWorkoutCheckin(energia, sono)
        }
    )
    }
}

/**
 * Cartão "Treino de hoje" com título, 3 MetricBlock, chevron para passos e ActionButton com ondas decorativas.
 */
@Composable
private fun TodayWorkoutCard(
    workout: WorkoutEntity,
    isToday: Boolean,
    onStartRunClick: () -> Unit,
    onConfirmPreWorkoutCheckin: (energia: Int, sono: Int) -> Unit,
    onClosePreWorkoutCheckin: () -> Unit,
    onMarkCompleted: () -> Unit,
    onMarkSkipped: () -> Unit,
    onResetStatus: () -> Unit
) {
    val isCompleted = workout.status == WorkoutEntity.STATUS_CONCLUIDO
    val isSkipped = workout.status == WorkoutEntity.STATUS_PULADO
    val reducedMotion = rememberReducedMotion()
    val specs = rememberAnimatedVisibilitySpecs(reduceMotion = reducedMotion)

    EliteCard(
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, Olive.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_treino_hoje")
    ) {
        Column(modifier = Modifier.padding(EliteSpacing.xl)) {
            // Topo: Rótulo de status (estilo discreto) - AnimatedContent for smooth transition
            val density = LocalDensity.current
            AnimatedContent(
                targetState = when {
                    isCompleted -> TodayStatus.Completed
                    isSkipped -> TodayStatus.Skipped
                    else -> TodayStatus.Planned
                },
                transitionSpec = {
                    ContentTransform(
                        targetContentEnter = fadeIn(tween(EliteMotion.Enter, delayMillis = 50, easing = EliteMotion.Easing)) +
                        slideInVertically(
                            animationSpec = tween(EliteMotion.Enter, delayMillis = 50, easing = EliteMotion.Easing)
                        ) { with(density) { -EliteSpacing.lg.roundToPx() } },
                        initialContentExit = fadeOut(tween(EliteMotion.Exit, delayMillis = 0, easing = EliteMotion.Easing)) +
                        slideOutVertically(
                            animationSpec = tween(EliteMotion.Exit, delayMillis = 0, easing = EliteMotion.Easing)
                        ) { with(density) { EliteSpacing.lg.roundToPx() } }
                    )
                }
            ) { state ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (state) {
                        TodayStatus.Completed -> {
                            Text(
                                text = "Concluído ✓",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Olive
                            )
                        }
                        TodayStatus.Skipped -> {
                            Text(
                                text = "Pulado",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                        }
                        TodayStatus.Planned -> {
                            Text(
                                text = if (isToday) "Treino de hoje" else "Treino selecionado",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.lg))

            // Título principal (Grande e impactante)
            Text(
                text = workout.titulo,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(EliteSpacing.xs))

            // Subtítulo: Duração
            Text(
                text = "${workout.duracaoMinutos} min",
                style = MaterialTheme.typography.titleMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(EliteSpacing.xl))

            // Barra segmentada (Visual simplificado)
            WorkoutBlocksBar(steps = WorkoutStepConverter.fromJson(workout.passosJson), modifier = Modifier.testTag("barra_blocos_treino"))

            Spacer(modifier = Modifier.height(EliteSpacing.xxl))

            // Botão Principal (Estilo CTA Runna)
            ActionButton(
                text = "Começar treino",
                onClick = onStartRunClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("start_run_button")
            )
        }
    }
}

private enum class TodayStatus {
    Planned, Completed, Skipped
}

/**
 * Mini preview da última corrida no cartão "Corridas recentes".
 */
@Composable
private fun RecentRunMiniPreview(
    log: WorkoutLogEntity,
    workout: WorkoutEntity?,
    onRequestRoutePoints: suspend (Long) -> List<RoutePointEntity>
) {
    var routePoints by remember { mutableStateOf<List<RoutePointEntity>>(emptyList()) }

    LaunchedEffect(log.workoutId) {
        routePoints = onRequestRoutePoints(log.workoutId)
    }

    val minutes = log.duracaoSegundos / 60
    val seconds = log.duracaoSegundos % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)
    val distanceKm = "%.2f km".format(log.distanciaMetros / 1000.0)

    val dateFormatted = try {
        java.time.Instant.ofEpochMilli(log.finalizadoEm)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .format(DateTimeFormatter.ofPattern("d 'de' MMMM", java.util.Locale.forLanguageTag("pt-BR")))
    } catch (_: Exception) {
        workout?.data ?: ""
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mini mapa em Canvas (ou ícone se sem GPS)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF0D0F0A),
            border = BorderStroke(1.dp, RunnaTheme.Line),
            modifier = Modifier.size(52.dp)
        ) {
            if (routePoints.size >= 2) {
                Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    // Garante ordem cronológica e início->fim correto
                    val orderedPoints = routePoints.sortedBy { it.timestampMillis }
                    val coords = orderedPoints.map { RoutePointCoord(it.latitude, it.longitude) }
                    val normalized = RouteNormalizer.normalize(coords)
                    val pixelPoints = RouteNormalizer.toCanvasOffsets(
                        normalizedPoints = normalized,
                        widthPx = size.width,
                        heightPx = size.height,
                        paddingPx = 4f
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
                            color = RunnaTheme.Teal,
                            style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                        // Marcadores de início e fim
                        drawCircle(
                            color = RunnaTheme.Teal,
                            radius = 4f,
                            center = androidx.compose.ui.geometry.Offset(pixelPoints.first().first, pixelPoints.first().second)
                        )
                        drawCircle(
                            color = RunnaTheme.Orange,
                            radius = 4f,
                            center = androidx.compose.ui.geometry.Offset(pixelPoints.last().first, pixelPoints.last().second)
                        )
                    }
                }
            } else {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = RunnaTheme.Teal,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = workout?.titulo ?: "Corrida Elite",
                style = runnaUi(15f, FontWeight.SemiBold),
                color = RunnaTheme.Text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dateFormatted,
                style = runnaUi(12f),
                color = RunnaTheme.Muted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = distanceKm,
                    style = runnaNum(15, FontWeight.SemiBold),
                    color = RunnaTheme.Teal
                )
                Text(
                    text = timeFormatted,
                    style = runnaNum(15, FontWeight.SemiBold),
                    color = RunnaTheme.Muted
                )
            }
        }
    }
}

/**
 * Faixa da semana integrada no Card.
 */
@Composable
private fun GoalMetric(value: String, unit: String, label: String) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = EliteNumberStyles.MetricMedium, color = TextColor)
            Spacer(Modifier.width(EliteSpacing.xs))
            Text(
                text = unit,
                style = MaterialTheme.typography.titleSmall,
                color = TextColor,
                modifier = Modifier.padding(bottom = EliteSpacing.xs)
            )
        }
        Spacer(modifier = Modifier.height(EliteSpacing.xxs))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
private fun WeekStripHeader(
    weekDates: List<LocalDate>,
    markers: List<DayMarker>,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit
) {
    val dayLabels = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val dayNames = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")

    Row(
        modifier = Modifier.fillMaxWidth().testTag("week_strip_header_row"),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        weekDates.forEachIndexed { index, date ->
            val marker = markers.getOrNull(index) ?: DayMarker(date = date, type = DayMarkerType.NONE)
            val isSelected = date == selectedDate
            val isToday = date == currentDate
            val stateText = when (marker.type) {
                DayMarkerType.FILLED_CIRCLE -> "feito"
                DayMarkerType.DASH -> "pulado"
                DayMarkerType.EMPTY_CIRCLE -> "treino planejado"
                DayMarkerType.MISSED -> "perdido"
                DayMarkerType.NONE -> "descanso"
            }
            val description = dayNames.getOrElse(index) { "" } + (if (isToday) ", hoje" else "") + ", " + stateText
            val filled = marker.type == DayMarkerType.FILLED_CIRCLE

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .semantics(mergeDescendants = true) { contentDescription = description }
                    .clickable { onSelectDate(date) }
                    .testTag("day_header_${date.dayOfMonth}")
                    .weight(1f)
            ) {
                Text(
                    text = dayLabels.getOrElse(index) { "" },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isToday || isSelected) Olive else TextMuted,
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(EliteSpacing.xxs))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .then(if (filled) Modifier.background(Olive) else Modifier)
                        .border(
                            width = if (isSelected || isToday) 2.dp else 0.dp,
                            color = if (isSelected || isToday) Olive else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        filled -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = OnOlive,
                            modifier = Modifier.size(14.dp)
                        )
                        marker.type == DayMarkerType.DASH -> Text(
                            text = "–",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                        else -> Text(
                            text = "${date.dayOfMonth}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday || isSelected) TextColor else TextMuted
                        )
                    }
                }
            }
        }
    }
}


// ============================================================
// TELA HOJE - estilo Runna (componentes novos)
// ============================================================

private fun runnaNum(size: Int, weight: FontWeight = FontWeight.SemiBold) = TextStyle(
    fontFamily = RunnaNumberFont, fontWeight = weight,
    fontSize = size.sp, lineHeight = (size * 1.08f).sp, fontFeatureSettings = "tnum"
)

private fun runnaUi(size: Float, weight: FontWeight = FontWeight.Normal) = TextStyle(
    fontFamily = RunnaTextFont, fontWeight = weight,
    fontSize = size.sp, lineHeight = (size * 1.3f).sp
)

/** Faixa da semana: círculo com o número, letra do dia e ponto de estado. */
@Composable
private fun RunnaWeekStrip(
    weekDates: List<LocalDate>,
    markers: List<DayMarker>,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit
) {
    val dayLabels = listOf("S", "T", "Q", "Q", "S", "S", "D")
    val dayNames = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
    Row(
        modifier = Modifier.fillMaxWidth().testTag("week_strip_header_row"),
        verticalAlignment = Alignment.Top
    ) {
        weekDates.forEachIndexed { index, date ->
            val marker = markers.getOrNull(index) ?: DayMarker(date = date, type = DayMarkerType.NONE)
            val isToday = date == currentDate
            val isSelected = date == selectedDate
            val stateText = when (marker.type) {
                DayMarkerType.FILLED_CIRCLE -> "feito"
                DayMarkerType.DASH -> "pulado"
                DayMarkerType.EMPTY_CIRCLE -> "treino planejado"
                DayMarkerType.MISSED -> "perdido"
                DayMarkerType.NONE -> "descanso"
            }
            val description = dayNames.getOrElse(index) { "" } + (if (isToday) ", hoje" else "") + ", " + stateText
            val dotColor = when (marker.type) {
                DayMarkerType.FILLED_CIRCLE -> RunnaTheme.Teal
                DayMarkerType.EMPTY_CIRCLE -> if (isToday) RunnaTheme.Orange else RunnaTheme.Muted
                DayMarkerType.MISSED -> RunnaTheme.RedDot
                DayMarkerType.DASH -> RunnaTheme.Muted.copy(alpha = 0.5f)
                DayMarkerType.NONE -> RunnaTheme.Line
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .semantics(mergeDescendants = true) { contentDescription = description }
                    .clickable { onSelectDate(date) }
                    .testTag("day_header_${date.dayOfMonth}")
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .then(if (isToday) Modifier.background(RunnaTheme.Teal) else Modifier)
                        .then(
                            if (isSelected && !isToday) Modifier.border(2.dp, RunnaTheme.Teal, CircleShape)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${date.dayOfMonth}",
                        style = runnaNum(16),
                        color = if (isToday) RunnaTheme.TealOn else RunnaTheme.Text
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = dayLabels.getOrElse(index) { "" },
                    style = runnaUi(12f, FontWeight.Medium),
                    color = if (isToday || isSelected) RunnaTheme.Teal else RunnaTheme.Muted
                )
                Spacer(Modifier.height(4.dp))
                Box(Modifier.size(4.dp).clip(CircleShape).background(dotColor))
            }
        }
    }
}

private class NoticeAction(val label: String, val tag: String, val primary: Boolean, val onClick: () -> Unit)

/** Aviso compacto (pausa, recuperação, sugestão, ajuste). */
@Composable
private fun RunnaNotice(
    title: String,
    body: String?,
    accent: Color,
    actions: List<NoticeAction>,
    tag: String
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(RunnaTheme.Card)
            .border(BorderStroke(1.dp, accent.copy(alpha = 0.5f)), shape)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(tag)
    ) {
        Text(title, style = runnaUi(14f, FontWeight.SemiBold), color = accent)
        if (body != null) {
            Spacer(Modifier.height(4.dp))
            Text(body, style = runnaUi(13f), color = RunnaTheme.Text)
        }
        if (actions.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                actions.forEach { a ->
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .then(if (a.primary) Modifier.background(accent) else Modifier)
                            .clickable(onClick = a.onClick)
                            .padding(horizontal = 14.dp)
                            .testTag(a.tag),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            a.label,
                            style = runnaUi(13f, FontWeight.SemiBold),
                            color = if (a.primary) RunnaTheme.TealOn else RunnaTheme.Muted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RunnaTile(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .clip(shape)
            .background(RunnaTheme.Card)
            .border(BorderStroke(1.dp, RunnaTheme.Line), shape)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { content() }
}

@Composable
private fun RunnaProgressRing(progress: Float, text: String) {
    Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 5.dp.toPx()
            val inset = stroke / 2f
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(Color(0x1AECE8DA), 0f, 360f, false, Offset(inset, inset), arcSize, style = Stroke(stroke))
            drawArc(
                RunnaTheme.Teal, -90f, 360f * progress.coerceIn(0f, 1f), false,
                Offset(inset, inset), arcSize, style = Stroke(stroke, cap = StrokeCap.Round)
            )
        }
        Text(text, style = runnaNum(15, FontWeight.Bold), color = RunnaTheme.Text)
    }
}

/** "8 × 1 min" quando todos os blocos têm a mesma duração; senão, o total em minutos. */
private fun partSummary(steps: List<WorkoutStep>, types: Set<StepType>): String {
    val list = steps.filter { it.type in types }
    if (list.isEmpty()) return "—"
    fun fmt(sec: Int) = if (sec % 60 == 0) "${sec / 60} min" else "$sec s"
    val durations = list.map { it.durationSeconds }.distinct()
    return when {
        list.size == 1 -> fmt(durations[0])
        durations.size == 1 -> "${list.size} × ${fmt(durations[0])}"
        else -> "${list.sumOf { it.durationSeconds } / 60} min"
    }
}

/** Card do treino: progresso do plano, título, gráfico de intensidade, resumo e botão. */
@Composable
private fun RunnaWorkoutHero(
    workout: WorkoutEntity,
    isToday: Boolean,
    totalWeeks: Int,
    showAxis: Boolean,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember(workout.passosJson) { WorkoutStepConverter.fromJson(workout.passosJson) }
    val completed = workout.status == WorkoutEntity.STATUS_CONCLUIDO
    val skipped = workout.status == WorkoutEntity.STATUS_PULADO
    val title = workout.titulo.substringAfter("•", workout.titulo).trim()
    val rightLabel = when {
        completed -> "Concluído"
        skipped -> "Pulado"
        isToday -> "Treino de hoje"
        else -> "Treino selecionado"
    }
    val totalMin = steps.sumOf { it.durationSeconds }.let { if (it > 0) it / 60 else workout.duracaoMinutos }
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = modifier
            .clip(shape)
            .background(RunnaTheme.Card)
            .border(BorderStroke(1.dp, RunnaTheme.Line), shape)
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 16.dp)
            .testTag("card_treino_hoje")
    ) {
        // Semana X de N  |  status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row {
                Text("Semana ${workout.semanaIndex}", style = runnaUi(13f, FontWeight.SemiBold), color = RunnaTheme.Text)
                if (totalWeeks > 0) Text(" de $totalWeeks", style = runnaUi(13f), color = RunnaTheme.Muted)
            }
            Text(
                rightLabel, style = runnaUi(13f),
                color = if (completed) RunnaTheme.Teal else RunnaTheme.Muted
            )
        }
        if (totalWeeks > 0) {
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                for (week in 1..totalWeeks) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (week <= workout.semanaIndex) RunnaTheme.Teal else RunnaTheme.Line)
                    )
                }
            }
        }

        // Título + duração
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(title, style = runnaNum(40, FontWeight.Bold), color = RunnaTheme.Text, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            Text("${workout.duracaoMinutos} min", style = runnaNum(20), color = RunnaTheme.Muted)
        }

        // Gráfico: largura = tempo, altura = intensidade (ocupa o espaço restante, até 110dp)
        Box(
            modifier = Modifier.fillMaxWidth().height(76.dp).padding(top = 12.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .testTag("barra_blocos_treino"),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                steps.forEach { step ->
                    val (color, h) = when (step.type) {
                        StepType.RUN -> RunnaTheme.Teal to 1f
                        StepType.WALK -> RunnaTheme.Walk to 0.45f
                        else -> RunnaTheme.Warm to 0.4f
                    }
                    Box(
                        Modifier
                            .weight(step.durationSeconds.toFloat().coerceAtLeast(1f))
                            .fillMaxHeight(h)
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(color)
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(RunnaTheme.Line))
        if (showAxis) {
            Spacer(Modifier.height(5.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("0", style = runnaUi(11f), color = RunnaTheme.Muted)
                Text("${totalMin / 2} min", style = runnaUi(11f), color = RunnaTheme.Muted)
                Text("$totalMin min", style = runnaUi(11f), color = RunnaTheme.Muted)
            }
        }

        // Resumo em 3 blocos
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(RunnaTheme.Line))
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            val parts = listOf(
                Triple(partSummary(steps, setOf(StepType.WARMUP, StepType.COOLDOWN)), "Aquec./desaq.", RunnaTheme.Warm),
                Triple(partSummary(steps, setOf(StepType.RUN)), "Correr", RunnaTheme.Teal),
                Triple(partSummary(steps, setOf(StepType.WALK)), "Caminhar", RunnaTheme.Walk)
            )
            parts.forEachIndexed { i, (value, label, dot) ->
                if (i > 0) Box(Modifier.fillMaxHeight().width(1.dp).background(RunnaTheme.Line))
                Column(Modifier.weight(1f).padding(start = if (i > 0) 12.dp else 0.dp)) {
                    Text(value, style = runnaNum(20), color = RunnaTheme.Text, maxLines = 1)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(dot))
                        Spacer(Modifier.width(6.dp))
                        Text(label, style = runnaUi(11.5f), color = RunnaTheme.Muted, maxLines = 1)
                    }
                }
            }
        }

        // Distância e Ritmo (para treinos concluídos ou com meta de distância)
        Spacer(Modifier.height(12.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(RunnaTheme.Line))
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            // Distância
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val distanceMeters = workout.distanciaAlvoM ?: steps.sumOf { it.durationSeconds }.let { if (it > 0) (it / 60.0 * 100).toInt() else 0 }
                Text(
                    text = WorkoutFormatters.formatDistance(distanceMeters),
                    style = runnaNum(28, FontWeight.Bold),
                    color = RunnaTheme.Teal
                )
                Spacer(Modifier.height(2.dp))
                Text("Distância", style = runnaUi(11f), color = RunnaTheme.Muted)
            }
            // Ritmo médio estimado
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val totalDistanceKm = (workout.distanciaAlvoM ?: steps.sumOf { it.durationSeconds }.let { if (it > 0) (it / 60.0 * 100).toInt() else 0 }) / 1000.0
                val paceText = if (totalDistanceKm > 0.1) {
                    val totalSecs = (workout.duracaoMinutos * 60L)
                    val paceSecPerKm = totalSecs / totalDistanceKm
                    val min = (paceSecPerKm / 60).toInt()
                    val sec = (paceSecPerKm % 60).toInt()
                    String.format("%d:%02d /km", min, sec)
                } else "— /km"
                Text(
                    text = paceText,
                    style = runnaNum(28, FontWeight.Bold),
                    color = RunnaTheme.Text
                )
                Spacer(Modifier.height(2.dp))
                Text("Ritmo médio", style = runnaUi(11f), color = RunnaTheme.Muted)
            }
        }

        // Botão principal
        Spacer(Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (completed) RunnaTheme.Line else RunnaTheme.Orange)
                .then(if (completed) Modifier else Modifier.clickable(onClick = onStart))
                .testTag("start_run_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (completed) "Treino concluído" else "Começar treino",
                style = runnaUi(17f, FontWeight.SemiBold),
                color = if (completed) RunnaTheme.Muted else RunnaTheme.OrangeOn
            )
        }
    }
}

/** Dia de descanso, no mesmo padrão visual do card do treino. */
@Composable
private fun RunnaRestCard(
    nextWorkout: WorkoutEntity?,
    onViewFullPlan: () -> Unit,
    onStartFreeRun: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val nextInfo = if (nextWorkout != null) {
        val dateFmt = try {
            LocalDate.parse(nextWorkout.data)
                .format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", java.util.Locale.forLanguageTag("pt-BR")))
        } catch (_: Exception) { nextWorkout.data }
        "Próximo treino: $dateFmt, ${nextWorkout.titulo}"
    } else "Recupere o corpo para a sua próxima corrida."

    Column(
        modifier = modifier
            .clip(shape)
            .background(RunnaTheme.Card)
            .border(BorderStroke(1.dp, RunnaTheme.Line), shape)
            .padding(18.dp)
            .testTag("card_descanso"),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hoje é dia de descanso", style = runnaNum(32, FontWeight.Bold), color = RunnaTheme.Text)
        Spacer(Modifier.height(8.dp))
        Text(nextInfo, style = runnaUi(14f), color = RunnaTheme.Muted)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f).height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(BorderStroke(1.dp, RunnaTheme.Line), RoundedCornerShape(16.dp))
                    .clickable(onClick = onViewFullPlan)
                    .testTag("btn_ver_plano_descanso"),
                contentAlignment = Alignment.Center
            ) { Text("Ver plano", style = runnaUi(15f, FontWeight.SemiBold), color = RunnaTheme.Teal) }
            Box(
                modifier = Modifier
                    .weight(1f).height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(RunnaTheme.Orange)
                    .clickable(onClick = onStartFreeRun)
                    .testTag("btn_free_run_descanso"),
                contentAlignment = Alignment.Center
            ) { Text("Corrida livre", style = runnaUi(15f, FontWeight.SemiBold), color = RunnaTheme.OrangeOn) }
        }
    }
}
