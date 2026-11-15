package com.example

import com.example.generator.PlanGenerator
import com.example.generator.PlanTemplate
import com.example.generator.SessionType
import com.example.generator.StepTemplate
import com.example.generator.WeekSessionTemplate
import com.example.generator.TemplatePlanInput
import com.example.model.StepType
import com.example.model.WorkoutStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class PlanGeneratorTemplateTest {

    private fun createSimpleTemplate(): PlanTemplate {
        val steps = listOf(
            StepTemplate(
                type = StepType.RUN,
                distanceMeters = 500,
                instruction = "Corra 500m",
                intensidade = "leve",
                repeticoes = 3
            ),
            StepTemplate(
                type = StepType.WALK,
                distanceMeters = 200,
                instruction = "Caminhe 200m",
                intensidade = "leve",
                repeticoes = 3
            )
        )

        val session1 = WeekSessionTemplate(
            id = "s1_test",
            titulo = "Teste Semana 1",
            tipo = SessionType.LEVE,
            passosTemplate = steps,
            intensidadeDescricao = "leve = ritmo de conversa",
            distanciaAlvoM = 2100
        )

        val session2 = WeekSessionTemplate(
            id = "s2_test",
            titulo = "Teste Semana 2",
            tipo = SessionType.LEVE,
            passosTemplate = steps,
            intensidadeDescricao = "leve = ritmo de conversa",
            distanciaAlvoM = 2100
        )

        return PlanTemplate(
            id = "test_template",
            modalidade = "test",
            nomeExibicao = "Teste",
            semanas = 2,
            aquecimentoSeg = 300,
            desaquecimentoSeg = 300,
            nivelMinimo = "iniciante",
            sessoesPorSemana = listOf(session1, session2)
        )
    }

    @Test
    fun `generateFromTemplate com template simples`() {
        val template = createSimpleTemplate()
        val input = TemplatePlanInput(
            template = template,
            dataInicio = LocalDate.of(2026, 11, 9),
            diasSelecionados = listOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY
            )
        )

        val plan = PlanGenerator.generateFromTemplate(input)
        assertNotNull(plan)
        assertEquals("test_template", plan.templateId)
        assertEquals(2, plan.totalSemanas)
        assertEquals(3, plan.diasPorSemana)
        assertEquals(6, plan.treinos.size) // 2 semanas x 3 dias

        // Verifica se tem aquecimento e desaquecimento
        val firstWorkout = plan.treinos.first()
        val warmupStep = firstWorkout.steps.firstOrNull { it.type == StepType.WARMUP }
        val cooldownStep = firstWorkout.steps.lastOrNull { it.type == StepType.COOLDOWN }
        assertNotNull(warmupStep)
        assertNotNull(cooldownStep)

        // Verifica modo conclusao (passos têm distância)
        assertEquals("distancia", firstWorkout.modoConclusao)
        assertNotNull(firstWorkout.distanciaAlvoM)
    }

    @Test
    fun `generateFromTemplate valida dias consecutivos`() {
        val template = createSimpleTemplate()
        val input = TemplatePlanInput(
            template = template,
            diasSelecionados = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY // consecutivo
            )
        )

        var exceptionThrown = false
        try {
            PlanGenerator.generateFromTemplate(input)
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
            assertTrue(e.message!!.contains("consecutivos"))
        }
        assertTrue("Deveria lançar exceção para dias consecutivos", exceptionThrown)
    }

    @Test
    fun `generateFromTemplate valida dias insuficientes`() {
        val template = createSimpleTemplate()
        // Template tem 1 sessão por semana, mas passamos 0 dias (lista vazia)
        val input = TemplatePlanInput(
            template = template,
            diasSelecionados = emptyList()
        )

        var exceptionThrown = false
        try {
            PlanGenerator.generateFromTemplate(input)
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
            assertTrue(e.message!!.contains("exige no mínimo"))
        }
        assertTrue("Deveria lançar exceção para dias insuficientes", exceptionThrown)
    }
}