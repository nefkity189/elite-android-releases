package com.elite.sch.app

import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.tracker.KmSplitsCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KmSplitsCalculatorTest {

    @Test
    fun `calcula lista vazia para menos de 2 pontos`() {
        val splits = KmSplitsCalculator.calculateSplits(emptyList())
        assertTrue(splits.isEmpty())

        val singlePoint = listOf(
            RoutePointEntity(1L, 1L, -23.55, -46.63, 10f, 1000L)
        )
        assertTrue(KmSplitsCalculator.calculateSplits(singlePoint).isEmpty())
    }

    @Test
    fun `calcula split de 1km com precisao`() {
        // Pontos cobrindo ~1km em 300 segundos (5:00 min/km)
        val p1 = RoutePointEntity(1L, 1L, -23.5500, -46.6300, 10f, 0L)
        val p2 = RoutePointEntity(2L, 1L, -23.5545, -46.6300, 10f, 150000L) // ~500m em 150s
        val p3 = RoutePointEntity(3L, 1L, -23.5590, -46.6300, 10f, 300900L) // ~1000m em 300.9s

        val splits = KmSplitsCalculator.calculateSplits(listOf(p1, p2, p3))
        assertEquals(1, splits.size)
        assertEquals(1, splits[0].kmIndex)
        assertFalse(splits[0].isPartial)
        assertEquals("04:59", splits[0].paceFormatted)
    }

    @Test
    fun `inclui split parcial final se a distancia sobressalente for maior ou igual a 100m`() {
        // Pontos cobrindo 1km + 400m parciais
        val p1 = RoutePointEntity(1L, 1L, -23.5500, -46.6300, 10f, 0L)
        val p2 = RoutePointEntity(2L, 1L, -23.5590, -46.6300, 10f, 300000L) // ~1km em 300s
        val p3 = RoutePointEntity(3L, 1L, -23.5626, -46.6300, 10f, 420000L) // +400m em +120s

        val splits = KmSplitsCalculator.calculateSplits(listOf(p1, p2, p3))
        assertEquals(2, splits.size)

        // 1º km
        assertEquals(1, splits[0].kmIndex)
        assertFalse(splits[0].isPartial)

        // 2º km (parcial)
        assertEquals(2, splits[1].kmIndex)
        assertTrue(splits[1].isPartial)
    }
}