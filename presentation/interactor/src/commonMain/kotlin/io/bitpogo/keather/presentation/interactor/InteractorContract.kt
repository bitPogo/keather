/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.presentation.interactor

import io.bitpogo.keather.entity.LocalizedWeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface InteractorContract {
    interface WeatherDataInteractor {
        val weatherData: SharedFlow<Result<LocalizedWeatherData>>

        fun refreshAll(scope: CoroutineScope)
        fun refresh(scope: CoroutineScope)
    }
}
