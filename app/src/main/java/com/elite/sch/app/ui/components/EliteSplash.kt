package com.elite.sch.app.ui.components

import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import com.elite.sch.app.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** Cores da marca. Não usar tint: a barra do meio é sempre vermelha (exceto versão monocromática). */
object EliteBrand {
    val Bone = Color(0xFFECE8DA)
    val Olive = Color(0xFFA3B565)
    val Red = Color(0xFFD12B2B)
    val Ink = Color(0xFF0A0B09)
}

/** Palavra "ELITE". Fonte: Saira Stencil One em res/font. */
val EliteStencilFont: FontFamily = FontFamily(Font(R.font.saira_stencil_one_regular, FontWeight.Normal))

// Linha do tempo em ms (igual ao protótipo html/elite-splash.html)
private const val T_HOLD = 2800f      // fim da entrada; a saída começa aqui quando o app estiver pronto
private const val T_END = 3100f       // fim da saída
private val Bezier = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)
private const val SKEW = -0.21256f    // tan(-12 graus)
private val FarTone = Color(0xFF6F735E)
private val FarShoe = Color(0xFF8E1F1F)
private const val BAR_W = 260         // largura da barra de carregamento em dp

private fun seg(t: Float, start: Float, dur: Float, ease: Easing = LinearEasing): Float =
    ease.transform(((t - start) / dur).coerceIn(0f, 1f))

private fun lerpF(a: Float, b: Float, p: Float) = a + (b - a) * p

/** Progresso simulado 0..100 entre 200 e 2500 ms, com ritmo irregular (28% em 0,5 s, 58% em 1,15 s, 86% em 1,8 s). */
private fun loadPct(tv: Float): Float {
    val x = ((tv - 200f) / 2300f).coerceIn(0f, 1f)
    val xs = floatArrayOf(0f, 0.22f, 0.5f, 0.78f, 1f); val ys = floatArrayOf(0f, 28f, 58f, 86f, 100f)
    for (i in 1 until xs.size) if (x <= xs[i]) return lerpF(ys[i - 1], ys[i], (x - xs[i - 1]) / (xs[i] - xs[i - 1]))
    return 100f
}

