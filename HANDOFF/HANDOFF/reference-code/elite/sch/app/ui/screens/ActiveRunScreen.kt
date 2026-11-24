package com.elite.sch.app.ui.screens

import com.elite.sch.app.ui.components.EliteDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.onSizeChanged
import android.widget.Toast
import com.elite.sch.app.ui.components.MapStyleFan
import com.elite.sch.app.ui.state.MapBaseStyle
import com.elite.sch.app.ui.state.pillLabel
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.elite.sch.app.ui.components.stepTypeColor
import com.elite.sch.app.service.RunState
import com.elite.sch.app.ui.components.FreeRunLayout
import com.elite.sch.app.ui.components.MapViewComposable
import com.elite.sch.app.ui.components.MetricBlock
import com.elite.sch.app.ui.components.rememberEliteWindowSizeClass
import com.elite.sch.app.ui.components.rememberTimerFontScale
import com.elite.sch.app.ui.state.CameraMode
import com.elite.sch.app.ui.state.MapPoint
import com.elite.sch.app.ui.state.MapPreferences
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.EliteTopScrimStops
import com.elite.sch.app.ui.theme.rememberReducedMotion
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.Glass
import com.elite.sch.app.ui.theme.GlassBar
import com.elite.sch.app.ui.theme.RunnaGlass
import com.elite.sch.app.ui.theme.RunnaGlassBorder
import com.elite.sch.app.ui.theme.EliteTrack
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Amber
import kotlinx.coroutines.delay

// Novos componentes Runna-style
import com.elite.sch.app.ui.components.ActionBar
import com.elite.sch.app.ui.components.BatteryWarningBanner
import com.elite.sch.app.ui.components.GpsStatusChip
import com.elite.sch.app.ui.components.RadialMenu
import com.elite.sch.app.ui.components.RunnaTheme
import com.elite.sch.app.ui.components.TimerRing
import com.elite.sch.app.ui.components.StatsRow
import com.elite.sch.app.ui.theme.Text as TextColor

