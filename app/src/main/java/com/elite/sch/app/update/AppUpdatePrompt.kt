package com.elite.sch.app.update

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppUpdatePrompt(onUpdate: (AppRelease) -> Unit) {
    val context = LocalContext.current
    var release by remember { mutableStateOf<AppRelease?>(null) }

    LaunchedEffect(Unit) {
        release = runCatching { GitHubReleaseRepository.checkForUpdate(context) }.getOrNull()
    }

    release?.let { latest ->
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Atualização disponível") },
            text = {
                androidx.compose.foundation.layout.Column {
                    Text("ELITE ${latest.versionName} já está disponível.")
                    if (latest.notes.isNotBlank()) {
                        Text(
                            text = latest.notes,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = "O Android pedirá sua confirmação para instalar.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    release = null
                    onUpdate(latest)
                }) { Text("Baixar atualização") }
            },
            dismissButton = {
                Button(onClick = {
                    release = null
                    android.content.Intent(android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://elitesch.web.app/")).also {
                        context.startActivity(it)
                    }
                }) { Text("Ver no site") }
            },
        )
    }
}
