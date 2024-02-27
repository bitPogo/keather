/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.store.di

import io.bitpogo.keather.presentation.interactor.InteractorContract
import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.store.WeatherInteractorFake
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.test.isNot

class KoinSpec {
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @JsName("fn0")
    fun `It contains an WeatherStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherStore(),
                module {
                    single<InteractorContract.WeatherDataInteractor> { WeatherInteractorFake() }
                },
            )
        }

        // When
        val store: StoreContract.WeatherStore? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
