package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/** Cores da marca. Não usar tint: a barra do meio é sempre vermelha (exceto versão monocromática). */
object EliteBrand {
    val Bone = Color(0xFFECE8DA)
    val Olive = Color(0xFFA3B565)
    val Red = Color(0xFFD12B2B)
    val Ink = Color(0xFF0A0B09)
}

// Geometria do símbolo na grade 64x64, já com a inclinação de 12 graus aplicada (ver BRAND_SPEC.md).
private val BONE_PTS = listOf(19.10f to 8.00f, 55.10f to 8.00f, 53.19f to 17.00f, 27.19f to 17.00f, 20.81f to 47.00f, 46.81f to 47.00f, 44.90f to 56.00f, 8.90f to 56.00f)
private val RED_PTS = listOf(25.06f to 27.00f, 45.06f to 27.00f, 43.15f to 36.00f, 23.15f to 36.00f)
// Caixa justa do símbolo na grade 64: x 8.9..55.1, y 8..56 (com 1 unidade de folga: 7..57)
private const val VB_X = 7f
private const val VB_Y = 7f
private const val VB_W = 50f
private const val VB_H = 50f

private fun DrawScope.poly(pts: List<Pair<Float, Float>>, color: Color, s: Float) {
    val path = Path()
    pts.forEachIndexed { i, (x, y) ->
        val px = (x - VB_X) * s; val py = (y - VB_Y) * s
        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    drawPath(path, color)
}

/** Símbolo Elite: "E" inclinado com barra do meio vermelha. Quadrado, escala pelo menor lado. */
@Composable
fun EliteMark(
    size: Dp,
    modifier: Modifier = Modifier,
    barColor: Color = EliteBrand.Bone,
    accentColor: Color = EliteBrand.Red,
) {
    Canvas(modifier.size(size)) {
        val s = minOf(this.size.width / VB_W, this.size.height / VB_H)
        poly(BONE_PTS, barColor, s)
        poly(RED_PTS, accentColor, s)
    }
}

/** Palavra "ELITE". Fonte: Saira Stencil One em res/font. */
val EliteStencilFont: FontFamily = FontFamily(Font(R.font.saira_stencil_one_regular, FontWeight.Normal))

@Composable
fun EliteWordmark(
    fontSize: TextUnit = 26.sp,
    color: Color = EliteBrand.Bone,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "ELITE",
        modifier = modifier,
        style = TextStyle(
            fontFamily = EliteStencilFont, fontWeight = FontWeight.Bold,
            fontSize = fontSize, lineHeight = fontSize, letterSpacing = 0.24.em, color = color,
        ),
    )
}
