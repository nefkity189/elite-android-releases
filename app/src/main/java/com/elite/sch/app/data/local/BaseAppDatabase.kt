package com.elite.sch.app.data.local

import androidx.room.RoomDatabase
import com.elite.sch.app.data.local.dao.ActiveRunDao
import com.elite.sch.app.data.local.dao.AutoAdjustmentDao
import com.elite.sch.app.data.local.dao.CheckinDao
import com.elite.sch.app.data.local.dao.PlanAdjustmentDao
import com.elite.sch.app.data.local.dao.PlanDao
import com.elite.sch.app.data.local.dao.RoutePointDao
import com.elite.sch.app.data.local.dao.RunnerProfileDao
import com.elite.sch.app.data.local.dao.WorkoutDao
import com.elite.sch.app.data.local.dao.WorkoutLogDao

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