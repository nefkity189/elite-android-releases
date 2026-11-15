package com.example

import com.example.data.local.WorkoutStepConverter
import com.example.model.StepType
import com.example.model.WorkoutStep
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class WorkoutStepConverterTest {

    @Test
    fun `converter roundtrip com distanceMeters`() {
        val steps = listOf(
            WorkoutStep(StepType.WARMUP, 300, "Aquecimento", 0),
            WorkoutStep(StepType.RUN, 180, "Corra 400m", 400),
            WorkoutStep(StepType.WALK, 120, "Caminhe 200m", 200),
            WorkoutStep(StepType.COOLDOWN, 300, "Desaquecimento", 0)
        )

        val json = WorkoutStepConverter.toJson(steps)
        val parsed = WorkoutStepConverter.fromJson(json)

        assertEquals(steps.size, parsed.size)
        for (i in steps.indices) {
            assertEquals(steps[i].type, parsed[i].type)
            assertEquals(steps[i].durationSeconds, parsed[i].durationSeconds)
            assertEquals(steps[i].instruction, parsed[i].instruction)
            assertEquals(steps[i].distanceMeters, parsed[i].distanceMeters)
        }
    }

    @Test
    fun `converter json antigo sem distanceMeters`() {
        // JSON antigo sem o campo distanceMeters
        val jsonOld = """
            [
                {"type":"WARMUP","duration":300,"instruction":"Aquecimento"},
                {"type":"RUN","duration":180,"instruction":"Corra"},
                {"type":"WALK","duration":120,"instruction":"Caminhe"},
                {"type":"COOLDOWN","duration":300,"instruction":"Desaquecimento"}
            ]
        """.trimIndent()

        val parsed = WorkoutStepConverter.fromJson(jsonOld)

        assertEquals(4, parsed.size)
        assertEquals(StepType.WARMUP, parsed[0].type)
        assertNull(parsed[0].distanceMeters)
        assertEquals(StepType.RUN, parsed[1].type)
        assertNull(parsed[1].distanceMeters)
        assertEquals(StepType.WALK, parsed[2].type)
        assertNull(parsed[2].distanceMeters)
        assertEquals(StepType.COOLDOWN, parsed[3].type)
        assertNull(parsed[3].distanceMeters)
    }

    @Test
    fun `converter json invalido retorna lista vazia`() {
        val parsed = WorkoutStepConverter.fromJson("json invalido")
        assertEquals(0, parsed.size)

        val parsed2 = WorkoutStepConverter.fromJson("")
        assertEquals(0, parsed2.size)
    }
}