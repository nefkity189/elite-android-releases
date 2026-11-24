package com.elite.sch.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.model.RunState
import com.elite.sch.app.ui.theme.RunnaTheme
import androidx.compose.material3.MaterialTheme

@Composable
fun FreeRunLayout(
    runState: RunState,
    totalTimeFormatted: String,
    progress: Float,
    modifier: Modifier = Modifier,
    testTag: String = "free_run_layout"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerRing(
            timeFormatted = totalTimeFormatted,
            progress = progress,
            progressColor = RunnaTheme.Teal,
            textColor = RunnaTheme.Text,
            testTag = "timer_ring"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Corrida livre",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = RunnaTheme.Teal
        )
        Spacer(modifier = Modifier.height(8.dp))
        StatsRow(
            distanceKm = "%.2f".format(runState.totalDistanceMeters / 1000.0),
            paceFormatted = runState.averagePaceFormatted,
            testTag = "stats_row"
        )
    }
}