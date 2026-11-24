package com.elite.sch.app.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import com.elite.sch.app.ui.state.CameraMode
import com.elite.sch.app.ui.components.RunnaTheme
import com.elite.sch.app.ui.theme.rememberReducedMotion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Leque de opcoes do mapa, ancorado no canto inferior direito (espelho do RadialMenu).
 *
 * Substitui o antigo botao "Centralizar no mapa", que nao tinha acao. Itens (de cima para a esquerda):
 *  90° Centralizar · 120° Modo escuro · 150° Tipo de mapa · 180° Nomes de ruas
 *
 * Geometria: container de 160dp; o botao ancora (46dp) fica no canto inferior direito, centro em
 * (137, 137). Botoes de 44dp a 96dp do centro. O container e grande de proposito: no Compose, toques
 * fora dos limites do pai nao chegam aos filhos. Por isso o leque deve ficar na RAIZ da tela:
 *   Modifier.align(BottomEnd).padding(end = 16.dp, bottom = alturaDaActionBar + 10.dp)
 */
@Composable
fun MapStyleFan(
    open: Boolean,
    onOpenChange: (Boolean) -> Unit,
    darkEnabled: Boolean,
    streetNamesEnabled: Boolean,
    styleSwitchEnabled: Boolean,
    onRecenter: () -> Unit,
    onDarkToggle: () -> Unit,
    onCycleBaseStyle: () -> Unit,
    onStreetNamesToggle: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "map_style_fan"
) {
    val reducedMotion = rememberReducedMotion()
    fun ms(v: Int) = if (reducedMotion) 0 else v

    val arcP = animateFloatAsState(if (open) 1f else 0f, tween(ms(250)), label = "fanArc").value

    val items = listOf(
        FanItem(Icons.Default.MyLocation, FanKind.ACTION, true, true, "Centralizar no mapa", 90f, "fan_recenter", onRecenter),
        FanItem(Icons.Default.DarkMode, FanKind.TOGGLE, darkEnabled, styleSwitchEnabled, "Modo escuro", 120f, "fan_dark", onDarkToggle),
        FanItem(Icons.Default.Map, FanKind.ACTION, false, styleSwitchEnabled, "Tipo de mapa", 150f, "fan_type", onCycleBaseStyle),
        FanItem(Icons.Default.TextFields, FanKind.TOGGLE, streetNamesEnabled, true, "Nomes de ruas", 180f, "fan_labels", onStreetNamesToggle)
    )

    Box(modifier = modifier.size(FanBox).testTag(testTag)) {
        // Quarto de circulo de vidro: o canto inferior direito e o centro do botao ancora
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-23).dp, y = (-23).dp)
                .size(RunnaTheme.ArcSize)
                .graphicsLayer {
                    scaleX = 0.2f + 0.8f * arcP
                    scaleY = 0.2f + 0.8f * arcP
                    transformOrigin = TransformOrigin(1f, 1f)
                    alpha = arcP
                }
                .clip(FanArcShape)
                .background(RunnaTheme.GlassElevated)
                .border(BorderStroke(1.dp, RunnaTheme.Line), FanArcShape)
        )

        items.forEachIndexed { index, item -> FanButton(item, index, open, ::ms) }

        // Botao ancora
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(RunnaTheme.LocateSize)
                .clip(CircleShape)
                .background(if (open) RunnaTheme.Teal else RunnaTheme.Glass)
                .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
                .clickable { onOpenChange(!open) }
                .semantics {
                    contentDescription = "Opcoes do mapa"
                    stateDescription = if (open) "Aberto" else "Fechado"
                }
                .testTag("btn_map_recenter"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (open) Icons.Default.Close else Icons.Default.Layers,
                contentDescription = null,
                tint = if (open) RunnaTheme.TealOn else RunnaTheme.Teal,
                modifier = Modifier.size(RunnaTheme.IconSize)
            )
        }
    }
}

private val FanBox = 160.dp
private val FanArcShape = RoundedCornerShape(topStart = 132.dp)
private const val FanRadius = 96f

private enum class FanKind { ACTION, TOGGLE }

private data class FanItem(
    val icon: ImageVector,
    val kind: FanKind,
    val active: Boolean,
    val enabled: Boolean,
    val label: String,
    val angleDeg: Float,
    val tag: String,
    val onClick: () -> Unit
)

@Composable
private fun BoxScope.FanButton(item: FanItem, index: Int, open: Boolean, ms: (Int) -> Int) {
    val p = animateFloatAsState(
        targetValue = if (open) 1f else 0f,
        animationSpec = tween(ms(280), delayMillis = if (open) ms(index * 50) else 0),
        label = "fanItem$index"
    ).value
    // Totalmente fechado: nao compoe o botao (nao recebe toque nem foco do TalkBack)
    if (p == 0f && !open) return

    val rad = item.angleDeg * PI.toFloat() / 180f
    val dx = FanRadius * cos(rad)       // 180° -> -96 (esquerda)
    val dy = -FanRadius * sin(rad)      // 90°  -> -96 (para cima)
    // Centro da ancora = (137, 137) no container de 160dp; o botao tem 44dp -> canto = centro - 22
    val x = (115f + dx * p).dp
    val y = (115f + dy * p).dp

    val filled = item.kind == FanKind.TOGGLE && item.active
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
            .background(if (filled) RunnaTheme.Teal else RunnaTheme.ItemOffBg)
            .border(BorderStroke(1.dp, RunnaTheme.Line), CircleShape)
            .clickable(enabled = open) { item.onClick() }
            .semantics {
                contentDescription = item.label
                if (item.kind == FanKind.TOGGLE) stateDescription = if (item.active) "Ligado" else "Desligado"
            }
            .testTag(item.tag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = when {
                filled -> RunnaTheme.TealOn
                item.kind == FanKind.ACTION -> RunnaTheme.Teal
                else -> RunnaTheme.Muted
            },
            modifier = Modifier.size(RunnaTheme.ItemIconSize)
        )
    }
}