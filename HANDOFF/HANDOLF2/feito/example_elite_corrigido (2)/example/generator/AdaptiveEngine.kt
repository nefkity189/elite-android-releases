package com.example.generator

import com.example.data.local.WorkoutStepConverter
import com.example.data.local.entities.WorkoutEntity
import com.example.model.StepType
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/** Limiares do motor de ajuste. Mude aqui para calibrar (com validação de um profissional). */
data class AdaptiveConfig(
    val esforcoAlto: Int = 8,
    val esforcoBaixo: Int = 4,
    val extraCaminhadaSeg: Int = 30,
    val maxExtraCaminhadaSeg: Int = 60,
    val diasSemCorridaDorForte: Int = 3,
    val diasSemProgressaoDorForte: Int = 7,
    val intervaloMinAvancoDias: Int = 7,
    val caminhadaDescansoMin: Int = 20
)

/** Resposta de um treino já feito. esforco de 1 a 10 (a UI grava 2, 4, 6, 8 ou 10). dor: 0, 1 ou 2. */
data class TrainingFeedback(
    val workoutId: Long,
    val data: LocalDate,
    val etapaIndex: Int,
    val esforco: Int,
    val dor: Int,
    val regiaoDor: String? = null,
    val energia: Int? = null,
    val sono: Int? = null
)

data class WorkoutChange(val antes: WorkoutEntity, val depois: WorkoutEntity)

data class AdaptiveResult(
    val regra: String?,
    val mensagem: String?,
    val mudancas: List<WorkoutChange>,
    /** Variação de etapa aplicada aos treinos futuros (negativo = mais leve). */
    val deslocamentoEtapa: Int,
    /** Verdadeiro quando é só uma sugestão: a UI precisa de um toque para aplicar. */
    val precisaConfirmacao: Boolean
) {
    val temMudancas: Boolean get() = mudancas.isNotEmpty()

    companion object {
        val NENHUM = AdaptiveResult(null, null, emptyList(), 0, false)
    }
}

enum class CheckinOutcome { MANTER, VERSAO_LEVE, DESCANSO }

/**
 * Motor de ajuste adaptativo: puro, sem rede e sem decisão por IA.
 * Regras conservadoras: na dúvida, mantém ou reduz. Nunca deixa o plano mais pesado
 * que o original, só mexe em treinos futuros "planejado" e nunca em concluído ou pulado.
 */
object AdaptiveEngine {

    const val REGRA_DOR_FORTE = "DOR_FORTE"
    const val REGRA_DOR_LEVE = "DOR_LEVE"
    const val REGRA_ESFORCO_ALTO = "ESFORCO_ALTO"
    const val REGRA_ESFORCO_ALTO_2X = "ESFORCO_ALTO_2X"
    const val REGRA_AVANCAR = "AVANCAR"
    const val REGRA_PULADOS_2X = "PULADOS_2X"

    const val MODO_CAUTELOSO = "cauteloso"
    const val MODO_EQUILIBRADO = "equilibrado"

    const val MSG_DOR_FORTE = "Pare e procure um profissional de saúde se a dor continuar."

    /** Texto fixo e local por regra (a UI e o P13 usam o mesmo mapa). */
    fun mensagemPadrao(regra: String): String = when (regra) {
        REGRA_DOR_FORTE -> MSG_DOR_FORTE
        REGRA_DOR_LEVE -> "Dor leve: vamos repetir esta etapa antes de avançar, com mais descanso entre os trotes."
        REGRA_ESFORCO_ALTO -> "Esforço alto: o próximo treino ganhou mais descanso entre os trotes."
        REGRA_ESFORCO_ALTO_2X -> "Dois treinos puxados seguidos: vamos repetir esta etapa antes de avançar."
        REGRA_AVANCAR -> "Seus últimos treinos foram tranquilos: dá para voltar a avançar uma etapa."
        REGRA_PULADOS_2X -> "Você pulou dois treinos: o plano volta algumas etapas para o retorno ser gradual."
        else -> ""
    }

    /** Check-in antes do treino: o que fazer hoje. */
    fun evaluateCheckin(energia: Int, sono: Int, dor: Int): CheckinOutcome = when {
        dor >= 2 -> CheckinOutcome.DESCANSO
        dor == 1 -> CheckinOutcome.VERSAO_LEVE
        energia <= 1 && sono <= 1 -> CheckinOutcome.VERSAO_LEVE
        else -> CheckinOutcome.MANTER
    }

