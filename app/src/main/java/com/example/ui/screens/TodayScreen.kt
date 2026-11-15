package com.example.ui.screens

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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import com.example.R
import com.example.ui.components.WorkoutBlocksBar
import com.example.ui.theme.AisleChartColors
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleNumberStyles
import com.example.ui.theme.AisleOnPrimary
import com.example.ui.theme.AisleShadowOverlay
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.PauseBannerGradientEnd
import com.example.ui.theme.rememberAnimatedVisibilitySpecs
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.local.WorkoutStepConverter
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity
import com.example.model.StepType
import com.example.model.WorkoutStep
import com.example.tracker.DayMarker
import com.example.tracker.DayMarkerType
import com.example.tracker.PaceCalculator
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import com.example.tracker.WeekMarkerCalculator
import com.example.tracker.WeeklyGoalsCalculator
import com.example.ui.components.ActionButton
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleCardGradient
import com.example.ui.components.AisleLogo
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.components.MetricBlock
import com.example.ui.components.ProgressRing
import com.example.ui.components.StepItemView
import com.example.ui.components.WaveArcs
import com.example.ui.screens.PreWorkoutCheckinBottomSheet
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleAlert
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AislePurple
import com.example.ui.theme.AisleTextAccent2
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTextTertiary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusPlanned
import com.example.ui.theme.StatusSkipped
import com.example.ui.theme.rememberAnimatedVisibilitySpecs
import com.example.ui.theme.rememberReducedMotion
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

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
    autoAdjustments: List<com.example.data.local.entities.AutoAdjustmentEntity> = emptyList(),
    adaptiveSuggestion: com.example.generator.AdaptiveResult? = null,
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
            title = { Text("Permissão de Localização", color = AisleTextPrimary) },
            text = {
                Text(
                    "Usamos sua localização só para medir distância e ritmo. Os dados ficam no seu celular.",
                    color = AisleTextSecondary
                )
            },
            containerColor = AisleCard,
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
                    colors = ButtonDefaults.buttonColors(containerColor = AisleTurquoise),
                    modifier = Modifier.testTag("btn_conceder_permissao_gps")
                ) {
                    Text("Continuar", color = AisleOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGpsExplanationDialog = false }) {
                    Text("Cancelar", color = AisleTextSecondary)
                }
            }
        )
    }

    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text("Localização não concedida", color = AisleTextPrimary) },
            text = {
                Text(
                    "Sem GPS, não registraremos distância nem ritmo em tempo real. Você pode seguir usando apenas o cronômetro e a voz, ou autorizar nas configurações.",
                    color = AisleTextSecondary
                )
            },
            containerColor = AisleCard,
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDeniedDialog = false
                        todayWorkout?.let { onStartRunClick(it, false) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AisleTurquoise),
                    modifier = Modifier.testTag("btn_seguir_sem_gps")
                ) {
                    Text("Seguir sem GPS", color = AisleOnPrimary, fontWeight = FontWeight.Bold)
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
                    Text("Abrir Configurações", color = AisleTurquoise)
                }
            }
        )
    }

    if (showGpsDisabledWarning) {
        AlertDialog(
            onDismissRequest = { showGpsDisabledWarning = false },
            title = { Text("GPS do aparelho desligado", color = AisleTextPrimary) },
            text = {
                Text(
                    "O GPS do seu smartphone está desativado. Ative a localização para que o app possa medir sua distância.",
                    color = AisleTextSecondary
                )
            },
            containerColor = AisleCard,
            confirmButton = {
                Button(
                    onClick = {
                        showGpsDisabledWarning = false
                        todayWorkout?.let { onStartRunClick(it, true) }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AisleTurquoise)
                ) {
                    Text("Continuar mesmo assim", color = AisleOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showGpsDisabledWarning = false
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                ) {
                    Text("Ativar GPS", color = AisleTurquoise)
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reiniciar plano?", color = AisleTextPrimary) },
            text = { Text("Isso apagará o progresso atual e permitirá configurar um novo plano do início.", color = AisleTextSecondary) },
            containerColor = AisleCard,
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetEntirePlan()
                    },
                    modifier = Modifier.testTag("confirm_reset_plan_button")
                ) {
                    Text("Reiniciar", color = AisleAlert, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancelar", color = AisleTextSecondary)
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
        com.example.ui.AdjustmentPresentation.recenteAtivo(autoAdjustments)?.let {
            com.example.ui.AdjustmentPresentation.toView(it, LocalDate.now())
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

    Scaffold(
        containerColor = AisleBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier.fillMaxSize().testTag("screen_today")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // 1. CABEÇALHO INTEGRADO COM WEEK STRIP (Runna-like)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = AisleSpacing.xxl, vertical = AisleSpacing.xl)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = headerDateText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = AisleTextSecondary
                            )
                            Text(
                                text = headerGreeting,
                                style = MaterialTheme.typography.headlineLarge,
                                color = AisleTextPrimary,
                                modifier = Modifier.testTag("txt_saudacao")
                            )
                        }
                        if (weekChipText != null) {
                            Surface(
                                shape = CircleShape,
                                color = AislePurple.copy(alpha = 0.28f)
                            ) {
                                Text(
                                    text = weekChipText,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AisleTextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(AisleSpacing.lg))
                    // Week Strip integrada organicamente ao cabeçalho
                    Box(modifier = Modifier.fillMaxWidth().testTag("week_strip_header")) {
                        WeekStripHeader(
                            weekDates = weekDates,
                            markers = weekMarkers,
                            currentDate = currentDate,
                            selectedDate = selectedDate,
                            onSelectDate = { selectedDate = it }
                        )
                    }
                }
            }

            // 2. CONTEÚDO DOS CARTÕES COM SOBREPOSIÇÃO DE 24 DP NA BASE DA FAIXA
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AisleSpacing.xxl),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.xl)
                ) {
                    // FAIXA DISCRETA DE PAUSA ATIVA
                    if (activePauseAdjustment != null) {
                        val fimFormatted = try {
                            LocalDate.parse(activePauseAdjustment.fim).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        } catch (_: Exception) {
                            activePauseAdjustment.fim
                        }
                        AisleCard(
                            modifier = Modifier.fillMaxWidth().testTag("banner_pausa_ativa")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = AisleSpacing.xl, vertical = AisleSpacing.lg),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = null,
                                    tint = AisleTurquoise,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(AisleSpacing.md))
                                Text(
                                    text = "Pausa até $fimFormatted",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AisleTextPrimary
                                )
                            }
                        }
                    }

                    // RECUPERAÇÃO DE CORRIDA ATIVA GRAVADA NO BANCO
                    if (activeRunToRecover != null) {
                        AisleCard(
                            border = BorderStroke(1.dp, AisleActionOrange),
                            modifier = Modifier.fillMaxWidth().testTag("card_recuperar_corrida")
                        ) {
                            Column(modifier = Modifier.padding(AisleSpacing.xl)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = AisleActionOrange)
                                    Spacer(modifier = Modifier.width(AisleSpacing.md))
                                    Text(
                                        text = stringResource(R.string.recover_run_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AisleTextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.sm))
                                Text(
                                    text = stringResource(R.string.recover_run_message),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AisleTextSecondary
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.lg))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = onDiscardActiveRun) {
                                        Text("Descartar", color = AisleAlert)
                                    }
                                    Spacer(modifier = Modifier.width(AisleSpacing.md))
                                    Button(
                                        onClick = onResumeActiveRun,
                                        colors = ButtonDefaults.buttonColors(containerColor = AisleTurquoise)
                                    ) {
                                        Text("Retomar corrida", color = AisleOnPrimary, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // CARTÃO DE SUGESTÃO DO MOTOR (modo cauteloso: precisa de um toque)
                    if (adaptiveSuggestion != null) {
                        AisleCard(
                            border = BorderStroke(1.dp, AisleTurquoise),
                            modifier = Modifier.fillMaxWidth().testTag("card_sugestao_ajuste")
                        ) {
                            Column(modifier = Modifier.padding(AisleSpacing.xl)) {
                                Text(
                                    text = "Sugestão do Aisle",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AisleTurquoise
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.sm))
                                Text(
                                    text = adaptiveSuggestion.mensagem
                                        ?: adaptiveSuggestion.regra?.let { com.example.generator.AdaptiveEngine.mensagemPadrao(it) }
                                        ?: "Dá para ajustar o seu plano.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AisleTextPrimary
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.lg))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    com.example.ui.components.AisleButton(
                                        text = "Aplicar",
                                        onClick = onAcceptSuggestion,
                                        modifier = Modifier.testTag("btn_aplicar_sugestao")
                                    )
                                    com.example.ui.components.AisleButton(
                                        text = "Agora não",
                                        onClick = onDismissSuggestion,
                                        variant = com.example.ui.components.AisleButtonVariant.Text,
                                        modifier = Modifier.testTag("btn_dispensar_sugestao")
                                    )
                                }
                            }
                        }
                    }

                    // CARTÃO "SEU PLANO SE AJUSTOU" (ajuste ativo recente)
                    if (recentAdjView != null) {
                        var showWhy by remember(recentAdjView.id) { mutableStateOf(false) }
                        AisleCard(
                            border = BorderStroke(1.dp, AislePurple),
                            modifier = Modifier.fillMaxWidth().testTag("card_ajuste_plano")
                        ) {
                            Column(modifier = Modifier.padding(AisleSpacing.xl)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Autorenew,
                                        contentDescription = null,
                                        tint = AisleTextAccent2
                                    )
                                    Spacer(modifier = Modifier.width(AisleSpacing.md))
                                    Text(
                                        text = "Seu plano se ajustou",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = AisleTextAccent2
                                    )
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.md))
                                Text(
                                    text = recentAdjView.motivo,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AisleTextPrimary
                                )
                                if (showWhy) {
                                    Spacer(modifier = Modifier.height(AisleSpacing.sm))
                                    Text(
                                        text = recentAdjView.titulo,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = AisleTextPrimary
                                    )
                                    if (recentAdjView.antes != null && recentAdjView.depois != null &&
                                        recentAdjView.antes != recentAdjView.depois) {
                                        Text(
                                            text = "${recentAdjView.antes}  →  ${recentAdjView.depois}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = AisleTextSecondary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.lg))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    com.example.ui.components.AisleButton(
                                        text = if (showWhy) "Ocultar" else "Por quê?",
                                        onClick = { showWhy = !showWhy },
                                        variant = com.example.ui.components.AisleButtonVariant.Secondary,
                                        modifier = Modifier.testTag("btn_por_que_ajuste")
                                    )
                                    com.example.ui.components.AisleButton(
                                        text = "Desfazer",
                                        onClick = { onUndoAdjustment(recentAdjView.id) },
                                        variant = com.example.ui.components.AisleButtonVariant.Text,
                                        modifier = Modifier.testTag("btn_desfazer_ajuste")
                                    )
                                }
                            }
                        }
                    }

                    // CARTÃO "TREINO DE HOJE"
                    if (displayedWorkout != null) {
                        TodayWorkoutAisleCard(
                            workout = displayedWorkout,
                            isToday = (selectedDate == currentDate),
                            onStartRunClick = {
                                // P12: Abrir check-in pré-treino ao invés de iniciar direto
                                onOpenPreWorkoutCheckin(displayedWorkout)
                            },
                            onConfirmPreWorkoutCheckin = onConfirmPreWorkoutCheckin,
                            onClosePreWorkoutCheckin = onClosePreWorkoutCheckin,
                            onMarkCompleted = { onMarkCompleted(displayedWorkout.id) },
                            onMarkSkipped = { onMarkSkipped(displayedWorkout.id) },
                            onResetStatus = { onResetStatus(displayedWorkout.id) }
                        )
                    } else {
                        // Dia de descanso
                        AisleCard(modifier = Modifier.fillMaxWidth().testTag("card_descanso")) {
                            Column(
                                modifier = Modifier.padding(AisleSpacing.xl),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = AislePurple.copy(alpha = 0.2f),
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Nightlight,
                                            contentDescription = null,
                                            tint = AislePurple,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.lg))
                                Text(
                                    text = "Hoje é dia de descanso",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AisleTextPrimary
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.xs))
                                val nextInfo = if (nextWorkout != null) {
                                    val dateFmt = try {
                                        LocalDate.parse(nextWorkout.data).format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", java.util.Locale.forLanguageTag("pt-BR")))
                                    } catch (_: Exception) {
                                        nextWorkout.data
                                    }
                                    "Próximo treino: $dateFmt • ${nextWorkout.titulo}"
                                } else {
                                    "Recupere o corpo para a sua próxima corrida."
                                }
                                Text(
                                    text = nextInfo,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AisleTextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.xl))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                                ) {
                                    TextButton(
                                        onClick = onViewFullPlan,
                                        modifier = Modifier.weight(1f).testTag("btn_ver_plano_descanso")
                                    ) {
                                        Text("Ver plano completo", color = AisleTurquoise, fontWeight = FontWeight.Bold)
                                    }
                                    com.example.ui.components.AisleButton(
                                        text = "Corrida Livre",
                                        onClick = { onStartFreeRun?.invoke() },
                                        variant = com.example.ui.components.AisleButtonVariant.Secondary,
                                        modifier = Modifier.weight(1f).testTag("btn_free_run_descanso")
                                    )
                                }
                            }
                        }
                    }

                    // Week Strip removida daqui (agora está no cabeçalho integrado)

                    // META DA SEMANA: versão minimalista Runna-like
                    Row(
                        modifier = Modifier.fillMaxWidth().testTag("card_weekly_goals_minimal"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProgressRing(
                            progress = weeklyGoals.workoutsProgress,
                            gradient = Brush.linearGradient(listOf(AisleTurquoise, AisleChartColors.RingTurquoiseEnd)),
                            valueText = "${weeklyGoals.completedWorkouts}/${weeklyGoals.plannedWorkouts}",
                            labelText = "",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.width(AisleSpacing.xl))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.1f km", weekLogsKm),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AisleTextPrimary
                            )
                            Text(
                                text = "$weekLogsMinutes min em movimento",
                                style = MaterialTheme.typography.bodySmall,
                                color = AisleTextSecondary
                            )
                        }
                    }

                    // CARTÃO "CORRIDAS RECENTES"
                    AisleCard(
                        modifier = Modifier.fillMaxWidth().testTag("card_recent_runs"),
                        onClick = if (lastCompletedLog != null && onViewWorkoutDetail != null) {
                            { onViewWorkoutDetail(lastCompletedLog, lastCompletedWorkout) }
                        } else null
                    ) {
                        Column(modifier = Modifier.padding(AisleSpacing.xl)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Corridas recentes",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AisleTextPrimary
                                )
                                if (lastCompletedLog != null) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Ver detalhes",
                                        tint = AisleTextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(AisleSpacing.lg))

                            if (lastCompletedLog != null) {
                                RecentRunMiniPreview(
                                    log = lastCompletedLog,
                                    workout = lastCompletedWorkout,
                                    onRequestRoutePoints = onRequestRoutePoints
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = AisleSpacing.lg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Sua primeira corrida aparece aqui",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = AisleTextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // RODAPÉ: estado dos ajustes automáticos e atalho para o histórico
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (ajustesAutomaticos) "Ajustes automáticos ligados · " else "Ajustes automáticos desligados · ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AisleTextSecondary
                        )
                        Text(
                            text = "Ver histórico",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AisleTurquoise,
                            modifier = Modifier
                                .clickable(onClick = onOpenAdjustments)
                                .padding(vertical = AisleSpacing.lg)
                                .testTag("link_ver_historico")
                        )
                    }

                    Spacer(modifier = Modifier.height(AisleSpacing.xxl))
                }
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