/** Splash animada. [isReady] devolve true quando o app já pode aparecer (a animação espera até 2,8 s no mínimo). */
@Composable
fun EliteSplash(isReady: () -> Boolean = { true }, onFinished: () -> Unit) {
    val ctx = LocalContext.current
    val reduce = remember {
        Settings.Global.getFloat(ctx.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }
    val t = remember { Animatable(if (reduce) T_HOLD else 0f) }
    val cycle by rememberInfiniteTransition(label = "run").animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing)), label = "cycle",
    )
    LaunchedEffect(Unit) {
        if (reduce) delay(600) else t.animateTo(T_HOLD, tween(T_HOLD.toInt(), easing = LinearEasing))
        while (!isReady()) delay(16)
        if (reduce) onFinished() else { t.animateTo(T_END, tween(300, easing = LinearEasing)); onFinished() }
    }
    val tv = t.value
    val exit = seg(tv, T_HOLD, 300f, FastOutLinearInEasing)
    val wm = seg(tv, 900f, 700f, Bezier)
    val tg = seg(tv, 1350f, 500f, FastOutSlowInEasing)
    val glow = seg(tv, 0f, 900f, FastOutSlowInEasing)
    val breath = if (tv > 1200f) (sin(2.0 * PI * (tv - 1200f) / 3200f).toFloat() * 0.5f + 0.5f) else 0f
    val pct = loadPct(tv)

    Box(Modifier.fillMaxSize().background(EliteBrand.Ink)) {
        Box(Modifier.fillMaxSize().alpha(1f - exit).scale(1f + 0.04f * exit)) {
            // brilho oliva atrás do logo (centro da tela)
            Canvas(Modifier.fillMaxSize()) {
                val c = Offset(size.width / 2f, size.height / 2f)
                val a = (glow * (1f + 0.5f * breath)).coerceAtMost(1.5f)
                drawCircle(
                    Brush.radialGradient(listOf(EliteBrand.Olive.copy(alpha = (0.18f * a).coerceAtMost(0.27f)), Color.Transparent), c, 240.dp.toPx()),
                    radius = 240.dp.toPx(), center = c,
                )
            }
            // logo centralizado na tela
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                SplashMark(tv)
                Spacer(Modifier.height(22.dp))
                Text(
                    "ELITE", modifier = Modifier.alpha(wm),
                    style = TextStyle(
                        fontFamily = EliteStencilFont, fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 40.sp,
                        letterSpacing = lerpF(0.6f, 0.24f, wm).em, color = EliteBrand.Bone,
                    ),
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    "MISSÃO DADA É MISSÃO CUMPRIDA", modifier = Modifier.alpha(tg).padding(top = ((1f - tg) * 6f).dp),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 9.sp,
                        letterSpacing = 0.2.em, color = Color(0xFF8F9384),
                    ),
                )
            }
            // barra de carregamento com o corredor em cima (0 a 100%)
            val fadeIn = seg(tv, 200f, 300f)
            Column(
                Modifier.align(Alignment.BottomCenter).padding(bottom = 84.dp).width(BAR_W.dp).alpha(fadeIn),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.fillMaxWidth().height(40.dp)) {
                    Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(2.dp).background(Color(0xFF1D2116)))
                    Box(Modifier.align(Alignment.BottomStart).fillMaxWidth(pct / 100f).height(2.dp).background(EliteBrand.Olive))
                    SmallRunner(
                        cycle = if (reduce) -1f else cycle,
                        modifier = Modifier.align(Alignment.BottomStart).offset(x = (BAR_W * pct / 100f - 16f).dp),
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "${pct.toInt()}%",
                    style = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, letterSpacing = 0.1.em, color = Color(0xFF8F9384)),
                )
            }
        }
    }
}

// ---------------- Corredor pequeno (30x40 dp; figura articulada na grade 200x150, recorte 40,10 a 140,145) ----------------
private fun rot(vx: Float, vy: Float, deg: Float): Offset {
    val r = Math.toRadians(deg.toDouble()); val c = cos(r).toFloat(); val s = sin(r).toFloat()
    return Offset(vx * c - vy * s, vx * s + vy * c) // positivo = horário na tela (igual ao CSS)
}
/** Interpola 4 valores em 0, 25, 50 e 75% do ciclo (voltando ao primeiro), com ease-in-out entre eles. */
private fun kf4(p: Float, v: FloatArray): Float {
    val x = (p % 1f) * 4f; val i = x.toInt() % 4; val f = x - x.toInt(); val e = f * f * (3f - 2f * f)
    return lerpF(v[i], v[(i + 1) % 4], e)
}
/** Vai e volta suave entre a (p=0) e b (p=0,5). */
private fun swing(p: Float, a: Float, b: Float) = lerpF(a, b, (1f - cos(2.0 * PI * p).toFloat()) / 2f)

private val THIGH = floatArrayOf(-50f, -10f, 35f, -5f)
private val SHIN = floatArrayOf(20f, 15f, 40f, 110f)


