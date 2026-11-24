package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.CheckinEntity

@Dao
interface CheckinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkin: CheckinEntity): Long

    @Query("SELECT * FROM checkin WHERE workoutId = :workoutId AND momento = :momento ORDER BY criadoEm DESC LIMIT 1")
    suspend fun getForWorkout(workoutId: Long, momento: String): CheckinEntity?

    @Query("DELETE FROM checkin")
    suspend fun clearAll()

    @Query("SELECT * FROM checkin ORDER BY criadoEm DESC")
    suspend fun getAll(): List<CheckinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checkins: List<CheckinEntity>)
}
