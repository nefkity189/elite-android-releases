package com.elite.sch.app.data.local

import com.elite.sch.app.model.StepType
import com.elite.sch.app.model.WorkoutStep
import org.json.JSONArray
import org.json.JSONObject

object WorkoutStepConverter {

    fun toJson(steps: List<WorkoutStep>): String {
        val array = JSONArray()
        for (step in steps) {
            val obj = JSONObject()
            obj.put("type", step.type.name)
            obj.put("duration", step.durationSeconds)
            obj.put("instruction", step.instruction)
            step.distanceMeters?.let {
                obj.put("distanceMeters", it)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun fromJson(json: String): List<WorkoutStep> {
        val list = mutableListOf<WorkoutStep>()
        if (json.isBlank()) return list
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val typeStr = obj.optString("type", StepType.RUN.name)
                val type = try {
                    StepType.valueOf(typeStr)
                } catch (_: Exception) {
                    StepType.RUN
                }
                val duration = obj.optInt("duration", 60)
                val instruction = obj.optString("instruction", "")
                val distanceMeters = if (obj.has("distanceMeters")) obj.optInt("distanceMeters") else -1
                list.add(WorkoutStep(
                    type = type,
                    durationSeconds = duration,
                    instruction = instruction,
                    distanceMeters = if (distanceMeters >= 0) distanceMeters else null
                ))
            }
        } catch (_: Exception) {
            // Em caso de falha no parser, retorna lista vazia segura
        }
        return list
    }
}