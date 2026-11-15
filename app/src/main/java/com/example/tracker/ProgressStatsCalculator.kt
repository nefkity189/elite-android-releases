package com.example.tracker

import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class CurrentWeekStats(
    val completedWorkoutsCount: Int,
    val totalWorkoutsPlanned: Int,
    val totalDurationSeconds: Long,
    val totalDistanceMeters: Double
)

object ProgressStatsCalculator {

    /**
     * Calcula o resumo da semana atual:
     * - Quantidade de treinos concluídos
     * - Tempo total em segundos
     * - Distância total em metros
     */
    fun calculateCurrentWeekStats(
        workouts: List<WorkoutEntity>,
        logs: List<WorkoutLogEntity>,
        currentDate: LocalDate = LocalDate.now()
    ): CurrentWeekStats {
        val startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val thisWeekWorkouts = workouts.filter { workout ->
            try {
                val d = LocalDate.parse(workout.data)
                !d.isBefore(startOfWeek) && !d.isAfter(endOfWeek)
            } catch (e: Exception) {
                false
            }
        }

        val completedWorkouts = thisWeekWorkouts.filter { it.status == WorkoutEntity.STATUS_CONCLUIDO }
        val completedWorkoutIds = completedWorkouts.map { it.id }.toSet()

        val thisWeekLogs = logs.filter { it.workoutId in completedWorkoutIds }

        val totalDurationSeconds = thisWeekLogs.sumOf { it.duracaoSegundos }.let { totalFromLogs ->
            if (totalFromLogs > 0L) totalFromLogs
            else completedWorkouts.sumOf { it.duracaoMinutos.toLong() * 60L }
        }

        val totalDistanceMeters = thisWeekLogs.sumOf { it.distanciaMetros }

        return CurrentWeekStats(
            completedWorkoutsCount = completedWorkouts.size,
            totalWorkoutsPlanned = thisWeekWorkouts.size,
            totalDurationSeconds = totalDurationSeconds,
            totalDistanceMeters = totalDistanceMeters
        )
    }

    /**
     * Calcula a sequência (streak) de semanas consecutivas com ao menos 1 treino concluído.
     * Considera a semana atual: se tiver >= 1 concluído, conta e continua olhando para trás;
     * se tiver 0 na semana atual, mantém a sequência construída nas semanas anteriores caso a semana passada tenha tido treino.
     */
    fun calculateWeeklyStreak(
        workouts: List<WorkoutEntity>,
        currentDate: LocalDate = LocalDate.now()
    ): Int {
        val completedDates = workouts
            .filter { it.status == WorkoutEntity.STATUS_CONCLUIDO }
            .mapNotNull {
                try {
                    LocalDate.parse(it.data)
                } catch (e: Exception) {
                    null
                }
            }

        if (completedDates.isEmpty()) return 0

        // Agrupa datas de treinos concluídos por segunda-feira da respectiva semana
        val completedWeeks = completedDates.map { date ->
            date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        }.toSet()

        val currentMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        var streak = 0
        var checkMonday = currentMonday

        // Se a semana atual ainda não tem treino concluído, o usuário não perde a sequência anterior
        if (!completedWeeks.contains(checkMonday)) {
            checkMonday = checkMonday.minusWeeks(1)
        }

        while (completedWeeks.contains(checkMonday)) {
            streak++
            checkMonday = checkMonday.minusWeeks(1)
        }

        return streak
    }
}
