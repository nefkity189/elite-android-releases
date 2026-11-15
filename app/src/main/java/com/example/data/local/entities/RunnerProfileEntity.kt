package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runner_profile")
data class RunnerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val nome: String? = null,
    val tempoSemCorrer: String,
    val frequenciaExercicio: String,
    val tipoExercicio: String,
    val diasPorSemana: Int,
    val diasSelecionadosJson: String, // ex: ["MONDAY","WEDNESDAY","FRIDAY"]
    val dataInicio: String, // ISO-8601 ex: "2026-10-31"
    val totalSemanas: Int,
    val dismissBatteryWarning: Boolean = false,
    val vozAtiva: Boolean = true,
    val vibracaoAtiva: Boolean = true,
    val lembretesAtivos: Boolean = false,
    val lembreteHora: Int = 7,
    val lembreteMinuto: Int = 0,
    val ajustesAutomaticos: Boolean = true,
    val modoAjuste: String = "cauteloso", // "cauteloso" ou "equilibrado"
    val atualizadoEm: Long = System.currentTimeMillis(),
    val ritmoAtualSegKm: Int? = null, // Ritmo atual em segundos por km
    val ritmoOrigem: String? = null, // Origem do ritmo: "auto", "manual", "teste"
    val alertasRitmoAtivos: Boolean = true, // Alertas de ritmo ligados/desligados
    val avisoDistanciaAtivo: Boolean = true, // Avisos por distância ligados/desligados
    val avisoDistanciaMetros: Int = 1000, // Intervalo dos avisos em metros (500, 1000, 2000)
    val showMapDuringRun: Boolean = true, // Mostrar mapa durante corrida ativa
    val defaultCameraMode: String = "follow_user" // Modo de câmera padrão: "follow_user" | "show_full_route"
)