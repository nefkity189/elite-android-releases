package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import com.example.data.local.entities.WorkoutEntity
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AisleCardBottom
import com.example.ui.theme.AislePurple
import com.example.ui.theme.AisleTextAccent2
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise

/**
 * Menu rápido acionado pelo ícone central "A" da navegação.
 * Permite iniciar Corrida Livre, dar play no treino agendado de hoje ou escolher/trocar de plano (2.5K, 5K, 10K).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleQuickLauncherBottomSheet(
    isOpen: Boolean,
    todayWorkout: WorkoutEntity? = null,
    activeModality: String = "plano_5k",
    onDismiss: () -> Unit,
    onStartFreeRun: () -> Unit,
    onStartTodayWorkout: (WorkoutEntity) -> Unit,
    onSelectModality: (String) -> Unit
) {
    if (!isOpen) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AisleCard,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
                .testTag("quick_launcher_bottom_sheet"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Cabeçalho
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Adicionar corrida",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = AisleTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botão RECOLHER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "RECOLHER",
                    style = MaterialTheme.typography.labelMedium,
                    color = AisleTextSecondary,
                    modifier = Modifier.clickable(onClick = onDismiss)
                )
            }

            // 1. Grid de opções conforme imagem
            // Primeira linha: Corrida livre | Treino 2.5k
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCard(
                    title = "Corrida livre",
                    icon = Icons.Default.DirectionsRun,
                    accentColor = AisleTurquoise,
                    onClick = {
                        onDismiss()
                        onStartFreeRun()
                    },
                    testTag = "btn_menu_corrida_livre"
                )
                MenuCard(
                    title = "Treino 2.5k",
                    icon = Icons.Default.Flag,
                    accentColor = AislePurple,
                    onClick = {
                        onDismiss()
                        onSelectModality("plano_2_5k")
                    },
                    testTag = "btn_menu_treino_25k"
                )
            }

            // Segunda linha: Treino 5k | Treino 10k
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCard(
                    title = "Treino 5k",
                    icon = Icons.Default.Timer,
                    accentColor = AisleActionOrange,
                    onClick = {
                        onDismiss()
                        onSelectModality("plano_5k")
                    },
                    testTag = "btn_menu_treino_5k"
                )
                MenuCard(
                    title = "Treino 10k",
                    icon = Icons.Default.Speed,
                    accentColor = AisleTextAccent2,
                    onClick = {
                        onDismiss()
                        onSelectModality("plano_10k")
                    },
                    testTag = "btn_menu_treino_10k"
                )
            }
        }
    }
}

// Card estilo menu conforme imagem (gradiente + ícone)
@Composable
private fun RowScope.MenuCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = accentColor,
        modifier = Modifier
            .weight(1f)
            .height(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
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
            // Ícone decorativo no canto direito
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
}

@Composable
private fun PlanOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val borderColor = if (isSelected) accentColor else AisleBorder
    val containerColor = if (isSelected) accentColor.copy(alpha = 0.08f) else AisleCardBottom

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = AisleTextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = AisleTextSecondary
                )
            }
            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Ativo",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = AisleTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
