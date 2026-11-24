package com.example.ui.components

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
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AisleFieldBorder
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleOnPrimary
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTrack
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.animateFloatAsStateWithReduceMotion
import com.example.ui.theme.rememberReducedMotion
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
        durationMillis = AisleMotion.ScalePressDuration,
        label = "switchPressScale"
    )
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .border(1.dp, if (enabled) AisleFieldBorder else AisleBorder, CircleShape)
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
            tint = if (enabled) AisleTurquoise else AisleFieldBorder,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Seletor numérico com − e + de 48 dp, que repete ao segurar.
 * [label] entra no texto falado: "Diminuir {label}" / "Aumentar {label}".
 */
@Composable
fun AisleStepper(
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
        Text(
            text = valueText,
            style = MaterialTheme.typography.headlineSmall,
            color = AisleTextPrimary,
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
fun AisleSwitchRow(
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
        durationMillis = AisleMotion.ScalePressDuration,
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if (enabled) AisleTextPrimary else AisleTextSecondary
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AisleTextSecondary
                )
            }
        }
        Spacer(Modifier.size(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AisleOnPrimary,
                checkedTrackColor = AisleTurquoise,
                uncheckedThumbColor = AisleTextSecondary,
                uncheckedTrackColor = AisleTrack,
                uncheckedBorderColor = AisleFieldBorder
            )
        )
    }
}

/** Diálogo padrão do Aisle (substitui o AlertDialog do Material). */
@Composable
fun AisleDialog(
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
    content: (@Composable ColumnScope.() -> Unit)? = null
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
                .clip(AisleShape.lg)
                .background(AisleCardBottom)
                .border(1.dp, AisleBorder, AisleShape.lg)
                .padding(24.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AisleTurquoise,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = AisleTextPrimary
            )
            if (text != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AisleTextSecondary
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
                    AisleButton(
                        text = dismissLabel,
                        onClick = onDismiss,
                        variant = AisleButtonVariant.Text,
                        modifier = if (dismissTestTag != null) {
                            Modifier.testTag(dismissTestTag).fillMaxWidth()
                        } else Modifier.fillMaxWidth()
                    )
                }
                if (confirmLabel != null && onConfirm != null) {
                    AisleButton(
                        text = confirmLabel,
                        onClick = onConfirm,
                        variant = if (destructive) AisleButtonVariant.Destructive
                        else AisleButtonVariant.Primary,
                        modifier = if (confirmTestTag != null) {
                            Modifier.testTag(confirmTestTag).fillMaxWidth()
                        } else Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
