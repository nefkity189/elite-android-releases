package com.elite.sch.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.RunnaTextStyles
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion

/**
 * Barra de ações inferior: largura total, cantos superiores 26dp, padding 16 (topo/lados) e 18 embaixo.
 * Duas colunas iguais (Pausar/Retomar + Parar) com 12dp de espaço.
 * Parar tem hold-to-confirm (1.5s) para evitar toques acidentais.
 */
@Composable
fun ActionBar(
    isPaused: Boolean = false,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "action_bar"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RunnaTheme.ActionBarShape)
            .background(RunnaTheme.Glass)
            .border(BorderStroke(1.dp, RunnaTheme.Line), RunnaTheme.ActionBarShape)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = RunnaTheme.ActionBarPadding,
                    end = RunnaTheme.ActionBarPadding,
                    top = RunnaTheme.ActionBarPadding,
                    bottom = RunnaTheme.ActionBarBottomPad
                ),
            horizontalArrangement = Arrangement.spacedBy(RunnaTheme.ActionBarGap),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pausar/Retomar - click normal
            ActionButton(
                label = if (isPaused) "Retomar" else "Pausar",
                icon = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                bg = RunnaTheme.Teal,
                fg = RunnaTheme.TealOn,
                onClick = onPauseClick,
                testTag = if (isPaused) "btn_resume" else "btn_pause"
            )
            // Parar - hold to confirm (1.5s)
            StopButton(
                onClick = onStopClick,
                testTag = "btn_stop"
            )
        }
    }
}

@Composable
private fun RowScope.ActionButton(
    label: String,
    icon: ImageVector,
    bg: Color,
    fg: Color,
    onClick: () -> Unit,
    testTag: String
) {
    val source = remember { MutableInteractionSource() }
    val pressed = source.collectIsPressedAsState().value
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "btnScale"
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .height(RunnaTheme.ButtonHeight)
            .graphicsLayer { scaleX = scale.value; scaleY = scale.value }
            .clip(RunnaTheme.ButtonShape)
            .background(bg)
            .clickable(interactionSource = source, indication = null, onClick = onClick)
            .semantics { contentDescription = label }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(RunnaTheme.ButtonIconSize))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = fg, style = RunnaTextStyles.Button)
        }
    }
}

/**
 * Botão Parar/Finalizar - estilo destrutivo à direita com hold-to-confirm (1.5s)
 */
@Composable
private fun RowScope.StopButton(
    onClick: () -> Unit,
    testTag: String
) {
    val pressed = remember { mutableStateOf(false) }
    val scaleAnim = animateFloatAsState(
        targetValue = if (pressed.value) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "stopBtnScale"
    )

    var isHolding by remember { mutableStateOf(false) }
    val holdProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isHolding) {
        if (isHolding) {
            scope.launch {
                holdProgress.snapTo(0f)
                val result = holdProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
                )
                if (result.endReason == AnimationEndReason.Finished && holdProgress.value >= 1f) {
                    isHolding = false
                    onClick()
                }
            }
        } else {
            scope.launch {
                holdProgress.snapTo(0f)
            }
        }
    }

    val reducedMotion = rememberReducedMotion()

    Box(
        modifier = Modifier
            .weight(1f)
            .height(RunnaTheme.ButtonHeight)
            .graphicsLayer { scaleX = scaleAnim.value; scaleY = scaleAnim.value }
            .clip(RunnaTheme.ButtonShape)
            .background(RunnaTheme.Orange)
            .border(BorderStroke(1.dp, RunnaTheme.Orange.copy(alpha = 0.3f)), RunnaTheme.ButtonShape)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    isHolding = true
                    val upOrCancel = waitForUpOrCancellation()
                    isHolding = false
                    if (upOrCancel == null && holdProgress.value < 1f) {
                        // Cancelado antes de completar 1.5s
                    }
                }
            }
            .semantics { contentDescription = "Parar" }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isHolding) {
                CircularProgressIndicator(
                    progress = { holdProgress.value },
                    color = RunnaTheme.OrangeOn,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = RunnaTheme.ActionBarPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    tint = RunnaTheme.OrangeOn,
                    modifier = Modifier.size(RunnaTheme.ButtonIconSize)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Parar",
                    style = RunnaTextStyles.Button,
                    color = RunnaTheme.OrangeOn
                )
            }
        }
    }
}
