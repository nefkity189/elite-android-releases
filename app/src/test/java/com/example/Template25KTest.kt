package com.example

import com.example.generator.PlanGenerator
import com.example.generator.Template25K
import com.example.generator.TemplatePlanInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class Template25KTest {

    @Test
    fun `template 25k tem estrutura correta`() {
        val template = Template25K.getTemplate()
        assertEquals("plano_2_5k", template.modalidade)
        assertEquals(6, template.semanas)
        // Template define 19 sessões total (3 por semana nas semanas 1-5 + 4 na semana 6)
        assertEquals(19, template.sessoesPorSemana.size)
        // Sessões regulares por semana = 3 (semana 6 tem prova no domingo além das 3 regulares)
        assertEquals(3, template.totalSessoesUnicas)
        assertTrue(template.temProva)
    }

    @Test
    fun `geracao de plano 25k com 3 dias por semana`() {
        val template = Template25K.getTemplate()
        val input = TemplatePlanInput(
            template = template,
            dataInicio = LocalDate.of(2026, 11, 10), // Terça-feira
            diasSelecionados = listOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY
            )
        )

        val plan = PlanGenerator.generateFromTemplate(input)
        assertNotNull(plan)
        assertEquals("plano_2_5k", plan.modalidade)
        assertEquals(6, plan.totalSemanas)
        assertEquals(3, plan.diasPorSemana)
        assertEquals(18, plan.treinos.size)
    }

    @Test
    fun `geracao de plano 25k a partir do template`() {
        val template = Template25K.getTemplate()
        val input = TemplatePlanInput(
            template = template,
            dataInicio = LocalDate.of(2026, 11, 9), // Segunda-feira
            diasSelecionados = listOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            permitirDiasConsecutivosNaSemanaDaProva = true
        )

        val plan = PlanGenerator.generateFromTemplate(input)
        assertNotNull(plan)
        assertEquals("plano_2_5k", plan.modalidade)
        assertEquals(6, plan.totalSemanas)
        assertEquals(4, plan.diasPorSemana)
        assertTrue(plan.treinos.isNotEmpty())

        // Verifica se a última semana tem a prova no domingo
        val lastWeekWorkouts = plan.treinos.filter { it.semanaIndex == 6 }
        assertTrue(lastWeekWorkouts.any { it.tipoSessao == com.example.generator.SessionType.PROVA })
    }

    @Test
    fun `assumidos report contem itens documentados`() {
        val reports = Template25K.assumidosReport
        assertTrue(reports.isNotEmpty())
        assertTrue(reports.any { it.contains("2,5") })
    }
}
