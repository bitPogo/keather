/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.di

import io.bitpogo.keather.presentation.ui.store.StoreContract
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication

class KoinSpec {
    @Test
    fun `It contains the WeatherStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveApp(),
            )
        }

        // When
        val store: StoreContract.WeatherStore? = koin.koin.getOrNull()

        // Then
        assertNotNull(store)
    }
}
