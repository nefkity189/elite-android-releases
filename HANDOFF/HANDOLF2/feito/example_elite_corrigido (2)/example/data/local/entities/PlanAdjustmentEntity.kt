package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plan_adjustment",
    indices = [
        Index(value = ["planId"])
    ]
)
data class PlanAdjustmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val tipo: String, // "pausa" ou "ferias"
    val inicio: String, // datas ISO ex: "2026-11-02"
    val fim: String, // datas ISO ex: "2026-11-08"
    val nivel: String, // "reduzido", "caminhada" ou "sem_treino"
    val retorno: String, // "gradual", "normal" ou "rapido"
    val criadoEm: Long = System.currentTimeMillis()
) {
    companion object {
        const val TIPO_PAUSA = "pausa"
        const val TIPO_FERIAS = "ferias"

        const val NIVEL_REDUZIDO = "reduzido"
        const val NIVEL_CAMINHADA = "caminhada"
        const val NIVEL_SEM_TREINO = "sem_treino"

        const val RETORNO_GRADUAL = "gradual"
        const val RETORNO_NORMAL = "normal"
        const val RETORNO_RAPIDO = "rapido"
    }
}
