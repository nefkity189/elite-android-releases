package com.example.workspace

import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.WorkoutEntity
import com.example.data.local.entities.ActiveRunEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.generator.GeneratedPlan
import com.example.generator.AdaptiveResult
import com.example.data.local.entities.PlanAdjustmentEntity
import com.example.data.local.entities.AutoAdjustmentEntity
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Estado isolado por modalidade (workspace).
 * Cada modalidade tem sua própria instância deste estado,
 * garantindo total isolamento de dados e contexto.
 */
data class WorkspaceState(
    // Identificador da modalidade
    val modality: String = "plano_5k",

    // Modalidade selecionada (para GoalSelection)
    val selectedModality: String = "plano_5k",

    // Dados do plano ativo
    val activePlan: PlanEntity? = null,
    val workouts: List<WorkoutEntity> = emptyList(),
    val workoutLogs: List<com.example.data.local.entities.WorkoutLogEntity> = emptyList(),
    val todayWorkout: WorkoutEntity? = null,
    val nextWorkout: WorkoutEntity? = null,
    val currentDate: LocalDate = LocalDate.now(),

    // Perfil do usuário (copia do banco local da modalidade)
    val userName: String = "",
    val tempoSemCorrer: String? = null,
    val frequenciaExercicio: String? = null,
    val tipoExercicio: String? = null,
    val diasPorSemana: Int = 3,
    val diasSelecionados: List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.FRIDAY
    ),
    val dataInicio: LocalDate = LocalDate.now(),
    val totalSemanas: Int = 9,
    val minSemanas: Int = 6,
    val dismissBatteryWarning: Boolean = false,
    val voiceEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val lembretesAtivos: Boolean = false,
    val lembreteHora: Int = 7,
    val lembreteMinuto: Int = 0,
    val avisoDistanciaAtivo: Boolean = true,
    val avisoDistanciaMetros: Int = 1000,

    // Wizard/Preview state
    val previewPlan: GeneratedPlan? = null,
    val previewErrorMessage: String? = null,

    // Ajustes (P11)
    val ajustesAutomaticos: Boolean = true,
    val modoAjuste: String = "cauteloso",
    val autoAdjustments: List<AutoAdjustmentEntity> = emptyList(),
    val activePauseAdjustment: PlanAdjustmentEntity? = null,

    // P12: Check-in pré-treino
    val showPreWorkoutCheckin: Boolean = false,
    val preWorkoutCheckinWorkout: WorkoutEntity? = null,

    // Navegação
    val currentScreen: AppScreen = AppScreen.Welcome,
    val screenStack: List<AppScreen> = listOf(AppScreen.Welcome),

    // Recuperação de corrida ativa
    val activeRunToRecover: ActiveRunEntity? = null,
    val runSummaryData: RunSummaryData? = null,

    // Sugestão pendente do motor de ajuste
    val adaptiveSuggestion: AdaptiveResult? = null,

    // Controle de fluxo
    val isLoading: Boolean = true,
    val goalSelected: Boolean = false
)

/**
 * Dados de resumo de corrida para a tela WorkoutSummary.
 */
data class RunSummaryData(
    val workoutId: Long,
    val workoutTitle: String,
    val elapsedSeconds: Long,
    val totalDistanceMeters: Double,
    val gpsDiagnostics: com.example.tracker.GpsDiagnostics? = null
)

/**
 * Telas da navegação do app.
 */
sealed interface AppScreen {
    data object Welcome : AppScreen
    data object QuizQuestion1 : AppScreen
    data object QuizQuestion2 : AppScreen
    data object QuizQuestion3 : AppScreen
    data object GoalSelection : AppScreen
    data object DaysSelection : AppScreen
    data object DurationSelection : AppScreen
    data object PlanPreview : AppScreen
    data object Today : AppScreen
    data object FullPlan : AppScreen
    data object Progress : AppScreen
    data object Profile : AppScreen
    data object ActiveRun : AppScreen
    data object WorkoutSummary : AppScreen
    data object PlanAdjustments : AppScreen
}

/**
 * Sessão global que persiste entre workspaces.
 * Contém dados que devem ser compartilhados ou sincronizados
 * entre todas as modalidades.
 */
data class GlobalSession(
    // Nome do usuário (sincronizado globalmente)
    val userName: String = "",

    // Última modalidade acessada (para restauração)
    val lastActiveModality: String = "plano_5k",

    // Configurações globais do app (não específicas de plano)
    val dismissBatteryWarning: Boolean = false,
    val voiceEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,

    // Backup/Restore
    val lastBackupTimestamp: Long = 0,

    // Modalidades que já completaram o onboarding (Quiz + configuração de plano)
    val completedOnboardingModalities: Set<String> = emptySet(),

    // Metadados
    val appVersion: String = "1.0",
    val firstLaunchTimestamp: Long = System.currentTimeMillis()
)