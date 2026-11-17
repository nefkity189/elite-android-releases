package com.example.ui.screens

import com.example.ui.components.AisleDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StepType
import com.example.model.WorkoutStep
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.ui.components.stepTypeColor
import com.example.ui.theme.AisleTrack
import com.example.service.RunState
import com.example.ui.components.AisleCard
import com.example.ui.components.FreeRunLayout
import com.example.ui.components.MapViewComposable
import com.example.ui.components.MetricBlock
import com.example.ui.components.PaceChart
import com.example.ui.components.rememberAisleWindowSizeClass
import com.example.ui.components.rememberTimerFontScale
import com.example.ui.state.CameraMode
import com.example.ui.state.MapPoint
import com.example.ui.state.MapPreferences
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleAlert
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
import com.example.ui.theme.rememberReducedMotion
import kotlinx.coroutines.delay

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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val windowSizeClass = rememberAisleWindowSizeClass()
    val timerFontScale = rememberTimerFontScale(windowSizeClass)

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
        AisleDialog(
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
            confirmTestTag = "confirm_finish_run_button"
        )
    }

    val isStepRun = runState.currentStep?.type == StepType.RUN
    val stepColor = if (isStepRun) AisleTurquoise else AisleTextAccent2
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

    // Indicador de sinal do GPS
    val (gpsSignalText, gpsSignalColor, gpsSignalIcon) = when {
        !runState.isGpsRequested -> Triple("Sem GPS", AisleTextSecondary, Icons.Default.GpsOff)
        !isDeviceGpsEnabled -> Triple("GPS Desligado", AisleAlert, Icons.Default.GpsOff)
        !runState.hasGpsSignal || runState.gpsAccuracy == null -> Triple("Sem sinal", AisleTextSecondary, Icons.Default.GpsNotFixed)
        runState.gpsAccuracy > 30f -> Triple("Sinal fraco", AisleActionOrange, Icons.Default.GpsNotFixed)
        else -> Triple("Sinal bom", StatusCompleted, Icons.Default.GpsFixed)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_active_run")
    ) {
        // === LAYOUT PRINCIPAL: Column que preenche a tela ===
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // ============ TOPO: GPS, Toggles, Avisos, Etapa Atual ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AisleSpacing.xl)
            ) {
                // Barra Superior: Status do GPS e Controles de Áudio/Vibração
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Indicador de Sinal do GPS
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = gpsSignalColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, gpsSignalColor.copy(alpha = 0.3f)),
                        modifier = Modifier.testTag("gps_signal_indicator")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = gpsSignalIcon,
                                contentDescription = null,
                                tint = gpsSignalColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = gpsSignalText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = gpsSignalColor
                            )
                        }
                    }

                    // Toggles de voz e vibração
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = AisleCard,
                            border = BorderStroke(1.dp, AisleBorder),
                            modifier = Modifier.size(48.dp)
                        ) {
                            IconButton(onClick = onToggleVoice) {
                                Icon(
                                    imageVector = if (runState.voiceEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                                    contentDescription = if (runState.voiceEnabled) "Voz ligada" else "Voz desligada",
                                    tint = if (runState.voiceEnabled) AisleTurquoise else AisleTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = AisleCard,
                            border = BorderStroke(1.dp, AisleBorder),
                            modifier = Modifier.size(48.dp)
                        ) {
                            IconButton(onClick = onToggleVibration) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = if (runState.vibrationEnabled) "Vibração ligada" else "Vibração desligada",
                                    tint = if (runState.vibrationEnabled) AisleTurquoise else AisleTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Avisos (GPS desligado no aparelho ou TTS indisponível)
                if (runState.isGpsRequested && !isDeviceGpsEnabled) {
                    Spacer(modifier = Modifier.height(AisleSpacing.md))
                    AisleCard(border = BorderStroke(1.dp, AisleAlert)) {
                        Row(
                            modifier = Modifier.padding(AisleSpacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = AisleAlert)
                            Spacer(modifier = Modifier.width(AisleSpacing.sm))
                            Text(
                                text = "O GPS do seu celular está desativado. Ative a localização nas configurações do aparelho para registrar distância.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AisleTextPrimary
                            )
                        }
                    }
                }

                if (!runState.ttsAvailable && runState.ttsWarning != null) {
                    Spacer(modifier = Modifier.height(AisleSpacing.xs))
                    AisleCard {
                        Text(
                            text = runState.ttsWarning,
                            style = MaterialTheme.typography.bodySmall,
                            color = AisleTextSecondary,
                            modifier = Modifier.padding(AisleSpacing.md)
                        )
                    }
                }

                // Aviso discreto de economia de bateria
                if (showBatteryWarning) {
                    Spacer(modifier = Modifier.height(AisleSpacing.md))
                    AisleCard(
                        border = BorderStroke(1.dp, AisleBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        context.startActivity(intent)
                                    }
                                } catch (_: Exception) {}
                            }
                            .testTag("battery_warning_banner")
                    ) {
                        Column(modifier = Modifier.padding(AisleSpacing.md)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.BatteryAlert,
                                    contentDescription = null,
                                    tint = AisleActionOrange,
                                    modifier = Modifier.size(18.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(AisleSpacing.sm))
                                Text(
                                    text = "Seu celular pode pausar o GPS com a tela apagada. Toque para ajustar.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AisleTextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { batteryBannerDismissedInSession = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Fechar aviso",
                                        tint = AisleTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(AisleSpacing.xxs))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        batteryBannerDismissedInSession = true
                                        onDismissBatteryWarning()
                                    },
                                    modifier = Modifier.testTag("btn_dismiss_battery_forever")
                                ) {
                                    Text(
                                        text = "Não mostrar de novo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AisleTurquoise
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AisleSpacing.xl))

                // ETAPA ATUAL: nome, cronômetro grande e instrução
                if (isFreeRun) {
                    FreeRunLayout(
                        runState = runState,
                        totalTimeFormatted = totalTimeFormatted
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("current_step_card"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(stepIcon, contentDescription = null, tint = stepColor, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(AisleSpacing.md))
                            Text(
                                text = stepActionLabel,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = stepColor
                            )
                        }
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        Text(
                            text = stepTimeFormatted,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFeatureSettings = "tnum",
                                fontSize = (56 * timerFontScale).sp
                            ),
                            color = AisleTextPrimary,
                            modifier = Modifier.testTag("step_countdown_timer")
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        Text(
                            text = runState.currentStep?.instruction ?: "Aguardando início...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AisleTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AisleSpacing.lg))

                // BARRA SEGMENTADA COM TODOS OS BLOCOS DO TREINO (apenas sem mapa)
                if (!showMapDuringRun || !runState.isGpsRequested) {
                    if (steps.isNotEmpty() && !isFreeRun) {
                        RunStepsBar(steps = steps, currentIndex = runState.currentStepIndex)
                    }
                }
            }

            // ============ MAPA: ocupa o espaço restante (flex 1) ============
            if (showMapDuringRun && runState.isGpsRequested) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)  // ocupa todo espaço vertical restante
                ) {
                    // Mapa real (AndroidView) - preenche todo o Box
                    MapViewComposable(
                        points = runState.routePoints ?: emptyList(),
                        currentLocation = runState.currentMapPoint,
                        cameraMode = cameraMode,
                        onCameraModeChange = onCameraModeChange,
                        onRecenter = onRecenter,
                        mapPreferences = mapPreferences,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("map_view_realtime")
                    )

                    // === OVERLAY NO MAPA (acima do AndroidView) ===
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Lado DIREITO do mapa: controles verticais pequenos
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.md),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Top
                        ) {
                            // Toggle expandir/colapsar mapa
                            IconButton(
                                onClick = { mapExpanded = !mapExpanded },
                                modifier = Modifier
                                    .size(40.dp)
                                    .semantics { contentDescription = if (mapExpanded) "Colapsar mapa" else "Expandir mapa" }
                                    .testTag("btn_toggle_map_expand")
                            ) {
                                Icon(
                                    imageVector = if (mapExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = AisleTurquoise,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Toggle modo de câmera
                            IconButton(
                                onClick = {
                                    val nextMode = when (cameraMode) {
                                        is CameraMode.FollowUser -> CameraMode.ShowFullRoute
                                        is CameraMode.FollowUserWithBearing -> CameraMode.ShowFullRoute
                                        is CameraMode.ShowFullRoute -> CameraMode.Free
                                        is CameraMode.Free -> CameraMode.FollowUserWithBearing
                                    }
                                    onCameraModeChange(nextMode)
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .semantics { contentDescription = "Alternar modo de câmera" }
                                    .testTag("btn_map_camera_mode")
                            ) {
                                Icon(
                                    imageVector = when (cameraMode) {
                                        is CameraMode.FollowUser -> Icons.Default.MyLocation
                                        is CameraMode.FollowUserWithBearing -> Icons.Default.MyLocation
                                        is CameraMode.ShowFullRoute -> Icons.Default.Explore
                                        is CameraMode.Free -> Icons.Default.ZoomOutMap
                                    },
                                    contentDescription = null,
                                    tint = AisleTurquoise,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Botão recenter (apenas modo Free)
                            if (cameraMode is CameraMode.Free) {
                                Spacer(modifier = Modifier.height(8.dp))
                                IconButton(
                                    onClick = onRecenter,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .semantics { contentDescription = "Recentralizar no usuário" }
                                        .testTag("btn_map_recenter")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MyLocation,
                                        contentDescription = null,
                                        tint = AisleActionOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Legenda do modo de câmera
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.7f)
                                )
                            ) {
                                Text(
                                    text = when (cameraMode) {
                                        is CameraMode.FollowUser -> "Seguindo você"
                                        is CameraMode.FollowUserWithBearing -> "Seguindo com direção"
                                        is CameraMode.ShowFullRoute -> "Rota completa"
                                        is CameraMode.Free -> "Livre (toque para recenter)"
                                    },
                                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // Centro inferior: Botões Pause/Resume (esq) e Parar/Finalizar (dir)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.lg),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Indicador de etapa (paginador) - acima dos botões
                            if (steps.isNotEmpty() && !isFreeRun) {
                                val totalMetrics = 3 + 1 + 1 + (if (runState.nextStep != null) 1 else 0)
                                val currentPage = 0
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    repeat(totalMetrics) { i ->
                                        Box(
                                            modifier = Modifier
                                                .size(if (i == currentPage) 8.dp else 4.dp)
                                                .clip(CircleShape)
                                                .background(if (i == currentPage) AisleTurquoise else AisleTextSecondary.copy(alpha = 0.5f))
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.md))
                            }

                            // Botões lado a lado: Pausar/Retomar (esq) | Parar/Finalizar (dir)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Pausar/Retomar - botão principal (esquerda)
                                ExpandedButton(
                                    isPlaying = !runState.isPaused,
                                    onClick = { if (runState.isPaused) onResumeClick() else onPauseClick() },
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                // Parar/Finalizar - botão destrutivo (direita)
                                StopButton(
                                    onClick = onFinishClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // ============ RODAPÉ SEM MAPA: métricas + botão finalizar ============
            if (!showMapDuringRun || !runState.isGpsRequested) {
                Spacer(modifier = Modifier.height(AisleSpacing.xxl))

                // MÉTRICAS entre dois divisores
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AisleBorder))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = AisleSpacing.lg),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBlock(label = "Tempo total", value = totalTimeFormatted, modifier = Modifier.weight(1f))
                    MetricBlock(label = "Distância", value = "$distanceKm km", modifier = Modifier.weight(1f))
                    MetricBlock(label = "Ritmo (min/km)", value = runState.averagePaceFormatted, modifier = Modifier.weight(1f))
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AisleBorder))

                Spacer(modifier = Modifier.height(AisleSpacing.xl))

                // GRÁFICO DE RITMO
                AisleCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(AisleSpacing.md)) {
                        Text(text = "Evolução do ritmo", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = AisleTextSecondary)
                        Spacer(modifier = Modifier.height(AisleSpacing.md))
                        PaceChart(currentPaceSeconds = runState.averagePaceSeconds, targetPaceSeconds = if (!isFreeRun) 360.0 else null, chartHeight = if (isFreeRun) 90.dp else 120.dp)
                    }
                }

                Spacer(modifier = Modifier.height(AisleSpacing.lg))

                // CARTÃO "A SEGUIR"
                if (runState.nextStep != null && !isFreeRun) {
                    AisleCard(
                        border = BorderStroke(1.dp, AisleBorder),
                        modifier = Modifier.fillMaxWidth().testTag("next_step_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "A seguir",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AisleTurquoise
                            )
                            Spacer(modifier = Modifier.width(AisleSpacing.md))
                            Text(
                                text = "${runState.nextStep.instruction} • ${runState.nextStep.durationFormatted}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = AisleTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (runState.isPaused) {
                    Spacer(modifier = Modifier.height(AisleSpacing.lg))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Surface(
                            shape = CircleShape,
                            color = AisleActionOrange.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, AisleActionOrange)
                        ) {
                            Text(
                                text = "Treino em pausa",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AisleActionOrange,
                                modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.xs)
                            )
                        }
                    }
                }

                // Botão Finalizar
                Spacer(modifier = Modifier.height(AisleSpacing.xl))
                HoldToFinishGradientButton(
                    onFinish = onFinishClick,
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.height(AisleSpacing.xl))
            }
        }
    }
}

/**
 * Botão Expandido (Pausar/Retomar) - estilo primário à esquerda
 */
@Composable
fun ExpandedButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
    val label = if (isPlaying) "Pausar" else "Retomar"
    val color = if (isPlaying) AisleTurquoise else AisleActionOrange

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = color,
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .testTag(if (isPlaying) "btn_pause" else "btn_resume")
    ) {
        androidx.compose.material3.TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AisleSpacing.md),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
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
 * Botão Parar/Finalizar - estilo destrutivo à direita
 */
