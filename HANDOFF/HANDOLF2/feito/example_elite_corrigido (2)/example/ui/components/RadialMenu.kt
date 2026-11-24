package com.example.ui.components

import android.provider.Settings
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Menu radial em quarto de círculo (canto superior direito).
 *
 * Geometria idêntica ao protótipo HTML:
 *  - contêiner de 160dp; o FAB (48dp) fica no canto superior direito, centro em (136, 24)
 *  - arco de vidro 132dp com o canto superior direito no centro do FAB
 *  - botões de 44dp a 80dp do centro do FAB, nos ângulos 180°, 135° e 90°
 *
 * O contêiner é grande de propósito: no Compose, toques fora dos limites do pai não chegam
 * aos filhos, então os botões precisam estar DENTRO da área do componente.
 * Posicione com Modifier.align(TopEnd).padding(top = 16.dp, end = 16.dp) na raiz da tela.
 */
@Composable
fun RadialMenu(
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    gpsKeepAliveEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    onVibrationToggle: (Boolean) -> Unit,
    onGpsKeepAliveToggle: (Boolean) -> Unit,
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "radial_menu"
) {
    val context = LocalContext.current
    val reducedMotion = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    fun ms(v: Int) = if (reducedMotion) 0 else v

    val arcP = animateFloatAsState(if (open) 1f else 0f, tween(ms(250)), label = "arc")
    val fabRotation = animateFloatAsState(if (open) 90f else 0f, tween(ms(300)), label = "fab")

    val r = 80f
    val d = r * 0.7071f
    val items = listOf(
        RadialItem(Icons.AutoMirrored.Filled.VolumeUp, Icons.AutoMirrored.Filled.VolumeOff, soundEnabled, "Som", -r, 0f) { onSoundToggle(!soundEnabled) },
        RadialItem(Icons.Default.Vibration, Icons.Default.Vibration, vibrationEnabled, "Vibração", -d, d) { onVibrationToggle(!vibrationEnabled) },
        RadialItem(Icons.Default.GpsFixed, Icons.Default.GpsFixed, gpsKeepAliveEnabled, "Manter GPS ativo", 0f, r) { onGpsKeepAliveToggle(!gpsKeepAliveEnabled) }
    )

    Box(modifier = modifier.size(RadialBox).testTag(testTag)) {
        // Quarto de círculo: canto superior direito = centro do FAB
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = 24.dp)
                .size(RunnaTheme.ArcSize)
                .graphicsLayer {
                    scaleX = 0.2f + 0.8f * arcP.value
                    scaleY = 0.2f + 0.8f * arcP.value
                    transformOrigin = TransformOrigin(1f, 0f)
                    alpha = arcP.value
                }
                .clip(RunnaTheme.ArcShape)
                .background(RunnaTheme.GlassElevated)
                .border(BorderStroke(1.dp, RunnaTheme.Line), RunnaTheme.ArcShape)
        )

        items.forEachIndexed { index, item -> RadialButton(item, index, open, ::ms) }

        // FAB âncora
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(RunnaTheme.FabSize)
                .graphicsLayer { rotationZ = fabRotation.value }
                .clip(CircleShape)
                .background(if (open) RunnaTheme.Teal else RunnaTheme.Glass)
                .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
                .clickable { onOpenChange(!open) }
                .semantics { contentDescription = "Configurações da corrida" }
                .testTag("radial_anchor"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = if (open) RunnaTheme.TealOn else RunnaTheme.Text,
                modifier = Modifier.size(RunnaTheme.IconSize)
            )
        }
    }
}

private val RadialBox = 160.dp

private data class RadialItem(
    val iconOn: ImageVector,
    val iconOff: ImageVector,
    val enabled: Boolean,
    val label: String,
    val dx: Float,
    val dy: Float,
    val onToggle: () -> Unit
)

@Composable
private fun BoxScope.RadialButton(item: RadialItem, index: Int, open: Boolean, ms: (Int) -> Int) {
    val p = animateFloatAsState(
        targetValue = if (open) 1f else 0f,
        animationSpec = tween(ms(280), delayMillis = if (open) ms(index * 50) else 0),
        label = "item$index"
    ).value
    // Centro do FAB = (136, 24) dentro do contêiner de 160dp; o botão tem 44dp -> canto = centro - 22
    val x = (114f + item.dx * p).dp
    val y = (2f + item.dy * p).dp
    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = x, y = y)
            .size(RunnaTheme.ItemSize)
            .graphicsLayer { scaleX = 0.5f + 0.5f * p; scaleY = 0.5f + 0.5f * p; alpha = p }
            .clip(CircleShape)
            .background(if (item.enabled) RunnaTheme.Teal else RunnaTheme.ItemOffBg)
            .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
            .then(if (open) Modifier.clickable { item.onToggle() } else Modifier)
            .semantics { contentDescription = item.label },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (item.enabled) item.iconOn else item.iconOff,
            contentDescription = null,
            tint = if (item.enabled) RunnaTheme.TealOn else RunnaTheme.Muted,
            modifier = Modifier.size(RunnaTheme.ItemIconSize)
        )
    }
}
