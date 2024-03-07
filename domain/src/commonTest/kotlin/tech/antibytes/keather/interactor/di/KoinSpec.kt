/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.interactor.di

import tech.antibytes.keather.interactor.kmock
import tech.antibytes.keather.interactor.repository.LocationRepositoryMock
import tech.antibytes.keather.interactor.repository.PositionRepositoryMock
import tech.antibytes.keather.interactor.repository.RepositoryContract
import tech.antibytes.keather.interactor.repository.WeatherRepositoryMock
import tech.antibytes.keather.presentation.interactor.InteractorContract
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
