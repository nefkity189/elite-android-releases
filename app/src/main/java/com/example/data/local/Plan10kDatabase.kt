package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.data.local.entities.CheckinEntity
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity

/**
 * Banco de dados do plano 10K (primeiro_5k_10k.db).
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
    version = 10,
    exportSchema = true
)
abstract class Plan10kDatabase : BaseAppDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: Plan10kDatabase? = null

        fun getInstance(context: Context): Plan10kDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Plan10kDatabase::class.java,
                    "primeiro_5k_10k.db"
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