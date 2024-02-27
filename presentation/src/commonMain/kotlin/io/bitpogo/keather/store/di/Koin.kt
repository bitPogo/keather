/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.store.di

import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.store.WeatherStore
import org.koin.core.module.Module
import org.koin.dsl.module

fun resolveWeatherStore(): Module = module {
    single<StoreContract.WeatherStore> {
        WeatherStore(get())
    }
}
