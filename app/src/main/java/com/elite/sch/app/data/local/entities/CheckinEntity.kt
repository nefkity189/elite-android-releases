package com.elite.sch.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Check-in antes ("pre") ou depois ("pos") de um treino.
 * energia e sono: 1 (baixa/ruim), 2 (normal), 3 (alta/boa); nulos no check-in pós.
 * dor: 0 nenhuma, 1 leve, 2 forte.
 */
@Entity(
    tableName = "checkin",
    indices = [Index(value = ["workoutId"])]
)
data class CheckinEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: Long?,
    val momento: String, // "pre" ou "pos"
    val energia: Int? = null,
    val sono: Int? = null,
    val dor: Int,
    val regiaoDor: String? = null, // panturrilha, canela, joelho, quadril, pe, outro
    val criadoEm: Long = System.currentTimeMillis()
) {
    companion object {
        const val MOMENTO_PRE = "pre"
        const val MOMENTO_POS = "pos"
        const val DOR_NENHUMA = 0
        const val DOR_LEVE = 1
        const val DOR_FORTE = 2
    }
}
