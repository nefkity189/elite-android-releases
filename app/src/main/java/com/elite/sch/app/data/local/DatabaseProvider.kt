package com.elite.sch.app.data.local

import android.content.Context

/**
 * Fábrica de bancos de dados por modalidade.
 * Cada modalidade (livre, plano_2_5k, plano_5k, plano_10k) possui seu próprio banco de dados.
 * Instâncias são cacheadas por modalidade.
 */
object DatabaseProvider {

    fun getDatabase(modality: String, context: Context): BaseAppDatabase {
        return when (modality) {
            MODALIDADE_LIVRE -> FreeRunDatabase.getInstance(context)
            MODALIDADE_2_5K -> Plan2_5kDatabase.getInstance(context)
            MODALIDADE_5K -> AppDatabase.getInstance(context)
            MODALIDADE_10K -> Plan10kDatabase.getInstance(context)
            else -> throw IllegalArgumentException("Modalidade desconhecida: $modality")
        }
    }

    const val MODALIDADE_LIVRE = "livre"
    const val MODALIDADE_2_5K = "plano_2_5k"
    const val MODALIDADE_5K = "plano_5k"
    const val MODALIDADE_10K = "plano_10k"

    fun getDatabaseFileName(modality: String): String {
        return when (modality) {
            MODALIDADE_LIVRE -> "primeiro_5k_livre.db"
            MODALIDADE_2_5K -> "primeiro_5k_2_5k.db"
            MODALIDADE_5K -> "primeiro_5k.db"
            MODALIDADE_10K -> "primeiro_5k_10k.db"
            else -> throw IllegalArgumentException("Modalidade desconhecida: $modality")
        }
    }
}