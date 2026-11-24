package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.DatabaseProvider
import com.example.worker.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val modalidades = listOf(
                        DatabaseProvider.MODALIDADE_LIVRE,
                        DatabaseProvider.MODALIDADE_2_5K,
                        DatabaseProvider.MODALIDADE_5K,
                        DatabaseProvider.MODALIDADE_10K
                    )

                    modalidades.forEach { modality ->
                        val db = DatabaseProvider.getDatabase(modality, context)
                        val profile = db.runnerProfileDao().getProfile().firstOrNull()
                        if (profile?.lembretesAtivos == true) {
                            ReminderScheduler.scheduleDailyReminder(
                                context = context,
                                modality = modality,
                                hour = profile.lembreteHora,
                                minute = profile.lembreteMinuto,
                                active = true
                            )
                        }
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