@Composable
fun ActiveRunScreen(
    runState: RunState,
    dismissBatteryWarning: Boolean = false,
    onDismissBatteryWarning: () -> Unit = {},
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onFinishClick: () -> Unit,
    onToggleVoice: () -> Unit,
    onToggleVibration: () -> Unit,
    steps: List<WorkoutStep> = emptyList(),
    isFreeRun: Boolean = false,
    showMapDuringRun: Boolean = true,
    cameraMode: CameraMode = CameraMode.FollowUser,
    onCameraModeChange: (CameraMode) -> Unit = {},
    onRecenter: () -> Unit = {},
    mapPreferences: MapPreferences = MapPreferences(),
    onMapPreferencesChange: (MapPreferences) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val windowSizeClass = rememberEliteWindowSizeClass()
    val timerFontScale = rememberTimerFontScale(windowSizeClass)
    val reducedMotion = rememberReducedMotion()

    // Tela sempre ligada durante a corrida ativa
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    var showFinishDialog by remember { mutableStateOf(false) }

    // Bloqueia voltar acidental
    BackHandler {
        showFinishDialog = true
    }

    if (showFinishDialog) {
        EliteDialog(
            title = "Encerrar o treino agora?",
            text = "Você poderá ver o resumo com o tempo e a distância percorrida até aqui.",
            onDismiss = { showFinishDialog = false },
            confirmLabel = "Encerrar e Salvar",
            onConfirm = {
                showFinishDialog = false
                onFinishClick()
            },
            dismissLabel = "Continuar Treinando",
            destructive = true,
                icon = Icons.Default.Warning,
                confirmTestTag = "confirm_finish_run_button",
                isFinishDialog = true
        )
    }

    val isStepRun = runState.currentStep?.type == StepType.RUN
    val stepColor = if (isStepRun) Olive else Brass
    val stepIcon = if (isStepRun) Icons.AutoMirrored.Filled.DirectionsRun else Icons.AutoMirrored.Filled.DirectionsWalk
    val stepActionLabel = if (isStepRun) "Corra" else "Caminhe"

    val minutes = runState.timeRemainingInStep / 60
    val seconds = runState.timeRemainingInStep % 60
    val stepTimeFormatted = "%02d:%02d".format(minutes, seconds)

    val totalMinutes = runState.elapsedSeconds / 60
    val totalSecs = runState.elapsedSeconds % 60
    val totalTimeFormatted = "%02d:%02d".format(totalMinutes, totalSecs)

    val distanceKm = "%.2f".format(runState.totalDistanceMeters / 1000.0)

    val locationManager = remember { context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager }
    val isDeviceGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: true

    // Verificação de economia de bateria
    val powerManager = remember { context.getSystemService(Context.POWER_SERVICE) as? PowerManager }
    val isPowerSaveMode = powerManager?.isPowerSaveMode == true
    val isIgnoringOptimizations = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: true
    } else {
        true
    }
    var batteryBannerDismissedInSession by remember { mutableStateOf(false) }
    val showBatteryWarning = (isPowerSaveMode || !isIgnoringOptimizations) && !dismissBatteryWarning && !batteryBannerDismissedInSession
    var mapExpanded by remember { mutableStateOf(false) }
    var mapControlsVisible by remember { mutableStateOf(true) }
    var showMapMenu by remember { mutableStateOf(false) }
    var radialOpen by remember { mutableStateOf(false) }

    // Leque de opções do mapa (botão inferior direito)
    var mapFanOpen by remember { mutableStateOf(false) }
    var mapBaseStyleName by rememberSaveable { mutableStateOf(MapBaseStyle.STANDARD.name) }
    var mapDark by rememberSaveable { mutableStateOf(false) }
    var mapStreetNames by rememberSaveable { mutableStateOf(true) }
    // Altura do bloco inferior (card "A seguir", quando existe, + ActionBar). O leque fica 10dp acima dele.
    var bottomBlockHeightPx by remember { mutableStateOf(0) }
    val mapBaseStyle = remember(mapBaseStyleName) { MapBaseStyle.valueOf(mapBaseStyleName) }
    val effectiveMapStyle = if (mapDark) MapBaseStyle.DARK else mapBaseStyle
    val mapOnline = !mapPreferences.useOfflineMap
    val toastOnlineOnly = { Toast.makeText(context, "Disponível só no mapa online", Toast.LENGTH_SHORT).show() }

    // Estados do Radial Menu
    var radialMenuOpen by remember { mutableStateOf(false) }
    var radialSoundEnabled by remember { mutableStateOf(runState.voiceEnabled) }
    var radialVibrationEnabled by remember { mutableStateOf(runState.vibrationEnabled) }
    var radialGpsKeepAliveEnabled by remember { mutableStateOf(true) }

    // === ANIMAÇÕES FLUIDAS ===
    val shouldRunPulseAnim = runState.hasGpsSignal && runState.isGpsRequested && !runState.isPaused
    val shouldRunGpsPulse = !runState.hasGpsSignal && runState.isGpsRequested && !runState.isPaused

    val pulseAnimTarget = remember { mutableStateOf(0f) }
    val gpsPulseAnimTarget = remember { mutableStateOf(0f) }

    LaunchedEffect(shouldRunPulseAnim, shouldRunGpsPulse) {
        pulseAnimTarget.value = if (shouldRunPulseAnim) 1f else 0f
        gpsPulseAnimTarget.value = if (shouldRunGpsPulse) 1f else 0f
    }

    val pulseAnim = animateFloatAsState(
        targetValue = pulseAnimTarget.value,
        animationSpec = if (reducedMotion) tween(200) else infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAnim"
    )

    val cameraModeAnim = animateFloatAsState(
        targetValue = when (cameraMode) {
            is CameraMode.FollowUser -> 0f
            is CameraMode.FollowUserWithBearing -> 1f
            is CameraMode.ShowFullRoute -> 2f
            is CameraMode.Free -> 3f
        },
        animationSpec = if (reducedMotion) tween(200) else spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "cameraModeAnim"
    )

    val mapControlsAlpha by animateFloatAsState(
        targetValue = if (mapControlsVisible) 1f else 0f,
        animationSpec = if (reducedMotion) tween(150) else tween(300, easing = LinearEasing),
        label = "mapControlsAlpha"
    )

    val mapControlsSlide by animateFloatAsState(
        targetValue = if (mapControlsVisible) 0f else 40f,
        animationSpec = if (reducedMotion) tween(150) else tween(300, easing = LinearEasing),
        label = "mapControlsSlide"
    )

    val mapExpandAnim = animateFloatAsState(
        targetValue = if (mapExpanded) 1f else 0f,
        animationSpec = if (reducedMotion) tween(200) else spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "mapExpandAnim"
    )

    val gpsPulseAnim = animateFloatAsState(
        targetValue = gpsPulseAnimTarget.value,
        animationSpec = if (reducedMotion) tween(200) else infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gpsPulseAnim"
    )

    // Update pulse animation targets when conditions change
    LaunchedEffect(shouldRunPulseAnim, shouldRunGpsPulse) {
        pulseAnimTarget.value = if (shouldRunPulseAnim) 1f else 0f
        gpsPulseAnimTarget.value = if (shouldRunGpsPulse) 1f else 0f
    }

    // Sincroniza estados do radial menu
    LaunchedEffect(runState.voiceEnabled) { radialSoundEnabled = runState.voiceEnabled }
    LaunchedEffect(runState.vibrationEnabled) { radialVibrationEnabled = runState.vibrationEnabled }

    // =====================================================================
    // LAYOUT ESTRUTURADO IGUAL AO HTML REFERENCE:
    // .phone (full screen) > .map + .shade (background) > .layer (flex column)
    //   .layer > .top (header: chip + radial)
    //   .layer > .banner (battery warning)
    //   .layer > .hero (timer + mode + stats) - padding-top:14px
    //   .layer > .spacer (flex:1, position:relative) - follow pill + locate btn
    //   .layer > .actions (bottom bar: pause + stop)
    // =====================================================================
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_active_run")
    ) {
        // ============ MAPA EM TELA CHEIA (background) ============
        if (showMapDuringRun && runState.isGpsRequested) {
            MapViewComposable(
                points = runState.routePoints ?: emptyList(),
                currentLocation = runState.currentMapPoint,
                cameraMode = cameraMode,
                onCameraModeChange = onCameraModeChange,
                onRecenter = onRecenter,
                mapPreferences = mapPreferences,
                isOfflineMode = mapPreferences.useOfflineMap,
                showStreetNames = mapStreetNames,
                mapStyleUrl = effectiveMapStyle.url,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("map_view_realtime")
            )

            // Gradiente vertical Elite sobre o mapa (SPEC seção 7):
            // 0.00 → 0.96, 0.30 → 0.94, 0.43 → 0.90, 0.53 → 0.00, 0.76 → 0.00, 1.00 → 0.85
            // Cor base: Elite Background (#0A0B09). Desenhar acima do mapa, abaixo do conteúdo.
            // Não esticar para baixo do ponto de posição (ele apagaria o trajeto).
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = EliteTopScrimStops
                                .map { (fraction, alpha) -> fraction to Background.copy(alpha = alpha) }
                                .toTypedArray()
                        )
                    )
            )
        }

        // ============ LAYER PRINCIPAL (flex column como HTML .layer) ============
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOPO: Header (chip GPS esquerda + radial menu direita) ---
            // HTML: .top{height:64px;padding:16px 16px 0;display:flex;align-items:flex-start;justify-content:space-between}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(RunnaTheme.TopHeight)
                    .padding(
                        start = RunnaTheme.TopPadding,
                        top = RunnaTheme.TopPadding,
                        end = RunnaTheme.TopPadding,
                        bottom = 0.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // GPS Status Chip (esquerda) - HTML: .chip{margin-top:8px;height:32px}
                GpsStatusChip(
                    runState = runState,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .testTag("gps_chip"),
                )

                // O RadialMenu é desenhado como overlay na raiz (abaixo), para abrir por cima de tudo
                Spacer(modifier = Modifier.size(RunnaTheme.FabSize))
            }

            // --- Aviso de bateria (logo abaixo do header) ---
            // HTML: .banner{margin:4px 16px 0;padding:10px 12px;border-radius:14px}
            BatteryWarningBanner(
                show = runState.isGpsRequested,
                dismissBatteryWarning = dismissBatteryWarning,
                onDismissBanner = { batteryBannerDismissedInSession = true },
                onDismissForever = {
                    batteryBannerDismissedInSession = true
                    onDismissBatteryWarning()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = RunnaTheme.ActionBarPadding,
                        end = RunnaTheme.ActionBarPadding,
                        top = 4.dp
                    ),
                testTag = "battery_warning"
            )

            // --- HERO: Timer + Mode + Stats ---
            // HTML: .hero{padding-top:14px;display:flex;flex-direction:column;align-items:center}
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // padding-top: 14dp (como HTML .hero)
                Spacer(modifier = Modifier.height(14.dp))

                if (isFreeRun) {
                    FreeRunLayout(
                        runState = runState,
                        totalTimeFormatted = totalTimeFormatted,
                        progress = 0f,
                        testTag = "free_run_layout"
                    )
                } else {
                    // Layout para treino estruturado (compatibilidade)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = RunnaTheme.ActionBarPadding,
                                end = RunnaTheme.ActionBarPadding
                            )
                            .testTag("current_step_card"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                                    // Mode row with icon and label
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(stepIcon, contentDescription = null, tint = stepColor, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(RunnaTheme.ActionBarGap))
                            Text(
                                text = stepActionLabel,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = stepColor
                            )
                        }
                                    Spacer(modifier = Modifier.height(4.dp))
                        
                                    // Step info: "Etapa X de Y"
                        Text(
                                        text = "Etapa ${runState.currentStepIndex + 1} de ${steps.size}",
                                        style = MaterialTheme.typography.bodyMedium,
                            color = RunnaTheme.Muted,
                            textAlign = TextAlign.Center
                        )
                                    Spacer(modifier = Modifier.height(8.dp))
                        
                                    // Timer with TimerRing (progress arc) - Teal for active, Amber for paused
                                    TimerRing(
                                        timeFormatted = totalTimeFormatted,
                                        progress = if (steps.isNotEmpty()) {
                                            val totalDuration = steps.sumOf { it.durationSeconds }.toFloat()
                                            val elapsedFraction = if (totalDuration > 0) {
                                                runState.elapsedSeconds / totalDuration
                                            } else {
                                                0f
                                            }
                                            elapsedFraction.coerceIn(0f, 1f)
                                        } else {
                                            0f
                                        },
                                                                            progressColor = if (runState.isPaused) Amber else Olive,
                                                                                                                                                        textColor = if (runState.isPaused) Amber else Text,
                                        testTag = "timer_ring"
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                        
                                    // Stats row (distance + pace)
                                    StatsRow(
                                        distanceKm = "%.2f".format(runState.totalDistanceMeters / 1000.0),
                                        paceFormatted = runState.averagePaceFormatted,
                                        testTag = "stats_row_hero"
                                    )
                    }
                }
            }

            // --- SPACER (flex:1, position:relative) ---
            // HTML: .spacer{flex:1;position:relative} contém .follow e .locate
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (showMapDuringRun && runState.isGpsRequested) {

                    // "Seguindo você" pill - canto inferior esquerdo do spacer
                    // HTML: .follow{position:absolute;left:16px;bottom:14px;padding:7px 13px;border-radius:15px}
                    // O texto muda conforme o modo escolhido em Perfil > Modo de câmera padrão
                    // (Seguindo você / Seguindo com direção / Rota completa).
                    run {
                        Surface(
                            shape = RunnaTheme.FollowPillShape,
                            color = RunnaTheme.Glass,
                            border = BorderStroke(1.dp, RunnaTheme.Line),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 14.dp)
                                .testTag("map_pill_following")
                        ) {
                            Text(
                                text = cameraMode.pillLabel(),
                                fontSize = RunnaTheme.FollowTextSize,
                                color = RunnaTheme.Text,
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)
                            )
                        }
                    }

                    // O botão do canto inferior direito agora é o MapStyleFan, desenhado na raiz
                    // (abaixo), para o leque abrir por cima de tudo e receber toques.
                }
            }

            // --- BARRA DE AÇÕES INFERIOR (Pause/Stop) ---
            // HTML: .actions{display:grid;grid-template-columns:1fr 1fr;gap:12px;padding:16px 16px calc(18px+safe-area);border-radius:26px 26px 0 0;backdrop-filter:blur(14px)}
            if (showMapDuringRun && runState.isGpsRequested) {
                            Column(
                    modifier = Modifier.fillMaxWidth().onSizeChanged { bottomBlockHeightPx = it.height },
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if (runState.nextStep != null && !isFreeRun) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        shape = RoundedCornerShape(18.dp),
                                        border = BorderStroke(1.dp, RunnaTheme.Line)
                                    ) {
                                        Text(
                                            text = "A seguir: ${runState.nextStep.instruction} ${runState.nextStep.durationFormatted}",
                                            fontSize = 12.5.sp,
                                            color = RunnaTheme.Muted,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                                        )
                                    }
                                }

                                // ActionBar
                                ActionBar(
                                    isPaused = runState.isPaused,
                                    onPauseClick = { if (runState.isPaused) onResumeClick() else onPauseClick() },
                                    onStopClick = onFinishClick,
                                    modifier = Modifier.fillMaxWidth(),
                                    testTag = "action_bar"
                                )
                            }
            } else {
                // Layout sem mapa (compatibilidade)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = EliteSpacing.md,
                            end = EliteSpacing.md,
                            top = EliteSpacing.xl,
                            bottom = EliteSpacing.xl
                        ),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(LineStrong))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = EliteSpacing.lg),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBlock(label = "Tempo total", value = totalTimeFormatted, modifier = Modifier.weight(1f))
                        MetricBlock(label = "Distância", value = "$distanceKm km", modifier = Modifier.weight(1f))
                        MetricBlock(label = "Ritmo (min/km)", value = runState.averagePaceFormatted, modifier = Modifier.weight(1f))
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(LineStrong))

                    Spacer(modifier = Modifier.height(EliteSpacing.xl))

                    if (runState.nextStep != null && !isFreeRun) {
                        Card(
                            border = BorderStroke(1.dp, LineStrong),
                            modifier = Modifier.fillMaxWidth().testTag("next_step_card")
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        start = EliteSpacing.md,
                                        end = EliteSpacing.md,
                                        top = EliteSpacing.md,
                                        bottom = EliteSpacing.md
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "A seguir", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = Olive)
                                Spacer(modifier = Modifier.width(EliteSpacing.md))
                                Text(
                                    text = "${runState.nextStep.instruction} • ${runState.nextStep.durationFormatted}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = TextColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    if (runState.isPaused) {
                        Spacer(modifier = Modifier.height(EliteSpacing.lg))
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Surface(
                                shape = CircleShape,
                                                    color = Amber.copy(alpha = 0.2f),
                                                    border = BorderStroke(1.dp, Amber)
                            ) {
                                Text(
                                    text = "Treino em pausa",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                                        color = Amber,
                                    modifier = Modifier.padding(horizontal = EliteSpacing.md, vertical = EliteSpacing.xs)
                                )
                            }
                        }
                                            Spacer(modifier = Modifier.height(EliteSpacing.md))
                                            Text(
                                                text = "Solte p/ cancelar",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                                                                color = Amber,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                    }

                    Spacer(modifier = Modifier.height(EliteSpacing.xl))
                    HoldToFinishGradientButton(
                        onFinish = onFinishClick,
                        modifier = Modifier.width(200.dp)
                    )
                    Spacer(modifier = Modifier.height(EliteSpacing.xl))
                }
            }
        }

        // ============ OVERLAY: fechar menu ao tocar fora + RadialMenu ============
        if (radialOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { radialOpen = false }
            )
        }
        // ============ OVERLAY: leque de opções do mapa (canto inferior direito) ============
        if (showMapDuringRun && runState.isGpsRequested) {
            if (mapFanOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { mapFanOpen = false }
                )
            }
            MapStyleFan(
                open = mapFanOpen,
                onOpenChange = { mapFanOpen = it; if (it) radialOpen = false },
                darkEnabled = mapDark,
                streetNamesEnabled = mapStreetNames,
                styleSwitchEnabled = mapOnline,
                onRecenter = {
                    // Volta ao modo escolhido no Perfil (não força "seguir você")
                    onCameraModeChange(mapPreferences.toCameraMode())
                    onRecenter()
                    mapFanOpen = false
                },
                onDarkToggle = { if (mapOnline) mapDark = !mapDark else toastOnlineOnly() },
                onCycleBaseStyle = {
                    if (mapOnline) {
                        val next = mapBaseStyle.nextSelectable()
                        mapBaseStyleName = next.name
                        mapDark = false
                        Toast.makeText(context, "Mapa: ${next.label}", Toast.LENGTH_SHORT).show()
                    } else toastOnlineOnly()
                },
                onStreetNamesToggle = { mapStreetNames = !mapStreetNames },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = with(LocalDensity.current) { bottomBlockHeightPx.toDp() } + 10.dp)
            )
        }

        RadialMenu(
            soundEnabled = radialSoundEnabled,
            vibrationEnabled = radialVibrationEnabled,
            gpsKeepAliveEnabled = radialGpsKeepAliveEnabled,
            onSoundToggle = { enabled -> radialSoundEnabled = enabled; onToggleVoice() },
            onVibrationToggle = { enabled -> radialVibrationEnabled = enabled; onToggleVibration() },
            onGpsKeepAliveToggle = { enabled -> radialGpsKeepAliveEnabled = enabled },
            open = radialOpen,
            onOpenChange = { radialOpen = it; if (it) mapFanOpen = false },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = RunnaTheme.TopPadding, end = RunnaTheme.TopPadding)
        )
    }
}

