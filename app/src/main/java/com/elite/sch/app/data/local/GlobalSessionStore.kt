package com.elite.sch.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataMigration
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.elite.sch.app.workspace.GlobalSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

/**
 * DataStore para persistir a sessão global entre reinicializações do app.
 * Armazena: lastActiveModality, completedOnboardingModalities, userName, dismissBatteryWarning, voiceEnabled, vibrationEnabled
 */
class GlobalSessionStore internal constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        // Chaves
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_LAST_ACTIVE_MODALITY = stringPreferencesKey("last_active_modality")
        private val KEY_COMPLETED_ONBOARDING = stringPreferencesKey("completed_onboarding_modalities")
        private val KEY_DISMISS_BATTERY_WARNING = stringPreferencesKey("dismiss_battery_warning")
        private val KEY_VOICE_ENABLED = stringPreferencesKey("voice_enabled")
        private val KEY_VIBRATION_ENABLED = stringPreferencesKey("vibration_enabled")
        private val KEY_APP_VERSION = stringPreferencesKey("app_version")
        private val KEY_FIRST_LAUNCH_TIMESTAMP = stringPreferencesKey("first_launch_timestamp")

        @Volatile
        private var INSTANCE: GlobalSessionStore? = null

        fun getInstance(context: Context): GlobalSessionStore {
            return INSTANCE ?: synchronized(this) {
                val appContext = context.applicationContext
                // Remove legacy un-extended or wrong-extension files if they exist
                try {
                    val legacyFile = File(appContext.filesDir, "datastore/global_session")
                    if (legacyFile.exists() && legacyFile.isFile) {
                        legacyFile.delete()
                    }
                    val legacyDotFile = File(appContext.filesDir, "datastore/global_session.preferences.pb")
                    if (legacyDotFile.exists() && legacyDotFile.isFile) {
                        legacyDotFile.delete()
                    }
                } catch (_: Exception) {}

                val store = PreferenceDataStoreFactory.create(
                    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
                    scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                ) {
                    appContext.preferencesDataStoreFile("global_session")
                }
                INSTANCE = GlobalSessionStore(store)
                INSTANCE!!
            }
        }
    }

    /**
     * Observa a sessão global como Flow.
     */
    val globalSession: Flow<GlobalSession> = dataStore.data
        .map { prefs ->
            val userName = prefs[KEY_USER_NAME] ?: ""
            val lastActiveModality = prefs[KEY_LAST_ACTIVE_MODALITY] ?: "plano_5k"
            val completedOnboardingStr = prefs[KEY_COMPLETED_ONBOARDING] ?: ""
            val dismissBatteryWarning = prefs[KEY_DISMISS_BATTERY_WARNING]?.toBoolean() ?: false
            val voiceEnabled = prefs[KEY_VOICE_ENABLED]?.toBoolean() ?: true
            val vibrationEnabled = prefs[KEY_VIBRATION_ENABLED]?.toBoolean() ?: true
            val appVersion = prefs[KEY_APP_VERSION] ?: "1.0"
            val firstLaunchTimestamp = prefs[KEY_FIRST_LAUNCH_TIMESTAMP]?.toLong() ?: System.currentTimeMillis()

            val completedOnboardingModalities = completedOnboardingStr
                .split(',')
                .filter { it.isNotBlank() }
                .toSet()

            GlobalSession(
                userName = userName,
                lastActiveModality = lastActiveModality,
                dismissBatteryWarning = dismissBatteryWarning,
                voiceEnabled = voiceEnabled,
                vibrationEnabled = vibrationEnabled,
                lastBackupTimestamp = 0,
                completedOnboardingModalities = completedOnboardingModalities,
                appVersion = appVersion,
                firstLaunchTimestamp = firstLaunchTimestamp
            )
        }

    /**
     * Atualiza o nome do usuário.
     */
    suspend fun setUserName(name: String) {
        dataStore.edit { it[KEY_USER_NAME] = name }
    }

    /**
     * Atualiza a última modalidade ativa.
     */
    suspend fun setLastActiveModality(modality: String) {
        dataStore.edit { it[KEY_LAST_ACTIVE_MODALITY] = modality }
    }

    /**
     * Atualiza as modalidades que completaram o onboarding.
     */
    suspend fun setCompletedOnboardingModalities(modalities: Set<String>) {
        dataStore.edit { it[KEY_COMPLETED_ONBOARDING] = modalities.joinToString(",") }
    }

    /**
     * Atualiza dismiss battery warning.
     */
    suspend fun setDismissBatteryWarning(dismiss: Boolean) {
        dataStore.edit { it[KEY_DISMISS_BATTERY_WARNING] = dismiss.toString() }
    }

    /**
     * Atualiza voz ativa.
     */
    suspend fun setVoiceEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_VOICE_ENABLED] = enabled.toString() }
    }

    /**
     * Atualiza vibração ativa.
     */
    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_VIBRATION_ENABLED] = enabled.toString() }
    }

    /**
     * Atualiza todos os campos de uma vez (para sincronização completa).
     */
    suspend fun updateAll(session: GlobalSession) {
        dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = session.userName
            prefs[KEY_LAST_ACTIVE_MODALITY] = session.lastActiveModality
            prefs[KEY_COMPLETED_ONBOARDING] = session.completedOnboardingModalities.joinToString(",")
            prefs[KEY_DISMISS_BATTERY_WARNING] = session.dismissBatteryWarning.toString()
            prefs[KEY_VOICE_ENABLED] = session.voiceEnabled.toString()
            prefs[KEY_VIBRATION_ENABLED] = session.vibrationEnabled.toString()
            prefs[KEY_APP_VERSION] = session.appVersion
            prefs[KEY_FIRST_LAUNCH_TIMESTAMP] = session.firstLaunchTimestamp.toString()
        }
    }

    /**
     * Limpa completamente o DataStore da sessão global (usado em "Apagar todos os meus dados").
     */
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}