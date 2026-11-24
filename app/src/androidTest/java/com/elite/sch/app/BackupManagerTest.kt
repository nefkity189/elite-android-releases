package com.elite.sch.app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elite.sch.app.backup.BackupManager
import com.elite.sch.app.data.local.AppDatabase
import com.elite.sch.app.data.local.BaseAppDatabase
import com.elite.sch.app.data.local.entities.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Testes para o sistema de backup/restore do aplicativo Elite.
 * Verifica se dados são exportados e restaurados corretamente.
 */
@RunWith(AndroidJUnit4::class)
class BackupManagerTest {

    private lateinit var context: Context
    private lateinit var backupManager: BackupManager
    private val testDbName = "test_backup_db.db"

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        backupManager = BackupManager(context)

        // Limpa banco de teste se existir
        val testDbFile = File(context.filesDir, testDbName)
        if (testDbFile.exists()) {
            testDbFile.delete()
        }
    }

    @After
    fun tearDown() {
        val testDbFile = File(context.filesDir, testDbName)
        if (testDbFile.exists()) {
            testDbFile.delete()
        }
    }

    /**
     * Teste: Exportar perfil do corredor e verificar preservação dos campos.
     */
    @Test
    fun testExportImportRunnerProfile() = runBlocking {
        val db = createTestDatabase()

        val profile = RunnerProfileEntity(
            nome = "João Silva",
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionadosJson = "MONDAY,WEDNESDAY,FRIDAY",
            dataInicio = "2026-11-01",
            totalSemanas = 9,
            vozAtiva = true,
            vibracaoAtiva = true
        )
        db.runnerProfileDao().insertProfileSync(profile)

        // Exporta backup
        var exportedFile: File? = null
        backupManager.exportBackup { result ->
            assertTrue("Exportação deve ter sucesso", result.isSuccess)
            exportedFile = result.getOrNull()
        }

        assertNotNull("Arquivo de backup não foi criado", exportedFile)
        assertTrue("Arquivo deve existir", exportedFile!!.exists())

        // Importa backup
        backupManager.importBackup(exportedFile!!) { result ->
            assertTrue("Importação deve ter sucesso", result.isSuccess)
        }

        // Verifica se dados foram restaurados
        val restoredProfile = db.runnerProfileDao().getProfileSync()
        assertNotNull("Perfil restaurado não deve ser nulo", restoredProfile)
        assertEquals("Nome deve ser preservado", "João Silva", restoredProfile?.nome)
        assertEquals("Total de semanas deve ser preservado", 9, restoredProfile?.totalSemanas)
    }

    /**
     * Teste: Exportar plano completo com treinos e verificar integridade.
     */
    @Test
    fun testExportImportPlanWithWorkouts() = runBlocking {
        val db = createTestDatabase()

        // Insere um plano
        val plan = PlanEntity(
            status = PlanEntity.STATUS_ATIVO,
            dataInicio = "2026-11-01",
            dataFim = "2026-12-31",
            totalSemanas = 6,
            diasPorSemana = 3,
            etapaInicial = 1,
            modalidade = "plano_5k",
            distanciaAlvoM = 5000
        )
        val planId = db.planDao().insertPlan(plan)

        // Insere treinos
        val workout = WorkoutEntity(
            planId = planId,
            semanaIndex = 1,
            treinoIndexNaSemana = 1,
            etapaIndex = 1,
            data = "2026-11-01",
            titulo = "Treino 1",
            resumoEtapa = "Caminhada inicial",
            passosJson = "[{\"tipo\":\"caminhada\",\"duracao\":5}]",
            duracaoMinutos = 20,
            duracaoCorridaMinutos = 5
        )
        db.workoutDao().insertWorkout(workout)

        // Exporta
        var exportedFile: File? = null
        backupManager.exportBackup { result ->
            exportedFile = result.getOrNull()
        }
        assertNotNull(exportedFile)

        // Importa
        backupManager.importBackup(exportedFile!!) { result ->
            assertTrue(result.isSuccess)
        }

        // Verifica planos e treinos
        val plans = db.planDao().getAll()
        val workouts = db.workoutDao().getAll()

        assertTrue("Deve ter pelo menos 1 plano", plans.size >= 1)
        assertTrue("Deve ter pelo menos 1 treino", workouts.size >= 1)
        assertEquals("Titulo deve ser preservado", "Treino 1", workouts[0].titulo)
    }

    /**
     * Teste: Versão incompatível do backup deve falhar na importação.
     */
    @Test
    fun testIncompatibleVersionFails() {
        // Cria arquivo JSON com versão incompatível
        val json = """
            {
                "version": "2.0",
                "exportedAt": 1000,
                "appVersion": "1.0.0",
                "modalities": []
            }
        """.trimIndent()

        val tempFile = File(context.cacheDir, "incompatible_backup.json")
        tempFile.writeText(json)

        // Importação deve falhar
        var failureReceived = false
        backupManager.importBackup(tempFile) { result ->
            failureReceived = true
            assertTrue("Deve receber erro", result.isFailure)
        }

        assertTrue("Callback deve ser chamado", failureReceived)
        tempFile.delete()
    }

    /**
     * Teste: Backup vazio deve funcionar sem erros.
     */
    @Test
    fun testEmptyBackupWorks() {
        val json = """
            {
                "version": "1.0",
                "exportedAt": 1000,
                "appVersion": "1.0.0",
                "modalities": []
            }
        """.trimIndent()

        val tempFile = File(context.cacheDir, "empty_backup.json")
        tempFile.writeText(json)

        var successReceived = false
        backupManager.importBackup(tempFile) { result ->
            successReceived = true
            assertTrue("Deve importar backup vazio com sucesso", result.isSuccess)
        }

        assertTrue("Callback deve ser chamado", successReceived)
        tempFile.delete()
    }

    /**
     * Teste: Método hasBackupFile verifica existência correta.
     */
    @Test
    fun testHasBackupFile() {
        assertFalse("Não deve haver backup inicialmente", backupManager.hasBackupFile())

        // Cria arquivo de backup
        val backupFile = File(context.filesDir, BackupManager.BACKUP_FILE_NAME)
        backupFile.writeText("""{"version":"1.0","modalities":[]}""")

        try {
            assertTrue("Deve encontrar backup após criar", backupManager.hasBackupFile())

            // Deleta e verifica
            backupManager.deleteBackupFile()
            assertFalse("Não deve encontrar backup após deletar", backupManager.hasBackupFile())
        } finally {
            if (backupFile.exists()) {
                backupFile.delete()
            }
        }
    }

    /**
     * Teste: Dados persistidos em todas as entidades antes e depois do restore.
     */
    @Test
    fun testAllEntityTypesPreserved() = runBlocking {
        val db = createTestDatabase()

        // Perfil
        db.runnerProfileDao().insertProfileSync(RunnerProfileEntity(
            nome = "Teste",
            tempoSemCorrer = "Nunca corri",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionadosJson = "MONDAY,WEDNESDAY,FRIDAY",
            dataInicio = "2026-01-01",
            totalSemanas = 1
        ))

        // Plano
        val plan = db.planDao().insertPlan(
            PlanEntity(status = PlanEntity.STATUS_ATIVO, dataInicio = "2026-01-01",
                       dataFim = "2026-12-31", totalSemanas = 1, diasPorSemana = 1, etapaInicial = 1)
        )

        // Treino
        val workout = db.workoutDao().insertWorkout(
            WorkoutEntity(
                planId = plan,
                semanaIndex = 1,
                treinoIndexNaSemana = 1,
                etapaIndex = 1,
                data = "2026-01-01",
                titulo = "T",
                resumoEtapa = "E",
                passosJson = "[]",
                duracaoMinutos = 30,
                duracaoCorridaMinutos = 20
            )
        )

        // Log
        db.workoutLogDao().insertLog(
            WorkoutLogEntity(
                workoutId = workout,
                duracaoSegundos = 1200,
                distanciaMetros = 3000.0,
                ritmoMedioSegundos = null,
                esforcoPercebido = 5
            )
        )

        // Rota
        db.routePointDao().insertPoint(
            RoutePointEntity(
                workoutId = workout,
                latitude = -23.5,
                longitude = -46.6,
                accuracy = 10f,
                timestampMillis = System.currentTimeMillis()
            )
        )

        // Check-in
        db.checkinDao().insert(CheckinEntity(
            workoutId = workout,
            momento = "pre",
            energia = 2,
            sono = 3,
            dor = 0
        ))

        // Ajuste manual
        db.planAdjustmentDao().insertAdjustment(
            PlanAdjustmentEntity(
                planId = plan,
                tipo = "pausa",
                inicio = "2026-01-01",
                fim = "2026-01-07",
                nivel = "reduzido",
                retorno = "gradual"
            )
        )

        // Ajuste automático
        db.autoAdjustmentDao().insert(
            AutoAdjustmentEntity(
                planId = plan,
                workoutOrigemId = workout,
                regra = "AVANCAR",
                antesJson = "{}",
                depoisJson = "{}",
                deslocamentoEtapa = 1
            )
        )

        // Exporta e importa
        var exportedFile: File? = null
        backupManager.exportBackup { result ->
            exportedFile = result.getOrNull()
        }
        backupManager.importBackup(exportedFile!!) { }

        // Verifica todas as entidades
        assertNotNull(db.runnerProfileDao().getProfileSync()?.nome)
        assertTrue(db.planDao().getAll().isNotEmpty())
        assertTrue(db.workoutDao().getAll().isNotEmpty())
        assertTrue(db.workoutLogDao().getAll().isNotEmpty())
        assertTrue(db.routePointDao().getAll().isNotEmpty())
        assertTrue(db.checkinDao().getAll().isNotEmpty())
        assertTrue(db.planAdjustmentDao().getAll().isNotEmpty())
        assertTrue(db.autoAdjustmentDao().getAll().isNotEmpty())
    }

    private fun createTestDatabase(): BaseAppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
    }
}