/**
 * Cartão "Treino de hoje" com título, 3 MetricBlock, chevron para passos e ActionButton com ondas decorativas.
 */
@Composable
private fun TodayWorkoutAisleCard(
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

    AisleCard(
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, AisleTurquoise.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_treino_hoje")
    ) {
        Column(modifier = Modifier.padding(AisleSpacing.xl)) {
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
                        targetContentEnter = fadeIn(tween(AisleMotion.Enter, delayMillis = 50, easing = AisleMotion.Easing)) +
                        slideInVertically(
                            animationSpec = tween(AisleMotion.Enter, delayMillis = 50, easing = AisleMotion.Easing)
                        ) { with(density) { -AisleSpacing.lg.roundToPx() } },
                        initialContentExit = fadeOut(tween(AisleMotion.Exit, delayMillis = 0, easing = AisleMotion.Easing)) +
                        slideOutVertically(
                            animationSpec = tween(AisleMotion.Exit, delayMillis = 0, easing = AisleMotion.Easing)
                        ) { with(density) { AisleSpacing.lg.roundToPx() } }
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
                                color = StatusCompleted
                            )
                        }
                        TodayStatus.Skipped -> {
                            Text(
                                text = "Pulado",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = StatusSkipped
                            )
                        }
                        TodayStatus.Planned -> {
                            Text(
                                text = if (isToday) "Treino de hoje" else "Treino selecionado",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = AisleTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.lg))

            // Título principal (Grande e impactante)
            Text(
                text = workout.titulo,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = AisleTextPrimary
            )

            Spacer(modifier = Modifier.height(AisleSpacing.xs))

            // Subtítulo: Duração
            Text(
                text = "${workout.duracaoMinutos} min",
                style = MaterialTheme.typography.titleMedium,
                color = AisleTextSecondary
            )

            Spacer(modifier = Modifier.height(AisleSpacing.xl))

            // Barra segmentada (Visual simplificado)
            WorkoutBlocksBar(steps = WorkoutStepConverter.fromJson(workout.passosJson), modifier = Modifier.testTag("barra_blocos_treino"))

            Spacer(modifier = Modifier.height(AisleSpacing.xxl))

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
            color = AisleBackground,
            border = BorderStroke(1.dp, AisleBorder),
            modifier = Modifier.size(68.dp)
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
                            brush = BrandGradient,
                            style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                        // Marcadores de início e fim
                        drawCircle(
                            color = AisleTurquoise,
                            radius = 4f,
                            center = androidx.compose.ui.geometry.Offset(pixelPoints.first().first, pixelPoints.first().second)
                        )
                        drawCircle(
                            color = AisleActionOrange,
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
                        tint = AisleTurquoise,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = workout?.titulo ?: "Corrida Aisle",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AisleTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = dateFormatted,
                style = MaterialTheme.typography.labelSmall,
                color = AisleTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = distanceKm,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AisleTurquoise
                )
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.labelMedium,
                    color = AisleTextSecondary
                )
            }
        }
    }
}

