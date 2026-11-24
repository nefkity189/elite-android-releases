package com.example.generator

import android.util.Log
import com.example.model.StepType
import com.example.model.WorkoutStep
import java.time.DayOfWeek
import java.time.LocalDate

data class PlanInput(
    val tempoSemCorrer: String,
    val frequenciaExercicio: String,
    val tipoExercicio: String,
    val diasPorSemana: Int = 3,
    val diasSelecionados: List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.FRIDAY
    ),
    val dataInicio: LocalDate = LocalDate.now(),
    val totalSemanas: Int = 9
)

data class GeneratedWorkout(
    val semanaIndex: Int, // 1..totalSemanas
    val treinoIndexNaSemana: Int, // 1..diasPorSemana
    val etapaIndex: Int, // 1..9
    val data: LocalDate,
    val titulo: String, // "Semana 1 • Treino 1"
    val resumoEtapa: String,
    val steps: List<WorkoutStep>,
    val duracaoTotalMinutos: Int,
    val duracaoCorridaMinutos: Int
)

data class GeneratedPlan(
    val dataInicio: LocalDate,
    val dataFim: LocalDate,
    val totalSemanas: Int,
    val diasPorSemana: Int,
    val etapaInicial: Int,
    val treinos: List<GeneratedWorkout>,
    val modalidade: String = "plano_5k"
)

/**
 * Entrada para geração de plano a partir de template (D5+).
 */
data class TemplatePlanInput(
    val template: PlanTemplate,
    val dataInicio: LocalDate = LocalDate.now(),
    val diasSelecionados: List<DayOfWeek> = listOf(
        DayOfWeek.TUESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.SATURDAY
    ),
    val permitirDiasConsecutivosNaSemanaDaProva: Boolean = false
)

object PlanGenerator {

