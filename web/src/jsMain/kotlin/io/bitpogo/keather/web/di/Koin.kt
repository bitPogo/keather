/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web.di

import io.bitpogo.keather.data.location.di.resolveLocationRepository
import io.bitpogo.keather.data.position.di.resolvePositionRepository
import io.bitpogo.keather.data.weather.di.resolveWeatherRepository
import io.bitpogo.keather.database.DatabaseFactory
import io.bitpogo.keather.database.di.resolveDatabase
import io.bitpogo.keather.interactor.di.resolveWeatherInteractor
import io.bitpogo.keather.store.di.resolveWeatherStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

suspend fun resolveApp(): KoinApplication {
    val database = DatabaseFactory.getInstance()

    val modules = listOf(
        resolveWeatherStore(),
        resolveWeatherInteractor(),
        resolveDatabase(database),
        resolveWeatherRepository(),
        resolveLocationRepository(),
        resolvePositionRepository(),
        module {
            single<CoroutineDispatcher>(named("io")) { Dispatchers.Main }
        },
    )

    return koinApplication {
        modules(modules)
    }
}