/**
 * Faixa da semana integrada no AisleCard.
 */
@Composable
private fun GoalMetric(value: String, unit: String, label: String) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = AisleNumberStyles.MetricMedium, color = AisleTextPrimary)
            Spacer(Modifier.width(AisleSpacing.xs))
            Text(
                text = unit,
                style = MaterialTheme.typography.titleSmall,
                color = AisleTextPrimary,
                modifier = Modifier.padding(bottom = AisleSpacing.xs)
            )
        }
        Spacer(modifier = Modifier.height(AisleSpacing.xxs))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = AisleTextSecondary)
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
                    color = if (isToday || isSelected) AisleTurquoise else AisleTextSecondary,
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(AisleSpacing.xxs))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .then(if (filled) Modifier.background(AisleTurquoise) else Modifier)
                        .border(
                            width = if (isSelected || isToday) 2.dp else 0.dp,
                            color = if (isSelected || isToday) AisleTurquoise else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        filled -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = AisleOnPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        marker.type == DayMarkerType.DASH -> Text(
                            text = "–",
                            style = MaterialTheme.typography.bodySmall,
                            color = AisleTextSecondary
                        )
                        else -> Text(
                            text = "${date.dayOfMonth}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday || isSelected) AisleTextPrimary else AisleTextSecondary
                        )
                    }
                }
            }
        }
    }
}
