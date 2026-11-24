package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
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
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteStepper
import com.elite.sch.app.ui.components.EliteTopBar
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteShape

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DaysSelectionScreen(
    diasPorSemana: Int,
    diasSelecionados: List<DayOfWeek>,
    dataInicio: LocalDate,
    onDiasPorSemanaChange: (Int) -> Unit,
    onDiaToggle: (DayOfWeek) -> Unit,
    onDataInicioChange: (LocalDate) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    errorMessage: String? = null,
    nextButtonText: String? = null,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEmpty: Boolean = false
) {
    BackHandler(onBack = onBackClick)

    if (isLoading) {
        LoadingState(message = "Configurando calendário...", modifier = modifier)
        return
    }

    if (isEmpty) {
        EmptyState(
            title = "Configuração indisponível",
            description = "Não foi possível carregar as opções de agendamento.",
            actionLabel = "Voltar",
            onActionClick = onBackClick,
            modifier = modifier
        )
        return
    }

    val scrollState = rememberScrollState()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("pt-BR"))
    val shortDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))

    // Vizinhos dos dias já marcados (não permite consecutivos, incluindo domingo e segunda)
    val forbiddenNeighbors = diasSelecionados
        .flatMap { listOf(it.minus(1), it.plus(1)) }
        .toSet()

    val isQuantityExact = diasSelecionados.size == diasPorSemana

Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = EliteSpacing.screenHorizontal)
            .padding(top = EliteSpacing.md, bottom = EliteSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(EliteSpacing.xl)
    ) {
        // Top bar
        EliteTopBar(
            title = "Dias de treino",
            onBack = onBackClick,
            backTestTag = "days_back_button",
            modifier = Modifier.fillMaxWidth().padding(horizontal = EliteSpacing.md)
        )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Como você prefere treinar?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )

                Spacer(modifier = Modifier.height(EliteSpacing.md))

                Text(
                    text = "Escolha sua frequência semanal e a data para dar a largada.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(EliteSpacing.xl))

                // 1. Quantos dias por semana
                Text(
                    text = "Dias por semana",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )

                Spacer(modifier = Modifier.height(EliteSpacing.md))
                
                EliteStepper(
                    value = diasPorSemana,
                    onValueChange = onDiasPorSemanaChange,
                    range = 2..4,
                    label = "dias por semana",
                    modifier = Modifier.fillMaxWidth(),
                    minusTestTag = "days_minus",
                    plusTestTag = "days_plus"
                )

                Spacer(modifier = Modifier.height(EliteSpacing.xl))

                // 2. Quais dias da semana
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quais dias da semana?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = "${diasSelecionados.size} de $diasPorSemana",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isQuantityExact) Olive else Red
                    )
                }

                Spacer(modifier = Modifier.height(EliteSpacing.md))

                val weekDays = listOf(
                    DayOfWeek.SUNDAY to "D",
                    DayOfWeek.MONDAY to "S",
                    DayOfWeek.TUESDAY to "T",
                    DayOfWeek.WEDNESDAY to "Q",
                    DayOfWeek.THURSDAY to "Q",
                    DayOfWeek.FRIDAY to "S",
                    DayOfWeek.SATURDAY to "S"
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(EliteSpacing.sm)
                ) {
                    weekDays.forEach { (day, label) ->
                        val isSelected = diasSelecionados.contains(day)
                        val isNeighborDisabled = !isSelected && day in forbiddenNeighbors
                        val isLimitReached = !isSelected && diasSelecionados.size >= diasPorSemana
                        val isClickable = isSelected || (!isNeighborDisabled && !isLimitReached)

                        Card(
                            shape = CircleShape,
                            border = when {
                                isSelected -> BorderStroke(2.dp, Olive)
                                isNeighborDisabled -> BorderStroke(1.dp, LineStrong.copy(alpha = 0.15f))
                                else -> BorderStroke(1.dp, LineStrong.copy(alpha = 0.35f))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .then(
                                    if (isClickable) {
                                        Modifier.clickable { onDiaToggle(day) }
                                    } else {
                                        Modifier
                                    }
                                )
                                .testTag("day_toggle_${day.name}")
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isClickable) TextColor else TextMuted
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(EliteSpacing.md))

                // Texto obrigatório: "Escolha dias com um dia de descanso entre eles"
                Card(
                    border = BorderStroke(1.dp, LineStrong.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Olive,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(EliteSpacing.xs))
                        Text(
                            text = "Escolha dias com um dia de descanso entre eles. Os dias vizinhos são desabilitados automaticamente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(EliteSpacing.xl))

                // 3. Data de início
                Text(
                    text = "Data de início",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(EliteSpacing.md))
                
                var showDatePicker by remember { mutableStateOf(false) }
                if (showDatePicker) {
                    com.elite.sch.app.ui.components.EliteDatePickerDialog(
                        initial = dataInicio,
                        minDate = LocalDate.now(),
                        maxDate = LocalDate.now().plusMonths(3),
                        onConfirm = { 
                            onDataInicioChange(it)
                            showDatePicker = false
                        },
                        onDismiss = { showDatePicker = false }
                    )
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                    shape = EliteShape.md
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.lg),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Olive)
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Text(text = dateFormatter.format(dataInicio), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                
                Spacer(modifier = Modifier.height(EliteSpacing.lg))
                
                // Resumo vivo
                Text(
                    text = "$diasPorSemana dias por semana: ${diasSelecionados.sorted().joinToString(", ") { it.name.take(3).lowercase() }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Olive
                )
            }

            if (!errorMessage.isNullOrBlank()) {
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RedSoft,
                    border = BorderStroke(1.dp, Red.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("days_error_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.xl))

            val actualButtonText = if (isQuantityExact) {
                nextButtonText ?: "Continuar para a Duração"
            } else {
                "Selecione exatamente $diasPorSemana dias"
            }

            // Botão Avançar: só habilita com a quantidade certa de dias
            EliteButton(
                text = actualButtonText,
                onClick = onNextClick,
                enabled = isQuantityExact,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("days_next_button")
            )
        }
    }
