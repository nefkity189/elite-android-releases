package com.elite.sch.app.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.core.content.ContextCompat
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.EliteStepper
import com.elite.sch.app.ui.components.Hairline
import com.elite.sch.app.ui.components.NameEditor
import com.elite.sch.app.ui.components.NavRow
import com.elite.sch.app.ui.components.ProfileHeader
import com.elite.sch.app.ui.components.SegmentedControl
import com.elite.sch.app.ui.components.SectionTitle
import com.elite.sch.app.ui.components.SettingsGroup
import com.elite.sch.app.ui.components.SettingsNotice
import com.elite.sch.app.ui.components.SwitchRow
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteNumberStyles
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.account.AuthState
import com.elite.sch.app.account.AccountDialog
import com.elite.sch.app.ui.components.account.AccountCard
import java.util.Locale

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
    ajustesAutomaticos: Boolean = true,
    onOpenAdjustments: () -> Unit = {},
    onOpenPrivacyData: () -> Unit = {},
    accountState: AuthState = AuthState.SignedOut,
    accountDialog: AccountDialog = AccountDialog.None,
    onAccountSignIn: (String, String) -> Unit = { _, _ -> },
    onAccountGoogleSignIn: () -> Unit = {},
    onAccountDialog: (AccountDialog) -> Unit = {},
    onAccountDialogDismiss: () -> Unit = {},
    onAccountSignOut: () -> Unit = {},
    onAccountUfConfirm: (String) -> Unit = {},
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
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
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

    if (showTimeDialog) {
        EliteDialog(
            title = "Horário do lembrete",
            text = "Escolha a hora e o minuto da notificação.",
            onDismiss = { showTimeDialog = false },
            confirmLabel = "Confirmar",
            onConfirm = {
                showTimeDialog = false
                onLembretesChange(lembretesAtivos, selectedHour, selectedMinute)
            },
                confirmTestTag = "btn_confirm_time",
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = String.format(Locale.ROOT, "%02d:%02d", selectedHour, selectedMinute),
                            style = MaterialTheme.typography.displayMedium,
                            color = Olive
                        )
                        Text("Hora", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                        EliteStepper(
                            value = selectedHour,
                            onValueChange = { selectedHour = it },
                            range = 0..23,
                            label = "hora",
                            valueText = String.format(Locale.ROOT, "%02d", selectedHour),
                            wrap = true,
                            minusTestTag = "btn_hour_minus",
                            plusTestTag = "btn_hour_plus"
                        )
                        Text("Minuto", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                        EliteStepper(
                            value = selectedMinute,
                            onValueChange = { selectedMinute = it },
                            range = 0..59,
                            label = "minuto",
                            valueText = String.format(Locale.ROOT, "%02d", selectedMinute),
                            step = 5,
                            wrap = true,
                            minusTestTag = "btn_min_minus",
                            plusTestTag = "btn_min_plus"
                        )
                    }
            }
            )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .testTag("screen_profile")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = EliteSpacing.screenHorizontal)
                .padding(bottom = EliteSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "PERFIL",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextColor
                )
            }

            ProfileHeader(
                userName = userName,
                onEditClick = {
                    showNameEditor = !showNameEditor
                    nameInput = userName
                    nameSavedMessage = false
                },
                isEditing = showNameEditor
            )

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

            SectionTitle("Conta")
            AccountCard(
                state = accountState,
                dialog = accountDialog,
                onSignIn = onAccountSignIn,
                onGoogleSignIn = onAccountGoogleSignIn,
                onPickUf = { onAccountDialog(AccountDialog.UfSheet) },
                onRequestSignOut = { onAccountDialog(AccountDialog.SignOut) },
                onSignOut = onAccountSignOut,
                onDismissDialog = onAccountDialogDismiss,
                onUfConfirm = onAccountUfConfirm
            )

            SectionTitle("Durante a corrida")
            SettingsGroup {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SwitchRow(
                        icon = Icons.Outlined.GraphicEq,
                        title = "Voz do treinador",
                        description = "Instruções sonoras para início, trocas de passo e ritmo",
                        checked = voiceEnabled,
                        onCheckedChange = onVoiceToggle,
                        testTag = "switch_voice"
                    )
                    SwitchRow(
                        icon = Icons.Outlined.Vibration,
                        title = "Vibração",
                        description = "Alertas táteis nas transições",
                        checked = vibrationEnabled,
                        onCheckedChange = onVibrationToggle,
                        testTag = "switch_vibration"
                    )
                    SwitchRow(
                        icon = Icons.Outlined.Straighten,
                        title = "Avisos por distância",
                        description = "Anúncios de voz a cada marca percorrida",
                        checked = avisoDistanciaAtivo,
                        onCheckedChange = onAvisoDistanciaAtivoToggle,
                        testTag = "switch_distance_alerts"
                    )
                    if (avisoDistanciaAtivo) {
                        DistanceIntervalRow(
                            distanceMeters = avisoDistanciaMetros,
                            onChange = onAvisoDistanciaMetrosChange
                        )
                    }
                    SwitchRow(
                        icon = Icons.Outlined.Map,
                        title = "Mostrar mapa durante corrida",
                        description = "Exibe sua posição e rota no mapa",
                        checked = showMapDuringRun,
                        onCheckedChange = onShowMapDuringRunToggle,
                        testTag = "switch_show_map"
                    )
                }
            }

            SectionTitle("Modo de câmera padrão")
            SegmentedControl(
                label = "",
                options = listOf("Seguir você", "Com direção", "Rota completa"),
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

            SectionTitle("Plano e lembretes")
            SettingsGroup {
                NavRow(
                    icon = Icons.Outlined.Tune,
                    title = "Ajustes do plano",
                    description = if (ajustesAutomaticos) {
                        "Ajustes automáticos ligados"
                    } else {
                        "Ajustes automáticos desligados"
                    },
                    onClick = onOpenAdjustments,
                    testTag = "card_ajustes_plano"
                )
            }
            SettingsGroup {
                Column(modifier = Modifier.padding(14.dp)) {
                    SwitchRow(
                        icon = Icons.Outlined.Notifications,
                        title = "Lembrete diário",
                        description = reminderDescription(lembretesAtivos, lembreteHora, lembreteMinuto),
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
                        testTag = "switch_reminders",
                        onDescriptionClick = if (lembretesAtivos) {
                            {
                                selectedHour = lembreteHora
                                selectedMinute = lembreteMinuto
                                showTimeDialog = true
                            }
                        } else {
                            null
                        }
                    )
                    if (showPermissionDeniedNotice) {
                        Spacer(Modifier.height(EliteSpacing.md))
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
                }
            }

            SettingsGroup {
                NavRow(
                    icon = Icons.Outlined.Shield,
                    title = "Privacidade e dados",
                    description = "Backup, aviso de saúde e dados do app",
                    onClick = onOpenPrivacyData,
                    testTag = "card_privacy_data"
                )
            }
        }
    }
}

