package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleChartColors
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.generator.PausePreviewData
import com.example.generator.RegenerationResult
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleDialog
import com.example.ui.components.AisleTopBar
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.screens.plan.DateChangeDialogSection
import com.example.ui.screens.plan.DeletePlanStep1Dialog
import com.example.ui.screens.plan.DeletePlanStep2Dialog
import com.example.ui.screens.plan.ExitToHubDialog
import com.example.ui.screens.plan.ManagePlanMenuDialog
import com.example.ui.screens.plan.PauseDialogSection
import com.example.ui.screens.plan.VacationDialogSection
import com.example.ui.screens.plan.WeekCard
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.StatusCompleted
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.rememberReducedMotion
import com.example.ui.theme.rememberAnimatedVisibilitySpecs
import java.time.DayOfWeek
import java.time.LocalDate

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

    val completedCount = workouts.count { it.status == WorkoutEntity.STATUS_CONCLUIDO }
    val progress = if (workouts.isNotEmpty()) completedCount.toFloat() / workouts.size.toFloat() else 0f
    val weeksGrouped = workouts.groupBy { it.semanaIndex }

    var showManageMenuDialog by remember { mutableStateOf(false) }
    var showPauseDialog by remember { mutableStateOf(false) }
    var showVacationDialog by remember { mutableStateOf(false) }
    var showDateChangeDialog by remember { mutableStateOf(false) }
    var showDeleteStep1Dialog by remember { mutableStateOf(false) }
    var showDeleteStep2Dialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

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
        AisleDialog(
            title = "Erro ao excluir plano",
            text = deletePlanError!!,
            onDismiss = { deletePlanError = null },
            confirmLabel = "OK",
            onConfirm = { deletePlanError = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .padding(horizontal = AisleSpacing.xl)
            .testTag("screen_plan"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top bar (a seta de voltar só aparece quando a tela é aberta como sub-tela)
        AisleTopBar(
            title = "Plano de Treino",
            titleStyle = MaterialTheme.typography.headlineMedium,
            onBack = if (showBackButton) onBackClick else null,
            backTestTag = "full_plan_back_button",
            action = {
                IconButton(
                    onClick = { showManageMenuDialog = true },
                    modifier = Modifier.testTag("btn_gerenciar_plano")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Gerenciar plano",
                        tint = AisleTextPrimary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Scroll progress indicator at top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .padding(bottom = AisleSpacing.xs),
            contentAlignment = Alignment.TopStart
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val slideOffsetPx = remember(density) { with(density) { AisleSpacing.xs.roundToPx() } }
                AnimatedVisibility(
                    visible = scrollProgress.value > 0.01f,
                    enter = fadeIn(
                        animationSpec = tween(AisleMotion.Enter, 0, AisleMotion.Easing)
                    ) + slideInVertically(
                        animationSpec = tween(AisleMotion.Enter, 0, AisleMotion.Easing)
                    ) { -slideOffsetPx },
                    exit = fadeOut(
                        animationSpec = tween(AisleMotion.Exit, 0, AisleMotion.Easing)
                    ) + slideOutVertically(
                        animationSpec = tween(AisleMotion.Exit, 0, AisleMotion.Easing)
                    ) { slideOffsetPx }
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(AisleCardBottom)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(scrollProgress.value)
                            .height(3.dp)
                            .background(AisleTextPrimary)
                            .clip(RoundedCornerShape(1.5.dp))
                    )
                }
            }
        }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
        ) {
            // Header do Plano: Semana X de N e Barra de Progresso
            item {
                Spacer(modifier = Modifier.height(AisleSpacing.xxs))
                AisleCard(
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth().testTag("plan_header_card")
                ) {
                    Column(modifier = Modifier.padding(AisleSpacing.lg)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = AisleCardBottom
                                ) {
                                    Text(
                                        text = "ETAPA ATUAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AisleTextPrimary,
                                        modifier = Modifier.padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.xs)
                                    )
                                }
                                Spacer(modifier = Modifier.height(AisleSpacing.md))
                                Text(
                                    text = "Semana $currentWeekNum de $totalWeeks",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = AisleTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(AisleSpacing.lg))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = StatusCompleted,
                            trackColor = AisleCardBottom
                        )

                        Spacer(modifier = Modifier.height(AisleSpacing.md))

                        Text(
                            text = "$completedCount de ${workouts.size} treinos concluídos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Lista de Semanas como Cards com stagger animation
            val sortedWeeks = weeksGrouped.toList().sortedBy { it.first }
            items(
                count = sortedWeeks.size,
                key = { index -> sortedWeeks[index].first }
            ) { index ->
                val weekNum = sortedWeeks[index].first
                val weekWorkouts = sortedWeeks[index].second
                val completedInWeek = weekWorkouts.count { it.status == WorkoutEntity.STATUS_CONCLUIDO }
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            delayMillis = (sortedWeeks.indexOfFirst { it.first == weekNum } * AisleMotion.StaggerDelay).coerceAtMost(500),
                            durationMillis = AisleMotion.Enter,
                            easing = AisleMotion.Easing
                        )
                    ) + slideInVertically(
                        animationSpec = tween(
                            delayMillis = (sortedWeeks.indexOfFirst { it.first == weekNum } * AisleMotion.StaggerDelay).coerceAtMost(500),
                            durationMillis = AisleMotion.Enter,
                            easing = AisleMotion.Easing
                        )
                    ) { with(density) { AisleSpacing.xl.roundToPx() } },
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = AisleMotion.Exit,
                            easing = AisleMotion.Easing
                        )
                    ) + slideOutVertically(
                        animationSpec = tween(
                            durationMillis = AisleMotion.Exit,
                            easing = AisleMotion.Easing
                        )
                    ) { with(density) { -AisleSpacing.xl.roundToPx() } }
                ) {
                    WeekCard(
                        weekNum = weekNum,
                        workouts = weekWorkouts,
                        isCurrentWeek = weekNum == currentWeekNum,
                        todayStr = todayStr,
                        onMarkCompleted = onMarkCompleted,
                        onMarkSkipped = onMarkSkipped,
                        onResetStatus = onResetStatus
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(AisleSpacing.xxl))
            }
        }
    }
}
