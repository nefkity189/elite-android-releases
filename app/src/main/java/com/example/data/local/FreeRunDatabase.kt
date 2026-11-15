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
 * Banco de dados da Corrida Livre (primeiro_5k_livre.db).
 * Contém apenas o perfil do usuário e os logs de corridas livres.
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
abstract class FreeRunDatabase : BaseAppDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: FreeRunDatabase? = null

        fun getInstance(context: Context): FreeRunDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FreeRunDatabase::class.java,
                    "primeiro_5k_livre.db"
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