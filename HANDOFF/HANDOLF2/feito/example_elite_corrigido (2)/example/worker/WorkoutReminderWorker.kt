package com.example.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.R
import com.example.data.local.DatabaseProvider
import com.example.data.local.entities.WorkoutEntity
import com.example.tracker.ReminderDecider
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class WorkoutReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "WorkoutReminderWorker"
        const val CHANNEL_ID = "lembretes_treino"
        const val CHANNEL_NAME = "Lembretes de treino"
    }

    override suspend fun doWork(): Result {
        return try {
            val modality = inputData.getString(ReminderScheduler.KEY_MODALITY) ?: DatabaseProvider.MODALIDADE_5K
            val db = DatabaseProvider.getDatabase(modality, context)
            val profile = db.runnerProfileDao().getProfile().firstOrNull()

            if (profile?.lembretesAtivos == true) {
                val activePlan = db.planDao().getActivePlan(modalidade = modality).firstOrNull()
                if (activePlan != null) {
                    val workouts = db.workoutDao().getWorkoutsForPlan(activePlan.id).firstOrNull()
                    if (workouts != null) {
                        val today = LocalDate.now()
                        val workout = ReminderDecider.getWorkoutForReminder(today, workouts)
                        if (workout != null) {
                            sendNotification(modality, workout)
                        }
                    }
                }

                // Reagenda para o dia seguinte no mesmo horário para esta modalidade
                ReminderScheduler.scheduleDailyReminder(
                    context = context,
                    modality = modality,
                    hour = profile.lembreteHora,
                    minute = profile.lembreteMinuto,
                    active = true
                )
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao executar WorkoutReminderWorker", e)
            Result.success() // Retorna success para evitar falhas em loop e garante robustez da cadeia
        }
    }

    private fun sendNotification(modality: String, workout: WorkoutEntity) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações de lembretes dos treinos do dia no Aisle"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        // ID de notificação único por modalidade para evitar sobreposição
        val notificationId = 2001 + modality.hashCode()

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Elite")
            .setContentText(ReminderDecider.formatNotificationText(context, workout))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
