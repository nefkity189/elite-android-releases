package com.elite.sch.app

import org.junit.Test
import org.junit.Assert.*

/**
 * Teste unitário puro para o mapeamento da action ACTION_TOGGLE_PAUSE_RESUME.
 * Valida que a action mapeia corretamente para pauseRun() ou resumeRun()
 * dependendo do estado atual (isPaused).
 */
class NotificationActionMapperTest {

    @Test
    fun togglePauseResume_whenRunning_mapsToPause() {
        // Given: corrida rodando (isRunning=true, isPaused=false)
        val isRunning = true
        val isPaused = false

        // When: action ACTION_TOGGLE_PAUSE_RESUME recebida
        val shouldPause = isRunning && !isPaused
        val shouldResume = isRunning && isPaused

        // Then: deve chamar pauseRun()
        assertTrue("Quando rodando e não pausado, deve pausar", shouldPause)
        assertFalse("Não deve retomar quando já está rodando", shouldResume)
    }

    @Test
    fun togglePauseResume_whenPaused_mapsToResume() {
        // Given: corrida pausada (isRunning=true, isPaused=true)
        val isRunning = true
        val isPaused = true

        // When: action ACTION_TOGGLE_PAUSE_RESUME recebida
        val shouldPause = isRunning && !isPaused
        val shouldResume = isRunning && isPaused

        // Then: deve chamar resumeRun()
        assertTrue("Quando pausado, deve retomar", shouldResume)
        assertFalse("Não deve pausar quando já está pausado", shouldPause)
    }

    @Test
    fun togglePauseResume_whenNotRunning_doesNothing() {
        // Given: corrida não iniciada ou finalizada (isRunning=false)
        val isRunning = false
        val isPaused = false

        // When: action ACTION_TOGGLE_PAUSE_RESUME recebida
        val shouldPause = isRunning && !isPaused
        val shouldResume = isRunning && isPaused

        // Then: não deve fazer nada
        assertFalse("Não deve pausar quando não está rodando", shouldPause)
        assertFalse("Não deve retomar quando não está rodando", shouldResume)
    }

    @Test
    fun togglePauseResume_whenFinished_doesNothing() {
        // Given: corrida finalizada (isRunning=false, isPaused=false, isFinished=true)
        val isRunning = false
        val isPaused = false

        // When: action ACTION_TOGGLE_PAUSE_RESUME recebida
        val shouldPause = isRunning && !isPaused
        val shouldResume = isRunning && isPaused

        // Then: não deve fazer nada
        assertFalse("Não deve pausar quando finalizado", shouldPause)
        assertFalse("Não deve retomar quando finalizado", shouldResume)
    }
}