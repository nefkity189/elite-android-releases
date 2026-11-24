package com.elite.sch.app

import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import com.elite.sch.app.tracker.GeoPoint
import com.elite.sch.app.tracker.GpsFilter
import com.elite.sch.app.tracker.PaceCalculator
import com.elite.sch.app.tracker.StepScheduler
import com.elite.sch.app.tracker.VoiceCue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RunLogicTest {

    @Test
    fun `calculo de distancia por haversine calcula distancia valida`() {
        // Distância conhecida entre Obelisco e MASP em SP (~2.3 km)
        val d = GpsFilter.calculateDistanceMeters(
            -23.5868, -46.6575,
            -23.5614, -46.6559
        )
        assertTrue("Distância deve ser em torno de 2.8 km (atual: $d)", d in 2700.0..2950.0)
    }

    @Test
    fun `ritmo medio so eh mostrado depois de 300 metros e se for mais rapido que 20 min por km`() {
        // Abaixo de 300 metros -> null e "--:--"
        assertNull(PaceCalculator.calculateAveragePaceSeconds(totalSeconds = 60, totalDistanceMeters = 250.0))
        assertEquals("--:--", PaceCalculator.formatPace(null))

        // A partir de 300 metros, mas ritmo >= 20:00 min/km (1200 s/km) -> null e "--:--"
        // 400m em 600s -> 1500 s/km (25:00 min/km -> mais lento que 20:00)
        assertNull(PaceCalculator.calculateAveragePaceSeconds(totalSeconds = 600, totalDistanceMeters = 400.0))
        assertEquals("--:--", PaceCalculator.formatPace(PaceCalculator.calculateAveragePaceSeconds(600, 400.0)))

        // Limite exato de 20:00 min/km (1200 s/km) -> null e "--:--"
        assertNull(PaceCalculator.calculateAveragePaceSeconds(totalSeconds = 480, totalDistanceMeters = 400.0))

        // A partir de 300 metros e mais rápido que 20:00 min/km -> calculado e formatado como "MM:SS"
        // 500m em 150s -> 300s/km (05:00 min/km)
        val pace = PaceCalculator.calculateAveragePaceSeconds(totalSeconds = 150, totalDistanceMeters = 500.0)
        assertNotNull(pace)
        assertEquals(300.0, pace!!, 0.01)
        assertEquals("05:00", PaceCalculator.formatPace(pace))
    }

    @Test
    fun `step scheduler avança passos conforme o tempo decorrido`() {
        val steps = listOf(
            WorkoutStep(StepType.WARMUP, 300, "Aquecimento 5 min"),
            WorkoutStep(StepType.RUN, 60, "Corrida 1 min"),
            WorkoutStep(StepType.WALK, 90, "Caminhada 1:30 min")
        )

        // No segundo 0: início do aquecimento
        val s0 = StepScheduler.evaluate(steps, elapsedSeconds = 0)
        assertEquals(0, s0.currentStepIndex)
        assertEquals(300L, s0.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Caminhe", isRun = false), s0.pendingVoiceCue)

        // No segundo 290: faltam 10 segundos para o aquecimento acabar (passo > 60s)
        val s290 = StepScheduler.evaluate(steps, elapsedSeconds = 290)
        assertEquals(0, s290.currentStepIndex)
        assertEquals(10L, s290.timeRemainingInStep)
        assertEquals(VoiceCue.TenSecondsRemaining, s290.pendingVoiceCue)

        // No segundo 300: início da corrida
        val s300 = StepScheduler.evaluate(steps, elapsedSeconds = 300)
        assertEquals(1, s300.currentStepIndex)
        assertEquals(60L, s300.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Comece a correr", isRun = true), s300.pendingVoiceCue)

        // No segundo 360: início da caminhada
        val s360 = StepScheduler.evaluate(steps, elapsedSeconds = 360)
        assertEquals(2, s360.currentStepIndex)
        assertEquals(90L, s360.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Caminhe", isRun = false), s360.pendingVoiceCue)

        // No segundo 450: treino finalizado (total 300+60+90 = 450s)
        val s450 = StepScheduler.evaluate(steps, elapsedSeconds = 450)
        assertTrue(s450.isFinished)
        assertEquals(VoiceCue.Completed, s450.pendingVoiceCue)
    }

    @Test
    fun `step scheduler dispara aviso de 10 segundos quando tick pula do segundo 11 para o segundo 9 restante`() {
        val steps = listOf(
            WorkoutStep(StepType.WARMUP, 120, "Aquecimento 2 min"),
            WorkoutStep(StepType.RUN, 60, "Corrida 1 min")
        )

        // No segundo 109: faltam 11 segundos -> ainda não deve disparar o aviso
        val s109 = StepScheduler.evaluate(steps, elapsedSeconds = 109)
        assertEquals(11L, s109.timeRemainingInStep)
        assertNull(s109.pendingVoiceCue)

        // No segundo 111 (tick pulou de 109s para 111s): faltam 9 segundos -> deve disparar TenSecondsRemaining
        val s111 = StepScheduler.evaluate(steps, elapsedSeconds = 111)
        assertEquals(9L, s111.timeRemainingInStep)
        assertEquals(VoiceCue.TenSecondsRemaining, s111.pendingVoiceCue)
    }

    @Test
    fun `step scheduler lista timestamps corretos para avisos sonoros`() {
        val steps = listOf(
            WorkoutStep(StepType.WARMUP, 120, "Aquecimento 2 min"),
            WorkoutStep(StepType.RUN, 60, "Corrida 1 min")
        )

        val warnings = StepScheduler.getWarningTimestamps(steps)
        // 0s: Caminhe
        // 110s: Faltam 10 segundos (120s - 10s)
        // 120s: Comece a correr
        // 180s: Treino concluído (120 + 60)
        val expected = listOf(
            0L to "Caminhe",
            110L to "Faltam 10 segundos",
            120L to "Comece a correr",
            180L to "Treino concluído"
        )
        assertEquals(expected, warnings)
    }

    @Test
    fun `step scheduler transicao de aquecimento para corrida e de corrida para caminhada`() {
        val steps = listOf(
            WorkoutStep(StepType.WARMUP, 300, "Aquecimento 5 min"),
            WorkoutStep(StepType.RUN, 60, "Corrida 1 min"),
            WorkoutStep(StepType.WALK, 90, "Caminhada 1:30 min")
        )

        // No segundo 0: início do aquecimento
        val s0 = StepScheduler.evaluate(steps, elapsedSeconds = 0)
        assertEquals(0, s0.currentStepIndex)
        assertEquals(300L, s0.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Caminhe", isRun = false), s0.pendingVoiceCue)

        // No segundo 300: início da corrida
        val s300 = StepScheduler.evaluate(steps, elapsedSeconds = 300)
        assertEquals(1, s300.currentStepIndex)
        assertEquals(60L, s300.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Comece a correr", isRun = true), s300.pendingVoiceCue)

        // No segundo 360: início da caminhada
        val s360 = StepScheduler.evaluate(steps, elapsedSeconds = 360)
        assertEquals(2, s360.currentStepIndex)
        assertEquals(90L, s360.timeRemainingInStep)
        assertEquals(VoiceCue.StartStep("Caminhe", isRun = false), s360.pendingVoiceCue)

        // No segundo 450: treino finalizado (total 300+60+90 = 450s)
        val s450 = StepScheduler.evaluate(steps, elapsedSeconds = 450)
        assertTrue(s450.isFinished)
        assertEquals(VoiceCue.Completed, s450.pendingVoiceCue)
    }

    @Test
    fun `historico de pace mantem os ultimos 30 valores e descarta valores nulos`() {
        val history = com.elite.sch.app.ui.components.PaceHistory()

        // Adiciona 35 valores
        for (i in 1..35) {
            history.addPace(i.toDouble() * 10.0)
        }

        // Deve manter apenas os últimos 30
        assertEquals(30, history.history.size)
        assertEquals(60.0, history.history.first(), 0.01)
        assertEquals(350.0, history.history.last(), 0.01)
    }

    @Test
    fun `historico de pace nao adiciona valores nulos`() {
        val history = com.elite.sch.app.ui.components.PaceHistory()

        history.addPace(300.0)
        history.addPace(null)
        history.addPace(350.0)

        assertEquals(2, history.history.size)
        assertEquals(300.0, history.history[0], 0.01)
        assertEquals(350.0, history.history[1], 0.01)
    }

    @Test
    fun `calculo de marcadores da semana mapeia planejado concluido pulado e descanso corretamente`() {
        val monday = java.time.LocalDate.of(2026, 11, 2)
        val weekDates = (0L..6L).map { monday.plusDays(it) }

        val workouts = listOf(
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 1L,
                planId = 1L,
                semanaIndex = 1,
                treinoIndexNaSemana = 1,
                etapaIndex = 1,
                data = weekDates[0].toString(), // Segunda: Concluído -> FILLED_CIRCLE
                titulo = "Treino 1",
                resumoEtapa = "Caminhada e corrida",
                passosJson = "[]",
                duracaoMinutos = 20,
                duracaoCorridaMinutos = 5,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_CONCLUIDO
            ),
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 2L,
                planId = 1L,
                semanaIndex = 1,
                treinoIndexNaSemana = 2,
                etapaIndex = 1,
                data = weekDates[2].toString(), // Quarta: Pulado -> DASH
                titulo = "Treino 2",
                resumoEtapa = "Caminhada e corrida",
                passosJson = "[]",
                duracaoMinutos = 20,
                duracaoCorridaMinutos = 5,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_PULADO
            ),
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 3L,
                planId = 1L,
                semanaIndex = 1,
                treinoIndexNaSemana = 3,
                etapaIndex = 1,
                data = weekDates[4].toString(), // Sexta: Planejado -> EMPTY_CIRCLE
                titulo = "Treino 3",
                resumoEtapa = "Caminhada e corrida",
                passosJson = "[]",
                duracaoMinutos = 20,
                duracaoCorridaMinutos = 5,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_PLANEJADO
            )
        )

        val markers = com.elite.sch.app.tracker.WeekMarkerCalculator.calculateWeekMarkers(
            weekDates = weekDates,
            workouts = workouts,
            currentDate = weekDates[0] // Segunda-feira
        )

        assertEquals(7, markers.size)
        // Seg: Concluído
        assertEquals(weekDates[0], markers[0].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.FILLED_CIRCLE, markers[0].type)
        // Ter: Descanso
        assertEquals(weekDates[1], markers[1].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.NONE, markers[1].type)
        // Qua: Pulado
        assertEquals(weekDates[2], markers[2].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.DASH, markers[2].type)
        // Qui: Descanso
        assertEquals(weekDates[3], markers[3].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.NONE, markers[3].type)
        // Sex: Planejado no futuro -> EMPTY_CIRCLE
        assertEquals(weekDates[4], markers[4].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.EMPTY_CIRCLE, markers[4].type)
        // Sáb: Descanso
        assertEquals(weekDates[5], markers[5].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.NONE, markers[5].type)
        // Dom: Descanso
        assertEquals(weekDates[6], markers[6].date)
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.NONE, markers[6].type)
    }

    @Test
    fun `marcador perdido eh atribuido para dia passado com treino planejado e sem status concluido ou pulado`() {
        val monday = java.time.LocalDate.of(2026, 11, 2)
        val weekDates = (0L..6L).map { monday.plusDays(it) }

        // Suponha que hoje seja Domingo (weekDates[6])
        val currentDate = weekDates[6]

        val workouts = listOf(
            // Treino planejado na Quarta (passado) que nunca foi concluído nem pulado
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 10L,
                planId = 1L,
                semanaIndex = 1,
                treinoIndexNaSemana = 1,
                etapaIndex = 1,
                data = weekDates[2].toString(), // Quarta-feira
                titulo = "Treino 1",
                resumoEtapa = "Caminhada e corrida",
                passosJson = "[]",
                duracaoMinutos = 20,
                duracaoCorridaMinutos = 5,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_PLANEJADO
            )
        )

        val markers = com.elite.sch.app.tracker.WeekMarkerCalculator.calculateWeekMarkers(
            weekDates = weekDates,
            workouts = workouts,
            currentDate = currentDate
        )

        // Quarta-feira (índice 2) era planejado no passado -> deve ser MISSED
        assertEquals(com.elite.sch.app.tracker.DayMarkerType.MISSED, markers[2].type)
    }

    @Test
    fun `normalizacao de rota para Canvas mantem pontos dentro do espaco disponivel preservando proporcao`() {
        // Rota em formato L: começa em (0,0), vai para o norte (0.01, 0), depois vira para o leste (0.01, 0.02)
        val points = listOf(
            com.elite.sch.app.tracker.RoutePointCoord(latitude = -23.5500, longitude = -46.6300),
            com.elite.sch.app.tracker.RoutePointCoord(latitude = -23.5400, longitude = -46.6300),
            com.elite.sch.app.tracker.RoutePointCoord(latitude = -23.5400, longitude = -46.6100)
        )

        val normalized = com.elite.sch.app.tracker.RouteNormalizer.normalize(points)
        assertEquals(3, normalized.size)

        // Todos os pontos normalizados devem estar estritamente no intervalo [0.0, 1.0]
        normalized.forEach { pt ->
            assertTrue("x deve estar em [0, 1], atual: ${pt.x}", pt.x in 0.0f..1.0f)
            assertTrue("y deve estar em [0, 1], atual: ${pt.y}", pt.y in 0.0f..1.0f)
        }

        // Ponto mais ao norte (latitude maior) deve ter menor Y (topo da tela)
        assertTrue("Norte deve estar no topo (y menor)", normalized[1].y < normalized[0].y)
        // Ponto mais a leste (longitude maior) deve ter maior X (direita da tela)
        assertTrue("Leste deve estar à direita (x maior)", normalized[2].x > normalized[1].x)

        // Conversão para pixels de Canvas com dimensões 400x300 e padding 20
        val offsets = com.elite.sch.app.tracker.RouteNormalizer.toCanvasOffsets(
            normalizedPoints = normalized,
            widthPx = 400f,
            heightPx = 300f,
            paddingPx = 20f
        )
        assertEquals(3, offsets.size)
        offsets.forEach { (px, py) ->
            assertTrue("px deve respeitar limites com padding (20..380), atual: $px", px in 20f..380f)
            assertTrue("py deve respeitar limites com padding (20..280), atual: $py", py in 20f..280f)
        }
    }

    @Test
    fun `calculo do resumo da semana atual e da sequencia de semanas seguidas`() {
        val today = java.time.LocalDate.of(2026, 11, 4) // Quarta-feira
        val mondayThisWeek = java.time.LocalDate.of(2026, 11, 2)
        val wednesdayThisWeek = java.time.LocalDate.of(2026, 11, 4)
        val fridayThisWeek = java.time.LocalDate.of(2026, 11, 6)

        val mondayLastWeek = mondayThisWeek.minusWeeks(1)
        val mondayTwoWeeksAgo = mondayThisWeek.minusWeeks(2)

        val workouts = listOf(
            // Duas semanas atrás: 1 concluído
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 1L, planId = 1L, semanaIndex = 1, treinoIndexNaSemana = 1, etapaIndex = 1,
                data = mondayTwoWeeksAgo.toString(), titulo = "W-2", resumoEtapa = "", passosJson = "[]",
                duracaoMinutos = 20, duracaoCorridaMinutos = 5,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_CONCLUIDO
            ),
            // Semana passada: 1 concluído
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 2L, planId = 1L, semanaIndex = 2, treinoIndexNaSemana = 1, etapaIndex = 1,
                data = mondayLastWeek.toString(), titulo = "W-1", resumoEtapa = "", passosJson = "[]",
                duracaoMinutos = 22, duracaoCorridaMinutos = 7,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_CONCLUIDO
            ),
            // Esta semana: Segunda concluído, Quarta concluído, Sexta planejado
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 3L, planId = 1L, semanaIndex = 3, treinoIndexNaSemana = 1, etapaIndex = 1,
                data = mondayThisWeek.toString(), titulo = "W0-T1", resumoEtapa = "", passosJson = "[]",
                duracaoMinutos = 25, duracaoCorridaMinutos = 8,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_CONCLUIDO
            ),
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 4L, planId = 1L, semanaIndex = 3, treinoIndexNaSemana = 2, etapaIndex = 1,
                data = wednesdayThisWeek.toString(), titulo = "W0-T2", resumoEtapa = "", passosJson = "[]",
                duracaoMinutos = 25, duracaoCorridaMinutos = 8,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_CONCLUIDO
            ),
            com.elite.sch.app.data.local.entities.WorkoutEntity(
                id = 5L, planId = 1L, semanaIndex = 3, treinoIndexNaSemana = 3, etapaIndex = 1,
                data = fridayThisWeek.toString(), titulo = "W0-T3", resumoEtapa = "", passosJson = "[]",
                duracaoMinutos = 25, duracaoCorridaMinutos = 8,
                status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_PLANEJADO
            )
        )

        val logs = listOf(
            com.elite.sch.app.data.local.entities.WorkoutLogEntity(
                id = 101L, workoutId = 3L, duracaoSegundos = 1500L, distanciaMetros = 3200.0,
                ritmoMedioSegundos = 468.0, esforcoPercebido = 6
            ),
            com.elite.sch.app.data.local.entities.WorkoutLogEntity(
                id = 102L, workoutId = 4L, duracaoSegundos = 1600L, distanciaMetros = 3400.0,
                ritmoMedioSegundos = 470.0, esforcoPercebido = 7
            )
        )

        // Resumo da semana atual
        val stats = com.elite.sch.app.tracker.ProgressStatsCalculator.calculateCurrentWeekStats(
            workouts = workouts,
            logs = logs,
            currentDate = today
        )
        assertEquals(2, stats.completedWorkoutsCount)
        assertEquals(3, stats.totalWorkoutsPlanned)
        assertEquals(3100L, stats.totalDurationSeconds) // 1500 + 1600
        assertEquals(6600.0, stats.totalDistanceMeters, 0.01) // 3200 + 3400

        // Streak de semanas consecutivas (2 semanas atrás + semana passada + semana atual = 3)
        val streak = com.elite.sch.app.tracker.ProgressStatsCalculator.calculateWeeklyStreak(
            workouts = workouts,
            currentDate = today
        )
        assertEquals(3, streak)
    }

    @Test
    fun `week marker calculator maps status movido to DASH and never to MISSED`() {
        val pastDate = java.time.LocalDate.of(2026, 3, 2)
        val currentDate = java.time.LocalDate.of(2026, 3, 10)

        val workoutMovido = com.elite.sch.app.data.local.entities.WorkoutEntity(
            id = 1L, planId = 1L, semanaIndex = 1, treinoIndexNaSemana = 1, etapaIndex = 1,
            data = pastDate.toString(), titulo = "Treino Movido", resumoEtapa = "", passosJson = "[]",
            duracaoMinutos = 25, duracaoCorridaMinutos = 8,
            status = com.elite.sch.app.data.local.entities.WorkoutEntity.STATUS_MOVIDO
        )

        val markers = com.elite.sch.app.tracker.WeekMarkerCalculator.calculateWeekMarkers(
            weekDates = listOf(pastDate),
            workouts = listOf(workoutMovido),
            currentDate = currentDate
        )

        assertEquals(1, markers.size)
        assertEquals(
            "Status movido no passado deve ser exibido como DASH e nunca como MISSED",
            com.elite.sch.app.tracker.DayMarkerType.DASH,
            markers[0].type
        )
    }
}

