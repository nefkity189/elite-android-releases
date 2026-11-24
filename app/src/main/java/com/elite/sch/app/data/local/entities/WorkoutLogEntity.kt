package com.elite.sch.app.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_log",
    indices = [Index(value = ["workoutId"])]
)
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: Long,
    val duracaoSegundos: Long,
    val distanciaMetros: Double,
    val ritmoMedioSegundos: Double?, // null se < 100m
    val esforcoPercebido: Int, // 1 a 10
    val finalizadoEm: Long = System.currentTimeMillis(),
    val anotacoes: String? = null,
    // Diagnóstico de GPS por corrida (versão 7 do banco)
    val gpsRecebidas: Int = 0,
    val gpsAceitas: Int = 0,
    val gpsRejPrecisao: Int = 0,
    val gpsRejAnteriores: Int = 0,
    val gpsRejVelocidade: Int = 0,
    val gpsRejDeslocamento: Int = 0,
    val gpsRejInicio: Int = 0,
    val gpsMaiorIntervaloMs: Long = 0L
)
