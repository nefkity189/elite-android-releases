package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.ActiveRunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveRunDao {
    @Query("SELECT * FROM active_run LIMIT 1")
    fun getActiveRunFlow(): Flow<ActiveRunEntity?>

    @Query("SELECT * FROM active_run LIMIT 1")
    suspend fun getActiveRun(): ActiveRunEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveRun(run: ActiveRunEntity)

    @Query("DELETE FROM active_run")
    suspend fun clearActiveRun()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveRunSilent(run: ActiveRunEntity)
}
