package com.elite.sch.app.account

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map

class AccountStore private constructor(private val store: DataStore<Preferences>) {
    companion object {
        private val signedIn = booleanPreferencesKey("signed_in")
        private val uid = stringPreferencesKey("uid")
        private val name = stringPreferencesKey("display_name")
        private val email = stringPreferencesKey("email")
        private val uf = stringPreferencesKey("uf")
        @Volatile private var instance: AccountStore? = null

        fun get(context: Context): AccountStore = instance ?: synchronized(this) {
            instance ?: AccountStore(PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            ) { context.applicationContext.preferencesDataStoreFile("account_store") }).also { instance = it }
        }
    }

    val account = store.data.map { prefs ->
        if (prefs[signedIn] == true) UserAccount(
            uid = prefs[uid].orEmpty(),
            displayName = prefs[name] ?: "Atleta Elite",
            email = prefs[email].orEmpty(),
            uf = prefs[uf]
        ) else null
    }

    suspend fun save(account: UserAccount?) {
        store.edit { prefs ->
            if (account == null) prefs.clear() else {
                prefs[signedIn] = true
                prefs[uid] = account.uid
                prefs[name] = account.displayName
                prefs[email] = account.email
                account.uf?.let { prefs[uf] = it } ?: prefs.remove(uf)
            }
        }
    }
}
