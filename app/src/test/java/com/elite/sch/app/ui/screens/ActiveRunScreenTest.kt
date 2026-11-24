package com.elite.sch.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.elite.sch.app.service.RunState
import com.elite.sch.app.ui.screens.ActiveRunScreen
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w480dp-h1200dp-xhdpi")
class ActiveRunScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun createScreen(
        isFreeRun: Boolean = false,
        steps: List<WorkoutStep> = emptyList(),
        runState: RunState = RunState()
    ) {
        composeRule.setContent {
            ActiveRunScreen(
                runState = runState,
                dismissBatteryWarning = false,
                onDismissBatteryWarning = {},
                onPauseClick = {},
                onResumeClick = {},
                onFinishClick = {},
                onToggleVoice = {},
                onToggleVibration = {},
                steps = steps,
                isFreeRun = isFreeRun,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Test
    fun `ActiveRunScreen renders free-run layout with timer and no step card`() {
        val steps = emptyList<WorkoutStep>()
        val runState = RunState(
            currentStep = null,
            timeRemainingInStep = 0L,
            totalDistanceMeters = 0.0,
            averagePaceSeconds = 360.0,
            averagePaceFormatted = "30:00"
        )

        createScreen(isFreeRun = true, steps = steps, runState = runState)

        composeRule.onNodeWithText("Corrida livre").assertExists()
        composeRule.onNodeWithText("Caminhe").assertExists()
        composeRule.onNodeWithText("Aguardando início...").assertExists()
        composeRule.onNodeWithTag("step_countdown_timer").assertDoesNotExist()
        composeRule.onNodeWithTag("current_step_card").assertDoesNotExist()
        composeRule.onNodeWithTag("next_step_card").assertDoesNotExist()
    }

    @Test
    fun `ActiveRunScreen renders structured workout layout with step card`() {
        val steps = listOf(WorkoutStep(StepType.RUN, 1800, "Correr 30 min"))
        val runState = RunState(
            currentStep = steps.first(),
            timeRemainingInStep = 1800L,
            totalDistanceMeters = 0.0,
            averagePaceSeconds = 360.0,
            averagePaceFormatted = "30:00"
        )

        createScreen(isFreeRun = false, steps = steps, runState = runState)

        composeRule.onNodeWithText("Corrida Livre").assertDoesNotExist()
        composeRule.onNodeWithText("Correr 30 min").assertExists()
        composeRule.onNodeWithTag("current_step_card").assertExists()
        composeRule.onNodeWithTag("step_countdown_timer").assertExists()
        composeRule.onNodeWithTag("next_step_card").assertDoesNotExist()
    }

}
