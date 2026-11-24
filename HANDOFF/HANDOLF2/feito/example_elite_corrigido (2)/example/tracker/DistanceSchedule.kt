package com.example.tracker

import com.example.generator.PaceDefaults
import com.example.model.StepType
import com.example.model.WorkoutStep

/**
 * Avalia o progresso de um treino baseado em distância.
 * O índice do passo é SEMPRE derivado da distância acumulada no bloco principal,
 * garantindo sobrevivência a reinícios do serviço (a distância já é salva em active_run).
 *
 * Regras de produto:
 * - A distância do plano (distanciaAlvoM) conta SOMENTE o bloco principal de corrida e caminhada.
 * - Aquecimento e desaquecimento ficam de fora do cálculo da distância alvo.
 * - Os passos avançam pela distância percorrida, não pelo tempo.
 * - Se o usuário andou 200m, o próximo passo de corrida começa ali.
 * - Ao completar a distância do bloco principal: aviso de voz, vibração e destaque na tela.
 * - O treino fecha ao fim do desaquecimento.
 */
object DistanceSchedule {

    /**
     * Fase do treino por distância.
     */
    enum class Phase {
        WARMUP,           // Aquecimento (fora do bloco principal)
        MAIN_BLOCK,       // Bloco principal: soma de RUN + WALK = distanciaAlvoM
        COOLDOWN          // Desaquecimento (fora do bloco principal)
    }

    /**
     * Resultado da avaliação do agendamento por distância.
     */
    data class EvaluationResult(
        val currentStepIndex: Int,
        val currentStep: WorkoutStep?,
        val metersRemainingInStep: Int,
        val metersRemainingInMainBlock: Int,
        val isMainBlockCompleted: Boolean,
        val isFinished: Boolean,
        val currentPhase: Phase,
        val pendingVoiceCue: DistanceVoiceCue?
    )

    /**
     * Avisos de voz específicos para treinos por distância.
     */
    sealed interface DistanceVoiceCue {
        data class StartStep(val message: String, val isRun: Boolean) : DistanceVoiceCue
        data class MainBlockCompleted(val totalKm: Int, val cooldownMinutes: Int) : DistanceVoiceCue
        data object WorkoutCompleted : DistanceVoiceCue
        data class GpsLostFallback(val estimatedMinutesRemaining: Int) : DistanceVoiceCue
        data object GpsRestored : DistanceVoiceCue
    }

