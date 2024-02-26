/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import io.bitpogo.keather.data.weather.KeatherDB
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.Worker

actual class DatabaseDriver {
    private val mutex = Mutex()
    private var driver: SqlDriver? = null
    actual val dataBase: KeatherDB
        get() = KeatherDB(driver!!)

    actual suspend fun open() {
        mutex.withLock {
            driver = WebWorkerDriver(
                Worker(
                    js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""),
                ),
            )
            KeatherDB.Schema.awaitCreate(driver as WebWorkerDriver)
        }
    }

    actual fun close() {
        driver?.close()
        driver = null
    }
}
