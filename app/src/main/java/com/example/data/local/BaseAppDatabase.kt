package com.example.data.local

import androidx.room.RoomDatabase
import com.example.data.local.dao.ActiveRunDao
import com.example.data.local.dao.AutoAdjustmentDao
import com.example.data.local.dao.CheckinDao
import com.example.data.local.dao.PlanAdjustmentDao
import com.example.data.local.dao.PlanDao
import com.example.data.local.dao.RoutePointDao
import com.example.data.local.dao.RunnerProfileDao
import com.example.data.local.dao.WorkoutDao
import com.example.data.local.dao.WorkoutLogDao

/**
 * Classe base abstrata compartilhada por todos os bancos de dados por modalidade.
 * Contém os DAOs abstratos que cada sub-banco deve implementar.
 */
abstract class BaseAppDatabase : RoomDatabase() {
    abstract fun runnerProfileDao(): RunnerProfileDao
    abstract fun planDao(): PlanDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun routePointDao(): RoutePointDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun activeRunDao(): ActiveRunDao
    abstract fun planAdjustmentDao(): PlanAdjustmentDao
    abstract fun checkinDao(): CheckinDao
    abstract fun autoAdjustmentDao(): AutoAdjustmentDao

    /**
     * Limpa todos os dados do banco (todas as tabelas).
     * Usado para resetar a modalidade atual.
     */
    open suspend fun clearAllData() {
        workoutDao().clearAll()
        workoutLogDao().clearAllLogs()
        routePointDao().clearAllRoutePoints()
        planAdjustmentDao().clearAll()
        autoAdjustmentDao().clearAll()
        checkinDao().clearAll()
        activeRunDao().clearActiveRun()
        planDao().clearAllPlans()
        runnerProfileDao().clearProfile()
    }
}