package com.elite.sch.app.data.competition

import android.util.Log

import com.elite.sch.app.ui.competition.CompetitionDistance
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.delay
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.Year
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Fuso horário oficial da temporada (config/season no Firestore). */
const val SEASON_TIMEZONE = "America/Sao_Paulo"
val SEASON_ZONE_ID = ZoneId.of(SEASON_TIMEZONE)

/** One competition room belongs to one Brazilian UF.  The absolute instant is
 * stored in Firestore; this mapping is only used to build and display that
 * state's local schedule. */
private val STATE_TIME_ZONES = mapOf(
    "AC" to "America/Rio_Branco", "AM" to "America/Manaus", "AP" to "America/Belem",
    "BA" to "America/Bahia", "CE" to "America/Fortaleza", "DF" to "America/Sao_Paulo",
    "ES" to "America/Sao_Paulo", "GO" to "America/Sao_Paulo", "MA" to "America/Fortaleza",
    "MG" to "America/Sao_Paulo", "MS" to "America/Campo_Grande", "MT" to "America/Cuiaba",
    "PA" to "America/Belem", "PB" to "America/Fortaleza", "PE" to "America/Recife",
    "PI" to "America/Fortaleza", "PR" to "America/Sao_Paulo", "RJ" to "America/Sao_Paulo",
    "RN" to "America/Fortaleza", "RO" to "America/Porto_Velho", "RR" to "America/Boa_Vista",
    "RS" to "America/Sao_Paulo", "SC" to "America/Sao_Paulo", "SE" to "America/Maceio",
    "SP" to "America/Sao_Paulo", "TO" to "America/Araguaina"
)

fun competitionZoneForState(state: String): ZoneId =
    ZoneId.of(STATE_TIME_ZONES[state.trim().uppercase()] ?: SEASON_TIMEZONE)

fun competitionTimeZoneIdForState(state: String): String =
    STATE_TIME_ZONES[state.trim().uppercase()] ?: SEASON_TIMEZONE

data class OnlineMission(
    val id: String,
    val distance: CompetitionDistance,
    val startsAt: Timestamp,
    val participants: Int,
    val state: String,
    val timeZone: String,
    val maxPlayers: Int = Int.MAX_VALUE,
    val results: List<OnlineMissionResult> = emptyList(),
    val fromCache: Boolean = false
)

data class OnlineMissionResult(
    val uid: String,
    val name: String,
    val timeSec: Long,
    val finishedAt: Timestamp?,
    val readyAt: Timestamp? = null,
    val startedAt: Timestamp? = null,
    val progressKm: Double = 0.0,
    val paceSecPerKm: Double? = null,
    val updatedAt: Timestamp? = null,
    val withdrawnAt: Timestamp? = null,
    val pending: Boolean = false
)

data class OnlineRankedUser(
    val uid: String,
    val name: String,
    val state: String,
    val elo: Int,
    val seasonId: String? = null,
    val lastScoredMissionId: String? = null
)

class OnlineCompetitionRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val missions = firestore.collection("missions")
    private val users = firestore.collection("users")

    fun observeCurrentUser(): Flow<OnlineRankedUser?> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val registration = users.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("OnlineCompetition", "Falha ao observar perfil do ranking", error)
                close(error)
            } else {
                val name = snapshot?.getString("name")?.trim().orEmpty()
                trySend(if (snapshot?.exists() == true && name.isNotBlank()) {
                    OnlineRankedUser(uid, name, snapshot.getString("state").orEmpty(), (snapshot.getLong("elo") ?: 0L).toInt(), snapshot.getString("seasonId"), snapshot.getString("lastScoredMissionId"))
                } else null)
            }
        }
        awaitClose { registration.remove() }
    }

    suspend fun currentUserState(): String? {
        val uid = auth.currentUser?.uid ?: return null
        return users.document(uid).get(com.google.firebase.firestore.Source.SERVER).awaitResult().getString("state")?.trim()?.takeIf { it.isNotBlank() }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun observeMissions(distance: CompetitionDistance, selectedId: String? = null): Flow<List<OnlineMission>> = flow {
        val state = requireNotNull(currentUserState()) { "Configure sua UF no perfil antes de ver as salas." }.uppercase()
        val zone = competitionZoneForState(state)
        emitAll(flow {
            while (true) {
                emit(Instant.ofEpochMilli(ServerTimeSync.nowSync()).atZone(zone).toLocalDate())
                delay(30_000L)
            }
        }.distinctUntilChanged().flatMapLatest { observeDay(distance, selectedId, state, zone) })
    }

    private fun observeDay(distance: CompetitionDistance, selectedId: String?, state: String, zone: ZoneId): Flow<List<OnlineMission>> = callbackFlow {
        val user = auth.currentUser
            val now = Instant.ofEpochMilli(ServerTimeSync.nowSync()).atZone(zone)
        if (user == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
            val start = Timestamp(now.toLocalDate().atStartOfDay(zone).toInstant().epochSecond, 0)
            val end = Timestamp(now.toLocalDate().plusDays(1).atStartOfDay(zone).toInstant().minusNanos(1).epochSecond, 999_999_999)
        val playerRegistrations = mutableMapOf<String, ListenerRegistration>()
        val playerResults = mutableMapOf<String, List<OnlineMissionResult>>()
        var latestDocuments = emptyList<com.google.firebase.firestore.DocumentSnapshot>()

        fun parsePlayers(snapshot: com.google.firebase.firestore.QuerySnapshot): List<OnlineMissionResult> =
            snapshot.documents.mapNotNull { player ->
                val name = player.getString("name")?.trim().orEmpty()
                if (name.isBlank()) null else OnlineMissionResult(
                    uid = player.id,
                    name = name,
                    timeSec = player.getLong("timeSec") ?: 0L,
                    finishedAt = player.getTimestamp("finishedAt"),
                    readyAt = player.getTimestamp("readyAt"),
                    startedAt = player.getTimestamp("startedAt"),
                    progressKm = player.getDouble("progressKm") ?: 0.0,
                    paceSecPerKm = player.getDouble("paceSecPerKm"),
                    updatedAt = player.getTimestamp("lastUpdatedAt"),
                    withdrawnAt = player.getTimestamp("withdrawnAt"),
                    pending = player.metadata.hasPendingWrites()
                )
            }.sortedWith(compareBy<OnlineMissionResult> { it.finishedAt == null }
                .thenBy { it.finishedAt ?: Timestamp(253402300799L, 999999999) }.thenBy { it.uid })

        fun emitMissions() {
            val serverMissions = latestDocuments.mapNotNull { doc ->
                val startsAt = doc.getTimestamp("startsAt") ?: return@mapNotNull null
                OnlineMission(
                    id = doc.id,
                    distance = distance,
                    startsAt = startsAt,
                    participants = (doc.getLong("playerCount") ?: 0L).toInt(),
                    state = state,
                    timeZone = doc.getString("timeZone") ?: competitionTimeZoneIdForState(state),
                    maxPlayers = (doc.getLong("maxPlayers") ?: Int.MAX_VALUE.toLong()).toInt(),
                    results = playerResults[doc.id].orEmpty(),
                    fromCache = doc.metadata.isFromCache || doc.metadata.hasPendingWrites()
                )
            }
            // The instant, not a local clock label or document ID, identifies a start time.
            // Existing documents can use legacy IDs from another time zone.
            val existingStarts = serverMissions.mapTo(mutableSetOf()) { it.startsAt.toDate().toInstant() }
                        val today = Instant.ofEpochMilli(ServerTimeSync.nowSync()).atZone(zone).withMinute(0).withSecond(0).withNano(0)
            val scheduled = (2..21).mapNotNull { hour ->
                val starts = today.withHour(hour)
                val id = scheduledMissionId(distance, starts.toInstant(), state)
                if (starts.toInstant() in existingStarts) null else OnlineMission(
                    id = id,
                    distance = distance,
                    startsAt = Timestamp(starts.toInstant().epochSecond, starts.toInstant().nano),
                    participants = 0,
                    state = state,
                    timeZone = competitionTimeZoneIdForState(state),
                    maxPlayers = Int.MAX_VALUE
                )
            }
            trySend((serverMissions + scheduled).sortedBy { it.startsAt }
                            .filter { it.startsAt.toDate().toInstant().atZone(zone).toLocalDate() == Instant.ofEpochMilli(ServerTimeSync.nowSync()).atZone(zone).toLocalDate() })
        }

        val registration = missions
            .whereEqualTo("state", state)
            .whereEqualTo("distanceKm", distance.meters / 1000.0)
            .whereGreaterThanOrEqualTo("startsAt", start)
            .whereLessThanOrEqualTo("startsAt", end)
            .orderBy("startsAt", Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                if (error != null) {
                    Log.e("OnlineCompetition", "Falha ao observar missões online", error)
                    close(error)
                    return@addSnapshotListener
                }
                latestDocuments = snapshot?.documents.orEmpty()
                val activeIds = latestDocuments.filter { it.id == selectedId || (it.getLong("playerCount") ?: 0L) > 0L }.map { it.id }.toSet()
                playerRegistrations.keys.toList().filterNot { it in activeIds }.forEach { id ->
                    playerRegistrations.remove(id)?.remove()
                    playerResults.remove(id)
                }
                latestDocuments.forEach { doc ->
                    if (doc.id in activeIds && doc.id !in playerRegistrations) {
                        playerRegistrations[doc.id] = doc.reference.collection("players")
                            .addSnapshotListener { playersSnapshot, playersError ->
                                if (playersError != null) {
                                    Log.e("OnlineCompetition", "Falha ao observar participantes", playersError)
                                    close(playersError)
                                } else if (playersSnapshot != null) {
                                    playerResults[doc.id] = parsePlayers(playersSnapshot)
                                    emitMissions()
                                }
                            }
                    }
                }
                emitMissions()
            }
        awaitClose {
            registration.remove()
            playerRegistrations.values.forEach { it.remove() }
        }
    }

    fun observeRanking(state: String? = null): Flow<List<OnlineRankedUser>> = callbackFlow {
        if (auth.currentUser == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        var rankingRegistration: ListenerRegistration? = null
        fun attachRanking(seasonId: String) {
            rankingRegistration?.remove()
            var query: Query = users.whereEqualTo("seasonId", seasonId)
                .orderBy("elo", Query.Direction.DESCENDING).limit(30)
            if (!state.isNullOrBlank()) query = users.whereEqualTo("seasonId", seasonId)
                .whereEqualTo("state", state).orderBy("elo", Query.Direction.DESCENDING).limit(30)
            rankingRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("OnlineCompetition", "Falha ao observar ranking online", error)
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.documents.orEmpty().mapNotNull { doc ->
                    val name = doc.getString("name")?.trim().orEmpty()
                    val elo = (doc.getLong("elo") ?: 0L).toInt()
                    if (name.isBlank() || elo <= 0) return@mapNotNull null
                    OnlineRankedUser(doc.id, name, doc.getString("state").orEmpty(), elo)
                })
            }
        }
        val seasonRegistration = firestore.collection("config").document("season")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("OnlineCompetition", "Falha ao observar temporada", error)
                    close(error)
                } else {
                    val seasonId = snapshot?.getString("id")
                        ?: snapshot?.getLong("year")?.toString()
                        ?: Year.now().value.toString()
                    attachRanking(seasonId)
                }
            }
        awaitClose {
            seasonRegistration.remove()
            rankingRegistration?.remove()
        }
    }

    suspend fun joinMission(virtualMission: OnlineMission): OnlineMission {
        val user = auth.currentUser ?: error("Faça login para entrar na competição online.")
        
            // Verifica se o usuário tem UF configurado (A14)
            val userDoc = firestore.collection("users").document(user.uid).get().awaitResult()
            val userUf = userDoc.getString("state")?.trim()?.uppercase()
            if (userUf.isNullOrBlank()) {
                error("Configure seu estado (UF) no perfil antes de entrar na competição.")
            }
        
        Log.d("OnlineCompetition", "Entrando na missão ${virtualMission.id} como ${user.uid}")
        val ref = missions.document(virtualMission.id)
        val playerRef = ref.collection("players").document(user.uid)
        
            // Obtém horário do servidor ANTES da transação
            val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.now()
        
        return firestore.runTransaction { transaction ->
            val mission = transaction.get(ref)
            val player = transaction.get(playerRef)
            if (!mission.exists()) {
                    // Nova missão: usa horário do servidor para validar
                    val startsAtMs = virtualMission.startsAt.toDate().time
                    if (startsAtMs <= serverNow) error("Esta miss\u00e3o j\u00e1 come\u00e7ou.")
                transaction.set(ref, mapOf(
                    "distanceKm" to virtualMission.distance.meters / 1000.0,
                    "startsAt" to virtualMission.startsAt,
                    "minPlayers" to 2,
                    "playerCount" to 1,
                    "state" to userUf,
                    "timeZone" to competitionTimeZoneIdForState(userUf)
                ))
                transaction.set(playerRef, newPlayerData(user, userDoc))
                return@runTransaction OnlineMission(ref.id, virtualMission.distance, virtualMission.startsAt, 1, userUf, competitionTimeZoneIdForState(userUf))
            }
            check(mission.getString("state") == userUf) { "Esta sala pertence a outra UF." }
            val currentCount = (mission.getLong("playerCount") ?: 0L).toInt()
                // Permite reentrada se o jogador já está na sala (mesmo que a prova tenha começado)
                if (player.exists()) {
                    if (player.contains("withdrawnAt")) error("Você desistiu desta prova.")
                    // Jogador já existe - apenas retorna a missão atualizada
                    val startsAt = mission.getTimestamp("startsAt") ?: error("A miss\u00e3o est\u00e1 sem hor\u00e1rio v\u00e1lido.")
                    val maxPlayers = (mission.getLong("maxPlayers") ?: Int.MAX_VALUE.toLong()).toInt()
                    return@runTransaction OnlineMission(
                        id = ref.id,
                        distance = virtualMission.distance,
                        startsAt = startsAt,
                        participants = currentCount,
                        state = userUf,
                        timeZone = mission.getString("timeZone") ?: competitionTimeZoneIdForState(userUf),
                        maxPlayers = maxPlayers
                    )
                }
                // Novo jogador: valida horário usando servidor
            val maxPlayers = (mission.getLong("maxPlayers") ?: Int.MAX_VALUE.toLong()).toInt()
            val startsAt = mission.getTimestamp("startsAt") ?: error("A miss\u00e3o est\u00e1 sem hor\u00e1rio v\u00e1lido.")
                if (startsAt.toDate().time <= serverNow) error("Esta missão já começou.")
                if (currentCount >= maxPlayers) error("Esta miss\u00e3o atingiu o limite de participantes.")
                transaction.update(ref, "playerCount", currentCount + 1)
                transaction.set(playerRef, newPlayerData(user, userDoc))
            OnlineMission(
                id = ref.id,
                distance = virtualMission.distance,
                startsAt = startsAt,
                    participants = currentCount + 1,
                state = userUf,
                timeZone = mission.getString("timeZone") ?: competitionTimeZoneIdForState(userUf),
                maxPlayers = maxPlayers
            )
        }.awaitResult().also {
            Log.d("OnlineCompetition", "Missão ${it.id} sincronizada: ${it.participants}/${it.maxPlayers}")
        }
    }

    private fun newPlayerData(user: com.google.firebase.auth.FirebaseUser, profile: com.google.firebase.firestore.DocumentSnapshot) = mapOf(
        "name" to (profile.getString("name")?.takeIf { it.isNotBlank() } ?: user.displayName ?: "Atleta Elite"),
        "state" to profile.getString("state").orEmpty().trim().uppercase(),
        "joinedAt" to FieldValue.serverTimestamp(),
        "progressKm" to 0.0,
        "finishedAt" to null,
        "timeSec" to 0,
        "eloDelta" to 0
    )

    suspend fun updatePlayerProgress(
        missionId: String,
        distanceMeters: Double,
        paceSecondsPerKm: Double?,
        elapsedSeconds: Long,
        gpsAccuracyMeters: Float?,
        clientElapsedRealtimeMs: Long
    ) {
        val uid = auth.currentUser?.uid ?: return
        val values = hashMapOf<String, Any>(
            "progressKm" to (distanceMeters / 1000.0).coerceAtLeast(0.0),
            "timeSec" to elapsedSeconds.coerceAtLeast(0L),
            "clientElapsedRealtimeMs" to clientElapsedRealtimeMs,
            "lastUpdatedAt" to FieldValue.serverTimestamp()
        )
        paceSecondsPerKm?.takeIf { it.isFinite() && it >= 0.0 }?.let { values["paceSecPerKm"] = it }
        gpsAccuracyMeters?.takeIf { it.isFinite() && it >= 0f }?.let { values["gpsAccuracyMeters"] = it.toDouble() }
        missions.document(missionId).collection("players").document(uid).update(values).awaitResult()
    }

    suspend fun leaveMission(missionId: String) {
        val uid = auth.currentUser?.uid ?: error("Faça login para sair da sala.")
        val ref = missions.document(missionId)
        val playerRef = ref.collection("players").document(uid)
        
            // Obtém horário do servidor ANTES da transação
            val serverNow = com.elite.sch.app.data.competition.ServerTimeSync.now()
        
        firestore.runTransaction { transaction ->
            val mission = transaction.get(ref)
            val player = transaction.get(playerRef)
            if (!mission.exists() || !player.exists()) return@runTransaction
            val startsAt = mission.getTimestamp("startsAt")
            if (player.contains("withdrawnAt")) return@runTransaction
            if (player.getTimestamp("finishedAt") != null) error("O resultado desta prova já foi registrado.")
            
                val startsAtMs = startsAt?.toDate()?.time ?: Long.MAX_VALUE
                val hasStarted = startsAtMs <= serverNow
            
                if (hasStarted) {
                    // Prova já começou: registra abandono com timestamp do servidor
                    transaction.update(playerRef, "withdrawnAt", FieldValue.serverTimestamp())
                    // Não remove o jogador nem decrementa playerCount - mantém histórico
                } else {
                    // Prova não começou: remove jogador completamente
                    val count = (mission.getLong("playerCount") ?: 0L).toInt()
                    transaction.delete(playerRef)
                    transaction.update(ref, "playerCount", (count - 1).coerceAtLeast(0))
            }
        }.awaitResult()
    }

    suspend fun claimStart(mission: com.elite.sch.app.ui.competition.DemoMission, sessionId: String) {
        val uid = auth.currentUser?.uid ?: error("Entre na conta.")
        val ref = missions.document(mission.id)
        val own = ref.collection("players").document(uid)
        val peerUid = mission.results.firstOrNull {
            it.uid != uid && it.withdrawnAt == null && !it.pending && it.readyAt != null &&
                ServerTimeSync.nowSync() - it.readyAt.toDate().time in 0L..45_000L
        }?.uid
        firestore.runTransaction { tx ->
            val race = tx.get(ref)
            val player = tx.get(own)
            check(player.exists() && !player.contains("withdrawnAt")) { "Inscricao indisponivel." }
            val existing = player.getString("sessionId")
            if (existing != null) {
                check(existing == sessionId) { "Esta prova ja foi iniciada em outro aparelho." }
                return@runTransaction
            }
            check(player.getTimestamp("finishedAt") == null) { "Prova ja concluida." }
            check((race.getLong("playerCount") ?: 0L) >= 2L) { "Faltam participantes." }
            check(peerUid != null) { "Aguarde outro corredor presente." }
            val peer = tx.get(ref.collection("players").document(peerUid))
            check(peer.exists() && !peer.contains("withdrawnAt")) { "O outro corredor saiu." }
            tx.update(own, mapOf("sessionId" to sessionId, "startedAt" to FieldValue.serverTimestamp(), "startPeerUid" to peerUid))
        }.awaitResult()
    }

    suspend fun updateRoomPresence(missionId: String) {
        val uid = auth.currentUser?.uid ?: return
        missions.document(missionId).collection("players").document(uid)
            .update("readyAt", FieldValue.serverTimestamp()).awaitResult()
    }

    suspend fun markPlayerFinished(
        missionId: String,
        distanceMeters: Double,
        paceSecondsPerKm: Double?,
        elapsedSeconds: Long,
        gpsAccuracyMeters: Float?,
        clientElapsedRealtimeMs: Long
    ) {
        val uid = auth.currentUser?.uid ?: return
        val values = hashMapOf<String, Any>(
            "progressKm" to (distanceMeters / 1000.0).coerceAtLeast(0.0),
            "timeSec" to elapsedSeconds.coerceAtLeast(0L),
            "finishedAt" to FieldValue.serverTimestamp(),
            "clientElapsedRealtimeMs" to clientElapsedRealtimeMs,
            "lastUpdatedAt" to FieldValue.serverTimestamp()
        )
        paceSecondsPerKm?.takeIf { it.isFinite() && it >= 0.0 }?.let { values["paceSecPerKm"] = it }
        gpsAccuracyMeters?.takeIf { it.isFinite() && it >= 0f }?.let { values["gpsAccuracyMeters"] = it.toDouble() }
        missions.document(missionId).collection("players").document(uid).update(values).awaitResult()
    }

    companion object {
        private val idFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")

        internal fun scheduledMissionId(distance: CompetitionDistance, startsAt: Instant, state: String): String =
            "${state.lowercase()}_${distance.meters / 1000}_${idFormatter.format(startsAt.atZone(ZoneOffset.UTC))}_utc"

        fun nextMission(distance: CompetitionDistance, requestedTime: String? = null, state: String = ""): OnlineMission {
                    val normalizedState = state.trim().uppercase()
                    val zone = competitionZoneForState(normalizedState)
                    val now = Instant.ofEpochMilli(ServerTimeSync.nowSync()).atZone(zone)
            val requested = requestedTime?.split(":")?.mapNotNull { it.toIntOrNull() }
            val requestedStart = if (requested?.size == 2) {
                now.withHour(requested[0]).withMinute(requested[1]).withSecond(0).withNano(0)
            } else null
            val starts = (requestedStart?.takeIf { it.isAfter(now) }
                ?: now.withMinute(0).withSecond(0).withNano(0).plusHours(1))
            return OnlineMission(
                id = scheduledMissionId(distance, starts.toInstant(), normalizedState),
                distance = distance,
                startsAt = Timestamp(starts.toInstant().epochSecond, starts.toInstant().nano),
                participants = 0,
                state = normalizedState,
                timeZone = competitionTimeZoneIdForState(normalizedState),
                maxPlayers = Int.MAX_VALUE
            )
        }
    }
}

private suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitResult(): T =
    suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { continuation.resume(it) }
        addOnFailureListener { continuation.resumeWithException(it) }
        addOnCanceledListener { continuation.cancel() }
    }
