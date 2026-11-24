package com.elite.sch.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elite.sch.app.data.local.entities.ActiveRunEntity
import com.elite.sch.app.data.local.entities.AutoAdjustmentEntity
import com.elite.sch.app.data.local.entities.CheckinEntity
import com.elite.sch.app.data.local.entities.PlanAdjustmentEntity
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.data.local.entities.RunnerProfileEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.data.local.entities.WorkoutLogEntity

/**
 * Banco de dados do plano 5K clássico (primeiro_5k_5k.db).
 * Mantém a versão e migrações do banco original para compatibilidade.
 */
@Database(
    entities = [
        RunnerProfileEntity::class,
        PlanEntity::class,
        WorkoutEntity::class,
        RoutePointEntity::class,
        WorkoutLogEntity::class,
        ActiveRunEntity::class,
        PlanAdjustmentEntity::class,
        CheckinEntity::class,
        AutoAdjustmentEntity::class
    ],
    version = 11,
    exportSchema = true
)
abstract class Plan5kDatabase : BaseAppDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: Plan5kDatabase? = null

        fun getInstance(context: Context): Plan5kDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Plan5kDatabase::class.java,
                    "primeiro_5k_5k.db"
                )
                    .addMigrations(*BaseMigrations.all())
                    .fallbackToDestructiveMigrationFrom(true, 1, 2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}