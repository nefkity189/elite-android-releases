package com.elite.sch.app.ui.screens.competition

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.elite.sch.app.ui.components.CompetitionShareCardGenerator
import android.content.Intent
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import com.elite.sch.app.ui.components.competition.CompetitionAction
import com.elite.sch.app.ui.components.competition.CompetitionAvatar
import com.elite.sch.app.ui.components.competition.CompetitionCard
import com.elite.sch.app.ui.components.competition.CompetitionPill
import com.elite.sch.app.ui.components.competition.CompetitionShareIcon
import com.elite.sch.app.ui.components.competition.CompetitionText
import com.elite.sch.app.ui.components.competition.CompetitionTopBar
import com.elite.sch.app.ui.components.competition.LeagueBadge
import com.elite.sch.app.ui.components.competition.RankingPersonRow
import com.elite.sch.app.ui.components.competition.SectionHeading
import com.elite.sch.app.ui.competition.CompetitionDemoState
import com.elite.sch.app.ui.competition.CompetitionDistance
import com.elite.sch.app.data.competition.OnlineRankedUser
import com.google.firebase.auth.FirebaseAuth
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted

private data class RankedRunner(val position: Int, val initials: String, val name: String, val league: String, val points: String, val movement: String)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    state: CompetitionDemoState,
    ranking: List<OnlineRankedUser> = emptyList(),
    onBack: () -> Unit,
    onSeason: () -> Unit,
    onShare: () -> Unit,
    onScope: (Boolean) -> Unit,
    onDistance: (CompetitionDistance?) -> Unit,
    onHistory: (Boolean) -> Unit
) {
    var selectedProfile by rememberSaveable { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        CompetitionTopBar("Ranking", onBack, trailing = { CompetitionShareIcon(onShare) })
        CompetitionCard(Modifier.fillMaxWidth().clickable(onClick = onSeason)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    CompetitionText("Temporada online", size = 16, bold = true)
                    CompetitionText("Dados do servidor", color = TextMuted, size = 13)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CompetitionText("Ver dados online", size = 14, color = Brass, bold = true)
                    
                    CompetitionText("›", size = 26, color = EliteText)
                }
            }
        }
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val currentUser = state.onlineUser ?: ranking.firstOrNull { it.uid == currentUid }
        if (currentUser != null) {
            val position = ranking.indexOfFirst { it.uid == currentUser.uid }.takeIf { it >= 0 }?.plus(1)
            val globalPosition = position.takeIf { !state.rankingScope }
            val statePosition = position.takeIf { state.rankingScope && currentUser.state.isNotBlank() }
            PlayerRankCard(currentUser, globalPosition, statePosition, onClick = { selectedProfile = currentUser.name })
        } else {
            CompetitionCard(Modifier.fillMaxWidth()) {
                CompetitionText(if (currentUid == null) "Entre na sua conta para ver seu perfil" else if (state.userProfileLoaded) "Perfil ainda nao encontrado no servidor" else "Carregando perfil do servidor", size = 16, bold = true)
                CompetitionText(if (state.userProfileLoaded) "O ranking mostra apenas posicoes confirmadas pelo Firestore." else "Consultando seu documento de usuario.", size = 13, color = TextMuted)
            }
        }
        ScopeTabs(state.rankingScope, "Global", "Meu estado") { onScope(it) }
        CompetitionText("Temporada atual · classificação geral", size = 13, color = TextMuted)
        val displayedRanking = ranking.mapIndexed { index, user ->
            RankedRunner(
                position = index + 1,
                initials = user.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString(""),
                name = user.name,
                league = leagueFor(user.elo),
                points = user.elo.toString(),
                movement = "—"
            )
        }
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { RankingPodium(ranking) }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    CompetitionText("● Zona de promoção", color = Olive, size = 11)
                    Spacer(Modifier.width(14.dp)); CompetitionText("● Zona de rebaixamento", color = Red, size = 11)
                }
            }
            items(displayedRanking, key = { it.position }) { runner ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.width(3.dp).height(58.dp).background(if (runner.movement.startsWith("▼")) Red else Olive))
                    RankingPersonRow(runner.position, runner.initials, runner.name, runner.league, runner.points, movement = runner.movement, modifier = Modifier.clickable { selectedProfile = runner.name })
                }
                
            }
        }
    }
    selectedProfile?.let { name ->
        androidx.compose.material3.ModalBottomSheet(onDismissRequest = { selectedProfile = null }, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true), containerColor = Card) {
            Column(Modifier.fillMaxWidth().padding(20.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                CompetitionAvatar(name.take(1), size = 62)
                CompetitionText(name, size = 24, bold = true, display = true)
                CompetitionText("Dados sincronizados do Firestore", color = TextMuted, size = 13)
                CompetitionAction("Compartilhar perfil", onShare, Modifier.fillMaxWidth(), secondary = true)
            }
        }
    }
}

