package com.example.tracker

import kotlin.math.max

/** Uma localização bruta do FusedLocationProvider, já convertida para tipos simples. */
data class RawFix(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    /** Location.time (relógio de parede) — gravado na rota. */
    val timeMillis: Long,
    /** Location.elapsedRealtimeNanos / 1_000_000 — relógio monotônico, usado nos filtros. */
    val elapsedRealtimeMillis: Long
)

enum class RejectReason { ACCURACY, BEFORE_START, WARMUP, SPEED, DISPLACEMENT }

/** Contadores de diagnóstico por corrida (gravados em workout_log). */
data class GpsDiagnostics(
    val received: Int = 0,
    val accepted: Int = 0,
    val rejectedAccuracy: Int = 0,
    val rejectedBeforeStart: Int = 0,
    val rejectedSpeed: Int = 0,
    val rejectedDisplacement: Int = 0,
    val rejectedWarmup: Int = 0,
    val maxGapMillis: Long = 0L
) {
    val rejectedTotal: Int
        get() = rejectedAccuracy + rejectedBeforeStart + rejectedSpeed + rejectedDisplacement + rejectedWarmup
}

data class FixResult(
    val accepted: Boolean,
    val reason: RejectReason?,
    val distanceAddedMeters: Double,
    val point: GeoPoint?
)

/**
 * Filtro de qualidade do GPS para uma corrida. Lógica pura (sem Android).
 *
 * Regras:
 *  - descarta localizações ANTERIORES ao início da corrida (elapsedRealtime da própria localização);
 *    um lote entregue com atraso é aceito por inteiro, desde que os pontos sejam posteriores ao início;
 *  - descarta os primeiros [WARMUP_MILLIS] depois do início (medido pelo instante da localização);
 *  - precisão pior que [MAX_ACCURACY_METERS] (20 m) é rejeitada;
 *  - deslocamento mínimo = max(3 m, metade da precisão do ponto atual);
 *  - velocidade acima de [MAX_SPEED_METERS_PER_SECOND] (7 m/s) é rejeitada.
 */
class GpsSession(private val startElapsedRealtimeMillis: Long) {

    companion object {
        const val MAX_ACCURACY_METERS = 20.0f
        const val WARMUP_MILLIS = 10_000L
        const val MAX_SPEED_METERS_PER_SECOND = 7.0
        const val MIN_DISPLACEMENT_METERS = 3.0
    }

    private var lastAccepted: RawFix? = null
    private var gapBaselineElapsed: Long = startElapsedRealtimeMillis
    private var justResumed: Boolean = false

    var totalDistanceMeters: Double = 0.0
        private set

    private var received = 0
    private var accepted = 0
    private var rejAccuracy = 0
    private var rejBefore = 0
    private var rejSpeed = 0
    private var rejDisp = 0
    private var rejWarmup = 0
    private var maxGap = 0L

    /** Ao retomar de uma pausa, o intervalo da pausa não conta como "intervalo sem ponto". */
    fun onResume(nowElapsedRealtimeMillis: Long) {
        gapBaselineElapsed = nowElapsedRealtimeMillis
        justResumed = true
    }

    /** Processa um lote em ordem cronológica. */
    fun processBatch(fixes: List<RawFix>): List<FixResult> =
        fixes.sortedBy { it.elapsedRealtimeMillis }.map { process(it) }

    fun process(fix: RawFix): FixResult {
        received++

        if (fix.elapsedRealtimeMillis < startElapsedRealtimeMillis) {
            rejBefore++
            return reject(RejectReason.BEFORE_START)
        }
        if (fix.elapsedRealtimeMillis - startElapsedRealtimeMillis < WARMUP_MILLIS) {
            rejWarmup++
            return reject(RejectReason.WARMUP)
        }
        if (fix.accuracy > MAX_ACCURACY_METERS) {
            rejAccuracy++
            return reject(RejectReason.ACCURACY)
        }

        val prev = lastAccepted
        var added = 0.0

        if (prev != null && !justResumed) {
            val dtSeconds = (fix.elapsedRealtimeMillis - prev.elapsedRealtimeMillis) / 1000.0
            val distance = GpsFilter.calculateDistanceMeters(
                prev.latitude, prev.longitude, fix.latitude, fix.longitude
            )
            val minDisplacement = max(MIN_DISPLACEMENT_METERS, fix.accuracy / 2.0)
            // dt <= 0 (ponto repetido ou fora de ordem) é tratado como deslocamento inválido
            if (dtSeconds <= 0.0 || distance < minDisplacement) {
                rejDisp++
                return reject(RejectReason.DISPLACEMENT)
            }
            if (distance / dtSeconds > MAX_SPEED_METERS_PER_SECOND) {
                rejSpeed++
                return reject(RejectReason.SPEED)
            }
            added = distance
        }

        accepted++
        totalDistanceMeters += added
        maxGap = max(maxGap, fix.elapsedRealtimeMillis - gapBaselineElapsed)
        lastAccepted = fix
        gapBaselineElapsed = fix.elapsedRealtimeMillis
        justResumed = false
        return FixResult(
            accepted = true,
            reason = null,
            distanceAddedMeters = added,
            point = GeoPoint(fix.latitude, fix.longitude, fix.accuracy, fix.timeMillis)
        )
    }

    /** Fecha o diagnóstico; [nowElapsedRealtimeMillis] conta o intervalo final sem ponto. */
    fun diagnostics(nowElapsedRealtimeMillis: Long? = null): GpsDiagnostics {
        var gap = maxGap
        if (nowElapsedRealtimeMillis != null) {
            gap = max(gap, nowElapsedRealtimeMillis - gapBaselineElapsed)
        }
        return GpsDiagnostics(
            received = received,
            accepted = accepted,
            rejectedAccuracy = rejAccuracy,
            rejectedBeforeStart = rejBefore,
            rejectedSpeed = rejSpeed,
            rejectedDisplacement = rejDisp,
            rejectedWarmup = rejWarmup,
            maxGapMillis = gap
        )
    }

    private fun reject(reason: RejectReason) = FixResult(false, reason, 0.0, null)
}
