package com.elite.sch.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as TextColor

/**
 * Tela de seleção de modalidade (Hub Principal).
 * Substitui o fluxo antigo de "GoalSelection" com o novo visual de cards.
 * Chamada por MainActivity.kt no estado AppScreen.GoalSelection.
 */
@Composable
fun GoalSelectionScreen(
    onSelectModality: (String) -> Unit,
    onStartFreeRun: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(EliteSpacing.xl)
            .testTag("screen_goal_selection"),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escolha seu plano",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.padding(bottom = EliteSpacing.xl)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalCard(
                title = "Corrida livre",
                icon = Icons.Default.DirectionsRun,
                accentColor = Olive,
                onClick = onStartFreeRun,
                testTag = "btn_goal_corrida_livre"
            )
            GoalCard(
                title = "Treino 2.5k",
                icon = Icons.Default.Flag,
                accentColor = Brass,
                onClick = { onSelectModality("plano_2_5k") },
                testTag = "btn_goal_treino_25k"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalCard(
                title = "Treino 5k",
                icon = Icons.Default.Timer,
                accentColor = Red,
                onClick = { onSelectModality("plano_5k") },
                testTag = "btn_goal_treino_5k"
            )
            GoalCard(
                title = "Treino 10k",
                icon = Icons.Default.Speed,
                accentColor = Brass,
                onClick = { onSelectModality("plano_10k") },
                testTag = "btn_goal_treino_10k"
            )
        }
    }
}

/**
 * Card de opção de plano (estilo menu).
 */
@Composable
private fun RowScope.GoalCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .heightIn(min = 96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(accentColor)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.15f),
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.CenterEnd)
        )
    }
}