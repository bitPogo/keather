/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database.di

import tech.antibytes.keather.data.location.LocationQueries
import tech.antibytes.keather.data.position.PositionQueries
import tech.antibytes.keather.data.weather.database.WeatherQueries
import tech.antibytes.keather.database.KeatherDB
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.dsl.koinApplication

class KoinTest {
    @Test
    fun `It contains a KeatherDB`() {
        val koin = koinApplication {
            modules(resolveDatabase())
        }
        val db: KeatherDB? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a PositionQueries`() {
        // Given
        val koin = koinApplication {
            modules(resolveDatabase())
        }
        val db: PositionQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a LocationQueries`() {
        // Given
        val koin = koinApplication {
            modules(resolveDatabase())
        }
        val db: LocationQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a WeatherQueries`() {
        // Given
        val koin = koinApplication {
            modules(resolveDatabase())
        }
        val db: WeatherQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }
}
