package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Ajuste automático feito pelo AdaptiveEngine. Guarda os treinos antes e depois
 * (JSON) para poder desfazer. deslocamentoEtapa: variação de etapa aplicada aos
 * treinos futuros (negativo = plano mais leve).
 */
@Entity(
    tableName = "auto_adjustment",
    indices = [Index(value = ["planId"])]
)
data class AutoAdjustmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val workoutOrigemId: Long,
    val regra: String,
    val antesJson: String,
    val depoisJson: String,
    val ativo: Boolean = true,
    val deslocamentoEtapa: Int = 0,
    val criadoEm: Long = System.currentTimeMillis()
)
