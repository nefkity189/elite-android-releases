package com.elite.sch.app

import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.tracker.DistanceSchedule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DistanceScheduleTest {

    private fun warmupStep(durationSec: Int = 300): WorkoutStep =
        WorkoutStep(StepType.WARMUP, durationSec, "Aquecimento", 0)

    private fun runStep(distanceM: Int, durationSec: Int = 0): WorkoutStep =
        WorkoutStep(StepType.RUN, durationSec, "Corra $distanceM metros", distanceM)

    private fun walkStep(distanceM: Int, durationSec: Int = 0): WorkoutStep =
        WorkoutStep(StepType.WALK, durationSec, "Caminhe $distanceM metros", distanceM)

    private fun cooldownStep(durationSec: Int = 300): WorkoutStep =
        WorkoutStep(StepType.COOLDOWN, durationSec, "Desaquecimento", 0)

    @Test
    fun `bloco principal - limite exato de cada passo`() {
        // 400m corrida + 200m caminhada = 600m bloco principal
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // Início do primeiro passo (corrida 400m)
        val r1 = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(1, r1.currentStepIndex) // índice global: 0=warmup, 1=run
        assertEquals(StepType.RUN, r1.currentStep?.type)
        assertEquals(400, r1.metersRemainingInStep)
        assertEquals(600, r1.metersRemainingInMainBlock)
        assertFalse(r1.isMainBlockCompleted)

        // Exatamente no limite: 400m percorridos -> transição para caminhada
        val r2 = DistanceSchedule.evaluate(steps, 400.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(2, r2.currentStepIndex) // 0=warmup, 1=run, 2=walk
        assertEquals(StepType.WALK, r2.currentStep?.type)
        assertEquals(200, r2.metersRemainingInStep)
        assertEquals(200, r2.metersRemainingInMainBlock)
        assertFalse(r2.isMainBlockCompleted)

        // Meio do passo de caminhada
        val r3 = DistanceSchedule.evaluate(steps, 500.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(2, r3.currentStepIndex)
        assertEquals(100, r3.metersRemainingInStep)
        assertEquals(100, r3.metersRemainingInMainBlock)
        assertFalse(r3.isMainBlockCompleted)

        // Limite final do bloco principal: 600m
        val r4 = DistanceSchedule.evaluate(steps, 600.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertTrue(r4.isMainBlockCompleted)
        assertEquals(0, r4.metersRemainingInMainBlock)
        // Deve ter voice cue de bloco principal completado
        assertTrue(r4.pendingVoiceCue is DistanceSchedule.DistanceVoiceCue.MainBlockCompleted)
    }

    @Test
    fun `andou mais que o planejado no passo`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // Usuário correu 450m no passo de 400m (ultrapassou)
        val r = DistanceSchedule.evaluate(steps, 450.0, DistanceSchedule.Phase.MAIN_BLOCK)
        // Deve estar no passo seguinte (caminhada)
        assertEquals(2, r.currentStepIndex)
        assertEquals(StepType.WALK, r.currentStep?.type)
        // Metros restantes no passo de caminhada = 200 - (450-400) = 150
        assertEquals(150, r.metersRemainingInStep)
        assertEquals(150, r.metersRemainingInMainBlock)
    }

    @Test
    fun `ultimo passo parcial do bloco principal`() {
        val steps = listOf(
            warmupStep(),
            runStep(300, 180),
            walkStep(200, 150),
            cooldownStep()
        )

        // 450m de 500m total (último passo parcialmente completado)
        val r = DistanceSchedule.evaluate(steps, 450.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(2, r.currentStepIndex) // walk
        assertEquals(50, r.metersRemainingInStep)
        assertEquals(50, r.metersRemainingInMainBlock)
        assertFalse(r.isMainBlockCompleted)
    }

    @Test
    fun `retomada do servico mantem indice derivado da distancia`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // Simula reinício do serviço: distância salva = 350m
        val r = DistanceSchedule.evaluate(steps, 350.0, DistanceSchedule.Phase.MAIN_BLOCK)
        // Deve estar no passo de corrida (índice 1), não reiniciar do zero
        assertEquals(1, r.currentStepIndex)
        assertEquals(StepType.RUN, r.currentStep?.type)
        assertEquals(50, r.metersRemainingInStep) // 400 - 350
        assertEquals(250, r.metersRemainingInMainBlock) // 600 - 350
    }

    @Test
    fun `gps perdido e recuperado`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        val now = System.currentTimeMillis()

        // GPS válido até 200m
        var r = DistanceSchedule.evaluate(
            steps, 200.0, DistanceSchedule.Phase.MAIN_BLOCK,
            lastGpsUpdateMillis = now, useGps = true
        )
        assertEquals(1, r.currentStepIndex)
        assertNull(r.pendingVoiceCue) // sem aviso de GPS perdido

        // GPS perdido por 35 segundos (lastGpsUpdateMillis = now - 35000)
        r = DistanceSchedule.evaluate(
            steps, 200.0, DistanceSchedule.Phase.MAIN_BLOCK,
            lastGpsUpdateMillis = now - 35_000, useGps = true
        )
        // Deve emitir aviso de fallback GPS
        assertTrue(r.pendingVoiceCue is DistanceSchedule.DistanceVoiceCue.GpsLostFallback)
        val fallback = r.pendingVoiceCue as DistanceSchedule.DistanceVoiceCue.GpsLostFallback
        assertTrue(fallback.estimatedMinutesRemaining > 0)

        // GPS restaurado (nova atualização válida)
        r = DistanceSchedule.evaluate(
            steps, 250.0, DistanceSchedule.Phase.MAIN_BLOCK,
            lastGpsUpdateMillis = now + 1000, useGps = true
        )
        // Não deve mais emitir GPS perdido
        assertFalse(r.pendingVoiceCue is DistanceSchedule.DistanceVoiceCue.GpsLostFallback)
    }

    @Test
    fun `bloco de um unico passo`() {
        val steps = listOf(
            warmupStep(),
            runStep(1000, 600), // único passo de 1km
            cooldownStep()
        )

        // Início
        var r = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(1, r.currentStepIndex)
        assertEquals(1000, r.metersRemainingInStep)
        assertEquals(1000, r.metersRemainingInMainBlock)

        // Meio
        r = DistanceSchedule.evaluate(steps, 500.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertEquals(500, r.metersRemainingInStep)

        // Fim do bloco principal
        r = DistanceSchedule.evaluate(steps, 1000.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertTrue(r.isMainBlockCompleted)
        assertEquals(0, r.metersRemainingInMainBlock)
        val cue = r.pendingVoiceCue as DistanceSchedule.DistanceVoiceCue.MainBlockCompleted
        assertEquals(1, cue.totalKm)
    }

    @Test
    fun `distancia zero no bloco principal`() {
        val steps = listOf(
            warmupStep(),
            // sem passos RUN/WALK com distância -> bloco principal vazio
            cooldownStep()
        )

        val r = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.MAIN_BLOCK)
        // Deve transicionar direto para cooldown (bloco principal "completado" com 0m)
        assertTrue(r.isMainBlockCompleted)
        assertEquals(0, r.metersRemainingInMainBlock)
    }

    @Test
    fun `sem GPS desde o inicio usa fallback de tempo`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // useGps = false, lastGpsUpdateMillis = 0
        val r = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.MAIN_BLOCK, 0L, false)
        // Deve usar fallback baseado em tempo (StepScheduler) - no tempo 0 está no aquecimento
        assertEquals(0, r.currentStepIndex)
        // Sem aviso de GPS perdido (já começou sem GPS), mas pode ter aviso normal de início de passo
        assertTrue(r.pendingVoiceCue == null || r.pendingVoiceCue is DistanceSchedule.DistanceVoiceCue.StartStep)
        assertFalse(r.pendingVoiceCue is DistanceSchedule.DistanceVoiceCue.GpsLostFallback)
    }

    @Test
    fun `aquecimento nao conta para distancia do bloco principal`() {
        val steps = listOf(
            warmupStep(300), // 5 min caminhada
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // No aquecimento, distância do bloco principal ainda é o total
        val r = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.WARMUP)
        assertEquals(0, r.currentStepIndex)
        assertEquals(StepType.WARMUP, r.currentStep?.type)
        assertEquals(600, r.metersRemainingInMainBlock) // 400 + 200
        assertFalse(r.isMainBlockCompleted)
    }

    @Test
    fun `transicao aquecimento para bloco principal`() {
        val steps = listOf(
            warmupStep(300),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // Avalia como se aquecimento tivesse completado
        val r = DistanceSchedule.evaluate(steps, 0.0, DistanceSchedule.Phase.MAIN_BLOCK)
        // Primeiro passo do bloco principal (índice 1 = após aquecimento)
        assertEquals(1, r.currentStepIndex)
        assertEquals(StepType.RUN, r.currentStep?.type)
    }

    @Test
    fun `desaquecimento apos bloco principal completado`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep(300) // 5 min
        )

        // Bloco principal completado (600m)
        val r = DistanceSchedule.evaluate(steps, 600.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertTrue(r.isMainBlockCompleted)
        assertEquals(StepType.COOLDOWN, r.currentStep?.type)
        // Voice cue deve indicar bloco principal completado com desaquecimento
        val cue = r.pendingVoiceCue as DistanceSchedule.DistanceVoiceCue.MainBlockCompleted
        assertEquals(5, cue.cooldownMinutes) // 300s = 5 min
    }

    @Test
    fun `treino finalizado apos desaquecimento`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150)
            // sem cooldown
        )

        val r = DistanceSchedule.evaluate(steps, 600.0, DistanceSchedule.Phase.MAIN_BLOCK)
        assertTrue(r.isMainBlockCompleted)
        assertEquals(DistanceSchedule.Phase.COOLDOWN, r.currentPhase)
        // Sem cooldown, treino finalizado logo após bloco principal
        val cue = r.pendingVoiceCue as DistanceSchedule.DistanceVoiceCue.MainBlockCompleted
        assertEquals(0, cue.cooldownMinutes)
    }

    @Test
    fun `determinePhase transiciona corretamente`() {
        val steps = listOf(
            warmupStep(),
            runStep(400, 240),
            walkStep(200, 150),
            cooldownStep()
        )

        // Aquecimento não completado
        var phase = DistanceSchedule.determinePhase(steps, false, 0.0, 600)
        assertEquals(DistanceSchedule.Phase.WARMUP, phase)

        // Aquecimento completado, bloco principal em andamento
        phase = DistanceSchedule.determinePhase(steps, true, 300.0, 600)
        assertEquals(DistanceSchedule.Phase.MAIN_BLOCK, phase)

        // Bloco principal completado, tem cooldown
        phase = DistanceSchedule.determinePhase(steps, true, 600.0, 600)
        assertEquals(DistanceSchedule.Phase.COOLDOWN, phase)
    }
}