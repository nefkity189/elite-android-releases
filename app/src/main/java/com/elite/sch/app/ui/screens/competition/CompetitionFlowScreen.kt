package com.elite.sch.app.ui.screens.competition

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.elite.sch.app.ui.components.CompetitionShareCardGenerator
import com.elite.sch.app.ui.components.ShareCardGenerator
import com.elite.sch.app.ui.components.EliteSymbol
import com.elite.sch.app.ui.components.competition.CompetitionAction
import com.elite.sch.app.ui.components.competition.CompetitionAvatar
import com.elite.sch.app.ui.components.competition.CompetitionBrand
import com.elite.sch.app.ui.components.competition.CompetitionCard
import com.elite.sch.app.ui.components.competition.CompetitionPill
import com.elite.sch.app.ui.components.competition.CompetitionShareIcon
import com.elite.sch.app.ui.components.competition.CompetitionText
import com.elite.sch.app.ui.components.competition.CompetitionTopBar
import com.elite.sch.app.ui.components.competition.CompetitionWatchIcon
import com.elite.sch.app.ui.components.competition.LeagueBadge
import com.elite.sch.app.ui.components.competition.RankingPersonRow
import com.elite.sch.app.ui.components.competition.SectionHeading
import com.elite.sch.app.ui.components.competition.StartCountdownOverlay
import com.elite.sch.app.ui.competition.CompetitionDemoViewModel
import com.elite.sch.app.ui.competition.CompetitionDistance
import com.elite.sch.app.ui.competition.DemoMission
import com.elite.sch.app.ui.competition.readyRunners
import com.elite.sch.app.ui.competition.MissionStatus
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Card
import com.elite.sch.app.ui.theme.CardElevated
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as EliteText
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.service.RunState
import com.elite.sch.app.service.RunTrackingService
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToLong

private enum class CompetitionPage { HUB, ROOM, NOT_STARTED, RACE, RESULT, RANKING, SEASON, ACHIEVEMENTS, SHARE, NOTICES, SPECTATE }

