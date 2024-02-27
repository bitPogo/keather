package io.bitpogo.keather.database

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