private fun leagueFor(elo: Int): String = when {
    elo >= 1600 -> "Elite"
    elo >= 1200 -> "Ouro"
    elo >= 800 -> "Prata"
    else -> "Bronze"
}

@Composable
private fun PlayerRankCard(user: OnlineRankedUser, globalPosition: Int?, statePosition: Int?, onClick: () -> Unit) {
    val initials = user.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("")
    CompetitionCard(Modifier.fillMaxWidth().clickable(onClick = onClick), border = Brass) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            CompetitionAvatar(initials, size = 44, highlighted = true)
            Column(Modifier.weight(1f)) {
                CompetitionText(user.name, size = 15, bold = true)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user.elo > 0) { LeagueBadge(leagueFor(user.elo)); Spacer(Modifier.width(5.dp)) }
                    CompetitionText("${user.elo} pts", size = 11, color = TextMuted)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CompetitionText("GLOBAL", size = 10, color = TextMuted, bold = true)
                CompetitionText(globalPosition?.let { "#$it" } ?: "—", size = 25, display = true, bold = true)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CompetitionText("ESTADO ${user.state.ifBlank { "?" }}", size = 10, color = TextMuted, bold = true)
                CompetitionText(statePosition?.let { "#$it" } ?: "—", size = 25, display = true, bold = true)
            }
        }
        val floor = (user.elo / 400) * 400
        val progress = ((user.elo - floor).coerceAtLeast(0) / 400f).coerceIn(0f, 1f)
        ProgressTrack(progress, Brass)
        CompetitionText(if (user.elo <= 0) "Ainda sem classificação: nenhum ponto registrado." else if (globalPosition == null && statePosition == null) "Seu perfil está fora das 30 posições exibidas." else "Elo sincronizado do servidor", size = 12, color = TextMuted)
    }
}

@Composable
private fun RankingPodium(ranking: List<OnlineRankedUser>) {
    Row(Modifier.fillMaxWidth().height(198.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
        val podium = ranking.take(3)
        if (podium.isEmpty()) {
            CompetitionText("O pódio aparecerá quando houver atletas com pontuação.", color = TextMuted, size = 13)
        } else {
            podium.getOrNull(1)?.let { PodiumSlot("2", it.name.take(2).uppercase(), it.name, it.elo.toString(), 120, Color(0xFFB4BAB8), Modifier.weight(1f)) }
            podium.getOrNull(0)?.let { PodiumSlot("1", it.name.take(2).uppercase(), it.name, it.elo.toString(), 166, Brass, Modifier.weight(1f)) }
            podium.getOrNull(2)?.let { PodiumSlot("3", it.name.take(2).uppercase(), it.name, it.elo.toString(), 104, Color(0xFFB07A4A), Modifier.weight(1f)) }
        }
    }
}

@Composable
private fun PodiumSlot(rank: String, initials: String, name: String, points: String, podiumHeight: Int, color: Color, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        if (rank == "1") Icon(Icons.Default.Star, null, tint = Brass, modifier = Modifier.size(24.dp))
        CompetitionAvatar(initials, size = if (rank == "1") 54 else 44)
        CompetitionText(name, size = 12, bold = true, maxLines = 1)
        CompetitionText(points, size = 22, display = true, bold = true, color = color)
        Box(Modifier.fillMaxWidth().height(podiumHeight.dp).clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)).background(color.copy(alpha = .13f)), contentAlignment = Alignment.Center) {
            CompetitionText(rank, size = 34, display = true, bold = true, color = color)
        }
    }
}

