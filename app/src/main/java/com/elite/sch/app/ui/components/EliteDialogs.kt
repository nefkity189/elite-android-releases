package com.elite.sch.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text as MaterialText
import com.elite.sch.app.ui.theme.Text as TextColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.elite.sch.app.ui.theme.EliteBorder
import com.elite.sch.app.ui.theme.EliteCardBottom
import com.elite.sch.app.ui.theme.EliteFieldBorder
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.EliteOnPrimary
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.EliteTextPrimary
import com.elite.sch.app.ui.theme.EliteTextSecondary
import com.elite.sch.app.ui.theme.EliteTrack
import com.elite.sch.app.ui.theme.EliteTurquoise
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.animateFloatAsStateWithReduceMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion
import com.elite.sch.app.ui.components.RunnaTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Botão redondo de 48 dp que repete a ação enquanto é mantido pressionado. */
@Composable
private fun RepeatIconButton(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnStep by rememberUpdatedState(onStep)
    val currentEnabled by rememberUpdatedState(enabled)
    val haptic = LocalHapticFeedback.current
    var pressed by remember { mutableStateOf(false) }
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "switchPressScale"
    )
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(1.dp, if (enabled) EliteFieldBorder else EliteBorder, CircleShape)
            .semantics {
                role = Role.Button
                contentDescription = description
                onClick(label = description) {
                    if (currentEnabled) currentOnStep()
                    true
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (!currentEnabled) return@detectTapGestures
                        pressed = true
                        currentOnStep()
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        coroutineScope {
                            val job = launch {
                                delay(400)
                                while (true) {
                                    if (currentEnabled) currentOnStep()
                                    delay(120)
                                }
                            }
                            tryAwaitRelease()
                            pressed = false
                            job.cancel()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) EliteTurquoise else EliteFieldBorder,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Seletor numérico com − e + de 48 dp, que repete ao segurar.
 * [label] entra no texto falado: "Diminuir {label}" / "Aumentar {label}".
 */
@Composable
fun EliteStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String,
    modifier: Modifier = Modifier,
    valueText: String = value.toString(),
    step: Int = 1,
    wrap: Boolean = false,
    minusTestTag: String? = null,
    plusTestTag: String? = null
) {
    val size = range.last - range.first + 1
    fun next(delta: Int): Int =
        if (wrap) Math.floorMod(value + delta - range.first, size) + range.first
        else (value + delta).coerceIn(range)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RepeatIconButton(
            icon = Icons.Filled.Remove,
            description = "Diminuir $label",
            enabled = wrap || value > range.first,
            onStep = { onValueChange(next(-step)) },
            modifier = if (minusTestTag != null) Modifier.testTag(minusTestTag) else Modifier
        )
        MaterialText(
            text = valueText,
            style = MaterialTheme.typography.headlineSmall,
            color = EliteTextPrimary,
            modifier = Modifier
                .defaultMinSize(minWidth = 40.dp)
                .semantics { stateDescription = "$label: $valueText" }
        )
        RepeatIconButton(
            icon = Icons.Filled.Add,
            description = "Aumentar $label",
            enabled = wrap || value < range.last,
            onStep = { onValueChange(next(step)) },
            modifier = if (plusTestTag != null) Modifier.testTag(plusTestTag) else Modifier
        )
    }
}

/**
 * Linha de configuração com switch; a linha inteira é clicável.
 *
 * Altura mínima de 64 dp: com `vertical = 8.dp` de padding a linha de 56 dp deixava
 * apenas ~40 dp de alvo real para o dedo nas linhas sem descrição (ex. "Vibração"),
 * abaixo do mínimo de 48 dp exigido pela RNF-06.
 */
@Composable
fun EliteSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "switchPressScale"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(vertical = 10.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                enabled = enabled,
                onClickLabel = title
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onCheckedChange(!checked)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            MaterialText(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if (enabled) EliteTextPrimary else EliteTextSecondary
            )
            if (description != null) {
                MaterialText(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EliteTextSecondary
                )
            }
        }
        Spacer(Modifier.size(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = EliteOnPrimary,
                checkedTrackColor = EliteTurquoise,
                uncheckedThumbColor = EliteTextSecondary,
                uncheckedTrackColor = EliteTrack,
                uncheckedBorderColor = EliteFieldBorder
            )
        )
    }
}

/** Diálogo padrão do Elite (substitui o AlertDialog do Material). */
@Composable
fun EliteDialog(
    title: String,
    onDismiss: () -> Unit,
    text: String? = null,
    confirmLabel: String? = null,
    onConfirm: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    dismissLabel: String? = "Cancelar",
    destructive: Boolean = false,
    icon: ImageVector? = null,
    confirmTestTag: String? = null,
    dismissTestTag: String? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    // Screen 4 specific: fixed dimensions 343x303dp, 24dp radius, red icon
    isFinishDialog: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = modifier
                .padding(24.dp)
                .widthIn(max = 360.dp)
                .fillMaxWidth()
                .clip(if (isFinishDialog) RoundedCornerShape(24.dp) else EliteShape.lg)
                .background(if (isFinishDialog) RunnaTheme.Card else EliteCardBottom)
                .border(if (isFinishDialog) BorderStroke(1.dp, RunnaTheme.Line) else BorderStroke(1.dp, EliteBorder), if (isFinishDialog) RoundedCornerShape(24.dp) else EliteShape.lg)
                .padding(if (isFinishDialog) 24.dp else 24.dp)
                .size(if (isFinishDialog) 343.dp else Dp.Unspecified, if (isFinishDialog) 303.dp else Dp.Unspecified)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isFinishDialog) Red else EliteTurquoise,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
            MaterialText(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = if (isFinishDialog) TextColor else EliteTextPrimary
            )
            if (text != null) {
                Spacer(Modifier.height(8.dp))
                MaterialText(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isFinishDialog) TextMuted else EliteTextSecondary
                )
            }
            if (content != null) {
                Spacer(Modifier.height(16.dp))
                content()
            }
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (dismissLabel != null) {
                    EliteButton(
                        text = dismissLabel,
                        onClick = onDismiss,
                        variant = if (isFinishDialog) EliteButtonVariant.Text else EliteButtonVariant.Text,
                        modifier = if (dismissTestTag != null) {
                            Modifier.testTag(dismissTestTag).fillMaxWidth()
                        } else Modifier.fillMaxWidth()
                    )
                }
                if (confirmLabel != null && onConfirm != null) {
                    EliteButton(
                        text = confirmLabel,
                        onClick = onConfirm,
                        variant = if (destructive || isFinishDialog) EliteButtonVariant.Destructive
                        else EliteButtonVariant.Primary,
                        modifier = if (confirmTestTag != null) {
                            Modifier.testTag(confirmTestTag).fillMaxWidth()
                        } else Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
