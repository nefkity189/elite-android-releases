package com.example.tracker

import kotlin.math.*

data class RoutePointCoord(
    val latitude: Double,
    val longitude: Double
)

data class NormalizedPoint(
    val x: Float,
    val y: Float
)

object RouteNormalizer {

    // Simplificação de trajeto usando Ramer-Douglas-Peucker
    fun simplify(points: List<RoutePointCoord>, epsilon: Double = 0.00005): List<RoutePointCoord> {
        if (points.size < 3) return points

        var dmax = 0.0
        var index = 0
        val end = points.size - 1

        for (i in 1 until end) {
            val d = perpendicularDistance(points[i], points[0], points[end])
            if (d > dmax) {
                index = i
                dmax = d
            }
        }

        return if (dmax > epsilon) {
            val recResults1 = simplify(points.subList(0, index + 1), epsilon)
            val recResults2 = simplify(points.subList(index, end + 1), epsilon)
            recResults1.dropLast(1) + recResults2
        } else {
            listOf(points[0], points[end])
        }
    }

    private fun perpendicularDistance(p: RoutePointCoord, start: RoutePointCoord, end: RoutePointCoord): Double {
        val dx = end.longitude - start.longitude
        val dy = end.latitude - start.latitude
        if (dx == 0.0 && dy == 0.0) return distance(p, start)

        val numerator = abs(dy * p.longitude - dx * p.latitude + end.longitude * start.latitude - end.latitude * start.longitude)
        val denominator = sqrt(dy * dy + dx * dx)
        return numerator / denominator
    }

    private fun distance(p1: RoutePointCoord, p2: RoutePointCoord): Double {
        return sqrt((p1.latitude - p2.latitude).pow(2) + (p1.longitude - p2.longitude).pow(2))
    }

    fun normalize(points: List<RoutePointCoord>): List<NormalizedPoint> {
        if (points.isEmpty()) return emptyList()
        if (points.size == 1) return listOf(NormalizedPoint(0.5f, 0.5f))

        val minLat = points.minOf { it.latitude }
        val maxLat = points.maxOf { it.latitude }
        val minLon = points.minOf { it.longitude }
        val maxLon = points.maxOf { it.longitude }

        val deltaLat = maxLat - minLat
        val meanLat = (minLat + maxLat) / 2.0
        val cosLat = abs(cos(Math.toRadians(meanLat)))
        val deltaLon = (maxLon - minLon) * cosLat

        if (deltaLat <= 1e-7 && deltaLon <= 1e-7) {
            return points.map { NormalizedPoint(0.5f, 0.5f) }
        }

        val maxDelta = max(deltaLat, deltaLon)
        val offsetX = (maxDelta - deltaLon) / 2.0
        val offsetY = (maxDelta - deltaLat) / 2.0

        return points.map { pt ->
            val adjLon = (pt.longitude - minLon) * cosLat
            val x = ((adjLon + offsetX) / maxDelta).toFloat().coerceIn(0f, 1f)
            val adjLat = maxLat - pt.latitude
            val y = ((adjLat + offsetY) / maxDelta).toFloat().coerceIn(0f, 1f)
            NormalizedPoint(x = x, y = y)
        }
    }

    fun toCanvasOffsets(
        normalizedPoints: List<NormalizedPoint>,
        widthPx: Float,
        heightPx: Float,
        paddingPx: Float = 24f
    ): List<Pair<Float, Float>> {
        val usableW = max(1f, widthPx - 2 * paddingPx)
        val usableH = max(1f, heightPx - 2 * paddingPx)
        val scale = min(usableW, usableH)
        val containerOffsetX = paddingPx + (usableW - scale) / 2f
        val containerOffsetY = paddingPx + (usableH - scale) / 2f

        return normalizedPoints.map { np ->
            Pair(containerOffsetX + np.x * scale, containerOffsetY + np.y * scale)
        }
    }
}
