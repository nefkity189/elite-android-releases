package com.elite.sch.app.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.app.NotificationCompat
import com.elite.sch.app.MainActivity
import com.elite.sch.app.R
import com.elite.sch.app.data.local.AppDatabase
import com.elite.sch.app.data.local.BaseAppDatabase
import com.elite.sch.app.data.local.DatabaseProvider
import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.ActiveRunEntity
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.tracker.GeoPoint
import com.elite.sch.app.tracker.GpsFilter
import com.elite.sch.app.tracker.GpsSession
import com.elite.sch.app.tracker.KmMilestoneTracker
import com.elite.sch.app.tracker.RawFix
import com.elite.sch.app.tracker.PaceCalculator
import com.elite.sch.app.tracker.StepScheduler
import com.elite.sch.app.tracker.VoiceCue
import com.elite.sch.app.ui.state.MapPoint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

data class RunState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val workoutId: Long = 0L,
    val workoutTitle: String = "",
    val elapsedSeconds: Long = 0L,
    val currentStepIndex: Int = 0,
    val currentStep: WorkoutStep? = null,
    val timeRemainingInStep: Long = 0L,
    val nextStep: WorkoutStep? = null,
    val totalDistanceMeters: Double = 0.0,
    val averagePaceSeconds: Double? = null,
    val averagePaceFormatted: String = "--:--",
    val hasGpsSignal: Boolean = false,
    val gpsAccuracy: Float? = null,
    val voiceEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val avisoDistanciaAtivo: Boolean = true,
    val avisoDistanciaMetros: Int = 1000,
    val ttsAvailable: Boolean = true,
    val ttsWarning: String? = null,
    val isGpsEnabledOnDevice: Boolean = true,
    val isGpsRequested: Boolean = true,
    val startCountdown: Int? = null,
    val gpsDiagnostics: com.elite.sch.app.tracker.GpsDiagnostics = com.elite.sch.app.tracker.GpsDiagnostics(),
    val currentMapPoint: MapPoint? = null, // Ponto atual para o mapa em tempo real
    val routePoints: List<MapPoint> = emptyList(), // Todos os pontos da rota para o mapa
    val modality: String = "plano_5k" // Default modality for free run detection
)

class RunTrackingService : Service(), TextToSpeech.OnInitListener {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var tickerJob: Job? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var audioManager: AudioManager? = null
    private var currentAudioFocusRequest: AudioFocusRequest? = null
    private var commandPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    private var steps: List<WorkoutStep> = emptyList()
    private var workoutId: Long = 0L
    private var workoutTitle: String = ""
    private var competitionStart: Boolean = false
    private var isGpsRequested: Boolean = true

        // Controle de inatividade GPS em competição (10 min sem movimento = abandono automático)
        private var isCompetitionRace: Boolean = false
        private var lastGpsMovementTime: Long = 0L
        private var inactivityMonitorJob: Job? = null
                private val INACTIVITY_TIMEOUT_MS = 10 * 60 * 1000L // 10 minutos

    // Configurações de avisos por distância
    private var avisoDistanciaAtivo: Boolean = true
    private var avisoDistanciaMetros: Int = 1000
    private var lastAnnouncedMilestoneIndex: Int = 0
    private var currentModality: String = "plano_5k"

    // Cronômetro preciso via SystemClock.elapsedRealtime
    private var startRealtime: Long = 0L
    private var pauseStartRealtime: Long = 0L
    private var totalPausedMillis: Long = 0L
    private var isPaused: Boolean = false

    // GPS e Distância
    private var gpsSession: GpsSession = GpsSession(0L)
    private var totalDistanceMeters: Double = 0.0

    // Pontos da rota para o mapa em tempo real
    private var routePoints = mutableListOf<MapPoint>()

    // Controle de avisos para não repetir
    private var lastStepIndexAnnounced: Int = -1
    private var hasAnnouncedTenSecondsForStep: Boolean = false
    private var lastAnnouncedKm: Int = 0
    private var countdownJob: Job? = null

