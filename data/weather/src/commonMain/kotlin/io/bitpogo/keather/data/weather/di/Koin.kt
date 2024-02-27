/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.di

import io.bitpogo.keather.data.weather.WeatherRepository
import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.api.ClientProvider
import io.bitpogo.keather.data.weather.api.WeatherApi
import io.bitpogo.keather.data.weather.database.WeatherStore
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlinx.datetime.Clock
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun resolveWeatherRepository(): Module = module {
    single<WeatherRepositoryContract.Store> {
        WeatherStore(get(), get())
    }
    single<WeatherRepositoryContract.ClientProvider> { ClientProvider() }
    single<WeatherRepositoryContract.Api> {
        WeatherApi(
            clock = Clock.System,
            requestBuilder = get<WeatherRepositoryContract.ClientProvider>().provide(),
        )
    }
    single<RepositoryContract.WeatherRepository> {
        WeatherRepository(get(named("io")), get(), get())
    }
}
