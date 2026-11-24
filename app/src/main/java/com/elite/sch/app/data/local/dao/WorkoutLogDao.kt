package com.elite.sch.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WorkoutLogEntity): Long

    @Query("SELECT * FROM workout_log WHERE workoutId = :workoutId LIMIT 1")
    fun getLogForWorkout(workoutId: Long): Flow<WorkoutLogEntity?>

    @Query("SELECT * FROM workout_log ORDER BY finalizadoEm DESC")
    fun getAllLogs(): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_log ORDER BY finalizadoEm DESC LIMIT :limit")
    suspend fun getRecentLogs(limit: Int): List<WorkoutLogEntity>

    @Query("SELECT * FROM workout_log WHERE id = :id LIMIT 1")
    suspend fun getLogById(id: Long): WorkoutLogEntity?

    @Query("UPDATE workout_log SET anotacoes = :anotacoes WHERE id = :id")
    suspend fun updateAnotacoes(id: Long, anotacoes: String?)

    @Query("DELETE FROM workout_log WHERE workoutId IN (SELECT id FROM workout WHERE planId = :planId)")
    suspend fun deleteByPlan(planId: Long)

    @Query("DELETE FROM workout_log WHERE workoutId IN (:workoutIds)")
    suspend fun deleteByWorkoutIds(workoutIds: List<Long>)

    @Query("DELETE FROM workout_log")
    suspend fun clearAllLogs()

    @Query("SELECT * FROM workout_log ORDER BY finalizadoEm DESC")
    suspend fun getAll(): List<WorkoutLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<WorkoutLogEntity>)
}
