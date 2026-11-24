package com.elite.sch.app.ui.competition

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.elite.sch.app.data.competition.OnlineCompetitionRepository
import com.elite.sch.app.data.competition.OnlineMission
import com.elite.sch.app.data.competition.OnlineRankedUser
import com.elite.sch.app.data.competition.OnlineMissionResult
import com.elite.sch.app.data.competition.ServerTimeSync
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.elite.sch.app.service.RunTrackingService
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

enum class CompetitionDistance(val label: String, val meters: Int) {
    K25("2,5K", 2_500), K5("5K", 5_000), K10("10K", 10_000)
}

enum class MissionStatus {
    OPEN, NEEDS_ONE, CONFIRMED, LIVE, FINISHED, NOT_STARTED
}

data class DemoMission(
    val time: String,
    val distance: CompetitionDistance,
    val status: MissionStatus,
    val participants: Int,
    val id: String = "",
    val startsAt: Timestamp? = null,
    val timeZone: String = "America/Sao_Paulo",
    val results: List<OnlineMissionResult> = emptyList(),
    /** ms restantes para entrada tardia; >0 significa que ainda pode entrar após a largada */
    val lateJoinWindowMs: Long = 0L
)

val DEMO_LATE_JOIN_WINDOW_MS = 5L * 60 * 1000 // 5 minutos

fun DemoMission.canLateJoin(nowMs: Long = ServerTimeSync.nowSync()): Boolean = lateJoinWindowMs > 0L
fun DemoMission.elapsedMinutesSinceStart(nowMs: Long = ServerTimeSync.nowSync()): Long =
    ((nowMs - (startsAt?.toDate()?.time ?: 0L)) / 60_000L).coerceAtLeast(0L)

fun DemoMission.readyRunners(nowMs: Long = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()): Int = results.count {
    val ready = it.readyAt?.toDate()?.time
    ready != null && nowMs - ready in 0L..45_000L && it.finishedAt == null && it.withdrawnAt == null && !it.pending
}

data class CompetitionDemoState(
    val distance: CompetitionDistance = CompetitionDistance.K5,
    val selectedMission: DemoMission = DemoMission("--:--", CompetitionDistance.K5, MissionStatus.OPEN, 0),
    val confirmed: Boolean = false,
    val raceFinished: Boolean = false,
    val distanceMeters: Int = 0,
    val resultTabState: Boolean = false,
    val rankingScope: Boolean = false,
    val rankingDistance: CompetitionDistance? = null,
    val rankingHistory: Boolean = false,
    val achievementLocal: Boolean = false,
    val noticePush: Boolean = false,
    val shareKind: Int = 0,
    val onlineMissions: List<DemoMission> = emptyList(),
    val onlineRanking: List<OnlineRankedUser> = emptyList(),
    val onlineStateRanking: List<OnlineRankedUser> = emptyList(),
    val onlineUser: OnlineRankedUser? = null,
    val userProfileLoaded: Boolean = false,
    val onlineSyncError: String? = null,
    val online: Boolean = false
)

