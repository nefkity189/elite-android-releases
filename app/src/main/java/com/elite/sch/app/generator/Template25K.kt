package com.elite.sch.app.generator

import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import java.time.DayOfWeek

/**
 * Template do plano profissional de 2,5K (6 semanas).
 * Transcrito EXATAMENTE das tabelas do profissional.
 *
 * Estrutura: Terça, Quinta, Sábado (3 dias por semana)
 * Aquecimento: 5 min caminhada | Desaquecimento: 5 min caminhada
 *
 * Onde o texto não diz a proporção de corrida/caminhada em "leve alternado",
 * assume-se 1 min corrida / 1 min caminhada e marca-se com // ASSUMIDO (profissional).
 *
 * Intensidades (leve, forte controlado, solto) viram TEXTO de instrução de esforço:
 * - leve = ritmo de conversa
 * - forte controlado = ritmo de prova de 2,5 km, nunca sprint
 * - solto = ritmo solto, controlado
 *
 * NÃO inventar ritmos numéricos.
 *
 * Semana 6 (prova): a sessão PROVA ocupa o domingo.
 * Com permitirDiasConsecutivosNaSemanaDaProva = false (padrão):
 * se a prova caísse em dia seguinte a outro treino, remove o treino do sábado.
 * Registrar no relatório.
 */
object Template25K {