    /** Versão leve do treino do dia: usa a etapa anterior (mínimo etapa 1). */
    fun lightVersion(workout: WorkoutEntity): WorkoutEntity {
        val d = LocalDate.parse(workout.data)
        return PauseApplier.applyPause(listOf(workout), d, d, "reduzido", d).first()
    }

    /**
     * Avalia o último treino registrado.
     * @param feedbacks respostas recentes, da mais antiga para a mais recente (a última é a do treino que acabou de acabar)
     * @param workouts todos os treinos do plano
     * @param deslocamentoAtual soma dos deslocamentos de etapa dos ajustes ativos (<= 0)
     * @param ultimoAvanco data do último avanço aplicado (para limitar a 1 por semana)
     */
    fun evaluate(
        feedbacks: List<TrainingFeedback>,
        workouts: List<WorkoutEntity>,
        today: LocalDate,
        modo: String = MODO_CAUTELOSO,
        ajustesAutomaticos: Boolean = true,
        deslocamentoAtual: Int = 0,
        ultimoAvanco: LocalDate? = null,
        config: AdaptiveConfig = AdaptiveConfig()
    ): AdaptiveResult {
        if (!ajustesAutomaticos || feedbacks.isEmpty()) return AdaptiveResult.NENHUM

        val last = feedbacks.last()
        val prev = feedbacks.dropLast(1).lastOrNull()

        val futuros = workouts
            .filter {
                it.status == WorkoutEntity.STATUS_PLANEJADO &&
                    LocalDate.parse(it.data).isAfter(last.data)
            }
            .sortedBy { LocalDate.parse(it.data) }
        val futurosDeCorrida = futuros.filter { it.duracaoCorridaMinutos > 0 }

        // 1) Dor forte, ou dor leve na mesma região em dois treinos seguidos
        val mesmaRegiao = last.dor == 1 && prev != null && prev.dor == 1 &&
            last.regiaoDor != null && last.regiaoDor == prev.regiaoDor
        if (last.dor >= 2 || mesmaRegiao) {
            return dorForte(last, futuros, futurosDeCorrida, config)
        }

        // 2) Dor leve: repete a etapa e soma caminhada no próximo treino
        if (last.dor == 1) {
            return repetirEtapa(
                regra = REGRA_DOR_LEVE,
                alvoEtapa = last.etapaIndex,
                futurosDeCorrida = futurosDeCorrida,
                extraCaminhada = config.extraCaminhadaSeg,
                config = config
            )
        }

        // 3) Esforço alto sem dor
        if (last.esforco >= config.esforcoAlto) {
            val doisSeguidos = prev != null && prev.dor == 0 && prev.esforco >= config.esforcoAlto
            return if (doisSeguidos) {
                repetirEtapa(REGRA_ESFORCO_ALTO_2X, last.etapaIndex, futurosDeCorrida, config.extraCaminhadaSeg, config)
            } else {
                repetirEtapa(REGRA_ESFORCO_ALTO, null, futurosDeCorrida, config.extraCaminhadaSeg, config)
            }
        }

        // 4) Esforço baixo em dois treinos seguidos: devolve uma etapa que tinha sido tirada
        if (prev != null && last.esforco <= config.esforcoBaixo && prev.esforco <= config.esforcoBaixo) {
            val semDor = last.dor == 0 && prev.dor == 0
            val checkinsNormais = listOf(last, prev).all { (it.energia ?: 2) >= 2 && (it.sono ?: 2) >= 2 }
            val semDorForteRecente = feedbacks.none {
                it.dor >= 2 && ChronoUnit.DAYS.between(it.data, today) < config.diasSemProgressaoDorForte
            }
            val semAvancoRecente = ultimoAvanco == null ||
                ChronoUnit.DAYS.between(ultimoAvanco, today) >= config.intervaloMinAvancoDias
            if (semDor && checkinsNormais && semDorForteRecente && semAvancoRecente &&
                deslocamentoAtual < 0 && futurosDeCorrida.isNotEmpty()
            ) {
                val mudancas = futurosDeCorrida.mapNotNull { w ->
                    val nova = (w.etapaIndex + 1).coerceAtMost(9)
                    if (nova == w.etapaIndex) null else WorkoutChange(w, rebuild(w, nova))
                }
                if (mudancas.isNotEmpty()) {
                    return AdaptiveResult(
                        regra = REGRA_AVANCAR,
                        mensagem = mensagemPadrao(REGRA_AVANCAR),
                        mudancas = mudancas,
                        deslocamentoEtapa = 1,
                        precisaConfirmacao = modo != MODO_EQUILIBRADO
                    )
                }
            }
        }
        return AdaptiveResult.NENHUM
    }

