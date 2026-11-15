package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.local.entities.RoutePointEntity
import com.example.tracker.NormalizedPoint
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class ShareCardData(
    val title: String,
    val dateText: String = "",
    val distanceMeters: Int,
    val elapsedSeconds: Int,
    val routePoints: List<RoutePointEntity> = emptyList()
)

data class TitleFitResult(
    val text: String,
    val fontSize: Float
)

object ShareCardColors {
    // Primitive colors matching Color.kt
    const val NAVY_900 = 0xFF0B1020      // AisleBackground
    const val NAVY_800 = 0xFF141B2E      // AisleCard
    const val NAVY_700 = 0xFF121829      // AisleCardBottom
    const val NAVY_650 = 0xFF1C253D      // AisleTrack
    const val NAVY_600 = 0xFF232B42      // AisleBorder
    const val WHITE = 0xFFFFFFFF
    const val TEAL_500 = 0xFF19C3B1      // AisleTurquoise
    const val VIOLET_500 = 0xFF7C4DFF    // AislePurple
    const val VIOLET_300 = 0xFFA68BFF    // AisleTextAccent2
    const val ORANGE_500 = 0xFFFF7A2F    // AisleActionOrange
    const val ORANGE_600 = 0xFFFF5A1F
    const val SLATE_300 = 0xFF9AA3B8     // AisleTextSecondary
    const val CYAN_400 = 0xFF00E5FF      // AisleChartColors.RingTurquoiseEnd
}

object ShareCardLayout {
    const val BITMAP_WIDTH = 1200
    const val BITMAP_HEIGHT = 1600
    const val MARGIN = 80f

    val GRADIENT_BANNER = RectF(0f, 0f, 1200f, 320f)
    val HEADER_BRAND = RectF(80f, 54f, 400f, 110f)
    val HEADER_DATE = RectF(800f, 74f, 1120f, 110f)
    val HEADER_TITLE = RectF(80f, 168f, 1120f, 240f)
    val METRICS_BOX = RectF(80f, 404f, 1120f, 560f)
    val DIVIDER_LINE = RectF(80f, 600f, 1120f, 602f)
    val MAP_CARD = RectF(80f, 650f, 1120f, 1420f)
    val FOOTER_TEXT = RectF(80f, 1484f, 1120f, 1520f)

    val MAP_INNER_RECT = RectF(160f, 730f, 1040f, 1340f)

    fun getLayoutRects(): List<RectF> = listOf(
        GRADIENT_BANNER,
        METRICS_BOX,
        DIVIDER_LINE,
        MAP_CARD,
        FOOTER_TEXT
    )

    fun fitTitleText(
        title: String,
        paint: Paint,
        maxWidth: Float = 1040f,
        startSize: Float = 72f,
        minSize: Float = 48f
    ): TitleFitResult {
        var currentSize = startSize
        val testPaint = Paint(paint)

        while (currentSize >= minSize) {
            testPaint.textSize = currentSize
            if (testPaint.measureText(title) <= maxWidth) {
                return TitleFitResult(title, currentSize)
            }
            currentSize -= 2f
        }

        testPaint.textSize = minSize
        var truncated = title
        val ellipsis = "..."
        while (truncated.isNotEmpty() && testPaint.measureText(truncated + ellipsis) > maxWidth) {
            truncated = truncated.substring(0, truncated.length - 1)
        }
        return TitleFitResult(truncated + ellipsis, minSize)
    }
}

object ShareCardGenerator {

    fun filterRoutePoints(points: List<RoutePointEntity>): List<RoutePointEntity> {
        val sorted = points.sortedBy { it.timestampMillis }
        val validAcc = sorted.filter { it.accuracy < 50f }
        if (validAcc.isEmpty()) return emptyList()

        val filtered = mutableListOf<RoutePointEntity>()
        for (pt in validAcc) {
            if (filtered.isEmpty()) {
                filtered.add(pt)
            } else {
                val last = filtered.last()
                val dtSec = (pt.timestampMillis - last.timestampMillis) / 1000.0
                val distMeters = calculateDistanceMeters(
                    last.latitude, last.longitude,
                    pt.latitude, pt.longitude
                )
                if (dtSec > 0) {
                    val speed = distMeters / dtSec
                    if (speed <= 12.0) { // Max 12 m/s (~43.2 km/h)
                        filtered.add(pt)
                    }
                } else if (distMeters == 0.0) {
                    // Ignore duplicate timestamp with zero displacement
                }
            }
        }
        return filtered
    }

    fun calculateTotalDistanceMeters(points: List<RoutePointCoord>): Double {
        var total = 0.0
        for (i in 0 until points.size - 1) {
            total += calculateDistanceMeters(
                points[i].latitude, points[i].longitude,
                points[i + 1].latitude, points[i + 1].longitude
            )
        }
        return total
    }

