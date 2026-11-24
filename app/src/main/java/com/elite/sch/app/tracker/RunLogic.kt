package com.elite.sch.app.tracker

import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestampMillis: Long
)

object GpsFilter {
    const val MAX_ACCURACY_METERS = 20.0f
    const val MAX_SPEED_METERS_PER_SECOND = 12.0
    const val MIN_DISPLACEMENT_METERS = 3.0

    /**
     * Verifica se um ponto GPS é válido com base no ponto anterior.
     * Retorna true para o primeiro ponto (previous == null).
     * Descarta pontos com precisão pior que MAX_ACCURACY_METERS.
     * Descarta saltos de velocidade acima de MAX_SPEED_METERS_PER_SECOND.
     * Descarta deslocamentos menores que MIN_DISPLACEMENT_METERS.
     */
    fun isPointValid(previous: GeoPoint?, current: GeoPoint): Boolean {
        // Primeiro ponto é sempre válido
        if (previous == null) return true

        // Verifica precisão
        if (current.accuracy > MAX_ACCURACY_METERS) return false

        // Calcula distância e tempo
        val distance = calculateDistanceMeters(
            previous.latitude, previous.longitude,
            current.latitude, current.longitude
        )
        val timeDeltaSeconds = (current.timestampMillis - previous.timestampMillis) / 1000.0
        
        // Evita divisão por zero
        if (timeDeltaSeconds <= 0) return false

        // Verifica velocidade máxima
        val speed = distance / timeDeltaSeconds
        if (speed > MAX_SPEED_METERS_PER_SECOND) return false

        // Verifica deslocamento mínimo
        if (distance < MIN_DISPLACEMENT_METERS) return false

        return true
    }

    /**
     * Calcula a distância em metros entre dois pontos geográficos usando fórmula de Haversine pura.
     */
    fun calculateDistanceMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadius = 6371000.0 // metros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}

object PaceCalculator {
    const val MIN_DISTANCE_FOR_PACE_METERS = 300.0
    const val MAX_PACE_SECONDS_PER_KM = 1200.0 // 20:00 min/km (mais rápido significa paceSeconds < 1200.0)

    /**
     * Retorna o ritmo médio em segundos por quilômetro.
     * Retorna null se a distância for inferior a 300 metros ou se o ritmo for >= 20:00 min/km.
     */
    fun calculateAveragePaceSeconds(totalSeconds: Long, totalDistanceMeters: Double): Double? {
        if (totalDistanceMeters < MIN_DISTANCE_FOR_PACE_METERS || totalSeconds <= 0L) {
            return null
        }
        val distanceKm = totalDistanceMeters / 1000.0
        val paceSeconds = totalSeconds / distanceKm
        if (paceSeconds >= MAX_PACE_SECONDS_PER_KM) {
            return null
        }
        return paceSeconds
    }

    /**
     * Formata apenas os números do ritmo no formato "MM:SS" (ex: "34:46" ou "--:--").
     * A unidade fica no rótulo ("Ritmo médio (min/km)").
     */
    fun formatPace(paceSeconds: Double?): String {
        if (paceSeconds == null || paceSeconds.isNaN() || paceSeconds.isInfinite() || paceSeconds <= 0.0 || paceSeconds >= MAX_PACE_SECONDS_PER_KM) {
            return "--:--"
        }
        val minutes = (paceSeconds / 60).toLong()
        val seconds = (paceSeconds % 60).toLong()
        return "%02d:%02d".format(minutes, seconds)
    }
}

sealed interface VoiceCue {
    data class StartStep(val message: String, val isRun: Boolean) : VoiceCue
    data object TenSecondsRemaining : VoiceCue
    data object Completed : VoiceCue
}

data class ScheduleState(
    val currentStepIndex: Int,
    val currentStep: WorkoutStep?,
    val timeRemainingInStep: Long,
    val nextStep: WorkoutStep?,
    val isFinished: Boolean,
    val pendingVoiceCue: VoiceCue?
)

object StepScheduler {

    /**
     * Determina o passo atual, tempo restante e avisos de voz dado o tempo decorrido.
     */
    fun evaluate(steps: List<WorkoutStep>, elapsedSeconds: Long): ScheduleState {
        if (steps.isEmpty()) {
            return ScheduleState(
                currentStepIndex = 0,
                currentStep = null,
                timeRemainingInStep = 0,
                nextStep = null,
                isFinished = false,
                pendingVoiceCue = null
            )
        }

        var accumulated = 0L
        for (i in steps.indices) {
            val step = steps[i]
            val stepDuration = step.durationSeconds.toLong()
            val stepStart = accumulated
            val stepEnd = stepStart + stepDuration

            if (elapsedSeconds < stepEnd) {
                val timeRemaining = stepEnd - elapsedSeconds
                val nextStep = if (i + 1 < steps.size) steps[i + 1] else null

                // Determina avisos de voz
                val voiceCue = when {
                    elapsedSeconds == stepStart -> {
                        val message = when (step.type) {
                            StepType.RUN -> "Comece a correr"
                            StepType.WALK, StepType.WARMUP, StepType.COOLDOWN -> "Caminhe"
                        }
                        VoiceCue.StartStep(message, step.type == StepType.RUN)
                    }
                    timeRemaining <= 10L && stepDuration > 60L -> {
                        VoiceCue.TenSecondsRemaining
                    }
                    else -> null
                }

                return ScheduleState(
                    currentStepIndex = i,
                    currentStep = step,
                    timeRemainingInStep = timeRemaining,
                    nextStep = nextStep,
                    isFinished = false,
                    pendingVoiceCue = voiceCue
                )
            }
            accumulated = stepEnd
        }

        // Treino concluído
        return ScheduleState(
            currentStepIndex = steps.size - 1,
            currentStep = steps.lastOrNull(),
            timeRemainingInStep = 0,
            nextStep = null,
            isFinished = true,
            pendingVoiceCue = if (elapsedSeconds == accumulated) VoiceCue.Completed else null
        )
    }

    /**
     * Retorna a lista de tempos em segundos quando avisos devem soar no treino.
     */
    fun getWarningTimestamps(steps: List<WorkoutStep>): List<Pair<Long, String>> {
        val timestamps = mutableListOf<Pair<Long, String>>()
        var accumulated = 0L
        for (step in steps) {
            val cue = when (step.type) {
                StepType.RUN -> "Comece a correr"
                else -> "Caminhe"
            }
            timestamps.add(accumulated to cue)
            if (step.durationSeconds > 60) {
                val tenSecBefore = accumulated + step.durationSeconds - 10
                timestamps.add(tenSecBefore to "Faltam 10 segundos")
            }
            accumulated += step.durationSeconds
        }
        timestamps.add(accumulated to "Treino concluído")
        return timestamps
    }
}
