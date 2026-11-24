package com.example.ui.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RunnaNumberStyles
import com.example.ui.theme.RunnaTextStyles

@Composable
fun StatsRow(
    distanceKm: String = "0.00",
    paceFormatted: String = "--:--",
    modifier: Modifier = Modifier,
    testTag: String = "stats_row"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(RunnaTheme.StatsRowHeight)
            .testTag(testTag),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coluna Esquerda - Distância
        // HTML: .stats{padding:0 28px} - usar 28dp horizontal padding
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = distanceKm, style = RunnaNumberStyles.MetricLarge, color = RunnaTheme.Text)
                Text(text = "km", style = RunnaNumberStyles.MetricUnit, color = RunnaTheme.Muted)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Distância", style = RunnaTextStyles.Label, color = RunnaTheme.Muted)
        }

        // Divisor vertical central (1px) - HTML: .stats i{height:34px;background:var(--line)}
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(RunnaTheme.StatsDividerHeight)
                .background(RunnaTheme.Line)
        )

        // Coluna Direita - Ritmo
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = paceFormatted, style = RunnaNumberStyles.MetricLarge, color = RunnaTheme.Text)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Ritmo (min/km)", style = RunnaTextStyles.Label, color = RunnaTheme.Muted)
        }
    }
}
