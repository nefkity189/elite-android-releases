package com.elite.sch.app.tracker

import kotlin.math.floor

/**
 * Rastreia marcos de quilômetros completos para avisos de voz.
 * Puro, sem dependências Android.
 */
object KmMilestoneTracker {

    /**
     * Dado a distância acumulada atual em metros, retorna a lista de quilômetros
     * inteiros recém-cruzados desde a última verificação.
     *
     * @param lastAnnouncedKm O último km que foi anunciado (inicializar com floor(distanciaSalva / 1000))
     * @param currentDistanceMeters Distância atual em metros
     * @return Pair com (lista de km cruzados, novo lastAnnouncedKm)
     */
    fun checkAndAdvance(
        lastAnnouncedKm: Int,
        currentDistanceMeters: Double
    ): Pair<List<Int>, Int> {
        return checkAndAdvanceInterval(lastAnnouncedKm, currentDistanceMeters, 1000)
    }

    /**
     * Versão genérica que rastreia marcos baseados em qualquer intervalo em metros.
     *
     * @param lastAnnouncedIndex O último índice de marco anunciado (0 se nenhum anunciado ainda).
     *                          Ex: para intervalMeters=500, 500m é índice 1, 1000m é índice 2.
     * @param currentDistanceMeters Distância acumulada atual em metros.
     * @param intervalMeters Intervalo dos avisos em metros (default: 1000).
     * @return Pair com (lista de índices de marcos cruzados, novo lastAnnouncedIndex)
     */
    fun checkAndAdvanceInterval(
        lastAnnouncedIndex: Int,
        currentDistanceMeters: Double,
        intervalMeters: Int = 1000
    ): Pair<List<Int>, Int> {
        // Ignora distâncias negativas ou inválidas
        if (currentDistanceMeters <= 0 || intervalMeters <= 0) {
            return emptyList<Int>() to lastAnnouncedIndex
        }

        val currentIndex = floor(currentDistanceMeters / intervalMeters.toDouble()).toInt()

        // Se a distância diminuiu (ex: GPS saltou para trás), ignora
        if (currentIndex < lastAnnouncedIndex) {
            return emptyList<Int>() to lastAnnouncedIndex
        }

        // Se não houve progresso de marco inteiro
        if (currentIndex == lastAnnouncedIndex) {
            return emptyList<Int>() to lastAnnouncedIndex
        }

        // Retorna todos os marcos cruzados
        val crossed = (lastAnnouncedIndex + 1..currentIndex).toList()

        return crossed to currentIndex
    }
}

/**
 * Resultado do processamento de marcos de km.
 */
data class KmMilestoneResult(
    val crossedKms: List<Int>,
    val lastAnnouncedKm: Int
)