package com.elite.sch.app.data.competition

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.elite.sch.app.service.RunState
import com.elite.sch.app.service.RunTrackingService
import com.elite.sch.app.ui.competition.DemoMission
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Service-owned producer. Firestore's persistent Android write queue handles network retries.
 * Route evidence remains in Room; only a bounded private GPS window is replicated remotely.
 */
object CompetitionSync {
    val context get() = FirebaseApp.getInstance().applicationContext
    private fun prefs(context: Context) = context.getSharedPreferences("competition_sync", Context.MODE_PRIVATE)
    private var lastEnqueuedAt = 0L
    private var lastGpsSentAt = 0L
    private var inFlight = false
    private var scoringInFlight = false
    private var generation = 0L
    private val _finishedMission = MutableStateFlow<String?>(null)
    val finishedMission = _finishedMission.asStateFlow()
    private val _scoredMission = MutableStateFlow<String?>(null)
    val scoredMission = _scoredMission.asStateFlow()
    fun belongsTo(uid: String?): Boolean = uid != null && prefs(context).getString("uid", null) == uid
    fun deviceSessionId(): String {
        val store = context.getSharedPreferences("competition_device", Context.MODE_PRIVATE)
        return store.getString("session", null) ?: java.util.UUID.randomUUID().toString().also {
            store.edit().putString("session", it).commit()
        }
    }
    fun activeMission(): String? = prefs(context).getString("mission", null)
    fun activeTargetMeters(): Int = prefs(context).getInt("target", 0)
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    // Callback para notificar quando a prova termina (detectado no serviço)
    private var onFinishCallback: (() -> Unit)? = null
    fun setOnFinishCallback(callback: (() -> Unit)?) { onFinishCallback = callback }
    private fun notifyFinish() { onFinishCallback?.invoke() }

    fun start(mission: DemoMission) {
        val uid = requireNotNull(FirebaseAuth.getInstance().currentUser?.uid) { "Faça login para competir." }
        val run = RunTrackingService.runState.value
        require(run.isRunning && !run.isFinished) { "Inicie o rastreamento GPS antes de entrar na prova." }
        require(mission.startsAt != null) { "Missão sem horário de largada." }
        // Usa horário do servidor para validar largada
        require(ServerTimeSync.isFresh) { "Sincronize o horario antes da largada." }
        val serverNow = ServerTimeSync.nowSync()
        require(serverNow >= mission.startsAt.toDate().time) { "Aguarde o horário da largada." }
        require(run.isGpsRequested) { "Ative o GPS para participar da competição." }
        val p = prefs(context)
        if (p.getString("mission", null) == mission.id && p.getString("uid", null) == uid) {
            if (p.getBoolean("finishConfirmed", false)) _finishedMission.value = mission.id
            else if (p.contains("finishDistance")) retryPendingFinish()
            return
        }
        check(!p.contains("finishDistance") || p.getBoolean("scoreConfirmed", false)) {
            "O resultado anterior aguarda pontuacao do servidor. Entre na conta original para sincronizar."
        }
        generation++
        inFlight = false
        _finishedMission.value = null
        _scoredMission.value = null
        p.edit().clear().putString("mission", mission.id).putString("uid", uid)
            .putLong("workout", run.workoutId).putInt("target", mission.distance.meters)
            .putFloat("baseDistance", run.totalDistanceMeters.toFloat()).putLong("baseTime", run.elapsedSeconds)
            .putLong("captureStartedAt", System.currentTimeMillis())
            .putLong("startsAt", mission.startsAt.toDate().time).apply()
        lastEnqueuedAt = 0L
        lastGpsSentAt = 0L
        _error.value = null
    }

    fun stop() {
        generation++
        inFlight = false
        onFinishCallback = null
        _finishedMission.value = null
        _scoredMission.value = null
        _error.value = null
        val p = prefs(context)
        if (!p.contains("finishDistance") || p.getBoolean("scoreConfirmed", false)) p.edit().clear().apply()
    }

    /** Keeps an offline abandonment retry until Firestore confirms it. */
    suspend fun abandonActiveMission(): Boolean {
        val mission = activeMission() ?: return true
        val uid = prefs(context).getString("uid", null)
        if (uid == null || uid != FirebaseAuth.getInstance().currentUser?.uid) return false
        if (prefs(context).contains("finishDistance")) {
            _error.value = "Chegada pendente: aguarde a confirmacao do servidor antes de sair."
            return false
        }
        markAbandonPending()
        return try {
            OnlineCompetitionRepository().leaveMission(mission)
            stop()
            true
        } catch (failure: Exception) {
            _error.value = "Nao foi possivel registrar a desistência. Ela sera reenviada quando houver conexao."
            false
        }
    }

