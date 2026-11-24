package com.elite.sch.app.ui.screens.plan

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteButtonVariant
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.StepItemView
import com.elite.sch.app.ui.theme.Amber
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorkoutRowItem(
    workout: WorkoutEntity,
    todayStr: String,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isCompleted = workout.status == WorkoutEntity.STATUS_CONCLUIDO
    val isToday = workout.data == todayStr && workout.status == WorkoutEntity.STATUS_PLANEJADO
    val (icon, iconColor, iconBackground) = when {
        isCompleted -> Triple(Icons.Default.Check, OnOlive, Olive)
        workout.status == WorkoutEntity.STATUS_PULADO -> Triple(Icons.Default.Close, Red, RedSoft)
        workout.status == WorkoutEntity.STATUS_PAUSADO -> Triple(Icons.Default.Pause, Amber, Amber.copy(alpha = 0.15f))
        isToday -> Triple(Icons.Default.PlayArrow, Olive, OliveSoft)
        else -> Triple(Icons.Default.CalendarMonth, Olive, OliveSoft)
    }

    val dateLabel = try {
        LocalDate.parse(workout.data)
            .format(DateTimeFormatter.ofPattern("EEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR")))
            .replace(".", "")
            .lowercase(Locale.forLanguageTag("pt-BR"))
    } catch (_: Exception) {
        workout.data
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable(onClick = onOpenDetails)
            .testTag("plan_workout_${workout.id}"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Treino ${workout.treinoIndexNaSemana} · $dateLabel",
                style = TextStyle(
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp
                ),
                color = EliteText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "• ${workout.resumoEtapa}",
                style = TextStyle(
                    fontFamily = EliteBodyFont,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp
                ),
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "${workout.duracaoMinutos} min",
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontSize = 12.sp,
                lineHeight = 16.8.sp
            ),
            color = TextMuted,
            maxLines = 1
        )
    }
}

@Composable
fun WorkoutDetailsDialog(
    workout: WorkoutEntity,
    onMarkCompleted: () -> Unit,
    onMarkSkipped: () -> Unit,
    onResetStatus: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateLabel = try {
        LocalDate.parse(workout.data)
            .format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR")))
            .replaceFirstChar { it.uppercase() }
    } catch (_: Exception) {
        workout.data
    }
    val steps = androidx.compose.runtime.remember(workout.passosJson) {
        WorkoutStepConverter.fromJson(workout.passosJson)
    }

    EliteDialog(
        title = workout.titulo,
        text = "$dateLabel • ${workout.duracaoMinutos} min",
        onDismiss = onDismiss,
        dismissLabel = "Fechar",
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(280.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = workout.resumoEtapa,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(steps.size) { idx ->
                    StepItemView(step = steps[idx], stepIndex = idx)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (workout.status == WorkoutEntity.STATUS_PLANEJADO) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    EliteButton(
                        text = "Pular",
                        onClick = {
                            onDismiss()
                            onMarkSkipped()
                        },
                        variant = EliteButtonVariant.Secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    EliteButton(
                        text = "Concluir",
                        onClick = {
                            onDismiss()
                            onMarkCompleted()
                        },
                        variant = EliteButtonVariant.Primary
                    )
                }
            } else {
                EliteButton(
                    text = "Desfazer",
                    onClick = {
                        onDismiss()
                        onResetStatus()
                    },
                    variant = EliteButtonVariant.Secondary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
