package com.example.ui.components

import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.R
import com.example.ui.state.CameraMode
import com.example.ui.state.MapPoint
import com.example.ui.state.MapPreferences
import com.example.ui.theme.AisleActionOrange
import com.example.ui.theme.AisleBackground
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.BrandGradient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.sources.VectorSource
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory

/**
 * Calcula o bearing (ângulo de rotação, em graus, 0 = norte, 90 = leste) entre dois
 * pontos geográficos usando a fórmula do fórum. O resultado é normalizado para [0, 360).
 * Usado para rotacionar a câmera e o marcador do usuário conforme a direção do movimento.
 */
private fun calculateBearing(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Float {
    val lat1Rad = lat1 * PI / 180.0
    val lat2Rad = lat2 * PI / 180.0
    val dLon = (lon2 - lon1) * PI / 180.0

    val y = sin(dLon) * cos(lat2Rad)
    val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(dLon)
    val bearingRad = atan2(y, x)
    val bearingDeg = (bearingRad * 180.0 / PI + 360.0) % 360.0
    return bearingDeg.toFloat()
}

/**
 * Converte lista de segmentos (cada segmento = lista de MapPoint) para GeoJSON MultiLineString.
 * Ordem GeoJSON: [longitude, latitude].
 * Pausas entre segmentos aparecem como buracos na linha (sem reta atravessando).
 */
private fun segmentsToGeoJson(segments: List<List<MapPoint>>): String {
    val linesJson = segments
        .filter { it.size >= 2 }
        .joinToString(",") { segment ->
            segment.joinToString(",", "[", "]") { point ->
                "[${point.longitude},${point.latitude}]"
            }
        }
    return """{"type":"Feature","geometry":{"type":"MultiLineString","coordinates":[$linesJson]},"properties":{}}"""
}

/**
 * Componente principal do mapa em tempo real com MapLibre Native.
 *
 * Renderiza:
 * - MultiLineString da rota percorrida (dupla camada: contorno escuro + linha ciano)
 * - Marcador da posição atual do usuário (SymbolLayer com ícone)
 * - Controles de câmera (seguir usuário / ver rota completa / livre)
 *
 * @param points Lista de pontos GPS da corrida atual (ordenados por timestamp)
 * @param currentLocation Localização atual do usuário (pode ser null se GPS não disponível)
 * @param cameraMode Modo de câmera atual
 * @param onCameraModeChange Callback para alterar modo de câmera
 * @param onRecenter Callback para recentralizar no usuário
 * @param mapPreferences Preferências persistidas (mostrar mapa, modo padrão)
 * @param isOfflineMode Se true, usa mapa offline (PMTiles local); se false, usa OpenFreeMap online
 * @param modifier Modifier do Compose
 */
@Composable
fun MapViewComposable(
    points: List<MapPoint>,
    currentLocation: MapPoint?,
    cameraMode: CameraMode,
    onCameraModeChange: (CameraMode) -> Unit,
    onRecenter: () -> Unit,
    mapPreferences: MapPreferences,
    isOfflineMode: Boolean = false,
    showStreetNames: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapPaddingPx = with(density) { 48.dp.toPx().toInt() }
    // Thinner route line (3dp main + 1.5x casing = 4.5dp total visual)
    val polylineWidthPx = with(density) { 3.dp.toPx() }

    // Refs para acessar MapLibreMap do lado do View
    val mapViewRef = remember { AtomicReference<MapView?>(null) }
    val mapLibreMapRef = remember { AtomicReference<MapLibreMap?>(null) }
    val mapReady = remember { mutableStateOf(false) }
    val styleLoaded = remember { mutableStateOf(false) }

    // Segmentos de rota (separados por pausas) - mantemos em estado para detectar mudanças
    val routeSegments = remember { mutableStateOf<List<List<MapPoint>>>(emptyList()) }
    val lastPointsHash = remember { mutableStateOf(0) }

    // Inicialização do MapLibre (necessário antes de usar MapView) - na UI thread
    val mapLibreInitialized = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = context) {
        MapLibre.getInstance(context)
        mapLibreInitialized.value = true
    }

    // Estado para modo offline
    val offlineAssetsReady = remember { mutableStateOf(false) }
    val offlineError = remember { mutableStateOf<String?>(null) }

    // Prepara assets offline (copia PMTiles, glyphs, sprites e estilo para filesDir)
    if (isOfflineMode && !offlineAssetsReady.value && offlineError.value == null) {
        LaunchedEffect(key1 = context) {
            try {
                prepareOfflineAssets(context)
                offlineAssetsReady.value = true
            } catch (e: Exception) {
                offlineError.value = e.message ?: "Erro ao preparar assets offline"
            }
        }
    }

    // Ciclo de vida do MapView vinculado ao LifecycleOwner
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            val mapView = mapViewRef.get()
            if (mapView == null) return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapViewRef.getAndSet(null)?.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // Saiu da tela sem a Activity morrer: libera o MapView (evita vazamento)
            mapViewRef.getAndSet(null)?.let { mv ->
                mv.onPause(); mv.onStop(); mv.onDestroy()
            }
            mapLibreMapRef.set(null)
        }
    }

    // Detecta mudanças nos pontos e reconstrói segmentos (quebra em pausas > 30s)
    DisposableEffect(key1 = points) {
        val hash = points.hashCode()
        if (hash != lastPointsHash.value) {
            lastPointsHash.value = hash
            // Agrupa pontos em segmentos: quebra quando gap > 30 segundos
            val segments = mutableListOf<MutableList<MapPoint>>()
            var currentSegment = mutableListOf<MapPoint>()
            var previousPoint: MapPoint? = null

            points.forEach { point ->
                if (previousPoint != null) {
                    val timeGap = point.timestampMillis - previousPoint.timestampMillis
                    if (timeGap > 30_000L) { // 30 segundos = pausa
                        if (currentSegment.size >= 2) {
                            segments.add(currentSegment)
                        }
                        currentSegment = mutableListOf()
                    }
                }
                currentSegment.add(point)
                previousPoint = point
            }
            if (currentSegment.size >= 2) {
                segments.add(currentSegment)
            }
            routeSegments.value = segments.map { it.toList() }.toList()
        }
        onDispose {}
    }

    // Atualiza a rota no mapa quando segmentos mudam
    DisposableEffect(routeSegments.value, styleLoaded.value) {
        if (mapReady.value && styleLoaded.value) {
            val map = mapLibreMapRef.get()
            val style = map?.style
            if (style != null) {
                val geoJson = segmentsToGeoJson(routeSegments.value)
                val source = style.getSourceAs<GeoJsonSource>("route-src")
                source?.setGeoJson(geoJson)
            }
        }
        onDispose {}
    }

    // Atualiza marcador do usuário + marcador de início + ponto atual (vermelho pulsante)
    DisposableEffect(currentLocation, routeSegments.value, styleLoaded.value) {
        if (mapReady.value && styleLoaded.value) {
            val map = mapLibreMapRef.get()
            val style = map?.style
            if (style != null) {
                currentLocation?.let { loc ->
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    val geoJson = """{"type":"Feature","geometry":{"type":"Point","coordinates":[${loc.longitude},${loc.latitude}]},"properties":{}}"""

                    // Atualiza localização do usuário (camada de símbolo rotativa)
                    val userSource = style.getSourceAs<GeoJsonSource>("user-location-src")
                    userSource?.setGeoJson(geoJson)

                    // Atualiza ponto atual (círculo vermelho pulsante)
                    val currentSource = style.getSourceAs<GeoJsonSource>("current-point-src")
                    currentSource?.setGeoJson(geoJson)

                    // Bearing dinâmico para FollowUserWithBearing
                    val bearing = if (cameraMode is CameraMode.FollowUserWithBearing) {
                        val previous = points.lastOrNull { it != currentLocation }
                        if (previous != null) {
                            calculateBearing(
                                previous.latitude, previous.longitude,
                                loc.latitude, loc.longitude
                            )
                        } else {
                            0f
                        }
                    } else {
                        0f
                    }

                    // Atualiza rotação do ícone via property
                    val userLayer = style.getLayerAs<SymbolLayer>("user-location-layer")
                    userLayer?.setProperties(PropertyFactory.iconRotate(bearing))

                    // Move câmera se em modo follow
                    if (cameraMode is CameraMode.FollowUser || cameraMode is CameraMode.FollowUserWithBearing) {
                        val cameraPosition = CameraPosition.Builder()
                            .target(latLng)
                            .zoom(17.0)
                            .bearing(bearing.toDouble())
                            .tilt(45.0)
                            .build()
                        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null)
                    }
                } ?: run {
                    // Limpa localização se null
                    val source = style.getSourceAs<GeoJsonSource>("user-location-src")
                    source?.setGeoJson("""{"type":"FeatureCollection","features":[]}""")
                    val currentSource = style.getSourceAs<GeoJsonSource>("current-point-src")
                    currentSource?.setGeoJson("""{"type":"FeatureCollection","features":[]}""")
                }

                // Atualiza marcador de início (primeiro ponto da rota) - ponto verde
                val firstPoint = routeSegments.value.firstOrNull()?.firstOrNull()
                firstPoint?.let { startLoc ->
                    val startGeoJson = """{"type":"Feature","geometry":{"type":"Point","coordinates":[${startLoc.longitude},${startLoc.latitude}]},"properties":{}}"""
                    val startSource = style.getSourceAs<GeoJsonSource>("start-point-src")
                    startSource?.setGeoJson(startGeoJson)
                }
            }
        }
        onDispose {}
    }

    // Ajusta câmera para "ver rota completa" quando modo muda
    DisposableEffect(cameraMode, routeSegments.value, styleLoaded.value) {
        if (mapReady.value && styleLoaded.value) {
            val map = mapLibreMapRef.get()
            if (map != null && cameraMode is CameraMode.ShowFullRoute && routeSegments.value.isNotEmpty()) {
                val builder = LatLngBounds.Builder()
                routeSegments.value.forEach { segment ->
                    segment.forEach { p ->
                        builder.include(LatLng(p.latitude, p.longitude))
                    }
                }
                currentLocation?.let { loc ->
                    builder.include(LatLng(loc.latitude, loc.longitude))
                }
                val bounds = builder.build()
                val padding = mapPaddingPx
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding), 500, null)
            }
        }
        onDispose {}
    }

    // MapView via AndroidView - aguarda inicialização do MapLibre
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AisleBackground)
    ) {
        // Mostra placeholder enquanto MapLibre inicializa
        if (!mapLibreInitialized.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AisleBackground),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = AisleTurquoise
                )
            }
        } else {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    MapView(ctx).also { mv ->
                        // O MapView nasce DEPOIS de ON_START/ON_RESUME já terem passado, então
                        // chamamos o ciclo de vida na mão (sem isso o mapa não carrega os tiles).
                        mapViewRef.set(mv)
                        mv.onCreate(null)
                        val st = lifecycleOwner.lifecycle.currentState
                        if (st.isAtLeast(Lifecycle.State.STARTED)) mv.onStart()
                        if (st.isAtLeast(Lifecycle.State.RESUMED)) mv.onResume()

                        mv.getMapAsync { map ->
                            mapLibreMapRef.set(map)
                            mapReady.value = true

                            // Carrega estilo baseado no modo (online ou offline)
                            if (isOfflineMode) {
                                // Modo offline: carrega estilo local com PMTiles
                                loadOfflineStyle(map, context, styleLoaded, polylineWidthPx, showStreetNames)
                            } else {
                                // Modo online: usa OpenFreeMap Liberty
                                map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
                                    if (!showStreetNames) hideAllLabels(style)
                                    setupMapLayers(style, polylineWidthPx)
                                    styleLoaded.value = true
                                }
                            }
                        }
                    }
                },
                update = { mv ->
                    // Intencionalmente vazio - ciclo de vida gerenciado pelo DisposableEffect acima
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

/**
 * Mapa compacto para o cartão "Corridas recentes" no TodayScreen.
 * Reutiliza a mesma lógica de normalização do RouteNormalizer (Canvas puro, sem MapLibre).
 */
@Composable
fun MiniRouteMap(
    points: List<MapPoint>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    androidx.compose.foundation.Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
            .clip(RoundedCornerShape(12.dp))
    ) {
        if (points.size < 2) return@Canvas

        val coords = points.map { com.example.tracker.RoutePointCoord(it.latitude, it.longitude) }
        val normalized = com.example.tracker.RouteNormalizer.normalize(coords)
        val pixelPoints = com.example.tracker.RouteNormalizer.toCanvasOffsets(
            normalizedPoints = normalized,
            widthPx = size.width,
            heightPx = size.height,
            paddingPx = 4f
        )

        if (pixelPoints.size >= 2) {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(pixelPoints.first().first, pixelPoints.first().second)
                for (i in 1 until pixelPoints.size) {
                    lineTo(pixelPoints[i].first, pixelPoints[i].second)
                }
            }
            drawPath(
                path = path,
                brush = BrandGradient,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 3.5f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                    join = androidx.compose.ui.graphics.StrokeJoin.Round
                )
            )
            drawCircle(
                color = AisleTurquoise,
                radius = 4f,
                center = androidx.compose.ui.geometry.Offset(pixelPoints.first().first, pixelPoints.first().second)
            )
            drawCircle(
                color = AisleActionOrange,
                radius = 4f,
                center = androidx.compose.ui.geometry.Offset(pixelPoints.last().first, pixelPoints.last().second)
            )
        }
    }
}

