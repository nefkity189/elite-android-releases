package com.elite.sch.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.R
import com.elite.sch.app.account.AuthState
import com.elite.sch.app.ui.theme.*
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.components.EliteLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(authState: AuthState, onGoogle: () -> Unit, onTerms: () -> Unit, onPrivacy: () -> Unit) {
    val pager = rememberPagerState(pageCount = { 4 })
    val loading = authState is AuthState.SigningIn

    Box(Modifier.fillMaxSize().background(Background)) {
        Box(Modifier.fillMaxWidth().height(468.dp)) {
            Image(painterResource(R.drawable.ic_login_topo), null, Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Background))))
        }
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(44.dp))
            EliteLogo(Modifier.fillMaxWidth(), symbolSize = 34.dp, showTagline = false)
            HorizontalPager(state = pager, modifier = Modifier.weight(1f).fillMaxWidth()) { page -> LoginPage(page) }
            Row(Modifier.fillMaxWidth().height(8.dp), horizontalArrangement = Arrangement.Center) {
                repeat(4) { page -> Box(Modifier.padding(horizontal = 5.dp).width(if (page == pager.currentPage) 24.dp else 8.dp).height(8.dp).clip(CircleShape).background(if (page == pager.currentPage) Olive else TextMuted)) }
            }
            Spacer(Modifier.height(14.dp))
            Button(onClick = onGoogle, enabled = !loading, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(52.dp), shape = RoundedCornerShape(28.dp), colors = ButtonDefaults.buttonColors(containerColor = Olive, contentColor = OnOlive)) {
                if (loading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = OnOlive) else Image(painterResource(R.drawable.ic_google_g), null, Modifier.size(20.dp), colorFilter = ColorFilter.tint(OnOlive))
                Spacer(Modifier.width(8.dp)); Text(if (loading) "Entrando…" else "Entrar com Google", fontFamily = EliteBodyFont, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(18.dp))
            LegalLinks(onTerms, onPrivacy)
            Spacer(Modifier.navigationBarsPadding().height(8.dp))
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LegalLinks(onTerms: () -> Unit, onPrivacy: () -> Unit) {
    FlowRow(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Center) {
        Text("Ao continuar, você aceita os ", color = TextMuted, fontFamily = EliteBodyFont, fontSize = 11.sp)
        Text("Termos", Modifier.clickable(onClick = onTerms), color = Olive, fontFamily = EliteBodyFont, fontSize = 11.sp)
        Text(" e a ", color = TextMuted, fontFamily = EliteBodyFont, fontSize = 11.sp)
        Text("Política de Privacidade", Modifier.clickable(onClick = onPrivacy), color = Olive, fontFamily = EliteBodyFont, fontSize = 11.sp)
        Text(".", color = TextMuted, fontFamily = EliteBodyFont, fontSize = 11.sp)
    }
}

@Composable private fun LoginPage(page: Int) {
    val titles = listOf("CORRA CONTRA\nGENTE DE VERDADE.", "SUBA NO PÓDIO\nE NO RANKING.", "SEU PLANO DE TREINO,\nSEMANA A SEMANA.", "MISSÃO DADA É\nMISSÃO CUMPRIDA.")
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) { Spacer(Modifier.height(30.dp)); PhoneMock(page); Spacer(Modifier.height(16.dp)); Text(titles[page], textAlign = TextAlign.Center, fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 32.sp, letterSpacing = .9.sp, color = EliteText) }
}

@Composable private fun PhoneMock(page: Int) {
    Box(Modifier.width(207.dp).height(404.dp).clip(RoundedCornerShape(28.dp)).background(Color(0xFF1D2116)).border(1.dp, LineStrong, RoundedCornerShape(28.dp)).padding(5.dp)) {
        Column(Modifier.fillMaxSize().clip(RoundedCornerShape(23.dp)).background(Background).padding(start = 14.dp, end = 14.dp, top = 24.dp, bottom = 14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(listOf("● COMPETIÇÃO ONLINE", "● RANKING E LIGAS", "● SEU PLANO", "● CONQUISTAS")[page], fontFamily = EliteBodyFont, fontWeight = FontWeight.SemiBold, fontSize = 8.sp, color = Olive)
                Text(listOf("Premium", "Temporada", "Semana 3 de 12", "2 de 6")[page], fontSize = 8.sp, color = TextMuted)
            }
            Spacer(Modifier.height(12.dp)); MockCards(page)
        }
        Box(Modifier.align(Alignment.TopCenter).padding(top = 8.dp).size(40.dp, 4.dp).clip(CircleShape).background(LineStrong))
    }
}

