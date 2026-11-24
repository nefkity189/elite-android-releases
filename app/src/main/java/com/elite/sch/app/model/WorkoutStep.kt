package com.elite.sch.app.model

enum class StepType {
    WARMUP,
    RUN,
    WALK,
    COOLDOWN
}

data class WorkoutStep(
    val type: StepType,
    val durationSeconds: Int,
    val instruction: String,
    val distanceMeters: Int? = null
) {
    /**
     * Retorna true se este passo é controlado por distância (não por tempo).
     * Quando por distância, durationSeconds guarda a duração ESTIMADA para resumos.
     */
    val isDistanceBased: Boolean
        get() = distanceMeters != null && distanceMeters > 0

    val durationFormatted: String
        get() {
            val minutes = durationSeconds / 60
            val seconds = durationSeconds % 60
            return when {
                minutes > 0 && seconds > 0 -> "$minutes min $seconds s"
                minutes > 0 -> "$minutes min"
                else -> "$seconds s"
            }
        }
}