/**
 * Tela full-screen opcional para ver a rota completa com mais detalhes.
 */
@Composable
fun MapScreen(
    points: List<MapPoint>,
    currentLocation: MapPoint?,
    cameraMode: CameraMode,
    onCameraModeChange: (CameraMode) -> Unit,
    onRecenter: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AisleBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AisleSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mapa da Corrida",
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = AisleTextPrimary
                )
                androidx.compose.material3.IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Fechar mapa" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = AisleTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            MapViewComposable(
                points = points,
                currentLocation = currentLocation,
                cameraMode = cameraMode,
                onCameraModeChange = onCameraModeChange,
                onRecenter = onRecenter,
                mapPreferences = MapPreferences(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AisleSpacing.md, vertical = AisleSpacing.md)
            )
        }
    }
}

/**
 * Esconde TODO texto/ícone do mapa base (nomes de ruas, bairros, POIs, números).
 * Chamar ANTES de setupMapLayers (que adiciona o nosso SymbolLayer do usuário).
 */
private fun hideAllLabels(style: Style) {
    style.layers.filterIsInstance<SymbolLayer>().forEach { layer ->
        layer.setProperties(PropertyFactory.visibility("none"))
    }
}

/**
 * Configura as camadas do mapa (rota + localização do usuário + marcadores) - comum para online/offline
 */
