package com.example.generator

import com.example.data.local.WorkoutStepConverter
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.model.StepType
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.roundToInt

data class RegenerationResult(
    val toDelete: List<WorkoutEntity>,
    val toCreate: List<WorkoutEntity>,
    val updatedPlan: PlanEntity,
    val novoTotalSemanas: Int,
    val dataUltimoTreino: LocalDate
)

object PlanRegenerator {

    /**
     * Função pura que recalcula o plano de treino:
     * - Treinos concluídos ou pulados nunca mudam.
     * - Nada antes de 'today' é alterado (treinos passados sem status continuam "perdidos" e não são reagendados).
     * - Só os treinos a partir de 'today' que NÃO estão concluídos/pulados são recalculados.
     * - O novo plano retoma na etapa do último treino concluído (repete essa etapa antes de avançar;
     *   se não houver nenhum concluído, retoma na etapa inicial do plano).
     * - Distribui as semanas restantes até a nova data final, apenas nos dias escolhidos e sem dois dias seguidos.
     * - A data do último treino não passa da nova data final.
     * - Respeita o mínimo de semanas exigido pelo perfil (8 para "Nada", 6 nos demais) a partir do início do plano.
     * - Respeita o máximo de 16 semanas restantes a partir de hoje.
     */
    fun regenerate(
        plan: PlanEntity,
        currentWorkouts: List<WorkoutEntity>,
        today: LocalDate,
        newEndDate: LocalDate,
        diasSelecionados: List<DayOfWeek> = emptyList(),
        frequenciaExercicio: String = "Outros",
        etapaDeRetomada: Int? = null
    ): RegenerationResult {
        val planStartDate = LocalDate.parse(plan.dataInicio)
        val minTotalWeeks = PlanGenerator.getMinimumWeeks(frequenciaExercicio)

        // Duração mínima a partir da data de início do plano:
        val minEndDate = planStartDate.plusWeeks(minTotalWeeks.toLong()).minusDays(1)
        if (newEndDate.isBefore(minEndDate)) {
            throw IllegalArgumentException(
                "A data final deve respeitar a duração mínima de $minTotalWeeks semanas a partir do início do plano ($minEndDate)."
            )
        }

        // Máximo de 16 semanas restantes a partir de hoje
        val maxRemainingEndDate = today.plusWeeks(16)
        if (newEndDate.isAfter(maxRemainingEndDate)) {
            throw IllegalArgumentException(
                "A nova data final não pode ultrapassar o limite de 16 semanas restantes ($maxRemainingEndDate)."
            )
        }

        if (newEndDate.isBefore(today)) {
            throw IllegalArgumentException("A nova data final não pode ser anterior a hoje.")
        }

        // Determinar dias escolhidos
        val daysToUse = if (diasSelecionados.isNotEmpty()) {
            diasSelecionados.distinct().sorted()
        } else {
            currentWorkouts.map { LocalDate.parse(it.data).dayOfWeek }.distinct().sorted()
        }.ifEmpty {
            listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        }

        val preferredSet = daysToUse.toSet()
        val diasPorSemana = daysToUse.size

        // 1. Treinos passados (antes de hoje) NUNCA são alterados
        // 2. Treinos a partir de hoje que já foram concluídos, pulados ou pausados também NÃO são alterados
        val immutableWorkouts = currentWorkouts.filter { workout ->
            val workoutDate = LocalDate.parse(workout.data)
            workoutDate.isBefore(today) ||
                workout.status == WorkoutEntity.STATUS_CONCLUIDO ||
                workout.status == WorkoutEntity.STATUS_PULADO ||
                workout.status == WorkoutEntity.STATUS_PAUSADO
        }

        // toDelete são apenas os treinos a partir de hoje que ainda não foram feitos, pulados ou pausados
        val toDelete = currentWorkouts.filter { workout ->
            val workoutDate = LocalDate.parse(workout.data)
            !workoutDate.isBefore(today) &&
                workout.status != WorkoutEntity.STATUS_CONCLUIDO &&
                workout.status != WorkoutEntity.STATUS_PULADO &&
                workout.status != WorkoutEntity.STATUS_PAUSADO
        }

        // Identificar a etapa do último treino concluído (ou usar etapaDeRetomada se fornecida)
        val lastCompletedWorkout = currentWorkouts
            .filter { it.status == WorkoutEntity.STATUS_CONCLUIDO }
            .maxByOrNull { LocalDate.parse(it.data) }

        val resumeStage = etapaDeRetomada ?: lastCompletedWorkout?.etapaIndex ?: plan.etapaInicial

        // Descobrir a data e semana do último treino preservado
        val lastImmutableDate = immutableWorkouts.maxOfOrNull { LocalDate.parse(it.data) }
        val lastImmutableWeekIndex = immutableWorkouts.maxOfOrNull { it.semanaIndex } ?: 0

        // Geração das novas datas a partir de today até newEndDate (sem dias seguidos, apenas preferredSet)
        val newWorkoutDates = mutableListOf<LocalDate>()
        var candidate = today

        // Se yesterday teve um treino preservado, não pode agendar today
        if (lastImmutableDate != null && candidate.isBefore(lastImmutableDate.plusDays(2))) {
            candidate = lastImmutableDate.plusDays(2)
        }

        while (!candidate.isAfter(newEndDate)) {
            if (candidate.dayOfWeek in preferredSet) {
                newWorkoutDates.add(candidate)
                candidate = candidate.plusDays(2) // Pelo menos 1 dia de descanso
            } else {
                candidate = candidate.plusDays(1)
            }
        }

        // Agrupamento em semanas: se a última semana preservada está incompleta, completá-la primeiro
        val lastWeekImmutableWorkouts = immutableWorkouts.filter { it.semanaIndex == lastImmutableWeekIndex }
        val maxTreinoIndexInLastWeek = lastWeekImmutableWorkouts.maxOfOrNull { it.treinoIndexNaSemana } ?: 0
        val slotsNeededInLastWeek = if (lastImmutableWeekIndex > 0 && maxTreinoIndexInLastWeek < diasPorSemana) {
            diasPorSemana - maxTreinoIndexInLastWeek
        } else {
            0
        }

        data class WeekChunk(
            val weekNumber: Int,
            val dates: List<LocalDate>,
            val startTreinoIndex: Int
        )

        val weekChunks = mutableListOf<WeekChunk>()
        var dateIdx = 0

        if (slotsNeededInLastWeek > 0 && dateIdx < newWorkoutDates.size) {
            val endIdx = minOf(dateIdx + slotsNeededInLastWeek, newWorkoutDates.size)
            val chunkDates = newWorkoutDates.subList(dateIdx, endIdx)
            weekChunks.add(
                WeekChunk(
                    weekNumber = lastImmutableWeekIndex,
                    dates = chunkDates,
                    startTreinoIndex = maxTreinoIndexInLastWeek + 1
                )
            )
            dateIdx = endIdx
        }

        var nextWeekNum = if (lastImmutableWeekIndex > 0) lastImmutableWeekIndex + 1 else 1
        while (dateIdx < newWorkoutDates.size) {
            val endIdx = minOf(dateIdx + diasPorSemana, newWorkoutDates.size)
            val chunkDates = newWorkoutDates.subList(dateIdx, endIdx)
            weekChunks.add(
                WeekChunk(
                    weekNumber = nextWeekNum,
                    dates = chunkDates,
                    startTreinoIndex = 1
                )
            )
            nextWeekNum++
            dateIdx = endIdx
        }

        val remainingWeeksCount = weekChunks.size

        // Validação de segurança: avanço máximo de 2 etapas por semana
        // Mínimo de semanas restantes exigido: teto((9 - etapaDeRetomada) / 2) + 1
        val minRemainingWeeks = Math.ceil((9.0 - resumeStage).coerceAtLeast(0.0) / 2.0).toInt() + 1
        if (remainingWeeksCount < minRemainingWeeks) {
            throw IllegalArgumentException(
                "Prazo curto demais para a etapa atual: mínimo de $minRemainingWeeks semanas restantes"
            )
        }

        val toCreate = mutableListOf<WorkoutEntity>()
        var currentStage = resumeStage

        for (chunkIndex in 0 until weekChunks.size) {
            val chunk = weekChunks[chunkIndex]

            // Na primeira semana restante (chunkIndex == 0), repete resumeStage antes de avançar!
            val calculatedStage = if (remainingWeeksCount == 1) {
                resumeStage
            } else {
                val progressRatio = chunkIndex.toDouble() / (remainingWeeksCount - 1)
                val rawStage = resumeStage + (progressRatio * (9 - resumeStage)).roundToInt()
                rawStage.coerceIn(resumeStage, 9)
            }

            // Garante limite de avanço a no máximo 2 etapas por semana
            val stageIndex = if (chunkIndex == 0) {
                resumeStage
            } else {
                calculatedStage.coerceIn(currentStage, minOf(currentStage + 2, 9))
            }
            currentStage = stageIndex

            val stageProgression = ProgressionTable.getStage(stageIndex)
            val steps = ProgressionTable.buildStepsForStage(stageIndex)
            val totalDurationSeconds = steps.sumOf { it.durationSeconds }
            val totalRunSeconds = steps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
            val totalDurationMin = Math.round(totalDurationSeconds / 60.0).toInt()
            val totalRunMin = Math.round(totalRunSeconds / 60.0).toInt()
            val passosJson = WorkoutStepConverter.toJson(steps)

            for ((offset, wDate) in chunk.dates.withIndex()) {
                val workoutNumberInWeek = chunk.startTreinoIndex + offset
                val currentWeekNum = chunk.weekNumber

                toCreate.add(
                    WorkoutEntity(
                        planId = plan.id,
                        semanaIndex = currentWeekNum,
                        treinoIndexNaSemana = workoutNumberInWeek,
                        etapaIndex = stageIndex,
                        data = wDate.toString(),
                        titulo = "Semana $currentWeekNum • Treino $workoutNumberInWeek",
                        resumoEtapa = stageProgression.summary,
                        passosJson = passosJson,
                        duracaoMinutos = totalDurationMin,
                        duracaoCorridaMinutos = totalRunMin,
                        status = WorkoutEntity.STATUS_PLANEJADO,
                        concluidoEm = null
                    )
                )
            }
        }

        val allWorkouts = immutableWorkouts + toCreate
        val totalSemanas = allWorkouts.maxOfOrNull { it.semanaIndex } ?: minTotalWeeks
        val finalLastWorkoutDate = allWorkouts.maxOfOrNull { LocalDate.parse(it.data) } ?: newEndDate

        val updatedPlan = plan.copy(
            dataFim = finalLastWorkoutDate.toString(),
            totalSemanas = maxOf(totalSemanas, minTotalWeeks)
        )

        return RegenerationResult(
            toDelete = toDelete,
            toCreate = toCreate,
            updatedPlan = updatedPlan,
            novoTotalSemanas = updatedPlan.totalSemanas,
            dataUltimoTreino = finalLastWorkoutDate
        )
    }
}
