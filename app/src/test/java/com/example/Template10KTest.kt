package com.example

import com.example.generator.PlanGenerator
import com.example.generator.SessionType
import com.example.generator.Template10K
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
class Template10KTest {

    @Test
    fun `template 10k tem estrutura correta`() {
        val template = Template10K.getTemplate()
        assertEquals("plano_10k", template.modalidade)
        assertEquals(12, template.semanas)
        // Template define 37 sessões no total (3 por semana nas semanas 1-11 + 4 na semana 12: terça, quinta, sábado, domingo_prova)
        assertEquals(37, template.sessoesPorSemana.size)
        // Sessões regulares por semana = 3 (semana 12 tem prova no domingo além das 3 regulares)
        assertEquals(3, template.totalSessoesUnicas)
        assertTrue(template.temProva)
    }

    @Test
    fun `geracao de plano 10k com 3 dias por semana`() {
        val template = Template10K.getTemplate()
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
        assertEquals("plano_10k", plan.modalidade)
        assertEquals(12, plan.totalSemanas)
        assertEquals(3, plan.diasPorSemana)
        assertEquals(36, plan.treinos.size)
    }

    @Test
    fun `geracao de plano 10k a partir do template`() {
        val template = Template10K.getTemplate()
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
        assertEquals("plano_10k", plan.modalidade)
        assertEquals(12, plan.totalSemanas)
        assertEquals(4, plan.diasPorSemana)
        assertTrue(plan.treinos.isNotEmpty())

        // Verifica se a última semana (semana 12) tem a prova no domingo
        val lastWeekWorkouts = plan.treinos.filter { it.semanaIndex == 12 }
        assertTrue(lastWeekWorkouts.any { it.tipoSessao == SessionType.PROVA })
    }

    @Test
    fun `assumidos report 10k contem itens documentados`() {
        val reports = Template10K.assumidosReport
        assertTrue(reports.isNotEmpty())
        assertTrue(reports.any { it.contains("10") })
    }
}
