package com.elite.sch.app.tracker

import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.model.StepType

data class WeeklyGoals(
    val completedWorkouts: Int,
    val plannedWorkouts: Int,
    val completedRunMinutes: Int,
    val plannedRunMinutes: Int
) {
    val workoutsProgress: Float
        get() = if (plannedWorkouts > 0) (completedWorkouts.toFloat() / plannedWorkouts).coerceIn(0f, 1f) else 0f

    val runMinutesProgress: Float
        get() = if (plannedRunMinutes > 0) (completedRunMinutes.toFloat() / plannedRunMinutes).coerceIn(0f, 1f) else 0f
}

object WeeklyGoalsCalculator {

    /**
     * Calcula as metas da semana a partir dos treinos da semana atual.
     * Regra mandatória: treinos com status "pausado" NÃO contam como planejados nem para metas de corrida.
     */
    fun calculate(weekWorkouts: List<WorkoutEntity>): WeeklyGoals {
        // Ignora treinos pausados
        val validWorkouts = weekWorkouts.filter { it.status != WorkoutEntity.STATUS_PAUSADO }

        val plannedWorkouts = validWorkouts.size
        val completedWorkouts = validWorkouts.count { it.status == WorkoutEntity.STATUS_CONCLUIDO }

        var plannedRunSeconds = 0
        var completedRunSeconds = 0

        for (workout in validWorkouts) {
            val steps = WorkoutStepConverter.fromJson(workout.passosJson)
            val runSec = steps
                .filter { it.type == StepType.RUN }
                .sumOf { it.durationSeconds }

            plannedRunSeconds += runSec
            if (workout.status == WorkoutEntity.STATUS_CONCLUIDO) {
                completedRunSeconds += runSec
            }
        }

        val plannedRunMinutes = (plannedRunSeconds + 59) / 60
        val completedRunMinutes = (completedRunSeconds + 59) / 60

        return WeeklyGoals(
            completedWorkouts = completedWorkouts,
            plannedWorkouts = plannedWorkouts,
            completedRunMinutes = completedRunMinutes,
            plannedRunMinutes = plannedRunMinutes
        )
    }
}