    suspend fun retryPendingAbandon() {
        if (prefs(context).getBoolean("abandonPending", false)) abandonActiveMission()
    }

    fun markAbandonPending() {
        if (activeMission() != null && !prefs(context).contains("finishDistance")) prefs(context).edit().putBoolean("abandonPending", true).apply()
    }

    fun hasPendingFinish(): Boolean = prefs(context).contains("finishDistance") && !prefs(context).getBoolean("finishConfirmed", false)
    fun hasConfirmedFinish(): Boolean = prefs(context).getBoolean("finishConfirmed", false)

    private fun stopConfirmedRun() {
        val p = prefs(context)
        val run = RunTrackingService.runState.value
        if (p.getBoolean("finishConfirmed", false) && run.isRunning && run.workoutId == p.getLong("workout", -1L)) {
            context.startService(Intent(context, RunTrackingService::class.java).apply {
                action = RunTrackingService.ACTION_STOP
            })
        }
    }

    fun restoreConfirmedFinish() {
        val p = prefs(context)
        if (belongsTo(FirebaseAuth.getInstance().currentUser?.uid) && p.getBoolean("finishConfirmed", false)) {
            _finishedMission.value = p.getString("mission", null)
            if (p.getBoolean("scoreConfirmed", false)) _scoredMission.value = p.getString("mission", null)
            stopConfirmedRun()
            scoreConfirmedFinish()
        }
    }

    fun retryPendingFinish() {
        val p = prefs(context)
        val uid = p.getString("uid", null) ?: return
        val mission = p.getString("mission", null) ?: return
        if (!hasPendingFinish() || uid != FirebaseAuth.getInstance().currentUser?.uid || inFlight) return
        val points = p.getString("finishPoints", null) ?: return
        val json = org.json.JSONArray(points)
        val evidence = (0 until json.length()).map { index ->
            val point = json.getJSONObject(index)
            mapOf("latitude" to point.getDouble("latitude"), "longitude" to point.getDouble("longitude"),
                "accuracy" to point.getDouble("accuracy"), "capturedAtMs" to point.getLong("capturedAtMs"))
        }
        if (evidence.isEmpty()) {
            _error.value = "Chegada pendente: nenhum ponto GPS valido para confirmar a prova."
            return
        }
        val db = FirebaseFirestore.getInstance()
        val missionRef = db.collection("missions").document(mission)
        val player = missionRef.collection("players").document(uid)
        val gps = missionRef.collection("gpsPrivate").document(uid)
        val elapsed = p.getLong("finishTime", 0L)
        val values = mapOf<String, Any>("progressKm" to p.getFloat("finishDistance", 0f).toDouble().coerceAtMost(p.getInt("target", 0).toDouble()) / 1000.0,
            "timeSec" to elapsed, "lastUpdatedAt" to FieldValue.serverTimestamp(),
            "finishedAt" to FieldValue.serverTimestamp(),
            "paceSecPerKm" to elapsed.toDouble() * 1000.0 / p.getInt("target", 1).coerceAtLeast(1))
        val attemptGeneration = generation
        lastEnqueuedAt = SystemClock.elapsedRealtime()
        inFlight = true
        _error.value = "Chegada pendente; aguardando confirmacao do Firebase."
        Log.i("CompetitionSync", "finish mission=$mission points=${evidence.size} distance=${values["progressKm"]} time=$elapsed")
        db.runTransaction { tx ->
            val remoteMission = tx.get(missionRef)
            val remote = tx.get(player)
            check(!remote.contains("withdrawnAt")) { "Voce desistiu desta prova." }
            check(remote.getString("sessionId") == deviceSessionId()) { "Sessao de outro aparelho." }
            if (remote.getTimestamp("finishedAt") == null) {
                tx.update(player, values)
                tx.set(gps, mapOf("points" to evidence, "updatedAt" to FieldValue.serverTimestamp()))
                if (remoteMission.getString("winnerUid") == null) tx.update(missionRef, mapOf(
                    "winnerUid" to uid, "winnerFinishedAt" to FieldValue.serverTimestamp(), "winnerTimeSec" to elapsed))
            }
            true
        }.addOnSuccessListener {
            if (generation != attemptGeneration || FirebaseAuth.getInstance().currentUser?.uid != uid) return@addOnSuccessListener
            inFlight = false
            p.edit().putBoolean("finishConfirmed", true).apply()
            _error.value = null
            _finishedMission.value = mission
            stopConfirmedRun()
            scoreConfirmedFinish()
            notifyFinish()
        }.addOnFailureListener { failure ->
            if (generation != attemptGeneration || FirebaseAuth.getInstance().currentUser?.uid != uid) return@addOnFailureListener
            inFlight = false
            Log.e("CompetitionSync", "finish failed mission=$mission points=${evidence.size} distance=${values["progressKm"]} time=$elapsed", failure)
            _error.value = "Chegada pendente: ${failure.localizedMessage}. Tentaremos novamente."
        }
    }