@Composable
fun StopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = AisleActionOrange,
        border = BorderStroke(1.dp, AisleActionOrange.copy(alpha = 0.3f)),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .testTag("btn_stop")
    ) {
        androidx.compose.material3.TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AisleSpacing.md),
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
                    text = "Parar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Botão pílula com gradiente da marca exigindo segurar 1,5s para finalizar o treino.
 * (Mantido para layout sem mapa)
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
                color = AisleCard,
                border = BorderStroke(1.dp, AisleBorder),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Text(
                    text = "Segure 1,5s para finalizar",
                    style = MaterialTheme.typography.labelSmall,
                    color = AisleTurquoise,
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
                .background(BrandGradient)
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
                    Text(
                        text = if (isHolding) "Solte p/ cancelar" else "Finalizar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Barra segmentada com todos os blocos do treino: o bloco atual fica em destaque,
 * os anteriores esmaecidos e os próximos na cor de trilha. Largura proporcional à duração.
 */
@Composable
fun RunStepsBar(
    steps: List<WorkoutStep>,
    currentIndex: Int,
    modifier: Modifier = Modifier
) {
    if (steps.isEmpty()) return
    val safeIndex = currentIndex.coerceIn(0, steps.size - 1)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Etapa ${safeIndex + 1} de ${steps.size}" },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val base = stepTypeColor(step.type)
            val color = when {
                index == safeIndex -> base
                index < safeIndex -> base.copy(alpha = 0.45f)
                else -> AisleTrack
            }
            Box(
                modifier = Modifier
                    .weight(step.durationSeconds.coerceAtLeast(1).toFloat())
                    .height(if (index == safeIndex) 12.dp else 8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color)
            )
        }
    }
}