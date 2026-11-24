package com.elite.sch.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_run")
data class ActiveRunEntity(
    @PrimaryKey val workoutId: Long,
    val startTimeMillis: Long,
    val elapsedRealtimeBase: Long,
    val pausedElapsedMillis: Long = 0L,
    val isPaused: Boolean = false,
    val totalDistanceMeters: Double = 0.0,
    val lastUpdatedMillis: Long = System.currentTimeMillis(),
    val modality: String = "plano_5k"
)
