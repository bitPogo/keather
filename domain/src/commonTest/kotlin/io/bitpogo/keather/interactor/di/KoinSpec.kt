/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.interactor.di

import io.bitpogo.keather.interactor.kmock
import io.bitpogo.keather.interactor.repository.LocationRepositoryMock
import io.bitpogo.keather.interactor.repository.PositionRepositoryMock
import io.bitpogo.keather.interactor.repository.RepositoryContract
import io.bitpogo.keather.interactor.repository.WeatherRepositoryMock
import io.bitpogo.keather.presentation.interactor.InteractorContract
import kotlin.js.JsName
import kotlin.test.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    RepositoryContract.WeatherRepository::class,
    RepositoryContract.PositionRepository::class,
    RepositoryContract.LocationRepository::class,
)
class KoinSpec {
    @Test
    @JsName("fn0")
    fun `It contains an WeatherInteractor`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherInteractor(),
                module {
                    single<RepositoryContract.WeatherRepository> { kmock<WeatherRepositoryMock>() }
                    single<RepositoryContract.PositionRepository> { kmock<PositionRepositoryMock>() }
                    single<RepositoryContract.LocationRepository> { kmock<LocationRepositoryMock>() }
                },
            )
        }

        // When
        val store: InteractorContract.WeatherDataInteractor? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
