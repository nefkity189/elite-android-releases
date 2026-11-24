package com.elite.sch.app.ui.components

import com.elite.sch.app.ui.competition.CompetitionDistance

/** Copy shared by invite cards so it always names the selected race distance. */
object CompetitionInviteCopy {
    fun distanceLabel(distance: CompetitionDistance): String = distance.label
}
