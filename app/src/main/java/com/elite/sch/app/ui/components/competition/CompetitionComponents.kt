package com.elite.sch.app.ui.components.competition

import android.provider.Settings
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.zIndex
import com.elite.sch.app.ui.components.EliteLogo
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.competition.CompetitionDistance
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted

private val PremiumA = Color(0xFF3A2E12)
private val PremiumB = Color(0xFF1A1608)
private val PremiumGlow = Color(0x59C9A24B)
private val PremiumSheen = Color(0x38FFF4D6)
private val BrassSoft = Color(0x29C9A24B)
private val Dim = Color(0xFF5E6255)
private val Bronze = Color(0xFFB07A4A)
private val Silver = Color(0xFFB4BAB8)
private val Platinum = Color(0xFF86BDB3)
private val Diamond = Color(0xFF7FB4DE)
private val EliteLeague = Color(0xFFE7E2D0)

@Composable
fun CompetitionText(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 14,
    color: Color = EliteText,
    bold: Boolean = false,
    display: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    letterSpacing: Float = 0f
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        fontFamily = if (display) EliteDisplayFont else EliteBodyFont,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = size.sp,
        lineHeight = (size * 1.2f).sp,
        letterSpacing = letterSpacing.sp
    )
}

@Composable
fun CompetitionCard(
    modifier: Modifier = Modifier,
    border: Color = Line,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Card)
            .border(1.dp, border, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
fun CompetitionTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Card)
                .border(1.dp, Line, CircleShape).clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = EliteText) }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            eyebrow?.let { CompetitionText(it.uppercase(), size = 11, bold = true, color = TextMuted, letterSpacing = 1.2f) }
            CompetitionText(title.uppercase(), size = 29, bold = true, display = true, maxLines = 1)
        }
        trailing?.invoke()
    }
}

@Composable
fun CompetitionPill(
    text: String,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    color: Color = Olive,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(24.dp)
    Row(
        modifier = modifier.heightInMin(34.dp).clip(shape)
            .background(if (active) color.copy(alpha = .15f) else CardElevated)
            .border(1.dp, if (active) color.copy(alpha = .55f) else Line, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) { CompetitionText(text, size = 13, color = if (active) color else EliteText, bold = active, maxLines = 1) }
}

private fun Modifier.heightInMin(value: androidx.compose.ui.unit.Dp): Modifier =
    this.then(Modifier.height(value))

@Composable
fun CompetitionAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondary: Boolean = false,
    enabled: Boolean = true,
    tag: String? = null
) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = modifier.height(52.dp).clip(shape)
            .background(if (!enabled) CardElevated else if (secondary) Background else Red)
            .border(1.dp, if (secondary || !enabled) Line else Red, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .then(if (tag != null) Modifier.testTag(tag) else Modifier)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) { CompetitionText(text, size = 16, color = if (enabled && !secondary) Color.White else TextMuted, bold = true, maxLines = 2) }
}

@Composable
fun CompetitionAvatar(initials: String, modifier: Modifier = Modifier, highlighted: Boolean = false, size: Int = 42) {
    val colors = listOf(Color(0xFF3B4426), Color(0xFF4A3F1E), Color(0xFF2E3B3A), Color(0xFF46301F), Color(0xFF3A2E3F))
    val color = colors[(initials.hashCode().ushr(1) % colors.size)]
    Box(
        modifier = modifier.size(size.dp).clip(CircleShape).background(color)
            .border(if (highlighted) 2.dp else 1.dp, if (highlighted) Olive else Line, CircleShape),
        contentAlignment = Alignment.Center
    ) { CompetitionText(initials, size = (size * .38f).toInt(), display = true, bold = true) }
}

@Composable
fun LeagueBadge(league: String, division: Int = 2, modifier: Modifier = Modifier, size: Int = 22) {
    val leagueColor = when (league.lowercase()) {
        "bronze" -> Bronze
        "prata" -> Silver
        "ouro" -> Brass
        "platina" -> Platinum
        "diamante" -> Diamond
        else -> EliteLeague
    }
    androidx.compose.foundation.Canvas(modifier.size(size.dp)) {
        val w = this.size.width
        val h = this.size.height
        val shield = Path().apply {
            moveTo(w * .5f, h * .08f); lineTo(w * .9f, h * .22f); lineTo(w * .88f, h * .55f)
            cubicTo(w * .84f, h * .77f, w * .65f, h * .9f, w * .5f, h * .96f)
            cubicTo(w * .34f, h * .9f, w * .15f, h * .77f, w * .12f, h * .55f)
            lineTo(w * .1f, h * .22f); close()
        }
        drawPath(shield, leagueColor)
        if (league.lowercase() != "elite") {
            val marks = when (division) { 1 -> 3; 2 -> 2; else -> 1 }
            repeat(marks) { i ->
                val y = h * (.39f + i * .17f)
                val chevron = Path().apply {
                    moveTo(w * .29f, y); lineTo(w * .5f, y + h * .12f); lineTo(w * .71f, y)
                }
                drawPath(chevron, Background, style = Stroke(width = w * .095f, cap = StrokeCap.Round))
            }
        } else {
            drawLine(Background, androidx.compose.ui.geometry.Offset(w * .35f, h * .39f), androidx.compose.ui.geometry.Offset(w * .57f, h * .39f), w * .11f)
            drawLine(Red, androidx.compose.ui.geometry.Offset(w * .53f, h * .39f), androidx.compose.ui.geometry.Offset(w * .72f, h * .39f), w * .11f)
        }
    }
}

