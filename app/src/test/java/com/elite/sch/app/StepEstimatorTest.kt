package com.elite.sch.app

import com.elite.sch.app.generator.PaceDefaults
import com.elite.sch.app.generator.StepEstimator
import com.elite.sch.app.model.StepType
import org.junit.Assert.assertEquals
import org.junit.Test

class StepEstimatorTest {

    @Test
    fun `estimativa com ritmo padrao para corrida`() {
        // 1000m a 450s/km (7:30) = 450s
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = 1000,
            type = StepType.RUN
        )
        assertEquals(450, duration)
    }

    @Test
    fun `estimativa com ritmo padrao para caminhada`() {
        // 1000m a 720s/km (12:00) = 720s
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = 1000,
            type = StepType.WALK
        )
        assertEquals(720, duration)
    }

    @Test
    fun `estimativa com ritmo customizado`() {
        // 500m a 300s/km (5:00) = 150s
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = 500,
            type = StepType.RUN,
            ritmoCorridaSegKm = 300
        )
        assertEquals(150, duration)
    }

    @Test
    fun `distancia zero resulta em duracao zero`() {
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = 0,
            type = StepType.RUN
        )
        assertEquals(0, duration)
    }

    @Test
    fun `distancia negativa resulta em duracao zero`() {
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = -100,
            type = StepType.RUN
        )
        assertEquals(0, duration)
    }

    @Test
    fun `ritmo invalido usa padrao`() {
        val duration = StepEstimator.estimateDurationSeconds(
            distanceMeters = 1000,
            type = StepType.RUN,
            ritmoCorridaSegKm = 0
        )
        assertEquals(PaceDefaults.corridaSegKm, duration)
    }
}