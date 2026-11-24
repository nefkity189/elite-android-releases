package com.elite.sch.app

import com.elite.sch.app.tracker.GpsSession
import com.elite.sch.app.tracker.RawFix
import com.elite.sch.app.tracker.RejectReason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GpsSessionTest {

    private val start = 100_000L
    private val baseLat = -23.5500
    private val lon = -46.6333

    /** Fix [secAfterStart] segundos depois do início; [latOffsetDeg] graus ao norte da base. */
    private fun fix(secAfterStart: Double, latOffsetDeg: Double, accuracy: Float = 3f) = RawFix(
        latitude = baseLat + latOffsetDeg,
        longitude = lon,
        accuracy = accuracy,
        timeMillis = 1_700_000_000_000L + (secAfterStart * 1000).toLong(),
        elapsedRealtimeMillis = start + (secAfterStart * 1000).toLong()
    )

    @Test
    fun `lote com 3 localizacoes eh processado por inteiro`() {
        val session = GpsSession(start)
        // ~4,45 m entre pontos, a cada 2,5 s (1,8 m/s)
        val batch = listOf(fix(15.0, 0.0), fix(17.5, 0.00004), fix(20.0, 0.00008))
        val results = session.processBatch(batch)
        assertEquals(3, results.size)
        assertTrue(results.all { it.accepted })
        val d = session.diagnostics()
        assertEquals(3, d.received)
        assertEquals(3, d.accepted)
        assertTrue(session.totalDistanceMeters in 8.0..10.0)
    }

    @Test
    fun `lote entregue com atraso eh aceito por inteiro`() {
        // Todos os pontos são posteriores ao início; o atraso de entrega (30 s) não importa
        val session = GpsSession(start)
        val batch = listOf(fix(40.0, 0.0), fix(42.5, 0.00004), fix(45.0, 0.00008), fix(47.5, 0.00012))
        val results = session.processBatch(batch)
        assertTrue(results.all { it.accepted })
        assertEquals(4, session.diagnostics().accepted)
    }

    @Test
    fun `localizacao anterior ao inicio da corrida eh rejeitada`() {
        val session = GpsSession(start)
        val r = session.process(fix(-1.0, 0.0))
        assertFalse(r.accepted)
        assertEquals(RejectReason.BEFORE_START, r.reason)
        assertEquals(1, session.diagnostics().rejectedBeforeStart)
    }

    @Test
    fun `aquecimento de 10 s eh medido pelo instante do ponto`() {
        val session = GpsSession(start)
        assertEquals(RejectReason.WARMUP, session.process(fix(5.0, 0.0)).reason)
        assertTrue(session.process(fix(10.0, 0.0)).accepted)
        assertEquals(1, session.diagnostics().rejectedWarmup)
    }

    @Test
    fun `precisao de 25 m eh rejeitada`() {
        val session = GpsSession(start)
        val r = session.process(fix(15.0, 0.0, accuracy = 25f))
        assertFalse(r.accepted)
        assertEquals(RejectReason.ACCURACY, r.reason)
        assertEquals(1, session.diagnostics().rejectedAccuracy)
    }

    @Test
    fun `velocidade de 8 m por s eh rejeitada`() {
        val session = GpsSession(start)
        assertTrue(session.process(fix(15.0, 0.0)).accepted)
        // ~8 m em 1 s
        val r = session.process(fix(16.0, 0.000072))
        assertFalse(r.accepted)
        assertEquals(RejectReason.SPEED, r.reason)
        assertEquals(1, session.diagnostics().rejectedSpeed)
    }

    @Test
    fun `deslocamento minimo eh o maior entre 3 m e precisao integral`() {
        val session = GpsSession(start)
        assertTrue(session.process(fix(15.0, 0.0, accuracy = 10f)).accepted)
        // ~4 m, mas precisão 10 m => mínimo 5 m => rejeitado
        val r = session.process(fix(17.5, 0.000036, accuracy = 10f))
        assertFalse(r.accepted)
        assertEquals(RejectReason.DISPLACEMENT, r.reason)
        // distância não acumulou
        assertEquals(0.0, session.totalDistanceMeters, 0.001)
    }

    @Test
    fun `contadores batem com o total recebido`() {
        val session = GpsSession(start)
        session.processBatch(
            listOf(
                fix(-2.0, 0.0),                       // antes do início
                fix(4.0, 0.0),                        // aquecimento
                fix(15.0, 0.0, accuracy = 40f),       // precisão
                fix(16.0, 0.0),                       // aceito
                fix(17.0, 0.000072),                  // velocidade
                fix(19.0, 0.00004),                   // aceito
                fix(21.5, 0.00004)                    // deslocamento (parado)
            )
        )
        val d = session.diagnostics()
        assertEquals(7, d.received)
        assertEquals(2, d.accepted)
        assertEquals(1, d.rejectedBeforeStart)
        assertEquals(1, d.rejectedWarmup)
        assertEquals(1, d.rejectedAccuracy)
        assertEquals(1, d.rejectedSpeed)
        assertEquals(1, d.rejectedDisplacement)
        assertEquals(d.received, d.accepted + d.rejectedTotal)
    }

    @Test
    fun `maior intervalo sem ponto eh registrado`() {
        val session = GpsSession(start)
        session.process(fix(15.0, 0.0))
        session.process(fix(17.0, 0.00004))
        session.process(fix(40.0, 0.00008))
        assertEquals(23_000L, session.diagnostics().maxGapMillis)
    }

    @Test
    fun `ponto rejeitado nao devolve ponto para gravar`() {
        val session = GpsSession(start)
        assertNull(session.process(fix(1.0, 0.0)).point)
    }

    @Test
    fun `distancia durante pausa eh ignorada ao retomar`() {
        val session = GpsSession(start)
        // 1. Ponto inicial aceito (após warmup)
        session.process(fix(15.0, 0.0))
        val distBeforePause = session.totalDistanceMeters

        // 2. Pausa e deslocamento de ~111m (0.001 graus de latitude)
        // Simula onPause (não tem método, mas o callback para de enviar fixes)
        // Simula onResume 30s depois
        val resumeTime = start + 45000L
        session.onResume(resumeTime)

        // 3. Primeiro ponto pós-retomada em local diferente
        val fixAfterResume = fix(46.0, 0.001) // 0.001 graus ~ 111 metros
        val result = session.process(fixAfterResume)

        // Verificação
        assertTrue("O ponto após resume deve ser aceito", result.accepted)
        assertEquals("A distância adicionada deve ser ZERO ao retomar", 0.0, result.distanceAddedMeters, 0.001)
        assertEquals("A distância total não deve ter aumentado", distBeforePause, session.totalDistanceMeters, 0.001)
    }
    @Test fun competitionStartsWithoutDiscardingFirstTenSeconds() {
        val session = GpsSession(start, warmupMillis = 0)
        assertTrue(session.process(fix(0.0, 0.0)).accepted)
        assertTrue(session.process(fix(2.5, 0.00004)).accepted)
        assertTrue(session.totalDistanceMeters > 4.0)
    }

    @Test fun poorAccuracyCannotAccumulateStationaryJitter() {
        val session = GpsSession(start)
        session.process(fix(15.0, 0.0, accuracy = 20f))
        repeat(10) { session.process(fix(17.0 + it * 2, 0.00004, accuracy = 20f)) }
        assertEquals(0.0, session.totalDistanceMeters, 0.001)
    }

    @Test fun gapLongerThanThirtySecondsDoesNotInventRoute() {
        val session = GpsSession(start)
        session.process(fix(15.0, 0.0))
        val result = session.process(fix(50.0, 0.0004))
        assertTrue(result.accepted)
        assertEquals(0.0, result.distanceAddedMeters, 0.001)
    }
}
