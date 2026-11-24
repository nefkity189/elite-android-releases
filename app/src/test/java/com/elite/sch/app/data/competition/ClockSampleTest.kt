package com.elite.sch.app.data.competition
import org.junit.Assert.assertEquals
import org.junit.Test
class ClockSampleTest {
    @Test fun wallClockChangesCannotMoveServerEstimate() {
        val sample = ClockSample(1_800_000_000_000L, 10_000L, 50)
        assertEquals(1_800_000_003_000L, sample.now(13_000L))
    }
    @Test fun backwardsMonotonicValueNeverMovesBeforeSample() {
        assertEquals(1_800_000_000_000L, ClockSample(1_800_000_000_000L, 10_000L, 0).now(0))
    }
}