    /**
     * Dois treinos pulados em sequência: recua conforme o tempo parado
     * (reusa PauseApplier.calculateRecuo).
     * @param datasPuladas datas dos treinos pulados em sequência (pelo menos 2)
     */
    fun evaluateSkips(
        datasPuladas: List<LocalDate>,
        workouts: List<WorkoutEntity>,
        today: LocalDate,
        ajustesAutomaticos: Boolean = true
    ): AdaptiveResult {
        if (!ajustesAutomaticos || datasPuladas.size < 2) return AdaptiveResult.NENHUM
        val primeira = datasPuladas.minOrNull() ?: return AdaptiveResult.NENHUM
        val dias = ChronoUnit.DAYS.between(primeira, today) + 1
        val recuo = PauseApplier.calculateRecuo(dias, "normal")
        if (recuo <= 0) return AdaptiveResult.NENHUM
        val ultima = datasPuladas.maxOrNull() ?: return AdaptiveResult.NENHUM
        val mudancas = workouts
            .filter {
                it.status == WorkoutEntity.STATUS_PLANEJADO &&
                    it.duracaoCorridaMinutos > 0 &&
                    LocalDate.parse(it.data).isAfter(ultima)
            }
            .sortedBy { LocalDate.parse(it.data) }
            .mapNotNull { w ->
                val nova = (w.etapaIndex - recuo).coerceAtLeast(1)
                if (nova == w.etapaIndex) null else WorkoutChange(w, rebuild(w, nova))
            }
        if (mudancas.isEmpty()) return AdaptiveResult.NENHUM
        return AdaptiveResult(
            regra = REGRA_PULADOS_2X,
            mensagem = mensagemPadrao(REGRA_PULADOS_2X),
            mudancas = mudancas,
            deslocamentoEtapa = -recuo,
            precisaConfirmacao = false
        )
    }

    // ---------- regras ----------

    private fun dorForte(
        last: TrainingFeedback,
        futuros: List<WorkoutEntity>,
        futurosDeCorrida: List<WorkoutEntity>,
        config: AdaptiveConfig
    ): AdaptiveResult {
        val limite = last.data.plusDays(config.diasSemCorridaDorForte.toLong())
        val mudancas = mutableListOf<WorkoutChange>()

        // Próximos dias: sem corrida (caminhada leve no lugar)
        val emDescanso = futuros.filter {
            it.duracaoCorridaMinutos > 0 && !LocalDate.parse(it.data).isAfter(limite)
        }
        emDescanso.forEach { mudancas.add(WorkoutChange(it, walkOnly(it, config))) }

        // Ao voltar: repete a etapa anterior à do treino com dor
        val depois = futurosDeCorrida.filter { it !in emDescanso }
        val alvo = (last.etapaIndex - 1).coerceAtLeast(1)
        val primeiro = depois.firstOrNull()
        var deslocamento = 0
        if (primeiro != null && primeiro.etapaIndex > alvo) {
            deslocamento = alvo - primeiro.etapaIndex
            depois.forEach { w ->
                val nova = (w.etapaIndex + deslocamento).coerceAtLeast(1)
                if (nova != w.etapaIndex) mudancas.add(WorkoutChange(w, rebuild(w, nova)))
            }
        }
        return AdaptiveResult(
            regra = REGRA_DOR_FORTE,
            mensagem = MSG_DOR_FORTE,
            mudancas = mudancas,
            deslocamentoEtapa = deslocamento,
            precisaConfirmacao = false
        )
    }

