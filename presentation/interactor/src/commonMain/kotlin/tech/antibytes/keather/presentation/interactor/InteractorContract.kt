/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.presentation.interactor

import tech.antibytes.keather.entity.LocalizedWeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

interface InteractorContract {
    interface WeatherDataInteractor {
        val weatherData: SharedFlow<Result<LocalizedWeatherData>>

        fun refreshAll(scope: CoroutineScope)
        fun refresh(scope: CoroutineScope)
    }
}
