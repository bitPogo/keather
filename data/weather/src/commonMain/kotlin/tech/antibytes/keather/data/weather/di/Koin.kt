/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.di

import tech.antibytes.keather.data.weather.WeatherRepository
import tech.antibytes.keather.data.weather.WeatherRepositoryContract
import tech.antibytes.keather.data.weather.api.ClientProvider
import tech.antibytes.keather.data.weather.api.WeatherApi
import tech.antibytes.keather.data.weather.database.WeatherStore
import tech.antibytes.keather.interactor.repository.RepositoryContract
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
            clientProvider = get(),
        )
    }
    single<RepositoryContract.WeatherRepository> {
        WeatherRepository(get(named("io")), get(), get())
    }
}
