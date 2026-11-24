package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.generator.AdaptiveEngine
import com.example.ui.AdjustmentPresentation
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleSelectableCard
import com.example.ui.components.AisleSwitchRow
import com.example.ui.components.AisleTopBar
import java.time.LocalDate
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.Amber
import com.example.ui.theme.Background
import com.example.ui.theme.LineStrong
import com.example.ui.theme.Olive
import com.example.ui.theme.Text as TextColor
import com.example.ui.theme.TextMuted

/** Ajustes do plano: liga/desliga, modo (cauteloso ou equilibrado) e histórico com Desfazer. */
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .padding(horizontal = AisleSpacing.screenHorizontal)
            .testTag("ajustes_screen")
    ) {
        AisleTopBar(title = "Ajustes do plano", onBack = onBack, backTestTag = "ajustes_back")

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
        ) {
            AisleCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(AisleSpacing.lg), verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
                    AisleSwitchRow(
                        title = "Ajustes automáticos",
                        description = "Usa seu esforço, dor e check-ins",
                        checked = ajustesAutomaticos,
                        onCheckedChange = onToggleAutomaticos,
                        modifier = Modifier.testTag("switch_ajustes_automaticos")
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
                        AisleSelectableCard(
                            text = "Cauteloso",
                            selected = modoAjuste == AdaptiveEngine.MODO_CAUTELOSO,
                            onClick = { onModoChange(AdaptiveEngine.MODO_CAUTELOSO) },
                            modifier = Modifier.weight(1f).testTag("modo_cauteloso")
                        )
                        AisleSelectableCard(
                            text = "Equilibrado",
                            selected = modoAjuste == AdaptiveEngine.MODO_EQUILIBRADO,
                            onClick = { onModoChange(AdaptiveEngine.MODO_EQUILIBRADO) },
                            modifier = Modifier.weight(1f).testTag("modo_equilibrado")
                        )
                    }
                    Text(
                        text = if (modoAjuste == AdaptiveEngine.MODO_EQUILIBRADO) {
                            "Equilibrado: aplica sozinho os avanços de etapa quando seus treinos estão tranquilos."
                        } else {
                            "Cauteloso: só sugere avanços de etapa. Você decide com um toque."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }

            Text(
                text = "Histórico",
                style = MaterialTheme.typography.titleLarge,
                color = TextColor
            )

            if (views.isEmpty()) {
                Text(
                    text = "Nenhum ajuste ainda. Quando o Aisle mudar um treino, ele aparece aqui.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    modifier = Modifier.testTag("ajustes_vazio")
                )
            } else {
                views.forEachIndexed { index, v ->
                    Column(modifier = Modifier.fillMaxWidth().testTag("ajuste_item_${v.id}")) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = v.dataLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                            Text(
                                text = if (v.ativo) "Ativo" else "Desfeito",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (v.ativo) Olive else Amber
                            )
                        }
                        Spacer(Modifier.height(AisleSpacing.xs))
                        Text(
                            text = v.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextColor
                        )
                        Spacer(Modifier.height(AisleSpacing.xs))
                        Text(
                            text = v.motivo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                        if (v.antes != null && v.depois != null && v.antes != v.depois) {
                            Spacer(Modifier.height(AisleSpacing.xs))
                            Text(
                                text = "${v.antes}  →  ${v.depois}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextColor
                            )
                        }
                        if (v.ativo) {
                            AisleButton(
                                text = "Desfazer",
                                onClick = { onUndo(v.id) },
                                variant = AisleButtonVariant.Text,
                                modifier = Modifier.testTag("btn_desfazer_ajuste")
                            )
                        }
                        if (index < views.lastIndex) {
                            Spacer(Modifier.height(AisleSpacing.sm))
                            HorizontalDivider(color = LineStrong)
                        }
                    }
                }
            }

            Text(
                text = "Sugestões do app, não aconselhamento médico. Os ajustes nunca mexem em treinos já concluídos.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
            Spacer(Modifier.height(AisleSpacing.lg))
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}
