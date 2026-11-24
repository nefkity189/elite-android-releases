package com.elite.sch.app

import android.content.Context
import android.media.MediaPlayer

/** Plays the mission callout stored in the app's raw resources. */
class VoiceCommander(context: Context) {

    private val appContext = context.applicationContext
    private var player: MediaPlayer? = null

    fun playMission() {
        release()

        val newPlayer = MediaPlayer.create(appContext, R.raw.missao) ?: return
        player = newPlayer
        newPlayer.setOnCompletionListener(::releasePlayer)
        newPlayer.setOnErrorListener { failedPlayer, _, _ ->
            releasePlayer(failedPlayer)
            true
        }

        try {
            newPlayer.start()
        } catch (_: IllegalStateException) {
            releasePlayer(newPlayer)
        }
    }

    /** Releases the player if playback is interrupted, such as when the Activity is destroyed. */
    fun release() {
        val currentPlayer = player ?: return
        player = null
        currentPlayer.setOnCompletionListener(null)
        currentPlayer.setOnErrorListener(null)
        currentPlayer.release()
    }

    private fun releasePlayer(finishedPlayer: MediaPlayer) {
        if (player === finishedPlayer) player = null
        finishedPlayer.release()
    }
}
