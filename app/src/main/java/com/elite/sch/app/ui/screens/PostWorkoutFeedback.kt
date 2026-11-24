package com.elite.sch.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Shield
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
import com.elite.sch.app.ui.AdjustmentPresentation
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteChoiceChip
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.TextDim
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.OnRed
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Amber
import com.elite.sch.app.ui.theme.Ok
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.EliteTextStyles
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.RedSoft
import androidx.compose.foundation.shape.RoundedCornerShape
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteShape


/** Os 5 níveis mostrados na tela; gravam 2, 4, 6, 8 e 10 (o intervalo antigo de 1 a 10 continua válido). */
private val EFFORT_LABELS = listOf("Muito fácil", "Fácil", "Moderado", "Difícil", "No limite")

private val PAIN_REGIONS = listOf(
    "joelho" to "Joelho",
    "canela" to "Canela",
    "tornozelo" to "Tornozelo",
    "quadril" to "Quadril",
    "panturrilha" to "Panturrilha"
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

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
        // ESFORÇO
        Text(
            text = "Esforço",
            style = MaterialTheme.typography.titleMedium,
            color = TextColor
        )
        Row(
            modifier = Modifier.fillMaxWidth().testTag("pos_esforco"),
            horizontalArrangement = Arrangement.spacedBy(EliteSpacing.xs)
        ) {
            EFFORT_LABELS.forEachIndexed { index, label ->
                val level = index + 1
                val selected = level == nivel
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 76.dp)
                        .clip(EliteShape.md)
                        .background(if (selected) Olive else CardElevated)
                        .border(
                            width = if (selected) 0.dp else 1.dp,
                            color = LineStrong,
                            shape = EliteShape.md
                        )
                        .selectable(selected = selected, role = Role.RadioButton) { onEsforcoChange(level * 2) }
                        .padding(vertical = EliteSpacing.sm, horizontal = EliteSpacing.xxs)
                        .testTag("rpe_$level"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$level",
                        style = EliteNumberStyles.Metric,
                        color = if (selected) OnOlive else TextColor,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = if (selected) OnOlive else TextMuted,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 2
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
            horizontalArrangement = Arrangement.spacedBy(EliteSpacing.sm)
        ) {
                    listOf(0 to "Não", 1 to "Sim").forEach { (value, label) ->
                EliteChoiceChip(
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
                horizontalArrangement = Arrangement.spacedBy(EliteSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(EliteSpacing.sm)
            ) {
                PAIN_REGIONS.forEach { (key, label) ->
                    EliteChoiceChip(
                        text = label,
                        selected = regiaoDor == key,
                        onClick = { onRegiaoChange(key) },
                        description = "Região da dor: $label"
                    )
                }
            }
        }

        // PRÓXIMO PASSO (prévia ao vivo, anunciada pelo leitor de tela) - with olive left border
        EliteCard(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite }
                        .testTag("card_proximo_passo"),
                    selected = true
        ) {
            Row(modifier = Modifier.padding(EliteSpacing.md), verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Filled.Autorenew,
                    contentDescription = null,
                    tint = Olive,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(Modifier.padding(start = EliteSpacing.md))
                Column {
                    Text(
                        text = "Próximo passo",
                        style = MaterialTheme.typography.labelLarge,
                        color = Olive
                    )
                    Spacer(Modifier.height(EliteSpacing.xs))
                    Text(
                        text = AdjustmentPresentation.previa(esforcoGravado, dor, ajustesAutomaticos),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextColor
                    )
                }
            }
        }

                // WARNING CARD - red border with shield icon
                EliteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_aviso_saude"),
                    border = BorderStroke(1.dp, Red.copy(alpha = 0.45f))
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Red,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(EliteSpacing.md))
                        Text(
                            text = "Dor forte? Pare e procure um profissional de saúde. O Elite não faz diagnóstico.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextColor
                        )
                    }
                }
    }
}
