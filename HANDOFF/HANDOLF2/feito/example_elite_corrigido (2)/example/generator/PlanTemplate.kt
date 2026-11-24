package com.example.generator

import com.example.model.StepType
import com.example.model.WorkoutStep
import java.time.DayOfWeek

/**
 * Modelo de plano de treino baseado em template (PlanTemplate).
 * Permite definir planos profissionais com sessões variadas por semana,
 * suportando passos por tempo OU distância, e intensidades descritivas.
 */
data class PlanTemplate(
    val id: String,
    val modalidade: String,           // ex: "plano_2_5k", "plano_5k", "plano_10k", "livre"
    val nomeExibicao: String,         // ex: "Primeiros 2,5K", "Primeiros 5K", "Primeiros 10K"
    val semanas: Int,
    val aquecimentoSeg: Int,          // aquecimento padrão em segundos
    val desaquecimentoSeg: Int,       // desaquecimento padrão em segundos
    val nivelMinimo: String,          // descrição do nível mínimo exigido
    val sessoesPorSemana: List<WeekSessionTemplate>
) {
    /**
     * Total de sessões únicas por semana (para validação de dias mínimos).
     * Calcula o máximo de sessões em qualquer semana do template.
     */
    val totalSessoesUnicas: Int
        get() {
            if (semanas <= 0) return 0
            // Agrupa sessões por semana (prefixo "s{N}_" no id) e pega o máximo
            val sessionsByWeek = mutableMapOf<Int, MutableList<WeekSessionTemplate>>()
            for (session in sessoesPorSemana) {
                // Tenta extrair número da semana do ID (ex: "s1_terca" -> 1)
                val parts = session.id.split("_")
                val weekNum = parts.firstOrNull()?.removePrefix("s")?.toIntOrNull() ?: 1
                sessionsByWeek.getOrPut(weekNum) { mutableListOf() }.add(session)
            }
            val regularCounts = sessionsByWeek.map { (_, sessoes) ->
                sessoes.count { it.tipo != SessionType.PROVA }
            }
            return regularCounts.maxOfOrNull { it } ?: (sessoesPorSemana.size / semanas)
        }

    /**
     * Verifica se o template exige sessão de prova (tipo PROVA).
     */
    val temProva: Boolean
        get() = sessoesPorSemana.any { it.tipo == SessionType.PROVA }
}

/**
 * Template de uma sessão semanal (ex: "Terça - Leve Alternado").
 * A sessão é instanciada para cada semana conforme a progressão.
 */
data class WeekSessionTemplate(
    val id: String,                   // identificador único da sessão no template
    val titulo: String,               // ex: "Leve Alternado", "Intervalado", "Longo", "Prova"
    val tipo: SessionType,            // LEVE, INTERVALADO, LONGO, PROVA
    val semanaDeRecuperacao: Boolean = false, // se true, volume reduzido nessa semana
    val passosTemplate: List<StepTemplate>,
    val intensidadeDescricao: String = "", // texto descritivo: "leve = ritmo de conversa", etc.
    val distanciaAlvoM: Int? = null   // se definido, treino por distância com esse alvo
) {
    /**
     * Duração total estimada da sessão em segundos (soma dos passos).
     * Para passos por distância, usa StepEstimator com ritmos padrão.
     */
    fun duracaoTotalEstimadaSegundos(
        ritmoCorridaSegKm: Int = PaceDefaults.corridaSegKm,
        ritmoCaminhadaSegKm: Int = PaceDefaults.caminhadaSegKm
    ): Int {
        return passosTemplate.sumOf { template ->
            template.repeticoes * template.passosInternos.sumOf { step ->
                step.distanceMeters?.let { dist ->
                    StepEstimator.estimateDurationSeconds(dist, step.type, ritmoCorridaSegKm, ritmoCaminhadaSegKm)
                } ?: step.durationSeconds
            }
        } + (if (distanciaAlvoM != null) 0 else aquecimentoPadrao + desaquecimentoPadrao)
    }

    companion object {
        const val aquecimentoPadrao = 5 * 60  // 5 min
        const val desaquecimentoPadrao = 5 * 60 // 5 min
    }
}

/**
 * Template de um passo dentro de uma sessão.
 * Suporta repetição (n vezes uma lista de sub-passos).
 */