@Composable
fun CompetitionFlowScreen(runState: RunState, onClose: () -> Unit) {
    val model: CompetitionDemoViewModel = viewModel()
    val state by model.state.collectAsState()
    var pageName by rememberSaveable { mutableStateOf(CompetitionPage.HUB.name) }
    val page = CompetitionPage.valueOf(pageName)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var countdown by remember { mutableStateOf<Int?>(null) }
    var lastCountdownSecond by remember { mutableIntStateOf(-1) }
    var countdownMission by rememberSaveable { mutableStateOf("") }

    fun go(to: CompetitionPage) { pageName = to.name }

    // The displayed start comes from the mission's absolute start timestamp.
    // The phone clock is not an authoritative server clock.
    LaunchedEffect(page, state.selectedMission.id) {
        if (page != CompetitionPage.ROOM || state.selectedMission.id.isBlank()) return@LaunchedEffect
        while (true) {
            try {
                model.updateRoomPresence(state.selectedMission)
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                android.util.Log.w("OnlineCompetition", "Não foi possível confirmar presença", failure)
            }
            kotlinx.coroutines.delay(15_000L)
        }
    }
    LaunchedEffect(page, state.selectedMission.id) {
        if (page != CompetitionPage.ROOM) return@LaunchedEffect
        while (true) {
            val mission = model.state.value.selectedMission
            val start = mission.startsAt?.toDate()?.time
            val fresh = com.elite.sch.app.data.competition.ServerTimeSync.isFresh
            val now = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
            val remaining = (start ?: Long.MAX_VALUE) - now
            if (fresh && start != null && mission.readyRunners(now) >= 2 && countdownMission != mission.id) {
                if (remaining in 1L..3_000L) {
                    val second = ((remaining + 999L) / 1000L).toInt()
                    countdown = second
                    if (lastCountdownSecond != second) {
                        lastCountdownSecond = second
                        if (runState.vibrationEnabled) RunTrackingService.vibrateToggleConfirmation(context)
                    }
                } else if (remaining in -60_000L..0L) {
                    if (model.startRace(context, mission)) {
                        countdownMission = mission.id
                        countdown = 0
                        go(CompetitionPage.RACE)
                        break
                    }
                    countdown = null
                    // Allow a bounded retry if the server is still just before the official start.
                    kotlinx.coroutines.delay(2_000L)
                }
            } else countdown = null
            kotlinx.coroutines.delay(100L)
        }
    }
    LaunchedEffect(state.raceFinished, state.selectedMission.id) {
        if (state.raceFinished) go(CompetitionPage.RESULT)
    }
    LaunchedEffect(countdown) {
        if (countdown == 0) {
            if (runState.vibrationEnabled) {
                com.elite.sch.app.service.RunTrackingService.vibrateToggleConfirmation(context)
                kotlinx.coroutines.delay(180L)
                com.elite.sch.app.service.RunTrackingService.vibrateToggleConfirmation(context)
            }
            kotlinx.coroutines.delay(820L)
            countdown = null
        }
    }
    fun publishCard(kind: Int, shouldShare: Boolean) {
        scope.launch {
            val inviteDistance = state.distance
            val mission = state.selectedMission.takeIf { it.id.isNotBlank() && it.distance == inviteDistance }
            val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            val ownResult = mission?.results?.firstOrNull { it.uid == uid && it.finishedAt != null }
            val finishers = mission?.results.orEmpty().filter { it.finishedAt != null }
                .sortedBy { it.finishedAt?.toDate()?.time ?: Long.MAX_VALUE }
            val placement = ownResult?.let { result -> finishers.indexOfFirst { it.uid == result.uid }.takeIf { it >= 0 }?.plus(1) }
            val resultTime = ownResult?.timeSec?.takeIf { it > 0 }?.let { seconds -> "%02d:%02d".format(seconds / 60, seconds % 60) }
            val runnerName = state.onlineUser?.name ?: ownResult?.name
            val ranking = if (state.rankingScope) state.onlineStateRanking else state.onlineRanking
            val rankingIndex = ranking.indexOfFirst { it.uid == uid }
            val rankingPosition = rankingIndex.takeIf { it >= 0 }?.plus(1)
            val podium = finishers.take(3).map { com.elite.sch.app.ui.components.SharePodiumEntry(it.name, formatElapsed(it.timeSec)) }
            val neighbors = ranking.mapIndexed { index, user -> com.elite.sch.app.ui.components.ShareRankingEntry(index + 1, user.name, user.elo) }
                .filter { rankingPosition != null && kotlin.math.abs(it.position - rankingPosition) <= 1 }
            if ((kind == 0 && resultTime == null) || (kind == 1 && (placement == null || placement > 3)) || kind == 2 || (kind == 3 && rankingPosition == null)) {
                android.widget.Toast.makeText(context, "Dados reais ainda não disponíveis para este card", android.widget.Toast.LENGTH_LONG).show()
                return@launch
            }
            runCatching {
                withContext(Dispatchers.IO) {
                    val bitmap = CompetitionShareCardGenerator.render(context, kind, inviteDistance, mission?.time, mission?.participants, runnerName, resultTime, placement, state.onlineUser?.elo, podium, rankingPosition, state.onlineUser?.state.takeIf { state.rankingScope }, neighbors)
                    try { ShareCardGenerator.saveShareCard(context, bitmap) } finally { bitmap.recycle() }
                }
            }.onSuccess { uri ->
                if (shouldShare) {
                    val message = if (kind >= 4) "Participe da corrida ${com.elite.sch.app.ui.components.CompetitionInviteCopy.distanceLabel(inviteDistance)} no Elite. Foco, miss\u00e3o dada, miss\u00e3o cumprida."
                    else "Minha competi\u00e7\u00e3o no Elite: miss\u00e3o cumprida! Foco, miss\u00e3o dada, miss\u00e3o cumprida."
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_TEXT, message)
                        clipData = android.content.ClipData.newRawUri("Elite", uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Compartilhar card Elite"))
                } else {
                    android.widget.Toast.makeText(context, "Card salvo em Imagens/Elite", android.widget.Toast.LENGTH_LONG).show()
                }
            }.onFailure { error ->
                android.util.Log.e("CompetitionShare", "Falha ao gerar card de competi\u00e7\u00e3o", error)
                android.widget.Toast.makeText(context, "N\u00e3o foi poss\u00edvel gerar o card", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    BackHandler(enabled = true) {
        if (countdown != null) return@BackHandler
        if (page == CompetitionPage.HUB) onClose() else go(CompetitionPage.HUB)
    }

    Box(Modifier.fillMaxSize()) {
    Column(
        Modifier.fillMaxSize().background(Background).statusBarsPadding().navigationBarsPadding()
            .testTag("competition_flow"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (page) {
            CompetitionPage.HUB -> CompetitionHubScreen(
                state = state,
                onBack = onClose,
                onDistance = model::chooseDistance,
                onMission = { mission ->
                    model.joinMission(mission) { joined ->
                        model.chooseMission(joined)
                        when (joined.status) {
                            MissionStatus.FINISHED -> go(CompetitionPage.RESULT)
                            MissionStatus.NOT_STARTED -> go(CompetitionPage.NOT_STARTED)
                            MissionStatus.LIVE -> go(CompetitionPage.ROOM)
                            else -> go(CompetitionPage.ROOM)
                        }
                    }
                },
                onRanking = { go(CompetitionPage.RANKING) },
                onAchievements = { go(CompetitionPage.ACHIEVEMENTS) },
                onSeason = { go(CompetitionPage.SEASON) },
                onNotices = { go(CompetitionPage.NOTICES) },
                    onShare = { mission -> model.chooseMission(mission); model.setShareKind(4); go(CompetitionPage.SHARE) },
                    onWatch = { go(CompetitionPage.SPECTATE); model.chooseMission(it) }
            )
            CompetitionPage.ROOM -> CompetitionRoomScreen(
                mission = state.selectedMission,
                confirmed = state.confirmed,
                onBack = { go(CompetitionPage.HUB) },
                onShare = { model.setShareKind(4); go(CompetitionPage.SHARE) },
                onRace = {
                    scope.launch { if (model.startRace(context, model.state.value.selectedMission)) go(CompetitionPage.RACE) }
                },
                onConfirm = model::confirmMission,
                                onLeave = { model.leaveMission(state.selectedMission, { go(CompetitionPage.HUB) }, { model.confirmAbandonMission(state.selectedMission) { go(CompetitionPage.HUB) } }) },
                                onConfirmAbandon = { model.confirmAbandonMission(state.selectedMission) { go(CompetitionPage.HUB) } } as () -> Unit,
                error = state.onlineSyncError
            )
            CompetitionPage.NOT_STARTED -> NotStartedScreen(
                mission = state.selectedMission,
                onBack = { go(CompetitionPage.HUB) },
                onShare = { model.setShareKind(4); go(CompetitionPage.SHARE) },
                onNext = { go(CompetitionPage.HUB) }
            )
            CompetitionPage.RACE -> MissionRaceScreen(
                mission = state.selectedMission,
                runState = com.elite.sch.app.data.competition.CompetitionSync.raceState(runState),
                onBack = { go(CompetitionPage.ROOM) },
                onDesist = { model.confirmAbandonMission(state.selectedMission) { go(CompetitionPage.HUB) } }
            )
            CompetitionPage.RESULT -> MissionResultScreen(
                state = state,
                results = state.selectedMission.results,
                onBack = { go(CompetitionPage.HUB) },
                onRanking = { go(CompetitionPage.RANKING) },
                onShare = { model.setShareKind(0); go(CompetitionPage.SHARE) },
                onScope = model::setResultState
            )
            CompetitionPage.RANKING -> RankingScreen(
                state = state,
                ranking = if (state.rankingScope) state.onlineStateRanking else state.onlineRanking,
                onBack = { go(CompetitionPage.HUB) },
                onSeason = { go(CompetitionPage.SEASON) },
                onShare = { model.setShareKind(3); go(CompetitionPage.SHARE) },
                onScope = model::setRankingScope,
                onDistance = model::setRankingDistance,
                onHistory = model::setRankingHistory
            )
            CompetitionPage.SEASON -> SeasonScreen(
                user = state.onlineUser,
                onBack = { go(CompetitionPage.HUB) },
                onShare = { model.setShareKind(3); go(CompetitionPage.SHARE) }
            )
            CompetitionPage.ACHIEVEMENTS -> AchievementsScreen(
                user = state.onlineUser,
                local = state.achievementLocal,
                onBack = { go(CompetitionPage.HUB) },
                onRanking = { go(CompetitionPage.RANKING) },
                onShare = { go(CompetitionPage.SHARE) },
                onLocal = model::setAchievementLocal
            )
            CompetitionPage.SHARE -> {
                val shareMission = state.selectedMission.takeIf { it.id.isNotBlank() && it.distance == state.distance }
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                val ownResult = shareMission?.results?.firstOrNull { it.uid == uid && it.finishedAt != null }
                val finishers = shareMission?.results.orEmpty().filter { it.finishedAt != null }
                    .sortedBy { it.finishedAt?.toDate()?.time ?: Long.MAX_VALUE }
                val placement = ownResult?.let { result -> finishers.indexOfFirst { it.uid == result.uid }.takeIf { it >= 0 }?.plus(1) }
                val resultTime = ownResult?.timeSec?.takeIf { it > 0 }?.let { seconds -> "%02d:%02d".format(seconds / 60, seconds % 60) }
                val ranking = if (state.rankingScope) state.onlineStateRanking else state.onlineRanking
                val rankingPosition = ranking.indexOfFirst { it.uid == uid }.takeIf { it >= 0 }?.plus(1)
                ShareScreen(
                    kind = state.shareKind,
                    distance = state.distance,
                    missionTime = shareMission?.time,
                    participants = shareMission?.participants,
                    runnerName = state.onlineUser?.name ?: ownResult?.name,
                    resultTime = resultTime,
                    placement = placement,
                    elo = state.onlineUser?.elo,
                    podium = finishers.take(3).map { com.elite.sch.app.ui.components.SharePodiumEntry(it.name, formatElapsed(it.timeSec)) },
                    rankPosition = rankingPosition,
                    rankState = state.onlineUser?.state.takeIf { state.rankingScope },
                    neighbors = ranking.mapIndexed { index, user -> com.elite.sch.app.ui.components.ShareRankingEntry(index + 1, user.name, user.elo) }.filter { rankingPosition != null && kotlin.math.abs(it.position - rankingPosition) <= 1 },
                    onBack = { go(CompetitionPage.HUB) },
                    onKind = model::setShareKind,
                    onShare = { publishCard(state.shareKind, true) },
                    onSave = { publishCard(state.shareKind, false) }
                )
            }
            CompetitionPage.NOTICES -> NoticesScreen(
                push = state.noticePush,
                onBack = { go(CompetitionPage.HUB) },
                onPush = model::setNoticePush,
                onInvite = { model.setShareKind(4); go(CompetitionPage.SHARE) }
            )
            CompetitionPage.SPECTATE -> MissionSpectatorScreen(
                mission = state.selectedMission,
                onBack = { go(CompetitionPage.HUB) }
            )
        }
    }
    countdown?.let { StartCountdownOverlay(it) }
    }
}

@Composable
private fun CompetitionHubScreen(
    state: com.elite.sch.app.ui.competition.CompetitionDemoState,
    onBack: () -> Unit,
    onDistance: (CompetitionDistance) -> Unit,
    onMission: (DemoMission) -> Unit,
    onRanking: () -> Unit,
    onAchievements: () -> Unit,
    onSeason: () -> Unit,
    onNotices: () -> Unit,
    onShare: (DemoMission) -> Unit,
    onWatch: (DemoMission) -> Unit = {},
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth().height(52.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(40.dp).clip(CircleShape).background(Card).border(1.dp, Line, CircleShape).clickable(onClick = onBack), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ArrowBack, "Voltar", tint = EliteText)
            }
            CompetitionBrand()
            Spacer(Modifier.weight(1f))
            val syncedNow = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
                .takeIf { it > 0L } ?: System.currentTimeMillis()
            val localTime = Instant.ofEpochMilli(syncedNow)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
            CompetitionPill("◷ ${localTime.format(DateTimeFormatter.ofPattern("HH:mm"))}", color = Olive, active = true)
            Box(Modifier.size(36.dp).clickable(onClick = onNotices), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Notifications, "Avisos", tint = TextMuted)
            }
        }
        CompetitionText("FOCO, MISSÃO DADA, MISSÃO CUMPRIDA.", size = 12, bold = true, color = Olive, letterSpacing = 1.1f)
        CompetitionText("COMPETIÇÃO ONLINE", size = 31, bold = true, display = true, maxLines = 1)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ShortcutCard("Ranking", "Dados do servidor", Icons.Default.EmojiEvents, onRanking, Modifier.weight(1f))
            ShortcutCard("Conquistas", "Dados do perfil", Icons.Default.Star, onAchievements, Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Background).border(1.dp, Line, RoundedCornerShape(28.dp)).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            CompetitionDistance.entries.forEach { distance ->
                CompetitionPill(distance.label, Modifier.weight(1f), active = state.distance == distance, color = Olive) { onDistance(distance) }
            }
        }
        SectionHeading("Missões de hoje", trailing = "02:00 – 21:00")
        state.onlineSyncError?.let { error ->
                    CompetitionText("Falha na sincronização com o Firebase: $error", size = 12, color = Red)
        }
        val missions = state.onlineMissions
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (missions.isEmpty()) {
                item { CompetitionText("Nenhuma missão online disponível para esta distância.", color = TextMuted, size = 14) }
            } else {
                items(missions, key = { it.id.ifBlank { it.time } }) { mission ->
                    MissionCard(mission, onClick = { onMission(mission) }, onShare = { onShare(mission) }, onWatch = { onWatch(mission) })
                }
            }
        }
    }
}