    companion object {
        const val CHANNEL_ID = "primeiro_5k_run_tracking"
        const val NOTIFICATION_ID = 5001

        const val ACTION_START = "com.elite.sch.app.service.ACTION_START"
        const val ACTION_PAUSE = "com.elite.sch.app.service.ACTION_PAUSE"
        const val ACTION_RESUME = "com.elite.sch.app.service.ACTION_RESUME"
        const val ACTION_STOP = "com.elite.sch.app.service.ACTION_STOP"
        const val ACTION_TOGGLE_VOICE = "com.elite.sch.app.service.ACTION_TOGGLE_VOICE"
        const val ACTION_TOGGLE_VIBRATION = "com.elite.sch.app.service.ACTION_TOGGLE_VIBRATION"
        const val ACTION_TOGGLE_PAUSE_RESUME = "com.elite.sch.app.service.ACTION_TOGGLE_PAUSE_RESUME"

        const val EXTRA_WORKOUT_ID = "EXTRA_WORKOUT_ID"
        const val EXTRA_WORKOUT_TITLE = "EXTRA_WORKOUT_TITLE"
        const val EXTRA_STEPS_JSON = "EXTRA_STEPS_JSON"
        const val EXTRA_USE_GPS = "EXTRA_USE_GPS"
        const val EXTRA_VOICE_ENABLED = "EXTRA_VOICE_ENABLED"
        const val EXTRA_VIBRATION_ENABLED = "EXTRA_VIBRATION_ENABLED"
        const val EXTRA_AVISO_DISTANCIA_ATIVO = "EXTRA_AVISO_DISTANCIA_ATIVO"
        const val EXTRA_AVISO_DISTANCIA_METROS = "EXTRA_AVISO_DISTANCIA_METROS"
        const val EXTRA_MODALITY = "EXTRA_MODALITY"

        private val _runState = MutableStateFlow(RunState())
        val runState: StateFlow<RunState> = _runState.asStateFlow()

        /** Short confirmation used when the user enables tactile alerts. */
        fun vibrateToggleConfirmation(context: Context) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                    ?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (vibrator?.hasVibrator() != true) return
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.cancel()
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(90L, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(90L)
                }
            } catch (_: RuntimeException) {
                // Some OEMs reject haptics while the device is in a restricted mode.
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager

        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val ptBr = Locale.forLanguageTag("pt-BR")
            val result = tts?.setLanguage(ptBr)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsInitialized = false
                _runState.value = _runState.value.copy(
                    ttsAvailable = false,
                    ttsWarning = "Voz em português do Brasil (pt-BR) não disponível no aparelho."
                )
            } else {
                tts?.let { configureEliteCoachVoice(it, ptBr) }
                isTtsInitialized = true
                setupUtteranceProgressListener()
                _runState.value = _runState.value.copy(ttsAvailable = true, ttsWarning = null)
            }
        } else {
            isTtsInitialized = false
            _runState.value = _runState.value.copy(
                ttsAvailable = false,
                ttsWarning = "Não foi possível inicializar o sintetizador de voz."
            )
        }
    }

    private fun configureEliteCoachVoice(engine: TextToSpeech, locale: Locale) {
        val portugueseVoices = engine.voices.orEmpty()
            .filter { it.locale.language.equals(locale.language, ignoreCase = true) }
        val brazilianVoices = portugueseVoices.filter {
            it.locale.country.equals(locale.country, ignoreCase = true)
        }
        val preferredVoices = brazilianVoices.ifEmpty { portugueseVoices }
        val maleVoice = preferredVoices.firstOrNull { voice ->
            Regex("(^|[^a-z])male([^a-z]|$)").containsMatchIn(voice.name.lowercase(Locale.ROOT))
        }

        if (maleVoice != null) {
            engine.voice = maleVoice
        }

        // Dynamic distance/time announcements use the device TTS. Natural male
        // coach voice at standard speed with a slightly deeper pitch.
        engine.setSpeechRate(1.0f)
        engine.setPitch(0.80f)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If service is restarted by system with null intent, try to restore active run from DB
        if (intent == null || intent.action == null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(NOTIFICATION_ID, buildNotification("Recuperando corrida", 0L, false),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification("Recuperando corrida", 0L, false))
                }
            } catch (error: RuntimeException) {
                stopSelf()
                return START_NOT_STICKY
            }
            restoreActiveRunIfExists()
            return START_STICKY
        }

        when (intent.action) {
            ACTION_START -> {
                competitionStart = intent.getBooleanExtra("COMPETITION_START", false)
                val wId = intent.getLongExtra(EXTRA_WORKOUT_ID, 0L)
                val title = intent.getStringExtra(EXTRA_WORKOUT_TITLE) ?: "Treino"
                val stepsJson = intent.getStringExtra(EXTRA_STEPS_JSON) ?: "[]"
                val useGps = intent.getBooleanExtra(EXTRA_USE_GPS, true)
                val voiceEnabled = intent.getBooleanExtra(EXTRA_VOICE_ENABLED, true)
                val vibrationEnabled = intent.getBooleanExtra(EXTRA_VIBRATION_ENABLED, true)
                val avisoDistanciaAtivo = intent.getBooleanExtra(EXTRA_AVISO_DISTANCIA_ATIVO, true)
                val avisoDistanciaMetros = intent.getIntExtra(EXTRA_AVISO_DISTANCIA_METROS, 1000)
                val modality = intent.getStringExtra(EXTRA_MODALITY) ?: "plano_5k"
                startRun(wId, title, stepsJson, useGps, voiceEnabled, vibrationEnabled, avisoDistanciaAtivo, avisoDistanciaMetros, modality)
            }
            ACTION_PAUSE -> pauseRun()
            ACTION_RESUME -> resumeRun()
            ACTION_STOP -> stopRun()
            ACTION_TOGGLE_VOICE -> {
                val current = _runState.value.voiceEnabled
                _runState.value = _runState.value.copy(voiceEnabled = !current)
            }
            ACTION_TOGGLE_VIBRATION -> {
                val current = _runState.value.vibrationEnabled
                val enabled = !current
                _runState.value = _runState.value.copy(vibrationEnabled = enabled)
                if (enabled) vibrateToggleConfirmation(this)
            }
            ACTION_TOGGLE_PAUSE_RESUME -> {
                if (_runState.value.isPaused) {
                    resumeRun()
                } else if (_runState.value.isRunning) {
                    pauseRun()
                }
            }
        }
        return if (intent.action == ACTION_STOP) START_NOT_STICKY else START_STICKY
    }

    /**
     * Attempts to restore an active run from the database when the service is recreated.
     * Called when the system restarts the service with a null intent.
     */
    private fun restoreActiveRunIfExists() {
        serviceScope.launch(Dispatchers.IO) {
            // First check all modalities for an active run, since we don't know which one it belongs to
            val modalities = listOf("livre", "plano_2_5k", "plano_5k", "plano_10k")
            var foundRun: ActiveRunEntity? = null
            var foundModality: String? = null

            for (modality in modalities) {
                val db = DatabaseProvider.getDatabase(modality, applicationContext)
                val activeRun = db.activeRunDao().getActiveRun()
                if (activeRun != null) {
                    val now = System.currentTimeMillis()
                    val maxAgeMillis = 2 * 60 * 60 * 1000L // 2 hours
                    if (now - activeRun.lastUpdatedMillis in 0 until maxAgeMillis &&
                        SystemClock.elapsedRealtime() >= activeRun.elapsedRealtimeBase) {
                        foundRun = activeRun
                        foundModality = modality
                        break
                    }
                }
            }

            if (foundRun != null && foundModality != null) {
                // Use the modality stored in the ActiveRunEntity if available, otherwise use found modality
                val modalityToUse = foundRun.modality.ifBlank { foundModality }
                restoreRunFromEntity(foundRun, modalityToUse)
            } else {
                stopSelf()
            }
        }
    }

    /**
     * Restores run state from an ActiveRunEntity.
     */
    private fun restoreRunFromEntity(entity: ActiveRunEntity, modality: String) {
        // Load workout steps from the workout
        serviceScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(modality, applicationContext)
            val workout = db.workoutDao().getWorkoutById(entity.workoutId)
            val steps = workout?.let { WorkoutStepConverter.fromJson(it.passosJson) } ?: emptyList()

            // Restore route points from database
            val savedRoutePoints = db.routePointDao().getPointsListForWorkout(entity.workoutId)
                .map { entity ->
                    MapPoint(
                        latitude = entity.latitude,
                        longitude = entity.longitude,
                        accuracy = entity.accuracy,
                        timestampMillis = entity.timestampMillis
                    )
                }
            routePoints.clear()
            routePoints.addAll(savedRoutePoints)

            // Restore all fields from the entity
            workoutId = entity.workoutId
            workoutTitle = workout?.titulo ?: "Treino"
            currentModality = modality
            this@RunTrackingService.steps = steps
            isPaused = entity.isPaused
            startRealtime = entity.elapsedRealtimeBase
            totalPausedMillis = entity.pausedElapsedMillis
            totalDistanceMeters = entity.totalDistanceMeters
            // Competition state is persisted separately from Room because the
            // server-bound session must survive a foreground-service restart.
            competitionStart = com.elite.sch.app.data.competition.CompetitionSync.activeMission() != null
            gpsSession = GpsSession(
                entity.elapsedRealtimeBase,
                entity.totalDistanceMeters,
                if (competitionStart) 0L else GpsSession.WARMUP_MILLIS
            )

            // Calculate elapsed seconds based on current time
            val nowRealtime = SystemClock.elapsedRealtime()
            val elapsedMillis = if (isPaused) {
                (entity.lastUpdatedMillis - entity.startTimeMillis).coerceAtLeast(0L)
            } else {
                nowRealtime - entity.elapsedRealtimeBase - entity.pausedElapsedMillis
            }
            val elapsedSeconds = elapsedMillis / 1000L

            // Determine current step based on elapsed time
            val schedule = StepScheduler.evaluate(steps, elapsedSeconds)
            lastStepIndexAnnounced = schedule.currentStepIndex
            hasAnnouncedTenSecondsForStep = false
            lastAnnouncedMilestoneIndex = (totalDistanceMeters / avisoDistanciaMetros.toDouble()).toInt()
            lastAnnouncedKm = (totalDistanceMeters / 1000.0).toInt()

            // Update run state
            _runState.value = RunState(
                isRunning = true,
                isPaused = isPaused,
                isFinished = false,
                workoutId = entity.workoutId,
                workoutTitle = workoutTitle,
                elapsedSeconds = elapsedSeconds,
                currentStepIndex = schedule.currentStepIndex,
                currentStep = schedule.currentStep,
                timeRemainingInStep = schedule.timeRemainingInStep,
                nextStep = schedule.nextStep,
                totalDistanceMeters = totalDistanceMeters,
                averagePaceSeconds = PaceCalculator.calculateAveragePaceSeconds(elapsedSeconds, totalDistanceMeters),
                averagePaceFormatted = PaceCalculator.formatPace(PaceCalculator.calculateAveragePaceSeconds(elapsedSeconds, totalDistanceMeters)),
                hasGpsSignal = false,
                voiceEnabled = _runState.value.voiceEnabled,
                vibrationEnabled = _runState.value.vibrationEnabled,
                avisoDistanciaAtivo = _runState.value.avisoDistanciaAtivo,
                avisoDistanciaMetros = _runState.value.avisoDistanciaMetros,
                isGpsRequested = _runState.value.isGpsRequested,
                routePoints = routePoints.toList(),
                modality = modality
            )

            // Restart foreground service and location updates
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val foregroundServiceType = if (_runState.value.isGpsRequested) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    } else {
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                    }
                }
                startForeground(
                    NOTIFICATION_ID,
                    buildNotification(
                        if (isPaused) getString(R.string.notification_status_paused) else getString(R.string.notification_status_running),
                        elapsedSeconds,
                        isPaused
                    ),
                    foregroundServiceType
                )
            } else {
                startForeground(NOTIFICATION_ID, buildNotification(
                    if (isPaused) getString(R.string.notification_status_paused) else getString(R.string.notification_status_running),
                    elapsedSeconds,
                    isPaused
                ))
            }

            if (_runState.value.isGpsRequested && !isPaused) {
                startLocationUpdates()
            }

            // Restart ticker
            startTicker()

            // Save updated state to DB
            saveActiveRunToDb()
        }
    }

    private fun startRun(
        wId: Long,
        title: String,
        stepsJson: String,
        useGps: Boolean,
        voiceEnabled: Boolean = true,
        vibrationEnabled: Boolean = true,
        avisoDistanciaAtivo: Boolean = true,
        avisoDistanciaMetros: Int = 1000,
        modality: String = "plano_5k"
    ) {
        workoutId = wId
        workoutTitle = title
        isGpsRequested = useGps
        steps = WorkoutStepConverter.fromJson(stepsJson)
        this.avisoDistanciaAtivo = avisoDistanciaAtivo
        this.avisoDistanciaMetros = avisoDistanciaMetros
        this.currentModality = modality

        totalPausedMillis = 0L
        isPaused = false
        totalDistanceMeters = 0.0
        routePoints.clear()
        gpsSession = GpsSession(SystemClock.elapsedRealtime())
        lastStepIndexAnnounced = -1
        hasAnnouncedTenSecondsForStep = false
        lastAnnouncedMilestoneIndex = (totalDistanceMeters / avisoDistanciaMetros.toDouble()).toInt()

        _runState.value = RunState(
            isRunning = true,
            isPaused = false,
            isFinished = false,
            workoutId = wId,
            workoutTitle = title,
            elapsedSeconds = 0L,
            currentStepIndex = 0,
            currentStep = steps.firstOrNull(),
            timeRemainingInStep = steps.firstOrNull()?.durationSeconds?.toLong() ?: 0L,
            nextStep = steps.getOrNull(1),
            totalDistanceMeters = 0.0,
            averagePaceSeconds = null,
            averagePaceFormatted = "--:--",
            hasGpsSignal = false,
            voiceEnabled = voiceEnabled,
            vibrationEnabled = vibrationEnabled,
            avisoDistanciaAtivo = avisoDistanciaAtivo,
            avisoDistanciaMetros = avisoDistanciaMetros,
            isGpsRequested = useGps,
            modality = modality,
            startCountdown = if (competitionStart) 0 else 3
        )

        val fineGranted = androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val coarseGranted = androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasLocationPermission = fineGranted || coarseGranted

        val effectiveUseGps = useGps && hasLocationPermission

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val foregroundServiceType = if (effectiveUseGps) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                } else {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                }
            }
            startForeground(
                NOTIFICATION_ID,
                buildNotification(getString(R.string.notification_status_starting), 0L, false),
                foregroundServiceType
            )
        } else {
            startForeground(NOTIFICATION_ID, buildNotification(getString(R.string.notification_status_starting), 0L, false))
        }

        countdownJob?.cancel()
        countdownJob = serviceScope.launch {
            (if (competitionStart) listOf(0) else RunStartCountdown.ticks()).forEach { tick ->
                _runState.value = _runState.value.copy(startCountdown = tick)
                if (tick > 0) {
                    if (vibrationEnabled) vibrateToggleConfirmation(this@RunTrackingService)
                    delay(1000L)
                }
            }
            startRealtime = SystemClock.elapsedRealtime()
            gpsSession = GpsSession(startRealtime, warmupMillis = if (competitionStart) 0L else GpsSession.WARMUP_MILLIS)
            if (effectiveUseGps) startLocationUpdates()
            _runState.value = _runState.value.copy(startCountdown = null)
            saveActiveRunToDb()
            startTicker()
            
                        // Inicia monitor de inatividade GPS para competições
                        if (competitionStart) {
                            isCompetitionRace = true
                            lastGpsMovementTime = SystemClock.elapsedRealtime()
                            startInactivityMonitor()
                        }

        // Primeiro aviso de voz (início)
        if (steps.isNotEmpty()) {
            val firstStep = steps.first()
            val initialCue = when (firstStep.type) {
                StepType.RUN -> getString(R.string.voice_start_run)
                else -> getString(R.string.voice_start_walk)
            }
            speakAndVibrate(initialCue, vibrate = true, isRun = firstStep.type == StepType.RUN)
            lastStepIndexAnnounced = 0
        } else {
            speakAndVibrate(getString(R.string.voice_free_run_start), vibrate = true, isRun = false)
        }
            delay(450L)
            _runState.value = _runState.value.copy(startCountdown = null)
        }
    }

    private fun pauseRun() {
        if (!isPaused && _runState.value.isRunning) {
            isPaused = true
            pauseStartRealtime = SystemClock.elapsedRealtime()
            _runState.value = _runState.value.copy(isPaused = true)
            updateNotification(getString(R.string.notification_status_paused), _runState.value.elapsedSeconds)
            saveActiveRunToDb()
            
                // Para monitor de inatividade ao pausar (usuário pausou intencionalmente)
                if (isCompetitionRace) {
                    stopInactivityMonitor()
                }
        }
    }

    private fun resumeRun() {
        if (isPaused && _runState.value.isRunning) {
            val pausedDuration = SystemClock.elapsedRealtime() - pauseStartRealtime
            totalPausedMillis += pausedDuration
            isPaused = false
            gpsSession.onResume(SystemClock.elapsedRealtime())
            _runState.value = _runState.value.copy(isPaused = false)
            updateNotification(getString(R.string.notification_status_running), _runState.value.elapsedSeconds)
            saveActiveRunToDb()
            
                // Reinicia monitor de inatividade se for competição
                if (isCompetitionRace) {
                    lastGpsMovementTime = SystemClock.elapsedRealtime()
                    startInactivityMonitor()
                }
        }
    }

    private fun stopRun() {
        val wasCompetitionRace = competitionStart
        countdownJob?.cancel()
        tickerJob?.cancel()
        stopInactivityMonitor()
        stopLocationUpdates()
        releaseAudioFocus()
        stopForeground(STOP_FOREGROUND_REMOVE)
        _runState.value = _runState.value.copy(
            isRunning = false,
            isPaused = false,
            isFinished = true,
            gpsDiagnostics = gpsSession.diagnostics(SystemClock.elapsedRealtime())
        )
        val modality = currentModality
        serviceScope.launch(Dispatchers.IO) {
            DatabaseProvider.getDatabase(modality, applicationContext)
                .activeRunDao()
                .clearActiveRun()
        }
        competitionStart = false
        if (wasCompetitionRace && !com.elite.sch.app.data.competition.CompetitionSync.hasConfirmedFinish()) {
            com.elite.sch.app.data.competition.CompetitionSync.markAbandonPending()
            serviceScope.launch {
                com.elite.sch.app.data.competition.CompetitionSync.abandonActiveMission()
            }
        }
        stopSelf()
    }

        private fun startInactivityMonitor() {
            inactivityMonitorJob?.cancel()
            inactivityMonitorJob = serviceScope.launch {
                while (isActive && isCompetitionRace && _runState.value.isRunning) {
                    delay(30_000L) // Verifica a cada 30 segundos
                    if (!isPaused) {
                        val now = SystemClock.elapsedRealtime()
                        val inactiveTime = now - lastGpsMovementTime
                        if (inactiveTime >= INACTIVITY_TIMEOUT_MS) {
                            // 10 minutos sem movimento GPS - abandona a competição automaticamente
                            handleCompetitionInactivity()
                            break
                        }
                    }
                }
            }
        }

        private fun stopInactivityMonitor() {
            inactivityMonitorJob?.cancel()
            inactivityMonitorJob = null
            isCompetitionRace = false
        }

        private fun handleCompetitionInactivity() {
            // Para o tracking GPS
            stopLocationUpdates()
            stopInactivityMonitor()
        
            // Notifica o usuário via TTS
            speakAndVibrate("Você foi removido da competição por inatividade. Sem sinal de GPS por 10 minutos.", vibrate = true, isRun = false)
        
            // Para a corrida
            _runState.value = _runState.value.copy(isRunning = false, isFinished = true)
        
            // Keep a retry marker if Firestore is temporarily unreachable.
            com.elite.sch.app.data.competition.CompetitionSync.markAbandonPending()
            serviceScope.launch {
                com.elite.sch.app.data.competition.CompetitionSync.abandonActiveMission()
            }
        
            // Para o serviço
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = serviceScope.launch {
            while (isActive && _runState.value.isRunning) {
                delay(1000L)
                if (!isPaused) {
                    val currentElapsedMillis = SystemClock.elapsedRealtime() - startRealtime - totalPausedMillis
                    val elapsedSeconds = currentElapsedMillis / 1000L

                    val schedule = StepScheduler.evaluate(steps, elapsedSeconds)
                    val isFreeRun = steps.isEmpty()

                    val pace = PaceCalculator.calculateAveragePaceSeconds(
                        totalSeconds = elapsedSeconds,
                        totalDistanceMeters = totalDistanceMeters
                    )
                    val paceFormatted = PaceCalculator.formatPace(pace)

                    // Lógica de marcos de distância configurável (avisoDistanciaMetros)
                    if (!isFreeRun && avisoDistanciaAtivo && avisoDistanciaMetros > 0) {
                        val (crossedIndices, newIndex) = KmMilestoneTracker.checkAndAdvanceInterval(
                            lastAnnouncedMilestoneIndex,
                            totalDistanceMeters,
                            avisoDistanciaMetros
                        )
                        if (crossedIndices.isNotEmpty()) {
                            lastAnnouncedMilestoneIndex = newIndex
                            val lastCrossedIndex = crossedIndices.last()
                            val milestoneDistanceMeters = lastCrossedIndex * avisoDistanciaMetros

                            val minutes = (elapsedSeconds / 60).toInt()
                            val seconds = (elapsedSeconds % 60).toInt()
                            val paceMin = (pace?.div(60) ?: 0.0).toInt()
                            val paceSec = (pace?.rem(60) ?: 0.0).toInt()

                            val distanceText = formatMilestoneDistance(milestoneDistanceMeters)
                            val message = getString(R.string.milestone_announcement, distanceText, minutes, seconds, paceMin, paceSec)
                            speakAndVibrate(message, vibrate = true)
                        }
                    }

                    // Transições e avisos sonoros
                    if (!isFreeRun) {
                        if (schedule.currentStepIndex != lastStepIndexAnnounced) {
                            lastStepIndexAnnounced = schedule.currentStepIndex
                            hasAnnouncedTenSecondsForStep = false
                            val cueText = when (schedule.currentStep?.type) {
                                StepType.RUN -> getString(R.string.voice_step_run)
                                else -> getString(R.string.voice_step_walk)
                            }
                            val isRun = schedule.currentStep?.type == StepType.RUN
                            speakAndVibrate(cueText, vibrate = true, isRun = isRun)
                        } else if (schedule.pendingVoiceCue == VoiceCue.TenSecondsRemaining && !hasAnnouncedTenSecondsForStep) {
                            hasAnnouncedTenSecondsForStep = true
                            speakAndVibrate(getString(R.string.voice_ten_seconds_remaining), vibrate = false)
                        }

                        if (schedule.isFinished && !_runState.value.isFinished) {
                            speakAndVibrate(getString(R.string.voice_workout_completed), vibrate = true, isRun = false)
                        }
                    } else {
                        // Lógica de marcos de KM para corrida livre
                        val (crossedKms, newLastKm) = KmMilestoneTracker.checkAndAdvance(lastAnnouncedKm, totalDistanceMeters)
                        if (crossedKms.isNotEmpty()) {
                            lastAnnouncedKm = newLastKm
                            val lastKm = crossedKms.last()
                            val minutes = (elapsedSeconds / 60).toInt()
                            val seconds = (elapsedSeconds % 60).toInt()
                            val paceMin = (pace?.div(60) ?: 0.0).toInt()
                            val paceSec = (pace?.rem(60) ?: 0.0).toInt()

                            val message = if (lastKm == 1) {
                                getString(R.string.voice_km_milestone_singular, minutes, seconds, paceMin, paceSec)
                            } else {
                                getString(R.string.voice_km_milestone_plural, lastKm, minutes, seconds, paceMin, paceSec)
                            }
                            speakAndVibrate(message, vibrate = true)
                        }
                    }

                    _runState.value = _runState.value.copy(
                        elapsedSeconds = elapsedSeconds,
                        currentStepIndex = schedule.currentStepIndex,
                        currentStep = schedule.currentStep,
                        timeRemainingInStep = schedule.timeRemainingInStep,
                        nextStep = schedule.nextStep,
                        isFinished = if (isFreeRun) false else schedule.isFinished,
                        totalDistanceMeters = totalDistanceMeters,
                        averagePaceSeconds = pace,
                        averagePaceFormatted = paceFormatted
                    )

                    updateNotification(
                        status = schedule.currentStep?.instruction ?: "Corrida",
                        elapsedSeconds = elapsedSeconds
                    )

                    // Grava estado ativo a cada 5 segundos
                    com.elite.sch.app.data.competition.CompetitionSync.tick(applicationContext, _runState.value)
                    if (elapsedSeconds % 5 == 0L) {
                        saveActiveRunToDb()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2500L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                if (isPaused) return
                // Processa TODAS as localizações do lote, em ordem cronológica
                val fixes = result.locations.map { loc ->
                    RawFix(
                        latitude = loc.latitude,
                        longitude = loc.longitude,
                        accuracy = loc.accuracy,
                        timeMillis = loc.time,
                        elapsedRealtimeMillis = loc.elapsedRealtimeNanos / 1_000_000L,
                        isMock = androidx.core.location.LocationCompat.isMock(loc)
                    )
                }
                if (fixes.isEmpty()) return
                val results = gpsSession.processBatch(fixes)
                var lastMapPoint: MapPoint? = null
                    var hadMovement = false
                results.forEach { r ->
                    r.point?.let { geoPoint ->
                        savePointToDb(geoPoint)
                        val mapPoint = MapPoint(
                            latitude = geoPoint.latitude,
                            longitude = geoPoint.longitude,
                            accuracy = geoPoint.accuracy,
                            timestampMillis = geoPoint.timestampMillis
                        )
                        lastMapPoint = mapPoint
                        routePoints.add(mapPoint)
                            hadMovement = true
                    }
                }
                totalDistanceMeters = gpsSession.totalDistanceMeters
                val last = result.lastLocation
                
                    // Atualiza timestamp do último movimento GPS válido (para monitor de inatividade em competição)
                    if (hadMovement && isCompetitionRace) {
                        lastGpsMovementTime = SystemClock.elapsedRealtime()
                    }
                
                _runState.value = _runState.value.copy(
                    hasGpsSignal = results.any { it.accepted },
                    gpsAccuracy = last?.accuracy ?: _runState.value.gpsAccuracy,
                    totalDistanceMeters = totalDistanceMeters,
                    gpsDiagnostics = gpsSession.diagnostics(),
                    currentMapPoint = lastMapPoint ?: _runState.value.currentMapPoint,
                    routePoints = routePoints.toList()
                )
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        } catch (_: SecurityException) {
            _runState.value = _runState.value.copy(hasGpsSignal = false)
        }
    }

    private fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null
    }

    private fun savePointToDb(point: GeoPoint) {
        // Grava todo ponto válido, usando o timestamp da própria localização
        serviceScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(currentModality, applicationContext)
            db.routePointDao().insertPoint(
                RoutePointEntity(
                    workoutId = workoutId,
                    latitude = point.latitude,
                    longitude = point.longitude,
                    accuracy = point.accuracy,
                    timestampMillis = point.timestampMillis
                )
            )
        }
    }

    private fun saveActiveRunToDb() {
        serviceScope.launch(Dispatchers.IO) {
            val db = DatabaseProvider.getDatabase(currentModality, applicationContext)
            db.activeRunDao().saveActiveRun(
                ActiveRunEntity(
                    workoutId = workoutId,
                    startTimeMillis = System.currentTimeMillis() - (_runState.value.elapsedSeconds * 1000L),
                    elapsedRealtimeBase = startRealtime,
                    pausedElapsedMillis = totalPausedMillis,
                    isPaused = isPaused,
                    totalDistanceMeters = totalDistanceMeters,
                    lastUpdatedMillis = System.currentTimeMillis(),
                    modality = currentModality
                )
            )
        }
    }

    private fun setupUtteranceProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // New utterance started - mark that we have an active utterance
                isTtsSpeaking = true
            }

            override fun onDone(utteranceId: String?) {
                // Only release focus if no new utterance has been queued since this one started
                if (!isTtsSpeaking) {
                    releaseAudioFocus()
                }
                isTtsSpeaking = false
            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                // Only release focus if no new utterance has been queued since this one started
                if (!isTtsSpeaking) {
                    releaseAudioFocus()
                }
                isTtsSpeaking = false
            }

            @Deprecated("Deprecated in Java", ReplaceWith("releaseAudioFocus()"))
            override fun onError(utteranceId: String?) {
                if (!isTtsSpeaking) {
                    releaseAudioFocus()
                }
                isTtsSpeaking = false
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                if (!isTtsSpeaking) {
                    releaseAudioFocus()
                }
                isTtsSpeaking = false
            }
        })
    }

    /** Tracks whether TTS is currently speaking an utterance. Set to true in onStart, false in onDone/onStop/onError. */
    private var isTtsSpeaking = false

    private fun releaseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentAudioFocusRequest?.let { req ->
                audioManager?.abandonAudioFocusRequest(req)
            }
            currentAudioFocusRequest = null
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(null)
        }
    }

    private fun speakAndVibrate(text: String, vibrate: Boolean, isRun: Boolean = false) {
        if (_runState.value.voiceEnabled) {
            val audioResource = voiceCommandResource(text)
            if (audioResource != null) {
                playVoiceCommand(audioResource)
            } else if (isTtsInitialized) {
                requestAudioFocusAndSpeak(text)
            }
        }
        if (vibrate && _runState.value.vibrationEnabled) {
            vibrateDevice(isRun)
        }
    }

    private fun voiceCommandResource(text: String): Int? = when (text) {
        getString(R.string.voice_free_run_start) -> R.raw.voice_corrida_livre
        getString(R.string.voice_step_run) -> R.raw.voice_agora_corra
        getString(R.string.voice_step_walk) -> R.raw.voice_agora_ande
        getString(R.string.voice_ten_seconds_remaining) -> R.raw.voice_dez_segundos
        getString(R.string.voice_workout_completed) -> R.raw.voice_treino_concluido
        else -> null
    }

    private fun playVoiceCommand(audioResource: Int) {
        commandPlayer?.release()
        commandPlayer = null
        tts?.stop()
        releaseAudioFocus()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(audioAttributes)
            .build()
        currentAudioFocusRequest = focusRequest
        audioManager?.requestAudioFocus(focusRequest)

        val player = MediaPlayer.create(this, audioResource) ?: run {
            releaseAudioFocus()
            return
        }
        commandPlayer = player
        player.setAudioAttributes(audioAttributes)
        player.setOnCompletionListener { finishedPlayer ->
            if (commandPlayer === finishedPlayer) commandPlayer = null
            finishedPlayer.release()
            releaseAudioFocus()
        }
        player.setOnErrorListener { failedPlayer, _, _ ->
            if (commandPlayer === failedPlayer) commandPlayer = null
            failedPlayer.release()
            releaseAudioFocus()
            true
        }
        try {
            player.start()
        } catch (_: IllegalStateException) {
            if (commandPlayer === player) commandPlayer = null
            player.release()
            releaseAudioFocus()
        }
    }

    private fun requestAudioFocusAndSpeak(text: String) {
        val utteranceId = "RUN_TTS_${System.currentTimeMillis()}"
        // Mark that a new utterance is being queued - this prevents the previous utterance's
        // onStop/onDone from releasing audio focus prematurely.
        isTtsSpeaking = true
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(audioAttributes)
                .build()

            currentAudioFocusRequest = focusRequest
            audioManager?.requestAudioFocus(focusRequest)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                null,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )
            @Suppress("DEPRECATION")
            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId
            @Suppress("DEPRECATION")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params)
        }
    }

    private fun vibrateDevice(isRun: Boolean) {
        try {
            if (vibrator?.hasVibrator() != true) return
            vibrator?.cancel()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isRun) {
                    // Vibração dupla e energética para correr
                    val pattern = longArrayOf(0, 150, 80, 150)
                    val amplitudes = intArrayOf(0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
                } else {
                    // Vibração simples para caminhar/avisos
                    vibrator?.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                @Suppress("DEPRECATION")
                if (isRun) {
                    val pattern = longArrayOf(0, 150, 80, 150)
                    vibrator?.vibrate(pattern, -1)
                } else {
                    vibrator?.vibrate(250)
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Corrida em Andamento",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Acompanhamento da corrida ativa no Elite"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(status: String, elapsedSeconds: Long, isPaused: Boolean): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Ação de pausar/retomar
        val toggleIntent = Intent(this, RunTrackingService::class.java).apply {
            action = ACTION_TOGGLE_PAUSE_RESUME
        }
        val togglePendingIntent = PendingIntent.getService(
            this, 0, toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val minutes = elapsedSeconds / 60
        val seconds = elapsedSeconds % 60
        val timeFormatted = "%02d:%02d".format(minutes, seconds)
        val distKm = "%.2f km".format(totalDistanceMeters / 1000.0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title_format, timeFormatted, distKm))
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        // Botão único: Pausar ou Retomar conforme estado
        val actionLabel = if (isPaused) getString(R.string.notification_action_resume) else getString(R.string.notification_action_pause)
        builder.addAction(NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_pause,
            actionLabel,
            togglePendingIntent
        ).build())

        return builder.build()
    }

    private fun updateNotification(status: String, elapsedSeconds: Long) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, buildNotification(status, elapsedSeconds, isPaused))
    }

    private fun formatMilestoneDistance(meters: Int): String {
        return when {
            meters < 1000 -> getString(R.string.milestone_format_meters, meters)
            meters == 1000 -> getString(R.string.milestone_format_km_singular)
            meters % 1000 == 0 -> getString(R.string.milestone_format_km_plural, meters / 1000)
            else -> {
                val km = meters / 1000
                val remaining = meters % 1000
                if (remaining == 500) {
                    getString(R.string.milestone_format_km_and_half, km, if (km > 1) "s" else "")
                } else {
                    getString(R.string.milestone_format_meters, meters)
                }
            }
        }
    }

    override fun onDestroy() {
        tickerJob?.cancel()
        countdownJob?.cancel()
        inactivityMonitorJob?.cancel()
        serviceScope.coroutineContext[kotlinx.coroutines.Job]?.cancel()
        stopLocationUpdates()
        releaseAudioFocus()
        commandPlayer?.release()
        commandPlayer = null
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
