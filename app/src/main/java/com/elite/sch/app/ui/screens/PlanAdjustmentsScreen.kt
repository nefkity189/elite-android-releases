package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.data.local.entities.AutoAdjustmentEntity
import com.elite.sch.app.generator.AdaptiveEngine
import com.elite.sch.app.ui.AdjustmentPresentation
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Amber
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteTextStyles
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted
import java.time.LocalDate

@Composable
fun PlanAdjustmentsScreen(
    ajustesAutomaticos: Boolean,
    modoAjuste: String,
    ajustes: List<AutoAdjustmentEntity>,
    onToggleAutomaticos: (Boolean) -> Unit,
    onModoChange: (String) -> Unit,
    onUndo: (Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    val hoje = remember { LocalDate.now() }
    val views = remember(ajustes) { ajustes.map { AdjustmentPresentation.toView(it, hoje) } }
    var showModeOptions by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .testTag("ajustes_screen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = EliteSpacing.screenHorizontal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .background(CardColor, CircleShape)
                    .border(1.dp, Line, CircleShape)
                    .testTag("ajustes_back")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = TextColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = "AJUSTES DO PLANO",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = EliteDisplayFont,
                color = TextColor
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = EliteSpacing.screenHorizontal,
                    end = EliteSpacing.screenHorizontal,
                    top = EliteSpacing.md,
                    bottom = EliteSpacing.md
                ),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardColor),
                border = BorderStroke(1.dp, Line)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("switch_ajustes_automaticos"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Ajustes automáticos",
                                fontFamily = EliteBodyFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                lineHeight = 17.sp,
                                color = TextColor
                            )
                            Text(
                                text = "Usa seu esforço, dor e check-ins",
                                fontFamily = EliteBodyFont,
                                fontSize = 12.sp,
                                lineHeight = 16.8.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = ajustesAutomaticos,
                            onCheckedChange = onToggleAutomaticos,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextColor,
                                checkedTrackColor = OliveDark,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CardColor,
                                uncheckedBorderColor = LineStrong
                            ),
                            modifier = Modifier.testTag("switch_ajustes_toggle")
                        )
                    }

                    HorizontalDivider(color = Line)

                    Text(
                        text = if (modoAjuste == AdaptiveEngine.MODO_EQUILIBRADO) {
                            "Equilibrado: aplica sozinho os avanços de etapa quando seus treinos estão tranquilos."
                        } else {
                            "Cauteloso: só sugere avanços de etapa. Você decide com um toque."
                        },
                        fontFamily = EliteBodyFont,
                        fontSize = 12.sp,
                        lineHeight = 16.8.sp,
                        color = TextMuted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showModeOptions = !showModeOptions }
                            .testTag("ajuste_mode_summary")
                    )

                    if (showModeOptions) {
                        Row(horizontalArrangement = Arrangement.spacedBy(EliteSpacing.sm)) {
                            ModeOption(
                                text = "Cauteloso",
                                selected = modoAjuste == AdaptiveEngine.MODO_CAUTELOSO,
                                modifier = Modifier.weight(1f).testTag("modo_cauteloso"),
                                onClick = { onModoChange(AdaptiveEngine.MODO_CAUTELOSO) }
                            )
                            ModeOption(
                                text = "Equilibrado",
                                selected = modoAjuste == AdaptiveEngine.MODO_EQUILIBRADO,
                                modifier = Modifier.weight(1f).testTag("modo_equilibrado"),
                                onClick = { onModoChange(AdaptiveEngine.MODO_EQUILIBRADO) }
                            )
                        }
                    }
                }
            }

            Text(
                text = "HISTÓRICO",
                style = EliteTextStyles.Label,
                color = TextMuted,
                modifier = Modifier.testTag("ajustes_history_title")
            )

            if (views.isEmpty()) {
                EmptyAdjustmentsCard()
            } else {
                views.forEachIndexed { index, view ->
                    AdjustmentHistoryCard(
                        view = view,
                        onUndo = { onUndo(view.id) },
                        modifier = Modifier.testTag("ajuste_item_${view.id}")
                    )
                    if (index < views.lastIndex) {
                        Spacer(Modifier.height(EliteSpacing.xs))
                    }
                }
            }
        }
    }
}

@Composable
private fun ModeOption(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .background(if (selected) OliveSoft else CardColor, RoundedCornerShape(14.dp))
            .border(1.dp, if (selected) OliveDark else Line, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = EliteBodyFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = if (selected) Olive else TextMuted
        )
    }
}

@Composable
private fun EmptyAdjustmentsCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .drawBehind {
                drawRoundRect(
                    color = Line,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx()))
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx())
                )
            }
            .padding(horizontal = 14.dp)
            .testTag("ajustes_vazio"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = "Nenhum ajuste ainda. Quando o Elite mudar um treino, ele aparece aqui.",
            fontFamily = EliteBodyFont,
            fontSize = 12.sp,
            lineHeight = 16.8.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun AdjustmentHistoryCard(
    view: com.elite.sch.app.ui.AdjustmentView,
    onUndo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        border = BorderStroke(1.dp, Line)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Brass.copy(alpha = 0.16f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Tune, contentDescription = null, tint = Brass, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = view.titulo,
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = TextColor
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${view.dataLabel} · ${view.motivo}",
                    fontFamily = EliteBodyFont,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = TextMuted
                )
                if (view.antes != null && view.depois != null && view.antes != view.depois) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${view.antes} → ${view.depois}",
                        fontFamily = EliteBodyFont,
                        fontSize = 12.sp,
                        color = TextColor
                    )
                }
            }
            if (view.ativo) {
                Text(
                    text = "Desfazer",
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Olive,
                    modifier = Modifier
                        .clickable(onClick = onUndo)
                        .testTag("btn_desfazer_ajuste")
                )
            } else {
                Text(
                    text = "Desfeito",
                    fontFamily = EliteBodyFont,
                    fontSize = 11.sp,
                    color = Amber
                )
            }
        }
    }
}
