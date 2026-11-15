package com.example.ui.screens

import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard as AisleCardColor
import com.example.ui.theme.AisleError
import com.example.ui.theme.AislePurple
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.components.AisleCard
import com.example.ui.components.AisleStepper
import com.example.ui.components.AisleSwitchRow
import com.example.ui.components.AisleSelectableCard
import androidx.compose.material.icons.filled.Warning
import com.example.ui.components.AisleDialog
import androidx.compose.runtime.mutableIntStateOf
import java.util.Locale
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Vibration
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleTopBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.ui.theme.AisleSpacing

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
    onUserNameChange: (String) -> Unit,
    onVoiceToggle: (Boolean) -> Unit = {},
    onVibrationToggle: (Boolean) -> Unit = {},
    onLembretesChange: (ativos: Boolean, hora: Int, minuto: Int) -> Unit = { _, _, _ -> },
    onAvisoDistanciaAtivoToggle: (Boolean) -> Unit = {},
    onAvisoDistanciaMetrosChange: (Int) -> Unit = {},
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
            icon = Icons.Filled.Warning,
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
            icon = Icons.Filled.Warning,
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
                    text = String.format(Locale.ROOT, "%02d:%02d", selectedHour, selectedMinute),
                    style = MaterialTheme.typography.displayMedium,
                    color = AisleTurquoise
                )
                Text("Hora", style = MaterialTheme.typography.labelMedium, color = AisleTextSecondary)
                AisleStepper(
                    value = selectedHour,
                    onValueChange = { selectedHour = it },
                    range = 0..23,
                    label = "hora",
                    valueText = String.format(Locale.ROOT, "%02d", selectedHour),
                    wrap = true,
                    minusTestTag = "btn_hour_minus",
                    plusTestTag = "btn_hour_plus"
                )
                Text("Minuto", style = MaterialTheme.typography.labelMedium, color = AisleTextSecondary)
                AisleStepper(
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
    }

    Scaffold(
        topBar = {
            AisleTopBar(
                title = "Perfil",
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = AisleBackground,
        modifier = modifier.fillMaxSize().testTag("screen_profile")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AisleSpacing.xl, vertical = AisleSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
        ) {
            // Emblema do Corredor
            AisleCard(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = AisleCardColor,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = AisleTurquoise,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AisleSpacing.lg))

                    Text(
                        text = userName.ifBlank { "Corredor Iniciante" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AisleTextPrimary
                    )

                    Spacer(modifier = Modifier.height(AisleSpacing.xs))

                    Text(
                        text = "Rumo aos seus 5K no Aisle",
                        style = MaterialTheme.typography.bodySmall,
                        color = AisleTextSecondary
                    )
                }
            }

            // 1. NOME EDITÁVEL
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                ) {
                    Text(
                        text = "Identificação",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            nameSavedMessage = false
                        },
                        label = { Text("Como devemos te chamar?") },
                        placeholder = { Text("Seu nome ou apelido") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = com.example.ui.components.aisleTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (nameSavedMessage) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AisleTurquoise,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(AisleSpacing.xs))
                                Text(
                                    text = "Nome salvo!",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AisleTurquoise
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        AisleButton(
                            text = "Salvar",
                            onClick = {
                                onUserNameChange(nameInput.trim())
                                nameSavedMessage = true
                            },
                            variant = AisleButtonVariant.Primary,
                            modifier = Modifier.testTag("profile_save_name_button")
                        )
                    }
                }
            }

            // 2. VOZ E VIBRAÇÃO (LIGA / DESLIGA)
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_preferences_voice_vibration")
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
                ) {
                    Text(
                        text = "Preferências de Treino",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Switch Voz
                    AisleSwitchRow(
                        title = "Voz do treinador",
                        description = "Instruções sonoras para início, trocas de passo e ritmo",
                        checked = voiceEnabled,
                        onCheckedChange = { onVoiceToggle(it) },
                        modifier = Modifier.testTag("switch_voice")
                    )

                    HorizontalDivider(color = AisleBorder.copy(alpha = 0.5f))

                    // Switch Vibração
                    AisleSwitchRow(
                        title = "Vibração",
                        description = "Alertas táteis nas transições de caminhada e corrida",
                        checked = vibrationEnabled,
                        onCheckedChange = { onVibrationToggle(it) },
                        modifier = Modifier.testTag("switch_vibration")
                    )
                }
            }

            // 3. AVISOS POR DISTÂNCIA
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_preferences_distance_alerts")
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
                ) {
                    Text(
                        text = "Avisos por Distância",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Switch Avisos por Distância
                    AisleSwitchRow(
                        title = "Avisos por distância",
                        description = "Anúncios de voz a cada marca de distância percorrida",
                        checked = avisoDistanciaAtivo,
                        onCheckedChange = { onAvisoDistanciaAtivoToggle(it) },
                        modifier = Modifier.testTag("switch_distance_alerts")
                    )

                    if (avisoDistanciaAtivo) {
                        HorizontalDivider(color = AisleBorder.copy(alpha = 0.5f))

                        // Seleção do intervalo
                        Text(
                            text = "Avisar a cada",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                        ) {
                            AisleSelectableCard(
                                text = "500 m",
                                selected = avisoDistanciaMetros == 500,
                                onClick = { onAvisoDistanciaMetrosChange(500) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("card_distance_500")
                            )
                            AisleSelectableCard(
                                text = "1 km",
                                selected = avisoDistanciaMetros == 1000,
                                onClick = { onAvisoDistanciaMetrosChange(1000) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("card_distance_1000")
                            )
                            AisleSelectableCard(
                                text = "2 km",
                                selected = avisoDistanciaMetros == 2000,
                                onClick = { onAvisoDistanciaMetrosChange(2000) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("card_distance_2000")
                            )
                        }
                    }
                }
            }

            // AJUSTES DO PLANO (Plano que respira)
            AisleCard(
                onClick = onOpenAdjustments,
                modifier = Modifier.fillMaxWidth().testTag("card_ajustes_plano")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(AisleSpacing.lg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ajustes do plano",
                            style = MaterialTheme.typography.titleMedium,
                            color = AisleTextPrimary
                        )
                        Text(
                            text = if (ajustesAutomaticos) "Ajustes automáticos ligados · ver histórico" else "Ajustes automáticos desligados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AisleTextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AisleTextSecondary
                    )
                }
            }

            // LEMBRETES
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_reminders")
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.lg)
                ) {
                    Text(
                        text = "Lembretes de Treino",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Switch Lembretes
                    AisleSwitchRow(
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
                        modifier = Modifier.testTag("switch_reminders")
                    )

                    if (showPermissionDeniedNotice) {
                        AisleCard(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(AisleSpacing.md)) {
                                Text(
                                    text = "As notificações estão desativadas para o aplicativo. Habilite nas configurações para receber lembretes.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AisleError
                                )
                                Spacer(modifier = Modifier.height(AisleSpacing.md))
                                AisleButton(
                                    text = "Abrir configurações",
                                    onClick = {
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
                                    variant = AisleButtonVariant.Secondary,
                                    modifier = Modifier.testTag("btn_open_notification_settings")
                                )
                            }
                        }
                    }

                    if (lembretesAtivos) {
                        HorizontalDivider(color = AisleBorder.copy(alpha = 0.5f))

                        // Horário
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = AisleTurquoise,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(AisleSpacing.md))
                                Column {
                                    Text(
                                        text = "Horário do lembrete",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = String.format(Locale.ROOT, "%02d:%02d", lembreteHora, lembreteMinuto),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = AisleTurquoise,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            AisleButton(
                                text = "Alterar",
                                onClick = {
                                    selectedHour = lembreteHora
                                    selectedMinute = lembreteMinuto
                                    showTimeDialog = true
                                },
                                variant = AisleButtonVariant.Secondary,
                                modifier = Modifier.testTag("btn_change_reminder_time")
                            )
                        }
                    }
                }
            }

            // 3. PRIVACIDADE E DADOS + APAGAR TODOS OS MEUS DADOS
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = AisleTurquoise,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.xs))
                        Text(
                            text = "Privacidade e dados",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Seus treinos, rotas de GPS e dados de corrida ficam armazenados exclusivamente no seu celular. O aplicativo não utiliza servidores externos, não exige login e não compartilha nenhuma informação com terceiros.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AisleTextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(AisleSpacing.xs))

                    AisleButton(
                        text = "Apagar todos os meus dados",
                        onClick = { showStep1DeleteDialog = true },
                        variant = AisleButtonVariant.Destructive,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_apagar_todos_dados")
                    )
                }
            }

            // 5. BACKUP E RESTAURAÇÃO
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AisleSpacing.md)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "B",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AisleTurquoise,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(AisleSpacing.xs))
                        Text(
                            text = "Backup e restauração",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Exporte todos os seus dados (planos, treinos, histórico, rotas GPS, check-ins) para um arquivo local. Compatível com versões futuras do app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AisleTextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(AisleSpacing.xs))

                    // Botão Exportar Backup
                    AisleButton(
                        text = "Exportar backup",
                        onClick = onExportBackup,
                        variant = AisleButtonVariant.Primary,
                        leadingIcon = {
                            Text(
                                text = "↑",
                                style = MaterialTheme.typography.labelLarge,
                                color = AisleTurquoise,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_export_backup")
                    )

                    // Botão Restaurar Backup (só aparece se tiver backup)
                    if (hasBackupFile) {
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        AisleButton(
                            text = "Restaurar backup",
                            onClick = onImportBackup,
                            variant = AisleButtonVariant.Secondary,
                            leadingIcon = {
                                Text(
                                    text = "↓",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AisleTurquoise,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_import_backup")
                        )

                        // Compartilhar backup: abre o seletor do sistema para o usuário
                        // escolher onde salvar/copiar o arquivo (nuvem, email, outro app).
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        AisleButton(
                            text = "Compartilhar backup",
                            onClick = onShareBackup,
                            variant = AisleButtonVariant.Secondary,
                            leadingIcon = {
                                Text(
                                    text = "↗",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AisleTurquoise,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_share_backup")
                        )

                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        AisleButton(
                            text = "Excluir backup local",
                            onClick = onDeleteBackup,
                            variant = AisleButtonVariant.Secondary,
                            leadingIcon = {
                                Text(
                                    text = "×",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = AisleTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_delete_backup")
                        )
                    }
                }
            }

            // 6. AVISO DE SAÚDE
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = null,
                        tint = AislePurple,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.md))
                    Column {
                        Text(
                            text = "Aviso de saúde",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AisleTextPrimary
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.xs))
                        Text(
                            text = "Este app não substitui orientação médica. Se tiver dúvida sobre sua saúde, consulte um médico antes de começar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AisleTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 5. SOBRE
            AisleCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_about_app")
            ) {
                Row(
                    modifier = Modifier.padding(AisleSpacing.lg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AisleTurquoise,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(AisleSpacing.md))
                    Column {
                        Text(
                            text = "Sobre o Aisle",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(AisleSpacing.xxs))
                        Text(
                            text = "Versão 1.0.0 (Build 1) • 100% nativo e local",
                            style = MaterialTheme.typography.bodySmall,
                            color = AisleTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(AisleSpacing.xxl))
        }
    }
}
