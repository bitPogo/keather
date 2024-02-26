/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.repository

import io.bitpogo.keather.data.weather.model.local.SaveableForecast
import io.bitpogo.keather.data.weather.model.local.SaveableLocation
import io.bitpogo.keather.data.weather.model.remote.Forecast
import io.bitpogo.keather.data.weather.model.remote.History
import io.bitpogo.keather.data.weather.model.remote.RequestPosition
import io.bitpogo.keather.http.networking.NetworkingContract

internal interface WeatherRepositoryContract {
    fun interface ClientProvider {
        fun provide(): NetworkingContract.RequestBuilder
    }

    interface Remote {
        suspend fun fetchForecast(location: RequestPosition, until: Long): Forecast
        suspend fun fetchHistory(location: RequestPosition, until: Long): History
    }

    interface Local {
        suspend fun setLocation(location: SaveableLocation)
        suspend fun fetchForecast(): List<SaveableForecast>
        suspend fun setForecast(days: List<SaveableForecast>)
        suspend fun fetchHistory(): List<SaveableForecast>
        suspend fun setHistory(days: List<SaveableForecast>)
    }
}
