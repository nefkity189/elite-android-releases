package com.elite.sch.app.ui.screens

import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.EliteTopBar
import com.elite.sch.app.ui.components.EliteButton
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.generator.GeneratedPlan
import com.elite.sch.app.generator.ProgressionTable
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.Text
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteShape

@Composable
fun PlanPreviewScreen(
    generatedPlan: GeneratedPlan?,
    onStartPlanClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)

    var hasAcceptedTerms by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingState(message = "Gerando seu plano personalizado...", modifier = modifier)
        return
    }

    if (isEmpty || generatedPlan == null) {
        EmptyState(
            title = "Plano não disponível",
            description = "Não encontramos dados suficientes para gerar a prévia do plano.",
            actionLabel = "Voltar e refazer",
            onActionClick = onBackClick,
            modifier = modifier
        )
        return
    }

    val dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM", java.util.Locale.forLanguageTag("pt-BR"))

    // Display name for the plan modality
    val generatedPlanModalidadeDisplay = generatedPlan.modalidade.replace("_", " ").lowercase()

    // Determina o total de etapas baseado no tipo de plano
    val totalEtapas = when (generatedPlanModalidadeDisplay) {
        "plano 2,5k" -> 6
        "plano 5k" -> 9
        "plano 10k" -> 12
        else -> 9
    }

    // Agrupa os treinos por semana para a lista de semanas
    val weeksGrouped = generatedPlan.treinos.groupBy { it.semanaIndex }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = EliteSpacing.screenHorizontal)
            .padding(top = EliteSpacing.md, bottom = EliteSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(EliteSpacing.lg)
    ) {
        // Top bar
        EliteTopBar(
            title = "Prévia do seu plano",
            onBack = onBackClick,
            backTestTag = "preview_back_button",
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = EliteSpacing.md)
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.lg)
        ) {
            item {
                Spacer(modifier = Modifier.height(EliteSpacing.md))
                // Resumo do plano
                EliteCard(
                    shape = EliteShape.lg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(EliteSpacing.lg)) {
                        Text(
                            text = "Plano ${generatedPlanModalidadeDisplay}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextColor
                        )
                        Spacer(modifier = Modifier.height(EliteSpacing.xs))
                        Text(
                            text = "${generatedPlan.totalSemanas} semanas • ${generatedPlan.diasPorSemana} treinos/semana • ${generatedPlan.treinos.size} treinos no total",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(EliteSpacing.md))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Início: ${dateFormatter.format(generatedPlan.dataInicio)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = Olive
                            )
                            Text(
                                text = "Meta 5K: ${dateFormatter.format(generatedPlan.dataFim)}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = Red
                            )
                        }
                    }
                }
            }

            // AVISO OBRIGATÓRIO DE SAÚDE
            item {
                EliteCard(
                    shape = EliteShape.lg,
                    border = BorderStroke(1.5.dp, Red.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.lg),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Red.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = "Aviso médico",
                                tint = Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aviso de Saúde",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Red
                            )
                            Spacer(modifier = Modifier.height(EliteSpacing.xs))
                            Text(
                                text = "Este app não substitui orientação médica. Se tiver dúvida sobre sua saúde, consulte um médico antes de começar.",
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp,
                                color = TextColor
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Cronograma semana a semana",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )
            }

            // Lista de semanas
            itemsIndexed(weeksGrouped.toList()) { _, (weekNum, workouts) ->
                val sampleWorkout = workouts.first()
                val stage = ProgressionTable.getStage(sampleWorkout.etapaIndex)

                EliteCard(
                    shape = EliteShape.md,
                    border = BorderStroke(1.dp, LineStrong.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(EliteSpacing.lg)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = EliteSpacing.sm, vertical = EliteSpacing.xxs)
                                    .background(CardElevated, EliteShape.sm)
                            ) {
                                Text(
                                    text = "Semana $weekNum",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextColor
                                )
                            }
                            Text(
                                text = "Etapa ${sampleWorkout.etapaIndex} de $totalEtapas",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(EliteSpacing.md))

                        Text(
                            text = stage.summary,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = TextColor
                        )

                        Spacer(modifier = Modifier.height(EliteSpacing.xs))

                        Text(
                            text = "${sampleWorkout.duracaoTotalMinutos} min total (${sampleWorkout.duracaoCorridaMinutos} min correndo) • ${workouts.size}x nesta semana",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(EliteSpacing.lg))
            }
        }

        // Bottom bar with checkbox and button
        Surface(
            color = Background,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = EliteSpacing.xl)
                    .padding(top = EliteSpacing.lg, bottom = EliteSpacing.xxl)
            ) {
                // Caixa de seleção obrigatória "Li e entendi"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(EliteShape.md)
                        .clickable { hasAcceptedTerms = !hasAcceptedTerms }
                        .padding(vertical = EliteSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = hasAcceptedTerms,
                        onCheckedChange = { hasAcceptedTerms = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Olive
                        ),
                        modifier = Modifier.testTag("checkbox_li_entendi")
                    )
                    Spacer(modifier = Modifier.width(EliteSpacing.sm))
                    Text(
                        text = "Li e entendi o aviso de saúde",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextColor
                    )
                }

                Spacer(modifier = Modifier.height(EliteSpacing.md))

                // Botão Iniciar plano - só habilita com a caixa marcada
                EliteButton(
                    text = "Começar meu plano",
                    onClick = onStartPlanClick,
                    enabled = hasAcceptedTerms,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_iniciar_plano")
                )
            }
        }
    }
}
