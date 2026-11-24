package com.elite.sch.app.ui.components

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.RunnaNumberFont
import com.elite.sch.app.ui.theme.RunnaTextFont
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.OnOlive
import androidx.compose.ui.res.painterResource
import com.elite.sch.app.R
import kotlinx.coroutines.launch
import kotlin.math.hypot

/**
 * Barra de navegação inferior. Índices: 0 Hoje, 1 Plano, 2 (botão A), 3 Progresso, 4 Perfil.
 * O botão A fica DENTRO da barra, alinhado aos outros itens.
 */
import com.elite.sch.app.ui.theme.Surface
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.Surface as SurfaceColor

@Composable
fun RunnaBottomNav(
    selected: Int,
    onSelect: (Int) -> Unit,
    onCoachClick: () -> Unit,
    modifier: Modifier = Modifier,
    showCoach: Boolean = true
) {
    val shape = RoundedCornerShape(24.dp)
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(start = 12.dp, end = 12.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(64.dp)
            .background(SurfaceColor, shape)
            .border(BorderStroke(1.dp, LineStrong), shape)
            .padding(horizontal = 6.dp)
            .testTag("bottom_nav"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("Hoje", Icons.Default.Home, selected == 0, Modifier.weight(1f)) { onSelect(0) }
        NavItem("Plano", Icons.Default.CalendarMonth, selected == 1, Modifier.weight(1f)) { onSelect(1) }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { if (showCoach) CoachButton(onClick = onCoachClick) }
        NavItem("Progresso", Icons.AutoMirrored.Filled.TrendingUp, selected == 3, Modifier.weight(1f)) { onSelect(3) }
        NavItem("Perfil", Icons.Default.Person, selected == 4, Modifier.weight(1f)) { onSelect(4) }
    }
}

@Composable
private fun NavItem(label: String, icon: ImageVector, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val color = if (selected) Olive else TextMuted
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .semantics { contentDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        Text(label, color = color, style = TextStyle(fontFamily = RunnaTextFont, fontWeight = FontWeight.Medium, fontSize = 11.sp))
    }
}

/**
 * Botão Elite (E inclinada): quadrado arredondado 48dp (raio 16) com borda de 2dp em degradê
 * cônico que gira (Olive → Red → Brass → Olive, 5s por volta), miolo escuro radial, brilho "respira",
 * e ao tocar: pulo elástico (.5s), giro do símbolo Y 360° (.6s), onda Olive (.6s).
 * Respeita "reduzir animações" do sistema.
 */
@Composable
fun CoachButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val reduced = remember {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    val shape = RoundedCornerShape(16.dp)
    val easeInOut = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)   // CSS ease-in-out
    val easeOut = CubicBezierEasing(0f, 0f, 0.58f, 1f)        // CSS ease-out
    val popEasing = CubicBezierEasing(0.3f, 1.6f, 0.5f, 1f)   // cubic-bezier(.3,1.6,.5,1)

    val infinite = rememberInfiniteTransition(label = "coach")
    val angle by infinite.animateFloat(
        0f, 360f, infiniteRepeatable(tween(5000, easing = LinearEasing)), label = "angle"
    )
    val glow by infinite.animateFloat(
        0f, 1f, infiniteRepeatable(tween(1600, easing = easeInOut), RepeatMode.Reverse), label = "glow"
    )

    val pop = remember { Animatable(1f) }
    val flip = remember { Animatable(0f) }
    val wave = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (pressed) 0.9f else 1f, tween(180), label = "press")
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .size(48.dp)
            // Brilho Olive "respira" (0 a 35%, 3.2s)
            .drawBehind {
                if (reduced) return@drawBehind
                val peak = 0.35f * glow
                val spread = 2.dp.toPx()
                val blur = 16.dp.toPx()
                val steps = (spread + blur).toInt() + 1
                for (e in 0 until steps) {
                    val dist = e + 0.5f
                    val t = ((dist - spread + blur) / (2f * blur)).coerceIn(0f, 1f)
                    val fall = 1f - t * t * (3f - 2f * t)
                    val a = peak * fall
                    if (a <= 0.002f) continue
                    drawRoundRect(
                        color = Olive.copy(alpha = a),
                        topLeft = Offset(-dist, -dist),
                        size = Size(size.width + 2f * dist, size.height + 2f * dist),
                        cornerRadius = CornerRadius(16.dp.toPx() + dist),
                        style = Stroke(1.dp.toPx())
                    )
                }
            }
            .graphicsLayer {
                val s = if (pop.isRunning) pop.value else pressScale
                scaleX = s; scaleY = s
            }
            .semantics { contentDescription = "Elite" }
            .testTag("btn_coach"),
        contentAlignment = Alignment.Center
    ) {
        // Onda ao tocar: contorno Olive 2dp, escala 1 → 1.7, opacidade 0.7 → 0
        Canvas(Modifier.fillMaxSize()) {
            if (wave.value > 0f && wave.value < 1f) {
                val sc = 1f + 0.7f * wave.value
                val bw = 2.dp.toPx() * sc
                val w = size.width * sc
                val h = size.height * sc
                drawRoundRect(
                    color = Olive.copy(alpha = 0.7f * (1f - wave.value)),
                    topLeft = Offset((size.width - w) / 2f + bw / 2f, (size.height - h) / 2f + bw / 2f),
                    size = Size(w - bw, h - bw),
                    cornerRadius = CornerRadius(16.dp.toPx() * sc - bw / 2f),
                    style = Stroke(bw)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .clickable(interactionSource = source, indication = null) {
                    if (!reduced) {
                        scope.launch {
                            launch {
                                pop.snapTo(0.88f)
                                pop.animateTo(1.12f, tween(300, easing = popEasing))
                                pop.animateTo(1f, tween(200, easing = popEasing))
                            }
                            launch { flip.snapTo(0f); flip.animateTo(360f, tween(600, easing = easeInOut)) }
                            launch { wave.snapTo(0f); wave.animateTo(1f, tween(600, easing = easeOut)) }
                        }
                    }
                    onClick()
                }
        ) {
            // Borda em degradê cônico girando: Olive → Red → Brass → Olive
            Canvas(Modifier.fillMaxSize()) {
                val c = Offset(size.width / 2f, size.height / 2f)
                rotate(if (reduced) -90f else angle - 90f, pivot = c) {
                    drawRect(
                        brush = Brush.sweepGradient(
                            listOf(Olive, Red, Brass, Olive),
                            center = c
                        ),
                        topLeft = Offset(-size.width / 2f, -size.height / 2f),
                        size = Size(size.width * 2f, size.height * 2f)
                    )
                }
            }
            // Miolo escuro: inset 2dp, raio 14, radial-gradient(circle at 30% 20%, #262B1A, #0C0E08 70%)
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .drawBehind {
                        val center = Offset(size.width * 0.3f, size.height * 0.2f)
                        val farthest = hypot(size.width - center.x, size.height - center.y)
                        drawRect(
                            Brush.radialGradient(
                                0f to Color(0xFF262B1A),
                                0.7f to Color(0xFF0C0E08),
                                1f to Color(0xFF0C0E08),
                                center = center,
                                radius = farthest
                            )
                        )
                    }
            )
            // Símbolo "E" inclinada (ic_elite_blade) 26dp, sem tint (barra vermelha fixa)
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                androidx.compose.ui.res.painterResource(id = R.drawable.ic_elite_blade)
                    .let { painter ->
                        androidx.compose.foundation.Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                            modifier = Modifier.size(26.dp)
                                .graphicsLayer {
                                    rotationY = flip.value
                                    cameraDistance = 1000f * density.density
                                }
                        )
                    }
            }
        }
    }
}
