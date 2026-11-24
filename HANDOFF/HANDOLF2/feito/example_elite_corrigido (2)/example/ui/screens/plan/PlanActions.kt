package com.example.ui.screens.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entities.PlanEntity
import com.example.generator.PlanGenerator
import com.example.generator.RegenerationResult
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleDatePickerDialog
import com.example.ui.components.AisleDialog
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleShape
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ManagePlanMenuDialog(
    onOpenPause: () -> Unit,
    onOpenVacation: () -> Unit,
    onOpenDateChange: () -> Unit,
    onOpenDeleteStep1: () -> Unit,
    onOpenChangePlan: () -> Unit,
    onDismiss: () -> Unit
) {
    AisleDialog(
        title = "Gerenciar Plano",
        text = "O que você deseja fazer com o seu plano de treino?",
        onDismiss = onDismiss,
        dismissLabel = "Fechar",
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AisleSpacing.sm)
            ) {
                AisleButton(
                    text = "Trocar de plano",
                    onClick = {
                        onDismiss()
                        onOpenChangePlan()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_menu_trocar_plano"),
                )
                AisleButton(
                    text = "Preciso de uma pausa",
                    onClick = {
                        onDismiss()
                        onOpenPause()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_menu_pausa")
                )
                AisleButton(
                    text = "Marcar férias",
                    onClick = {
                        onDismiss()
                        onOpenVacation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_menu_ferias")
                )
                AisleButton(
                    text = "Alterar data final",
                    onClick = {
                        onDismiss()
                        onOpenDateChange()
                    },
                    variant = AisleButtonVariant.Secondary,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_menu_alterar_data")
                )
                AisleButton(
                    text = "Excluir plano",
                    onClick = {
                        onDismiss()
                        onOpenDeleteStep1()
                    },
                    variant = AisleButtonVariant.Destructive,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_excluir_plano")
                )
            }
        }
    )
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
        AisleDatePickerDialog(
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

    AisleDialog(
        title = "Alterar Data Final",
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
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
                    AisleButton(
                        text = "Escolher data",
                        onClick = { showEndDatePicker = true },
                        variant = AisleButtonVariant.Secondary,
                        modifier = Modifier.testTag("btn_escolher_data_final")
                    )
                }

                if (previewResult != null) {
                    AisleCard(
                        shape = AisleShape.md,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("regeneration_preview_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(AisleSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)
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
                    AisleCard(
                        shape = AisleShape.md,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("regeneration_error_card")
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(AisleSpacing.md)
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
    AisleDialog(
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
    AisleDialog(
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
    AisleDialog(
        title = "Confirmar exclusão definitiva",
        text = "Tem certeza de que deseja excluir o plano? Esta ação removerá o cronograma e você poderá montar um novo plano quando quiser.",
        onDismiss = onDismiss,
        confirmLabel = "Sim, excluir plano",
        onConfirm = onConfirm,
        confirmTestTag = "btn_confirm_delete_plan_step2",
        dismissLabel = "Cancelar",
        destructive = true
    )
}
