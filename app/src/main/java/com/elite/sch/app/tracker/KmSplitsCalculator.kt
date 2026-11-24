package com.elite.sch.app.tracker

import com.elite.sch.app.data.local.entities.RoutePointEntity

data class KmSplit(
    val kmIndex: Int,          // 1 para 1º km, 2 para 2º km, etc.
    val distanceMeters: Double, // Ex: 1000.0 para km completo, ou 450.0 para último km parcial
    val durationSeconds: Long,  // Tempo gasto nesse trecho
    val paceSeconds: Double,    // Ritmo (s/km) nesse trecho
    val paceFormatted: String,  // "05:30"
    val isPartial: Boolean      // true se for o último trecho incompleto (ex: 450m)
)

object KmSplitsCalculator {

    /**
     * Calcula os tempos parciais (splits) a cada quilômetro percorrido com base na lista de pontos de rota do GPS.
     */
    fun calculateSplits(routePoints: List<RoutePointEntity>): List<KmSplit> {
        if (routePoints.size < 2) return emptyList()

        val splits = mutableListOf<KmSplit>()
        var currentKmIndex = 1
        var segmentStartTime = routePoints.first().timestampMillis
        var lastPoint = routePoints.first()
        var currentKmDistance = 0.0

        for (i in 1 until routePoints.size) {
            val pt = routePoints[i]
            val dist = GpsFilter.calculateDistanceMeters(
                lastPoint.latitude, lastPoint.longitude,
                pt.latitude, pt.longitude
            )

            // Só acumula se for um deslocamento válido
            if (dist >= GpsFilter.MIN_DISPLACEMENT_METERS && pt.accuracy <= GpsFilter.MAX_ACCURACY_METERS) {
                currentKmDistance += dist
                lastPoint = pt

                if (currentKmDistance >= 1000.0) {
                    val durationMillis = pt.timestampMillis - segmentStartTime
                    val durationSeconds = (durationMillis / 1000L).coerceAtLeast(1L)
                    val pace = durationSeconds / (currentKmDistance / 1000.0)

                    splits.add(
                        KmSplit(
                            kmIndex = currentKmIndex,
                            distanceMeters = currentKmDistance,
                            durationSeconds = durationSeconds,
                            paceSeconds = pace,
                            paceFormatted = PaceCalculator.formatPace(pace),
                            isPartial = false
                        )
                    )

                    currentKmIndex++
                    currentKmDistance = 0.0
                    segmentStartTime = pt.timestampMillis
                }
            }
        }

        // Se sobrou uma fração final >= 100m
        if (currentKmDistance >= 100.0) {
            val durationMillis = routePoints.last().timestampMillis - segmentStartTime
            val durationSeconds = (durationMillis / 1000L).coerceAtLeast(1L)
            val pace = durationSeconds / (currentKmDistance / 1000.0)

            splits.add(
                KmSplit(
                    kmIndex = currentKmIndex,
                    distanceMeters = currentKmDistance,
                    durationSeconds = durationSeconds,
                    paceSeconds = pace,
                    paceFormatted = PaceCalculator.formatPace(pace),
                    isPartial = true
                )
            )
        }

        return splits
    }
}