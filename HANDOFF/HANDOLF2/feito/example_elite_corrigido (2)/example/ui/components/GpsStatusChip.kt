package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.RunState

@Composable
fun GpsStatusChip(
    runState: RunState,
    modifier: Modifier = Modifier,
    testTag: String = "gps_chip"
) {
    val (text, signalColor) = when {
        !runState.isGpsRequested -> "GPS desligado" to RunnaTheme.Muted
        !runState.hasGpsSignal -> "Sem sinal" to RunnaTheme.RedDot
        else -> "GPS OK" to RunnaTheme.Teal
    }

    Surface(
        shape = RunnaTheme.ChipShape,
        color = RunnaTheme.Glass,
        border = BorderStroke(1.dp, RunnaTheme.Line),
        modifier = modifier
            .height(RunnaTheme.ChipHeight)
            .testTag(testTag)
            .semantics { contentDescription = "Status do GPS: $text" }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ponto de status
            Box(
                modifier = Modifier
                    .size(RunnaTheme.ChipDotSize)
                    .background(signalColor, androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(text = text, fontSize = 13.sp, color = RunnaTheme.Muted)
        }
    }
}