    fun scoreConfirmedFinish() {
        val p = prefs(context)
        val uid = p.getString("uid", null) ?: return
        val mission = p.getString("mission", null) ?: return
        if (!p.getBoolean("finishConfirmed", false) || p.getBoolean("scoreConfirmed", false) ||
            uid != FirebaseAuth.getInstance().currentUser?.uid || scoringInFlight) return
        val db = FirebaseFirestore.getInstance()
        val missionRef = db.collection("missions").document(mission)
        val playerRef = missionRef.collection("players").document(uid)
        val userRef = db.collection("users").document(uid)
        val historyRef = userRef.collection("competitionHistory").document(mission)
        scoringInFlight = true
        db.runTransaction { tx ->
            val history = tx.get(historyRef)
            if (!history.exists()) {
                val missionDoc = tx.get(missionRef)
                val player = tx.get(playerRef)
                val user = tx.get(userRef)
                val season = tx.get(db.collection("config").document("season"))
                val finishedAt = requireNotNull(player.getTimestamp("finishedAt")) { "Chegada ainda nao confirmada." }
                val distanceKm = requireNotNull(missionDoc.getDouble("distanceKm"))
                val seasonId = requireNotNull(season.getString("id"))
                check(user.getString("seasonId") == seasonId) { "Temporada da chegada nao corresponde ao perfil." }
                val startsAt = requireNotNull(missionDoc.getTimestamp("startsAt"))
                check(startsAt >= requireNotNull(season.getTimestamp("startsAt")) &&
                    startsAt < requireNotNull(season.getTimestamp("endsAt"))) { "Chegada fora da temporada atual." }
                val distanceField = when (distanceKm) {
                    2.5 -> "elo25"
                    5.0 -> "elo5"
                    10.0 -> "elo10"
                    else -> error("Distancia da missao invalida.")
                }
                val points = if (missionDoc.getString("winnerUid") == uid) 30L else 20L
                tx.update(userRef, mapOf(
                    "elo" to (user.getLong("elo") ?: 0L) + points,
                    distanceField to (user.getLong(distanceField) ?: 0L) + points,
                    "lastScoredMissionId" to mission
                ))
                tx.set(historyRef, mapOf("points" to points, "distanceKm" to distanceKm,
                    "finishedAt" to finishedAt, "seasonId" to seasonId))
            }
            true
        }.addOnSuccessListener {
            scoringInFlight = false
            if (uid == FirebaseAuth.getInstance().currentUser?.uid && p.getString("mission", null) == mission) {
                p.edit().putBoolean("scoreConfirmed", true).apply()
                _scoredMission.value = mission
                _error.value = null
            }
        }.addOnFailureListener { failure ->
            scoringInFlight = false
            Log.e("CompetitionSync", "score failed mission=$mission", failure)
            _error.value = "Pontuacao pendente: ${failure.localizedMessage}. Tentaremos novamente."
        }
    }

    fun raceState(raw: RunState): RunState {
        val p = prefs(context)
        if (!p.contains("mission") || raw.workoutId != p.getLong("workout", -1L)) return raw.copy(totalDistanceMeters = 0.0, elapsedSeconds = 0)
        val distance = (raw.totalDistanceMeters - p.getFloat("baseDistance", 0f)).coerceAtLeast(0.0)
        val elapsed = ((ServerTimeSync.nowSync() - p.getLong("startsAt", Long.MAX_VALUE)) / 1000L).coerceAtLeast(0)
        val pace = if (distance >= 1.0) elapsed * 1000.0 / distance else null
        return raw.copy(totalDistanceMeters = distance, elapsedSeconds = elapsed, averagePaceSeconds = pace,
            averagePaceFormatted = com.elite.sch.app.tracker.PaceCalculator.formatPace(pace))
    }

