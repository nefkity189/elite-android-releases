package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.ActionGradient
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AisleFieldBorder
import com.example.ui.theme.AisleOnAction
import com.example.ui.theme.AisleOnPrimary
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTrack
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.AisleAlert
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.BrandButtonGradient
import com.example.ui.theme.animateFloatAsStateWithReduceMotion
import com.example.ui.theme.rememberReducedMotion

enum class AisleButtonVariant { Primary, Action, Secondary, Text, Destructive }

/**
 * Botão único do Aisle. Altura mínima de 52 dp, estado de carregamento,
 * ícones opcionais, feedback de pressionado (escurece 8%) sem mudar o layout.
 */
@Composable
fun AisleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AisleButtonVariant = AisleButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val active = enabled && !loading
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed && active) 0.97f else 1f,
        reduceMotion = rememberReducedMotion(),
        durationMillis = AisleMotion.ScalePressDuration,
        label = "buttonScale"
    )

    val contentColor: Color = when {
        !enabled -> AisleTextSecondary
        variant == AisleButtonVariant.Primary -> AisleTextPrimary
        variant == AisleButtonVariant.Action -> AisleOnAction
        variant == AisleButtonVariant.Secondary -> AisleTurquoise
        variant == AisleButtonVariant.Text -> AisleTurquoise
        else -> AisleAlert
    }
    val background: Brush? = when {
        !enabled -> Brush.horizontalGradient(listOf(AisleTrack, AisleTrack))
        variant == AisleButtonVariant.Primary -> BrandButtonGradient
        variant == AisleButtonVariant.Action -> ActionGradient
        else -> null
    }
    val border: BorderStroke? = when {
        !enabled -> null
        variant == AisleButtonVariant.Secondary -> BorderStroke(2.dp, AisleTurquoise)
        variant == AisleButtonVariant.Destructive -> BorderStroke(1.dp, AisleAlert)
        else -> null
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .clip(AisleShape.pill)
            .then(if (background != null) Modifier.background(background) else Modifier)
            .then(if (border != null) Modifier.border(border, AisleShape.pill) else Modifier)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = active,
                role = Role.Button,
                onClick = {
                    if (active) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .padding(horizontal = AisleSpacing.xxl, vertical = AisleSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = contentColor
                )
                Spacer(Modifier.width(8.dp))
            } else if (leadingIcon != null) {
                leadingIcon()
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (trailingIcon != null && !loading) {
                Spacer(Modifier.width(8.dp))
                trailingIcon()
            }
        }
        // Escurece 8% ao pressionar, sem alterar o tamanho
        if (pressed && active) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.08f))
            )
        }
    }
}

/**
 * Cartão de seleção (única ou múltipla). Selecionado = borda turquesa de 2 dp,
 * ícone de check e fundo turquesa translúcido. A seleção nunca depende só de cor.
 */
@Composable
fun AisleSelectableCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    multiple: Boolean = false
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) AisleTurquoise else AisleBorder,
        animationSpec = tween(150),
        label = "selectableBorder"
    )
    val container = if (selected) AisleTurquoise.copy(alpha = 0.12f) else AisleCardBottom

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 110.dp)
            .clip(AisleShape.md)
            .background(container)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = AisleShape.md
            )
            .selectable(
                selected = selected,
                role = if (multiple) Role.Checkbox else Role.RadioButton,
                onClick = onClick
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = AisleTextPrimary,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .then(
                    if (selected) Modifier.background(AisleTurquoise)
                    else Modifier.border(2.dp, AisleFieldBorder, CircleShape)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = AisleOnPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Barra de progresso segmentada (ex.: Pergunta 2 de 3), anunciada pelo leitor de tela.
 */
@Composable
fun StepProgressBar(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier,
    description: String = "Etapa $current de $total"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..total) {
            val color by animateColorAsState(
                targetValue = if (i <= current) AisleTurquoise else AisleTrack,
                animationSpec = tween(200),
                label = "segment$i"
            )
            Box(
                Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/**
 * Barra superior simples: voltar (48 dp) + título.
 */
@Composable
fun AisleTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    backTestTag: String = "top_bar_back",
    action: (@Composable () -> Unit)? = null,
    titleStyle: androidx.compose.ui.text.TextStyle? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag(backTestTag)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = AisleTextPrimary
                )
            }
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = titleStyle ?: MaterialTheme.typography.titleMedium,
            color = AisleTextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (action != null) {
            action()
        }
    }
}

/** Cor de cada tipo de etapa (nunca só cor: a legenda e o semantics descrevem). */
fun stepTypeColor(type: com.example.model.StepType): Color = when (type) {
    com.example.model.StepType.WARMUP -> com.example.ui.theme.StepColors.Warmup
    com.example.model.StepType.RUN -> com.example.ui.theme.StepColors.Run
    com.example.model.StepType.WALK -> com.example.ui.theme.StepColors.Walk
    com.example.model.StepType.COOLDOWN -> com.example.ui.theme.StepColors.Cooldown
}

/**
 * Barra segmentada com os blocos do treino, proporcional ao tempo de cada passo.
 * [currentIndex] >= 0 destaca o passo atual (usado na corrida).
 */
@Composable
fun WorkoutBlocksBar(
    steps: List<com.example.model.WorkoutStep>,
    modifier: Modifier = Modifier,
    currentIndex: Int = -1,
    showLegend: Boolean = true
) {
    if (steps.isEmpty()) return
    val runMinutes = steps.filter { it.type == com.example.model.StepType.RUN }.sumOf { it.durationSeconds } / 60
    val description = "Treino em ${steps.size} blocos, com $runMinutes minutos de corrida"

    androidx.compose.foundation.layout.Column(
        modifier = modifier.semantics(mergeDescendants = true) { contentDescription = description }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(14.dp),
            horizontalArrangement = Arrangement.spacedBy(AisleSpacing.xxs)
        ) {
            steps.forEachIndexed { index, step ->
                val dimmed = currentIndex >= 0 && index != currentIndex
                Box(
                    Modifier
                        .weight(step.durationSeconds.toFloat().coerceAtLeast(1f))
                        .height(14.dp)
                        .clip(CircleShape)
                        .background(stepTypeColor(step.type).copy(alpha = if (dimmed) 0.45f else 1f))
                )
            }
        }
        if (showLegend) {
            Spacer(modifier = Modifier.height(AisleSpacing.md))
            Row(horizontalArrangement = Arrangement.spacedBy(AisleSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                LegendItem("Corrida", com.example.ui.theme.StepColors.Run, AisleTurquoise)
                LegendItem("Caminhada", com.example.ui.theme.StepColors.Walk, AisleTextSecondary)
                LegendItem("Aquec./desaquec.", com.example.ui.theme.StepColors.Warmup, com.example.ui.theme.AisleTextAccent2)
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, swatch: Color, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(androidx.compose.foundation.shape.RoundedCornerShape(2.dp)).background(swatch))
        Spacer(Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = textColor)
    }
}

/**
 * Chip de escolha compacto (3 por linha). Selecionado = borda de 2 dp, fundo turquesa translúcido,
 * texto em negrito e ícone de check: nunca só cor.
 */
@Composable
fun AisleChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .clip(AisleShape.md)
            .background(if (selected) AisleTurquoise.copy(alpha = 0.12f) else AisleCardBottom)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) AisleTurquoise else AisleBorder,
                shape = AisleShape.md
            )
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .semantics { if (description != null) contentDescription = description }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AisleTurquoise,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = if (selected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyLarge,
            color = AisleTextPrimary
        )
    }
}
