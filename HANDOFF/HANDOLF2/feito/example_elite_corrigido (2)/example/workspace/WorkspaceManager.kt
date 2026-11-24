package com.example.workspace

import android.app.Application
import com.example.data.local.BaseAppDatabase
import com.example.data.local.DatabaseProvider
import com.example.data.local.PlanRepository
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.generator.GeneratedPlan
import com.example.generator.AdaptiveResult
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.generator.PlanInput
import com.example.generator.PlanGenerator
import com.example.generator.TemplatePlanInput
import com.example.generator.Template25K
import com.example.generator.Template10K
import com.example.generator.PlanRegenerator
import com.example.generator.RegenerationResult
import com.example.generator.PauseApplier
import com.example.generator.PausePreviewData
import com.example.generator.AdaptiveEngine
import com.example.generator.CheckinOutcome
import com.example.worker.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.supervisorScope
import androidx.room.withTransaction

/**
 * Gerenciador de workspaces (ambientes isolados por modalidade).
 * Responsável por:
 * - Ciclo de vida dos workspaces (criação, hidratação, destruição)
 * - Troca de modalidade com preservação de estado
 * - Observação de bancos de dados por modalidade
 * - Sincronização com GlobalSession
 */
class WorkspaceManager(
    private val application: Application,
    private val globalSession: MutableStateFlow<GlobalSession>,
    private val scope: CoroutineScope
) {

    // Estado atual do workspace ativo
    private val _currentWorkspace = MutableStateFlow<WorkspaceState>(
        WorkspaceState(modality = globalSession.value.lastActiveModality)
    )
    val currentWorkspace: StateFlow<WorkspaceState> = _currentWorkspace

    // Repositório atual (para o banco da modalidade ativa)
    private var currentRepository: PlanRepository? = null
    private var currentModality: String = globalSession.value.lastActiveModality
    private var currentDb: BaseAppDatabase? = null

    // Jobs de observação ativos
    private val observeJobs = mutableListOf<Job>()

    init {
        // Inicializa o workspace da modalidade salva
        scope.launch {
            hydrateWorkspace(globalSession.value.lastActiveModality)
        }
    }

    /**
     * Hidrata um workspace para a modalidade especificada.
     * Cancela observações antigas, abre o banco e carrega dados.
     *
     * @param targetScreen Se fornecido, sobrescreve a tela de destino quando não há plano ativo.
     *                     Útil quando o usuário seleciona explicitamente uma modalidade na GoalSelection.
     */
    suspend fun hydrateWorkspace(modality: String, targetScreen: AppScreen? = null) {
        // Cancela observações antigas
        observeJobs.forEach { it.cancel() }
        observeJobs.clear()

        // Reseta estado do wizard ao trocar de modalidade (independência total por distância - spec §6)
        resetWizardState()

        currentModality = modality

        // Abre o banco da modalidade
        val newDb = withContext(Dispatchers.IO) {
            DatabaseProvider.getDatabase(modality, application)
        }
        currentDb = newDb

        // Cria repositório para a modalidade
        val newRepo = PlanRepository(
            database = newDb,
            modalidade = modality,
            profileDao = newDb.runnerProfileDao(),
            planDao = newDb.planDao(),
            workoutDao = newDb.workoutDao(),
            routePointDao = newDb.routePointDao(),
            workoutLogDao = newDb.workoutLogDao(),
            activeRunDao = newDb.activeRunDao()
        )
        currentRepository = newRepo

        // Atualiza estado com a nova modalidade
        _uiStateUpdate { it.copy(modality = modality, isLoading = true) }

        // Inicia observações
        startObservations()

        // Carrega perfil e plano ativo (apenas ACTIVE + aceite registrado = acceptedAt > 0)
        scope.launch {
            val profile = newRepo.runnerProfile.firstOrNull()
            val activePlan = newRepo.activePlan.firstOrNull()

            // Verifica se o plano ativo tem aceite registrado (acceptedAt > 0)
            val hasAcceptedPlan = activePlan?.let { it.status == PlanEntity.STATUS_ATIVO && it.acceptedAt > 0 } ?: false

            _uiStateUpdate { current ->
                var updated = current.copy(
                    isLoading = false,
                    modality = modality,
                    activePlan = if (hasAcceptedPlan) activePlan else null
                )

                if (profile != null) {
                    val days = try {
                        profile.diasSelecionadosJson.split(",")
                            .mapNotNull { name -> try { DayOfWeek.valueOf(name.trim()) } catch (e: Exception) { null } }
                            .ifEmpty { listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) }
                    } catch (e: Exception) {
                        listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
                    }

                    updated = updated.copy(
                        userName = profile.nome ?: current.userName,
                        dismissBatteryWarning = profile.dismissBatteryWarning,
                        voiceEnabled = profile.vozAtiva,
                        vibrationEnabled = profile.vibracaoAtiva,
                        lembretesAtivos = profile.lembretesAtivos,
                        lembreteHora = profile.lembreteHora,
                        lembreteMinuto = profile.lembreteMinuto,
                        avisoDistanciaAtivo = profile.avisoDistanciaAtivo,
                        avisoDistanciaMetros = profile.avisoDistanciaMetros,
                        showMapDuringRun = profile.showMapDuringRun,
                        defaultCameraMode = profile.defaultCameraMode,
                        diasSelecionados = days,
                        frequenciaExercicio = profile.frequenciaExercicio,
                        ajustesAutomaticos = profile.ajustesAutomaticos,
                        modoAjuste = profile.modoAjuste
                    )
                }

                // Se já tem plano ativo COM ACEITE, vai para Today; senão, verifica onboarding
                if (hasAcceptedPlan) {
                    updated = updated.copy(
                        currentScreen = AppScreen.Today,
                        screenStack = listOf(AppScreen.Today),
                        goalSelected = true
                    )
                } else {
                    // Fluxo de onboarding condicional:
                    // - Se já completou onboarding para esta modalidade, vai direto para GoalSelection
                    // - Se nunca completou, começa do Welcome
                    // Se um targetScreen foi fornecido (ex: seleção explícita na GoalSelection), use-o.
                    val destinationScreen = targetScreen ?: (if (globalSession.value.completedOnboardingModalities.contains(modality)) AppScreen.GoalSelection else AppScreen.Welcome)
                    updated = updated.copy(
                        currentScreen = destinationScreen,
                        screenStack = listOf(destinationScreen),
                        goalSelected = destinationScreen is AppScreen.GoalSelection
                    )
                }

                updated
            }
        }
    }

    /**
     * Inicia as observações do banco de dados atual.
     */
    private fun startObservations() {
        val repo = currentRepository ?: return

        // Observa plano ativo
        val planJob = scope.launch {
            repo.activePlan.collectLatest { plan ->
                _uiStateUpdate { current ->
                    var updated = current.copy(
                        activePlan = plan,
                        isLoading = false
                    )

                    if (plan != null) {
                        // Carrega workouts e treino de hoje
                        updated = updated.copy(
                            currentScreen = if (current.currentScreen is AppScreen.Today ||
                                current.currentScreen is AppScreen.FullPlan ||
                                current.currentScreen is AppScreen.Progress ||
                                current.currentScreen is AppScreen.Profile ||
                                current.currentScreen is AppScreen.ActiveRun ||
                                current.currentScreen is AppScreen.PlanAdjustments ||
                                current.currentScreen is AppScreen.WorkoutSummary) {
                                current.currentScreen
                            } else {
                                AppScreen.Today
                            }
                        )
                    } else {
                        val nextScreen = if (current.activePlan != null) {
                            if (current.currentScreen is AppScreen.FullPlan) {
                                AppScreen.Today
                            } else if (current.currentScreen is AppScreen.Today || current.currentScreen is AppScreen.Profile) {
                                current.currentScreen
                            } else {
                                AppScreen.Welcome
                            }
                        } else {
                            current.currentScreen
                        }
                        updated = updated.copy(
                            workouts = emptyList(),
                            todayWorkout = null,
                            nextWorkout = null,
                            activePauseAdjustment = null,
                            autoAdjustments = emptyList(),
                            currentScreen = nextScreen
                        )
                    }
                    updated
                }
            }
        }
        observeJobs.add(planJob)

        // Observa workouts (re-query sempre que o plano ativo mudar, ex.: plano recém-aceito)
        val workoutsJob = scope.launch {
            repo.activePlan
                .flatMapLatest { plan ->
                    repo.getWorkouts(plan?.id ?: -1)
                }
                .collectLatest { workoutList ->
                    val today = LocalDate.now()
                    val todayStr = today.toString()
                    val todayW = workoutList.find { it.data == todayStr }
                    val nextW = workoutList
                        .filter { it.data >= todayStr && it.status == WorkoutEntity.STATUS_PLANEJADO }
                        .minByOrNull { it.data }

                    _uiStateUpdate { current ->
                        current.copy(
                            workouts = workoutList,
                            todayWorkout = todayW,
                            nextWorkout = nextW,
                            currentDate = today,
                            isLoading = false
                        )
                    }
                }
        }
        observeJobs.add(workoutsJob)

        // Observa workout logs
        val workoutLogsJob = scope.launch {
            repo.allWorkoutLogs.collectLatest { logs ->
                _uiStateUpdate { it.copy(workoutLogs = logs) }
            }
        }
        observeJobs.add(workoutLogsJob)

        // Observa ajustes automáticos
        val autoAdjJob = scope.launch {
            repo.activePlan.collectLatest { plan ->
                if (plan != null) {
                    repo.observeAutoAdjustments(plan.id).collectLatest { list ->
                        _uiStateUpdate { it.copy(autoAdjustments = list) }
                    }
                }
            }
        }
        observeJobs.add(autoAdjJob)

        // Observa ajuste ativo
        val activeAdjJob = scope.launch {
            repo.activePlan.collectLatest { plan ->
                if (plan != null) {
                    repo.getActiveAdjustment(plan.id, LocalDate.now()).collectLatest { adj ->
                        _uiStateUpdate { it.copy(activePauseAdjustment = adj) }
                    }
                }
            }
        }
        observeJobs.add(activeAdjJob)

        // Observa corrida ativa
        val activeRunJob = scope.launch {
            repo.activeRunFlow.collectLatest { activeRun ->
                _uiStateUpdate { it.copy(activeRunToRecover = activeRun) }
            }
        }
        observeJobs.add(activeRunJob)
    }

    /**
     * Atualiza o estado do workspace atual de forma thread-safe.
     */
    private inline fun _uiStateUpdate(block: (WorkspaceState) -> WorkspaceState) {
        _currentWorkspace.value = block(_currentWorkspace.value)
    }

    /**
     * Atualiza o estado de loading do workspace.
     */
    fun updateLoadingState(isLoading: Boolean) {
        _uiStateUpdate { it.copy(isLoading = isLoading) }
    }

    /**
     * Retorna o repositório atual.
     */
    fun getRepository(): PlanRepository? = currentRepository

    /**
     * Retorna a modalidade atual.
     */
    fun getCurrentModality(): String = currentModality

    /**
     * Troca para uma nova modalidade.
     */
    suspend fun switchToModality(modality: String) {
        if (modality == currentModality) return
        hydrateWorkspace(modality)
    }

    /**
     * Gera preview do plano usando o gerador tradicional (para 5K).
     */
    fun generatePreview() {
        val state = _currentWorkspace.value
        try {
            val input = PlanInput(
                tempoSemCorrer = state.tempoSemCorrer ?: "Nunca corri",
                frequenciaExercicio = state.frequenciaExercicio ?: "Nada",
                tipoExercicio = state.tipoExercicio ?: "Nenhum",
                diasPorSemana = state.diasPorSemana,
                diasSelecionados = state.diasSelecionados,
                dataInicio = state.dataInicio,
                totalSemanas = state.totalSemanas
            )

            val plan = PlanGenerator.generate(input)
            _uiStateUpdate { it.copy(previewPlan = plan, previewErrorMessage = null) }
            navigateTo(AppScreen.PlanPreview)
        } catch (e: IllegalArgumentException) {
            _uiStateUpdate { it.copy(previewErrorMessage = e.message ?: "Configuração de plano inválida.") }
        }
    }

    /**
     * Gera plano a partir de template (para 2.5K, 10K).
     */
    fun generateTemplatePlan() {
        val state = _currentWorkspace.value
        val modality = state.modality

        when (modality) {
            "plano_2_5k", "plano_10k" -> {
                val template = when (modality) {
                    "plano_2_5k" -> Template25K.getTemplate()
                    "plano_10k" -> Template10K.getTemplate()
                    else -> throw IllegalArgumentException("Modalidade desconhecida: $modality")
                }

                try {
                    val input = TemplatePlanInput(
                        template = template,
                        dataInicio = state.dataInicio,
                        diasSelecionados = state.diasSelecionados
                    )

                    val generatedPlan = PlanGenerator.generateFromTemplate(input)

                    // Converte para GeneratedPlan (compatibilidade)
                    val plan = GeneratedPlan(
                        dataInicio = generatedPlan.dataInicio,
                        dataFim = generatedPlan.dataFim,
                        totalSemanas = generatedPlan.totalSemanas,
                        diasPorSemana = generatedPlan.diasPorSemana,
                        etapaInicial = 1,
                        treinos = generatedPlan.treinos.map { wt ->
                            com.example.generator.GeneratedWorkout(
                                semanaIndex = wt.semanaIndex,
                                treinoIndexNaSemana = wt.diaDaSemana.ordinal + 1,
                                etapaIndex = 1,
                                data = wt.data,
                                titulo = wt.titulo,
                                resumoEtapa = wt.resumoSessao,
                                steps = wt.steps,
                                duracaoTotalMinutos = wt.duracaoTotalMinutos,
                                duracaoCorridaMinutos = wt.duracaoCorridaMinutos
                            )
                        },
                        modalidade = generatedPlan.modalidade
                    )

                    _uiStateUpdate { it.copy(previewPlan = plan, previewErrorMessage = null) }
                    navigateTo(AppScreen.PlanPreview)
                } catch (e: IllegalArgumentException) {
                    _uiStateUpdate { it.copy(previewErrorMessage = e.message ?: "Configuração de plano inválida.") }
                }
            }
            "plano_5k" -> {
                generatePreview()
            }
        }
    }

    /**
     * Inicia o plano (salva no banco e navega para Today).
     * O plano já foi salvo como DRAFT em saveProfileAndGeneratePlan.
     * Aqui apenas marcamos como aceito e ativo.
     */
    suspend fun startPlan() {
        val state = _currentWorkspace.value
        val repo = currentRepository ?: return
        val previewPlan = state.previewPlan ?: return

        _uiStateUpdate { it.copy(isLoading = true) }

        // Persiste perfil + plano como DRAFT antes do aceite (spec §8, §9).
        // O plano gerado só existed em memória (previewPlan); sem este salvamento
        // não há registro no banco e o fluxo ficava travado em "Gerando seu plano".
        val profile = RunnerProfileEntity(
            nome = state.userName,
            tempoSemCorrer = state.tempoSemCorrer ?: "Nunca corri",
            frequenciaExercicio = state.frequenciaExercicio ?: "Nada",
            tipoExercicio = state.tipoExercicio ?: "Nenhum",
            diasPorSemana = state.diasPorSemana,
            diasSelecionadosJson = state.diasSelecionados.joinToString(",") { it.name },
            dataInicio = state.dataInicio.toString(),
            totalSemanas = state.totalSemanas,
            dismissBatteryWarning = state.dismissBatteryWarning,
            vozAtiva = state.voiceEnabled,
            vibracaoAtiva = state.vibrationEnabled,
            lembretesAtivos = state.lembretesAtivos,
            lembreteHora = state.lembreteHora,
            lembreteMinuto = state.lembreteMinuto,
            ajustesAutomaticos = state.ajustesAutomaticos,
            modoAjuste = state.modoAjuste,
            avisoDistanciaAtivo = state.avisoDistanciaAtivo,
            avisoDistanciaMetros = state.avisoDistanciaMetros
        )
        val planId = repo.saveProfileAndGeneratePlan(profile, previewPlan)

        // Marca o plano como aceito e ativo
        val accepted = repo.acceptPlan(planId)
        if (!accepted) {
            _uiStateUpdate { it.copy(isLoading = false, previewErrorMessage = "Erro ao aceitar o plano. Tente novamente.") }
            return
        }

        val existingProfile = repo.runnerProfile.firstOrNull()
        val lembretesAtivos = existingProfile?.lembretesAtivos ?: state.lembretesAtivos
        val lembreteHora = existingProfile?.lembreteHora ?: state.lembreteHora
        val lembreteMinuto = existingProfile?.lembreteMinuto ?: state.lembreteMinuto

        if (lembretesAtivos) {
            ReminderScheduler.scheduleDailyReminder(
                application,
                currentModality,
                lembreteHora,
                lembreteMinuto,
                true
            )
        }

        _uiStateUpdate {
            it.copy(
                isLoading = false,
                lembretesAtivos = lembretesAtivos,
                lembreteHora = lembreteHora,
                lembreteMinuto = lembreteMinuto,
                currentScreen = AppScreen.Today,
                screenStack = listOf(AppScreen.Today),
                goalSelected = true
            )
        }

        // Marcar onboarding como completo para esta modalidade
        completeOnboardingForCurrentModality()
    }

    /**
     * Atualiza o nome do usuário (sincroniza com GlobalSession).
     */
    fun setUserName(name: String) {
        _uiStateUpdate { it.copy(userName = name) }
        scope.launch {
            currentRepository?.updateNome(name)
        }
    }

    /**
     * Define dismiss battery warning.
     */
    fun setDismissBatteryWarning(dismiss: Boolean) {
        _uiStateUpdate { it.copy(dismissBatteryWarning = dismiss) }
        scope.launch {
            currentRepository?.updateDismissBatteryWarning(dismiss)
        }
    }

    /**
     * Define voz ativa.
     */
    fun setVoiceEnabled(enabled: Boolean) {
        _uiStateUpdate { it.copy(voiceEnabled = enabled) }
        scope.launch {
            currentRepository?.updateVozAtiva(enabled)
        }
    }

    /**
     * Define lembretes de treino (ativa/desativa + horário). Atualiza o estado reativo
     * do workspace e persiste no perfil; o agendamento é feito pelo ViewModel.
     */
    fun setLembretes(ativos: Boolean, hora: Int, minuto: Int) {
        _uiStateUpdate { it.copy(lembretesAtivos = ativos, lembreteHora = hora, lembreteMinuto = minuto) }
        scope.launch {
            currentRepository?.updateLembretes(ativos, hora, minuto)
        }
    }

    /**
     * Define vibração ativa.
     */
    fun setVibrationEnabled(enabled: Boolean) {
        _uiStateUpdate { it.copy(vibrationEnabled = enabled) }
        scope.launch {
            currentRepository?.updateVibracaoAtiva(enabled)
        }
    }

    /**
     * Define aviso de distância ativo.
     */
    fun setAvisoDistanciaAtivo(ativo: Boolean) {
        _uiStateUpdate { it.copy(avisoDistanciaAtivo = ativo) }
        scope.launch {
            currentRepository?.updateAvisoDistanciaAtivo(ativo)
        }
    }

    /**
     * Define metros do aviso de distância.
     */
    fun setAvisoDistanciaMetros(metros: Int) {
        _uiStateUpdate { it.copy(avisoDistanciaMetros = metros) }
        scope.launch {
            currentRepository?.updateAvisoDistanciaMetros(metros)
        }
    }

    /**
     * Define se deve mostrar o mapa durante a corrida.
     */
    fun setShowMapDuringRun(show: Boolean) {
        _uiStateUpdate { it.copy(showMapDuringRun = show) }
        scope.launch {
            currentRepository?.updateShowMapDuringRun(show)
        }
    }

    /**
     * Define o modo de câmera padrão do mapa.
     */
    fun setDefaultCameraMode(mode: String) {
        _uiStateUpdate { it.copy(defaultCameraMode = mode) }
        scope.launch {
            currentRepository?.updateDefaultCameraMode(mode)
        }
    }

    /**
     * Navega para uma tela.
     */
    fun navigateTo(screen: AppScreen) {
        _uiStateUpdate { current ->
            current.copy(
                currentScreen = screen,
                screenStack = current.screenStack + screen
            )
        }
    }

    /**
     * Navega para uma aba principal (substitui stack).
     */
    fun switchTab(screen: AppScreen) {
        _uiStateUpdate { current ->
            current.copy(
                currentScreen = screen,
                screenStack = listOf(screen)
            )
        }
    }

    /**
     * Volta na navegação.
     */
    fun navigateBack() {
        _uiStateUpdate { current ->
            if (current.screenStack.size > 1) {
                val newStack = current.screenStack.dropLast(1)
                current.copy(
                    currentScreen = newStack.last(),
                    screenStack = newStack
                )
            } else {
                current.copy(currentScreen = AppScreen.Welcome)
            }
        }
    }

    /**
     * Define tempo sem correr.
     */
    fun setTempoSemCorrer(value: String) {
        _uiStateUpdate { it.copy(tempoSemCorrer = value) }
    }

    /**
     * Define frequência de exercício.
     */
    fun setFrequenciaExercicio(value: String) {
        val minWeeks = PlanGenerator.getMinimumWeeks(value)
        _uiStateUpdate { current ->
            val adjustedTotal = if (current.totalSemanas < minWeeks) minWeeks else current.totalSemanas
            current.copy(
                frequenciaExercicio = value,
                minSemanas = minWeeks,
                totalSemanas = adjustedTotal
            )
        }
    }

    /**
     * Define tipo de exercício.
     */
    fun setTipoExercicio(value: String) {
        _uiStateUpdate { it.copy(tipoExercicio = value) }
    }

    /**
     * Define dias por semana.
     */
    fun setDiasPorSemana(count: Int) {
        val safeCount = count.coerceIn(2, 3)
        val defaultDays = PlanGenerator.getDefaultDays(safeCount)
        _uiStateUpdate {
            it.copy(
                diasPorSemana = safeCount,
                diasSelecionados = defaultDays
            )
        }
    }

    /**
     * Alterna dia da semana.
     */
    fun toggleDiaSemana(day: DayOfWeek) {
        _uiStateUpdate { current ->
            val currentDays = current.diasSelecionados.toMutableList()
            if (currentDays.contains(day)) {
                currentDays.remove(day)
            } else {
                val forbidden = currentDays.flatMap { listOf(it.minus(1), it.plus(1)) }.toSet()
                if (day !in forbidden && currentDays.size < current.diasPorSemana) {
                    currentDays.add(day)
                }
            }
            current.copy(diasSelecionados = currentDays.sorted())
        }
    }

    /**
     * Define data de início.
     */
    fun setDataInicio(date: LocalDate) {
        _uiStateUpdate { it.copy(dataInicio = date) }
    }

    /**
     * Define total de semanas.
     */
    fun setTotalSemanas(weeks: Int) {
        val minWeeks = _currentWorkspace.value.minSemanas
        _uiStateUpdate { it.copy(totalSemanas = weeks.coerceIn(minWeeks, 16)) }
    }

    /**
     * Volta para seleção de objetivo.
     */
    fun backToGoalSelection() {
        _uiStateUpdate { current ->
            current.copy(
                goalSelected = false,
                selectedModality = "plano_5k"
            )
        }
        navigateTo(AppScreen.GoalSelection)
    }

    /**
     * Seleciona modalidade (chamado da tela GoalSelection).
     * Verifica no banco da modalidade se existe plano ACTIVE + aceite; se sim, vai direto para Today.
     * Caso contrário, abre o quiz/geração correspondente.
     */
    suspend fun selectModality(modality: String) {
        // Se já está na mesma modalidade com plano ativo e aceite, vai direto para FullPlan
        val currentState = _currentWorkspace.value
        val activePlan = currentState.activePlan
        if (activePlan != null && currentModality == modality && activePlan.acceptedAt > 0) {
            switchTab(AppScreen.FullPlan)
            return
        }

        // Para modalidades com distância, consulta o banco específico para verificar ACTIVE + aceite
        if (modality in setOf("plano_2_5k", "plano_5k", "plano_10k")) {
            val db = DatabaseProvider.getDatabase(modality, application)
            val repo = PlanRepository(
                database = db,
                modalidade = modality,
                profileDao = db.runnerProfileDao(),
                planDao = db.planDao(),
                workoutDao = db.workoutDao(),
                routePointDao = db.routePointDao(),
                workoutLogDao = db.workoutLogDao(),
                activeRunDao = db.activeRunDao()
            )

            val acceptedPlan = repo.getActivePlanForModality(modality).firstOrNull()
            val hasAcceptedPlan = acceptedPlan?.let { it.status == PlanEntity.STATUS_ATIVO && it.acceptedAt > 0 } ?: false

            val targetScreen = if (hasAcceptedPlan) {
                AppScreen.Today
            } else {
                when (modality) {
                    "plano_5k" -> AppScreen.QuizQuestion1
                    "plano_2_5k", "plano_10k" -> AppScreen.DaysSelection
                    else -> AppScreen.GoalSelection
                }
            }

            _uiStateUpdate { current ->
                current.copy(
                    selectedModality = modality,
                    goalSelected = true,
                    previewErrorMessage = null
                )
            }

            hydrateWorkspace(modality, targetScreen)
            return
        }

        // Treino Livre: vai direto para Today
        val targetScreen = when (modality) {
            "livre" -> AppScreen.Today
            else -> null
        }

        _uiStateUpdate { current ->
            current.copy(
                selectedModality = modality,
                goalSelected = true,
                previewErrorMessage = null
            )
        }

        hydrateWorkspace(modality, targetScreen)
    }

    /**
     * Marca o onboarding como completo para a modalidade atual.
     * Chamado após o usuário gerar e iniciar um plano (Quiz → PlanPreview → Today).
     */
    fun completeOnboardingForCurrentModality() {
        val modality = currentModality
        globalSession.value = globalSession.value.copy(
            completedOnboardingModalities = globalSession.value.completedOnboardingModalities + modality
        )
    }

    /**
     * Limpa o onboarding completado para uma modalidade específica.
     * Chamado quando o plano dessa modalidade é excluído, permitindo recomeçar o quiz.
     */
    fun clearOnboardingForModality(modality: String) {
        globalSession.value = globalSession.value.copy(
            completedOnboardingModalities = globalSession.value.completedOnboardingModalities - modality
        )
    }

    /**
     * Verifica se a modalidade já completou o onboarding.
     */
    fun hasCompletedOnboarding(modality: String): Boolean {
        return globalSession.value.completedOnboardingModalities.contains(modality)
    }

    /**
     * Verifica se a modalidade atual já completou o onboarding.
     */
    fun currentModalityHasCompletedOnboarding(): Boolean {
        return hasCompletedOnboarding(currentModality)
    }

    /**
     * Descarta corrida ativa.
     */
    fun discardActiveRun() {
        scope.launch {
            currentRepository?.clearActiveRun()
        }
    }

    /**
     * Atualiza treino de hoje (chamado ao voltar ao primeiro plano).
     */
    fun refreshToday() {
        val today = LocalDate.now()
        val todayStr = today.toString()
        val workoutList = _currentWorkspace.value.workouts

        val todayW = workoutList.find { it.data == todayStr }
        val nextW = workoutList
            .filter { it.data >= todayStr && it.status == WorkoutEntity.STATUS_PLANEJADO }
            .minByOrNull { it.data }

        _uiStateUpdate { current ->
            current.copy(
                currentDate = today,
                todayWorkout = todayW,
                nextWorkout = nextW
            )
        }
    }

    /**
     * Callback quando corrida termina.
     */
    fun onRunFinished(
        workoutId: Long,
        workoutTitle: String,
        elapsedSeconds: Long,
        totalDistanceMeters: Double
    ) {
        val runState = com.example.service.RunTrackingService.runState.value
        val gpsDiagnostics = runState.gpsDiagnostics
        val routePoints = runState.routePoints
        _uiStateUpdate {
            it.copy(
                runSummaryData = RunSummaryData(
                    workoutId = workoutId,
                    workoutTitle = workoutTitle,
                    elapsedSeconds = elapsedSeconds,
                    totalDistanceMeters = totalDistanceMeters,
                    gpsDiagnostics = gpsDiagnostics,
                    routePoints = routePoints
                ),
                currentScreen = AppScreen.WorkoutSummary,
                screenStack = it.screenStack + AppScreen.WorkoutSummary
            )
        }
    }

    /**
     * Inicia corrida livre.
     */
    fun startFreeRun(onStart: (WorkoutEntity, Boolean) -> Unit) {
        scope.launch {
            val repo = currentRepository ?: return@launch
            val state = _currentWorkspace.value
            val workout = repo.getOrCreateFreeRunWorkout()

            // Navega para ActiveRun
            _uiStateUpdate {
                it.copy(
                    currentScreen = AppScreen.ActiveRun,
                    screenStack = it.screenStack + AppScreen.ActiveRun
                )
            }

            // Corrida livre sempre usa GPS (o Boolean é consumido como useGps pelo caller).
            onStart(workout, true)
        }
    }

    /**
     * Inicia treino planejado.
     */
    fun startPlannedWorkout(workout: WorkoutEntity, onStart: (WorkoutEntity, Boolean) -> Unit) {
        scope.launch {
            val state = _currentWorkspace.value
            val isFreeRun = state.modality == "livre"

            // Mostra check-in pré-treino para planos estruturados
            if (!isFreeRun) {
                _uiStateUpdate {
                    it.copy(
                        showPreWorkoutCheckin = true,
                        preWorkoutCheckinWorkout = workout
                    )
                }
            } else {
                _uiStateUpdate {
                    it.copy(
                        currentScreen = AppScreen.ActiveRun,
                        screenStack = it.screenStack + AppScreen.ActiveRun
                    )
                }
                onStart(workout, isFreeRun)
            }
        }
    }

    /**
     * Confirma check-in pré-treino.
     */
    fun confirmPreWorkoutCheckin(energia: Int, sono: Int, onStart: (WorkoutEntity, Boolean) -> Unit) {
        val workout = _currentWorkspace.value.preWorkoutCheckinWorkout ?: return
        val state = _currentWorkspace.value
        val isFreeRun = state.modality == "livre"

        // Avalia check-in
        val outcome = AdaptiveEngine.evaluateCheckin(energia, sono, 0)

        _uiStateUpdate {
            it.copy(
                showPreWorkoutCheckin = false,
                preWorkoutCheckinWorkout = null,
                currentScreen = AppScreen.ActiveRun,
                screenStack = it.screenStack + AppScreen.ActiveRun
            )
        }

        onStart(workout, isFreeRun)
    }

    /**
     * Fecha check-in pré-treino.
     */
    fun closePreWorkoutCheckin() {
        _uiStateUpdate {
            it.copy(
                showPreWorkoutCheckin = false,
                preWorkoutCheckinWorkout = null
            )
        }
    }

    /**
     * Limpa os dados de resumo de corrida.
     */
    fun clearRunSummaryData() {
        _uiStateUpdate { it.copy(runSummaryData = null) }
    }

    /**
     * Reinicia fluxo de wizard (limpa estado de preview/wizard).
     */
    fun resetWizardState() {
        _uiStateUpdate { it.copy(
            previewPlan = null,
            previewErrorMessage = null,
            tempoSemCorrer = null,
            frequenciaExercicio = null,
            tipoExercicio = null,
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.now(),
            totalSemanas = 9,
            minSemanas = 6,
            goalSelected = false
        )}
    }

    /**
     * Aplica ajuste de pausa (P11).
     */
    suspend fun applyPauseAdjustment() {
        val repo = currentRepository ?: return
        val state = _currentWorkspace.value
        val plan = state.activePlan ?: return
        val adjustment = state.activePauseAdjustment ?: return

        val preview = PauseApplier.previewPause(
            plan = plan,
            workouts = state.workouts,
            startDate = LocalDate.parse(adjustment.inicio),
            endDate = LocalDate.parse(adjustment.fim),
            nivel = adjustment.nivel,
            retorno = adjustment.retorno,
            today = LocalDate.now(),
            diasSelecionados = state.diasSelecionados,
            frequenciaExercicio = state.frequenciaExercicio ?: "Outros"
        )

        repo.applyAdjustment(
            adjustment = adjustment,
            updatedWorkoutsWithPause = preview.updatedWorkoutsWithPause,
            regenerationResult = preview.regenerationResult
        )

        _uiStateUpdate { it.copy(activePauseAdjustment = null) }
    }

    /**
     * Regenera plano (P11).
     */
    suspend fun regeneratePlan() {
        val repo = currentRepository ?: return
        val state = _currentWorkspace.value
        val plan = state.activePlan ?: return

        val input = PlanInput(
            tempoSemCorrer = state.tempoSemCorrer ?: "Nunca corri",
            frequenciaExercicio = state.frequenciaExercicio ?: "Nada",
            tipoExercicio = state.tipoExercicio ?: "Nenhum",
            diasPorSemana = state.diasPorSemana,
            diasSelecionados = state.diasSelecionados,
            dataInicio = state.dataInicio,
            totalSemanas = state.totalSemanas
        )

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = state.workouts,
            today = LocalDate.now(),
            newEndDate = LocalDate.parse(plan.dataFim),
            diasSelecionados = state.diasSelecionados,
            frequenciaExercicio = state.frequenciaExercicio ?: "Outros"
        )

        repo.applyRegeneration(
            updatedPlan = result.updatedPlan,
            toDelete = result.toDelete,
            toCreate = result.toCreate
        )
    }

    /**
     * Obtém preview de pausa.
     */
    suspend fun getPausePreview(): PausePreviewData? {
        val repo = currentRepository ?: return null
        val state = _currentWorkspace.value
        val plan = state.activePlan ?: return null

        return PauseApplier.previewPause(
            plan = plan,
            workouts = state.workouts,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            nivel = "sem_treino",
            retorno = "normal",
            today = LocalDate.now(),
            diasSelecionados = state.diasSelecionados,
            frequenciaExercicio = state.frequenciaExercicio ?: "Outros"
        )
    }

    /**
     * Salva anotações do treino.
     */
    suspend fun saveAnotacoes(logId: Long, anotacoes: String?) {
        currentRepository?.updateWorkoutLogAnotacoes(logId, anotacoes?.ifBlank { null })
    }

    /**
     * Obtém pontos de rota para um treino.
     */
    suspend fun getRoutePointsForWorkout(workoutId: Long): List<com.example.data.local.entities.RoutePointEntity> {
        return currentRepository?.getRoutePoints(workoutId) ?: emptyList()
    }

    /**
     * Limpa todos os dados de todas as modalidades (livre, 2.5K, 5K, 10K).
     * Cancela observadores ativos antes de limpar os bancos para evitar crashes.
     * Após limpeza, remove arquivos de backup, limpa DataStore e hidrata workspace limpo.
     */
    suspend fun clearAllDataForAllModalities(onResult: (Boolean) -> Unit = {}) {
        withContext(Dispatchers.IO) {
            try {
                // 1. Cancela todos os observadores ativos e lembretes agendados
                observeJobs.forEach { it.cancel() }
                observeJobs.clear()
                try {
                    ReminderScheduler.cancelAllReminders(application)
                } catch (e: Exception) {
                    android.util.Log.e("WorkspaceManager", "Erro ao cancelar lembretes", e)
                }

                // 2. Limpa todos os 4 bancos de dados de forma transacional
                val modalities = listOf(
                    DatabaseProvider.MODALIDADE_LIVRE,
                    DatabaseProvider.MODALIDADE_2_5K,
                    DatabaseProvider.MODALIDADE_5K,
                    DatabaseProvider.MODALIDADE_10K
                )

                var allSuccess = true
                for (modality in modalities) {
                    try {
                        val db = DatabaseProvider.getDatabase(modality, application)
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
                    } catch (e: Exception) {
                        android.util.Log.e("WorkspaceManager", "Erro ao limpar banco da modalidade $modality", e)
                        allSuccess = false
                    }
                }

                // 3. Remove arquivos locais de backup e cache
                try {
                    val backupManager = com.example.backup.BackupManager(application)
                    backupManager.deleteBackupFile()
                } catch (e: Exception) {
                    android.util.Log.e("WorkspaceManager", "Erro ao apagar backup local", e)
                }
                try {
                    application.cacheDir.deleteRecursively()
                } catch (_: Exception) {}

                if (!allSuccess) {
                    withContext(Dispatchers.Main) {
                        onResult(false)
                    }
                    return@withContext
                }

                // 4. Reseta estado do wizard
                resetWizardState()

                // 5. Reseta perfil global e limpa DataStore
                try {
                    com.example.data.local.GlobalSessionStore.getInstance(application).clearAll()
                } catch (e: Exception) {
                    android.util.Log.e("WorkspaceManager", "Erro ao limpar GlobalSessionStore", e)
                }
                globalSession.value = GlobalSession()

                // 6. Hidrata workspace limpo para modalidade padrão (5K)
                currentModality = DatabaseProvider.MODALIDADE_5K
                val newDb = DatabaseProvider.getDatabase(DatabaseProvider.MODALIDADE_5K, application)
                currentDb = newDb

                val newRepo = PlanRepository(
                    database = newDb,
                    modalidade = DatabaseProvider.MODALIDADE_5K,
                    profileDao = newDb.runnerProfileDao(),
                    planDao = newDb.planDao(),
                    workoutDao = newDb.workoutDao(),
                    routePointDao = newDb.routePointDao(),
                    workoutLogDao = newDb.workoutLogDao(),
                    activeRunDao = newDb.activeRunDao()
                )
                currentRepository = newRepo

                _uiStateUpdate { current ->
                    current.copy(
                        modality = DatabaseProvider.MODALIDADE_5K,
                        selectedModality = DatabaseProvider.MODALIDADE_5K,
                        isLoading = false,
                        activePlan = null,
                        workouts = emptyList(),
                        todayWorkout = null,
                        nextWorkout = null,
                        workoutLogs = emptyList(),
                        currentScreen = AppScreen.Welcome,
                        screenStack = listOf(AppScreen.Welcome),
                        goalSelected = false,
                        userName = "",
                        tempoSemCorrer = null,
                        frequenciaExercicio = null,
                        tipoExercicio = null
                    )
                }

                // 7. Reinicia observações
                startObservations()

                withContext(Dispatchers.Main) {
                    onResult(true)
                }
            } catch (e: Throwable) {
                android.util.Log.e("WorkspaceManager", "Falha geral em clearAllDataForAllModalities: ${e.message}", e)
                // Se falhar, tenta reiniciar as observações para não deixar o app quebrado
                try {
                    startObservations()
                } catch (_: Exception) {}
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    /**
     * Limpa recursos ao destruir.
     */
    fun destroy() {
        observeJobs.forEach { it.cancel() }
        observeJobs.clear()
        currentRepository = null
        currentDb = null
    }
}