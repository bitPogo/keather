/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.database.di

import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.position.PositionQueries
import io.bitpogo.keather.data.weather.database.WeatherQueries
import io.bitpogo.keather.database.DatabaseFactory
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication

@Ignore
class KoinTest {
    @Test
    @JsName("fn0")
    fun `It contains a PositionQueries`() = runTest {
        // Given
        val db = DatabaseFactory.getInstance()

        val koin = koinApplication {
            modules(resolveDatabase(db))
        }
        val queries: PositionQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(queries)
    }

    @Test
    @JsName("fn1")
    fun `It contains a LocationQueries`() = runTest {
        // Given
        val db = DatabaseFactory.getInstance()

        val koin = koinApplication {
            modules(resolveDatabase(db))
        }
        val queries: LocationQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(queries)
    }

    @Test
    @JsName("fn2")
    fun `It contains a WeatherQueries`() = runTest {
        // Given
        val db = DatabaseFactory.getInstance()

        val koin = koinApplication {
            modules(resolveDatabase(db))
        }
        val queries: WeatherQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(queries)
    }
}
