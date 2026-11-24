package com.elite.sch.app.account

import android.content.Context

/** Login dismissal is independent from AccountStore, whose sign-out clears its preferences. */
class LoginPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    fun isDismissed(): Boolean = prefs.getBoolean(KEY_DISMISSED, false)

    fun dismiss() {
        prefs.edit().putBoolean(KEY_DISMISSED, true).apply()
    }

    companion object {
        private const val KEY_DISMISSED = "login_dismissed"
    }
}