@Composable
private fun ShortcutCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(60.dp).clip(RoundedCornerShape(18.dp)).background(Card)
            .border(1.dp, Line, RoundedCornerShape(18.dp)).clickable(onClick = onClick).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(Brass.copy(alpha = .15f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Brass, modifier = Modifier.size(22.dp))
        }
        Column(Modifier.weight(1f)) {
            CompetitionText(title, size = 16, bold = true, maxLines = 1)
            CompetitionText(subtitle, color = TextMuted, size = 12, maxLines = 1)
        }
    }
}

@Composable
private fun MissionCard(mission: DemoMission, onClick: () -> Unit, onShare: () -> Unit, onWatch: () -> Unit = {}) {
    val statusLabel = when (mission.status) {
        MissionStatus.NEEDS_ONE -> "FALTA 1 COMPETIDOR"
        MissionStatus.OPEN -> "AGUARDANDO"
        MissionStatus.CONFIRMED -> "CONFIRMADA"
        MissionStatus.LIVE -> "EM ANDAMENTO"
        MissionStatus.FINISHED -> "ENCERRADA"
        MissionStatus.NOT_STARTED -> "NÃO INICIADA"
    }
    val statusColor = when (mission.status) {
        MissionStatus.NEEDS_ONE -> Color(0xFFE0A93A)
        MissionStatus.OPEN -> Olive
        MissionStatus.CONFIRMED -> Color(0xFF7FB069)
        MissionStatus.LIVE -> Red
        MissionStatus.FINISHED, MissionStatus.NOT_STARTED -> TextMuted
    }
    if (mission.status == MissionStatus.FINISHED || mission.status == MissionStatus.NOT_STARTED || mission.status == MissionStatus.LIVE) {
        Row(
            Modifier.fillMaxWidth().height(74.dp).clip(RoundedCornerShape(16.dp)).background(Card)
                .border(1.dp, Line, RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CompetitionText(mission.time, size = 28, display = true, bold = true)
            CompetitionPill(mission.distance.label, color = TextMuted)
            Column(Modifier.weight(1f)) {
                CompetitionPill(statusLabel, color = statusColor, active = true)
                if (mission.status == MissionStatus.NOT_STARTED) CompetitionText("por falta de competidores", size = 11, color = TextMuted)
            }
            CompetitionShareIcon(onShare, Modifier.size(44.dp))
            if (mission.status == MissionStatus.LIVE) CompetitionWatchIcon(onWatch, Modifier.size(44.dp))
        }
    } else {
        CompetitionCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompetitionText(mission.time, size = 36, display = true, bold = true)
                Spacer(Modifier.width(12.dp)); CompetitionPill(mission.distance.label)
                Spacer(Modifier.weight(1f)); CompetitionPill(statusLabel, color = statusColor, active = true)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EmptyAvatar()
                CompetitionText("${mission.participants}/∞", size = 19, display = true, bold = true)
                CompetitionText("Mínimo 2 competidores", size = 12, color = TextMuted, maxLines = 1)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompetitionAction("Entrar na missão", onClick, Modifier.weight(1f), tag = "btn_entrar_missao")
                CompetitionShareIcon(onShare)
            }
        }
    }
}

