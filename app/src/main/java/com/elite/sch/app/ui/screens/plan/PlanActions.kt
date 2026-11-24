package com.elite.sch.app.ui.screens.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.generator.PlanGenerator
import com.elite.sch.app.generator.RegenerationResult
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteButtonVariant
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteDatePickerDialog
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ManagePlanMenuDialog(
    onOpenPause: () -> Unit,
    onOpenVacation: () -> Unit,
    onOpenDateChange: () -> Unit,
    onOpenDeleteStep1: () -> Unit,
    onOpenChangePlan: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = com.elite.sch.app.ui.theme.Surface,
        contentColor = EliteText,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(LineStrong)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "GERENCIAR PLANO",
                style = TextStyle(
                    fontFamily = EliteDisplayFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    lineHeight = 23.sp,
                    letterSpacing = 0.8.sp
                ),
                color = EliteText
            )
            Text(
                text = "O que você deseja fazer com o seu plano de treino?",
                style = TextStyle(
                    fontFamily = EliteBodyFont,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp
                ),
                color = TextMuted
            )
            ManagePlanOption("Trocar de plano", Icons.Default.CalendarMonth, testTag = "btn_menu_trocar_plano") {
                onDismiss()
                onOpenChangePlan()
            }
            ManagePlanOption("Preciso de uma pausa", Icons.Default.AccessTime, testTag = "btn_menu_pausa") {
                onDismiss()
                onOpenPause()
            }
            ManagePlanOption("Marcar férias", Icons.Default.DirectionsWalk, testTag = "btn_menu_ferias") {
                onDismiss()
                onOpenVacation()
            }
            ManagePlanOption("Alterar data final", Icons.Default.Edit, testTag = "btn_menu_alterar_data") {
                onDismiss()
                onOpenDateChange()
            }
            ManagePlanOption(
                label = "Excluir plano",
                icon = Icons.Default.Delete,
                destructive = true,
                testTag = "btn_excluir_plano"
            ) {
                onDismiss()
                onOpenDeleteStep1()
            }
        }
    }
}

@Composable
private fun ManagePlanOption(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    destructive: Boolean = false,
    testTag: String,
    onClick: () -> Unit
) {
    val accent = if (destructive) Color(0xFFF0807C) else Olive
    val tile = if (destructive) RedSoft else OliveSoft
    val borderColor = if (destructive) Red.copy(alpha = 0.45f) else LineStrong.copy(alpha = 0.35f)
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(shape)
            .background(Card)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(tile),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 20.sp
            ),
            color = if (destructive) accent else EliteText
        )
    }
}

