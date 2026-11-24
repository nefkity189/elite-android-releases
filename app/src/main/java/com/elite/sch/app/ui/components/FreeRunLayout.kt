package com.elite.sch.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.service.RunState
import com.elite.sch.app.ui.theme.RunnaNumberStyles
import com.elite.sch.app.ui.theme.RunnaTextStyles
import com.elite.sch.app.ui.components.rememberEliteWindowSizeClass
import com.elite.sch.app.ui.components.rememberTimerFontScale
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun FreeRunLayout(
    runState: RunState,
    totalTimeFormatted: String,
    progress: Float = 0.0f,
    modifier: Modifier = Modifier,
    testTag: String = "free_run_layout"
) {
    Column(
        modifier = modifier.fillMaxWidth().testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TimerRing (112px) com anel de progresso - verde-água (Teal) para corrida livre
        TimerRing(
            timeFormatted = totalTimeFormatted,
            progress = progress,
            progressColor = RunnaTheme.Teal,
            textColor = RunnaTheme.Text,
            testTag = "timer_ring"
        )

        // Rótulo "Corrida livre" em verde-água, 12px, peso 500 - conforme spec HANDOFF
        Text(
            text = "Corrida livre",
            style = RunnaTextStyles.Label,
            color = RunnaTheme.Teal
        )

        // StatsRow - Distância + Ritmo
        StatsRow(
            distanceKm = "%.2f".format(runState.totalDistanceMeters / 1000.0),
            paceFormatted = runState.averagePaceFormatted,
            testTag = "stats_row"
        )
    }
}