@Composable
private fun EmptyAvatar() {
    Box(Modifier.size(30.dp).clip(CircleShape).border(1.dp, TextMuted, CircleShape), contentAlignment = Alignment.Center) {
        CompetitionText("+", size = 18, color = TextMuted)
    }
}

@Composable
private fun CompetitionRoomScreen(mission: DemoMission, confirmed: Boolean, onBack: () -> Unit, onShare: () -> Unit, onRace: () -> Unit, onConfirm: () -> Unit, onLeave: () -> Unit, onConfirmAbandon: () -> Unit, error: String?) {
    var nowMs by remember(mission.id) { mutableStateOf(com.elite.sch.app.data.competition.ServerTimeSync.nowSync()) }
    LaunchedEffect(mission.id) {
        while (true) {
            nowMs = com.elite.sch.app.data.competition.ServerTimeSync.nowSync()
            kotlinx.coroutines.delay(1_000L)
        }
    }
    
    val startTimeMs = mission.startsAt?.toDate()?.time ?: 0L
    val secondsToStart = ((startTimeMs - nowMs).coerceAtLeast(0L) + 999L) / 1000L
    val isStartTime = nowMs >= startTimeMs
    val showCountdown = confirmed && secondsToStart <= 3 && secondsToStart > 0
    val hasStarted = isStartTime
    
    var showAbandonDialog by remember { mutableStateOf(false) }
    
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar("Missão ${mission.distance.label} · ${mission.time}", onBack, eyebrow = "Sala da missão")
        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
        CompetitionCard(Modifier.fillMaxWidth(), border = if (confirmed) OliveDark else Line) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                CompetitionText("COMEÇA EM", size = 13, bold = true, color = TextMuted, letterSpacing = 1.2f)
                CompetitionPill(
                    when {
                        showCountdown -> "LARGADA EM ${secondsToStart}"
                        isStartTime && mission.participants >= 2 -> "GO!"
                        confirmed -> "CONFIRMADA"
                        else -> "FALTA 1 COMPETIDOR"
                    },
                    color = when {
                        showCountdown -> Red
                        isStartTime && mission.participants >= 2 -> Olive
                        confirmed -> Olive
                        else -> Color(0xFFE0A93A)
                    },
                    active = true
                )
            }
            CompetitionText(
                if (showCountdown) secondsToStart.toString() 
                else if (isStartTime && mission.participants >= 2) "GO!" 
                else secondsToStart.let(::formatElapsed),
                            size = if (showCountdown || (isStartTime && mission.participants >= 2)) 72 else 68,
                display = true,
                bold = true,
                modifier = Modifier.fillMaxWidth(),
                color = if (showCountdown) Red else if (isStartTime && mission.participants >= 2) Olive else EliteText
            )
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                LobbyMetric("DISTÂNCIA", mission.distance.label)
                LobbyMetric("HORÁRIO", mission.time)
                LobbyMetric("MÍNIMO", "2")
            }
            CompetitionText("⚑  Vence quem concluir primeiro", size = 14, bold = true, color = Olive, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        CompetitionCard(Modifier.fillMaxWidth()) {
            SectionHeading("Competidores", trailing = "${mission.readyRunners(nowMs)} presente(s)")
            if (mission.results.isEmpty()) {
                CompetitionText("Carregando participantes da sala", color = TextMuted, size = 13)
            } else {
                mission.results.forEach { player ->
                    val isCurrent = player.uid == com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                    val readyAt = player.readyAt?.toDate()?.time
                    val isPresent = readyAt != null && nowMs - readyAt in 0L..45_000L && player.withdrawnAt == null && !player.pending
                    LobbyRunner(if (isCurrent) "EU" else player.name.take(2).uppercase(), if (isCurrent) "Você" else player.name, if (isPresent) "Presente" else "Inscrito · aguardando presença", isPresent)
                }
            }
        }
        CompetitionCard(Modifier.fillMaxWidth(), border = OliveDark) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Groups, null, tint = Olive, modifier = Modifier.size(28.dp))
                Column(Modifier.weight(1f)) {
                    SectionHeading("Chame reforços para a missão")
                    CompetitionText("O link leva direto para esta missão. Quem já tem o Elite cai na sala; quem não tem vai para o download.", size = 13, color = TextMuted)
                }
            }
            CompetitionAction("Compartilhar convite", onShare, Modifier.fillMaxWidth(), secondary = true)
        }
        CompetitionCard(Modifier.fillMaxWidth()) {
            SectionHeading("Regras da missão")
            listOf("Vence quem concluir ${mission.distance.label} primeiro.", "A missão só começa com no mínimo 2 competidores.", "É preciso completar a distância para registrar o resultado.").forEach { it ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Check, null, tint = Olive, modifier = Modifier.size(18.dp))
                    CompetitionText(it, size = 13)
                }
            }
        }
        }
        error?.let { CompetitionText(it, size = 12, color = Red) }
        CompetitionAction(
            when {
                showCountdown -> "Largando em ${secondsToStart}..."
                isStartTime && confirmed -> "Entrar na prova"
                confirmed -> "Aguardando horário da largada"
                else -> "Aguardando o mínimo de competidores"
            },
            if (isStartTime && confirmed) onRace else onConfirm,
            Modifier.fillMaxWidth(),
            enabled = isStartTime && confirmed,
            tag = "btn_entrar_prova"
        )
        CompetitionAction(
            if (hasStarted) "Abandonar prova" else "Sair da sala",
                    { if (hasStarted) { showAbandonDialog = true } else { onLeave() } },
            Modifier.fillMaxWidth().navigationBarsPadding(),
            secondary = true,
            // Leaving before the start must remain possible, including when
            // the room is waiting for the second competitor.
            enabled = true
        )
        
        // Dialog de confirmação de abandono
        if (showAbandonDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAbandonDialog = false },
                title = { CompetitionText("Abandonar prova", size = 20, bold = true) },
                text = { CompetitionText("A prova já começou. Se você abandonar agora, seu resultado será marcado como desistência e não poderá voltar. Tem certeza?", size = 14, color = TextMuted) },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        showAbandonDialog = false
                        onConfirmAbandon()
                    }) {
                        CompetitionText("Confirmar abandono", color = Red, bold = true)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showAbandonDialog = false }) {
                        CompetitionText("Cancelar", color = TextMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun LobbyMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CompetitionText(label, size = 11, bold = true, color = TextMuted, letterSpacing = 1f)
        CompetitionText(value, size = 22, display = true, bold = true)
    }
}

