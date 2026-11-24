package com.example.ui.components

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
import com.example.model.StepType
import com.example.model.WorkoutStep
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleNumberStyles
import com.example.ui.theme.AislePrimaryContainer
import com.example.ui.theme.AisleOnPrimaryContainer
import com.example.ui.theme.AisleSecondaryContainer
import com.example.ui.theme.AisleOnSecondaryContainer
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleSurfaceVariant
import com.example.ui.theme.AisleOnSurfaceVariant
import com.example.ui.theme.AisleTertiaryContainer
import com.example.ui.theme.AisleOnTertiaryContainer
import com.example.ui.theme.AisleSurface
import com.example.ui.theme.AisleOnSurface
import com.example.ui.theme.AislePrimary
import com.example.ui.theme.AisleOnPrimary
import com.example.ui.theme.animateFloatAsStateWithReduceMotion
import com.example.ui.theme.rememberReducedMotion
import com.example.ui.theme.AisleCard

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
                color = AislePrimary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = AisleOnSurfaceVariant
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
            .padding(AisleSpacing.giant)
            .testTag("empty_state"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.xl)
        ) {
            Surface(
                shape = CircleShape,
                color = AislePrimaryContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AislePrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AisleOnSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AisleOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = AisleSpacing.lg)
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
fun AisleSkeleton(
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
        durationMillis = AisleMotion.ShimmerDuration,
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
                        color = AisleSurfaceVariant.copy(alpha = 0.5f),
                        shape = shape
                    )
                    .graphicsLayer { this.alpha = shimmerAlpha }
            )
        }
    }
}

@Composable
fun AisleShimmerBox(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp),
    heightDp: Dp = 120.dp
) {
    val reduceMotion = rememberReducedMotion()
    val shimmerAlpha by animateFloatAsStateWithReduceMotion(
        targetValue = 1f,
        reduceMotion = reduceMotion,
        durationMillis = AisleMotion.ShimmerDuration,
        easing = LinearEasing,
        label = "shimmerBox"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp)
            .background(
                color = AisleSurfaceVariant.copy(alpha = 0.5f),
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
            AisleSecondaryContainer,
            AisleOnSecondaryContainer
        )
        StepType.RUN -> Triple(
            Icons.Default.DirectionsRun,
            AislePrimaryContainer,
            AisleOnPrimaryContainer
        )
        StepType.WALK -> Triple(
            Icons.Default.DirectionsWalk,
            AisleSurfaceVariant,
            AisleOnSurfaceVariant
        )
        StepType.COOLDOWN -> Triple(
            Icons.Default.SelfImprovement,
            AisleTertiaryContainer,
            AisleOnTertiaryContainer
        )
    }

    AisleCard(
        shape = AisleShape.md,
        modifier = modifier
            .fillMaxWidth()
            .testTag("step_item_$stepIndex")
    ) {
        Row(
            modifier = Modifier
                .padding(AisleSpacing.md)
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
            Spacer(modifier = Modifier.width(AisleSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.instruction,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = AisleOnSurface
                )
            }
            Spacer(modifier = Modifier.width(AisleSpacing.xs))
            Surface(
                shape = AisleShape.xs,
                color = badgeColor
            ) {
                Text(
                    text = step.durationFormatted,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = badgeTextColor,
                    modifier = Modifier.padding(horizontal = AisleSpacing.sm, vertical = AisleSpacing.xxs)
                )
            }
        }
    }
}
