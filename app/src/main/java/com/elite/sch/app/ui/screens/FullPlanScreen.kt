package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.elite.sch.app.ui.theme.EliteChartColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.PausePreviewData
import com.elite.sch.app.generator.RegenerationResult
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
import com.elite.sch.app.ui.screens.plan.DateChangeDialogSection
import com.elite.sch.app.ui.screens.plan.DeletePlanStep1Dialog
import com.elite.sch.app.ui.screens.plan.DeletePlanStep2Dialog
import com.elite.sch.app.ui.screens.plan.ExitToHubDialog
import com.elite.sch.app.ui.screens.plan.ManagePlanMenuDialog
import com.elite.sch.app.ui.screens.plan.PauseDialogSection
import com.elite.sch.app.ui.screens.plan.VacationDialogSection
import com.elite.sch.app.ui.screens.plan.WeekCard
import com.elite.sch.app.ui.screens.plan.WorkoutDetailsDialog
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion
import com.elite.sch.app.ui.theme.rememberAnimatedVisibilitySpecs
import java.time.DayOfWeek
import java.time.LocalDate
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive

@Composable
fun FullPlanScreen(
    workouts: List<WorkoutEntity>,
    activePlan: PlanEntity? = null,
    currentDate: LocalDate = LocalDate.now(),
    frequenciaExercicio: String = "Outros",
    @Suppress("UNUSED_PARAMETER") diasSelecionados: List<DayOfWeek> = emptyList(),
    onMarkCompleted: (Long) -> Unit,
    onMarkSkipped: (Long) -> Unit,
    onResetStatus: (Long) -> Unit,
    onPreviewRegeneration: (LocalDate) -> Result<RegenerationResult> = { Result.failure(Exception()) },
    onApplyRegeneration: (LocalDate) -> Unit = {},
    onPreviewPause: (startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Result<PausePreviewData> = { _, _, _, _ -> Result.failure(Exception()) },
    onApplyPause: (previewData: PausePreviewData, tipo: String, startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Unit = { _, _, _, _, _, _ -> },
    onDeletePlan: (onResult: (Boolean) -> Unit) -> Unit = { _ -> },
    onBackClick: () -> Unit = {},
    onOpenChangePlan: () -> Unit = {},
    onCreatePlanClick: () -> Unit = onBackClick,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    showBackButton: Boolean = false,
    modifier: Modifier = Modifier,
) {
    if (showBackButton) {
        BackHandler(onBack = onBackClick)
    }

    if (isLoading) {
        LoadingState(message = "Carregando cronograma...", modifier = modifier)
        return
    }

    if (isEmpty || workouts.isEmpty()) {
        EmptyState(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            title = "Nenhum plano ativo",
            description = "Você ainda não possui um plano montado.",
            actionLabel = "Criar meu plano",
            onActionClick = onCreatePlanClick,
            modifier = modifier,
        )
        return
    }

    val todayStr = currentDate.toString()
    val totalWeeks = activePlan?.totalSemanas ?: (workouts.maxOfOrNull { it.semanaIndex } ?: 1)
    val currentWeekNum = workouts.find { it.data == todayStr }?.semanaIndex
        ?: workouts.filter { it.data >= todayStr }.minByOrNull { it.data }?.semanaIndex
        ?: (workouts.lastOrNull()?.semanaIndex ?: 1)

    val progress = if (totalWeeks > 0) currentWeekNum.toFloat() / totalWeeks else 0f
    val weeksGrouped = workouts.groupBy { it.semanaIndex }

    var showManageMenuDialog by remember { mutableStateOf(false) }
    var showPauseDialog by remember { mutableStateOf(false) }
    var showVacationDialog by remember { mutableStateOf(false) }
    var showDateChangeDialog by remember { mutableStateOf(false) }
    var showDeleteStep1Dialog by remember { mutableStateOf(false) }
    var showDeleteStep2Dialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var selectedWorkout by remember { mutableStateOf<WorkoutEntity?>(null) }

    val planEndDate = remember(workouts, activePlan) {
        val lastDateStr = workouts.maxOfOrNull { it.data }
        if (lastDateStr != null) {
            try { LocalDate.parse(lastDateStr) } catch (_: Exception) { currentDate.plusWeeks(9) }
        } else {
            currentDate.plusWeeks(9)
        }
    }

    // Scroll progress indicator state
    val listState = rememberLazyListState()
    val reducedMotion = rememberReducedMotion()
    val density = LocalDensity.current
    val specs = rememberAnimatedVisibilitySpecs(reduceMotion = reducedMotion)
    val scrollProgress = remember {
        derivedStateOf {
            if (listState.layoutInfo.totalItemsCount > 0) {
                val firstVisibleItem = listState.firstVisibleItemIndex
                val firstVisibleItemOffset = listState.firstVisibleItemScrollOffset
                val itemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 1
                val totalItems = weeksGrouped.size + 2 // header + spacer
                val progress = (firstVisibleItem + firstVisibleItemOffset / itemHeight.toFloat()) / totalItems
                progress.coerceIn(0f, 1f)
            } else 0f
        }
    }

    // Diálogo Menu Gerenciar Plano
    if (showManageMenuDialog) {
        ManagePlanMenuDialog(
            onOpenPause = { showPauseDialog = true },
            onOpenVacation = { showVacationDialog = true },
            onOpenDateChange = { showDateChangeDialog = true },
            onOpenDeleteStep1 = { showDeleteStep1Dialog = true },
            onOpenChangePlan = { showExitDialog = true },
            onDismiss = { showManageMenuDialog = false }
        )
    }

    // Diálogo de saída: trocar de plano / voltar ao início
    if (showExitDialog) {
        ExitToHubDialog(
            onConfirm = { onOpenChangePlan() },
            onDismiss = { showExitDialog = false }
        )
    }

    // Diálogo Preciso de uma Pausa
    if (showPauseDialog) {
        PauseDialogSection(
            currentDate = currentDate,
            workouts = workouts,
            activePlan = activePlan,
            onPreviewPause = onPreviewPause,
            onApplyPause = onApplyPause,
            onDismiss = { showPauseDialog = false }
        )
    }

    // Diálogo Marcar Férias
    if (showVacationDialog) {
        VacationDialogSection(
            currentDate = currentDate,
            workouts = workouts,
            activePlan = activePlan,
            onPreviewPause = onPreviewPause,
            onApplyPause = onApplyPause,
            onDismiss = { showVacationDialog = false }
        )
    }

    // Diálogo Alterar Data Final com Prévia
    if (showDateChangeDialog) {
        DateChangeDialogSection(
            currentDate = currentDate,
            planEndDate = planEndDate,
            activePlan = activePlan,
            frequenciaExercicio = frequenciaExercicio,
            onPreviewRegeneration = onPreviewRegeneration,
            onApplyRegeneration = onApplyRegeneration,
            onDismiss = { showDateChangeDialog = false }
        )
    }

    // Diálogos de Excluir Plano em 2 etapas
    if (showDeleteStep1Dialog) {
        DeletePlanStep1Dialog(
            onConfirm = {
                showDeleteStep1Dialog = false
                showDeleteStep2Dialog = true
            },
            onDismiss = { showDeleteStep1Dialog = false }
        )
    }

    var deletePlanError by remember { mutableStateOf<String?>(null) }

    if (showDeleteStep2Dialog) {
        DeletePlanStep2Dialog(
            onConfirm = {
                showDeleteStep2Dialog = false
                onDeletePlan { success ->
                    if (!success) {
                        deletePlanError = "Não foi possível excluir o plano. Tente novamente."
                    } else {
                        deletePlanError = null
                    }
                }
            },
            onDismiss = { showDeleteStep2Dialog = false }
        )
    }

    // Exibe erro de exclusão se houver
    if (deletePlanError != null) {
        EliteDialog(
            title = "Erro ao excluir plano",
            text = deletePlanError!!,
            onDismiss = { deletePlanError = null },
            confirmLabel = "OK",
            onConfirm = { deletePlanError = null }
        )
    }

    selectedWorkout?.let { workout ->
        WorkoutDetailsDialog(
            workout = workout,
            onMarkCompleted = { onMarkCompleted(workout.id) },
            onMarkSkipped = { onMarkSkipped(workout.id) },
            onResetStatus = { onResetStatus(workout.id) },
            onDismiss = { selectedWorkout = null }
        )
    }

    val planTypeLabel = when (activePlan?.modalidade?.lowercase()) {
        "plano_2_5k" -> "PLANO 2.5K"
        "plano_10k" -> "PLANO 10K"
        "livre" -> "CORRIDA LIVRE"
        else -> "PLANO 5K"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp)
            .testTag("screen_plan")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CardColor)
                        .border(1.dp, Line, CircleShape)
                        .clickable(onClick = onBackClick)
                        .testTag("full_plan_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
            }
            Text(
                text = "PLANO DE TREINO",
                style = MaterialTheme.typography.headlineLarge,
                color = TextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CardColor)
                    .border(1.dp, Line, CircleShape)
                    .clickable { showManageMenuDialog = true }
                    .testTag("btn_gerenciar_plano"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Gerenciar plano",
                    tint = TextColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            contentAlignment = Alignment.TopStart
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = scrollProgress.value > 0.01f,
                enter = specs.enter,
                exit = specs.exit
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(CardElevated)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(scrollProgress.value)
                            .height(3.dp)
                            .background(TextColor)
                            .clip(RoundedCornerShape(1.5.dp))
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                EliteCard(
                    shape = RoundedCornerShape(18.dp),
                    selected = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("plan_header_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ETAPA ATUAL",
                                style = TextStyle(
                                    fontFamily = EliteBodyFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    letterSpacing = 1.32.sp
                                ),
                                color = Olive
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OliveSoft
                            ) {
                                Text(
                                    text = planTypeLabel,
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
                        Text(
                            text = "SEMANA $currentWeekNum DE $totalWeeks",
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyle(
                                fontFamily = EliteDisplayFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                lineHeight = 31.5.sp,
                                letterSpacing = 0.9.sp
                            ),
                            color = TextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(CardElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .background(Olive)
                            )
                        }
                    }
                }
            }

            val sortedWeeks = weeksGrouped.toList().sortedBy { it.first }
            items(
                count = sortedWeeks.size,
                key = { index -> sortedWeeks[index].first }
            ) { index ->
                val weekNum = sortedWeeks[index].first
                val weekWorkouts = sortedWeeks[index].second
                WeekCard(
                    weekNum = weekNum,
                    workouts = weekWorkouts,
                    isCurrentWeek = weekNum == currentWeekNum,
                    todayStr = todayStr,
                    onShowWorkoutDetails = { selectedWorkout = it }
                )
            }
        }
    }
}
