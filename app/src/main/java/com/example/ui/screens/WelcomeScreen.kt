package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.rememberAnimatedVisibilitySpecs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleLogo
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.components.aisleTextFieldColors
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AislePurple
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.rememberReducedMotion
import com.example.ui.theme.AisleError

@Composable
fun WelcomeScreen(
    userName: String = "",
    onUserNameChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LoadingState(message = "Preparando sua jornada...", modifier = modifier)
        return
    }

    if (isEmpty) {
        EmptyState(
            title = "Bem-vindo ao Aisle",
            description = "Nenhum dado encontrado no momento.",
            actionLabel = "Começar",
            onActionClick = onStartClick,
            modifier = modifier
        )
        return
    }

    // Nome obrigatório: bloquear avanço se vazio ou só espaços (spec §2)
    val trimmedName = userName.trim()
    val isNameValid = trimmedName.isNotBlank()
    var showNameError by remember { mutableStateOf(false) }

    // Entrada única: fade + subida de 16 dp em 300 ms (sem movimento se reduzido)
    val reducedMotion = rememberReducedMotion()
    var visible by remember { mutableStateOf(reducedMotion) }
    LaunchedEffect(Unit) { visible = true }

    val density = LocalDensity.current
    val slideOffsetPx = remember(density) { with(density) { 16.dp.roundToPx() } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AisleTurquoise.copy(alpha = 0.08f),
                        AislePurple.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(500f, 0f),
                    radius = 1000f
                )
            )
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AisleSpacing.xl, vertical = AisleSpacing.xl)
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = if (reducedMotion) fadeIn(tween(100))
                    else fadeIn(tween(300)) + slideInVertically(tween(300)) { slideOffsetPx }
                ) {
                    Column {
                        AisleLogo(symbolSize = 48.dp)
                        Spacer(modifier = Modifier.height(AisleSpacing.xxl))
                        Text(
                            text = "Do zero\naos 5 km",
                            style = MaterialTheme.typography.displayMedium,
                            color = AisleTextPrimary
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.lg))
                        Text(
                            text = "Um plano de caminhada e corrida que respeita o seu fôlego, do primeiro passo até os 5 km.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AisleTextSecondary
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.xxl))
                        Column(verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)) {
                            BenefitsStagger(
                                benefits = listOf(
                                    Benefit(Icons.Filled.PhoneAndroid, "Tudo no seu celular", "Sem login e sem nuvem."),
                                    Benefit(Icons.Filled.TrendingUp, "Progressivo", "A corrida aumenta aos poucos."),
                                    Benefit(Icons.Filled.Schedule, "Sem pressa", "Você escolhe os dias e a duração.")
                                ),
                                reduceMotion = reducedMotion
                            )
                        }
                        Spacer(modifier = Modifier.height(AisleSpacing.xxl))
                        Column(verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
                            OutlinedTextField(
                                value = userName,
                                onValueChange = onUserNameChange,
                                label = { Text("Como devemos te chamar?") },
                                placeholder = { Text("Seu nome ou apelido") },
                                singleLine = true,
                                shape = AisleShape.md,
                                colors = aisleTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_user_name"),
                                isError = showNameError
                            )
                            if (showNameError) {
                                Text(
                                    text = "O nome é obrigatório para continuar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AisleError
                                )
                            }
                        }
                    }
                }
            }

            // CTA fixo na base, acima do teclado e das barras do sistema
            AisleButton(
                text = "Começar",
                onClick = {
                    if (isNameValid) {
                        showNameError = false
                        onStartClick()
                    } else {
                        showNameError = true
                    }
                },
                variant = AisleButtonVariant.Primary,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AisleSpacing.xl)
                    .testTag("btn_comecar")
            )
            Spacer(modifier = Modifier.height(AisleSpacing.lg))
            Spacer(Modifier.navigationBarsPadding().imePadding())
        }
    }
}

@Composable
private fun BenefitRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AisleCard)
                .border(1.dp, AisleBorder, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AisleTurquoise,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(AisleSpacing.lg))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleSmall, color = AisleTextPrimary)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = AisleTextSecondary)
        }
    }
}

private data class Benefit(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
private fun BenefitsStagger(
    benefits: List<Benefit>,
    reduceMotion: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        benefits.forEachIndexed { index, benefit ->
            val specs = rememberAnimatedVisibilitySpecs(
                reduceMotion = reduceMotion,
                enterDuration = AisleMotion.Enter,
                enterDelay = if (reduceMotion) 0 else index * AisleMotion.StaggerDelay,
                exitDelay = if (reduceMotion) 0 else (benefits.size - 1 - index) * AisleMotion.StaggerDelay
            )
            AnimatedVisibility(
                visible = true,
                enter = specs.enter,
                exit = specs.exit
            ) {
                BenefitRow(benefit.icon, benefit.title, benefit.subtitle)
            }
        }
    }
}