private fun setupMapLayers(
    style: Style,
    polylineWidthPx: Float
) {
    // Cria fonte para a rota (GeoJSON MultiLineString)
    val routeSource = GeoJsonSource("route-src", """{"type":"FeatureCollection","features":[]}""")
    style.addSource(routeSource)

    // Contorno escuro por baixo (casing) - técnica da lição Maps v2
    val casingLayer = LineLayer("route-casing", "route-src").withProperties(
        PropertyFactory.lineColor(android.graphics.Color.parseColor("#0B3D47")),
        PropertyFactory.lineWidth(polylineWidthPx * 1.7f),
        PropertyFactory.lineCap("round"),
        PropertyFactory.lineJoin("round")
    )
    style.addLayer(casingLayer)

    // Linha principal, fina e clara, por cima
    val lineLayer = LineLayer("route-line", "route-src").withProperties(
        PropertyFactory.lineColor(android.graphics.Color.parseColor("#00B4D8")),
        PropertyFactory.lineWidth(polylineWidthPx.toFloat()),
        PropertyFactory.lineCap("round"),
        PropertyFactory.lineJoin("round")
    )
    style.addLayer(lineLayer)

    // Fonte para localização do usuário (Point)
    val userSource = GeoJsonSource("user-location-src", """{"type":"FeatureCollection","features":[]}""")
    style.addSource(userSource)

    // Camada de símbolo para o usuário (ícone rotativo)
    val userLayer = SymbolLayer("user-location-layer", "user-location-src").withProperties(
        PropertyFactory.iconImage("user-location-icon"),
        PropertyFactory.iconSize(1.2f),
        PropertyFactory.iconAllowOverlap(true),
        PropertyFactory.iconIgnorePlacement(true),
        PropertyFactory.iconRotate(0f),
        PropertyFactory.iconAnchor("center")
    )
    style.addLayer(userLayer)

    // === NOVOS MARCADORES: Início (verde) e localização atual (vermelho pulsante) ===

    // Marcador de início (ponto verde) - usa CircleLayer para ponto simples
    val startSource = GeoJsonSource("start-point-src", """{"type":"FeatureCollection","features":[]}""")
    style.addSource(startSource)

    val startLayer = CircleLayer("start-point-layer", "start-point-src").withProperties(
        PropertyFactory.circleColor(android.graphics.Color.parseColor("#00C853")), // Verde vibrante
        PropertyFactory.circleRadius(8f),
        PropertyFactory.circleStrokeWidth(2f),
        PropertyFactory.circleStrokeColor(android.graphics.Color.WHITE),
        PropertyFactory.circleOpacity(1f)
    )
    style.addLayer(startLayer)

    // Marcador de localização atual (ponto vermelho pulsante) - usa CircleLayer com animação
    val currentSource = GeoJsonSource("current-point-src", """{"type":"FeatureCollection","features":[]}""")
    style.addSource(currentSource)

    val currentLayer = CircleLayer("current-point-layer", "current-point-src").withProperties(
        PropertyFactory.circleColor(android.graphics.Color.parseColor("#FF1744")), // Vermelho vibrante
        PropertyFactory.circleRadius(10f),
        PropertyFactory.circleStrokeWidth(3f),
        PropertyFactory.circleStrokeColor(android.graphics.Color.WHITE),
        PropertyFactory.circleOpacity(1f)
    )
    style.addLayer(currentLayer)
}