    /**
     * Gera o plano de treino completo de acordo com as regras:
     * - O primeiro treino cai exatamente na data de início e é de caminhada e corrida.
     * - Todos os treinos depois do primeiro DEVEM cair nos dias escolhidos.
     * - Nunca dois treinos de corrida em dias seguidos (espaçamento mínimo de 1 dia de descanso).
     * - Valida a seleção estritamente: não troca em silêncio os dias escolhidos.
     * - Planos mais curtos comprimem as etapas; planos mais longos repetem etapas.
     * - Nível de condicionamento alto (3 vezes ou mais por semana com cardio) começa na etapa 2.
     */
    fun generate(input: PlanInput): GeneratedPlan {
        // Validação 1: Dias por semana (somente 2 ou 3)
        if (input.diasPorSemana !in 2..3) {
            throw IllegalArgumentException("Quantidade de dias por semana inválida (${input.diasPorSemana}). Permitido apenas 2 ou 3 dias.")
        }

        // Validação 2: Quantidade de dias selecionados
        val uniqueDays = input.diasSelecionados.distinct()
        if (uniqueDays.size != input.diasPorSemana) {
            throw IllegalArgumentException(
                "Quantidade de dias selecionados (${uniqueDays.size}) não corresponde a diasPorSemana (${input.diasPorSemana})."
            )
        }

        // Validação 3: Dias consecutivos proibidos (incluindo domingo e segunda)
        if (hasConsecutiveDays(uniqueDays)) {
            throw IllegalArgumentException(
                "A seleção contém dias consecutivos. Escolha dias com pelo menos um dia de descanso entre eles."
            )
        }

        // Validação 4: Duração mínima (8 semanas para sedentários/Nada, 6 semanas para os demais)
        val minWeeks = getMinimumWeeks(input.frequenciaExercicio)
        if (input.totalSemanas < minWeeks || input.totalSemanas > 16) {
            throw IllegalArgumentException(
                "Duração inválida (${input.totalSemanas} semanas). O mínimo para o seu perfil é de $minWeeks semanas e o máximo é 16 semanas."
            )
        }

        val totalSemanas = input.totalSemanas
        val diasPorSemana = input.diasPorSemana
        val preferredSet = uniqueDays.toSet()

        // Determina etapa inicial pelo nível de condicionamento
        val isHighFitness = isHighFitnessLevel(
            input.frequenciaExercicio,
            input.tipoExercicio
        )
        val startStage = if (isHighFitness) 2 else 1

        // Geração das datas dos treinos:
        // 1. O primeiro treino cai na data de início
        // 2. Todos os treinos seguintes caem obrigatoriamente nos dias escolhidos
        // 3. Nunca dois treinos em dias consecutivos
        val workoutDates = mutableListOf<LocalDate>()
        var lastDate = input.dataInicio
        workoutDates.add(lastDate)

        val totalWorkouts = totalSemanas * diasPorSemana
        var candidate = lastDate.plusDays(2)

        while (workoutDates.size < totalWorkouts) {
            if (candidate.dayOfWeek in preferredSet) {
                workoutDates.add(candidate)
                lastDate = candidate
                candidate = lastDate.plusDays(2)
            } else {
                candidate = candidate.plusDays(1)
            }
        }

        // Monta os treinos atribuindo as etapas calculadas
        val treinos = mutableListOf<GeneratedWorkout>()
        var dateCursor = 0

        for (weekIdx in 0 until totalSemanas) {
            val weekNumber = weekIdx + 1
            val stageIndex = calculateStageForWeek(
                weekIndex = weekIdx,
                totalWeeks = totalSemanas,
                startStage = startStage
            )
            val stageProgression = ProgressionTable.getStage(stageIndex)
            val steps = ProgressionTable.buildStepsForStage(stageIndex)

            val totalDurationSeconds = steps.sumOf { it.durationSeconds }
            val totalRunSeconds = steps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
            val totalDurationMin = Math.round(totalDurationSeconds / 60.0).toInt()
            val totalRunMin = Math.round(totalRunSeconds / 60.0).toInt()

            for (workoutIdx in 0 until diasPorSemana) {
                val workoutNumberInWeek = workoutIdx + 1
                val workoutDate = workoutDates[dateCursor++]

                treinos.add(
                    GeneratedWorkout(
                        semanaIndex = weekNumber,
                        treinoIndexNaSemana = workoutNumberInWeek,
                        etapaIndex = stageIndex,
                        data = workoutDate,
                        titulo = "Semana $weekNumber • Treino $workoutNumberInWeek",
                        resumoEtapa = stageProgression.summary,
                        steps = steps,
                        duracaoTotalMinutos = totalDurationMin,
                        duracaoCorridaMinutos = totalRunMin
                    )
                )
            }
        }

        val dataFim = treinos.last().data

        return GeneratedPlan(
            dataInicio = input.dataInicio,
            dataFim = dataFim,
            totalSemanas = totalSemanas,
            diasPorSemana = diasPorSemana,
            etapaInicial = startStage,
            treinos = treinos,
            modalidade = "plano_5k"
        )
    }

    /**
     * Duração mínima: 8 semanas se a frequência de exercício for "Nada"; 6 semanas nos demais casos.
     * Valores provisórios sujeitos a validação profissional.
     */
    fun getMinimumWeeks(frequencia: String): Int {
        return if (frequencia.trim().equals("Nada", ignoreCase = true)) 8 else 6
    }

    /**
     * Verifica se há dias consecutivos na lista de dias da semana,
     * incluindo domingo e segunda-feira.
     */
    fun hasConsecutiveDays(days: Collection<DayOfWeek>): Boolean {
        val set = days.toSet()
        for (day in set) {
            if (set.contains(day.plus(1))) {
                return true
            }
        }
        return false
    }