@Composable
private fun LobbyRunner(initials: String, name: String, status: String, active: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        if (active) CompetitionAvatar(initials, size = 42, highlighted = initials == "RN") else EmptyAvatar()
        Column(Modifier.weight(1f)) {
            CompetitionText(name, bold = true, size = 15, color = if (active) EliteText else TextMuted)
            CompetitionText(status, size = 12, color = TextMuted)
        }
        if (active) CompetitionText("●  Na sala", color = Olive, size = 12)
    }
}

@Composable
private fun NotStartedScreen(mission: DemoMission, onBack: () -> Unit, onShare: () -> Unit, onNext: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CompetitionTopBar("Miss\u00e3o n\u00e3o iniciada", onBack, eyebrow = "Competi\u00e7\u00e3o online")
        CompetitionCard(Modifier.fillMaxWidth(), border = Line) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(64.dp).clip(CircleShape).background(CardElevated), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Schedule, null, tint = TextMuted, modifier = Modifier.size(32.dp))
                }
                CompetitionPill("N\u00c3O INICIADA POR FALTA DE COMPETIDORES", color = TextMuted)
                CompetitionText("MISS\u00c3O N\u00c3O INICIADA", size = 27, display = true, bold = true, modifier = Modifier.fillMaxWidth())
                CompetitionText("N\u00e3o houve competidores suficientes. O m\u00ednimo \u00e9 2.", size = 14, color = TextMuted, modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LobbyMetric("HOR\u00c1RIO", mission.time); LobbyMetric("DIST\u00c2NCIA", mission.distance.label); LobbyMetric("M\u00cdNIMO", "2")
                }
            }
        }
        CompetitionInviteCard(mission, onShare, Modifier.fillMaxWidth())
        CompetitionAction("Ver pr\u00f3ximas miss\u00f5es", onNext, Modifier.fillMaxWidth(), secondary = true)
    }
}