// ============================================================
// COMPONENTES AUXILIARES (mantidos do arquivo original)
// ============================================================

/**
 * Botão de controle do mapa (pequeno, redondo, 40dp)
 */
@Composable
fun MapControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    expanded: Boolean = false,
    active: Boolean = false,
    desc: String,
    testTag: String,
    color: androidx.compose.ui.graphics.Color = Olive
) {
    val scaleAnim = animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "mapControlScale"
    )

    Surface(
        shape = CircleShape,
        color = if (active) color else RunnaGlass,
        border = BorderStroke(1.dp, if (active) color.copy(alpha = 0.3f) else RunnaGlassBorder),
        modifier = Modifier
            .size(40.dp)
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .semantics { contentDescription = desc }
            .testTag(testTag)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ -> onClick() }
                )
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (active) Color.White else color,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Botão de toggle (voz/vibração) com animação suave
 */
@Composable
fun ToggleButton(
    enabled: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    activeColor: androidx.compose.ui.graphics.Color,
    inactiveColor: androidx.compose.ui.graphics.Color
) {
    val pressed = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scaleAnim = animateFloatAsState(
        targetValue = if (pressed.value) 0.95f else 1f,
        animationSpec = if (pressed.value) {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        },
        label = "toggleScale"
    )

    Surface(
        shape = CircleShape,
        color = if (enabled) activeColor else RunnaGlass,
        border = BorderStroke(1.dp, if (enabled) activeColor.copy(alpha = 0.3f) else RunnaGlassBorder),
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
    ) {
        IconButton(
            onClick = {
                pressed.value = true
                onClick()
                scope.launch {
                    delay(100)
                    pressed.value = false
                }
            },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = if (enabled) "Ligado" else "Desligado",
                tint = if (enabled) Color.White else inactiveColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Botão animado de ação (Pausar/Retomar) com feedback visual simples
 */
@Composable
fun AnimatedActionButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pulseAnim: androidx.compose.runtime.State<Float>,
    isPrimary: Boolean
) {
    val label = if (isPlaying) "Pausar" else "Retomar"
    val color = if (isPlaying) Olive else Red

    val pressed = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scaleAnim = animateFloatAsState(
        targetValue = if (pressed.value) 0.95f else 1f,
        animationSpec = if (pressed.value) {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        },
        label = "buttonScale"
    )

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = color,
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .testTag(if (isPlaying) "btn_pause" else "btn_resume")
    ) {
        androidx.compose.material3.TextButton(
            onClick = {
                pressed.value = true
                onClick()
                scope.launch {
                    delay(100)
                    pressed.value = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = RunnaTheme.ActionBarPadding,
                        end = RunnaTheme.ActionBarPadding
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = isPlaying,
                    enter = fadeIn(animationSpec = tween(120, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(120, easing = LinearEasing)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                AnimatedVisibility(
                    visible = !isPlaying,
                    enter = fadeIn(animationSpec = tween(120, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(120, easing = LinearEasing)),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Botão Parar/Finalizar - estilo destrutivo à direita com hold-to-confirm (1.5s)
 */
@Composable
fun StopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pressed = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scaleAnim = animateFloatAsState(
        targetValue = if (pressed.value) 0.95f else 1f,
        animationSpec = if (pressed.value) {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        },
        label = "stopButtonScale"
    )

    var isHolding by remember { mutableStateOf(false) }
    val holdProgress = remember { Animatable(0f) }

    LaunchedEffect(isHolding) {
        if (isHolding) {
            holdProgress.snapTo(0f)
            val result = holdProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
            if (result.endReason == AnimationEndReason.Finished && holdProgress.value >= 1f) {
                isHolding = false
                onClick()
            }
        } else {
            holdProgress.snapTo(0f)
        }
    }

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Red,
        border = BorderStroke(1.dp, Red.copy(alpha = 0.3f)),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .testTag("btn_stop")
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    isHolding = true
                    val upOrCancel = waitForUpOrCancellation()
                    isHolding = false
                    if (upOrCancel == null && holdProgress.value < 1f) {
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isHolding) {
                CircularProgressIndicator(
                    progress = { holdProgress.value },
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }

            androidx.compose.material3.TextButton(
                onClick = {},
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = RunnaTheme.ActionBarPadding,
                            end = RunnaTheme.ActionBarPadding
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHolding) "Solte p/ cancelar" else "Parar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Botão pílula com gradiente da marca exigindo segurar 1,5s para finalizar o treino.
 */
@Composable
fun HoldToFinishGradientButton(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHolding by remember { mutableStateOf(false) }
    var showTapHint by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isHolding) {
        if (isHolding) {
            progress.snapTo(0f)
            val result = progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
            if (result.endReason == AnimationEndReason.Finished && progress.value >= 1f) {
                isHolding = false
                onFinish()
            }
        } else {
            progress.snapTo(0f)
        }
    }

    LaunchedEffect(showTapHint) {
        if (showTapHint) {
            delay(2500)
            showTapHint = false
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showTapHint) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = RunnaGlass,
                border = BorderStroke(1.dp, RunnaGlassBorder),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "Segure 1,5s para finalizar",
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(CircleShape)
                .background(Olive)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown()
                        isHolding = true
                        val upOrCancel = waitForUpOrCancellation()
                        isHolding = false
                        if (upOrCancel == null && progress.value < 1f) {
                            showTapHint = true
                        }
                    }
                }
                .testTag("btn_finish_run_hold")
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isHolding) {
                    CircularProgressIndicator(
                        progress = { progress.value },
                        color = Color.White,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val completionScale = animateFloatAsState(
                        targetValue = if (isHolding && progress.value >= 1f) 1.2f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                        label = "completionScale"
                    )
                    AnimatedVisibility(
                        visible = !isHolding,
                        enter = fadeIn(animationSpec = tween(120, easing = LinearEasing)),
                        exit = fadeOut(animationSpec = tween(120, easing = LinearEasing)),
                    ) {
                        Text(
                            text = "Finalizar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .graphicsLayer { scaleX = completionScale.value; scaleY = completionScale.value }
                        )
                    }
                    AnimatedVisibility(
                        visible = isHolding,
                        enter = fadeIn(animationSpec = tween(120, easing = LinearEasing)),
                        exit = fadeOut(animationSpec = tween(120, easing = LinearEasing)),
                    ) {
                        Text(
                            text = "Solte p/ cancelar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .graphicsLayer { scaleX = completionScale.value; scaleY = completionScale.value }
                        )
                    }
                }
            }
        }
    }
                    }
