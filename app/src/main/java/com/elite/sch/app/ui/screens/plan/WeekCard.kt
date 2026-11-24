package com.elite.sch.app.ui.screens.plan

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.rememberReducedMotion

@Composable
fun WeekCard(
    weekNum: Int,
    workouts: List<WorkoutEntity>,
    isCurrentWeek: Boolean,
    todayStr: String,
    onShowWorkoutDetails: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(isCurrentWeek) }
    val completedInWeek = workouts.count { it.status == WorkoutEntity.STATUS_CONCLUIDO }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(if (rememberReducedMotion()) 0 else 200),
        label = "weekChevronRotation"
    )

    EliteCard(
        shape = RoundedCornerShape(18.dp),
        selected = isCurrentWeek,
        modifier = modifier
            .fillMaxWidth()
            .testTag("week_card_$weekNum"),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Semana $weekNum",
                        style = TextStyle(
                            fontFamily = EliteDisplayFont,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            lineHeight = 23.sp,
                            letterSpacing = 0.8.sp
                        ),
                        color = EliteText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = when {
                            isCurrentWeek -> "Esta semana"
                            completedInWeek == workouts.size -> "${workouts.size} treinos · concluída"
                            else -> "${workouts.size} treinos"
                        },
                        style = TextStyle(
                            fontFamily = EliteBodyFont,
                            fontSize = 12.sp,
                            lineHeight = 16.8.sp
                        ),
                        color = if (isCurrentWeek) Olive else TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isCurrentWeek || completedInWeek > 0) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = OliveSoft
                        ) {
                            Text(
                                text = "$completedInWeek/${workouts.size}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = TextStyle(
                                    fontFamily = EliteBodyFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp,
                                    letterSpacing = 1.sp
                                ),
                                color = Olive
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = if (expanded) "Recolher semana" else "Expandir semana",
                        tint = EliteText,
                        modifier = Modifier
                            .size(22.dp)
                            .rotate(rotation)
                    )
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Line)
                    )
                    Spacer(Modifier.height(2.dp))
                    workouts.forEach { workout ->
                        WorkoutRowItem(
                            workout = workout,
                            todayStr = todayStr,
                            onOpenDetails = { onShowWorkoutDetails(workout) }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .border(1.dp, LineStrong, RoundedCornerShape(22.dp))
                            .clickable(enabled = workouts.isNotEmpty()) {
                                workouts.firstOrNull()?.let(onShowWorkoutDetails)
                            }
                            .testTag("week_details_$weekNum"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ver detalhes",
                            style = TextStyle(
                                fontFamily = EliteBodyFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            ),
                            color = EliteText
                        )
                    }
                }
            }
        }
    }
}