    /**
     * Repete uma etapa: alvoEtapa define a etapa do próximo treino (nunca mais pesada que a atual).
     * Se alvoEtapa for nulo, não mexe nas etapas, só soma caminhada.
     */
    private fun repetirEtapa(
        regra: String,
        alvoEtapa: Int?,
        futurosDeCorrida: List<WorkoutEntity>,
        extraCaminhada: Int,
        config: AdaptiveConfig
    ): AdaptiveResult {
        val primeiro = futurosDeCorrida.firstOrNull() ?: return AdaptiveResult.NENHUM
        var deslocamento = 0
        val finais = futurosDeCorrida.associate { it.id to it }.toMutableMap()

        if (alvoEtapa != null && primeiro.etapaIndex > alvoEtapa) {
            deslocamento = -1
            futurosDeCorrida.forEach { w ->
                val nova = (w.etapaIndex + deslocamento).coerceAtLeast(1)
                if (nova != w.etapaIndex) finais[w.id] = rebuild(w, nova)
            }
        }
        // Soma caminhada no próximo treino (sobre a versão já recalculada)
        val comCaminhada = addWalk(finais.getValue(primeiro.id), extraCaminhada, config.maxExtraCaminhadaSeg)
        if (comCaminhada != null) finais[primeiro.id] = comCaminhada

        val mudancas = futurosDeCorrida.mapNotNull { w ->
            val depois = finais.getValue(w.id)
            if (depois == w) null else WorkoutChange(w, depois)
        }
        if (mudancas.isEmpty()) return AdaptiveResult.NENHUM
        return AdaptiveResult(
            regra = regra,
            mensagem = mensagemPadrao(regra),
            mudancas = mudancas,
            deslocamentoEtapa = deslocamento,
            precisaConfirmacao = false
        )
    }

    // ---------- construção de treinos ----------

    /** Recria passos, resumo e durações a partir da tabela de progressão. */
    private fun rebuild(w: WorkoutEntity, etapa: Int): WorkoutEntity {
        val steps = ProgressionTable.buildStepsForStage(etapa)
        val total = steps.sumOf { it.durationSeconds }
        val corrida = steps.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
        return w.copy(
            etapaIndex = etapa.coerceIn(1, 9),
            resumoEtapa = ProgressionTable.getStage(etapa).summary,
            passosJson = WorkoutStepConverter.toJson(steps),
            duracaoMinutos = Math.round(total / 60.0).toInt(),
            duracaoCorridaMinutos = Math.round(corrida / 60.0).toInt()
        )
    }

    private fun walkOnly(w: WorkoutEntity, config: AdaptiveConfig): WorkoutEntity {
        val d = LocalDate.parse(w.data)
        val base = PauseApplier.applyPause(listOf(w), d, d, "caminhada", d).first()
        return base.copy(
            duracaoMinutos = config.caminhadaDescansoMin,
            resumoEtapa = "Caminhada leve de ${config.caminhadaDescansoMin} min"
        )
    }

    /**
     * Soma [extra] segundos a cada caminhada do treino, limitado a [maxExtra] acima do original
     * da etapa. Devolve null se nada mudar.
     */
    private fun addWalk(w: WorkoutEntity, extra: Int, maxExtra: Int): WorkoutEntity? {
        val atuais = WorkoutStepConverter.fromJson(w.passosJson)
        val originaisWalk = ProgressionTable.buildStepsForStage(w.etapaIndex)
            .filter { it.type == StepType.WALK }
        var indiceWalk = 0
        var mudou = false
        val novos = atuais.map { step ->
            if (step.type != StepType.WALK) {
                step
            } else {
                val original = originaisWalk.getOrNull(indiceWalk)?.durationSeconds ?: step.durationSeconds
                indiceWalk++
                val alvo = minOf(step.durationSeconds + extra, original + maxExtra)
                if (alvo > step.durationSeconds) {
                    mudou = true
                    step.copy(durationSeconds = alvo)
                } else {
                    step
                }
            }
        }
        if (!mudou) return null
        val total = novos.sumOf { it.durationSeconds }
        val corrida = novos.filter { it.type == StepType.RUN }.sumOf { it.durationSeconds }
        return w.copy(
            resumoEtapa = ProgressionTable.getStage(w.etapaIndex).summary + " (mais descanso entre os trotes)",
            passosJson = WorkoutStepConverter.toJson(novos),
            duracaoMinutos = Math.round(total / 60.0).toInt(),
            duracaoCorridaMinutos = Math.round(corrida / 60.0).toInt()
        )
    }
}
