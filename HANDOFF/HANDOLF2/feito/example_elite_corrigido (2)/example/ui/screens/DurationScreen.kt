package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import com.example.ui.components.AisleDialog
import com.example.ui.components.AisleShimmerBox
import com.example.ui.components.AisleSkeleton
import com.example.ui.components.AisleTopBar
import com.example.ui.components.AisleButton
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
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
import com.example.ui.theme.Background
import com.example.ui.theme.Card as CardColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.Olive
import com.example.ui.theme.Red
import com.example.ui.theme.LineStrong
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.CardElevated
import com.example.ui.theme.Text as TextColor
import com.example.ui.theme.AisleShape



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

    if (errorMessage != null) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = AisleSpacing.md),
            border = BorderStroke(1.dp, Red)
        ) {
            Row(
                modifier = Modifier.padding(AisleSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(AisleSpacing.sm))
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Red
                )
            }
        }
    }

    if (isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = AisleSpacing.screenHorizontal)
                .padding(top = AisleSpacing.md, bottom = AisleSpacing.xxl),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.xl)
        ) {
            AisleTopBar(
                title = "Duração do plano",
                onBack = onBackClick,
                backTestTag = "duration_back_button",
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = AisleSpacing.md)
            )
            AisleSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 36.dp,
                shape = RoundedCornerShape(8.dp),
                count = 1
            )
            AisleSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 16.dp,
                shape = RoundedCornerShape(8.dp),
                count = 2,
                spacingDp = AisleSpacing.xs
            )
            AisleShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                shape = AisleShape.lg,
                heightDp = 280.dp
            )
            AisleSkeleton(
                modifier = Modifier.fillMaxWidth(),
                heightDp = 16.dp,
                shape = RoundedCornerShape(8.dp),
                count = 4,
                spacingDp = AisleSpacing.xs
            )
            Spacer(modifier = Modifier.height(AisleSpacing.xl))
            AisleShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                shape = AisleShape.md,
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
            .verticalScroll(scrollState)
            .padding(horizontal = AisleSpacing.screenHorizontal)
            .padding(top = AisleSpacing.md, bottom = AisleSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(AisleSpacing.xl)
    ) {
        // Top bar
        AisleTopBar(
            title = "Duração do plano",
            onBack = onBackClick,
            backTestTag = "duration_back_button",
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = AisleSpacing.md)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Em quantas semanas deseja alcançar os 5K?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(AisleSpacing.md))

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
                    modifier = Modifier.padding(AisleSpacing.lg)
                )
            }

            if (minSemanas >= 8) {
                Spacer(modifier = Modifier.height(AisleSpacing.lg))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(AisleSpacing.lg),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Olive,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.md))
                        Text(
                            text = "Como você não se exercita atualmente ('Nada'), a duração mínima provisória é de 8 semanas para proteção do seu corpo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.xxl))

            // Display visual do número de semanas
            Card(
                shape = AisleShape.xl,
                border = BorderStroke(1.dp, LineStrong.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.xl),
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

                    Spacer(modifier = Modifier.height(AisleSpacing.lg))

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

                    Spacer(modifier = Modifier.height(AisleSpacing.lg))

                    Text(
                        text = descriptionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AisleSpacing.md))

                    Text(
                        text = "Estes valores e etapas são provisórios e requerem validação médica.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.xl))

            // Escolha rápida
            Text(
                text = "Escolha rápida",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )

            Spacer(modifier = Modifier.height(AisleSpacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md)
            ) {
                quickChoices.forEach { weeks ->
                    val isSelected = weeks == totalSemanas
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        border = if (isSelected) BorderStroke(2.dp, Olive) else BorderStroke(1.dp, LineStrong),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTotalSemanasChange(weeks) }
                            .testTag("quick_weeks_$weeks")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = AisleSpacing.md)
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

        Spacer(modifier = Modifier.height(AisleSpacing.xl))

        // Botão Avançar
        AisleButton(
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