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
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Warning
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
import com.example.ui.components.PaceChart
import com.example.ui.components.MetricBlock
import com.example.ui.components.rememberAisleWindowSizeClass
import com.example.ui.components.rememberTimerFontScale
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
            .padding(AisleSpacing.xl)
            .testTag("screen_active_run")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
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

                // ETAPA ATUAL: nome, cronômetro grande e instrução (sem moldura de cartão)
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

                // BARRA SEGMENTADA COM TODOS OS BLOCOS DO TREINO (o atual em destaque)
                if (steps.isNotEmpty() && !isFreeRun) {
                    Spacer(modifier = Modifier.height(AisleSpacing.xl))
                    RunStepsBar(steps = steps, currentIndex = runState.currentStepIndex)
                }

                Spacer(modifier = Modifier.height(AisleSpacing.xxl))

                // MÉTRICAS entre dois divisores: tempo total, distância e ritmo
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AisleBorder))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = AisleSpacing.lg),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricBlock(
                        label = "Tempo total",
                        value = totalTimeFormatted,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBlock(
                        label = "Distância",
                        value = "$distanceKm km",
                        modifier = Modifier.weight(1f)
                    )
                    MetricBlock(
                        label = "Ritmo (min/km)",
                        value = runState.averagePaceFormatted,
                        modifier = Modifier.weight(1f)
                    )
                }
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AisleBorder))

                Spacer(modifier = Modifier.height(AisleSpacing.xl))

                // GRÁFICO DE RITMO EM TEMPO REAL
                AisleCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(AisleSpacing.md)) {
                        Text(
                            text = "Evolução do ritmo",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = AisleTextSecondary
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.md))
                        PaceChart(
                            currentPaceSeconds = runState.averagePaceSeconds,
                            targetPaceSeconds = if (!isFreeRun) 360.0 else null,
                            chartHeight = if (isFreeRun) 90.dp else 120.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AisleSpacing.lg))

                // CARTÃO "A SEGUIR" COM O PRÓXIMO PASSO E A DURAÇÃO
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
            }

            Spacer(modifier = Modifier.height(AisleSpacing.md))

            // BOTÕES DE AÇÃO: Pausar/Retomar em pílula secundária; Finalizar com gradiente da marca (1,5s)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AisleSpacing.xs),
                horizontalArrangement = Arrangement.spacedBy(AisleSpacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (runState.isPaused) {
                    // Pílula secundária: Retomar
                    val reducedMotion = rememberReducedMotion()
                    var resumeRotation by remember { mutableStateOf(0f) }
                    LaunchedEffect(runState.isPaused) {
                        if (runState.isPaused && !reducedMotion) {
                            resumeRotation += 360f
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = AisleCard,
                        border = BorderStroke(1.5.dp, AisleTurquoise),
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .clip(CircleShape)
                            .clickable { onResumeClick() }
                            .testTag("btn_resume_run")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val animatedRotation by animateFloatAsState(
                                targetValue = resumeRotation,
                                animationSpec = if (rememberReducedMotion()) spring(dampingRatio = 1f) else spring(stiffness = 1000f, dampingRatio = 1f),
                                label = "resume_icon_rotation"
                            )
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = AisleTurquoise,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(animatedRotation)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retomar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AisleTurquoise)
                        }
                    }
                } else {
                    // Pílula secundária: Pausar
                    val reducedMotion = rememberReducedMotion()
                    var pauseRotation by remember { mutableStateOf(0f) }
                    LaunchedEffect(runState.isPaused) {
                        if (!runState.isPaused && !reducedMotion) {
                            pauseRotation += 360f
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = AisleCard,
                        border = BorderStroke(1.dp, AisleBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .clip(CircleShape)
                            .clickable { onPauseClick() }
                            .testTag("btn_pause_run")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val animatedRotation by animateFloatAsState(
                                targetValue = pauseRotation,
                                animationSpec = if (rememberReducedMotion()) spring(dampingRatio = 1f) else spring(stiffness = 1000f, dampingRatio = 1f),
                                label = "pause_icon_rotation"
                            )
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = null,
                                tint = AisleTextSecondary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(animatedRotation)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pausar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AisleTextSecondary)
                        }
                    }
                }

                // Botão de finalizar com o gradiente da marca (segurar 1,5s)
                HoldToFinishGradientButton(
                    onFinish = onFinishClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Botão pílula com gradiente da marca exigindo segurar 1,5s para finalizar o treino.
 */
@Composable
private fun HoldToFinishGradientButton(
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
                // Progresso animado da pressão circular / barra
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
private fun RunStepsBar(
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
