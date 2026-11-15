package com.example.generator

/**
 * Estimativa pura de duração em segundos com base na distância e ritmo.
 * Usado quando o GPS não está disponível ou quando o usuário corre por distância.
 */
object StepEstimator {

    fun estimateDurationSeconds(
        distanceMeters: Int,
        type: com.example.model.StepType,
        ritmoCorridaSegKm: Int? = null,
        ritmoCaminhadaSegKm: Int? = null
    ): Int {
        if (distanceMeters <= 0) return 0

        val pace = when (type) {
            com.example.model.StepType.RUN -> {
                ritmoCorridaSegKm?.takeIf { it > 0 } ?: PaceDefaults.corridaSegKm
            }
            com.example.model.StepType.WALK -> {
                ritmoCaminhadaSegKm?.takeIf { it > 0 } ?: PaceDefaults.caminhadaSegKm
            }
            else -> PaceDefaults.corridaSegKm // Warmup/cooldown assumem corrida
        }

        // Duração = distância (m) / 1000 * pace (s/km)
        return (distanceMeters / 1000.0 * pace).toInt()
    }
}

/**
 * Valores de ritmo padrão para estimativas.
 * Valores provisórios; validar com profissional.
 */
object PaceDefaults {
    val corridaSegKm: Int = 450 // 7 minutos 30 segundos por km
    val caminhadaSegKm: Int = 720 // 12 minutos por km
}