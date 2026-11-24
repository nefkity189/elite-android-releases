package com.elite.sch.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.PausePreviewData
import com.elite.sch.app.generator.RegenerationResult
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w480dp-h1200dp-xhdpi")
class FullPlanScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val today = LocalDate.of(2025, 11, 5) // Fixed date for tests

    // Sample workouts for testing
    private val sampleWorkouts = listOf(
        createWorkout(1L, 1, 1, today.minusDays(2), WorkoutEntity.STATUS_CONCLUIDO, 3, "Treino concluído"),
        createWorkout(2L, 1, 2, today, WorkoutEntity.STATUS_PLANEJADO, 4, "Treino de hoje"),
        createWorkout(3L, 1, 3, today.plusDays(2), WorkoutEntity.STATUS_PLANEJADO, 4, "Treino futuro"),
        createWorkout(4L, 2, 1, today.plusDays(5), WorkoutEntity.STATUS_PLANEJADO, 5, "Semana 2 treino 1"),
        createWorkout(5L, 2, 2, today.plusDays(7), WorkoutEntity.STATUS_PLANEJADO, 5, "Semana 2 treino 2"),
        createWorkout(6L, 2, 3, today.plusDays(9), WorkoutEntity.STATUS_PLANEJADO, 6, "Semana 2 treino 3"),
    )

    private val samplePlan = PlanEntity(
        id = 1L,
        status = PlanEntity.STATUS_ATIVO,
        dataInicio = today.minusWeeks(1).toString(),
        dataFim = today.plusWeeks(8).toString(),
        totalSemanas = 10,
        diasPorSemana = 3,
        etapaInicial = 1
    )

    private fun createWorkout(
        id: Long,
        semana: Int,
        treinoNaSemana: Int,
        date: LocalDate,
        status: String,
        etapa: Int,
        titulo: String
    ): WorkoutEntity {
        val steps = listOf(
            WorkoutStep(StepType.WALK, 10 * 60, "Aquecimento"),
            WorkoutStep(StepType.RUN, 20 * 60, "Corrida"),
            WorkoutStep(StepType.WALK, 10 * 60, "Desaquecimento")
        )
        val passosJson = WorkoutStepConverter.toJson(steps)
        return WorkoutEntity(
            id = id,
            planId = 1L,
            semanaIndex = semana,
            treinoIndexNaSemana = treinoNaSemana,
            etapaIndex = etapa,
            data = date.toString(),
            titulo = titulo,
            resumoEtapa = "Etapa $etapa",
            passosJson = passosJson,
            duracaoMinutos = 40,
            duracaoCorridaMinutos = 20,
            status = status,
            concluidoEm = if (status == WorkoutEntity.STATUS_CONCLUIDO) System.currentTimeMillis() else null
        )
    }

    private fun createScreen(
        workouts: List<WorkoutEntity> = sampleWorkouts,
        activePlan: PlanEntity? = samplePlan,
        currentDate: LocalDate = today,
        onMarkCompleted: (Long) -> Unit = {},
        onMarkSkipped: (Long) -> Unit = {},
        onResetStatus: (Long) -> Unit = {},
        onPreviewRegeneration: (LocalDate) -> Result<RegenerationResult> = { Result.failure(Exception()) },
        onApplyRegeneration: (LocalDate) -> Unit = {},
        onPreviewPause: (LocalDate, LocalDate, String, String) -> Result<PausePreviewData> = { _, _, _, _ -> Result.failure(Exception()) },
        onApplyPause: (PausePreviewData, String, LocalDate, LocalDate, String, String) -> Unit = { _, _, _, _, _, _ -> },
        onDeletePlan: ((Boolean) -> Unit) -> Unit = { _ -> },
        onBackClick: () -> Unit = {},
        onCreatePlanClick: () -> Unit = {},
        isLoading: Boolean = false,
        isEmpty: Boolean = false,
        showBackButton: Boolean = false,
        modifier: Modifier = Modifier
    ) {
        composeRule.setContent {
            FullPlanScreen(
                workouts = workouts,
                activePlan = activePlan,
                currentDate = currentDate,
                onMarkCompleted = onMarkCompleted,
                onMarkSkipped = onMarkSkipped,
                onResetStatus = onResetStatus,
                onPreviewRegeneration = onPreviewRegeneration,
                onApplyRegeneration = onApplyRegeneration,
                onPreviewPause = onPreviewPause,
                onApplyPause = onApplyPause,
                onDeletePlan = onDeletePlan,
                onBackClick = onBackClick,
                onCreatePlanClick = onCreatePlanClick,
                isLoading = isLoading,
                isEmpty = isEmpty,
                showBackButton = showBackButton,
                modifier = modifier.fillMaxSize()
            )
        }
    }

    @Test
    fun `FullPlanScreen shows loading state`() {
        createScreen(workouts = emptyList(), isLoading = true)

        composeRule.onNodeWithTag("loading_state").assertExists()
        composeRule.onNodeWithText("Carregando cronograma...").assertExists()
    }

    @Test
    fun `FullPlanScreen shows empty state when no workouts`() {
        var createPlanClicked = false
        createScreen(
            workouts = emptyList(),
            isEmpty = true,
            onCreatePlanClick = { createPlanClicked = true }
        )

        composeRule.onNodeWithText("Nenhum plano ativo").assertExists()
        composeRule.onNodeWithText("Você ainda não possui um plano montado.").assertExists()
        composeRule.onNodeWithText("Criar meu plano").assertExists()

        // Click create plan button
        composeRule.onNodeWithText("Criar meu plano").performClick()
        assertTrue(createPlanClicked)
    }

    @Test
    fun `FullPlanScreen shows header information correctly`() {
        createScreen()

        // Check header content
        // currentWeekNum = 1, totalWeeks = 10 (from activePlan)
        composeRule.onNodeWithText("Semana 1 de 10").assertExists()
        composeRule.onNodeWithText("ETAPA ATUAL").assertExists()
        composeRule.onNodeWithText("1 de 6 treinos concluídos").assertExists()
        composeRule.onNodeWithText("16%").assertExists()
    }

    @Test
    fun `FullPlanScreen renders week cards`() {
        createScreen()

        // Check for week cards
        composeRule.onNodeWithText("Semana 1").assertExists()

        // Scroll to week 2
        composeRule.onNode(hasScrollAction()).performScrollToNode(hasTestTag("week_card_2"))
        composeRule.onNodeWithText("Semana 2").assertExists()

        // Week 1 should be expanded (current week) - check for workout items
        composeRule.onNode(hasScrollAction()).performScrollToNode(hasTestTag("week_card_1"))
        composeRule.onNodeWithText("Treino concluído").assertExists()
        composeRule.onNodeWithText("Treino de hoje").assertExists()
        composeRule.onNodeWithText("Treino futuro").assertExists()
    }

    @Test
    fun `FullPlanScreen clicking week card toggles expansion`() {
        createScreen(workouts = sampleWorkouts)

        // Week 2 should be collapsed
        composeRule.onNode(hasScrollAction()).performScrollToNode(hasTestTag("week_card_2"))
        composeRule.onNodeWithText("Semana 2").assertExists()

        // Week 2 workouts should NOT be visible yet
        composeRule.onNodeWithText("Semana 2 treino 1").assertDoesNotExist()

        // Click week 2 card to expand
        composeRule.onNodeWithTag("week_card_2").performClick()

        // Week 2 workouts should now be visible
        composeRule.onNodeWithText("Semana 2 treino 1").assertExists()
    }

    @Test
    fun `FullPlanScreen workout status labels are correct`() {
        createScreen()

        // Completed workout should show "feito"
        composeRule.onNodeWithText("feito").assertExists()

        // Today's workout should show "hoje"
        composeRule.onNodeWithText("hoje").assertExists()

        // Future workout should show "futuro"
        composeRule.onNodeWithText("futuro").assertExists()
    }

    @Test
    fun `FullPlanScreen manage plan button opens dialog`() {
        createScreen()

        // Click manage plan button
        composeRule.onNodeWithTag("btn_gerenciar_plano").performClick()

        // Dialog should appear
        composeRule.onNodeWithText("Gerenciar Plano").assertExists()
        composeRule.onNodeWithText("O que você deseja fazer com o seu plano de treino?").assertExists()
        composeRule.onNodeWithText("Preciso de uma pausa").assertExists()
        composeRule.onNodeWithText("Marcar férias").assertExists()
        composeRule.onNodeWithText("Alterar data final").assertExists()
        composeRule.onNodeWithText("Excluir plano").assertExists()

        // Click close
        composeRule.onNodeWithText("Fechar").performClick()
        composeRule.onNodeWithText("Gerenciar Plano").assertDoesNotExist()
    }

    @Test
    fun `FullPlanScreen delete plan confirmation dialog`() {
        var deletePlanCalled = false
        createScreen(onDeletePlan = { deletePlanCalled = true })

        // Open manage menu
        composeRule.onNodeWithTag("btn_gerenciar_plano").performClick()
        composeRule.onNodeWithText("Excluir plano").performClick()

        // First confirmation dialog
        composeRule.onNodeWithText("Excluir plano de treino?").assertExists()
        composeRule.onNodeWithText("O plano atual e os próximos treinos agendados serão removidos. Todo o seu histórico de corridas e rotas concluídas será preservado.").assertExists()
        composeRule.onNodeWithTag("btn_confirm_delete_plan_step1").performClick()

        // Second confirmation dialog
        composeRule.onNodeWithText("Confirmar exclusão definitiva").assertExists()
        composeRule.onNodeWithTag("btn_confirm_delete_plan_step2").performClick()

        // Delete should be called
        assertTrue(deletePlanCalled)
    }

    @Test
    fun `FullPlanScreen workout detail dialog opens on click`() {
        createScreen()

        // Click on first workout card
        composeRule.onNodeWithTag("plan_workout_1").performClick()

        // Dialog should show workout details
        // Check for "Desfazer" button which is unique to the dialog (for completed workouts)
        composeRule.onNodeWithText("Desfazer").assertExists()
        composeRule.onNodeWithText("Fechar").assertExists()

        // Workout title should also be present in the dialog
        composeRule.onAllNodesWithText("Treino concluído").assertCountEquals(2) // Card + Dialog Title

        // Close dialog
        composeRule.onNodeWithText("Fechar").performClick()
    }

    @Test
    fun `FullPlanScreen back button triggers callback`() {
        var backClicked = false
        createScreen(showBackButton = true, onBackClick = { backClicked = true })

        composeRule.onNodeWithTag("full_plan_back_button").performClick()
        assertTrue(backClicked)
    }
}
