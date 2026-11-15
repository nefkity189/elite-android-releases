package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.RoutePointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutePointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoint(point: RoutePointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoints(points: List<RoutePointEntity>)

    @Query("SELECT * FROM route_point WHERE workoutId = :workoutId ORDER BY timestampMillis ASC")
    fun getPointsForWorkout(workoutId: Long): Flow<List<RoutePointEntity>>

    @Query("SELECT * FROM route_point WHERE workoutId = :workoutId ORDER BY timestampMillis ASC")
    suspend fun getPointsListForWorkout(workoutId: Long): List<RoutePointEntity>

    @Query("DELETE FROM route_point WHERE workoutId = :workoutId")
    suspend fun deletePointsForWorkout(workoutId: Long)

    @Query("DELETE FROM route_point WHERE workoutId IN (SELECT id FROM workout WHERE planId = :planId)")
    suspend fun deleteByPlan(planId: Long)

    @Query("DELETE FROM route_point WHERE workoutId IN (:workoutIds)")
    suspend fun deleteByWorkoutIds(workoutIds: List<Long>)

    @Query("DELETE FROM route_point")
    suspend fun clearAllRoutePoints()

    @Query("SELECT * FROM route_point ORDER BY timestampMillis ASC")
    suspend fun getAll(): List<RoutePointEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<RoutePointEntity>)
}