@Composable
private fun CompetitionInviteCard(mission: DemoMission, onShare: () -> Unit, modifier: Modifier = Modifier) {
    CompetitionCard(modifier, border = OliveDark) {
        Row(Modifier.fillMaxWidth().height(148.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                Modifier.width(94.dp).fillMaxSize().clip(RoundedCornerShape(12.dp))
                    .background(Background).border(1.dp, OliveDark, RoundedCornerShape(12.dp)).padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    EliteSymbol(modifier = Modifier.size(14.dp), useBlade = true, size = 14.dp)
                    CompetitionText("ELITE", size = 10, bold = true, color = Olive, letterSpacing = 1f)
                }
                CompetitionText("MISS\u00c3O PRECISA DE VOC\u00ca", size = 12, display = true, bold = true, maxLines = 3)
                CompetitionText("${mission.distance.label} \u00b7 ${mission.time}", size = 12, bold = true, color = Olive)
                Box(Modifier.fillMaxWidth().height(3.dp).clip(CircleShape).background(Red))
                CompetitionText("FOCO, MISS\u00c3O DADA, MISS\u00c3O CUMPRIDA.", size = 5, color = TextMuted, maxLines = 2)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                CompetitionText("CONVOQUE COMPETIDORES", size = 19, display = true, bold = true)
                CompetitionText(
                    "Compartilhe o card de convite e atraia gente para as pr\u00f3ximas miss\u00f5es. Quem tem o Elite entra direto; quem n\u00e3o tem vai para o download.",
                    size = 12, color = TextMuted
                )
            }
        }
        Box(
            Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(28.dp))
                .background(Olive).clickable(onClick = onShare),
            contentAlignment = Alignment.Center
        ) {
            CompetitionText("Compartilhar convite", size = 16, bold = true, color = Color(0xFF0E1306))
        }
    }
}

@Composable
private fun MissionRaceScreen(
    mission: DemoMission,
    runState: RunState,
    onBack: () -> Unit,
    onDesist: () -> Unit
) {
    val distanceMeters = runState.totalDistanceMeters
    val distanceKm = (distanceMeters / 1000.0).toFloat().coerceAtMost(mission.distance.meters / 1000f)
    val targetKm = mission.distance.meters / 1000f
    val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
    val position = mission.results.filter { it.withdrawnAt == null }
        .sortedWith(compareByDescending<com.elite.sch.app.data.competition.OnlineMissionResult> { it.progressKm }
            .thenBy { it.timeSec }.thenBy { it.uid })
        .indexOfFirst { it.uid == currentUid }.takeIf { it >= 0 }?.plus(1)
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            CompetitionPill("${mission.distance.label} · ${mission.time}", active = true, color = Olive)
            CompetitionPill("●  AO VIVO", active = true, color = Red)
            CompetitionPill("⚑  1º a concluir vence", active = true, color = Olive)
        }
        Column(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CompetitionText("TEMPO DECORRIDO", size = 13, bold = true, color = TextMuted, letterSpacing = 1.1f)
            CompetitionText(formatElapsed(runState.elapsedSeconds), size = 92, display = true, bold = true)
        }
        CompetitionCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.Bottom) {
                    CompetitionText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f", distanceKm), size = 32, display = true, bold = true)
                    CompetitionText(" / ${String.format(Locale.forLanguageTag("pt-BR"), "%.1f", targetKm)} km", size = 19, display = true, color = TextMuted)
                }
                CompetitionText("${(distanceKm / targetKm * 100).toInt()}%", size = 23, display = true, bold = true, color = Olive)
            }
            Box(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(CardElevated)) {
                Box(Modifier.fillMaxWidth((distanceMeters.toFloat() / mission.distance.meters.toFloat()).coerceIn(0f, 1f)).fillMaxSize().clip(CircleShape).background(Olive))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RaceMetric("PACE ATUAL", runState.averagePaceFormatted, Modifier.weight(1f))
                    RaceMetric("SUA POSIÇÃO", position?.let { "${it}º" } ?: "—", Modifier.weight(1f))
        }
        CompetitionCard(Modifier.weight(1f).fillMaxWidth()) {
            SectionHeading("Placar ao vivo", trailing = "Dados do servidor")
            Column(Modifier.verticalScroll(rememberScrollState())) {
                mission.results.filter { it.withdrawnAt == null }.sortedByDescending { it.progressKm }.forEach { player ->
                    val own = player.uid == com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                    val km = player.progressKm
                    val age = player.updatedAt?.toDate()?.time?.let {
                        (com.elite.sch.app.data.competition.ServerTimeSync.nowSync() - it).coerceAtLeast(0) / 1000
                    }
                    RankingPersonRow(0, if (own) "EU" else player.name.take(2).uppercase(),
                        if (own) "Voce" else player.name, "",
                        String.format(Locale.forLanguageTag("pt-BR"), "%.2f km", km),
                        currentUser = own,
                        detail = if (player.pending) "Envio pendente" else
                            "Tempo: " + formatElapsed(player.timeSec) + if (age == null || age > 30) " - sinal atrasado" else "")
                }
            }

        }
        Row(Modifier.fillMaxWidth().padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(Icons.Default.Info, null, tint = TextMuted, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp))
            CompetitionText("Conclua a distância para registrar o resultado.", size = 12, color = TextMuted)
        }
        val syncError by com.elite.sch.app.data.competition.CompetitionSync.error.collectAsState()
        syncError?.let { CompetitionText(it, size = 12, color = Red) }
        CompetitionAction("Desistir", onDesist, Modifier.fillMaxWidth(), secondary = true)
    }
}

