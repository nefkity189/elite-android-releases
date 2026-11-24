package com.elite.sch.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.RunnaTheme
import com.elite.sch.app.ui.theme.rememberReducedMotion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI

@Composable
fun ActionBar(
    isPaused: Boolean,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "action_bar"
) {
    val reducedMotion = rememberReducedMotion()
    
    // Pause button state
    var pausePressed by remember { mutableStateOf(false) }
    
    // Stop button hold state
    var stopHoldProgress by remember { mutableStateOf(0f) }
    var stopHoldJob by remember { mutableStateOf<Job?>(null) }
    
    val pauseScale = animateFloatAsState(
        targetValue = if (pausePressed) 0.96f else 1f,
        animationSpec = if (reducedMotion) tween(150) else spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "pauseScale"
    ).value
    
    val stopHoldAnim = animateFloatAsState(
        targetValue = stopHoldProgress,
        animationSpec = if (reducedMotion) tween(0) else tween(1500),
        label = "stopHoldAnim"
    ).value
    
    val pauseIcon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause
    val pauseContentDesc = if (isPaused) "Retomar" else "Pausar"
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = RunnaTheme.ActionBarPadding,
                end = RunnaTheme.ActionBarPadding,
                top = 16.dp,
                bottom = 18.dp
            )
            .testTag(testTag),
        shape = RoundedCornerShape(topStart = RunnaTheme.ActionBarRadius, topEnd = RunnaTheme.ActionBarRadius),
        color = RunnaTheme.Glass,
        border = BorderStroke(1.dp, RunnaTheme.Line)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pause button
            Box(
                modifier = Modifier
                    .size(RunnaTheme.ActionBarButtonSize)
                    .graphicsLayer {
                        scaleX = pauseScale
                        scaleY = pauseScale
                    }
                    .clip(CircleShape)
                    .background(RunnaTheme.Teal)
                    .clickable(
                        onClick = onPauseClick,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) {
                        pausePressed = true
                    }
                    .pointerInput(Unit) {
                        androidx.compose.foundation.gestures.detectTapGestures(
                            onPress = { pausePressed = true },
                            onTap = { pausePressed = false },
                            onCancel = { pausePressed = false }
                        )
                    }
                    .semantics {
                        contentDescription = pauseContentDesc
                    }
                    .testTag("btn_pause"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = pauseIcon,
                    contentDescription = null,
                    tint = RunnaTheme.TealOn,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            // Stop button with hold-to-confirm
            Box(
                modifier = Modifier
                    .size(RunnaTheme.ActionBarButtonSize)
                    .clip(CircleShape)
                    .background(RunnaTheme.Red)
                    .pointerInput(Unit) {
                        androidx.compose.foundation.gestures.detectTapGestures(
                            onPress = {
                                stopHoldJob?.cancel()
                                stopHoldJob = CoroutineScope(Dispatchers.Main).launch {
                                    var elapsed = 0L
                                    while (elapsed < 1500) {
                                        kotlinx.coroutines.delay(16)
                                        elapsed += 16
                                        stopHoldProgress = (elapsed / 1500f).coerceIn(0f, 1f)
                                    }
                                    if (stopHoldProgress >= 1f) {
                                        onStopClick()
                                    }
                                    stopHoldProgress = 0f
                                }
                            },
                            onCancel = {
                                stopHoldJob?.cancel()
                                stopHoldProgress = 0f
                            }
                        )
                    }
                    .semantics {
                        contentDescription = "Parar"
                    }
                    .testTag("btn_stop"),
                contentAlignment = Alignment.Center
            ) {
                // Stop icon
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    tint = RunnaTheme.RedOn,
                    modifier = Modifier.size(18.dp)
                )
                
                // Hold progress ring
                if (stopHoldProgress > 0f) {
                    Box(
                        modifier = Modifier
                            .size(RunnaTheme.ActionBarButtonSize)
                            .graphicsLayer {
                                rotate(-90f)
                            }
                    ) {
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier.size(RunnaTheme.ActionBarButtonSize)
                        ) {
                            val strokeWidth = 3.dp
                            val radius = (RunnaTheme.ActionBarButtonSize.value - strokeWidth.value) / 2
                            val sweep = stopHoldProgress * 360f
                            
                            drawArc(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    listOf(RunnaTheme.RedOn, RunnaTheme.RedOn)
                                ),
                                startAngle = 0f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = androidx.compose.ui.geometry.Offset(strokeWidth.value / 2, strokeWidth.value / 2),
                                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                                style = androidx.compose.ui.graphics.Stroke(width = strokeWidth.value, cap = androidx.compose.ui.graphics.Cap.Round)
                            )
                        }
                    }
                }
            }
        }
    }
}