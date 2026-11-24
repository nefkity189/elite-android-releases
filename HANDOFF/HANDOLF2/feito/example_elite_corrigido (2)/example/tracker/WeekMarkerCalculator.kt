package com.example.tracker

import com.example.data.local.entities.WorkoutEntity
import java.time.LocalDate

enum class DayMarkerType {
    EMPTY_CIRCLE,   // Treino planejado (hoje ou futuro)
    FILLED_CIRCLE,  // Treino concluído
    DASH,           // Treino pulado
    MISSED,         // Treino planejado passado que não foi feito nem pulado ("perdido")
    NONE            // Dia de descanso
}

data class DayMarker(
    val date: LocalDate,
    val type: DayMarkerType
)

object WeekMarkerCalculator {

    /**
     * Mapeia uma lista de datas (ex: seg a dom de uma semana) e os treinos cadastrados
     * para uma lista de marcadores de dia (EMPTY_CIRCLE, FILLED_CIRCLE, DASH, MISSED, NONE).
     * Dias passados com treino planejado e sem status concluído/pulado viram MISSED.
     */
    fun calculateWeekMarkers(
        weekDates: List<LocalDate>,
        workouts: List<WorkoutEntity>,
        currentDate: LocalDate = LocalDate.now()
    ): List<DayMarker> {
        val workoutsByDate = workouts.groupBy { it.data }
        return weekDates.map { date ->
            val workout = workoutsByDate[date.toString()]?.firstOrNull()
            val marker = when (workout?.status) {
                WorkoutEntity.STATUS_CONCLUIDO -> DayMarkerType.FILLED_CIRCLE
                WorkoutEntity.STATUS_PULADO -> DayMarkerType.DASH
                WorkoutEntity.STATUS_MOVIDO -> DayMarkerType.DASH
                WorkoutEntity.STATUS_PAUSADO -> DayMarkerType.DASH
                WorkoutEntity.STATUS_PLANEJADO -> {
                    if (date.isBefore(currentDate)) {
                        DayMarkerType.MISSED
                    } else {
                        DayMarkerType.EMPTY_CIRCLE
                    }
                }
                null -> DayMarkerType.NONE
                else -> {
                    if (date.isBefore(currentDate)) {
                        DayMarkerType.MISSED
                    } else {
                        DayMarkerType.EMPTY_CIRCLE
                    }
                }
            }
            DayMarker(date = date, type = marker)
        }
    }
}
