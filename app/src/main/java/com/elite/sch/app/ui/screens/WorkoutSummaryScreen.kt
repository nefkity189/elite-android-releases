package com.elite.sch.app.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import com.elite.sch.app.tracker.KmSplitsCalculator
import com.elite.sch.app.tracker.PaceCalculator
import com.elite.sch.app.tracker.PaceDataPoint
import com.elite.sch.app.tracker.PaceSeriesCalculator
import com.elite.sch.app.tracker.RouteNormalizer
import com.elite.sch.app.tracker.RoutePointCoord
import com.elite.sch.app.ui.components.ActionButton
import com.elite.sch.app.ui.components.EliteCard
import com.elite.sch.app.ui.components.BrandGradientButton
import com.elite.sch.app.ui.components.MetricBlock
import com.elite.sch.app.ui.components.ShareCardData
import com.elite.sch.app.ui.components.ShareCardGenerator
import com.elite.sch.app.ui.components.WorkoutFormatters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import androidx.compose.ui.graphics.SolidColor
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.Background
import com.elite.sch.app.ui.theme.Brass
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.Red
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.EliteDisplayFont
import com.elite.sch.app.ui.theme.EliteLogoFont
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutSummaryScreen(
    workoutTitle: String,
    elapsedSeconds: Long,
    totalDistanceMeters: Double,
    modifier: Modifier = Modifier,
    routePoints: List<RoutePointEntity> = emptyList(),
    allCompletedLogs: List<WorkoutLogEntity> = emptyList(),
    initialRpe: Int? = null,
    initialAnotacoes: String? = null,
    isDetailOnly: Boolean = false,
    finalizadoEm: Long? = null,
    modalidadeLabel: String? = null,
    diagnostics: com.elite.sch.app.tracker.GpsDiagnostics? = null,
    ajustesAutomaticos: Boolean = true,
    onPainChange: (dor: Int, regiaoDor: String?) -> Unit = { _, _ -> },
    onSaveLog: (esforco: Int, anotacoes: String?) -> Unit,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var rpeValue by remember { mutableIntStateOf(initialRpe ?: 6) }
    var dorValue by remember { mutableIntStateOf(0) }
    var regiaoDorValue by remember { mutableStateOf<String?>(null) }
    var anotacoesInput by remember { mutableStateOf(initialAnotacoes ?: "") }
    var isSharing by remember { mutableStateOf(false) }
    var showSharePreview by remember { mutableStateOf(false) }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeFormatted = if (minutes >= 60) {
        val hours = minutes / 60
        val remMins = minutes % 60
        "%02d:%02d:%02d".format(hours, remMins, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
    val distanceKm = "%.2f km".format(totalDistanceMeters / 1000.0)

    val paceSeconds = PaceCalculator.calculateAveragePaceSeconds(elapsedSeconds, totalDistanceMeters)
    val paceFormatted = PaceCalculator.formatPace(paceSeconds)

    // Série de ritmo calculada pela função pura
    val paceSeries = remember(routePoints) {
        PaceSeriesCalculator.calculate(routePoints, windowSeconds = 30L)
    }

    // Seus recordes pessoais calculados entre todas as corridas concluídas + corrida atual
    val allDistances = allCompletedLogs.map { it.distanciaMetros } + totalDistanceMeters
    val allDurations = allCompletedLogs.map { it.duracaoSegundos } + elapsedSeconds
    val maxDistanceMeters = allDistances.maxOrNull() ?: totalDistanceMeters
    val maxDurationSeconds = allDurations.maxOrNull() ?: elapsedSeconds

    val maxDistanceFormatted = "%.2f km".format(maxDistanceMeters / 1000.0)
    val maxDurationFormatted = run {
        val m = maxDurationSeconds / 60
        val s = maxDurationSeconds % 60
        if (m >= 60) "%02d:%02d:%02d".format(m / 60, m % 60, s) else "%02d:%02d".format(m, s)
    }

    fun startSharing() {
        isSharing = true
        scope.launch {
            try {
                val uri = withContext(Dispatchers.IO) {
                    val shareData = ShareCardData(
                        title = workoutTitle,
                        distanceMeters = totalDistanceMeters.toInt(),
                        elapsedSeconds = elapsedSeconds.toInt(),
                        routePoints = routePoints
                    )
                    ShareCardGenerator.generateShareImage(context, shareData)
                }

                val distStr = WorkoutFormatters.formatDistance(totalDistanceMeters.toInt())
                val timeStr = WorkoutFormatters.formatTime(elapsedSeconds.toInt())
                val paceStr = WorkoutFormatters.formatPace(totalDistanceMeters.toInt(), elapsedSeconds.toInt())
                val shareText = "Completei $workoutTitle no Elite: $distStr em $timeStr • Ritmo $paceStr"

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    if (uri != null) {
                        putExtra(Intent.EXTRA_STREAM, uri as android.os.Parcelable)
                        type = "image/png"
                        clipData = android.content.ClipData.newRawUri("Elite", uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    } else {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        android.widget.Toast.makeText(
                            context,
                            "Sem dados de rota GPS neste treino. Compartilhando apenas o texto.",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                }
                val shareIntent = Intent.createChooser(sendIntent, "Compartilhar no Elite")
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(shareIntent)
            } catch (exception: Exception) {
                android.util.Log.e("ShareCard", "Erro ao gerar ou compartilhar o cartão.", exception)
                val causeMessage = generateSequence<Throwable>(exception) { it.cause }
                    .mapNotNull { it.localizedMessage?.takeIf(String::isNotBlank) }
                    .lastOrNull()
                    ?: exception.javaClass.simpleName
                android.widget.Toast.makeText(
                    context,
                    "Não foi possível compartilhar: $causeMessage",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            } finally {
                isSharing = false
            }
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSharing()
        } else {
            isSharing = false
            android.widget.Toast.makeText(
                context,
                "Sem permissão para salvar o cartão na galeria. Conceda acesso ao armazenamento e tente novamente.",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

    fun requestSharing() {
        if (isSharing) return
        if (
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isSharing = true
            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            startSharing()
        }
    }

    val sharePreviewData = remember(workoutTitle, totalDistanceMeters, elapsedSeconds, routePoints) {
        ShareCardData(
            title = workoutTitle,
            distanceMeters = totalDistanceMeters.toInt(),
            elapsedSeconds = elapsedSeconds.toInt(),
            routePoints = routePoints
        )
    }

    if (showSharePreview) {
        AnimatedVisibility(
            visible = showSharePreview,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
        ) {
            ShareCardPreviewScreen(
                data = sharePreviewData,
                isSharing = isSharing,
                onShare = ::requestSharing,
                onBack = { showSharePreview = false }
            )
        }
        return
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .testTag("screen_workout_summary")
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(top = EliteSpacing.md),
            verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
        ) {
            // Barra superior - always show back arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                            IconButton(onClick = onClose) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Voltar",
                                    tint = TextColor
                                )
                }

                Text(
                    text = if (isDetailOnly) "Detalhes da Corrida" else "Treino Concluído!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )

                            // Spacer to balance the back button (or share icon in detail mode)
                            if (isDetailOnly) {
                                IconButton(
                                    onClick = { showSharePreview = true },
                                    enabled = !isSharing,
                                    modifier = Modifier.testTag("btn_share_detail_top")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartilhar corrida",
                                        tint = Olive
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(48.dp))
                            }
            }

            // Hero
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = EliteSpacing.md), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = distanceKm, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = Olive)
                Row(horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)) {
                    MetricBlock(value = timeFormatted, label = "Tempo")
                    MetricBlock(value = paceFormatted, label = "Ritmo Médio")
                }
            }


            // CARTÃO COM A ROTA DESENHADA (Início e fim marcados)
            EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_route_canvas")) {
                if (routePoints.size >= 2) {
                    Column(modifier = Modifier.padding(EliteSpacing.md)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Trajeto percorrido",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(EliteSpacing.sm), verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).background(Olive, CircleShape))
                                Text("Início", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(modifier = Modifier.width(EliteSpacing.xs))
                                Box(modifier = Modifier.size(8.dp).background(Red, CircleShape))
                                Text("Fim", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            }
                        }
                        Spacer(modifier = Modifier.height(EliteSpacing.md))
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            val orderedPoints = routePoints.sortedBy { it.timestampMillis }
                            val coords = orderedPoints.map { RoutePointCoord(it.latitude, it.longitude) }
                            val normalized = RouteNormalizer.normalize(coords)
                            val pixelPoints = RouteNormalizer.toCanvasOffsets(
                                normalizedPoints = normalized,
                                widthPx = size.width,
                                heightPx = size.height,
                                paddingPx = 20f
                            )

                            if (pixelPoints.size >= 2) {
                                val path = Path().apply {
                                    moveTo(pixelPoints.first().first, pixelPoints.first().second)
                                    for (i in 1 until pixelPoints.size) {
                                        lineTo(pixelPoints[i].first, pixelPoints[i].second)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    brush = SolidColor(Olive),
                                    style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                )

                                // Marcador de Início (Turquesa com centro branco)
                                val start = pixelPoints.first()
                                drawCircle(color = Olive, radius = 9f, center = Offset(start.first, start.second))
                                drawCircle(color = Color.White, radius = 4f, center = Offset(start.first, start.second))

                                // Marcador de Fim (Laranja com centro branco)
                                val finish = pixelPoints.last()
                                drawCircle(color = Red, radius = 9f, center = Offset(finish.first, finish.second))
                                drawCircle(color = Color.White, radius = 4f, center = Offset(finish.first, finish.second))
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(EliteSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Text(
                            text = "Corrida registrada sem dados de rota GPS. Tempo e passos foram gravados normalmente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // 7. LINHA DE MÉTRICAS: TEMPO, RITMO MÉDIO E ESFORÇO PERCEBIDO
            if (!isDetailOnly) {
                EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_summary_metrics")) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(EliteSpacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBlock(
                                        label = "Distância",
                                        value = distanceKm,
                                        modifier = Modifier.weight(1f)
                                    )
                                    MetricBlock(
                            label = "Tempo",
                            value = timeFormatted,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBlock(
                            label = "Ritmo médio",
                            value = "$paceFormatted /km",
                            valueColor = Olive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }


            // Novos cards para modo detalhe (histórico)
            if (isDetailOnly) {
                // Card "Todos os dados"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_all_data"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161911)),
                    border = BorderStroke(1.dp, Color(0xFF3D4535))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Todos os dados",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextColor
                            )
                            if (modalidadeLabel != null) {
                                Box(
                                    modifier = Modifier
                                        .background(OliveSoft, RoundedCornerShape(11.dp))
                                        .padding(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = modalidadeLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Olive
                                    )
                                }
                            }
                        }
                        if (finalizadoEm != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR")).format(Date(finalizadoEm)),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        // Grid 2x2 com métricas
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                                Text(text = "Distância", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(Modifier.height(2.dp))
                                Text(text = distanceKm, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
                            }
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                                Text(text = "Tempo", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(Modifier.height(2.dp))
                                Text(text = timeFormatted, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                                Text(text = "Ritmo médio", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(Modifier.height(2.dp))
                                Text(text = "$paceFormatted /km", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Olive)
                            }
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                                Text(text = "Passos", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(Modifier.height(2.dp))
                                val steps = diagnostics?.accepted ?: 0
                                Text(text = if (steps > 0) "$steps passos" else "—", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextColor)
                            }
                        }
                    }
                }

                // Card "Compartilhar corrida"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_share_run"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161911)),
                    border = BorderStroke(1.dp, Color(0xFF3D4535))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Miniatura
                        val shareRoute = remember(routePoints) {
                            ShareCardGenerator.filterRoutePoints(routePoints)
                                .map { RoutePointCoord(it.latitude, it.longitude) }
                                .let { RouteNormalizer.simplify(it) }
                        }
                        val normalizedRoute = remember(shareRoute) {
                            if (shareRoute.size >= 2) RouteNormalizer.normalize(shareRoute) else emptyList()
                        }
                        Box(
                            modifier = Modifier
                                .size(88.dp, 110.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0xFF1E2314), Background)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, OliveDark, RoundedCornerShape(12.dp))
                        ) {
                            // Marca ELITE simplificada
                            Text(
                                text = "ELITE",
                                fontFamily = EliteLogoFont,
                                fontSize = 6.sp,
                                letterSpacing = 1.4.sp,
                                color = TextColor,
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                            // Rota simplificada (Canvas)
                            if (normalizedRoute.size >= 2) {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(x = 8.dp, y = 22.dp)
                                ) {
                                    drawPath(
                                        path = Path().apply {
                                            val start = normalizedRoute.first()
                                            val end = normalizedRoute.last()
                                            moveTo(start.x * size.width, start.y * size.height)
                                            for (point in normalizedRoute.drop(1)) {
                                                lineTo(point.x * size.width, point.y * size.height)
                                            }
                                        },
                                        color = Olive,
                                        style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                                    )
                                }
                            }
                            // Distância na miniatura
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 8.dp, bottom = 22.dp)
                            ) {
                                val distParts = distanceKm.split(" ", limit = 2)
                                Text(
                                    text = distParts.firstOrNull() ?: "",
                                    fontFamily = EliteDisplayFont,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextColor
                                )
                                Text(
                                    text = distParts.getOrNull(1) ?: "",
                                    fontFamily = EliteDisplayFont,
                                    fontSize = 8.sp,
                                    color = TextMuted,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        // Corpo do card
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Compartilhar corrida",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextColor
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Gerar cartão visual da sua performance",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )

                            Spacer(Modifier.height(12.dp))

                            // Botão CTA
                            Button(
                                onClick = { showSharePreview = true },
                                enabled = !isSharing,
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth()
                                    .testTag("btn_share_run_cta"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Olive
                                ),
                                border = BorderStroke(1.dp, Olive),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Compartilhar no Elite",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }


            // Pós-treino: esforço em 5 níveis, dor e prévia do próximo passo
            if (!isDetailOnly) {
                PostWorkoutFeedback(
                    esforcoGravado = rpeValue,
                    onEsforcoChange = { rpeValue = it },
                    dor = dorValue,
                    onDorChange = {
                        dorValue = it
                        onPainChange(dorValue, regiaoDorValue)
                    },
                    regiaoDor = regiaoDorValue,
                    onRegiaoChange = {
                        regiaoDorValue = it
                        onPainChange(dorValue, regiaoDorValue)
                    },
                    ajustesAutomaticos = ajustesAutomaticos
                )
            }

            // 8. GRÁFICO "RITMO AO LONGO DO TREINO" DESENHADO EM CANVAS A PARTIR DE ROUTE_POINT
            EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_pace_chart")) {
                Column(modifier = Modifier.padding(EliteSpacing.md)) {
                    Text(
                        text = "Ritmo ao longo do treino",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = "Janela móvel de 30s filtrada",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (paceSeries.size >= 2) {
                        PaceCanvasChart(
                            paceSeries = paceSeries,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sem dados de GPS suficientes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            KmSplitsCard(routePoints = routePoints)

            // 9. CARTÃO "SEUS RECORDES": MAIOR DISTÂNCIA E MAIOR DURAÇÃO ENTRE AS CORRIDAS CONCLUÍDAS
            EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_personal_records")) {
                Column(modifier = Modifier.padding(EliteSpacing.md)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Red,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Seus recordes",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Brass
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MetricBlock(
                            label = "Maior distância",
                            value = maxDistanceFormatted,
                            valueColor = Olive
                        )
                        MetricBlock(
                            label = "Maior duração",
                            value = maxDurationFormatted,
                            valueColor = Brass
                        )
                    }
                }
            }

            // 11. ANOTAÇÕES PARTICULARES
            EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_anotacoes_particulares")) {
                Column(
                    modifier = Modifier.padding(EliteSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(EliteSpacing.md)
                ) {
                    Text(
                        text = "Anotações particulares",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = "Sensações, clima ou observações do seu treino (salvo no celular).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )

                    OutlinedTextField(
                        value = anotacoesInput,
                        onValueChange = { anotacoesInput = it },
                        placeholder = { Text("Ex: Bom ritmo no trecho intermediário...", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Olive,
                            unfocusedBorderColor = com.elite.sch.app.ui.theme.EliteFieldBorder,
                            focusedTextColor = TextColor,
                            unfocusedTextColor = TextColor
                        ),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("input_anotacoes_particulares")
                    )
                }
            }

            if (diagnostics != null && diagnostics.received > 0) {
                TechnicalDetailsCard(diagnostics)
            }

            Spacer(modifier = Modifier.height(EliteSpacing.lg))
        }

        // RODAPÉ FIXO: botão fora da rolagem, acima dos botões do sistema
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LineStrong)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.systemBars.union(WindowInsets.tappableElement)
                            .only(WindowInsetsSides.Bottom)
                    )
                    .padding(top = 12.dp, bottom = 40.dp)
            ) {
                if (!isDetailOnly) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = EliteSpacing.md),
                                        horizontalArrangement = Arrangement.spacedBy(EliteSpacing.md)
                                    ) {
                                                        // Share button (ghost with icon)
                                        TextButton(
                                            onClick = { showSharePreview = true },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(56.dp)
                                                .testTag("btn_share_workout"),
                                            enabled = !isSharing,
                                            contentPadding = PaddingValues(horizontal = 4.dp),
                                                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = Olive,
                                                disabledContentColor = Olive.copy(alpha = 0.5f)
                                            )
                                        ) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.Center,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Share,
                                                                    contentDescription = null,
                                                                    tint = Olive,
                                                                    modifier = Modifier.size(18.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Text(
                                                                    text = "Compartilhar no Elite",
                                                                    style = MaterialTheme.typography.labelMedium.copy(lineHeight = 16.sp),
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Olive,
                                                                    maxLines = 2,
                                                                    textAlign = TextAlign.Start
                                                                )
                                                            }
                                        }
                                        // Save button (red/destructive)
                                        ActionButton(
                                            text = "Salvar e concluir treino",
                                            onClick = { onSaveLog(rpeValue, anotacoesInput.trim().ifBlank { null }) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(56.dp)
                                                .testTag("btn_save_workout_summary"),
                                            horizontalPadding = 6.dp,
                                            verticalPadding = 2.dp,
                                            textStyle = MaterialTheme.typography.labelMedium.copy(lineHeight = 16.sp),
                                            maxLines = 2
                                        )
                                    }
                } else {
                    BrandGradientButton(
                        text = "Salvar alterações",
                        onClick = {
                            onSaveLog(rpeValue, anotacoesInput.trim().ifBlank { null })
                            onClose()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_save_workout_changes")
                    )
                }
            }
        }
      }
    }
}

@Composable
private fun KmSplitsCard(
    routePoints: List<RoutePointEntity>,
    modifier: Modifier = Modifier
) {
    val splits = remember(routePoints) {
        KmSplitsCalculator.calculateSplits(routePoints)
    }

    if (splits.isNotEmpty()) {
        EliteCard(modifier = modifier.testTag("card_km_splits")) {
            Column(modifier = Modifier.padding(EliteSpacing.lg)) {
                Text(
                    text = "Parciais por Quilômetro (Splits)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )
                Spacer(modifier = Modifier.height(EliteSpacing.md))

                val maxPace = splits.maxOfOrNull { it.paceSeconds } ?: 1.0

                splits.forEach { split ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = EliteSpacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (split.isPartial) "Km ${split.kmIndex} (parcial)" else "Km ${split.kmIndex}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextColor,
                            modifier = Modifier.width(110.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(LineStrong)
                        ) {
                            val progress = (split.paceSeconds / maxPace).toFloat().coerceIn(0.1f, 1.0f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Olive)
                            )
                        }
                        Spacer(modifier = Modifier.width(EliteSpacing.md))
                        Text(
                            text = "${split.paceFormatted} /km",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Olive
                        )
                    }
                }
            }
        }
    }
}

/**
 * Gráfico de ritmo em Canvas conectando os pontos de PaceDataPoint.
 */
@Composable
private fun PaceCanvasChart(
    paceSeries: List<PaceDataPoint>,
    modifier: Modifier = Modifier
) {
    // Eixo vertical limitado ao intervalo entre os percentis 5 e 95
    val (minPace, maxPace) = PaceSeriesCalculator.axisRange(paceSeries)
    val segments = PaceSeriesCalculator.splitIntoSegments(paceSeries)
    val minTime = paceSeries.first().timeSeconds
    val maxTime = paceSeries.last().timeSeconds

    val minPaceFormatted = PaceCalculator.formatPace(minPace)
    val maxPaceFormatted = PaceCalculator.formatPace(maxPace)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mais rápido: $minPaceFormatted /km",
                style = MaterialTheme.typography.labelSmall,
                color = Olive
            )
            Text(
                text = "Mais lento: $maxPaceFormatted /km",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(EliteSpacing.xs))

        Canvas(modifier = Modifier.fillMaxWidth().weight(1f)) {
            val w = size.width
            val h = size.height
            val padX = 16f
            val padY = 16f

            val timeDelta = max(1L, maxTime - minTime).toDouble()
            val paceDelta = max(1.0, maxPace - minPace)

            // Cada trecho contínuo é uma linha; intervalos longos não são ligados
            segments.forEach { segment ->
                val xyPoints = segment.map { pt ->
                    val normX = ((pt.timeSeconds - minTime) / timeDelta).toFloat().coerceIn(0f, 1f)
                    val normY = ((pt.paceSecondsPerKm - minPace) / paceDelta).toFloat().coerceIn(0f, 1f)
                    Offset(padX + normX * (w - 2 * padX), padY + normY * (h - 2 * padY))
                }
                if (xyPoints.size == 1) {
                    drawCircle(brush = SolidColor(Olive), radius = 4f, center = xyPoints[0])
                } else {
                    val path = Path()
                    xyPoints.forEachIndexed { index, o ->
                        if (index == 0) path.moveTo(o.x, o.y) else path.lineTo(o.x, o.y)
                    }
                    drawPath(
                        path = path,
                        brush = SolidColor(Olive),
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }
    }
}


/** Seção recolhida com os contadores do filtro de GPS (diagnóstico). */
@Composable
private fun TechnicalDetailsCard(d: com.elite.sch.app.tracker.GpsDiagnostics) {
    var expanded by remember { mutableStateOf(false) }
    EliteCard(modifier = Modifier.fillMaxWidth().testTag("card_detalhes_tecnicos")) {
        Column(modifier = Modifier.padding(EliteSpacing.lg), verticalArrangement = Arrangement.spacedBy(EliteSpacing.xs)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Detalhes técnicos", fontWeight = FontWeight.Bold, color = TextColor)
                Text(if (expanded) "Ocultar" else "Mostrar", color = TextMuted)
            }
            if (expanded) {
                val lines = listOf(
                    "Localizações recebidas" to d.received.toString(),
                    "Aceitas" to d.accepted.toString(),
                    "Rejeitadas por precisão" to d.rejectedAccuracy.toString(),
                    "Rejeitadas por velocidade" to d.rejectedSpeed.toString(),
                    "Rejeitadas por deslocamento" to d.rejectedDisplacement.toString(),
                    "Rejeitadas: anteriores ao início" to d.rejectedBeforeStart.toString(),
                    "Rejeitadas: início (estabilização)" to d.rejectedWarmup.toString(),
                    "Maior intervalo sem ponto" to "%.1f s".format(d.maxGapMillis / 1000.0)
                )
                lines.forEach { (k, v) ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(k, color = TextMuted, style = MaterialTheme.typography.bodySmall)
                        Text(v, color = TextColor, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