@Composable
private fun DivisionDivider(label: String, league: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Box(Modifier.weight(1f).height(1.dp).background(Line)); LeagueBadge(league, size = 16)
        CompetitionText(label.uppercase(), size = 10, bold = true, color = TextMuted, letterSpacing = .7f)
        Box(Modifier.weight(1f).height(1.dp).background(Line))
    }
}

@Composable
private fun ScopeTabs(selected: Boolean, left: String, right: String, onClick: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Background).padding(3.dp)) {
        CompetitionPill(left, Modifier.weight(1f), active = !selected) { onClick(false) }
        CompetitionPill(right, Modifier.weight(1f), active = selected) { onClick(true) }
    }
}

@Composable
fun SeasonScreen(user: OnlineRankedUser?, onBack: () -> Unit, onShare: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar("Temporada online", onBack, trailing = { CompetitionShareIcon(onShare) })
        CompetitionCard(Modifier.fillMaxWidth()) {
            SectionHeading("Seu desempenho")
            if (user == null) {
                CompetitionText("Perfil ainda não disponível no servidor.", color = TextMuted)
            } else {
                CompetitionText(user.name, size = 20, bold = true)
                CompetitionText(user.elo.toString() + " pontos", size = 28, display = true, bold = true, color = Brass)
                CompetitionText(if (user.elo > 0) "Liga " + leagueFor(user.elo) else "Ainda sem classificação", color = TextMuted)
            }
            CompetitionText("Os dados são carregados do seu perfil no Firebase.", size = 12, color = TextMuted)
        }
        CompetitionCard(Modifier.fillMaxWidth()) {
            SectionHeading("Temporada")
            CompetitionText("A classificação considera os pontos registrados na temporada atual.", color = TextMuted)
        }
    }
}
@Composable
private fun ProgressTrack(fraction: Float, color: Color) {
    Box(Modifier.fillMaxWidth().height(9.dp).clip(CircleShape).background(CardElevated)) {
        Box(Modifier.fillMaxWidth(fraction.coerceIn(0f, 1f)).fillMaxSize().clip(CircleShape).background(color))
    }
}

private data class Achievement(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val description: String, val progress: String, val done: Boolean, val local: Boolean = false)

@Composable
fun AchievementsScreen(user: OnlineRankedUser?, local: Boolean, onBack: () -> Unit, onRanking: () -> Unit, onShare: () -> Unit, onLocal: (Boolean) -> Unit) {
    val achievements = buildList {
        if ((user?.elo ?: 0) > 0) {
            add(Achievement(Icons.Default.EmojiEvents, "Primeira missão concluída", "Pontuação confirmada pelo servidor.", "${user?.elo} pontos", done = true))
            if (!user?.state.isNullOrBlank()) add(Achievement(Icons.Default.Place, "Primeiro ponto no estado", "Resultado registrado para ${user?.state}.", "${user?.elo} pontos", done = true, local = true))
        }
    }
    val visible = achievements.filter { it.local == local }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar("Conquistas", onBack, trailing = { CompetitionPill("◆  Ranking", color = Brass, active = true, onClick = onRanking) })
        CompetitionCard(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CompetitionText("${achievements.count { it.done }} / ${achievements.size}", size = 36, display = true, bold = true, color = Brass)
                Column(Modifier.weight(1f)) {
                    ProgressTrack(if (achievements.isEmpty()) 0f else achievements.count { it.done }.toFloat() / achievements.size, Brass)
                    CompetitionText("Dados confirmados pelo servidor.", size = 12, color = TextMuted)
                }
            }
        }
        ScopeTabs(local, "Globais", "Locais / Estado") { onLocal(it) }
        
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            if (visible.isEmpty()) {
                item {
                    CompetitionCard(Modifier.fillMaxWidth()) {
                        CompetitionText("Nenhuma conquista confirmada", size = 16, bold = true)
                        CompetitionText("Conclua uma missão para registrar sua primeira conquista.", size = 12, color = TextMuted)
                    }
                }
            }
            items(visible, key = { it.title }) { achievement -> AchievementCard(achievement, onShare) }
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement, onShare: () -> Unit) {
    CompetitionCard(Modifier.fillMaxWidth(), border = if (achievement.done) Brass.copy(alpha = .55f) else Line) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(if (achievement.done) Brass.copy(alpha = .16f) else CardElevated), contentAlignment = Alignment.Center) {
                Icon(if (achievement.done) achievement.icon else Icons.Default.Lock, null, tint = if (achievement.done) Brass else TextMuted, modifier = Modifier.size(25.dp))
            }
            Column(Modifier.weight(1f)) {
                CompetitionText(achievement.title, size = 15, bold = true)
                CompetitionText(achievement.description, size = 12, color = TextMuted)
            }
            CompetitionPill(if (achievement.local) "ESTADO" else "GLOBAL", color = TextMuted)
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompetitionPill(if (achievement.done) "DESBLOQUEADA" else "EM PROGRESSO", active = true, color = if (achievement.done) Brass else Olive)
            if (!achievement.done) { ProgressTrack(.6f, Olive); CompetitionText(achievement.progress, color = Olive, bold = true) }
            if (achievement.done && achievement.title == "Top 10 do estado") CompetitionShareIcon(onShare, Modifier.size(40.dp))
        }
    }
}

