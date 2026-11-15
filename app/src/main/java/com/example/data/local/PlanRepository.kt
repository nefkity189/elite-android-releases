package com.example.data.local

import com.example.data.local.dao.ActiveRunDao
import com.example.data.local.dao.AutoAdjustmentDao
import com.example.data.local.dao.CheckinDao
import com.example.data.local.dao.PlanAdjustmentDao
import com.example.data.local.dao.PlanDao
import com.example.data.local.dao.RoutePointDao
import com.example.data.local.dao.RunnerProfileDao
import com.example.data.local.dao.WorkoutDao
import com.example.data.local.dao.WorkoutLogDao
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.data.local.entities.CheckinEntity
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.RoutePointEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.WorkoutLogEntity
import com.example.generator.AdaptiveEngine
import com.example.generator.AdaptiveResult
import com.example.generator.GeneratedPlan
import com.example.generator.PlanGenerator
import com.example.generator.PlanInput
import com.example.generator.PlanRegenerator
import com.example.generator.RegenerationResult
import com.example.generator.TrainingFeedback
import com.example.generator.WorkoutChange
import com.example.generator.PausePreviewData
import com.example.generator.PauseApplier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

import androidx.room.withTransaction

class PlanRepository(
    private val database: BaseAppDatabase,
    private val modalidade: String,
    private val profileDao: RunnerProfileDao,
    private val planDao: PlanDao,
    private val workoutDao: WorkoutDao,
    private val routePointDao: RoutePointDao,
    private val workoutLogDao: WorkoutLogDao,
    private val activeRunDao: ActiveRunDao,
    private val planAdjustmentDao: PlanAdjustmentDao = database.planAdjustmentDao(),
    private val checkinDao: CheckinDao = database.checkinDao(),
    private val autoAdjustmentDao: AutoAdjustmentDao = database.autoAdjustmentDao()
) {
    val activePlan: Flow<PlanEntity?> = planDao.getActivePlan(modalidade = modalidade)
    val runnerProfile: Flow<RunnerProfileEntity?> = profileDao.getProfile()
    val activeRunFlow: Flow<ActiveRunEntity?> = activeRunDao.getActiveRunFlow()

    /**
     * Consulta o plano ativo de uma modalidade específica (usado ao trocar de plano
     * sem depender do repositório atual, que é acoplado à modalidade ativa).
     */
    fun getActivePlanForModality(modality: String): Flow<PlanEntity?> =
        planDao.getActivePlan(modalidade = modality)

    suspend fun updateNome(nome: String) {
        profileDao.updateNome(nome)
    }

    suspend fun updateDismissBatteryWarning(dismiss: Boolean) {
        profileDao.updateDismissBatteryWarning(dismiss)
    }

    fun getWorkouts(planId: Long): Flow<List<WorkoutEntity>> {
        return workoutDao.getWorkoutsForPlan(planId)
    }

    fun getWorkoutForDate(planId: Long, date: LocalDate): Flow<WorkoutEntity?> {
        return workoutDao.getWorkoutForDate(planId, date.toString())
    }

    fun getNextUpcomingWorkout(planId: Long, afterDate: LocalDate): Flow<WorkoutEntity?> {
        return workoutDao.getNextUpcomingWorkout(planId, afterDate.toString())
    }

    suspend fun getActiveRun(): ActiveRunEntity? {
        return activeRunDao.getActiveRun()
    }

    suspend fun saveActiveRun(run: ActiveRunEntity) {
        activeRunDao.saveActiveRun(run)
    }

    suspend fun clearActiveRun() {
        activeRunDao.clearActiveRun()
    }

    suspend fun insertRoutePoint(point: RoutePointEntity) {
        routePointDao.insertPoint(point)
    }

    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity? {
        return workoutDao.getWorkoutById(workoutId)
    }

    /**
     * Obtém ou cria o plano "livre" (modalidade 'livre').
     * Um único plano contêiner para todas as corridas livres do usuário.
     */
    suspend fun getOrCreateFreeRunPlan(): PlanEntity {
        val existing = planDao.getFreeRunPlan().first()
        if (existing != null) return existing
        return database.withTransaction {
            val newPlan = PlanEntity(
                status = PlanEntity.STATUS_ATIVO,
                dataInicio = LocalDate.now().toString(),
                dataFim = LocalDate.now().plusYears(1).toString(),
                totalSemanas = 52,
                diasPorSemana = 0,
                etapaInicial = 0,
                termoAceito = true,
                criadoEm = System.currentTimeMillis(),
                modalidade = PlanEntity.MODALIDADE_LIVRE,
                distanciaAlvoM = 0,
                acceptedAt = System.currentTimeMillis(),
                acceptedTermsVersion = "1.0"
            )
            val planId = planDao.insertPlan(newPlan)
            planDao.getPlanById(planId)!!
        }
    }

    /**
     * Cria um treino de corrida livre para hoje associado ao plano livre.
     * Retorna o ID do treino criado.
     */
    suspend fun createFreeRunWorkout(): Long {
        val plan = getOrCreateFreeRunPlan()
        val today = LocalDate.now().toString()
        val existing = workoutDao.getWorkoutForPlanAndDate(plan.id, today)
        if (existing != null && existing.status == WorkoutEntity.STATUS_PLANEJADO) return existing.id

        // Cria um treino "livre" sem passos pré-definidos
        val workout = WorkoutEntity(
            planId = plan.id,
            semanaIndex = 0,
            treinoIndexNaSemana = 0,
            etapaIndex = 0,
            data = today,
            titulo = "Corrida Livre",
            resumoEtapa = "Sem passos definidos",
            passosJson = "[]",
            duracaoMinutos = 0,
            duracaoCorridaMinutos = 0,
            status = WorkoutEntity.STATUS_PLANEJADO,
            concluidoEm = null,
            distanciaAlvoM = null,
            modoConclusao = WorkoutEntity.MODO_CONCLUSAO_TEMPO
        )
        return workoutDao.insertWorkout(workout)
    }

    suspend fun saveProfileAndGeneratePlan(
        profile: RunnerProfileEntity,
        generatedPlan: GeneratedPlan
    ): Long {
        // Arquiva qualquer plano anterior
        planDao.archiveActivePlans()

        // Preserva lembretes caso o perfil existente já os possua ativos e o novo não tenha configurado
        val existingProfile = profileDao.getProfile().firstOrNull()
        val profileWithReminders = if (existingProfile != null && !profile.lembretesAtivos && existingProfile.lembretesAtivos) {
            profile.copy(
                lembretesAtivos = existingProfile.lembretesAtivos,
                lembreteHora = existingProfile.lembreteHora,
                lembreteMinuto = existingProfile.lembreteMinuto
            )
        } else {
            profile
        }

        // Preserva as preferências de ajuste automático
        val finalProfile = if (existingProfile != null) {
            profileWithReminders.copy(
                ajustesAutomaticos = existingProfile.ajustesAutomaticos,
                modoAjuste = existingProfile.modoAjuste
            )
        } else {
            profileWithReminders
        }

        // Salva perfil
        profileDao.insertProfile(finalProfile)

        // Salva novo plano como DRAFT (aguardando aceite)
        val distanciaAlvoM = when (generatedPlan.modalidade) {
            "plano_2_5k" -> 2500
            "plano_10k" -> 10000
            else -> 5000 // plano_5k or default
        }
        val planEntity = PlanEntity(
            status = PlanEntity.STATUS_DRAFT,
            dataInicio = generatedPlan.dataInicio.toString(),
            dataFim = generatedPlan.dataFim.toString(),
            totalSemanas = generatedPlan.totalSemanas,
            diasPorSemana = generatedPlan.diasPorSemana,
            etapaInicial = generatedPlan.etapaInicial,
            termoAceito = false,
            criadoEm = System.currentTimeMillis(),
            modalidade = generatedPlan.modalidade,
            distanciaAlvoM = distanciaAlvoM,
            acceptedAt = 0,
            acceptedTermsVersion = ""
        )
        val planId = planDao.insertPlan(planEntity)

        // Converte e insere todos os treinos gerados
        val workoutEntities = generatedPlan.treinos.map { treino ->
            WorkoutEntity(
                planId = planId,
                semanaIndex = treino.semanaIndex,
                treinoIndexNaSemana = treino.treinoIndexNaSemana,
                etapaIndex = treino.etapaIndex,
                data = treino.data.toString(),
                titulo = treino.titulo,
                resumoEtapa = treino.resumoEtapa,
                passosJson = WorkoutStepConverter.toJson(treino.steps),
                duracaoMinutos = treino.duracaoTotalMinutos,
                duracaoCorridaMinutos = treino.duracaoCorridaMinutos,
                status = WorkoutEntity.STATUS_PLANEJADO,
                concluidoEm = null
            )
        }
        workoutDao.insertWorkouts(workoutEntities)
        return planId
    }

    /**
     * Marca um plano como aceito e ativo (chamado após aceite explícito do usuário).
     * Persiste acceptedAt e acceptedTermsVersion em transação.
     */
    suspend fun acceptPlan(planId: Long, termsVersion: String = "1.0"): Boolean {
        return database.withTransaction {
            val plan = planDao.getPlanById(planId)
            if (plan == null || plan.status != PlanEntity.STATUS_DRAFT) {
                return@withTransaction false
            }
            val acceptedAt = System.currentTimeMillis()
            val updatedPlan = plan.copy(
                status = PlanEntity.STATUS_ATIVO,
                termoAceito = true,
                acceptedAt = acceptedAt,
                acceptedTermsVersion = termsVersion
            )
            planDao.updatePlan(updatedPlan)
            true
        }
    }

    suspend fun markWorkoutCompleted(workoutId: Long) {
        workoutDao.updateWorkoutStatus(
            workoutId = workoutId,
            status = WorkoutEntity.STATUS_CONCLUIDO,
            concluidoEm = System.currentTimeMillis()
        )
    }

    suspend fun finishRunWithLog(
        workoutId: Long,
        duracaoSegundos: Long,
        distanciaMetros: Double,
        ritmoMedioSegundos: Double?,
        esforcoPercebido: Int,
        anotacoes: String? = null,
        diagnostics: com.example.tracker.GpsDiagnostics? = null
    ): Long {
        val id = workoutLogDao.insertLog(
            WorkoutLogEntity(
                workoutId = workoutId,
                duracaoSegundos = duracaoSegundos,
                distanciaMetros = distanciaMetros,
                ritmoMedioSegundos = ritmoMedioSegundos,
                esforcoPercebido = esforcoPercebido,
                anotacoes = anotacoes,
                finalizadoEm = System.currentTimeMillis(),
                gpsRecebidas = diagnostics?.received ?: 0,
                gpsAceitas = diagnostics?.accepted ?: 0,
                gpsRejPrecisao = diagnostics?.rejectedAccuracy ?: 0,
                gpsRejAnteriores = diagnostics?.rejectedBeforeStart ?: 0,
                gpsRejVelocidade = diagnostics?.rejectedSpeed ?: 0,
                gpsRejDeslocamento = diagnostics?.rejectedDisplacement ?: 0,
                gpsRejInicio = diagnostics?.rejectedWarmup ?: 0,
                gpsMaiorIntervaloMs = diagnostics?.maxGapMillis ?: 0L
            )
        )
        workoutDao.updateWorkoutStatus(
            workoutId = workoutId,
            status = WorkoutEntity.STATUS_CONCLUIDO,
            concluidoEm = System.currentTimeMillis()
        )
        activeRunDao.clearActiveRun()
        return id
    }

    val allWorkoutLogs: Flow<List<WorkoutLogEntity>> = workoutLogDao.getAllLogs()

    suspend fun updateVozAtiva(vozAtiva: Boolean) {
        profileDao.updateVozAtiva(vozAtiva)
    }

    suspend fun updateVibracaoAtiva(vibracaoAtiva: Boolean) {
        profileDao.updateVibracaoAtiva(vibracaoAtiva)
    }

    suspend fun updateLembretes(ativos: Boolean, hora: Int, minuto: Int) {
        profileDao.updateLembretes(ativos, hora, minuto)
    }

    suspend fun updateLembretesAtivos(ativos: Boolean) {
        profileDao.updateLembretesAtivos(ativos)
    }

    suspend fun updateWorkoutLogAnotacoes(logId: Long, anotacoes: String?) {
        workoutLogDao.updateAnotacoes(logId, anotacoes)
    }

    suspend fun getRoutePoints(workoutId: Long): List<RoutePointEntity> {
        return routePointDao.getPointsListForWorkout(workoutId)
    }

    suspend fun markWorkoutSkipped(workoutId: Long) {
        workoutDao.updateWorkoutStatus(
            workoutId = workoutId,
            status = WorkoutEntity.STATUS_PULADO,
            concluidoEm = null
        )
    }

    suspend fun resetWorkoutStatus(workoutId: Long) {
        workoutDao.updateWorkoutStatus(
            workoutId = workoutId,
            status = WorkoutEntity.STATUS_PLANEJADO,
            concluidoEm = null
        )
    }

    fun getActiveAdjustment(planId: Long, date: LocalDate): Flow<PlanAdjustmentEntity?> {
        return planAdjustmentDao.getActiveAdjustmentForDate(planId, date.toString())
    }

    suspend fun applyPauseAdjustment(
        adjustment: PlanAdjustmentEntity,
        updatedWorkoutsWithPause: List<WorkoutEntity>,
        regenerationResult: RegenerationResult
    ) {
        database.withTransaction {
            planAdjustmentDao.insertAdjustment(adjustment)
            workoutDao.insertWorkouts(updatedWorkoutsWithPause)
            if (regenerationResult.toDelete.isNotEmpty()) {
                workoutDao.deleteWorkouts(regenerationResult.toDelete)
            }
            if (regenerationResult.toCreate.isNotEmpty()) {
                workoutDao.insertWorkouts(regenerationResult.toCreate)
            }
            planDao.insertPlan(regenerationResult.updatedPlan)
        }
    }

    suspend fun deleteActivePlan(planId: Long) {
        if (planId <= 0L) return
        try {
            database.withTransaction {
                // Preserva o histórico de corridas: desvincula treinos concluídos (planId = 0)
                workoutDao.unlinkCompletedWorkoutsFromPlan(planId)
                workoutDao.deleteByPlan(planId)
                planAdjustmentDao.deleteByPlan(planId)
                autoAdjustmentDao.deleteByPlan(planId)
                activeRunDao.clearActiveRun()
                planDao.deletePlan(planId)
            }
        } catch (e: Exception) {
            android.util.Log.e("PlanRepository", "Erro ao excluir plano ativo $planId", e)
        }
    }

    /**
     * Exclui o plano e verifica se a exclusão foi confirmada no banco.
     * Retorna true se o plano não existe mais após a operação.
     * Preserva o histórico de corridas (workout_log e route_point) desvinculando treinos concluídos.
     */
    suspend fun deletePlanWithVerification(planId: Long): Boolean {
        if (planId <= 0L) {
            android.util.Log.w("PlanRepository", "deletePlanWithVerification chamado com planId inválido: $planId")
            return false
        }
        android.util.Log.d("PlanRepository", "deletePlanWithVerification INICIO planId=$planId")
        return try {
            database.withTransaction {
                android.util.Log.d("PlanRepository", "deletePlanWithVerification transacao INICIO planId=$planId")
                // 1. Obter IDs dos treinos do plano ANTES de deletar (necessário para deletar logs/pontos relacionados)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 1: getWorkoutIdsForPlan planId=$planId")
                val workoutIds = workoutDao.getWorkoutIdsForPlan(planId)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 1 done: workoutIds=$workoutIds")

                // 2. Deleta workout_log e route_point dos treinos do plano que NÃO são concluídos (ou de todos, mantendo logs se necessário)
                // Nota: O spec exige preservar o histórico de corridas (workout_log e route_point).
                // Portanto, desvinculamos os treinos concluídos (planId = 0) e deletamos apenas logs/pontos de treinos não concluídos,
                // ou preservamos logs dos treinos concluídos e desvinculamos.
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 2: getWorkoutsForPlanList planId=$planId")
                val completedWorkoutIds = workoutDao.getWorkoutsForPlanList(planId)
                    .filter { it.status == WorkoutEntity.STATUS_CONCLUIDO }
                    .map { it.id }
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 2 done: completedWorkoutIds=$completedWorkoutIds")

                val nonCompletedWorkoutIds = workoutIds.filter { !completedWorkoutIds.contains(it) }
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 2b: nonCompletedWorkoutIds=$nonCompletedWorkoutIds")

                if (nonCompletedWorkoutIds.isNotEmpty()) {
                    android.util.Log.d("PlanRepository", "deletePlanWithVerification step 2c: deleting non-completed logs/points")
                    workoutLogDao.deleteByWorkoutIds(nonCompletedWorkoutIds)
                    routePointDao.deleteByWorkoutIds(nonCompletedWorkoutIds)
                    android.util.Log.d("PlanRepository", "deletePlanWithVerification step 2c done")
                }

                // 3. Preserva o histórico de corridas: desvincula treinos concluídos (planId = 0)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 3: unlinkCompletedWorkoutsFromPlan planId=$planId")
                workoutDao.unlinkCompletedWorkoutsFromPlan(planId)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 3 done")

                // 4. Deleta treinos do plano que não foram desvinculados (ou seja, não concluídos)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 4: deleteWorkoutsForPlan planId=$planId")
                workoutDao.deleteWorkoutsForPlan(planId)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 4 done")

                // 5. Deleta ajustes
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 5: deleteByPlan planId=$planId")
                planAdjustmentDao.deleteByPlan(planId)
                autoAdjustmentDao.deleteByPlan(planId)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 5 done")

                // 6. Limpa corrida ativa se pertencer a este plano
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 6: clearActiveRun")
                activeRunDao.clearActiveRun()
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 6 done")

                // 7. Deleta o plano
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 7: deletePlan planId=$planId")
                planDao.deletePlan(planId)
                android.util.Log.d("PlanRepository", "deletePlanWithVerification step 7 done")
            }
            // Verifica se o plano realmente foi removido
            val plan = planDao.getPlanById(planId)
            val success = plan == null
            if (!success) {
                android.util.Log.w("PlanRepository", "Plano $planId ainda existe após tentativa de exclusão")
            }
            success
        } catch (e: Throwable) {
            android.util.Log.e("PlanRepository", "Erro ao excluir plano com verificação $planId: ${e.message}", e)
            // Log detalhado para depuração: classe, causa, stacktrace resumido
            val stackTrace = e.stackTraceToString().take(2000)
            android.util.Log.e("PlanRepository", "Detalhe exceção: classe=${e.javaClass.name} causa=${e.cause?.javaClass?.name} msg=${e.cause?.message}\n$stackTrace")
            false
        }
    }

    suspend fun applyRegeneration(
        updatedPlan: PlanEntity,
        toDelete: List<WorkoutEntity>,
        toCreate: List<WorkoutEntity>
    ) {
        database.withTransaction {
            if (toDelete.isNotEmpty()) {
                workoutDao.deleteWorkouts(toDelete)
            }
            if (toCreate.isNotEmpty()) {
                workoutDao.insertWorkouts(toCreate)
            }
            planDao.insertPlan(updatedPlan)
        }
    }

    suspend fun resetAll() {
        database.withTransaction {
            workoutDao.clearAll()
            planAdjustmentDao.clearAll()
            autoAdjustmentDao.clearAll()
            checkinDao.clearAll()
            planDao.clearAllPlans()
            profileDao.clearProfile()
            activeRunDao.clearActiveRun()
        }
    }

    suspend fun clearAllData() {
        database.withTransaction {
            workoutDao.clearAll()
            workoutLogDao.clearAllLogs()
            routePointDao.clearAllRoutePoints()
            planAdjustmentDao.clearAll()
            autoAdjustmentDao.clearAll()
            checkinDao.clearAll()
            activeRunDao.clearActiveRun()
            planDao.clearAllPlans()
            profileDao.clearProfile()
        }
    }

    /**
     * Limpa todos os dados de todas as modalidades (livre, 2.5K, 5K, 10K).
     * Garante que nenhum dado persiste no celular após a exclusão.
     */
    suspend fun clearAllDataAllModalities(context: android.content.Context) {
        val modalities = listOf(
            DatabaseProvider.MODALIDADE_LIVRE,
            DatabaseProvider.MODALIDADE_2_5K,
            DatabaseProvider.MODALIDADE_5K,
            DatabaseProvider.MODALIDADE_10K
        )
        for (modality in modalities) {
            val db = DatabaseProvider.getDatabase(modality, context)
            db.withTransaction {
                db.workoutDao().clearAll()
                db.workoutLogDao().clearAllLogs()
                db.routePointDao().clearAllRoutePoints()
                db.planAdjustmentDao().clearAll()
                db.autoAdjustmentDao().clearAll()
                db.checkinDao().clearAll()
                db.activeRunDao().clearActiveRun()
                db.planDao().clearAllPlans()
                db.runnerProfileDao().clearProfile()
            }
        }
    }

    // ---------- Métodos para compatibilidade com WorkspaceManager ----------

    /**
     * Obtém ou cria um treino de corrida livre para hoje.
     * Alias para compatibilidade com WorkspaceManager.
     */
    suspend fun getOrCreateFreeRunWorkout(): WorkoutEntity {
        val plan = getOrCreateFreeRunPlan()
        val today = LocalDate.now().toString()
        val existing = workoutDao.getWorkoutForPlanAndDate(plan.id, today)
        if (existing != null && existing.status == WorkoutEntity.STATUS_PLANEJADO) return existing

        // Cria um treino "livre" sem passos pré-definidos
        val workout = WorkoutEntity(
            planId = plan.id,
            semanaIndex = 0,
            treinoIndexNaSemana = 0,
            etapaIndex = 0,
            data = today,
            titulo = "Corrida Livre",
            resumoEtapa = "Sem passos definidos",
            passosJson = "[]",
            duracaoMinutos = 0,
            duracaoCorridaMinutos = 0,
            status = WorkoutEntity.STATUS_PLANEJADO,
            concluidoEm = null,
            distanciaAlvoM = null,
            modoConclusao = WorkoutEntity.MODO_CONCLUSAO_TEMPO
        )
        val workoutId = workoutDao.insertWorkout(workout)
        return workout.copy(id = workoutId)
    }

    /**
     * Aplica um ajuste de pausa (P11) - alias para applyPauseAdjustment.
     */
    suspend fun applyAdjustment(
        adjustment: PlanAdjustmentEntity,
        updatedWorkoutsWithPause: List<WorkoutEntity>,
        regenerationResult: RegenerationResult
    ) {
        applyPauseAdjustment(adjustment, updatedWorkoutsWithPause, regenerationResult)
    }

    /**
     * Regenera o plano ativo usando PlanRegenerator.
     */
    suspend fun regeneratePlan(
        plan: PlanEntity,
        workouts: List<WorkoutEntity>,
        input: PlanInput,
        today: LocalDate
    ): RegenerationResult {
        return PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = workouts,
            today = today,
            newEndDate = LocalDate.parse(plan.dataFim),
            diasSelecionados = input.diasSelecionados,
            frequenciaExercicio = input.frequenciaExercicio
        )
    }

    /**
     * Gera preview do plano usando o gerador tradicional.
     */
    fun generatePreview(input: PlanInput): GeneratedPlan {
        return PlanGenerator.generate(input)
    }

    // ---------- Plano que respira (P11) ----------

    suspend fun updateAjustesAutomaticos(ativo: Boolean) {
        profileDao.updateAjustesAutomaticos(ativo)
    }

    suspend fun updateModoAjuste(modo: String) {
        profileDao.updateModoAjuste(modo)
    }

    suspend fun updateAvisoDistanciaAtivo(ativo: Boolean) {
        profileDao.updateAvisoDistanciaAtivo(ativo)
    }

    suspend fun updateAvisoDistanciaMetros(metros: Int) {
        profileDao.updateAvisoDistanciaMetros(metros)
    }

    fun observeAutoAdjustments(planId: Long): Flow<List<AutoAdjustmentEntity>> {
        return autoAdjustmentDao.observeForPlan(planId)
    }

    suspend fun saveCheckin(checkin: CheckinEntity): Long = checkinDao.insert(checkin)

    /**
     * Salva o log do treino, o check-in pós e roda o motor de ajuste, tudo numa transação.
     * Uma falha do motor nunca impede o salvamento do log.
     * Devolve o id do log e uma sugestão pendente (modo cauteloso), se houver.
     */
    suspend fun finishRunAndAdjust(
        planId: Long?,
        workoutId: Long,
        duracaoSegundos: Long,
        distanciaMetros: Double,
        ritmoMedioSegundos: Double?,
        esforcoPercebido: Int,
        anotacoes: String?,
        diagnostics: com.example.tracker.GpsDiagnostics?,
        dor: Int,
        regiaoDor: String?,
        today: LocalDate
    ): Pair<Long, AdaptiveResult?> {
        return database.withTransaction {
            val logId = finishRunWithLog(
                workoutId = workoutId,
                duracaoSegundos = duracaoSegundos,
                distanciaMetros = distanciaMetros,
                ritmoMedioSegundos = ritmoMedioSegundos,
                esforcoPercebido = esforcoPercebido,
                anotacoes = anotacoes,
                diagnostics = diagnostics
            )
            var pendente: AdaptiveResult? = null
            try {
                checkinDao.insert(
                    CheckinEntity(
                        workoutId = workoutId,
                        momento = CheckinEntity.MOMENTO_POS,
                        dor = dor,
                        regiaoDor = regiaoDor
                    )
                )
                if (planId != null) {
                    pendente = runAdaptiveEngine(planId, workoutId, today)
                }
            } catch (e: Exception) {
                // O ajuste é opcional: o log do treino já está salvo.
                pendente = null
            }
            logId to pendente
        }
    }

    private suspend fun runAdaptiveEngine(planId: Long, workoutId: Long, today: LocalDate): AdaptiveResult? {
        val profile = profileDao.getProfile().firstOrNull() ?: return null
        if (!profile.ajustesAutomaticos) return null

        val workouts = workoutDao.getWorkoutsForPlanList(planId)
        val feedbacks = workoutLogDao.getRecentLogs(4).reversed().mapNotNull { log ->
            val w = workoutDao.getWorkoutById(log.workoutId) ?: return@mapNotNull null
            val pos = checkinDao.getForWorkout(log.workoutId, CheckinEntity.MOMENTO_POS)
            val pre = checkinDao.getForWorkout(log.workoutId, CheckinEntity.MOMENTO_PRE)
            TrainingFeedback(
                workoutId = log.workoutId,
                data = LocalDate.parse(w.data),
                etapaIndex = w.etapaIndex,
                esforco = log.esforcoPercebido,
                dor = pos?.dor ?: 0,
                regiaoDor = pos?.regiaoDor,
                energia = pre?.energia,
                sono = pre?.sono
            )
        }
        val ultimoAvanco = autoAdjustmentDao.lastAdvanceAt(planId)?.let {
            java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        }
        val resultado = AdaptiveEngine.evaluate(
            feedbacks = feedbacks,
            workouts = workouts,
            today = today,
            modo = profile.modoAjuste,
            ajustesAutomaticos = profile.ajustesAutomaticos,
            deslocamentoAtual = autoAdjustmentDao.sumDeslocamentoAtivo(planId),
            ultimoAvanco = ultimoAvanco
        )
        if (!resultado.temMudancas) return null
        if (resultado.precisaConfirmacao) return resultado // só sugere; a UI pede um toque
        applyAdaptiveResult(planId, workoutId, resultado)
        return null
    }

    /** Grava as mudanças do motor e o registro de ajuste (com antes e depois) numa transação. */
    suspend fun applyAdaptiveResult(planId: Long, workoutOrigemId: Long, resultado: AdaptiveResult) {
        val regra = resultado.regra ?: return
        database.withTransaction {
            resultado.mudancas.forEach { workoutDao.updateWorkout(it.depois) }
            autoAdjustmentDao.insert(
                AutoAdjustmentEntity(
                    planId = planId,
                    workoutOrigemId = workoutOrigemId,
                    regra = regra,
                    antesJson = changesToJson(resultado.mudancas) { it.antes },
                    depoisJson = changesToJson(resultado.mudancas) { it.depois },
                    ativo = true,
                    deslocamentoEtapa = resultado.deslocamentoEtapa
                )
            )
        }
    }

    /** Avalia dois treinos pulados em sequência (chamado depois de marcar um treino como pulado). */
    suspend fun evaluateAfterSkip(planId: Long, workoutId: Long, today: LocalDate): Boolean {
        val profile = profileDao.getProfile().firstOrNull() ?: return false
        if (!profile.ajustesAutomaticos) return false
        val workouts = workoutDao.getWorkoutsForPlanList(planId)
        val passados = workouts
            .filter { LocalDate.parse(it.data) <= today && it.status != WorkoutEntity.STATUS_PLANEJADO }
            .sortedBy { LocalDate.parse(it.data) }
        // Sequência de pulados no fim da lista; só age quando chega exatamente a 2
        val sequencia = passados.reversed().takeWhile { it.status == WorkoutEntity.STATUS_PULADO }
        if (sequencia.size != 2) return false
        val resultado = com.example.generator.AdaptiveEngine.evaluateSkips(
            datasPuladas = sequencia.map { LocalDate.parse(it.data) },
            workouts = workouts,
            today = today,
            ajustesAutomaticos = true
        )
        if (!resultado.temMudancas) return false
        applyAdaptiveResult(planId, workoutId, resultado)
        return true
    }

    /** Desfaz um ajuste: restaura os treinos que ainda estão planejados e marca como desfeito. */
    suspend fun undoAdjustment(adjustmentId: Long) {
        database.withTransaction {
            val adj = autoAdjustmentDao.getById(adjustmentId) ?: return@withTransaction
            if (!adj.ativo) return@withTransaction
            val antes = org.json.JSONArray(adj.antesJson)
            for (i in 0 until antes.length()) {
                val o = antes.getJSONObject(i)
                val atual = workoutDao.getWorkoutById(o.getLong("id")) ?: continue
                if (atual.status != WorkoutEntity.STATUS_PLANEJADO) continue
                workoutDao.updateWorkout(
                    atual.copy(
                        etapaIndex = o.getInt("etapaIndex"),
                        resumoEtapa = o.getString("resumoEtapa"),
                        passosJson = o.getString("passosJson"),
                        duracaoMinutos = o.getInt("duracaoMinutos"),
                        duracaoCorridaMinutos = o.getInt("duracaoCorridaMinutos")
                    )
                )
            }
            autoAdjustmentDao.setAtivo(adjustmentId, false)
        }
    }

    private fun changesToJson(
        mudancas: List<WorkoutChange>,
        pick: (WorkoutChange) -> WorkoutEntity
    ): String {
        val array = org.json.JSONArray()
        mudancas.forEach { change ->
            val w = pick(change)
            array.put(
                org.json.JSONObject()
                    .put("id", w.id)
                    .put("etapaIndex", w.etapaIndex)
                    .put("resumoEtapa", w.resumoEtapa)
                    .put("passosJson", w.passosJson)
                    .put("duracaoMinutos", w.duracaoMinutos)
                    .put("duracaoCorridaMinutos", w.duracaoCorridaMinutos)
            )
        }
        return array.toString()
    }
}
