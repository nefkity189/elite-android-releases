package com.elite.sch.app.generator

import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PausePreviewData(
    val recuo: Int,
    val etapaDeRetomada: Int,
    val treinosAfetadosCount: Int,
    val novaDataUltimoTreino: LocalDate,
    val foiEstendido: Boolean,
    val dataFimEstendida: LocalDate?,
    val regenerationResult: RegenerationResult,
    val updatedWorkoutsWithPause: List<WorkoutEntity>
)

object PauseApplier {

    /**
     * Calcula o recuo (setback) em etapas conforme duração do período e tipo de retorno:
     * - Duração até 3 dias: 0 etapas
     * - Duração de 4 a 7 dias: 1 etapa
     * - Duração de 8 ou mais dias: 2 etapas
     *
     * Modificadores por retorno:
     * - "normal": recuo base
     * - "gradual": recuo base + 1 (mínimo etapa 1 ao aplicar no cálculo da etapa)
     * - "rapido": recuo base - 1 (mínimo 0 de recuo)
     */
    fun calculateRecuo(
        periodDays: Long,
        retorno: String
    ): Int {
        val baseRecuo = when {
            periodDays <= 3L -> 0
            periodDays in 4L..7L -> 1
            else -> 2
        }
        return when (retorno) {
            "gradual" -> baseRecuo + 1
            "rapido" -> (baseRecuo - 1).coerceAtLeast(0)
            else -> baseRecuo
        }
    }

    /**
     * Calcula a etapa de retomada garantindo que seja no mínimo 1 e no máximo 9:
     */
    fun calculateEtapaDeRetomada(
        lastCompletedStage: Int,
        recuo: Int
    ): Int {
        return (lastCompletedStage - recuo).coerceIn(1, 9)
    }