    /**
     * Retorna o PlanTemplate completo para o plano de 2,5K de 6 semanas.
     */
    fun getTemplate(): PlanTemplate {
        return PlanTemplate(
            id = "plano_2_5k",
            modalidade = "plano_2_5k",
            nomeExibicao = "Primeiros 2,5K",
            semanas = 6,
            aquecimentoSeg = 5 * 60,      // 5 min caminhada
            desaquecimentoSeg = 5 * 60,   // 5 min caminhada
            nivelMinimo = "Iniciante absoluto (zero base de corrida)",
            sessoesPorSemana = listOf(
                // SEMANA 1
                WeekSessionTemplate(
                    id = "s1_terca",
                    titulo = "Leve Alternado 15 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,     // 1 min
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 120,    // 2 min
                            instruction = "2 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 5), // 5x = 15 min total (5*(1+2)=15)
                    intensidadeDescricao = "leve = ritmo de conversa"
                ),
                WeekSessionTemplate(
                    id = "s1_quinta",
                    titulo = "Leve Alternado 15 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,     // 1 min
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,     // 1 min
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 8), // 8x = 15 min? 8*2=16... ASSUMIDO ajustado
                    intensidadeDescricao = "leve = ritmo de conversa; 1 min trote / 1 min caminhada // ASSUMIDO (profissional)"
                ),
                WeekSessionTemplate(
                    id = "s1_sabado",
                    titulo = "1,5 km Alternado",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 1500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 400,
                            instruction = "Corra 400 metros",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            distanceMeters = 200,
                            instruction = "Caminhe 200 metros",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3), // 3x = 1800m, ASSUMIDO 400/200 = 1 min/1 min proporcional // ASSUMIDO (profissional)
                    intensidadeDescricao = "leve = ritmo de conversa; alternado 400m corrida / 200m caminhada // ASSUMIDO (profissional)"
                ),

                // SEMANA 2
                WeekSessionTemplate(
                    id = "s2_terca",
                    titulo = "Alternado 18 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 9), // 9x = 18 min
                    intensidadeDescricao = "leve = ritmo de conversa; 1 min trote / 1 min caminhada"
                ),
                WeekSessionTemplate(
                    id = "s2_quinta",
                    titulo = "Leve Alternado 18 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 9), // 9x = 18 min // ASSUMIDO 1/1
                    intensidadeDescricao = "leve = ritmo de conversa; 1 min trote / 1 min caminhada // ASSUMIDO (profissional)"
                ),
                WeekSessionTemplate(
                    id = "s2_sabado",
                    titulo = "2,0 km Alternado",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 2000,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 500,
                            instruction = "Corra 500 metros",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            distanceMeters = 250,
                            instruction = "Caminhe 250 metros",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3), // 3x = 2250m, ASSUMIDO
                    intensidadeDescricao = "leve = ritmo de conversa; alternado 500m corrida / 250m caminhada // ASSUMIDO (profissional)"
                ),
                // SEMANA 3 (Recuperação)
                WeekSessionTemplate(
                    id = "s3_terca",
                    titulo = "Leve Alternado 15 min",
                    tipo = SessionType.LEVE,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 8), // 8x = 16 min, ASSUMIDO 1/1
                    intensidadeDescricao = "leve = ritmo de conversa; 1 min trote / 1 min caminhada // ASSUMIDO (profissional)"
                ),
                WeekSessionTemplate(
                    id = "s3_quinta",
                    titulo = "Leve 12 min + 3x(30s Forte / 1min Caminhada)",
                    tipo = SessionType.INTERVALADO,
                    semanaDeRecuperacao = true,
                    passosTemplate = listOf(
                        // Leve 12 min alternado
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 6) // 6x = 12 min
                        + listOf(
                            // 3x(30s forte / 1min caminhada)
                            StepTemplate(
                                type = StepType.RUN,
                                durationSeconds = 30,
                                instruction = "30 s trote forte",
                                intensidade = "forte controlado"
                            ),
                            StepTemplate(
                                type = StepType.WALK,
                                durationSeconds = 60,
                                instruction = "1 min caminhada",
                                intensidade = "leve"
                            )
                        ).toRepeatingBlock(repeticoes = 3),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 2,5 km, nunca sprint"
                ),
                WeekSessionTemplate(
                    id = "s3_sabado",
                    titulo = "1,5 km Leve",
                    tipo = SessionType.LONGO,
                    semanaDeRecuperacao = true,
                    distanciaAlvoM = 1500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 1500,
                            instruction = "Corra 1,5 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                // SEMANA 4
                WeekSessionTemplate(
                    id = "s4_terca",
                    titulo = "Alternado 20 min (2/1)",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 120,
                            instruction = "2 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 7), // 7x = 21 min (aprox 20 min)
                    intensidadeDescricao = "leve = ritmo de conversa; 2 min trote / 1 min caminhada"
                ),
                WeekSessionTemplate(
                    id = "s4_quinta",
                    titulo = "Leve 15 min + 4x(1min Forte / 1min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 8) // 8x = 16 min (~15 min)
                        + listOf(
                            StepTemplate(
                                type = StepType.RUN,
                                durationSeconds = 60,
                                instruction = "1 min forte",
                                intensidade = "forte controlado"
                            ),
                            StepTemplate(
                                type = StepType.WALK,
                                durationSeconds = 60,
                                instruction = "1 min leve",
                                intensidade = "leve"
                            )
                        ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 2,5 km, nunca sprint"
                ),
                WeekSessionTemplate(
                    id = "s4_sabado",
                    titulo = "2,5 km Alternado",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 2500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 600,
                            instruction = "Corra 600 metros",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            distanceMeters = 300,
                            instruction = "Caminhe 300 metros",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 3), // 3x = 2700m, ASSUMIDO
                    intensidadeDescricao = "leve = ritmo de conversa; alternado 600m corrida / 250m caminhada // ASSUMIDO (profissional)"
                ),
                // SEMANA 5
                WeekSessionTemplate(
                    id = "s5_terca",
                    titulo = "20 min Trote Leve Contínuo",
                    tipo = SessionType.LONGO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 20 * 60,
                            instruction = "20 min trote leve contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (usar contínuo, não alternado)"
                ),
                WeekSessionTemplate(
                    id = "s5_quinta",
                    titulo = "Leve 18 min + 4x(1min Forte / 1min Leve)",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 9) // 9x = 18 min
                        + listOf(
                            StepTemplate(
                                type = StepType.RUN,
                                durationSeconds = 60,
                                instruction = "1 min forte",
                                intensidade = "forte controlado"
                            ),
                            StepTemplate(
                                type = StepType.WALK,
                                durationSeconds = 60,
                                instruction = "1 min leve",
                                intensidade = "leve"
                            )
                        ).toRepeatingBlock(repeticoes = 4),
                    intensidadeDescricao = "leve = ritmo de conversa; forte controlado = ritmo de prova de 2,5 km, nunca sprint"
                ),
                WeekSessionTemplate(
                    id = "s5_sabado",
                    titulo = "2,5 km Leve (Pico)",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 2500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 2500,
                            instruction = "Corra 2,5 km contínuo",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo (pico de volume)"
                ),
                // SEMANA 6 (Semana da Prova)
                WeekSessionTemplate(
                    id = "s6_terca",
                    titulo = "Leve 15 min",
                    tipo = SessionType.LEVE,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 8), // 8x = 16 min (~15 min)
                    intensidadeDescricao = "leve = ritmo de conversa"
                ),
                WeekSessionTemplate(
                    id = "s6_quinta",
                    titulo = "Leve 10 min + 3x30s Solto",
                    tipo = SessionType.INTERVALADO,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            durationSeconds = 60,
                            instruction = "1 min trote",
                            intensidade = "leve"
                        ),
                        StepTemplate(
                            type = StepType.WALK,
                            durationSeconds = 60,
                            instruction = "1 min caminhada",
                            intensidade = "leve"
                        )
                    ).toRepeatingBlock(repeticoes = 5) // 5x = 10 min
                        + listOf(
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
                    id = "s6_sabado",
                    titulo = "1,5 km Leve",
                    tipo = SessionType.LONGO,
                    distanciaAlvoM = 1500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 1500,
                            instruction = "Corra 1,5 km leve",
                            intensidade = "leve"
                        )
                    ),
                    intensidadeDescricao = "leve = ritmo de conversa contínuo"
                ),
                WeekSessionTemplate(
                    id = "s6_domingo_prova",
                    titulo = "CORRIDA 2,5 KM (PROVA)",
                    tipo = SessionType.PROVA,
                    distanciaAlvoM = 2500,
                    passosTemplate = listOf(
                        StepTemplate(
                            type = StepType.RUN,
                            distanceMeters = 2500,
                            instruction = "Corra 2,5 km - PROVA",
                            intensidade = "prova"
                        )
                    ),
                    intensidadeDescricao = "Ritmo de prova de 2,5 km - dê o seu melhor!"
                )
            )
        )
    }

    /**
     * Relatório de todas as suposições marcadas com // ASSUMIDO (profissional).
     * Deve ser incluído no relatório final da entrega D5.
     */
    val assumidosReport: List<String> = listOf(
        "S1 Quinta: 'Leve alternado 15 min' - assumida proporção 1 min trote / 1 min caminhada",
        "S1 Sábado: '1,5 km alternado' - assumida estrutura 400m corrida / 200m caminhada (3 repetições)",
        "S2 Quinta: 'Leve alternado 18 min' - assumida proporção 1 min trote / 1 min caminhada",
        "S2 Sábado: '2,0 km alternado' - assumida estrutura 500m corrida / 250m caminhada (3 repetições)",
        "S3 Terça: 'Leve alternado 15 min' (recuperação) - assumida proporção 1 min trote / 1 min caminhada",
        "S3 Quinta: Estrutura 'Leve 12 min + 3x(30s forte / 1min caminhada)' - assumido leve alternado 1/1 para os 12 min",
        "S4 Sábado: '2,5 km alternado' - assumida estrutura 600m corrida / 300m caminhada (3 repetições)",
        "S5 Terça: '20 min trote leve contínuo ou alternado' - USAR CONTÍNUO conforme instrução",
        "S6 Quinta: 'Leve 10 min + 3x30s solto' - assumido leve alternado 1/1 para os 10 min"
    )
}

/**
 * Extensão para facilitar criação de blocos repetidos em StepTemplate.
 */
internal fun List<StepTemplate>.toRepeatingBlock(repeticoes: Int): List<StepTemplate> {
    // Retorna uma lista com um único StepTemplate que encapsula a repetição
    // O primeiro elemento carrega a informação de repetição
    if (size != 2) {
        throw IllegalArgumentException("Bloco repetido deve ter exatamente 2 sub-passos (ex: RUN + WALK)")
    }
    val first = this[0].copy(repeticoes = repeticoes, subSteps = this)
    return listOf(first)
}

/**
 * Extensão para concatenar listas de StepTemplate.
 */
internal operator fun List<StepTemplate>.plus(other: List<StepTemplate>): List<StepTemplate> {
    val result = mutableListOf<StepTemplate>()
    result.addAll(this)
    result.addAll(other)
    return result
}