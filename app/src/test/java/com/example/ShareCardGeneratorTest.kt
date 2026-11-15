package com.example

import android.graphics.Paint
import android.graphics.RectF
import com.example.data.local.entities.RoutePointEntity
import com.example.tracker.RouteNormalizer
import com.example.tracker.RoutePointCoord
import com.example.ui.components.ShareCardData
import com.example.ui.components.ShareCardGenerator
import com.example.ui.components.ShareCardLayout
import com.example.ui.components.WorkoutFormatters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ShareCardGeneratorTest {

    @Test
    fun `(a) layout rects do not overlap and fit within 1200x1600`() {
        val rects = ShareCardLayout.getLayoutRects()
        val bounds = RectF(0f, 0f, 1200f, 1600f)

        for (rect in rects) {
            assertTrue("Rect $rect must be inside 1200x1600", bounds.contains(rect))
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
}
