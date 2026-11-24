package com.elite.sch.app

import com.elite.sch.app.data.local.WorkoutStepConverter
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.PauseApplier
import com.elite.sch.app.model.StepType
import com.elite.sch.app.tracker.ReminderDecider
import java.time.DayOfWeek
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PauseApplierTest {

    private fun createSamplePlan(
        startDate: LocalDate = LocalDate.of(2026, 3, 2),
        weeks: Int = 8
    ): PlanEntity {
        return PlanEntity(
            id = 1L,
            status = PlanEntity.STATUS_ATIVO,
            dataInicio = startDate.toString(),
            dataFim = startDate.plusWeeks(weeks.toLong()).minusDays(1).toString(),
            totalSemanas = weeks,
            diasPorSemana = 3,
            etapaInicial = 1,
            termoAceito = true
        )
    }

    private fun createSampleWorkout(
        id: Long,
        date: LocalDate,
        semana: Int,
        treinoNaSemana: Int,
        etapa: Int,
        status: String = WorkoutEntity.STATUS_PLANEJADO
    ): WorkoutEntity {
        return WorkoutEntity(
            id = id,
            planId = 1L,
            semanaIndex = semana,
            treinoIndexNaSemana = treinoNaSemana,
            etapaIndex = etapa,
            data = date.toString(),
            titulo = "Semana $semana • Treino $treinoNaSemana",
            resumoEtapa = "Etapa $etapa",
            passosJson = "[]",
            duracaoMinutos = 30,
            duracaoCorridaMinutos = 10,
            status = status,
            concluidoEm = if (status == WorkoutEntity.STATUS_CONCLUIDO) 1000L else null
        )
    }

    @Test
    fun `calculo do recuo para 3, 5 e 8 dias nos tres retornos`() {
        // 1. Período de 3 dias (base 0)
        assertEquals(0, PauseApplier.calculateRecuo(3L, "normal"))
        assertEquals(1, PauseApplier.calculateRecuo(3L, "gradual"))
        assertEquals(0, PauseApplier.calculateRecuo(3L, "rapido"))

        // 2. Período de 5 dias (base 1)
        assertEquals(1, PauseApplier.calculateRecuo(5L, "normal"))
        assertEquals(2, PauseApplier.calculateRecuo(5L, "gradual"))
        assertEquals(0, PauseApplier.calculateRecuo(5L, "rapido"))

        // 3. Período de 8 dias (base 2)
        assertEquals(2, PauseApplier.calculateRecuo(8L, "normal"))
        assertEquals(3, PauseApplier.calculateRecuo(8L, "gradual"))
        assertEquals(1, PauseApplier.calculateRecuo(8L, "rapido"))
    }

    @Test
    fun `periodos de 3, 7 e 14 dias no PauseApplier preview`() {
        val plan = createSamplePlan(startDate = LocalDate.of(2026, 3, 2), weeks = 12)
        val today = LocalDate.of(2026, 3, 16)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val w1 = createSampleWorkout(1L, LocalDate.of(2026, 3, 2), 1, 1, 3, WorkoutEntity.STATUS_CONCLUIDO)
        val w2 = createSampleWorkout(2L, LocalDate.of(2026, 3, 4), 1, 2, 4, WorkoutEntity.STATUS_CONCLUIDO)
        val workouts = listOf(w1, w2)

        // Período de 3 dias
        val preview3 = PauseApplier.previewPause(
            plan = plan,
            workouts = workouts,
            startDate = today,
            endDate = today.plusDays(2), // 3 dias: 16, 17, 18
            nivel = "sem_treino",
            retorno = "normal",
            today = today,
            diasSelecionados = dias,
            frequenciaExercicio = "Outros"
        )
        assertEquals(0, preview3.recuo)
        assertEquals(4, preview3.etapaDeRetomada) // 4 - 0 = 4

        // Período de 7 dias
        val preview7 = PauseApplier.previewPause(
            plan = plan,
            workouts = workouts,
            startDate = today,
            endDate = today.plusDays(6), // 7 dias
            nivel = "sem_treino",
            retorno = "normal",
            today = today,
            diasSelecionados = dias,
            frequenciaExercicio = "Outros"
        )
        assertEquals(1, preview7.recuo)
        assertEquals(3, preview7.etapaDeRetomada) // 4 - 1 = 3

        // Período de 14 dias
        val preview14 = PauseApplier.previewPause(
            plan = plan,
            workouts = workouts,
            startDate = today,
            endDate = today.plusDays(13), // 14 dias
            nivel = "sem_treino",
            retorno = "normal",
            today = today,
            diasSelecionados = dias,
            frequenciaExercicio = "Outros"
        )
        assertEquals(2, preview14.recuo)
        assertEquals(2, preview14.etapaDeRetomada) // 4 - 2 = 2
    }

    @Test
    fun `PauseApplier nos tres niveis - sem_treino, caminhada e reduzido`() {
        val today = LocalDate.of(2026, 3, 10)
        val d1 = today
        val d2 = today.plusDays(2)

        val w1 = createSampleWorkout(1L, d1, 2, 1, 4, WorkoutEntity.STATUS_PLANEJADO)
        val w2 = createSampleWorkout(2L, d2, 2, 2, 4, WorkoutEntity.STATUS_PLANEJADO)
        val workouts = listOf(w1, w2)

        // 1. Nível sem_treino: viram pausado
        val resultSemTreino = PauseApplier.applyPause(
            workouts = workouts,
            startDate = d1,
            endDate = d2,
            nivel = "sem_treino",
            today = today
        )
        assertTrue(resultSemTreino.all { it.status == WorkoutEntity.STATUS_PAUSADO })

        // 2. Nível caminhada: 20 minutos de caminhada leve com um passo só
        val resultCaminhada = PauseApplier.applyPause(
            workouts = workouts,
            startDate = d1,
            endDate = d2,
            nivel = "caminhada",
            today = today
        )
        for (w in resultCaminhada) {
            assertEquals(WorkoutEntity.STATUS_PLANEJADO, w.status)
            assertEquals(20, w.duracaoMinutos)
            assertEquals(0, w.duracaoCorridaMinutos)
            val steps = WorkoutStepConverter.fromJson(w.passosJson)
            assertEquals(1, steps.size)
            assertEquals(StepType.WALK, steps[0].type)
            assertEquals(20 * 60, steps[0].durationSeconds)
        }

        // 3. Nível reduzido: usa a etapa anterior à do treino (mínimo etapa 1)
        val resultReduzido = PauseApplier.applyPause(
            workouts = workouts,
            startDate = d1,
            endDate = d2,
            nivel = "reduzido",
            today = today
        )
        for (w in resultReduzido) {
            assertEquals(WorkoutEntity.STATUS_PLANEJADO, w.status)
            assertEquals(3, w.etapaIndex) // 4 - 1 = 3
        }

        // Teste de mínimo etapa 1 no reduzido
        val wStage1 = createSampleWorkout(3L, d1, 1, 1, 1, WorkoutEntity.STATUS_PLANEJADO)
        val resultReduzidoMin1 = PauseApplier.applyPause(
            workouts = listOf(wStage1),
            startDate = d1,
            endDate = d2,
            nivel = "reduzido",
            today = today
        )
        assertEquals(1, resultReduzidoMin1[0].etapaIndex)
    }

    @Test
    fun `PauseApplier retroativo transforma treinos passados sem status em pausado`() {
        val today = LocalDate.of(2026, 3, 10)
        val dPast = LocalDate.of(2026, 3, 8)
        val dToday = today

        // Treino do passado que não foi feito nem pulado (ficou planejado no banco)
        val wPast = createSampleWorkout(1L, dPast, 2, 1, 3, WorkoutEntity.STATUS_PLANEJADO)
        val wToday = createSampleWorkout(2L, dToday, 2, 2, 3, WorkoutEntity.STATUS_PLANEJADO)

        // Aplica pausa começando no passado
        val result = PauseApplier.applyPause(
            workouts = listOf(wPast, wToday),
            startDate = dPast,
            endDate = dToday,
            nivel = "caminhada", // Mesmo com nível caminhada, o passado sem status vira pausado!
            today = today
        )

        val updatedPast = result.find { it.id == 1L }
        val updatedToday = result.find { it.id == 2L }

        assertNotNull(updatedPast)
        assertEquals("Treino passado sem status no período deve virar pausado", WorkoutEntity.STATUS_PAUSADO, updatedPast?.status)
        assertEquals("Treino de hoje ou futuro segue a regra do nível", WorkoutEntity.STATUS_PLANEJADO, updatedToday?.status)
        assertEquals(20, updatedToday?.duracaoMinutos)
    }

    @Test
    fun `PauseApplier preserva treinos concluidos e pulados`() {
        val today = LocalDate.of(2026, 3, 10)
        val dPast = LocalDate.of(2026, 3, 8)
        val dToday = today

        val wDone = createSampleWorkout(1L, dPast, 2, 1, 3, WorkoutEntity.STATUS_CONCLUIDO)
        val wSkipped = createSampleWorkout(2L, dToday, 2, 2, 3, WorkoutEntity.STATUS_PULADO)
        val wPlanned = createSampleWorkout(3L, dToday.plusDays(2), 2, 3, 3, WorkoutEntity.STATUS_PLANEJADO)

        val result = PauseApplier.applyPause(
            workouts = listOf(wDone, wSkipped, wPlanned),
            startDate = dPast,
            endDate = dToday.plusDays(3),
            nivel = "sem_treino",
            today = today
        )

        assertEquals("Treino concluído não pode ser alterado", WorkoutEntity.STATUS_CONCLUIDO, result.find { it.id == 1L }?.status)
        assertEquals("Treino pulado não pode ser alterado", WorkoutEntity.STATUS_PULADO, result.find { it.id == 2L }?.status)
        assertEquals("Treino planejado vira pausado", WorkoutEntity.STATUS_PAUSADO, result.find { it.id == 3L }?.status)
    }

    @Test
    fun `lembrete nao dispara em dia pausado`() {
        val today = LocalDate.of(2026, 3, 10)
        val pausedToday = createSampleWorkout(1L, today, 2, 1, 2, WorkoutEntity.STATUS_PAUSADO)
        val plannedAnotherDay = createSampleWorkout(2L, today.plusDays(2), 2, 2, 2, WorkoutEntity.STATUS_PLANEJADO)

        // ReminderDecider deve retornar null para o dia pausado
        val reminderWorkout = ReminderDecider.getWorkoutForReminder(today, listOf(pausedToday, plannedAnotherDay))
        assertNull("Lembrete não pode disparar em dia com treino pausado", reminderWorkout)
    }
}
