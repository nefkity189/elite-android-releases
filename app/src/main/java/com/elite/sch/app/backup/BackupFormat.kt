package com.elite.sch.app.backup

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
 * Contrato de versionamento para backups do Elite.
 * Qualquer versão futura do app deve manter compatibilidade com esta estrutura,
 * permitindo que backups criados em versões anteriores sejam restaurados.
 */
const val BACKUP_VERSION = "1.0"

/**
 * Dados exportados de um banco de dados por modalidade.
 * Contém todas as tabelas do banco em um formato serializável.
 */
data class ModalityBackup(
    val modality: String,
    val profile: RunnerProfileEntity? = null,
    val plans: List<PlanEntity> = emptyList(),
    val workouts: List<WorkoutEntity> = emptyList(),
    val workoutLogs: List<WorkoutLogEntity> = emptyList(),
    val routePoints: List<RoutePointEntity> = emptyList(),
    val checkins: List<CheckinEntity> = emptyList(),
    val planAdjustments: List<PlanAdjustmentEntity> = emptyList(),
    val autoAdjustments: List<AutoAdjustmentEntity> = emptyList(),
    val activeRun: ActiveRunEntity? = null
)

/**
 * Arquivo de backup completo do app.
 * Inclui metadados de versão e dados de todas as modalidades.
 */
data class AppBackup(
    val version: String = BACKUP_VERSION,
    val exportedAt: Long = System.currentTimeMillis(),
    val appVersion: String = "1.0.0",
    val modalities: List<ModalityBackup> = emptyList()
)

/**
 * Factory para criar backups a partir de dados do banco de dados.
 */
object BackupFactory {

    fun createBackup(
        modality: String,
        profile: RunnerProfileEntity?,
        plans: List<PlanEntity>,
        workouts: List<WorkoutEntity>,
        workoutLogs: List<WorkoutLogEntity>,
        routePoints: List<RoutePointEntity>,
        checkins: List<CheckinEntity>,
        planAdjustments: List<PlanAdjustmentEntity>,
        autoAdjustments: List<AutoAdjustmentEntity>,
        activeRun: ActiveRunEntity?
    ): ModalityBackup {
        return ModalityBackup(
            modality = modality,
            profile = profile,
            plans = plans,
            workouts = workouts,
            workoutLogs = workoutLogs,
            routePoints = routePoints,
            checkins = checkins,
            planAdjustments = planAdjustments,
            autoAdjustments = autoAdjustments,
            activeRun = activeRun
        )
    }
}