data class StepTemplate(
    val type: StepType,
    val durationSeconds: Int = 0,     // usado quando NÃO é por distância
    val distanceMeters: Int? = null,  // se definido, passo controlled por distância
    val instruction: String = "",
    val intensidade: String = "",     // ex: "leve", "forte controlled", "solto"
    val repeticoes: Int = 1,          // número de vezes que este bloco se repete
    val subSteps: List<StepTemplate> = emptyList() // Para blocos compostos: ex: 3x(30s forte / 1min caminhada)
) {
    val isDistanceBased: Boolean
        get() = distanceMeters != null && distanceMeters > 0

    /**
     * Expande este template em uma lista plana de WorkoutStep.
     * Aquecimento e desaquecimento são adicionados externamente pelo gerador.
     */
    fun expand(
        ritmoCorridaSegKm: Int = PaceDefaults.corridaSegKm,
        ritmoCaminhadaSegKm: Int = PaceDefaults.caminhadaSegKm
    ): List<WorkoutStep> {
        val steps = mutableListOf<WorkoutStep>()
        repeat(repeticoes) {
            for (subStep in passosInternos) {
                val estimatedDuration = if (subStep.isDistanceBased) {
                    StepEstimator.estimateDurationSeconds(
                        subStep.distanceMeters!!,
                        subStep.type,
                        ritmoCorridaSegKm,
                        ritmoCaminhadaSegKm
                    )
                } else {
                    subStep.durationSeconds
                }
                steps.add(WorkoutStep(
                    type = subStep.type,
                    durationSeconds = estimatedDuration,
                    instruction = buildInstruction(subStep),
                    distanceMeters = subStep.distanceMeters
                ))
            }
        }
        return steps
    }

    /**
     * Sub-passos internos para suportar blocos compostos (ex: 3x(30s forte / 1min caminhada)).
     * Se vazio, usa este próprio step como único.
     */
    val passosInternos: List<StepTemplate>
        get() = if (subSteps.isEmpty()) listOf(this) else subSteps

    private fun buildInstruction(step: StepTemplate): String {
        val base = when (step.type) {
            StepType.RUN -> "Corra"
            StepType.WALK -> "Caminhe"
            StepType.WARMUP -> "Aquecimento"
            StepType.COOLDOWN -> "Desaquecimento"
        }
        val intensityText = if (step.intensidade.isNotBlank()) " (${step.intensidade})" else ""
        val distText = step.distanceMeters?.let { " ${it} metros" } ?: ""
        val timeText = if (!step.isDistanceBased) " ${step.durationSeconds / 60} min" else ""
        return "$base$intensityText$distText$timeText".trim()
    }
}

/**
 * Tipos de sessão para organização e regras de distribuição.
 */
enum class SessionType {
    LEVE,           // Rodagem leve, recuperação ativa
    INTERVALADO,    // Séries de velocidade/ritmo
    LONGO,          // Corrida longa de endurance
    PROVA,          // Simulação de prova / dia da prova
    FORCA,          // Treino de força (orientação textual, sem rastreio GPS)
    EDUCATIVOS      // Exercícios educativos/drills
}

/**
 * Resultado da geração de um plano a partir de um template.
 */
data class GeneratedPlanFromTemplate(
    val templateId: String,
    val modalidade: String,
    val dataInicio: java.time.LocalDate,
    val dataFim: java.time.LocalDate,
    val totalSemanas: Int,
    val diasPorSemana: Int,
    val diasSelecionados: List<DayOfWeek>,
    val treinos: List<GeneratedWorkoutFromTemplate>
)

/**
 * Treino individual gerado a partir do template.
 */
data class GeneratedWorkoutFromTemplate(
    val semanaIndex: Int,             // 1..totalSemanas
    val diaDaSemana: DayOfWeek,
    val data: java.time.LocalDate,
    val sessaoTemplateId: String,
    val titulo: String,               // ex: "Semana 3 • Terça - Leve Alternado"
    val resumoSessao: String,
    val steps: List<WorkoutStep>,
    val duracaoTotalMinutos: Int,
    val duracaoCorridaMinutos: Int,
    val distanciaAlvoM: Int?,         // null se por tempo
    val modoConclusao: String,        // "tempo" ou "distancia"
    val tipoSessao: SessionType,
    val intensidadeDescricao: String
)