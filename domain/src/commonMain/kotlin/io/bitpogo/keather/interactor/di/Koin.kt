/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.interactor.di

import io.bitpogo.keather.interactor.WeatherDataInteractor
import io.bitpogo.keather.presentation.interactor.InteractorContract
import org.koin.core.module.Module
import org.koin.dsl.module

fun resolveWeatherInteractor(): Module = module {
    single<InteractorContract.WeatherDataInteractor> {
        WeatherDataInteractor(get(), get(), get())
    }
}
