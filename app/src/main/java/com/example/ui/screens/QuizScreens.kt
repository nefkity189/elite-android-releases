package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleSelectableCard
import com.example.ui.components.AisleTopBar
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.components.StepProgressBar
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary

@Composable
fun QuizQuestionScreen(
    stepNumber: Int,
    totalSteps: Int = 3,
    questionTitle: String,
    questionSubtitle: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)

    if (isLoading) {
        LoadingState(message = "Processando respostas...", modifier = modifier)
        return
    }

    if (isEmpty || options.isEmpty()) {
        EmptyState(
            title = "Nenhuma opção encontrada",
            description = "Ocorreu um problema ao carregar as opções da pergunta.",
            actionLabel = "Voltar",
            onActionClick = onBackClick,
            modifier = modifier
        )
        return
    }

    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .statusBarsPadding()
            .padding(horizontal = AisleSpacing.xl)
    ) {
        AisleTopBar(
            title = "Pergunta $stepNumber de $totalSteps",
            onBack = onBackClick,
            backTestTag = "quiz_back_button"
        )

        Spacer(Modifier.height(AisleSpacing.sm))
        StepProgressBar(
            current = stepNumber,
            total = totalSteps,
            description = "Pergunta $stepNumber de $totalSteps"
        )
        Spacer(Modifier.height(AisleSpacing.xl))

        AnimatedContent(
            targetState = stepNumber,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(animationSpec = tween(AisleMotion.Enter)) { it } + fadeIn(tween(AisleMotion.Enter)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(AisleMotion.Exit)) { -it } + fadeOut(tween(AisleMotion.Exit)))
                } else {
                    (slideInHorizontally(animationSpec = tween(AisleMotion.Exit)) { -it } + fadeIn(tween(AisleMotion.Exit)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(AisleMotion.Enter)) { it } + fadeOut(tween(AisleMotion.Enter)))
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = "quizQuestionTransition"
        ) { currentStep ->
            androidx.compose.runtime.key(currentStep) {
                // Conteúdo rolável da pergunta
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                Text(
                    text = questionTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AisleTextPrimary
                )
                Spacer(Modifier.height(AisleSpacing.md))
                Text(
                    text = questionSubtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AisleTextSecondary
                )
                Spacer(Modifier.height(AisleSpacing.xl))

                Column(verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)) {
                    options.forEachIndexed { index, option ->
                        AisleSelectableCard(
                            text = option,
                            selected = option == selectedOption,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onOptionSelected(option)
                            },
                            modifier = Modifier.testTag("quiz_option_${stepNumber}_$index")
                        )
                    }
                }
                Spacer(Modifier.height(AisleSpacing.lg))
                }
            }
        }

        AisleButton(
            text = if (stepNumber == totalSteps) "Continuar para os Dias" else "Próxima pergunta",
            onClick = onNextClick,
            enabled = selectedOption != null,
            variant = AisleButtonVariant.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quiz_next_button")
        )
        Spacer(Modifier.height(AisleSpacing.lg))
        Spacer(Modifier.navigationBarsPadding())
    }
}
