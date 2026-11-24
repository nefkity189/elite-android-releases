package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import com.example.data.local.entities.RoutePointEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONArray
import org.json.JSONObject
import org.maplibre.android.MapLibre
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.Style
import org.maplibre.android.snapshotter.MapSnapshotter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume

/**
 * Gera uma imagem do mapa (OpenFreeMap, grátis) com a rota desenhada e SEM nenhum nome
 * (ruas, bairros, POIs, números de casa). Funciona sem precisar de um MapView na tela.
 *
 * Como remove os nomes: baixa o JSON do estilo e apaga todas as camadas do tipo "symbol"
 * (são elas que desenham texto e ícones). Sobram só ruas, água, parques e prédios.
 *
 * Chame [render] de uma corrotina (ex.: rememberCoroutineScope). O MapSnapshotter exige a
 * thread principal; por isso o download roda em IO e o snapshot em Main.
 */
object ShareMapSnapshotter {

    private const val TAG = "ShareMap"

    // Troque por .../styles/positron para um mapa mais limpo e claro, ou /bright.
    private const val STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"

    // O MapSnapshotter costuma falhar acima de ~1024 px de lado.
    const val SNAPSHOT_W = 960
    const val SNAPSHOT_H = 711 // mesma proporção do MAP_CARD do cartão (1040 x 770)

    private const val PAUSE_GAP_MS = 30_000L

    @Volatile
    private var cachedStyleJson: String? = null

    /** Retorna o bitmap do mapa com a rota, ou null se algo falhar (aí o cartão usa o desenho simples). */
    suspend fun render(context: Context, rawPoints: List<RoutePointEntity>): Bitmap? {
        val points = ShareCardGenerator.filterRoutePoints(rawPoints)
        if (points.size < 2) return null

        val styleJson = loadStyleWithoutLabels() ?: return null

        return withTimeoutOrNull(20_000L) {
            withContext(Dispatchers.Main) {
                snapshot(context.applicationContext, styleJson, points)
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // 1) Estilo sem nomes
    // ---------------------------------------------------------------------------------------

    private suspend fun loadStyleWithoutLabels(): String? {
        cachedStyleJson?.let { return it }
        return withContext(Dispatchers.IO) {
            try {
                val conn = (URL(STYLE_URL).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 10_000
                    readTimeout = 10_000
                    setRequestProperty("Accept", "application/json")
                }
                val text = try {
                    if (conn.responseCode != 200) {
                        Log.e(TAG, "Estilo HTTP ${conn.responseCode}")
                        return@withContext null
                    }
                    conn.inputStream.bufferedReader().use { it.readText() }
                } finally {
                    conn.disconnect()
                }

                val root = JSONObject(text)
                val layers = root.getJSONArray("layers")
                val kept = JSONArray()
                var removed = 0
                for (i in 0 until layers.length()) {
                    val layer = layers.getJSONObject(i)
                    if (layer.optString("type") == "symbol") removed++ else kept.put(layer)
                }
                root.put("layers", kept)
                Log.d(TAG, "Camadas de texto/ícone removidas: $removed")

                root.toString().also { cachedStyleJson = it }
            } catch (e: Exception) {
                Log.e(TAG, "Falha ao baixar/processar o estilo: ${e.message}", e)
                null
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // 2) Snapshot + rota desenhada por cima
    // ---------------------------------------------------------------------------------------

    private suspend fun snapshot(
        appContext: Context,
        styleJson: String,
        points: List<RoutePointEntity>
    ): Bitmap? = suspendCancellableCoroutine { cont ->
        try {
            MapLibre.getInstance(appContext)

            val bounds = paddedBounds(points)
            val options = MapSnapshotter.Options(SNAPSHOT_W, SNAPSHOT_H)
                .withStyleBuilder(Style.Builder().fromJson(styleJson))
                .withRegion(bounds)
                .withPixelRatio(1f)

            val snapshotter = MapSnapshotter(appContext, options)
            cont.invokeOnCancellation { runCatching { snapshotter.cancel() } }

            snapshotter.start(
                { snap ->
                    try {
                        val base = snap.bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        val canvas = Canvas(base)
                        drawRoute(canvas, points) { lat, lon -> snap.pixelForLatLng(LatLng(lat, lon)) }
                        if (cont.isActive) cont.resume(base)
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao desenhar rota no snapshot: ${e.message}", e)
                        if (cont.isActive) cont.resume(null)
                    }
                },
                { error ->
                    Log.e(TAG, "MapSnapshotter erro: $error")
                    if (cont.isActive) cont.resume(null)
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao iniciar MapSnapshotter: ${e.message}", e)
            if (cont.isActive) cont.resume(null)
        }
    }

    /** Caixa da rota com ~25% de folga e tamanho mínimo (rotas minúsculas não viram zoom absurdo). */
    private fun paddedBounds(points: List<RoutePointEntity>): LatLngBounds {
        var minLat = points.minOf { it.latitude }
        var maxLat = points.maxOf { it.latitude }
        var minLon = points.minOf { it.longitude }
        var maxLon = points.maxOf { it.longitude }

        val minSpan = 0.003 // ~330 m
        if (maxLat - minLat < minSpan) {
            val c = (maxLat + minLat) / 2; minLat = c - minSpan / 2; maxLat = c + minSpan / 2
        }
        if (maxLon - minLon < minSpan) {
            val c = (maxLon + minLon) / 2; minLon = c - minSpan / 2; maxLon = c + minSpan / 2
        }
        val padLat = (maxLat - minLat) * 0.25
        val padLon = (maxLon - minLon) * 0.25
        return LatLngBounds.Builder()
            .include(LatLng(minLat - padLat, minLon - padLon))
            .include(LatLng(maxLat + padLat, maxLon + padLon))
            .build()
    }

    private fun drawRoute(
        canvas: Canvas,
        points: List<RoutePointEntity>,
        project: (lat: Double, lon: Double) -> android.graphics.PointF
    ) {
        // Pausas viram buracos na linha, igual ao MapView ao vivo.
        val path = Path()
        var prev: RoutePointEntity? = null
        for (p in points) {
            val px = project(p.latitude, p.longitude)
            if (prev == null || p.timestampMillis - prev.timestampMillis > PAUSE_GAP_MS) {
                path.moveTo(px.x, px.y)
            } else {
                path.lineTo(px.x, px.y)
            }
            prev = p
        }

        val casing = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = android.graphics.Color.WHITE
            strokeWidth = 18f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        val line = Paint(casing).apply {
            color = android.graphics.Color.parseColor("#00B4D8")
            strokeWidth = 11f
        }
        canvas.drawPath(path, casing)
        canvas.drawPath(path, line)

        val start = project(points.first().latitude, points.first().longitude)
        val end = project(points.last().latitude, points.last().longitude)
        val white = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.WHITE }

        // Início: verde
        canvas.drawCircle(start.x, start.y, 17f, white)
        canvas.drawCircle(
            start.x, start.y, 12f,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#00C853") }
        )

        // Onde você parou: vermelho com halo
        canvas.drawCircle(
            end.x, end.y, 30f,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#55FF1744") }
        )
        canvas.drawCircle(end.x, end.y, 18f, white)
        canvas.drawCircle(
            end.x, end.y, 13f,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#FF1744") }
        )
    }
}
