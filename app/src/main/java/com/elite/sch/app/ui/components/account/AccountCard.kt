package com.elite.sch.app.ui.components.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.account.*
import com.elite.sch.app.ui.components.EliteButton
import com.elite.sch.app.ui.components.EliteDialog
import com.elite.sch.app.ui.components.eliteTextFieldColors
import com.elite.sch.app.ui.theme.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccountCard(
    state: AuthState,
    dialog: AccountDialog,
    onSignIn: (String, String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onPickUf: () -> Unit,
    onRequestSignOut: () -> Unit,
    onSignOut: () -> Unit,
    onDismissDialog: () -> Unit,
    onUfConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedUf by remember { mutableStateOf<String?>(null) }
    val signedIn = state as? AuthState.SignedIn
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier.fillMaxWidth().clip(shape).background(Card).border(1.dp, Line, shape).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        if (signedIn == null) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(OliveSoft), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Person, null, tint = Olive)
                }
                Text("ENTRE PARA COMPETIR", style = MaterialTheme.typography.titleMedium, color = Text, fontWeight = FontWeight.Bold, letterSpacing = .5.sp)
            }
            Text("Entre para participar das missões online, acompanhar seu ranking e guardar seu progresso na nuvem.", style = MaterialTheme.typography.bodySmall, color = TextMuted, lineHeight = 19.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Missões online", "Ranking", "Conquistas").forEach { label ->
                    Text(label, modifier = Modifier.clip(RoundedCornerShape(15.dp)).background(OliveSoft).padding(horizontal = 10.dp, vertical = 6.dp), color = Olive, fontSize = 11.sp, maxLines = 1)
                }
            }
            OutlinedTextField(
                value = login, onValueChange = { login = it }, label = { Text("E-mail") }, singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("acct.login"), colors = eliteTextFieldColors()
            )
            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Senha") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth().testTag("acct.password"), colors = eliteTextFieldColors()
            )
            if (state is AuthState.Error) {
                Text(state.message, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Amber.copy(alpha = .13f)).border(1.dp, Amber.copy(alpha = .35f), RoundedCornerShape(12.dp)).padding(12.dp), color = Text, style = MaterialTheme.typography.bodySmall)
            }
            EliteButton(
                text = if (state is AuthState.SigningIn) "Entrando…" else "Entrar com Google",
                onClick = onGoogleSignIn, enabled = state !is AuthState.SigningIn,
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag("acct.google")
            )
            OutlinedButton(
                onClick = { onSignIn(login, password) }, enabled = state !is AuthState.SigningIn,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("acct.email")
            ) {
                Text("Entrar com e-mail")
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(OliveSoft).border(2.dp, OliveDark, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                    Text(signedIn.account.displayName.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""), color = Olive, fontWeight = FontWeight.Bold)
                }
                Column(Modifier.weight(1f)) {
                    Text(signedIn.account.displayName, color = Text, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold)
                    Text(signedIn.account.email, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
                Text("● Conectado", modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Ok.copy(alpha = .16f)).padding(horizontal = 8.dp, vertical = 5.dp), color = Ok, fontSize = 10.sp, maxLines = 1)
            }
            HorizontalDivider(color = Line)
            Row(Modifier.fillMaxWidth().heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Outlined.Place, null, tint = TextMuted)
                Column(Modifier.weight(1f)) {
                    Text("Estado", color = Text, fontWeight = FontWeight.Medium)
                    Text(if (signedIn.account.uf == null) "Escolha para aparecer no ranking local" else "Usado no ranking Meu estado", color = if (signedIn.account.uf == null) Amber else TextMuted, style = MaterialTheme.typography.labelSmall)
                }
                TextButton(onClick = { selectedUf = signedIn.account.uf; onPickUf() }, modifier = Modifier.testTag("acct.in.uf")) {
                    Text(signedIn.account.uf ?: "Escolher", color = Olive)
                }
            }
            HorizontalDivider(color = Line)
            OutlinedButton(onClick = onRequestSignOut, modifier = Modifier.fillMaxWidth().height(48.dp).testTag("acct.in.signout"), colors = ButtonDefaults.outlinedButtonColors(contentColor = Text)) { Text("Sair da conta") }
        }
    }

    if (dialog == AccountDialog.SignOut) EliteDialog("Sair da conta?", onDismiss = onDismissDialog, text = "Seu plano e seus treinos continuam salvos neste aparelho.", confirmLabel = "Sair", onConfirm = onSignOut, destructive = true)
    if (dialog == AccountDialog.UfSheet) {
        var choice by remember(dialog) { mutableStateOf(selectedUf) }
        ModalBottomSheet(onDismissRequest = onDismissDialog, containerColor = com.elite.sch.app.ui.theme.Surface, contentColor = Text) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("SEU ESTADO", style = MaterialTheme.typography.titleLarge, color = Text)
                Text("Usado no ranking Meu estado. Você pode mudar depois.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                LazyVerticalGrid(columns = GridCells.Fixed(5), modifier = Modifier.heightIn(max = 400.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(BRAZIL_UFS) { uf -> FilterChip(selected = choice == uf, onClick = { choice = uf }, label = { Text(uf) }, modifier = Modifier.heightIn(min = 48.dp).testTag("uf.$uf")) }
                }
                EliteButton("Confirmar", onClick = { choice?.let(onUfConfirm) }, enabled = choice != null, modifier = Modifier.fillMaxWidth().height(56.dp).testTag("uf.confirm"))
            }
        }
    }
}
