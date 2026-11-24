package com.elite.sch.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.RunnaTheme

@Composable
fun StatsRow(
    distanceKm: String,
    paceFormatted: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RunnaTheme.ActionBarPadding)
    ) {
        Text(
            text = distanceKm,
            style = MaterialTheme.typography.titleLarge,
            color = RunnaTheme.Text,
            fontWeight = androidx.compose.ui.text.style.FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = paceFormatted,
            style = MaterialTheme.typography.titleLarge,
            color = RunnaTheme.Muted,
            fontWeight = androidx.compose.ui.text.style.FontWeight.Medium
        )
    }
}