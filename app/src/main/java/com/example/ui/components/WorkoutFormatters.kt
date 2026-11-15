package com.example.ui.components

import java.util.Locale

object WorkoutFormatters {
    fun formatDistance(meters: Int): String = String.format(Locale.forLanguageTag("pt-BR"), "%.2f km", meters / 1000.0)

    fun formatTime(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) String.format("%d:%02d:%02d", h, m, s) else String.format("%02d:%02d", m, s)
    }

    fun formatPace(meters: Int, seconds: Int): String {
        if (meters < 100 || seconds < 60) return "— /km"
        val paceSecPerKm = seconds / (meters / 1000.0)
        if (paceSecPerKm < 120 || paceSecPerKm > 1500) return "— /km"
        val min = (paceSecPerKm / 60).toInt()
        val sec = (paceSecPerKm % 60).toInt()
        return String.format("%d:%02d /km", min, sec)
    }
}
