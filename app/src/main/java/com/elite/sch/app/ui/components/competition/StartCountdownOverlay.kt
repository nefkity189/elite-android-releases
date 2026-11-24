package com.elite.sch.app.ui.components.competition

import android.provider.Settings
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.Olive

/** The caller owns the clock and begins tracking at zero, not after the animation. */
@Composable
fun StartCountdownOverlay(seconds: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val reducedMotion = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    val label = if (seconds <= 0) "JÁ!" else seconds.toString()
    Box(modifier.fillMaxSize().background(Color(0xF0050604)).testTag("start"), contentAlignment = Alignment.Center) {
        key(seconds) {
            var target by androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
            androidx.compose.runtime.LaunchedEffect(seconds) { target = 1f }
            val phase by animateFloatAsState(target, tween(1000, easing = CubicBezierEasing(.2f, .8f, .2f, 1f)), label = "start")
            val opacity = if (reducedMotion) 1f else if (phase < .25f) phase * 4f else (1f - phase) / .75f
            val scale = if (reducedMotion) 1f else 1.5f - .5f * phase
            Text(
                text = label,
                modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }.alpha(opacity.coerceIn(0f, 1f))
                    .testTag(if (seconds <= 0) "start.go" else "start.n$seconds"),
                fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold,
                fontSize = if (seconds <= 0) 120.sp else 180.sp,
                lineHeight = 180.sp, color = if (seconds <= 0) Color(0xFFECE8DA) else Olive,
                textAlign = TextAlign.Center, maxLines = 1
            )
        }
    }
}
