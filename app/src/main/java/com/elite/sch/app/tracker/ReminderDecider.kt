package com.elite.sch.app.tracker

import android.content.Context
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.R
import java.time.LocalDate

object ReminderDecider {

    /**
     * Decide se deve haver notificação de lembrete num dado dia:
     * Retorna o WorkoutEntity se houver um treino no dia com status PLANEJADO (ainda não concluído).
     * Retorna null se não houver treino ou se o treino do dia já estiver concluído, pulado ou movido.
     */
    fun getWorkoutForReminder(
        date: LocalDate,
        workouts: List<WorkoutEntity>
    ): WorkoutEntity? {
        val dateStr = date.toString()
        val workout = workouts.find { it.data == dateStr } ?: return null
        return if (workout.status == WorkoutEntity.STATUS_PLANEJADO) {
            workout
        } else {
            null
        }
    }

    /**
     * Formata o texto da notificação:
     * "Hoje é dia de treino: {título do treino}"
     */
    fun formatNotificationText(context: Context, workout: WorkoutEntity): String {
        return "${context.getString(R.string.today_workout_card_title)}: ${workout.titulo}"
    }
}
