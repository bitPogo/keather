/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.Worker

object DatabaseFactory {
    private val mutex = Mutex()

    suspend fun getInstance(): KeatherDB {
        val driver = mutex.withLock {
            WebWorkerDriver(
                Worker(
                    js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""),
                ),
            ).apply {
                KeatherDB.Schema.awaitCreate(this)
            }
        }

        return KeatherDB.invoke(driver)
    }
}
