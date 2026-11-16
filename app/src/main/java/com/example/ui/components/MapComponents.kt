package com.example.ui.components

import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.View
import androidx.compose.foundation.Canvas
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.MapType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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
 * Componente principal do mapa em tempo real.
 *
 * Renderiza:
 * - Polyline da rota percorrida (cor da marca, largura 6dp, caps arredondados)
 * - Marcador azul da posição atual do usuário
 * - Botão de alternância de modo de câmera (seguir usuário / ver rota completa)
 * - Botão "Recentralizar" quando no modo livre
 *
 * @param points Lista de pontos GPS da corrida atual (ordenados por timestamp)
 * @param currentLocation Localização atual do usuário (pode ser null se GPS não disponível)
 * @param cameraMode Modo de câmera atual
 * @param onCameraModeChange Callback para alterar modo de câmera
 * @param onRecenter Callback para recentralizar no usuário
 * @param mapPreferences Preferências persistidas (mostrar mapa, modo padrão)
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapPaddingPx = with(density) { 48.dp.toPx().toInt() }
    val polylineWidthPx = with(density) { 6.dp.toPx() }

    // Refs para acessar GoogleMap do lado do View
    val mapViewRef = remember { AtomicReference<MapView?>(null) }
    val googleMapRef = remember { AtomicReference<GoogleMap?>(null) }
    val userMarkerRef = remember { AtomicReference<Marker?>(null) }
    val routePolylineRef = remember { AtomicReference<Polyline?>(null) }
    val mapReady = remember { mutableStateOf(false) }
    val mapInitJob = remember { mutableStateOf<Job?>(null) }

    // Inicialização do Maps SDK (necessário antes de usar MapView)
    DisposableEffect(key1 = context) {
        MapsInitializer.initialize(context)
        onDispose {}
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
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Atualiza a polyline quando os pontos mudam
    DisposableEffect(key1 = points) {
        if (mapReady.value) {
            val map = googleMapRef.get()
            if (map != null) {
                if (points.size < 2) {
                    routePolylineRef.get()?.remove()
                    routePolylineRef.set(null)
                } else {
                    val polylineOptions = PolylineOptions()
                        .color(AndroidColor.parseColor("#00B4D8"))
                        .width(polylineWidthPx)
                        .startCap(RoundCap())
                        .endCap(RoundCap())
                        .geodesic(true)

                    points.forEach { p ->
                        polylineOptions.add(LatLng(p.latitude, p.longitude))
                    }

                    routePolylineRef.get()?.remove()
                    routePolylineRef.set(map.addPolyline(polylineOptions))
                }
            }
        }
        onDispose {}
    }

    // Atualiza marcador do usuário
    DisposableEffect(key1 = currentLocation) {
        if (mapReady.value) {
            val map = googleMapRef.get()
            if (map != null) {
                currentLocation?.let { loc ->
                    val latLng = LatLng(loc.latitude, loc.longitude)
                    val marker = userMarkerRef.get()
                    if (marker == null) {
                        val markerOptions = MarkerOptions()
                            .position(latLng)
                            .title("Sua posição")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .anchor(0.5f, 0.5f)
                            .flat(true)
                        userMarkerRef.set(map.addMarker(markerOptions))
                    } else {
                        marker.position = latLng
                    }

                    // Bearing dinâmico: heading do último ponto da rota até o atual.
                    // Sem ponto anterior, cai para bearing 0 (norte no topo).
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

                    if (cameraMode is CameraMode.FollowUser || cameraMode is CameraMode.FollowUserWithBearing) {
                        val cameraPosition = CameraPosition.Builder()
                            .target(latLng)
                            .zoom(17f)
                            .bearing(bearing)
                            .tilt(45f)
                            .build()
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null)
                    }
                } ?: run {
                    userMarkerRef.get()?.remove()
                    userMarkerRef.set(null)
                }
            }
        }
        onDispose {}
    }

    // Ajusta câmera para "ver rota completa" quando modo muda
    DisposableEffect(key1 = cameraMode) {
        if (mapReady.value) {
            val map = googleMapRef.get()
            if (map != null && cameraMode is CameraMode.ShowFullRoute && points.size >= 2) {
                val builder = LatLngBounds.Builder()
                points.forEach { p -> builder.include(LatLng(p.latitude, p.longitude)) }
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

    // MapView via AndroidView

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AisleBackground)
    ) {
        // Mapa
        // Factory runs ONCE to create the MapView and initialize the GoogleMap.
        // Update is intentionally EMPTY — all map mutations (polyline, marker, camera)
        // are driven by DisposableEffect(key1 = points/currentLocation/cameraMode)
        // which read googleMapRef.get() directly. This avoids re-requesting the map
        // on every recomposition (which caused flicker, duplicate callbacks, leaks).
        androidx.compose.ui.viewinterop.AndroidView(
            factory = { ctx ->
                MapView(ctx).also { mv ->
                    mv.getMapAsync { map ->
                        googleMapRef.set(map)
                        // Mostra ponto azul e satélite (estilo X1)
                        try {
                            map.isMyLocationEnabled = true
                            map.mapType = MapType.HYBRID.value
                        } catch (e: SecurityException) {
                            // Silencioso: permissão negada pelo usuário
                        }
                        mapReady.value = true
                    }
                    mapViewRef.set(mv)
                }
            },
            update = { mv ->
                // Intentionally empty — MapView lifecycle is handled by
                // the DisposableEffect(key1 = lifecycleOwner) above.
                // Do NOT call getMapAsync here.
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )

        // Overlay de controles no canto inferior direito
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AisleSpacing.md),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(AisleSpacing.xs)
            ) {
                // Botão modo de câmera
                Surface(
                    shape = CircleShape,
                    color = AisleCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AisleBorder),
                    modifier = Modifier
                        .semantics { contentDescription = "Alternar modo de câmera" }
                        .testTag("btn_map_camera_mode")
                ) {
                    IconButton(
                        onClick = {
                            val nextMode = when (cameraMode) {
                                is CameraMode.FollowUser -> CameraMode.ShowFullRoute
                                is CameraMode.FollowUserWithBearing -> CameraMode.ShowFullRoute
                                is CameraMode.ShowFullRoute -> CameraMode.Free
                                is CameraMode.Free -> CameraMode.FollowUserWithBearing
                            }
                            onCameraModeChange(nextMode)
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(AisleSpacing.xs)
                    ) {
                        Icon(
                            imageVector = when (cameraMode) {
                                is CameraMode.FollowUser -> Icons.Default.MyLocation
                                is CameraMode.FollowUserWithBearing -> Icons.Default.MyLocation
                                is CameraMode.ShowFullRoute -> Icons.Default.Explore
                                is CameraMode.Free -> Icons.Default.ZoomOutMap
                            },
                            contentDescription = null,
                            tint = AisleTurquoise,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Botão recentralizar (só aparece no modo Free)
                if (cameraMode is CameraMode.Free) {
                    Surface(
                        shape = CircleShape,
                        color = AisleActionOrange,
                        modifier = Modifier
                            .semantics { contentDescription = "Recentralizar no usuário" }
                            .testTag("btn_map_recenter")
                    ) {
                        IconButton(
                            onClick = onRecenter,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(AisleSpacing.xs)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Legenda do modo atual
                Card(
                    modifier = Modifier
                        .padding(bottom = AisleSpacing.xs)
                        .fillMaxWidth()
                        .testTag("card_camera_mode_legend"),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = when (cameraMode) {
                            is CameraMode.FollowUser -> "Seguindo você"
                            is CameraMode.FollowUserWithBearing -> "Seguindo com direção"
                            is CameraMode.ShowFullRoute -> "Rota completa"
                            is CameraMode.Free -> "Livre (toque para recenter)"
                        },
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

/**
 * Mapa compacto para o cartão "Corridas recentes" no TodayScreen.
 * Reutiliza a mesma lógica de normalização do RouteNormalizer.
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