package com.elite.sch.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "route_point",
    indices = [Index(value = ["workoutId"])]
)
data class RoutePointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: Long,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestampMillis: Long
)
