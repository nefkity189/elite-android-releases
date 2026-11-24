package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.generator.AdaptiveEngine
import com.elite.sch.app.generator.CheckinOutcome
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteButtonVariant
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteChoiceChip
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
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
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.EliteTextStyles
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.RedSoft
import androidx.compose.foundation.shape.RoundedCornerShape
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteShape


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreWorkoutCheckinBottomSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (energia: Int, sono: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    var energia by remember { mutableStateOf(3) }
    var sono by remember { mutableStateOf(3) }

    val ENERGY_LABELS = listOf(
        1 to "Muito baixa",
        2 to "Baixa",
        3 to "Normal",
        4 to "Boa",
        5 to "Ótima"
    )

    val SLEEP_LABELS = listOf(
        1 to "Muito ruim",
        2 to "Ruim",
        3 to "Normal",
        4 to "Boa",
        5 to "Ótima"
    )

    val outcome = remember(energia, sono) {
        AdaptiveEngine.evaluateCheckin(energia, sono, 0) // dor=0 no pré-treino
    }

    // Back handler para fechar ao pressionar voltar
    BackHandler(enabled = isOpen, onBack = onDismiss)

    val sheetState = rememberModalBottomSheetState()

    // Sync sheet state with isOpen
    androidx.compose.runtime.LaunchedEffect(isOpen) {
        if (isOpen) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(EliteSpacing.xl)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
        ) {
            // Handle bar
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(LineStrong)
                    .clip(EliteShape.md)
            )

            Spacer(modifier = Modifier.height(EliteSpacing.lg))

            // Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Como você está hoje?",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextColor
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = "Isso ajuda o Elite a ajustar seu treino de hoje",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(EliteSpacing.xl))

            // ENERGIA
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EliteSpacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = null,
                        tint = Olive,
                        modifier = Modifier.size(24.dp).padding(end = EliteSpacing.lg)
                    )
                    Column {
                        Text(
                            text = "Energia",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextColor
                        )
                        Text(
                            text = "Como você acordou hoje",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)
                ) {
                    ENERGY_LABELS.forEach { (level, label) ->
                        EliteChoiceChip(
                            text = "$level",
                            selected = energia == level,
                            onClick = { energia = level },
                            description = "Energia $label",
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 72.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.lg))

            // SONO
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(EliteSpacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = Brass,
                        modifier = Modifier.size(24.dp).padding(end = EliteSpacing.lg)
                    )
                    Column {
                        Text(
                            text = "Sono",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextColor
                        )
                        Text(
                            text = "Como você dormiu ontem à noite",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)
                ) {
                    SLEEP_LABELS.forEach { (level, label) ->
                        EliteChoiceChip(
                            text = "$level",
                            selected = sono == level,
                            onClick = { sono = level },
                            description = "Sono $label",
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 72.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.lg))

            // Prévia do resultado
            EliteCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(when (outcome) {
                        CheckinOutcome.DESCANSO -> CardElevated.copy(alpha = 0.3f)
                        CheckinOutcome.VERSAO_LEVE -> CardElevated.copy(alpha = 0.3f)
                        else -> CardElevated
                    })
            ) {
                Column(modifier = Modifier.padding(EliteSpacing.lg), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when (outcome) {
                            CheckinOutcome.DESCANSO -> Icons.Default.Warning
                            CheckinOutcome.VERSAO_LEVE -> Icons.Default.Autorenew
                            else -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        tint = when (outcome) {
                            CheckinOutcome.DESCANSO -> Red
                            CheckinOutcome.VERSAO_LEVE -> Olive
                            else -> Olive
                        },
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(EliteSpacing.md))
                    Text(
                        text = when (outcome) {
                            CheckinOutcome.DESCANSO -> "Recomendamos descanso hoje"
                            CheckinOutcome.VERSAO_LEVE -> "Treino leve: menos intensidade, mais descanso"
                            else -> "Treino normal mantido"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = TextColor
                    )
                    Spacer(modifier = Modifier.height(EliteSpacing.xs))
                    Text(
                        text = when (outcome) {
                            CheckinOutcome.DESCANSO -> "Energia e sono muito baixos ou dor forte. Melhor recuperar."
                            CheckinOutcome.VERSAO_LEVE -> "Ajuste automático aplicado: etapa anterior com mais caminhada."
                            else -> "Seus sinais estão bons. O plano segue como está."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.xxl))

            // Botão confirmar
            EliteButton(
                text = "Confirmar e iniciar treino",
                onClick = { onConfirm(energia, sono) },
                variant = EliteButtonVariant.Primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("btn_confirm_checkin")
            )

            Spacer(modifier = Modifier.height(EliteSpacing.xs))

            EliteButton(
                text = "Pular check-in",
                onClick = { onConfirm(3, 3) }, // padrão neutro
                variant = EliteButtonVariant.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_skip_checkin")
            )

            // Padding inferior generoso (100dp) para garantir que os botões fiquem bem acima do botão central "A" da barra de navegação (que sobressai 32dp)
            Spacer(modifier = Modifier.height(EliteSpacing.giant))
        }
    }
}