package com.elite.sch.app

import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.tracker.WeeklyGoalsCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class WeeklyGoalsCalculatorTest {

    private fun createWorkout(
        id: Long,
        status: String,
        runDurationSeconds: Int
    ): WorkoutEntity {
        val steps = listOf(
            WorkoutStep(
                type = StepType.WALK,
                durationSeconds = 300,
                instruction = "Caminhe"
            ),
            WorkoutStep(
                type = StepType.RUN,
                durationSeconds = runDurationSeconds,
                instruction = "Corra"
            )
        )
        return WorkoutEntity(
            id = id,
            planId = 1L,
            semanaIndex = 1,
            treinoIndexNaSemana = id.toInt(),
            etapaIndex = 1,
            data = "2026-05-1${id}",
            titulo = "Treino $id",
            resumoEtapa = "Etapa 1",
            passosJson = WorkoutStepConverter.toJson(steps),
            duracaoMinutos = 20,
            duracaoCorridaMinutos = (runDurationSeconds + 59) / 60,
            status = status
        )
    }

    @Test
    fun `empty workouts return all zeros`() {
        val goals = WeeklyGoalsCalculator.calculate(emptyList())
        assertEquals(0, goals.completedWorkouts)
        assertEquals(0, goals.plannedWorkouts)
        assertEquals(0, goals.completedRunMinutes)
        assertEquals(0, goals.plannedRunMinutes)
        assertEquals(0f, goals.workoutsProgress, 0.001f)
        assertEquals(0f, goals.runMinutesProgress, 0.001f)
    }

    @Test
    fun `paused workouts are excluded from planned workouts and planned run minutes`() {
        val workouts = listOf(
            createWorkout(id = 1, status = WorkoutEntity.STATUS_CONCLUIDO, runDurationSeconds = 600), // 10 min
            createWorkout(id = 2, status = WorkoutEntity.STATUS_PAUSADO, runDurationSeconds = 600),   // Pausado (IGNORADO)
            createWorkout(id = 3, status = WorkoutEntity.STATUS_PLANEJADO, runDurationSeconds = 600)  // 10 min
        )

        val goals = WeeklyGoalsCalculator.calculate(workouts)

        // Apenas 2 treinos são válidos (1 concluído, 1 planejado, 0 pausado)
        assertEquals(1, goals.completedWorkouts)
        assertEquals(2, goals.plannedWorkouts)
        assertEquals(10, goals.completedRunMinutes)
        assertEquals(20, goals.plannedRunMinutes)
        assertEquals(0.5f, goals.workoutsProgress, 0.001f)
        assertEquals(0.5f, goals.runMinutesProgress, 0.001f)
    }

    @Test
    fun `all completed workouts calculate 100 percent progress`() {
        val workouts = listOf(
            createWorkout(id = 1, status = WorkoutEntity.STATUS_CONCLUIDO, runDurationSeconds = 480), // 8 min
            createWorkout(id = 2, status = WorkoutEntity.STATUS_CONCLUIDO, runDurationSeconds = 480), // 8 min
            createWorkout(id = 3, status = WorkoutEntity.STATUS_CONCLUIDO, runDurationSeconds = 480)  // 8 min
        )

        val goals = WeeklyGoalsCalculator.calculate(workouts)

        assertEquals(3, goals.completedWorkouts)
        assertEquals(3, goals.plannedWorkouts)
        assertEquals(24, goals.completedRunMinutes)
        assertEquals(24, goals.plannedRunMinutes)
        assertEquals(1.0f, goals.workoutsProgress, 0.001f)
        assertEquals(1.0f, goals.runMinutesProgress, 0.001f)
    }

    @Test
    fun `skipped workouts count as planned but not completed`() {
        val workouts = listOf(
            createWorkout(id = 1, status = WorkoutEntity.STATUS_CONCLUIDO, runDurationSeconds = 600),
            createWorkout(id = 2, status = WorkoutEntity.STATUS_PULADO, runDurationSeconds = 600),
            createWorkout(id = 3, status = WorkoutEntity.STATUS_PLANEJADO, runDurationSeconds = 600)
        )

        val goals = WeeklyGoalsCalculator.calculate(workouts)

        assertEquals(1, goals.completedWorkouts)
        assertEquals(3, goals.plannedWorkouts)
        assertEquals(10, goals.completedRunMinutes)
        assertEquals(30, goals.plannedRunMinutes)
    }
}
