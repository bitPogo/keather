/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.repository

import io.bitpogo.keather.data.weather.model.Forecast

internal interface WeatherRepositoryContract {
    interface Remote {
        suspend fun fetchForecast(): Forecast
        suspend fun fetchHistory(): Forecast
    }
}
