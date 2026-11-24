package com.elite.sch.app

import com.elite.sch.app.tracker.KmMilestoneTracker
import org.junit.Assert.assertEquals
import org.junit.Test

class KmMilestoneTrackerTest {

    @Test
    fun `exatamente 1000m retorna km 1`() {
        val (crossed, next) = KmMilestoneTracker.checkAndAdvance(0, 1000.0)
        assertEquals(listOf(1), crossed)
        assertEquals(1, next)
    }

    @Test
    fun `999 para 1001 retorna km 1`() {
        // Primeiro check a 999m (nada anunciado)
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvance(0, 999.0)
        assertEquals(emptyList<Int>(), crossed1)
        assertEquals(0, next1)

        // Segundo check a 1001m
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvance(next1, 1001.0)
        assertEquals(listOf(1), crossed2)
        assertEquals(1, next2)
    }

    @Test
    fun `salto de 2,3 km retorna kms 1 e 2`() {
        val (crossed, next) = KmMilestoneTracker.checkAndAdvance(0, 2300.0)
        assertEquals(listOf(1, 2), crossed)
        assertEquals(2, next)
    }

    @Test
    fun `pausa e retomada nao repete km`() {
        // Chegou em 1200m
        val (_, next1) = KmMilestoneTracker.checkAndAdvance(0, 1200.0)
        assertEquals(1, next1)

        // "Pausou" (distancia nao sobe)
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvance(next1, 1200.0)
        assertEquals(emptyList<Int>(), crossed2)
        assertEquals(1, next2)

        // Retomou e foi para 2100m
        val (crossed3, next3) = KmMilestoneTracker.checkAndAdvance(next2, 2100.0)
        assertEquals(listOf(2), crossed3)
        assertEquals(2, next3)
    }

    @Test
    fun `restauracao em 2,4 km inicializado corretamente`() {
        // Inicializa com base na distancia salva (floor(2400/1000) = 2)
        val initialLastKm = 2

        // Check em 2500m (nada novo)
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvance(initialLastKm, 2500.0)
        assertEquals(emptyList<Int>(), crossed1)
        assertEquals(2, next1)

        // Check em 3100m
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvance(next1, 3100.0)
        assertEquals(listOf(3), crossed2)
        assertEquals(3, next2)
    }

    @Test
    fun `distancia decrescente eh ignorada`() {
        val (_, next1) = KmMilestoneTracker.checkAndAdvance(0, 1500.0)
        assertEquals(1, next1)

        // GPS saltou para 900m
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvance(next1, 900.0)
        assertEquals(emptyList<Int>(), crossed2)
        assertEquals(1, next2) // Mantem o ultimo anunciado
    }

    @Test
    fun `longa distancia 10 km`() {
        var lastKm = 0
        for (m in 0..10500 step 100) {
            val (crossed, next) = KmMilestoneTracker.checkAndAdvance(lastKm, m.toDouble())
            if (m > 0 && m % 1000 == 0) {
                assertEquals(listOf(m / 1000), crossed)
            } else if (crossed.isNotEmpty()) {
                // Se cruzou, deve ser exatamente o proximo km
                assertEquals(listOf(lastKm + 1), crossed)
            }
            lastKm = next
        }
        assertEquals(10, lastKm)
    }

    @Test
    fun `distancia zero nada anunciado`() {
        val (crossed, next) = KmMilestoneTracker.checkAndAdvance(0, 0.0)
        assertEquals(emptyList<Int>(), crossed)
        assertEquals(0, next)
    }

    @Test
    fun `distancia muito pequena`() {
        val (crossed, next) = KmMilestoneTracker.checkAndAdvance(0, 0.5)
        assertEquals(emptyList<Int>(), crossed)
        assertEquals(0, next)
    }

    @Test
    fun `inicializacao com lastKm maior que zero`() {
        // Simula reiniciar o app apos ja ter corrido 5.5km
        val (crossed, next) = KmMilestoneTracker.checkAndAdvance(5, 5500.0)
        assertEquals(emptyList<Int>(), crossed)
        assertEquals(5, next)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvance(next, 6100.0)
        assertEquals(listOf(6), crossed2)
        assertEquals(6, next2)
    }

    // === Testes para checkAndAdvanceInterval (intervalos configuráveis) ===

    @Test
    fun `intervalo 500m 250 para 750 retorna marco 1`() {
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 250.0, 500)
        assertEquals(emptyList<Int>(), crossed1)
        assertEquals(0, next1)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(next1, 750.0, 500)
        assertEquals(listOf(1), crossed2)
        assertEquals(1, next2)
    }

    @Test
    fun `intervalo 500m 1250 para 1750 retorna marco 3`() {
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 1250.0, 500)
        assertEquals(listOf(1, 2), crossed1) // 500, 1000
        assertEquals(2, next1)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(next1, 1750.0, 500)
        assertEquals(listOf(3), crossed2) // 1500
        assertEquals(3, next2)
    }

    @Test
    fun `intervalo 2000m 1500 para 3500 retorna marco 1`() {
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 1500.0, 2000)
        assertEquals(emptyList<Int>(), crossed1)
        assertEquals(0, next1)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(next1, 3500.0, 2000)
        assertEquals(listOf(1), crossed2)
        assertEquals(1, next2)
    }

    @Test
    fun `intervalo 2000m 4100 retorna marco 2`() {
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 4100.0, 2000)
        assertEquals(listOf(1, 2), crossed1)
        assertEquals(2, next1)
    }

    @Test
    fun `intervalo invalido ou zero nao retorna marcos`() {
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 1000.0, 0)
        assertEquals(emptyList<Int>(), crossed1)
        assertEquals(0, next1)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(0, 1000.0, -500)
        assertEquals(emptyList<Int>(), crossed2)
        assertEquals(0, next2)
    }

    @Test
    fun `distancia decrescente com intervalo 500 ignora`() {
        val (_, next1) = KmMilestoneTracker.checkAndAdvanceInterval(0, 1500.0, 500)
        assertEquals(3, next1) // 500, 1000, 1500

        // GPS saltou para 900m
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(next1, 900.0, 500)
        assertEquals(emptyList<Int>(), crossed2)
        assertEquals(3, next2) // Mantem o ultimo anunciado (1500m = índice 3)
    }

    @Test
    fun `inicializacao com lastIndex maior que zero`() {
        // Simula reiniciar o app apos ja ter corrido 2500m (intervalo 500 = índice 5)
        val (crossed, next) = KmMilestoneTracker.checkAndAdvanceInterval(5, 2500.0, 500)
        assertEquals(emptyList<Int>(), crossed)
        assertEquals(5, next)

        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(next, 3100.0, 500)
        assertEquals(listOf(6), crossed2) // 3000m
        assertEquals(6, next2)
    }

    @Test
    fun `checkAndAdvanceDefaultCompatibleComInterval1000`() {
        // Verifica que checkAndAdvance ainda funciona igual ao chamar checkAndAdvanceInterval(..., 1000)
        val (crossed1, next1) = KmMilestoneTracker.checkAndAdvance(0, 2500.0)
        val (crossed2, next2) = KmMilestoneTracker.checkAndAdvanceInterval(0, 2500.0, 1000)
        assertEquals(crossed1, crossed2)
        assertEquals(next1, next2)
    }
}