/** UI state for the live competition flow. */
class CompetitionDemoViewModel : ViewModel() {
    private val startMutex = Mutex()
    private val repository = OnlineCompetitionRepository()
    private val _state = MutableStateFlow(CompetitionDemoState())
    val state: StateFlow<CompetitionDemoState> = _state.asStateFlow()
    private var missionsJob: Job? = null
    private var rankingJob: Job? = null
    private var stateRankingJob: Job? = null
    private var progressJob: Job? = null
    private var userJob: Job? = null
    private var observedCompetitionState: String? = null
    private val auth = FirebaseAuth.getInstance()
    private var observedUid: String? = null
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val uid = firebaseAuth.currentUser?.uid
        if (uid != observedUid) {
            observedUid = uid
            onAccountChanged(uid)
        }
    }

    init {
        viewModelScope.launch {
            com.elite.sch.app.data.competition.CompetitionSync.error.collect { error ->
                _state.update { it.copy(onlineSyncError = error) }
            }
        }
            // Inicializa sincronização de horário com servidor
        viewModelScope.launch {
            while (true) {
                if (auth.currentUser != null) {
                    try { ServerTimeSync.now() }
                    catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled }
                    catch (error: Exception) { reportError(error.message ?: "Horario indisponivel.") }
                    com.elite.sch.app.data.competition.CompetitionSync.scoreConfirmedFinish()
                }
                kotlinx.coroutines.delay(30_000L)
            }
        }
        viewModelScope.launch {
            com.elite.sch.app.data.competition.CompetitionSync.finishedMission.collect { missionId ->
                if (missionId != null && missionId == _state.value.selectedMission.id) {
                    _state.update { it.copy(raceFinished = true) }
                }
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun onAccountChanged(uid: String?) {
        missionsJob?.cancel(); missionsJob = null
        rankingJob?.cancel(); rankingJob = null
        stateRankingJob?.cancel(); stateRankingJob = null
        userJob?.cancel(); userJob = null
        progressJob?.cancel(); progressJob = null
        observedCompetitionState = null
        if (!com.elite.sch.app.data.competition.CompetitionSync.belongsTo(uid)) {
            com.elite.sch.app.data.competition.CompetitionSync.stop()
            ServerTimeSync.invalidate()
        }
        _state.update { current ->
            CompetitionDemoState(distance = CompetitionDistance.entries.firstOrNull {
                com.elite.sch.app.data.competition.CompetitionSync.belongsTo(uid) &&
                    it.meters == com.elite.sch.app.data.competition.CompetitionSync.activeTargetMeters()
            } ?: current.distance, online = false)
        }
        if (uid != null) {
            com.elite.sch.app.data.competition.CompetitionSync.restoreConfirmedFinish()
            viewModelScope.launch {
                com.elite.sch.app.data.competition.CompetitionSync.retryPendingAbandon()
                com.elite.sch.app.data.competition.CompetitionSync.retryPendingFinish()
            }
            observeDistance(_state.value.distance)
            observeRanking(null)
            userJob = viewModelScope.launch {
                repository.observeCurrentUser()
                    .catch { error -> _state.update { it.copy(onlineSyncError = error.message, userProfileLoaded = true) } }
                    .collect { user ->
                        val newState = user?.state?.trim()?.uppercase()?.takeIf { it.length == 2 }
                        val changed = newState != observedCompetitionState
                        observedCompetitionState = newState
                        _state.update { it.copy(onlineUser = user, userProfileLoaded = true) }
                        // A change in profile UF must replace the active room feed immediately.
                        if (changed && newState != null) {
                            observeDistance(_state.value.distance)
                            observeRanking(newState)
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        auth.removeAuthStateListener(authListener)
        missionsJob?.cancel(); rankingJob?.cancel(); stateRankingJob?.cancel(); userJob?.cancel(); progressJob?.cancel()
        super.onCleared()
    }

    private fun observeRanking(state: String?) {
        if (state == null) rankingJob?.cancel() else stateRankingJob?.cancel()
        val job = viewModelScope.launch {
            repository.observeRanking(state)
                .catch { error -> _state.update { it.copy(onlineSyncError = error.message) } }
                .collect { ranking -> _state.update {
                    if (state == null) it.copy(onlineRanking = ranking) else it.copy(onlineStateRanking = ranking)
                } }
        }
        if (state == null) rankingJob = job else stateRankingJob = job
    }

    fun chooseDistance(value: CompetitionDistance) {
        _state.update { it.copy(distance = value) }
        observeDistance(value)
    }

    private fun observeDistance(distance: CompetitionDistance) {
        missionsJob?.cancel()
        missionsJob = viewModelScope.launch {
            try { ServerTimeSync.now() } catch (error: Exception) {
                reportError(error.message ?: "Sem conexao com o servidor."); return@launch
            }
            repository.observeMissions(distance, _state.value.selectedMission.id)
                .combine(flow { while (true) { emit(ServerTimeSync.nowSync()); kotlinx.coroutines.delay(1_000L) } }) { missions, _ -> missions }
                .catch { error ->
                    // Cancelling the old listener while the profile UF changes is expected.
                    if (error is kotlinx.coroutines.CancellationException) return@catch
                    _state.update { it.copy(onlineSyncError = error.message, online = false) }
                }
                .collect { missions ->
                    val converted = missions.map { it.toDemoMission() }
                        .filterNot { mission ->
                            mission.status == MissionStatus.NOT_STARTED &&
                                (mission.startsAt?.toDate()?.time ?: Long.MAX_VALUE) < ServerTimeSync.nowSync()
                        }
                        .filterNot { mission ->
                            mission.status == MissionStatus.NEEDS_ONE &&
                                (mission.startsAt?.toDate()?.time ?: Long.MAX_VALUE) < ServerTimeSync.nowSync()
                        }
                    val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
                                        _state.update { current ->
                        val selected = converted.firstOrNull { mission -> mission.id == current.selectedMission.id }
                            ?: converted.firstOrNull { mission -> mission.id == com.elite.sch.app.data.competition.CompetitionSync.finishedMission.value }
                            ?: current.selectedMission
                        current.copy(onlineMissions = converted, online = missions.none { it.fromCache }, onlineSyncError = null, selectedMission = selected,
                                                confirmed = selected.readyRunners(serverNow) >= 2,
                                                raceFinished = current.raceFinished || selected.id == com.elite.sch.app.data.competition.CompetitionSync.finishedMission.value)
                    }
                }
        }
    }
    fun chooseMission(mission: DemoMission) {
        val changed = mission.id != _state.value.selectedMission.id
        _state.update {
            it.copy(selectedMission = mission,
                raceFinished = if (changed) false else it.raceFinished,
                confirmed = mission.readyRunners() >= 2)
        }
        if (changed) observeDistance(mission.distance)
    }

    fun reportError(message: String) { _state.update { it.copy(onlineSyncError = message) } }

    suspend fun startRace(context: android.content.Context, mission: DemoMission): Boolean = startMutex.withLock {
        val uid = auth.currentUser?.uid ?: return@withLock false
        try {
            val serverNow = ServerTimeSync.now()
            val existing = com.elite.sch.app.data.competition.CompetitionSync.activeMission()
            check(existing == null || existing == mission.id) { "Finalize ou abandone sua outra prova primeiro." }
            check(serverNow >= (mission.startsAt?.toDate()?.time ?: Long.MAX_VALUE)) { "Aguarde a largada." }
            if (existing != mission.id) {
                val elapsedMs = serverNow - mission.startsAt!!.toDate().time
                check(elapsedMs <= DEMO_LATE_JOIN_WINDOW_MS) {
                    val remainingMin = ((DEMO_LATE_JOIN_WINDOW_MS - elapsedMs) / 1000L / 60L).coerceAtLeast(0L)
                    if (remainingMin > 0) "Janela encerrada — resta ${remainingMin}min para entrar." else "Janela de entrada encerrada (5 min)."
                }
                check(mission.readyRunners(serverNow) >= 2) { "Aguarde dois corredores presentes." }
                val permission = androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                check(permission == android.content.pm.PackageManager.PERMISSION_GRANTED) { "Autorize a localizacao precisa antes de correr." }
                val manager = context.getSystemService(android.content.Context.LOCATION_SERVICE) as android.location.LocationManager
                check(androidx.core.location.LocationManagerCompat.isLocationEnabled(manager)) { "Ative a localizacao do celular." }
                check(!RunTrackingService.runState.value.isRunning) { "Encerre o treino atual antes de iniciar esta prova." }
                repository.claimStart(mission, com.elite.sch.app.data.competition.CompetitionSync.deviceSessionId())
                val workoutId = System.currentTimeMillis()
                val intent = android.content.Intent(context, RunTrackingService::class.java).apply {
                    action = RunTrackingService.ACTION_START
                    putExtra(RunTrackingService.EXTRA_WORKOUT_ID, workoutId)
                    putExtra(RunTrackingService.EXTRA_WORKOUT_TITLE, "Missao " + mission.distance.label)
                    putExtra(RunTrackingService.EXTRA_MODALITY, "livre")
                    putExtra("COMPETITION_START", true)
                    putExtra(RunTrackingService.EXTRA_USE_GPS, true)
                }
                androidx.core.content.ContextCompat.startForegroundService(context, intent)
                withTimeout(10_000L) {
                    RunTrackingService.runState.first { it.workoutId == workoutId && it.isRunning && it.startCountdown == null }
                }
            }
            check(auth.currentUser?.uid == uid) { "A conta mudou." }
            com.elite.sch.app.data.competition.CompetitionSync.start(mission)
            _state.update { it.copy(onlineSyncError = null) }
            true
        } catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled }
        catch (error: Exception) { reportError(error.message ?: "Nao foi possivel largar."); false }
    }

    fun stopOnlineProgressSync() {
        com.elite.sch.app.data.competition.CompetitionSync.stop()
    com.elite.sch.app.data.competition.CompetitionSync.setOnFinishCallback(null)
    }

    fun joinMission(mission: DemoMission, onDone: (DemoMission) -> Unit) {
        val requestUid = auth.currentUser?.uid
        viewModelScope.launch {
            runCatching {
                val currentState = repository.currentUserState()
                    ?: error("Configure sua UF no perfil antes de entrar na competição.")
                val candidate = if (mission.id.isBlank()) OnlineCompetitionRepository.nextMission(mission.distance, mission.time, currentState) else OnlineMission(
                    id = mission.id,
                    distance = mission.distance,
                    startsAt = mission.startsAt ?: OnlineCompetitionRepository.nextMission(mission.distance, state = currentState).startsAt,
                    participants = mission.participants,
                    state = currentState,
                    timeZone = com.elite.sch.app.data.competition.competitionTimeZoneIdForState(currentState),
                    maxPlayers = Int.MAX_VALUE
                )
                repository.joinMission(candidate)
            }.onSuccess { joined ->
                if (auth.currentUser?.uid != requestUid) return@onSuccess
                val updated = joined.toDemoMission()
                val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
                _state.update { it.copy(selectedMission = updated, confirmed = updated.readyRunners(serverNow) >= 2) }
                observeDistance(updated.distance)
                onDone(updated)
            }.onFailure { error ->
                Log.e("OnlineCompetition", "Falha ao entrar na missão online", error)
                _state.update { it.copy(onlineSyncError = error.message) }
            }
        }
    }
    fun confirmMission() = Unit

    suspend fun updateRoomPresence(mission: DemoMission) {
        if (mission.id.isNotBlank()) repository.updateRoomPresence(mission.id)
    }

    fun leaveMission(mission: DemoMission, onDone: () -> Unit, onConfirmAbandon: (() -> Unit)? = null) {
        if (mission.id.isBlank()) {
            onDone()
            return
        }
        
            val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
            val startsAtMs = mission.startsAt?.toDate()?.time ?: Long.MAX_VALUE
            val hasStarted = startsAtMs <= serverNow
        
            if (hasStarted) {
                // Prova já começou: pede confirmação de abandono
                onConfirmAbandon?.invoke()
                return
            }
        
            // Prova não começou: sai direto
        viewModelScope.launch {
            runCatching { repository.leaveMission(mission.id) }
                .onSuccess {
                    _state.update { it.copy(confirmed = false, selectedMission = it.selectedMission.copy(participants = (it.selectedMission.participants - 1).coerceAtLeast(0))) }
                    onDone()
                }
                .onFailure { error ->
                    Log.e("OnlineCompetition", "Falha ao sair da missão", error)
                    _state.update { it.copy(onlineSyncError = error.message) }
                }
        }
    }
    
        fun confirmAbandonMission(mission: DemoMission, onDone: () -> Unit) {
            if (com.elite.sch.app.data.competition.CompetitionSync.hasPendingFinish()) {
                reportError("Chegada pendente: aguarde a confirmacao do servidor antes de sair.")
                return
            }
            if (mission.id.isBlank()) {
                onDone()
                return
            }
        
            // Para o GPS tracking
            viewModelScope.launch {
                runCatching { repository.leaveMission(mission.id) }
                    .onSuccess {
                        stopOnlineProgressSync()
                        val context = com.elite.sch.app.data.competition.CompetitionSync.context
                        context.startService(android.content.Intent(context, RunTrackingService::class.java).apply {
                            action = RunTrackingService.ACTION_STOP
                        })
                        _state.update { it.copy(confirmed = false, raceFinished = false, distanceMeters = 0) }
                        onDone()
                    }
                    .onFailure { error ->
                        Log.e("OnlineCompetition", "Falha ao abandonar missão", error)
                        _state.update { it.copy(onlineSyncError = error.message) }
                    }
            }
        }
    fun updateRaceDistance(meters: Int) = _state.update { it.copy(distanceMeters = meters.coerceIn(0, it.selectedMission.distance.meters)) }
    fun finishRace() = _state.update { it.copy(raceFinished = true, distanceMeters = it.selectedMission.distance.meters) }
    fun setResultState(state: Boolean) = _state.update { it.copy(resultTabState = state) }
    fun setRankingScope(state: Boolean) {
        _state.update { it.copy(rankingScope = state) }
    }
    fun setRankingDistance(value: CompetitionDistance?) = _state.update { it.copy(rankingDistance = value) }
    fun setRankingHistory(value: Boolean) = _state.update { it.copy(rankingHistory = value) }
    fun setAchievementLocal(value: Boolean) = _state.update { it.copy(achievementLocal = value) }
    fun setNoticePush(value: Boolean) = _state.update { it.copy(noticePush = value) }
    fun setShareKind(value: Int) = _state.update { it.copy(shareKind = value.coerceIn(0, 5)) }
}

private fun OnlineMission.toDemoMission(): DemoMission {
    val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
    // A room is shown in the timezone chosen for its UF, never in the
    // emulator's timezone or a runner's manually altered device clock.
    val localTime = startsAt.toDate().toInstant().atZone(
        runCatching { java.time.ZoneId.of(timeZone) }.getOrDefault(com.elite.sch.app.data.competition.SEASON_ZONE_ID)
    )
    val started = localTime.toInstant().toEpochMilli() <= serverNow
    val allDone = results.isNotEmpty() && results.all { it.finishedAt != null || it.withdrawnAt != null }
    val hasFinisher = results.any { it.finishedAt != null }
    val status = when {
        started && allDone -> if (hasFinisher) MissionStatus.FINISHED else MissionStatus.NOT_STARTED
        started && serverNow > startsAt.toDate().time + 3 * 60 * 60 * 1000L -> if (hasFinisher) MissionStatus.FINISHED else MissionStatus.NOT_STARTED
        started && results.none { it.startedAt != null } && serverNow > startsAt.toDate().time + 60_000L -> MissionStatus.NOT_STARTED
        started && participants < 2 -> if (hasFinisher) MissionStatus.FINISHED else MissionStatus.NOT_STARTED
        started && results.none { it.startedAt != null } -> MissionStatus.CONFIRMED
        started -> MissionStatus.LIVE
        participants >= 2 -> MissionStatus.CONFIRMED
        participants == 1 -> MissionStatus.NEEDS_ONE
        else -> MissionStatus.OPEN
    }
    return DemoMission(
        time = localTime.toLocalTime().toString().take(5),
        distance = distance,
        status = status,
        participants = participants,
        id = id,
        startsAt = startsAt,
        timeZone = timeZone,
        results = results,
        lateJoinWindowMs = lateJoinWindowMs
    )
}