@Composable
private fun RaceMetric(label: String, value: String, modifier: Modifier = Modifier) {
    CompetitionCard(modifier) {
        CompetitionText(label, size = 11, bold = true, color = TextMuted, letterSpacing = 1f)
        CompetitionText(value, size = 30, display = true, bold = true, color = if (label.contains("POSIÇÃO")) Olive else EliteText)
    }
}

private fun formatElapsed(totalSeconds: Long): String {
    val safe = totalSeconds.coerceAtLeast(0L)
    val hours = safe / 3600L
    val minutes = (safe % 3600L) / 60L
    val seconds = safe % 60L
    return if (hours > 0) String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
    else String.format(Locale.US, "%02d:%02d", minutes, seconds)
}

@Composable
private fun MissionResultScreen(
    state: com.elite.sch.app.ui.competition.CompetitionDemoState,
    results: List<com.elite.sch.app.data.competition.OnlineMissionResult>,
    onBack: () -> Unit,
    onRanking: () -> Unit,
    onShare: () -> Unit,
    onScope: (Boolean) -> Unit
) {
    val currentUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
    val finished = results.filter { it.finishedAt != null }
    val myResult = finished.firstOrNull { it.uid == currentUid }
    val myPlace = finished.indexOfFirst { it.uid == currentUid }.takeIf { it >= 0 }?.plus(1)
    val scoreReady = state.onlineUser?.lastScoredMissionId == state.selectedMission.id
    val globalPlace = state.onlineRanking.indexOfFirst { it.uid == currentUid }.takeIf { it >= 0 }?.plus(1)
    val statePlace = state.onlineStateRanking.indexOfFirst { it.uid == currentUid }.takeIf { it >= 0 }?.plus(1)
    val averagePace = myResult?.timeSec?.takeIf { it > 0 }?.let {
        formatElapsed((it / (state.selectedMission.distance.meters / 1000.0)).roundToLong())
    }
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompetitionTopBar(if (myResult != null) "Chegada registrada" else "Aguardando sincronização", onBack, eyebrow = "${state.selectedMission.distance.label} · ${state.selectedMission.time}")
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CompetitionCard(Modifier.fillMaxWidth(), border = Brass) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        CompetitionText(myPlace?.let { "${it}º" } ?: "—", size = 78, display = true, bold = true, color = Brass)
                        CompetitionText("LUGAR", size = 13, bold = true, color = Brass, letterSpacing = 1.1f)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        CompetitionAvatar("EU", highlighted = true, size = 48)
                                            CompetitionText("Você", size = 17, bold = true)
                        CompetitionText("Perfil carregado do servidor", color = TextMuted)
                    }
                }
                val winner = results.firstOrNull { it.finishedAt != null }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    LobbyMetric("TEMPO TOTAL", myResult?.timeSec?.let(::formatElapsed) ?: "—")
                    LobbyMetric("PACE MÉDIO", averagePace ?: "—")
                    LobbyMetric("ELO", if (scoreReady) state.onlineUser?.elo?.toString() ?: "—" else "—")
                }
                if (winner != null) {
                    CompetitionText("Primeira chegada recebida: ${winner.name}", size = 14, bold = true, color = Brass)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        CompetitionText("Classificação por recebimento no servidor", size = 13, color = TextMuted)
                    CompetitionPill("Dados online", active = true, color = Olive)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RankChip("GLOBAL", if (scoreReady) globalPlace?.let { "${it}º" } ?: "—" else "—", Modifier.weight(1f))
                    RankChip("ESTADO", if (scoreReady) statePlace?.let { "${it}º" } ?: "—" else "—", Modifier.weight(1f))
                }
            }
            CompetitionCard(Modifier.fillMaxWidth()) {
                SectionHeading("Classificação da missão", trailing = "Elo da missão")
                if (results.isEmpty()) {
                    CompetitionText("Os resultados serão exibidos quando a missão online for concluída.", color = TextMuted, size = 13)
                } else {
                    results.forEachIndexed { index, result ->
                        RankingPersonRow(
                            index + 1,
                            if (index == 0) "1º" else "${index + 1}º",
                            result.name,
                            "—",
                            result.timeSec.takeIf { it > 0 }?.let(::formatElapsed) ?: "Em andamento",
                            currentUser = result.uid == com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid,
                            detail = if (result.finishedAt != null) "Concluiu a distância" else "Em andamento"
                        )
                    }
                }
            }
            CompetitionCard(Modifier.fillMaxWidth()) {
                ResultScopeTabs(state.resultTabState, onScope)
                CompetitionText(if (scoreReady) "Ranking e elo confirmados pelo servidor. A ordem da prova pode mudar após chegadas atrasadas."
                    else "Chegada confirmada. Elo e ranking aguardam sincronização com o servidor.", color = TextMuted, size = 13)
                
            }
        }
        Row(Modifier.fillMaxWidth().navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompetitionAction("Compartilhar resultado", onShare, Modifier.weight(1.2f), secondary = true)
            CompetitionAction("Ver ranking completo", onRanking, Modifier.weight(1f), secondary = true)
        }
    }
}

