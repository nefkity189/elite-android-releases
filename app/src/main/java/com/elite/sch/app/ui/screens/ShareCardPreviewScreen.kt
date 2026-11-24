package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.R
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.tracker.RouteNormalizer
import com.elite.sch.app.tracker.RoutePointCoord
import com.elite.sch.app.ui.components.ShareCardData
import com.elite.sch.app.ui.components.ShareCardGenerator
import com.elite.sch.app.ui.components.WorkoutFormatters
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.EliteTextStyles
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted

@Composable
fun ShareCardPreviewScreen(
    data: ShareCardData,
    isSharing: Boolean,
    onShare: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("share_card_preview_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "COMPARTILHAR NO ELITE",
                style = EliteTextStyles.Label,
                color = TextMuted
            )
            Spacer(Modifier.height(16.dp))
            ShareCardPreview(data = data)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onShare,
                enabled = !isSharing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("btn_share_card_preview"),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = Color(0xFFFFF4F0)
                )
            ) {
                Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(10.dp))
                Text(
                    text = if (isSharing) "Preparando cartão..." else "Compartilhar",
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            if (isSharing) {
                Spacer(Modifier.height(10.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Olive,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Composable
private fun ShareCardPreview(data: ShareCardData) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(359f / 449f)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1E2314), Background)
                )
            )
            .border(1.dp, OliveDark, RoundedCornerShape(22.dp))
            .testTag("share_card_preview")
    ) {
        val scale = (maxWidth / 359.dp).coerceIn(0.72f, 1.05f)
        val distanceFormatted = WorkoutFormatters.formatDistance(data.distanceMeters)
        val distanceParts = distanceFormatted.split(" ", limit = 2)
        val distanceValue = distanceParts.firstOrNull().orEmpty()
        val distanceUnit = distanceParts.getOrNull(1).orEmpty()
        val time = WorkoutFormatters.formatTime(data.elapsedSeconds)
        val pace = WorkoutFormatters.formatPace(data.distanceMeters, data.elapsedSeconds)
        val routePoints = remember(data.routePoints) {
            ShareCardGenerator.filterRoutePoints(data.routePoints)
                .map { RoutePointCoord(it.latitude, it.longitude) }
                .let { RouteNormalizer.simplify(it) }
        }
        val normalizedRoute = remember(routePoints) {
            if (routePoints.size >= 2) RouteNormalizer.normalize(routePoints) else emptyList()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp * scale),
            verticalArrangement = Arrangement.spacedBy(12.dp * scale)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp * scale),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp * scale)) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(R.drawable.ic_elite_mark),
                        contentDescription = null,
                        modifier = Modifier.size(width = 26.dp * scale, height = 30.dp * scale)
                    )
                    Text(
                        text = "ELITE",
                        fontFamily = EliteLogoFont,
                        fontSize = 18.sp * scale,
                        letterSpacing = 4.32.sp * scale,
                        color = TextColor
                    )
                }
                Box(
                    modifier = Modifier
                        .background(OliveSoft, RoundedCornerShape(6.dp * scale))
                        .padding(horizontal = 8.dp * scale, vertical = 3.dp * scale)
                ) {
                    Text(
                        text = "TREINO CONCLUÍDO",
                        fontFamily = EliteBodyFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp * scale,
                        letterSpacing = 1.sp * scale,
                        color = Olive
                    )
                }
            }

            RoutePreview(
                points = normalizedRoute,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp * scale)) {
                Text(
                    text = "DISTÂNCIA",
                    style = EliteTextStyles.Label.copy(
                        fontSize = 11.sp * scale,
                        lineHeight = 14.sp * scale
                    ),
                    color = TextMuted
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = distanceValue,
                        fontFamily = EliteDisplayFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 72.sp * scale,
                        lineHeight = 68.sp * scale,
                        color = TextColor
                    )
                    Text(
                        text = distanceUnit,
                        fontFamily = EliteDisplayFont,
                        fontSize = 24.sp * scale,
                        lineHeight = 26.sp * scale,
                        color = TextMuted,
                        modifier = Modifier.padding(start = 4.dp * scale, bottom = 8.dp * scale)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp * scale)
            ) {
                ShareMetric(
                    label = "TEMPO",
                    value = time,
                    modifier = Modifier.weight(1f),
                    scale = scale
                )
                ShareMetric(
                    label = "RITMO",
                    value = pace,
                    modifier = Modifier.weight(1f),
                    scale = scale
                )
            }

            Spacer(Modifier.height(5.dp * scale))
            Text(
                text = "ELITE · MISSÃO DADA É MISSÃO CUMPRIDA",
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp * scale,
                letterSpacing = 1.8.sp * scale,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RoutePreview(
    points: List<com.elite.sch.app.tracker.NormalizedPoint>,
    modifier: Modifier = Modifier
) {
    if (points.size < 2) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sem rota GPS neste treino",
                fontFamily = EliteBodyFont,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
        return
    }

    Canvas(modifier = modifier.testTag("share_preview_route")) {
        val route = points.map { point ->
            androidx.compose.ui.geometry.Offset(point.x * size.width, point.y * size.height)
        }
        val path = Path().apply {
            route.forEachIndexed { index, point ->
                if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
            }
        }
        drawPath(
            path,
            color = Color(0xFF1C2015),
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawPath(
            path,
            color = Olive,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        val start = route.first()
        val finish = route.last()
        drawCircle(Olive, radius = 6.dp.toPx(), center = start)
        drawCircle(Red, radius = 6.dp.toPx(), center = finish)
    }
}

@Composable
private fun ShareMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    scale: Float
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = EliteTextStyles.Label.copy(
                fontSize = 11.sp * scale,
                lineHeight = 14.sp * scale
            ),
            color = TextMuted
        )
        Text(
            text = value,
            fontFamily = EliteDisplayFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp * scale,
            lineHeight = 42.sp * scale,
            color = TextColor,
            maxLines = 1
        )
    }
}
