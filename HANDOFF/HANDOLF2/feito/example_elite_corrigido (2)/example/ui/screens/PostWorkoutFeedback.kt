package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.AdjustmentPresentation
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleChoiceChip
import com.example.ui.theme.AisleNumberStyles
import com.example.ui.theme.Background
import com.example.ui.theme.Card
import com.example.ui.theme.Line
import com.example.ui.theme.LineStrong
import com.example.ui.theme.Text
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextDim
import com.example.ui.theme.Olive
import com.example.ui.theme.OliveDark
import com.example.ui.theme.OliveSoft
import com.example.ui.theme.OnOlive
import com.example.ui.theme.Red
import com.example.ui.theme.OnRed
import com.example.ui.theme.Brass
import com.example.ui.theme.Amber
import com.example.ui.theme.Ok
import com.example.ui.theme.EliteBodyFont
import com.example.ui.theme.EliteDisplayFont
import com.example.ui.theme.EliteLogoFont
import com.example.ui.theme.EliteNumberStyles
import com.example.ui.theme.EliteTextStyles
import com.example.ui.theme.Card as CardColor
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.RedSoft
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.CardElevated
import com.example.ui.theme.Text as TextColor
import com.example.ui.theme.AisleShape


/** Os 5 níveis mostrados na tela; gravam 2, 4, 6, 8 e 10 (o intervalo antigo de 1 a 10 continua válido). */
private val EFFORT_LABELS = listOf("Muito fácil", "Fácil", "Moderado", "Difícil", "No limite")

private val PAIN_REGIONS = listOf(
    "panturrilha" to "Panturrilha",
    "canela" to "Canela",
    "joelho" to "Joelho",
    "quadril" to "Quadril",
    "pe" to "Pé",
    "outro" to "Outro"
)

/** Nível de 1 a 5 a partir do valor gravado (1 a 10). */
fun effortLevelFromStored(stored: Int): Int = ((stored + 1) / 2).coerceIn(1, 5)

/**
 * Pós-treino: esforço em 5 níveis, "Sentiu dor?", "Onde?" e a prévia do próximo passo.
 * [esforcoGravado] é o valor 1 a 10 que será salvo; [onEsforcoChange] recebe 2, 4, 6, 8 ou 10.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostWorkoutFeedback(
    esforcoGravado: Int,
    onEsforcoChange: (Int) -> Unit,
    dor: Int,
    onDorChange: (Int) -> Unit,
    regiaoDor: String?,
    onRegiaoChange: (String?) -> Unit,
    ajustesAutomaticos: Boolean,
    modifier: Modifier = Modifier
) {
    val nivel = effortLevelFromStored(esforcoGravado)

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
        // ESFORÇO
        Text(
            text = "Esforço",
            style = MaterialTheme.typography.titleMedium,
            color = TextColor
        )
        Row(
            modifier = Modifier.fillMaxWidth().testTag("pos_esforco"),
            horizontalArrangement = Arrangement.spacedBy(AisleSpacing.xs)
        ) {
            EFFORT_LABELS.forEachIndexed { index, label ->
                val level = index + 1
                val selected = level == nivel
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 76.dp)
                        .clip(AisleShape.md)
                        .background(if (selected) Olive else CardElevated)
                        .border(
                            width = if (selected) 0.dp else 1.dp,
                            color = LineStrong,
                            shape = AisleShape.md
                        )
                        .selectable(selected = selected, role = Role.RadioButton) { onEsforcoChange(level * 2) }
                        .padding(vertical = AisleSpacing.sm, horizontal = AisleSpacing.xxs)
                        .testTag("rpe_$level"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$level",
                        style = AisleNumberStyles.Metric,
                        color = if (selected) OnOlive else TextColor,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = if (selected) OnOlive else TextMuted,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }

        // DOR
        Text(
            text = "Sentiu dor?",
            style = MaterialTheme.typography.titleMedium,
            color = TextColor
        )
        Row(
            modifier = Modifier.fillMaxWidth().testTag("pos_dor"),
            horizontalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
        ) {
            listOf(0 to "Nenhuma", 1 to "Leve", 2 to "Forte").forEach { (value, label) ->
                AisleChoiceChip(
                    text = label,
                    selected = dor == value,
                    onClick = {
                        onDorChange(value)
                        if (value == 0) onRegiaoChange(null)
                    },
                    description = "Dor: $label",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (dor > 0) {
            Text(
                text = "Onde?",
                style = MaterialTheme.typography.titleMedium,
                color = TextColor
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth().testTag("pos_regiao"),
                horizontalArrangement = Arrangement.spacedBy(AisleSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
            ) {
                PAIN_REGIONS.forEach { (key, label) ->
                    AisleChoiceChip(
                        text = label,
                        selected = regiaoDor == key,
                        onClick = { onRegiaoChange(key) },
                        description = "Região da dor: $label"
                    )
                }
            }
        }

        // PRÓXIMO PASSO (prévia ao vivo, anunciada pelo leitor de tela)
        AisleCard(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite }
                .testTag("card_proximo_passo")
        ) {
            Row(modifier = Modifier.padding(AisleSpacing.md), verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Filled.Autorenew,
                    contentDescription = null,
                    tint = Olive,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(Modifier.padding(start = AisleSpacing.md))
                Column {
                    Text(
                        text = "Próximo passo",
                        style = MaterialTheme.typography.labelLarge,
                        color = Olive
                    )
                    Spacer(Modifier.height(AisleSpacing.xs))
                    Text(
                        text = AdjustmentPresentation.previa(esforcoGravado, dor, ajustesAutomaticos),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor
                    )
                }
            }
        }

        Text(
            text = "Dor forte? Pare e procure um profissional de saúde. O Aisle não faz diagnóstico.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}