@Composable
fun ShareScreen(kind: Int, distance: CompetitionDistance = CompetitionDistance.K5, missionTime: String? = null, participants: Int? = null, runnerName: String? = null, resultTime: String? = null, placement: Int? = null, elo: Int? = null, podium: List<com.elite.sch.app.ui.components.SharePodiumEntry> = emptyList(), rankPosition: Int? = null, rankState: String? = null, neighbors: List<com.elite.sch.app.ui.components.ShareRankingEntry> = emptyList(), onBack: () -> Unit, onKind: (Int) -> Unit, onShare: () -> Unit, onSave: () -> Unit) {
    val labels = listOf("Participa\u00e7\u00e3o", "P\u00f3dio", "Conquista", "Ranking", "Convite", "Sem app")
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar("Compartilhar", onBack, Modifier.padding(horizontal = 16.dp), eyebrow = null, trailing = {})
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            labels.forEachIndexed { index, label -> CompetitionPill(label, active = kind == index) { onKind(index) } }
        }
        Box(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 52.dp), contentAlignment = Alignment.Center) {
            ShareCardPreview(kind, distance, missionTime, participants, runnerName, resultTime, placement, elo, podium, rankPosition, rankState, neighbors)
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            CompetitionPill("\u21e9", Modifier.size(52.dp), onClick = onSave)
            CompetitionAction("Compartilhar card", onShare, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ShareCardPreview(kind: Int, distance: CompetitionDistance, missionTime: String?, participants: Int?, runnerName: String?, resultTime: String?, placement: Int?, elo: Int?, podium: List<com.elite.sch.app.ui.components.SharePodiumEntry>, rankPosition: Int?, rankState: String?, neighbors: List<com.elite.sch.app.ui.components.ShareRankingEntry>) {
    val context = LocalContext.current
    val bitmap by produceState<Bitmap?>(initialValue = null, kind, distance, missionTime, participants, runnerName, resultTime, placement, elo, podium, rankPosition, rankState, neighbors) {
        value = withContext(Dispatchers.IO) { CompetitionShareCardGenerator.render(context, kind, distance, missionTime, participants, runnerName, resultTime, placement, elo, podium, rankPosition, rankState, neighbors) }
    }
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Pr\u00e9via do card de compartilhamento",
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun NoticesScreen(push: Boolean, onBack: () -> Unit, onPush: (Boolean) -> Unit, onInvite: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar("Avisos", onBack)
        CompetitionText("Nenhum aviso disponível.", color = TextMuted)
    }
}
