package com.elite.sch.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elite.sch.app.data.local.entities.RunnerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunnerProfileDao {
    @Query("SELECT * FROM runner_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<RunnerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: RunnerProfileEntity)

    @Query("UPDATE runner_profile SET nome = :nome WHERE id = 1")
    suspend fun updateNome(nome: String)

    @Query("UPDATE runner_profile SET dismissBatteryWarning = :dismiss WHERE id = 1")
    suspend fun updateDismissBatteryWarning(dismiss: Boolean)

    @Query("UPDATE runner_profile SET vozAtiva = :vozAtiva WHERE id = 1")
    suspend fun updateVozAtiva(vozAtiva: Boolean)

    @Query("UPDATE runner_profile SET vibracaoAtiva = :vibracaoAtiva WHERE id = 1")
    suspend fun updateVibracaoAtiva(vibracaoAtiva: Boolean)

    @Query("UPDATE runner_profile SET lembretesAtivos = :ativos, lembreteHora = :hora, lembreteMinuto = :minuto WHERE id = 1")
    suspend fun updateLembretes(ativos: Boolean, hora: Int, minuto: Int)

    @Query("UPDATE runner_profile SET lembretesAtivos = :ativos WHERE id = 1")
    suspend fun updateLembretesAtivos(ativos: Boolean)

    @Query("UPDATE runner_profile SET ajustesAutomaticos = :ativo WHERE id = 1")
    suspend fun updateAjustesAutomaticos(ativo: Boolean)

    @Query("UPDATE runner_profile SET modoAjuste = :modo WHERE id = 1")
    suspend fun updateModoAjuste(modo: String)

    @Query("UPDATE runner_profile SET avisoDistanciaAtivo = :ativo WHERE id = 1")
    suspend fun updateAvisoDistanciaAtivo(ativo: Boolean)

    @Query("UPDATE runner_profile SET avisoDistanciaMetros = :metros WHERE id = 1")
    suspend fun updateAvisoDistanciaMetros(metros: Int)

    @Query("UPDATE runner_profile SET showMapDuringRun = :ativo WHERE id = 1")
    suspend fun updateShowMapDuringRun(ativo: Boolean)

    @Query("UPDATE runner_profile SET defaultCameraMode = :modo WHERE id = 1")
    suspend fun updateDefaultCameraMode(modo: String)

    @Query("DELETE FROM runner_profile")
    suspend fun clearProfile()

    @Query("SELECT * FROM runner_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileSync(): RunnerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileSync(profile: RunnerProfileEntity)
}
