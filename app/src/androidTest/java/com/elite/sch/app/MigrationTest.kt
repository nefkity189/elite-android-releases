package com.elite.sch.app

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.elite.sch.app.data.local.AppDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    var helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun migration_8_to_9() {
        helper.createDatabase("primeiro_5k.db", 8).apply {
            close()
        }

        val db8 = helper.runMigrationsAndValidate(
            "primeiro_5k.db",
            9,
            true
        )

        val cursor1 = db8.query("PRAGMA table_info(plan)")
        var hasModalidade = false
        var hasDistanciaAlvoM = false
        while (cursor1.moveToNext()) {
            val name = cursor1.getString(cursor1.getColumnIndexOrThrow("name"))
            if (name == "modalidade") hasModalidade = true
            if (name == "distanciaAlvoM") hasDistanciaAlvoM = true
        }
        cursor1.close()

        assert(hasModalidade) { "plan table should have modalidade column" }
        assert(hasDistanciaAlvoM) { "plan table should have distanciaAlvoM column" }

        db8.close()
    }

    @Test
    fun migration_10_to_11_app() {
        helper.createDatabase("primeiro_5k.db", 10).apply { close() }

        val migrated = helper.runMigrationsAndValidate(
            "primeiro_5k.db",
            11,
            true
        )

        assertRunnerProfileMapColumns(migrated)
        migrated.close()
    }

    @Test
    fun migration_10_to_11_plan2_5k() {
        helper.createDatabase("primeiro_5k_2_5k.db", 10).apply { close() }

        val migrated = helper.runMigrationsAndValidate(
            "primeiro_5k_2_5k.db",
            11,
            true
        )

        assertRunnerProfileMapColumns(migrated)
        migrated.close()
    }

    @Test
    fun migration_10_to_11_plan5k() {
        helper.createDatabase("primeiro_5k.db", 10).apply { close() }

        val migrated = helper.runMigrationsAndValidate(
            "primeiro_5k.db",
            11,
            true
        )

        assertRunnerProfileMapColumns(migrated)
        migrated.close()
    }

    @Test
    fun migration_10_to_11_plan10k() {
        helper.createDatabase("primeiro_5k_10k.db", 10).apply { close() }

        val migrated = helper.runMigrationsAndValidate(
            "primeiro_5k_10k.db",
            11,
            true
        )

        assertRunnerProfileMapColumns(migrated)
        migrated.close()
    }

    @Test
    fun migration_10_to_11_free_run() {
        helper.createDatabase("primeiro_5k_livre.db", 10).apply { close() }

        val migrated = helper.runMigrationsAndValidate(
            "primeiro_5k_livre.db",
            11,
            true
        )

        assertRunnerProfileMapColumns(migrated)
        migrated.close()
    }

    private fun assertRunnerProfileMapColumns(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        val cursor = db.query("PRAGMA table_info(runner_profile)")
        val names = mutableSetOf<String>()
        val defaults = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val defaultValue = cursor.getString(cursor.getColumnIndexOrThrow("dflt_value"))
            names.add(name)
            if (defaultValue != null) defaults[name] = defaultValue
        }
        cursor.close()

        assert("showMapDuringRun" in names) {
            "runner_profile should have showMapDuringRun column"
        }
        assert("defaultCameraMode" in names) {
            "runner_profile should have defaultCameraMode column"
        }
        assert(defaults["showMapDuringRun"] == "1") {
            "showMapDuringRun default should be 1 (true), got ${defaults["showMapDuringRun"]}"
        }
        assert(defaults["defaultCameraMode"] == "'follow_user'") {
            "defaultCameraMode default should be 'follow_user', got ${defaults["defaultCameraMode"]}"
        }
    }
}