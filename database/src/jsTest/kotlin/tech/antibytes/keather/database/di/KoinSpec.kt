/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database.di

import tech.antibytes.keather.data.location.LocationQueries
import tech.antibytes.keather.data.position.PositionQueries
import tech.antibytes.keather.data.weather.database.WeatherQueries
import tech.antibytes.keather.database.DatabaseFactory
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication

class KoinSpec {
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
