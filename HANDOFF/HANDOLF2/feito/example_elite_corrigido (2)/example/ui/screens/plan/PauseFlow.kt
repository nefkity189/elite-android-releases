package com.example.ui.screens.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.data.local.entities.WorkoutEntity
import com.example.generator.PausePreviewData
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleDateRangePickerDialog
import com.example.ui.components.AisleDialog
import com.example.ui.components.AisleSelectableCard
import com.example.ui.theme.AisleSpacing
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun PauseOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    AisleSelectableCard(
        text = "$title\n$subtitle",
        selected = selected,
        onClick = onClick,
        modifier = Modifier.testTag(testTag)
    )
}

@Composable
fun PauseDialogSection(
    currentDate: LocalDate,
    workouts: List<WorkoutEntity>,
    activePlan: PlanEntity?,
    onPreviewPause: (startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Result<PausePreviewData>,
    onApplyPause: (previewData: PausePreviewData, tipo: String, startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Unit,
    onDismiss: () -> Unit
) {
    var pauseStartDate by remember(currentDate) { mutableStateOf(currentDate) }
    var pauseEndDate by remember(currentDate) { mutableStateOf(currentDate.plusDays(3)) }
    var pauseNivel by remember { mutableStateOf("sem_treino") }
    var pauseRetorno by remember { mutableStateOf("normal") }
    var showPauseRangePicker by remember { mutableStateOf(false) }

    val pausePeriodDays = ChronoUnit.DAYS.between(pauseStartDate, pauseEndDate) + 1
    val isPauseValid = (pausePeriodDays in 1..14) &&
        !pauseEndDate.isBefore(currentDate) &&
        !pauseStartDate.isBefore(currentDate.minusDays(7)) &&
        !pauseEndDate.isBefore(pauseStartDate)

    val pausePreviewResult = remember(pauseStartDate, pauseEndDate, pauseNivel, pauseRetorno, workouts, activePlan) {
        if (isPauseValid) {
            onPreviewPause(pauseStartDate, pauseEndDate, pauseNivel, pauseRetorno)
        } else {
            val errorMsg = when {
                pausePeriodDays !in 1..14 -> "O período deve ter entre 1 e 14 dias."
                pauseStartDate.isBefore(currentDate.minusDays(7)) -> "O início pode ser no máximo 7 dias no passado."
                pauseEndDate.isBefore(currentDate) -> "O fim não pode ser anterior a hoje."
                else -> "A data final deve ser igual ou posterior à data inicial."
            }
            Result.failure(IllegalArgumentException(errorMsg))
        }
    }
    val pausePreview = pausePreviewResult.getOrNull()
    val pauseError = pausePreviewResult.exceptionOrNull()?.message

    if (showPauseRangePicker) {
        AisleDateRangePickerDialog(
            initialStart = pauseStartDate,
            initialEnd = pauseEndDate,
            minDate = currentDate.minusDays(7),
            maxDate = currentDate.plusDays(20),
            minDays = 1,
            maxDays = 14,
            endNotBefore = currentDate,
            onConfirm = { st, en ->
                pauseStartDate = st
                pauseEndDate = en
                showPauseRangePicker = false
            },
            onDismiss = { showPauseRangePicker = false }
        )
    }

    AisleDialog(
        title = "Preciso de uma pausa",
        icon = Icons.Default.Pause,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("aviso_saude_pausa")
                ) {
                    Row(
                        modifier = Modifier.padding(AisleSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Se estiver doente ou com dor, não treine e procure um profissional de saúde.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                Text(
                    text = "Período da pausa (1 a 14 dias):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${pauseStartDate.format(DateTimeFormatter.ofPattern("dd/MM"))} a ${pauseEndDate.format(DateTimeFormatter.ofPattern("dd/MM"))} • $pausePeriodDays ${if (pausePeriodDays == 1L) "dia" else "dias"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pause_periodo")
                    )
                    AisleButton(
                        text = "Escolher datas",
                        onClick = { showPauseRangePicker = true },
                        variant = AisleButtonVariant.Secondary,
                        modifier = Modifier.testTag("btn_pause_periodo_escolher")
                    )
                }

                Text(
                    text = "Nível durante a pausa:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PauseOptionCard(
                        title = "Reduzido",
                        subtitle = "Mantém os treinos usando a etapa anterior (mínimo etapa 1)",
                        selected = pauseNivel == "reduzido",
                        onClick = { pauseNivel = "reduzido" },
                        testTag = "nivel_reduzido"
                    )
                    PauseOptionCard(
                        title = "Só caminhada",
                        subtitle = "Caminhada leve de 20 minutos com um passo só",
                        selected = pauseNivel == "caminhada",
                        onClick = { pauseNivel = "caminhada" },
                        testTag = "nivel_caminhada"
                    )
                    PauseOptionCard(
                        title = "Sem treino",
                        subtitle = "Nenhum treino no período; treinos planejados viram pausa",
                        selected = pauseNivel == "sem_treino",
                        onClick = { pauseNivel = "sem_treino" },
                        testTag = "nivel_sem_treino"
                    )
                }

                Text(
                    text = "Ritmo de retorno após a pausa:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PauseOptionCard(
                        title = "Gradual",
                        subtitle = "Recua 1 etapa a mais para adaptação suave",
                        selected = pauseRetorno == "gradual",
                        onClick = { pauseRetorno = "gradual" },
                        testTag = "retorno_gradual"
                    )
                    PauseOptionCard(
                        title = "Normal",
                        subtitle = "Recuo proporcional aos dias parados",
                        selected = pauseRetorno == "normal",
                        onClick = { pauseRetorno = "normal" },
                        testTag = "retorno_normal"
                    )
                    PauseOptionCard(
                        title = "Rápido",
                        subtitle = "Recua 1 etapa a menos para retomada mais rápida",
                        selected = pauseRetorno == "rapido",
                        onClick = { pauseRetorno = "rapido" },
                        testTag = "retorno_rapido"
                    )
                }

                if (pausePreview != null) {
                    AisleCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_previa_pausa")
                    ) {
                        Column(
                            modifier = Modifier.padding(AisleSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)
                        ) {
                            Text(
                                text = "Prévia da Retomada",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("• Recuo: ${pausePreview.recuo} ${if (pausePreview.recuo == 1) "etapa" else "etapas"}", style = MaterialTheme.typography.bodySmall)
                            Text("• Etapa de retomada: Etapa ${pausePreview.etapaDeRetomada}", style = MaterialTheme.typography.bodySmall)
                            Text("• Treinos afetados: ${pausePreview.treinosAfetadosCount}", style = MaterialTheme.typography.bodySmall)
                            Text("• Data do último treino: ${pausePreview.novaDataUltimoTreino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}", style = MaterialTheme.typography.bodySmall)

                            if (pausePreview.foiEstendido) {
                                Spacer(modifier = Modifier.height(AisleSpacing.xs))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                        .testTag("aviso_prazo_estendido_pausa"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "O prazo final foi estendido até ${pausePreview.dataFimEstendida?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} para garantir progressão segura (máx. 2 etapas por semana).",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(AisleSpacing.md)
                                    )
                                }
                            }
                        }
                    }
                } else if (pauseError != null) {
                    AisleCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pausa_error_card")
                    ) {
                        Text(
                            text = pauseError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(AisleSpacing.md)
                        )
                    }
                }
            }
        },
        confirmLabel = "Confirmar pausa",
        onConfirm = {
            val preview = pausePreview ?: return@AisleDialog
            onDismiss()
            onApplyPause(preview, "pausa", pauseStartDate, pauseEndDate, pauseNivel, pauseRetorno)
        },
        confirmTestTag = "btn_confirmar_pausa",
        dismissLabel = "Cancelar",
        dismissTestTag = null
    )
}

