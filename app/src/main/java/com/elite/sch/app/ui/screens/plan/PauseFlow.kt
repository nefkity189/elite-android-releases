package com.elite.sch.app.ui.screens.plan

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.PausePreviewData
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteButtonVariant
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteDateRangePickerDialog
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.EliteSelectableCard
import com.elite.sch.app.ui.theme.Amber
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.ChronoUnit

@Composable
fun PauseOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    EliteSelectableCard(
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
    var pauseEndDate by remember(currentDate) { mutableStateOf(currentDate.plusDays(6)) }
    var pauseNivel by remember { mutableStateOf("caminhada") }
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
        EliteDateRangePickerDialog(
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

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Card)
                        .border(1.dp, Line, RoundedCornerShape(50))
                        .clickable(onClick = onDismiss)
                        .testTag("pause_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = EliteText,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "PRECISO DE UMA\nPAUSA",
                    style = TextStyle(
                        fontFamily = EliteDisplayFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.96.sp
                    ),
                    color = EliteText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(14.dp))
            PauseHealthNotice()
            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                PauseSectionLabel("PERÍODO DA PAUSA (1 A 14 DIAS)")
                PauseDateCard(
                    startDate = pauseStartDate,
                    endDate = pauseEndDate,
                    days = pausePeriodDays,
                    onClick = { showPauseRangePicker = true }
                )

                PauseSectionLabel("NÍVEL DURANTE A PAUSA")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PauseLevelCard(
                        title = "Mantém os treinos",
                        subtitle = "Usa a etapa anterior (mínimo etapa 1)",
                        selected = pauseNivel == "reduzido",
                        testTag = "nivel_reduzido",
                        onClick = { pauseNivel = "reduzido" }
                    )
                    PauseLevelCard(
                        title = "Só caminhada",
                        subtitle = "Caminhada leve de 20 minutos com um passo só",
                        selected = pauseNivel == "caminhada",
                        testTag = "nivel_caminhada",
                        onClick = { pauseNivel = "caminhada" }
                    )
                    PauseLevelCard(
                        title = "Sem treino",
                        subtitle = "Treinos planejados viram pausa",
                        selected = pauseNivel == "sem_treino",
                        testTag = "nivel_sem_treino",
                        onClick = { pauseNivel = "sem_treino" }
                    )
                }

                PauseSectionLabel("RITMO DE RETORNO")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PauseReturnOption(
                        label = "Cauteloso",
                        selected = pauseRetorno == "gradual",
                        testTag = "retorno_gradual",
                        modifier = Modifier.weight(1f)
                    ) { pauseRetorno = "gradual" }
                    PauseReturnOption(
                        label = "Proporcional",
                        selected = pauseRetorno == "normal",
                        testTag = "retorno_normal",
                        modifier = Modifier.weight(1f)
                    ) { pauseRetorno = "normal" }
                    PauseReturnOption(
                        label = "Rápido",
                        selected = pauseRetorno == "rapido",
                        testTag = "retorno_rapido",
                        modifier = Modifier.weight(1f)
                    ) { pauseRetorno = "rapido" }
                }

                PausePreviewCard(
                    preview = pausePreview,
                    error = pauseError
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(if (pausePreview != null) Red else Red.copy(alpha = 0.4f))
                    .clickable(enabled = pausePreview != null) {
                        val preview = pausePreview ?: return@clickable
                        onDismiss()
                        onApplyPause(preview, "pausa", pauseStartDate, pauseEndDate, pauseNivel, pauseRetorno)
                    }
                    .testTag("btn_confirmar_pausa"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Confirmar pausa",
                    style = TextStyle(
                        fontFamily = EliteBodyFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    ),
                    color = Color(0xFFFFF4F0)
                )
            }
        }
    }
}

@Composable
private fun PauseSectionLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = EliteBodyFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            letterSpacing = 1.32.sp
        ),
        color = TextMuted
    )
}

