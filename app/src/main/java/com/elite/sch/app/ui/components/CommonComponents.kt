package com.elite.sch.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.ui.theme.EliteBackground
import com.elite.sch.app.ui.theme.EliteCardBottom
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.ElitePrimaryContainer
import com.elite.sch.app.ui.theme.EliteOnPrimaryContainer
import com.elite.sch.app.ui.theme.EliteSecondaryContainer
import com.elite.sch.app.ui.theme.EliteOnSecondaryContainer
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteSurfaceVariant
import com.elite.sch.app.ui.theme.EliteOnSurfaceVariant
import com.elite.sch.app.ui.theme.EliteTertiaryContainer
import com.elite.sch.app.ui.theme.EliteOnTertiaryContainer
import com.elite.sch.app.ui.theme.EliteSurface
import com.elite.sch.app.ui.theme.EliteOnSurface
import com.elite.sch.app.ui.theme.ElitePrimary
import com.elite.sch.app.ui.theme.EliteOnPrimary
import com.elite.sch.app.ui.theme.animateFloatAsStateWithReduceMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion
import com.elite.sch.app.ui.theme.EliteCard

@Composable
fun LoadingState(
    message: String = "Carregando...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("loading_state"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = ElitePrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = EliteOnSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyState(
    icon: ImageVector = Icons.Default.FitnessCenter,
    title: String,
    description: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Boxrolável: com a bottom bar + insetos, o conteúdo de 320dp+ de altura não cabe
    // na tela (ex. Perfil -> "Ajustes do plano") e o botão de ação ficava inatingível.
    // `verticalScroll` + `Arrangement.Center` mantém a centralização quando há espaço
    // sobrando e permite rolar (e o clique) quando não há.
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(EliteSpacing.giant)
            .testTag("empty_state"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.xl)
        ) {
            Surface(
                shape = CircleShape,
                color = ElitePrimaryContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ElitePrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(EliteSpacing.sm)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EliteOnSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EliteOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = EliteSpacing.lg)
                )
            }
            if (actionLabel != null && onActionClick != null) {
                Button(
                    onClick = onActionClick,
                    modifier = Modifier
                        .height(52.dp)
                        .testTag("empty_state_action_button")
                ) {
                    Text(actionLabel)
                }
            }
        }
    }
}

@Composable
fun EliteSkeleton(
    modifier: Modifier = Modifier,
    heightDp: Dp = 16.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
    count: Int = 1,
    spacingDp: Dp = 12.dp
) {
    val reduceMotion = rememberReducedMotion()
    val shimmerAlpha by animateFloatAsStateWithReduceMotion(
        targetValue = 1f,
        reduceMotion = reduceMotion,
        durationMillis = EliteMotion.ShimmerDuration,
        easing = LinearEasing,
        label = "skeletonShimmer"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacingDp)
    ) {
        (0 until count).forEach { _ ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(heightDp)
                    .background(
                        color = EliteSurfaceVariant.copy(alpha = 0.5f),
                        shape = shape
                    )
                    .graphicsLayer { this.alpha = shimmerAlpha }
            )
        }
    }
}

@Composable
fun EliteShimmerBox(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp),
    heightDp: Dp = 120.dp
) {
    val reduceMotion = rememberReducedMotion()
    val shimmerAlpha by animateFloatAsStateWithReduceMotion(
        targetValue = 1f,
        reduceMotion = reduceMotion,
        durationMillis = EliteMotion.ShimmerDuration,
        easing = LinearEasing,
        label = "shimmerBox"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp)
            .background(
                color = EliteSurfaceVariant.copy(alpha = 0.5f),
                shape = shape
            )
            .graphicsLayer { this.alpha = shimmerAlpha }
    )
}

@Composable
fun StepItemView(
    step: WorkoutStep,
    stepIndex: Int,
    modifier: Modifier = Modifier
) {
    val (icon, badgeColor, badgeTextColor) = when (step.type) {
        StepType.WARMUP -> Triple(
            Icons.Default.DirectionsWalk,
            EliteSecondaryContainer,
            EliteOnSecondaryContainer
        )
        StepType.RUN -> Triple(
            Icons.Default.DirectionsRun,
            ElitePrimaryContainer,
            EliteOnPrimaryContainer
        )
        StepType.WALK -> Triple(
            Icons.Default.DirectionsWalk,
            EliteSurfaceVariant,
            EliteOnSurfaceVariant
        )
        StepType.COOLDOWN -> Triple(
            Icons.Default.SelfImprovement,
            EliteTertiaryContainer,
            EliteOnTertiaryContainer
        )
    }

    EliteCard(
        shape = EliteShape.md,
        modifier = modifier
            .fillMaxWidth()
            .testTag("step_item_$stepIndex")
    ) {
        Row(
            modifier = Modifier
                .padding(EliteSpacing.md)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeTextColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(EliteSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.instruction,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = EliteOnSurface
                )
            }
            Spacer(modifier = Modifier.width(EliteSpacing.xs))
            Surface(
                shape = EliteShape.xs,
                color = badgeColor
            ) {
                Text(
                    text = step.durationFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = badgeTextColor,
                    modifier = Modifier.padding(horizontal = EliteSpacing.sm, vertical = EliteSpacing.xxs)
                )
            }
        }
    }
}