@Composable
private fun DistanceIntervalRow(
    distanceMeters: Int,
    onChange: (Int) -> Unit
) {
    val options = listOf(500, 1000, 2000)
    val selectedIndex = options.indexOf(distanceMeters).takeIf { it >= 0 } ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .testTag("distance_interval_stepper"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Avisar a cada",
            fontFamily = EliteBodyFont,
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.weight(1f)
        )
        IntervalButton(
            icon = Icons.Filled.Remove,
            description = "Diminuir intervalo de aviso",
            testTag = "btn_distance_minus",
            onClick = {
                if (selectedIndex > 0) onChange(options[selectedIndex - 1])
            },
            enabled = selectedIndex > 0
        )
        Text(
            text = when (options[selectedIndex]) {
                500 -> "500 m"
                1000 -> "1 km"
                else -> "2 km"
            },
            style = EliteNumberStyles.Small,
            color = TextColor,
            modifier = Modifier.padding(horizontal = 12.dp).testTag("distance_interval_value")
        )
        IntervalButton(
            icon = Icons.Filled.Add,
            description = "Aumentar intervalo de aviso",
            testTag = "btn_distance_plus",
            onClick = {
                if (selectedIndex < options.lastIndex) onChange(options[selectedIndex + 1])
            },
            enabled = selectedIndex < options.lastIndex
        )
    }
}

@Composable
private fun IntervalButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    testTag: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(36.dp)
            .border(1.dp, Line, CircleShape)
            .background(CardColor, CircleShape)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = if (enabled) TextColor else TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun reminderDescription(active: Boolean, hour: Int, minute: Int): String {
    val base = "Apenas nos dias com treino pendente"
    return if (active) {
        "$base · ${String.format(Locale.ROOT, "%02d:%02d", hour, minute)}"
    } else {
        base
    }
}
