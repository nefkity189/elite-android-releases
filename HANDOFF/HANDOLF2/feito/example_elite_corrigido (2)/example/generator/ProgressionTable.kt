package com.example.generator

import com.example.model.StageProgression
import com.example.model.StepBlock
import com.example.model.StepType
import com.example.model.WorkoutStep

/**
 * Tabela de progressão provisória para o plano Primeiro 5K.
 *
 * NOTA IMPORTANTE: Estas 9 etapas são provisórias e devem ser validadas
 * por um profissional de educação física ou médico esportivo.
 */
object ProgressionTable {

    const val WARMUP_DURATION_SECONDS = 5 * 60 // 5 min de caminhada
    const val COOLDOWN_DURATION_SECONDS = 5 * 60 // 5 min de caminhada

    val stages: Map<Int, StageProgression> = mapOf(
        1 to StageProgression(
            stageNumber = 1,
            title = "Etapa 1: Primeiros passos intercalados",
            summary = "8x (1 min corrida + 1 min 30 s caminhada)",
            repetitions = 8,
            runSeconds = 60,
            walkSeconds = 90
        ),
        2 to StageProgression(
            stageNumber = 2,
            title = "Etapa 2: Intervalos de 2 minutos",
            summary = "6x (2 min corrida + 2 min caminhada)",
            repetitions = 6,
            runSeconds = 120,
            walkSeconds = 120
        ),
        3 to StageProgression(
            stageNumber = 3,
            title = "Etapa 3: Estendendo o tempo de corrida",
            summary = "5x (3 min corrida + 2 min caminhada)",
            repetitions = 5,
            runSeconds = 180,
            walkSeconds = 120
        ),
        4 to StageProgression(
            stageNumber = 4,
            title = "Etapa 4: Correndo 4 minutos",
            summary = "4x (4 min corrida + 2 min caminhada)",
            repetitions = 4,
            runSeconds = 240,
            walkSeconds = 120
        ),
        5 to StageProgression(
            stageNumber = 5,
            title = "Etapa 5: Séries de 6 minutos",
            summary = "3x (6 min corrida + 2 min caminhada)",
            repetitions = 3,
            runSeconds = 360,
            walkSeconds = 120
        ),
        6 to StageProgression(
            stageNumber = 6,
            title = "Etapa 6: Dois blocos de 10 minutos",
            summary = "2x (10 min corrida + 3 min caminhada)",
            repetitions = 2,
            runSeconds = 600,
            walkSeconds = 180
        ),
        7 to StageProgression(
            stageNumber = 7,
            title = "Etapa 7: Blocos longos com pausa central",
            summary = "15 min correndo, 3 min caminhando, 10 min correndo",
            customBlocks = listOf(
                StepBlock(StepType.RUN, 15 * 60, "15 min correndo em ritmo constante"),
                StepBlock(StepType.WALK, 3 * 60, "3 min caminhando para recuperação"),
                StepBlock(StepType.RUN, 10 * 60, "10 min correndo firmemente")
            )
        ),
        8 to StageProgression(
            stageNumber = 8,
            title = "Etapa 8: Corrida contínua de 25 minutos",
            summary = "25 min correndo contínuos",
            customBlocks = listOf(
                StepBlock(StepType.RUN, 25 * 60, "25 min correndo sem interrupções")
            )
        ),
        9 to StageProgression(
            stageNumber = 9,
            title = "Etapa 9: Rumo aos 5K (30 minutos contínuos)",
            summary = "30 min correndo contínuos",
            customBlocks = listOf(
                StepBlock(StepType.RUN, 30 * 60, "30 min correndo contínuos - Você está pronto para tentar os 5K!")
            )
        )
    )

    fun getStage(stageNumber: Int): StageProgression {
        return stages[stageNumber.coerceIn(1, 9)] ?: stages.getValue(1)
    }

    /**
     * Monta a lista completa de passos para uma etapa:
     * 1. Aquecimento: 5 min caminhando
     * 2. Intervalos de corrida e caminhada da etapa
     * 3. Desaquecimento: 5 min caminhando
     */
    fun buildStepsForStage(stageNumber: Int): List<WorkoutStep> {
        val stage = getStage(stageNumber)
        val steps = mutableListOf<WorkoutStep>()

        // 1. Aquecimento obrigatório (5 min caminhando)
        steps.add(
            WorkoutStep(
                type = StepType.WARMUP,
                durationSeconds = WARMUP_DURATION_SECONDS,
                instruction = "Aquecimento: caminhada em ritmo confortável (5 min)"
            )
        )

        // 2. Blocos de corrida e caminhada
        if (stage.customBlocks != null) {
            stage.customBlocks.forEach { block ->
                steps.add(
                    WorkoutStep(
                        type = block.type,
                        durationSeconds = block.durationSeconds,
                        instruction = block.instruction
                    )
                )
            }
        } else {
            for (i in 1..stage.repetitions) {
                steps.add(
                    WorkoutStep(
                        type = StepType.RUN,
                        durationSeconds = stage.runSeconds,
                        instruction = "Série $i/${stage.repetitions}: ${formatMinutesSeconds(stage.runSeconds)} correndo"
                    )
                )
                steps.add(
                    WorkoutStep(
                        type = StepType.WALK,
                        durationSeconds = stage.walkSeconds,
                        instruction = "Série $i/${stage.repetitions}: ${formatMinutesSeconds(stage.walkSeconds)} caminhando"
                    )
                )
            }
        }

        // 3. Desaquecimento obrigatório (5 min caminhando)
        steps.add(
            WorkoutStep(
                type = StepType.COOLDOWN,
                durationSeconds = COOLDOWN_DURATION_SECONDS,
                instruction = "Desaquecimento: caminhada leve e respiração profunda (5 min)"
            )
        )

        return steps
    }

    private fun formatMinutesSeconds(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return when {
            m > 0 && s > 0 -> "$m min $s s"
            m > 0 -> "$m min"
            else -> "$s s"
        }
    }
}
