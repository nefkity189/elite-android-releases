package com.example.ui.components

import android.content.Context
import android.content.ContentValues
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
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.data.local.entities.RoutePointEntity
import com.example.tracker.NormalizedPoint
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.core.content.res.ResourcesCompat
import com.example.R

data class ShareCardData(
    val title: String,
    val dateText: String = "",
    val distanceMeters: Int,
    val elapsedSeconds: Int,
    val routePoints: List<RoutePointEntity> = emptyList(),
    /** Mapa já renderizado (sem nomes de ruas) com a rota por cima. Se null, usa o desenho simples. */
    val mapSnapshot: Bitmap? = null
)

data class TitleFitResult(
    val text: String,
    val fontSize: Float
)

/** Fontes do cartao (Elite): Saira Stencil One so no logotipo, Barlow Condensed nos numeros, Inter no texto. */
class ShareCardFonts(
    val logo: Typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
    val display: Typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD),
    val displayMedium: Typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL),
    val body: Typeface = Typeface.DEFAULT,
    val bodyBold: Typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
) {
    companion object {
        fun load(context: Context): ShareCardFonts {
            val d = ShareCardFonts()
            fun f(id: Int, fallback: Typeface) = try { ResourcesCompat.getFont(context, id) ?: fallback } catch (e: Exception) { fallback }
            return ShareCardFonts(
                logo = f(R.font.saira_stencil_one_regular, d.logo),
                display = f(R.font.barlow_condensed_bold, d.display),
                displayMedium = f(R.font.barlow_condensed_medium, d.displayMedium),
                body = f(R.font.inter_regular, d.body),
                bodyBold = f(R.font.inter_semibold, d.bodyBold)
            )
        }
    }
}

object ShareCardColors {
    // Elite colors (SPEC seção 3)
    const val BACKGROUND = 0xFF0A0B09      // Elite Background
    const val SURFACE = 0xFF10120D         // Elite Surface
    const val CARD = 0xFF161911            // Elite Card
    const val CARD_ELEVATED = 0xFF1D2116   // Elite CardElevated
    const val FIELD = 0xFF0D0F0A           // Elite Field
    const val LINE = 0x1AECE8DA            // 10% osso: bordas/divisores
    const val LINE_STRONG = 0x2EECE8DA     // 18% osso: bordas campo/sheet
    const val TEXT = 0xFFECE8DA            // Osso
    const val TEXT_MUTED = 0xFF8F9384
    const val TEXT_DIM = 0xFF5E6255
    const val OLIVE = 0xFFA3B565           // Seleção, Pausar, abas ativas
    const val OLIVE_DARK = 0xFF5D6B34
    const val OLIVE_SOFT = 0x24A3B565      // 14%
    const val ON_OLIVE = 0xFF0E1306
    const val RED = 0xFFD12B2B             // Começar treino, Parar
    const val RED_SOFT = 0x26D12B2B
    const val ON_RED = 0xFFFFF4F0
    const val BRASS = 0xFFC9A24B           // Conquistas, patente
    const val AMBER = 0xFFE0A93A           // Avisos, pausa
    const val OK = 0xFF7FB069              // GPS ativo
    const val WHITE = 0xFFFFFFFF
    const val SLATE_300 = 0xFF8F9384       // TextMuted (reusing)
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

