package com.elite.sch.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.elite.sch.app.ui.state.CameraMode

@Composable
fun MapOverlay(
    cameraMode: CameraMode = CameraMode.FollowUser,
    onRecenter: () -> Unit = {},
    mapControlsVisible: Boolean = true,
    modifier: Modifier = Modifier,
    testTag: String = "map_overlay"
) {
    val showFollow = cameraMode is CameraMode.FollowUser || cameraMode is CameraMode.FollowUserWithBearing
    val showLocate = true

    Box(
        modifier = modifier.fillMaxSize().testTag(testTag),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (showFollow) {
            Surface(
                shape = RunnaTheme.FollowPillShape,
                color = RunnaTheme.Glass,
                border = BorderStroke(1.dp, RunnaTheme.Line),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 14.dp)
                    .testTag("map_pill_following")
            ) {
                Text(
                    text = "Seguindo você",
                    fontSize = RunnaTheme.FollowTextSize,
                    color = RunnaTheme.Text,
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)
                )
            }
        }

        if (showLocate) {
            Surface(
                shape = CircleShape,
                color = RunnaTheme.Glass,
                border = BorderStroke(1.dp, RunnaTheme.Line),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 10.dp)
                    .size(RunnaTheme.LocateSize)
                    .semantics { contentDescription = "Centralizar no mapa" }
                    .testTag("btn_map_recenter")
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = RunnaTheme.Teal,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