@Composable
private fun RankChip(label: String, value: String, modifier: Modifier = Modifier) {
    CompetitionCard(modifier) {
        CompetitionText(label, size = 10, bold = true, color = TextMuted, letterSpacing = .8f)
        CompetitionText(value, size = 24, display = true, bold = true)
    }
}

@Composable
private fun ResultScopeTabs(value: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Background).padding(3.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        com.elite.sch.app.ui.components.competition.CompetitionPill("Global", Modifier.weight(1f), active = !value) { onChange(false) }
        com.elite.sch.app.ui.components.competition.CompetitionPill("Meu estado", Modifier.weight(1f), active = value) { onChange(true) }
    }
}

@Composable
private fun ScopeTabs(value: Boolean, left: String, right: String, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Background).padding(3.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        CompetitionPill(left, Modifier.weight(1f), active = !value) { onChange(false) }
        CompetitionPill(right, Modifier.weight(1f), active = value) { onChange(true) }
    }
}

@Composable
private fun MissionSpectatorScreen(mission: DemoMission, onBack: () -> Unit) {
    var elapsedSec by remember { mutableStateOf<Long>(0L) }
    LaunchedEffect(mission.id) {
        while (true) {
            val startMs = mission.startsAt?.toDate()?.time ?: System.currentTimeMillis()
            elapsedSec = (System.currentTimeMillis() - startMs) / 1000L
            kotlinx.coroutines.delay(500L)
        }
    }

    Column(
        Modifier.fillMaxSize().background(Background).statusBarsPadding().navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CompetitionTopBar(title = "Assistir corrida", onBack = onBack)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            item {
                // Timer pill
                Row(
                    Modifier.fillMaxWidth().height(52.dp).clip(RoundedCornerShape(16.dp)).background(Card),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.padding(start = 14.dp)) {
                        CompetitionText("AO VIVO", size = 10, bold = true, color = Olive, letterSpacing = .8f)
                        CompetitionText("${mission.distance.label} • ${mission.time}", size = 13, color = TextMuted)
                    }
                    Box(Modifier.size(44.dp).clip(CircleShape).background(OliveSoft), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Red, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Leader card
            item {
                val sorted = mission.results
                    .filter { !it.pending && (it.withdrawnAt == null || it.finishedAt != null) }
                    .sortedWith(compareBy({ it.finishedAt != null }, { it.timeSec }))
                val leader = sorted.firstOrNull()
                CompetitionCard(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        CompetitionAvatar(leader?.name ?: "—")
                        Column(Modifier.weight(1f)) {
                            CompetitionText(leader?.name ?: "Sem resultados ainda", size = 15, bold = true, maxLines = 1)
                            if (leader != null) {
                                CompetitionText("Lidera a missão", size = 12, color = TextMuted)
                            } else {
                                CompetitionText("Aguarde os primeiros competidores", size = 12, color = TextMuted)
                            }
                        }
                        if (leader?.finishedAt == null) {
                            CompetitionPill(formatElapsed(elapsedSec), color = Olive, active = true)
                        } else {
                            CompetitionPill(formatElapsed(leader.timeSec), color = Brass, active = true)
                        }
                    }
                }
            }

            // Metrics
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RaceMetric("TEMPO CORRIDA", formatElapsed(elapsedSec), Modifier.weight(1f))
                    RaceMetric("DISTÂNCIA", mission.distance.label, Modifier.weight(1f))
                }
            }

            // Live scoreboard
            item {
                CompetitionCard(Modifier.fillMaxWidth()) {
                    SectionHeading("Em tempo real")
                    if (mission.results.isEmpty()) {
                        CompetitionText("Nenhum resultado registrado ainda.", color = TextMuted, size = 13)
                    } else {
                        val all = mission.results.sortedWith(compareBy({ it.withdrawnAt != null || it.finishedAt == null }, { it.finishedAt?.toDate()?.time ?: Long.MAX_VALUE }, { it.timeSec }))
                        all.forEachIndexed { index, r ->
                            val initials = r.name.takeUnless { it.isBlank() }?.take(2)?.uppercase() ?: "?"
                            Row(
                                Modifier.fillMaxWidth().height(48.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CompetitionText("#${index + 1}", size = 14, bold = true, color = if (index == 0) Brass else TextMuted)
                                CompetitionAvatar(initials)
                                Column(Modifier.weight(1f)) {
                                    CompetitionText(r.name, size = 14, bold = true, maxLines = 1)
                                    CompetitionText(
                                        when {
                                            r.finishedAt != null -> "Concluiu em ${formatElapsed(r.timeSec)}"
                                            r.withdrawnAt != null -> "Desistiu"
                                            r.pending -> "Aguardando"
                                            else -> formatElapsed(elapsedSec)
                                        },
                                        size = 11, color = TextMuted
                                    )
                                }
                                if (r.paceSecPerKm != null && r.timeSec > 0) {
                                    val paceMin = r.timeSec / (r.progressKm / 1000.0).toLong()
                                    CompetitionText(
                                        " %s /km".format(String.format(Locale.US, "%d:%02d", paceMin / 60, paceMin % 60)),
                                        size = 12, color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Note
            item {
                CompetitionCard(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.Info, contentDescription = null, tint = TextMuted,
                            modifier = Modifier.size(16.dp).align(Alignment.Top)
                        )
                        CompetitionText(
                            "Modo espectador — apenas visualização. Não afeta a corrida nem consome bateria adicional.",
                            size = 11, color = TextMuted
                        )
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp).navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CompetitionAction("Voltar às missões", onBack, Modifier.weight(1f), secondary = true)
        }
    }
}

