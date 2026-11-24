package com.elite.sch.app.ui.components

import com.elite.sch.app.ui.competition.CompetitionDistance
import org.junit.Assert.assertEquals
import org.junit.Test

class CompetitionInviteCopyTest {
    @Test
    fun `invite copy follows selected competition distance`() {
        assertEquals("2,5K", CompetitionInviteCopy.distanceLabel(CompetitionDistance.K25))
        assertEquals("5K", CompetitionInviteCopy.distanceLabel(CompetitionDistance.K5))
        assertEquals("10K", CompetitionInviteCopy.distanceLabel(CompetitionDistance.K10))
    }
}
