package com.elite.sch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.EliteShimmerBox
import com.elite.sch.app.ui.components.EliteSkeleton
import com.elite.sch.app.ui.components.EliteTopBar
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.EliteShape



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationScreen(
    totalSemanas: Int,
    minSemanas: Int = 6,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {},
    onTotalSemanasChange: (Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)

    if (isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = EliteSpacing.screenHorizontal)
                .padding(top = EliteSpacing.md, bottom = EliteSpacing.xxl),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.xl)
        ) {
            EliteTopBar(
                title = "Duração do plano",
                onBack = onBackClick,
                backTestTag = "duration_back_button",
                modifier = Modifier.fillMaxWidth().padding(horizontal = EliteSpacing.md)
            )
            EliteSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 36.dp,
                shape = RoundedCornerShape(8.dp),
                count = 1
            )
            EliteSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 16.dp,
                shape = RoundedCornerShape(8.dp),
                count = 2,
                spacingDp = EliteSpacing.xs
            )
            EliteShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                shape = EliteShape.lg,
                heightDp = 280.dp
            )
            EliteSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 16.dp,
                shape = RoundedCornerShape(8.dp),
                count = 4,
                spacingDp = EliteSpacing.xs
            )
            Spacer(modifier = Modifier.height(EliteSpacing.xl))
            EliteShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                shape = EliteShape.md,
                heightDp = 56.dp
            )
        }
        return
    }

    if (isEmpty) {
        EmptyState(
            title = "Duração indisponível",
            description = "Ocorreu um erro ao carregar os parâmetros de duração.",
            actionLabel = "Voltar",
            onActionClick = onBackClick,
            modifier = modifier
        )
        return
    }

    val scrollState = rememberScrollState()

    val descriptionText = when {
        totalSemanas <= 8 -> "Ritmo mais puxado. Comprime as etapas para avançar com determinação."
        totalSemanas <= 11 -> "Ritmo equilibrado e clássico (Padrão). Permite avanço consistente sem sobrecarga."
        else -> "Ritmo mais gradual e seguro. Repete etapas para criar uma base sólida e confortável."
    }

    val quickChoices = if (minSemanas >= 8) listOf(8, 9, 12, 16) else listOf(6, 9, 12, 16)
    val sliderSteps = (16 - minSemanas - 1).coerceAtLeast(0)

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
            title = "Duração do plano",
            onBack = onBackClick,
            backTestTag = "duration_back_button",
            modifier = Modifier.fillMaxWidth().padding(horizontal = EliteSpacing.md)
        )
        errorMessage?.let { message ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Red)
            ) {
                Row(
                    modifier = Modifier.padding(EliteSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(EliteSpacing.sm))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor
                    )
                }
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Em quantas semanas deseja alcançar os 5K?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(EliteSpacing.md))

            // Texto obrigatório solicitado:
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mais curto = mais puxado. Mais longo = mais gradual.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor,
                    modifier = Modifier.padding(EliteSpacing.lg)
                )
            }

            if (minSemanas >= 8) {
                Spacer(modifier = Modifier.height(EliteSpacing.lg))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.lg),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Olive,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Text(
                            text = "Como você não se exercita atualmente ('Nada'), a duração mínima provisória é de 8 semanas para proteção do seu corpo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.xxl))

            // Display visual do número de semanas
            Card(
                shape = EliteShape.xl,
                border = BorderStroke(1.dp, LineStrong.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(EliteSpacing.xl),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalSemanas",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = Olive
                    )
                    Text(
                        text = if (totalSemanas == 1) "semana" else "semanas",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(EliteSpacing.lg))

                    // Slider com faixa de minSemanas a 16
                    Slider(
                        value = totalSemanas.coerceIn(minSemanas, 16).toFloat(),
                        onValueChange = { onTotalSemanasChange(it.roundToInt()) },
                        valueRange = minSemanas.toFloat()..16f,
                        steps = sliderSteps,
                        colors = SliderDefaults.colors(
                            thumbColor = Olive,
                            activeTrackColor = Olive,
                            inactiveTrackColor = CardElevated
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("duration_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$minSemanas sem (Mín)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Text(
                            text = "9 sem (Padrão)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Olive
                        )
                        Text(
                            text = "16 sem (Máx)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(EliteSpacing.lg))

                    Text(
                        text = descriptionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(EliteSpacing.md))

                    Text(
                        text = "Estes valores e etapas são provisórios e requerem validação médica.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(EliteSpacing.xl))

            // Escolha rápida
            Text(
                text = "Escolha rápida",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(EliteSpacing.md))

            // Grupo centralizado de botões de escolha rápida
            Box(
                modifier = Modifier.width(320.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(EliteSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    quickChoices.forEach { weeks ->
                        val isSelected = weeks == totalSemanas
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) BorderStroke(2.dp, Olive) else BorderStroke(1.dp, LineStrong),
                            modifier = Modifier
                                .width(72.dp)
                                .clickable { onTotalSemanasChange(weeks) }
                                .testTag("quick_weeks_$weeks")
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = EliteSpacing.md)
                            ) {
                                Text(
                                    text = "${weeks} sem",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Olive else TextColor
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(EliteSpacing.xl))

        // Botão Avançar
        EliteButton(
            text = "Ver prévia do plano",
            onClick = onNextClick,
            trailingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("duration_next_button")
        )
    }
}