@Composable private fun MockCards(page: Int) {
    Column(Modifier.fillMaxSize()) {
        when (page) {
            0 -> {
                MiniCard { Row(verticalAlignment = Alignment.CenterVertically) { Text("5K", Modifier.clip(RoundedCornerShape(7.dp)).background(Olive.copy(alpha = .14f)).padding(6.dp), fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Olive); Spacer(Modifier.width(10.dp)); Column { MiniText("Missão das 13:00", 12, true); MiniText("3 na sala", 9, color = TextMuted) } } }
                Spacer(Modifier.height(18.dp)); MiniText("Começa em", 10, color = TextMuted, modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
                Text("18:42", Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = EliteDisplayFont, fontWeight = FontWeight.Bold, fontSize = 54.sp, lineHeight = 56.sp, color = EliteText)
                MiniText("Vence quem concluir primeiro", 9, color = TextMuted, modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
                Spacer(Modifier.height(12.dp)); listOf("VC" to "Você", "RM" to "Rafael M.", "CS" to "Camila S.").forEach { (initials, name) -> Row(Modifier.fillMaxWidth().height(34.dp), verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(26.dp).clip(CircleShape).background(Olive.copy(alpha = if (initials == "VC") 1f else .14f)), contentAlignment = Alignment.Center) { MiniText(initials, 9, true, if (initials == "VC") OnOlive else Olive) }; Spacer(Modifier.width(8.dp)); MiniText(name, 11, modifier = Modifier.weight(1f)); MiniText("● Na sala", 9, color = Color(0xFF7FB069)) } }
                Spacer(Modifier.weight(1f)); MiniButton("Entrar na prova")
            }
            1 -> {
                MiniCard { Column { MiniText("◇  LIGA OURO II", 18, true, Brass, display = true); MiniText("Faltam 58 pontos para subir", 9, color = TextMuted); Spacer(Modifier.height(8.dp)); Box(Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(LineStrong)) { Box(Modifier.fillMaxWidth(.65f).height(4.dp).background(Olive)) } } }
                Spacer(Modifier.height(12.dp)); MiniText("Global      Meu estado", 10, color = Olive)
                Spacer(Modifier.height(12.dp)); listOf("1    Bruno A.          3.120 pts", "2    Camila S.         2.940 pts", "3    Rafael M.         2.815 pts", "4    Você                2.402 pts").forEach { MiniText(it, 11, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) }
                Spacer(Modifier.weight(1f)); MiniText("Exemplo ilustrativo", 8, color = TextMuted, modifier = Modifier.fillMaxWidth(), align = TextAlign.Center)
            }
            2 -> {
                MiniText("━ ━ ━ ━ ━ ━ ━ ━ ━", 10, color = Olive); Spacer(Modifier.height(10.dp)); MiniText("CORRIDA LEVE", 25, true, display = true); MiniText("Treino de hoje · 42 min", 10, color = TextMuted); Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth().height(100.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) { listOf(40, 100, 46, 100, 46, 100, 40).forEachIndexed { i, h -> Box(Modifier.weight(1f).height(h.dp).clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)).background(if (i % 2 == 1) Olive else Brass.copy(alpha = .7f))) } }
                Spacer(Modifier.height(18.dp)); MiniText("5,2 km       6:10       3 blocos", 14, true, display = true); MiniText("Distância       Ritmo /km     De corrida", 8, color = TextMuted); Spacer(Modifier.weight(1f)); MiniButton("Começar treino")
            }
            else -> listOf("Primeira missão" to "Conclua uma missão.", "Primeira vitória" to "Seja o primeiro.", "Sequência" to "5 missões seguidas.", "Participação completa" to "2,5K, 5K e 10K.", "Pódio estadual" to "Chegue ao pódio.").forEachIndexed { index, (title, sub) -> MiniCard { Row(verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(32.dp).clip(CircleShape).background(Brass.copy(alpha = .16f)), contentAlignment = Alignment.Center) { MiniText(if (index < 3) "★" else "🔒", 13, color = Brass) }; Spacer(Modifier.width(8.dp)); Column { MiniText(title, 10, true); MiniText(sub, 8, color = TextMuted) } } }; Spacer(Modifier.height(7.dp)) }
        }
    }
}

@Composable private fun MiniCard(content: @Composable () -> Unit) { Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Card).border(1.dp, LineStrong.copy(alpha = .55f), RoundedCornerShape(12.dp)).padding(10.dp)) { content() } }
@Composable private fun MiniText(value: String, size: Int, bold: Boolean = false, color: Color = EliteText, modifier: Modifier = Modifier, align: TextAlign = TextAlign.Start, display: Boolean = false) { Text(value, modifier, fontFamily = if (display) EliteDisplayFont else EliteBodyFont, fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal, fontSize = size.sp, lineHeight = (size + 3).sp, color = color, textAlign = align, maxLines = 1) }
@Composable private fun MiniButton(label: String) { Box(Modifier.fillMaxWidth().height(36.dp).clip(CircleShape).background(Red), contentAlignment = Alignment.Center) { MiniText(label, 11, true, Color.White) } }

