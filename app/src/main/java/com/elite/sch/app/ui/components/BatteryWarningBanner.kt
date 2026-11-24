package com.elite.sch.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elite.sch.app.ui.theme.RunnaTextStyles

@Composable
fun BatteryWarningBanner(
    show: Boolean = false,
    dismissBatteryWarning: Boolean = false,
    onDismissBanner: () -> Unit = {},
    onDismissForever: () -> Unit = {},
    modifier: Modifier = Modifier,
    testTag: String = "battery_warning"
) {
    var dismissedInSession by remember { mutableStateOf(false) }
    val shouldShow = show && !dismissBatteryWarning && !dismissedInSession

    AnimatedVisibility(
        visible = shouldShow,
        enter = fadeIn(animationSpec = tween(200)) + slideInVertically(initialOffsetY = { -50 }, animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(targetOffsetY = { -50 }, animationSpec = tween(200))
    ) {
        Surface(
            shape = RunnaTheme.BannerShape,
            color = RunnaTheme.Glass,
            border = BorderStroke(1.dp, RunnaTheme.Line),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = RunnaTheme.ActionBarPadding, vertical = 4.dp)
                .clickable { onDismissBanner() }
                .testTag(testTag)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "O celular pode pausar o GPS com a tela apagada. Toque para ajustar.",
                        style = RunnaTextStyles.BodySmall,
                        color = androidx.compose.ui.graphics.Color(0xFFECE8DA)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Não mostrar de novo",
                        style = RunnaTextStyles.LabelSmall,
                        color = RunnaTheme.Teal,
                        modifier = Modifier.testTag("btn_dismiss_battery_forever")
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fechar aviso",
                    tint = RunnaTheme.Muted,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { dismissedInSession = true }
                )
            }
        }
    }
}
