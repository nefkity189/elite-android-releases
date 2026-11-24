package com.example.ui.screens

import com.example.ui.components.ActionRow
import com.example.ui.components.Footnote
import com.example.ui.components.Hairline
import com.example.ui.components.InfoRow
import com.example.ui.components.NameEditor
import com.example.ui.components.NavRow
import com.example.ui.components.PrivacyInfoRow
import com.example.ui.components.ProfileHeader
import com.example.ui.components.RadioList
import com.example.ui.components.SectionTitle
import com.example.ui.components.SegmentedControl
import com.example.ui.components.SettingsGroup
import com.example.ui.components.SettingsNotice
import com.example.ui.components.SwitchRow
import com.example.ui.components.ValueRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleDialog
import com.example.ui.components.AisleTopBar
import com.example.ui.components.AisleStepper
import com.example.ui.theme.Background
import com.example.ui.theme.Card
import com.example.ui.theme.Line
import com.example.ui.theme.LineStrong
import com.example.ui.theme.Text
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextDim
import com.example.ui.theme.Olive
import com.example.ui.theme.OliveDark
import com.example.ui.theme.OliveSoft
import com.example.ui.theme.OnOlive
import com.example.ui.theme.Red
import com.example.ui.theme.OnRed
import com.example.ui.theme.Brass
import com.example.ui.theme.Amber
import com.example.ui.theme.Ok
import com.example.ui.theme.EliteBodyFont
import com.example.ui.theme.EliteDisplayFont
import com.example.ui.theme.EliteLogoFont
import com.example.ui.theme.EliteNumberStyles
import com.example.ui.theme.EliteTextStyles
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
import androidx.compose.foundation.shape.RoundedCornerShape


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    voiceEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    lembretesAtivos: Boolean = false,
    lembreteHora: Int = 7,
    lembreteMinuto: Int = 0,
    avisoDistanciaAtivo: Boolean = true,
    avisoDistanciaMetros: Int = 1000,
    showMapDuringRun: Boolean = true,
    defaultCameraMode: String = "follow_user",
    onUserNameChange: (String) -> Unit,
    onVoiceToggle: (Boolean) -> Unit = {},
    onVibrationToggle: (Boolean) -> Unit = {},
    onLembretesChange: (ativos: Boolean, hora: Int, minuto: Int) -> Unit = { _, _, _ -> },
    onAvisoDistanciaAtivoToggle: (Boolean) -> Unit = {},
    onAvisoDistanciaMetrosChange: (Int) -> Unit = {},
    onShowMapDuringRunToggle: (Boolean) -> Unit = {},
    onDefaultCameraModeChange: (String) -> Unit = {},
    onClearAllData: (onResult: (Boolean) -> Unit) -> Unit = { _ -> },
    onExportBackup: () -> Unit = {},
    onShareBackup: () -> Unit = {},
    onImportBackup: () -> Unit = {},
    onDeleteBackup: () -> Unit = {},
    hasBackupFile: Boolean = false,
    ajustesAutomaticos: Boolean = true,
    onOpenAdjustments: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nameInput by remember(userName) { mutableStateOf(userName) }
    var nameSavedMessage by remember { mutableStateOf(false) }
    var showNameEditor by remember { mutableStateOf(false) }

    var showTimeDialog by remember { mutableStateOf(false) }
    var selectedHour by remember(lembreteHora) { mutableIntStateOf(lembreteHora) }
    var selectedMinute by remember(lembreteMinuto) { mutableIntStateOf(lembreteMinuto) }

    val hasNotificationPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    var showPermissionDeniedNotice by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showPermissionDeniedNotice = false
            onLembretesChange(true, lembreteHora, lembreteMinuto)
        } else {
            showPermissionDeniedNotice = true
            onLembretesChange(false, lembreteHora, lembreteMinuto)
        }
    }

    // Diálogos de confirmação em duas etapas para "Apagar todos os meus dados"
    var showStep1DeleteDialog by remember { mutableStateOf(false) }
    var showStep2DeleteDialog by remember { mutableStateOf(false) }
    var clearAllDataError by remember { mutableStateOf<String?>(null) }

    // Etapa 1
    if (showStep1DeleteDialog) {
        AisleDialog(
            title = "Apagar todos os dados?",
            text = "Todo o seu histórico de corridas, treinos concluídos, rotas gravadas, anotações e o plano atual serão apagados permanentemente.",
            onDismiss = { showStep1DeleteDialog = false },
            confirmLabel = "Continuar",
            onConfirm = {
                showStep1DeleteDialog = false
                showStep2DeleteDialog = true
            },
            destructive = true,
            icon = Icons.Outlined.Warning,
            confirmTestTag = "btn_confirm_delete_step1"
        )
    }

    // Etapa 2
    if (showStep2DeleteDialog) {
        AisleDialog(
            title = "Confirmar exclusão definitiva",
            text = "Esta ação é irreversível. Todos os dados serão excluídos do seu aparelho agora e o aplicativo voltará ao estado inicial.",
            onDismiss = { showStep2DeleteDialog = false },
            confirmLabel = "Sim, apagar tudo",
            onConfirm = {
                showStep2DeleteDialog = false
                onClearAllData { success ->
                    if (!success) {
                        clearAllDataError = "Não foi possível apagar todos os dados. Tente novamente."
                    } else {
                        clearAllDataError = null
                    }
                }
            },
            destructive = true,
            icon = Icons.Outlined.Warning,
            confirmTestTag = "btn_confirm_delete_step2"
        )
    }

    // Exibe erro de exclusão se houver
    if (clearAllDataError != null) {
        AisleDialog(
            title = "Erro ao apagar dados",
            text = clearAllDataError!!,
            onDismiss = { clearAllDataError = null },
            confirmLabel = "OK",
            onConfirm = { clearAllDataError = null }
        )
    }

    if (showTimeDialog) {
        AisleDialog(
            title = "Horário do lembrete",
            text = "Escolha a hora e o minuto da notificação.",
            onDismiss = { showTimeDialog = false },
            confirmLabel = "Confirmar",
            onConfirm = {
                showTimeDialog = false
                onLembretesChange(lembretesAtivos, selectedHour, selectedMinute)
            },
            confirmTestTag = "btn_confirm_time"
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = String.format(java.util.Locale.ROOT, "%02d:%02d", selectedHour, selectedMinute),
                    style = MaterialTheme.typography.displayMedium,
                    color = Olive
                )
                Text("Hora", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                AisleStepper(
                    value = selectedHour,
                    onValueChange = { selectedHour = it },
                    range = 0..23,
                    label = "hora",
                    valueText = String.format(java.util.Locale.ROOT, "%02d", selectedHour),
                    wrap = true,
                    minusTestTag = "btn_hour_minus",
                    plusTestTag = "btn_hour_plus"
                )
                Text("Minuto", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                AisleStepper(
                    value = selectedMinute,
                    onValueChange = { selectedMinute = it },
                    range = 0..59,
                    label = "minuto",
                    valueText = String.format(java.util.Locale.ROOT, "%02d", selectedMinute),
                    step = 5,
                    wrap = true,
                    minusTestTag = "btn_min_minus",
                    plusTestTag = "btn_min_plus"
                )
            }
        }
    }

    Scaffold(
        topBar = {
            AisleTopBar(
                title = "Perfil",
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },
        containerColor = Background,
        modifier = modifier.fillMaxSize().testTag("screen_profile")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = AisleSpacing.screenHorizontal, end = AisleSpacing.screenHorizontal, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ProfileHeader
            ProfileHeader(
                userName = userName,
                onEditClick = {
                    showNameEditor = true
                    nameInput = userName
                    nameSavedMessage = false
                },
                isEditing = showNameEditor
            )

            // NameEditor (appears when editing)
            if (showNameEditor) {
                NameEditor(
                    nameInput = nameInput,
                    onNameChange = { nameInput = it; nameSavedMessage = false },
                    onSaveClick = {
                        onUserNameChange(nameInput.trim())
                        nameSavedMessage = true
                    },
                    nameSavedMessage = nameSavedMessage
                )
            }

            // Treino section
            SectionTitle("Treino")
            SettingsGroup {
                SwitchRow(
                    icon = Icons.Outlined.GraphicEq,
                    title = "Voz do treinador",
                    description = "Instruções sonoras para início, trocas de passo e ritmo",
                    checked = voiceEnabled,
                    onCheckedChange = onVoiceToggle,
                    testTag = "switch_voice"
                )
                Hairline()
                SwitchRow(
                    icon = Icons.Outlined.Vibration,
                    title = "Vibração",
                    description = "Alertas táteis nas transições de caminhada e corrida",
                    checked = vibrationEnabled,
                    onCheckedChange = onVibrationToggle,
                    testTag = "switch_vibration"
                )
                Hairline()
                SwitchRow(
                    icon = Icons.Outlined.Straighten,
                    title = "Avisos por distância",
                    description = "Anúncios de voz a cada marca de distância percorrida",
                    checked = avisoDistanciaAtivo,
                    onCheckedChange = onAvisoDistanciaAtivoToggle,
                    testTag = "switch_distance_alerts"
                )
                if (avisoDistanciaAtivo) {
                    Hairline()
                    SegmentedControl(
                        label = "Avisar a cada",
                        options = listOf("500 m", "1 km", "2 km"),
                        selectedIndex = when (avisoDistanciaMetros) {
                            500 -> 0
                            1000 -> 1
                            2000 -> 2
                            else -> 1
                        },
                        onOptionClick = { index ->
                            val meters = when (index) {
                                0 -> 500
                                1 -> 1000
                                2 -> 2000
                                else -> 1000
                            }
                            onAvisoDistanciaMetrosChange(meters)
                        },
                        testTags = listOf("card_distance_500", "card_distance_1000", "card_distance_2000")
                    )
                }
            }

            // Mapa section
            SectionTitle("Mapa")
            SettingsGroup {
                SwitchRow(
                    icon = Icons.Outlined.Map,
                    title = "Mostrar mapa durante corrida",
                    description = "Exibe sua posição e rota no mapa enquanto corre",
                    checked = showMapDuringRun,
                    onCheckedChange = onShowMapDuringRunToggle,
                    testTag = "switch_show_map"
                )
                if (showMapDuringRun) {
                    Hairline()
                    RadioList(
                        label = "Modo de câmera padrão",
                        options = listOf("Seguir você", "Seguir com direção", "Rota completa"),
                        selectedIndex = when (defaultCameraMode) {
                            "follow_user" -> 0
                            "follow_user_with_bearing" -> 1
                            "show_full_route" -> 2
                            else -> 0
                        },
                        onOptionClick = { index ->
                            val mode = when (index) {
                                0 -> "follow_user"
                                1 -> "follow_user_with_bearing"
                                2 -> "show_full_route"
                                else -> "follow_user"
                            }
                            onDefaultCameraModeChange(mode)
                        },
                        testTags = listOf("card_camera_follow", "card_camera_bearing", "card_camera_route")
                    )
                }
            }

            // Plano e lembretes section
            SectionTitle("Plano e lembretes")
            SettingsGroup {
                NavRow(
                    icon = Icons.Outlined.Tune,
                    title = "Ajustes do plano",
                    description = if (ajustesAutomaticos) "Ajustes automáticos ligados" else "Ajustes automáticos desligados",
                    onClick = onOpenAdjustments,
                    testTag = "card_ajustes_plano"
                )
                Hairline()
                SwitchRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Lembrete diário",
                    description = "Notificação apenas nos dias em que houver treino pendente",
                    checked = lembretesAtivos,
                    onCheckedChange = { checked ->
                        if (checked) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                showPermissionDeniedNotice = false
                                onLembretesChange(true, lembreteHora, lembreteMinuto)
                            }
                        } else {
                            showPermissionDeniedNotice = false
                            onLembretesChange(false, lembreteHora, lembreteMinuto)
                        }
                    },
                    testTag = "switch_reminders"
                )
                if (showPermissionDeniedNotice) {
                    Hairline()
                    SettingsNotice(
                        message = "As notificações estão desativadas para o aplicativo. Habilite nas configurações para receber lembretes.",
                        buttonText = "Abrir configurações",
                        onButtonClick = {
                            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                            } else {
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = android.net.Uri.fromParts("package", context.packageName, null)
                                }
                            }
                            context.startActivity(intent)
                        },
                        testTag = "btn_open_notification_settings"
                    )
                }
                if (lembretesAtivos && !showPermissionDeniedNotice) {
                    Hairline()
                    ValueRow(
                        icon = Icons.Outlined.Schedule,
                        title = "Horário do lembrete",
                        value = String.format(java.util.Locale.ROOT, "%02d:%02d", lembreteHora, lembreteMinuto),
                        onClick = {
                            selectedHour = lembreteHora
                            selectedMinute = lembreteMinuto
                            showTimeDialog = true
                        },
                        testTag = "btn_change_reminder_time"
                    )
                }
            }

            // Privacidade e dados section
            SectionTitle("Privacidade e dados")
            SettingsGroup {
                PrivacyInfoRow(
                    icon = Icons.Outlined.Shield,
                    text = "Seus treinos, rotas de GPS e dados de corrida ficam armazenados exclusivamente no seu celular. O aplicativo não utiliza servidores externos, não exige login e não compartilha nenhuma informação com terceiros."
                )
            }

            // Backup e restauração section
            SectionTitle("Backup e restauração")
            SettingsGroup {
                ActionRow(
                    icon = Icons.Outlined.FileUpload,
                    title = "Exportar backup",
                    onClick = onExportBackup,
                    testTag = "btn_export_backup"
                )
                if (hasBackupFile) {
                    Hairline()
                    ActionRow(
                        icon = Icons.Outlined.FileDownload,
                        title = "Restaurar backup",
                        onClick = onImportBackup,
                        testTag = "btn_import_backup"
                    )
                    Hairline()
                    ActionRow(
                        icon = Icons.Outlined.Share,
                        title = "Compartilhar backup",
                        onClick = onShareBackup,
                        testTag = "btn_share_backup"
                    )
                    Hairline()
                    ActionRow(
                        icon = Icons.Outlined.Delete,
                        title = "Excluir backup local",
                        onClick = onDeleteBackup,
                        testTag = "btn_delete_backup"
                    )
                }
            }
            Footnote("Exporte todos os seus dados (planos, treinos, histórico, rotas GPS, check-ins) para um arquivo local. Compatível com versões futuras do app.")

            // Apagar todos os meus dados - danger group
            SettingsGroup(danger = true, modifier = Modifier.padding(top = 16.dp)) {
                ActionRow(
                    icon = Icons.Filled.DeleteForever,
                    title = "Apagar todos os meus dados",
                    onClick = { showStep1DeleteDialog = true },
                    testTag = "btn_apagar_todos_dados",
                    destructive = true
                )
            }

            // Sobre section
            SectionTitle("Sobre")
            SettingsGroup {
                InfoRow(
                    icon = Icons.Filled.HealthAndSafety,
                    title = "Aviso de saúde",
                    description = "Este app não substitui orientação médica. Se tiver dúvida sobre sua saúde, consulte um médico antes de começar."
                )
                Hairline()
                InfoRow(
                    icon = Icons.Filled.Info,
                    title = "Sobre o Aisle",
                    description = "Versão 1.0.0 (Build 1), 100% nativo e local",
                    testTag = "card_about_app"
                )
            }

            Spacer(modifier = Modifier.height(AisleSpacing.xxl))
        }
    }
}