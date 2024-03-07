/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlDriverMock
import tech.antibytes.keather.data.location.LocationQueries
import tech.antibytes.keather.data.weather.WeatherRepositoryContract
import tech.antibytes.keather.data.weather.database.WeatherQueries
import tech.antibytes.keather.data.weather.kmock
import tech.antibytes.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    SqlDriver::class,
)
class KoinSpec {
    @Test
    @JsName("fn0")
    fun `It contains an WeatherRepositoryStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                    single { WeatherQueries(kmock<SqlDriverMock>()) }
                },
            )
        }

        // When
        val store: WeatherRepositoryContract.Store? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn1")
    fun `It contains an ClientProvider`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
            )
        }

        // When
        val store: WeatherRepositoryContract.ClientProvider? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn2")
    fun `It contains a WeatherRepositoryContractApi`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveWeatherRepository(),
            )
        }

        // When
        val store: WeatherRepositoryContract.Api? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn3")
    fun `It contains an LocationRepositoryContract`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveWeatherRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                    single { WeatherQueries(kmock<SqlDriverMock>()) }
                    single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
                },
            )
        }

        // When
        val store: RepositoryContract.WeatherRepository? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
