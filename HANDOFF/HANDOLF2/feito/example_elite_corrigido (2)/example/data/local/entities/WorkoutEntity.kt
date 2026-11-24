package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout",
    indices = [
        Index(value = ["planId"]),
        Index(value = ["data", "status"])
    ]
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val semanaIndex: Int,
    val treinoIndexNaSemana: Int,
    val etapaIndex: Int,
    val data: String, // ISO-8601 ex: "2026-10-31"
    val titulo: String, // "Semana 1 • Treino 1"
    val resumoEtapa: String,
    val passosJson: String,
    val duracaoMinutos: Int,
    val duracaoCorridaMinutos: Int,
    val status: String = STATUS_PLANEJADO, // "planejado", "concluido", "pulado"
    val concluidoEm: Long? = null,
    val distanciaAlvoM: Int? = null, // Distância alvo do treino em metros (para treinos por distância)
    val modoConclusao: String = MODO_TEMPO // "tempo" ou "distancia"
) {
    companion object {
        const val STATUS_PLANEJADO = "planejado"
        const val STATUS_CONCLUIDO = "concluido"
        const val STATUS_PULADO = "pulado"
        const val STATUS_MOVIDO = "movido"
        const val STATUS_PAUSADO = "pausado"
        const val MODO_TEMPO = "tempo"
        const val MODO_DISTANCIA = "distancia"
        const val MODO_CONCLUSAO_TEMPO = "tempo"
    }
}