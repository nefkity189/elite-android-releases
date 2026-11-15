package com.example

import com.example.data.local.WorkoutStepConverter
import com.example.data.local.entities.WorkoutEntity
import com.example.generator.AdaptiveEngine
import com.example.generator.AdaptiveResult
import com.example.generator.CheckinOutcome
import com.example.generator.ProgressionTable
import com.example.generator.TrainingFeedback
import com.example.model.StepType
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AdaptiveEngineTest {

    private val dia = LocalDate.of(2026, 11, 2) // data do último treino feito

    private fun treino(
        id: Long,
        data: LocalDate,
        etapa: Int,
        status: String = WorkoutEntity.STATUS_PLANEJADO
    ): WorkoutEntity {
        val steps = ProgressionTable.buildStepsForStage(etapa)
        val total = steps.sumOf { it.durationSeconds }
        val corrida = steps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
        return WorkoutEntity(
            id = id,
            planId = 1L,
            semanaIndex = 1,
            treinoIndexNaSemana = 1,
            etapaIndex = etapa,
            data = data.toString(),
            titulo = "Treino $id",
            resumoEtapa = ProgressionTable.getStage(etapa).summary,
            passosJson = WorkoutStepConverter.toJson(steps),
            duracaoMinutos = Math.round(total / 60.0).toInt(),
            duracaoCorridaMinutos = Math.round(corrida / 60.0).toInt(),
            status = status
        )
    }

    /** Treino 1 já concluído em [dia] (etapa 3) e cinco treinos futuros nas etapas 4, 4, 5, 5 e 6. */
    private fun plano(): List<WorkoutEntity> = listOf(
        treino(1, dia, 3, WorkoutEntity.STATUS_CONCLUIDO),
        treino(2, dia.plusDays(2), 4),
        treino(3, dia.plusDays(4), 4),
        treino(4, dia.plusDays(7), 5),
        treino(5, dia.plusDays(9), 5),
        treino(6, dia.plusDays(11), 6)
    )

    private fun fb(
        esforco: Int,
        dor: Int = 0,
        regiao: String? = null,
        data: LocalDate = dia,
        etapa: Int = 3,
        energia: Int? = null,
        sono: Int? = null,
        id: Long = 1
    ) = TrainingFeedback(id, data, etapa, esforco, dor, regiao, energia, sono)

    private fun walks(w: WorkoutEntity): List<Int> =
        WorkoutStepConverter.fromJson(w.passosJson).filter { it.type == StepType.WALK }.map { it.durationSeconds }

    private fun originalWalks(etapa: Int): List<Int> =
        ProgressionTable.buildStepsForStage(etapa).filter { it.type == StepType.WALK }.map { it.durationSeconds }

    private fun avaliar(
        feedbacks: List<TrainingFeedback>,
        workouts: List<WorkoutEntity> = plano(),
        modo: String = AdaptiveEngine.MODO_CAUTELOSO,
        ajustes: Boolean = true,
        deslocamento: Int = 0,
        ultimoAvanco: LocalDate? = null
    ): AdaptiveResult = AdaptiveEngine.evaluate(
        feedbacks = feedbacks,
        workouts = workouts,
        today = dia,
        modo = modo,
        ajustesAutomaticos = ajustes,
        deslocamentoAtual = deslocamento,
        ultimoAvanco = ultimoAvanco
    )

    @Test
    fun `ajustes desligados nao mudam nada`() {
        val r = avaliar(listOf(fb(esforco = 10, dor = 2)), ajustes = false)
        assertFalse(r.temMudancas)
        assertNull(r.regra)
    }

    @Test
    fun `sem respostas nao muda nada`() {
        assertFalse(avaliar(emptyList()).temMudancas)
    }

    @Test
    fun `dor forte troca a corrida dos proximos 3 dias por caminhada e usa a mensagem fixa`() {
        val r = avaliar(listOf(fb(esforco = 6, dor = 2, regiao = "canela")))
        assertEquals(AdaptiveEngine.REGRA_DOR_FORTE, r.regra)
        assertEquals(AdaptiveEngine.MSG_DOR_FORTE, r.mensagem)
        val primeiro = r.mudancas.first { it.antes.id == 2L }.depois
        assertEquals(0, primeiro.duracaoCorridaMinutos)
        assertEquals(20, primeiro.duracaoMinutos)
        // dia +4 está fora dos 3 dias: não vira caminhada
        val dia4 = r.mudancas.first { it.antes.id == 3L }.depois
        assertTrue(dia4.duracaoCorridaMinutos > 0)
    }

    @Test
    fun `dor forte repete a etapa anterior ao voltar`() {
        val r = avaliar(listOf(fb(esforco = 6, dor = 2, etapa = 3)))
        val volta = r.mudancas.first { it.antes.id == 3L }.depois
        assertEquals(2, volta.etapaIndex) // etapa 3 com dor: volta na etapa 2
        assertTrue(r.deslocamentoEtapa < 0)
    }

    @Test
    fun `dor leve repete a etapa atual e soma 30 segundos de caminhada`() {
        val r = avaliar(listOf(fb(esforco = 6, dor = 1, regiao = "joelho", etapa = 3)))
        assertEquals(AdaptiveEngine.REGRA_DOR_LEVE, r.regra)
        val prox = r.mudancas.first { it.antes.id == 2L }.depois
        assertEquals(3, prox.etapaIndex)
        assertEquals(originalWalks(3).map { it + 30 }, walks(prox))
    }

    @Test
    fun `dor leve em regioes diferentes continua sendo dor leve`() {
        val r = avaliar(
            listOf(
                fb(esforco = 4, dor = 1, regiao = "joelho", data = dia.minusDays(2), id = 0),
                fb(esforco = 6, dor = 1, regiao = "canela")
            )
        )
        assertEquals(AdaptiveEngine.REGRA_DOR_LEVE, r.regra)
    }

    @Test
    fun `dor leve na mesma regiao em dois treinos seguidos vira dor forte`() {
        val r = avaliar(
            listOf(
                fb(esforco = 4, dor = 1, regiao = "canela", data = dia.minusDays(2), id = 0),
                fb(esforco = 6, dor = 1, regiao = "canela")
            )
        )
        assertEquals(AdaptiveEngine.REGRA_DOR_FORTE, r.regra)
    }

    @Test
    fun `esforco alto sem dor soma caminhada sem mudar a etapa`() {
        val r = avaliar(listOf(fb(esforco = 8)))
        assertEquals(AdaptiveEngine.REGRA_ESFORCO_ALTO, r.regra)
        assertEquals(1, r.mudancas.size)
        val prox = r.mudancas.first().depois
        assertEquals(4, prox.etapaIndex)
        assertEquals(originalWalks(4).map { it + 30 }, walks(prox))
        assertEquals(0, r.deslocamentoEtapa)
    }

    @Test
    fun `dois treinos seguidos com esforco alto repetem a etapa`() {
        val r = avaliar(listOf(fb(esforco = 8, data = dia.minusDays(2), id = 0), fb(esforco = 10)))
        assertEquals(AdaptiveEngine.REGRA_ESFORCO_ALTO_2X, r.regra)
        assertEquals(3, r.mudancas.first { it.antes.id == 2L }.depois.etapaIndex)
    }

    @Test
    fun `caminhada extra nunca passa de 60 segundos acima do original`() {
        var treinos = plano()
        repeat(4) {
            val r = avaliar(listOf(fb(esforco = 8)), workouts = treinos)
            r.mudancas.forEach { ch -> treinos = treinos.map { if (it.id == ch.depois.id) ch.depois else it } }
        }
        val prox = treinos.first { it.id == 2L }
        val limite = originalWalks(prox.etapaIndex).map { it + 60 }
        walks(prox).zip(limite).forEach { (atual, max) -> assertTrue(atual <= max) }
    }

    @Test
    fun `avanco no modo cauteloso so sugere e pede confirmacao`() {
        val r = avaliar(
            listOf(fb(esforco = 2, data = dia.minusDays(2), id = 0), fb(esforco = 4)),
            deslocamento = -1
        )
        assertEquals(AdaptiveEngine.REGRA_AVANCAR, r.regra)
        assertTrue(r.precisaConfirmacao)
        assertEquals(1, r.deslocamentoEtapa)
        assertEquals(5, r.mudancas.first { it.antes.id == 2L }.depois.etapaIndex)
    }

    @Test
    fun `avanco no modo equilibrado aplica sozinho`() {
        val r = avaliar(
            listOf(fb(esforco = 2, data = dia.minusDays(2), id = 0), fb(esforco = 4)),
            modo = AdaptiveEngine.MODO_EQUILIBRADO,
            deslocamento = -1
        )
        assertEquals(AdaptiveEngine.REGRA_AVANCAR, r.regra)
        assertFalse(r.precisaConfirmacao)
    }

    @Test
    fun `teto - sem reducao anterior o plano nunca fica mais pesado que o original`() {
        val r = avaliar(
            listOf(fb(esforco = 2, data = dia.minusDays(2), id = 0), fb(esforco = 2)),
            modo = AdaptiveEngine.MODO_EQUILIBRADO,
            deslocamento = 0
        )
        assertFalse(r.temMudancas)
    }

    @Test
    fun `avanco e bloqueado por dor forte nos ultimos 7 dias`() {
        val r = avaliar(
            listOf(
                fb(esforco = 6, dor = 2, data = dia.minusDays(3), id = 0),
                fb(esforco = 2, data = dia.minusDays(2), id = 1),
                fb(esforco = 2, id = 2)
            ),
            modo = AdaptiveEngine.MODO_EQUILIBRADO,
            deslocamento = -1
        )
        assertFalse(r.temMudancas)
    }

    @Test
    fun `avanco e no maximo um por semana`() {
        val r = avaliar(
            listOf(fb(esforco = 2, data = dia.minusDays(2), id = 0), fb(esforco = 2)),
            modo = AdaptiveEngine.MODO_EQUILIBRADO,
            deslocamento = -1,
            ultimoAvanco = dia.minusDays(3)
        )
        assertFalse(r.temMudancas)
    }

    @Test
    fun `avanco exige check-ins normais`() {
        val r = avaliar(
            listOf(fb(esforco = 2, data = dia.minusDays(2), id = 0, energia = 1, sono = 1), fb(esforco = 2)),
            modo = AdaptiveEngine.MODO_EQUILIBRADO,
            deslocamento = -1
        )
        assertFalse(r.temMudancas)
    }

    @Test
    fun `nunca altera treinos concluidos ou pulados e nada fica mais pesado`() {
        val treinos = plano() + treino(9, dia.plusDays(3), 6, WorkoutEntity.STATUS_PULADO) +
            treino(10, dia.plusDays(5), 6, WorkoutEntity.STATUS_CONCLUIDO)
        listOf(
            fb(esforco = 6, dor = 2), fb(esforco = 6, dor = 1, regiao = "pe"), fb(esforco = 10)
        ).forEach { feedback ->
            val r = avaliar(listOf(feedback), workouts = treinos)
            r.mudancas.forEach { ch ->
                assertEquals(WorkoutEntity.STATUS_PLANEJADO, ch.antes.status)
                assertTrue(ch.depois.etapaIndex <= ch.antes.etapaIndex)
                assertTrue(ch.depois.duracaoCorridaMinutos <= ch.antes.duracaoCorridaMinutos)
            }
            assertFalse(r.mudancas.any { it.antes.id == 9L || it.antes.id == 10L || it.antes.id == 1L })
        }
    }

    @Test
    fun `dois pulados seguidos recuam conforme o tempo parado`() {
        val r = AdaptiveEngine.evaluateSkips(
            datasPuladas = listOf(dia.minusDays(5), dia.minusDays(2)),
            workouts = plano(),
            today = dia
        )
        assertEquals(AdaptiveEngine.REGRA_PULADOS_2X, r.regra)
        assertEquals(-1, r.deslocamentoEtapa) // 6 dias parados = recuo de 1 etapa
        assertEquals(3, r.mudancas.first { it.antes.id == 2L }.depois.etapaIndex)
    }

    @Test
    fun `dois pulados em poucos dias nao recuam e ajustes desligados ignoram`() {
        val curto = AdaptiveEngine.evaluateSkips(listOf(dia.minusDays(1), dia), plano(), dia)
        assertFalse(curto.temMudancas)
        val off = AdaptiveEngine.evaluateSkips(listOf(dia.minusDays(5), dia.minusDays(2)), plano(), dia, false)
        assertFalse(off.temMudancas)
    }

    @Test
    fun `check-in antes do treino`() {
        assertEquals(CheckinOutcome.MANTER, AdaptiveEngine.evaluateCheckin(2, 2, 0))
        assertEquals(CheckinOutcome.VERSAO_LEVE, AdaptiveEngine.evaluateCheckin(1, 1, 0))
        assertEquals(CheckinOutcome.MANTER, AdaptiveEngine.evaluateCheckin(1, 2, 0))
        assertEquals(CheckinOutcome.VERSAO_LEVE, AdaptiveEngine.evaluateCheckin(3, 3, 1))
        assertEquals(CheckinOutcome.DESCANSO, AdaptiveEngine.evaluateCheckin(3, 3, 2))
    }

    @Test
    fun `versao leve usa a etapa anterior`() {
        val leve = AdaptiveEngine.lightVersion(treino(7, dia.plusDays(2), 5))
        assertEquals(4, leve.etapaIndex)
        assertEquals(WorkoutEntity.STATUS_PLANEJADO, leve.status)
    }
}
