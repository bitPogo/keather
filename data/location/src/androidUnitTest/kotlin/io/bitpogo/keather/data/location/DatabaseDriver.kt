/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriver {
    private var driver: SqlDriver? = null
    actual val dataBase: KeatherDB
        get() = KeatherDB(driver!!)

    actual suspend fun open() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        driver = AndroidSqliteDriver(
            schema = KeatherDB.Schema.synchronous(),
            context = app,
            name = testDatabase,
            callback = object : AndroidSqliteDriver.Callback(KeatherDB.Schema.synchronous()) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            },
        )
    }

    actual fun close() {
        driver?.close()
        driver = null

        val app = ApplicationProvider.getApplicationContext<Application>()
        app.deleteDatabase(testDatabase)
    }
}
