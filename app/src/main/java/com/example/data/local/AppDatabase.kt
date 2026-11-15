package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.data.local.entities.CheckinEntity
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity

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