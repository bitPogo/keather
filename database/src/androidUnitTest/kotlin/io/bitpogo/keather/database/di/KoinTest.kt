/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.database.di

import android.content.Context
import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.position.PositionQueries
import io.bitpogo.keather.data.weather.database.WeatherQueries
import io.bitpogo.keather.database.KeatherDB
import io.mockk.mockk
import kotlin.test.assertNotNull
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication

class KoinTest {
    @Test
    fun `It contains a KeatherDB`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koin = koinApplication {
            androidContext(context)
            modules(resolveDatabase())
        }
        val db: KeatherDB? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a PositionQueries`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koin = koinApplication {
            androidContext(context)
            modules(resolveDatabase())
        }
        val db: PositionQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a LocationQueries`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koin = koinApplication {
            androidContext(context)
            modules(resolveDatabase())
        }
        val db: LocationQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }

    @Test
    fun `It contains a WeatherQueries`() {
        // Given
        val context: Context = mockk(relaxed = true)

        val koin = koinApplication {
            androidContext(context)
            modules(resolveDatabase())
        }
        val db: WeatherQueries? = koin.koin.getOrNull()

        // Then
        assertNotNull(db)
    }
}
