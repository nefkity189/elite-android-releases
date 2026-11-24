package com.example.backup

import android.content.Context
import android.content.Intent
import android.content.ClipData
import androidx.core.content.FileProvider
import android.util.Log
import androidx.room.withTransaction
import com.example.data.local.BaseAppDatabase
import com.example.data.local.DatabaseProvider
import com.example.data.local.entities.*
import com.example.worker.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Gerenciador de backup/restore do aplicativo Aisle.
 *
 * Suporta backup de todas as 4 modalidades (livre, 2.5k, 5k, 10k) em um único arquivo JSON
 * versionado para compatibilidade futura.
 */
class BackupManager(private val context: Context) {

    companion object {
        private const val TAG = "BackupManager"
        const val BACKUP_FILE_NAME = "aisle_backup.json"
    }

    private val modalities = listOf(
        DatabaseProvider.MODALIDADE_LIVRE,
        DatabaseProvider.MODALIDADE_2_5K,
        DatabaseProvider.MODALIDADE_5K,
        DatabaseProvider.MODALIDADE_10K
    )

    /**
     * Exporta todos os dados de todas as modalidades para um arquivo JSON.
     *
     * @param destination Caminho opcional de destino. Quando fornecido, o backup é
     *                    escrito diretamente nessa pasta (o usuário escolheu onde
     *                    salvar). Quando nulo, salva no filesDir interno.
     */
    fun exportBackup(destination: File? = null, callback: (Result<File>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val appBackup = exportAllModalities()
                val file = writeBackupToFile(appBackup, destination)
                callback(Result.success(file))
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao exportar backup", e)
                callback(Result.failure(e))
            }
        }
    }

    /**
     * Exporta backup completo para um arquivo específico (não diretório).
     * Usado quando o usuário escolhe o arquivo via CreateDocument.
     */
    fun exportBackupToFile(destinationFile: File, callback: (Result<File>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val appBackup = exportAllModalities()
                val jsonObject = appBackup.toJson()

                // Grava diretamente no arquivo escolhido
                FileOutputStream(destinationFile).use { output ->
                    output.write(jsonObject.toString(2).toByteArray())
                }

                // Também grava no filesDir interno para shareBackupFile() funcionar
                val internalFile = File(context.filesDir, BACKUP_FILE_NAME)
                FileOutputStream(internalFile).use { output ->
                    output.write(jsonObject.toString(2).toByteArray())
                }

                Log.d(TAG, "Backup salvo em: ${destinationFile.absolutePath}")
                Log.d(TAG, "Backup também salvo internamente em: ${internalFile.absolutePath}")
                callback(Result.success(destinationFile))
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao exportar backup para arquivo", e)
                callback(Result.failure(e))
            }
        }
    }

    /**
     * Abre o seletor de compartilhamento do sistema com o arquivo de backup salvo.
     * O usuário escolhe para onde enviar ( nuvem, email, outro app ) sem que o
     * app decida por ele. Requer que o backup já tenha sido exportado.
     */
    fun shareBackupFile(context: Context): Boolean {
        val file = getBackupFile() ?: return false
        return try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Backup do Aisle")
                // setClipData allows the share-sheet resolver (a separate app) to read
                // the stream for preview; EXTRA_STREAM alone does not grant it.
                clipData = ClipData.newRawUri("backup", uri)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar backup"))
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao compartilhar backup", e)
            false
        }
    }

    /**
     * Importa/restore todos os dados de um arquivo JSON de backup.
     */
    fun importBackup(file: File, callback: (Result<Unit>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonString = file.readText()
                val jsonObject = JSONObject(jsonString)
                val appBackup = parseBackup(jsonObject)

                // Valida versão do backup
                if (!isVersionCompatible(appBackup.version)) {
                    throw IllegalArgumentException(
                        "Versão de backup incompatível: ${appBackup.version}. " +
                        "Versão atual: $BACKUP_VERSION"
                    )
                }

                restoreAllModalities(appBackup)
                callback(Result.success(Unit))
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao importar backup", e)
                callback(Result.failure(e))
            }
        }
    }

    /**
     * Exporta dados de todas as modalidades.
     */
    private suspend fun exportAllModalities(): AppBackup {
        val modalityBackups = mutableListOf<ModalityBackup>()

        for (modality in modalities) {
            val db = DatabaseProvider.getDatabase(modality, context)
            val modalityBackup = exportModality(db, modality)
            modalityBackups.add(modalityBackup)
        }

        return AppBackup(
            version = BACKUP_VERSION,
            exportedAt = System.currentTimeMillis(),
            appVersion = getAppVersion(),
            modalities = modalityBackups
        )
    }

    /**
     * Exporta dados de uma modalidade específica.
     */
    private suspend fun exportModality(db: BaseAppDatabase, modality: String): ModalityBackup {
        return db.withTransaction {
            val profile = db.runnerProfileDao().getProfileSync()
            val plans = db.planDao().getAll()
            val workouts = db.workoutDao().getAll()
            val workoutLogs = db.workoutLogDao().getAll()
            val routePoints = db.routePointDao().getAll()
            val checkins = db.checkinDao().getAll()
            val planAdjustments = db.planAdjustmentDao().getAll()
            val autoAdjustments = db.autoAdjustmentDao().getAll()
            val activeRun = db.activeRunDao().getActiveRun()

            ModalityBackup(
                modality = modality,
                profile = profile,
                plans = plans,
                workouts = workouts,
                workoutLogs = workoutLogs,
                routePoints = routePoints,
                checkins = checkins,
                planAdjustments = planAdjustments,
                autoAdjustments = autoAdjustments,
                activeRun = activeRun
            )
        }
    }

    /**
     * Restaura dados de todas as modalidades.
     */
    private suspend fun restoreAllModalities(appBackup: AppBackup) {
        for (modalityBackup in appBackup.modalities) {
            val db = DatabaseProvider.getDatabase(modalityBackup.modality, context)
            restoreModality(db, modalityBackup)

            // Reagenda lembretes se ativos no perfil restaurado
            val profile = modalityBackup.profile
            if (profile?.lembretesAtivos == true) {
                ReminderScheduler.scheduleDailyReminder(
                    context = context,
                    modality = modalityBackup.modality,
                    hour = profile.lembreteHora,
                    minute = profile.lembreteMinuto,
                    active = true
                )
            } else {
                ReminderScheduler.cancelReminder(context, modalityBackup.modality)
            }
        }
    }

    /**
     * Restaura dados de uma modalidade específica.
     */
    private suspend fun restoreModality(db: BaseAppDatabase, backup: ModalityBackup) {
        db.withTransaction {
            // Limpa dados existentes primeiro
            db.workoutDao().clearAll()
            db.workoutLogDao().clearAllLogs()
            db.routePointDao().clearAllRoutePoints()
            db.planAdjustmentDao().clearAll()
            db.autoAdjustmentDao().clearAll()
            db.checkinDao().clearAll()
            db.activeRunDao().clearActiveRun()
            db.planDao().clearAllPlans()
            db.runnerProfileDao().clearProfile()

            // Restaura dados do backup
            backup.profile?.let { db.runnerProfileDao().insertProfileSync(it) }
            if (backup.plans.isNotEmpty()) db.planDao().insertAll(backup.plans)
            if (backup.workouts.isNotEmpty()) db.workoutDao().insertAll(backup.workouts)
            if (backup.workoutLogs.isNotEmpty()) db.workoutLogDao().insertAll(backup.workoutLogs)
            if (backup.routePoints.isNotEmpty()) db.routePointDao().insertAll(backup.routePoints)
            if (backup.checkins.isNotEmpty()) db.checkinDao().insertAll(backup.checkins)
            if (backup.planAdjustments.isNotEmpty()) db.planAdjustmentDao().insertAll(backup.planAdjustments)
            if (backup.autoAdjustments.isNotEmpty()) db.autoAdjustmentDao().insertAll(backup.autoAdjustments)
            backup.activeRun?.let { db.activeRunDao().saveActiveRunSilent(it) }
        }
    }

    /**
     * Escreve o backup em arquivo JSON.
     *
     * @param destination Pasta de destino escolhida pelo usuário. Se nulo, usa o
     *                    filesDir interno (comportamento padrão).
     *                    Se fornecido, o backup também é gravado no filesDir interno
     *                    para que o compartilhamento funcione.
     */
    private fun writeBackupToFile(appBackup: AppBackup, destination: File? = null): File {
        val jsonObject = appBackup.toJson()
        val dir = destination ?: context.filesDir
        if (destination != null && !dir.exists() && !dir.mkdirs()) {
            throw IOException("Não foi possível criar o diretório de destino: ${dir.absolutePath}")
        }
        val file = File(dir, BACKUP_FILE_NAME)

        FileOutputStream(file).use { output ->
            output.write(jsonObject.toString(2).toByteArray())
        }

        // Se um destino externo foi fornecido, também grava no filesDir interno
        // para que shareBackupFile() encontre o arquivo
        if (destination != null) {
            val internalFile = File(context.filesDir, BACKUP_FILE_NAME)
            FileOutputStream(internalFile).use { output ->
                output.write(jsonObject.toString(2).toByteArray())
            }
            Log.d(TAG, "Backup também salvo internamente em: ${internalFile.absolutePath}")
        }

        Log.d(TAG, "Backup salvo em: ${file.absolutePath}")
        return file
    }

    /**
     * Parseia JSON para AppBackup.
     */
    private fun parseBackup(jsonObject: JSONObject): AppBackup {
        val version = jsonObject.optString("version", "1.0")
        val exportedAt = jsonObject.optLong("exportedAt", System.currentTimeMillis())
        val appVersion = jsonObject.optString("appVersion", "1.0.0")

        val modalitiesArray = jsonObject.optJSONArray("modalities") ?: JSONArray()
        val modalityBackups = mutableListOf<ModalityBackup>()

        for (i in 0 until modalitiesArray.length()) {
            val modalityJson = modalitiesArray.getJSONObject(i)
            modalityBackups.add(parseModalityBackup(modalityJson))
        }

        return AppBackup(
            version = version,
            exportedAt = exportedAt,
            appVersion = appVersion,
            modalities = modalityBackups
        )
    }

    /**
     * Parseia um ModalityBackup do JSON.
     */
    private fun parseModalityBackup(json: JSONObject): ModalityBackup {
        val modality = json.getString("modality")

        val profile = json.optJSONObject("profile")?.let { parseRunnerProfile(it) }
        val plans = parsePlanList(json.optJSONArray("plans"))
        val workouts = parseWorkoutList(json.optJSONArray("workouts"))
        val workoutLogs = parseWorkoutLogList(json.optJSONArray("workoutLogs"))
        val routePoints = parseRoutePointList(json.optJSONArray("routePoints"))
        val checkins = parseCheckinList(json.optJSONArray("checkins"))
        val planAdjustments = parsePlanAdjustmentList(json.optJSONArray("planAdjustments"))
        val autoAdjustments = parseAutoAdjustmentList(json.optJSONArray("autoAdjustments"))
        val activeRun = json.optJSONObject("activeRun")?.let { parseActiveRun(it) }

        return ModalityBackup(
            modality = modality,
            profile = profile,
            plans = plans,
            workouts = workouts,
            workoutLogs = workoutLogs,
            routePoints = routePoints,
            checkins = checkins,
            planAdjustments = planAdjustments,
            autoAdjustments = autoAdjustments,
            activeRun = activeRun
        )
    }

    // Parsers individuais para cada tipo de entidade
    private fun parseRunnerProfile(json: JSONObject): RunnerProfileEntity {
        return RunnerProfileEntity(
            id = json.optInt("id", 1),
            nome = json.optString("nome", "").takeIf { it.isNotEmpty() },
            tempoSemCorrer = json.optString("tempoSemCorrer", ""),
            frequenciaExercicio = json.optString("frequenciaExercicio", ""),
            tipoExercicio = json.optString("tipoExercicio", ""),
            diasPorSemana = json.optInt("diasPorSemana", 3),
            diasSelecionadosJson = json.optString("diasSelecionadosJson", "MONDAY,WEDNESDAY,FRIDAY"),
            dataInicio = json.optString("dataInicio", ""),
            totalSemanas = json.optInt("totalSemanas", 9),
            dismissBatteryWarning = json.optBoolean("dismissBatteryWarning", false),
            vozAtiva = json.optBoolean("vozAtiva", true),
            vibracaoAtiva = json.optBoolean("vibracaoAtiva", true),
            lembretesAtivos = json.optBoolean("lembretesAtivos", false),
            lembreteHora = json.optInt("lembreteHora", 7),
            lembreteMinuto = json.optInt("lembreteMinuto", 0),
            ajustesAutomaticos = json.optBoolean("ajustesAutomaticos", true),
            modoAjuste = json.optString("modoAjuste", "cauteloso"),
            alertasRitmoAtivos = json.optBoolean("alertasRitmoAtivos", true),
            avisoDistanciaAtivo = json.optBoolean("avisoDistanciaAtivo", true),
            avisoDistanciaMetros = json.optInt("avisoDistanciaMetros", 1000)
        )
    }

    private fun parsePlanList(jsonArray: JSONArray?): List<PlanEntity> {
        val list = mutableListOf<PlanEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parsePlan(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parsePlan(json: JSONObject): PlanEntity {
        return PlanEntity(
            id = json.optLong("id", 0),
            status = json.optString("status", "ativo"),
            dataInicio = json.optString("dataInicio", ""),
            dataFim = json.optString("dataFim", ""),
            totalSemanas = json.optInt("totalSemanas", 9),
            diasPorSemana = json.optInt("diasPorSemana", 3),
            etapaInicial = json.optInt("etapaInicial", 1),
            termoAceito = json.optBoolean("termoAceito", true),
            criadoEm = json.optLong("criadoEm", System.currentTimeMillis()),
            modalidade = json.optString("modalidade", "plano_5k"),
            distanciaAlvoM = json.optInt("distanciaAlvoM", 5000),
            acceptedAt = json.optLong("acceptedAt", 0),
            acceptedTermsVersion = json.optString("acceptedTermsVersion", "")
        )
    }

    private fun parseWorkoutList(jsonArray: JSONArray?): List<WorkoutEntity> {
        val list = mutableListOf<WorkoutEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parseWorkout(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parseWorkout(json: JSONObject): WorkoutEntity {
        return WorkoutEntity(
            id = json.optLong("id", 0),
            planId = json.optLong("planId", 0),
            semanaIndex = json.optInt("semanaIndex", 0),
            treinoIndexNaSemana = json.optInt("treinoIndexNaSemana", 0),
            etapaIndex = json.optInt("etapaIndex", 0),
            data = json.optString("data", ""),
            titulo = json.optString("titulo", ""),
            resumoEtapa = json.optString("resumoEtapa", ""),
            passosJson = json.optString("passosJson", "[]"),
            duracaoMinutos = json.optInt("duracaoMinutos", 0),
            duracaoCorridaMinutos = json.optInt("duracaoCorridaMinutos", 0),
            status = json.optString("status", "planejado"),
            concluidoEm = json.optLong("concluidoEm").takeIf { it > 0 },
            distanciaAlvoM = json.optInt("distanciaAlvoM").takeIf { it > 0 },
            modoConclusao = json.optString("modoConclusao", "tempo")
        )
    }

    private fun parseWorkoutLogList(jsonArray: JSONArray?): List<WorkoutLogEntity> {
        val list = mutableListOf<WorkoutLogEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parseWorkoutLog(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parseWorkoutLog(json: JSONObject): WorkoutLogEntity {
        return WorkoutLogEntity(
            id = json.optLong("id", 0),
            workoutId = json.optLong("workoutId", 0),
            duracaoSegundos = json.optLong("duracaoSegundos", 0),
            distanciaMetros = json.optDouble("distanciaMetros", 0.0),
            ritmoMedioSegundos = json.optDouble("ritmoMedioSegundos").takeIf { it > 0 },
            esforcoPercebido = json.optInt("esforcoPercebido", 5),
            finalizadoEm = json.optLong("finalizadoEm", System.currentTimeMillis()),
            anotacoes = json.optString("anotacoes", "").nullIfEmpty(),
            gpsRecebidas = json.optInt("gpsRecebidas", 0),
            gpsAceitas = json.optInt("gpsAceitas", 0),
            gpsRejPrecisao = json.optInt("gpsRejPrecisao", 0),
            gpsRejAnteriores = json.optInt("gpsRejAnteriores", 0),
            gpsRejVelocidade = json.optInt("gpsRejVelocidade", 0),
            gpsRejDeslocamento = json.optInt("gpsRejDeslocamento", 0),
            gpsRejInicio = json.optInt("gpsRejInicio", 0),
            gpsMaiorIntervaloMs = json.optLong("gpsMaiorIntervaloMs", 0L)
        )
    }

    private fun parseRoutePointList(jsonArray: JSONArray?): List<RoutePointEntity> {
        val list = mutableListOf<RoutePointEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parseRoutePoint(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parseRoutePoint(json: JSONObject): RoutePointEntity {
        return RoutePointEntity(
            id = json.optLong("id", 0),
            workoutId = json.optLong("workoutId", 0),
            latitude = json.optDouble("latitude", 0.0),
            longitude = json.optDouble("longitude", 0.0),
            accuracy = json.optDouble("accuracy", 0.0).toFloat(),
            timestampMillis = json.optLong("timestampMillis", System.currentTimeMillis())
        )
    }

    private fun parseCheckinList(jsonArray: JSONArray?): List<CheckinEntity> {
        val list = mutableListOf<CheckinEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parseCheckin(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parseCheckin(json: JSONObject): CheckinEntity {
        return CheckinEntity(
            id = json.optLong("id", 0),
            workoutId = json.optLong("workoutId").takeIf { it > 0 },
            momento = json.optString("momento", "pre"),
            energia = json.optInt("energia", 0).takeIf { it > 0 },
            sono = json.optInt("sono", 0).takeIf { it > 0 },
            dor = json.optInt("dor", 0),
            regiaoDor = json.optString("regiaoDor", "").nullIfEmpty(),
            criadoEm = json.optLong("criadoEm", System.currentTimeMillis())
        )
    }

    private fun parsePlanAdjustmentList(jsonArray: JSONArray?): List<PlanAdjustmentEntity> {
        val list = mutableListOf<PlanAdjustmentEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parsePlanAdjustment(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parsePlanAdjustment(json: JSONObject): PlanAdjustmentEntity {
        return PlanAdjustmentEntity(
            id = json.optLong("id", 0),
            planId = json.optLong("planId", 0),
            tipo = json.optString("tipo", ""),
            inicio = json.optString("inicio", ""),
            fim = json.optString("fim", ""),
            nivel = json.optString("nivel", ""),
            retorno = json.optString("retorno", ""),
            criadoEm = json.optLong("criadoEm", System.currentTimeMillis())
        )
    }

    private fun parseAutoAdjustmentList(jsonArray: JSONArray?): List<AutoAdjustmentEntity> {
        val list = mutableListOf<AutoAdjustmentEntity>()
        jsonArray?.let {
            for (i in 0 until it.length()) {
                list.add(parseAutoAdjustment(it.getJSONObject(i)))
            }
        }
        return list
    }

    private fun parseAutoAdjustment(json: JSONObject): AutoAdjustmentEntity {
        return AutoAdjustmentEntity(
            id = json.optLong("id", 0),
            planId = json.optLong("planId", 0),
            workoutOrigemId = json.optLong("workoutOrigemId", 0),
            regra = json.optString("regra", ""),
            antesJson = json.optString("antesJson", ""),
            depoisJson = json.optString("depoisJson", ""),
            ativo = json.optBoolean("ativo", true),
            deslocamentoEtapa = json.optInt("deslocamentoEtapa", 0),
            criadoEm = json.optLong("criadoEm", System.currentTimeMillis())
        )
    }

    private fun parseActiveRun(json: JSONObject): ActiveRunEntity {
        return ActiveRunEntity(
            workoutId = json.optLong("workoutId", 0),
            startTimeMillis = json.optLong("startTimeMillis", System.currentTimeMillis()),
            elapsedRealtimeBase = json.optLong("elapsedRealtimeBase", 0L),
            pausedElapsedMillis = json.optLong("pausedElapsedMillis", 0L),
            isPaused = json.optBoolean("isPaused", false),
            totalDistanceMeters = json.optDouble("totalDistanceMeters", 0.0),
            lastUpdatedMillis = json.optLong("lastUpdatedMillis", System.currentTimeMillis())
        )
    }

    /**
     * Verifica se a versão do backup é compatível com a versão atual do app.
     */
    private fun isVersionCompatible(backupVersion: String): Boolean {
        try {
            val currentParts = BACKUP_VERSION.split(".").map { it.toInt() }
            val backupParts = backupVersion.split(".").map { it.toInt() }

            // Major version deve ser igual
            return currentParts.first() == backupParts.first()
        } catch (e: Exception) {
            Log.w(TAG, "Erro ao comparar versões, assumindo incompatível", e)
            return false
        }
    }

    /**
     * Obtém a versão do app do BuildConfig.
     */
    private fun getAppVersion(): String {
        return try {
            "1.0.0" // Fallback se BuildConfig não estiver disponível
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * Verifica se existe um arquivo de backup salvo.
     */
    fun hasBackupFile(): Boolean {
        val file = File(context.filesDir, BACKUP_FILE_NAME)
        return file.exists()
    }

    /**
     * Obtém o arquivo de backup se existir.
     */
    fun getBackupFile(): File? {
        val file = File(context.filesDir, BACKUP_FILE_NAME)
        return if (file.exists()) file else null
    }

    /**
     * Deleta o arquivo de backup local.
     */
    fun deleteBackupFile(): Boolean {
        val file = File(context.filesDir, BACKUP_FILE_NAME)
        return file.delete()
    }
}

// Helpers
private fun String.nullIfEmpty(): String? = if (isEmpty()) null else this

/** Extensão para serialização do AppBackup para JSON. */
private fun AppBackup.toJson(): JSONObject {
    val json = JSONObject()
    json.put("version", version)
    json.put("exportedAt", exportedAt)
    json.put("appVersion", appVersion)

    val modalitiesArray = JSONArray()
    for (modality in modalities) {
        modalitiesArray.put(modality.toJson())
    }
    json.put("modalities", modalitiesArray)

    return json
}

/** Extensão para serialização do ModalityBackup para JSON. */
private fun ModalityBackup.toJson(): JSONObject {
    val json = JSONObject()
    json.put("modality", modality)

    profile?.let { json.put("profile", it.toJson()) }
    json.put("plans", plans.toJsonArray { it.toJson() })
    json.put("workouts", workouts.toJsonArray { it.toJson() })
    json.put("workoutLogs", workoutLogs.toJsonArray { it.toJson() })
    json.put("routePoints", routePoints.toJsonArray { it.toJson() })
    json.put("checkins", checkins.toJsonArray { it.toJson() })
    json.put("planAdjustments", planAdjustments.toJsonArray { it.toJson() })
    json.put("autoAdjustments", autoAdjustments.toJsonArray { it.toJson() })
    activeRun?.let { json.put("activeRun", it.toJson()) }

    return json
}

private fun <T : Any> List<T>.toJsonArray(toJson: (T) -> JSONObject): JSONArray {
    val array = JSONArray()
    forEach { array.put(toJson(it)) }
    return array
}

// Extensions de toJson para cada entidade
private fun RunnerProfileEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("nome", nome)
        put("tempoSemCorrer", tempoSemCorrer)
        put("frequenciaExercicio", frequenciaExercicio)
        put("tipoExercicio", tipoExercicio)
        put("diasPorSemana", diasPorSemana)
        put("diasSelecionadosJson", diasSelecionadosJson)
        put("dataInicio", dataInicio)
        put("totalSemanas", totalSemanas)
        put("dismissBatteryWarning", dismissBatteryWarning)
        put("vozAtiva", vozAtiva)
        put("vibracaoAtiva", vibracaoAtiva)
        put("lembretesAtivos", lembretesAtivos)
        put("lembreteHora", lembreteHora)
        put("lembreteMinuto", lembreteMinuto)
        put("ajustesAutomaticos", ajustesAutomaticos)
        put("modoAjuste", modoAjuste)
        put("alertasRitmoAtivos", alertasRitmoAtivos)
        put("avisoDistanciaAtivo", avisoDistanciaAtivo)
        put("avisoDistanciaMetros", avisoDistanciaMetros)
    }
}

private fun PlanEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("status", status)
        put("dataInicio", dataInicio)
        put("dataFim", dataFim)
        put("totalSemanas", totalSemanas)
        put("diasPorSemana", diasPorSemana)
        put("etapaInicial", etapaInicial)
        put("termoAceito", termoAceito)
        put("criadoEm", criadoEm)
        put("modalidade", modalidade)
        put("distanciaAlvoM", distanciaAlvoM)
        put("acceptedAt", acceptedAt)
        put("acceptedTermsVersion", acceptedTermsVersion)
    }
}

private fun WorkoutEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("planId", planId)
        put("semanaIndex", semanaIndex)
        put("treinoIndexNaSemana", treinoIndexNaSemana)
        put("etapaIndex", etapaIndex)
        put("data", data)
        put("titulo", titulo)
        put("resumoEtapa", resumoEtapa)
        put("passosJson", passosJson)
        put("duracaoMinutos", duracaoMinutos)
        put("duracaoCorridaMinutos", duracaoCorridaMinutos)
        put("status", status)
        concluidoEm?.let { put("concluidoEm", it) }
        distanciaAlvoM?.let { put("distanciaAlvoM", it) }
        put("modoConclusao", modoConclusao)
    }
}

private fun WorkoutLogEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("workoutId", workoutId)
        put("duracaoSegundos", duracaoSegundos)
        put("distanciaMetros", distanciaMetros)
        ritmoMedioSegundos?.let { put("ritmoMedioSegundos", it) }
        put("esforcoPercebido", esforcoPercebido)
        put("finalizadoEm", finalizadoEm)
        anotacoes?.let { put("anotacoes", it) }
        put("gpsRecebidas", gpsRecebidas)
        put("gpsAceitas", gpsAceitas)
        put("gpsRejPrecisao", gpsRejPrecisao)
        put("gpsRejAnteriores", gpsRejAnteriores)
        put("gpsRejVelocidade", gpsRejVelocidade)
        put("gpsRejDeslocamento", gpsRejDeslocamento)
        put("gpsRejInicio", gpsRejInicio)
        put("gpsMaiorIntervaloMs", gpsMaiorIntervaloMs)
    }
}

private fun RoutePointEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("workoutId", workoutId)
        put("latitude", latitude)
        put("longitude", longitude)
        put("accuracy", accuracy)
        put("timestampMillis", timestampMillis)
    }
}

private fun CheckinEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        workoutId?.let { put("workoutId", it) }
        put("momento", momento)
        energia?.let { put("energia", it) }
        sono?.let { put("sono", it) }
        put("dor", dor)
        regiaoDor?.let { put("regiaoDor", it) }
        put("criadoEm", criadoEm)
    }
}

private fun PlanAdjustmentEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("planId", planId)
        put("tipo", tipo)
        put("inicio", inicio)
        put("fim", fim)
        put("nivel", nivel)
        put("retorno", retorno)
        put("criadoEm", criadoEm)
    }
}

private fun AutoAdjustmentEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("planId", planId)
        put("workoutOrigemId", workoutOrigemId)
        put("regra", regra)
        put("antesJson", antesJson)
        put("depoisJson", depoisJson)
        put("ativo", ativo)
        put("deslocamentoEtapa", deslocamentoEtapa)
        put("criadoEm", criadoEm)
    }
}

private fun ActiveRunEntity.toJson(): JSONObject {
    return JSONObject().apply {
        put("workoutId", workoutId)
        put("startTimeMillis", startTimeMillis)
        put("elapsedRealtimeBase", elapsedRealtimeBase)
        put("pausedElapsedMillis", pausedElapsedMillis)
        put("isPaused", isPaused)
        put("totalDistanceMeters", totalDistanceMeters)
        put("lastUpdatedMillis", lastUpdatedMillis)
    }
}