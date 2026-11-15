package com.example.ui.screens.plan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entities.WorkoutEntity
import com.example.ui.components.AisleCard
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleShape

@Composable
fun WeekCard(
    weekNum: Int,
    workouts: List<WorkoutEntity>,
    isCurrentWeek: Boolean,
    todayStr: String,
    onMarkCompleted: (Long) -> Unit,
    onMarkSkipped: (Long) -> Unit,
    onResetStatus: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(isCurrentWeek) }
    val completedInWeek = workouts.count { it.status == WorkoutEntity.STATUS_CONCLUIDO }

    AisleCard(
        shape = AisleShape.lg,
        border = BorderStroke(
            width = if (isCurrentWeek) 1.5.dp else 1.dp,
            color = if (isCurrentWeek) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("week_card_$weekNum"),
    ) {
        Column(
            modifier = Modifier
                .padding(AisleSpacing.md)
                .animateContentSize()
        ) {
            // Header do card da semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .semantics(mergeDescendants = true) {
                        contentDescription = "Semana $weekNum, $completedInWeek de ${workouts.size} treinos, toque para expandir ou recolher"
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Semana $weekNum",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrentWeek) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        if (isCurrentWeek) {
                            Spacer(modifier = Modifier.width(AisleSpacing.xs))
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, AisleShape.xs)
                                    .padding(horizontal = AisleSpacing.sm, vertical = AisleSpacing.xxs)
                            ) {
                                Text(
                                    text = "Esta semana",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(AisleSpacing.xs))
                    Text(
                        text = "$completedInWeek de ${workouts.size} treinos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Recolher semana" else "Expandir semana"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = AisleSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
                ) {
                    workouts.forEach { workout ->
                        WorkoutRowItem(
                            workout = workout,
                            todayStr = todayStr,
                            onMarkCompleted = { onMarkCompleted(workout.id) },
                            onMarkSkipped = { onMarkSkipped(workout.id) },
                            onResetStatus = { onResetStatus(workout.id) }
                        )
                    }
                }
            }
        }
    }
}