    /**
     * Avalia o estado atual do treino por distância.
     *
     * @param steps Lista completa de passos do treino (inclui WARMUP, RUN/WALK do bloco principal, COOLDOWN)
     * @param totalDistanceMeters Distância total acumulada no bloco principal (RUN + WALK)
     * @param currentPhase Fase atual do treino
     * @param lastGpsUpdateMillis Timestamp da última atualização de GPS válida (0 se não há GPS)
     * @param useGps Se true, usa GPS; se false, usa fallback de tempo estimado
     * @return EvaluationResult com estado atual e avisos pendentes
     */
    fun evaluate(
        steps: List<WorkoutStep>,
        totalDistanceMeters: Double,
        currentPhase: Phase,
        lastGpsUpdateMillis: Long = 0L,
        useGps: Boolean = true
    ): EvaluationResult {
        if (steps.isEmpty()) {
            return EvaluationResult(
                currentStepIndex = 0,
                currentStep = null,
                metersRemainingInStep = 0,
                metersRemainingInMainBlock = 0,
                isMainBlockCompleted = true,
                isFinished = true,
                currentPhase = Phase.MAIN_BLOCK,
                pendingVoiceCue = null
            )
        }

        // Separa os passos por fase
        val (warmupSteps, mainBlockSteps, cooldownSteps) = splitStepsByPhase(steps)

        // Distância alvo do bloco principal = soma de distanceMeters dos passos RUN + WALK do bloco principal
        val mainBlockTargetMeters = mainBlockSteps
            .filter { it.isDistanceBased }
            .sumOf { it.distanceMeters!! }

        // Se GPS desativado: usa fallback de tempo
        if (!useGps) {
            return fallbackToTimeBased(steps, 0L)
        }

        // Se não há passos por distância no bloco principal
        if (mainBlockTargetMeters <= 0) {
            // Se está na fase MAIN_BLOCK, retorna completado com 0m
            if (currentPhase == Phase.MAIN_BLOCK) {
                return EvaluationResult(
                    currentStepIndex = warmupSteps.size,
                    currentStep = cooldownSteps.firstOrNull(),
                    metersRemainingInStep = 0,
                    metersRemainingInMainBlock = 0,
                    isMainBlockCompleted = true,
                    isFinished = cooldownSteps.isEmpty(),
                    currentPhase = if (cooldownSteps.isNotEmpty()) Phase.COOLDOWN else Phase.MAIN_BLOCK,
                    pendingVoiceCue = if (cooldownSteps.isEmpty()) DistanceVoiceCue.WorkoutCompleted else null
                )
            }
            // Para outras fases (WARMUP, COOLDOWN), usa fallback de tempo
            return fallbackToTimeBased(steps, 0L)
        }

        when (currentPhase) {
            Phase.WARMUP -> {
                // Aquecimento: ainda controlado por tempo, não por distância
                return evaluateWarmup(warmupSteps, totalDistanceMeters, mainBlockTargetMeters)
            }
            Phase.MAIN_BLOCK -> {
                return evaluateMainBlock(
                    mainBlockSteps = mainBlockSteps,
                    cooldownSteps = cooldownSteps,
                    warmupSteps = warmupSteps,
                    totalDistanceMeters = totalDistanceMeters,
                    mainBlockTargetMeters = mainBlockTargetMeters,
                    lastGpsUpdateMillis = lastGpsUpdateMillis,
                    useGps = useGps
                )
            }
            Phase.COOLDOWN -> {
                return evaluateCooldown(cooldownSteps, mainBlockTargetMeters)
            }
        }
    }

    /**
     * Divide os passos em aquecimento, bloco principal e desaquecimento.
     * Assume: WARMUP no início, COOLDOWN no final, resto = bloco principal.
     */
    private fun splitStepsByPhase(
        steps: List<WorkoutStep>
    ): Triple<List<WorkoutStep>, List<WorkoutStep>, List<WorkoutStep>> {
        val warmup = steps.takeWhile { it.type == StepType.WARMUP }
        val afterWarmup = steps.drop(warmup.size)
        val cooldown = afterWarmup.takeLastWhile { it.type == StepType.COOLDOWN }
        val mainBlock = afterWarmup.dropLast(cooldown.size)
        return Triple(warmup, mainBlock, cooldown)
    }

    /**
     * Avaliação do aquecimento (controlado por tempo, não por distância).
     */
    private fun evaluateWarmup(
        warmupSteps: List<WorkoutStep>,
        totalDistanceMeters: Double,
        mainBlockTargetMeters: Int
    ): EvaluationResult {
        if (warmupSteps.isEmpty()) {
            // Sem aquecimento, transiciona direto para bloco principal
            return EvaluationResult(
                currentStepIndex = warmupSteps.size,
                currentStep = null,
                metersRemainingInStep = mainBlockTargetMeters,
                metersRemainingInMainBlock = mainBlockTargetMeters,
                isMainBlockCompleted = false,
                isFinished = false,
                currentPhase = Phase.MAIN_BLOCK,
                pendingVoiceCue = null
            )
        }

        // Aquecimento usa duração estimada (tempo), distância não conta para o bloco principal
        var accumulatedTime = 0L
        for (i in warmupSteps.indices) {
            val step = warmupSteps[i]
            val stepDuration = step.durationSeconds.toLong()
            // Como é por tempo, estimamos progresso proporcional ao tempo decorrido
            // Mas para DistanceSchedule, o aquecimento é "transparente" - não conta distância
            // Retornamos o primeiro passo do aquecimento como atual
            val message = when (step.type) {
                StepType.WARMUP -> "Aquecimento: ${step.instruction}"
                else -> step.instruction
            }
            return EvaluationResult(
                currentStepIndex = i,
                currentStep = step,
                metersRemainingInStep = 0, // aquecimento não tem metros restantes
                metersRemainingInMainBlock = mainBlockTargetMeters,
                isMainBlockCompleted = false,
                isFinished = false,
                currentPhase = Phase.WARMUP,
                pendingVoiceCue = DistanceVoiceCue.StartStep(message, isRun = false)
            )
        }

        // Aquecimento completo, transiciona para bloco principal
        return EvaluationResult(
            currentStepIndex = warmupSteps.size,
            currentStep = null,
            metersRemainingInStep = mainBlockTargetMeters,
            metersRemainingInMainBlock = mainBlockTargetMeters,
            isMainBlockCompleted = false,
            isFinished = false,
            currentPhase = Phase.MAIN_BLOCK,
            pendingVoiceCue = null
        )
    }