    /**
     * Função pura que altera os treinos no período da pausa:
     * - Concluídos e pulados nunca mudam.
     * - Treinos passados sem status dentro do período viram "pausado".
     * - Treinos planejados a partir de hoje:
     *   - "sem_treino": viram "pausado"
     *   - "caminhada": substituídos por caminhada leve de 20 min com um passo só
     *   - "reduzido": usam a etapa anterior à do treino (mínimo etapa 1)
     */
    fun applyPause(
        workouts: List<WorkoutEntity>,
        startDate: LocalDate,
        endDate: LocalDate,
        nivel: String,
        today: LocalDate
    ): List<WorkoutEntity> {
        val walkStep = listOf(
            WorkoutStep(
                type = StepType.WALK,
                durationSeconds = 20 * 60,
                instruction = "Caminhada leve"
            )
        )
        val walkJson = WorkoutStepConverter.toJson(walkStep)

        return workouts.map { workout ->
            val workoutDate = LocalDate.parse(workout.data)
            val isInPeriod = !workoutDate.isBefore(startDate) && !workoutDate.isAfter(endDate)

            if (!isInPeriod) {
                workout
            } else {
                if (workout.status == WorkoutEntity.STATUS_CONCLUIDO || workout.status == WorkoutEntity.STATUS_PULADO) {
                    workout
                } else {
                    val isPast = workoutDate.isBefore(today)

                    when (nivel) {
                        "sem_treino" -> {
                            workout.copy(status = WorkoutEntity.STATUS_PAUSADO)
                        }
                        "caminhada" -> {
                            if (isPast) {
                                workout.copy(status = WorkoutEntity.STATUS_PAUSADO)
                            } else {
                                workout.copy(
                                    duracaoMinutos = 20,
                                    duracaoCorridaMinutos = 0,
                                    resumoEtapa = "Caminhada leve de 20 min",
                                    passosJson = walkJson,
                                    status = WorkoutEntity.STATUS_PLANEJADO
                                )
                            }
                        }
                        "reduzido" -> {
                            if (isPast) {
                                workout.copy(status = WorkoutEntity.STATUS_PAUSADO)
                            } else {
                                val newStage = (workout.etapaIndex - 1).coerceAtLeast(1)
                                val stageProgression = ProgressionTable.getStage(newStage)
                                val steps = ProgressionTable.buildStepsForStage(newStage)
                                val totalSeconds = steps.sumOf { it.durationSeconds }
                                val totalRunSeconds = steps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }

                                workout.copy(
                                    etapaIndex = newStage,
                                    resumoEtapa = stageProgression.summary,
                                    passosJson = WorkoutStepConverter.toJson(steps),
                                    duracaoMinutos = Math.round(totalSeconds / 60.0).toInt(),
                                    duracaoCorridaMinutos = Math.round(totalRunSeconds / 60.0).toInt(),
                                    status = WorkoutEntity.STATUS_PLANEJADO
                                )
                            }
                        }
                        else -> {
                            workout.copy(status = WorkoutEntity.STATUS_PAUSADO)
                        }
                    }
                }
            }
        }
    }

    /**
     * Calcula a prévia completa da pausa/férias:
     * - Aplica a pausa aos treinos.
     * - Determina recuo e etapa de retomada.
     * - Se o prazo até a data final atual for curto demais para a etapa de retomada,
     *   estende automaticamente a data final até o mínimo necessário.
     * - Regenera o plano a partir de endDate + 1 dia.
     */
    fun previewPause(
        plan: PlanEntity,
        workouts: List<WorkoutEntity>,
        startDate: LocalDate,
        endDate: LocalDate,
        nivel: String,
        retorno: String,
        today: LocalDate,
        diasSelecionados: List<DayOfWeek> = emptyList(),
        frequenciaExercicio: String = "Outros"
    ): PausePreviewData {
        val periodDays = ChronoUnit.DAYS.between(startDate, endDate) + 1
        val recuo = calculateRecuo(periodDays, retorno)

        // Etapa do último treino concluído (ou inicial se nenhum concluído)
        val lastCompleted = workouts
            .filter { it.status == WorkoutEntity.STATUS_CONCLUIDO }
            .maxByOrNull { LocalDate.parse(it.data) }
        val baseStage = lastCompleted?.etapaIndex ?: plan.etapaInicial
        val etapaDeRetomada = calculateEtapaDeRetomada(baseStage, recuo)

        // Aplica a pausa
        val updatedWorkouts = applyPause(workouts, startDate, endDate, nivel, today)

        val treinosAfetadosCount = workouts.count { workout ->
            val d = LocalDate.parse(workout.data)
            !d.isBefore(startDate) && !d.isAfter(endDate) &&
                workout.status != WorkoutEntity.STATUS_CONCLUIDO &&
                workout.status != WorkoutEntity.STATUS_PULADO
        }

        val regenerateFrom = endDate.plusDays(1)
        val currentEndDate = LocalDate.parse(plan.dataFim)

        // Semanas mínimas necessárias para a etapa de retomada (teto((9 - etapa) / 2) + 1)
        val minRemainingWeeks = Math.ceil((9.0 - etapaDeRetomada).coerceAtLeast(0.0) / 2.0).toInt() + 1
        val minRequiredEndDate = regenerateFrom.plusWeeks(minRemainingWeeks.toLong()).minusDays(1)

        var candidateEndDate = if (currentEndDate.isBefore(minRequiredEndDate)) {
            minRequiredEndDate
        } else {
            currentEndDate
        }

        var foiEstendido = candidateEndDate.isAfter(currentEndDate)
        var regenResult: RegenerationResult? = null

        // Tenta regenerar, estendendo dia a dia caso necessário para encontrar a data mínima válida
        var attempts = 0
        while (attempts < 60) {
            try {
                regenResult = PlanRegenerator.regenerate(
                    plan = plan,
                    currentWorkouts = updatedWorkouts,
                    today = regenerateFrom,
                    newEndDate = candidateEndDate,
                    diasSelecionados = diasSelecionados,
                    frequenciaExercicio = frequenciaExercicio,
                    etapaDeRetomada = etapaDeRetomada
                )
                break
            } catch (e: IllegalArgumentException) {
                if (e.message?.contains("Prazo curto demais") == true || e.message?.contains("duração mínima") == true) {
                    candidateEndDate = candidateEndDate.plusDays(1)
                    foiEstendido = true
                    attempts++
                } else {
                    throw e
                }
            }
        }

        if (regenResult == null) {
            throw IllegalStateException("Não foi possível encontrar uma data final segura para a retomada.")
        }

        return PausePreviewData(
            recuo = recuo,
            etapaDeRetomada = etapaDeRetomada,
            treinosAfetadosCount = treinosAfetadosCount,
            novaDataUltimoTreino = regenResult.dataUltimoTreino,
            foiEstendido = foiEstendido,
            dataFimEstendida = if (foiEstendido) candidateEndDate else null,
            regenerationResult = regenResult,
            updatedWorkoutsWithPause = updatedWorkouts
        )
    }
}
