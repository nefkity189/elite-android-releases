package com.example.generator

import com.example.model.StepType
import com.example.model.WorkoutStep
import java.time.DayOfWeek

/**
 * Template do plano profissional de 10K (12 semanas).
 * Transcrito EXATAMENTE das tabelas do profissional.
 *
 * Estrutura: Terça, Quinta, Sábado (3 dias por semana)
 * Aquecimento: 5 min caminhada | Desaquecimento: 5 min caminhada
 *
 * Inclui treinos intervalados, longos e ritmo de prova.
 * Requer base de 20-30 min contínuos ou conclusão do plano 5K.
 *
 * Semana 12 (prova): a sessão PROVA ocupa o domingo.
 * Com permitirDiasConsecutivosNaSemanaDaProva = false (padrão):
 * se a prova caísse em dia seguinte a outro treino, remove o treino do sábado.
 * Registrar no relatório.
 */
object Template10K {

    /**
     * Retorna o PlanTemplate completo para o plano de 10K de 12 semanas.
     */
    fun getTemplate(): PlanTemplate {
        return PlanTemplate(
            id = "plano_10k",
            modalidade = "plano_10k",
            nomeExibicao = "Primeiros 10K",
            semanas = 12,
            aquecimentoSeg = 5 * 60,      // 5 min caminhada
            desaquecimentoSeg = 5 * 60,   // 5 min caminhada
            nivelMinimo = "Base de 20-30 min contínuos ou plano 5K concluído",
            sessoesPorSemana = listOf(
                // SEMANA 1
                WeekSessionTemplate(
                    id = "s1_terca",
                    titulo = "Leve 30 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30 * 60,
                            instruction = "30 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s1_quinta",
                    titulo = "Intervalado 4x(3min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 3 * 60,
                            instruction = "3 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s1_sabado",
                    titulo = "Longo 5 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 5000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 5000,
                            instruction = "Corra 5 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 2
                WeekSessionTemplate(
                    id = "s2_terca",
                    titulo = "Leve 35 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 35 * 60,
                            instruction = "35 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s2_quinta",
                    titulo = "Intervalado 5x(3min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 3 * 60,
                            instruction = "3 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 5),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s2_sabado",
                    titulo = "Longo 6 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 6000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 6000,
                            instruction = "Corra 6 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 3 (Recuperação)
                WeekSessionTemplate(
                    id = "s3_terca",
                    titulo = "Leve 25 min",
                    tipo = SessionType.LEVE,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 25 * 60,
                            instruction = "25 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (recuperação)"
                ),
                WeekSessionTemplate(
                    id = "s3_quinta",
                    titulo = "Fartlek 30 min (10x 1min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 10),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s3_sabado",
                    titulo = "Longo 5 km Leve",
                    tipo = SessionType.LONGO,
                    semanaDeRecuperacao = true,
                    distanciaAlvoM = 5000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 5000,
                            instruction = "Corra 5 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 4
                WeekSessionTemplate(
                    id = "s4_terca",
                    titulo = "Leve 40 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 40 * 60,
                            instruction = "40 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s4_quinta",
                    titulo = "Intervalado 3x(5min Forte / 3min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 5 * 60,
                            instruction = "5 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 3 * 60,
                            instruction = "3 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s4_sabado",
                    titulo = "Longo 7 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 7000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 7000,
                            instruction = "Corra 7 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 5
                WeekSessionTemplate(
                    id = "s5_terca",
                    titulo = "Progressivo 40 min (20 Leve + 15 Ritmo + 5 Forte)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min leve",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 15 * 60,
                            instruction = "15 min ritmo de prova 10K",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 5 * 60,
                            instruction = "5 min forte",
                            intensidade = "forte controlado"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 10 km"
                ),
                WeekSessionTemplate(
                    id = "s5_quinta",
                    titulo = "Intervalado 4x(4min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 4 * 60,
                            instruction = "4 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s5_sabado",
                    titulo = "Longo 8 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 8000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 8000,
                            instruction = "Corra 8 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 6
                WeekSessionTemplate(
                    id = "s6_terca",
                    titulo = "Leve 45 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 45 * 60,
                            instruction = "45 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s6_quinta",
                    titulo = "Intervalado 5x(4min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 4 * 60,
                            instruction = "4 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 5),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s6_sabado",
                    titulo = "Longo 9 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 9000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 9000,
                            instruction = "Corra 9 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 7 (Recuperação)
                WeekSessionTemplate(
                    id = "s7_terca",
                    titulo = "Leve 30 min",
                    tipo = SessionType.LEVE,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30 * 60,
                            instruction = "30 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (recuperação)"
                ),
                WeekSessionTemplate(
                    id = "s7_quinta",
                    titulo = "Fartlek 30 min (8x 1min Forte / 2min Leve)",
                    tipo = SessionType.INTERVALADO,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 8),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s7_sabado",
                    titulo = "Longo 6 km Leve",
                    tipo = SessionType.LONGO,
                    semanaDeRecuperacao = true,
                    distanciaAlvoM = 6000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 6000,
                            instruction = "Corra 6 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 8
                WeekSessionTemplate(
                    id = "s8_terca",
                    titulo = "Progressivo 45 min (20 Leve + 20 Ritmo + 5 Forte)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min leve",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min ritmo de prova 10K",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 5 * 60,
                            instruction = "5 min forte",
                            intensidade = "forte controlado"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 10 km"
                ),
                WeekSessionTemplate(
                    id = "s8_quinta",
                    titulo = "Intervalado 4x(5min Forte / 3min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 5 * 60,
                            instruction = "5 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 3 * 60,
                            instruction = "3 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s8_sabado",
                    titulo = "Longo 10 km Leve (Pico de Volume)",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 10000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 10000,
                            instruction = "Corra 10 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (pico de volume)"
                ),

                // SEMANA 9
                WeekSessionTemplate(
                    id = "s9_terca",
                    titulo = "Leve 40 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 40 * 60,
                            instruction = "40 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s9_quinta",
                    titulo = "Intervalado 3x(6min Forte / 3min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 6 * 60,
                            instruction = "6 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 3 * 60,
                            instruction = "3 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s9_sabado",
                    titulo = "Longo 8 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 8000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 8000,
                            instruction = "Corra 8 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 10 (Recuperação)
                WeekSessionTemplate(
                    id = "s10_terca",
                    titulo = "Leve 30 min",
                    tipo = SessionType.LEVE,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30 * 60,
                            instruction = "30 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (recuperação)"
                ),
                WeekSessionTemplate(
                    id = "s10_quinta",
                    titulo = "Intervalado 3x(3min Forte / 2min Leve) - Ajuste de Ritmo",
                    tipo = SessionType.INTERVALADO,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 3 * 60,
                            instruction = "3 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 2 * 60,
                            instruction = "2 min leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "forte controlado = ritmo de prova de 10 km; leve = recuperação ativa"
                ),
                WeekSessionTemplate(
                    id = "s10_sabado",
                    titulo = "Longo 6 km Leve",
                    tipo = SessionType.LONGO,
                    semanaDeRecuperacao = true,
                    distanciaAlvoM = 6000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 6000,
                            instruction = "Corra 6 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 11 (Tapering)
                WeekSessionTemplate(
                    id = "s11_terca",
                    titulo = "Leve 25 min + 4x30s Solto",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 25 * 60,
                            instruction = "25 min trote leve",
                            intensidade = "leve"
                        )
                    ) + listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30,
                            instruction = "30 s solto",
                            intensidade = "solto"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "leve = ritmo de conversa; solto = ritmo solto, controlado"
                ),
                WeekSessionTemplate(
                    id = "s11_quinta",
                    titulo = "Leve 20 min + 3x1min Forte",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min trote leve",
                            intensidade = "leve"
                        )
                    ) + listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min forte",
                            intensidade = "forte controlado"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 90,
                            instruction = "1min30s leve",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 10 km"
                ),
                WeekSessionTemplate(
                    id = "s11_sabado",
                    titulo = "Leve 30 min (Descanso Ativo)",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30 * 60,
                            instruction = "30 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),

                // SEMANA 12 (Semana da Prova)
                WeekSessionTemplate(
                    id = "s12_terca",
                    titulo = "Leve 20 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s12_quinta",
                    titulo = "Leve 15 min + 3x30s Solto",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 15 * 60,
                            instruction = "15 min trote leve",
                            intensidade = "leve"
                        )
                    ) + listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 30,
                            instruction = "30 s solto",
                            intensidade = "solto"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "leve = ritmo de conversa; solto = ritmo solto, controlado"
                ),
                WeekSessionTemplate(
                    id = "s12_sabado",
                    titulo = "Leve 15 min (Descanso Pré-Prova)",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 15 * 60,
                            instruction = "15 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s12_domingo_prova",
                    titulo = "CORRIDA 10 KM (PROVA)",
                    tipo = SessionType.PROVA,
                    distanciaAlvoM = 10000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 10000,
                            instruction = "Corra 10 km - PROVA",
                            intensidade = "prova"
                        )
                    ),
                    intensidadeDescricao = "Ritmo de prova de 10 km - dê o seu melhor!"
                )
            )
        )
    }

    /**
     * Relatório de todas as suposições marcadas com // ASSUMIDO (profissional).
     * Deve ser incluído no relatório final da entrega D5.
     */
    val assumidosReport: List<String> = listOf(
        "S1 Quinta: 'Intervalado 4x(3min Forte / 2min Leve)' - assumida estrutura padrão de intervalados",
        "S2 Quinta: 'Intervalado 5x(3min Forte / 2min Leve)' - progressão padrão",
        "S3 Quinta: 'Fartlek 30 min (10x 1min Forte / 2min Leve)' - assumida estrutura fartlek",
        "S4 Quinta: 'Intervalado 3x(5min Forte / 3min Leve)' - assumida progressão de duração",
        "S5 Terça: 'Progressivo 40 min (20 Leve + 15 Ritmo + 5 Forte)' - assumida estrutura progressiva",
        "S5 Quinta: 'Intervalado 4x(4min Forte / 2min Leve)' - assumida progressão padrão",
        "S6 Quinta: 'Intervalado 5x(4min Forte / 2min Leve)' - assumida progressão padrão",
        "S7 Quinta: 'Fartlek 30 min (8x 1min Forte / 2min Leve)' - assumida estrutura fartlek recuperação",
        "S8 Terça: 'Progressivo 45 min (20 Leve + 20 Ritmo + 5 Forte)' - assumida estrutura progressiva",
        "S8 Quinta: 'Intervalado 4x(5min Forte / 3min Leve)' - assumida progressão padrão",
        "S9 Quinta: 'Intervalado 3x(6min Forte / 3min Leve)' - assumida progressão padrão",
        "S10 Quinta: 'Intervalado 3x(3min Forte / 2min Leve)' - assumida ajuste de ritmo tapering",
        "S11 Terça: 'Leve 25 min + 4x30s Solto' - assumida estrutura tapering",
        "S11 Quinta: 'Leve 20 min + 3x1min Forte' - assumida estrutura tapering",
        "S12 Quinta: 'Leve 15 min + 3x30s Solto' - assumida estrutura tapering final"
    )
}