    /**
     * Avaliação do bloco principal (controlado por distância).
     * O índice do passo é SEMPRE derivado da distância acumulada.
     */
    private fun evaluateMainBlock(
        mainBlockSteps: List<WorkoutStep>,
        cooldownSteps: List<WorkoutStep>,
        warmupSteps: List<WorkoutStep>,
        totalDistanceMeters: Double,
        mainBlockTargetMeters: Int,
        lastGpsUpdateMillis: Long,
        useGps: Boolean
    ): EvaluationResult {
        // Filtra apenas passos com distância definida (RUN + WALK do bloco principal)
        val distanceSteps = mainBlockSteps.filter { it.isDistanceBased }
        val warmupCount = warmupSteps.size

        // Verifica se GPS foi perdido (> 30s sem atualização válida)
        val now = System.currentTimeMillis()
        val gpsLost = useGps && lastGpsUpdateMillis > 0 && (now - lastGpsUpdateMillis) > 30_000L

        // Calcula distância percorrida no bloco principal
        val distanceInMainBlock = totalDistanceMeters.coerceIn(0.0, mainBlockTargetMeters.toDouble())
        val distanceRemaining = (mainBlockTargetMeters - distanceInMainBlock).toInt()

        // Deriva o passo atual BASEADO NA DISTÂNCIA ACUMULADA (não tempo)
        var accumulatedMeters = 0
        for (i in distanceSteps.indices) {
            val step = distanceSteps[i]
            val stepDistance = step.distanceMeters!!
            val stepStart = accumulatedMeters
            val stepEnd = accumulatedMeters + stepDistance

            if (distanceInMainBlock < stepEnd) {
                // Encontrou o passo atual
                val metersRemainingInStep = (stepEnd - distanceInMainBlock).toInt()
                val isFirstMomentInStep = distanceInMainBlock < stepStart + 1.0 // ~1m de tolerância

                val voiceCue = if (isFirstMomentInStep && i < distanceSteps.size) {
                    val message = when (step.type) {
                        StepType.RUN -> "Corra ${stepDistance} metros"
                        StepType.WALK -> "Caminhe ${stepDistance} metros"
                        else -> step.instruction
                    }
                    DistanceVoiceCue.StartStep(message, step.type == StepType.RUN)
                } else if (gpsLost) {
                    DistanceVoiceCue.GpsLostFallback(
                        estimatedMinutesRemaining = estimateRemainingMinutes(
                            distanceRemaining, step.type
                        )
                    )
                } else {
                    null
                }

                // Índice global = aquecimento + índice no bloco principal
                val globalIndex = warmupCount + mainBlockSteps.indexOfFirst { it === step }

                return EvaluationResult(
                    currentStepIndex = globalIndex,
                    currentStep = step,
                    metersRemainingInStep = metersRemainingInStep,
                    metersRemainingInMainBlock = distanceRemaining,
                    isMainBlockCompleted = false,
                    isFinished = false,
                    currentPhase = Phase.MAIN_BLOCK,
                    pendingVoiceCue = voiceCue
                )
            }
            accumulatedMeters = stepEnd
        }

        // Bloco principal completado - transiciona para desaquecimento
        val cooldownMinutes = cooldownSteps
            .filter { it.type == StepType.COOLDOWN }
            .sumOf { it.durationSeconds / 60 }
        val totalKm = mainBlockTargetMeters / 1000

        return EvaluationResult(
            currentStepIndex = warmupCount + distanceSteps.size,
            currentStep = cooldownSteps.firstOrNull(),
            metersRemainingInStep = 0,
            metersRemainingInMainBlock = 0,
            isMainBlockCompleted = true,
            isFinished = false,
            currentPhase = Phase.COOLDOWN,
            pendingVoiceCue = DistanceVoiceCue.MainBlockCompleted(totalKm, cooldownMinutes)
        )
    }

