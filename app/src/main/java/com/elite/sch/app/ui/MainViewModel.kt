package com.elite.sch.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elite.sch.app.data.local.AppDatabase
import com.elite.sch.app.data.local.BaseAppDatabase
import com.elite.sch.app.data.local.DatabaseProvider
import com.elite.sch.app.data.local.GlobalSessionStore
import com.elite.sch.app.backup.BackupManager
import com.elite.sch.app.data.local.PlanRepository
import com.elite.sch.app.data.local.entities.ActiveRunEntity
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.RunnerProfileEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.GeneratedPlan
import com.elite.sch.app.generator.PlanGenerator
import com.elite.sch.app.generator.PlanInput
import com.elite.sch.app.generator.PlanTemplate
import com.elite.sch.app.generator.Template10K
import com.elite.sch.app.generator.Template25K
import com.elite.sch.app.generator.TemplatePlanInput
import com.elite.sch.app.tracker.PaceCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import com.elite.sch.app.generator.AdaptiveResult
import com.elite.sch.app.generator.PlanRegenerator
import com.elite.sch.app.generator.RegenerationResult
import com.elite.sch.app.generator.PauseApplier
import com.elite.sch.app.generator.PausePreviewData
import com.elite.sch.app.data.local.entities.PlanAdjustmentEntity
import com.elite.sch.app.worker.ReminderScheduler
import java.time.DayOfWeek
import java.time.LocalDate
import com.elite.sch.app.workspace.WorkspaceManager
import com.elite.sch.app.workspace.WorkspaceState
import com.elite.sch.app.workspace.GlobalSession
import com.elite.sch.app.workspace.AppScreen
import com.elite.sch.app.workspace.RunSummaryData
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import com.elite.sch.app.account.AccountDialog
import com.elite.sch.app.account.AccountStore
import com.elite.sch.app.account.AuthState
import com.elite.sch.app.account.FirebaseAccountRepository
import com.elite.sch.app.account.LoginPrefs
import com.elite.sch.app.account.UserAccount
import kotlinx.coroutines.withTimeoutOrNull

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val accountRepository = FirebaseAccountRepository(AccountStore.get(application))
    val accountState: StateFlow<AuthState> = accountRepository.authState
    private val loginPrefs = LoginPrefs(application)
    private val _restoreFinished = MutableStateFlow(false)
    val restoreFinished: StateFlow<Boolean> = _restoreFinished.asStateFlow()
    val loginDismissed: Boolean get() = loginPrefs.isDismissed()
    private val _accountDialog = MutableStateFlow(AccountDialog.None)
    val accountDialog: StateFlow<AccountDialog> = _accountDialog.asStateFlow()

    // Sessão global (persistida via DataStore)
    private val globalSessionStore = GlobalSessionStore.getInstance(application)
    private val _globalSession = MutableStateFlow(GlobalSession())
    val globalSession: StateFlow<GlobalSession> = _globalSession.asStateFlow()

    // Estado reativo da existência do arquivo de backup (atualizado após export/import/delete).
    private val _backupFileExists = MutableStateFlow(false)
    val backupFileExists: StateFlow<Boolean> = _backupFileExists.asStateFlow()

    // Gerenciador de workspaces
    private val workspaceManager: WorkspaceManager

    // Estado do workspace atual (delegado ao WorkspaceManager)
    val uiState: StateFlow<WorkspaceState>

    init {
        viewModelScope.launch {
            withTimeoutOrNull(4_000L) { accountRepository.restore() }
            _restoreFinished.value = true
        }
        // Inicializa WorkspaceManager
        workspaceManager = WorkspaceManager(application, _globalSession, viewModelScope)

        // Expõe o estado do workspace atual
        uiState = workspaceManager.currentWorkspace

        // Carrega sessão global do DataStore e sincroniza com StateFlow
        loadGlobalSessionFromStore()

        // Inicializa estado reativo do arquivo de backup de forma assíncrona
        // para evitar bloqueio durante a construção do ViewModel
        viewModelScope.launch {
            refreshBackupFileState()
        }
    }

    /**
     * Carrega a sessão global do DataStore e observa mudanças para persistência.
     */
    private fun loadGlobalSessionFromStore() {
        viewModelScope.launch {
            val session = globalSessionStore.globalSession.first()
            _globalSession.value = session
            // Hidrata o workspace com a última modalidade salva
            workspaceManager.hydrateWorkspace(session.lastActiveModality)

            // Observa mudanças subsequentes na sessão global para persistir no DataStore
            _globalSession
                .drop(1)
                .collect { updatedSession ->
                    globalSessionStore.updateAll(updatedSession)
                }
        }
    }

    // ============ DELEGAÇÃO PARA WORKSPACE MANAGER ============

    fun setUserName(name: String) {
        workspaceManager.setUserName(name)
        _globalSession.value = _globalSession.value.copy(userName = name)
    }

    fun onSignInClick(login: String, password: String) {
        if (accountState.value is AuthState.SigningIn) return
        viewModelScope.launch {
            accountRepository.signIn(login, password)
            val signedIn = accountState.value as? AuthState.SignedIn ?: return@launch
            val currentName = _globalSession.value.userName.trim()
            if (currentName.isEmpty() || currentName == "Recruta") setUserName(signedIn.account.displayName)
        }
    }

    fun onGoogleSignIn(idToken: String) {
        if (accountState.value is AuthState.SigningIn) return
        viewModelScope.launch {
            accountRepository.signInWithGoogle(idToken)
            val signedIn = accountState.value as? AuthState.SignedIn ?: return@launch
            val currentName = _globalSession.value.userName.trim()
            if (currentName.isEmpty() || currentName == "Recruta") setUserName(signedIn.account.displayName)
        }
    }

    fun onSkipLogin() {
        loginPrefs.dismiss()
    }

    fun openAccountDialog(dialog: AccountDialog) { _accountDialog.value = dialog }
    fun dismissAccountDialog() { _accountDialog.value = AccountDialog.None }
    fun onSignOutConfirm() { viewModelScope.launch { accountRepository.signOut(); dismissAccountDialog() } }
    fun onUfConfirm(uf: String) { viewModelScope.launch {
        try {
            accountRepository.updateUf(uf)
            dismissAccountDialog()
        } catch (error: Exception) {
            android.widget.Toast.makeText(getApplication(), "Não foi possível salvar o estado. Verifique a conexão e tente novamente.", android.widget.Toast.LENGTH_LONG).show()
        }
    } }

    fun setDismissBatteryWarning(dismiss: Boolean) {
        workspaceManager.setDismissBatteryWarning(dismiss)
        _globalSession.value = _globalSession.value.copy(dismissBatteryWarning = dismiss)
    }

    fun setVoiceEnabled(enabled: Boolean) {
        workspaceManager.setVoiceEnabled(enabled)
        _globalSession.value = _globalSession.value.copy(voiceEnabled = enabled)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        workspaceManager.setVibrationEnabled(enabled)
        _globalSession.value = _globalSession.value.copy(vibrationEnabled = enabled)
    }

    fun setAvisoDistanciaAtivo(ativo: Boolean) {
        workspaceManager.setAvisoDistanciaAtivo(ativo)
    }

    fun setAvisoDistanciaMetros(metros: Int) {
        workspaceManager.setAvisoDistanciaMetros(metros)
    }

    fun setShowMapDuringRun(show: Boolean) {
        workspaceManager.setShowMapDuringRun(show)
    }

    fun setDefaultCameraMode(mode: String) {
        workspaceManager.setDefaultCameraMode(mode)
    }

    fun navigateTo(screen: AppScreen) {
        workspaceManager.navigateTo(screen)
    }

    fun switchTab(screen: AppScreen) {
        workspaceManager.switchTab(screen)
    }

    fun navigateBack() {
        workspaceManager.navigateBack()
    }

    fun setTempoSemCorrer(value: String) {
        workspaceManager.setTempoSemCorrer(value)
    }

    fun setFrequenciaExercicio(value: String) {
        workspaceManager.setFrequenciaExercicio(value)
    }

    fun setTipoExercicio(value: String) {
        workspaceManager.setTipoExercicio(value)
    }

    fun setDiasPorSemana(count: Int) {
        workspaceManager.setDiasPorSemana(count)
    }

    fun toggleDiaSemana(day: DayOfWeek) {
        workspaceManager.toggleDiaSemana(day)
    }

    fun setDataInicio(date: LocalDate) {
        workspaceManager.setDataInicio(date)
    }

    fun setTotalSemanas(weeks: Int) {
        workspaceManager.setTotalSemanas(weeks)
    }

    fun generatePreview() {
        workspaceManager.generatePreview()
    }

    fun dismissPreviewError() {
        // WorkspaceManager doesn't have this, but we can reset error via state update
        // For now, we'll handle it by the workspace manager's state
    }

    suspend fun startPlan() {
        workspaceManager.startPlan()
    }

    suspend fun backToGoalSelection() {
        workspaceManager.backToGoalSelection()
    }

    suspend fun selectModality(modality: String) {
        workspaceManager.selectModality(modality)
        _globalSession.value = _globalSession.value.copy(lastActiveModality = modality)
    }

    /** Marca o plano atual como aceito e ativo (chamado do PlanPreviewScreen). */
    suspend fun acceptCurrentPlan(): Boolean {
        val repo = workspaceManager.getRepository() ?: return false
        val plan = workspaceManager.currentWorkspace.value.activePlan ?: return false
        return repo.acceptPlan(plan.id)
    }

    fun generateTemplatePlan() {
        workspaceManager.generateTemplatePlan()
    }

    fun discardActiveRun() {
        workspaceManager.discardActiveRun()
    }

    fun refreshToday() {
        workspaceManager.refreshToday()
    }

    fun onRunFinished(
        workoutId: Long,
        workoutTitle: String,
        elapsedSeconds: Long,
        totalDistanceMeters: Double
    ) {
        workspaceManager.onRunFinished(workoutId, workoutTitle, elapsedSeconds, totalDistanceMeters)
    }

    fun startFreeRun(onStart: (WorkoutEntity, Boolean) -> Unit) {
        workspaceManager.startFreeRun(onStart)
    }

    fun saveWorkoutSummary(
        esforcoPercebido: Int,
        anotacoes: String? = null,
        dor: Int = 0,
        regiaoDor: String? = null
    ) {
        val summary = workspaceManager.currentWorkspace.value.runSummaryData ?: return
        viewModelScope.launch {
            val repo = workspaceManager.getRepository() ?: return@launch
            val pace = PaceCalculator.calculateAveragePaceSeconds(
                totalSeconds = summary.elapsedSeconds,
                totalDistanceMeters = summary.totalDistanceMeters
            )
            val (_, sugestao) = repo.finishRunAndAdjust(
                planId = workspaceManager.currentWorkspace.value.activePlan?.id,
                workoutId = summary.workoutId,
                duracaoSegundos = summary.elapsedSeconds,
                distanciaMetros = summary.totalDistanceMeters,
                ritmoMedioSegundos = pace,
                esforcoPercebido = esforcoPercebido,
                anotacoes = anotacoes,
                diagnostics = summary.gpsDiagnostics,
                dor = dor,
                regiaoDor = regiaoDor,
                today = workspaceManager.currentWorkspace.value.currentDate
            )
            // Limpa o resumo após salvar (evita salvamento repetido / dados stale)
            workspaceManager.clearRunSummaryData()
        }
    }

    /** Abre o check-in pré-treino para o treino especificado. */
    fun openPreWorkoutCheckin(workout: WorkoutEntity) {
        // Delegado ao WorkspaceManager.startPlannedWorkout, que exibe o overlay
        // de check-in pré-treino. O início efetivo da corrida é wired em
        // confirmPreWorkoutCheckin (MainActivity), não aqui.
        workspaceManager.startPlannedWorkout(workout) { _, _ -> }
    }

    /** Fecha o check-in pré-treino sem salvar. */
    fun closePreWorkoutCheckin() {
        workspaceManager.closePreWorkoutCheckin()
    }

    /** Confirma o check-in pré-treino. */
    fun confirmPreWorkoutCheckin(energia: Int, sono: Int, onStart: (WorkoutEntity, Boolean) -> Unit) {
        workspaceManager.confirmPreWorkoutCheckin(energia, sono, onStart)
    }

    /** Aplica a sugestão pendente (ex.: avançar uma etapa no modo cauteloso). */
    fun acceptAdaptiveSuggestion(originWorkoutId: Long) {
        val repo = workspaceManager.getRepository() ?: return
        val sugestao = workspaceManager.currentWorkspace.value.adaptiveSuggestion ?: return
        val planId = workspaceManager.currentWorkspace.value.activePlan?.id ?: return
        viewModelScope.launch {
            repo.applyAdaptiveResult(planId, originWorkoutId, sugestao.copy(precisaConfirmacao = false))
        }
    }

    fun dismissAdaptiveSuggestion() {
        // Handled by WorkspaceManager state
    }

    fun undoAutoAdjustment(adjustmentId: Long) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch { repo.undoAdjustment(adjustmentId) }
    }

    fun setAjustesAutomaticos(ativo: Boolean) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch { repo.updateAjustesAutomaticos(ativo) }
    }

    fun setModoAjuste(modo: String) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch { repo.updateModoAjuste(modo) }
    }

    fun markWorkoutCompleted(workoutId: Long) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch {
            repo.markWorkoutCompleted(workoutId)
        }
    }

    fun markWorkoutSkipped(workoutId: Long) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch {
            repo.markWorkoutSkipped(workoutId)
            val planId = workspaceManager.currentWorkspace.value.activePlan?.id
            if (planId != null) {
                try {
                    repo.evaluateAfterSkip(planId, workoutId, workspaceManager.currentWorkspace.value.currentDate)
                } catch (e: Exception) {
                    // O ajuste é opcional: o treino já foi marcado como pulado.
                }
            }
        }
    }

    fun resetWorkoutStatus(workoutId: Long) {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch {
            repo.resetWorkoutStatus(workoutId)
        }
    }

    fun resetEntirePlan() {
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch {
            ReminderScheduler.cancelAllReminders(getApplication())
            repo.resetAll()
            workspaceManager.resetWizardState()
        }
    }

    fun setLembretes(ativos: Boolean, hora: Int, minuto: Int) {
        workspaceManager.setLembretes(ativos, hora, minuto)
        viewModelScope.launch {
            ReminderScheduler.scheduleDailyReminder(getApplication(), workspaceManager.getCurrentModality(), hora, minuto, ativos)
        }
    }

    fun deleteCurrentPlan(onResult: (Boolean) -> Unit = {}) {
        val repo = workspaceManager.getRepository() ?: return
        val plan = workspaceManager.currentWorkspace.value.activePlan ?: return
        val modality = workspaceManager.getCurrentModality()
        viewModelScope.launch {
            // Set loading state
            workspaceManager.updateLoadingState(true)
            try {
                ReminderScheduler.cancelAllReminders(getApplication())
                android.util.Log.d("MainViewModel", "Tentando excluir plano: id=${plan.id} modalidade=$modality")
                val success = repo.deletePlanWithVerification(plan.id)
                android.util.Log.d("MainViewModel", "deletePlanWithVerification result: $success")
                if (success) {
                    workspaceManager.resetWizardState()
                    // Libera apenas o quiz dessa distância (spec §10 #9-11)
                    workspaceManager.clearOnboardingForModality(modality)
                    // Navega para Today (Home) após exclusão bem-sucedida
                    workspaceManager.switchTab(AppScreen.Today)
                    onResult(true)
                } else {
                    // Falha na verificação: plano ainda existe
                    onResult(false)
                }
            } catch (e: Throwable) {
                // Erro durante exclusão (inclui Errors, CancellationException, etc.)
                android.util.Log.e("MainViewModel", "Erro ao excluir plano: ${plan.id}", e)
                onResult(false)
            } finally {
                workspaceManager.updateLoadingState(false)
            }
        }
    }

    fun previewRegeneration(newEndDate: LocalDate): Result<RegenerationResult> {
        val plan = workspaceManager.currentWorkspace.value.activePlan ?: return Result.failure(IllegalStateException("Nenhum plano ativo."))
        val workouts = workspaceManager.currentWorkspace.value.workouts
        val today = workspaceManager.currentWorkspace.value.currentDate
        val dias = workspaceManager.currentWorkspace.value.diasSelecionados
        val freq = workspaceManager.currentWorkspace.value.frequenciaExercicio ?: "Outros"

        return try {
            val res = PlanRegenerator.regenerate(
                plan = plan,
                currentWorkouts = workouts,
                today = today,
                newEndDate = newEndDate,
                diasSelecionados = dias,
                frequenciaExercicio = freq
            )
            Result.success(res)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun applyRegeneration(newEndDate: LocalDate, onComplete: () -> Unit = {}) {
        val result = previewRegeneration(newEndDate).getOrNull() ?: return
        val repo = workspaceManager.getRepository() ?: return
        viewModelScope.launch {
            repo.applyRegeneration(
                updatedPlan = result.updatedPlan,
                toDelete = result.toDelete,
                toCreate = result.toCreate
            )
            val state = workspaceManager.currentWorkspace.value
            if (state.lembretesAtivos) {
                ReminderScheduler.scheduleDailyReminder(
                    getApplication(),
                    workspaceManager.getCurrentModality(),
                    state.lembreteHora,
                    state.lembreteMinuto,
                    true
                )
            }
            onComplete()
        }
    }

    fun previewPause(
        startDate: LocalDate,
        endDate: LocalDate,
        nivel: String,
        retorno: String
    ): Result<PausePreviewData> {
        val plan = workspaceManager.currentWorkspace.value.activePlan ?: return Result.failure(IllegalStateException("Nenhum plano ativo."))
        val workouts = workspaceManager.currentWorkspace.value.workouts
        val today = workspaceManager.currentWorkspace.value.currentDate
        val dias = workspaceManager.currentWorkspace.value.diasSelecionados
        val freq = workspaceManager.currentWorkspace.value.frequenciaExercicio ?: "Outros"

        return try {
            val preview = PauseApplier.previewPause(
                plan = plan,
                workouts = workouts,
                startDate = startDate,
                endDate = endDate,
                nivel = nivel,
                retorno = retorno,
                today = today,
                diasSelecionados = dias,
                frequenciaExercicio = freq
            )
            Result.success(preview)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun applyPause(
        previewData: PausePreviewData,
        tipo: String,
        startDate: LocalDate,
        endDate: LocalDate,
        nivel: String,
        retorno: String,
        onComplete: () -> Unit = {}
    ) {
        val repo = workspaceManager.getRepository() ?: return
        val plan = workspaceManager.currentWorkspace.value.activePlan ?: return
        viewModelScope.launch {
            val adjustment = PlanAdjustmentEntity(
                planId = plan.id,
                tipo = tipo,
                inicio = startDate.toString(),
                fim = endDate.toString(),
                nivel = nivel,
                retorno = retorno,
                criadoEm = System.currentTimeMillis()
            )
            repo.applyPauseAdjustment(
                adjustment = adjustment,
                updatedWorkoutsWithPause = previewData.updatedWorkoutsWithPause,
                regenerationResult = previewData.regenerationResult
            )
            val state = workspaceManager.currentWorkspace.value
            if (state.lembretesAtivos) {
                ReminderScheduler.scheduleDailyReminder(
                    getApplication(),
                    workspaceManager.getCurrentModality(),
                    state.lembreteHora,
                    state.lembreteMinuto,
                    true
                )
            }
            onComplete()
        }
    }

    fun clearAllUserData(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            workspaceManager.updateLoadingState(true)
            try {
                ReminderScheduler.cancelAllReminders(getApplication())
                workspaceManager.clearAllDataForAllModalities { success ->
                    if (success) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
            } catch (e: Throwable) {
                android.util.Log.e("MainViewModel", "Erro ao limpar todos os dados", e)
                onResult(false)
            } finally {
                workspaceManager.updateLoadingState(false)
            }
        }
    }

    /**
     * Exporta backup completo de todas as modalidades para arquivo JSON.
     */
    fun exportBackup(onResult: (Result<java.io.File>) -> Unit) {
        val backupManager = BackupManager(getApplication())
        backupManager.exportBackup { result ->
            // Atualiza o estado reativo do arquivo de backup (ex.: botão "Restaurar backup").
            android.util.Log.d("MainViewModel", "exportBackup callback, calling refreshBackupFileState")
            refreshBackupFileState()
            android.util.Log.d("MainViewModel", "exportBackup callback, _backupFileExists.value = ${_backupFileExists.value}")
            onResult(result)
        }
    }

    /**
     * Exporta backup completo para um arquivo específico (não diretório).
     * Usado quando o usuário escolhe o arquivo via CreateDocument.
     */
    fun exportBackupToFile(destinationFile: java.io.File, onResult: (Result<java.io.File>) -> Unit) {
        val backupManager = BackupManager(getApplication())
        backupManager.exportBackupToFile(destinationFile) { result ->
            refreshBackupFileState()
            onResult(result)
        }
    }

    /**
     * Abre o seletor de compartilhamento do sistema com o backup salvo.
     * O usuário escolhe onde mandar o arquivo (nuvem, e-mail, outro app) em vez de
     * o app decidir por ele.
     */
    fun shareBackup(context: android.content.Context): Boolean {
        val backupManager = BackupManager(getApplication())
        return backupManager.shareBackupFile(context)
    }

    /** Refresh do estado do arquivo de backup ao entrar na tela de perfil. */
    fun refreshBackupOnProfileEnter() {
        refreshBackupFileState()
    }

    /**
     * Importa/restaura backup de um arquivo JSON.
     */
    fun importBackup(file: java.io.File, onResult: (Result<Unit>) -> Unit) {
        val backupManager = BackupManager(getApplication())
        backupManager.importBackup(file) { result ->
            if (result.isSuccess) {
                // Após restore, hidrata a modalidade ativa salva na sessão global
                // (lastActiveModality do DataStore, que persiste entre reinícios)
                viewModelScope.launch {
                    workspaceManager.hydrateWorkspace(globalSession.value.lastActiveModality)
                }
            }
            refreshBackupFileState()
            onResult(result)
        }
    }

    // Estado reativo da existência do arquivo de backup (atualizado após export/import/delete).

    /**
     * Verifica se existe arquivo de backup salvo e atualiza o estado reativo.
     */
    fun hasBackupFile(): Boolean {
        val exists = BackupManager(getApplication()).hasBackupFile()
        _backupFileExists.value = exists
        return exists
    }

    /** Força uma reavaliação do estado do arquivo de backup (úpico após operações). */
    fun refreshBackupFileState() {
        val exists = BackupManager(getApplication()).hasBackupFile()
        android.util.Log.d("MainViewModel", "refreshBackupFileState: _backupFileExists.value = $exists")
        _backupFileExists.value = exists
    }

    /**
     * Obtém o arquivo de backup se existir.
     */
    fun getBackupFile(): java.io.File? {
        val backupManager = BackupManager(getApplication())
        return backupManager.getBackupFile()
    }

    /**
     * Deleta o arquivo de backup local.
     */
    fun deleteBackupFile(): Boolean {
        val backupManager = BackupManager(getApplication())
        val deleted = backupManager.deleteBackupFile()
        if (deleted) {
            _backupFileExists.value = false
        }
        return deleted
    }

    /**
     * Inicia treino planejado com check-in pré-treino.
     */
    fun startPlannedWorkout(workout: WorkoutEntity, onStart: (WorkoutEntity, Boolean) -> Unit) {
        workspaceManager.startPlannedWorkout(workout, onStart)
    }

    /**
     * Aplica ajuste de pausa (P11).
     */
    suspend fun applyPauseAdjustment() {
        workspaceManager.applyPauseAdjustment()
    }

    /**
     * Regenera plano (P11).
     */
    suspend fun regeneratePlan() {
        workspaceManager.regeneratePlan()
    }

    /**
     * Obtém preview de pausa.
     */
    suspend fun getPausePreview(): PausePreviewData? {
        return workspaceManager.getPausePreview()
    }

    /**
     * Salva anotações do treino.
     */
    suspend fun saveAnotacoes(logId: Long, anotacoes: String?) {
        workspaceManager.saveAnotacoes(logId, anotacoes)
    }

    /**
     * Obtém pontos de rota para um treino.
     */
    suspend fun getRoutePointsForWorkout(workoutId: Long): List<com.elite.sch.app.data.local.entities.RoutePointEntity> {
        return workspaceManager.getRoutePointsForWorkout(workoutId)
    }

    /**
     * Obtém a modalidade atual.
     */
    fun getCurrentModality(): String = workspaceManager.getCurrentModality()

    /**
     * Obtém o repositório atual.
     */
    fun getRepository(): PlanRepository? = workspaceManager.getRepository()

    override fun onCleared() {
        super.onCleared()
        workspaceManager.destroy()
    }
}