    fun calculateDistanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun mapNormalizedPointsToCanvas(
        normalizedPoints: List<NormalizedPoint>,
        inner: RectF = ShareCardLayout.MAP_INNER_RECT
    ): List<Pair<Float, Float>> {
        val side = minOf(inner.width(), inner.height())
        val ox = inner.centerX() - side / 2f
        val oy = inner.centerY() - side / 2f

        return normalizedPoints.map { np ->
            Pair(ox + np.x * side, oy + np.y * side)
        }
    }

    fun renderShareCard(data: ShareCardData): Bitmap {
        val width = ShareCardLayout.BITMAP_WIDTH
        val height = ShareCardLayout.BITMAP_HEIGHT
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fundo escuro do card - AisleBackground
        val bgPaint = Paint().apply { color = ShareCardColors.NAVY_900.toInt() }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 1. Faixa de gradiente y 0 a 320 - BrandGradient (Teal500 -> Violet500)
        val bannerPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                intArrayOf(
                    ShareCardColors.NAVY_900.toInt(),
                    ShareCardColors.TEAL_500.toInt(),
                    ShareCardColors.VIOLET_500.toInt()
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(ShareCardLayout.GRADIENT_BANNER, bannerPaint)

        // AISLE na linha de base 110 (56 px)
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.WHITE.toInt()
            textSize = 56f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("AISLE", 80f, 110f, brandPaint)

        // Data à direita, linha de base 110, 36 px - AisleTextSecondary
        val formattedDate = if (data.dateText.isNotBlank()) {
            data.dateText
        } else {
            SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).format(Date())
        }
        val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 36f
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(formattedDate, 1120f, 110f, datePaint)

        // Título na linha de base 240 (72 px a 48 px / ellipsize)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.WHITE.toInt()
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val fitResult = ShareCardLayout.fitTitleText(data.title, titlePaint, maxWidth = 1040f)
        titlePaint.textSize = fitResult.fontSize
        canvas.drawText(fitResult.text, 80f, 240f, titlePaint)

        // 2. Três colunas (x = 80, 426, 773, largura 347): Distância, Tempo, Ritmo
        val distanceStr = WorkoutFormatters.formatDistance(data.distanceMeters)
        val timeStr = WorkoutFormatters.formatTime(data.elapsedSeconds)
        val paceStr = WorkoutFormatters.formatPace(data.distanceMeters, data.elapsedSeconds)

        val valPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.WHITE.toInt()
            textSize = 96f
            typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        }
        val unitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 40f
            typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
        }
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 34f
            typeface = Typeface.DEFAULT
        }

        val (distVal, distUnit) = parseValueAndUnit(distanceStr, "km")
        val timeVal = timeStr
        val timeUnit = ""
        val (paceVal, paceUnit) = parseValueAndUnit(paceStr, "/km")

        val columns = listOf(
            Triple("Distância", distVal, distUnit),
            Triple("Tempo", timeVal, timeUnit),
            Triple("Ritmo", paceVal, paceUnit)
        )
        val colXs = floatArrayOf(80f, 426f, 773f)

        for (i in 0..2) {
            val (label, valText, unitText) = columns[i]
            val x = colXs[i]

            canvas.drawText(valText, x, 500f, valPaint)
            val valWidth = valPaint.measureText(valText)
            if (unitText.isNotEmpty()) {
                canvas.drawText(unitText, x + valWidth + 8f, 500f, unitPaint)
            }
            canvas.drawText(label, x, 560f, labelPaint)
        }

        // Divisor em y=600, 2 px - AisleBorder
        val dividerPaint = Paint().apply { color = ShareCardColors.NAVY_600.toInt() }
        canvas.drawRect(ShareCardLayout.DIVIDER_LINE, dividerPaint)

        // Cartão do mapa: x 80, y 650, 1040 x 770, raio 40, fundo - AisleCard, borda - AisleBorder
        val mapBgPaint = Paint().apply { color = ShareCardColors.NAVY_800.toInt() }
        canvas.drawRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, mapBgPaint)

        val mapBorderPaint = Paint().apply {
            color = ShareCardColors.NAVY_600.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, mapBorderPaint)

        // Processamento da Rota
        val filteredPoints = filterRoutePoints(data.routePoints)
        val coords = filteredPoints.map { RoutePointCoord(it.latitude, it.longitude) }
        val simplifiedCoords = RouteNormalizer.simplify(coords)
        val totalExtension = calculateTotalDistanceMeters(simplifiedCoords)

        val hasValidRoute = simplifiedCoords.size >= 2 && totalExtension >= 100.0

        if (hasValidRoute) {
            val normalizedPoints = RouteNormalizer.normalize(simplifiedCoords)
            val canvasPoints = mapNormalizedPointsToCanvas(normalizedPoints)

            if (canvasPoints.size >= 2) {
                val path = Path()
                canvasPoints.forEachIndexed { i, pt ->
                    if (i == 0) path.moveTo(pt.first, pt.second) else path.lineTo(pt.first, pt.second)
                }

                val routePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = LinearGradient(
                        160f, 730f, 1040f, 1340f,
                        intArrayOf(
                            ShareCardColors.TEAL_500.toInt(),
                            ShareCardColors.VIOLET_500.toInt()
                        ),
                        null, Shader.TileMode.CLAMP
                    )
                    strokeWidth = 16f
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                }
                canvas.drawPath(path, routePaint)

                val startPt = canvasPoints.first()
                val finishPt = canvasPoints.last()

                val startPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.TEAL_500.toInt() }
                val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.WHITE.toInt() }
                canvas.drawCircle(startPt.first, startPt.second, 14f, startPaint)
                canvas.drawCircle(startPt.first, startPt.second, 6f, whitePaint)

                val markerLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ShareCardColors.WHITE.toInt()
                    textSize = 34f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("Início", startPt.first + 18f, startPt.second + 10f, markerLabelPaint)

                val finishPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.ORANGE_500.toInt() }
                canvas.drawCircle(finishPt.first, finishPt.second, 14f, finishPaint)
                canvas.drawCircle(finishPt.first, finishPt.second, 6f, whitePaint)

                canvas.drawText("Fim", finishPt.first + 18f, finishPt.second + 10f, markerLabelPaint)
            }
        } else {
            drawNoRouteState(canvas)
        }

        // Rodapé: Aisle • Sem barreiras na linha de base 1520, 36 px, turquesa - AisleTurquoise
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.TEAL_500.toInt()
            textSize = 36f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("Aisle • Sem barreiras", 80f, 1520f, footerPaint)

        return bitmap
    }

    private fun parseValueAndUnit(formatted: String, fallbackUnit: String): Pair<String, String> {
        val trimmed = formatted.trim()
        if (trimmed == "— /km" || trimmed.startsWith("—")) return Pair("—", "/km")
        if (trimmed.endsWith(fallbackUnit)) {
            val valPart = trimmed.removeSuffix(fallbackUnit).trim()
            return Pair(valPart, fallbackUnit)
        }
        val parts = trimmed.split(" ")
        return if (parts.size >= 2) {
            Pair(parts[0], parts.subList(1, parts.size).joinToString(" "))
        } else {
            Pair(trimmed, "")
        }
    }

    private fun drawNoRouteState(canvas: Canvas) {
        val pinColor = ShareCardColors.SLATE_300.toInt()
        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = pinColor
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
        }

        val pinPath = Path()
        pinPath.addCircle(600f, 920f, 36f, Path.Direction.CW)
        pinPath.moveTo(568f, 940f)
        pinPath.lineTo(600f, 990f)
        pinPath.lineTo(632f, 940f)
        pinPath.moveTo(530f, 860f)
        pinPath.lineTo(670f, 1020f)
        canvas.drawPath(pinPath, iconPaint)

        val noRouteTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 44f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Sem rota GPS neste treino", 600f, 1070f, noRouteTitlePaint)

        val noRouteSubPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 34f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Treino curto ou sinal fraco.", 600f, 1125f, noRouteSubPaint)
        canvas.drawText("O tempo foi registrado normalmente.", 600f, 1165f, noRouteSubPaint)
    }

    fun saveShareCard(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cacheDir = File(context.cacheDir, "share")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            
            // Limpeza: apagar arquivos com mais de 24 horas
            cacheDir.listFiles()?.forEach { 
                if (System.currentTimeMillis() - it.lastModified() > 86400000) it.delete() 
            }

            val file = File(cacheDir, "share_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: Exception) {
            android.util.Log.e("ShareCard", "Erro ao gerar imagem de compartilhamento", e)
            null
        }
    }

    fun generateShareImage(context: Context, data: ShareCardData): Uri? {
        val bitmap = renderShareCard(data)
        return saveShareCard(context, bitmap)
    }

    fun generateShareImage(
        context: Context,
        workoutTitle: String,
        distanceKm: String,
        timeFormatted: String,
        paceFormatted: String,
        routePoints: List<RoutePointEntity>
    ): Uri? {
        val distanceMeters = parseDistanceKmToMeters(distanceKm)
        val elapsedSeconds = parseTimeFormattedToSeconds(timeFormatted)
        val data = ShareCardData(
            title = workoutTitle,
            dateText = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).format(Date()),
            distanceMeters = distanceMeters,
            elapsedSeconds = elapsedSeconds,
            routePoints = routePoints
        )
        return generateShareImage(context, data)
    }

    private fun parseDistanceKmToMeters(distKmStr: String): Int {
        val cleaned = distKmStr.replace("km", "").replace(",", ".").trim()
        val valDouble = cleaned.toDoubleOrNull() ?: 0.0
        return (valDouble * 1000).toInt()
    }

    private fun parseTimeFormattedToSeconds(timeStr: String): Int {
        val parts = timeStr.trim().split(":")
        return when (parts.size) {
            3 -> (parts[0].toIntOrNull() ?: 0) * 3600 + (parts[1].toIntOrNull() ?: 0) * 60 + (parts[2].toIntOrNull() ?: 0)
            2 -> (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
            else -> 0
        }
    }
}
