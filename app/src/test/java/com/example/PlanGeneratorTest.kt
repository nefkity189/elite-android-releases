package com.example

import com.example.generator.PlanGenerator
import com.example.generator.PlanInput
import com.example.model.StepType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class PlanGeneratorTest {

    @Test
    fun `primeiro treino cai na data de inicio especificada`() {
        val dataInicio = LocalDate.of(2026, 11, 2) // Segunda-feira
        val input = PlanInput(
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = dataInicio,
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(dataInicio, plan.treinos.first().data)
    }

    @Test
    fun `primeiro treino cai na data de inicio mesmo se data for um dia fora do padrao`() {
        val dataInicio = LocalDate.of(2026, 11, 5) // Quinta-feira
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Baixo impacto, como caminhada",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = dataInicio,
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(dataInicio, plan.treinos.first().data)
    }

    @Test
    fun `nunca dois treinos de corrida em dias seguidos para qualquer configuracao`() {
        // Removido o caso de 4 dias conforme solicitado (somente 2 e 3 dias)
        val configs = listOf(
            Pair(2, 6),
            Pair(3, 9),
            Pair(3, 16),
            Pair(2, 12)
        )

        for ((dias, semanas) in configs) {
            val selectedDays = PlanGenerator.getDefaultDays(dias)
            val input = PlanInput(
                tempoSemCorrer = "De 3 a 6 meses",
                frequenciaExercicio = "1 a 2 vezes",
                tipoExercicio = "Baixo impacto, como caminhada",
                diasPorSemana = dias,
                diasSelecionados = selectedDays,
                dataInicio = LocalDate.of(2026, 11, 1),
                totalSemanas = semanas
            )

            val plan = PlanGenerator.generate(input)
            for (i in 0 until plan.treinos.size - 1) {
                val current = plan.treinos[i].data
                val next = plan.treinos[i + 1].data
                assertTrue(
                    "Dias consecutivos encontrados entre $current e $next para dias=$dias semanas=$semanas",
                    next.toEpochDay() >= current.toEpochDay() + 2
                )
            }
        }
    }

    @Test
    fun `o plano termina em no maximo totalSemanas x 7 dias a partir do inicio para 2 e 3 dias`() {
        val testCases = listOf(
            // 2 dias (ex: Terça e Sábado)
            PlanInput(
                tempoSemCorrer = "Menos de 3 meses",
                frequenciaExercicio = "1 a 2 vezes",
                tipoExercicio = "Baixo impacto, como caminhada",
                diasPorSemana = 2,
                diasSelecionados = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY),
                dataInicio = LocalDate.of(2026, 11, 3), // Terça-feira
                totalSemanas = 8
            ),
            // 3 dias (ex: Segunda, Quarta, Sexta)
            PlanInput(
                tempoSemCorrer = "De 3 a 6 meses",
                frequenciaExercicio = "1 a 2 vezes",
                tipoExercicio = "Baixo impacto, como caminhada",
                diasPorSemana = 3,
                diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                dataInicio = LocalDate.of(2026, 11, 2), // Segunda-feira
                totalSemanas = 9
            ),
            // 3 dias com início em dia fora do padrão (ex: Quinta-feira)
            PlanInput(
                tempoSemCorrer = "Nunca corri",
                frequenciaExercicio = "1 a 2 vezes",
                tipoExercicio = "Baixo impacto, como caminhada",
                diasPorSemana = 3,
                diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                dataInicio = LocalDate.of(2026, 11, 5), // Quinta-feira
                totalSemanas = 10
            )
        )

        for (input in testCases) {
            val plan = PlanGenerator.generate(input)
            val durationDays = plan.treinos.last().data.toEpochDay() - plan.dataInicio.toEpochDay()
            val maxAllowedDays = input.totalSemanas * 7L
            assertTrue(
                "O plano de ${input.diasPorSemana} dias e ${input.totalSemanas} semanas demorou $durationDays dias, ultrapassando o limite de $maxAllowedDays dias.",
                durationDays <= maxAllowedDays
            )
        }
    }

    @Test
    fun `todos os treinos depois do primeiro caem nos dias escolhidos`() {
        val chosenDays = listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY)
        val chosenSet = chosenDays.toSet()

        val input = PlanInput(
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Baixo impacto, como caminhada",
            diasPorSemana = 3,
            diasSelecionados = chosenDays,
            dataInicio = LocalDate.of(2026, 11, 1), // Domingo (fora dos dias escolhidos)
            totalSemanas = 8
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(LocalDate.of(2026, 11, 1), plan.treinos.first().data)

        val subsequentWorkouts = plan.treinos.drop(1)
        assertTrue(subsequentWorkouts.isNotEmpty())
        for (workout in subsequentWorkouts) {
            assertTrue(
                "Treino na data ${workout.data} (${workout.data.dayOfWeek}) não caiu nos dias escolhidos $chosenDays",
                workout.data.dayOfWeek in chosenSet
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `dias consecutivos sao rejeitados com erro de validacao`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Nenhum",
            diasPorSemana = 2,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY), // Consecutivos!
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )
        PlanGenerator.generate(input)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `domingo e segunda sao rejeitados como dias consecutivos`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Nenhum",
            diasPorSemana = 2,
            diasSelecionados = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY), // Consecutivos circulares!
            dataInicio = LocalDate.of(2026, 11, 1),
            totalSemanas = 9
        )
        PlanGenerator.generate(input)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `quantidade de dias diferente de 2 ou 3 eh rejeitada`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Nenhum",
            diasPorSemana = 4, // 4 dias rejeitado
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )
        PlanGenerator.generate(input)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `frequencia Nada rejeita duracao menor que 8 semanas`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "Nada", // Sedentário
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 6 // Inválido para Nada (mínimo 8)
        )
        PlanGenerator.generate(input)
    }

    @Test
    fun `usuario com condicionamento alto (3 vezes ou mais + cardio) comeca na etapa 2`() {
        val input = PlanInput(
            tempoSemCorrer = "Menos de 3 meses",
            frequenciaExercicio = "3 vezes ou mais",
            tipoExercicio = "Cardio, como bicicleta ou natação",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(2, plan.etapaInicial)
        assertEquals(2, plan.treinos.first().etapaIndex)
    }

    @Test
    fun `usuario iniciante comeca na etapa 1`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(1, plan.etapaInicial)
        assertEquals(1, plan.treinos.first().etapaIndex)
    }

    @Test
    fun `total de treinos gerados eh exatamente diasPorSemana multiplicado por totalSemanas`() {
        val input = PlanInput(
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Baixo impacto, como caminhada",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(27, plan.treinos.size)
    }

    @Test
    fun `plano mais curto de 6 semanas comprime etapas e atinge a etapa 9`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes", // Permite 6 semanas
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 6
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(1, plan.treinos.first().etapaIndex)
        assertEquals(9, plan.treinos.last().etapaIndex)
    }

    @Test
    fun `plano mais longo de 16 semanas repete etapas sem regredir`() {
        val input = PlanInput(
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Baixo impacto, como caminhada",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 16
        )

        val plan = PlanGenerator.generate(input)
        assertEquals(1, plan.treinos.first().etapaIndex)
        assertEquals(9, plan.treinos.last().etapaIndex)

        // Verifica monotonicidade (nunca regride etapa)
        for (i in 0 until plan.treinos.size - 1) {
            val currentStage = plan.treinos[i].etapaIndex
            val nextStage = plan.treinos[i + 1].etapaIndex
            assertTrue("Etapa regrediu de $currentStage para $nextStage", nextStage >= currentStage)
        }
    }

    @Test
    fun `cada treino contem aquecimento de 5 min e desaquecimento de 5 min`() {
        val input = PlanInput(
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "1 a 2 vezes",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionados = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            dataInicio = LocalDate.of(2026, 11, 2),
            totalSemanas = 9
        )

        val plan = PlanGenerator.generate(input)
        for (treino in plan.treinos) {
            val firstStep = treino.steps.first()
            val lastStep = treino.steps.last()

            assertEquals(StepType.WARMUP, firstStep.type)
            assertEquals(300, firstStep.durationSeconds) // 5 min

            assertEquals(StepType.COOLDOWN, lastStep.type)
            assertEquals(300, lastStep.durationSeconds) // 5 min
        }
    }
}
