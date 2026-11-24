package com.example.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migrações compartilhadas por todos os bancos de dados por modalidade.
 * Mantém a mesma história de versões do banco original (v10).
 */
object BaseMigrations {
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE workout_log ADD COLUMN anotacoes TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN vozAtiva INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN vibracaoAtiva INTEGER NOT NULL DEFAULT 1")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN lembretesAtivos INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN lembreteHora INTEGER NOT NULL DEFAULT 7")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN lembreteMinuto INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `plan_adjustment` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `planId` INTEGER NOT NULL,
                    `tipo` TEXT NOT NULL,
                    `inicio` TEXT NOT NULL,
                    `fim` TEXT NOT NULL,
                    `nivel` TEXT NOT NULL,
                    `retorno` TEXT NOT NULL,
                    `criadoEm` INTEGER NOT NULL
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_plan_adjustment_planId` ON `plan_adjustment` (`planId`)")
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRecebidas INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsAceitas INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRejPrecisao INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRejAnteriores INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRejVelocidade INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRejDeslocamento INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsRejInicio INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE workout_log ADD COLUMN gpsMaiorIntervaloMs INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `checkin` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `workoutId` INTEGER,
                    `momento` TEXT NOT NULL,
                    `energia` INTEGER,
                    `sono` INTEGER,
                    `dor` INTEGER NOT NULL,
                    `regiaoDor` TEXT,
                    `criadoEm` INTEGER NOT NULL
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_checkin_workoutId` ON `checkin` (`workoutId`)")
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `auto_adjustment` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `planId` INTEGER NOT NULL,
                    `workoutOrigemId` INTEGER NOT NULL,
                    `regra` TEXT NOT NULL,
                    `antesJson` TEXT NOT NULL,
                    `depoisJson` TEXT NOT NULL,
                    `ativo` INTEGER NOT NULL,
                    `deslocamentoEtapa` INTEGER NOT NULL,
                    `criadoEm` INTEGER NOT NULL
                )
            """.trimIndent())
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_auto_adjustment_planId` ON `auto_adjustment` (`planId`)")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN ajustesAutomaticos INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN modoAjuste TEXT NOT NULL DEFAULT 'cauteloso'")
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE plan ADD COLUMN modalidade TEXT NOT NULL DEFAULT 'plano_5k'")
            db.execSQL("ALTER TABLE plan ADD COLUMN distanciaAlvoM INTEGER NOT NULL DEFAULT 5000")
            db.execSQL("ALTER TABLE workout ADD COLUMN distanciaAlvoM INTEGER DEFAULT NULL")
            db.execSQL("ALTER TABLE workout ADD COLUMN modoConclusao TEXT NOT NULL DEFAULT 'tempo'")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN ritmoAtualSegKm INTEGER DEFAULT NULL")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN ritmoOrigem TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN alertasRitmoAtivos INTEGER NOT NULL DEFAULT 1")
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN avisoDistanciaAtivo INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN avisoDistanciaMetros INTEGER NOT NULL DEFAULT 1000")
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN showMapDuringRun INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE runner_profile ADD COLUMN defaultCameraMode TEXT NOT NULL DEFAULT 'follow_user'")
        }
    }

    fun all(): Array<Migration> = arrayOf(
        MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6,
        MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9,
        MIGRATION_9_10, MIGRATION_10_11
    )
}