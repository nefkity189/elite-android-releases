package com.elite.sch.app.tracker

import com.elite.sch.app.data.local.entities.RoutePointEntity

data class PaceDataPoint(
    val timeSeconds: Long,
    val paceSecondsPerKm: Double
)

object PaceSeriesCalculator {
    const val DEFAULT_WINDOW_SECONDS = 30L
    const val MAX_PACE_SECONDS_PER_KM = 1200.0 // 20:00 min/km
    const val MIN_PACE_SECONDS_PER_KM = 180.0 // 3:00 min/km (mais rápido que isso é ruído de GPS)
    const val MAX_GAP_SECONDS = 60L // intervalo maior que isso interrompe a linha do gráfico
    const val MIN_SPEED_METERS_PER_SECOND = 0.5 // Ignora trechos parados (< 0.5 m/s)

    /**
     * Calcula a série temporal de ritmo ao longo do treino com janela deslizante de [windowSeconds].
     * Ignora trechos parados (< 0.5 m/s) ou mais lentos que 20:00 min/km (>= 1200 s/km).
     */
    fun calculate(
        points: List<RoutePointEntity>,
        windowSeconds: Long = DEFAULT_WINDOW_SECONDS
    ): List<PaceDataPoint> {
        if (points.size < 2) return emptyList()

        val sorted = points.sortedBy { it.timestampMillis }
        val startTimestamp = sorted.first().timestampMillis
        val result = mutableListOf<PaceDataPoint>()

        var windowStartIndex = 0

        for (i in 1 until sorted.size) {
            val currentPoint = sorted[i]
            val currentTimeSec = (currentPoint.timestampMillis - startTimestamp) / 1000

            // Ajusta o início da janela para manter duração <= windowSeconds
            while (windowStartIndex < i &&
                (currentPoint.timestampMillis - sorted[windowStartIndex].timestampMillis) / 1000 > windowSeconds
            ) {
                windowStartIndex++
            }

            val startPoint = sorted[windowStartIndex]
            val durationSec = (currentPoint.timestampMillis - startPoint.timestampMillis) / 1000.0

            // Exige ao menos 8 segundos de amostra dentro da janela para cálculo estável
            if (durationSec >= 8.0) {
                var windowDistanceMeters = 0.0
                for (j in windowStartIndex until i) {
                    windowDistanceMeters += GpsFilter.calculateDistanceMeters(
                        sorted[j].latitude,
                        sorted[j].longitude,
                        sorted[j + 1].latitude,
                        sorted[j + 1].longitude
                    )
                }

                if (windowDistanceMeters > 0.0) {
                    val speed = windowDistanceMeters / durationSec
                    if (speed >= MIN_SPEED_METERS_PER_SECOND) {
                        val distanceKm = windowDistanceMeters / 1000.0
                        val pace = durationSec / distanceKm
                        if (pace < MAX_PACE_SECONDS_PER_KM && pace >= MIN_PACE_SECONDS_PER_KM) {
                            result.add(PaceDataPoint(timeSeconds = currentTimeSec, paceSecondsPerKm = pace))
                        }
                    }
                }
            }
        }

        return result
    }

    /** Divide a série em trechos contínuos: um intervalo > [maxGapSeconds] entre dois pontos interrompe a linha. */
    fun splitIntoSegments(
        series: List<PaceDataPoint>,
        maxGapSeconds: Long = MAX_GAP_SECONDS
    ): List<List<PaceDataPoint>> {
        if (series.isEmpty()) return emptyList()
        val segments = mutableListOf<MutableList<PaceDataPoint>>()
        var current = mutableListOf(series.first())
        for (i in 1 until series.size) {
            if (series[i].timeSeconds - series[i - 1].timeSeconds > maxGapSeconds) {
                segments.add(current)
                current = mutableListOf()
            }
            current.add(series[i])
        }
        segments.add(current)
        return segments
    }

    /** Percentil (interpolação linear) de uma lista de valores; p entre 0 e 100. */
    fun percentile(values: List<Double>, p: Double): Double {
        require(values.isNotEmpty())
        val sorted = values.sorted()
        if (sorted.size == 1) return sorted[0]
        val rank = (p / 100.0) * (sorted.size - 1)
        val lo = kotlin.math.floor(rank).toInt()
        val hi = kotlin.math.ceil(rank).toInt()
        return sorted[lo] + (sorted[hi] - sorted[lo]) * (rank - lo)
    }

    /** Eixo vertical do gráfico: do percentil 5 ao 95 dos ritmos (pares min, max). */
    fun axisRange(series: List<PaceDataPoint>): Pair<Double, Double> {
        val paces = series.map { it.paceSecondsPerKm }
        val lo = percentile(paces, 5.0)
        val hi = percentile(paces, 95.0)
        return if (hi - lo < 1.0) (lo - 5.0) to (hi + 5.0) else lo to hi
    }
}
