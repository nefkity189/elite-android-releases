package com.elite.sch.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.tracker.RouteNormalizer
import com.elite.sch.app.tracker.RoutePointCoord
import com.elite.sch.app.ui.components.ShareCardData
import com.elite.sch.app.ui.components.ShareCardGenerator
import com.elite.sch.app.ui.components.ShareCardLayout
import com.elite.sch.app.ui.components.WorkoutFormatters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ShareCardGeneratorTest {

    @Test
    fun `(a) layout rects do not overlap and fit within 1200x1500`() {
        val rects = ShareCardLayout.getLayoutRects()
        val bounds = RectF(0f, 0f, 1200f, 1500f)

        for (rect in rects) {
            assertTrue("Rect $rect must be inside 1200x1500", bounds.contains(rect))
        }

        for (i in rects.indices) {
            for (j in i + 1 until rects.size) {
                val r1 = rects[i]
                val r2 = rects[j]
                assertFalse("Rects $r1 and $r2 must not intersect", RectF.intersects(r1, r2))
            }
        }
    }

    @Test
    fun `(b) long title fits within 1040 px`() {
        val longTitle = "Corrida de Treinamento Muito Longa no Parque Ibirapuera em um Domingo Ensolarado com Vários Amigos"
        val paint = Paint()
        val result = ShareCardLayout.fitTitleText(longTitle, paint, maxWidth = 1040f)

        paint.textSize = result.fontSize
        val width = paint.measureText(result.text)

        assertTrue("Measured width ($width) must be <= 1040 px", width <= 1040f)
        assertTrue("Font size (${result.fontSize}) must be >= 48 px", result.fontSize >= 48f)
    }

    @Test
    fun `(c) 2 to 1 route ratio remains 2 to 1 after uniform mapping`() {
        // Lat range: 0.02 (height), Lon range: 0.01 (width) -> ratio 2:1
        val points = listOf(
            RoutePointCoord(-23.500, -46.630),
            RoutePointCoord(-23.520, -46.630),
            RoutePointCoord(-23.520, -46.620),
            RoutePointCoord(-23.500, -46.620)
        )

        val normalized = RouteNormalizer.normalize(points)
        val canvasPoints = ShareCardGenerator.mapNormalizedPointsToCanvas(normalized)

        val minX = canvasPoints.minOf { it.first }
        val maxX = canvasPoints.maxOf { it.first }
        val minY = canvasPoints.minOf { it.second }
        val maxY = canvasPoints.maxOf { it.second }

        val mappedWidth = maxX - minX
        val mappedHeight = maxY - minY
        val mappedRatio = mappedHeight / mappedWidth

        assertTrue("Mapped ratio ($mappedRatio) should preserve route proportions", mappedRatio in 1.8f..2.5f)
    }

    @Test
    fun `(d) 300 m GPS jump in 1 s is removed`() {
        val basePoint = RoutePointEntity(
            id = 1,
            workoutId = 1,
            latitude = -23.5500,
            longitude = -46.6333,
            accuracy = 5f,
            timestampMillis = 1000L
        )

        // Point ~300 meters away in 1 second (speed = 300 m/s > 12 m/s)
        val jumpPoint = RoutePointEntity(
            id = 2,
            workoutId = 1,
            latitude = -23.5527,
            longitude = -46.6333,
            accuracy = 5f,
            timestampMillis = 2000L
        )

        // Point 10 meters away from base point at 3 seconds
        val normalPoint = RoutePointEntity(
            id = 3,
            workoutId = 1,
            latitude = -23.5501,
            longitude = -46.6333,
            accuracy = 5f,
            timestampMillis = 3000L
        )

        val input = listOf(basePoint, jumpPoint, normalPoint)
        val filtered = ShareCardGenerator.filterRoutePoints(input)

        assertEquals(2, filtered.size)
        assertEquals(1L, filtered[0].id)
        assertEquals(3L, filtered[1].id)
    }

    @Test
    fun `(e) 0 m and 5 min returns dash pace`() {
        val distanceMeters = 0
        val elapsedSeconds = 300

        val pace = WorkoutFormatters.formatPace(distanceMeters, elapsedSeconds)
        assertEquals("— /km", pace)

        val data = ShareCardData(
            title = "Treino Curto",
            distanceMeters = distanceMeters,
            elapsedSeconds = elapsedSeconds
        )

        val paceFromData = WorkoutFormatters.formatPace(data.distanceMeters, data.elapsedSeconds)
        assertEquals("— /km", paceFromData)
    }

    @Test
    fun `(f) renderShareCard produces valid bitmap for valid route`() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val routePoints = createSampleRoutePoints()

        val data = ShareCardData(
            title = "Treino de Teste",
            distanceMeters = 5000,
            elapsedSeconds = 1800,
            routePoints = routePoints
        )

        val bitmap = ShareCardGenerator.renderShareCard(data)

        assertNotNull("Bitmap não deve ser nulo", bitmap)
        assertEquals("Largura do bitmap", 1200, bitmap.width)
        assertEquals("Altura do bitmap", 1500, bitmap.height)
        assertFalse("Bitmap não deve estar reciclado", bitmap.isRecycled)
        assertTrue("Bitmap deve ser mutável", bitmap.isMutable)
    }

    @Test
    fun `(g) renderShareCard produces valid bitmap for empty route (no route state)`() {
        val data = ShareCardData(
            title = "Treino Sem Rota",
            distanceMeters = 3000,
            elapsedSeconds = 1200,
            routePoints = emptyList()
        )

        val bitmap = ShareCardGenerator.renderShareCard(data)

        assertNotNull("Bitmap não deve ser nulo mesmo sem rota", bitmap)
        assertEquals(1200, bitmap.width)
        assertEquals(1500, bitmap.height)
    }

    @Test
    fun `(h) generateShareImage returns non-null URI for valid route`() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val routePoints = createSampleRoutePoints()

        val data = ShareCardData(
            title = "Treino Completo",
            distanceMeters = 5000,
            elapsedSeconds = 1800,
            routePoints = routePoints
        )

        // Primeiro verifica se o bitmap é renderizado corretamente (funciona em Robolectric)
        val bitmap = ShareCardGenerator.renderShareCard(data)
        assertNotNull("Bitmap deve ser renderizado", bitmap)
        assertEquals(1200, bitmap.width)
        assertEquals(1500, bitmap.height)

        val uri = ShareCardGenerator.generateShareImage(context, data)

        assertNotNull("URI deve ser produzida pelo MediaStore", uri)
        val imageUri = checkNotNull(uri)
        assertEquals("Esquema deve ser content", "content", imageUri.scheme)
        assertEquals(
            "Authority deve ser do MediaStore",
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.authority,
            imageUri.authority
        )
        try {
            val cursor = context.contentResolver.query(
                imageUri,
                arrayOf(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    MediaStore.Images.Media.IS_PENDING
                ),
                null,
                null,
                null
            )
            assertNotNull("A imagem deve estar publicada no MediaStore", cursor)
            cursor?.use {
                assertTrue("O cartão deve estar visível na coleção de imagens", it.moveToFirst())
                assertEquals(
                    "Pictures/Elite",
                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH))
                )
                assertEquals(
                    0,
                    it.getInt(it.getColumnIndexOrThrow(MediaStore.Images.Media.IS_PENDING))
                )
            }

            val input = context.contentResolver.openInputStream(imageUri)
            assertNotNull("O PNG deve estar acessível pelo URI", input)
            input?.use {
                assertTrue("O PNG salvo não deve estar vazio", it.read() != -1)
            }
        } finally {
            context.contentResolver.delete(imageUri, null, null)
        }
    }

    @Test
    fun `(i) generateShareImage returns null for empty route (expected - no route state)`() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val data = ShareCardData(
            title = "Treino Sem Rota",
            distanceMeters = 3000,
            elapsedSeconds = 1200,
            routePoints = emptyList()
        )

        val uri = ShareCardGenerator.generateShareImage(context, data)

        // Sem rota GPS, o compartilhamento continua sendo apenas texto.
        assertNull("URI deve ser null para rota vazia (sem arquivo salvo)", uri)
    }

    @Test
    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    fun `(j) map snapshot is not embedded in the branded route card`() {
        val mapColor = Color.MAGENTA
        val mapSnapshot = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888).apply {
            eraseColor(mapColor)
        }
        val data = ShareCardData(
            title = "Corrida Livre",
            distanceMeters = 5000,
            elapsedSeconds = 1800,
            routePoints = createSampleRoutePoints(),
            mapSnapshot = mapSnapshot
        )

        val card = ShareCardGenerator.renderShareCard(data)

        assertFalse(
            "The card uses its own branded route treatment instead of the map snapshot",
            card.getPixel(600, 500) == mapColor
        )
    }

    private fun createSampleRoutePoints(): List<RoutePointEntity> {
        val baseTime = System.currentTimeMillis()
        return listOf(
            RoutePointEntity(
                id = 1, workoutId = 1,
                latitude = -23.5500, longitude = -46.6333,
                accuracy = 5f, timestampMillis = baseTime
            ),
            RoutePointEntity(
                id = 2, workoutId = 1,
                latitude = -23.5510, longitude = -46.6330,
                accuracy = 5f, timestampMillis = baseTime + 30_000
            ),
            RoutePointEntity(
                id = 3, workoutId = 1,
                latitude = -23.5520, longitude = -46.6325,
                accuracy = 5f, timestampMillis = baseTime + 60_000
            ),
            RoutePointEntity(
                id = 4, workoutId = 1,
                latitude = -23.5530, longitude = -46.6320,
                accuracy = 5f, timestampMillis = baseTime + 90_000
            ),
            RoutePointEntity(
                id = 5, workoutId = 1,
                latitude = -23.5540, longitude = -46.6315,
                accuracy = 5f, timestampMillis = baseTime + 120_000
            )
        )
    }
}
