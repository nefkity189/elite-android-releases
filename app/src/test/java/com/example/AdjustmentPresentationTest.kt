package com.example

import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.generator.AdaptiveEngine
import com.example.ui.AdjustmentPresentation
import com.example.ui.screens.effortLevelFromStored
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AdjustmentPresentationTest {

    private fun adj(id: Long, criadoEm: Long, ativo: Boolean = true) = AutoAdjustmentEntity(
        id = id, planId = 1, workoutOrigemId = 1, regra = AdaptiveEngine.REGRA_ESFORCO_ALTO,
        antesJson = "[]", depoisJson = "[]", ativo = ativo, criadoEm = criadoEm
    )

    private val dia = 24L * 60 * 60 * 1000

    @Test
    fun esforcoGravadoVira5Niveis() {
        assertEquals(1, effortLevelFromStored(1))
        assertEquals(1, effortLevelFromStored(2))
        assertEquals(2, effortLevelFromStored(4))
        assertEquals(3, effortLevelFromStored(5))
        assertEquals(3, effortLevelFromStored(6))
        assertEquals(4, effortLevelFromStored(8))
        assertEquals(5, effortLevelFromStored(9))
        assertEquals(5, effortLevelFromStored(10))
    }

    @Test
    fun previaDorForteUsaMensagemFixaDoMotor() {
        assertEquals(AdaptiveEngine.MSG_DOR_FORTE, AdjustmentPresentation.previa(esforco = 2, dor = 2, ajustesAutomaticos = true))
    }

    @Test
    fun previaDorLeveEEsforcoAlto() {
        assertEquals(AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_DOR_LEVE), AdjustmentPresentation.previa(6, 1, true))
        assertEquals(AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_ESFORCO_ALTO), AdjustmentPresentation.previa(8, 0, true))
        assertEquals(AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_ESFORCO_ALTO), AdjustmentPresentation.previa(10, 0, true))
    }

    @Test
    fun previaTudoCertoQuandoEsforcoBaixoSemDor() {
        assertEquals("Tudo certo. O plano segue como está.", AdjustmentPresentation.previa(4, 0, true))
    }

    @Test
    fun previaComAjustesDesligadosNaoPrometeMudanca() {
        val texto = AdjustmentPresentation.previa(10, 2, ajustesAutomaticos = false)
        assertEquals("Ajustes automáticos desligados: o plano segue como está.", texto)
    }

    @Test
    fun ajusteRecenteIgnoraDesfeitoEAntigo() {
        val agora = 100 * dia
        val lista = listOf(
            adj(1, agora - 5 * dia),               // antigo
            adj(2, agora - 1 * dia, ativo = false), // desfeito
            adj(3, agora - 1 * dia),               // recente e ativo
            adj(4, agora - dia / 2)                // mais recente
        )
        assertEquals(4L, AdjustmentPresentation.recenteAtivo(lista, agora)?.id)
        assertNull(AdjustmentPresentation.recenteAtivo(lista.take(2), agora))
    }

    @Test
    fun tituloPorRegra() {
        assertEquals("Corrida pausada por 3 dias", AdjustmentPresentation.titulo(AdaptiveEngine.REGRA_DOR_FORTE))
        assertEquals("Ajuste no plano", AdjustmentPresentation.titulo("OUTRA"))
    }
}
