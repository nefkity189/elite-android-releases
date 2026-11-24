package com.elite.sch.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.elite.sch.app.data.local.entities.PlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Query("SELECT * FROM plan WHERE status = :status AND modalidade = :modalidade ORDER BY id DESC LIMIT 1")
    fun getActivePlan(status: String = PlanEntity.STATUS_ATIVO, modalidade: String): Flow<PlanEntity?>

    @Query("SELECT * FROM plan WHERE modalidade = :modalidade LIMIT 1")
    fun getFreeRunPlan(modalidade: String = PlanEntity.MODALIDADE_LIVRE): Flow<PlanEntity?>

    @Query("SELECT * FROM plan WHERE id = :planId")
    suspend fun getPlanById(planId: Long): PlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: PlanEntity): Long

    @Update
    suspend fun updatePlan(plan: PlanEntity)

    @Query("UPDATE plan SET status = :newStatus WHERE status = :oldStatus")
    suspend fun archiveActivePlans(
        oldStatus: String = PlanEntity.STATUS_ATIVO,
        newStatus: String = PlanEntity.STATUS_CANCELADO
    )

    @Query("DELETE FROM plan WHERE id = :planId")
    suspend fun deletePlan(planId: Long)

    @Query("DELETE FROM plan")
    suspend fun clearAllPlans()

    @Query("SELECT * FROM plan ORDER BY id DESC")
    suspend fun getAll(): List<PlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plans: List<PlanEntity>)
}