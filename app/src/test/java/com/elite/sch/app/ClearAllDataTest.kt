package com.elite.sch.app

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import com.elite.sch.app.data.local.AppDatabase
import com.elite.sch.app.data.local.PlanRepository
import com.elite.sch.app.data.local.entities.AutoAdjustmentEntity
import com.elite.sch.app.data.local.entities.CheckinEntity
import com.elite.sch.app.data.local.entities.PlanEntity
import com.elite.sch.app.data.local.entities.RoutePointEntity
import com.elite.sch.app.data.local.entities.RunnerProfileEntity
import com.elite.sch.app.data.local.entities.WorkoutEntity
import com.elite.sch.app.data.local.entities.WorkoutLogEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ClearAllDataTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: PlanRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = PlanRepository(
            database = database,
            modalidade = "plano_5k",
            profileDao = database.runnerProfileDao(),
            planDao = database.planDao(),
            workoutDao = database.workoutDao(),
            routePointDao = database.routePointDao(),
            workoutLogDao = database.workoutLogDao(),
            activeRunDao = database.activeRunDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun getRowCount(tableName: String): Long {
        val cursor = database.query(SimpleSQLiteQuery("SELECT COUNT(*) FROM $tableName"))
        return if (cursor.moveToFirst()) cursor.getLong(0) else 0L
    }

    @Test
    fun `clearAllData removes all rows from profile, plan, workout, workout_log and route_point`() = runBlocking {
        // 1. Inserir Perfil
        val profile = RunnerProfileEntity(
            id = 1,
            nome = "Corredor Teste",
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionadosJson = "[\"MONDAY\",\"WEDNESDAY\",\"FRIDAY\"]",
            dataInicio = "2026-03-01",
            totalSemanas = 8,
            lembretesAtivos = true,
            lembreteHora = 7,
            lembreteMinuto = 30
        )
        database.runnerProfileDao().insertProfile(profile)

        // 2. Inserir Plano
        val plan = PlanEntity(
            id = 1L,
            status = PlanEntity.STATUS_ATIVO,
            dataInicio = "2026-03-01",
            dataFim = "2026-04-26",
            totalSemanas = 8,
            diasPorSemana = 3,
            etapaInicial = 1,
            termoAceito = true,
            modalidade = "plano_5k",
            distanciaAlvoM = 5000
        )
        val planId = database.planDao().insertPlan(plan)

        // 3. Inserir Treinos (com status concluído e planejado)
        val workout1 = WorkoutEntity(
            id = 101L,
            planId = planId,
            semanaIndex = 1,
            treinoIndexNaSemana = 1,
            etapaIndex = 1,
            data = "2026-03-01",
            titulo = "Semana 1 - Treino 1",
            resumoEtapa = "Etapa 1",
            passosJson = "[]",
            duracaoMinutos = 30,
            duracaoCorridaMinutos = 8,
            status = WorkoutEntity.STATUS_CONCLUIDO,
            concluidoEm = System.currentTimeMillis(),
            distanciaAlvoM = null,
            modoConclusao = WorkoutEntity.MODO_TEMPO
        )
        val workout2 = WorkoutEntity(
            id = 102L,
            planId = planId,
            semanaIndex = 1,
            treinoIndexNaSemana = 2,
            etapaIndex = 1,
            data = "2026-03-03",
            titulo = "Semana 1 - Treino 2",
            resumoEtapa = "Etapa 1",
            passosJson = "[]",
            duracaoMinutos = 30,
            duracaoCorridaMinutos = 8,
            status = WorkoutEntity.STATUS_PLANEJADO,
            concluidoEm = null,
            distanciaAlvoM = null,
            modoConclusao = WorkoutEntity.MODO_TEMPO
        )
        database.workoutDao().insertWorkouts(listOf(workout1, workout2))

        // 4. Inserir Corrida finalizada (workout_log)
        database.workoutLogDao().insertLog(
            WorkoutLogEntity(
                workoutId = 101L,
                duracaoSegundos = 1800,
                distanciaMetros = 3200.0,
                ritmoMedioSegundos = 562.5,
                esforcoPercebido = 3,
                finalizadoEm = System.currentTimeMillis(),
                anotacoes = "Primeira corrida muito boa!"
            )
        )

        // 5. Inserir Pontos de Rota (route_point)
        database.routePointDao().insertPoints(
            listOf(
                RoutePointEntity(
                    workoutId = 101L,
                    latitude = -23.5505,
                    longitude = -46.6333,
                    accuracy = 10f,
                    timestampMillis = 1000L
                ),
                RoutePointEntity(
                    workoutId = 101L,
                    latitude = -23.5510,
                    longitude = -46.6340,
                    accuracy = 10f,
                    timestampMillis = 2000L
                )
            )
        )

        // Confirma que foram criados registros em todas as tabelas
        assertTrue("runner_profile deve conter dados", getRowCount("runner_profile") > 0)
        database.checkinDao().insert(CheckinEntity(workoutId = 1L, momento = "pos", dor = 0))
        database.autoAdjustmentDao().insert(
            AutoAdjustmentEntity(planId = 1L, workoutOrigemId = 1L, regra = "DOR_LEVE", antesJson = "[]", depoisJson = "[]")
        )
        assertTrue("checkin deve conter dados", getRowCount("checkin") > 0)
        assertTrue("auto_adjustment deve conter dados", getRowCount("auto_adjustment") > 0)
        assertTrue("plan deve conter dados", getRowCount("plan") > 0)
        assertTrue("workout deve conter dados", getRowCount("workout") > 0)
        assertTrue("workout_log deve conter dados", getRowCount("workout_log") > 0)
        assertTrue("route_point deve conter dados", getRowCount("route_point") > 0)

        // Chama clearAllData()
        repository.clearAllData()

        // Confirma que todas as tabelas foram zeradas
        assertEquals("runner_profile deve estar vazio", 0L, getRowCount("runner_profile"))
        assertEquals("checkin deve estar vazio", 0L, getRowCount("checkin"))
        assertEquals("auto_adjustment deve estar vazio", 0L, getRowCount("auto_adjustment"))
        assertEquals("plan deve estar vazio", 0L, getRowCount("plan"))
        assertEquals("workout deve estar vazio", 0L, getRowCount("workout"))
        assertEquals("workout_log deve estar vazio", 0L, getRowCount("workout_log"))
        assertEquals("route_point deve estar vazio", 0L, getRowCount("route_point"))
    }

    @Test
    fun `novo plano preserva lembretes ativos e horario`() = runBlocking {
        // Perfil existente com lembretes ativos configurados para as 08:30
        val existingProfile = RunnerProfileEntity(
            id = 1,
            nome = "Corredor Antigo",
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionadosJson = "[\"MONDAY\",\"WEDNESDAY\",\"FRIDAY\"]",
            dataInicio = "2026-03-01",
            totalSemanas = 8,
            lembretesAtivos = true,
            lembreteHora = 8,
            lembreteMinuto = 30
        )
        database.runnerProfileDao().insertProfile(existingProfile)

        // Usuário gera novo plano através do fluxo
        val input = com.elite.sch.app.generator.PlanInput(
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "Outros",
            tipoExercicio = "Musculação",
            diasPorSemana = 3,
            diasSelecionados = listOf(java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.WEDNESDAY, java.time.DayOfWeek.FRIDAY),
            dataInicio = java.time.LocalDate.of(2026, 4, 1),
            totalSemanas = 6
        )
        val generatedPlan = com.elite.sch.app.generator.PlanGenerator.generate(input)

        // Novo perfil montado sem lembretes explicitados (padrão desligado/7:00)
        val newProfile = RunnerProfileEntity(
            nome = "Corredor Antigo",
            tempoSemCorrer = "Mais de 6 meses",
            frequenciaExercicio = "Outros",
            tipoExercicio = "Musculação",
            diasPorSemana = 3,
            diasSelecionadosJson = "[\"MONDAY\",\"WEDNESDAY\",\"FRIDAY\"]",
            dataInicio = "2026-04-01",
            totalSemanas = 6,
            lembretesAtivos = false,
            lembreteHora = 7,
            lembreteMinuto = 0,
            ritmoAtualSegKm = null,
            ritmoOrigem = null,
            alertasRitmoAtivos = true
        )

        repository.saveProfileAndGeneratePlan(newProfile, generatedPlan)

        // Perfil resultante deve preservar lembretesAtivos = true, 8:30
        val current = database.runnerProfileDao().getProfile().first()

        assertTrue("Lembretes devem permanecer ativos", current?.lembretesAtivos == true)
        assertEquals("Hora do lembrete deve ser preservada (8)", 8, current?.lembreteHora)
        assertEquals("Minuto do lembrete deve ser preservado (30)", 30, current?.lembreteMinuto)
    }
}