    fun tick(context: Context, raw: RunState, force: Boolean = false) {
        val run = raceState(raw)
        val p = prefs(context)
        val mission = p.getString("mission", null) ?: return
        val uid = p.getString("uid", null) ?: return
        if (p.contains("finishDistance")) {
            if (SystemClock.elapsedRealtime() - lastEnqueuedAt >= 10_000L) retryPendingFinish()
            return
        }
        if (uid != FirebaseAuth.getInstance().currentUser?.uid ||
            run.workoutId != p.getLong("workout", -1L) || !run.isRunning || run.isPaused ||
            p.getBoolean("finishConfirmed", false)) return
        if (com.elite.sch.app.data.competition.ServerTimeSync.nowSync() < p.getLong("startsAt", Long.MAX_VALUE)) return
        val target = p.getInt("target", 0).toDouble()
        val distance = run.totalDistanceMeters
        if (target <= 0 || !distance.isFinite() || distance < 0 || run.elapsedSeconds < 0) return
        val finished = distance >= target
        if (inFlight && !finished) return
        val elapsed = run.elapsedSeconds
        val now = SystemClock.elapsedRealtime()
        if (!force && !finished && now - lastEnqueuedAt < 10_000L) return
        lastEnqueuedAt = now
        val db = FirebaseFirestore.getInstance()
        val player = db.collection("missions").document(mission).collection("players").document(uid)
        val values = hashMapOf<String, Any>(
            "sessionId" to deviceSessionId(),
            "progressKm" to distance.coerceAtMost(target) / 1000.0,
            "timeSec" to elapsed,
            "lastUpdatedAt" to FieldValue.serverTimestamp(),
            "clientElapsedRealtimeMs" to now
        )
        run.averagePaceSeconds?.takeIf { it.isFinite() && it >= 0 }?.let { values["paceSecPerKm"] = it }
        run.gpsAccuracy?.takeIf { it.isFinite() && it >= 0 }?.let { values["gpsAccuracyMeters"] = it.toDouble() }
        // Separate document: other participants must never read precise coordinates.
        val points = run.routePoints.filter {
            it.timestampMillis >= p.getLong("captureStartedAt", 0L) &&
                it.latitude.isFinite() && it.longitude.isFinite() && it.accuracy > 0 && it.accuracy <= 20
        }.takeLast(5).map {
            mapOf("latitude" to it.latitude, "longitude" to it.longitude,
                "accuracy" to it.accuracy.toDouble(), "capturedAtMs" to it.timestampMillis)
        }
        if (finished) {
            if (points.isEmpty()) {
                _error.value = "Chegada sem ponto GPS valido; aguardando nova leitura."
                return
            }
            val json = org.json.JSONArray()
            points.forEach { point -> json.put(org.json.JSONObject(point)) }
            if (!p.edit().putFloat("finishDistance", distance.toFloat()).putLong("finishTime", elapsed)
                    .putString("finishPoints", json.toString()).commit()) {
                _error.value = "Nao foi possivel salvar a chegada no aparelho."
                return
            }
            retryPendingFinish()
            return
        }
        val attemptGeneration = generation
        inFlight = true
        Log.i("CompetitionSync", "progress mission=$mission distance=${values["progressKm"]} time=$elapsed")
        player.update(values).addOnSuccessListener {
            if (generation != attemptGeneration || FirebaseAuth.getInstance().currentUser?.uid != uid) return@addOnSuccessListener
            inFlight = false
            _error.value = null
            if (points.isNotEmpty() && now - lastGpsSentAt >= 60_000L) {
                lastGpsSentAt = now
                player.parent.parent!!.collection("gpsPrivate").document(uid)
                    .set(mapOf("points" to points, "updatedAt" to FieldValue.serverTimestamp()))
                    .addOnFailureListener { failure ->
                        lastGpsSentAt = 0L
                        Log.e("CompetitionSync", "gpsPrivate failed mission=$mission points=${points.size}", failure)
                    }
            }
        }.addOnFailureListener { failure ->
            if (generation != attemptGeneration || FirebaseAuth.getInstance().currentUser?.uid != uid) return@addOnFailureListener
            inFlight = false
            Log.e("CompetitionSync", "players failed mission=$mission distance=${values["progressKm"]} time=$elapsed", failure)
            _error.value = "Falha no envio do progresso: ${failure.localizedMessage}. A tentativa sera repetida."
        }

    }

    fun finish(run: RunState) = tick(context, run, force = true)
}
