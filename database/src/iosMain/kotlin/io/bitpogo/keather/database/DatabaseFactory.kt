package io.bitpogo.keather.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

internal object DatabaseFactory {
    fun getInstance(): KeatherDB {
        val driver = NativeSqliteDriver(
            DatabaseConfiguration(
                name = DATABASE_NAME,
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
                extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true),
            ),
        )

        return KeatherDB.invoke(driver)
    }

    private const val DATABASE_NAME = "KeatherDB.db"
}