@Composable
private fun SmallRunner(cycle: Float, modifier: Modifier = Modifier) {
    Canvas(modifier.size(30.dp, 40.dp)) {
        val s = size.height / 135f
        fun P(x: Float, y: Float) = Offset((x - 40f) * s, (y - 10f) * s)
        fun line(c: Color, a: Offset, b: Offset, w: Float) = drawLine(c, P(a.x, a.y), P(b.x, b.y), w * s, StrokeCap.Round)
        val still = cycle < 0f
        val pA = if (still) 0f else cycle
        val pB = (pA + 0.5f) % 1f
        val thA = if (still) -48f else kf4(pA, THIGH); val shA = if (still) 22f else kf4(pA, SHIN)
        val thB = if (still) 32f else kf4(pB, THIGH); val shB = if (still) 55f else kf4(pB, SHIN)
        val arA = if (still) 40f else swing(pA, 40f, -45f); val frA = if (still) -62f else swing(pA, -60f, -90f)
        val arB = if (still) -45f else swing(pB, 40f, -45f); val frB = if (still) -88f else swing(pB, -60f, -90f)
        val dy = if (still) 0f else -3f * (1f - cos(2.0 * PI * pA * 2.0).toFloat()) / 2f
        fun T(o: Offset) = Offset(o.x, o.y + dy)
        val hip = Offset(94f, 72f); val sho = Offset(112f, 38f)
        fun leg(th: Float, sh: Float, body: Color, shoe: Color, wTh: Float, wSh: Float, wShoe: Float) {
            val knee = hip + rot(0f, 32f, th); val ank = knee + rot(0f, 30f, th + sh); val toe = ank + rot(11f, 2f, th + sh)
            line(body, T(hip), T(knee), wTh); line(body, T(knee), T(ank), wSh); line(shoe, T(ank), T(toe), wShoe)
        }
        fun arm(a: Float, f: Float, body: Color, w1: Float, w2: Float) {
            val el = sho + rot(0f, 22f, a); val wr = el + rot(0f, 20f, a + f)
            line(body, T(sho), T(el), w1); line(body, T(el), T(wr), w2)
        }
        arm(arB, frB, FarTone, 9f, 7.5f)
        leg(thB, shB, FarTone, FarShoe, 13f, 9.5f, 7f)
        line(EliteBrand.Bone, T(hip), T(Offset(103f, 55f)), 14f); line(EliteBrand.Bone, T(Offset(103f, 55f)), T(sho), 18f)
        drawCircle(EliteBrand.Bone, 10f * s, P(121f, 23f + dy))
        leg(thA, shA, EliteBrand.Bone, EliteBrand.Red, 14.5f, 10.5f, 8f)
        arm(arA, frA, EliteBrand.Bone, 10f, 8.5f)
    }
}

@Composable
private fun SplashMark(tv: Float) {
    val spine = seg(tv, 200f, 450f, Bezier)
    val top = seg(tv, 350f, 400f, Bezier)
    val bot = seg(tv, 450f, 400f, Bezier)
    val redP = seg(tv, 750f, 450f)
    val redScale = if (redP < 0.7f) 1.12f * FastOutSlowInEasing.transform(redP / 0.7f) else 1.12f - 0.12f * ((redP - 0.7f) / 0.3f)
    val redColor = lerp(Color(0xFFFF7A6E), EliteBrand.Red, seg(tv, 750f, 600f))
    Canvas(Modifier.size(104.dp)) {
        val s = size.width / 64f
        fun DrawScope.poly(pts: List<Pair<Float, Float>>, c: Color, alpha: Float = 1f) {
            val path = Path()
            pts.forEachIndexed { i, (x, y) ->
                val px = (x + 6.8f + SKEW * y) * s; val py = y * s
                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            path.close(); drawPath(path, c, alpha)
        }
        val yEnd = 8f + 48f * spine
        poly(listOf(14f to 8f, 24.2f to 8f, 24.2f to yEnd, 14f to yEnd), EliteBrand.Bone)
        val dx = -18f * (1f - top)
        poly(listOf(23.6f + dx to 8f, 50f + dx to 8f, 50f + dx to 17f, 23.6f + dx to 17f), EliteBrand.Bone, top)
        val dxb = -18f * (1f - bot)
        poly(listOf(23.6f + dxb to 47f, 50f + dxb to 47f, 50f + dxb to 56f, 23.6f + dxb to 56f), EliteBrand.Bone, bot)
        val rx = 23.6f + (44f - 23.6f) * redScale
        poly(listOf(23.6f to 27f, rx to 27f, rx to 36f, 23.6f to 36f), redColor)
    }
}