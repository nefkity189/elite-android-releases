package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleLogo
import com.example.ui.components.AisleTopBar
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.rememberAnimatedVisibilitySpecs
import com.example.ui.theme.rememberReducedMotion
import com.example.ui.theme.Background
import com.example.ui.theme.Card as CardColor
import com.example.ui.theme.Text
import com.example.ui.theme.TextMuted
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.Olive
import com.example.ui.theme.Red
import com.example.ui.theme.RedSoft
import com.example.ui.theme.Brass
import com.example.ui.theme.LineStrong
import com.example.ui.theme.CardElevated
import com.example.ui.theme.Text as TextColor
import com.example.ui.theme.AisleShape

@Composable
fun GoalScreen(
    currentModality: String = "plano_5k",
    onModalitySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val modalityOptions = remember {
        listOf(
            ModalityOption(
                id = "livre",
                title = "Corrida Livre",
                subtitle = "Sem plano, sem alvo. Apenas você e o caminho.",
                icon = Icons.Filled.DirectionsRun,
                accentColor = Olive,
                description = "Corra ou caminhe quando quiser, sem metas. Avisos de voz a cada quilômetro."
            ),
            ModalityOption(
                id = "plano_2_5k",
                title = "Primeiros 2,5K",
                subtitle = "6 semanas • 3 treinos/semana • Meta: 2,5 km",
                icon = Icons.Filled.Flag,
                accentColor = Brass,
                description = "Plano profissional para quem começa do zero. Aquecimento e desaquecimento de 5 min. Progressão gradual com corrida/caminhada alternada."
            ),
            ModalityOption(
                id = "plano_5k",
                title = "Primeiros 5K",
                subtitle = "9 semanas • 2 ou 3 treinos/semana • Meta: 5 km",
                icon = Icons.Filled.Timer,
                accentColor = Red,
                description = "O plano clássico do Aisle. 9 etapas progressivas de caminhada e corrida. Você define os dias e a duração."
            ),
            ModalityOption(
                id = "plano_10k",
                title = "Primeiros 10K",
                subtitle = "12 semanas • 3 treinos/semana • Meta: 10 km",
                icon = Icons.Filled.Speed,
                accentColor = Brass,
                description = "Plano avançado para quem já corre 5K. Inclui treinos intervalados, longos e ritmo de prova. Requer base de 20-30 min contínuos."
            )
        )
    }

    var selectedModality by remember { mutableStateOf(currentModality) }
    val selectedOption = modalityOptions.find { it.id == selectedModality } ?: modalityOptions[0]

    LaunchedEffect(currentModality) {
        if (currentModality != selectedModality) {
            selectedModality = currentModality
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .padding(horizontal = AisleSpacing.screenHorizontal)
    ) {
        AisleTopBar(
            title = "Escolha seu objetivo",
            onBack = onBackClick,
            backTestTag = "goal_back_button"
        )

        Spacer(Modifier.height(AisleSpacing.md))

        // Logo e título
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AisleLogo(symbolSize = 36.dp)
            Spacer(Modifier.height(AisleSpacing.md))
            Text(
                text = "Como você quer correr hoje?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(AisleSpacing.xs))
            Text(
                text = "Toque em uma modalidade para ver os detalhes",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(AisleSpacing.xl))

        // Grid de modalidades (cards expansíveis)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
        ) {
            modalityOptions.forEach { option ->
                ModalityCard(
                    option = option,
                    isSelected = selectedModality == option.id,
                    onClick = {
                        selectedModality = option.id
                    }
                )
            }
            Spacer(Modifier.height(AisleSpacing.md))
        }

        // Botão continuar único e claro
        AisleButton(
            text = "Continuar com ${selectedOption.title}",
            onClick = {
                onModalitySelected(selectedModality)
                onContinueClick()
            },
            variant = AisleButtonVariant.Primary,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AisleSpacing.md)
                .testTag("goal_continue_button")
        )

        Spacer(Modifier.navigationBarsPadding())
    }
}

@Composable
private fun ModalityCard(
    option: ModalityOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 2.dp else 1.dp
    val borderColor = if (isSelected) option.accentColor else LineStrong
    val containerColor = if (isSelected) option.accentColor.copy(alpha = 0.12f) else CardElevated
    val reducedMotion = rememberReducedMotion()
    val specs = rememberAnimatedVisibilitySpecs(reduceMotion = reducedMotion)

    Surface(
        shape = AisleShape.md,
        color = containerColor,
        border = androidx.compose.foundation.BorderStroke(borderWidth, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícone
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(option.accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = option.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(AisleSpacing.md))

                // Texto Título + Subtítulo
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = option.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = TextColor
                    )
                    Spacer(Modifier.height(AisleSpacing.xs))
                    Text(
                        text = option.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }

                Spacer(Modifier.width(AisleSpacing.sm))

                // Ícone de Seleção / Chevron
                Icon(
                    imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = if (isSelected) option.accentColor else TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Conteúdo detalhado expansível
            AnimatedVisibility(
                visible = isSelected,
                enter = specs.enter,
                exit = specs.exit
            ) {
                Column {
                    Spacer(Modifier.height(AisleSpacing.md))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(option.accentColor.copy(alpha = 0.25f))
                    )
                    Spacer(Modifier.height(AisleSpacing.md))

                    Text(
                        text = option.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextColor
                    )

                    Spacer(Modifier.height(AisleSpacing.md))

                    when (option.id) {
                        "livre" -> FreeRunHighlights()
                        "plano_2_5k" -> Plan25KHighlights()
                        "plano_5k" -> Plan5KHighlights()
                        "plano_10k" -> Plan10KHighlights()
                    }
                }
            }
        }
    }
}

@Composable
private fun FreeRunHighlights() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HighlightRow("Voz a cada km", "Avisos sonoros automáticos a cada quilômetro completo")
        HighlightRow("Sem meta", "Corra ou caminhe o tempo que quiser")
        HighlightRow("Ritmo livre", "O app mostra seu ritmo em tempo real")
    }
}

@Composable
private fun Plan25KHighlights() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HighlightRow("6 semanas", "Programa profissional com 18 sessões + prova final")
        HighlightRow("Por distância", "Treinos concluídos ao completar a distância alvo (ex: 1,5 km, 2,0 km...)")
        HighlightRow("Aquecimento 5 min", "Caminhada leve antes e depois de cada sessão")
        HighlightRow("Dias fixos", "Terça, Quinta, Sábado (Prova no Domingo da semana 6)")
    }
}

@Composable
private fun Plan5KHighlights() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HighlightRow("9 etapas", "Progressão clássica de caminhada para corrida contínua")
        HighlightRow("Você escolhe", "2 ou 3 treinos por semana, dias que preferir")
        HighlightRow("Ajuste inteligente", "O plano se adapta se você pular ou sentir dor")
    }
}

@Composable
private fun Plan10KHighlights() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HighlightRow("12 semanas", "Programa completo com treinos intervalados e longos")
        HighlightRow("Requisito", "Já corre 20-30 min contínuos ou concluiu o plano 5K")
        HighlightRow("Ritmo de prova", "Treinos com alvo de ritmo específico para os 10K")
    }
}

@Composable
private fun HighlightRow(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Olive)
        )
        Spacer(Modifier.width(AisleSpacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = TextColor)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
    }
}

data class ModalityOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val accentColor: Color,
    val description: String
)