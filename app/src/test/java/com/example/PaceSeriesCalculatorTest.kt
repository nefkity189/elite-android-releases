package com.example

import com.example.data.local.entities.RoutePointEntity
import com.example.tracker.PaceDataPoint
import com.example.tracker.PaceSeriesCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PaceSeriesCalculatorTest {

    private fun createPoint(
        id: Long,
        lat: Double,
        lon: Double,
        timestampSec: Long
    ): RoutePointEntity {
        return RoutePointEntity(
            id = id,
            workoutId = 1L,
            latitude = lat,
            longitude = lon,
            accuracy = 5.0f,
            timestampMillis = timestampSec * 1000L
        )
    }

    @Test
    fun `empty or single point returns empty list`() {
        assertTrue(PaceSeriesCalculator.calculate(emptyList()).isEmpty())
        val singlePoint = listOf(createPoint(1, -23.5505, -46.6333, 0))
        assertTrue(PaceSeriesCalculator.calculate(singlePoint).isEmpty())
    }

    @Test
    fun `steady running pace is calculated correctly`() {
        // ~111 metros por grau de latitude (0.001 grau ≈ 111 metros)
        // Criando deslocamento regular de corrida: ~50 metros a cada 10 segundos (~5 m/s, ~200 s/km = 3:20 min/km)
        val points = mutableListOf<RoutePointEntity>()
        var currentLat = -23.5500
        for (i in 0..10) {
            points.add(createPoint(i.toLong(), currentLat, -46.6333, i * 5L))
            currentLat += 0.0002 // ~22 metros a cada 5 segundos (~3:45 min/km)
        }

        val result = PaceSeriesCalculator.calculate(points, windowSeconds = 30L)
        assertTrue(result.isNotEmpty())
        for ((_, paceSecondsPerKm) in result) {
            assertTrue(paceSecondsPerKm < PaceSeriesCalculator.MAX_PACE_SECONDS_PER_KM)
            assertTrue(paceSecondsPerKm >= PaceSeriesCalculator.MIN_PACE_SECONDS_PER_KM)
        }
    }

    @Test
    fun `stopped segments are ignored`() {
        // Utilizador parado no mesmo ponto por 60 segundos
        val points = mutableListOf<RoutePointEntity>()
        for (i in 0..10) {
            points.add(createPoint(i.toLong(), -23.550520, -46.633308, i * 6L))
        }

        val result = PaceSeriesCalculator.calculate(points, windowSeconds = 30L)
        // Como não há deslocamento, os trechos são parados e descartados
        assertTrue(result.isEmpty())
    }

    @Test
    fun `segments slower than 20 min per km are ignored`() {
        // Movimento extremamente lento: 1 metro em 30 segundos (< 0,5 m/s e > 20 min/km)
        val points = mutableListOf<RoutePointEntity>()
        var currentLat = -23.550000
        for (i in 0..6) {
            points.add(createPoint(i.toLong(), currentLat, -46.6333, i * 10L))
            currentLat += 0.000008 // ~0,8 metros a cada 10 segundos
        }

        val result = PaceSeriesCalculator.calculate(points, windowSeconds = 30L)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `paces faster than 3 min per km are ignored`() {
        // ~55 m a cada 5 s = 11 m/s (1:30 min/km) → ruído de GPS
        val points = (0..10).map { createPoint(it.toLong(), -23.55 + it * 0.0005, -46.6333, it * 5L) }
        assertTrue(PaceSeriesCalculator.calculate(points, windowSeconds = 30L).isEmpty())
    }

    @Test
    fun `gap larger than 60 seconds splits the line`() {
        val series = listOf(
            PaceDataPoint(0, 300.0), PaceDataPoint(10, 305.0), PaceDataPoint(20, 310.0),
            PaceDataPoint(100, 320.0), PaceDataPoint(110, 315.0)
        )
        val segments = PaceSeriesCalculator.splitIntoSegments(series)
        assertEquals(2, segments.size)
        assertEquals(3, segments[0].size)
        assertEquals(2, segments[1].size)
    }

    @Test
    fun `axis uses percentile 5 to 95 and ignores outliers`() {
        val series = (1..100).map { PaceDataPoint(it * 10L, 300.0 + it) } +
            PaceDataPoint(2000, 190.0) + PaceDataPoint(2010, 1190.0)
        val (lo, hi) = PaceSeriesCalculator.axisRange(series)
        assertTrue(lo > 190.0)
        assertTrue(hi < 1190.0)
        assertTrue(lo < hi)
    }
}