@Composable
fun PremiumCompetitionCard(
    isPremium: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reduceMotion = remember {
        runCatching { Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f }
            .getOrDefault(false)
    }
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale = if (pressed) .98f else 1f
    val transition = rememberInfiniteTransition(label = "competition-card-sheen")
    val sheenX by transition.animateFloat(
        initialValue = -120f,
        targetValue = 420f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3600
                -120f at 0
                420f at 1620
                420f at 3600
            },
            initialStartOffset = androidx.compose.animation.core.StartOffset(600)
        ), label = "competition-card-sheen-x"
    )
    Box(modifier = modifier.fillMaxWidth().height(112.dp)) {
        Box(
            modifier = Modifier.fillMaxSize().scale(scale).clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(PremiumA, PremiumB)))
                .border(1.5.dp, Brass, RoundedCornerShape(16.dp))
                .clickable(interactionSource = interaction, indication = null, onClick = onClick)
                .testTag("btn_menu_competicao")
        ) {
            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)).drawBehind {
                drawCircle(PremiumGlow, radius = 100.dp.toPx(), center = androidx.compose.ui.geometry.Offset(size.width - 10.dp.toPx(), 4.dp.toPx()))
            })
            if (!reduceMotion) {
                Box(Modifier.fillMaxSize().drawWithContent {
                    drawContent()
                    withTransform({ rotate(-20f, pivot = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)) }) {
                        drawRect(
                            brush = Brush.horizontalGradient(listOf(Color(0x001A1608), PremiumSheen, Color(0x001A1608))),
                            topLeft = androidx.compose.ui.geometry.Offset(sheenX.dp.toPx(), -size.height * .2f),
                            size = androidx.compose.ui.geometry.Size(70.dp.toPx(), size.height * 1.4f)
                        )
                    }
                })
            }
            Row(
                modifier = Modifier.fillMaxSize().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(Modifier.size(52.dp).clip(CircleShape).background(Brass), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFF0E1306), modifier = Modifier.size(28.dp))
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Row(Modifier.clip(RoundedCornerShape(10.dp)).background(Brass).padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WorkspacePremium, null, tint = Color(0xFF0E1306), modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp)); CompetitionText("PREMIUM", size = 10, color = Color(0xFF0E1306), bold = true, letterSpacing = .9f)
                    }
                    CompetitionText("COMPETIÇÃO ONLINE", size = 22, display = true, bold = true, maxLines = 1)
                }
                Box(Modifier.size(34.dp).clip(CircleShape).background(BrassSoft).border(1.dp, Brass, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(if (isPremium) Icons.Default.ChevronRight else Icons.Default.Lock, if (isPremium) "Abrir competição" else "Acesso Premium", tint = Brass, modifier = Modifier.size(18.dp))
                }
            }
            Row(
                Modifier.align(Alignment.BottomStart).padding(start = 14.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChipIcon(Icons.Default.Schedule, "Toda hora, 02h–21h")
                ChipIcon(Icons.Default.BarChart, "Ranking e ligas")
            }
        }
        Box(Modifier.align(Alignment.TopStart).padding(start = 16.dp).offsetY((-9).dp).zIndex(1f).clip(RoundedCornerShape(9.dp)).background(Red).padding(horizontal = 8.dp, vertical = 2.dp)) {
            CompetitionText("NOVO", size = 10, bold = true, color = Color.White, letterSpacing = 1f)
        }
    }
}

@Composable
private fun ChipIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(Modifier.clip(RoundedCornerShape(11.dp)).background(BrassSoft).padding(horizontal = 9.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, tint = EliteText, modifier = Modifier.size(11.dp))
        CompetitionText(label, size = 10, bold = true, maxLines = 1)
    }
}

private fun Modifier.offsetY(value: androidx.compose.ui.unit.Dp): Modifier =
    this.then(Modifier.offset(y = value))

@Composable
fun CompetitionShareIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(Olive.copy(alpha = .12f))
            .border(1.dp, OliveDark, CircleShape).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Icon(Icons.Default.Share, contentDescription = "Compartilhar convite", tint = Olive, modifier = Modifier.size(22.dp)) }
}

@Composable
fun CompetitionWatchIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(Olive.copy(alpha = .12f))
            .border(1.dp, OliveDark, CircleShape).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Icon(Icons.Default.Visibility, contentDescription = "Assistir ao vivo", tint = Olive, modifier = Modifier.size(22.dp)) }
}

@Composable
fun CompetitionBrand(modifier: Modifier = Modifier) {
    EliteLogo(modifier = modifier, symbolSize = 34.dp, showTagline = false)
}

@Composable
fun SectionHeading(text: String, modifier: Modifier = Modifier, trailing: String? = null) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        CompetitionText(text.uppercase(), size = 20, display = true, bold = true)
        trailing?.let { CompetitionText(it, color = TextMuted, size = 13) }
    }
}

@Composable
fun RankingPersonRow(
    position: Int,
    initials: String,
    name: String,
    league: String,
    points: String,
    detail: String? = null,
    currentUser: Boolean = false,
    movement: String = "—",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(14.dp))
            .background(if (currentUser) Olive.copy(alpha = .12f) else Card)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        CompetitionText(if (position > 0) position.toString() else "—", size = 19, display = true, bold = true, color = if (currentUser) Olive else TextMuted, modifier = Modifier.width(24.dp))
        CompetitionAvatar(initials, size = 36, highlighted = currentUser)
        Column(Modifier.weight(1f)) {
            CompetitionText(name, size = 14, bold = true, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                LeagueBadge(league, modifier = Modifier.size(16.dp), size = 16)
                Spacer(Modifier.width(4.dp))
                CompetitionText(detail ?: "${league} · ${points} pts", size = 11, color = TextMuted, maxLines = 1)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            CompetitionText(points, size = 19, display = true, bold = true)
            CompetitionText(movement, size = 11, color = if (movement.startsWith("▼")) Red else Olive)
        }
    }
}