/**
 * Carrega estilo offline a partir de assets com PMTiles local
 */
private fun loadOfflineStyle(
    map: MapLibreMap,
    context: Context,
    styleLoaded: androidx.compose.runtime.MutableState<Boolean>,
    polylineWidthPx: Float,
    showStreetNames: Boolean = true
) {
    val filesDir = context.filesDir.absolutePath

    try {
        // Lê o estilo JSON dos assets
        val json = context.assets.open("style_offline.json").bufferedReader().readText()
            .replace("__FILES_DIR__", filesDir)

        // Carrega o estilo
        map.setStyle(Style.Builder().fromJson(json)) { style ->
            if (!showStreetNames) hideAllLabels(style)
            setupMapLayers(style, polylineWidthPx)
            styleLoaded.value = true
        }
    } catch (e: Exception) {
        // Fallback para estilo online se offline falhar
        map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
            if (!showStreetNames) hideAllLabels(style)
            setupMapLayers(style, polylineWidthPx)
            styleLoaded.value = true
        }
    }
}

/**
 * Prepara assets offline copiando PMTiles, glyphs e sprites de assets para filesDir
 */
private suspend fun prepareOfflineAssets(context: Context) {
    val filesDir = context.filesDir
    val coroutineScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

    coroutineScope.launch {
        // Copia PMTiles
        copyAssetToFile(context, "brazil.pmtiles", File(filesDir, "brazil.pmtiles"))

        // Copia glyphs (estrutura de pastas)
        copyAssetDir(context, "glyphs", File(filesDir, "glyphs"))

        // Copia sprites
        copyAssetDir(context, "sprites", File(filesDir, "sprites"))
    }
}

/**
 * Copia um arquivo de assets para o sistema de arquivos
 */
private suspend fun copyAssetToFile(context: Context, assetName: String, destFile: File) {
    if (destFile.exists() && destFile.length() > 0) return

    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        context.assets.open(assetName).use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        }
    }
}

/**
 * Copia um diretório de assets recursivamente
 */
private suspend fun copyAssetDir(context: Context, assetDir: String, destDir: File) {
    if (destDir.exists() && destDir.listFiles()?.isNotEmpty() == true) return

    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val assets = context.assets.list(assetDir) ?: return@withContext
        destDir.mkdirs()

        for (asset in assets) {
            val srcPath = "$assetDir/$asset"
            val destFile = File(destDir, asset)

            try {
                val subAssets = context.assets.list(srcPath)
                if (subAssets?.isNotEmpty() == true) {
                    // É um diretório
                    copyAssetDir(context, srcPath, destFile)
                } else {
                    // É um arquivo
                    copyAssetToFile(context, srcPath, destFile)
                }
            } catch (e: Exception) {
                // Ignora arquivos que não podem ser listados
            }
        }
    }
}