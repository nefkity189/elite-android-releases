package com.example.ui

import com.example.data.local.entities.AutoAdjustmentEntity
import com.example.generator.AdaptiveEngine
import org.json.JSONArray
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Versão legível de um ajuste automático, para Hoje e para a tela Ajustes do plano. */
data class AdjustmentView(
    val id: Long,
    val titulo: String,
    val motivo: String,
    val antes: String?,
    val depois: String?,
    val ativo: Boolean,
    val dataLabel: String,
    val regra: String,
    val criadoEm: Long
)

object AdjustmentPresentation {

    fun titulo(regra: String): String = when (regra) {
        AdaptiveEngine.REGRA_DOR_FORTE -> "Corrida pausada por 3 dias"
        AdaptiveEngine.REGRA_DOR_LEVE -> "Etapa repetida, com mais descanso"
        AdaptiveEngine.REGRA_ESFORCO_ALTO -> "Mais descanso entre os trotes"
        AdaptiveEngine.REGRA_ESFORCO_ALTO_2X -> "Etapa repetida"
        AdaptiveEngine.REGRA_AVANCAR -> "Avanço de etapa"
        AdaptiveEngine.REGRA_PULADOS_2X -> "Retorno gradual"
        else -> "Ajuste no plano"
    }

    /** Primeiro resumo de etapa do JSON de treinos (antes ou depois), ou null se não der para ler. */
    fun primeiroResumo(json: String): String? = try {
        val arr = JSONArray(json)
        if (arr.length() == 0) null else arr.getJSONObject(0).optString("resumoEtapa").ifBlank { null }
    } catch (e: Exception) {
        null
    }

    fun dataLabel(criadoEm: Long, hoje: LocalDate): String {
        val d = Instant.ofEpochMilli(criadoEm).atZone(ZoneId.systemDefault()).toLocalDate()
        return when (d) {
            hoje -> "Hoje"
            hoje.minusDays(1) -> "Ontem"
            else -> d.format(DateTimeFormatter.ofPattern("dd/MM"))
        }
    }

    fun toView(e: AutoAdjustmentEntity, hoje: LocalDate): AdjustmentView = AdjustmentView(
        id = e.id,
        titulo = titulo(e.regra),
        motivo = AdaptiveEngine.mensagemPadrao(e.regra),
        antes = primeiroResumo(e.antesJson),
        depois = primeiroResumo(e.depoisJson),
        ativo = e.ativo,
        dataLabel = dataLabel(e.criadoEm, hoje),
        regra = e.regra,
        criadoEm = e.criadoEm
    )

    /** Ajuste ativo mais recente dentro da janela (padrão 2 dias), para o cartão da tela Hoje. */
    fun recenteAtivo(lista: List<AutoAdjustmentEntity>, agora: Long = System.currentTimeMillis(), janelaDias: Int = 2): AutoAdjustmentEntity? {
        val limite = agora - janelaDias * 24L * 60 * 60 * 1000
        return lista.filter { it.ativo && it.criadoEm >= limite }.maxByOrNull { it.criadoEm }
    }

    /**
     * Prévia do que o motor tende a fazer com as respostas da tela pós-treino.
     * Usa os mesmos textos do motor. Regras que dependem do histórico (dois treinos seguidos,
     * mesma região) só são confirmadas ao salvar.
     */
    fun previa(esforco: Int, dor: Int, ajustesAutomaticos: Boolean): String {
        if (!ajustesAutomaticos) return "Ajustes automáticos desligados: o plano segue como está."
        return when {
            dor >= 2 -> AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_DOR_FORTE)
            dor == 1 -> AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_DOR_LEVE)
            esforco >= 8 -> AdaptiveEngine.mensagemPadrao(AdaptiveEngine.REGRA_ESFORCO_ALTO)
            else -> "Tudo certo. O plano segue como está."
        }
    }
}