    /**
     * Avaliação do desaquecimento (controlado por tempo).
     */
    private fun evaluateCooldown(
        cooldownSteps: List<WorkoutStep>,
        mainBlockTargetMeters: Int
    ): EvaluationResult {
        if (cooldownSteps.isEmpty()) {
            // Sem desaquecimento, treino finalizado
            return EvaluationResult(
                currentStepIndex = 0,
                currentStep = null,
                metersRemainingInStep = 0,
                metersRemainingInMainBlock = 0,
                isMainBlockCompleted = true,
                isFinished = true,
                currentPhase = Phase.COOLDOWN,
                pendingVoiceCue = DistanceVoiceCue.WorkoutCompleted
            )
        }

        // Desaquecimento usa tempo, mas indicamos que o bloco principal acabou
        val firstCooldown = cooldownSteps.first()
        return EvaluationResult(
            currentStepIndex = 0, // índice será ajustado pelo chamador
            currentStep = firstCooldown,
            metersRemainingInStep = 0,
            metersRemainingInMainBlock = 0,
            isMainBlockCompleted = true,
            isFinished = false,
            currentPhase = Phase.COOLDOWN,
            pendingVoiceCue = null
        )
    }

    /**
     * Fallback para treinos por tempo (modoConclusao = "tempo" ou sem passos por distância).
     */
    private fun fallbackToTimeBased(steps: List<WorkoutStep>, elapsedSeconds: Long): EvaluationResult {
        // Usa o StepScheduler existente para lógica baseada em tempo
        val schedule = StepScheduler.evaluate(steps, elapsedSeconds)
        return EvaluationResult(
            currentStepIndex = schedule.currentStepIndex,
            currentStep = schedule.currentStep,
            metersRemainingInStep = 0,
            metersRemainingInMainBlock = 0,
            isMainBlockCompleted = schedule.isFinished,
            isFinished = schedule.isFinished,
            currentPhase = Phase.MAIN_BLOCK,
            pendingVoiceCue = schedule.pendingVoiceCue?.let { cue ->
                when (cue) {
                    is VoiceCue.StartStep -> DistanceVoiceCue.StartStep(cue.message, cue.isRun)
                    is VoiceCue.Completed -> DistanceVoiceCue.WorkoutCompleted
                    else -> null
                }
            }
        )
    }

    /**
     * Estima minutos restantes baseado no tipo de passo e distância restante.
     * Usa PaceDefaults do StepEstimator.
     */
    private fun estimateRemainingMinutes(distanceRemainingMeters: Int, stepType: StepType): Int {
        val pace = when (stepType) {
            StepType.RUN -> PaceDefaults.corridaSegKm
            StepType.WALK -> PaceDefaults.caminhadaSegKm
            else -> PaceDefaults.corridaSegKm
        }
        val seconds = (distanceRemainingMeters / 1000.0 * pace).toLong()
        return (seconds / 60).toInt().coerceAtLeast(1)
    }

    /**
     * Determina a fase atual baseado nos passos e progresso.
     * Usado para transição automática entre fases.
     */
    fun determinePhase(
        steps: List<WorkoutStep>,
        warmupCompleted: Boolean,
        mainBlockDistanceMeters: Double,
        mainBlockTargetMeters: Int
    ): Phase {
        val (warmupSteps, mainBlockSteps, cooldownSteps) = splitStepsByPhase(steps)

        if (!warmupCompleted && warmupSteps.isNotEmpty()) {
            return Phase.WARMUP
        }

        if (mainBlockDistanceMeters < mainBlockTargetMeters && mainBlockSteps.isNotEmpty()) {
            return Phase.MAIN_BLOCK
        }

        if (cooldownSteps.isNotEmpty()) {
            return Phase.COOLDOWN
        }

        return Phase.MAIN_BLOCK // fallback
    }
}