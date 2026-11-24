package com.elite.sch.app.data.competition

import android.os.SystemClock
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

/** Estimate for presentation only. Firestore request.time authorizes mutations. */
object ServerTimeSync {
    private val mutex = Mutex()
    @Volatile private var sample: ClockSample? = null

    suspend fun sync(): Long = mutex.withLock {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: error("Entre na conta para sincronizar o horario.")
        withTimeout(12_000L) {
            val ref = FirebaseFirestore.getInstance().collection("_timeSync").document(uid)
            val before = SystemClock.elapsedRealtime()
            ref.set(mapOf("serverTime" to FieldValue.serverTimestamp())).await()
            val snapshot = ref.get(Source.SERVER).await()
            val after = SystemClock.elapsedRealtime()
            check(FirebaseAuth.getInstance().currentUser?.uid == uid) { "A conta mudou durante a sincronizacao." }
            val stamp = snapshot.getTimestamp("serverTime") ?: error("Horario indisponivel.")
            check(after - before <= 4_000L) { "Conexao instavel para sincronizar a largada. Tente novamente." }
            sample = ClockSample(stamp.toDate().time, (before + after) / 2, (after - before) / 2)
            nowSync()
        }
    }

    suspend fun now(): Long {
        if (!isFresh) sync()
        return nowSync()
    }

    // Zero means unavailable; business operations must use now() or require isFresh.
    fun nowSync(): Long = sample?.now(SystemClock.elapsedRealtime()) ?: 0L
    val isFresh: Boolean get() = sample?.let {
        SystemClock.elapsedRealtime() - it.monotonicMs in 0L..240_000L
    } ?: false
    fun invalidate() { sample = null }
}

/** Independent of changes to the phone's wall clock. */
internal data class ClockSample(val serverMs: Long, val monotonicMs: Long, val uncertaintyMs: Long) {
    fun now(monotonicNow: Long): Long = serverMs + (monotonicNow - monotonicMs).coerceAtLeast(0L)
}
