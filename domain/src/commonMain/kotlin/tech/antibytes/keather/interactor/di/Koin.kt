/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.interactor.di

import tech.antibytes.keather.interactor.WeatherDataInteractor
import tech.antibytes.keather.presentation.interactor.InteractorContract
import org.koin.core.module.Module
import org.koin.dsl.module

fun resolveWeatherInteractor(): Module = module {
    single<InteractorContract.WeatherDataInteractor> {
        WeatherDataInteractor(get(), get(), get())
    }
}