@Composable
private fun PauseHealthNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Card)
            .border(1.dp, Amber.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .padding(14.dp)
            .testTag("aviso_saude_pausa"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = Amber,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = "Se estiver doente ou com dor, não treine e procure um profissional de saúde.",
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontSize = 12.sp,
                lineHeight = 16.8.sp
            ),
            color = EliteText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PauseDateCard(
    startDate: LocalDate,
    endDate: LocalDate,
    days: Long,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("pt-BR"))
    val range = "${startDate.format(formatter)} a ${endDate.format(formatter)}"
        .replace(".", "")
        .lowercase(Locale.forLanguageTag("pt-BR"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Card)
            .border(1.dp, Line, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag("btn_pause_periodo_escolher"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = range,
                style = TextStyle(
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                ),
                color = EliteText,
                modifier = Modifier.testTag("pause_periodo")
            )
            Text(
                text = "$days ${if (days == 1L) "dia" else "dias"}",
                style = TextStyle(fontFamily = EliteBodyFont, fontSize = 12.sp, lineHeight = 16.8.sp),
                color = TextMuted
            )
        }
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = "Escolher período",
            tint = EliteText,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun PauseLevelCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clip(shape)
            .background(if (selected) OliveSoft else Card)
            .border(1.dp, if (selected) Olive else Line, shape)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(vertical = 14.dp)
                    .width(3.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(topEnd = 3.dp, bottomEnd = 3.dp))
                    .background(Olive)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = EliteBodyFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                ),
                color = EliteText
            )
            Text(
                text = subtitle,
                style = TextStyle(fontFamily = EliteBodyFont, fontSize = 12.sp, lineHeight = 16.8.sp),
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PauseReturnOption(
    label: String,
    selected: Boolean,
    testTag: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(if (selected) Olive else Card)
            .border(if (selected) 0.dp else 1.dp, if (selected) Color.Transparent else Line, shape)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
            color = if (selected) OnOlive else TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PausePreviewCard(
    preview: PausePreviewData?,
    error: String?
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Card)
            .border(1.dp, Line, shape)
            .padding(14.dp)
            .testTag(if (preview != null) "card_previa_pausa" else "pausa_error_card"),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = if (preview != null) "PRÉVIA DA RETOMADA" else "PRÉVIA INDISPONÍVEL",
            style = TextStyle(
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                letterSpacing = 1.32.sp
            ),
            color = Olive
        )
        if (preview != null) {
            Spacer(Modifier.height(3.dp))
            Text("• Recuo: ${preview.recuo} ${if (preview.recuo == 1) "etapa" else "etapas"}", style = MaterialTheme.typography.bodySmall, color = EliteText)
            Text("• Etapa de retomada: Etapa ${preview.etapaDeRetomada}", style = MaterialTheme.typography.bodySmall, color = EliteText)
            Text("• Treinos afetados: ${preview.treinosAfetadosCount}", style = MaterialTheme.typography.bodySmall, color = EliteText)
            if (preview.foiEstendido) {
                Text(
                    text = "Prazo final estendido até ${preview.dataFimEstendida?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Amber,
                    modifier = Modifier.testTag("aviso_prazo_estendido_pausa")
                )
            }
        } else if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = Red
            )
        }
    }
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
        EliteDateRangePickerDialog(
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

    EliteDialog(
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
                    EliteButton(
                        text = "Escolher datas",
                        onClick = { showVacationRangePicker = true },
                        variant = EliteButtonVariant.Secondary,
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
                        modifier = Modifier.padding(EliteSpacing.md)
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
                    EliteCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_previa_ferias")
                    ) {
                        Column(
                            modifier = Modifier.padding(EliteSpacing.md),
                            verticalArrangement = Arrangement.spacedBy(EliteSpacing.xs)
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
                                Spacer(modifier = Modifier.height(EliteSpacing.xs))
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
                                        modifier = Modifier.padding(EliteSpacing.md)
                                    )
                                }
                            }
                        }
                    }
                } else if (vacationError != null) {
                    EliteCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ferias_error_card")
                    ) {
                        Text(
                            text = vacationError,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(EliteSpacing.md)
                        )
                    }
                }
            }
        },
        confirmLabel = "Confirmar férias",
        onConfirm = {
            val preview = vacationPreview ?: return@EliteDialog
            onDismiss()
            onApplyPause(preview, "ferias", vacationStartDate, vacationEndDate, "sem_treino", vacationRetorno)
        },
        confirmTestTag = "btn_confirmar_ferias",
        dismissLabel = "Cancelar",
        dismissTestTag = null
    )
}
