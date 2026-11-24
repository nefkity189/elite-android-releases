package com.elite.sch.app.service

import org.junit.Assert.assertEquals
import org.junit.Test

class RunStartCountdownTest {
    @Test
    fun `normal race start shows three two one and go`() {
        assertEquals(listOf(3, 2, 1, 0), RunStartCountdown.ticks())
    }
}
