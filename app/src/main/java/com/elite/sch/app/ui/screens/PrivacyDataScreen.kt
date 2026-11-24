package com.elite.sch.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.R
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted

@Composable
fun PrivacyDataScreen(
    hasBackupFile: Boolean,
    onExportBackup: () -> Unit,
    onShareBackup: () -> Unit,
    onImportBackup: () -> Unit,
    onDeleteBackup: () -> Unit,
    onClearAllData: (onResult: (Boolean) -> Unit) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var showFirstDeleteDialog by remember { mutableStateOf(false) }
    var showFinalDeleteDialog by remember { mutableStateOf(false) }
    var showHealthNotice by remember { mutableStateOf(false) }
    var clearAllDataError by remember { mutableStateOf(false) }

    if (showFirstDeleteDialog) {
        EliteDialog(
            title = "Apagar todos os dados?",
            text = "Todo o seu histórico de corridas, treinos concluídos, rotas gravadas, anotações e o plano atual serão apagados permanentemente.",
            onDismiss = { showFirstDeleteDialog = false },
            confirmLabel = "Continuar",
            onConfirm = {
                showFirstDeleteDialog = false
                showFinalDeleteDialog = true
            },
            destructive = true,
            icon = Icons.Filled.DeleteForever,
            confirmTestTag = "btn_confirm_delete_step1"
        )
    }

    if (showFinalDeleteDialog) {
        EliteDialog(
            title = "Confirmar exclusão definitiva",
            text = "Esta ação é irreversível. Todos os dados serão excluídos do seu aparelho agora e o aplicativo voltará ao estado inicial.",
            onDismiss = { showFinalDeleteDialog = false },
            confirmLabel = "Sim, apagar tudo",
            onConfirm = {
                showFinalDeleteDialog = false
                onClearAllData { success ->
                    clearAllDataError = !success
                }
            },
            destructive = true,
            icon = Icons.Filled.DeleteForever,
            confirmTestTag = "btn_confirm_delete_step2"
        )
    }

    if (clearAllDataError) {
        EliteDialog(
            title = "Erro ao apagar dados",
            text = "Não foi possível apagar todos os dados. Tente novamente.",
            onDismiss = { clearAllDataError = false },
            confirmLabel = "OK",
            onConfirm = { clearAllDataError = false }
        )
    }

    if (showHealthNotice) {
        EliteDialog(
            title = "Aviso de saúde",
            text = "Este app não substitui orientação médica. Se tiver dúvida sobre sua saúde, consulte um médico antes de começar.",
            onDismiss = { showHealthNotice = false },
            confirmLabel = "Entendi",
            onConfirm = { showHealthNotice = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = EliteSpacing.screenHorizontal)
            .padding(top = 0.dp, bottom = EliteSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(EliteSpacing.lg)
    ) {
        Text(
            text = "PRIVACIDADE E DADOS",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
            fontFamily = EliteDisplayFont,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            border = BorderStroke(1.dp, Line),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = EliteSpacing.md)) {
                PrivacyActionRow(
                    icon = Icons.Outlined.FileUpload,
                    title = "Exportar backup",
                    onClick = onExportBackup,
                    testTag = "btn_export_backup"
                )
                PrivacyDivider()
                PrivacyActionRow(
                    icon = Icons.Outlined.FileDownload,
                    title = "Restaurar backup",
                    onClick = onImportBackup,
                    testTag = "btn_import_backup"
                )
                PrivacyDivider()
                PrivacyActionRow(
                    icon = Icons.Outlined.Share,
                    title = "Compartilhar backup",
                    onClick = onShareBackup,
                    testTag = "btn_share_backup"
                )
                PrivacyDivider()
                PrivacyActionRow(
                    icon = Icons.Outlined.Delete,
                    title = "Excluir backup local",
                    onClick = onDeleteBackup,
                    enabled = hasBackupFile,
                    testTag = "btn_delete_backup"
                )
            }
        }

        PrivacyActionCard(
            icon = Icons.Outlined.HealthAndSafety,
            title = "Aviso de saúde",
            iconTint = Color(0xFFE0A93A),
            iconBackground = Color(0x29E0A93A),
            testTag = "card_health_notice",
            onClick = { showHealthNotice = true }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardColor, RoundedCornerShape(18.dp))
                .border(BorderStroke(1.dp, Red.copy(alpha = 0.45f)), RoundedCornerShape(18.dp))
                .clickable { showFirstDeleteDialog = true }
                .padding(horizontal = EliteSpacing.md, vertical = EliteSpacing.md)
                .testTag("btn_apagar_todos_dados"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)
        ) {
            Icon(Icons.Filled.DeleteForever, contentDescription = null, tint = Red, modifier = Modifier.size(22.dp))
            Text(
                text = "Apagar todos os meus dados",
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Red
            )
        }

        Spacer(modifier = Modifier.height(74.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            border = BorderStroke(1.dp, Line),
            modifier = Modifier.fillMaxWidth().testTag("card_about_app")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(EliteSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
                    Image(
                        painter = painterResource(R.drawable.ic_elite_mark),
                        contentDescription = null,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = "ELITE",
                        fontFamily = EliteLogoFont,
                        fontSize = 26.sp,
                        letterSpacing = 6.24.sp,
                        color = TextColor
                    )
                }
                Spacer(modifier = Modifier.height(EliteSpacing.md))
                Text(
                    text = "Sobre o Elite · Versão 1.0.0 (Build 1)\n100% nativo e local",
                    fontFamily = EliteBodyFont,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PrivacyActionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 10.dp)
            .height(40.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(OliveSoft, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Olive, modifier = Modifier.size(22.dp))
            }
            Text(
                text = title,
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (enabled) TextColor else TextMuted
            )
        }
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = TextColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun PrivacyDivider() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = EliteSpacing.xxs)
            .height(1.dp)
            .background(LineStrong)
    )
}

@Composable
private fun PrivacyActionCard(
    icon: ImageVector,
    title: String,
    iconTint: Color,
    iconBackground: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColor, RoundedCornerShape(18.dp))
            .border(BorderStroke(1.dp, Line), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(EliteSpacing.md)
            .height(40.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBackground, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            }
            Text(
                text = title,
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextColor
            )
        }
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = TextColor,
            modifier = Modifier.size(22.dp)
        )
    }
}
