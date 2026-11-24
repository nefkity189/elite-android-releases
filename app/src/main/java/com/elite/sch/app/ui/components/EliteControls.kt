package com.elite.sch.app.ui.components

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
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.ActionGradient
import com.elite.sch.app.ui.theme.EliteActionOrange
import com.elite.sch.app.ui.theme.EliteBorder
import com.elite.sch.app.ui.theme.EliteCardBottom
import com.elite.sch.app.ui.theme.EliteFieldBorder
import com.elite.sch.app.ui.theme.EliteOnAction
import com.elite.sch.app.ui.theme.EliteOnPrimary
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.EliteTextPrimary
import com.elite.sch.app.ui.theme.EliteTextSecondary
import com.elite.sch.app.ui.theme.EliteTrack
import com.elite.sch.app.ui.theme.EliteTurquoise
import com.elite.sch.app.ui.theme.EliteAlert
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.BrandButtonGradient
import com.elite.sch.app.ui.theme.animateFloatAsStateWithReduceMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion

enum class EliteButtonVariant { Primary, Action, Secondary, Text, Destructive }

/**
 * Botão único do Elite. Altura mínima de 52 dp, estado de carregamento,
 * ícones opcionais, feedback de pressionado (escurece 8%) sem mudar o layout.
 */
@Composable
fun EliteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: EliteButtonVariant = EliteButtonVariant.Primary,
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
        durationMillis = EliteMotion.ScalePressDuration,
        label = "buttonScale"
    )

    val contentColor: Color = when {
        !enabled -> EliteTextSecondary
        variant == EliteButtonVariant.Primary -> EliteTextPrimary
        variant == EliteButtonVariant.Action -> EliteOnAction
        variant == EliteButtonVariant.Secondary -> EliteTurquoise
        variant == EliteButtonVariant.Text -> EliteTurquoise
        else -> EliteAlert
    }
    val background: Brush? = when {
        !enabled -> Brush.horizontalGradient(listOf(EliteTrack, EliteTrack))
        variant == EliteButtonVariant.Primary -> BrandButtonGradient
        variant == EliteButtonVariant.Action -> ActionGradient
        else -> null
    }
    val border: BorderStroke? = when {
        !enabled -> null
        variant == EliteButtonVariant.Secondary -> BorderStroke(2.dp, EliteTurquoise)
        variant == EliteButtonVariant.Destructive -> BorderStroke(1.dp, EliteAlert)
        else -> null
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .clip(EliteShape.pill)
            .then(if (background != null) Modifier.background(background) else Modifier)
            .then(if (border != null) Modifier.border(border, EliteShape.pill) else Modifier)
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
            .padding(horizontal = EliteSpacing.xxl, vertical = EliteSpacing.lg),
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
fun EliteSelectableCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    multiple: Boolean = false
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) EliteTurquoise else EliteBorder,
        animationSpec = tween(150),
        label = "selectableBorder"
    )
    val container = if (selected) EliteTurquoise.copy(alpha = 0.12f) else EliteCardBottom

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 110.dp)
            .clip(EliteShape.md)
            .background(container)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = EliteShape.md
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
            color = EliteTextPrimary,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .then(
                    if (selected) Modifier.background(EliteTurquoise)
                    else Modifier.border(2.dp, EliteFieldBorder, CircleShape)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = EliteOnPrimary,
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
                targetValue = if (i <= current) EliteTurquoise else EliteTrack,
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
fun EliteTopBar(
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
                    tint = EliteTextPrimary
                )
            }
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = titleStyle ?: MaterialTheme.typography.titleMedium,
            color = EliteTextPrimary,
            modifier = Modifier.weight(1f)
        )
        if (action != null) {
            action()
        }
    }
}

/** Cor de cada tipo de etapa (nunca só cor: a legenda e o semantics descrevem). */
fun stepTypeColor(type: com.elite.sch.app.model.StepType): Color = when (type) {
    com.elite.sch.app.model.StepType.WARMUP -> com.elite.sch.app.ui.theme.StepColors.Warmup
    com.elite.sch.app.model.StepType.RUN -> com.elite.sch.app.ui.theme.StepColors.Run
    com.elite.sch.app.model.StepType.WALK -> com.elite.sch.app.ui.theme.StepColors.Walk
    com.elite.sch.app.model.StepType.COOLDOWN -> com.elite.sch.app.ui.theme.StepColors.Cooldown
}

/**
 * Barra segmentada com os blocos do treino, proporcional ao tempo de cada passo.
 * [currentIndex] >= 0 destaca o passo atual (usado na corrida).
 */
@Composable
fun WorkoutBlocksBar(
    steps: List<com.elite.sch.app.model.WorkoutStep>,
    modifier: Modifier = Modifier,
    currentIndex: Int = -1,
    showLegend: Boolean = true
) {
    if (steps.isEmpty()) return
    val runMinutes = steps.filter { it.type == com.elite.sch.app.model.StepType.RUN }.sumOf { it.durationSeconds } / 60
    val description = "Treino em ${steps.size} blocos, com $runMinutes minutos de corrida"

    androidx.compose.foundation.layout.Column(
        modifier = modifier.semantics(mergeDescendants = true) { contentDescription = description }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(14.dp),
            horizontalArrangement = Arrangement.spacedBy(EliteSpacing.xxs)
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
            Spacer(modifier = Modifier.height(EliteSpacing.md))
            Row(horizontalArrangement = Arrangement.spacedBy(EliteSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                LegendItem("Corrida", com.elite.sch.app.ui.theme.StepColors.Run, EliteTurquoise)
                LegendItem("Caminhada", com.elite.sch.app.ui.theme.StepColors.Walk, EliteTextSecondary)
                LegendItem("Aquec./desaquec.", com.elite.sch.app.ui.theme.StepColors.Warmup, com.elite.sch.app.ui.theme.EliteTextAccent2)
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
fun EliteChoiceChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .clip(EliteShape.md)
            .background(if (selected) EliteTurquoise.copy(alpha = 0.12f) else EliteCardBottom)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) EliteTurquoise else EliteBorder,
                shape = EliteShape.md
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
                tint = EliteTurquoise,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = if (selected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyLarge,
            color = EliteTextPrimary
        )
    }
}
