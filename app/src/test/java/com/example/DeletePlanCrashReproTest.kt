package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DatabaseProvider
import com.example.data.local.entities.PlanEntity
import com.example.data.local.entities.RunnerProfileEntity
import com.example.workspace.GlobalSession
import com.example.workspace.WorkspaceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DeletePlanCrashReproTest {

    private lateinit var app: Application

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testWorkspaceClearAllData() = runBlocking {
        val globalSession = MutableStateFlow(GlobalSession(userName = "Corredor Teste"))
        val manager = WorkspaceManager(app, globalSession, CoroutineScope(Dispatchers.Unconfined))

        // Wait / ensure hydration
        manager.hydrateWorkspace(DatabaseProvider.MODALIDADE_5K)

        val db = DatabaseProvider.getDatabase(DatabaseProvider.MODALIDADE_5K, app)
        val profile = RunnerProfileEntity(
            nome = "Teste",
            tempoSemCorrer = "Nunca",
            frequenciaExercicio = "Nada",
            tipoExercicio = "Nenhum",
            diasPorSemana = 3,
            diasSelecionadosJson = "MONDAY,WEDNESDAY,FRIDAY",
            dataInicio = "2026-11-15",
            totalSemanas = 8
        )
        db.runnerProfileDao().insertProfile(profile)
        val plan = PlanEntity(
            status = PlanEntity.STATUS_ATIVO,
            dataInicio = "2026-11-15",
            dataFim = "2027-01-15",
            totalSemanas = 8,
            diasPorSemana = 3,
            etapaInicial = 1,
            modalidade = "plano_5k",
            acceptedAt = 123456L
        )
        val planId = db.planDao().insertPlan(plan)
        assertTrue(planId > 0)

        // Verify inserted
        val plansBefore = db.planDao().getAll()
        assertEquals(1, plansBefore.size)

        // Now call clearAllDataForAllModalities
        var callbackSuccess: Boolean? = null
        manager.clearAllDataForAllModalities { success ->
            callbackSuccess = success
        }

        println("Callback success: $callbackSuccess")
        val plansAfter = db.planDao().getAll()
        println("Plans after clear: $plansAfter")
        assertEquals(0, plansAfter.size)
        assertEquals(true, callbackSuccess)
    }
}
