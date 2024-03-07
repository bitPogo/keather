/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

internal object DatabaseFactory {
    fun getInstance(context: Context): KeatherDB {
        val driver = AndroidSqliteDriver(
            schema = KeatherDB.Schema.synchronous(),
            context = context,
            name = DATABASE_NAME,
        )

        return KeatherDB.invoke(driver)
    }
}
