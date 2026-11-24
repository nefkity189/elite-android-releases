package com.elite.sch.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plan")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val status: String = STATUS_DRAFT, // "rascunho", "ativo", "concluido", "cancelado"
    val dataInicio: String, // ISO-8601 ex: "2026-10-31"
    val dataFim: String,
    val totalSemanas: Int,
    val diasPorSemana: Int,
    val etapaInicial: Int,
    val termoAceito: Boolean = false,
    val criadoEm: Long = System.currentTimeMillis(),
    val modalidade: String = "plano_5k", // "plano_2_5k", "plano_5k", "plano_10k", "livre"
    val distanciaAlvoM: Int = 5000, // Distância alvo do plano em metros
    val acceptedAt: Long = 0, // Timestamp do aceite explícito (0 = não aceito)
    val acceptedTermsVersion: String = "" // Versão dos termos aceitos
) {
    companion object {
        const val STATUS_DRAFT = "rascunho"
        const val STATUS_ATIVO = "ativo"
        const val STATUS_CONCLUIDO = "concluido"
        const val STATUS_CANCELADO = "cancelado"
        const val MODALIDADE_LIVRE = "livre"
    }
}