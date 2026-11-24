package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.PlanAdjustmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanAdjustmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdjustment(adjustment: PlanAdjustmentEntity): Long

    @Query("SELECT * FROM plan_adjustment WHERE planId = :planId ORDER BY criadoEm DESC")
    fun getAdjustmentsForPlan(planId: Long): Flow<List<PlanAdjustmentEntity>>

    @Query("SELECT * FROM plan_adjustment WHERE planId = :planId AND inicio <= :date AND fim >= :date ORDER BY criadoEm DESC LIMIT 1")
    fun getActiveAdjustmentForDate(planId: Long, date: String): Flow<PlanAdjustmentEntity?>

    @Query("DELETE FROM plan_adjustment WHERE planId = :planId")
    suspend fun deleteByPlan(planId: Long)

    @Query("DELETE FROM plan_adjustment")
    suspend fun clearAll()

    @Query("SELECT * FROM plan_adjustment ORDER BY criadoEm DESC")
    suspend fun getAll(): List<PlanAdjustmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(adjustments: List<PlanAdjustmentEntity>)
}
