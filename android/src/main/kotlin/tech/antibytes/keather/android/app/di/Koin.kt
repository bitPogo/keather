/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.di

import tech.antibytes.keather.data.location.di.resolveLocationRepository
import tech.antibytes.keather.data.position.di.resolvePositionRepository
import tech.antibytes.keather.data.weather.di.resolveWeatherRepository
import tech.antibytes.keather.database.di.resolveDatabase
import tech.antibytes.keather.interactor.di.resolveWeatherInteractor
import tech.antibytes.keather.store.di.resolveWeatherStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun resolveApp(): List<Module> = listOf(
    resolveWeatherStore(),
    resolveWeatherInteractor(),
    resolveDatabase(),
    resolveWeatherRepository(),
    resolveLocationRepository(),
    resolvePositionRepository(),
    module {
        single<CoroutineDispatcher>(named("io")) { Dispatchers.IO }
    },
)