@Composable
fun DateChangeDialogSection(
    currentDate: LocalDate,
    planEndDate: LocalDate,
    activePlan: PlanEntity?,
    frequenciaExercicio: String,
    onPreviewRegeneration: (LocalDate) -> Result<RegenerationResult>,
    onApplyRegeneration: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedEndDate by remember(planEndDate) { mutableStateOf(planEndDate) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val previewState = remember(selectedEndDate, activePlan) {
        onPreviewRegeneration(selectedEndDate)
    }
    val previewResult = previewState.getOrNull()
    val previewError = previewState.exceptionOrNull()?.message

    if (showEndDatePicker) {
        val minEnd = activePlan?.let {
            try { LocalDate.parse(it.dataInicio).plusWeeks(PlanGenerator.getMinimumWeeks(frequenciaExercicio).toLong()).minusDays(1) } catch (_: Exception) { null }
        } ?: currentDate
        EliteDatePickerDialog(
            initial = selectedEndDate,
            minDate = if (minEnd.isBefore(currentDate)) currentDate else minEnd,
            maxDate = currentDate.plusWeeks(16),
            onConfirm = {
                selectedEndDate = it
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    EliteDialog(
        title = "Alterar Data Final",
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
            ) {
                Text(
                    text = "Ajuste a data de término do seu plano. Os treinos futuros serão recalculados mantendo o que você já concluiu.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nova data final", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = selectedEndDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("texto_data_final")
                        )
                    }
                    EliteButton(
                        text = "Escolher data",
                        onClick = { showEndDatePicker = true },
                        variant = EliteButtonVariant.Secondary,
                        modifier = Modifier.testTag("btn_escolher_data_final")
                    )
                }

                if (previewResult != null) {
                    EliteCard(
                        shape = EliteShape.md,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("regeneration_preview_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(EliteSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(EliteSpacing.xs)
                        ) {
                            Text(
                                text = "Prévia do novo cronograma:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "• Duração total: ${previewResult.novoTotalSemanas} semanas",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• Data do último treino: ${previewResult.dataUltimoTreino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• Treinos reprogramados: ${previewResult.toCreate.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    val minWeeks = PlanGenerator.getMinimumWeeks(frequenciaExercicio)
                    val errorMessage = previewError ?: "Data inválida. O plano deve ter no mínimo $minWeeks semanas desde o início e no máximo 16 semanas restantes a partir de hoje."
                    EliteCard(
                        shape = EliteShape.md,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("regeneration_error_card")
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(EliteSpacing.md)
                        )
                    }
                }
            }
        },
        confirmLabel = "Confirmar alteração",
        onConfirm = {
            onDismiss()
            onApplyRegeneration(selectedEndDate)
        },
        confirmTestTag = "btn_confirm_regeneration",
        dismissLabel = "Cancelar",
        dismissTestTag = null
    )
}

@Composable
fun ExitToHubDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    EliteDialog(
        title = "Trocar de plano",
        text = "Você será redirecionado ao hub e terá que escolher uma nova modalidade. O plano atual será mantido em segundo plano.",
        onDismiss = onDismiss,
        confirmLabel = "Voltar ao hub",
        onConfirm = onConfirm,
        confirmTestTag = "btn_confirm_exit_to_hub",
        dismissLabel = "_continuar editando",
        destructive = false
    )
}

@Composable
fun DeletePlanStep1Dialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    EliteDialog(
        title = "Excluir plano de treino?",
        text = "O plano atual e os próximos treinos agendados serão removidos. Todo o seu histórico de corridas e rotas concluídas será preservado.",
        onDismiss = onDismiss,
        confirmLabel = "Continuar",
        onConfirm = onConfirm,
        confirmTestTag = "btn_confirm_delete_plan_step1",
        dismissLabel = "Cancelar",
        destructive = true
    )
}

@Composable
fun DeletePlanStep2Dialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 343.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(com.elite.sch.app.ui.theme.CardElevated)
                    .border(
                        width = 1.dp,
                        color = Red.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RedSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFF0807C),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "EXCLUIR PLANO DE TREINO?",
                    style = TextStyle(
                        fontFamily = EliteDisplayFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        lineHeight = 23.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = EliteText
                )
                Text(
                    text = "Isso apagará o progresso atual e permitirá configurar um novo plano do início.",
                    style = TextStyle(
                        fontFamily = EliteBodyFont,
                        fontSize = 14.sp,
                        lineHeight = 19.6.sp
                    ),
                    color = TextMuted
                )
                PlanDeleteDialogButton(
                    label = "Sim, excluir plano",
                    destructive = true,
                    testTag = "btn_confirm_delete_plan_step2",
                    onClick = onConfirm
                )
                PlanDeleteDialogButton(
                    label = "Cancelar",
                    destructive = false,
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun PlanDeleteDialogButton(
    label: String,
    destructive: Boolean,
    testTag: String? = null,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(shape)
            .background(if (destructive) Red else Color.Transparent)
            .then(if (destructive) Modifier else Modifier.border(1.dp, LineStrong, shape))
            .clickable(onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = if (destructive) Color(0xFFFFF4F0) else EliteText
        )
    }
}
