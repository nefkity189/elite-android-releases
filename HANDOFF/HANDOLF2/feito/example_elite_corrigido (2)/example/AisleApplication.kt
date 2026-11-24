package com.aistudio.primeiro5k.kptwlz

import android.app.Application
import androidx.work.Configuration

class AisleApplication : Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}