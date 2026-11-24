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
    version = 12,
    exportSchema = true
)
abstract class AppDatabase : BaseAppDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "primeiro_5k.db"
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