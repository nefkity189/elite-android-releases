package com.elite.sch.app.worker

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.elite.sch.app.data.local.DatabaseProvider
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    const val KEY_MODALITY = "MODALITY"

    fun getWorkName(modality: String): String = "workout_daily_reminder_$modality"

    fun scheduleDailyReminder(
        context: Context,
        modality: String,
        hour: Int,
        minute: Int,
        active: Boolean
    ) {
        val workManager = WorkManager.getInstance(context)
        val workName = getWorkName(modality)
        if (!active) {
            workManager.cancelUniqueWork(workName)
            return
        }

        val now = LocalDateTime.now()
        var targetTime = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!targetTime.isAfter(now)) {
            targetTime = targetTime.plusDays(1)
        }
        val delayMillis = Duration.between(now, targetTime).toMillis().coerceAtLeast(0)

        val inputData = workDataOf(KEY_MODALITY to modality)

        val workRequest = OneTimeWorkRequestBuilder<WorkoutReminderWorker>()
            .setInputData(inputData)
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelReminder(context: Context, modality: String) {
        WorkManager.getInstance(context).cancelUniqueWork(getWorkName(modality))
    }

    fun cancelAllReminders(context: Context) {
        val workManager = WorkManager.getInstance(context)
        listOf(
            DatabaseProvider.MODALIDADE_LIVRE,
            DatabaseProvider.MODALIDADE_2_5K,
            DatabaseProvider.MODALIDADE_5K,
            DatabaseProvider.MODALIDADE_10K
        ).forEach { modality ->
            workManager.cancelUniqueWork(getWorkName(modality))
        }
    }
}
