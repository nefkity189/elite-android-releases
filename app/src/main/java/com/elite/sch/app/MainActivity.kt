package com.elite.sch.app

import com.elite.sch.app.ui.theme.EliteBottomBarSurface
import com.elite.sch.app.ui.theme.EliteBorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import java.io.File
import java.io.FileOutputStream
import android.content.ContentResolver
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.viewModels
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import com.elite.sch.app.service.RunTrackingService
import com.elite.sch.app.update.AppRelease
import com.elite.sch.app.update.AppUpdatePrompt
import com.elite.sch.app.workspace.AppScreen
import com.elite.sch.app.ui.MainViewModel
import com.elite.sch.app.account.AccountDialog
import com.elite.sch.app.account.AuthState
import com.elite.sch.app.account.REQUIRE_LOGIN_FOR_COMPETITION
import com.elite.sch.app.ui.components.EliteAdaptiveContainer
import com.elite.sch.app.ui.components.EliteBottomNavIconSize
import com.elite.sch.app.ui.components.EliteSplash
import com.elite.sch.app.ui.components.EliteBottomBarHeight
import com.elite.sch.app.ui.components.EliteCenterActionSize
import com.elite.sch.app.ui.components.EliteCenterSymbolSize
import com.elite.sch.app.ui.components.EliteQuickLauncherBottomSheet
import com.elite.sch.app.ui.screens.competition.CompetitionFlowScreen
import com.elite.sch.app.ui.components.EliteResponsiveFrame
import com.elite.sch.app.ui.components.EliteSymbol
import com.elite.sch.app.ui.components.BrandGradientButton
import com.elite.sch.app.ui.components.RunnaBottomNav
import com.elite.sch.app.ui.components.rememberEliteWindowSizeClass
import com.elite.sch.app.ui.screens.ActiveRunScreen
import com.elite.sch.app.ui.screens.DaysSelectionScreen
import com.elite.sch.app.ui.screens.DurationScreen
import com.elite.sch.app.ui.screens.FullPlanScreen
import com.elite.sch.app.ui.screens.GoalScreen
import com.elite.sch.app.ui.screens.GoalSelectionScreen
import com.elite.sch.app.ui.screens.PlanPreviewScreen
import com.elite.sch.app.ui.screens.PlanAdjustmentsScreen
import com.elite.sch.app.ui.screens.PrivacyDataScreen
import com.elite.sch.app.ui.screens.ProfileScreen
import com.elite.sch.app.ui.screens.ProgressScreen
import com.elite.sch.app.ui.screens.QuizQuestionScreen
import com.elite.sch.app.ui.screens.TodayScreen
import com.elite.sch.app.ui.screens.WelcomeScreen
import com.elite.sch.app.ui.screens.WorkoutSummaryScreen
import com.elite.sch.app.ui.screens.LoginScreen
import com.elite.sch.app.ui.state.CameraMode
import com.elite.sch.app.ui.state.MapPreferences
import com.elite.sch.app.ui.theme.EliteBackground
import com.elite.sch.app.ui.theme.EliteCard
import com.elite.sch.app.ui.theme.ElitePurple
import com.elite.sch.app.ui.theme.EliteTextPrimary
import com.elite.sch.app.ui.theme.EliteTextSecondary
import com.elite.sch.app.ui.theme.EliteTheme
import com.elite.sch.app.ui.theme.EliteTurquoise
import com.elite.sch.app.ui.theme.BrandGradient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var voiceCommander: VoiceCommander

    private fun downloadAppUpdate(release: AppRelease) {
        runCatching {
            val request = DownloadManager.Request(Uri.parse(release.apkUrl))
                .setTitle("Atualização do ELITE ${release.versionName}")
                .setDescription("O Android avisará quando o download terminar.")
                .setMimeType("application/vnd.android.package-archive")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(
                    this,
                    Environment.DIRECTORY_DOWNLOADS,
                    "elite-${release.versionName}.apk",
                )
            (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
            android.widget.Toast.makeText(
                this,
                "Download iniciado. Toque na notificação ao terminar para instalar.",
                android.widget.Toast.LENGTH_LONG,
            ).show()
        }.onFailure { error ->
            android.widget.Toast.makeText(
                this,
                error.message ?: "Não foi possível baixar a atualização.",
                android.widget.Toast.LENGTH_LONG,
            ).show()
        }
    }

    private fun launchGoogleSignIn(onToken: (String) -> Unit) {
        lifecycleScope.launch {
            try {
                // Use the explicit Google sign-in option for a first login. This opens
                // the account chooser reliably on devices that have no authorized
                // credential yet (GetGoogleIdOption may immediately cancel there).
                val googleIdOption = GetSignInWithGoogleOption.Builder(
                    getString(R.string.default_web_client_id)
                ).build()
                val result = CredentialManager.create(this@MainActivity).getCredential(
                    this@MainActivity,
                    GetCredentialRequest(listOf(googleIdOption))
                )
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    onToken(GoogleIdTokenCredential.createFrom(credential.data).idToken)
                } else {
                    throw IllegalStateException("Credencial Google inválida.")
                }
            } catch (error: GetCredentialException) {
                android.widget.Toast.makeText(
                    this@MainActivity,
                    "Login Google cancelado ou indisponível.",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } catch (error: Exception) {
                android.widget.Toast.makeText(
                    this@MainActivity,
                    error.message ?: "Não foi possível entrar com Google.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val system = installSplashScreen()
        super.onCreate(savedInstanceState)
        system.setOnExitAnimationListener { it.remove() }
        enableEdgeToEdge()
        voiceCommander = VoiceCommander(applicationContext)
        setContent {
            EliteTheme {
                var showSplash by rememberSaveable { mutableStateOf(true) }
                val restoreFinished by viewModel.restoreFinished.collectAsStateWithLifecycle()
                val accountState by viewModel.accountState.collectAsStateWithLifecycle()
                val showLogin = restoreFinished && accountState !is AuthState.SignedIn
                Box(Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .semantics { testTagsAsResourceId = true },
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(
                            viewModel = viewModel,
                            onPlayMission = voiceCommander::playMission,
                            onGoogleSignIn = { launchGoogleSignIn(viewModel::onGoogleSignIn) }
                        )
                    }
                    if (!showSplash && showLogin) {
                        LoginScreen(
                            authState = accountState,
                            onGoogle = { launchGoogleSignIn(viewModel::onGoogleSignIn) },
                            onTerms = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://elitesch.web.app/termos.html")))
                            },
                            onPrivacy = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://elitesch.web.app/privacidade.html")))
                            },
                        )
                    }
                    if (showSplash) EliteSplash(isReady = { restoreFinished }, onFinished = { showSplash = false })
                    if (!showSplash && !showLogin) AppUpdatePrompt(onUpdate = ::downloadAppUpdate)
                }
            }
        }
    }

    override fun onDestroy() {
        if (::voiceCommander.isInitialized) voiceCommander.release()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    onPlayMission: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accountState by viewModel.accountState.collectAsStateWithLifecycle()
    val accountDialog by viewModel.accountDialog.collectAsStateWithLifecycle()
    val runState by RunTrackingService.runState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val appScope = rememberCoroutineScope()

    var showPermissionDialog by remember { mutableStateOf(false) }

    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions.all { it.value }
            if (!granted) {
                showPermissionDialog = true
            }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(permissionsToRequest.toTypedArray())
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permissões necessárias") },
            text = { Text("Para garantir o melhor monitoramento dos seus treinos via GPS e enviar alertas importantes, precisamos de permissão para acessar sua localização e enviar notificações.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Configurações")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Agora não")
                }
            }
        )
    }

    var showRestBottomSheet by remember { mutableStateOf(false) }
    var showQuickLauncher by remember { mutableStateOf(false) }
    var showCompetition by rememberSaveable { mutableStateOf(false) }
    var awaitingCompetitionUf by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(awaitingCompetitionUf, accountState) {
        val account = (accountState as? AuthState.SignedIn)?.account
        if (awaitingCompetitionUf && account?.uf in com.elite.sch.app.account.BRAZIL_UFS) {
            awaitingCompetitionUf = false
            showCompetition = true
        }
    }
    LaunchedEffect(accountState) {
        val account = (accountState as? AuthState.SignedIn)?.account
        if (accountState is AuthState.SignedOut || (account != null && account.uf !in com.elite.sch.app.account.BRAZIL_UFS)) {
            showCompetition = false
        }
    }
    var selectedDetailLog by remember { mutableStateOf<Pair<WorkoutLogEntity, WorkoutEntity?>?>(null) }

    // Launcher para seleção de arquivo de backup (importação)
    val backupContext = LocalContext.current
    val backupFilePickerLauncher = rememberLauncherForActivityResult(OpenDocument()) { uri: Uri? ->
        uri?.let { selectedUri ->
            val contentResolver: ContentResolver = backupContext.getContentResolver()
            contentResolver.openInputStream(selectedUri).use { inputStream ->
                inputStream?.let { input ->
                    val tempFile = File(backupContext.cacheDir, "backup_import_${System.currentTimeMillis()}.json")
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                    viewModel.importBackup(tempFile) { result ->
                        appScope.launch {
                            val msg = if (result.isSuccess) {
                                "Backup restaurado com sucesso"
                            } else {
                                "Erro ao restaurar backup: ${result.exceptionOrNull()?.message ?: "desconhecido"}"
                            }
                            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    // Launcher para exportar backup (usuário escolhe onde salvar, ex: Downloads)
    val backupExportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val contentResolver: ContentResolver = backupContext.getContentResolver()
            val tempFile = File(backupContext.cacheDir, "backup_export_${System.currentTimeMillis()}.json")
            viewModel.exportBackupToFile(tempFile) { result ->
                // Callback roda em Dispatchers.IO - faz a cópia aqui mesmo (thread background OK)
                if (result.isSuccess) {
                    try {
                        contentResolver.openOutputStream(selectedUri)?.use { output ->
                            tempFile.inputStream().use { it.copyTo(output) }
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "Erro ao copiar backup para destino", e)
                    }
                }
                // Toast precisa da thread principal
                val handler = android.os.Handler(android.os.Looper.getMainLooper())
                handler.post {
                    if (result.isSuccess) {
                        android.widget.Toast.makeText(context, "Backup exportado com sucesso", android.widget.Toast.LENGTH_LONG).show()
                    } else {
                        android.widget.Toast.makeText(context, "Erro ao exportar backup: ${result.exceptionOrNull()?.message ?: "desconhecido"}", android.widget.Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // Diálogo detalhe da corrida aberta via histórico / corridas recentes
    selectedDetailLog?.let { (log, workout) ->
        var detailRoutePoints by remember(log.workoutId) { mutableStateOf<List<RoutePointEntity>>(emptyList()) }
        LaunchedEffect(key1 = log.workoutId) {
            detailRoutePoints = viewModel.getRoutePointsForWorkout(log.workoutId)
        }
        Dialog(
            onDismissRequest = { selectedDetailLog = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            WorkoutSummaryScreen(
                workoutTitle = workout?.titulo ?: "Detalhes da Corrida",
                elapsedSeconds = log.duracaoSegundos,
                totalDistanceMeters = log.distanciaMetros,
                routePoints = detailRoutePoints,
                allCompletedLogs = state.workoutLogs,
                initialRpe = log.esforcoPercebido,
                initialAnotacoes = log.anotacoes,
                isDetailOnly = true,
                finalizadoEm = log.finalizadoEm,
                modalidadeLabel = when (state.selectedModality) {
                    com.elite.sch.app.data.local.DatabaseProvider.MODALIDADE_LIVRE -> "Livre"
                    com.elite.sch.app.data.local.DatabaseProvider.MODALIDADE_2_5K -> "Plano 2.5K"
                    com.elite.sch.app.data.local.DatabaseProvider.MODALIDADE_5K -> "Plano 5K"
                    com.elite.sch.app.data.local.DatabaseProvider.MODALIDADE_10K -> "Plano 10K"
                    else -> "Corrida"
                },
                diagnostics = com.elite.sch.app.tracker.GpsDiagnostics(
                    received = log.gpsRecebidas,
                    accepted = log.gpsAceitas,
                    rejectedAccuracy = log.gpsRejPrecisao,
                    rejectedBeforeStart = log.gpsRejAnteriores,
                    rejectedSpeed = log.gpsRejVelocidade,
                    rejectedDisplacement = log.gpsRejDeslocamento,
                    rejectedWarmup = log.gpsRejInicio,
                    maxGapMillis = log.gpsMaiorIntervaloMs
                ),
                onSaveLog = { _, notes ->
                    appScope.launch { viewModel.saveAnotacoes(log.id, notes) }
                },
                onClose = { selectedDetailLog = null }
            )
        }
    }

    // Folha inferior quando o botão central é tocado em dia de descanso ou sem plano
    if (showRestBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRestBottomSheet = false },
            containerColor = EliteCard,
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = ElitePurple.copy(alpha = 0.2f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Nightlight,
                            contentDescription = null,
                            tint = ElitePurple,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Hoje é dia de descanso.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EliteTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                val next = state.nextWorkout
                val nextInfo = if (next != null) {
                    val dateFmt = try {
                        LocalDate.parse(next.data).format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR")))
                    } catch (_: Exception) {
                        next.data
                    }
                    "Próximo treino: $dateFmt • ${next.titulo}"
                } else {
                    "Recupere suas energias para os próximos treinos."
                }
                Text(
                    text = nextInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EliteTextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                BrandGradientButton(
                    text = "Ver treino",
                    onClick = {
                        showRestBottomSheet = false
                        viewModel.switchTab(AppScreen.FullPlan)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_bottomsheet_ver_treino")
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // Menu rápido do ícone central "A": escolha Corrida Livre, Treino de Hoje ou troque de plano.
    val quickLauncherScope = rememberCoroutineScope()
    EliteQuickLauncherBottomSheet(
        isOpen = showQuickLauncher,
        todayWorkout = state.todayWorkout,
        activeModality = state.selectedModality,
        onDismiss = { showQuickLauncher = false },
        onStartFreeRun = { viewModel.startFreeRun { workout, useGps -> startRunServiceAndNavigate(context = context, viewModel = viewModel, workout = workout, useGps = useGps, voiceEnabled = state.voiceEnabled, vibrationEnabled = state.vibrationEnabled, avisoDistanciaAtivo = state.avisoDistanciaAtivo, avisoDistanciaMetros = state.avisoDistanciaMetros, modality = "livre") } },
        onStartTodayWorkout = { workout -> viewModel.openPreWorkoutCheckin(workout) },
        onSelectModality = { modality ->
            appScope.launch {
                when (modality) {
                    "livre" -> viewModel.selectModality("livre")
                    else -> viewModel.selectModality(modality)
                }
            }
        },
        // Acesso temporariamente aberto enquanto Billing e paywall ficam para a próxima etapa.
        isPremium = true,
        onOpenCompetition = {
            val account = (accountState as? AuthState.SignedIn)?.account
            if (account == null) {
                viewModel.switchTab(AppScreen.Profile)
            } else if (account.uf !in com.elite.sch.app.account.BRAZIL_UFS) {
                awaitingCompetitionUf = true
                viewModel.switchTab(AppScreen.Profile)
                viewModel.openAccountDialog(com.elite.sch.app.account.AccountDialog.UfSheet)
            } else {
                showCompetition = true
            }
        }
    )

    val isMainTabScreen = state.currentScreen is AppScreen.Today ||
            state.currentScreen is AppScreen.FullPlan ||
            state.currentScreen is AppScreen.Progress ||
            state.currentScreen is AppScreen.Profile ||
            state.currentScreen is AppScreen.PrivacyData ||
            state.currentScreen is AppScreen.PlanAdjustments

    // Esconder FAB central quando o check-in pré-treino estiver aberto (evita sobreposição com botão confirmar)
    val showCenterFab = !state.showPreWorkoutCheckin

    if (isMainTabScreen) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (!showCompetition) {
                RunnaBottomNav(
                    selected = when (state.currentScreen) {
                        is AppScreen.Today -> 0
                        is AppScreen.FullPlan -> 1
                        is AppScreen.Progress -> 3
                        is AppScreen.Profile -> 4
                        is AppScreen.PrivacyData -> 4
                        is AppScreen.PlanAdjustments -> 4
                        else -> -1
                    },
                    onSelect = { index ->
                        when (index) {
                            0 -> viewModel.switchTab(AppScreen.Today)
                            1 -> viewModel.switchTab(AppScreen.FullPlan)
                            3 -> viewModel.switchTab(AppScreen.Progress)
                            4 -> viewModel.switchTab(AppScreen.Profile)
                        }
                    },
                    onCoachClick = { showQuickLauncher = true },
                    showCoach = showCenterFab
                )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                EliteResponsiveFrame {
                when (state.currentScreen) {
                    is AppScreen.Today -> {
                        TodayScreen(
                            todayWorkout = state.todayWorkout,
                            nextWorkout = state.nextWorkout,
                            allWorkouts = state.workouts,
                            workoutLogs = state.workoutLogs,
                            userName = state.userName,
                            activeRunToRecover = state.activeRunToRecover,
                            activePauseAdjustment = state.activePauseAdjustment,
                            currentDate = state.currentDate,
                            onStartRunClick = { workout, useGps ->
                                startRunServiceAndNavigate(
                                    context = context,
                                    viewModel = viewModel,
                                    workout = workout,
                                    useGps = useGps,
                                    voiceEnabled = state.voiceEnabled,
                                    vibrationEnabled = state.vibrationEnabled,
                                    avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                                    avisoDistanciaMetros = state.avisoDistanciaMetros,
                                    modality = state.selectedModality
                                )
                            },
                            onResumeActiveRun = { viewModel.navigateTo(AppScreen.ActiveRun) },
                            onDiscardActiveRun = { viewModel.discardActiveRun() },
                            onMarkCompleted = { viewModel.markWorkoutCompleted(it) },
                            onMarkSkipped = { viewModel.markWorkoutSkipped(it) },
                            onResetStatus = { viewModel.resetWorkoutStatus(it) },
                            onViewFullPlan = { viewModel.switchTab(AppScreen.FullPlan) },
                            onResetEntirePlan = { viewModel.resetEntirePlan() },
                            onOpenProfile = { viewModel.switchTab(AppScreen.Profile) },
                            onOpenReminders = { viewModel.switchTab(AppScreen.Profile) },
                            onRequestRoutePoints = { viewModel.getRoutePointsForWorkout(it) },
                            onViewWorkoutDetail = { log, workout ->
                                selectedDetailLog = log to workout
                            },
                            onRefreshToday = { viewModel.refreshToday() },
                            autoAdjustments = state.autoAdjustments,
                            adaptiveSuggestion = state.adaptiveSuggestion,
                            ajustesAutomaticos = state.ajustesAutomaticos,
                            onUndoAdjustment = { viewModel.undoAutoAdjustment(it) },
                            onOpenAdjustments = { viewModel.navigateTo(AppScreen.PlanAdjustments) },
                            onAcceptSuggestion = {
                                viewModel.acceptAdaptiveSuggestion(
                                    state.workoutLogs.maxByOrNull { it.id }?.workoutId ?: 0L
                                )
                            },
                            onDismissSuggestion = { viewModel.dismissAdaptiveSuggestion() },
                            onStartFreeRun = { viewModel.startFreeRun { workout, useGps ->
                                startRunServiceAndNavigate(
                                    context = context,
                                    viewModel = viewModel,
                                    workout = workout,
                                    useGps = useGps,
                                    voiceEnabled = state.voiceEnabled,
                                    vibrationEnabled = state.vibrationEnabled,
                                    avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                                    avisoDistanciaMetros = state.avisoDistanciaMetros,
                                    modality = "livre"
                                )
                            } },
                            isLoading = state.isLoading,
                            isEmpty = state.activePlan == null && state.workouts.isEmpty(),
                            // P12: Check-in pré-treino
                            showPreWorkoutCheckin = state.showPreWorkoutCheckin,
                            preWorkoutCheckinWorkout = state.preWorkoutCheckinWorkout,
                            onConfirmPreWorkoutCheckin = { energia, sono ->
                                appScope.launch {
                                    viewModel.confirmPreWorkoutCheckin(energia, sono) { w, useGps ->
                                        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        if (fine || coarse) {
                                            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                                            val isGpsEnabled = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: true
                                            if (!isGpsEnabled) {
                                                startRunServiceAndNavigate(
                                                    context = context,
                                                    viewModel = viewModel,
                                                    workout = w,
                                                    useGps = true,
                                                    voiceEnabled = state.voiceEnabled,
                                                    vibrationEnabled = state.vibrationEnabled,
                                                    avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                                                    avisoDistanciaMetros = state.avisoDistanciaMetros,
                                                    modality = state.selectedModality
                                                )
                                            } else {
                                                startRunServiceAndNavigate(
                                                    context = context,
                                                    viewModel = viewModel,
                                                    workout = w,
                                                    useGps = true,
                                                    voiceEnabled = state.voiceEnabled,
                                                    vibrationEnabled = state.vibrationEnabled,
                                                    avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                                                    avisoDistanciaMetros = state.avisoDistanciaMetros,
                                                    modality = state.selectedModality
                                                )
                                            }
                                        } else {
                                            startRunServiceAndNavigate(
                                                context = context,
                                                viewModel = viewModel,
                                                workout = w,
                                                useGps = false,
                                                voiceEnabled = state.voiceEnabled,
                                                vibrationEnabled = state.vibrationEnabled,
                                                avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                                                avisoDistanciaMetros = state.avisoDistanciaMetros,
                                                modality = state.selectedModality
                                            )
                                        }
                                    }
                                }
                            },
                            onClosePreWorkoutCheckin = { viewModel.closePreWorkoutCheckin() },
                            onOpenPreWorkoutCheckin = { viewModel.openPreWorkoutCheckin(it) }
                        )
                    }

                    is AppScreen.FullPlan -> {
                        FullPlanScreen(
                            workouts = state.workouts,
                            activePlan = state.activePlan,
                            currentDate = state.currentDate,
                            frequenciaExercicio = state.frequenciaExercicio ?: "Outros",
                            diasSelecionados = state.diasSelecionados,
                            onMarkCompleted = { viewModel.markWorkoutCompleted(it) },
                            onMarkSkipped = { viewModel.markWorkoutSkipped(it) },
                            onResetStatus = { viewModel.resetWorkoutStatus(it) },
                            onPreviewRegeneration = { viewModel.previewRegeneration(it) },
                            onApplyRegeneration = { viewModel.applyRegeneration(it) },
                            onPreviewPause = { start, end, nivel, retorno ->
                                viewModel.previewPause(start, end, nivel, retorno)
                            },
                            onApplyPause = { previewData, tipo, start, end, nivel, retorno ->
                                viewModel.applyPause(previewData, tipo, start, end, nivel, retorno)
                            },
                            onDeletePlan = { callback -> viewModel.deleteCurrentPlan(callback) },
                            onBackClick = { viewModel.switchTab(AppScreen.Today) },
                            onOpenChangePlan = {
                                appScope.launch {
                                    viewModel.deleteCurrentPlan()
                                }
                            },
                            onCreatePlanClick = { viewModel.navigateTo(AppScreen.QuizQuestion1) },
                            isLoading = state.isLoading,
                            isEmpty = state.workouts.isEmpty(),
                            showBackButton = false
                        )
                    }

                    is AppScreen.Progress -> {
                        ProgressScreen(
                            workouts = state.workouts,
                            logs = state.workoutLogs,
                            currentDate = state.currentDate,
                            onSaveAnotacoes = { logId, notes ->
                                appScope.launch { viewModel.saveAnotacoes(logId, notes) }
                            },
                            onRequestRoutePoints = { workoutId ->
                                viewModel.getRoutePointsForWorkout(workoutId)
                            },
                            onViewTodayClick = {
                                viewModel.switchTab(AppScreen.Today)
                            },
                            onViewRunDetail = { log, workout ->
                                selectedDetailLog = log to workout
                            }
                        )
                    }

                    is AppScreen.Profile -> {
                        ProfileScreen(
                            userName = state.userName,
                            voiceEnabled = state.voiceEnabled,
                            vibrationEnabled = state.vibrationEnabled,
                            lembretesAtivos = state.lembretesAtivos,
                            lembreteHora = state.lembreteHora,
                            lembreteMinuto = state.lembreteMinuto,
                            avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                            avisoDistanciaMetros = state.avisoDistanciaMetros,
                            showMapDuringRun = state.showMapDuringRun,
                            defaultCameraMode = state.defaultCameraMode,
                            onUserNameChange = { viewModel.setUserName(it) },
                            onVoiceToggle = { viewModel.setVoiceEnabled(it) },
                            onVibrationToggle = {
                                viewModel.setVibrationEnabled(it)
                                if (it) RunTrackingService.vibrateToggleConfirmation(context)
                            },
                            onLembretesChange = { ativos, hora, minuto ->
                                viewModel.setLembretes(ativos, hora, minuto)
                            },
                            onAvisoDistanciaAtivoToggle = { viewModel.setAvisoDistanciaAtivo(it) },
                            onAvisoDistanciaMetrosChange = { viewModel.setAvisoDistanciaMetros(it) },
                            onShowMapDuringRunToggle = { viewModel.setShowMapDuringRun(it) },
                            onDefaultCameraModeChange = { viewModel.setDefaultCameraMode(it) },
                            ajustesAutomaticos = state.ajustesAutomaticos,
                            onOpenAdjustments = { viewModel.navigateTo(AppScreen.PlanAdjustments) },
                            onOpenPrivacyData = { viewModel.navigateTo(AppScreen.PrivacyData) },
                            accountState = accountState,
                            accountDialog = accountDialog,
                            onAccountSignIn = viewModel::onSignInClick,
                            onAccountGoogleSignIn = onGoogleSignIn,
                            onAccountDialog = viewModel::openAccountDialog,
                            onAccountDialogDismiss = {
                                awaitingCompetitionUf = false
                                viewModel.dismissAccountDialog()
                            },
                            onAccountSignOut = viewModel::onSignOutConfirm,
                            onAccountUfConfirm = viewModel::onUfConfirm
                        )
                    }

                    is AppScreen.PrivacyData -> {
                        val backupFileExists by viewModel.backupFileExists.collectAsStateWithLifecycle()
                        LaunchedEffect(true) {
                            viewModel.refreshBackupOnProfileEnter()
                        }
                        PrivacyDataScreen(
                            hasBackupFile = backupFileExists,
                            onExportBackup = { backupExportLauncher.launch("elite_backup.json") },
                            onShareBackup = {
                                val ok = viewModel.shareBackup(context)
                                if (!ok) {
                                    android.widget.Toast.makeText(
                                        context,
                                        "Nenhum backup para compartilhar",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onImportBackup = { backupFilePickerLauncher.launch(arrayOf("application/json")) },
                            onDeleteBackup = { viewModel.deleteBackupFile() },
                            onClearAllData = { callback -> viewModel.clearAllUserData(callback) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    is AppScreen.PlanAdjustments -> {
                        PlanAdjustmentsScreen(
                            ajustesAutomaticos = state.ajustesAutomaticos,
                            modoAjuste = state.modoAjuste,
                            ajustes = state.autoAdjustments,
                            onToggleAutomaticos = { viewModel.setAjustesAutomaticos(it) },
                            onModoChange = { viewModel.setModoAjuste(it) },
                            onUndo = { viewModel.undoAutoAdjustment(it) },
                            onBack = { viewModel.navigateBack() }
                        )
                    }

                    else -> Unit
                }
                }

                if (showCompetition && (accountState as? AuthState.SignedIn)?.account?.uf in com.elite.sch.app.account.BRAZIL_UFS) {
                    CompetitionFlowScreen(runState = runState, onClose = { showCompetition = false })
                }
            }
        }
    } else {
        EliteResponsiveFrame {
        when (state.currentScreen) {
            is AppScreen.Welcome -> {
                WelcomeScreen(
                    userName = state.userName,
                    onUserNameChange = { viewModel.setUserName(it) },
                    isLoading = state.isLoading,
                    isEmpty = false,
                    onStartClick = {
                        onPlayMission()
                        viewModel.navigateTo(AppScreen.GoalSelection)
                    }
                )
            }

            is AppScreen.GoalSelection -> {
                GoalSelectionScreen(
                    onSelectModality = { modality ->
                        appScope.launch { viewModel.selectModality(modality) }
                    },
                    onStartFreeRun = { viewModel.startFreeRun { workout, useGps ->
                        startRunServiceAndNavigate(
                            context = context,
                            viewModel = viewModel,
                            workout = workout,
                            useGps = useGps,
                            voiceEnabled = state.voiceEnabled,
                            vibrationEnabled = state.vibrationEnabled,
                            avisoDistanciaAtivo = state.avisoDistanciaAtivo,
                            avisoDistanciaMetros = state.avisoDistanciaMetros,
                            modality = "livre"
                        )
                    } }
                )
            }

            is AppScreen.QuizQuestion1 -> {
                QuizQuestionScreen(
                    stepNumber = 1,
                    totalSteps = 3,
                    questionTitle = "Há quanto tempo você não corre?",
                    questionSubtitle = "Não se preocupe se nunca correu antes: o plano começa com caminhadas suaves.",
                    options = listOf(
                        "Nunca corri",
                        "Mais de 6 meses",
                        "De 3 a 6 meses",
                        "Menos de 3 meses"
                    ),
                    selectedOption = state.tempoSemCorrer,
                    onOptionSelected = { viewModel.setTempoSemCorrer(it) },
                    onNextClick = { viewModel.navigateTo(AppScreen.QuizQuestion2) },
                    onBackClick = { viewModel.navigateBack() }
                )
            }

            is AppScreen.QuizQuestion2 -> {
                QuizQuestionScreen(
                    stepNumber = 2,
                    totalSteps = 3,
                    questionTitle = "Quanto você se exercita por semana?",
                    questionSubtitle = "Qualquer movimento conta, desde caminhadas diárias até treinos na academia.",
                    options = listOf(
                        "Nada",
                        "1 a 2 vezes",
                        "3 vezes ou mais"
                    ),
                    selectedOption = state.frequenciaExercicio,
                    onOptionSelected = { viewModel.setFrequenciaExercicio(it) },
                    onNextClick = { viewModel.navigateTo(AppScreen.QuizQuestion3) },
                    onBackClick = { viewModel.navigateBack() }
                )
            }

            is AppScreen.QuizQuestion3 -> {
                QuizQuestionScreen(
                    stepNumber = 3,
                    totalSteps = 3,
                    questionTitle = "Que tipo de exercício você pratica?",
                    questionSubtitle = "Isso nos ajuda a calibrar a intensidade inicial do seu plano.",
                    options = listOf(
                        "Nenhum",
                        "Baixo impacto, como caminhada",
                        "Cardio, como bicicleta ou natação"
                    ),
                    selectedOption = state.tipoExercicio,
                    onOptionSelected = { viewModel.setTipoExercicio(it) },
                    onNextClick = { viewModel.navigateTo(AppScreen.DaysSelection) },
                    onBackClick = { viewModel.navigateBack() }
                )
            }

            is AppScreen.DaysSelection -> {
                val isTemplatePlan = state.selectedModality == "plano_2_5k" || state.selectedModality == "plano_10k"
                val onNextClick: () -> Unit = when (state.selectedModality) {
                    "plano_2_5k", "plano_10k" -> ({ viewModel.generateTemplatePlan() })
                    else -> ({ viewModel.navigateTo(AppScreen.DurationSelection) })
                }
                DaysSelectionScreen(
                    diasPorSemana = state.diasPorSemana,
                    diasSelecionados = state.diasSelecionados,
                    dataInicio = state.dataInicio,
                    onDiasPorSemanaChange = { viewModel.setDiasPorSemana(it) },
                    onDiaToggle = { viewModel.toggleDiaSemana(it) },
                    onDataInicioChange = { viewModel.setDataInicio(it) },
                    onNextClick = onNextClick,
                    onBackClick = { viewModel.navigateBack() },
                    errorMessage = state.previewErrorMessage,
                    nextButtonText = if (isTemplatePlan) "Gerar meu plano" else null
                )
            }

            is AppScreen.DurationSelection -> {
                DurationScreen(
                    totalSemanas = state.totalSemanas,
                    minSemanas = state.minSemanas,
                    errorMessage = state.previewErrorMessage,
                    onDismissError = { viewModel.dismissPreviewError() },
                    onTotalSemanasChange = { viewModel.setTotalSemanas(it) },
                    onNextClick = { viewModel.generatePreview() },
                    onBackClick = { viewModel.navigateBack() }
                )
            }

            is AppScreen.PlanPreview -> {
                val startPlanScope = rememberCoroutineScope()
                PlanPreviewScreen(
                    generatedPlan = state.previewPlan,
                    onStartPlanClick = { startPlanScope.launch { viewModel.startPlan() } },
                    onBackClick = { viewModel.navigateBack() },
                    isLoading = state.isLoading,
                    isEmpty = state.previewPlan == null
                )
            }

            is AppScreen.ActiveRun -> {
                val currentWorkout = state.workouts.find { it.id == runState.workoutId } ?: state.todayWorkout
                val workoutSteps = currentWorkout?.let { com.elite.sch.app.data.local.WorkoutStepConverter.fromJson(it.passosJson) } ?: emptyList()
                val isFreeRun = runState.modality == "livre"

                // Estado do mapa (controlado localmente na tela, persistido via ViewModel)
                var cameraMode by remember { mutableStateOf<com.elite.sch.app.ui.state.CameraMode>(com.elite.sch.app.ui.state.CameraMode.FollowUser) }
                var showMapDuringRun by remember { mutableStateOf(state.showMapDuringRun) }
                var mapPreferences by remember(state) {
                    mutableStateOf(
                        com.elite.sch.app.ui.state.MapPreferences(state.showMapDuringRun, state.defaultCameraMode)
                    )
                }

                ActiveRunScreen(
                    runState = runState,
                    dismissBatteryWarning = state.dismissBatteryWarning,
                    onDismissBatteryWarning = { viewModel.setDismissBatteryWarning(true) },
                    steps = workoutSteps,
                    isFreeRun = isFreeRun,
                    showMapDuringRun = showMapDuringRun,
                    cameraMode = cameraMode,
                    onCameraModeChange = { cameraMode = it },
                    onRecenter = { /* Recuperado pelo MapViewComposable */ },
                    mapPreferences = mapPreferences,
                    onMapPreferencesChange = { updated ->
                        mapPreferences = updated
                        if (updated.showMapDuringRun != state.showMapDuringRun) {
                            showMapDuringRun = updated.showMapDuringRun
                            viewModel.setShowMapDuringRun(updated.showMapDuringRun)
                        }
                        if (updated.defaultCameraMode != state.defaultCameraMode) {
                            viewModel.setDefaultCameraMode(updated.defaultCameraMode)
                            cameraMode = updated.toCameraMode()
                        }
                    },
                    onPauseClick = {
                        val intent = Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_PAUSE
                        }
                        context.startService(intent)
                    },
                    onResumeClick = {
                        val intent = Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_RESUME
                        }
                        context.startService(intent)
                    },
                    onFinishClick = {
                        val intent = Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_STOP
                        }
                        context.startService(intent)
                        val wId = if (runState.workoutId != 0L) runState.workoutId else state.todayWorkout?.id ?: 0L
                        val wTitle = runState.workoutTitle.ifBlank { state.todayWorkout?.titulo ?: "Treino" }
                        viewModel.onRunFinished(
                            workoutId = wId,
                            workoutTitle = wTitle,
                            elapsedSeconds = runState.elapsedSeconds,
                            totalDistanceMeters = runState.totalDistanceMeters
                        )
                    },
                    onToggleVoice = {
                        val intent = Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_TOGGLE_VOICE
                        }
                        context.startService(intent)
                    },
                    onToggleVibration = {
                        val intent = Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_TOGGLE_VIBRATION
                        }
                        context.startService(intent)
                    }
                )
            }

            is AppScreen.WorkoutSummary -> {
                val summary = state.runSummaryData
                if (summary != null) {
                    var summaryDor by remember(summary.workoutId) { mutableIntStateOf(0) }
                    var summaryRegiaoDor by remember(summary.workoutId) { mutableStateOf<String?>(null) }
                    var summaryRoutePoints by remember(summary.workoutId) { mutableStateOf<List<RoutePointEntity>>(emptyList()) }
                    // Usa routePoints do summary (vem do RunTrackingService em tempo real) se disponível,
                    // senão carrega do banco de dados
                    LaunchedEffect(summary.workoutId) {
                        val dbPoints = summary.routePoints.map { mp ->
                            com.elite.sch.app.data.local.entities.RoutePointEntity(
                                id = 0,
                                workoutId = summary.workoutId,
                                latitude = mp.latitude,
                                longitude = mp.longitude,
                                accuracy = mp.accuracy,
                                timestampMillis = mp.timestampMillis
                            )
                        }
                        summaryRoutePoints = if (dbPoints.isNotEmpty()) dbPoints else viewModel.getRoutePointsForWorkout(summary.workoutId)
                    }
                    WorkoutSummaryScreen(
                        workoutTitle = summary.workoutTitle,
                        elapsedSeconds = summary.elapsedSeconds,
                        totalDistanceMeters = summary.totalDistanceMeters,
                        routePoints = summaryRoutePoints,
                        allCompletedLogs = state.workoutLogs,
                        diagnostics = summary.gpsDiagnostics,
                        ajustesAutomaticos = state.ajustesAutomaticos,
                        onPainChange = { d, r -> summaryDor = d; summaryRegiaoDor = r },
                        onSaveLog = { rpe, notes ->
                            viewModel.saveWorkoutSummary(rpe, notes, summaryDor, summaryRegiaoDor)
                        }
                    )
                } else {
                    viewModel.switchTab(AppScreen.Today)
                }
            }

            else -> Unit
        }
        }
    }
}

/**
 * Barra inferior com 5 posições:
 * 1. Hoje (Casa)
 * 2. Plano (Calendário)
 * 3. Central (Circular com símbolo "A", gradiente da marca, sobressai da barra)
 * 4. Progresso (Gráfico)
 * 5. Perfil (Engrenagem)
 */
@Composable
fun EliteBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit,
    onCenterActionClick: () -> Unit,
    showCenterAction: Boolean = true
) {
    val windowSizeClass = rememberEliteWindowSizeClass()
    val barHeight = EliteBottomBarHeight(windowSizeClass)
    val iconSize = EliteBottomNavIconSize(windowSizeClass)
    val centerActionSize = EliteCenterActionSize(windowSizeClass)
    val centerSymbolSize = EliteCenterSymbolSize(windowSizeClass)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_navigation_bar"),
        contentAlignment = Alignment.TopCenter
    ) {
        // Barra de fundo refinada: cantos 28 dp, borda "sutil", altura adaptativa
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = EliteBottomBarSurface,
            border = BorderStroke(1.dp, EliteBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.systemBars.union(WindowInsets.tappableElement)
                            .only(WindowInsetsSides.Bottom)
                    )
                    .padding(bottom = 12.dp)
                    .height(barHeight)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Hoje
                BottomTabItem(
                    icon = Icons.Default.Home,
                    label = "Hoje",
                    selected = currentScreen is AppScreen.Today,
                    onClick = { onTabSelected(AppScreen.Today) },
                    testTag = "tab_hoje",
                    modifier = Modifier.weight(1f),
                    iconSize = iconSize
                )

                // 2. Plano
                BottomTabItem(
                    icon = Icons.Default.CalendarMonth,
                    label = "Plano",
                    selected = currentScreen is AppScreen.FullPlan,
                    onClick = { onTabSelected(AppScreen.FullPlan) },
                    testTag = "tab_plano",
                    modifier = Modifier.weight(1f),
                    iconSize = iconSize
                )

                // 3. Espaçador para o botão central sobressalente
                Spacer(modifier = Modifier.weight(1f))

                // 4. Progresso
                BottomTabItem(
                    icon = Icons.AutoMirrored.Filled.ShowChart,
                    label = "Progresso",
                    selected = currentScreen is AppScreen.Progress,
                    onClick = { onTabSelected(AppScreen.Progress) },
                    testTag = "tab_progresso",
                    modifier = Modifier.weight(1f),
                    iconSize = iconSize
                )

                // 5. Perfil
                BottomTabItem(
                    icon = Icons.Default.Person,
                    label = "Perfil",
                    selected = currentScreen is AppScreen.Profile,
                    onClick = { onTabSelected(AppScreen.Profile) },
                    testTag = "tab_perfil",
                    modifier = Modifier.weight(1f),
                    iconSize = iconSize
                )
            }
        }

        // Botão central refinado: sobressai da barra, tamanho adaptativo
        // Centralizado na borda superior da barra (metade do botão acima, metade abaixo)
        AnimatedVisibility(visible = showCenterAction) {
            Box(
                modifier = Modifier
                    .offset(y = (-centerActionSize / 2))
                    .size(centerActionSize)
                    .clip(CircleShape)
                    .background(EliteBackground)
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(centerActionSize - 6.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(BrandGradient)
                        .clickable(
                            role = androidx.compose.ui.semantics.Role.Button,
                            onClickLabel = "Começar treino de hoje"
                        ) { onCenterActionClick() }
                        .testTag("btn_center_action_elite"),
                    contentAlignment = Alignment.Center
                ) {
                    EliteSymbol(
                        modifier = Modifier.size(centerSymbolSize),
                        useBlade = false,
                        size = centerSymbolSize
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomTabItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    iconSize: androidx.compose.ui.unit.Dp = 22.dp
) {
    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) EliteTurquoise else EliteTextSecondary,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) EliteTurquoise else EliteTextSecondary,
            maxLines = 1
        )
    }
}

private fun startRunServiceAndNavigate(
    context: Context,
    viewModel: MainViewModel,
    workout: WorkoutEntity,
    useGps: Boolean,
    voiceEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    avisoDistanciaAtivo: Boolean = true,
    avisoDistanciaMetros: Int = 1000,
    modality: String = "plano_5k"
) {
    val intent = Intent(context, RunTrackingService::class.java).apply {
        action = RunTrackingService.ACTION_START
        putExtra(RunTrackingService.EXTRA_WORKOUT_ID, workout.id)
        putExtra(RunTrackingService.EXTRA_WORKOUT_TITLE, workout.titulo)
        putExtra(RunTrackingService.EXTRA_STEPS_JSON, workout.passosJson)
        putExtra(RunTrackingService.EXTRA_USE_GPS, useGps)
        putExtra(RunTrackingService.EXTRA_VOICE_ENABLED, voiceEnabled)
        putExtra(RunTrackingService.EXTRA_VIBRATION_ENABLED, vibrationEnabled)
        putExtra(RunTrackingService.EXTRA_AVISO_DISTANCIA_ATIVO, avisoDistanciaAtivo)
        putExtra(RunTrackingService.EXTRA_AVISO_DISTANCIA_METROS, avisoDistanciaMetros)
        putExtra(RunTrackingService.EXTRA_MODALITY, modality)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
    viewModel.navigateTo(AppScreen.ActiveRun)
}
