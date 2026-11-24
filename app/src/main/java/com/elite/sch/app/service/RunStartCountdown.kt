package com.elite.sch.app.service

/** Values shown to the runner before a regular run begins; zero is the "JÁ!" cue. */
object RunStartCountdown {
    fun ticks(): List<Int> = listOf(3, 2, 1, 0)
}
