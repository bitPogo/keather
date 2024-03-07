/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.location.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

actual class DatabaseDriver {
    private var driver: SqlDriver? = null
    actual val dataBase: KeatherDB
        get() = KeatherDB(driver!!)
    private var invocations = 0

    actual suspend fun open() {
        driver = NativeSqliteDriver(
            DatabaseConfiguration(
                name = "$testDatabase$invocations.db",
                version = KeatherDB.Schema.version.toInt(),
                inMemory = true,
                create = { connection ->
                    wrapConnection(connection) { KeatherDB.Schema.synchronous().create(it) }
                },
                upgrade = { connection, oldVersion, newVersion ->
                    wrapConnection(connection) {
                        KeatherDB.Schema.synchronous().migrate(
                            it,
                            oldVersion.toLong(),
                            newVersion.toLong(),
                        )
                    }
                },
                // extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true),
            ),
        )
        invocations++
    }

    actual fun close() {
        driver?.close()
        driver = null
    }
}
