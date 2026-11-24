package com.elite.sch.app

import com.elite.sch.app.tracker.KmMilestoneTracker
import org.junit.Assert.assertEquals
import org.junit.Test

class FreeRunAndVoiceTest {

    @Test
    fun testKmMilestoneTrackingLogic() {
        // Testa a lógica de marcos de quilômetro para corrida livre e avisos de voz
        var lastKm = 0

        // 500m - nenhum km cruzado
        val (c1, n1) = KmMilestoneTracker.checkAndAdvance(lastKm, 500.0)
        assertEquals(emptyList<Int>(), c1)
        assertEquals(0, n1)
        lastKm = n1

        // 1050m - cruzou o km 1
        val (c2, n2) = KmMilestoneTracker.checkAndAdvance(lastKm, 1050.0)
        assertEquals(listOf(1), c2)
        assertEquals(1, n2)
        lastKm = n2

        // 1900m - ainda no km 1
        val (c3, n3) = KmMilestoneTracker.checkAndAdvance(lastKm, 1900.0)
        assertEquals(emptyList<Int>(), c3)
        assertEquals(1, n3)
        lastKm = n3

        // 2100m - cruzou o km 2
        val (c4, n4) = KmMilestoneTracker.checkAndAdvance(lastKm, 2100.0)
        assertEquals(listOf(2), c4)
        assertEquals(2, n4)
    }
}