    /**
     * Verifica se o usuário possui condicionamento alto:
     * 3 vezes ou mais por semana com cardio (bicicleta ou natação).
     */
    fun isHighFitnessLevel(frequencia: String, tipoExercicio: String): Boolean {
        val freqMatches = frequencia.contains("3 vezes ou mais", ignoreCase = true)
        val cardioMatches = tipoExercicio.contains("Cardio", ignoreCase = true)
        return freqMatches && cardioMatches
    }

    /**
     * Calcula a etapa para uma determinada semana:
     * Planos curtos comprimem etapas (pulam suavemente para cobrir 1..9 até o final);
     * Planos longos repetem etapas para uma evolução mais gradual.
     */
    fun calculateStageForWeek(
        weekIndex: Int,
        totalWeeks: Int,
        startStage: Int = 1
    ): Int {
        if (totalWeeks <= 1) return startStage
        val progress = weekIndex.toDouble() / (totalWeeks - 1)
        val targetStage = 9
        val stage = startStage + Math.round(progress * (targetStage - startStage)).toInt()
        return stage.coerceIn(startStage, targetStage)
    }

    /**
     * Fornece conjunto padrão inicial válido para cada quantidade de dias.
     */
    fun getDefaultDays(count: Int): List<DayOfWeek> {
        return when (count) {
            2 -> listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY)
            else -> listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) // 3 dias
        }
    }

    /**
     * NOVO: Gera plano a partir de um PlanTemplate (D5 - motor de modelos).
     *
     * Regras:
     * - As sessões da semana são distribuídas nos dias escolhidos pelo usuário, na ordem,
     *   com a sessão LONGO no último dia da semana escolhido.
     * - NUNCA dias consecutivos (ADR-05).
     * - Se o usuário escolheu menos dias do que o modelo exige, lança exceção.
     * - Semana da prova: a sessão PROVA ocupa o último dia disponível.
     *   Se permitirDiasConsecutivosNaSemanaDaProva = false (padrão):
     *   se a prova caísse em dia seguinte a outro treino, remove o treino do dia anterior.
     *   Registrar no relatório (via callback ou log).
     */
    fun generateFromTemplate(input: TemplatePlanInput): GeneratedPlanFromTemplate {
        val template = input.template
        val uniqueDays = input.diasSelecionados.distinct()
        val diasPorSemana = uniqueDays.size

        // Validação: dias consecutivos proibidos (exceto possivelmente semana da prova)
        if (hasConsecutiveDays(uniqueDays) && !input.permitirDiasConsecutivosNaSemanaDaProva) {
            throw IllegalArgumentException(
                "A seleção contém dias consecutivos. Escolha dias com pelo menos um dia de descanso entre eles."
            )
        }

        // Validação: usuário deve escolher pelo menos o número de sessões únicas do template
        val minDiasNecessarios = template.totalSessoesUnicas
        if (diasPorSemana < minDiasNecessarios) {
            throw IllegalArgumentException(
                "Este plano exige no mínimo $minDiasNecessarios dias por semana " +
                "(o template define ${template.totalSessoesUnicas} sessões únicas). " +
                "Você selecionou $diasPorSemana dia(s)."
            )
        }

        // Ordena dias da semana para distribuição consistente
        val sortedDays = uniqueDays.sortedBy { it.ordinal }
        val preferredSet = sortedDays.toSet()

        // Gera datas dos treinos para todas as semanas
        val workoutDates = mutableListOf<LocalDate>()
        var firstDate = input.dataInicio
        while (firstDate.dayOfWeek !in preferredSet) {
            firstDate = firstDate.plusDays(1)
        }
        var lastDate = firstDate
        workoutDates.add(lastDate)

        val totalWorkouts = template.semanas * diasPorSemana
        var candidate = lastDate.plusDays(2)

        while (workoutDates.size < totalWorkouts) {
            if (candidate.dayOfWeek in preferredSet) {
                workoutDates.add(candidate)
                lastDate = candidate
                candidate = lastDate.plusDays(2)
            } else {
                candidate = candidate.plusDays(1)
            }
        }

        // Distribui sessões do template nas semanas
        val treinos = mutableListOf<GeneratedWorkoutFromTemplate>()
        var dateCursor = 0

        // Mapeia sessões do template por semana (prefixo "s{N}_" no id indica a semana)
        val sessoesPorSemana = template.sessoesPorSemana

        for (weekIdx in 0 until template.semanas) {
            val weekNumber = weekIdx + 1
            val isRaceWeek = weekIdx == template.semanas - 1 && template.temProva

            // Filtra sessões desta semana baseada no prefixo do ID (ex: "s1_", "s2_", etc.)
            val weekPrefix = "s${weekNumber}_"
            val sessoesDaSemana = mutableListOf<WeekSessionTemplate>()
            sessoesDaSemana.addAll(sessoesPorSemana.filter { it.id.startsWith(weekPrefix) })

            // Se o filtro retornou vazio para esta semana, usa as sessões da semana 1 como fallback
            if (sessoesDaSemana.isEmpty()) {
                val fallbackPrefix = "s1_"
                sessoesDaSemana.addAll(sessoesPorSemana.filter { it.id.startsWith(fallbackPrefix) })
            }

            // Se for semana da prova e não permite dias consecutivos, remove treino anterior à prova
            if (isRaceWeek && !input.permitirDiasConsecutivosNaSemanaDaProva) {
                val provaSession = sessoesDaSemana.find { it.tipo == SessionType.PROVA }
                if (provaSession != null) {
                    // A prova será no último dia disponível da semana
                    // Se houver sessão no dia anterior, remove
                    val provaDayIndex = diasPorSemana - 1 // último dia da semana
                    if (provaDayIndex > 0) {
                        // Remove a sessão do dia anterior à prova (penúltimo dia)
                        if (sessoesDaSemana.size > provaDayIndex) {
                            val removed = sessoesDaSemana.removeAt(provaDayIndex - 1)
                            // Log: registra remoção para relatório
                            Log.d("PlanGenerator", "RELATÓRIO D5: Semana $weekNumber (prova) - removida sessão '${removed.titulo}' do dia anterior à prova para evitar dias consecutivos")
                        }
                    }
                }
            }

            // Distribui sessões nos dias da semana
            // Regra: sessão LONGO no último dia escolhido
            val longoSession = sessoesDaSemana.find { it.tipo == SessionType.LONGO }
            val otherSessions = sessoesDaSemana.filter { it.tipo != SessionType.LONGO }
            val provaSession = sessoesDaSemana.find { it.tipo == SessionType.PROVA }

            val sessoesOrdenadas = mutableListOf<WeekSessionTemplate>()
            sessoesOrdenadas.addAll(otherSessions.filter { it.tipo != SessionType.PROVA })

            // Se tem prova, ela vai no último dia
            if (isRaceWeek && provaSession != null) {
                // LONGO (se houver e não for prova) no penúltimo, PROVA no último
                if (longoSession != null && longoSession.tipo != SessionType.PROVA) {
                    sessoesOrdenadas.add(longoSession)
                }
                sessoesOrdenadas.add(provaSession)
            } else {
                // Sem prova: LONGO no último dia
                sessoesOrdenadas.addAll(otherSessions.filter { it.tipo != SessionType.LONGO })
                if (longoSession != null) {
                    sessoesOrdenadas.add(longoSession)
                }
            }

            // Garante que temos exatamente uma sessão por dia selecionado
            // Se sobrarem sessões, truncamos; se faltarem, duplicamos a última (exceto prova)
            while (sessoesOrdenadas.size < diasPorSemana) {
                val lastSession = sessoesOrdenadas.lastOrNull()
                    ?: WeekSessionTemplate(
                        id = "default",
                        titulo = "Corrida",
                        tipo = SessionType.LEVE,
                        semanaDeRecuperacao = false,
                        passosTemplate = listOf(
                            StepTemplate(
                                type = StepType.RUN,
                                durationSeconds = 1800,
                                instruction = "Correr 30 min"
                            )
                        ),
                        intensidadeDescricao = "",
                        distanciaAlvoM = null
                    )
                sessoesOrdenadas.add(lastSession)
            }
            if (sessoesOrdenadas.size > diasPorSemana) {
                sessoesOrdenadas.subList(diasPorSemana, sessoesOrdenadas.size).clear()
            }

            // Gera treinos para cada dia da semana
            for (dayIdx in 0 until diasPorSemana) {
                if (dateCursor >= workoutDates.size) break

                val sessionTemplate = sessoesOrdenadas[dayIdx]
                val workoutDate = workoutDates[dateCursor++]

                // Expande passos do template
                val expandedSteps = expandSessionSteps(sessionTemplate, template)

                // Calcula durações
                val totalDurationSeconds = expandedSteps.sumOf { it.durationSeconds }
                val totalRunSeconds = expandedSteps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
                val totalDurationMin = Math.round(totalDurationSeconds / 60.0).toInt()
                val totalRunMin = Math.round(totalRunSeconds / 60.0).toInt()

                val modoConclusao = if (sessionTemplate.distanciaAlvoM != null) "distancia" else "tempo"
                val distanciaAlvoTreino = sessionTemplate.distanciaAlvoM

                // Monta título
                val dayName = workoutDate.dayOfWeek.toString()
                val titulo = "Semana $weekNumber • ${sessionTemplate.titulo}"

                treinos.add(
                    GeneratedWorkoutFromTemplate(
                        semanaIndex = weekNumber,
                        diaDaSemana = workoutDate.dayOfWeek,
                        data = workoutDate,
                        sessaoTemplateId = sessionTemplate.id,
                        titulo = titulo,
                        resumoSessao = sessionTemplate.titulo,
                        steps = expandedSteps,
                        duracaoTotalMinutos = totalDurationMin,
                        duracaoCorridaMinutos = totalRunMin,
                        distanciaAlvoM = distanciaAlvoTreino,
                        modoConclusao = modoConclusao,
                        tipoSessao = sessionTemplate.tipo,
                        intensidadeDescricao = sessionTemplate.intensidadeDescricao
                    )
                )
            }
        }

        val dataFim = treinos.last().data

        return GeneratedPlanFromTemplate(
            templateId = template.id,
            modalidade = template.modalidade,
            dataInicio = input.dataInicio,
            dataFim = dataFim,
            totalSemanas = template.semanas,
            diasPorSemana = diasPorSemana,
            diasSelecionados = sortedDays,
            treinos = treinos
        )
    }

    /**
     * Expande os passos de uma sessão incluindo aquecimento e desaquecimento do template.
     */
    private fun expandSessionSteps(
        session: WeekSessionTemplate,
        template: PlanTemplate
    ): List<WorkoutStep> {
        val steps = mutableListOf<WorkoutStep>()

        // 1. Aquecimento
        if (template.aquecimentoSeg > 0) {
            steps.add(WorkoutStep(
                type = StepType.WARMUP,
                durationSeconds = template.aquecimentoSeg,
                instruction = "Aquecimento: caminhada em ritmo confortável (${template.aquecimentoSeg / 60} min)"
            ))
        }

        // 2. Passos da sessão (expandidos do template)
        for (stepTemplate in session.passosTemplate) {
            steps.addAll(stepTemplate.expand())
        }

        // 3. Desaquecimento
        if (template.desaquecimentoSeg > 0) {
            steps.add(WorkoutStep(
                type = StepType.COOLDOWN,
                durationSeconds = template.desaquecimentoSeg,
                instruction = "Desaquecimento: caminhada leve e respiração profunda (${template.desaquecimentoSeg / 60} min)"
            ))
        }

        return steps
    }
}
