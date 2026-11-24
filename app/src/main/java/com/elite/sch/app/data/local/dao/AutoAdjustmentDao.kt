package com.elite.sch.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elite.sch.app.data.local.entities.AutoAdjustmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoAdjustmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(adjustment: AutoAdjustmentEntity): Long

    @Query("SELECT * FROM auto_adjustment WHERE planId = :planId ORDER BY criadoEm DESC")
    fun observeForPlan(planId: Long): Flow<List<AutoAdjustmentEntity>>

    @Query("SELECT * FROM auto_adjustment WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AutoAdjustmentEntity?

    @Query("UPDATE auto_adjustment SET ativo = :ativo WHERE id = :id")
    suspend fun setAtivo(id: Long, ativo: Boolean)

    @Query("SELECT COALESCE(SUM(deslocamentoEtapa), 0) FROM auto_adjustment WHERE planId = :planId AND ativo = 1")
    suspend fun sumDeslocamentoAtivo(planId: Long): Int

    @Query("SELECT criadoEm FROM auto_adjustment WHERE planId = :planId AND regra = 'AVANCAR' AND ativo = 1 ORDER BY criadoEm DESC LIMIT 1")
    suspend fun lastAdvanceAt(planId: Long): Long?

    @Query("DELETE FROM auto_adjustment WHERE planId = :planId")
    suspend fun deleteByPlan(planId: Long)

    @Query("DELETE FROM auto_adjustment")
    suspend fun clearAll()

    @Query("SELECT * FROM auto_adjustment ORDER BY criadoEm DESC")
    suspend fun getAll(): List<AutoAdjustmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(adjustments: List<AutoAdjustmentEntity>)
}