    fun renderShareCard(data: ShareCardData, fonts: ShareCardFonts = ShareCardFonts()): Bitmap {
        val width = ShareCardLayout.BITMAP_WIDTH
        val height = ShareCardLayout.BITMAP_HEIGHT

        Log.d("ShareCard", "renderShareCard INICIO: title=${data.title}, distanceMeters=${data.distanceMeters}, elapsedSeconds=${data.elapsedSeconds}, routePoints=${data.routePoints.size}")

        val bitmap = try {
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        } catch (e: OutOfMemoryError) {
            Log.e("ShareCard", "OOM ao criar bitmap ${width}x${height}: ${e.message}", e)
            throw e
        }

        if (bitmap == null || bitmap.isRecycled) {
            Log.e("ShareCard", "Bitmap nulo ou reciclado após createBitmap")
            throw IllegalStateException("Falha ao alocar bitmap")
        }

        val canvas = Canvas(bitmap)
        Log.d("ShareCard", "Bitmap criado: ${bitmap.width}x${bitmap.height}, isMutable=${bitmap.isMutable}")

        // Fundo escuro do card - Elite Background
        val bgPaint = Paint().apply { color = ShareCardColors.BACKGROUND.toInt() }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // 1. Faixa superior do cartao: Surface solido (sem degrade) e filete Olive embaixo
        val bannerPaint = Paint().apply { color = ShareCardColors.SURFACE.toInt() }
        canvas.drawRect(ShareCardLayout.GRADIENT_BANNER, bannerPaint)
        val bannerLine = Paint().apply { color = ShareCardColors.OLIVE.toInt() }
        canvas.drawRect(0f, ShareCardLayout.GRADIENT_BANNER.bottom - 4f, width.toFloat(), ShareCardLayout.GRADIENT_BANNER.bottom, bannerLine)

        // ELITE na linha de base 110 (Saira Stencil One, 26sp no spec, mas aqui em 56px para o card grande)
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.TEXT.toInt()
            textSize = 56f
            typeface = fonts.logo
            letterSpacing = 0.24f
        }
        canvas.drawText("ELITE", 80f, 110f, brandPaint)

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
            typeface = fonts.bodyBold
        }
        val fitResult = ShareCardLayout.fitTitleText(data.title, titlePaint, maxWidth = 1040f)
        titlePaint.textSize = fitResult.fontSize
        canvas.drawText(fitResult.text, 80f, 240f, titlePaint)

        // 2. Três colunas (x = 80, 426, 773, largura 347): Distância, Tempo, Ritmo
        val distanceStr = WorkoutFormatters.formatDistance(data.distanceMeters)
        val timeStr = WorkoutFormatters.formatTime(data.elapsedSeconds)
        val paceStr = WorkoutFormatters.formatPace(data.distanceMeters, data.elapsedSeconds)

        Log.d("ShareCard", "Metrics formatados: distance=$distanceStr, time=$timeStr, pace=$paceStr")

        val valPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.TEXT.toInt()
            textSize = 96f
            typeface = fonts.display
        }
        val unitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 40f
            typeface = fonts.displayMedium
        }
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.SLATE_300.toInt()
            textSize = 34f
            typeface = fonts.body
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

        // Divisor em y=600, 2 px - Elite LineStrong
        val dividerPaint = Paint().apply { color = ShareCardColors.LINE_STRONG.toInt() }
        canvas.drawRect(ShareCardLayout.DIVIDER_LINE, dividerPaint)

        // Cartão do mapa: x 80, y 650, 1040 x 770, raio 40, fundo - Elite Card, borda - Elite LineStrong
        val mapBgPaint = Paint().apply { color = ShareCardColors.CARD.toInt() }
        canvas.drawRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, mapBgPaint)

        val mapBorderPaint = Paint().apply {
            color = ShareCardColors.LINE_STRONG.toInt()
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, mapBorderPaint)

        // Mapa real (OpenFreeMap sem nomes) já com a rota desenhada: ocupa o cartão inteiro
        val snap = data.mapSnapshot
        if (snap != null && !snap.isRecycled) {
            val save = canvas.save()
            val clip = Path().apply {
                addRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, Path.Direction.CW)
            }
            canvas.clipPath(clip)
            canvas.drawBitmap(
                snap,
                null,
                ShareCardLayout.MAP_CARD,
                Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            )
            drawMapBrandStrip(canvas, data, fonts)
            canvas.restoreToCount(save)
            canvas.drawRoundRect(ShareCardLayout.MAP_CARD, 40f, 40f, mapBorderPaint)
        } else {
        // Processamento da Rota
        Log.d("ShareCard", "Processando rota: ${data.routePoints.size} pontos originais")
        val filteredPoints = filterRoutePoints(data.routePoints)
        Log.d("ShareCard", "Pontos filtrados: ${filteredPoints.size}")

        val coords = filteredPoints.map { RoutePointCoord(it.latitude, it.longitude) }
        val simplifiedCoords = RouteNormalizer.simplify(coords)
        Log.d("ShareCard", "Pontos simplificados: ${simplifiedCoords.size}")

        val totalExtension = calculateTotalDistanceMeters(simplifiedCoords)
        Log.d("ShareCard", "Extensão total da rota: $totalExtension metros")

        val hasValidRoute = simplifiedCoords.size >= 2 && totalExtension >= 100.0
        Log.d("ShareCard", "hasValidRoute=$hasValidRoute (size=${simplifiedCoords.size}, extension=$totalExtension)")

        if (hasValidRoute) {
            val normalizedPoints = RouteNormalizer.normalize(simplifiedCoords)
            Log.d("ShareCard", "Pontos normalizados: ${normalizedPoints.size}")

            val canvasPoints = mapNormalizedPointsToCanvas(normalizedPoints)
            Log.d("ShareCard", "Pontos no canvas: ${canvasPoints.size}")

            if (canvasPoints.size >= 2) {
                val path = Path()
                canvasPoints.forEachIndexed { i, pt ->
                    if (i == 0) path.moveTo(pt.first, pt.second) else path.lineTo(pt.first, pt.second)
                }

                val routePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    shader = LinearGradient(
                        160f, 730f, 1040f, 1340f,
                        intArrayOf(
                            ShareCardColors.OLIVE.toInt(),
                            ShareCardColors.OLIVE.toInt()
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

                val startPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.OLIVE.toInt() }
                val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.WHITE.toInt() }
                canvas.drawCircle(startPt.first, startPt.second, 14f, startPaint)
                canvas.drawCircle(startPt.first, startPt.second, 6f, whitePaint)

                val markerLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ShareCardColors.TEXT.toInt()
                    textSize = 34f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.drawText("Início", startPt.first + 18f, startPt.second + 10f, markerLabelPaint)

                val finishPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ShareCardColors.RED.toInt() }
                canvas.drawCircle(finishPt.first, finishPt.second, 14f, finishPaint)
                canvas.drawCircle(finishPt.first, finishPt.second, 6f, whitePaint)

                canvas.drawText("Fim", finishPt.first + 18f, finishPt.second + 10f, markerLabelPaint)

                Log.d("ShareCard", "Rota desenhada com sucesso: start=($startPt), finish=($finishPt)")
            } else {
                Log.w("ShareCard", "canvasPoints.size < 2 (${canvasPoints.size}), desenhando estado sem rota")
                drawNoRouteState(canvas)
            }
        } else {
            Log.d("ShareCard", "Rota inválida, desenhando estado sem rota")
            drawNoRouteState(canvas)
        }
        } // fim do else (sem mapSnapshot)

        // Rodapé: ELITE • Missão dada é missão cumprida, Olive, 36 px
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.OLIVE.toInt()
            textSize = 36f
            typeface = fonts.bodyBold
        }
        canvas.drawText("ELITE • Missão dada é missão cumprida", 80f, 1520f, footerPaint)

        Log.d("ShareCard", "renderShareCard FIM: bitmap válido")
        return bitmap
    }

    private fun drawMapBrandStrip(canvas: Canvas, data: ShareCardData, fonts: ShareCardFonts = ShareCardFonts()) {
        val mapCard = ShareCardLayout.MAP_CARD
        val strip = RectF(mapCard.left, mapCard.bottom - 58f, mapCard.right, mapCard.bottom)
        val stripPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                strip.left,
                strip.top,
                strip.right,
                strip.top,
                intArrayOf(
                    ShareCardColors.SURFACE.toInt(),
                    ShareCardColors.SURFACE.toInt(),
                    ShareCardColors.SURFACE.toInt()
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(strip, stripPaint)
        canvas.drawRect(
            strip,
            Paint().apply { color = Color.argb(104, 10, 11, 9) }
        )

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.TEXT.toInt()
            typeface = fonts.display
        }
        val fittedTitle = ShareCardLayout.fitTitleText(
            "ELITE • ${data.title}",
            titlePaint,
            maxWidth = 380f,
            startSize = 24f,
            minSize = 16f
        )
        titlePaint.textSize = fittedTitle.fontSize
        canvas.drawText(fittedTitle.text, strip.left + 20f, strip.top + 25f, titlePaint)

        val attributionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ShareCardColors.TEXT.toInt()
            textSize = 15f
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(
            "© OpenFreeMap • © OpenMapTiles • © OpenStreetMap",
            strip.right - 20f,
            strip.bottom - 8f,
            attributionPaint
        )
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

    fun saveShareCard(context: Context, bitmap: Bitmap): Uri {
        check(!bitmap.isRecycled) { "Bitmap já reciclado; não é possível salvar o cartão." }

        val resolver = context.contentResolver
        val fileName = "aisle_share_${UUID.randomUUID()}.png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/Aisle"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                val picturesDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val aisleDirectory = java.io.File(picturesDirectory, "Aisle")
                if (!aisleDirectory.exists() && !aisleDirectory.mkdirs()) {
                    throw IOException("Não foi possível criar a pasta pública Pictures/Aisle.")
                }
                put(
                    MediaStore.Images.Media.DATA,
                    java.io.File(aisleDirectory, fileName).absolutePath
                )
            }
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("O MediaStore não criou o registro da imagem.")

        try {
            val output = resolver.openOutputStream(uri, "w")
                ?: throw IOException("O MediaStore não abriu o arquivo para gravação.")
            output.use {
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                    throw IOException("O Android não conseguiu comprimir o cartão em PNG.")
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val publishedRows = resolver.update(
                    uri,
                    ContentValues().apply {
                        put(MediaStore.Images.Media.IS_PENDING, 0)
                    },
                    null,
                    null
                )
                if (publishedRows == 0) {
                    throw IOException("O MediaStore não publicou a imagem.")
                }
            }

            return uri
        } catch (exception: Exception) {
            try {
                if (resolver.delete(uri, null, null) == 0) {
                    throw IOException("O MediaStore não removeu a imagem incompleta.")
                }
            } catch (cleanupException: Exception) {
                exception.addSuppressed(cleanupException)
            }
            throw exception
        }
    }

    fun generateShareImage(context: Context, data: ShareCardData): Uri? {
        if (data.routePoints.isEmpty()) return null
        val bitmap = renderShareCard(data, ShareCardFonts.load(context))
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
