package com.elite.sch.app

import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.generator.PlanGenerator
import com.elite.sch.app.generator.PlanRegenerator
import com.elite.sch.app.tracker.ReminderDecider
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import java.time.DayOfWeek
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PlanRegeneratorTest {

    private fun createSamplePlan(
        startDate: LocalDate = LocalDate.of(2026, 3, 2), // Segunda-feira
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
            titulo = "Semana $semana - Treino $treinoNaSemana",
            resumoEtapa = "Etapa $etapa",
            passosJson = "[]",
            duracaoMinutos = 30,
            duracaoCorridaMinutos = 10,
            status = status,
            concluidoEm = if (status == WorkoutEntity.STATUS_CONCLUIDO) 1000L else null
        )
    }

    @Test
    fun `completed and skipped workouts are preserved and not deleted`() {
        val plan = createSamplePlan()
        val d1 = LocalDate.of(2026, 3, 2)
        val d2 = LocalDate.of(2026, 3, 4)
        val d3 = LocalDate.of(2026, 3, 6)
        val d4 = LocalDate.of(2026, 3, 9)
        val d5 = LocalDate.of(2026, 3, 11)

        val w1 = createSampleWorkout(1L, d1, 1, 1, 1, WorkoutEntity.STATUS_CONCLUIDO)
        val w2 = createSampleWorkout(2L, d2, 1, 2, 1, WorkoutEntity.STATUS_PULADO)
        val w3 = createSampleWorkout(3L, d3, 1, 3, 1, WorkoutEntity.STATUS_PLANEJADO)
        val w4 = createSampleWorkout(4L, d4, 2, 1, 2, WorkoutEntity.STATUS_PLANEJADO)
        val w5 = createSampleWorkout(5L, d5, 2, 2, 2, WorkoutEntity.STATUS_PLANEJADO)

        val today = d3
        val newEndDate = LocalDate.of(2026, 5, 3) // 9 semanas a partir do início
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(w1, w2, w3, w4, w5),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        // w1 e w2 não podem estar na lista de exclusão
        assertFalse("Treino concluído não pode ser apagado", result.toDelete.any { it.id == w1.id })
        assertFalse("Treino pulado não pode ser apagado", result.toDelete.any { it.id == w2.id })
        // w3, w4, w5 devem ser apagados porque são de hoje em diante e planejados
        assertTrue("Treino planejado de hoje deve ser apagado para recálculo", result.toDelete.any { it.id == w3.id })
        assertTrue("Treino futuro deve ser apagado", result.toDelete.any { it.id == w4.id })
    }

    @Test
    fun `nothing before today is altered even without status`() {
        val plan = createSamplePlan()
        val dPast1 = LocalDate.of(2026, 3, 2)
        val dPast2 = LocalDate.of(2026, 3, 4)
        val today = LocalDate.of(2026, 3, 9)

        // wPast2 é do passado e nunca teve status atualizado (ficou como planejado/perdido)
        val wPast1 = createSampleWorkout(1L, dPast1, 1, 1, 1, WorkoutEntity.STATUS_CONCLUIDO)
        val wPast2 = createSampleWorkout(2L, dPast2, 1, 2, 1, WorkoutEntity.STATUS_PLANEJADO)
        val wToday = createSampleWorkout(3L, today, 2, 1, 2, WorkoutEntity.STATUS_PLANEJADO)

        val newEndDate = LocalDate.of(2026, 5, 10)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(wPast1, wPast2, wToday),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        // wPast2 é anterior a hoje: NÃO pode ser apagado
        assertFalse("Treino passado sem status não pode ser apagado", result.toDelete.any { it.id == wPast2.id })
        // Treinos gerados só devem começar a partir de hoje
        assertTrue("Treinos criados devem ter data >= today", result.toCreate.all { LocalDate.parse(it.data) >= today })
    }

    @Test
    fun `resumes at the stage of the last completed workout repeating that stage`() {
        val plan = createSamplePlan()
        val d1 = LocalDate.of(2026, 3, 2)
        val d2 = LocalDate.of(2026, 3, 4)
        val today = LocalDate.of(2026, 3, 6)

        // Último concluído foi na etapa 4
        val w1 = createSampleWorkout(1L, d1, 1, 1, 3, WorkoutEntity.STATUS_CONCLUIDO)
        val w2 = createSampleWorkout(2L, d2, 1, 2, 4, WorkoutEntity.STATUS_CONCLUIDO)

        val newEndDate = LocalDate.of(2026, 5, 15)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(w1, w2),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        // Primeiro treino recalculado deve começar na etapa 4 (repetindo a etapa do último concluído)
        val firstNew = result.toCreate.first()
        assertEquals("Deve retomar na etapa do último concluído (etapa 4)", 4, firstNew.etapaIndex)
    }

    @Test
    fun `resumes at initial stage if no workout has been completed`() {
        val plan = createSamplePlan()
        val today = LocalDate.of(2026, 3, 6)
        val d1 = LocalDate.of(2026, 3, 2)
        val d2 = LocalDate.of(2026, 3, 4)

        val w1 = createSampleWorkout(1L, d1, 1, 1, 1, WorkoutEntity.STATUS_PULADO)
        val w2 = createSampleWorkout(2L, d2, 1, 2, 1, WorkoutEntity.STATUS_PLANEJADO)

        val newEndDate = LocalDate.of(2026, 5, 15)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(w1, w2),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        val firstNew = result.toCreate.first()
        assertEquals("Sem concluídos deve retomar na etapa inicial (etapa 1)", 1, firstNew.etapaIndex)
    }

    @Test
    fun `last workout date does not exceed newEndDate`() {
        val plan = createSamplePlan()
        val today = LocalDate.of(2026, 3, 2)
        val newEndDate = LocalDate.of(2026, 5, 3)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = emptyList(),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        assertTrue(
            "A data do último treino (${result.dataUltimoTreino}) não pode ultrapassar newEndDate ($newEndDate)",
            !result.dataUltimoTreino.isAfter(newEndDate)
        )
        assertTrue(
            "Todos os treinos criados devem ter data <= newEndDate",
            result.toCreate.all { !LocalDate.parse(it.data).isAfter(newEndDate) }
        )
    }

    @Test
    fun `regenerated workouts fall strictly on chosen days with no consecutive days`() {
        val plan = createSamplePlan()
        val today = LocalDate.of(2026, 3, 2)
        val newEndDate = LocalDate.of(2026, 5, 10)
        val dias = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = emptyList(),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        val dates = result.toCreate.map { LocalDate.parse(it.data) }
        assertTrue("Treinos devem cair estritamente nos dias escolhidos", dates.all { it.dayOfWeek in dias })

        // Verifica que não há dias consecutivos
        for (i in 0 until dates.size - 1) {
            val dCurrent = dates[i]
            val dNext = dates[i + 1]
            assertFalse(
                "Não pode haver dias consecutivos ($dCurrent e $dNext)",
                dCurrent.plusDays(1) == dNext
            )
        }
    }

    @Test
    fun `respects minimum weeks and rejects invalid short end date`() {
        val plan = createSamplePlan(startDate = LocalDate.of(2026, 3, 2), weeks = 8)
        val today = LocalDate.of(2026, 3, 2)
        // 5 semanas a partir do início é menor que o mínimo de 8 semanas para freq "Nada"
        val tooShortEndDate = LocalDate.of(2026, 4, 5)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        try {
            PlanRegenerator.regenerate(
                plan = plan,
                currentWorkouts = emptyList(),
                today = today,
                newEndDate = tooShortEndDate,
                diasSelecionados = dias,
                frequenciaExercicio = "Nada"
            )
            fail("Deveria ter lançado IllegalArgumentException para duração inferior ao mínimo")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("duração mínima") == true)
        }
    }

    @Test
    fun `reminder decider returns workout only if planned on that date`() {
        val today = LocalDate.of(2026, 3, 10)
        val otherDay = LocalDate.of(2026, 3, 12)

        val plannedToday = createSampleWorkout(1L, today, 2, 1, 2, WorkoutEntity.STATUS_PLANEJADO)
        val completedToday = createSampleWorkout(2L, today, 2, 1, 2, WorkoutEntity.STATUS_CONCLUIDO)
        val skippedToday = createSampleWorkout(3L, today, 2, 1, 2, WorkoutEntity.STATUS_PULADO)
        val plannedOtherDay = createSampleWorkout(4L, otherDay, 2, 2, 2, WorkoutEntity.STATUS_PLANEJADO)

        // 1. Dia com treino planejado -> retorna o treino
        val result1 = ReminderDecider.getWorkoutForReminder(today, listOf(plannedToday, plannedOtherDay))
        assertNotNull(result1)
        assertEquals(plannedToday.id, result1?.id)

        // 2. Dia com treino já concluído -> null
        val result2 = ReminderDecider.getWorkoutForReminder(today, listOf(completedToday))
        assertNull(result2)

        // 3. Dia com treino pulado -> null
        val result3 = ReminderDecider.getWorkoutForReminder(today, listOf(skippedToday))
        assertNull(result3)

        // 4. Dia sem treino -> null
        val result4 = ReminderDecider.getWorkoutForReminder(LocalDate.of(2026, 3, 11), listOf(plannedToday))
        assertNull(result4)

        // Formatação do texto de notificação
        val notifText = ReminderDecider.formatNotificationText(context = ApplicationProvider.getApplicationContext(), plannedToday)
        assertTrue(notifText.contains(plannedToday.titulo))
    }

    @Test
    fun `etapa 3 com 2 semanas restantes e rejeitada com mensagem de prazo curto`() {
        val plan = createSamplePlan(startDate = LocalDate.of(2026, 1, 5), weeks = 12)
        val today = LocalDate.of(2026, 3, 2) // Segunda-feira (semana 9 do plano)
        val wLastDone = createSampleWorkout(1L, today.minusDays(3), 8, 3, 3, WorkoutEntity.STATUS_CONCLUIDO)

        // 2 semanas restantes: hoje (02/03) até domingo (15/03)
        val newEndDate = today.plusWeeks(2).minusDays(1)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        try {
            PlanRegenerator.regenerate(
                plan = plan,
                currentWorkouts = listOf(wLastDone),
                today = today,
                newEndDate = newEndDate,
                diasSelecionados = dias,
                frequenciaExercicio = "Nada"
            )
            fail("Deveria ter lançado IllegalArgumentException informando mínimo de 4 semanas restantes")
        } catch (e: IllegalArgumentException) {
            assertEquals("Prazo curto demais para a etapa atual: mínimo de 4 semanas restantes", e.message)
        }
    }

    @Test
    fun `etapa 3 com 4 semanas e aceita e nenhuma semana avanca mais de 2 etapas`() {
        val plan = createSamplePlan(startDate = LocalDate.of(2026, 1, 5), weeks = 12)
        val today = LocalDate.of(2026, 3, 2) // Segunda-feira
        val wLastDone = createSampleWorkout(1L, today.minusDays(3), 8, 3, 3, WorkoutEntity.STATUS_CONCLUIDO)

        // 4 semanas restantes: hoje (02/03) até domingo (29/03)
        val newEndDate = today.plusWeeks(4).minusDays(1)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(wLastDone),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        assertNotNull(result)
        val weeks = result.toCreate.groupBy { it.semanaIndex }
        assertEquals(4, weeks.size)

        var prevStage = 3
        var isFirstWeek = true
        for ((_, workoutsInWeek) in weeks) {
            val weekStage = workoutsInWeek.first().etapaIndex
            assertTrue("Todos os treinos da semana devem ter a mesma etapa", workoutsInWeek.all { it.etapaIndex == weekStage })
            if (isFirstWeek) {
                assertEquals("Primeira semana deve repetir etapa de retomada (3)", 3, weekStage)
                isFirstWeek = false
            } else {
                val advance = weekStage - prevStage
                assertTrue("Nenhuma semana pode avançar mais de 2 etapas (avanço foi $advance)", advance in 0..2)
            }
            prevStage = weekStage
        }
        assertEquals("Última semana deve atingir a etapa 9", 9, prevStage)
    }

    @Test
    fun `numeracao das semanas completa semana incompleta antes de abrir a proxima`() {
        val plan = createSamplePlan(startDate = LocalDate.of(2026, 1, 5), weeks = 12)
        val dMon = LocalDate.of(2026, 3, 16)
        val dWed = LocalDate.of(2026, 3, 18)
        val dFri = LocalDate.of(2026, 3, 20)

        // 2 dos 3 treinos da semana 3 já passaram
        val w1 = createSampleWorkout(1L, dMon, 3, 1, 3, WorkoutEntity.STATUS_CONCLUIDO)
        val w2 = createSampleWorkout(2L, dWed, 3, 2, 3, WorkoutEntity.STATUS_CONCLUIDO)

        val today = dFri
        val newEndDate = today.plusWeeks(6)
        val dias = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

        val result = PlanRegenerator.regenerate(
            plan = plan,
            currentWorkouts = listOf(w1, w2),
            today = today,
            newEndDate = newEndDate,
            diasSelecionados = dias,
            frequenciaExercicio = "Nada"
        )

        val firstCreated = result.toCreate[0]
        assertEquals("Primeiro novo treino deve continuar na Semana 3", 3, firstCreated.semanaIndex)
        assertEquals("Primeiro novo treino deve ser o Treino 3", 3, firstCreated.treinoIndexNaSemana)
        assertEquals("Semana 3 • Treino 3", firstCreated.titulo)

        val secondCreated = result.toCreate[1]
        assertEquals("Segundo novo treino deve abrir a Semana 4", 4, secondCreated.semanaIndex)
        assertEquals("Segundo novo treino deve ser o Treino 1", 1, secondCreated.treinoIndexNaSemana)
        assertEquals("Semana 4 • Treino 1", secondCreated.titulo)
    }

    @Test
    fun `canal dos lembretes possui id e nome proprios e nao usa canal de tracking`() {
        assertEquals("lembretes_treino", com.elite.sch.app.worker.WorkoutReminderWorker.CHANNEL_ID)
        assertEquals("Lembretes de treino", com.elite.sch.app.worker.WorkoutReminderWorker.CHANNEL_NAME)
    }
}
