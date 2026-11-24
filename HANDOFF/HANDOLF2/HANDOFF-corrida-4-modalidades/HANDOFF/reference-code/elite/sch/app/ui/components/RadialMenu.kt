package com.elite.sch.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.elite.sch.app.ui.theme.RunnaTheme
import com.elite.sch.app.ui.theme.rememberReducedMotion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Menu radial do topo direito (engrenagem).
 * Contêiner 160×160 no canto superior direito (padding 16/16).
 * Botão âncora 48 (engrenagem). Aberto: âncora gira 90° em 300 ms e fica --teal com ícone --teal-on.
 * Arco --glass-elevated de 132, canto superior-direito = centro da âncora; escala .2→1 + opacidade, 250 ms, origem no canto, FastOutSlowIn.
 * Três botões de 44 a 80 do centro da âncora, 280 ms FastOutSlowIn, atraso 0/50/100 ms ao abrir (sem atraso ao fechar), escala .5→1 + opacidade:
 * Som (180°: −80, 0) · Vibração (135°: −56,6, +56,6) · GPS ativo (90° para baixo: 0, +80).
 * Ligado = fundo --teal + ícone --teal-on; desligado = --item-off-bg + --muted.
 * Botões fechados não recebem toque. Toque fora fecha (scrim).
 * Não alterar. Abrir um leque fecha o outro.
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
    val reducedMotion = rememberReducedMotion()
    fun ms(v: Int) = if (reducedMotion) 0 else v

    val arcP = animateFloatAsState(if (open) 1f else 0f, tween(ms(250)), label = "radialArc").value
    val anchorRotation = animateFloatAsState(if (open) 90f else 0f, tween(ms(300)), label = "anchorRotation").value

    val items = listOf(
        RadialItem(Icons.Default.VolumeUp, soundEnabled, true, "Som", 180f, "radial_sound", { onSoundToggle(!soundEnabled) }),
        RadialItem(Icons.Default.Vibration, vibrationEnabled, true, "Vibração", 135f, "radial_vibration", { onVibrationToggle(!vibrationEnabled) }),
        RadialItem(Icons.Default.GpsFixed, gpsKeepAliveEnabled, true, "GPS ativo", 90f, "radial_gps", { onGpsKeepAliveToggle(!gpsKeepAliveEnabled) })
    )

    Box(modifier = modifier.size(RadialBox).testTag(testTag)) {
        // Quarto de círculo de vidro: o canto superior direito é o centro do botão âncora
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-23).dp, y = (-23).dp)
                .size(RunnaTheme.ArcSize)
                .graphicsLayer {
                    scaleX = 0.2f + 0.8f * arcP
                    scaleY = 0.2f + 0.8f * arcP
                    transformOrigin = TransformOrigin(1f, 0f)
                    alpha = arcP
                }
                .clip(RadialArcShape)
                .background(RunnaTheme.GlassElevated)
                .border(BorderStroke(1.dp, RunnaTheme.Line), RadialArcShape)
        )

        items.forEachIndexed { index, item -> RadialButton(item, index, open, ::ms) }

        // Botão âncora
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(RunnaTheme.TopFabSize)
                .graphicsLayer {
                    rotationZ = anchorRotation
                }
                .clip(CircleShape)
                .background(if (open) RunnaTheme.Teal else RunnaTheme.Glass)
                .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
                .clickable { onOpenChange(!open) }
                .semantics {
                    contentDescription = "Opções"
                    stateDescription = if (open) "Aberto" else "Fechado"
                }
                .testTag("btn_radial_anchor"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = if (open) RunnaTheme.TealOn else RunnaTheme.Teal,
                modifier = Modifier.size(RunnaTheme.IconSize)
            )
        }
    }
}

private val RadialBox = 160.dp
private val RadialArcShape = RoundedCornerShape(bottomStart = 132.dp)
private const val RadialRadius = 80f

private data class RadialItem(
    val icon: ImageVector,
    val active: Boolean,
    val enabled: Boolean,
    val label: String,
    val angleDeg: Float,
    val tag: String,
    val onClick: () -> Unit
)

@Composable
private fun BoxScope.RadialButton(item: RadialItem, index: Int, open: Boolean, ms: (Int) -> Int) {
    val p = animateFloatAsState(
        targetValue = if (open) 1f else 0f,
        animationSpec = tween(ms(280), delayMillis = if (open) ms(index * 50) else 0),
        label = "radialItem$index"
    ).value
    // Totalmente fechado: não compõe o botão (não recebe toque nem foco do TalkBack)
    if (p == 0f && !open) return

    val rad = item.angleDeg * PI.toFloat() / 180f
    val dx = RadialRadius * cos(rad)       // 180° -> -80 (esquerda)
    val dy = RadialRadius * sin(rad)       // 90°  -> +80 (para baixo)
    // Centro da âncora = (137, 23) no contêiner de 160dp; o botão tem 44dp -> canto = centro - 22
    val x = (115f + dx * p).dp
    val y = (-93f + dy * p).dp

    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = x, y = y)
            .size(RunnaTheme.ItemSize)
            .graphicsLayer {
                scaleX = 0.5f + 0.5f * p
                scaleY = 0.5f + 0.5f * p
                alpha = p * (if (item.enabled) 1f else 0.4f)
            }
            .clip(CircleShape)
            .background(if (item.active) RunnaTheme.Teal else RunnaTheme.ItemOffBg)
            .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
            .clickable(enabled = open) { item.onClick() }
            .semantics {
                contentDescription = item.label
                stateDescription = if (item.active) "Ligado" else "Desligado"
            }
            .testTag(item.tag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = when {
                item.active -> RunnaTheme.TealOn
                else -> RunnaTheme.Muted
            },
            modifier = Modifier.size(RunnaTheme.ItemIconSize)
        )
    }
}