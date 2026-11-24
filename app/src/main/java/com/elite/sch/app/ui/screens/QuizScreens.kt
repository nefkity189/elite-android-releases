package com.elite.sch.app.ui.screens

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
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteButtonVariant
import com.elite.sch.app.ui.components.EliteSelectableCard
import com.elite.sch.app.ui.components.EliteTopBar
import com.elite.sch.app.ui.components.EmptyState
import com.elite.sch.app.ui.components.LoadingState
import com.elite.sch.app.ui.components.StepProgressBar
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted

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
            .background(Background)
            .statusBarsPadding()
            .padding(horizontal = EliteSpacing.xl)
    ) {
        EliteTopBar(
            title = "Pergunta $stepNumber de $totalSteps",
            onBack = onBackClick,
            backTestTag = "quiz_back_button"
        )

        Spacer(Modifier.height(EliteSpacing.sm))
        StepProgressBar(
            current = stepNumber,
            total = totalSteps,
            description = "Pergunta $stepNumber de $totalSteps"
        )
        Spacer(Modifier.height(EliteSpacing.xl))

        AnimatedContent(
            targetState = stepNumber,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(animationSpec = tween(EliteMotion.Enter)) { it } + fadeIn(tween(EliteMotion.Enter)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(EliteMotion.Exit)) { -it } + fadeOut(tween(EliteMotion.Exit)))
                } else {
                    (slideInHorizontally(animationSpec = tween(EliteMotion.Exit)) { -it } + fadeIn(tween(EliteMotion.Exit)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(EliteMotion.Enter)) { it } + fadeOut(tween(EliteMotion.Enter)))
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
                    color = TextColor
                )
                Spacer(Modifier.height(EliteSpacing.md))
                Text(
                    text = questionSubtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted
                )
                Spacer(Modifier.height(EliteSpacing.xl))

                Column(verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
                    options.forEachIndexed { index, option ->
                        EliteSelectableCard(
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
                Spacer(Modifier.height(EliteSpacing.lg))
                }
            }
        }

        EliteButton(
            text = if (stepNumber == totalSteps) "Continuar para os Dias" else "Próxima pergunta",
            onClick = onNextClick,
            enabled = selectedOption != null,
            variant = EliteButtonVariant.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quiz_next_button")
        )
        Spacer(Modifier.height(EliteSpacing.lg))
        Spacer(Modifier.navigationBarsPadding())
    }
}
