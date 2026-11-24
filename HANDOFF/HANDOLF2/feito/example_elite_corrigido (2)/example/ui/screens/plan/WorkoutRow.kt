package com.example.ui.screens.plan


import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.WorkoutStepConverter
import com.example.data.local.entities.WorkoutEntity
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleDialog
import com.example.ui.components.StepItemView
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleNumberStyles
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.StatusPlanned
import com.example.ui.theme.StatusSkipped
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorkoutRowItem(
    workout: WorkoutEntity,
    todayStr: String,
    onMarkCompleted: () -> Unit,
    onMarkSkipped: () -> Unit,
    onResetStatus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showStepsDialog by remember { mutableStateOf(false) }

    // Status com ícone + texto
    val (statusLabel, statusColor, statusIcon) = when {
        workout.status == WorkoutEntity.STATUS_PAUSADO -> Triple("Pausa", StatusSkipped, Icons.Default.PauseCircle)
        workout.status == WorkoutEntity.STATUS_CONCLUIDO -> Triple("feito", StatusCompleted, Icons.Default.CheckCircle)
        workout.status == WorkoutEntity.STATUS_PULADO -> Triple("pulado", StatusSkipped, Icons.Default.Cancel)
        (workout.data == todayStr) && (workout.status == WorkoutEntity.STATUS_PLANEJADO) -> Triple("hoje", MaterialTheme.colorScheme.primary, Icons.AutoMirrored.Filled.DirectionsRun)
        (workout.data > todayStr) && (workout.status == WorkoutEntity.STATUS_PLANEJADO) -> Triple("futuro", StatusPlanned, Icons.Default.Schedule)
        (workout.data < todayStr) && (workout.status == WorkoutEntity.STATUS_PLANEJADO) -> Triple("perdido", MaterialTheme.colorScheme.error, Icons.Default.ErrorOutline)
        else -> Triple("planejado", StatusPlanned, Icons.Default.Schedule)
    }

    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR"))
    val workoutDateStr = try {
        LocalDate.parse(workout.data).format(dateFormatter).replaceFirstChar { it.uppercase() }
    } catch (_: Exception) {
        workout.data
    }

    val steps = remember(workout.passosJson) {
        WorkoutStepConverter.fromJson(workout.passosJson)
    }

    AisleCard(
        shape = AisleShape.md,
        modifier = modifier
            .fillMaxWidth()
            .clickable { showStepsDialog = true }
            .testTag("plan_workout_${workout.id}"),
    ) {
        Row(
            modifier = Modifier.padding(AisleSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = workout.titulo,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.xs))
                    Surface(
                        shape = AisleShape.xs,
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = AisleSpacing.sm, vertical = AisleSpacing.xxs)
                        ) {
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(AisleSpacing.xxs))
                            Text(
                                text = statusLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(AisleSpacing.xs))
                Text(
                    text = workoutDateStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${workout.duracaoMinutos} min",
                        style = AisleNumberStyles.Small,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.xs))
                    Text(
                        text = "• ${workout.resumoEtapa}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Ver detalhes",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (showStepsDialog) {
        AisleDialog(
            title = workout.titulo,
            text = "$workoutDateStr • ${workout.duracaoMinutos} min",
            onDismiss = { showStepsDialog = false },
            dismissLabel = "Fechar",
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
                ) {
                    item {
                        Text(
                            text = workout.resumoEtapa,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.md))
                    }
                    items(steps.size) { idx ->
                        StepItemView(step = steps[idx], stepIndex = idx)
                    }
                }
                Spacer(modifier = Modifier.height(AisleSpacing.xl))
                if (workout.status == WorkoutEntity.STATUS_PLANEJADO) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AisleButton(
                            text = "Pular",
                            onClick = {
                                showStepsDialog = false
                                onMarkSkipped()
                            },
                            variant = AisleButtonVariant.Secondary
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.xs))
                        AisleButton(
                            text = "Concluir",
                            onClick = {
                                showStepsDialog = false
                                onMarkCompleted()
                            },
                            variant = AisleButtonVariant.Primary
                        )
                    }
                } else {
                    AisleButton(
                        text = "Desfazer",
                        onClick = {
                            showStepsDialog = false
                            onResetStatus()
                        },
                        variant = AisleButtonVariant.Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
