package com.elite.sch.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.elite.sch.app.data.local.entities.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout WHERE planId = :planId ORDER BY data ASC, treinoIndexNaSemana ASC")
    fun getWorkoutsForPlan(planId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workout WHERE planId = :planId AND data = :date LIMIT 1")
    fun getWorkoutForDate(planId: Long, date: String): Flow<WorkoutEntity?>

    @Query("SELECT * FROM workout WHERE planId = :planId AND data >= :currentDate AND status = 'planejado' ORDER BY data ASC LIMIT 1")
    fun getNextUpcomingWorkout(planId: Long, currentDate: String): Flow<WorkoutEntity?>

    @Query("SELECT * FROM workout WHERE planId = :planId ORDER BY data ASC, treinoIndexNaSemana ASC")
    suspend fun getWorkoutsForPlanList(planId: Long): List<WorkoutEntity>

    @Query("SELECT * FROM workout WHERE id = :workoutId LIMIT 1")
    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity?

    @Query("SELECT * FROM workout WHERE planId = :planId AND data = :date LIMIT 1")
    suspend fun getWorkoutForPlanAndDate(planId: Long, date: String): WorkoutEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("UPDATE workout SET status = :status, concluidoEm = :concluidoEm WHERE id = :workoutId")
    suspend fun updateWorkoutStatus(workoutId: Long, status: String, concluidoEm: Long?)

    @Query("UPDATE workout SET planId = 0 WHERE planId = :planId AND status = 'concluido'")
    suspend fun unlinkCompletedWorkoutsFromPlan(planId: Long)

    @Query("DELETE FROM workout WHERE planId = :planId")
    suspend fun deleteWorkoutsForPlan(planId: Long)

    @Query("DELETE FROM workout WHERE planId = :planId")
    suspend fun deleteByPlan(planId: Long)

    @Query("SELECT id FROM workout WHERE planId = :planId")
    suspend fun getWorkoutIdsForPlan(planId: Long): List<Long>

    @Query("DELETE FROM workout")
    suspend fun clearAll()

    @androidx.room.Delete
    suspend fun deleteWorkouts(workouts: List<WorkoutEntity>)

    @Query("SELECT * FROM workout ORDER BY data ASC, treinoIndexNaSemana ASC")
    suspend fun getAll(): List<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<WorkoutEntity>)
}