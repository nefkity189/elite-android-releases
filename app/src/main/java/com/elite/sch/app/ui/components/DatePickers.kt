@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.elite.sch.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/** O DatePicker do Material 3 trabalha em milissegundos UTC à meia-noite. */
internal fun LocalDate.toPickerMillis(): Long =
    this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

internal fun Long.toPickerLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

private fun selectableBetween(min: LocalDate, max: LocalDate) = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val d = utcTimeMillis.toPickerLocalDate()
        return !d.isBefore(min) && !d.isAfter(max)
    }

    override fun isSelectableYear(year: Int): Boolean = year in min.year..max.year
}

private fun LocalDate.clampTo(min: LocalDate, max: LocalDate): LocalDate =
    when {
        isBefore(min) -> min
        isAfter(max) -> max
        else -> this
    }

private val PtBrFmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM")

/**
 * Seletor de período (pausa e férias).
 * IMPORTANTE: o seletor é o conteúdo direto do DatePickerDialog. Não colocar dentro de
 * Column(verticalScroll), porque ele já rola por dentro e o Compose derruba o app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliteDateRangePickerDialog(
    initialStart: LocalDate,
    initialEnd: LocalDate,
    minDate: LocalDate,
    maxDate: LocalDate,
    minDays: Int,
    maxDays: Int,
    endNotBefore: LocalDate? = null,
    onConfirm: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    // Valores iniciais sempre dentro das datas selecionáveis
    val safeStart = initialStart.clampTo(minDate, maxDate)
    val safeEnd = initialEnd.clampTo(safeStart, maxDate)
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = safeStart.toPickerMillis(),
        initialSelectedEndDateMillis = safeEnd.toPickerMillis(),
        initialDisplayedMonthMillis = safeStart.toPickerMillis(),
        selectableDates = selectableBetween(minDate, maxDate)
    )

    val startMs = state.selectedStartDateMillis
    val endMs = state.selectedEndDateMillis
    val start = startMs?.toPickerLocalDate()
    val end = endMs?.toPickerLocalDate()
    val days = if (start != null && end != null) ChronoUnit.DAYS.between(start, end) + 1 else 0L

    val error: String? = when {
        start == null || end == null -> "Escolha a data inicial e a final."
        days < minDays || days > maxDays -> "O período deve ter entre $minDays e $maxDays dias."
        endNotBefore != null && end.isBefore(endNotBefore) -> "O fim não pode ser anterior a hoje."
        else -> null
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { if (start != null && end != null && error == null) onConfirm(start, end) },
                enabled = error == null,
                modifier = Modifier.testTag("btn_datepicker_confirmar")
            ) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("btn_datepicker_cancelar")) {
                Text("Cancelar")
            }
        }
    ) {
        DateRangePicker(
            state = state,
            modifier = Modifier.height(480.dp),
            title = null,
            headline = {
                Box(modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 8.dp)) {
                    if (error != null) {
                        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text(
                            "${start!!.format(PtBrFmt)} a ${end!!.format(PtBrFmt)} • $days ${if (days == 1L) "dia" else "dias"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors()
        )
    }
}

/** Seletor de uma data (alterar data final). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliteDatePickerDialog(
    initial: LocalDate,
    minDate: LocalDate,
    maxDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val lo = if (minDate.isAfter(maxDate)) maxDate else minDate
    val safe = initial.clampTo(lo, maxDate)
    val state = rememberDatePickerState(
        initialSelectedDateMillis = safe.toPickerMillis(),
        initialDisplayedMonthMillis = safe.toPickerMillis(),
        selectableDates = selectableBetween(lo, maxDate)
    )
    val picked = state.selectedDateMillis?.toPickerLocalDate()
    val error = if (picked == null) "Escolha uma data." else null

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { if (picked != null) onConfirm(picked) },
                enabled = error == null,
                modifier = Modifier.testTag("btn_datepicker_confirmar")
            ) { Text("Confirmar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    ) {
        DatePicker(
            state = state,
            title = null,
            headline = {
                Box(modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 8.dp)) {
                    Text(
                        error ?: "Último treino até ${picked!!.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                        color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            showModeToggle = false
        )
    }
}