@Composable
fun VacationDialogSection(
    currentDate: LocalDate,
    workouts: List<WorkoutEntity>,
    activePlan: PlanEntity?,
    onPreviewPause: (startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Result<PausePreviewData>,
    onApplyPause: (previewData: PausePreviewData, tipo: String, startDate: LocalDate, endDate: LocalDate, nivel: String, retorno: String) -> Unit,
    onDismiss: () -> Unit
) {
    var vacationStartDate by remember(currentDate) { mutableStateOf(currentDate.plusDays(1)) }
    var vacationEndDate by remember(currentDate) { mutableStateOf(currentDate.plusDays(7)) }
    var vacationRetorno by remember { mutableStateOf("normal") }
    var showVacationRangePicker by remember { mutableStateOf(false) }

    val vacationPeriodDays = ChronoUnit.DAYS.between(vacationStartDate, vacationEndDate) + 1
    val isVacationValid = (vacationPeriodDays in 1..21) &&
        !vacationStartDate.isBefore(currentDate) &&
        !vacationEndDate.isBefore(vacationStartDate)

    val vacationPreviewResult = remember(vacationStartDate, vacationEndDate, vacationRetorno, workouts, activePlan) {
        if (isVacationValid) {
            onPreviewPause(vacationStartDate, vacationEndDate, "sem_treino", vacationRetorno)
        } else {
            val errorMsg = when {
                vacationPeriodDays !in 1..21 -> "O período de férias deve ter entre 1 e 21 dias."
                vacationStartDate.isBefore(currentDate) -> "As férias devem começar a partir de hoje."
                else -> "A data final deve ser igual ou posterior à data inicial."
            }
            Result.failure(IllegalArgumentException(errorMsg))
        }
    }
    val vacationPreview = vacationPreviewResult.getOrNull()
    val vacationError = vacationPreviewResult.exceptionOrNull()?.message

    if (showVacationRangePicker) {
        AisleDateRangePickerDialog(
            initialStart = vacationStartDate,
            initialEnd = vacationEndDate,
            minDate = currentDate.plusDays(1),
            maxDate = currentDate.plusDays(90),
            minDays = 1,
            maxDays = 21,
            onConfirm = { st, en ->
                vacationStartDate = st
                vacationEndDate = en
                showVacationRangePicker = false
            },
            onDismiss = { showVacationRangePicker = false }
        )
    }

    AisleDialog(
        title = "Marcar férias",
        icon = Icons.Default.DateRange,
        onDismiss = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Defina um período futuro de 1 a 21 dias. Durante as férias, o nível é sempre Sem treino e os treinos agendados serão pausados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${vacationStartDate.format(DateTimeFormatter.ofPattern("dd/MM"))} a ${vacationEndDate.format(DateTimeFormatter.ofPattern("dd/MM"))} • $vacationPeriodDays ${if (vacationPeriodDays == 1L) "dia" else "dias"}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ferias_periodo")
                    )
                    AisleButton(
                        text = "Escolher datas",
                        onClick = { showVacationRangePicker = true },
                        variant = AisleButtonVariant.Secondary,
                        modifier = Modifier.testTag("btn_ferias_periodo_escolher")
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nível de atividade: Sem treino (fixo para férias)",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(AisleSpacing.md)
                    )
                }

                Text(
                    text = "Ritmo de retorno após as férias:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PauseOptionCard(
                        title = "Gradual",
                        subtitle = "Recua 1 etapa a mais para adaptação suave",
                        selected = vacationRetorno == "gradual",
                        onClick = { vacationRetorno = "gradual" },
                        testTag = "retorno_gradual"
                    )
                    PauseOptionCard(
                        title = "Normal",
                        subtitle = "Recuo proporcional aos dias parados",
                        selected = vacationRetorno == "normal",
                        onClick = { vacationRetorno = "normal" },
                        testTag = "retorno_normal"
                    )
                    PauseOptionCard(
                        title = "Rápido",
                        subtitle = "Recua 1 etapa a menos para retomada mais rápida",
                        selected = vacationRetorno == "rapido",
                        onClick = { vacationRetorno = "rapido" },
                        testTag = "retorno_rapido"
                    )
                }

                if (vacationPreview != null) {
                    AisleCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_previa_ferias")
                    ) {
                        Column(
                            modifier = Modifier.padding(AisleSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)
                        ) {
                            Text(
                                text = "Prévia da Retomada",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("• Recuo: ${vacationPreview.recuo} ${if (vacationPreview.recuo == 1) "etapa" else "etapas"}", style = MaterialTheme.typography.bodySmall)
                            Text("• Etapa de retomada: Etapa ${vacationPreview.etapaDeRetomada}", style = MaterialTheme.typography.bodySmall)
                            Text("• Treinos afetados: ${vacationPreview.treinosAfetadosCount}", style = MaterialTheme.typography.bodySmall)
                            Text("• Data do último treino: ${vacationPreview.novaDataUltimoTreino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}", style = MaterialTheme.typography.bodySmall)

                            if (vacationPreview.foiEstendido) {
                                Spacer(modifier = Modifier.height(AisleSpacing.xs))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("aviso_prazo_estendido_ferias")
                                ) {
                                    Text(
                                        text = "O prazo final foi estendido até ${vacationPreview.dataFimEstendida?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} para garantir progressão segura (máx. 2 etapas por semana).",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(AisleSpacing.md)
                                    )
                                }
                            }
                        }
                    }
                } else if (vacationError != null) {
                    AisleCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ferias_error_card")
                    ) {
                        Text(
                            text = vacationError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(AisleSpacing.md)
                        )
                    }
                }
            }
        },
        confirmLabel = "Confirmar férias",
        onConfirm = {
            val preview = vacationPreview ?: return@AisleDialog
            onDismiss()
            onApplyPause(preview, "ferias", vacationStartDate, vacationEndDate, "sem_treino", vacationRetorno)
        },
        confirmTestTag = "btn_confirmar_ferias",
        dismissLabel = "Cancelar",
        dismissTestTag = null
    )
}
