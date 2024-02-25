/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.repository

import io.bitpogo.keather.data.weather.model.local.SaveableForecastDay
import io.bitpogo.keather.data.weather.model.remote.Forecast
import io.bitpogo.keather.data.weather.model.remote.History
import io.bitpogo.keather.data.weather.model.remote.RequestLocation
import io.bitpogo.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Remote {
        suspend fun fetchForecast(location: RequestLocation, until: Long): Forecast
        suspend fun fetchHistory(location: RequestLocation, until: Long): History
    }

    interface Local {
        suspend fun fetchLastUpdate(): Long
        suspend fun setLastUpdate(timestamp: Long)
        suspend fun fetchForecast(): List<SaveableForecastDay>
        suspend fun setForecast(days: List<SaveableForecastDay>)
        suspend fun fetchHistory(): List<SaveableForecastDay>
        suspend fun setHistory(days: List<SaveableForecastDay>)
    }
}
