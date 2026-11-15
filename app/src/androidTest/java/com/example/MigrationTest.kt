package com.example

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.data.local.AppDatabase
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
        val db = helper.createDatabase("primeiro_5k.db", 8).apply {
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
}
