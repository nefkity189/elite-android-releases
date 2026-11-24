package com.example.model

data class StepBlock(
    val type: StepType,
    val durationSeconds: Int,
    val instruction: String
)

data class StageProgression(
    val stageNumber: Int,
    val title: String,
    val summary: String,
    val repetitions: Int = 1,
    val runSeconds: Int = 0,
    val walkSeconds: Int = 0,
    val customBlocks: List<StepBlock>? = null
)
